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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tohu.DomainModelAssociation;
import org.tohu.DummyData;
import org.tohu.Question;

/**
 * @author Damon Horrell
 */
public class DomainModelRulesTest {

	private static final Logger logger = LoggerFactory.getLogger(DomainModelRulesTest.class);
	
	private KnowledgeBase knowledgeBase;

	private DummyData data;

	private Question questionPrimitiveChar;
	private Question questionPrimitiveByte;
	private Question questionPrimitiveShort;
	private Question questionPrimitiveInt;
	private Question questionPrimitiveLong;
	private Question questionPrimitiveFloat;
	private Question questionPrimitiveDouble;
	private Question questionPrimitiveBoolean;
	private Question questionString;
	private Question questionObjectChar;
	private Question questionObjectByte;
	private Question questionObjectShort;
	private Question questionObjectInt;
	private Question questionObjectLong;
	private Question questionObjectFloat;
	private Question questionObjectDouble;
	private Question questionBigInteger;
	private Question questionBigDecimal;
	private Question questionObjectBoolean;
	private Question questionDate;

	private DomainModelAssociation associationPrimitiveChar;
	private DomainModelAssociation associationPrimitiveByte;
	private DomainModelAssociation associationPrimitiveShort;
	private DomainModelAssociation associationPrimitiveInt;
	private DomainModelAssociation associationPrimitiveLong;
	private DomainModelAssociation associationPrimitiveFloat;
	private DomainModelAssociation associationPrimitiveDouble;
	private DomainModelAssociation associationPrimitiveBoolean;
	private DomainModelAssociation associationString;
	private DomainModelAssociation associationObjectChar;
	private DomainModelAssociation associationObjectByte;
	private DomainModelAssociation associationObjectShort;
	private DomainModelAssociation associationObjectInt;
	private DomainModelAssociation associationObjectLong;
	private DomainModelAssociation associationObjectFloat;
	private DomainModelAssociation associationObjectDouble;
	private DomainModelAssociation associationBigInteger;
	private DomainModelAssociation associationBigDecimal;
	private DomainModelAssociation associationObjectBoolean;
	private DomainModelAssociation associationDate;

