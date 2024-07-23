# Scala ZIO Project Setup

This document provides a step-by-step guide to setting up a Scala project using the ZIO and http4s libraries with a well-configured `build.sbt`.

## Project Structure

1. **Create the Project Structure:**

    ```bash
    mkdir -p learning/project
    mkdir -p learning/src/main/scala
    mkdir -p learning/src/test/scala
    ```

2. **Add Scalafmt and sbt-assembly Plugins:**

   Create a file named `plugins.sbt` inside the `learning/project` directory with the following content:

    ```scala
    addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.6")
    addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.15.0")
    ```

3. **Configure `build.sbt`:**

   Create a `build.sbt` file in the root of your `learning` directory with the following content:

   ```scala
   name := "learning"

   version := "0.1.0-SNAPSHOT"
   
   scalaVersion := "3.3.3"
   
   lazy val zioVersion = "2.0.17"
   lazy val http4sVersion = "0.23.12" // Use a stable version of http4s
   
   libraryDependencies ++= Seq(
   "dev.zio" %% "zio" % zioVersion,
   "dev.zio" %% "zio-streams" % zioVersion,
   "dev.zio" %% "zio-test" % zioVersion % Test,
   "dev.zio" %% "zio-test-sbt" % zioVersion % Test,
   "org.http4s" %% "http4s-blaze-server" % http4sVersion,
   "org.http4s" %% "http4s-blaze-client" % http4sVersion,
   "org.http4s" %% "http4s-circe" % http4sVersion,
   "org.http4s" %% "http4s-dsl" % http4sVersion,
   "io.circe" %% "circe-generic" % "0.14.5",
   "io.circe" %% "circe-parser" % "0.14.5"
   )
   
   scalacOptions ++= Seq(
   "-deprecation",
   "-encoding", "utf8",
   "-feature",
   "-unchecked",
   "-Wunused:imports",
   "-Wunused:explicits",
   "-Wunused:implicits",
   "-Wunused:locals",
   "-Wunused:params",
   "-Wunused:privates",
   "-Wvalue-discard"
   )
   
   ThisBuild / organization := "com.example"
   
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

