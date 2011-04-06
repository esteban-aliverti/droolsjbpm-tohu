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
package org.tohu.write.questionnaire;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Formatter;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tohu.domain.questionnaire.Application;
import org.tohu.domain.questionnaire.Page;
import org.tohu.domain.questionnaire.PageElement;
import org.tohu.domain.questionnaire.conditions.PageElementCondition;
import org.tohu.domain.questionnaire.framework.ListEntryTuple;
import org.tohu.support.TohuDataItemObject;
import org.tohu.write.questionnaire.helpers.CopyrightWriter;

/**
 * Write out an {@link Application} object to a drl file.
 * 
 *   Will also include Global Impact (see {@link TohuDataItemObject}) definitions, 
 *   the logic associated with conditional table entry items and 
 *   files that were to be included, based on the Spreadsheet's list of files to include.
 *   
 *   A file in the import directory called <code>Copyright.drl</code> will be included at the top
 *   of the drl file.
 * 
 * @author Derek Rendall
 */
public class ApplicationTemplate {
	
	private static final Logger logger = LoggerFactory.getLogger(ApplicationTemplate.class);
	
	private Application app;
	
	public ApplicationTemplate(Application application) {
		super();
		app = application;
	}
	
	/**
	 * Make sure that the directories and files exist, setting up for actually writing the drl file.
	 * Will include copyright and import files (simple read line and then write line).
	 * 
	 * @param directory
	 * @param importDirectory
	 * @param seperatePageDirectories
	 * @return
	 */
	public boolean generateDRLFile(String directory, String importDirectory, boolean seperatePageDirectories) {
	    String fileName = directory + "/" + app.getId().replace(' ', '_') + ".drl";
	    try {
	    	File outdir = new File(directory);
	        
	        //Basic directory existence checks
	        if (outdir.exists() && !outdir.isDirectory()) {
	            throw new IOException(directory + " is not a valid directory.");
	        }
	        
	        // create the directory if it doesn't exist.
	        if(!outdir.exists()) {
	            if(!outdir.mkdir()) {
	            	throw new IOException("Unable to create directory: " + directory);
	            }
	        }
	        Formatter fmtFile;
	        fmtFile = new Formatter(new FileOutputStream(fileName));
	        CopyrightWriter.writeCopyright(fmtFile, importDirectory);
	        writeDRLFileContents(fmtFile);
	        
		    if (importDirectory != null) {
		    	File importDir = new File(importDirectory);
		        if(!importDir.exists()) {
	            	throw new IOException("Unable to access import directory: " + importDirectory);
		        }
		        if (!importDirectory.endsWith("/") || !importDirectory.endsWith("\\")) {
					importDirectory = importDirectory + "/";
				}
		        for (Iterator<String> imports = app.getImports().iterator(); imports.hasNext();) {
					String importFileName = imports.next();
			    	File importFile = new File(importDirectory + importFileName);
			    	if (!importFile.exists()) {
			    		throw new IOException("The import file does not exist: " + importDirectory + importFileName);
			    	}
			        BufferedReader reader = new BufferedReader(new FileReader(importFile));

			        //... Loop as long as there are input lines.
			        String line = null;
			        while ((line=reader.readLine()) != null) {
			        	fmtFile.format("%s\n", line);
			        }

			        //... Close reader and writer.
			        reader.close();  // Close to unlock.
				}
		    }
	        
	        fmtFile.close();
		} catch (IOException e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
			return false;
		}
	    logger.debug("The " + fileName + " file has been written");  
	    int count = 1;
		for (Iterator<Page> iterator = app.getPageList().iterator(); iterator.hasNext();) {
			Page pg = iterator.next();
			boolean processed = new PageTemplate(pg).generateDRLFile(app, directory, importDirectory, count, seperatePageDirectories);
			if (!processed) {
				return false;
			}
			count++;
		}

	    return true;
	}
	
