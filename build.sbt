val SCALA_VERSION = "2.12.6"
val GROUP_ID = "net.petitviolet"
val VERSION = "0.1.2"

def commonSettings(moduleName: String) = Seq(
  name := moduleName,
  organization := GROUP_ID,
  version := VERSION,
  scalaVersion := SCALA_VERSION,
  crossScalaVersions := Seq("2.11.11", SCALA_VERSION),
  publishTo := sonatypePublishTo.value,
  testOptions in Test += Tests.Argument("-oDF"),
  javacOptions ++= Seq("-encoding", "UTF-8"),
  scalafmtOnCompile := true,
  scalafmtSbtCheck := true,
)

lazy val enhancedDateTimeRoot = (project in file("."))
  .settings(commonSettings("enhancedDateTimeRoot"))
  .aggregate(enhancedDateTimeCache, enhancedDateTime)

lazy val enhancedDateTime = (project in file("modules/enhanced_date_time"))
  .settings(commonSettings("enhanced_date_time"))

lazy val enhancedDateTimeCache = (project in file("modules/cached_date_time"))
  .settings(commonSettings("cached_enhanced_date_time"))
  .dependsOn(enhancedDateTime)

lazy val example = (project in file("modules/example"))
  .settings(commonSettings("example"))
    .settings(
      libraryDependencies += "net.petitviolet" % "enhanced_date_time" % VERSION
    )
//   .dependsOn(enhancedDateTimeCache)

