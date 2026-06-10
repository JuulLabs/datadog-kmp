import SampleLibrary
import SwiftUI

class BasicError : Error {}

@main
struct SampleApp: App {
    
    @State private var message: String = "Hello, world!"
    @State private var exceptionMessage: String = "Ouch!"
    
    private let logger: DatadogLogger
    
    init() {
        DatadogInitializer(configuration: ConfigurationKt.configuration)
            .initialize(trackingConsent: TrackingConsent.granted)
        logger = DatadogLogger(name: "root")
        InitializeKt.initializeDatadog(iosLogger: logger)
    }
    
    var body: some Scene {
        WindowGroup {
            VStack {
                HStack {
                    Text(verbatim: "Message: ")
                    TextField("Log message", text: $message)
                    
                    Button("Info") {
                        // Test logging basic info message to Datadog from app layer.
                        logger.info(
                            message: "App: \(message)",
                            attributes: ["from": "app"],
                            error: nil
                        )
                        
                        // Test logging basic info message to Datadog from library layer.
                        LoggerKt.info(message: "Lib: \(message)")
                    }
                }
                
                HStack {
                    Text(verbatim: "Exception: ")
                    TextField("Exception message", text: $exceptionMessage)
                    
                    Button("Exception") {
                        // Test logging exception to Datadog from app layer.
                        do {
                            throw BasicError()
                        } catch {
                            logger.error(
                                message: "App: \(message)",
                                attributes: ["from": "app"],
                                error: error
                            )
                        }
                        
                        // Test logging exception to Datadog from library layer.
                        LoggerKt.testException(message: "Lib: \(message)", exceptionMessage: "Lib: \(exceptionMessage)")
                    }
                }
            }
        }
    }
}
