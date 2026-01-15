// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "FirebaseAppleBridge",
    platforms: [.iOS(.v15)],
    products: [
        .library(name: "FirebaseAIBridge", targets: ["FirebaseAIBridge"]),
        .library(name: "FirebaseAppBridge", targets: ["FirebaseAppBridge"]),
    ],
    dependencies: [
        .package(url: "https://github.com/firebase/firebase-ios-sdk.git", from: "12.8.0"),
        .package(url: "https://github.com/uny/firebase-objc-sdk.git", branch: "feature/firebase-ai")
    ],
    targets: [
        .target(
            name: "FirebaseAIBridge",
            dependencies: [
                .product(name: "FirebaseAILogicObjC", package: "firebase-objc-sdk"),
            ]
        ),
        .target(
            name: "FirebaseAppBridge",
            dependencies: [
                .product(name: "FirebaseCore", package: "firebase-ios-sdk")
            ]
        ),
    ]
)
