plugins {
   `java-library`
}

dependencies {
   implementation(project(":annotations"))
}

java {
   sourceCompatibility = JavaVersion.VERSION_1_8
   targetCompatibility = JavaVersion.VERSION_1_8
}