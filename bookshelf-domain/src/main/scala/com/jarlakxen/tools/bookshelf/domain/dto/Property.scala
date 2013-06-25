package com.jarlakxen.tools.bookshelf.domain.dto

import org.bson.types.ObjectId
import com.jarlakxen.tools.bookshelf.domain.mongo.{ MongoModel, MongoObject, NamedDAO, ChildDAO }
import scala.collection.mutable.Map
import com.novus.salat.annotations.raw.Key

case class Property(name : String, parentId : ObjectId, values : Map[String, PropertyValue] = Map(), id : ObjectId = null ) extends MongoModel[Property] {

	def value( enviroment : Enviroment ) : Option[PropertyValue] = {
		this.values.contains(enviroment.id) match {
			case true => Some(this.values( enviroment.id ))
			case false => None
		}
	}
	
	def cloneWithId(id : ObjectId) = this.copy( id = id)
}

object Property extends MongoObject[Property] with NamedDAO[Property] with ChildDAO[Property] {

	def apply( name : String, parent : MongoModel[_ <: AnyRef], values : Map[Enviroment, PropertyValue] ) : Property = {
		new Property( name = name, parentId = parent.id, values = values.map( entry => ( entry._1.id.toString(), entry._2 ) ) )
	}

	def deleteAllReferenceOf( parentProperty : Property ) = this.synchronized {
		Property.findAll.foreach { property =>
			for( envKey <- property.values.keys ){
			  val currentValue = property.values(envKey)
			  if(currentValue.linkId == property.id){
				  property.values(envKey) = PropertyValue(linkEnviromentId = currentValue.linkEnviromentId, fixValue = parentProperty.values(envKey).fixValue )
				  property.update
			  }
			}
		}
	}
	
	
	def deleteAllFromEnvironment( enviroment : Enviroment ) = this.synchronized {
		Property.findAll.foreach { propery =>
			propery.values -= enviroment.id
			propery.update
		}
	}
}