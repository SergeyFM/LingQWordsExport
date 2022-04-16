@echo off
echo -----------------------------------------------------------------------------------------------------
echo This script will copy pictures and audio files to your anki media folder.
echo Only new and updated files are to be copied.
echo Make sure the path is right:
set MEDIA_FOLDER="%APPDATA%\Anki2\User 1\collection.media"
echo Anki media folder: %MEDIA_FOLDER%
echo -----------------------------------------------------------------------------------------------------
pause
echo Copy images from pic folder... ----------------------------------------------------------------------
xcopy ..\pic %MEDIA_FOLDER% /d
echo Copy audio files from mp3 folder... -----------------------------------------------------------------
xcopy ..\mp3 %MEDIA_FOLDER% /d
pause