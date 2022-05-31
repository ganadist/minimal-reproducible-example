@echo off

set gitlfs_guideline=https://wiki.linecorp.com/x/x_wAYg#LINEAndroidgitlfsmigrationguideforde

@rem Check the existence of git-lfs
git-lfs >nul 2>nul

if errorlevel 1 (
   echo git-lfs is not installed. 
   start %gitlfs_guideline%
   echo Please see %gitlfs_guideline%
   exit /b
)

@rem Check the file pulled from lfs is normally
set words="version https://git-lfs.github.com/spec/v1"
set result=""

@rem Get the first line
for /f "tokens=*" %%a in ('findstr /B /L %words% %CLASSPATH%') do set result="%%a"

@rem Compare the headers
if %words%==%result% (
   echo Git lfs is not applied correctly. Check it again
   exit /b
)
