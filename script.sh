#!/usr/bin/env bash
./gradlew   assembleDebug
adb install -t -r app/build/outputs/apk/debug/app-debug.apk
adb logcat -c
adb logcat > out.txt
