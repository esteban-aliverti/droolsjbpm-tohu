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
package org.tohu.domain.questionnaire.framework;

/**
 * Holds the Statics for the PageElement information.
 *
 * @author Derek Rendall
 */
public interface PageElementConstants {

	public static String ITEM_TYPE_REUSE = "Reuse";
	public static String ITEM_TYPE_GROUP = "Group";
	public static String ITEM_TYPE_QUESTION = "Question";
	public static String ITEM_TYPE_MULTI_CHOICE_Q = "MultipleChoiceQuestion";
	public static String ITEM_TYPE_NOTE = "Note";
	public static String ITEM_TYPE_VALIDATION = "Validation";
	public static String ITEM_TYPE_NORMAL_IMPACT = "Impact";
	public static String ITEM_TYPE_FUNCTION_IMPACT = "FunctionImpact";
	public static String ITEM_TYPE_ALTERNATE_IMPACT = "AlternateImpact";
	public static String ITEM_TYPE_DATA_ITEM = "TohuDataItemObject";
	
	public static String FIELD_TYPE_BOOLEAN = "Boolean";
	public static String FIELD_TYPE_TEXT = "Text";
	public static String FIELD_TYPE_NUMBER = "Number";
	public static String FIELD_TYPE_DECIMAL = "Decimal";
	public static String FIELD_TYPE_DATE = "Date";
	public static String FIELD_TYPE_LIST = "List";
	
	public static String TYPE_BOOLEAN = "Question.TYPE_BOOLEAN";
	public static String TYPE_TEXT = "Question.TYPE_TEXT";
	public static String TYPE_NUMBER = "Question.TYPE_NUMBER";
	public static String TYPE_DECIMAL = "Question.TYPE_DECIMAL";
	public static String TYPE_DATE = "Question.TYPE_DATE";
	public static String TYPE_LIST = "Question.TYPE_LIST";
	
	public static String FUNCTION_MAX = "max";
	public static String FUNCTION_MIN = "min";
	public static String FUNCTION_SUM = "sum";
	public static String FUNCTION_COUNT = "count";
	public static String FUNCTION_AVERAGE = "average";

}
