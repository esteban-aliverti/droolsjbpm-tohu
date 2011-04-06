/*
 * Copyright 2009 Solnet Solutions Limited (http://www.solnetsolutions.co.nz/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tohu.load;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

/**
 * 
 * @author David Plumpton
 */
public class PeriodicRuleLoaderTest {
	
	@Test
	@Ignore
	public void createPeriodicRuleLoader() {
		// TODO Make this generic
		String ruleFile = "../life-example/life-example-rules/src/test/resources/LifeInsuranceRules.xls";
		String outputDir = "../life-example/life-example-rules/target/temp-rules";
		String importDir = "../life-example/life-example-rules/src/test/resources/";
		String droolsDir = "../life-example/life-example-rules/target/simulated-drools-directory";
		PeriodicRuleLoader periodic = new PeriodicRuleLoader(ruleFile, outputDir, importDir, droolsDir);
		periodic.setPeriodSeconds(1);
		assertTrue("Conversion from spreadsheet to DRL files should succeed", periodic.loadRules());
	}
	
}
