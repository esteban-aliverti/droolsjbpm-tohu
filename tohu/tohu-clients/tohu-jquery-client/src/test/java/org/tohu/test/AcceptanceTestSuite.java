package org.tohu.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.tohu.BaseTestFramework;

/**
 * List all the test classes you would like to
 * run for the "Acceptance" process.  
 * 
 * The Acceptance process runs all the regression
 * tests for all the browsers configured in the class
 * {@link BaseTestFramework} 
 * @author rb1317
 *
 */

@RunWith(Suite.class)
@SuiteClasses(       
    {
        DateFieldTest.class,
        DynamicallyGeneratedErrorsTest.class,
        DynamicPageTest.class,
        ErrorHandlingTest.class,
        FileUploadTest.class,
        InitialPageTest.class,
        InitialPageWithErrorsTest.class,
        ReadOnlyPageTest.class        
    }
)

/**
 * Wrapper class for the Test suite 
 * to run all regression tests
 * 
 */
public class AcceptanceTestSuite {
    
}

