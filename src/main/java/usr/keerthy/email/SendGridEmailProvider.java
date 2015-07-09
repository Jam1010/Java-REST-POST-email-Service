package usr.keerthy.email;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.message.BasicNameValuePair;

/**
 * @author sriramj
 *
 */
public class SendGridEmailProvider extends BaseEmailProvider {
	
	// NOTE! Set your sendgrid account password here
	private static final String SENDGRID_ACCOUNT_PASSWORD = "sendgrid_account_password";
	// NOTE! Set your sendgrid account username here
	private static final String SENDGRID_ACCOUNT_USERNAME = "sendgrid_account_username";
	
	private static final String SENDGRID_ENDPOINT = "https://api.sendgrid.com/api/mail.send.json";

	private SendGridEmailProvider() {
	}
	
	private static class LazySingletonContainer {
		private static final SendGridEmailProvider SINGLETON = new SendGridEmailProvider();
	}
	
	public static SendGridEmailProvider getInstance() {
		return LazySingletonContainer.SINGLETON;
	}

	@Override
	HttpPost getHttpPost(Email email) throws IOException {
		final HttpPost post = new HttpPost(
				SENDGRID_ENDPOINT);

		final List<NameValuePair> formFields = new ArrayList<>();
		formFields.add(new BasicNameValuePair("api_user", SENDGRID_ACCOUNT_USERNAME));
		formFields.add(new BasicNameValuePair("api_key", SENDGRID_ACCOUNT_PASSWORD));
		
		formFields.add(new BasicNameValuePair("to", email.getTo()));
		formFields.add(new BasicNameValuePair("toname", email.getToName()));
		formFields.add(new BasicNameValuePair("from", email.getFrom()));
		formFields.add(new BasicNameValuePair("fromname", email.getFromName()));
		formFields
				.add(new BasicNameValuePair("subject", email.getSubject()));
		formFields.add(new BasicNameValuePair("html", email.getHtmlEscapedBody()));
		HttpEntity formEntity = new UrlEncodedFormEntity(formFields,
				"UTF-8");
		post.setEntity(formEntity);
		return post;
	}

	@Override
	HttpClientContext getHttpContext(Email email) {
		return HttpClientContext.create();
	}

}
