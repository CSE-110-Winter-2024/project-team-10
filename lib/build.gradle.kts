plugins {
    id("java-library")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    implementation("androidx.annotation:annotation:1.7.1")
    implementation("androidx.room:room-common:2.6.1")
    implementation("org.jetbrains:annotations:15.0")
}