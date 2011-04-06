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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Formatter;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tohu.domain.questionnaire.Application;
import org.tohu.domain.questionnaire.Page;
import org.tohu.domain.questionnaire.PageElement;
import org.tohu.write.questionnaire.helpers.CopyrightWriter;

/**
 * Create the {@link Page} drl file, including import statements. Most of the actual writing is done by
 * using {@link PageElementTemplate} for each {@link PageElement}.
 * 
 *   A file in the import directory called <code>Copyright.drl</code> will be included at the top
 *   of the drl file.
 * 
 * @author Derek Rendall
 */
public class PageTemplate {
	
	private static final Logger logger = LoggerFactory.getLogger(PageTemplate.class);
	
	protected Page pg;
	
	public PageTemplate(Page page) {
		super();
		this.pg = page;
	}
	
	
	/**
	 * Create the page based DRL file
	 * 
	 * @param application
	 * @param directory
	 * @param importDirectory
	 * @param count
	 * @param seperatePageDirectories
	 * @return
	 */
	public boolean generateDRLFile(Application application, String directory, String importDirectory, int count, boolean seperatePageDirectories) {
		String pageNumber = String.valueOf(count);
		if (pageNumber.length() == 1) {
			pageNumber = "page0" + pageNumber;
		}
		else {
			pageNumber = "page" + pageNumber;
		}
		String subDirectory = directory + "/" + pageNumber;
		if (!seperatePageDirectories) {
			subDirectory = directory;
		}
	    String fileName = subDirectory + "/" + pg.getId().replace(' ', '_') + ".drl";
	    //logger.debug("Preparing to write file: " + fileName);
	    try {
	    	File outdir = new File(subDirectory);
	        
	        //Basic directory existence checks
	        if (outdir.exists() && !outdir.isDirectory()) {
	            throw new IOException(subDirectory + " is not a valid directory.");
	        }
	        
	        // create the directory if it doesn't exist.
	        if(!outdir.exists()) {
	            if(!outdir.mkdir()) {
	            	throw new IOException("Unable to create directory: " + subDirectory);
	            }
	        }
	        Formatter fmtFile;
	        fmtFile = new Formatter(new FileOutputStream(fileName));
	        CopyrightWriter.writeCopyright(fmtFile, importDirectory);
	        writeDRLFileContents(application, fmtFile);
	        fmtFile.close();
		} catch (IOException e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
			return false;
		}
	    logger.debug("The " + fileName + " file has been written");  
	    return true;
	}
	
	protected void writeDRLForPageElement(Application application, Formatter fmt, PageElement element) throws IOException {
		if (element.isAGroupType()) {
			for (Iterator<PageElement> i = element.getChildren().iterator(); i.hasNext();) {
				PageElement child = (PageElement) i.next();
				child.addGroupId(element.getId());
			}
		}
		new PageElementTemplate(element).writeDRLFileContents(application, fmt);
		for (Iterator<PageElement> i = element.getChildren().iterator(); i.hasNext();) {
			PageElement child = (PageElement) i.next();
			writeDRLForPageElement(application, fmt, child);
		}
	}
	
	protected void writeDRLFileContents(Application application, Formatter fmt) throws IOException {
	    fmt.format("package %s.%s;\n\n", application.getApplicationClass(), pg.getSheetName().replace(' ', '_').toLowerCase());
	    	    
	    fmt.format("import java.util.Calendar;\n");	// TODO only include if rule require it?
	    
	    fmt.format("import org.tohu.Group;\n");
	    fmt.format("import org.tohu.InvalidAnswer;\n");
	    fmt.format("import org.tohu.MultipleChoiceQuestion;\n");
	    fmt.format("import org.tohu.MultipleChoiceQuestion.PossibleAnswer;\n");
	    fmt.format("import org.tohu.Note;\n");
	    fmt.format("import org.tohu.Question;\n");
	    fmt.format("import org.tohu.Answer;\n");
	    fmt.format("import org.tohu.Questionnaire;\n");
	    fmt.format("import org.tohu.support.TohuDataItemObject;\n\n");
	    fmt.format("import %s.*;\n\n", application.getApplicationClass());	// needed for the definitions of the display facts	   
	    
	    if ((pg.getElements() != null) && (pg.getElements().size() > 0)) {
			writeDRLForPageElement(application, fmt, pg.getElements().get(0));
		}
	    
	}
}
