rootProject.name = "otus_java_2024_02_AV"

include("Hw04AdvancedTesting")
include("Hw06ReflectionAPI")
include("Hw08StreamAPI")
include("Hw11Multithreading")
include("Hw15PatternsPart1")
include("Hw16PatternsPart2")
include("Hw19InteractionWithDB")
include("Hw21HTTPprotocol")
include("Hw22WebServer")
include("Hw23SpringContext")
include("Hw24SpringBootPart1")
include("Hw26JPAHibernate")
include("Hw28JPQL")
include("Hw29SpringDataJDBC")
include("Hw32BuildingRESTservices")
include("Hw33Serialization")
include("Hw40JMS")
include("Hw41Microservices1")
include("Hw41Microservices2")
include("Hw43Microservices3")

pluginManagement {
    val jgitver: String by settings
    val dependencyManagement: String by settings
    val springframeworkBoot: String by settings
    val johnrengelmanShadow: String by settings
    val jib: String by settings
    val protobufVer: String by settings
    val sonarlint: String by settings
    val spotless: String by settings

    plugins {
        id("fr.brouillard.oss.gradle.jgitver") version jgitver
        id("io.spring.dependency-management") version dependencyManagement
        id("org.springframework.boot") version springframeworkBoot
        id("com.github.johnrengelman.shadow") version johnrengelmanShadow
        id("com.google.cloud.tools.jib") version jib
        id("com.google.protobuf") version protobufVer
        id("name.remal.sonarlint") version sonarlint
        id("com.diffplug.spotless") version spotless
    }
}