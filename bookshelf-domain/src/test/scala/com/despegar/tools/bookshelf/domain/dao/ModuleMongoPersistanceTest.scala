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
class ModuleMongoPersistanceTest extends Specification {
	
    val context = new Before { 
    	def before = {
    		MongoStore.init("mongodb://localhost", "test", true)
    	}
    }
	
	"Module" should {

		"persist" in context {
			
			var module = new Module("API", "", null);
			
			module save
			
			var aux = Module findByName "API"
			
			aux match {
				case None => failure("Value not stored")
				case Some(x) => {
					x.name must be equalTo( "API" )
				}
			}
			
			Module.count must be_==( 1 )
			
			module delete

			Module.count must be_==( 0 )
		}
		
		"persist and add to existing project" in context {
			
			var project = new Project("Project1", "");
			
			project save
			
			var module = new Module("Module1", "", project);
			
			module save
			
			module delete
			
			project delete

			Module.count must be_==( 0 )
			
			Project.count must be_==( 0 )
		}
	}

}