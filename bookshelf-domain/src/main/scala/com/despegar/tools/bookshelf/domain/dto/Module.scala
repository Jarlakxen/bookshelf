package com.despegar.tools.bookshelf.domain.dto

import com.google.code.morphia.annotations.{Entity, Serialized, Reference}
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import org.bson.types.ObjectId
import annotation.target.field
import com.despegar.tools.bookshelf.domain.mongo.{MongoModel, MongoObject, NamedDAO, ChildDAO}
import java.util.ArrayList


@Entity
case class Module(var name: String, var description: String, @(Reference @field) var parent: Project) extends MongoModel[Module]{

	private def this() = this("", "", null)  // needed by morphia
	
	def properties = Property.findAllByParent(this).get

}

object Module extends MongoObject[Module] with NamedDAO[Module] with ChildDAO[Module]  {
	
}