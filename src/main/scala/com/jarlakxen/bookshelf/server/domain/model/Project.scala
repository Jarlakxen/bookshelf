package com.jarlakxen.bookshelf.server.domain.model

import org.bson.types.ObjectId
import com.jarlakxen.bookshelf.server.domain.dao._
import scala.util._

case class Project(name: String, description: String, id: ObjectId = new ObjectId) extends BaseDAO[Project] {

  def modules = Module findAllByParent (this)

  def cleanModules = {
    for (module <- modules) {
      module.cleanProperties
      Try(module.delete)
    }
    this
  }

}

object Project extends ServiceDAO[Project] with NamedDAO[Project] {

  ensureIndexes

  def indexes = { i =>
    import i._
    Unique("name")
  }

}