name := "tapir-talk"

version := "0.1"

scalaVersion := "2.13.7"

val TapirVersion           = "0.17.19"

// https://mvnrepository.com/artifact/io.circe/circe-generic
libraryDependencies += "io.circe" %% "circe-generic" % "0.15.0-M1"

libraryDependencies += "org.typelevel" %% "cats-effect" % "2.5.3"
libraryDependencies += "org.http4s" %% "http4s-blaze-server" % "0.21.23"
libraryDependencies += "com.softwaremill.sttp.tapir"  %% "tapir-http4s-server"      % TapirVersion
libraryDependencies += "com.softwaremill.sttp.tapir"  %% "tapir-core"               % TapirVersion
libraryDependencies += "com.softwaremill.sttp.tapir"  %% "tapir-json-circe"         % TapirVersion
libraryDependencies += "com.softwaremill.sttp.tapir"  %% "tapir-openapi-docs"       % TapirVersion
libraryDependencies += "com.softwaremill.sttp.tapir"  %% "tapir-openapi-model"      % TapirVersion
libraryDependencies += "com.softwaremill.sttp.tapir"  %% "tapir-openapi-circe-yaml" % TapirVersion
libraryDependencies += "com.softwaremill.sttp.tapir"  %% "tapir-redoc-http4s"       % TapirVersion
libraryDependencies += "com.softwaremill.sttp.tapir"  %% "tapir-swagger-ui-http4s"  % TapirVersion