package com.despegar.tools.bookshelf.web.rest

import org.scalatra.ScalatraServlet
import org.scalatra.scalate.ScalateSupport
import com.despegar.tools.bookshelf.domain.dto.Enviroment

class EnviromentServlet extends RestService{

	get("/enviroments"){
		asJson(Enviroment.findAll);
	}	
}