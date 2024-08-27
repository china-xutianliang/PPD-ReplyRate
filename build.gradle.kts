plugins {
    kotlin("jvm") version "1.9.22"
    id("org.jetbrains.compose") version "1.6.0"
    id("org.springframework.boot") version "2.6.5" apply false
    id("io.spring.dependency-management") version "1.0.11.RELEASE" apply false
}

allprojects {
    group = "com.xtl"
    version = "1.0-SNAPSHOT"

    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }


    ext {
        set("kotlinVersion", "1.9.22")
        set("material3Version", "1.6.0")
        set("composeVersion", "1.6.0")
        set("springBootVersion", "2.6.5")
        set("springCloudVersion", "3.1.7")
        set("h2Version", "2.1.214")
        set("lombokVersion", "1.18.32")
        set("hutoolVersion", "5.8.25")
        set("commonsLangVersion", "3.14.0")
        set("slf4jVersion", "1.7.36")
        set("logbackVersion", "1.2.11")
        set("mybatisPlusVersion", "3.5.7")
        set("appiumVersion", "9.2.2")
        set("seleniumVersion", "4.20.0")
        set("junitVersion", "5.8.2")

        set("modelmapper", "3.2.0")
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "kotlin")


    dependencies {

        // kotlin依赖
        "implementation"("org.projectlombok:lombok:${rootProject.extra["lombokVersion"]}")
        "annotationProcessor"("org.projectlombok:lombok:${rootProject.extra["lombokVersion"]}")


        "implementation"("org.apache.commons:commons-lang3:${rootProject.extra["commonsLangVersion"]}")
        "implementation"("cn.hutool:hutool-all:${rootProject.extra["hutoolVersion"]}")

        // SLF4J 和 Logback 依赖
        "implementation"("org.slf4j:slf4j-api:${rootProject.extra["slf4jVersion"]}")
        "implementation"("ch.qos.logback:logback-classic:${rootProject.extra["logbackVersion"]}")

        // 对象转换工具类
        "implementation"("org.modelmapper:modelmapper:${rootProject.extra["modelmapper"]}")

    }


    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "17"
            freeCompilerArgs = listOf("-Xjsr305=strict")
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
