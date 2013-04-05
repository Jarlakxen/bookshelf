import sbt._
import Keys._
import scala.xml._
import com.github.siasia._

import com.typesafe.sbteclipse.plugin.EclipsePlugin._
import com.typesafe.sbt.SbtStartScript

import org.scalatra.sbt._
import org.scalatra.sbt.PluginKeys._
import com.mojolly.scalate.ScalatePlugin._
import ScalateKeys._

object Bookshelf extends Build {

	lazy val standardSettings = Project.defaultSettings ++ Seq(
				organization := "com.bookshelf.server",
				version := "0.1.0",
				scalaVersion := "2.9.2",
				scalacOptions ++= Seq("-encoding", "UTF-8")//, "-deprecation", "-unchecked"),
				//javacOptions ++= Seq("-Xlint:unchecked")
			) ++ Seq( EclipseKeys.projectFlavor := EclipseProjectFlavor.Scala,
					  EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource, 
					  EclipseKeys.withSource := true, 
					  EclipseKeys.executionEnvironment := Some(EclipseExecutionEnvironment.JavaSE16) ) ++
				//Seq( classpathTypes ~= (_ + "orbit") ) ++
				Seq(resolvers ++= Seq("OSS Sonatype" at "https://oss.sonatype.org/content/groups/scala-tools",
									  "Sonatype Nexus Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
									  "Sonatype Nexus Releases" at "https://oss.sonatype.org/content/repositories/releases",
									  "TypeSafe Akka Releases" at "http://repo.typesafe.com/typesafe/simple/akka-releases-cache"))

	def BaseProject(id: String, base: String, settings: Seq[Project.Setting[_]] = Nil) = Project(id = "bookshelf" + id, base = file(base), settings = standardSettings ++ settings)
	def RootProject() = BaseProject(id = "", base = ".")
	def SubProject(id: String, base: String, settings: Seq[Project.Setting[_]] = Nil) = BaseProject(id = "-" + id, base = base, settings = settings)

	lazy val root = RootProject() aggregate (api, domain, service)

	lazy val api = SubProject("api", "bookshelf-api")

	lazy val domain = SubProject("domain", "bookshelf-domain", MongoSettings() ++ ConfigLoaderSettings() ++ UtilsSettings() ++ TestSettings()) dependsOn ( api )

	lazy val service = SubProject("service", "bookshelf-service", WebPlugin.webSettings ++ SbtStartScript.startScriptForWarSettings ++ ScalatraSettings() ++ JettySettings() ++ TestSettings()) dependsOn ( domain )
	
	override def projects = Seq(root, api, domain, service)

}

object JettySettings {
	
	def apply() = {	
		
    	lazy val jetty = "org.eclipse.jetty" % "jetty-webapp" % "8.1.7.v20120910" % "container,compile"
    	lazy val servlet_orbit = "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "container,provided,compile" artifacts Artifact("javax.servlet", "jar", "jar")

		Seq(libraryDependencies ++= Seq(jetty, servlet_orbit))
	}
}

object ScalatraSettings {
	
	def apply() = {
		val scalatraVersion = "2.2.0"//"2.1.1"
		lazy val scalatra = "org.scalatra" % "scalatra" % scalatraVersion withSources() withJavadoc()
		lazy val scalate = "org.scalatra" % "scalatra-scalate" % scalatraVersion withSources() withJavadoc()
		lazy val scalatra_json = "org.scalatra" % "scalatra-json" % scalatraVersion withSources()

		//lazy val json4sNative = "org.json4s" %% "json4s-native" % "3.1.0" withSources()
		lazy val json4sJackson = "org.json4s" %% "json4s-jackson" % "3.1.0" withSources()
		lazy val json4sExt = "org.json4s" %% "json4s-ext" % "3.1.0" withSources()

		lazy val logback = "ch.qos.logback" % "logback-classic" % "1.0.9" % "runtime"
		
		Seq(libraryDependencies ++= Seq(scalatra, scalate, scalatra_json, json4sJackson, json4sExt, logback)) ++ ScalatraPlugin.scalatraWithJRebel ++ scalateSettings
	}
}

object ConfigLoaderSettings {
	
	def apply() = {		

		lazy val config = "com.typesafe" % "config" % "1.0.0" withSources()
		
		Seq(libraryDependencies ++= Seq(config))
	}
}

object MongoSettings {
	
	def apply() = {		

		lazy val salat = "com.novus" %% "salat" % "1.9.1"
		
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
