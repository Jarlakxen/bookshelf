import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.{ DefaultServlet, ServletContextHandler }
import org.eclipse.jetty.webapp.WebAppContext
import org.eclipse.jetty.server.nio.SelectChannelConnector

object JettyLauncher {

	def main( args : Array[String] ) {
		val port = if ( System.getenv( "PORT" ) != null ) System.getenv( "PORT" ).toInt else 8080

		val server = new Server( port )
		val context = new WebAppContext()
		context setContextPath "/"
		context.setResourceBase( "src/main/webapp" )

		context.addServlet( classOf[com.jarlakxen.tools.bookshelf.web.view.ViewsServlet], "/bookshelf/*" )
		context.addServlet( classOf[com.jarlakxen.tools.bookshelf.web.rest.ProjectServlet], "/bookshelf/rest/project/*" )
		context.addServlet( classOf[com.jarlakxen.tools.bookshelf.web.rest.ModuleServlet], "/bookshelf/rest/module/*" )
		context.addServlet( classOf[com.jarlakxen.tools.bookshelf.web.rest.PropertiesGroupServlet], "/bookshelf/rest/propertiesGroup/*" )
		context.addServlet( classOf[com.jarlakxen.tools.bookshelf.web.rest.PropertyServlet], "/bookshelf/rest/property/*" )
		context.addServlet( classOf[com.jarlakxen.tools.bookshelf.web.rest.EnviromentServlet], "/bookshelf/rest/enviroment/*" )
		context.addServlet( classOf[com.jarlakxen.tools.bookshelf.web.rest.QueryServlet], "/bookshelf/query/*" )

		context.addServlet( classOf[DefaultServlet], "/" )

		server.setHandler( context )

		server.start
		server.join
	}
}