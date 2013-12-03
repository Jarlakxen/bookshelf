package com.jarlakxen.bookshelf.server.service.rest

import com.jarlakxen.bookshelf.server.domain.model._
import com.jarlakxen.bookshelf.server.domain.dao._
import scala.util._

class ProjectServlet extends RestService {

  // ++++++++++++++++++++++++++++++++++
  // 		Project RestFul Services
  // ++++++++++++++++++++++++++++++++++

  get("/") {
    Project.query()
  }

  get("/:id") {
    Project.findById(params("id"))
  }

  post("/") {
    val newProject = extract[Project]

    newProject.save
  }

  put("/") {
    val project = extract[Project]

    project.update
  }

  delete("/:id") {

    Project.findById(params("id")) match {
      case Some(project) => {
        project.cleanModules
        project.delete
        true
      }
      case None => false
    }
  }

  // ++++++++++++++++++++++++++++++++++
  // 		Modules RestFul Services
  // ++++++++++++++++++++++++++++++++++

  get("/:projectId/modules") {
    Module.findAllByParentId(params("projectId"))
  }
}