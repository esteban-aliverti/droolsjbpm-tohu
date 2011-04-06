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
package org.tohu.load.spreadsheet.questionnaire;

import static org.junit.Assert.fail;

import org.junit.Ignore;
import org.junit.Test;
import org.tohu.load.questionnaire.TohuSpreadsheetLoader;

/**
 * TODO Need to flesh out actual tests as opposed to just creating DRL files from test spreadsheets.
 * 
 * @author Derek Rendall
 */
public class TohuSpreadsheetLoaderTest {

	@Test
	@Ignore
	public void testProcessSimpleFile() {
		TohuSpreadsheetLoader loader = new TohuSpreadsheetLoader();
		if (!loader.processFile("./src/test/resources/SampleDecisionTreeSimple.xls", "./src/test/resources/DecisionTreeSimpleOutput", "./src/test/resources")) {
			fail("File Not Processed");
		}
		
	}
	
	@Test
	@Ignore
	public void testProcessComplexFile() {
		TohuSpreadsheetLoader loader = new TohuSpreadsheetLoader();

		if (!loader.processFile("./src/test/resources/SampleDecisionTreeComplex.xls", "./src/test/resources/DecisionTreeComplexOutput", "./src/test/resources")) {
			fail("File Not Processed");
		}
		
	}

}
