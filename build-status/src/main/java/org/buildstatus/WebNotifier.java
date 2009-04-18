package org.buildstatus;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import jetbrains.buildServer.notification.Notificator;
import jetbrains.buildServer.notification.NotificatorRegistry;
import jetbrains.buildServer.serverSide.SBuildType;
import jetbrains.buildServer.serverSide.SRunningBuild;
import jetbrains.buildServer.users.SUser;

public class WebNotifier implements Notificator {

	private static final String TYPE = "BuildStatusNotifier";
	private static final String TYPE_NAME = "Build status AppEngine Notifier";

	private static final String BASE_URL = "http://build-status.appspot.com/";
	private static final String APP_NAME_PARAM = "?app_name=";
	static final String PASS = "pass";
	static final String FAIL = "fail";

	public WebNotifier(NotificatorRegistry notificatorRegistry)
			throws IOException {
		notificatorRegistry.register(this);
	}

	public void notifyBuildFailed(SRunningBuild sRunningBuild, Set<SUser> arg1) {
		try {
			notifyBuildStatusApp(FAIL, sRunningBuild.getFullName());
		} catch (Exception e) {
			// Do nothing for now...
		}
	}

	public void notifyBuildSuccessful(SRunningBuild sRunningBuild,
			Set<SUser> sUsers) {
		try {
			notifyBuildStatusApp(PASS, sRunningBuild.getFullName());
		} catch (Exception e) {
			// Do nothing for now...
		}
	}

	public void notifyBuildFailing(SRunningBuild arg0, Set<SUser> arg1) {
		// Do nothing
	}

	public void notifyBuildProbablyHanging(SRunningBuild arg0, Set<SUser> arg1) {
		// Do nothing
	}

	public void notifyBuildStarted(SRunningBuild arg0, Set<SUser> arg1) {
		// Do nothing
	}

	public void notifyResponsibleChanged(SBuildType arg0, Set<SUser> arg1) {
		// Do nothing
	}

	public void notifyLabelingFailed(jetbrains.buildServer.Build build,
			jetbrains.buildServer.vcs.VcsRoot root, java.lang.Throwable t,
			java.util.Set<jetbrains.buildServer.users.SUser> users) {
		// Do nothing
	}

	/**
	 * Notify the build-status AppEngine app of failed or passed build by
	 * calling http://build-status.appspot.com/pass?app_name=PROJECT or
	 * http://build-status.appspot.com/fail?app_name=PROJECT
	 * 
	 * @param action
	 *            build-status http action : 'pass' or 'fail'
	 * @param appName
	 *            application name
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	void notifyBuildStatusApp(String action, String appName)
			throws MalformedURLException, IOException {
		new URL(BASE_URL + action + APP_NAME_PARAM + appName).openStream();
	}

	public String getNotificatorType() {
		return TYPE;
	}

	public String getDisplayName() {
		return TYPE_NAME;
	}

}
