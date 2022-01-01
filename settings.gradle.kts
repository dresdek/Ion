pluginManagement {
	repositories {
		gradlePluginPortal()
		maven("https://papermc.io/repo/repository/maven-public/")
	}
}

rootProject.name = "Ion"

include("common")
include("proxy")
include("server")