package com.despegar.tools.bookshelf.web.view

import org.scalatra.ScalatraServlet
import org.scalatra.scalate.ScalateSupport

class HomeServlet extends ScalatraServlet with ScalateSupport {
	
	get("/"){
		contentType="text/html"
		
		mustache("main")
	}
}