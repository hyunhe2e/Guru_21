plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
}


android {
    namespace = "com.example.guru_21"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.guru_21"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        buildTypes {
            getByName("release") {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
                buildConfigField("String", "PLACES_API_KEY", "\"AIzaSyDqm9PtQEAMdjgTIGCulWugc4cBIN7_JLQ\"")
            }

            getByName("debug") {
                buildConfigField("String", "PLACES_API_KEY", "\"AIzaSyDqm9PtQEAMdjgTIGCulWugc4cBIN7_JLQ\"")
            }
        }


    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}
val kotlinVersion = "1.8.10" // Kotlin 버전 정의

dependencies {
    implementation("io.jsonwebtoken:jjwt-api:0.11.5") // API
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5") // Implementation
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5") // Jackson parser
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.play.services.maps)
    implementation("com.google.android.gms:play-services-maps:19.0.0")  // 구글 place API
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:$kotlinVersion")) // 변수 사용
    implementation("com.google.android.libraries.places:places:3.5.0") // 구글 place API
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx) // 이미지뷰로 사진 가져오는 라이브러리
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}