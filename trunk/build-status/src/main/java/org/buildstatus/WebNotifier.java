package org.buildstatus;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Set;

import jetbrains.buildServer.notification.Notificator;
import jetbrains.buildServer.notification.NotificatorRegistry;
import jetbrains.buildServer.serverSide.SBuildType;
import jetbrains.buildServer.serverSide.SRunningBuild;
import jetbrains.buildServer.users.SUser;

/**
 * TeamCity notifier for build-status AppEngine application
 * 
 * @see http://build-status.appspot.com
 * @author Grégory PAUL <paulgreg at gmail.com>
 */
public class WebNotifier implements Notificator {

	private static final String TYPE = "BuildStatusNotifier";
	private static final String TYPE_NAME = "Build status AppEngine Notifier";

	static final String BASE_URL = "http://build-status.appspot.com/";
	static final String APP_NAME_PARAM = "?app_name=";
	static final String PASS = "pass";
	static final String FAIL = "fail";

	public WebNotifier(NotificatorRegistry notificatorRegistry) throws IOException {
		notificatorRegistry.register(this);
	}

	public void notifyBuildFailed(SRunningBuild sRunningBuild, Set<SUser> arg1) {
		info("Build failed");
		try {
			notifyBuildStatusApp(FAIL, formatProjectName(sRunningBuild.getFullName()));
		} catch (Exception e) {
			error(e.getMessage() + "-" + e.toString());
		}
	}

	public void notifyBuildSuccessful(SRunningBuild sRunningBuild, Set<SUser> sUsers) {
		info("Build passed");
		try {
			notifyBuildStatusApp(PASS, formatProjectName(sRunningBuild.getFullName()));
		} catch (Exception e) {
			error(e.getMessage() + "-" + e.toString());
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

	public void notifyLabelingFailed(jetbrains.buildServer.Build build, jetbrains.buildServer.vcs.VcsRoot root,
			java.lang.Throwable t, java.util.Set<jetbrains.buildServer.users.SUser> users) {
		// Do nothing
	}

	public String getNotificatorType() {
		return TYPE;
	}

	public String getDisplayName() {
		return TYPE_NAME;
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
	void notifyBuildStatusApp(String action, String appName) throws MalformedURLException, IOException {
		String url = BASE_URL + action + APP_NAME_PARAM + appName;
		info("Sending request to " + url);
		new URL(url).openStream();
	}

	/**
	 * Transform a full name to a simple project name. Note that spaces are
	 * replaced by underscores.
	 * 
	 * @param fullName
	 *            full TeamCity name : "Builds :: your project name here"
	 * @return a simple project name
	 */
	String formatProjectName(final String fullName) {
		String separator = ":: ";
		int idxToRemove = fullName.lastIndexOf(separator) + separator.length();
		return fullName.substring(idxToRemove).replaceAll(" ", "_");
	}

	/**
	 * Log with an out.println
	 * 
	 * @param s
	 *            String to log
	 */
	private void info(String s) {
		System.out.println(this.getClass().toString() + "# " + new Date().toString() + ": " + s);
	}

	/**
	 * Log with an err.println
	 * 
	 * @param s
	 *            String to log
	 */
	private void error(String s) {
		System.err.println(this.getClass().toString() + "# " + new Date().toString() + ": " + s);
	}
}
