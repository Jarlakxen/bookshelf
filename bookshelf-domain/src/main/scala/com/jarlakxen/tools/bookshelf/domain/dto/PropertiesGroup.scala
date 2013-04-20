package com.jarlakxen.tools.bookshelf.domain.dto

import com.jarlakxen.tools.bookshelf.domain.mongo.{MongoModel, MongoObject, NamedDAO}
import com.novus.salat.annotations.raw.Key
import org.bson.types.ObjectId

case class PropertiesGroup(name: String, description: String, id : ObjectId = null) extends MongoModel[PropertiesGroup]{
	
	def properties = Property.findAllByParent(this)
	
	def cloneWithId(id : ObjectId) = this.copy( id = id)
}

object PropertiesGroup extends MongoObject[PropertiesGroup] with NamedDAO[PropertiesGroup]{
	
}