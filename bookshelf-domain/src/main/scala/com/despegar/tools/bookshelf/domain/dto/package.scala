package com.despegar.tools.bookshelf.domain

import com.despegar.tools.bookshelf.domain.mongo.MongoModel
import scala.collection.JavaConversions._
import com.despegar.tools.bookshelf.api.dto.ApiModel
import org.bson.types.ObjectId
import com.despegar.tools.bookshelf.domain.dto.{ Enviroment => DomainEnviroment }
import com.despegar.tools.bookshelf.api.dto.{ Enviroment => ApiEnviroment }
import com.despegar.tools.bookshelf.domain.dto.{ Project => DomainProject }
import com.despegar.tools.bookshelf.api.dto.{ Project => ApiProject }
import com.despegar.tools.bookshelf.domain.dto.{ Module => DomainModule }
import com.despegar.tools.bookshelf.api.dto.{ Module => ApiModule }
import com.despegar.tools.bookshelf.domain.dto.{ Property => DomainProperty }
import com.despegar.tools.bookshelf.api.dto.{ Property => ApiProperty }
import com.despegar.tools.bookshelf.domain.dto.{ PropertyValue => DomainPropertyValue }
import com.despegar.tools.bookshelf.api.dto.{ PropertyValue => ApiPropertyValue }
import com.despegar.tools.bookshelf.domain.dto.{ PropertiesGroup => DomainPropertiesGroup }
import com.despegar.tools.bookshelf.api.dto.{ PropertiesGroup => ApiPropertiesGroup }


