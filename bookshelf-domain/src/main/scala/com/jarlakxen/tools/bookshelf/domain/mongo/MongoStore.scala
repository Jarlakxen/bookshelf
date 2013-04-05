package com.jarlakxen.tools.bookshelf.domain.mongo

import scala.collection.mutable.Map
import com.mongodb.casbah.Imports._
import com.mongodb.{ ServerAddress, Mongo }
import com.mongodb.casbah.{ MongoDB, MongoOptions, MongoConnection }
import com.jarlakxen.tools.bookshelf.domain.utils.Config

object MongoStore {

	private lazy val MongoDBCollections = connect( "bookshelf" )

	private val collections : Map[String, MongoCollection] = Map()

	private def connect( name : String ) : MongoDB = {

		val opts = MongoOptions( autoConnectRetry = true, connectionsPerHost = Config.getInt("db.connectionsPerHost") )
		
		val connection = new MongoConnection( new Mongo( new ServerAddress( Config.getString("db.host"), Config.getInt("db.port") ), opts ) )( name )
		
		if( Config.hasKey("db.user") && Config.hasKey("db.password") ){
			connection.authenticate(Config.getString("db.user"), Config.getString("db.password"));
		}
		
		connection.setWriteConcern(WriteConcern.FsyncSafe)
		
		connection
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