package com.jarlakxen.bookshelf.server.service.rest

import com.jarlakxen.bookshelf.server.domain.model._
import com.jarlakxen.bookshelf.server.domain.dao._

class PropertyServlet extends RestService {

  // ++++++++++++++++++++++++++++++++++
  // 		Property RestFul Services
  // ++++++++++++++++++++++++++++++++++

  get("/:id") {
    Property.findById(params("id")).map(_.cloneWithFinalValue)
  }

  post("/") {
    val newProperty = extract[Property]
    newProperty.save
  }

  put("/") {
    val property = extract[Property]
    property.update
  }

  delete("/:id") {
    Property.findById(params("id")) match {
      case Some(property) => {
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
        property.delete
      }
      case None => false
    }

  }
}