package usr.keerthy;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EmailProcessorIntegrationTest {

    private HttpServer server;
    private WebTarget target;

    @Before
    public void setUp() throws Exception {
        // start the server
        server = Main.startServer();
        // create the client
        Client c = ClientBuilder.newClient();

        // uncomment the following line if you want to enable
        // support for JSON in the client (you also have to uncomment
        // dependency on jersey-media-json module in pom.xml and Main.startServer())
        // --
        // c.configuration().enable(new org.glassfish.jersey.media.json.JsonJaxbFeature());

        target = c.target(Main.BASE_URI);
    }

    @After
    public void tearDown() throws Exception {
        server.shutdownNow();
    }

    /**
     * Test to see that the message "Got it!" is sent in the response.
     */
    @Test
    public void testGetIt() {
        Entity<String> emailJson = Entity.json("{\"to\":\"sriramkeerthy@gmail.com\","
				+ "\"to_name\":\"sriram\","
				+ "\"from\":\"noreply@uber.com\","
				+ "\"from_name\":\"Uber\","
				+ "\"subject\":\"Testing\","
				+ "\"body\":\"<h1>Test Email Processor app</h1>\"}");
		Response response = target.path("email").request().post(emailJson);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }
}
