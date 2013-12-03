package com.jarlakxen.bookshelf.server.domain.model

import org.bson.types.ObjectId
import com.jarlakxen.bookshelf.server.domain.dao._

case class PropertyValue(linkEnviromentId: ObjectId = null, linkId: ObjectId = null, fixValue: String = null) {

  private def enviroment = Enviroment.findById(linkEnviromentId).get

  def value: String = {

    if (linkId != null) {
      Property.findById(linkId) match {
        case Some(property) => property.value(enviroment) match {
          case Some(value) => value.value
          case None => null
        }
        case None => null
      }
    } else {
      fixValue
    }
  }
  
  def cloneWithFinalValue = PropertyValue(linkEnviromentId, linkId, value)

}

object PropertyValue {
  
  def apply(value: String) = new PropertyValue(null, null, value);

  def apply(linkEnviroment: ObjectId, linkId: ObjectId) = new PropertyValue(linkEnviroment, linkId, null);

  def apply(enviroment: Enviroment, link: Property) = new PropertyValue(enviroment.id, link.id, null);

}