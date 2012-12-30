package com.despegar.tools.bookshelf.web.rest

import com.despegar.tools.bookshelf.domain.dto._

class PropertyServlet extends RestService{

	get("/:id"){
		multiParams("splat")
	}
	
	post("/"){
		//multiParams("splat")	
		request.body
		//parsedBody
	}
	
	post("/:id/algo"){
		//multiParams("splat")	
		request.body
		//parsedBody
	}
	
}