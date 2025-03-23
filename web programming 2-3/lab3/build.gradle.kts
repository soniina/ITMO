plugins {
    id("war")
    kotlin("jvm")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    providedCompile("jakarta.platform:jakarta.jakartaee-api:9.1.0")
    providedCompile("org.primefaces:primefaces:12.0.0")

    implementation("org.eclipse.persistence:org.eclipse.persistence.jpa:3.0.2")

    implementation("org.postgresql:postgresql:42.7.2")

    testImplementation(kotlin("test"))

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation(kotlin("stdlib-jdk8"))
}

tasks.war {
    archiveFileName.set("server.war")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}