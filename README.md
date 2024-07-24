# Scala ZIO Project Setup

This document provides a step-by-step guide to setting up a Scala project using the ZIO and http4s libraries with a well-configured `build.sbt`.

## Project Structure
- **Create the SBT project**
  ```bash
  sbt new scala/scala3.g8
  ```

- **Add the required Dependencies**

  ./project/Libraries.scala
    ```scala
    import sbt._

  // version Object
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

  // Libraries setup 
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
    ```

- **Configure `build.sbt`:**

   ```scala
  ThisBuild / version := "0.1.0-SNAPSHOT"

  ThisBuild / scalaVersion := Versions.scalaVersion

  lazy val root = (project in file("."))
    .settings(
      name := "file-streaming-app"
    )

  libraryDependencies ++= Libraries.allDeps
   enablePlugins(AssemblyPlugin)
   
   ThisBuild / scalafmtOnCompile := true
   ThisBuild / scalafmtConfig := file(".scalafmt.conf")
   ```

4. **Create Scalafmt Configuration File:**

   Create a `.scalafmt.conf` file in the root of your `learning` directory with the following content:

    ```hocon
    version = "3.0.0"

    maxColumn = 120
    align = most
    ```

5. **Create a Simple ZIO and http4s Application:**

   Create a simple ZIO and http4s application in `learning/src/main/scala/Main.scala`:

    ```scala
    import zio._
    import org.http4s.HttpRoutes
    import org.http4s.dsl.Http4sDsl
    import org.http4s.blaze.server.BlazeServerBuilder
    import zio.interop.catz._

    object Main extends ZIOAppDefault {

      val dsl = new Http4sDsl[Task]{}
      import dsl._

      val httpApp = HttpRoutes.of[Task] {
        case GET -> Root / "hello" => 
          Ok("Hello, ZIO with http4s!")
      }.orNotFound

      val server = ZIO.runtime[ZEnv].flatMap { implicit rts =>
        BlazeServerBuilder[Task]
          .bindHttp(8080, "localhost")
          .withHttpApp(httpApp)
          .serve
          .compile
          .drain
          .orDie
      }

      def run = server
    }
    ```

6. **Add a Test Case:**

   Create a test file in `learning/src/test/scala/MySpec.scala`:

    ```scala
    import zio.test._
    import zio.test.Assertion._
    import zio.test.TestAspect._
    import zio._

    object MySpec extends ZIOSpecDefault {

      def spec = suite("MySpec")(
        test("simple test") {
          assertTrue(1 + 1 == 2)
        }
      ) @@ silent
    }
    ```

## Running the Project

To run your ZIO and http4s application, navigate to the `learning` directory and use the following command:

```bash
sbt run
sbt ~reStart
```

Or
- VSCODE: There is small text on top of the object you can click it to run the main object

## Running the test

To run the tests, use the following command:

```bash
sbt test
```

## Formatting Code

To format your code with Scalafmt, use the following command:

```bash
sbt scalafmt
```

## Creating a Fat JAR

To create a fat JAR, use the following command:
```bash
sbt assembly
```

This will create a fat JAR in the target/scala-3.3.3/ directory which you can run with:
```bash
java -jar target/scala-3.3.3/learning-assembly-0.1.0-SNAPSHOT.jar
```
