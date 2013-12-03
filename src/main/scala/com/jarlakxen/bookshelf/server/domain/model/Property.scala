package com.jarlakxen.bookshelf.server.domain.model

import org.bson.types.ObjectId
import com.jarlakxen.bookshelf.server.domain.dao._

case class Property(name: String, parentId: ObjectId, values: Map[String, PropertyValue] = Map(), id: ObjectId = new ObjectId) extends BaseDAO[Property] {

  def value(enviroment: Enviroment) = this.values.get(enviroment.id)

}

object Property extends ServiceDAO[Property] with NamedDAO[Property] with ChildDAO[Property] {

  ensureIndexes

  def indexes = { i =>
    import i._
    Index("parentId")
    Unique("name")
  }

  def apply(name: String, parent: BaseDAO[_ <: AnyRef], values: Map[Enviroment, PropertyValue]): Property = {
    Property(name, parent.id, values.map(entry => (entry._1.id.toString, entry._2)))
  }

  def findLinkedWith(property: Property) = Property.query().filter(_.values.exists(_._2.linkId == property.id))

  def deleteEnvironment(enviroment: Enviroment) {
    this.synchronized {
      Property.query().foreach { propery =>
        propery.copy(values = (propery.values - enviroment.id)).update
      }
    }
  }
}