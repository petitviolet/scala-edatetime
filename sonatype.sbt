// Your profile name of the sonatype account. The default is the same with the organization value
sonatypeProfileName := "net.petitviolet"
organization := "net.petitviolet"

publishMavenStyle := true

licenses := Seq("APL2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

// To sync with Maven central, you need to supply the following information:
pomExtra in Global := {
  <url>https://github.com/petitviolet/scala-enhanced-datetime</url>
  <licenses>
    <license>
      <name>Apache 2</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
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
