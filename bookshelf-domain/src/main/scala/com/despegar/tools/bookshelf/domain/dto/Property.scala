package com.despegar.tools.bookshelf.domain.dto

import org.bson.types.ObjectId
import com.despegar.tools.bookshelf.domain.mongo.{ MongoModel, MongoObject, NamedDAO, ChildDAO }
import scala.collection.mutable.Map
import com.novus.salat.annotations.raw.Key

case class Property(var name : String, var parentId : ObjectId, var values : Map[String, String] = Map(), var id : ObjectId = null ) extends MongoModel[Property] {

	private var _parent : Option[MongoModel[_ <: AnyRef]] = None

	def parent = this.synchronized {
		_parent match {
			case Some( value ) => value
			case None => {
				null
			}
		}
	}

	def value( enviroment : Enviroment ) = this.values( enviroment.name )

}

object Property extends MongoObject[Property] with NamedDAO[Property] with ChildDAO[Property] {

	def apply( name : String, parent : MongoModel[_ <: AnyRef], values : Map[Enviroment, String] ) : Property = {
		new Property( name = name, parentId = parent.id, values = values.map( entry => ( entry._1.name, entry._2 ) ) )
	}

	def deleteEnvironmentFromAll( enviroment : Enviroment ) {
		deleteEnvironmentFromAll( enviroment.name )
	}

	def deleteEnvironmentFromAll( enviroment : String ) {
		this.synchronized {
			Property.findAll.foreach { propery =>
				propery.values = Map()
				propery.update
			}
		}
	}
}