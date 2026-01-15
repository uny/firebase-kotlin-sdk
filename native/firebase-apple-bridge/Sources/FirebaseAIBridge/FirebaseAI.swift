import Foundation
import FirebaseCore
import FirebaseAILogic

@objc(KTFFirebaseAI)
public class FirebaseAI: NSObject {
    let value: FirebaseAILogic.FirebaseAI

    init(value: FirebaseAILogic.FirebaseAI) {
        self.value = value
        super.init()
    }

    @objc public static func firebaseAI(backend: BackendBridge) -> FirebaseAIBridgeClass {
        let ai = FirebaseAILogic.FirebaseAI.firebaseAI(backend: backend.value)
        return FirebaseAI(value: ai)
    }
}
