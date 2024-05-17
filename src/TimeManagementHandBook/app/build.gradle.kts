plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.time_management_handbook"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.time_management_handbook"
        minSdk = 31
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    packaging {
        resources {
            excludes.add("META-INF/DEPENDENCIES")
            excludes.add("META-INF/INDEX.LIST")
        }
    }
    buildToolsVersion = "34.0.0"

}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.mediarouter:mediarouter:1.7.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation(files("libs/connectToSSMS.jar"))

    implementation ("com.google.oauth-client:google-oauth-client-jetty:1.36.0")
    implementation ("com.google.apis:google-api-services-calendar:v3-rev20220715-2.0.0")
    implementation ("com.google.api-client:google-api-client-android:2.5.1")
    implementation ("com.google.android.gms:play-services-auth:21.1.1")
    implementation ("com.google.http-client:google-http-client-android:1.44.2")

    implementation ("com.google.oauth-client:google-oauth-client:1.36.0")

    implementation ("com.google.api-client:google-api-client-android:2.5.1")
    implementation ("com.google.apis:google-api-services-calendar:v3-rev411-1.25.0")
    implementation ("com.google.api-client:google-api-client-gson:2.5.1")
    implementation ("com.google.oauth-client:google-oauth-client-jetty:1.36.0")

    implementation ("com.google.apis:google-api-services-calendar:v3-rev310-1.25.0")

    implementation ("joda-time:joda-time:2.12.7")
}