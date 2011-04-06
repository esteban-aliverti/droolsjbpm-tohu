


********************************************************************************
********************************************************************************


    ------------ Section A
    
        The Tohu framework has successfully being tested on the following webservers
        
        Jetty 		 	 6.1.10
        Apache Tomcat 		 6.0.14
        IBM Websphere 		 6.1.0.17
        
        The Tohu framework has successfully being tested on the following browsers
        
        IE 6, 7, 8
        Firefox 3, 3.5
        Safari 4
        Chrome 4


********************************************************************************
********************************************************************************


    ------------ Section B
    
    
        The libraries packaged in this binary distribution are all required for any 
        extension of this framework for web application purposes.  It is imperative
        that you reference them as part of your build process and include them 
        within your generated distributed WAR file. 

        ----- maven users        
		The poms's extracted from the binary distribution will automatically 
        	download and install some of the dependencies (jars)into your maven repository.  Otherwise
	        one will need to manually add them into your repository using the dependency groupId 
	        and ArtifactId defined in the poms as a reference when installing them.  Once done, all one
	        needs to do is run mvn clean install jetty:run.  This will generate a War file and deploy it 
        	to the Jetty webserver.
        
	----- Apache Ant Users
        	For Ant users, one will need to create an Ant build file with the sample project embedded within
	        this binary distribution called SampleProject.  The Ant build file will need to configures its
	        classpath tag to include the dependencies (jars) extracted from the binary distribution and 
	        use the standard build Ant tags to generate a WAR file.  The War file can be deployed to any
	        webserver mentioned in section A.
        
	----- Eclipse users
		one can run "mvn eclipse:eclipse" to generate a ".project" file and import
	        the project into eclipse via its "import" facility.  Make sure the M2_REPO classpath variable
	        is defined or alternately you can manually import the jars located in the SampleProject.
	        Then one can deploy to a webserver (e.g. Tomcat) within eclipse.



********************************************************************************
********************************************************************************


    ------------ Section C
    
        The underlying Drools execution server requires a "KnowledgeAgent" so that it 
        can determine where to locate all the required drool files to fire when it 
        receives a request.  The "KnowledgeAgent" is actually an xml resource found at
        \src\main\resources\org\tohu\utility\agent\Tohu.xml. 
        Once you have implemented all rules within Drool files, then you will need 
        to update the Tohu.xml file indicating where these new drool files are located.  
            
        
        An example is provided below
        
        This file will be referred to as "MyKnowledgeAgent.xml" from here on.
        
        <change-set xmlns='http://drools.org/drools-5.0/change-set' 
        		 		     xmlns:xs='http://www.w3.org/2001/XMLSchema-instance' 
        		 		               xs:schemaLocation='http://drools.org/drools-5.0/change-set 
                              drools-change-set-5.0.xsd'>
        
          <add>
            <resource type="DRL" source="classpath:org/tohu/Active.drl"/>
            <resource type="DRL" source="classpath:org/tohu/DomainModel.drl"/>
            <resource type="DRL" source="classpath:org/tohu/Item.drl"/>
            <resource type="DRL" source="classpath:org/tohu/Queries.drl"/>
            <resource type="DRL" source="classpath:org/tohu/Question.drl"/>
            <resource type="DRL" source="classpath:org/tohu/Changes.drl"/>
            <resource type="DRL" source="classpath:org/tohu/listener.drl"/>    
            <resource type="DRL" source="classpath:org/tohu/support/SupportQueries.drl" />
            <resource type="DRL" source="classpath:#New_Drool_Files_Path#"/>
            	<!-- 
		     for linux based systems there's currently a "file handle release" issue, 
		     so use the following line instead of the above one.  Ensure your webserver 
		     has access rights to the directory location and its resource contents
		     <resource type="DRL" source="${absolute_path}/#New_Drool_Files_Path#"/> 
 		 -->
          </add>
        </change-set> 
        
        notes:
        
        The ${absolute_path} property above, if used, needs to be set to a file path to 
        where all your  additional drool files representing your questionnaire are to be 
        found.
        
        #New_Drool_Files_Path# is the project source path to where the drool files are located
        
        For example if you had the following resources defined within your Tohu extended 
        project
        
        src/main/resource/rules/Questionnaire.drl
        src/main/resource/rules/Page1.drl
        src/main/resource/rules/Page2.drl
        src/main/resource/rules/Completion.drl
        
        Then this would result in #New_Drool_Files_Path# being set to 
        "src/main/resource/rules"
 

