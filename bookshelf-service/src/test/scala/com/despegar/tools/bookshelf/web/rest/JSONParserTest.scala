package com.despegar.tools.bookshelf.web.rest

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
import com.despegar.tools.bookshelf.api.dto.Enviroment

@RunWith( classOf[JUnitRunner] )
class JSONParserTest extends Specification {
	
	protected implicit val jsonFormats : Formats = DefaultFormats
	
	"JsonMethods" should {
		
		"serializate Enviroment" in {
			
			val env = Enviroment("", "LOCAL", "a");
			
			val value = JsonMethods.parse("{\"id\":\"\",\"name\":\"LOCAL\",\"description\":\"a\"}");
			
			value.extract[com.despegar.tools.bookshelf.api.dto.Enviroment] must be equalTo env			
		}
	}

}