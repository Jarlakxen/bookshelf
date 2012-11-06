package com.despegar.tools.bookshelf.web.rest

import org.scalatra.ScalatraServlet
import org.scalatra.scalate.ScalateSupport
import org.reflections.Reflections
import com.google.code.morphia.annotations.Entity
import javax.servlet.ServletConfig
import org.json4s.native.Serialization.{read, write => swrite}
import org.json4s.DefaultFormats

trait RestService extends ScalatraServlet with ScalateSupport {

	implicit val formats = DefaultFormats

	def asJson( value : AnyRef ) = swrite(value)

	def fromJson[A:Manifest]( json : String ) : A = read[A](json)

}