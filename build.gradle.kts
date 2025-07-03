plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.5.3"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.1"
    id("org.ajoberstar.reckon") version "0.18.1"
}

group = "com.nazarethlabs"
// versão gerenciada pelo plugin Reckon a partir das tags do Git
// version = "x.x.x"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// o plugin usa o arquivo .editorconfig para as regras de formatação
ktlint {
    version.set("1.3.1")
    verbose.set(true)
    outputToConsole.set(true)
}

// a verificação do Ktlint é executada junto com a task 'check' (ex: ./gradlew build)
tasks.check {
    dependsOn(tasks.ktlintCheck)
}

// reckon para inferir a versão a partir do Git
reckon {
    scopeFromProp()
    stageFromProp("alpha", "beta", "rc", "final")
}

// tarefa para gerar o CHANGELOG.md usando conventional-changelog-cli
// pré-requisito: npm install -g conventional-changelog-cli --registry=https://registry.npmjs.org/
tasks.register("generateChangelog", Exec::class) {
    description = "Gera o CHANGELOG.md a partir dos conventional commits."
    commandLine("conventional-changelog", "-p", "eslint", "-i", "CHANGELOG.md", "-s")
    isIgnoreExitValue = true
}

tasks.processResources {
    expand(project.properties)
}
