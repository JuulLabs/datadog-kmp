tasks.register<Delete>("clean") {
    description = "Delete sample Xcode project created with xcodegen"
    group = "Project Setup"
    delete(
        "$projectDir/SampleApp.xcodeproj/",
        "$projectDir/Info.plist",
    )
}

tasks.register<Exec>("generateXcodeProject") {
    description = "Use xcodegen to create a sample Xcode project"
    group = "Project Setup"
    dependsOn(tasks.getByPath(":library:createSwiftPackage"))
    val paths = listOf(
        "/usr/local/bin/xcodegen",
        "/opt/homebrew/bin/xcodegen",
    )
    val xcodegen = paths.firstOrNull { file(it).exists() }
        ?: error("xcodegen not found at $paths")
    commandLine(xcodegen)
}

tasks.register<Exec>("openXcode") {
    description = "Open sample Xcode project"
    group = "Project Setup"
    dependsOn("generateXcodeProject")
    commandLine("open", "SampleApp.xcodeproj")
}
