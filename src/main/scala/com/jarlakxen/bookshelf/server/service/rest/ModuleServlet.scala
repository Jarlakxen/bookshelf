package com.jarlakxen.bookshelf.server.service.rest

import com.jarlakxen.bookshelf.server.domain.model._
import com.jarlakxen.bookshelf.server.domain.dao._
import scala.util._

class ModuleServlet extends RestService {

  // ++++++++++++++++++++++++++++++++++
  // 		Module RestFul Services
  // ++++++++++++++++++++++++++++++++++

  
  get("/:id") {
    Module.findById(params("id"));
  }

  post("/") {
    val newModule = extract[Module]

    newModule.save
  }

  put("/") {
    val module = extract[Module]

    module.update
  }

  delete("/:id") {
    val module = Module.findById(params("id")) match {
      case Some(module) => {
        module.cleanProperties
        module.delete
        true
      }
      case None => false
    }
  }

  // ++++++++++++++++++++++++++++++++++
  // 		Properties RestFul Services
  // ++++++++++++++++++++++++++++++++++

  get("/:moduleId/properties") {
    Property.findAllByParentId(params("moduleId"))
  }
}