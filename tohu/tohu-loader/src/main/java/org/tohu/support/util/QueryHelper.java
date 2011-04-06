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
package org.tohu.support.util;

import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.QueryResults;
import org.drools.runtime.rule.QueryResultsRow;
import org.tohu.support.TohuDataItemObject;

/**
 * TODO need to decide what goes in here and whether this is the right place for it
 *
 * TODO javadoc
 *
 * @author Derek Rendall
 */
public class QueryHelper {

	StatefulKnowledgeSession knowledgeSession;

	public QueryHelper(StatefulKnowledgeSession knowledgeSession) {
		this.knowledgeSession = knowledgeSession;
	}

	/**
	 * Returns a map of Tohu Data Items (and sub classes) ids and answers sorted by id.
	 *
	 * @return
	 */
	public SortedMap<String, Object> getTohuDataItems() {
		SortedMap<String, Object> dataItems = new TreeMap<String, Object>();
		QueryResults queryResults = knowledgeSession.getQueryResults("tohuDataItems");
		for (Iterator<QueryResultsRow> iterator = queryResults.iterator(); iterator.hasNext();) {
			QueryResultsRow row = iterator.next();
			TohuDataItemObject obj = (TohuDataItemObject) row.get("tohuDataItem");
			dataItems.put(obj.getId(), obj.getAnswer());
		}
		return dataItems;
	}

}
