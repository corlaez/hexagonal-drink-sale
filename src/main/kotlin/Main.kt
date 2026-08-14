
fun main(args: Array<String>) {
    val firstArg = args.first()

    return if(firstArg == "console") {
        // Doesn't work with IntelliJ Gradle Runner
        // It will work with IntelliJ Kotlin Runner. However, IntelliJ's console hijacks ESC and control+c.
        // The app is usable still but to be able to use ESC then Gradle shadowJar task should be used and
        // The app should be started in an external console like bash
        initConsoleApp()
    } else if(firstArg == "server") {
        initHttpServer()
    } else {
        // However, you could use shadowJar and then execute that jar directly in a external console
        // this would also solve the inconvenient fact that IntelliJ's console captures ESC and control+c
        System.err.println("First argument must be either (console|server)")
        println("IntelliJ should have access to already saved configurations for each mode")
    }
}
