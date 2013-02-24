package com.despegar.tools.bookshelf.domain.dto

import com.despegar.tools.bookshelf.domain.mongo.{MongoModel, MongoObject, NamedDAO}
import com.novus.salat.annotations.raw.Key
import org.bson.types.ObjectId

case class PropertiesGroup(var name: String, var description: String, var id : ObjectId = null) extends MongoModel[PropertiesGroup]{
	
	def properties = Property.findAllByParent(this)
	
}

object PropertiesGroup extends MongoObject[PropertiesGroup] with NamedDAO[PropertiesGroup]{
	
}