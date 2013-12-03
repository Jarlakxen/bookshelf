package com.jarlakxen.bookshelf.server.utils

import com.typesafe.config.ConfigFactory
import scala.util._

object Config {

  val content = ConfigFactory.load()

  def hasKey( key: String ) = Try( content.getAnyRef( key ) ) match {
    case Success( _ ) => true
    case _ => false
  }

  def onKey( key: String )( f: AnyRef => Unit ) = Try( content.getAnyRef( key ) ) match {
    case Success( v ) => f( v )
    case _ => Unit
  }

  def getOptionString( key: String ) = Try( content.getString( key ) ).toOption
  def getString( key: String, default: String ) = getOptionString( key ) getOrElse default
  def getString( key: String ) = content.getString( key )

  def getOptionStringList( key: String, sep: String = "," ): Option[List[String]] = getOptionString( key ) match {
    case Some( o ) => Some( o.split( sep ).map( _.trim ).toList )
    case _ => None
  }
  def getStringList( key: String, default: List[String] ): List[String] = getOptionStringList( key ) getOrElse default
  def getStringList( key: String, sep: String = "," ) = content.getString( key ).split( sep ).map( _.trim ).toList

  def getOptionInt( key: String ) = Try( content.getInt( key ) ).toOption
  def getInt( key: String, default: Int ) = getOptionInt( key ) getOrElse default
  def getInt( key: String ) = content.getInt( key )

  def getOptionLong( key: String ) = Try( content.getLong( key ) ).toOption
  def getLong( key: String, default: Long ) = getOptionLong( key ) getOrElse default
  def getLong( key: String ) = content.getLong( key )

  def getOptionBoolean( key: String ) = Try( content.getBoolean( key ) ).toOption
  def getBoolean( key: String, default: Boolean ) = getOptionBoolean( key ) getOrElse default
  def getBoolean( key: String ) = content.getBoolean( key )

}