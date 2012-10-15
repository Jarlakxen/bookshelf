package com.despegar.tools.bookshelf.web.rest

import org.scalatra.ScalatraServlet
import org.scalatra.scalate.ScalateSupport
import net.liftweb.json.ext.JodaTimeSerializers
import net.liftweb.json.DefaultFormats
import net.liftweb.json._
import net.liftweb.json.Serialization.{read, write}
import org.reflections.Reflections
import scalaj.collection.Imports._
import com.google.code.morphia.annotations.Entity
import javax.servlet.ServletConfig

trait RestService extends ScalatraServlet with ScalateSupport {
	
	implicit val formats = DefaultFormats
	
	def asJson[A <: AnyRef](value: A) = write(value)

}