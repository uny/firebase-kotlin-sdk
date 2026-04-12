package dev.ynagai.firebase.analytics

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSClassFromString
import platform.Foundation.NSDictionary
import platform.Foundation.NSSelectorFromString
import platform.Foundation.dictionaryWithDictionary
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
internal actual fun setConsentInternal(consentSettings: Map<Any?, Any>) {
    val cls = NSClassFromString("FIRAnalytics") as? NSObject ?: return
    val sel = NSSelectorFromString("setConsent:")
    val dict = NSDictionary.dictionaryWithDictionary(consentSettings)
    cls.performSelector(sel, withObject = dict)
}
