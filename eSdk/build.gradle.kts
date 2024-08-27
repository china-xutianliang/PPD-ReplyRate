plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

dependencies {

    implementation(project(":eCore"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib:${rootProject.extra["kotlinVersion"]}")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${rootProject.extra["kotlinVersion"]}")
    implementation("org.springframework.boot:spring-boot-starter-web:${rootProject.extra["springBootVersion"]}")
}


tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = false
}

tasks.named<Jar>("jar") {
    enabled = true
}