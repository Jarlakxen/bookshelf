package com.despegar.tools.bookshelf.domain.mongo

import com.mongodb.Mongo
import org.bson.types.ObjectId
import com.google.code.morphia.{ Datastore, Morphia }
import com.google.code.morphia.dao.BasicDAO
import com.google.code.morphia.annotations.{Id, Transient, Reference, Embedded, Serialized}
import com.google.code.morphia.mapping.Mapper
import com.google.code.morphia.query.{ QueryResults, UpdateOperations, Query }
import com.google.code.morphia.logging.MorphiaLoggerFactory
import scala.Predef._
import com.google.code.morphia.logging.slf4j.SLF4JLogrImplFactory
import annotation.target.field
import com.mongodb.MongoURI
import scala.collection.JavaConversions._
import org.json4s.FieldSerializer
import org.json4s.FieldSerializer._


class DAO[T, K]( val _class : Class[T], val _datastore : Datastore ) extends BasicDAO[T, K]( _class, _datastore) {
	def this(datastore : Datastore)( implicit m : Manifest[T] ) = {
		this(m.erasure.asInstanceOf[Class[T]], datastore)
		datastore.ensureIndexes( DAO.this.getEntityClass )
	}
}

object DAO {
	
	def apply[T](_class : Class[T], _datastore : Datastore) = {
		val dao = new BasicDAO[T, ObjectId]( _class, _datastore)
		dao.ensureIndexes()
		dao;
	}
	
}

abstract class MongoModel[T]( implicit m : Manifest[T] ) {
	type ReferenceField = Reference @field
	type EmbeddedField = Embedded @field
	type TransientField = Transient @field
	type SerializedField = Serialized @field
	
	@Transient private val _clazz : Class[T] = m.erasure.asInstanceOf[Class[T]]
	@Transient protected val _dao = DAO[T]( _clazz, MongoStore.datastore )
	
	@Id var id : ObjectId = _
	
	private def cast : T = this.asInstanceOf[T]

	def getId = id
	def setId(id : ObjectId) = this.id = id
	
	protected def createQueryToFindMe : Query[T] = {
		if ( !isPersistent ) throw new IllegalStateException( "Can't perform query on myself until I have been saved!" )
		_dao.createQuery.field( Mapper.ID_KEY ).equal( id )
	}
	
	def isPersistent = id != null
	def save : T = { _dao.save( cast ); cast }
	def update( ops : UpdateOperations[T] ) { _dao.updateFirst( createQueryToFindMe, ops ) }
	def update( query : Query[T], ops : UpdateOperations[T] ) { _dao.update( query, ops ) }
	def delete = { if ( isPersistent ) _dao.delete( cast ) }
}

abstract class MongoObject[T]( implicit m : Manifest[T] ) {
	private val _clz : Class[T] = m.erasure.asInstanceOf[Class[T]]

	protected val _dao : DAO[T, ObjectId] = new DAO[T, ObjectId]( _clz, MongoStore.datastore )
	protected def createQuery = _dao.createQuery
	protected def createUpdateOperations = _dao.createUpdateOperations
	protected def update( query : Query[T], updateOperations : UpdateOperations[T] ) = _dao.update( query, updateOperations )
	

	protected def asList( q : QueryResults[T] ): Seq[T] = q.asList

	def findById( id : ObjectId ) : Option[T] = Option( _dao.get( id ) )
	def findById( id : String ) : Option[T] = findById( new ObjectId( id ) )

	def findAll = asList( _dao.find )

	def count = _dao.count

	def deleteById( id : ObjectId ) { _dao.deleteById( id ) }
	def deleteById( id : String ) { deleteById( new ObjectId( id ) ) }
	def deleteAll = _dao.deleteByQuery( _dao.createQuery )
	def drop = _dao.getCollection.drop
}

trait NamedModelObject[T] {
	
	this : MongoObject[T] =>
	
	def findByName(name: String) : Option[T] = Option(this.createQuery.field("name").equal(name).get)
	
}