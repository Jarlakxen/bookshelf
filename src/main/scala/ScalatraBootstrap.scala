import org.scalatra._
import javax.servlet.ServletContext
import com.jarlakxen.bookshelf.server.service.rest._
import com.jarlakxen.bookshelf.server.web.view._

class Scalatra extends LifeCycle {
	
	override def init(context: ServletContext) {
		
		// Main Page
		context.mount(new ViewsServlet, "/bookshelf/*")
		
		// REST Services
		context.mount(new ProjectServlet, "/bookshelf/rest/project/*")
		context.mount(new ModuleServlet, "/bookshelf/rest/module/*")
		context.mount(new PropertiesGroupServlet, "/bookshelf/rest/propertiesGroup/*")
		context.mount(new PropertyServlet, "/bookshelf/rest/property/*")
		context.mount(new EnviromentServlet, "/bookshelf/rest/enviroment/*")
		
		context.mount(new QueryServlet, "/bookshelf/query/*")
	}
}
