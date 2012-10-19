package com.despegar.tools.bookshelf.domain.dto

import org.bson.types.ObjectId
import com.google.code.morphia.annotations.{ Entity, Serialized, Reference }
import annotation.target.field
import com.despegar.tools.bookshelf.domain.mongo.{ MongoModel, MongoObject, NamedModelObject }
import scala.collection.immutable.Map
import scala.collection.JavaConversions._

@Entity
case class Property( name : String, @( Reference @field ) parent : Property, values : java.util.Map[String, String] )  extends MongoModel[Property]{

	def this( name : String, values : Map[Enviroment, String] ) = this( name, null, (for ((key, value) <- values) yield (key.name, value)) )
	
	def this( name : String ) = this( name, Map() )
	
	private def this() = this( "" ) // needed by morphia

	def value( enviroment : Enviroment ) = if ( parent != null ) parent.values.get( enviroment.name ) else values.get( enviroment.name )

}

object Property extends MongoObject[Property] with NamedModelObject[Property] {

}