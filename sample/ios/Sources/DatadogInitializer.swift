import DatadogCore
import DatadogInternal
import DatadogLogs
import Foundation
import SampleLibrary

class DatadogInitializer : Initializer {
    
    let configuration: Configuration
    
    init(configuration: Configuration) {
        self.configuration = configuration
    }
    
    func initialize(trackingConsent: SampleLibrary.TrackingConsent) {
        initialize(trackingConsent: trackingConsent, onReady: nil)
    }
    
    func initialize(trackingConsent: SampleLibrary.TrackingConsent, onReady: (() -> Void)? = nil) {
        Datadog.initialize(
            with: configuration.toDatadogType(),
            trackingConsent: trackingConsent.toDatadogType()
        )
        if (configuration.features.logs) {
            Logs.enable()
        }
        onReady?()
    }
    
    func setTrackingConsent(trackingConsent: SampleLibrary.TrackingConsent) {
        Datadog.set(trackingConsent: trackingConsent.toDatadogType())
    }
}

private extension Configuration {
    func toDatadogType() -> Datadog.Configuration {
        return Datadog.Configuration(
            clientToken: clientToken,
            env: environment,
            site: site.toDatadogType(),
            service: service
        )
    }
}

private extension SampleLibrary.TrackingConsent {
    func toDatadogType() -> TrackingConsent {
        switch self {
        case SampleLibrary.TrackingConsent.granted:
            return .granted
        case SampleLibrary.TrackingConsent.notgranted:
            return .notGranted
        case SampleLibrary.TrackingConsent.pending:
            return .pending
        default:
            fatalError("Unexpected value \(self)")
        }
    }
}

private extension SampleLibrary.Site {
    func toDatadogType() -> DatadogSite {
        switch self {
        case SampleLibrary.Site.us1:
            return DatadogSite.us1
        case SampleLibrary.Site.us3:
            return DatadogSite.us3
        case SampleLibrary.Site.us5:
            return DatadogSite.us5
        case SampleLibrary.Site.eu:
            return DatadogSite.eu1
        case SampleLibrary.Site.us1Fed:
            return DatadogSite.us1_fed
        default:
            fatalError("Unexpected value \(self)")
        }
    }
}
