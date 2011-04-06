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
package org.tohu.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.drools.runtime.rule.FactHandle;
import org.junit.Before;
import org.junit.Test;
import org.tohu.Answer;
import org.tohu.InvalidAnswer;
import org.tohu.Item;
import org.tohu.Question;
import org.tohu.xml.ChangeCollector;
import org.tohu.xml.ItemId;
import org.tohu.xml.event.ObjectInsertedEventMock;
import org.tohu.xml.event.ObjectRetractedEventMock;
import org.tohu.xml.event.ObjectUpdatedEventMock;

/**
 * @author Damon Horrell
 *
 * Note: these tests use different object instances for
 */
public class ChangeCollectorTest {

	private Question question1;
	private Question question2;
	private Question question3;
	private Question question4;
	private Question question5;
	private Answer answer5Old;
	private Answer answer5New;
	private InvalidAnswer invalidAnswer1;
	private Dummy dummy;

	private ChangeCollector c;

	private Iterator<Entry<Object, FactHandle>> createIterator;
	private Entry<Object, FactHandle> createEntry;
	private Iterator<Object> updateIterator;
	private Iterator<Object> deleteIterator;

	@Before
	public void setup() {
		question1 = new Question("question1");
		question1.setAnswerType(Question.TYPE_TEXT);
		question1.setPreLabel("What is answer 1?");
		question1.setAnswer("answer1");
		question1.setActive(true);
		question2 = new Question("question2");
		question2.setAnswerType(Question.TYPE_TEXT);
		question2.setPreLabel("What is answer 2?");
		question2.setAnswer("answer2");
		question2.setActive(true);
		question3 = new Question("question3");
		question3.setAnswerType(Question.TYPE_TEXT);
		question3.setPreLabel("What is answer 3?");
		question3.setAnswer("answer3");
		question3.setActive(true);
		// question 4 is used for testing inactive being treated the same as deleted
		question4 = new Question("question4");
		question4.setAnswerType(Question.TYPE_TEXT);
		question4.setPreLabel("What is answer 4?");
		question4.setAnswer("answer4");
		question4.setActive(true);
		// question 5 is used for testing no update sent if the only change is the answer
		question5 = new Question("question5");
		question5.setAnswerType(Question.TYPE_TEXT);
		question5.setPreLabel("What is answer 5?");
		question5.setAnswer("answer5");
		question5.setActive(true);
		answer5Old = new Answer("question5", "answer5Old");
		answer5New = new Answer("question5", "answer5New");
		invalidAnswer1 = new InvalidAnswer("question1", "invalid answer 1");
		invalidAnswer1.setActive(true);
		dummy = new Dummy();
	}

	@Test
	public void testCreate() {
		c = new ChangeCollector();
		c.initialise(Collections.emptySet());

		c.objectInserted(new ObjectInsertedEventMock("2", question2));
		c.objectInserted(new ObjectInsertedEventMock("dummy", dummy));
		c.objectInserted(new ObjectInsertedEventMock("1", question1));
		c.objectInserted(new ObjectInsertedEventMock("ia1", invalidAnswer1));

		assertEquals(3, c.getCreate().size());
		createIterator = getCreateSorted(c).entrySet().iterator();
		createEntry = createIterator.next();
		assertEquals("1", createEntry.getValue().toExternalForm());
		assertEquals("answer1", ((Question) createEntry.getKey()).getAnswer());
		createEntry = createIterator.next();
		assertEquals("2", createEntry.getValue().toExternalForm());
		assertEquals("answer2", ((Question) createEntry.getKey()).getAnswer());
		createEntry = createIterator.next();
		assertEquals("ia1", createEntry.getValue().toExternalForm());
		assertEquals("invalid answer 1", ((InvalidAnswer) createEntry.getKey()).getReason());
		assertNull(c.getUpdate());
		assertNull(c.getDelete());
	}

