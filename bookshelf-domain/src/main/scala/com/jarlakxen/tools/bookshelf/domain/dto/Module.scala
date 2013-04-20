package com.jarlakxen.tools.bookshelf.domain.dto

import org.bson.types.ObjectId
import com.jarlakxen.tools.bookshelf.domain.mongo.{ MongoModel, MongoObject, NamedDAO, ChildDAO }
import com.novus.salat.annotations.raw.Key

case class Module( name : String, description : String, parentId : ObjectId, id : ObjectId = null ) extends MongoModel[Module] {

	lazy val parent : Project = Project.findById( parentId ).get

	def properties = Property findAllByParent ( this )

	def cloneWithId(id : ObjectId) = this.copy( id = id)

}

object Module extends MongoObject[Module] with NamedDAO[Module] with ChildDAO[Module] {

	def apply( name : String, description : String, parent : Project ) : Module = new Module( name = name, description = description, parentId = parent.id )

}