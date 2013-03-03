package com.despegar.tools.bookshelf.domain.dto

import com.novus.salat.annotations.raw.Salat
import org.bson.types.ObjectId

case class PropertyValue(var linkEnviromentId : ObjectId = null, var linkId : ObjectId = null, var fixValue : String ) {

	private def linkEnviroment = Enviroment.findById( linkEnviromentId ).get

	def value : String = {

		if ( linkId != null && linkEnviromentId != null ) {

			return Property.findById( linkId ).get.value( linkEnviroment ).value
		}

		return fixValue
	}

}

object PropertyValue {

	def apply( value : String ) = new PropertyValue( "", "", value );

	def apply( linkEnviroment : ObjectId, linkId : ObjectId ) = new PropertyValue( linkEnviroment, linkId, null );

	def apply( enviroment : Enviroment, link : Property ) = new PropertyValue( enviroment.id, link.id, null );

}