	@Test
	public void testUpdate() {
		c = new ChangeCollector();
		c.initialise(Arrays.asList(new Object[] { question1, question2, dummy }));

		question1.setPreLabel("What is answer 1a?");
		question1.setAnswer("answer1a");
		question2.setPreLabel("What is answer 2a?");
		question2.setAnswer("answer2a");

		c.objectUpdated(new ObjectUpdatedEventMock("2", question2, question2));
		c.objectUpdated(new ObjectUpdatedEventMock("dummy", dummy, dummy));
		c.objectUpdated(new ObjectUpdatedEventMock("1", question1, question1));

		assertNull(c.getCreate());
		assertEquals(2, c.getUpdate().size());
		updateIterator = getUpdateSorted(c).iterator();
		assertEquals("answer1a", ((Question) updateIterator.next()).getAnswer());
		assertEquals("answer2a", ((Question) updateIterator.next()).getAnswer());
		assertNull(c.getDelete());
	}

	@Test
	public void testDelete() {
		c = new ChangeCollector();
		c.initialise(Arrays.asList(new Object[] { question1, question2, invalidAnswer1, dummy }));

		c.objectRetracted(new ObjectRetractedEventMock("2", question2));
		c.objectRetracted(new ObjectRetractedEventMock("dummy", dummy));
		c.objectRetracted(new ObjectRetractedEventMock("1", question1));
		c.objectRetracted(new ObjectRetractedEventMock("ia1", invalidAnswer1));

		assertNull(c.getCreate());
		assertNull(c.getUpdate());
		assertEquals(3, c.getDelete().size());
		deleteIterator = getDeleteSorted(c).iterator();
		assertEquals("question1", ((ItemId)deleteIterator.next()).getId());
		assertEquals("question1", ((InvalidAnswer)deleteIterator.next()).getQuestionId());
		assertEquals("question2", ((ItemId)deleteIterator.next()).getId());
	}

	@Test
	public void testCreateThenUpdate() {
		c = new ChangeCollector();
		c.initialise(Arrays.asList(new Object[] { question3 }));

		c.objectInserted(new ObjectInsertedEventMock("1", question1));
		c.objectInserted(new ObjectInsertedEventMock("2", question2));
		question1.setPreLabel("What is answer 1a?");
		question1.setAnswer("answer1a");
		c.objectUpdated(new ObjectUpdatedEventMock("1", question1, question1));
		question3.setPreLabel("What is answer 3a?");
		question3.setAnswer("answer3a");
		c.objectUpdated(new ObjectUpdatedEventMock("3", question3, question3));

		assertEquals(2, c.getCreate().size());
		createIterator = getCreateSorted(c).entrySet().iterator();
		createEntry = createIterator.next();
		assertEquals("1", createEntry.getValue().toExternalForm());
		assertEquals("answer1a", ((Question) createEntry.getKey()).getAnswer());
		createEntry = createIterator.next();
		assertEquals("2", createEntry.getValue().toExternalForm());
		assertEquals("answer2", ((Question) createEntry.getKey()).getAnswer());
		assertEquals(1, c.getUpdate().size());
		updateIterator = getUpdateSorted(c).iterator();
		assertEquals("answer3a", ((Question) updateIterator.next()).getAnswer());
		assertNull(c.getDelete());
	}

	@Test
	public void testCreateThenDelete() {
		c = new ChangeCollector();
		c.initialise(Arrays.asList(new Object[] { question3 }));

		c.objectInserted(new ObjectInsertedEventMock("2", question2));
		c.objectInserted(new ObjectInsertedEventMock("1", question1));
		c.objectRetracted(new ObjectRetractedEventMock("3", question3));
		c.objectRetracted(new ObjectRetractedEventMock("2", question2));

		assertEquals(1, c.getCreate().size());
		createIterator = getCreateSorted(c).entrySet().iterator();
		createEntry = createIterator.next();
		assertEquals("1", createEntry.getValue().toExternalForm());
		assertEquals("answer1", ((Question) createEntry.getKey()).getAnswer());
		assertNull(c.getUpdate());
		assertEquals(1, c.getDelete().size());
		deleteIterator = getDeleteSorted(c).iterator();
		assertEquals("question3", ((ItemId)deleteIterator.next()).getId());
	}

