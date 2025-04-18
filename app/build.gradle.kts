
plugins {
    alias(libs.plugins.android.application)
    jacoco
}

android {
    namespace = "com.example.recipebook"
    compileSdk = 34


    defaultConfig {
        applicationId = "com.example.recipebook"
        minSdk = 19
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true


        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }



    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            enableAndroidTestCoverage = true
            enableUnitTestCoverage = true
            isMinifyEnabled = false
        }
    }

    flavorDimensions += listOf("default")

    productFlavors {
        create("free") {
            applicationId = "com.example.recipebook.free"
            versionName = "1.0-free"
            dimension = "default"
            resValue("string", "app_name", "RecipeBook Free")
        }
        create("paid") {
            applicationId = "com.example.recipebook.paid"
            versionName = "1.0-paid"
            dimension = "default"
            resValue("string", "app_name", "RecipeBook Paid")
        }
    }




    buildFeatures{
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildToolsVersion = "34.0.0"


}



dependencies {
    // Network
    implementation(libs.retrofit)
    implementation(libs.gson)
    implementation(libs.okhttp)

    // Picture
    implementation(libs.picasso)

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.room.common)
    testImplementation(libs.junit)

    // Test dependencies: junit and mockito for testing
    testImplementation(libs.junit.v412)
    testImplementation(libs.mockito.core)

    testImplementation("org.mockito:mockito-inline:4.0.0")
    testImplementation("org.robolectric:robolectric:4.9")

    // Espresso core and testing
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.4.0")

    // AndroidX testing libraries
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test:runner:1.4.0")
    androidTestImplementation("androidx.test:rules:1.4.0")

    // pdf
    implementation("com.itextpdf:itext7-core:7.1.14")
}


val exclusions = listOf(
    "**/R.class",
    "**/R\$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*",
    "**/*Test*.*"
)

tasks.withType(Test::class) {
    configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
    }
}


android.applicationVariants.all {
        // Correctly manipulating the string in Kotlin
        var variantName = this.name
        if (variantName[0].isLowerCase()) {
            variantName = variantName[0].toUpperCase() + variantName.substring(1)
        }

        val unitTests = "test${variantName}UnitTest"
        val androidTests = "connected${variantName}AndroidTest"

        tasks.register<JacocoReport>("Jacoco${variantName}CodeCoverage") {
            dependsOn(unitTests, androidTests)
            group = "Reporting"
            description = "Execute UI and unit tests, generate and combine Jacoco coverage report"
            reports {
                xml.required.set(true)
                html.required.set(true)
            }
            sourceDirectories.setFrom(layout.projectDirectory.dir("src/main"))
            classDirectories.setFrom(files(
                fileTree(layout.buildDirectory.dir("intermediates/javac/")) {
                    exclude(exclusions)
                },
                fileTree(layout.buildDirectory.dir("tmp/kotlin-classes/")) {
                    exclude(exclusions)
                }
            ))
            executionData.setFrom(files(
                fileTree(layout.buildDirectory) { include("**/*.exec", "**/*.ec") }
            ))
        }
    }

