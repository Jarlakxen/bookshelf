package com.despegar.tools.bookshelf.web.rest

import org.junit.runner.RunWith
import scala.collection.mutable.Map
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import org.specs2.mutable.Specification
import org.specs2.mutable.Before
import org.specs2.runner.JUnitRunner
import org.scalatra.json._
import org.json4s.jackson.JsonMethods
import org.json4s.Extraction
import org.json4s.{ DefaultFormats, Formats }
import com.despegar.tools.bookshelf.api.dto.Enviroment
import org.json4s.jackson.Serialization.{read, write => swrite}
import com.despegar.tools.bookshelf.api.dto.Property

@RunWith( classOf[JUnitRunner] )
class JSONParserTest extends Specification {
	
	protected implicit val jsonFormats : Formats = DefaultFormats
	
	"JsonMethods" should {
		
		"deserializate Property" in {
			
			val property = Property("1", "Test", "", Map("Key1"->"Value1"));
			
			val value = swrite(property);
			
			read[com.despegar.tools.bookshelf.api.dto.Property](value) must be equalTo property			
		}
		
		"deserializate Enviroment" in {
			
			val env = Enviroment("", "LOCAL", "a");
			
			val value = swrite(env);
			
			read[com.despegar.tools.bookshelf.api.dto.Enviroment](value) must be equalTo env			
		}
		
		"serializate Enviroment" in {
			
			val env = Enviroment("", "LOCAL", "a");
			
			val value = JsonMethods.parse("{\"id\":\"\",\"name\":\"LOCAL\",\"description\":\"a\"}");
			
			value.extract[com.despegar.tools.bookshelf.api.dto.Enviroment] must be equalTo env			
		}
	}

}