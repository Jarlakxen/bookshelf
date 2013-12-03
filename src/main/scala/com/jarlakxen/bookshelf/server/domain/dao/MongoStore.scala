package com.jarlakxen.bookshelf.server.domain.dao

import scala.collection.mutable.Map
import com.mongodb.casbah.Imports._
import com.mongodb.ServerAddress
import com.mongodb.casbah.{ MongoDB, MongoOptions, MongoConnection }
import com.jarlakxen.bookshelf.server.utils.Config

object MongoStore {

  private lazy val MongoDBCollections = connect(Config.getString("mongodb.default.db", "cars-marx"))

  private val collections: Map[String, MongoCollection] = Map()

  private def connect(name: String): MongoDB = {

    val opts = MongoOptions(autoConnectRetry = true,
      connectionsPerHost = Config.getInt("mongodb.default.options.connectionsPerHost", 50))

    val servers = Config.getString("mongodb.default.servers", "localhost:27017").split(";").map({ server =>
      server.trim.split(":").toList match {
        case host :: port :: Nil => new ServerAddress(host, port.toInt)
        case host :: Nil => new ServerAddress(host)
        case unknown => throw new IllegalArgumentException(unknown.toString)
      }
    }).toList

    val connection = MongoConnection(servers, opts)(name)

    if (Config.hasKey("mongodb.default.user") && Config.hasKey("mongodb.default.password")) {
      connection.authenticate(Config.getString("mongodb.default.user"), Config.getString("mongodb.default.password"));
    }

    connection.setWriteConcern(writeConcern)

    connection
  }

  def dropDatabase = MongoDBCollections dropDatabase

  def collectionOf[T <: AnyRef](implicit man: Manifest[T]): MongoCollection = collectionOf(man.erasure.getSimpleName())

  def collectionOf(name: String): MongoCollection = this.synchronized {

    collections.get(name) match {
      case Some(value) => value

      case None => {
        val newCollection = MongoDBCollections(name)
        collections(name) = newCollection
        newCollection
      }
    }
  }

}