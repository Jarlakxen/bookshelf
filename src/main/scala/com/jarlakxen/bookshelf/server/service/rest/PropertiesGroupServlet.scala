package com.jarlakxen.bookshelf.server.service.rest

import com.jarlakxen.bookshelf.server.domain.model._
import com.jarlakxen.bookshelf.server.domain.dao._

class PropertiesGroupServlet extends RestService {

  // ++++++++++++++++++++++++++++++++++
  // 		Properties Group RestFul Services
  // ++++++++++++++++++++++++++++++++++

  get("/") {
    PropertiesGroup.query()
  }

  get("/:id") {
    PropertiesGroup.findById(params("id"))
  }

  post("/") {
    val newPropertiesGroup = extract[PropertiesGroup]
    newPropertiesGroup.save
  }

  put("/") {
    val propertiesGroup = extract[PropertiesGroup]
    propertiesGroup.update
  }

  delete("/:id") {

    PropertiesGroup.findById(params("id")) match {
      case Some(propertiesGroup) => {
        propertiesGroup.cleanProperties
        propertiesGroup.delete
        true
      }
      case None => false
    }
  }

  // ++++++++++++++++++++++++++++++++++
  // 		Properties RestFul Services
  // ++++++++++++++++++++++++++++++++++

  get("/:propertiesGroupId/properties") {
    Property.findAllByParentId(params("propertiesGroupId"))
  }
}