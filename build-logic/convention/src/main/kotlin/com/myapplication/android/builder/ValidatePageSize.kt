package com.myapplication.android.builder

import com.android.build.api.artifact.SingleArtifact
import com.android.build.gradle.internal.tasks.MergeNativeLibsTask
import java.io.File
import kotlin.text.trim
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction

/**
 * Reference : https://android.googlesource.com/platform/frameworks/support/+/refs/heads/androidx-main/buildSrc/private/src/main/kotlin/androidx/build/VerifyELFRegionAlignmentTask.kt
 */
@CacheableTask
abstract class ValidatePageSizeTask : DefaultTask() {
    private val ALLOWED_PAGE_SIZES = arrayOf(
        "2**14", // 16KB
        "2**15", // 32KB
        "2**16", // 64KB
    )

    init {
        group = "Verification"
        description = "Task for verifying alignment in shared libs"
    }

    @get:InputFiles
    @get:PathSensitive(PathSensitivity.NONE)
    abstract val objDumpPath: RegularFileProperty

    @get:[InputFiles Classpath]
    abstract val outDirs: ConfigurableFileCollection

    @TaskAction
    fun verifyELFRegionAlignment() {
        outDirs.asFileTree.files
            .filter { it.extension == "so" }
            .filter { it.path.contains("arm64-v8a") }
            .forEach {
                val alignment = getELFAlignment(objDumpPath.asFile.get().path, it.path)
                if (alignment in ALLOWED_PAGE_SIZES) {
                    logger.info("ELF alignment of ${it.name} is $alignment, which is allowed.")
                } else {
                    val message = "Expected ELF alignment of 2**14 or higher for file `${it.name}`, " +
                            "but got $alignment.\n Please see " +
                            "https://d.android.com/guide/practices/page-sizes?hl=en"
                    if (Const.NATIVE_PAGE_SIZE_NOT_16KB_OR_HIGHER.contains(it.name)) {
                        logger.warn(message)
                    } else {
                        throw GradleException(message)
                    }
                }
            }
    }
}

private fun getELFAlignment(objDumpPath: String, filePath: String): String? {
    val alignment =
        ProcessBuilder(
            objDumpPath,
            "-p",
            filePath
        ).start().inputStream.bufferedReader().useLines { lines ->
            lines.filter {
                it.contains("LOAD")
            }.map {
                it.split(" ").last()
            }.firstOrNull()
        }
    return alignment
}

private fun Project.getObjDump(ndkHome: File): File? {
    val ndkWhich = ndkHome.resolve("ndk-which")
    if (!ndkWhich.exists()) {
        return null
    }

    val objDumpPath = providers.exec {
        commandLine(ndkWhich.path, "objdump")
        isIgnoreExitValue = true
    }.standardOutput.asText.get().trim()

    if (objDumpPath.isEmpty()) {
        return null
    }
    val objDumpFile = File(objDumpPath)
    if (!objDumpFile.canExecute()) {
        return null
    }

    return objDumpFile
}

internal fun Project.configurePageSizeCheck() {
    val objDumpFile =
        try {
            val androidNdkHome = componentsExtension.sdkComponents.ndkDirectory.get().asFile
            getObjDump(androidNdkHome)
        } catch (_: Throwable) {
            null
        }

    if (objDumpFile == null) {
        logger.warn("objdump is not available, and skipping PageSizeCheck")
        return
    }

    componentsExtension.onVariants { variant ->
        val checkTaskProvider = project.tasks.register(
            "validate" + variant.name.toCamelCase() + "PageSizeAlignment",
            ValidatePageSizeTask::class.java
        ) {
            objDumpPath.set(objDumpFile)
            outDirs.from(
                variant.artifacts.get(SingleArtifact.MERGED_NATIVE_LIBS).map { dir ->
                    dir.asFile
                }
            )
            cacheEvenIfNoOutputs()
        }

        tasks.withType(MergeNativeLibsTask::class.java) {
            // taskName is in the format "merge<VariantName>NativeLibs"
            val variantName = name.removePrefix("merge").removeSuffix("NativeLibs")
            if (variantName == variant.name.toCamelCase()) {
                finalizedBy(checkTaskProvider)
            }
        }
    }
}

private fun Task.cacheEvenIfNoOutputs() {
    this.outputs.file(this.getDummyOutput())
}

private fun Task.getDummyOutput(): Provider<RegularFile> {
    return project.layout.buildDirectory.file("dummyOutput/" + this.name.replace(":", "-"))
}
