package com.despegar.tools.bookshelf.domain.dto

import com.google.code.morphia.annotations.{Entity, Serialized, Reference}
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import org.bson.types.ObjectId
import annotation.target.field
import com.despegar.tools.bookshelf.domain.mongo.{MongoModel, MongoObject, NamedModelObject}
import java.util.ArrayList


@Entity
case class Module(var name: String, var description: String, @(Reference @field) var properties: java.util.List[Property]) extends MongoModel[Module]{

	private def this() = this("", "", new ArrayList())  // needed by morphia
	
	def this(name: String, description: String) = this(name, description, new ArrayList())

}

object Module extends MongoObject[Module] with NamedModelObject[Module] {
	
}