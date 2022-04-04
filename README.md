# Taking screenshots

1. Disable animations:
```
adb shell settings put global window_animation_scale 0
adb shell settings put global animator_duration_scale 0
adb shell settings put global transition_animation_scale 0
adb shell "su 0 am start -a android.intent.action.REBOOT"
```

2. Disable keyboard:
(Depending on system version, not all these commands will succeed).
```
# Show all keyboards (-a for all, -s for short summary).
adb shell ime list -s -a

# Disable some known keyboards
adb shell ime disable com.google.android.inputmethod.latin
adb shell ime disable com.android.inputmethod.latin
adb shell ime disable com.android.inputmethod.latin/.LatinIME
adb shell ime disable com.google.android.googlequicksearchbox
adb shell ime disable com.google.android.apps.inputmethod.hindi/.HindiInputMethodService
adb shell ime disable com.google.android.inputmethod.pinyin/.PinyinIME
adb shell ime disable jp.co.omronsoft.openwnn/.OpenWnnJAJP

# Some Stack Overflow answers suggest using the following command.
# Not sure why, but probably worth checking if `adb shell ime disable` is not working on some OS versions.
# adb shell pm disable-user com.google.android.inputmethod.latin
```

3. Run tests:
Use Android Studio and run tests in `ScreenshotTest.kt` file.

4. Download screenshots from the device:
```
adb root
adb pull /data/data/net.chmielowski.randomchoice/files/ screenshots
```
