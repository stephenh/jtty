import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.jetty.webapp.WebInfConfiguration;
import org.mortbay.jetty.webapp.WebXmlConfiguration;

public class Jtty {
	public static void main(String[] args) throws Exception {
		if (args.length < 2) {
			System.err.println("Usage: java -jar jtty.jar <port> <location>");
			System.err.println(" location = path/to/war | contextPath,path/to/war | virtualHost,contextPath,path/to/war");
			return;
		}

		Handler[] handlers = new Handler[args.length - 1];
		for (int i = 1; i < args.length; i++) {
			WebAppContext app = new WebAppContext();
			String[] parts = args[i].split(",");
			if (parts.length == 1) {
				app.setContextPath("/");
				app.setWar(parts[0]);
			} else if (parts.length == 2) {
				app.setContextPath(parts[0]);
				app.setWar(parts[1]);
			} else if (parts.length == 3) {
				app.setVirtualHosts(new String[] { parts[0] });
				app.setContextPath(parts[1]);
				app.setWar(parts[2]);
			}
			// Skip TagLibConfiguration because its hits the net
			app.setConfigurationClasses(new String[] { WebInfConfiguration.class.getName(), WebXmlConfiguration.class.getName() });
			handlers[i - 1] = app;
		}

		Server server = new Server(Integer.parseInt(args[0]));
		server.setStopAtShutdown(true);
		server.setHandlers(handlers);
		server.getConnectors()[0].setHeaderBufferSize(24 * 1024); // for large GET requests, e.g. fakesdb
		server.setAttribute("org.mortbay.jetty.Request.maxFormContentSize", 0); // for large POST requests
		server.start();
	}
}
