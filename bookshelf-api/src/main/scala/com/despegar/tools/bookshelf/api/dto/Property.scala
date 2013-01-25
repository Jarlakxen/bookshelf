package com.despegar.tools.bookshelf.api.dto


case class Property( id : String, name : String, parentId: String, values : Map[String, String] ) extends ApiModel;
