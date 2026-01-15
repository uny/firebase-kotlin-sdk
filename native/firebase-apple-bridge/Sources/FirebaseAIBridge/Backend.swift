import Foundation
import FirebaseAILogic

@objc(KTFBackend)
public class Backend: NSObject {
    let value: FirebaseAILogic.Backend

    init(value: FirebaseAILogic.Backend) {
        self.value = value
        super.init()
    }

    @objc public static func googleAI() -> Backend {
        return Backend(value: .googleAI())
    }

    @objc public static func vertexAI() -> BackendBridge {
        return BackendBridge(value: .vertexAI())
    }

    @objc public static func vertexAI(location: String) -> BackendBridge {
        return BackendBridge(value: .vertexAI(location: location))
    }
}
