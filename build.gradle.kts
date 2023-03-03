plugins {
	java
	id("org.springframework.boot") version "2.7.8"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.github.nbbrd.java-sql-util:java-sql-jdbc:1.0.3")
	implementation("org.xerial:sqlite-jdbc:3.40.1.0")
    implementation("joda-time:joda-time:2.12.2")
	implementation("name.remal.gradle-plugins.lombok:lombok:2.0.2")
	implementation("io.freefair.lombok:io.freefair.lombok.gradle.plugin:6.6.2")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
