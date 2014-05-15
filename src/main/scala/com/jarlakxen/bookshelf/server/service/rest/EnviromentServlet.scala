package com.jarlakxen.bookshelf.server.service.rest

import com.jarlakxen.bookshelf.server.domain.model._
import scala.util._

class EnviromentServlet extends RestService {

  get("/") {
    Enviroment.query()
  }

  get("/:id") {
    Enviroment.findById(params("id"));
  }

  post("/") {
    val newEnviroment = extract[Enviroment]

    newEnviroment.save
    
    Try(Property.addEnvironment(newEnviroment))
    
    newEnviroment
  }

  put("/") {
    val enviroment = extract[Enviroment]

    enviroment.update
  }

  delete("/:id") {
    Enviroment.findById(params("id")) match {
      case Some(enviroment) => {
        Try(Property.deleteEnvironment(enviroment))
        enviroment.delete
        true
      }
      case None => false
    }
  }
}