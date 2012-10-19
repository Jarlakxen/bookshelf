import org.scalatra._
import javax.servlet.ServletContext
import com.despegar.tools.bookshelf.web.view.HomeServlet
import com.despegar.tools.bookshelf.web.rest._
import com.despegar.tools.bookshelf.domain.mongo.MongoStore

class Scalatra extends LifeCycle {

	val morphia = MongoStore.init("mongodb://localhost", "bookshelf")
	
	override def init(context: ServletContext) {
		
		// Main Page
		context.mount(new HomeServlet, "/bookshelf/*")
		
		// REST Services
		context.mount(new ProjectServlet, "/bookshelf/rest/*")
		context.mount(new EnviromentServlet, "/bookshelf/rest/*")
	}
}
