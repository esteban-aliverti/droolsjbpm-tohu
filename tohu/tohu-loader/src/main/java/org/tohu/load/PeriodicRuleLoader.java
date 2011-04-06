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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.tohu.load.questionnaire.TohuSpreadsheetLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Monitors a file every few seconds and if it is modified then the Tohu rules
 * are regenerated.
 * 
 * @author David Plumpton
 */
public class PeriodicRuleLoader {

	private static final Logger logger = LoggerFactory.getLogger(PeriodicRuleLoader.class);
	
	/** Name of the rule file */
	private String ruleFile;
	/** Temporary working directory */
	private String outputDir;
	/** Directory to import any needed resources from */
	private String importDir;
	/** Final destination directory to copy files to */
	private String droolsDir;
	/** Scan interval */
	private int seconds = 5;
	/** When last updated */
	private long lastUpdateTime;
	
	/**
	 * Intended to be run via "mvn exec:java"
	 * @param args see below for details
	 * args are:
	 * <ol>
	 * <li>Rule file</li>
	 * <li>Temp output directory</li>
	 * <li>Import directory</li>
	 * <li>Drools directory</li>
	 */
	public static void main(String [] args) {
		if (args.length != 4) {
			throw new IllegalArgumentException("Must have three arguments");
		}
		PeriodicRuleLoader periodic = new PeriodicRuleLoader(args[0], args[1], args[2], args[3]);
		periodic.start();
	}
	
	public PeriodicRuleLoader(String ruleFile, String outputDir, String importDir, String droolsDir) {
		this.ruleFile = ruleFile;
		this.outputDir = outputDir;
		this.importDir = importDir;
		this.droolsDir = droolsDir;
		this.lastUpdateTime = System.currentTimeMillis();
	}

	public void setPeriodSeconds(int seconds) {
		this.seconds = seconds;
	}

	/**
	 * Loop forever looking periodically for updates
	 */
	public void start() {
		boolean abort = false;
		File file = new File(ruleFile);
		if (!file.exists()) {
			abort = true;
			logger.error("ERROR: Rule File does not exist: " + file.getAbsolutePath());
		}
		file = new File(outputDir);
		if (!file.exists()) {
			logger.warn("Warning: Output Directory does not exist: " + file.getAbsolutePath());
		}

		file = new File(importDir);
		if (!file.exists()) {
			abort = true;
			logger.error("ERROR: Import Directory does not exist: " + file.getAbsolutePath());
		}
		
		file = new File(droolsDir);
		if (!file.exists()) {
			logger.warn("Warning: Drools Directory does not exist: " + file.getAbsolutePath());
		}
		
		if (abort) {
			throw new IllegalArgumentException("Required file or directory does not exist: " + ruleFile + " or " + importDir);
		}
		
		logger.info("\n\nScanning every " + seconds + " seconds ...\n");
		
		for (;;) {
			try {
				Thread.sleep(seconds * 1000L);
				examineRules();
			} catch (InterruptedException ignore) {
			}
		}
	}
	
	/**
	 * Examples rules for any changes, handle if necessary
	 */
	void examineRules() {
		File file = new File(ruleFile);
		logger.debug("Examining Rules");
		if (file.lastModified() > lastUpdateTime) {
			logger.info("Rules updated, start conversion");
			lastUpdateTime = file.lastModified();
			try {
				// Wait another second just in case the converter is still streaming data to the file
				Thread.sleep(1000L);
			} catch (InterruptedException ignore) {
			}
			loadRules();
			logger.debug("Rules updated, move files to drools directory: " + droolsDir);
			moveRules();
			logger.debug("Move complete");
		} 
	}

	/**
	 * Use the Tohu loader to process the rules, output goes to the temp working directory
	 */
	boolean loadRules() {
		TohuSpreadsheetLoader loader = new TohuSpreadsheetLoader();
		return loader.processFile(ruleFile, outputDir, importDir);
	}

	/**
	 * Move the newly created .drl files into the drools directory (need to delete the existing files)
	 */
	void moveRules() {
		File dir = new File(outputDir);
		ArrayList<File> list = new ArrayList<File>();
		gatherAllRuleFiles(dir, list);
		
		File destination = new File(droolsDir);
		for (File file : list) {
			File destFile = new File(destination + File.separator + file.getName());
			destFile.delete();
			boolean result = file.renameTo(destFile);
			if (!result) {
				logger.error("Move failed for " + file.getName());
			}
		}
	}
	
	/**
	 * Recursively find all .drl files
	 * @param file starting point
	 * @param list build up a list of File objects
	 */
	void gatherAllRuleFiles(File file, List<File> list) {
		if (file.isDirectory()) {
			File[] contents = file.listFiles();
			for (File subFile : contents) {
				gatherAllRuleFiles(subFile, list);
			}
			return;
		}
		if (file.getName().endsWith(".drl")) {
			list.add(file);
		}
	}
}
