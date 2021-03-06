package com.jarlakxen.bookshelf.server.domain.model

import org.bson.types.ObjectId
import com.jarlakxen.bookshelf.server.domain.dao._

case class Property(name: String, parentId: ObjectId, values: Map[String, PropertyValue] = Map(), id: ObjectId = new ObjectId) extends BaseDAO[Property] {

  def value(enviroment: Enviroment) = this.values.get(enviroment.id)

  def cloneWithFinalValue = this.copy(values = this.values.mapValues(_.cloneWithFinalValue))

}

object Property extends ServiceDAO[Property] with NamedDAO[Property] with ChildDAO[Property] {

  ensureIndexes

  def indexes = { i =>
    import i._
    Index("parentId")
    Index("name")
  }

  def apply(name: String, parent: BaseDAO[_ <: AnyRef], values: Map[Enviroment, PropertyValue]): Property = {
    Property(name, parent.id, values.map(entry => (entry._1.id.toString, entry._2)))
  }

  def findLinkedWith(property: Property) = Property.query().filter(_.values.exists(_._2.linkId == property.id))

  def addEnvironment(newEnviroment: Enviroment) {
    this.synchronized {
      Property.query().par.foreach { propery =>
        propery.copy(values = propery.values ++ Map(newEnviroment.id.toString -> PropertyValue())).update
      }
    }
  }

  def deleteEnvironment(enviroment: Enviroment) {
    this.synchronized {
      Property.query().par.foreach { propery =>
        propery.copy(values = (propery.values - enviroment.id)).update
      }
    }
  }
}