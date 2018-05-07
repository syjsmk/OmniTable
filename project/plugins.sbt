addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.2")
addSbtPlugin("com.github.ddispaltro" % "sbt-reactjs" % "0.6.8")

dependencyOverrides += "org.webjars.npm" % "minimatch" % "3.0.4"
dependencyOverrides += "org.webjars.npm" % "graceful-readlink" % "1.0.1"