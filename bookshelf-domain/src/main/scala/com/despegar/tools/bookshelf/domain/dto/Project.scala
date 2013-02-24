package com.despegar.tools.bookshelf.domain.dto

import org.bson.types.ObjectId
import com.despegar.tools.bookshelf.domain.mongo.{MongoModel, MongoObject, NamedDAO}
import com.novus.salat.annotations.raw.Key


case class Project(var name: String,var description: String, var id : ObjectId = null) extends MongoModel[Project]{

	def modules = Module findAllByParent(this)
	
}

object Project extends MongoObject[Project] with NamedDAO[Project] {
	
}