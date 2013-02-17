package com.despegar.tools.bookshelf.domain.dto

import scala.collection.JavaConversions._
import com.despegar.tools.bookshelf.domain.mongo.MongoModel
import com.despegar.tools.bookshelf.domain.mongo.MongoObject
import com.despegar.tools.bookshelf.domain.mongo.NamedDAO
import com.google.code.morphia.annotations.Entity
import com.despegar.tools.bookshelf.domain.mongo.ChildDAO

@Entity
case class PropertiesGroup(var name: String, var description: String) extends MongoModel[PropertiesGroup]{

	private def this() = this("", "")  // needed by morphia
	
	
	def properties = PropertiesGroup.findAllByParent(this).get
	
}

object PropertiesGroup extends MongoObject[PropertiesGroup] with NamedDAO[PropertiesGroup] with ChildDAO[PropertiesGroup]{
	
}