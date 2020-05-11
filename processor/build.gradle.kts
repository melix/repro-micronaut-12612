plugins {
   `java-library`
}

dependencies {
   implementation(project(":annotations"))
   implementation("org.ow2.asm:asm:8.0.1")
}

java {
   sourceCompatibility = JavaVersion.VERSION_1_8
   targetCompatibility = JavaVersion.VERSION_1_8
}