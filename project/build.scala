import sbt._
import Keys._
import scala.xml._
import com.github.siasia._
import com.typesafe.sbteclipse.plugin.EclipsePlugin._

object Bookshelf extends Build {

	EclipseKeys.withSource := true
	EclipseKeys.executionEnvironment := Some(EclipseExecutionEnvironment.JavaSE16)

	lazy val standardSettings = Project.defaultSettings ++ Seq(
				organization := "com.despegar.tools",
				version := "0.1.0",
				scalaVersion := "2.9.2",
				scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-unchecked"),
				javacOptions ++= Seq("-Xlint:unchecked")
			) ++ Seq( classpathTypes ~= (_ + "orbit") ) ++ Seq(resolvers ++= Seq("OSS Sonatype" at "https://oss.sonatype.org/content/groups/scala-tools/",
																				 "Sonatype Nexus Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
																				 "Morphia Maven Repository" at "http://morphia.googlecode.com/svn/mavenrepo/") )

	def BaseProject(id: String, base: String, settings: Seq[Project.Setting[_]] = Nil) = Project(id = "bookshelf" + id, base = file(base), settings = standardSettings ++ settings)
	def RootProject() = BaseProject(id = "", base = ".")
	def SubProject(id: String, base: String, settings: Seq[Project.Setting[_]] = Nil) = BaseProject(id = "-" + id, base = base, settings = settings)

	lazy val root = RootProject() aggregate (api, domain, service)

	lazy val api = SubProject("api", "bookshelf-api")

	lazy val domain = SubProject("domain", "bookshelf-domain", MongoSettings() ++ UtilsSettings() ++ TestSettings()) dependsOn ( api )

	lazy val service = SubProject("service", "bookshelf-service", WebPlugin.webSettings ++ ScalatraSettings() ++ JettySettings()) dependsOn ( domain )
	
	override def projects = Seq(root, api, domain, service)

}

object JettySettings {
	
	def apply() = {		
		lazy val servlet_orbit = "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "container;provided;test" artifacts (Artifact("javax.servlet", "jar", "jar"))
		lazy val jetty = "org.eclipse.jetty" % "jetty-webapp" % "8.1.7.v20120910" % "container" 
		
		Seq(libraryDependencies ++= Seq(servlet_orbit, jetty))
	}
}

object ScalatraSettings {
	
	def apply() = {
		lazy val scalatraVersion = "2.2.0-SNAPSHOT"//"2.1.1"
		lazy val scalatra = "org.scalatra" % "scalatra" % scalatraVersion withSources() withJavadoc()
		lazy val scalate = "org.scalatra" % "scalatra-scalate" % scalatraVersion withSources() withJavadoc()
		lazy val scalatra_json = "org.scalatra" % "scalatra-json" % scalatraVersion withSources()

		//lazy val json4sNative = "org.json4s" %% "json4s-native" % "3.0.0" withSources()
		lazy val json4sJackson = "org.json4s" %% "json4s-jackson" % "3.0.0" withSources()


		// Pick your favorite slf4j binding
		lazy val slf4jBinding = "ch.qos.logback" % "logback-classic" % "0.9.29" % "runtime"
		
		Seq(libraryDependencies ++= Seq(scalatra, scalate, scalatra_json, json4sJackson, slf4jBinding))
	}
}


object MongoSettings {
	
	def apply() = {		
		lazy val morphia = "com.google.code.morphia" % "morphia" % "0.99.1-SNAPSHOT" withSources()
		lazy val morphia_logger = "com.google.code.morphia" % "morphia-logging-slf4j" % "0.99" withSources()
		lazy val slf4j = "org.slf4j" % "slf4j-log4j12" % "1.7.1"
		lazy val drivers = "org.mongodb" % "mongo-java-driver" % "2.8.0" withSources()
		
		Seq(libraryDependencies ++= Seq(morphia, morphia_logger, slf4j, drivers))
	}
}

object UtilsSettings {
	
	def apply() = {
		lazy val reflection = "org.reflections" % "reflections" % "0.9.8" withJavadoc()

		Seq(libraryDependencies ++= Seq(reflection))
	}
}

object TestSettings {
	
	def apply() = {
		lazy val specs2 = "org.specs2" %% "specs2" % "1.12.1" % "test" withSources() withJavadoc()
		lazy val junit = "junit" % "junit" % "4.8.1" % "test"
		
		Seq(libraryDependencies ++= Seq(specs2, junit))
	}
}
