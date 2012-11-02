package com.despegar.tools.bookshelf.api.dto

case class Property( id : String, name : String, moduleId: String, values : Map[Enviroment, String] );
