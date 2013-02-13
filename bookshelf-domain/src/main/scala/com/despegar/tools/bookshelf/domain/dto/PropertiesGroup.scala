package com.despegar.tools.bookshelf.domain.dto

import scala.collection.JavaConversions._
import com.despegar.tools.bookshelf.domain.mongo.MongoModel
import com.despegar.tools.bookshelf.domain.mongo.MongoObject
import com.despegar.tools.bookshelf.domain.mongo.NamedDAO
import com.google.code.morphia.annotations.Entity

@Entity
case class PropertiesGroup(var name: String, var description: String, var properties: java.util.List[Property]) extends MongoModel[PropertiesGroup]{

	private def this() = this("", "", List())  // needed by morphia
	
	def this( name : String, description: String ) = this( name, description, List() )
	
}

object PropertiesGroup extends MongoObject[PropertiesGroup] with NamedDAO[PropertiesGroup]{
	
}