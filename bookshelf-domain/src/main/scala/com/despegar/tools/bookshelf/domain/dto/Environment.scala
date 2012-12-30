package com.despegar.tools.bookshelf.domain.dto

import org.bson.types.ObjectId
import com.google.code.morphia.annotations.Id
import com.despegar.tools.bookshelf.domain.mongo.NamedDAO
import com.despegar.tools.bookshelf.domain.mongo.MongoObject
import com.despegar.tools.bookshelf.domain.mongo.MongoModel

case class Enviroment(name : String, description : String ) extends MongoModel[Enviroment] {

	private def this() = this( "", "" ) // needed by morphia

}

object Enviroment extends MongoObject[Enviroment] with NamedDAO[Enviroment] {

}