	@Test
	public void testUpdateThenDelete() {
		c = new ChangeCollector();
		c.initialise(Arrays.asList(new Object[] { question1, question2, question3 }));

		question3.setPreLabel("What is answer 3a?");
		question3.setAnswer("answer3a");
		c.objectUpdated(new ObjectUpdatedEventMock("3", question3, question3));
		question1.setPreLabel("What is answer 1a?");
		question1.setAnswer("answer1a");
		c.objectUpdated(new ObjectUpdatedEventMock("1", question1, question1));
		c.objectRetracted(new ObjectRetractedEventMock("2", question2));
		c.objectRetracted(new ObjectRetractedEventMock("3", question3));

		assertNull(c.getCreate());
		assertEquals(1, c.getUpdate().size());
		updateIterator = getUpdateSorted(c).iterator();
		assertEquals("answer1a", ((Question) updateIterator.next()).getAnswer());
		assertEquals(2, c.getDelete().size());
		deleteIterator = getDeleteSorted(c).iterator();
		assertEquals("question2", ((ItemId)deleteIterator.next()).getId());
		assertEquals("question3", ((ItemId)deleteIterator.next()).getId());
	}

	@Test
	public void testDeleteThenRecreate() {
		c = new ChangeCollector();
		c.initialise(Arrays.asList(new Object[] { question1 }));

		c.objectRetracted(new ObjectRetractedEventMock("1", question1));
		question1.setPreLabel("What is answer 1a?");
		question1.setAnswer("answer1a");
		c.objectInserted(new ObjectInsertedEventMock("1", question1));

		assertNull(c.getCreate());
		assertEquals(1, c.getUpdate().size());
		updateIterator = getUpdateSorted(c).iterator();
		assertEquals("answer1a", ((Question) updateIterator.next()).getAnswer());
		assertNull(c.getDelete());
	}

	@Test
	public void testUpdateTwice() {
		c = new ChangeCollector();
		c.initialise(Arrays.asList(new Object[] { question1, question2 }));

		question1.setPreLabel("What is answer 1a?");
		question1.setAnswer("answer1a");
		c.objectUpdated(new ObjectUpdatedEventMock("1", question1, question1));
		question2.setPreLabel("What is answer 2a?");
		question2.setAnswer("answer2a");
		c.objectUpdated(new ObjectUpdatedEventMock("2", question2, question2));
		question1.setPreLabel("What is answer 1b?");
		question1.setAnswer("answer1b");
		c.objectUpdated(new ObjectUpdatedEventMock("1", question1, question1));

		assertNull(c.getCreate());
		assertEquals(2, c.getUpdate().size());
		updateIterator = getUpdateSorted(c).iterator();
		assertEquals("answer1b", ((Question) updateIterator.next()).getAnswer());
		assertEquals("answer2a", ((Question) updateIterator.next()).getAnswer());
		assertNull(c.getDelete());
	}

	@Test
	public void testUpdateTwiceNoChange() {
		c = new ChangeCollector();
		c.initialise(Arrays.asList(new Object[] { question1, question2 }));

		question1.setPreLabel("What is answer 1a?");
		question1.setAnswer("answer1a");
		c.objectUpdated(new ObjectUpdatedEventMock("1", question1, question1));
		question2.setPreLabel("What is answer 2a?");
		question2.setAnswer("answer2a");
		c.objectUpdated(new ObjectUpdatedEventMock("2", question2, question2));
		question1.setPreLabel("What is answer 1?");
		question1.setAnswer("answer1");
		c.objectUpdated(new ObjectUpdatedEventMock("1", question1, question1));

		assertNull(c.getCreate());
		assertEquals(1, c.getUpdate().size());
		updateIterator = getUpdateSorted(c).iterator();
		assertEquals("answer2a", ((Question) updateIterator.next()).getAnswer());
		assertNull(c.getDelete());
	}

	@Test
	public void testCreateInactive() {
		c = new ChangeCollector();
		c.initialise(Arrays.asList(new Object[] {}));

		question4.setActive(false);
		c.objectInserted(new ObjectInsertedEventMock("4", question4));

		assertNull(c.getCreate());
		assertNull(c.getUpdate());
		assertNull(c.getDelete());
	}

	@Test
	public void testDeleteInactive() {
		c = new ChangeCollector();
		question4.setActive(false);
		c.initialise(Arrays.asList(new Object[] { question4 }));

		c.objectRetracted(new ObjectRetractedEventMock("4", question4));

		assertNull(c.getCreate());
		assertNull(c.getUpdate());
		assertNull(c.getDelete());
	}

