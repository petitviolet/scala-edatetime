def commonSettings(moduleName: String) = Seq(
  name := moduleName,
  organization := "net.petitviolet",
  version := "0.1",
  scalaVersion := "2.12.6",
  testOptions in Test += Tests.Argument("-oDF"),
  javacOptions ++= Seq("-encoding", "UTF-8"),
  scalafmtOnCompile := true,
  scalafmtSbtCheck := true
)

lazy val dateTimeUtilRoot = (project in file("."))
  .settings(commonSettings("dateTimeUtilsRoot"))
  .aggregate(cachedDateTime, dateTimeUtil)

lazy val dateTimeUtil = (project in file("modules/date_time_util"))
  .settings(commonSettings("dateTimeUtil"))

lazy val cachedDateTime = (project in file("modules/cached_date_time"))
  .settings(commonSettings("cachedDateTime"))
  .dependsOn(dateTimeUtil)

lazy val example = (project in file("modules/example"))
  .settings(commonSettings("example"))
.dependsOn(cachedDateTime)
