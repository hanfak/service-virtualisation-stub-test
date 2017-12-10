import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.assertj.core.api.WithAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;


public class WeatherApplicationTest implements WithAssertions {

    @Test
    public void servesTemperatureBasedOnForecastIoResponse() throws IOException {
        forecastIoService.stubFor(get(urlEqualTo("/data/2.5/weather?q=London,uk&appid=42f829d2049915097be4c996d1275d8d"))
                .willReturn(aResponse().withBody("{\"main\":{\"temp\":100}}")));

        HttpResponse response = Request.Get("http://localhost:" + weatherApplication.port() + "/locationtemp")
                .execute()
                .returnResponse();

        String content = IOUtils.toString(response.getEntity().getContent());

        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(200);
        assertThat(content).isEqualTo("100");
    }

    @Rule
    public WireMockRule forecastIoService = new WireMockRule(8080);

    private WeatherApp weatherApplication = new WeatherApp(PropertyLoader.loadProperties());

    @Before
    public void setUp() {
        weatherApplication.start();
    }

    @After
    public void tearDown() {
        weatherApplication.stop();
    }
}