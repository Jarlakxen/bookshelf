package com.despegar.tools.bookshelf.domain.dao

import org.junit.runner.RunWith
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import org.specs2.mutable.Specification
import org.specs2.mutable.Before
import org.specs2.runner.JUnitRunner
import com.despegar.tools.bookshelf.domain.dto._
import com.despegar.tools.bookshelf.domain.mongo.MongoStore

@RunWith( classOf[JUnitRunner] )
class ProjectMongoPersistanceTest extends Specification {
	
    val context = new Before { def before = MongoStore.init("mongodb://localhost", "test") }
	
	"Project" should {

		"persist" in context {
			var project = new Project("Cars", "")
			
			project save
			
			var aux = Project findByName "Cars"
			
			aux match {
				case None => failure("Value not stored")
				case Some(x) => x.name must be equalTo( "Cars" )
			}
			
			Project.count must be_==( 1 )
			
			project delete

			Project.count must be_==( 0 )
		}
	}

}