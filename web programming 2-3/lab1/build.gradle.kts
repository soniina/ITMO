plugins {
    kotlin("jvm") version "1.9.23"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    flatDir {
        dir("libs")
    }
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(files("libs/fastcgi-lib.jar"))
}

tasks.jar {
    manifest {
        attributes (
            "MAIN-Class" to "MainKt"
        )
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    archiveFileName = "server.jar"
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}