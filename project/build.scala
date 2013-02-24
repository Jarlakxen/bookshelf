import sbt._
import Keys._
import scala.xml._
import com.github.siasia._
import com.typesafe.sbteclipse.plugin.EclipsePlugin._

object Bookshelf extends Build {

	EclipseKeys.withSource := true
	EclipseKeys.executionEnvironment := Some(EclipseExecutionEnvironment.JavaSE16)

	lazy val standardSettings = Project.defaultSettings ++ Seq(
				organization := "com.bookshelf.server",
				version := "0.1.0",
				scalaVersion := "2.9.2",
				scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-unchecked"),
				javacOptions ++= Seq("-Xlint:unchecked")
			) ++ Seq( classpathTypes ~= (_ + "orbit") ) ++ Seq(resolvers ++= Seq("OSS Sonatype" at "https://oss.sonatype.org/content/groups/scala-tools/",
																				 "Sonatype Nexus Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
																				 "Sonatype Nexus Releases" at "https://oss.sonatype.org/content/repositories/releases",
																				 "Morphia Maven Repository" at "http://morphia.googlecode.com/svn/mavenrepo/") )

	def BaseProject(id: String, base: String, settings: Seq[Project.Setting[_]] = Nil) = Project(id = "bookshelf" + id, base = file(base), settings = standardSettings ++ settings)
	def RootProject() = BaseProject(id = "", base = ".")
	def SubProject(id: String, base: String, settings: Seq[Project.Setting[_]] = Nil) = BaseProject(id = "-" + id, base = base, settings = settings)

	lazy val root = RootProject() aggregate (api, domain, service)

	lazy val api = SubProject("api", "bookshelf-api")

	lazy val domain = SubProject("domain", "bookshelf-domain", MongoSettings() ++ UtilsSettings() ++ TestSettings()) dependsOn ( api )

	lazy val service = SubProject("service", "bookshelf-service", WebPlugin.webSettings ++ ScalatraSettings() ++ JettySettings() ++ TestSettings()) dependsOn ( domain )
	
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
		val scalatraVersion = "2.2.0-RC3"//"2.1.1"
		lazy val scalatra = "org.scalatra" % "scalatra" % scalatraVersion withSources() withJavadoc()
		lazy val scalate = "org.scalatra" % "scalatra-scalate" % scalatraVersion withSources() withJavadoc()
		lazy val scalatra_json = "org.scalatra" % "scalatra-json" % scalatraVersion withSources()

		//lazy val json4sNative = "org.json4s" %% "json4s-native" % "3.1.0" withSources()
		lazy val json4sJackson = "org.json4s" %% "json4s-jackson" % "3.1.0" withSources()
		lazy val json4sExt = "org.json4s" %% "json4s-ext" % "3.1.0" withSources()

		lazy val logback = "ch.qos.logback" % "logback-classic" % "1.0.9" % "runtime"
		
		Seq(libraryDependencies ++= Seq(scalatra, scalate, scalatra_json, json4sJackson, json4sExt, logback))
	}
}


object MongoSettings {
	
	def apply() = {		

		lazy val salat = "com.novus" %% "salat" % "1.9.2-SNAPSHOT"
		
		Seq(libraryDependencies ++= Seq(salat))
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
