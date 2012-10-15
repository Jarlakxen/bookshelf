import org.scalatra._
import javax.servlet.ServletContext
import com.despegar.tools.bookshelf.web.view.HomeServlet
import com.despegar.tools.bookshelf.web.rest._
import com.despegar.tools.bookshelf.domain.mongo.MongoStore

class Scalatra extends LifeCycle {

	override def init(context: ServletContext) {

		MongoStore.init("mongodb://localhost", "test")
		
		// Main Page
		context.mount(new HomeServlet, "/bookshelf/*")
		
		// REST Services
		context.mount(new ProjectServlet, "/bookshelf/rest/*")
		context.mount(new EnviromentServlet, "/bookshelf/rest/*")
	}
}
