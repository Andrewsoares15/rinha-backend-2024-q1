import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.2"
    id("io.spring.dependency-management") version "1.1.4"
    // Apply GraalVM Native Image plugin
    id("org.graalvm.buildtools.native") version "0.9.28"
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"
    kotlin("plugin.jpa") version "1.9.22"
}

group = "com.rinhabackend"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_21
}

graalvmNative {
    agent {
        defaultMode.set("standard") // Default agent mode if one isn't specified using `-Pagent=mode_name`
        enabled.set(true) // Enables the agent

        modes {
            // The standard agent mode generates metadata without conditions.
            standard {

            }
        }

        builtinCallerFilter.set(true)
        builtinHeuristicFilter.set(true)
        enableExperimentalPredefinedClasses.set(false)
        enableExperimentalUnsafeAllocationTracing.set(false)
        trackReflectionMetadata.set(true)

        // Copies metadata collected from tasks into the specified directories.
//        metadataCopy {
//            inputTaskNames.add("test") // Tasks previously executed with the agent attached.
//            outputDirectories.add("src/main/resources/META-INF/native-image")
//            mergeWithExisting.set(true) // Instead of copying, merge with existing metadata in the output directories.
//        }
    }

    binaries {
        named("main") {
            imageName.set("rinha") // The name of the native image, defaults to the project name
            buildArgs.add("--verbose")
//            buildArgs.add("--static")
//            buildArgs.add("--libc=glibc") // or musl
//            buildArgs.add("--link-at-build-time")
            buildArgs.add("-H:+AddAllCharsets")
        }
    }
}


repositories {
    maven {
        url = uri("https://raw.githubusercontent.com/graalvm/native-build-tools/snapshots")
    }
    gradlePluginPortal()
	mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")
    implementation("org.flywaydb:flyway-core")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "21"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}