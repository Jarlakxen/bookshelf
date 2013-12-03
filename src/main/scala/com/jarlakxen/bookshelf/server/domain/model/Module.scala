package com.jarlakxen.bookshelf.server.domain.model

import org.bson.types.ObjectId
import com.jarlakxen.bookshelf.server.domain.dao._
import scala.util._

case class Module(name: String, description: String, parentId: ObjectId, id: ObjectId = new ObjectId) extends BaseDAO[Module] {

  lazy val parent = Project.findById(parentId).get

  def properties = Property findAllByParent this

  def cleanProperties = {
    for (property <- properties) Try(property.delete)
    this
  }

}

object Module extends ServiceDAO[Module] with NamedDAO[Module] with ChildDAO[Module] {

  ensureIndexes

  def indexes = { i =>
    import i._
    Index("parentId")
    Unique("name")
  }

  def apply(name: String, description: String, parent: Project): Module = Module(name, description, parent.id)

}