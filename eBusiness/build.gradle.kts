import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

dependencies {
    implementation(project(":eCore"))
    implementation(project(":eSdk"))


    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.skiko:skiko-awt:0.7.93")

    implementation("org.jetbrains.kotlin:kotlin-stdlib:${rootProject.extra["kotlinVersion"]}")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${rootProject.extra["kotlinVersion"]}")
    implementation("org.jetbrains.compose.material3:material3:${rootProject.extra["composeVersion"]}")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap:${rootProject.extra["springCloudVersion"]}")
    implementation("org.springframework.boot:spring-boot-starter-web:${rootProject.extra["springBootVersion"]}")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:${rootProject.extra["springBootVersion"]}")
    implementation("com.h2database:h2:${rootProject.extra["h2Version"]}")
    implementation("org.projectlombok:lombok:${rootProject.extra["lombokVersion"]}")
    implementation("cn.hutool:hutool-all:${rootProject.extra["hutoolVersion"]}")
    implementation("org.apache.commons:commons-lang3:${rootProject.extra["commonsLangVersion"]}")
    implementation("org.slf4j:slf4j-api:${rootProject.extra["slf4jVersion"]}")
    implementation("ch.qos.logback:logback-classic:${rootProject.extra["logbackVersion"]}")

    implementation("com.baomidou:mybatis-plus-boot-starter:${rootProject.extra["mybatisPlusVersion"]}")
    implementation("com.baomidou:mybatis-plus-annotation:${rootProject.extra["mybatisPlusVersion"]}")
    implementation("com.github.pagehelper:pagehelper-spring-boot-starter:2.1.0")

    implementation("io.appium:java-client:${rootProject.extra["appiumVersion"]}")
    implementation("org.seleniumhq.selenium:selenium-java:${rootProject.extra["seleniumVersion"]}")
    implementation("org.seleniumhq.selenium:selenium-api:${rootProject.extra["seleniumVersion"]}")
    implementation("org.seleniumhq.selenium:selenium-remote-driver:${rootProject.extra["seleniumVersion"]}")
    testImplementation("org.springframework.boot:spring-boot-starter-test:${rootProject.extra["springBootVersion"]}")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa:${rootProject.extra["springBootVersion"]}")
    testImplementation("com.h2database:h2:${rootProject.extra["h2Version"]}")
    testImplementation("org.junit.jupiter:junit-jupiter-api:${rootProject.extra["junitVersion"]}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${rootProject.extra["junitVersion"]}")
    annotationProcessor("org.projectlombok:lombok:${rootProject.extra["lombokVersion"]}")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.6.4")

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


compose.desktop {
    application {
        mainClass = "com.xtl.ebusiness.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "eDS"
            packageVersion = "1.0.0"

            // 设置项目资源目录appResourcesRootDir.set     rootProject.layout.projectDirectory根路径   project.layout.projectDirectory子项目路径
            appResourcesRootDir.set(rootProject.layout.projectDirectory.dir("resources"))



            // 使用 includeAllModules 来包含 JRE
            jvmArgs.add("-Xms256m")
            jvmArgs.add("-Xmx512m")
            jvmArgs.add("-Dfile.encoding=UTF-8")
            includeAllModules = true

            // 设置程序的图标
            macOS {
                iconFile.set(project.file("src\\main\\resources\\icons\\main.ico"))
            }
            windows() {

                // 启动程序图标
                iconFile.set(project.file("src/main/resources/icons/main.ico"))

                // 包含所有模块
                includeAllModules = true
                // 使用 copySpec 将 appium.exe 复制到应用程序安装目录
                copySpec().from(rootProject.layout.projectDirectory.dir("resources/windows")) {
                    include("appium.exe")
                }.into("./") // 复制到根目录

            }
            linux {
                iconFile.set(project.file("src\\main\\resources\\icons\\main.ico"))
            }
        }
    }
}
println("根项目资源路径: " + rootProject.layout.projectDirectory.dir("resources/windows").asFile.absolutePath)



springBoot {
    mainClass.set("com.xtl.ebusiness.AutomationApplication")
}


tasks.register<JavaExec>("runSpringBoot") {
    group = "application"
    description = "Run the Spring Boot application"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("com.xtl.ebusiness.AutomationApplication")
}



