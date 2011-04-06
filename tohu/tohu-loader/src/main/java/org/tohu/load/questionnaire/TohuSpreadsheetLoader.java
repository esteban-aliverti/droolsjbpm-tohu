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
package org.tohu.load.questionnaire;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tohu.domain.questionnaire.Application;
import org.tohu.load.spreadsheet.sections.SpreadsheetSection;
import org.tohu.load.spreadsheet.sections.SpreadsheetSectionSplitter;
import org.tohu.load.spreadsheet.WorkbookData;
import org.tohu.write.questionnaire.ApplicationTemplate;

/**
 * The main entry point for processing a Questionnaire based spreadsheet. 
 * 
 * @author Derek Rendall
 */
public class TohuSpreadsheetLoader implements SpreadsheetSectionConstants {
	
	private static final Logger logger = LoggerFactory.getLogger(TohuSpreadsheetLoader.class);
	
	/** useful section heading to avoid processing rest of spreadsheet - can store temp working stuff after this line */
	public static final String SHEET_END ="END";
		
	private WorkbookData wbData;
	private Application application;
	
	private String outputDirectory;
	private String importDirectory;

	private boolean seperatePageDirectories = true;
	
	public TohuSpreadsheetLoader() {
		super();
	}

	/**
	 * Will call {@link #processFile(String, String, String, boolean)} passing in true to create 
	 * separate page directories.
	 * 
	 * @param filename
	 * @param outputDirectory
	 * @param importDirectory
	 * @return
	 */
	public boolean processFile(String filename, String outputDirectory, String importDirectory) {
		return processFile(filename, outputDirectory, importDirectory, true);
	}
	
	/**
	 * Start the process of loading the Questionnaire data 
	 * 
	 * @param filename
	 * 			The path and file name of the spreadsheet file 
	 * @param outputDirectory
	 * 			Where to place the resulting DRL files.
	 * @param importDirectory
	 * 			Where to locate files that are referred to as imports in the spreadsheet.
	 * @param seperatePageDirectories
	 * 			Is each page created in a sub-directory of the output directory.
	 * @return
	 */
	public boolean processFile(String filename, String outputDirectory, String importDirectory, boolean seperatePageDirectories) {
		wbData = new WorkbookData();
		this.outputDirectory = outputDirectory;
		this.importDirectory = importDirectory;
		this.seperatePageDirectories = seperatePageDirectories;
		
		if (!wbData.loadWorkbook(filename)) {
			logger.debug("Data not loaded from workbook");
			return false;
		}
		
		return processData(PAGE_SECTION_HEADINGS);
	}
	
	/**
	 * Load up the data from the spreadsheet and split into sections based on the section headings.
	 * 
	 * Then extract the application and page information.
	 * 
	 * Then create the rule files.
	 * 
	 * @param sectionHeadingNames
	 * @return
	 * 			true if everything went OK
	 */
	protected boolean processData(String[] sectionHeadingNames) {
		List<SpreadsheetSection> sections = new SpreadsheetSectionSplitter(sectionHeadingNames).splitIntoSections(wbData);
		
		application = new ExtractApplication(sections).processApp();
		if (application == null) {
			logger.debug("No Application Object Created");
			return false;
		}
		
		if (!new ExtractPages(sections, application).processPages()) {
			logger.debug("Page Extraction failed");
			return false;
		}
		
		application.processTableEntries();
		
		return createRuleFiles();
	}
	
	/**
	 * Write the data to drl files in the output directory.
	 * 
	 * @return
	 */
	protected boolean createRuleFiles() {
		boolean processed = new ApplicationTemplate(application).generateDRLFile(outputDirectory, importDirectory, seperatePageDirectories);
		if (!processed) {
			logger.debug("Failed to create rule files");
		}
		return processed;
	}
}
