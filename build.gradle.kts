plugins {
	java
	id("org.springframework.boot") version "3.4.2"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "cmf"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-websocket")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	developmentOnly("org.springframework.boot:spring-boot-docker-compose")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	// Lombok
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	//DB
	runtimeOnly("com.h2database:h2")
	runtimeOnly("com.mysql:mysql-connector-j")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")

	//redis
	implementation("org.springframework.boot:spring-boot-starter-data-redis")

	// Security
	implementation("org.springframework.boot:spring-boot-starter-security")
	testImplementation("org.springframework.security:spring-security-test")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-client")

	//Swagger
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0")
	implementation("org.java-websocket:Java-WebSocket:1.5.2")

	// JWT
	implementation("io.jsonwebtoken:jjwt-api:0.12.6")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

	// WebClient
	implementation ("org.springframework.boot:spring-boot-starter-webflux")


	// aws
	implementation(platform("software.amazon.awssdk:bom:2.24.0"))
	implementation("software.amazon.awssdk:s3")

	// Spring Security OAuth2
	implementation ("org.springframework.security:spring-security-oauth2-client:6.4.2") // Or the version you're using
	implementation ("org.springframework.security:spring-security-oauth2-core:6.4.2") // Or the version you're using

	// Spring Kafka
	implementation("org.springframework.kafka:spring-kafka")

	// Kafka 클라이언트 (Redpanda와 호환)
	implementation("org.apache.kafka:kafka-clients:3.6.0")

	// JSON 직렬화/역직렬화 (Kafka 메시지 처리)
	implementation("com.fasterxml.jackson.core:jackson-databind")

	// 테스트용 Kafka 임베디드 서버 (선택 사항)
	testImplementation("org.springframework.kafka:spring-kafka-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
