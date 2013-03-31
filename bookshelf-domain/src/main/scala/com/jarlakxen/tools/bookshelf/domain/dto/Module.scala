package com.jarlakxen.tools.bookshelf.domain.dto

import org.bson.types.ObjectId
import com.jarlakxen.tools.bookshelf.domain.mongo.{ MongoModel, MongoObject, NamedDAO, ChildDAO }
import com.novus.salat.annotations.raw.Key

case class Module(var name : String, var description : String, parentId : ObjectId, var id : ObjectId = null ) extends MongoModel[Module] {

	private var _parent : Option[Project] = None

	def parent = this.synchronized {
		_parent match {
			case Some( value ) => value
			case None => {
				_parent = Project findById ( parentId )
				_parent.get
			}
		}
	}

	def properties = Property findAllByParent ( this )

}

object Module extends MongoObject[Module] with NamedDAO[Module] with ChildDAO[Module] {

	def apply( name : String, description : String, parent : Project ) : Module = new Module( name = name, description = description, parentId = parent.id )

}