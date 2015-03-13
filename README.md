# buildstatusnotifier

Note : Automatically exported from [code.google.com/p/buildstatusnotifier](http://code.google.com/p/buildstatusnotifier).

## Description

That [TeamCity](http://www.jetbrains.com/teamcity/) notifier will notify the [build-status application](http://build-status.appspot.com) for the current project name, according to last build status (passed or failed).

That [application](http://build-status.appspot.com) shows simply a green or red screen according to last build status.

## Installation

Just place the jar file in your Teamcity/webapps/WEB-INF/lib folder and restart teamcity. 

Then, log in and go to "My settings and tools" to enable the "Build status !AppEngine Notifier".

Then, after a build, check your logs (stdout or catalina.out) to get the app_name parameter to use for [build-status application](http://build-status.appspot.com) (as [you can see here](https://github.com/paulgreg/buildstatusnotifier/blob/master/teamcity-notifier/src/main/java/org/buildstatus/WebNotifier.java#L110), it takes the project name, remove first part delimited by ':: ' and replace spaces by underscores).

Note that if you would like to change the project name the notifier use, I suggest you to use [tcPlugins](http://netwolfuk.wordpress.com/teamcity-plugins/). That plugin allows to set the URL and so, specify the name (?appname=) used to contact build-status.appspot.com.

## Build from sources

You will need a JDK >=5 and Maven 2.

Please note that project requires 2 !TeamCity dependencies that doesn't seem available in any maven repository.
So, in order to build that project, go to your Teamcity/webapps/WEB-INF/lib folder and type to add theses 2 libraries to your local repo :

        mvn install:install-file -DgroupId=com.jetbrains -DartifactId=common-api -Dversion=8171 -Dpackaging=jar -Dfile=./common-api.jar
        mvn install:install-file -DgroupId=com.jetbrains -DartifactId=server-api -Dversion=8171 -Dpackaging=jar -Dfile=./server-api.jar
   

Then, go back to sources (in build-status folder, in where you'll find a pom.xml) and type : 
`mvn install`

=Contact=
Contact me, Grégory PAUL, for any remark, bug or question.
My username is paulgreg, both on gmail and on [twitter](http://twitter.com/paulgreg).
It should not be too complicated for you to determine my gmail adress. ;)

## More information about TeamCity Notifiers

You'll find Teamcity Documentation for Plugins on [TeamCity's Wiki](http://www.jetbrains.net/confluence/display/TCD4/Developing+TeamCity+Plugins).

Here are also anothers !TeamCity Notifiers: [buildbunny](http://code.google.com/p/tcgrowl/ tcgrowl] and [http://code.google.com/p/buildbunny/).
