package com.jarlakxen.tools.bookshelf.api.dto

import scala.Enumeration

case class Property( id : String, name : String, parentId: String, values : Map[String, PropertyValue] = Map()) extends ApiModel;