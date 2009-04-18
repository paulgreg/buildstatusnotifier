package org.buildstatus;


import java.util.Collection;
import java.util.List;

import jetbrains.buildServer.notification.Notificator;
import jetbrains.buildServer.notification.NotificatorRegistry;
import jetbrains.buildServer.serverSide.UserPropertyInfo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WebNotifierTest {

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
	public void notifyBuildStatusAppPass() {
		try {
			notifier.notifyBuildStatusApp(WebNotifier.PASS, "junit-test");
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
}
