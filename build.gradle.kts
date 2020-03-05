import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.3.0.M2"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    kotlin("jvm") version "1.3.70"
    kotlin("plugin.spring") version "1.3.70"
    id("idea")
}

group = "nu.westlin.spring.security"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_13
java.targetCompatibility = JavaVersion.VERSION_13

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

configure<org.gradle.plugins.ide.idea.model.IdeaModel> {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

tasks.withType<org.springframework.boot.gradle.tasks.run.BootRun> {
    systemProperties(System.getProperties().mapKeys { it.key as String })
}
