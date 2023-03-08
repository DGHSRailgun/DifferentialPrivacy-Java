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
//    implementation(fileTree(mapOf("dir" to "/Users/liyuanshi/DPProjects/differential-privacy/java/bazel-bin", "include" to listOf("*.jar"))))
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("com.google.guava:guava:31.1-jre")
    implementation("com.google.protobuf:protobuf-java:4.0.0-rc-2")
    compileOnly("com.google.auto.value:auto-value-annotations:1.10.1")
    annotationProcessor("com.google.auto.value:auto-value:1.10.1")

}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}


