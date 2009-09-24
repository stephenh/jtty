import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;

public class Jtty {
	public static void main(String[] args) throws Exception {
		if (args.length < 2) {
			System.err.println("Usage: java -jar jtty.jar <port> <location>");
			return;
		}

		WebAppContext app = new WebAppContext();
		app.setContextPath("/");
		app.setWar(args[1]);

		Server server = new Server(Integer.parseInt(args[0]));
		server.setStopAtShutdown(true);
		server.setHandler(app);
		server.start();
	}
}
