package com.jarlakxen.bookshelf.server.domain.model

import org.junit.runner.RunWith
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import org.specs2.mutable.Specification
import org.specs2.mutable.Before
import org.specs2.runner.JUnitRunner
import scala.collection.immutable.Map
import org.bson.types.ObjectId
import com.jarlakxen.bookshelf.server.domain.dao._

@RunWith(classOf[JUnitRunner])
class ModuleMongoPersistanceTest extends Specification {

  implicit def context = new Before {
    def before = {
      System.setProperty("db.name", "test-bookshelf")
      MongoStore dropDatabase
    }
  }

  "Module" should {

    "persist" in {

      var module = new Module("API", "", new ObjectId);

      module save

      var aux = Module findByName "API"

      aux match {
        case None => failure("Value not stored")
        case Some(x) => {
          x.name must be equalTo ("API")
        }
      }

      Module.count() must be_==(1)

      aux.get.delete

      Module.count() must be_==(0)
    }

    "persist and add to existing project" in {

      var project = new Project("Project1", "");

      project save

      var module = Module("Module1", "", project);

      module save

      Module.count() must be_==(1)

      Project.count() must be_==(1)

      project.modules.size must be_==(1)

      module delete

      project delete

      Module.count() must be_==(0)

      Project.count() must be_==(0)
    }
  }

}