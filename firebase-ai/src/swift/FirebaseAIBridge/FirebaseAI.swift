import Foundation

@objc public class FirebaseAI: NSObject {
    @objc public static func ai(_ backend: Backend) -> FirebaseAI {
        return FirebaseAI()
    }
}
