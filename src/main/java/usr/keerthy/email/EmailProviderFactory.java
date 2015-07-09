package usr.keerthy.email;

import java.util.HashSet;
import java.util.Set;

/**
 * @author sriramj
 *
 */
public class EmailProviderFactory {
	private static final String EMAIL_PROVIDER_MAIL_GUN = "MailGun";
	private static final String EMAIL_PROVIDER_SEND_GRID = "SendGrid";

	public static final Set<String> emailProviders = new HashSet<>();

	static {
		emailProviders.add(EMAIL_PROVIDER_MAIL_GUN);
		emailProviders.add(EMAIL_PROVIDER_SEND_GRID);
	}

	// initialize sendgrid as the default email provider, incase we put invalid
	// value in the pom.xml config file.
	private volatile String emailProviderName = EMAIL_PROVIDER_SEND_GRID;

	private EmailProviderFactory() {
	}

	private static class LazySingletonContainer {
		private static final EmailProviderFactory SINGLETON = new EmailProviderFactory();
	}

	public static EmailProviderFactory getInstance() {
		return LazySingletonContainer.SINGLETON;
	}

	public EmailProvider getProvider() {
		switch (emailProviderName) {
		case EMAIL_PROVIDER_SEND_GRID:
			return SendGridEmailProvider.getInstance();
		case EMAIL_PROVIDER_MAIL_GUN:
			return MailGunEmailProvider.getInstance();
		default:
			// should never match default
			// hardcoded email provider is SendGrid
			return SendGridEmailProvider.getInstance();
		}
	}

	public boolean changeProvider(String providerName) {
		if (emailProviders.contains(providerName)) {
			emailProviderName = providerName;
			return true;
		}

		// invalid email provider
		return false;
	}

}
