import sbt._
import Keys._

import com.earldouglas.xsbtwebplugin._
import com.earldouglas.xsbtwebplugin.PluginKeys._

import com.typesafe.sbteclipse.plugin.EclipsePlugin._

object Bookshelf extends Build {

    val Organization = "com.jarlakxen.bookshelf.server"
    val Name = "Bookshelf"
    val Version = "2.2-SNAPSHOT"
    val ScalaVersion = "2.10.4"
    val ScalatraVersion = "2.2.1"

	lazy val project = Project (
        "bookshelf-server",
        file("."),
        settings =  Defaults.defaultSettings ++
                    com.earldouglas.xsbtwebplugin.WebPlugin.webSettings ++
                    Seq(
                        organization := Organization,
                        name := Name,
                        version := Version,
                        scalaVersion := ScalaVersion,
                        scalacOptions ++= Seq( "-deprecation", "-unchecked", "-feature", "-language:implicitConversions", "-language:postfixOps" ),
                        EclipseKeys.withSource := true,
                        EclipseKeys.projectFlavor := EclipseProjectFlavor.Scala) ++
                    Seq( classpathTypes ~= (_ + "orbit") ) ++
                    Seq(resolvers ++= Seq(Classpaths.typesafeReleases)) ++

                    Seq(
                        libraryDependencies ++= Seq(

                            "org.scala-lang" % "scala-reflect"      % ScalaVersion,

                            // Scalatra
                            "org.scalatra"  %% "scalatra"           % ScalatraVersion,
                            "org.scalatra"  %% "scalatra-auth"      % ScalatraVersion,
                            "org.scalatra"  %% "scalatra-scalate"   % ScalatraVersion,
                            "org.scalatra"  %% "scalatra-json"      % ScalatraVersion,
                            "org.json4s"    %% "json4s-jackson"     % "3.2.9",
                            "org.json4s"    %% "json4s-ext"         % "3.2.9",

                            // Log
                            "ch.qos.logback" % "logback-classic"    % "1.1.2",

                            // Salat
                            "com.novus"     %% "salat-core" % "1.9.3",
                            "com.novus"     %% "salat-util" % "1.9.3",

                            // Utils
                            "com.github.nscala-time" %% "nscala-time" % "0.8.0",

                            // Jetty
                            "org.eclipse.jetty" % "jetty-webapp" % "9.1.0.v20131115" % "container" artifacts (Artifact("jetty-webapp", "jar", "jar")),
                            "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "container;provided;test" artifacts (Artifact("javax.servlet", "jar", "jar")),

                            // Testing
                            "org.specs2" %% "specs2" % "2.3.11" % "test",
                            "junit" % "junit" % "4.11" % "test"
                        )
                    )
        ) 
}
