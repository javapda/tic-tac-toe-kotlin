import java.util.*

val mainKotlinClass="com.javapda.SimpleTicTacToeKt"
val theJdkVersion = 17
group = "com.javapda"
version = "0.0.1-SNAPSHOT"
plugins {
    kotlin("jvm") version "1.9.23"
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(theJdkVersion)
}
application {
    mainClass.set(mainKotlinClass)
}

// Make the build task depend on shadowJar
tasks.named("build") {
    dependsOn("shadowJar")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = mainKotlinClass
        attributes["Jdk-Version"] = theJdkVersion
        attributes["Author"] = "John Kroubalkian"
        attributes["Build-Date"] = Date()
    }
}
