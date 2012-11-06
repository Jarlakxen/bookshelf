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
	implicit def DomainToApiModule( moduleModel : Module ) : ApiModel = com.despegar.tools.bookshelf.api.dto.Module( moduleModel.id, moduleModel.name, moduleModel.description, moduleModel.parent.id )
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
		case com.despegar.tools.bookshelf.api.dto.Enviroment( null, name, description ) => new Enviroment( name, description )
		case com.despegar.tools.bookshelf.api.dto.Enviroment( id, name, description ) => {
			val enviroment = new Enviroment( name, description )
			enviroment.id = id
			enviroment
		}
	}

	implicit def ApiToDomainProject( projectModel : com.despegar.tools.bookshelf.api.dto.Project ) : MongoModel[_] = projectModel match {
		case com.despegar.tools.bookshelf.api.dto.Project( null, name, description ) => new Project( name, description )
		case com.despegar.tools.bookshelf.api.dto.Project( id, name, description ) => {
			val project = new Project( name, description )
			project.id = id
			project
		}
	}

	implicit def ApiToDomainModule( moduleModel : com.despegar.tools.bookshelf.api.dto.Module ) : MongoModel[_] = moduleModel match {
		case com.despegar.tools.bookshelf.api.dto.Module( null, name, description, null ) => new Module( name, description )
		case com.despegar.tools.bookshelf.api.dto.Module( id, name, description, projectId ) => {
			val module = new Module( name, description )
			module.id = id
			if(projectId!=null){ 
				module.parent = Project.findById( projectId ).get
			}
			module
		}
	}

	implicit def ApiToDomainProperty( propertyModel : com.despegar.tools.bookshelf.api.dto.Property ) : MongoModel[_] = propertyModel match {
		case com.despegar.tools.bookshelf.api.dto.Property( null, name, null, null ) => new Property( name )
		case com.despegar.tools.bookshelf.api.dto.Property( id, name, moduleId, values ) => {
			val property = new Property( propertyModel.name, null, values)
			property.id = id
			property
		}
	}

}