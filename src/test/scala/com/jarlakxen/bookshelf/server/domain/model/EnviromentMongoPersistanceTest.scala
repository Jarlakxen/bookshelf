package com.jarlakxen.bookshelf.server.domain.model

import org.junit.runner.RunWith
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import org.specs2.mutable.Specification
import org.specs2.mutable.Before
import org.specs2.runner.JUnitRunner
import com.jarlakxen.bookshelf.server.domain.dao._

@RunWith(classOf[JUnitRunner])
class EnviromentMongoPersistanceTest extends Specification {

  implicit def context = new Before {
    def before = {
      System.setProperty("db.name", "test-bookshelf")
      MongoStore dropDatabase
    }
  }

  "Enviroment" should {

    "persist" in {
      var enviroment = Enviroment("Local", "")

      enviroment save

      var aux = Enviroment findByName "Local"

      aux match {
        case None => failure("Value not stored")
        case Some(x) => x.name must be equalTo ("Local")
      }

      Enviroment.count() must be_==(1)

      aux.get.delete

      Enviroment.count() must be_==(0)
    }
  }

}