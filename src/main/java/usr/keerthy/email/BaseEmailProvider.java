/**
 * 
 */
package usr.keerthy.email;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * @author sriramj
 *
 */
public abstract class BaseEmailProvider implements EmailProvider {

	/* (non-Javadoc)
	 * @see usr.keerthy.email.EmailProvider#sendMail(usr.keerthy.email.Email)
	 */
	@Override
	public final EmailStatus sendMail(Email email) {
		final EmailStatus status = new EmailStatus();
		try {
			final CloseableHttpClient client = HttpClients.createDefault();

			final HttpClientContext httpContext = getHttpContext(email);
			final HttpPost post = getHttpPost(email);

			final CloseableHttpResponse response = client.execute(post,
					httpContext);
			status.statusCode = response.getStatusLine().getStatusCode();
			status.statusMessage = response.getStatusLine().getReasonPhrase();

			// read response body for status details from email provider
			final BufferedReader br = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			String line = br.readLine();
			status.details = "";
			while (line != null) {
				status.details += line + "\n";
				line = br.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
			status.statusCode = 500;
			status.statusMessage = e.getMessage();
		}
		return status;
	}

	abstract HttpPost getHttpPost(Email email) throws IOException;

	abstract HttpClientContext getHttpContext(Email email);

}
