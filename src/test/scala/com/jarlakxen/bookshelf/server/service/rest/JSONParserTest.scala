package com.jarlakxen.bookshelf.server.service.rest

import org.junit.runner.RunWith
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import org.specs2.mutable.Specification
import org.specs2.mutable.Before
import org.specs2.runner.JUnitRunner
import org.scalatra.json._
import org.json4s.jackson.JsonMethods
import org.json4s.Extraction
import org.json4s.{ DefaultFormats, Formats }
import scala.collection.immutable.Map
import org.json4s.jackson.Serialization.{read, write => swrite}
import com.jarlakxen.bookshelf.server.domain.model._
import com.jarlakxen.bookshelf.server.domain.dao._
import org.bson.types.ObjectId

@RunWith( classOf[JUnitRunner] )
class JSONParserTest extends Specification {
	
	protected implicit val jsonFormats : Formats = DefaultFormats
	
	"JsonMethods" should {
		
		"deserializate Property" in {
			
			val env = Enviroment("", "LOCAL", "a");
			
			val property = Property("1", "Test", Map("LOCAL"->PropertyValue("", "", "Value1")));
			
			val value = swrite(property);
			
			read[Property](value) must be equalTo property			
		}
		
		"serializate Property" in {
			
			val property = Property("", "key3", Map("Local"->PropertyValue("", "", "value3")));
			
			val value = JsonMethods.parse("{\"id\":\"\",\"name\":\"key3\",\"parentId\":\"512a852a84ae2776bf15dc03\",\"values\":{\"Local\":{\"linkEnviromentId\":\"\",\"linkId\":\"\",\"value\":\"value3\"}}}");
			
			value.extract[Property] must be equalTo property
			
			val domainProperty = value.extract[Property]
			
			domainProperty.id must beNull
			domainProperty.name must be equalTo "key3"
			domainProperty.parentId must be equalTo new ObjectId("512a852a84ae2776bf15dc03")
			domainProperty.values.size must be equalTo 1
			
			domainProperty.save
			
			domainProperty.id must not beNull
		}
		
		"deserializate Enviroment" in {
			
			val env = Enviroment("", "LOCAL", "a");
			
			val value = swrite(env);
			
			read[Enviroment](value) must be equalTo env			
		}
		
		"serializate Enviroment" in {
			
			val env = Enviroment("", "LOCAL", "a");
			
			val value = JsonMethods.parse("{\"id\":\"\",\"name\":\"LOCAL\",\"description\":\"a\"}");
			
			value.extract[Enviroment] must be equalTo env			
		}
	}

}