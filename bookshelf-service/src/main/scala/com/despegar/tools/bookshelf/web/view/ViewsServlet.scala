package com.despegar.tools.bookshelf.web.view

import org.scalatra.ScalatraServlet
import org.scalatra.scalate.ScalateSupport

class ViewsServlet extends ScalatraServlet with ScalateSupport {
	
	before() {
		contentType = "text/html"
	}
	
	get("/"){
		mustache("main")
	}
	
	get("/home"){
		mustache("main")
	}
		
	get("/enviroments"){
		mustache("enviroments")
	}
}