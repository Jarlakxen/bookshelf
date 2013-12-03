package com.jarlakxen.bookshelf.server.domain.dao

import com.novus.salat.dao.SalatDAO
import org.bson.types.ObjectId
import com.mongodb.casbah.commons.MongoDBObject
import scala.reflect.runtime.universe._
import scala.reflect.ClassTag
import com.mongodb.DBObject
import scala.collection.mutable.HashMap
import java.util.concurrent.atomic.AtomicInteger

abstract class BaseDAO[T <: AnyRef](implicit man: Manifest[T]) {

  private val _dao = new SalatDAO[T, ObjectId](MongoStore.collectionOf[T]) {}

  private def cast: T = this.asInstanceOf[T]

  def dao = _dao

  def id(): ObjectId

  def save: T = { _dao.insert(cast, writeConcern).get; cast }

  def update: T = { dao.update(MongoDBObject("_id" -> id()), cast, false, false, writeConcern); cast }

  def delete = _dao.remove(cast, writeConcern)
}

abstract class ServiceDAO[T <: AnyRef](implicit man: Manifest[T]) {

  private val collection = MongoStore.collectionOf[T]
  protected val _dao = new SalatDAO[T, ObjectId](collection) {}

  def dao = _dao

  private lazy val indexesDefinitions = {
    val definition = new Object with Indexes
    indexes(definition)
    definition
  }

  def indexes: Indexes => Unit

  def findById(id: ObjectId): Option[T] = _dao.findOneById(id)
  def findById(id: String): Option[T] = findById(new ObjectId(id))

  def query(fieldsCriteria: Map[String, Any]): List[T] = query(MongoDBObject(fieldsCriteria.toList))

  def query(criteria: DBObject = MongoDBObject.empty): List[T] = _dao.find(criteria).toList

  def queryOne(fieldsCriteria: Map[String, Any]): Option[T] = queryOne(MongoDBObject(fieldsCriteria.toList))

  def queryOne(criteria: DBObject): Option[T] = _dao.findOne(criteria)

  def count(fieldsCriteria: Map[String, Any]): Long = count(MongoDBObject(fieldsCriteria.toList))

  def count(criteria: DBObject = MongoDBObject.empty): Long = _dao.count(criteria)

  def deleteAll = _dao.remove(MongoDBObject.empty, writeConcern)

  def projections[P <: Any](field: String, query: DBObject = MongoDBObject.empty)(implicit m: Manifest[P]): List[P] = _dao.primitiveProjections(query, field)(m, context)

  def projection[P <: Any](field: String, query: DBObject)(implicit m: Manifest[P]): Option[P] = _dao.primitiveProjection(query, field)(m, context)

  def ensureIndexes() {
    for ((name, fields) <- indexesDefinitions.toMap) {
      collection.underlying.ensureIndex(MongoDBObject((for (index <- fields._1) yield (index, 1)).toList), name, fields._2)
    }
  }

}

trait Indexes {

  private var indexes = HashMap.empty[String, (Seq[String], Boolean)]
  private val counter = new AtomicInteger(0)

  def Unique(fields: String*) {
    create(fields.toList, true)
  }

  def Index(fields: String*) {
    create(fields.toList, false)
  }

  private def create(fields: List[String], unique: Boolean) {
    fields match {
      case head :: Nil => indexes.put("_" + head.toLowerCase, (fields, unique))
      case head :: tail => indexes.put("_complex_index" + counter.getAndIncrement(), (fields, unique))
    }
  }

  def toMap = indexes.toMap
}

trait NamedDAO[T <: AnyRef] {

	self : ServiceDAO[T] =>

	def findByName( name : String ) : Option[T] = self._dao.findOne( MongoDBObject( "name" -> name ) )

}

trait ChildDAO[T <: AnyRef] {

	self : ServiceDAO[T] =>

	def findAllByParent( parent : BaseDAO[_] ) : List[T] = findAllByParentId( parent.id )
	def findAllByParentId( parentId : ObjectId ) : List[T] = self._dao.find( MongoDBObject( "parentId" -> parentId ) ).toList

}