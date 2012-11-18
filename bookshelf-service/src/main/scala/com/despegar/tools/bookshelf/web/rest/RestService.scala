package com.despegar.tools.bookshelf.web.rest

import org.scalatra.ScalatraServlet
import org.scalatra.scalate.ScalateSupport
import javax.servlet.ServletConfig

import org.json4s.{ DefaultFormats, Formats }

// JSON handling support from Scalatra
import org.scalatra.json._

trait RestService extends ScalatraServlet with ScalateSupport with JacksonJsonSupport with JValueResult {

	protected implicit val jsonFormats : Formats = DefaultFormats

	before() {
		contentType = formats( "json" )
	}

	def extract[A : Manifest] : A = parsedBody.extract[A]

}