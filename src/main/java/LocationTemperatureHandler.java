import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.json.JSONException;
import org.json.JSONObject;

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

        try {
            String forecastIo = forecastIoFor();
            String temperature = parseJson(forecastIo);
            System.out.println(temperature);
            response.setStatus(SC_OK);
            response.getWriter().print(temperature);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        baseRequest.setHandled(true);
    }

    private String forecastIoFor() throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpGet httpget = new HttpGet(getForecastIoUrl());
            System.out.println(httpget.getURI());
            httpget.addHeader("accept-encoding", "identity");

            ResponseHandler<String> responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();
                System.out.println(status);
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };
            return httpclient.execute(httpget, responseHandler);
        }
    }

    private String parseJson(String forecastIo) throws JSONException {
        System.out.println(forecastIo);
        JSONObject jsonObject = new JSONObject(forecastIo);
        JSONObject jsonMainArr = jsonObject.getJSONObject("main");
        return jsonMainArr.getString("temp");

//        JSONObject jsonObject = new JSONObject(forecastIo);
//        String dt = jsonObject.getString("dt");
//        return dt;
    }

    private String getForecastIoUrl() {
        return properties.getProperty("weather-application.forecastio.url");
    }
}
