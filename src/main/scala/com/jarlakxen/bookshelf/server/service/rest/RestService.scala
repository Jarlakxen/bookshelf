package com.jarlakxen.bookshelf.server.service.rest

import org.scalatra.ScalatraServlet
import org.scalatra.scalate.ScalateSupport
import javax.servlet.ServletConfig
import org.scalatra.{ ScalatraServlet, GZipSupport, FutureSupport, AsyncResult, InternalServerError }
import org.scalatra.json.JacksonJsonSupport
import org.json4s.{ Formats, DefaultFormats, Serializer }
import org.json4s.ext.JodaTimeSerializers
import org.bson.types.ObjectId
import org.json4s._
import java.io.{ StringWriter, PrintWriter }

trait RestService extends ScalatraServlet with ScalateSupport with JacksonJsonSupport with GZipSupport {

  protected implicit val jsonFormats: Formats = DefaultFormats.withDouble ++ JodaTimeSerializers.all + ObjectIdSerializer + BigDecimalSerializer

  before() {
    contentType = formats("json")
  }

  def extract[A: Manifest]: A = {
    try {
      parse(request.body).extract[A]
    } catch {
      case ex: MappingException => throw ex.copy(msg = ex.msg + ". Body:" + request.body)
    }
  }
  private def EnumSerializers(enums: Enumeration*) = enums.map(new org.json4s.ext.EnumNameSerializer(_))

  error {
    case t: Throwable => {
      val stringWriter = new StringWriter();
      t.printStackTrace(new PrintWriter(stringWriter));
      InternalServerError(stringWriter.toString)
    }
  }

}

object ObjectIdSerializer extends Serializer[ObjectId] {
  val ObjectIdClass = classOf[ObjectId]

  def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), ObjectId] = {
    case (TypeInfo(ObjectIdClass, _), JString(v)) => new ObjectId(v)
  }
  def serialize(implicit formats: Formats): PartialFunction[Any, JValue] = {
    case x: ObjectId => JString(x.toString())
  }
}

object BigDecimalSerializer extends Serializer[BigDecimal] {
  val BigDecimalClass = classOf[BigDecimal]

  def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), BigDecimal] = {
    case (TypeInfo(BigDecimalClass, _), JString(v)) => BigDecimal(v)
    case (TypeInfo(BigDecimalClass, _), JInt(v)) => BigDecimal(v)
    case (TypeInfo(BigDecimalClass, _), JNull) => BigDecimal(0)
  }
  def serialize(implicit formats: Formats): PartialFunction[Any, JValue] = {
    case v: BigDecimal => JString(v.bigDecimal.toPlainString())
  }
}