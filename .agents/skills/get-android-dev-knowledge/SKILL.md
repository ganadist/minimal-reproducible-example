---
name: get-android-dev-knowledge
description: Access official Android developer documentation. This skill provides up-to-date information (more current than pre-trained knowledge) and uses fewer tokens than the web search tool. Use this skill when working with Android platform APIs, AndroidX libraries, Google APIs (including Google Play, Firebase, Google Cloud, Google Ads, Google GenAI, and ML Kit), Kotlin integrations, Material Design, or Android build configuration. It is best for API references, code samples, best practices, and troubleshooting Google technologies.
user-invocable: true
argument-hint: <QUERY>
allowed-tools:
  - "Bash(.agents/tools/android-cli/android docs search: *)"
  - "Bash(.agents/tools/android-cli/android docs fetch: *)"
---

# Android Developer Knowledge Skill

This skill searches authoritative, high-quality Android developer documentation in the Android Knowledge Base.
By providing a few keywords, this will return high quality articles that contain examples or guidance on how to use Android APIs or libraries.
Use this skill to obtain additional information on how to achieve Android-specific tasks or to know more about Android APIs, surfaces, libraries, or devices.

Always use this to get the most up-to-date information about Android concepts. Typical good use cases are:
  - Finding migration guides for APIs.
  - Finding examples for APIs.
  - Finding up-to-date information about Android APIs.
  - Finding best practices for Android concepts.

## Workflow

### 1. Search Documentation

Run `.agents/tools/android-cli/android docs search`:

Usage:
```
.agents/tools/android-cli/android docs search '<QUERY>'
```

Usage Example:
```
.agents/tools/android-cli/android docs search 'Android Jetpack Compose quickstart'
```

### 2. Fetch Contents from Results

Run `.agents/tools/android-cli/android docs fetch` with a specific `<KB_URL>` from the search results above.
Use one or more documents from the most relevant results as references for your current work.

Usage:
```
.agents/tools/android-cli/android docs fetch '<KB_URL>'
```

Usage Example:
```
.agents/tools/android-cli/android docs fetch 'kb://android/develop/ui/compose/documentation'
.agents/tools/android-cli/android docs fetch 'kb://android/develop/ui/compose/tutorial'
```
