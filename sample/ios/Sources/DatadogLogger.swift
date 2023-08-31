import DatadogLogs
import Foundation
import SampleLibrary
import AnyCodable

class DatadogLogger : SampleLibrary.IosLogger {

    private let logger: LoggerProtocol
    
    init(name: String, level: LoggerLevel = LoggerLevel.debug, configuration: LoggerConfiguration? = nil) {
        self.logger = DatadogLogs.Logger.create(
            with: datadogLoggerConfiguration(
                name: name,
                level: level,
                configuration: configuration
            )
        )
    }
    
    func notice(message: String, attributes: [String : Any]? = nil, throwable: KotlinThrowable? = nil) {
        notice(
            message: message,
            attributes: attributes?.mapValuesToEncodable(),
            error: throwable?.asNSError()
        )
    }

    func notice(message: String, attributes: [String : Encodable]? = nil, error: Error? = nil) {
        logger.notice(message, error: error, attributes: attributes)
    }
    
    func debug(message: String, attributes: [String : Any]? = nil, throwable: KotlinThrowable? = nil) {
        debug(
            message: message,
            attributes: attributes?.mapValuesToEncodable(),
            error: throwable?.asNSError()
        )
    }

    func debug(message: String, attributes: [String : Encodable]? = nil, error: Error? = nil) {
        logger.debug(message, error: error, attributes: attributes)
    }
    
    func info(message: String, attributes: [String : Any]? = nil, throwable: KotlinThrowable? = nil) {
        info(
            message: message,
            attributes: attributes?.mapValuesToEncodable(),
            error: throwable?.asNSError()
        )
    }

    func info(message: String, attributes: [String : Encodable]? = nil, error: Error? = nil) {
        logger.info(message, error: error, attributes: attributes)
    }
    
    func warn(message: String, attributes: [String : Any]? = nil, throwable: KotlinThrowable? = nil) {
        warn(
            message: message,
            attributes: attributes?.mapValuesToEncodable(),
            error: throwable?.asNSError()
        )
    }

    func warn(message: String, attributes: [String : Encodable]? = nil, error: Error? = nil) {
        logger.warn(message, error: error, attributes: attributes)
    }
    
    func error(message: String, attributes: [String : Any]? = nil, throwable: KotlinThrowable? = nil) {
        error(
            message: message,
            attributes: attributes?.mapValuesToEncodable(),
            error: throwable?.asNSError()
        )
    }

    func error(message: String, attributes: [String : Encodable]? = nil, error: Error? = nil) {
        logger.error(message, error: error, attributes: attributes)
    }
}

private extension [String: Any] {
    func mapValuesToEncodable() -> [String: Encodable]? {
        mapValues { AnyEncodable($0) }
    }
}

private func datadogLoggerConfiguration(
    name: String,
    level: LoggerLevel,
    configuration: LoggerConfiguration?
) -> DatadogLogs.Logger.Configuration {
    if configuration != nil {
        return DatadogLogs.Logger.Configuration(
            service: configuration!.serviceName,
            networkInfoEnabled: configuration!.networkInfoEnabled,
            bundleWithRumEnabled: configuration!.bundleWithRumEnabled,
            bundleWithTraceEnabled: configuration!.bundleWithTraceEnabled,
            remoteSampleRate: configuration!.remoteSampleRate,
            remoteLogThreshold: toDatadogLogLevel(from: level)
        )
    } else {
        return DatadogLogs.Logger.Configuration(
            name: name,
            remoteLogThreshold: toDatadogLogLevel(from: level)
        )
    }
}

private func toDatadogLogLevel(from: LoggerLevel) -> DatadogLogs.LogLevel {
    switch from {
    case LoggerLevel.debug, LoggerLevel.verbose:
        return DatadogLogs.LogLevel.debug
    case LoggerLevel.info:
        return DatadogLogs.LogLevel.info
    case LoggerLevel.notice:
        return DatadogLogs.LogLevel.notice
    case LoggerLevel.warn:
        return DatadogLogs.LogLevel.warn
    case LoggerLevel.error:
        return DatadogLogs.LogLevel.error
    case LoggerLevel.assert:
        return DatadogLogs.LogLevel.critical
    default:
        fatalError("Unexpected value \(from)")
    }
}
