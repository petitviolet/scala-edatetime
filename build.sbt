val SCALA_VERSION = "2.13.0"
val GROUP_ID = "net.petitviolet"
val libraryVersion = "0.4.0"

val libraryName = "edatetime"

def commonSettings(moduleName: String) = Seq(
  name := moduleName,
  organization := GROUP_ID,
  version := libraryVersion,
  scalaVersion := SCALA_VERSION,
  crossScalaVersions := Seq("2.11.11", "2.12.8", SCALA_VERSION),
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
  },
  libraryDependencies ++= commonDependencies
)

lazy val commonDependencies = Seq(
  "org.scalatest" %% "scalatest" % "3.1.0-SNAP13" % Test,
  "org.scalacheck" %% "scalacheck" % "1.14.0" % Test,
)

lazy val edatetimeRoot = (project in file("."))
  .settings(commonSettings("enhancedDateTimeRoot"))
  .aggregate(cachedEdatetime, edatetime)

lazy val edatetime = (project in file(s"modules/$libraryName".replace('-', '_')))
  .settings(commonSettings(libraryName))

lazy val cachedEdatetime = (project in file(s"modules/cached-edatetime".replace('-', '_')))
  .settings(commonSettings("cached-edatetime"))
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

