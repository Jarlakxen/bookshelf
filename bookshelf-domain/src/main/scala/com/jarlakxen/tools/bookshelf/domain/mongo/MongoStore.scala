package com.jarlakxen.tools.bookshelf.domain.mongo

import scala.collection.mutable.Map
import com.mongodb.casbah.Imports._
import com.mongodb.{ ServerAddress, Mongo }
import com.mongodb.casbah.{ MongoDB, MongoOptions, MongoConnection }
import com.typesafe.config.ConfigFactory

object MongoStore {

	private lazy val config = ConfigFactory.load()

	private lazy val MongoDBCollections = connect( "bookshelf" )

	private val collections : Map[String, MongoCollection] = Map()

	private def connect( name : String ) : MongoDB = {
		val opts = MongoOptions( autoConnectRetry = true, connectionsPerHost = config.getInt("db.connectionsPerHost") )
		new MongoConnection( new Mongo( new ServerAddress( config.getString("db.host"), config.getInt("db.port") ), opts ) )( name )
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