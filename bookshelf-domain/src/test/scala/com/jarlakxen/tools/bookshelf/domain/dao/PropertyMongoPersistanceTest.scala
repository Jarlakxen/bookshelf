package com.jarlakxen.tools.bookshelf.domain.dao

import org.junit.runner.RunWith
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import org.specs2.mutable.Specification
import org.specs2.mutable.Before
import org.specs2.runner.JUnitRunner
import com.jarlakxen.tools.bookshelf.domain.dto._
import scala.collection.mutable.Map
import org.bson.types.ObjectId
import com.jarlakxen.tools.bookshelf.domain.mongo.MongoStore

@RunWith( classOf[JUnitRunner] )
class PropertyMongoPersistanceTest extends Specification {

	implicit def context = new Before {
		def before = {
			System.setProperty( "db.name", "test-bookshelf" )
			MongoStore dropDatabase
		}
	}

	"Property" should {

		"persist" in {

			val project = new Project( "P1", "" )
			val module = Module( "M1", "", project )
			//module.id = new ObjectId

			val localEnviroment = Enviroment( "Local", "" );
			val prodEnviroment = Enviroment( "Prod", "" );

			var property = Property( "test.key1", module, Map( localEnviroment -> PropertyValue("10"), prodEnviroment -> PropertyValue("20") ) );

			property save

			var aux = Property findByName "test.key1"

			aux match {
				case None => failure( "Value not stored" )
				case Some( x ) => {
					x.name must be equalTo ( "test.key1" )

					x.value( localEnviroment ) must be equalTo ( Some(PropertyValue("10")) )
					x.value( prodEnviroment ) must be equalTo ( Some(PropertyValue("20")) )
				}
			}

			Property.count must be_==( 1 )

			property delete

			Property.count must be_==( 0 )
		}

		"remove enviroment" in {

			val project = new Project( "P1", "" )
			val module = Module( "M1", "", project )
			//module.id = new ObjectId

			val localEnviroment = Enviroment( "Local", "" );
			val prodEnviroment = Enviroment( "Prod", "" );

			var property1 = Property( "test.key1", module, Map( localEnviroment -> PropertyValue("10"), prodEnviroment -> PropertyValue("20") ) ).save
			var property2 = Property( "test.key2", module, Map( localEnviroment -> PropertyValue("15"), prodEnviroment -> PropertyValue("25") ) ).save

			Property deleteAllFromEnvironment ( prodEnviroment )

			val properties = Property findAll

			//property1.delete
			//property2.delete

			properties must not beNull

			properties.forall( v => v.values must not haveKey ( "Prod" ) )

		}

	}

}