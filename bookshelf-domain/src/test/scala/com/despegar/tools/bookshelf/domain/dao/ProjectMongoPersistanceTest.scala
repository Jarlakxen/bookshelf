package com.despegar.tools.bookshelf.domain.dao

import org.junit.runner.RunWith
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import org.specs2.mutable.Specification
import org.specs2.mutable.Before
import org.specs2.runner.JUnitRunner
import com.despegar.tools.bookshelf.domain.dto._

@RunWith( classOf[JUnitRunner] )
class ProjectMongoPersistanceTest extends Specification {

	"Project" should {

		"persist and delete" in {
			var project = new Project( "Cars", "" )

			project save

			var aux = Project findByName "Cars"

			aux match {
				case None => failure( "Value not stored" )
				case Some( x ) => x.name must be equalTo ( "Cars" )
			}

			Project.count must be_==( 1 )

			project delete

			Project.count must be_==( 0 )
		}

		"update" in {
			var project = new Project( "Cars", "" )

			project save

			project name = "Cars2"

			project update

			var aux = Project findByName "Cars2"

			aux match {
				case None => failure( "Value not stored" )
				case Some( x ) => x.name must be equalTo ( "Cars2" )
			}

			Project.count must be_==( 1 )

			aux.get.delete

			Project.count must be_==( 0 )
		}

		"deleteAll" in {
			new Project( "Cars1", "" ) save

			new Project( "Cars2", "" ) save

			new Project( "Cars3", "" ) save

			Project.count must be_==( 3 )

			Project deleteAll

			Project.count must be_==( 0 )
		}
	}

}