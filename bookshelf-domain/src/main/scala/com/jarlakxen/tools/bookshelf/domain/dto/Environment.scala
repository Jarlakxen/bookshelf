package com.jarlakxen.tools.bookshelf.domain.dto

import org.bson.types.ObjectId
import com.jarlakxen.tools.bookshelf.domain.mongo.{ MongoModel, MongoObject, NamedDAO }
import com.novus.salat.annotations.raw.Key

case class Enviroment( name : String, description : String, id : ObjectId = null ) extends MongoModel[Enviroment] {

	def cloneWithId(id : ObjectId) = this.copy( id = id)
	
}

object Enviroment extends MongoObject[Enviroment] with NamedDAO[Enviroment] {

}