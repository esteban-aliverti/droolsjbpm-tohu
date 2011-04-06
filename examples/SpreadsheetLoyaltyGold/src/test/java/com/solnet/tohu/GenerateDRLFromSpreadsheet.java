package com.solnet.tohu;

import org.tohu.load.questionnaire.TohuSpreadsheetLoader;

public class GenerateDRLFromSpreadsheet {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TohuSpreadsheetLoader loader = new TohuSpreadsheetLoader();
		if (!loader.processFile("./src/main/resources/spreadsheet/LoyaltyGoldDecisionTree.xls", "./src/main/resources/rules", "./src/main/resources/spreadsheet", false)) {
			System.out.println("File Not Processed");
		}

	}

}
