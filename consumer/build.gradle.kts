plugins {
   `java-library`
}

dependencies {
   implementation(project(":annotations"))
   annotationProcessor(project(":processor"))
}

java {
   sourceCompatibility = JavaVersion.VERSION_1_8
   targetCompatibility = JavaVersion.VERSION_1_8
}