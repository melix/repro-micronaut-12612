plugins {
   application
}

dependencies {
   implementation(project(":consumer"))
}

java {
   sourceCompatibility = JavaVersion.VERSION_1_8
   targetCompatibility = JavaVersion.VERSION_1_8
}

application {
   mainClass.set("com.acme.app.Main")
}