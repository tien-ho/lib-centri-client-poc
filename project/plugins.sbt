logLevel := Level.Warn

// See https://wiki.audaxhealth.com/display/ENG/Build+Structure#BuildStructure-Localconfiguration
credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

resolvers += Resolver.url("Rally Plugin Releases", url("https://artifacts.werally.in/artifactory/ivy-plugins-release"))(Resolver.ivyStylePatterns)

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

addSbtPlugin("com.rallyhealth" %% "rally-versioning" % "1.2.0")

addSbtPlugin("com.rallyhealth" %% "rally-sbt-plugin" % "0.13.0")

addSbtPlugin("com.rallyhealth.sbt" %% "rally-shading-sbt-plugin" % "1.0.2")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.0")
