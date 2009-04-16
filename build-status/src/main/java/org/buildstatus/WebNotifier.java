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
   private static final String PASS = "pass";
   private static final String FAIL = "fail";
   private static final String APP_NAME_PARAM = "?app_name=";
   
   public WebNotifier(NotificatorRegistry notificatorRegistry) throws IOException {
      notificatorRegistry.register(this);
   }

   @Override
   public void notifyBuildFailed(SRunningBuild sRunningBuild, Set<SUser> arg1) {
      notifyBuildStatusApp(FAIL, sRunningBuild.getFullName());
   }

   @Override
   public void notifyBuildSuccessful(SRunningBuild sRunningBuild, Set<SUser> sUsers) {
      notifyBuildStatusApp(PASS, sRunningBuild.getFullName());
   }

   /**
    * Notify the build-status AppEngine app of failed or passed build by calling http://build-status.appspot.com/pass?app_name=PROJECT or http://build-status.appspot.com/fail?app_name=PROJECT 
    * @param action build-status http action : 'pass' or 'fail'
    * @param appName application name
    */
   private void notifyBuildStatusApp(String action, String appName) {
      try {
         new URL(BASE_URL + action + APP_NAME_PARAM + appName).openStream();
      } catch (MalformedURLException e) {
         e.printStackTrace(); // To replace by a logger
      } catch (IOException e) {
         e.printStackTrace(); // To replace by a logger
      }
   }

   @Override
   public void notifyBuildFailing(SRunningBuild arg0, Set<SUser> arg1) {
      // Do nothing
   }

   @Override
   public void notifyBuildProbablyHanging(SRunningBuild arg0, Set<SUser> arg1) {
      // Do nothing
   }

   @Override
   public void notifyBuildStarted(SRunningBuild arg0, Set<SUser> arg1) {
      // Do nothing
   }

   @Override
   public void notifyResponsibleChanged(SBuildType arg0, Set<SUser> arg1) {
      // Do nothing
   }

   @Override
   public void notifyLabelingFailed(jetbrains.buildServer.Build build, jetbrains.buildServer.vcs.VcsRoot root, java.lang.Throwable t,
         java.util.Set<jetbrains.buildServer.users.SUser> users) {
      // Do nothing
   }

   public String getNotificatorType() {
      return TYPE;
   }

   public String getDisplayName() {
      return TYPE_NAME;
   }
}