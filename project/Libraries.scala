import sbt._

object Versions {
  val scalaVersion = "3.3.3"

  val zioVersion        = "2.1.6"
  val zioHttpVersion    = "3.0.0-RC9"
  val zioConfigVersion  = "4.0.2"
  val zioJsonVersion    = "0.7.1"
  val zioLoggingVersion = "2.1.16"

  val sttpZioJsonVersion = "4.0.0-M16"
  val tapirVersion       = "1.10.14"

  val flywayVersion = "10.16.0"
  val hikariVersion = "5.1.0"
  val quillVersion  = "4.8.0"
  val sqliteVersion = "3.46.0.0"

  val logbackVersion = "1.5.6"
}

object Libraries {
  import Versions._

  // zio
  val zioCore           = "dev.zio" %% "zio"                 % zioVersion
  val zioHttp           = "dev.zio" %% "zio-http"            % zioHttpVersion
  val zioConfig         = "dev.zio" %% "zio-config"          % zioConfigVersion
  val zioConfigMagnolia = "dev.zio" %% "zio-config-magnolia" % zioConfigVersion
  val zioConfigTypesafe = "dev.zio" %% "zio-config-typesafe" % zioConfigVersion
  val zioJson           = "dev.zio" %% "zio-json"            % zioJsonVersion
  val zioTest           = "dev.zio" %% "zio-test"            % zioVersion % Test
  val zioTestSbt        = "dev.zio" %% "zio-test-sbt"        % zioVersion % Test

  //tapir
  val tapirCore        = "com.softwaremill.sttp.tapir" %% "tapir-core"         % tapirVersion
  val tapirOpenApiDocs = "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs" % tapirVersion
  val tapirSwaggerUi   = "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui"   % tapirVersion

  //db
  val sqliteJDBC = "org.xerial"   % "sqlite-jdbc"    % sqliteVersion
  val flyway     = "org.flywaydb" % "flyway-core"    % flywayVersion
  val hikari     = "com.zaxxer"   % "HikariCP"       % hikariVersion
  val quill      = "io.getquill" %% "quill-jdbc-zio" % quillVersion

  val zioLogging  = "dev.zio"       %% "zio-logging"       % zioLoggingVersion
  val slf4jLogger = "dev.zio"       %% "zio-logging-slf4j" % zioLoggingVersion
  val logback     = "ch.qos.logback" % "logback-classic"   % logbackVersion

  val allDeps = Seq(
    zioCore,
    zioHttp,
    zioConfig,
    zioConfigMagnolia,
    zioConfigTypesafe,
    zioJson,
    zioTest,
    zioTestSbt,
    tapirCore,
    tapirOpenApiDocs,
    tapirSwaggerUi,
    sqliteJDBC,
    flyway,
    hikari,
    quill,
    zioLogging,
    slf4jLogger,
    logback
  )
}
