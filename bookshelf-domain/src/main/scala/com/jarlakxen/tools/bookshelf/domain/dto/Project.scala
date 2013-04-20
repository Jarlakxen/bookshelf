package com.jarlakxen.tools.bookshelf.domain.dto

import org.bson.types.ObjectId
import com.jarlakxen.tools.bookshelf.domain.mongo.{ MongoModel, MongoObject, NamedDAO }
import com.novus.salat.annotations.raw.Key

case class Project( name : String, description : String, id : ObjectId = null ) extends MongoModel[Project] {

	def modules = Module findAllByParent ( this )

	def cloneWithId(id : ObjectId) = this.copy( id = id)
}

object Project extends MongoObject[Project] with NamedDAO[Project] {

}