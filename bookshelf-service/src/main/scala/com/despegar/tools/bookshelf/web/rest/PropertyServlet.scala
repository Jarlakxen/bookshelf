package com.despegar.tools.bookshelf.web.rest

import com.despegar.tools.bookshelf.domain.dto._

class PropertyServlet extends RestService{
	
	get("/:id"){
		Property.findById( params("id") ).get.asApi;
	}
	
	post("/"){
		var property = extract[com.despegar.tools.bookshelf.api.dto.Property].asDomain
		
		property save
		
		property asApi
	}
	
	delete("/:id"){
		val property = Property.findById( params("id") ).get
			
		property.delete
	}
}