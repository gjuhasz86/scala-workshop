name := "web-scraping"
version := "1.0"
scalaVersion := "2.12.3"

libraryDependencies += "net.sourceforge.htmlunit" % "htmlunit" % "2.26"
libraryDependencies += "org.seleniumhq.selenium" % "selenium-java" % "2.35.0"

libraryDependencies += "io.circe" %% "circe-core" % "0.7.0"
libraryDependencies += "io.circe" %% "circe-generic" % "0.7.0"
libraryDependencies += "io.circe" %% "circe-parser" % "0.7.0"
libraryDependencies += "io.circe" %% "circe-optics" % "0.7.0"


libraryDependencies += "org.scalaj" %% "scalaj-http" % "2.3.0"

//libraryDependencies += "org.littleshoot" % "littleproxy" % "1.1.2"
//libraryDependencies += "com.github.ganskef" % "littleproxy-mitm" % "1.1.0"
//
//libraryDependencies += "io.lemonlabs" %% "scala-uri" % "0.4.16"
//
//
//libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.7"
//libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0"