	/**
	 * Includes writing the import (class) statements, and the Questionnaire definition.
	 * 
	 * @param fmt
	 * @throws IOException
	 */
	public void writeDRLFileContents(Formatter fmt) throws IOException {
	    fmt.format("package %s;\n\n", app.getApplicationClass());
	    fmt.format("import org.tohu.Group;\n");
	    fmt.format("import org.tohu.InvalidAnswer;\n");
	    fmt.format("import org.tohu.MultipleChoiceQuestion;\n");
	    fmt.format("import org.tohu.MultipleChoiceQuestion.PossibleAnswer;\n");
	    fmt.format("import org.tohu.Note;\n");
	    fmt.format("import org.tohu.Question;\n");
	    fmt.format("import org.tohu.Answer;\n");
	    fmt.format("import org.tohu.Questionnaire;\n");
	    fmt.format("import org.tohu.support.TohuDataItemObject;\n\n");

	    fmt.format("import java.text.SimpleDateFormat;\n");
	    fmt.format("import java.util.Calendar;\n");
	    fmt.format("import java.util.Date;\n");
	    fmt.format("import java.util.Arrays;\n\n");
	    
	    // TODO add in ability to add additional import statements?
	    // Maybe have an import filename as well as include filenames
	    
	    fmt.format("rule \"%s\"\ndialect \"mvel\"\n", app.getId());
	    fmt.format("then\n");
	    fmt.format("\tQuestionnaire questionnaire = new Questionnaire(\"%s\");\n", app.getId());
	    fmt.format("\tquestionnaire.setLabel(\"%s\");\n", app.getApplicationName());
	    fmt.format("\tquestionnaire.setCompletionAction(\"%s\");\n", app.getCompletionAction());
	    fmt.format("\tquestionnaire.setItems({%s});\n", app.getItemList());
	    if (app.getActivePage() != null) {
	    	fmt.format("\tquestionnaire.setActiveItem(\"%s\");\n", app.getActivePage());
	    }
	    fmt.format("\tquestionnaire.setEnableActionValidation(%s);\n", app.getActionValidation());
	    fmt.format("\tquestionnaire.setMarkupAllowed(%s);\n", app.getMarkupAllowed());
	    fmt.format("\tinsertLogical(questionnaire);\n");
	    
	    fmt.format("end\n\n");
	    
	    
	    for (Iterator<PageElement> i = app.getGlobalElements().iterator(); i.hasNext();) {
	    	PageElement element = (PageElement) i.next();
			new PageElementTemplate(element).writeDRLFileContents(app, fmt);
		}
	    
	    // Used as a class to control insert/remove PossibleAnswer entry logic
	    fmt.format("declare ListEntryFact\n");
	    fmt.format("\tid : String @key\n");
	    fmt.format("end\n\n");

	    // Now create any PossibleAnswer insert/remove logic, if there are any tuples with some logic
		for (Iterator<Page> iterator = app.getPageList().iterator(); iterator.hasNext();) {
			Page pg = iterator.next();
			processConditionalTableElement(fmt, pg.getParentPageElement());
		}
	    
	}
	
	
	/**
	 * Recursively find all list tuples with conditional logic, and then call {@link #writeConditionalTableElement(Formatter, PageElement, ListEntryTuple, int)}
	 * 
	 * @param fmt
	 * @param element
	 * @throws IOException
	 */
	protected void processConditionalTableElement(Formatter fmt, PageElement element) throws IOException {
		if (element == null) {
			return;
		}
		if ((element.getLookupTable() != null) && (element.getLookupTable().getEntries() != null)) {
			int count = 0;
			for (Iterator<ListEntryTuple> iterator = element.getLookupTable().getEntries().iterator(); iterator.hasNext();) {
				ListEntryTuple tuple = (ListEntryTuple) iterator.next();
				count++;
				if (tuple.getConditionClause() != null) {
					writeConditionalTableElement(fmt, element, tuple, count);
				}
			}
		}
		
		for (Iterator<PageElement> iterator = element.getChildren().iterator(); iterator.hasNext();) {
			PageElement child = (PageElement) iterator.next();
			processConditionalTableElement(fmt, child);
		}
	}
	

	
	/**
	 * Writes the drl logic for one tuple:
	 * <p>
	 * The controlling fact is written:<br>
	 * The conditional logic is used to create a named instance of the (dynamically defined) ListEntryFact object.
	 * </p>
	 * 
	 * <p>
	 * The insert rule is written:<br>
	 * If the controlling fact exists but the entry is not in the list of objects, <br>
	 * then insert a new PossibleAnswer into the list, at rowNumber
	 * </p>
	 * 
	 * <p>
	 * The remove rule is written:<br>
	 * If the controlling fact no longer exists but the entry is still in the list of objects, <br>
	 * then remove the associated PossibleAnswer from the list
	 * </p>
	 * 
	 * @param fmt
	 * @param element
	 * @param tuple
	 * @param rowNumber
	 * 			Important for distinguishing id and rule names if there is more than one conditional tuple for a list
	 * @throws IOException
	 */
	protected void writeConditionalTableElement(Formatter fmt, PageElement element, ListEntryTuple tuple, int rowNumber) throws IOException {
		String displayFactId = element.getId() + "row" + String.valueOf(rowNumber);
		
	    fmt.format("rule \"Trigger %s\"\n", displayFactId);
	    fmt.format("salience 50\n");
	    fmt.format("when\n");
	    
	    PageElementCondition c = new PageElementCondition(PageElementCondition.TYPE_INCLUSION, displayFactId, rowNumber);
	    c.addElement(tuple.getConditionClause());
	    PageElement pe = new PageElement();
	    pe.setId(element.getId(), -1, rowNumber);
	    pe.setDisplayCondition(c);
	    new WhenClauseTemplate(pe).writeLogicSectionDRLFileContents(app, fmt, false);
		
	    fmt.format("then\n");
	    fmt.format("\tListEntryFact lef = new ListEntryFact();\n");
	    fmt.format("\tlef.setId(\"%s\");\n", displayFactId);
	    fmt.format("\tinsertLogical(lef);\n");
	    fmt.format("end\n\n");
	    
	    fmt.format("rule \"Include %s\"\n", displayFactId);
	    fmt.format("salience 25\n");
	    fmt.format("no-loop\n");
	    fmt.format("when\n");
	    fmt.format("\tlef : ListEntryFact(id == \"%s\")\n", displayFactId);
	    fmt.format("\tmcq : MultipleChoiceQuestion(id == \"%s\")\n", element.getId());
	    fmt.format("\teval(!mcq.hasPossibleAnswer(\"%s\"))\n", tuple.getId());
	    fmt.format("then\n");
	    fmt.format("\tmcq.insertPossibleAnswer(new PossibleAnswer(\"%s\", \"%s\"), %d);\n", tuple.getId(), tuple.getRepresentation(), rowNumber);
	    fmt.format("\tupdate(mcq);\n");
	    fmt.format("end\n\n");
	    
	    fmt.format("rule \"Remove %s\"\n", displayFactId);
	    fmt.format("salience 15\n");
	    fmt.format("no-loop\n");
	    fmt.format("when\n");
	    fmt.format("\tnot lef : ListEntryFact(id == \"%s\")\n", displayFactId);
	    fmt.format("\tmcq : MultipleChoiceQuestion(id == \"%s\")\n", element.getId());
	    fmt.format("\teval(mcq.hasPossibleAnswer(\"%s\"))\n", tuple.getId());
	    fmt.format("then\n");
	    fmt.format("\tmcq.removePossibleAnswer(\"%s\");\n", tuple.getId());
	    fmt.format("\tupdate(mcq);\n");
	    fmt.format("end\n\n");
	}
	
}
