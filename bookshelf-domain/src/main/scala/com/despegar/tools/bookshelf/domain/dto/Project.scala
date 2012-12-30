package com.despegar.tools.bookshelf.domain.dto

import com.google.code.morphia.annotations.{Entity, Serialized, Reference}
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import org.bson.types.ObjectId
import annotation.target.field
import com.despegar.tools.bookshelf.domain.mongo.{MongoModel, MongoObject, NamedDAO}
import java.util.ArrayList


@Entity
case class Project(var name: String, var description: String) extends MongoModel[Project]{

	private def this() = this("", "")  // needed by morphia
	
}

object Project extends MongoObject[Project] with NamedDAO[Project] {
	
}