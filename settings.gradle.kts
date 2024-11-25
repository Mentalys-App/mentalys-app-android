import java.util.Properties

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {

    // Load local.properties
    val localPropertiesFile = File(rootDir, "local.properties")
    val localProperties = Properties()
    if (localPropertiesFile.exists()) {
        localProperties.load(localPropertiesFile.inputStream())
    }

    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/Cuberto/liquid-swipe-android")
            credentials {
                username = localProperties.getProperty("gpr.user") ?: ""
                password = localProperties.getProperty("gpr.key") ?: ""
            }
        }

    }
}

rootProject.name = "MentalysApp"
include(":app")