	@Test
	public void testUpdateActiveToInactive() {
		c = new ChangeCollector();
		c.initialise(Arrays.asList(new Object[] { question4}));

		question4.setActive(false);
		c.objectUpdated(new ObjectUpdatedEventMock("4", question4, question4));

		assertNull(c.getCreate());
		assertNull(c.getUpdate());
		assertEquals(1, c.getDelete().size());
		deleteIterator = getDeleteSorted(c).iterator();
		assertEquals("question4", ((ItemId)deleteIterator.next()).getId());
	}

	@Test
	public void testUpdateInactiveToActive() {
		c = new ChangeCollector();
		question4.setActive(false);
		c.initialise(Arrays.asList(new Object[] { question4}));

		question4.setActive(true);
		question4.setAnswer("answer4Active");
		c.objectUpdated(new ObjectUpdatedEventMock("4", question4, question4));

		assertEquals(1, c.getCreate().size());
		createIterator = getCreateSorted(c).entrySet().iterator();
		createEntry = createIterator.next();
		assertEquals("4", createEntry.getValue().toExternalForm());
		assertEquals("answer4Active", ((Question) createEntry.getKey()).getAnswer());
		assertNull(c.getUpdate());
		assertNull(c.getDelete());
	}

	@Test
	public void testUpdateOnlyChangedAnswerAndIsSame() {
		c = new ChangeCollector();
		c.initialise(Arrays.asList(new Object[] { question5, answer5New }));

		question5.setAnswer("answer5New");
		c.objectUpdated(new ObjectUpdatedEventMock("5", question5, question5));
		c.objectRetracted(new ObjectRetractedEventMock("a", answer5New));

		assertNull(c.getCreate());
		assertNull(c.getUpdate());
		assertNull(c.getDelete());
	}

	@Test
	public void testUpdateOnlyChangedAnswerAndIsDifferent() {
		c = new ChangeCollector();
		c.initialise(Arrays.asList(new Object[] { question5,answer5Old }));

		question5.setAnswer("answer5New");
		c.objectUpdated(new ObjectUpdatedEventMock("5", question5, question5));
		c.objectRetracted(new ObjectRetractedEventMock("a", answer5Old));

		assertNull(c.getCreate());
		assertEquals(1, c.getUpdate().size());
		updateIterator = getUpdateSorted(c).iterator();
		assertEquals("answer5New", ((Question) updateIterator.next()).getAnswer());
		assertNull(c.getDelete());
	}

	private Map<Object, FactHandle> getCreateSorted(ChangeCollector c2) {
		TreeMap<Object, FactHandle> map = new TreeMap<Object, FactHandle>(new ItemComparator());
		map.putAll(c.getCreate());
		return map;
	}

	private List<Object> getUpdateSorted(ChangeCollector c2) {
		ArrayList<Object> list = new ArrayList<Object>(c2.getUpdate());
		Collections.sort(list, new ItemComparator());
		return list;
	}

	private List<Object> getDeleteSorted(ChangeCollector c2) {
		ArrayList<Object> list = new ArrayList<Object>(c2.getDelete());
		Collections.sort(list, new DeleteComparator());
		return list;
	}

	private class ItemComparator implements Comparator<Object> {

		public int compare(Object o1, Object o2) {
			if (o1 instanceof Item) {
				if (o2 instanceof Item) {
					return ((Item) o1).getId().compareTo(((Item) o2).getId());
				}
				return -1;
			}
			if (o2 instanceof Item) {
				return 1;
			}
			return o1.hashCode() - o2.hashCode();
		}

	}

	private class DeleteComparator implements Comparator<Object> {

		public int compare(Object o1, Object o2) {
			String id1 = o1 instanceof ItemId ? ((ItemId)o1).getId() : ((InvalidAnswer)o1).getId();
			String id2 = o2 instanceof ItemId ? ((ItemId)o2).getId() : ((InvalidAnswer)o2).getId();
			return id1.compareTo(id2);
		}
	}

	private class Dummy {
	}
}
