package com.despegar.tools.bookshelf.domain

import com.despegar.tools.bookshelf.domain.mongo.MongoModel
import scala.collection.JavaConversions._
import com.despegar.tools.bookshelf.api.dto.ApiModel
import org.bson.types.ObjectId

package object dto {
	
	implicit def ObjectIdToString(id : ObjectId) : String = id.toString()
	implicit def StringToObjectId(id : String) : ObjectId = new ObjectId(id)
	
	// Domain To Api Implicits
	class RichDomain(model : MongoModel[_]) {
		def asApi = DomainToApi(model)
	}
	
	class RichDomainSeq(model : Seq[MongoModel[_]]) {
		def asApi = model map( v => DomainToApi(v) )
	}
	
	implicit def DomainSeqToApiSeq(model : Seq[MongoModel[_]]) = new RichDomainSeq(model)
	implicit def DomainToRichDomain(model : MongoModel[_]) = new RichDomain(model)

	implicit def DomainToApi( model : MongoModel[_] ) : ApiModel = model match {
		case enviroment : Enviroment => DomainToApiEnviroment( enviroment )
		case projectModel : Project => DomainToApiProject( projectModel )
		case moduleModel : Module => DomainToApiModule( moduleModel )
		case propertyModel : Property => DomainToApiProperty( propertyModel )
		case _ => null
	}

	implicit def DomainToApiEnviroment( enviromentModel : Enviroment ) : ApiModel = com.despegar.tools.bookshelf.api.dto.Enviroment( enviromentModel.id, enviromentModel.name, enviromentModel.description )
	implicit def DomainToApiProject( projectModel : Project ) : ApiModel = com.despegar.tools.bookshelf.api.dto.Project( projectModel.id, projectModel.name, projectModel.description )
	implicit def DomainToApiModule( moduleModel : Module ) : ApiModel = com.despegar.tools.bookshelf.api.dto.Module( moduleModel.id, moduleModel.name, moduleModel.parent.id, moduleModel.description )
	implicit def DomainToApiProperty( propertyModel : Property ) : ApiModel = com.despegar.tools.bookshelf.api.dto.Property( propertyModel.id, propertyModel.name, propertyModel.parent.id, propertyModel.values )

	
	// Api To Domain Implicits
	class RichApi(model : ApiModel) {
		def asDomain = ApiToDomain(model)
	}
	
	class RichApiSeq(model : Seq[ApiModel]) {
		def asDomain = model map( v => ApiToDomain(v) )
	}
	
	implicit def ApiSeqToDomainSeq(model : Seq[ApiModel]) = new RichApiSeq(model)
	implicit def ApiToRichApi(model : ApiModel) = new RichApi(model)
	
	implicit def ApiToDomain( model : ApiModel ) : MongoModel[_] = model match {
		case enviroment : com.despegar.tools.bookshelf.api.dto.Enviroment => ApiToDomainEnviroment( enviroment )
		case projectModel : com.despegar.tools.bookshelf.api.dto.Project => ApiToDomainProject( projectModel )
		case moduleModel : com.despegar.tools.bookshelf.api.dto.Module => ApiToDomainModule( moduleModel )
		case propertyModel : com.despegar.tools.bookshelf.api.dto.Property => ApiToDomainProperty( propertyModel )
		case _ => null
	}

	implicit def ApiToDomainEnviroment( enviromentModel : com.despegar.tools.bookshelf.api.dto.Enviroment ) : MongoModel[_] = enviromentModel match {
		case com.despegar.tools.bookshelf.api.dto.Enviroment( id : String, name, description ) if id.nonEmpty => {
			val enviroment = new Enviroment( name, description )
			enviroment.id = id
			enviroment
		}
		case com.despegar.tools.bookshelf.api.dto.Enviroment( _, name, description ) => new Enviroment( name, description )
	}

	implicit def ApiToDomainProject( projectModel : com.despegar.tools.bookshelf.api.dto.Project ) : MongoModel[_] = projectModel match {
		case com.despegar.tools.bookshelf.api.dto.Project( id : String, name, description ) if id.nonEmpty => {
			val project = Project.findById(id).get 
			project.name = name;
			project.description = description;
			project
		}
		case com.despegar.tools.bookshelf.api.dto.Project( _, name, description ) => new Project( name, description )
	}

	implicit def ApiToDomainModule( moduleModel : com.despegar.tools.bookshelf.api.dto.Module ) : MongoModel[_] = moduleModel match {
		case com.despegar.tools.bookshelf.api.dto.Module( id : String, name, _ , description)  if id.nonEmpty => {
			val module = Module.findById(id).get 
			module.name = name;
			module.description = description;			
			module
		}
		case com.despegar.tools.bookshelf.api.dto.Module( _, name, parentId, description ) => new Module( name, description, Project.findById(parentId).get )
	}

	implicit def ApiToDomainProperty( propertyModel : com.despegar.tools.bookshelf.api.dto.Property ) : MongoModel[_] = propertyModel match {
		case com.despegar.tools.bookshelf.api.dto.Property( id : String, name, _, values ) if id.nonEmpty => {
			val property = new Property( propertyModel.name, null, values)
			property.id = id
			property
		}
		case com.despegar.tools.bookshelf.api.dto.Property( _, name, parentId, null ) => new Property( name, Module.findById(parentId).get )
	}

}