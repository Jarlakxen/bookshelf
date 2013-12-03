package com.jarlakxen.bookshelf.server.domain.model

import org.bson.types.ObjectId
import com.jarlakxen.bookshelf.server.domain.dao._
import scala.util._

case class PropertiesGroup(name: String, description: String, id: ObjectId = new ObjectId) extends BaseDAO[PropertiesGroup] {

  def properties = Property findAllByParent this

  def cleanProperties = {
    for (property <- properties) {

      // Remove references
      Property.findLinkedWith(property).foreach { linkedProperty =>
        linkedProperty.copy(values = linkedProperty.values.mapValues { value =>
          if (value.linkId == property.id) {
            value.cloneWithFinalValue
          } else {
            value
          }
        }).update
      }

      Try(property.delete)
    }
    this
  }
}

object PropertiesGroup extends ServiceDAO[PropertiesGroup] with NamedDAO[PropertiesGroup] {

  ensureIndexes

  def indexes = { i =>
    import i._
    Unique("name")
  }

}