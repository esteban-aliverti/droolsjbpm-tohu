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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Arrays;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.FactHandle;
import org.junit.Before;
import org.junit.Test;
import org.tohu.Group;
import org.tohu.Question;
import org.tohu.Questionnaire;

/**
 * @author Damon
 * 
 */
public class ReadOnlyTest {

	private KnowledgeBase knowledgeBase;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		knowledgeBuilder.add(ResourceFactory.newClassPathResource("org/tohu/Active.drl"), ResourceType.DRL);
		knowledgeBuilder.add(ResourceFactory.newClassPathResource("org/tohu/Queries.drl"), ResourceType.DRL);
		knowledgeBuilder.add(ResourceFactory.newClassPathResource("org/tohu/ReadOnly.drl"), ResourceType.DRL);
		assertFalse(knowledgeBuilder.hasErrors());
		knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
		knowledgeBase.addKnowledgePackages(knowledgeBuilder.getKnowledgePackages());
	}

	@Test
	public void testReadOnly() {
		StatefulKnowledgeSession knowledgeSession = knowledgeBase.newStatefulKnowledgeSession();
		try {
			Questionnaire questionnaire = new Questionnaire("questionnaire");
			questionnaire.setItems(new String[]{"group1","group2","group3", "group4"});
			Group group1 = new Group("group1");
			group1.setItems(new String[]{"question1"});
			group1.setPresentationStyles(new String[]{"readonly"});
			Group group2 = new Group("group2");
			group2.setItems(new String[]{"question1"});
			Group group3 = new Group("group3");
			group3.setItems(new String[]{"group3a"});
			group3.setPresentationStyles(new String[]{"readonly"});
			Group group3a = new Group("group3a");
			group3a.setItems(new String[]{"question1"});
			Group group4 = new Group("group4");
			group4.setItems(new String[]{"group4a","group4b"});
			Group group4a = new Group("group4a");
			group4a.setItems(new String[]{"question1"});
			Group group4b = new Group("group4b");
			group4b.setItems(new String[]{"question1"});
			group4b.setPresentationStyles(new String[]{"readonly"});
			Question question1 = new Question("question1");
			question1.setAnswerType(Question.TYPE_TEXT);
			FactHandle handleQuestionnaire = knowledgeSession.insert(questionnaire);
			knowledgeSession.insert(group1);
			knowledgeSession.insert(group2);
			knowledgeSession.insert(group3);
			knowledgeSession.insert(group3a);
			knowledgeSession.insert(group4);
			knowledgeSession.insert(group4a);
			knowledgeSession.insert(group4b);
			knowledgeSession.insert(question1);
			knowledgeSession.fireAllRules();

			assertEquals(true, isReadOnlyInherited(question1));

			questionnaire.setActiveItem("group1");
			knowledgeSession.update(handleQuestionnaire, questionnaire);
			knowledgeSession.fireAllRules();
			assertEquals(true, isReadOnlyInherited(question1));

			questionnaire.setActiveItem("group2");
			knowledgeSession.update(handleQuestionnaire, questionnaire);
			knowledgeSession.fireAllRules();
			assertEquals(false, isReadOnlyInherited(question1));

			questionnaire.setActiveItem("group3");
			knowledgeSession.update(handleQuestionnaire, questionnaire);
			knowledgeSession.fireAllRules();
			assertEquals(true, isReadOnlyInherited(question1));

			questionnaire.setActiveItem("group4");
			knowledgeSession.update(handleQuestionnaire, questionnaire);
			knowledgeSession.fireAllRules();
			assertEquals(true, isReadOnlyInherited(question1));
		} finally {
			knowledgeSession.dispose();
		}
	}

	private boolean isReadOnlyInherited(Question question) {
		return question.getPresentationStyles() != null && Arrays.asList(question.getPresentationStyles()).contains("readonly-inherited");
	}
}
