package com.jarlakxen.tools.bookshelf.domain.mongo

import org.bson.types.ObjectId
import scala.Predef._
import scala.annotation.target.{ setter, getter }
import com.novus.salat._
import com.novus.salat.global._
import com.novus.salat.dao.SalatDAO
import com.novus.salat.annotations.raw.{ Ignore, Key, Persist, Salat }
import com.mongodb.casbah.MongoConnection
import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.WriteConcern

object DAO {

	implicit val ctx = new Context {
		val name = "Always-TypeHint-Context"

		override val typeHintStrategy = StringTypeHintStrategy( when = TypeHintFrequency.Always, typeHint = "_class" )
	}

	ctx.registerGlobalKeyOverride( remapThis = "id", toThisInstead = "_id" )

	def apply[T <: AnyRef]( implicit m : Manifest[T] ) = {
		val collection = MongoStore.Collection( m.erasure.getSimpleName() )
		//MongoConnection()( "bookshelf" )( m.erasure.getSimpleName() )

		new SalatDAO[T, ObjectId]( collection = collection ) {}
	}
}

@Salat
abstract class MongoModel[T <: AnyRef]( implicit m : Manifest[T] ) {

	import DAO._

	private val _clazz : Class[T] = m.erasure.asInstanceOf[Class[T]]
	private val _dao = DAO[T]

	private def cast : T = this.asInstanceOf[T]

	def dao = _dao

	def id : ObjectId
	def id_=( id : ObjectId )

	def isPersistent = id != null
	def save : T = { id_=(_dao.insert( cast ).get); cast }
	def update : T = { dao.update( MongoDBObject( "_id" -> id ), cast, false, false, WriteConcern.FsyncSafe ); cast }
	def saveOrUpdate : T = id match {
		case null => save
		case _ => update
	}
	def delete = { if ( isPersistent ) _dao.remove( cast, WriteConcern.FsyncSafe ) }
}

abstract class MongoObject[T <: AnyRef]( implicit m : Manifest[T] ) {

	import DAO._

	private val _clazz : Class[T] = m.erasure.asInstanceOf[Class[T]]
	protected val _dao = DAO[T]

	def findById( id : ObjectId ) = _dao.findOneById( id )
	def findById( id : String ) = _dao.findOneById( new ObjectId( id ) )

	def findAll = _dao.find( MongoDBObject.empty ).toList

	def count = _dao.count( MongoDBObject.empty )

	def deleteAll = _dao.remove( MongoDBObject.empty )
}

trait NamedDAO[T <: AnyRef] {

	this : MongoObject[T] =>

	def findByName( name : String ) : Option[T] = this._dao.findOne( MongoDBObject( "name" -> name ) )

}

trait ChildDAO[T <: AnyRef] {

	this : MongoObject[T] =>

	def findAllByParent( parent : MongoModel[_] ) : List[T] = findAllByParentId( parent.id )
	def findAllByParentId( parentId : ObjectId ) : List[T] = this._dao.find( MongoDBObject( "parentId" -> parentId ) ).toList

}