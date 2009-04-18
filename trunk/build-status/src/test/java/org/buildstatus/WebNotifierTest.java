package org.buildstatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.List;

import jetbrains.buildServer.notification.Notificator;
import jetbrains.buildServer.notification.NotificatorRegistry;
import jetbrains.buildServer.serverSide.UserPropertyInfo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WebNotifierTest {

	private static final String TEST_APP_NAME = "junit-test";
	private WebNotifier notifier;

	@Before
	public void setUp() throws Exception {

		NotificatorRegistry mockedRegistry = new NotificatorRegistry() {

			public Notificator findNotificator(String arg0) {
				return null;
			}

			public Collection<Notificator> getNotificators() {
				return null;
			}

			public void register(Notificator arg0) {
			}

			public void register(Notificator arg0, List<UserPropertyInfo> arg1) {
			}

			public void unregister(Notificator arg0) {
			}

		};

		notifier = new WebNotifier(mockedRegistry);
	}

	@Test
	public void notifyBuildStatusAppPass() throws MalformedURLException, IOException {
		try {
			notifier.notifyBuildStatusApp(WebNotifier.PASS, TEST_APP_NAME);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}

		Assert.assertTrue(isThatStringPresentOnAppEngineTestPage("favicon-pass.png"));
		Assert.assertFalse(isThatStringPresentOnAppEngineTestPage("favicon-fail.png"));
	}

	@Test
	public void notifyBuildStatusAppFail() throws MalformedURLException,
			IOException {
		try {
			notifier.notifyBuildStatusApp(WebNotifier.FAIL, TEST_APP_NAME);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}

		Assert.assertTrue(isThatStringPresentOnAppEngineTestPage("favicon-fail.png"));
		Assert.assertFalse(isThatStringPresentOnAppEngineTestPage("favicon-true.png"));
	}

	/**
	 * Search the corresponding string in the AppEngine "junit-test" web page
	 * @param searchedString looking for String
	 * @return true if found, false otherwise
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	private boolean isThatStringPresentOnAppEngineTestPage(String searchedString) throws IOException,
			MalformedURLException {
		InputStream is = new URL(WebNotifier.BASE_URL
				+ WebNotifier.APP_NAME_PARAM + TEST_APP_NAME).openStream();

		boolean faviconFound = false;

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));

		String line = null;
		while ((line = reader.readLine()) != null) {
			if (line.indexOf(searchedString) != -1) {
				faviconFound = true;
				break;
			}
		}

		return faviconFound;
	}
}