package object dto {

	implicit def ObjectIdToString( id : ObjectId ) : String = id match {
		case value if value != null => value.toString()
		case _ => null
	}
	
	implicit def StringToObjectId( id : String ) : ObjectId = id match {
		case value if value != null && value.nonEmpty => new ObjectId( value )
		case _ => null
	}

	// +++++++++++++++++++++++++++
	// 	  Enviroment Implicits
	// +++++++++++++++++++++++++++
	
	implicit def functionDomainEnviromentTransformer( model : DomainEnviroment ) = new DomainEnviromentTransformer(model)
	implicit def functionApiEnviromentTransformer( model : ApiEnviroment ) = new ApiEnviromentTransformer(model)
		
	class DomainEnviromentTransformer( model : DomainEnviroment ) {
		def asApi = ApiEnviroment(model.id, model.name, model.description)
	}
	
	class ApiEnviromentTransformer( model : ApiEnviroment ) {
		def asDomain = ApiToDomainEnviroment(model)
	}
	
	implicit def ApiToDomainEnviroment( model : ApiEnviroment ) : DomainEnviroment = {
		val domainModel = new DomainEnviroment( model.name, model.description, null )
		
		if( model.id != null && model.id.nonEmpty){
			domainModel.id = model.id
		}
	
		domainModel
	} 
	
	
	// +++++++++++++++++++++++++++
	// 	  Project Implicits
	// +++++++++++++++++++++++++++
	
	implicit def functionDomainProjectTransformer( model : DomainProject ) = new DomainProjectTransformer(model)
	implicit def functionApiProjectTransformer( model : ApiProject ) = new ApiProjectTransformer(model)
		
	class DomainProjectTransformer( domainModel : DomainProject ) {
		def asApi = ApiProject(domainModel.id, domainModel.name, domainModel.description)
	}
	
	class ApiProjectTransformer( apiModel : ApiProject ) {
		def asDomain = ApiToDomainProject(apiModel)
	}
	
	implicit def ApiToDomainProject( model : ApiProject ) : DomainProject  = {
		val domainModel = new DomainProject( model.name, model.description )
		
		if( model.id != null && model.id.nonEmpty){
			domainModel.id = model.id
		}
	
		domainModel
	}
	
	
	// +++++++++++++++++++++++++++
	// 	  Module Implicits
	// +++++++++++++++++++++++++++
	
	implicit def functionDomainModuleTransformer( model : DomainModule ) = new DomainModuleTransformer(model)
	implicit def functionApiModuleTransformer( model : ApiModule ) = new ApiModuleTransformer(model)
		
	class DomainModuleTransformer( domainModel : DomainModule ) {
		def asApi = ApiModule(domainModel.id, domainModel.name, domainModel.description, domainModel.parentId)
	}
	
	class ApiModuleTransformer( apiModel : ApiModule ) {
		def asDomain = ApiToDomainModule(apiModel)
	}
	
	implicit def ApiToDomainModule( model : ApiModule ) : DomainModule = {
		val domainModel = new DomainModule( model.name, model.description, model.parentId)
		
		if( model.id != null && model.id.nonEmpty){
			domainModel.id = model.id
		}
	
		domainModel
	} 
	
	
	// +++++++++++++++++++++++++++
	// 	  Property Implicits
	// +++++++++++++++++++++++++++
	
	implicit def functionDomainPropertyTransformer( model : DomainProperty ) = new DomainPropertyTransformer(model)
	implicit def functionApiPropertyTransformer( model : ApiProperty ) = new ApiPropertyTransformer(model)
		
	class DomainPropertyTransformer( domainModel : DomainProperty ) {
		def asApi = ApiProperty(domainModel.id, domainModel.name, domainModel.parentId, collection.immutable.Map() ++ domainModel.values.map( kv => (Enviroment.findById(kv._1).get.name, DomainToApiPropertyValue(kv._2))))
	}
	
	class ApiPropertyTransformer( apiModel : ApiProperty ) {
		def asDomain = ApiToDomainProperty(apiModel)
	}
	
	implicit def ApiToDomainProperty( model : ApiProperty ) : DomainProperty = {
		
		val domainModel = new DomainProperty( model.name, model.parentId, collection.mutable.Map() ++ model.values.map( kv => (Enviroment.findByName(kv._1).get.id.toString(), ApiToDomainPropertyValue(kv._2))) )
		
		if( model.id != null && model.id.nonEmpty){
			domainModel.id = model.id
		}
	
		domainModel
	} 
	
	// +++++++++++++++++++++++++++
	// 	PropertyValue Implicits
	// +++++++++++++++++++++++++++
	
	implicit def functionDomainPropertyValueTransformer( model : DomainPropertyValue ) = new DomainPropertyValueTransformer(model)
	implicit def functionApiPropertyValueTransformer( model : ApiPropertyValue ) = new ApiPropertyValueTransformer(model)
		
	class DomainPropertyValueTransformer( domainModel : DomainPropertyValue ) {
		def asApi = DomainToApiPropertyValue(domainModel)
	}
	
	implicit def DomainToApiPropertyValue( domainModel : DomainPropertyValue ) = ApiPropertyValue(domainModel.linkEnviromentId, domainModel.linkId, domainModel.value)
	
	class ApiPropertyValueTransformer( apiModel : ApiPropertyValue ) {
		def asDomain = ApiToDomainPropertyValue(apiModel)
	}

	implicit def ApiToDomainPropertyValue( model : ApiPropertyValue ) : DomainPropertyValue = {
		val value = model.linkId match {
			case v if v == null || v.isEmpty => model.value
			case _ => null
		}

		new DomainPropertyValue( model.linkEnviromentId, model.linkId, value )
	} 

	// +++++++++++++++++++++++++++
	// 	  PropertiesGroup Implicits
	// +++++++++++++++++++++++++++
	
	implicit def functionDomainPropertiesGroupTransformer( model : DomainPropertiesGroup ) = new DomainPropertiesGroupTransformer(model)
	implicit def functionApiPropertiesGroupTransformer( model : ApiPropertiesGroup ) = new ApiPropertiesGroupTransformer(model)
		
	class DomainPropertiesGroupTransformer( domainModel : DomainPropertiesGroup ) {
		def asApi = ApiPropertiesGroup(domainModel.id, domainModel.name, domainModel.description)
	}
	
	class ApiPropertiesGroupTransformer( apiModel : ApiPropertiesGroup ) {
		def asDomain = ApiToDomainPropertiesGroup(apiModel)
	}
	
	implicit def ApiToDomainPropertiesGroup( model : ApiPropertiesGroup ) : DomainPropertiesGroup = {
		val domainModel = new DomainPropertiesGroup( model.name, model.description)
		
		if( model.id != null && model.id.nonEmpty){
			domainModel.id = model.id
		}
	
		domainModel
	}
}

