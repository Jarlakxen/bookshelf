package com.despegar.tools.bookshelf.api.dto

import scala.collection.mutable._

case class Property( id : String, name : String, parentId: String, values : Map[String, String] ) extends ApiModel;
