import Foundation
import FirebaseAILogic

@objc public class Backend: NSObject {
    let value: FirebaseAILogic.Backend

    init(value: FirebaseAILogic.Backend) {
        self.value = value
    }

    @objc public static func googleAI() -> Backend {
        return Backend(value: .googleAI())
    }
}
