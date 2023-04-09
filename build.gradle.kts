plugins {
    application
    id("java")
}

application {
    mainClass.set("org.example.Main")
}

group = "org.example"
version = "1.0-SNAPSHOT"


repositories {
    mavenCentral()

}



dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("com.google.guava:guava:31.1-jre")
    implementation("com.google.protobuf:protobuf-java:4.0.0-rc-2")
    compileOnly("com.google.auto.value:auto-value-annotations:1.10.1")
    annotationProcessor("com.google.auto.value:auto-value:1.10.1")
    implementation("org.mongodb:mongo-java-driver:3.12.11")
    compileOnly("org.projectlombok:lombok:1.18.24")
    // https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-core
    implementation("org.apache.logging.log4j:log4j-core:2.20.0")
    implementation("org.apache.commons:commons-lang3:3.12.0")
// https://mvnrepository.com/artifact/com.opencsv/opencsv
    implementation("com.opencsv:opencsv:5.7.1")


}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}


