import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.webapp.WebAppContext;

public class Jtty {
	public static void main(String[] args) throws Exception {
		if (args.length < 2) {
			System.err.println("Usage: java -jar jtty.jar <port> <location>");
			System.err.println(" location = path/to/war | contextPath,path/to/war | virtualHost,contextPath,path/to/war");
			return;
		}

		HandlerList handlers = new HandlerList();
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
			app.setMaxFormContentSize(0); // for large POST requests
			handlers.addHandler(app);
		}

		Server server = new Server(Integer.parseInt(args[0]));
		server.setStopAtShutdown(true);
		server.setHandler(handlers);
		server.getConnectors()[0].setRequestBufferSize(24 * 1024); // for large GET requests, e.g. fakesdb
		server.start();
	}
}
