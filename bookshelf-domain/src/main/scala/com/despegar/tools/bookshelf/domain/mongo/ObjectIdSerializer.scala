package com.despegar.tools.bookshelf.domain.mongo

import org.bson.types.ObjectId
import org.json4s.Serializer
import org.json4s.Formats
import org.json4s.TypeInfo
import org.json4s.JsonAST._
import org.json4s.MappingException

class ObjectIdSerializer extends Serializer[ObjectId] {
	private val Class = classOf[ObjectId]

	def deserialize( implicit format : Formats ) = {
		case ( TypeInfo( Class, _ ), json ) => json match {
			case JString( s ) => new ObjectId( s )
			case x => throw new MappingException( "Can't convert " + x + " to  ObjectId" )
		}
	}

	def serialize( implicit format : Formats ) = {
		case x : ObjectId => JString( x.toString )
	}
}

object ObjectIdSerializer {
	def apply() : Serializer[_] = new ObjectIdSerializer()
}