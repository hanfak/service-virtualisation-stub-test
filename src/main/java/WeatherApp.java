import exceptions.WebServerShutdownException;
import exceptions.WebServerStartupException;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

import java.util.Date;
import java.util.Properties;

import static java.lang.Integer.parseInt;

public class WeatherApp {

    private Server server;
    private Properties properties;

    private WeatherApp(Properties properties) {
        this.properties = properties;
    }

    public static void main(String[] args) throws Exception {
        WeatherApp weatherApplication = new WeatherApp(PropertyLoader.loadProperties());
//        weatherApplication.startServer();
        weatherApplication.start();
        weatherApplication.join();
    }

    private void start() {
        startServer();
        info("Starting application...");

        server.setHandler(createContextHandlers());

        try {
            server.start();
        } catch (Exception e) {
            throw new WebServerStartupException(e);
        }

        info("Started!");
    }

    private void startServer() {
        info("Starting server!");
        server = new Server(port());
        info("Server started!");
    }

    private int port() {
        return parseInt(properties.getProperty("port"));
    }

    private ContextHandlerCollection createContextHandlers() {
        ContextHandler context = new ContextHandler();
        context.setHandler(new HomeHandler());

        // TODO: any other path /locationtemp/asf should return 404
        ContextHandler locationTempContext = new ContextHandler();
        locationTempContext.setContextPath("/locationtemp");
        locationTempContext.setHandler(new LocationTemperatureHandler(properties));

        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[] { context, locationTempContext });
        return contexts;
    }

    private void join() throws InterruptedException {
        server.join();
    }

    // For testing
    @SuppressWarnings("unused")
    public void stop() {
        info("Stopping application...");

        try {
            server.stop();
            server.join();
        } catch (Exception e) {
            throw new WebServerShutdownException(e);
        }

        info("Stopped!");
    }

    private static void info(String msg) {
        System.out.println(new Date() + ": " + msg);
    }
}
