package com.despegar.tools.bookshelf.api.dto

import scala.collection.mutable.LinkedList

case class PropertiesGroup(id : String, name: String, description: String, properties: Seq[Property] ) extends ApiModel;