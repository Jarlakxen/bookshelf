package com.despegar.tools.bookshelf.domain.mongo

import scala.collection.mutable.Map
import com.mongodb.casbah.Imports._
import com.mongodb.{ ServerAddress, Mongo }
import com.mongodb.casbah.{ MongoDB, MongoOptions, MongoConnection }

object MongoStore {

	val configurationFile : String = "mongodb.properties"

	private lazy val Configuration = Nil

	private lazy val MongoDBCollections = connect( "bookshelf" )

	private val collections : Map[String, MongoCollection] = Map()

	private def connect( name : String ) : MongoDB = {
		val opts = MongoOptions( autoConnectRetry = true, connectionsPerHost = 50 )
		new MongoConnection( new Mongo( new ServerAddress( "localhost", 27017 ), opts ) )( name )
	}

	def Collection( name : String ) = this.synchronized {

		var collection = collections.get( name )

		collection match {
			case Some(value) => value
			
			case None => {
					val newCollection = MongoDBCollections( name )
					collections( name ) = newCollection
					newCollection
			}
		}
	}
}