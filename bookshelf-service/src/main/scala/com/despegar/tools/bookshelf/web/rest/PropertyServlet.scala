package com.despegar.tools.bookshelf.web.rest

import com.despegar.tools.bookshelf.domain.dto.Property
import com.despegar.tools.bookshelf.api.dto.{ Property => ApiProperty }

class PropertyServlet extends RestService {
	
	// ++++++++++++++++++++++++++++++++++
	// 		Property RestFul Services
	// ++++++++++++++++++++++++++++++++++
	
	get("/:id"){
		Property.findById( params("id") ).get.asApi
	}
	
	post("/"){
		var property : Property = extract[ApiProperty]
		
		property saveOrUpdate
		
		property asApi
	}
	
	delete("/:id"){
		val property = Property.findById( params("id") ).get
			
		property.delete
	}
}