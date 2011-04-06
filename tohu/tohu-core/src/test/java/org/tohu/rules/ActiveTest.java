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
package org.tohu.rules;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.FactHandle;
import org.drools.runtime.rule.QueryResults;
import org.drools.runtime.rule.QueryResultsRow;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tohu.Group;
import org.tohu.Item;
import org.tohu.Note;
import org.tohu.Questionnaire;

/**
 * @author Damon
 * 
 *         TODO add tests for InvalidAnswer - should be active only if question is active (and exists of course)
 */
public class ActiveTest {

	private static final Logger logger = LoggerFactory.getLogger(ActiveTest.class);
	
	private KnowledgeBase knowledgeBase;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		knowledgeBuilder.add(ResourceFactory.newClassPathResource("org/tohu/Active.drl"), ResourceType.DRL);
		knowledgeBuilder.add(ResourceFactory.newClassPathResource("org/tohu/Queries.drl"), ResourceType.DRL);
		logger.debug(Arrays.toString(knowledgeBuilder.getErrors().toArray()));
		assertFalse(knowledgeBuilder.hasErrors());
		knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
		knowledgeBase.addKnowledgePackages(knowledgeBuilder.getKnowledgePackages());
	}

	@Test
	public void testActiveObjects() {
		StatefulKnowledgeSession knowledgeSession = knowledgeBase.newStatefulKnowledgeSession();
		try {
			Questionnaire questionnaire = new Questionnaire("questionnaire");
			questionnaire.setItems(new String[] { "group1", "group2", "note4" });
			Group group1 = new Group("group1");
			group1.setItems(new String[] { "note1", "note2" });
			Group group2 = new Group("group2");
			group2.setItems(new String[] { "note3" });
			Note note1 = new Note("note1");
			Note note2 = new Note("note2");
			Note note3 = new Note("note3");
			Note note4 = new Note("note4");
			Note note5 = new Note("note5");
			Note note6 = new Note("note6");
			FactHandle handleQuestionnaire = knowledgeSession.insert(questionnaire);
			FactHandle handleGroup1 = knowledgeSession.insert(group1);
			knowledgeSession.insert(group2);
			knowledgeSession.insert(note1);
			knowledgeSession.insert(note2);
			knowledgeSession.insert(note3);
			knowledgeSession.insert(note4);
			knowledgeSession.insert(note5);
			knowledgeSession.insert(note6);
			knowledgeSession.fireAllRules();

			QueryResults queryResults = knowledgeSession.getQueryResults("activeObjects");
			assertArrayEquals(new String[] { "object" }, queryResults.getIdentifiers());
			Set<String> itemIds = getItemIds(queryResults);
			assertEquals(new HashSet<String>(Arrays.asList(new String[] { "questionnaire", "group1", "group2", "note1", "note2",
					"note3", "note4", "note5", "note6" })), itemIds);

			questionnaire.setActiveItem("group1");
			knowledgeSession.update(handleQuestionnaire, questionnaire);
			knowledgeSession.fireAllRules();
			queryResults = knowledgeSession.getQueryResults("activeObjects");
			itemIds = getItemIds(queryResults);
			assertEquals(new HashSet<String>(Arrays.asList(new String[] { "questionnaire", "group1", "note1", "note2" })),
					itemIds);

			questionnaire.setActiveItem("group2");
			knowledgeSession.update(handleQuestionnaire, questionnaire);
			knowledgeSession.fireAllRules();
			queryResults = knowledgeSession.getQueryResults("activeObjects");
			itemIds = getItemIds(queryResults);
			assertEquals(new HashSet<String>(Arrays.asList(new String[] { "questionnaire", "group2", "note3" })), itemIds);

			questionnaire.setActiveItem("note4");
			knowledgeSession.update(handleQuestionnaire, questionnaire);
			knowledgeSession.fireAllRules();
			queryResults = knowledgeSession.getQueryResults("activeObjects");
			itemIds = getItemIds(queryResults);
			assertEquals(new HashSet<String>(Arrays.asList(new String[] { "questionnaire", "note4" })), itemIds);

			questionnaire.setActiveItem("unknown");
			knowledgeSession.update(handleQuestionnaire, questionnaire);
			knowledgeSession.fireAllRules();
			queryResults = knowledgeSession.getQueryResults("activeObjects");
			itemIds = getItemIds(queryResults);
			assertEquals(new HashSet<String>(Arrays.asList(new String[] { "questionnaire" })), itemIds);

			questionnaire.setActiveItem("group1");
			knowledgeSession.update(handleQuestionnaire, questionnaire);
			group1.setItems(new String[] { "note1", "note2", "note5" });
			knowledgeSession.update(handleGroup1, group1);
			knowledgeSession.fireAllRules();
			queryResults = knowledgeSession.getQueryResults("activeObjects");
			itemIds = getItemIds(queryResults);
			assertEquals(
					new HashSet<String>(Arrays.asList(new String[] { "questionnaire", "group1", "note1", "note2", "note5" })),
					itemIds);

			group1.setItems(new String[] { "note1", "note2" });
			knowledgeSession.update(handleGroup1, group1);
			knowledgeSession.fireAllRules();
			queryResults = knowledgeSession.getQueryResults("activeObjects");
			itemIds = getItemIds(queryResults);
			assertEquals(new HashSet<String>(Arrays.asList(new String[] { "questionnaire", "group1", "note1", "note2" })),
					itemIds);
		} finally {
			knowledgeSession.dispose();
		}
	}

	@Test
	public void testDuplicateSameGroup() {
		StatefulKnowledgeSession knowledgeSession = knowledgeBase.newStatefulKnowledgeSession();
		try {
			Questionnaire questionnaire = new Questionnaire("questionnaire");
			questionnaire.setItems(new String[] { "group1", "group2" });
			Group group1 = new Group("group1");
			group1.setItems(new String[] { "note1", "note1" });
			Group group2 = new Group("group2");
			group2.setItems(new String[] { "note2" });
			Note note1 = new Note("note1");
			Note note2 = new Note("note2");
			FactHandle handleQuestionnaire = knowledgeSession.insert(questionnaire);
			knowledgeSession.insert(group1);
			knowledgeSession.insert(group2);
			knowledgeSession.insert(note1);
			knowledgeSession.insert(note2);
			knowledgeSession.fireAllRules();

			QueryResults queryResults = knowledgeSession.getQueryResults("activeObjects");
			assertArrayEquals(new String[] { "object" }, queryResults.getIdentifiers());
			Set<String> itemIds = getItemIds(queryResults);
			assertEquals(new HashSet<String>(Arrays
					.asList(new String[] { "questionnaire", "group1", "group2", "note1", "note2" })), itemIds);

			questionnaire.setActiveItem("group1");
			knowledgeSession.update(handleQuestionnaire, questionnaire);
			knowledgeSession.fireAllRules();
			queryResults = knowledgeSession.getQueryResults("activeObjects");
			itemIds = getItemIds(queryResults);
			assertEquals(new HashSet<String>(Arrays.asList(new String[] { "questionnaire", "group1", "note1" })), itemIds);

			questionnaire.setActiveItem("group2");
			knowledgeSession.update(handleQuestionnaire, questionnaire);
			knowledgeSession.fireAllRules();
			queryResults = knowledgeSession.getQueryResults("activeObjects");
			itemIds = getItemIds(queryResults);
			assertEquals(new HashSet<String>(Arrays.asList(new String[] { "questionnaire", "group2", "note2" })), itemIds);

			questionnaire.setActiveItem("unknown");
			knowledgeSession.update(handleQuestionnaire, questionnaire);
			knowledgeSession.fireAllRules();
			queryResults = knowledgeSession.getQueryResults("activeObjects");
			itemIds = getItemIds(queryResults);
			assertEquals(new HashSet<String>(Arrays.asList(new String[] { "questionnaire" })), itemIds);
		} finally {
			knowledgeSession.dispose();
		}
	}

	@Test
	public void testDuplicateSamePage() {
		StatefulKnowledgeSession knowledgeSession = knowledgeBase.newStatefulKnowledgeSession();
		try {
			Questionnaire questionnaire = new Questionnaire("questionnaire");
			questionnaire.setItems(new String[] { "group1", "group2" });
			Group group1 = new Group("group1");
			group1.setItems(new String[] { "group3", "group4" });
			Group group2 = new Group("group2");
			group2.setItems(new String[] { "note2" });
			Group group3 = new Group("group3");
			group3.setItems(new String[] { "note1" });
			Group group4 = new Group("group4");
			group4.setItems(new String[] { "note1" });
			Note note1 = new Note("note1");
			Note note2 = new Note("note2");
			FactHandle handleQuestionnaire = knowledgeSession.insert(questionnaire);
			knowledgeSession.insert(group1);
			knowledgeSession.insert(group2);
			knowledgeSession.insert(group3);
			knowledgeSession.insert(group4);
			knowledgeSession.insert(note1);
			knowledgeSession.insert(note2);
			knowledgeSession.fireAllRules();

			QueryResults queryResults = knowledgeSession.getQueryResults("activeObjects");
			assertArrayEquals(new String[] { "object" }, queryResults.getIdentifiers());
			Set<String> itemIds = getItemIds(queryResults);
			assertEquals(new HashSet<String>(Arrays.asList(new String[] { "questionnaire", "group1", "group2", "group3",
					"group4", "note1", "note2" })), itemIds);

			questionnaire.setActiveItem("group1");
			knowledgeSession.update(handleQuestionnaire, questionnaire);
			knowledgeSession.fireAllRules();
			queryResults = knowledgeSession.getQueryResults("activeObjects");
			itemIds = getItemIds(queryResults);
			assertEquals(new HashSet<String>(Arrays
					.asList(new String[] { "questionnaire", "group1", "group3", "group4", "note1" })), itemIds);

			questionnaire.setActiveItem("group2");
			knowledgeSession.update(handleQuestionnaire, questionnaire);
			knowledgeSession.fireAllRules();
			queryResults = knowledgeSession.getQueryResults("activeObjects");
			itemIds = getItemIds(queryResults);
			assertEquals(new HashSet<String>(Arrays.asList(new String[] { "questionnaire", "group2", "note2" })), itemIds);

			questionnaire.setActiveItem("unknown");
			knowledgeSession.update(handleQuestionnaire, questionnaire);
			knowledgeSession.fireAllRules();
			queryResults = knowledgeSession.getQueryResults("activeObjects");
			itemIds = getItemIds(queryResults);
			assertEquals(new HashSet<String>(Arrays.asList(new String[] { "questionnaire" })), itemIds);
		} finally {
			knowledgeSession.dispose();
		}
	}

	@Test
	public void testDuplicateDifferentPage() {
		StatefulKnowledgeSession knowledgeSession = knowledgeBase.newStatefulKnowledgeSession();
		try {
			Questionnaire questionnaire = new Questionnaire("questionnaire");
			questionnaire.setItems(new String[] { "group1", "group2" });
			Group group1 = new Group("group1");
			group1.setItems(new String[] { "note1" });
			Group group2 = new Group("group2");
			group2.setItems(new String[] { "note1", "note2" });
			Note note1 = new Note("note1");
			Note note2 = new Note("note2");
			FactHandle handleQuestionnaire = knowledgeSession.insert(questionnaire);
			knowledgeSession.insert(group1);
			knowledgeSession.insert(group2);
			knowledgeSession.insert(note1);
			knowledgeSession.insert(note2);
			knowledgeSession.fireAllRules();

			QueryResults queryResults = knowledgeSession.getQueryResults("activeObjects");
			assertArrayEquals(new String[] { "object" }, queryResults.getIdentifiers());
			Set<String> itemIds = getItemIds(queryResults);
			assertEquals(new HashSet<String>(Arrays
					.asList(new String[] { "questionnaire", "group1", "group2", "note1", "note2" })), itemIds);

			questionnaire.setActiveItem("group1");
			knowledgeSession.update(handleQuestionnaire, questionnaire);
			knowledgeSession.fireAllRules();
			queryResults = knowledgeSession.getQueryResults("activeObjects");
			itemIds = getItemIds(queryResults);
			assertEquals(new HashSet<String>(Arrays.asList(new String[] { "questionnaire", "group1", "note1" })), itemIds);

			questionnaire.setActiveItem("group2");
			knowledgeSession.update(handleQuestionnaire, questionnaire);
			knowledgeSession.fireAllRules();
			queryResults = knowledgeSession.getQueryResults("activeObjects");
			itemIds = getItemIds(queryResults);
			assertEquals(new HashSet<String>(Arrays.asList(new String[] { "questionnaire", "group2", "note1", "note2" })),
					itemIds);

			questionnaire.setActiveItem("unknown");
			knowledgeSession.update(handleQuestionnaire, questionnaire);
			knowledgeSession.fireAllRules();
			queryResults = knowledgeSession.getQueryResults("activeObjects");
			itemIds = getItemIds(queryResults);
			assertEquals(new HashSet<String>(Arrays.asList(new String[] { "questionnaire" })), itemIds);
		} finally {
			knowledgeSession.dispose();
		}
	}

	@Test
	public void testNavigationReturn() {
		StatefulKnowledgeSession knowledgeSession = knowledgeBase.newStatefulKnowledgeSession();
		try {
			Questionnaire questionnaire = new Questionnaire("questionnaire");
			questionnaire.setItems(new String[] { "group1", "group2" });
			questionnaire.setActiveItem("group1");
			Group group1 = new Group("group1");
			group1.setItems(new String[] { "note1" });
			Group group2 = new Group("group2");
			group2.setItems(new String[] { "note1", "note2" });
			Group group3 = new Group("group3");
			group3.setItems(new String[] { "note3" });
			Note note1 = new Note("note1");
			Note note2 = new Note("note2");
			Note note3 = new Note("note3");
			FactHandle handleQuestionnaire = knowledgeSession.insert(questionnaire);
			knowledgeSession.insert(group1);
			knowledgeSession.insert(group2);
			knowledgeSession.insert(group3);
			knowledgeSession.insert(note1);
			knowledgeSession.insert(note2);
			knowledgeSession.insert(note3);
			knowledgeSession.fireAllRules();

			QueryResults queryResults = knowledgeSession.getQueryResults("activeObjects");
			assertArrayEquals(new String[] { "object" }, queryResults.getIdentifiers());
			Set<String> itemIds = getItemIds(queryResults);
			assertEquals(new HashSet<String>(Arrays.asList(new String[] { "questionnaire", "group1", "note1" })), itemIds);

			questionnaire.navigationBranch(new String[] { "group3" }, "group3");
			knowledgeSession.update(handleQuestionnaire, questionnaire);
			knowledgeSession.fireAllRules();
			queryResults = knowledgeSession.getQueryResults("activeObjects");
			itemIds = getItemIds(queryResults);
			assertEquals(new HashSet<String>(Arrays.asList(new String[] { "questionnaire", "group3", "note3" })), itemIds);

			questionnaire.setActiveItem(Questionnaire.COMPLETION_ACTION_RETURN);
			knowledgeSession.update(handleQuestionnaire, questionnaire);
			knowledgeSession.fireAllRules();
			queryResults = knowledgeSession.getQueryResults("activeObjects");
			itemIds = getItemIds(queryResults);
			assertEquals(new HashSet<String>(Arrays.asList(new String[] { "questionnaire", "group1", "note1" })), itemIds);
		} finally {
			knowledgeSession.dispose();
		}
	}

	@Test
	public void testAvailableItems() {
		StatefulKnowledgeSession knowledgeSession = knowledgeBase.newStatefulKnowledgeSession();
		try {
			Questionnaire questionnaire = new Questionnaire("questionnaire");
			questionnaire.setItems(new String[] { "group1", "group2", "group3", "group4", "group5", "group6", "group7" });
			Group group2 = new Group("group2");
			Group group5 = new Group("group5");
			Group group6 = new Group("group6");
			knowledgeSession.insert(questionnaire);
			knowledgeSession.insert(group2);
			knowledgeSession.insert(group5);
			knowledgeSession.insert(group6);
			knowledgeSession.fireAllRules();
			assertArrayEquals(new String[] { "group2", "group5", "group6" }, questionnaire.getAvailableItems());
		} finally {
			knowledgeSession.dispose();
		}
	}

	private Set<String> getItemIds(QueryResults queryResults) {
		Set<String> itemIds = new HashSet<String>();
		for (Iterator<QueryResultsRow> iterator = queryResults.iterator(); iterator.hasNext();) {
			QueryResultsRow row = iterator.next();
			Item item = (Item) row.get("object");
			itemIds.add(item.getId());
		}
		return itemIds;
	}
}
