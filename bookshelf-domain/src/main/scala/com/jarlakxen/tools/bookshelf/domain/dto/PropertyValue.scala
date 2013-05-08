package com.jarlakxen.tools.bookshelf.domain.dto

import com.novus.salat.annotations.raw.Salat
import org.bson.types.ObjectId

case class PropertyValue(linkEnviromentId : ObjectId = null, linkId : ObjectId = null, fixValue : String = null) {

	private def linkEnviroment = Enviroment.findById( linkEnviromentId ).get

	def value : String = {

		if ( linkId != null && linkEnviromentId != null ) {

			return Property.findById( linkId ).get.value( linkEnviroment ) match {
				case Some(v) => v.value
				case None => null
			}
		}

		return fixValue
	}

}

object PropertyValue {

	def apply( value : String ) = new PropertyValue( null, null, value );

	def apply( linkEnviroment : ObjectId, linkId : ObjectId ) = new PropertyValue( linkEnviroment, linkId, null );

	def apply( enviroment : Enviroment, link : Property ) = new PropertyValue( enviroment.id, link.id, null );

}