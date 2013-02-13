package com.despegar.tools.bookshelf.domain.dto

import org.bson.types.ObjectId
import com.google.code.morphia.annotations.{ Entity, Serialized, Reference }
import annotation.target.field
import com.despegar.tools.bookshelf.domain.mongo.{ MongoModel, MongoObject, NamedDAO, ChildDAO }
import scala.collection.mutable.Map
import scala.collection.JavaConversions._

@Entity
case class Property(var name : String, @(Reference @field) var parent: MongoModel[_], var values : java.util.Map[String, String] )  extends MongoModel[Property]{

	def this( name : String, parent: Module, values : Map[Enviroment, String] ) = this( name, parent, (for ((key, value) <- values) yield (key.name, value)) )
	
	def this( name : String, parent: Module ) = this( name, parent, Map[Enviroment, String]() )
	
	private def this() = this( "", null ) // needed by morphia

	def value( enviroment : Enviroment ) : String = values.get( enviroment.name )

}

object Property extends MongoObject[Property] with NamedDAO[Property] with ChildDAO[Property] {

	def deleteEnvironmentFromAll(name: String) = update(createQuery, createUpdateOperations.unset("values."+name) )
	
}