import Foundation
import FirebaseCore

@objc(KTFFirebaseApp)
public class FirebaseApp: NSObject {
    let app: FirebaseCore.FirebaseApp

    init(app: FirebaseCore.FirebaseApp) {
        self.app = app
        super.init()
    }

    @objc public static func app() -> FirebaseApp? {
        guard let app = FirebaseCore.FirebaseApp.app() else { return nil }
        return FirebaseApp(app: app)
    }

    @objc public static func app(name: String) -> FirebaseApp? {
        guard let app = FirebaseCore.FirebaseApp.app(name: name) else { return nil }
        return FirebaseApp(app: app)
    }

    @objc public var name: String {
        return app.name
    }
}
