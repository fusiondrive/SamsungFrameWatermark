package dev.steve.samsungframewatermark

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class Entry : IXposedHookLoadPackage {

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName != "com.sec.android.app.camera") return

        XposedBridge.log("SamsungFrameWatermark: entering " + lpparam.packageName)

        try {
            // hook: l4.f.h(x1.c)
            XposedHelpers.findAndHookMethod(
                "l4.f",
                lpparam.classLoader,
                "h",
                XposedHelpers.findClass("x1.c", lpparam.classLoader),
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        val featureObj = param.args[0]
                        val featureName = featureObj?.toString() ?: ""

                        if (featureName.contains("SUPPORT_FRAME_WATERMARK")) {
                            param.result = true
                            XposedBridge.log("SamsungFrameWatermark: FORCE SUPPORT_FRAME_WATERMARK = true")
                        }
                    }
                }
            )
        } catch (t: Throwable) {
            XposedBridge.log("SamsungFrameWatermark: hook failed: ${t.javaClass.name}: ${t.message}")
        }
    }
}