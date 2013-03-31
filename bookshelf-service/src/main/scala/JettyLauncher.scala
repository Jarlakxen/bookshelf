import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.{ DefaultServlet, ServletContextHandler }
import org.eclipse.jetty.webapp.WebAppContext
import org.eclipse.jetty.server.nio.SelectChannelConnector

object JettyLauncher {
	/*
	def main( args : Array[String] ) {
		val port = if ( System.getenv( "PORT" ) != null ) System.getenv( "PORT" ).toInt else 8080

		val server = new Server( port )
		val context = new WebAppContext()
		context setContextPath "/"
		context.setResourceBase( "src/main/webapp" )*/
	//context.addServlet( classOf[com.despegar.tools.bookshelf.web.view.ViewsServlet], "/bookshelf/*" )
	/*context.addServlet( classOf[DefaultServlet], "/" )

		server.setHandler( context )

		server.start
		server.join
	}*/

	def main( args : Array[String] ) {
		val port = if ( System.getenv( "PORT" ) != null ) System.getenv( "PORT" ).toInt else 8080
		
		println( "Starting on port %s...".format( port ) )

		val server : Server = new Server

		server setGracefulShutdown 5000
		server setSendServerVersion false
		server setSendDateHeader true
		server setStopAtShutdown true

		val connector = new SelectChannelConnector
		connector setPort port
		connector setMaxIdleTime 90000
		server addConnector connector

		val webapp = "src/main/webapp"
		val webApp = new WebAppContext
		webApp setContextPath "/"
		webApp setResourceBase webapp
		webApp setDescriptor ( webapp + "/WEB-INF/web.xml" );

		server setHandler webApp

		server.start()
	}
}