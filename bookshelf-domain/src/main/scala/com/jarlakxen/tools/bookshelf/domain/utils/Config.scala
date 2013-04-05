package com.jarlakxen.tools.bookshelf.domain.utils

import com.typesafe.config.ConfigFactory

object Config {
	
	private val conf = ConfigFactory.load()

	def hasKey( key : String ) = ExceptionOption( conf.getAnyRef(key) )  match {
		case Some( _ ) => true
		case _ => false
	}
	
	def getOptionString( key : String ) = ExceptionOption( conf.getString( key ) )
	def getString( key : String, default : String ) = getOptionString( key ) getOrElse default
	def getString( key : String ) = conf.getString( key )

	def getOptionStringList( key : String, sep : String = "," ) : Option[List[String]] = getOptionString( key ) match {
		case Some( o ) => Some( o.split( sep ).map( _.trim ).toList )
		case _ => None
	}
	def getStringList( key : String, default : List[String] ) : List[String] = getOptionStringList( key ) getOrElse default
	def getStringList( key : String, sep : String = "," ) = conf.getString( key ).split( sep ).map( _.trim ).toList

	def getOptionInt( key : String ) = ExceptionOption( conf.getInt( key ) )
	def getInt( key : String, default : Int ) = getOptionInt( key ) getOrElse default
	def getInt( key : String ) = conf.getInt( key )

	def getOptionBoolean( key : String ) = ExceptionOption( conf.getBoolean( key ) )
	def getBoolean( key : String, default : Boolean ) = getOptionBoolean( key ) getOrElse default
	def getBoolean( key : String ) = conf.getBoolean( key )
	
}