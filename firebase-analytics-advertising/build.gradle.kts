plugins {
    id("dev.ynagai.firebase.library")
}

kotlin {
    androidLibrary {
        namespace = "dev.ynagai.firebase.analytics.advertising"
    }

    swiftPMDependencies {
        swiftPackage(
            url = url("https://github.com/firebase/firebase-ios-sdk.git"),
            version = from(libs.versions.firebase.apple.get()),
            // 加算(additive)ライブラリ。ソースから import する必要はなく、リンクされるだけで
            // firebase-analytics(FirebaseAnalyticsCore)に IDFA 収集(AdId support)を上乗せする。
            products = listOf(product("FirebaseAnalyticsIdentitySupport")),
        )
    }

    // iOS: force-link the IdentitySupport AdId member so Kotlin/Native does not
    // dead-strip it (it is only detected at runtime; nothing references it at
    // link time). See forceIdentitySupport.def. Without this the module is a
    // no-op on iOS and IDFA is never re-enabled.
    listOf(iosArm64(), iosSimulatorArm64()).forEach { target ->
        target.compilations.getByName("main").cinterops.create("forceIdentitySupport") {
            defFile(project.file("src/nativeInterop/cinterop/forceIdentitySupport.def"))
        }
    }

    sourceSets {
        androidMain.dependencies {
            // Android の広告 ID(AAID)収集を再導入する（firebase-analytics 側で除外した分）。
            implementation(libs.play.services.ads.identifier)
        }
    }
}
