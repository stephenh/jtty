import java.util.HashSet;

import javax.servlet.SessionTrackingMode;

import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.util.BlockingArrayQueue;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;

/** A simple class for booting Jetty by pointing it at a war, with sane defaults. */
public class Jtty {
  public static void main(String[] args) throws Exception {
    if (args.length < 2) {
      System.err.println("Usage: java -jar jtty.jar <port> <location>");
      System.err.println(" location = path/to/war | contextPath,path/to/war | virtualHost,contextPath,path/to/war");
      return;
    }

    // I don't like sessions, but realistically most people probably use it
    boolean sessions = Boolean.valueOf(System.getProperty("jtty.sessions", "true"));

    ContextHandlerCollection handlers = new ContextHandlerCollection();
    for (int i = 1; i < args.length; i++) {
      WebAppContext app = new WebAppContext();
      String[] parts = args[i].split(",");
      if (parts.length == 1) {
        // just path/to/war, so assume root /
        app.setContextPath("/");
        app.setWar(parts[0]);
      } else if (parts.length == 2) {
        // contextPath,path/to/war
        app.setContextPath(parts[0]);
        app.setWar(parts[1]);
      } else if (parts.length == 3) {
        // virtualHost,contextPath,path/to/war
        app.setVirtualHosts(new String[] { parts[0] });
        app.setContextPath(parts[1]);
        app.setWar(parts[2]);
      }
      app.setMaxFormKeys(200);
      app.setMaxFormContentSize(10485760); // for large POST requests, 10mb
      app.getSecurityHandler().setLoginService(new HashLoginService());
      if (!sessions) {
        app.getSessionHandler().getSessionManager().setSessionTrackingModes(new HashSet<SessionTrackingMode>());
      }
      handlers.addHandler(app);
    }

    ThreadPool pool = new QueuedThreadPool(//
      200, // max threads
      2, // min threads
      60000, // idle time
      new BlockingArrayQueue<Runnable>(500)); // fixed queue size

    Server server = new Server(pool);

    HttpConfiguration config = new HttpConfiguration();
    config.setSendServerVersion(false);

    ServerConnector connector = new ServerConnector(server, new HttpConnectionFactory(config));
    connector.setPort(Integer.parseInt(args[0]));
    server.setConnectors(new Connector[] { connector });

    server.setStopAtShutdown(true);
    server.setHandler(handlers);
    server.start();
  }
}
