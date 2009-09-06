package org.buildstatus;
import hudson.Launcher;
import hudson.Extension;
import hudson.util.FormValidation;
import hudson.model.Build;
import hudson.model.BuildListener;
import hudson.model.AbstractProject;
import hudson.model.Result;
import hudson.tasks.Builder;
import hudson.tasks.BuildStepDescriptor;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.QueryParameter;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Sample {@link Builder}.
 *
 * <p>
 * When the user configures the project and enables this builder,
 * {@link DescriptorImpl#newInstance(StaplerRequest)} is invoked
 * and a new {@link BuildStatusBuilder} is created. The created
 * instance is persisted to the project configuration XML by using
 * XStream, so this allows you to use instance fields (like {@link #name})
 * to remember the configuration.
 *
 * <p>
 * When a build is performed, the {@link #perform(Build, Launcher, BuildListener)} method
 * will be invoked. 
 *
 * @author Kohsuke Kawaguchi
 */
public class BuildStatusBuilder extends Builder {

    private final String name;

	private static final String TYPE = "BuildStatusNotifier";
	private static final String TYPE_NAME = "Build status AppEngine Notifier";

	static final String BASE_URL = "http://build-status.appspot.com/";
	static final String APP_NAME_PARAM = "?app_name=";
	static final String PASS = "pass";
	static final String FAIL = "fail";

    
    @DataBoundConstructor
    public BuildStatusBuilder(String name) {
        this.name = name;
    }

    /**
     * We'll use this from the <tt>config.jelly</tt>.
     */
    public String getName() {
        return name;
    }

    public boolean perform(Build build, Launcher launcher, BuildListener listener) {
        // this is where you 'build' the project
        // since this is a dummy, we just say 'hello world' and call that a build
    	PrintStream logger = listener.getLogger();

    	String result = Result.SUCCESS.equals(build.getResult()) ? PASS : FAIL;
		logger.println("Result:" + result);
    	
		try {
			notifyBuildStatusApp(logger, result, formatProjectName(build.getProject().getFullName()));
		} catch (Exception e) {
			logger.println(e.getMessage() + "-" + e.toString());
		}

		// this also shows how you can consult the global configuration of the builder
        if(getDescriptor().useFrench())
            logger.println("Bonjour, "+name+"!");
        else
            logger.println("Hello, "+name+"!");
        
        return true;
    }

    // overrided for better type safety.
    // if your plugin doesn't really define any property on Descriptor,
    // you don't have to do this.
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl)super.getDescriptor();
    }

    
	/**
	 * Notify the build-status AppEngine app of failed or passed build by
	 * calling http://build-status.appspot.com/pass?app_name=PROJECT or
	 * http://build-status.appspot.com/fail?app_name=PROJECT
	 * @param logger 
	 * 
	 * @param action
	 *            build-status http action : 'pass' or 'fail'
	 * @param appName
	 *            application name
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	void notifyBuildStatusApp(PrintStream logger, String action, String appName) throws MalformedURLException, IOException {
		String url = BASE_URL + action + APP_NAME_PARAM + appName;
		logger.println("Sending request to " + url);
		new URL(url).openStream();
	}

	/**
	 * Transform a full name to a simple project name. Note that spaces are
	 * replaced by underscores.
	 * 
	 * @param fullName
	 *            full name : "your project name here"
	 * @return a simple project name
	 */
	String formatProjectName(final String fullName) {
		return fullName.replaceAll(" ", "_");
	}
	
    /**
     * Descriptor for {@link BuildStatusBuilder}. Used as a singleton.
     * The class is marked as public so that it can be accessed from views.
     *
     * <p>
     * See <tt>views/hudson/plugins/hello_world/HelloWorldBuilder/*.jelly</tt>
     * for the actual HTML fragment for the configuration screen.
     */
    @Extension // this marker indicates Hudson that this is an implementation of an extension point.
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {
        /**
         * To persist global configuration information,
         * simply store it in a field and call save().
         *
         * <p>
         * If you don't want fields to be persisted, use <tt>transient</tt>.
         */
        private boolean useFrench;

        /**
         * Performs on-the-fly validation of the form field 'name'.
         *
         * @param value
         *      This parameter receives the value that the user has typed.
         * @return
         *      Indicates the outcome of the validation. This is sent to the browser.
         */
        public FormValidation doCheckName(@QueryParameter String value) throws IOException, ServletException {
            if(value.length()==0)
                return FormValidation.error("Please set a name");
            if(value.length()<4)
                return FormValidation.warning("Isn't the name too short?");
            return FormValidation.ok();
        }

        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            // indicates that this builder can be used with all kinds of project types 
            return true;
        }

        /**
         * This human readable name is used in the configuration screen.
         */
        public String getDisplayName() {
            return "Build status AppEngine Notifier";
        }

        public boolean configure(StaplerRequest req, JSONObject o) throws FormException {
            // to persist global configuration information,
            // set that to properties and call save().
            useFrench = o.getBoolean("useFrench");
            save();
            return super.configure(req,o);
        }

        /**
         * This method returns true if the global configuration says we should speak French.
         */
        public boolean useFrench() {
            return useFrench;
        }
    }
  
}

