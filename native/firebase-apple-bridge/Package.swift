// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "FirebaseAppleBridge",
    platforms: [.iOS(.v15)],
    products: [
        .library(name: "FirebaseAIBridge", type: .dynamic, targets: ["FirebaseAIBridge"]),
        .library(name: "FirebaseAppBridge", type: .dynamic, targets: ["FirebaseAppBridge"]),
    ],
    dependencies: [
        .package(url: "https://github.com/firebase/firebase-ios-sdk.git", from: "12.8.0"),
    ],
    targets: [
        .target(
            name: "FirebaseAIBridge",
            dependencies: [
                .product(name: "FirebaseAILogic", package: "firebase-ios-sdk"),
                .product(name: "FirebaseCore", package: "firebase-ios-sdk"),
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
