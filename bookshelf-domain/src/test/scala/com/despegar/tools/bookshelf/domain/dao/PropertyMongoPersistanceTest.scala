package com.despegar.tools.bookshelf.domain.dao

import org.junit.runner.RunWith
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import org.specs2.mutable.Specification
import org.specs2.mutable.Before
import org.specs2.runner.JUnitRunner
import com.despegar.tools.bookshelf.domain.dto._
import com.despegar.tools.bookshelf.domain.mongo.MongoStore
import scala.collection.immutable.Map

@RunWith( classOf[JUnitRunner] )
class PropertyMongoPersistanceTest extends Specification {
	
    val context = new Before { def before = MongoStore.init("mongodb://localhost", "test") }
	
	"Property" should {

		"persist" in context {
			
			val localEnviroment = Enviroment("Local", "");
			val prodEnviroment = Enviroment("Prod", "");
			
			var property = new Property("test.key1", Map( localEnviroment -> "10", prodEnviroment -> "20" ));
			
			property save
			
			var aux = Property findByName "test.key1"
			
			aux match {
				case None => failure("Value not stored")
				case Some(x) => {
					x.name must be equalTo( "test.key1" )
					
					x.value(localEnviroment) must be equalTo( "10" )
					x.value(prodEnviroment) must be equalTo( "20" )	
				}
			}
			
			Property.count must be_==( 1 )
			
			property delete

			Property.count must be_==( 0 )
		}
		
		"remove enviroment" in context {
			
			val localEnviroment = Enviroment("Local", "");
			val prodEnviroment = Enviroment("Prod", "");
			
			var property1 = new Property("test.key1", Map( localEnviroment -> "10", prodEnviroment -> "20" )).save
			var property2 = new Property("test.key2", Map( localEnviroment -> "15", prodEnviroment -> "25" )).save
			
			Property deleteEnvironmentFromAll("Prod")
			
			val properties = Property findAll
			
			property1.delete
			property2.delete
			
			properties must not beNull
			
			properties.forall( v => v.values.asScala must not haveKey("Prod"))
			
		}
	}

}