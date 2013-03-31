package com.jarlakxen.tools.bookshelf.domain.dto

import org.bson.types.ObjectId
import com.jarlakxen.tools.bookshelf.domain.mongo.{ MongoModel, MongoObject, NamedDAO }
import com.novus.salat.annotations.raw.Key

case class Enviroment( var name : String, var description : String, var id : ObjectId = null ) extends MongoModel[Enviroment] {

}

object Enviroment extends MongoObject[Enviroment] with NamedDAO[Enviroment] {

}