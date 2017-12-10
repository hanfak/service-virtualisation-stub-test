import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

import static javax.servlet.http.HttpServletResponse.SC_OK;

public class LocationTemperatureHandler extends AbstractHandler {
    private final Properties properties;

    LocationTemperatureHandler(Properties properties) {
        this.properties = properties;
    }

    @Override
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException, ServletException {
            response.setStatus(SC_OK);
            response.getWriter().print("Working, property is:  " + properties.getProperty("random.prop"));
        baseRequest.setHandled(true);
    }
}