	private FactHandle handleQuestionPrimitiveChar;
	private FactHandle handleQuestionPrimitiveByte;
	private FactHandle handleQuestionPrimitiveShort;
	private FactHandle handleQuestionPrimitiveInt;
	private FactHandle handleQuestionPrimitiveLong;
	private FactHandle handleQuestionPrimitiveFloat;
	private FactHandle handleQuestionPrimitiveDouble;
	private FactHandle handleQuestionPrimitiveBoolean;
	private FactHandle handleQuestionString;
	private FactHandle handleQuestionObjectChar;
	private FactHandle handleQuestionObjectByte;
	private FactHandle handleQuestionObjectShort;
	private FactHandle handleQuestionObjectInt;
	private FactHandle handleQuestionObjectLong;
	private FactHandle handleQuestionObjectFloat;
	private FactHandle handleQuestionObjectDouble;
	private FactHandle handleQuestionBigInteger;
	private FactHandle handleQuestionBigDecimal;
	private FactHandle handleQuestionObjectBoolean;
	private FactHandle handleQuestionDate;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		knowledgeBuilder.add(ResourceFactory.newClassPathResource("org/tohu/DomainModel.drl"), ResourceType.DRL);
		if (knowledgeBuilder.hasErrors()) {
			logger.debug(Arrays.toString(knowledgeBuilder.getErrors().toArray()));
		}
		assertFalse(knowledgeBuilder.hasErrors());
		knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
		knowledgeBase.addKnowledgePackages(knowledgeBuilder.getKnowledgePackages());
	}

	private void createData() {
		data = new DummyData();
	}

	private void createQuestions() {
		questionPrimitiveChar = new Question("questionPrimitiveChar");
		questionPrimitiveByte = new Question("questionPrimitiveByte");
		questionPrimitiveShort = new Question("questionPrimitiveShort");
		questionPrimitiveInt = new Question("questionPrimitiveInt");
		questionPrimitiveLong = new Question("questionPrimitiveLong");
		questionPrimitiveFloat = new Question("questionPrimitiveFloat");
		questionPrimitiveDouble = new Question("questionPrimitiveDouble");
		questionPrimitiveBoolean = new Question("questionPrimitiveBoolean");
		questionString = new Question("questionString");
		questionObjectChar = new Question("questionObjectChar");
		questionObjectByte = new Question("questionObjectByte");
		questionObjectShort = new Question("questionObjectShort");
		questionObjectInt = new Question("questionObjectInt");
		questionObjectLong = new Question("questionObjectLong");
		questionObjectFloat = new Question("questionObjectFloat");
		questionObjectDouble = new Question("questionObjectDouble");
		questionBigInteger = new Question("questionBigInteger");
		questionBigDecimal = new Question("questionBigDecimal");
		questionObjectBoolean = new Question("questionObjectBoolean");
		questionDate = new Question("questionDate");
	}

	private void createAssociations() {
		associationPrimitiveChar = new DomainModelAssociation("questionPrimitiveChar", data, "primitiveChar");
		associationPrimitiveByte = new DomainModelAssociation("questionPrimitiveByte", data, "primitiveByte");
		associationPrimitiveShort = new DomainModelAssociation("questionPrimitiveShort", data, "primitiveShort");
		associationPrimitiveInt = new DomainModelAssociation("questionPrimitiveInt", data, "primitiveInt");
		associationPrimitiveLong = new DomainModelAssociation("questionPrimitiveLong", data, "primitiveLong");
		associationPrimitiveFloat = new DomainModelAssociation("questionPrimitiveFloat", data, "primitiveFloat");
		associationPrimitiveDouble = new DomainModelAssociation("questionPrimitiveDouble", data, "primitiveDouble");
		associationPrimitiveBoolean = new DomainModelAssociation("questionPrimitiveBoolean", data, "primitiveBoolean");
		associationString = new DomainModelAssociation("questionString", data, "string");
		associationObjectChar = new DomainModelAssociation("questionObjectChar", data, "objectChar");
		associationObjectByte = new DomainModelAssociation("questionObjectByte", data, "objectByte");
		associationObjectShort = new DomainModelAssociation("questionObjectShort", data, "objectShort");
		associationObjectInt = new DomainModelAssociation("questionObjectInt", data, "objectInt");
		associationObjectLong = new DomainModelAssociation("questionObjectLong", data, "objectLong");
		associationObjectFloat = new DomainModelAssociation("questionObjectFloat", data, "objectFloat");
		associationObjectDouble = new DomainModelAssociation("questionObjectDouble", data, "objectDouble");
		associationBigInteger = new DomainModelAssociation("questionBigInteger", data, "bigInteger");
		associationBigDecimal = new DomainModelAssociation("questionBigDecimal", data, "bigDecimal");
		associationObjectBoolean = new DomainModelAssociation("questionObjectBoolean", data, "objectBoolean");
		associationDate = new DomainModelAssociation("questionDate", data, "date");
	}

	private void insertFacts(StatefulKnowledgeSession knowledgeSession) {
		knowledgeSession.insert(data);

		handleQuestionPrimitiveChar = knowledgeSession.insert(questionPrimitiveChar);
		handleQuestionPrimitiveByte = knowledgeSession.insert(questionPrimitiveByte);
		handleQuestionPrimitiveShort = knowledgeSession.insert(questionPrimitiveShort);
		handleQuestionPrimitiveInt = knowledgeSession.insert(questionPrimitiveInt);
		handleQuestionPrimitiveLong = knowledgeSession.insert(questionPrimitiveLong);
		handleQuestionPrimitiveFloat = knowledgeSession.insert(questionPrimitiveFloat);
		handleQuestionPrimitiveDouble = knowledgeSession.insert(questionPrimitiveDouble);
		handleQuestionPrimitiveBoolean = knowledgeSession.insert(questionPrimitiveBoolean);
		handleQuestionString = knowledgeSession.insert(questionString);
		handleQuestionObjectChar = knowledgeSession.insert(questionObjectChar);
		handleQuestionObjectByte = knowledgeSession.insert(questionObjectByte);
		handleQuestionObjectShort = knowledgeSession.insert(questionObjectShort);
		handleQuestionObjectInt = knowledgeSession.insert(questionObjectInt);
		handleQuestionObjectLong = knowledgeSession.insert(questionObjectLong);
		handleQuestionObjectFloat = knowledgeSession.insert(questionObjectFloat);
		handleQuestionObjectDouble = knowledgeSession.insert(questionObjectDouble);
		handleQuestionBigInteger = knowledgeSession.insert(questionBigInteger);
		handleQuestionBigDecimal = knowledgeSession.insert(questionBigDecimal);
		handleQuestionObjectBoolean = knowledgeSession.insert(questionObjectBoolean);
		handleQuestionDate = knowledgeSession.insert(questionDate);

		knowledgeSession.insert(associationPrimitiveChar);
		knowledgeSession.insert(associationPrimitiveByte);
		knowledgeSession.insert(associationPrimitiveShort);
		knowledgeSession.insert(associationPrimitiveInt);
		knowledgeSession.insert(associationPrimitiveLong);
		knowledgeSession.insert(associationPrimitiveFloat);
		knowledgeSession.insert(associationPrimitiveDouble);
		knowledgeSession.insert(associationPrimitiveBoolean);
		knowledgeSession.insert(associationString);
		knowledgeSession.insert(associationObjectChar);
		knowledgeSession.insert(associationObjectByte);
		knowledgeSession.insert(associationObjectShort);
		knowledgeSession.insert(associationObjectInt);
		knowledgeSession.insert(associationObjectLong);
		knowledgeSession.insert(associationObjectFloat);
		knowledgeSession.insert(associationObjectDouble);
		knowledgeSession.insert(associationBigInteger);
		knowledgeSession.insert(associationBigDecimal);
		knowledgeSession.insert(associationObjectBoolean);
		knowledgeSession.insert(associationDate);
	}

	private void updateQuestions(StatefulKnowledgeSession knowledgeSession) {
		knowledgeSession.update(handleQuestionPrimitiveChar, questionPrimitiveChar);
		knowledgeSession.update(handleQuestionPrimitiveByte, questionPrimitiveByte);
		knowledgeSession.update(handleQuestionPrimitiveShort, questionPrimitiveShort);
		knowledgeSession.update(handleQuestionPrimitiveInt, questionPrimitiveInt);
		knowledgeSession.update(handleQuestionPrimitiveLong, questionPrimitiveLong);
		knowledgeSession.update(handleQuestionPrimitiveFloat, questionPrimitiveFloat);
		knowledgeSession.update(handleQuestionPrimitiveDouble, questionPrimitiveDouble);
		knowledgeSession.update(handleQuestionPrimitiveBoolean, questionPrimitiveBoolean);
		knowledgeSession.update(handleQuestionString, questionString);
		knowledgeSession.update(handleQuestionObjectChar, questionObjectChar);
		knowledgeSession.update(handleQuestionObjectByte, questionObjectByte);
		knowledgeSession.update(handleQuestionObjectShort, questionObjectShort);
		knowledgeSession.update(handleQuestionObjectInt, questionObjectInt);
		knowledgeSession.update(handleQuestionObjectLong, questionObjectLong);
		knowledgeSession.update(handleQuestionObjectFloat, questionObjectFloat);
		knowledgeSession.update(handleQuestionObjectDouble, questionObjectDouble);
		knowledgeSession.update(handleQuestionBigInteger, questionBigInteger);
		knowledgeSession.update(handleQuestionBigDecimal, questionBigDecimal);
		knowledgeSession.update(handleQuestionObjectBoolean, questionObjectBoolean);
		knowledgeSession.update(handleQuestionDate, questionDate);
	}

	@Test
	public void testDeriveAnswerType() {
		StatefulKnowledgeSession knowledgeSession = knowledgeBase.newStatefulKnowledgeSession();
		try {
			createData();
			createQuestions();
			createAssociations();

			insertFacts(knowledgeSession);
			knowledgeSession.fireAllRules();

			assertEquals(Question.TYPE_TEXT, questionPrimitiveChar.getAnswerType());
			assertEquals(Question.TYPE_NUMBER, questionPrimitiveByte.getAnswerType());
			assertEquals(Question.TYPE_NUMBER, questionPrimitiveShort.getAnswerType());
			assertEquals(Question.TYPE_NUMBER, questionPrimitiveInt.getAnswerType());
			assertEquals(Question.TYPE_NUMBER, questionPrimitiveLong.getAnswerType());
			assertEquals(Question.TYPE_DECIMAL, questionPrimitiveFloat.getAnswerType());
			assertEquals(Question.TYPE_DECIMAL, questionPrimitiveDouble.getAnswerType());
			assertEquals(Question.TYPE_BOOLEAN, questionPrimitiveBoolean.getAnswerType());
			assertEquals(Question.TYPE_TEXT, questionString.getAnswerType());
			assertEquals(Question.TYPE_TEXT, questionObjectChar.getAnswerType());
			assertEquals(Question.TYPE_NUMBER, questionObjectByte.getAnswerType());
			assertEquals(Question.TYPE_NUMBER, questionObjectShort.getAnswerType());
			assertEquals(Question.TYPE_NUMBER, questionObjectInt.getAnswerType());
			assertEquals(Question.TYPE_NUMBER, questionObjectLong.getAnswerType());
			assertEquals(Question.TYPE_DECIMAL, questionObjectFloat.getAnswerType());
			assertEquals(Question.TYPE_DECIMAL, questionObjectDouble.getAnswerType());
			assertEquals(Question.TYPE_NUMBER, questionBigInteger.getAnswerType());
			assertEquals(Question.TYPE_DECIMAL, questionBigDecimal.getAnswerType());
			assertEquals(Question.TYPE_BOOLEAN, questionObjectBoolean.getAnswerType());
			assertEquals(Question.TYPE_DATE, questionDate.getAnswerType());
		} finally {
			knowledgeSession.dispose();
		}
	}

	@Test
	public void testCopyDomainModelToQuestion() {
		StatefulKnowledgeSession knowledgeSession = knowledgeBase.newStatefulKnowledgeSession();
		try {
			createData();
			createQuestions();
			createAssociations();

			data.setPrimitiveChar('a');
			data.setPrimitiveByte((byte) 123);
			data.setPrimitiveShort((short) 456);
			data.setPrimitiveInt(78901);
			data.setPrimitiveLong(Long.MAX_VALUE);
			data.setPrimitiveFloat(1.23F);
			data.setPrimitiveDouble(4.56);
			data.setPrimitiveBoolean(true);
			data.setString("abc");
			data.setObjectChar('d');
			data.setObjectByte((byte) -7);
			data.setObjectShort((short) -999);
			data.setObjectInt(null);
			data.setObjectLong(Long.MIN_VALUE);
			data.setObjectFloat(1234.567F);
			data.setObjectDouble(8.9);
			data.setBigInteger(new BigInteger("1234"));
			data.setBigDecimal(new BigDecimal("56.789"));
			data.setObjectBoolean(false);
			data.setDate(new SimpleDateFormat("dd/MM/yyyy").parse("12/03/2004"));

			insertFacts(knowledgeSession);
			knowledgeSession.fireAllRules();

			assertEquals("a", questionPrimitiveChar.getTextAnswer());
			assertEquals(123, questionPrimitiveByte.getNumberAnswer());
			assertEquals(456, questionPrimitiveShort.getNumberAnswer());
			assertEquals(78901, questionPrimitiveInt.getNumberAnswer());
			assertEquals(Long.MAX_VALUE, questionPrimitiveLong.getNumberAnswer());
			assertEquals(1.23, questionPrimitiveFloat.getDecimalAnswer());
			assertEquals(4.56, questionPrimitiveDouble.getDecimalAnswer());
			assertEquals(true, questionPrimitiveBoolean.getBooleanAnswer());
			assertEquals("abc", questionString.getTextAnswer());
			assertEquals("d", questionObjectChar.getTextAnswer());
			assertEquals(-7, questionObjectByte.getNumberAnswer());
			assertEquals(-999, questionObjectShort.getNumberAnswer());
			assertEquals(null, questionObjectInt.getNumberAnswer());
			assertEquals(Long.MIN_VALUE, questionObjectLong.getNumberAnswer());
			assertEquals(1234.567F, questionObjectFloat.getDecimalAnswer());
			assertEquals(8.9, questionObjectDouble.getDecimalAnswer());
			assertEquals(1234, questionBigInteger.getNumberAnswer());
			assertEquals(56.789, questionBigDecimal.getDecimalAnswer());
			assertEquals(false, questionObjectBoolean.getBooleanAnswer());
			assertEquals(new SimpleDateFormat("dd/MM/yyyy").parse("12/03/2004"), questionDate.getDateAnswer());
		} catch (ParseException e) {
			throw new RuntimeException(e);
		} finally {
			knowledgeSession.dispose();
		}
	}

	@Test
	public void testCopyQuestionToDomainModel() {
		StatefulKnowledgeSession knowledgeSession = knowledgeBase.newStatefulKnowledgeSession();
		try {
			createData();
			createQuestions();
			createAssociations();

			insertFacts(knowledgeSession);
			knowledgeSession.fireAllRules();

			questionPrimitiveChar.setTextAnswer("a");
			questionPrimitiveByte.setNumberAnswer(123L);
			questionPrimitiveShort.setNumberAnswer(456L);
			questionPrimitiveInt.setNumberAnswer(78901L);
			questionPrimitiveLong.setNumberAnswer(Long.MAX_VALUE);
			questionPrimitiveFloat.setDecimalAnswer(new BigDecimal("1.23"));
			questionPrimitiveDouble.setDecimalAnswer(new BigDecimal("4.56"));
			questionPrimitiveBoolean.setBooleanAnswer(true);
			questionString.setTextAnswer("abc");
			questionObjectChar.setTextAnswer("d");
			questionObjectByte.setNumberAnswer(-7L);
			questionObjectShort.setNumberAnswer(-999L);
			questionObjectInt.setNumberAnswer(null);
			questionObjectLong.setNumberAnswer(Long.MIN_VALUE);
			questionObjectFloat.setDecimalAnswer(new BigDecimal("1234.567"));
			questionObjectDouble.setDecimalAnswer(new BigDecimal("8.9"));
			questionBigInteger.setNumberAnswer(1234L);
			questionBigDecimal.setDecimalAnswer(new BigDecimal("56.789"));
			questionObjectBoolean.setBooleanAnswer(false);
			questionDate.setAnswer(new SimpleDateFormat("dd/MM/yyyy").parse("12/03/2004"));

			updateQuestions(knowledgeSession);
			knowledgeSession.fireAllRules();

			assertEquals('a', data.getPrimitiveChar());
			assertEquals(123, data.getPrimitiveByte());
			assertEquals(456, data.getPrimitiveShort());
			assertEquals(78901, data.getPrimitiveInt());
			assertEquals(Long.MAX_VALUE, data.getPrimitiveLong());
			assertEquals(1.23, data.getPrimitiveFloat());
			assertEquals(4.56, data.getPrimitiveDouble());
			assertEquals(true, data.isPrimitiveBoolean());
			assertEquals("abc", data.getString());
			assertEquals('d', data.getObjectChar());
			assertEquals(-7, data.getObjectByte());
			assertEquals(-999, data.getObjectShort());
			assertEquals(null, data.getObjectInt());
			assertEquals(Long.MIN_VALUE, data.getObjectLong());
			assertEquals(1234.567F, data.getObjectFloat());
			assertEquals(8.9, data.getObjectDouble());
			assertEquals(1234, data.getBigInteger());
			assertEquals(56.789, data.getBigDecimal());
			assertEquals(false, data.getObjectBoolean());
			assertEquals(new SimpleDateFormat("dd/MM/yyyy").parse("12/03/2004"), data.getDate());
		} catch (ParseException e) {
			throw new RuntimeException(e);
		} finally {
			knowledgeSession.dispose();
		}
	}

}