********************************************************************************
********************************************************************************


    ------------ section D
        
        One does not necessarily need to write drool files to define your business rules.
        Tohu comes with a spreadsheet that allows you to do the same.  Tohu will effectively
        parse this file and generate drool files for you during runtime.  Therefore you can 
        make changes and see them immediately on the webpage (one must restart a 
        user session though)
        
        The spreadsheet is located in src\main\resources\spreadsheet\SampleDecisionTreeOnePage.xls.
        
	----- maven users
        	For Tohu to pick up changes you first must run the following command
		"mvn exec:java"
		then run the following command in another command line console
		"mvn clean package jetty:run"
		which will deploy the web application to jetty
        
            	enter in information into the sample web app.
	        Then make changes to the spreadsheet.
            	wait 30 seconds until the drool file is updated.
            	then clear the browser session.
            	goto the sample web app home page again by re-entering the url.
            	the changes you made in the spreadsheet should be reflects on the web-app.
        
        
        ----- Apache Ant Users
            	For Apache Ant users the "mvn exec:java" command is equivalent to running a 
            	Tohu java application called PeriodicRuleLoader. which is found in the Tohu-Loader jar.  
           	It will need the following parameters supplied to it
            	rules spreadsheet file (default is /src/main/resources/spreadsheet/SampleDecisionTreeOnePage.xls)
            	rules destination directory (default is /target/temp-rules-dir)
          	rules import directory (default is /src/main/resources/rules)
           	drools destination directory (default is /src/main/resources/rules)

            	enter in information into the sample web app. 
            	Then make changes to the spreadsheet.
            	wait 30 seconds until the drool file is updated.
            	then clear the browser session.
            	goto the sample web app home page again by re-entering the url.
            	the changes you made in the spreadsheet should be reflects on the web-app.
            
********************************************************************************
********************************************************************************


    ------------ Section E
    
        The Tohu-JQuery-Client framework jar must be unpacked and its contents placed 
        into the webroot directory of your WAR being deployed. 
    
        ----- maven users 
            	This is already done for maven users as the pom extracted from the binary distribution will have the 
            	following defined 
    
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-dependency-plugin</artifactId>
                <executions>
                  <execution>
                    <id>unpack</id>
                    <phase>validate</phase>
                    <goals>
                      <goal>unpack</goal>
                    </goals>
                    <configuration>
                      <artifactItems>
                        <artifactItem>
            		 	<groupId>org.tohu</groupId>                
            		 	<artifactId>tohu-jquery-client</artifactId>
            		 	<version>${tohu.version}</version>
                        </artifactItem>
                      </artifactItems>
                    </configuration>
                  </execution>
                </executions>
            </plugin>
          
          
        ----- Apache Users      
            The following example is how it would be done with Apache Ant
    
            <target name="init">		 		 
            		 <property file="src/main/filters/${user.name}.properties"/>		 		 
            		 ....
            		 ....
            		 ....
            		 <property name="libs" value="${dirs.base}/libs"/>		 		 		 
            		 <property name="dirs.base" value="${basedir}"/>
            		 <property name="warDir" value="${dirs.base}/build/war"/>
            
            </target>
            
            <target name="unpack">
            		 <unjar src="${libs}/tohu-jquery-client-X.X.X-SNAPSHOT.jar" dest="${warDir}">		 		 		 
            		 </unjar>
            </target>
            
            <!-- Create the War File -->
            <target name="buildWar" depends="init, .., unpack">
            		 ....		 		 
            		 ....
            		 <jar jarfile="${earDir}/${warFile}" basedir="${warDir}" />
            </target>		       


********************************************************************************
********************************************************************************


    ------------ Section F
    
        The StyleTemplate.css file extracted from this binary download is a template of all the style 
        classes currently used by Tohu. Each CSS element has a description of what 
        it is used for. The sample project has an instance of this template css file populated
        with styling attributes.  One can change this to alter the layout affect of the Sample
        web application.
        
        if the external developer wants to create their own styling for some tohu widgets,
        then can do so by applying their styles in the spreadsheet file SampleDecisionTreeOnePage.xls
        The columns titled "style" should contain the style classes ytou wish to reference for the 
        Tohu component being displayed on the web end.  One can add as many additional columns titled
        "style" to the spreadsheet.  Then one must simply update the the style.css file in the 
        sample project to reflect the definitions of this style classes.
        
        Also note that Tohu provides many hooks to perform post processing of the dynamic 
        html.  This implies that one can actually change the traditional tohu widgets 
        currently available to one's you would like to build and use.
        
        Find below a template piece of functionality for post processing a question whose
        anwserable options are initially generated as 2 mutually exclusive radio buttons.
        
        This javascript adds post processing functionality to any question
        with either or both of the following presentation styles "radio", "or" 
        configured for the question in drools  
        Using for example JQuery, one could manipulate the existing html generated 
        for this question.  It could display the 2 radio options as underlined labels 
        and undisplay the radio buttons using a combination of JQuery and CSS.  The 
        JQuery functionality could add a click handler to the label HTML element to 
        update the underlying invisible radio button when selected. 
        
        /*
        		 customiseable hook
        		 @param obj - Tohu Question Object.
        */
        function postProcessQuestion(obj) {
        		 if (hasStyle(obj, "radio")) {
        		 		 postProcessRadio(obj.id);		 
        		 }
        		 if (hasStyle(obj, "or")) {
        		 		 postProcessOr(obj.id);		 		 
        		 }		 		 
        }
        
        function postProcessRadio(String) {
        		 ....
        		 ....
        }
        
        function postProcessOr(String) {
        		 ....
        		 ....
        }
        
        /**
         * Determines if the specified object has a particular style.
         * 
         * @param obj Fact object.
         * @param style String
         * @return Boolean
         */
        function hasStyle(obj, style) {
        		 return (obj.presentationStyles != null) && (jQuery.inArray(style, obj.presentationStyles.split(",")) >= 0);
        }
