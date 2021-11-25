name := "tapir-talk"

version := "0.1"

scalaVersion := "2.13.7"
addCompilerPlugin(
  "org.typelevel" %% "kind-projector" % "0.13.2" cross CrossVersion.full
)

val TapirVersion = "0.19.0"

// https://mvnrepository.com/artifact/io.circe/circe-generic
libraryDependencies += "io.circe" %% "circe-generic" % "0.15.0-M1"
libraryDependencies += "com.softwaremill.sttp.model" %% "core" % "1.4.18"

libraryDependencies += "org.typelevel" %% "cats-effect"         % "3.2.9"
libraryDependencies += "org.http4s"    %% "http4s-blaze-server" % "0.23.6"
libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % TapirVersion
libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-core" % TapirVersion
libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % TapirVersion
libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs" % TapirVersion
libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-openapi-model" % TapirVersion
libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % TapirVersion
libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % TapirVersion
libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-redoc-bundle" % TapirVersion
