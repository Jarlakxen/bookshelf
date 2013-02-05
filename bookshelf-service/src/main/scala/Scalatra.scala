import org.scalatra._
import javax.servlet.ServletContext
import com.despegar.tools.bookshelf.web.view.ViewsServlet
import com.despegar.tools.bookshelf.web.rest._
import com.despegar.tools.bookshelf.domain.mongo.MongoStore

class Scalatra extends LifeCycle {

	val morphia = MongoStore.init("mongodb://localhost", "bookshelf")
	
	override def init(context: ServletContext) {
		
		// Main Page
		context.mount(new ViewsServlet, "/bookshelf/*")
		
		// REST Services
		context.mount(new ProjectServlet, "/bookshelf/rest/project/*")
		context.mount(new ModuleServlet, "/bookshelf/rest/module/*")
		context.mount(new PropertyServlet, "/bookshelf/rest/property/*")
		context.mount(new EnviromentServlet, "/bookshelf/rest/enviroment/*")
		
		context.mount(new QueryServlet, "/bookshelf/query/*")
	}
}
