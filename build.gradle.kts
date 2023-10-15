import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	war
	id("org.springframework.boot") version "3.1.3"
	id("io.spring.dependency-management") version "1.1.3"
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
	kotlin("plugin.jpa") version "1.8.22"
}

group = "dev.mateux"
version = "0.0.1"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.1.4")
	implementation("org.springframework.boot:spring-boot-starter-web:3.1.4")
	implementation("org.springframework.boot:spring-boot-starter-validation:3.1.4")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.springframework.boot:spring-boot-starter-security:3.1.4")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server:3.1.4")

	runtimeOnly("org.postgresql:postgresql:42.6.0")

	providedRuntime("org.springframework.boot:spring-boot-starter-tomcat:3.1.4")

	testImplementation("org.springframework.boot:spring-boot-starter-test:3.1.4")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
