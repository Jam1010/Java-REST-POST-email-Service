package usr.keerthy.email;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicNameValuePair;

/**
 * @author sriramj
 *
 */
public class MailGunEmailProvider extends BaseEmailProvider {

	// NOTE! Use your mailgun api secret-key here
	private static final String MAILGUN_API_PASSWORD = "key-abcd123";
	// NOTE! Use your mailgun custom domain name here
	private static final String MAILGUN_CUSTOM_DOMAIN = "xyz.mailgun.org";

	private static final String MAILGUN_API_USER = "api"; // do not change
	private static final String MAILGUN_ENDPOINT = "https://api.mailgun.net/v3/"
			+ MAILGUN_CUSTOM_DOMAIN + "/messages";

	private MailGunEmailProvider() {
	}

	private static class LazySingletonContainer {
		private static final MailGunEmailProvider SINGLETON = new MailGunEmailProvider();
	}

	public static MailGunEmailProvider getInstance() {
		return LazySingletonContainer.SINGLETON;
	}

	@Override
	HttpPost getHttpPost(final Email email) throws IOException {
		final HttpPost post = new HttpPost(MAILGUN_ENDPOINT);
		final List<NameValuePair> formFields = new ArrayList<>();
		formFields.add(new BasicNameValuePair("to", email.getToName() + " <"
				+ email.getTo() + ">"));
		formFields.add(new BasicNameValuePair("from", email.getFromName()
				+ " <" + email.getFrom() + ">"));
		formFields.add(new BasicNameValuePair("subject", email.getSubject()));
		formFields.add(new BasicNameValuePair("text", email
				.getHtmlEscapedBody()));
		HttpEntity formEntity = new UrlEncodedFormEntity(formFields, "UTF-8");
		post.setEntity(formEntity);
		return post;
	}

	@Override
	HttpClientContext getHttpContext(final Email email) {
		final HttpClientContext httpContext = HttpClientContext.create();
		final CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(AuthScope.ANY,
				new UsernamePasswordCredentials(MAILGUN_API_USER + ":"
						+ MAILGUN_API_PASSWORD));
		httpContext.setCredentialsProvider(credsProvider);
		return httpContext;
	}

}
