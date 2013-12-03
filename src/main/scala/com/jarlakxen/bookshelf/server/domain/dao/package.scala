package com.jarlakxen.bookshelf.server.domain

import org.bson.types.ObjectId
import com.novus.salat._
import com.mongodb.casbah.WriteConcern
import com.novus.salat.transformers.CustomTransformer
import com.mongodb.DBObject
import com.mongodb.casbah.commons.MongoDBObject


package object dao {
  com.mongodb.casbah.commons.conversions.scala.RegisterConversionHelpers()
  com.mongodb.casbah.commons.conversions.scala.RegisterJodaTimeConversionHelpers()

  implicit val writeConcern = WriteConcern.Safe

  implicit val context: Context = {

    val context = new Context {
      val name = "Always-TypeHint-Context"

      override val typeHintStrategy = StringTypeHintStrategy(when = TypeHintFrequency.Always, typeHint = "_class")
    }

    context.registerGlobalKeyOverride(remapThis = "id", toThisInstead = "_id")

    context
  }

  implicit def ObjectIdToString(id: ObjectId): String = id match {
    case value if value != null => value.toString()
    case _ => null
  }

  implicit def StringToObjectId(id: String): ObjectId = id match {
    case value if value != null && value.nonEmpty => new ObjectId(value)
    case _ => null
  }
}