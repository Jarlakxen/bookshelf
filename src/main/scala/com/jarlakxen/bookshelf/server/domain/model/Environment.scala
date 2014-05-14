package com.jarlakxen.bookshelf.server.domain.model

import org.bson.types.ObjectId
import com.jarlakxen.bookshelf.server.domain.dao._

case class Enviroment(name: String, description: String, id: ObjectId = new ObjectId) extends BaseDAO[Enviroment] {

}

object Enviroment extends ServiceDAO[Enviroment] with NamedDAO[Enviroment] {
  
  ensureIndexes
  
  def indexes = { i =>
    import i._
    Unique("name")
  }
  
}