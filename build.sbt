val SCALA_VERSION = "2.12.8"
val GROUP_ID = "net.petitviolet"
val VERSION = "0.3.0"

val NAME = "edatetime"
val CACHE_NAME = "cached-edatetime"

def commonSettings(moduleName: String) = Seq(
  name := moduleName,
  organization := GROUP_ID,
  version := VERSION,
  scalaVersion := SCALA_VERSION,
  crossScalaVersions := Seq("2.11.11", SCALA_VERSION),
  testOptions in Test += Tests.Argument("-oDF"),
  javacOptions ++= Seq("-encoding", "UTF-8"),
  scalafmtOnCompile := true,
  scalafmtSbtCheck := true,
  publishMavenStyle := true,
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases"  at nexus + "service/local/staging/deploy/maven2")
  }

)

lazy val edatetimeRoot = (project in file("."))
  .settings(commonSettings("enhancedDateTimeRoot"))
  .aggregate(cachedEdatetime, edatetime)

lazy val edatetime = (project in file(s"modules/$NAME".replace('-', '_')))
  .settings(commonSettings(NAME))

lazy val cachedEdatetime = (project in file(s"modules/$CACHE_NAME".replace('-', '_')))
  .settings(commonSettings(CACHE_NAME))
  .dependsOn(edatetime)

lazy val example = (project in file("./example"))
  .settings(commonSettings("example"))
    .settings(
//      libraryDependencies ++= List(
//        GROUP_ID %% NAME % VERSION,
//        GROUP_ID %% CACHE_NAME % VERSION
//      )
    )
   .dependsOn(edatetime, cachedEdatetime)

