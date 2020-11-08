import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.1.7.RELEASE"
	id("io.spring.dependency-management") version "1.0.8.RELEASE"
	kotlin("jvm") version "1.2.71"
	kotlin("plugin.spring") version "1.2.71"
	jacoco
}

group = "com.zembrzuski.loader"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	maven(url = "https://jitpack.io")
	mavenCentral()
}

dependencies {
	 // implementation("org.springframework.boot:spring-boot-starter-data-elasticsearch")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.apache.commons:commons-lang3:3.9")
	implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.10.0.pr1")
 	implementation("com.fasterxml.jackson.core:jackson-core:2.10.0.pr1")
	// implementation("org.elasticsearch.client:transport:7.2.0")

	implementation("org.elasticsearch:elasticsearch:6.4.3")
	implementation("org.elasticsearch.client:elasticsearch-rest-high-level-client:6.4.3")
	implementation("commons-io:commons-io:2.6")
	implementation("com.google.firebase:firebase-admin:6.8.1")
	implementation("com.github.jkcclemens:khttp:0.1.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.mockk:mockk:1.9.3")
	// testCompile group: 'org.junit.jupiter', name: 'junit-jupiter-engine', version: '5.5.1'

}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}

// gvfs-open build/reports/coverage/index.html &
tasks.jacocoTestReport {
	reports {
		xml.isEnabled = false
		csv.isEnabled = false
		html.isEnabled = true
		html.destination = file("$buildDir/reports/coverage")
	}

	classDirectories.setFrom(
			sourceSets.main.get().output.asFileTree.matching {
				exclude("**/com/zembrzuski/loader/filesystemloader/FilesystemLoaderApplication*")
				exclude("**/com/zembrzuski/loader/filesystemloader/config/**")
			}
	)
}
