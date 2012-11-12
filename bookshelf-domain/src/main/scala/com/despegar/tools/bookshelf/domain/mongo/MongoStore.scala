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
import com.google.code.morphia.logging.slf4j.SLF4JLogrImplFactory
import scala.annotation.target.field


trait MorphiaMongo {
	val morphia : Morphia = new Morphia()
	protected var _ds : Datastore = null

	def mapPackage( packageName : String ) : MorphiaMongo = { morphia.mapPackage( packageName ); this }
	def map[T]( clazz : Class[T] ) : MorphiaMongo = { morphia.map( clazz ); this }
	def indexes = datastore.ensureIndexes()
	def datastore = {
		if ( _ds == null ) throw new NullPointerException( "You have to initialize the DB first!" )
		_ds
	}
}

object MongoStore extends MorphiaMongo {
	def init( uri:String, database : String, reset : Boolean = false ) = {
		MorphiaLoggerFactory.reset()
		MorphiaLoggerFactory.registerLogger(classOf[SLF4JLogrImplFactory]);
		_ds = morphia.createDatastore(new Mongo(new MongoURI(uri)), database );
		if( reset ) _ds.getDB().dropDatabase()
		this
	}
}