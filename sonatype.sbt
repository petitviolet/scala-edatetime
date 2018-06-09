// Your profile name of the sonatype account. The default is the same with the organization value
sonatypeProfileName := "net.petitviolet"
organization := "net.petitviolet"

publishMavenStyle := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

// License of your choice
licenses := Seq("MIT" -> url("https://petitviolet.mit-license.org/"))
homepage := Some(url("https://github.com/petitviolet/scala-enhanced-datetime"))
scmInfo := Some(
  ScmInfo(
    url("https://github.com/petitviolet/scala-enhanced-datetime"),
    "scm:git@github.com:petitviolet/scala-enhanced-datetime.git"
  )
)
developers := List(
  Developer(
    id="net.petitviolet",
    name="petitviolet",
    email="violethero0820@gmail.com",
    url=url("https://www.petitviolet.net")
  )
)

pomExtra in Global := {
  <url>https://github.com/petitviolet/scala-enhanced-datetime</url>
    <licenses>
      <license>
        <name>MIT</name>
        <url>https://petitviolet.mit-license.org/</url>
      </license>
    </licenses>
    <scm>
      <connection>scm:git:github.com/petitviolet/scala-enhanced-datetime</connection>
      <developerConnection>scm:git:git@github.com:petitviolet/scala-enhanced-datetime</developerConnection>
      <url>github.com/petitviolet/scala-enhanced-datetime</url>
    </scm>
    <developers>
      <developer>
        <id>net.petitviolet</id>
        <name>Hiroki Komurasaki</name>
        <url>https://www.petitviolet.net</url>
      </developer>
    </developers>
}

