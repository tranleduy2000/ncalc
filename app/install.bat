mkdir release

adb uninstall com.duy.calculator.free
adb install -r release\app-release.apk
adb shell am start -n "com.duy.calculator.free/com.duy.calculator.activities.ActivitySplashScreen" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER
exit