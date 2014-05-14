package com.jarlakxen.bookshelf.server.web.view

import org.scalatra.ScalatraServlet
import org.scalatra.scalate.ScalateSupport

class ViewsServlet extends ScalatraServlet with ScalateSupport {

  before() {
    contentType = "text/html"
  }

  get("/") {
    mustache("main", "layout" -> "")
  }

  get("/partial/:name") {
    mustache("partial/" + params("name"), "layout" -> "")
  }
}