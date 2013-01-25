package com.despegar.tools.bookshelf.web.rest

import org.scalatra.ScalatraServlet
import org.scalatra.scalate.ScalateSupport
import javax.servlet.ServletConfig

import org.json4s.{ DefaultFormats, Formats }
import org.json4s.jackson.JsonMethods._

import org.scalatra.json._
import org.scalatra.GZipSupport

trait RestService extends ScalatraServlet with ScalateSupport with JacksonJsonSupport with GZipSupport {

	protected implicit val jsonFormats : Formats = DefaultFormats

	before() {
		contentType = formats( "json" )
	}

	def extract[A : Manifest] : A = parse(request.body).extract[A]

}