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
package org.tohu.domain;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tohu.DummyData;
import org.tohu.Question;
import org.tohu.domain.DomainModelSupport;

public class DomainModelSupportTest {

	private static final Logger logger = LoggerFactory.getLogger(DomainModelSupportTest.class);
	
	private DummyData data;

	@Before
	public void setUp() throws Exception {
		data = new DummyData();
	}

	@Test
	public void testText() {
		setTextProperty("string", "hello");
		setTextProperty("primitiveChar", "ax");
		setTextProperty("objectChar", "bx");
		assertEquals("hello", getTextProperty("string"));
		assertEquals("a", getTextProperty("primitiveChar"));
		assertEquals("b", getTextProperty("objectChar"));
	}

	@Test
	public void testNumber() {
		setNumberProperty("primitiveByte", 1L);
		setNumberProperty("primitiveShort", 2L);
		setNumberProperty("primitiveInt", 3L);
		setNumberProperty("primitiveLong", 4L);
		setNumberProperty("objectByte", 5L);
		setNumberProperty("objectShort", 6L);
		setNumberProperty("objectInt", 7L);
		setNumberProperty("objectLong", 8L);
		setNumberProperty("bigInteger", 9L);
		assertEquals(1, getNumberProperty("primitiveByte"));
		assertEquals(2, getNumberProperty("primitiveShort"));
		assertEquals(3, getNumberProperty("primitiveInt"));
		assertEquals(4, getNumberProperty("primitiveLong"));
		assertEquals(5, getNumberProperty("objectByte"));
		assertEquals(6, getNumberProperty("objectShort"));
		assertEquals(7, getNumberProperty("objectInt"));
		assertEquals(8, getNumberProperty("objectLong"));
		assertEquals(9, getNumberProperty("bigInteger"));
	}

	@Test
	public void testDecimal() {
		setDecimalProperty("primitiveFloat", new BigDecimal("1.2"));
		setDecimalProperty("primitiveDouble", new BigDecimal("3.45"));
		setDecimalProperty("objectFloat", new BigDecimal("-6.789"));
		setDecimalProperty("objectDouble", new BigDecimal("123.456"));
		setDecimalProperty("bigDecimal", new BigDecimal("0.99999"));
		assertEquals(new BigDecimal("1.2"), getDecimalProperty("primitiveFloat"));
		assertEquals(new BigDecimal("3.45"), getDecimalProperty("primitiveDouble"));
		assertEquals(new BigDecimal("-6.789"), getDecimalProperty("objectFloat"));
		assertEquals(new BigDecimal("123.456"), getDecimalProperty("objectDouble"));
		assertEquals(new BigDecimal("0.99999"), getDecimalProperty("bigDecimal"));
	}

	@Test
	public void testBoolean() {
		setBooleanProperty("primitiveBoolean", Boolean.TRUE);
		setBooleanProperty("objectBoolean", Boolean.TRUE);
		assertEquals(Boolean.TRUE, getBooleanProperty("primitiveBoolean"));
		assertEquals(Boolean.TRUE, getBooleanProperty("objectBoolean"));
	}

	@Test
	public void testDate() {
		Date date = Calendar.getInstance().getTime();
		setDateProperty("date", date);
		assertEquals(date, getDateProperty("date"));
	}
	
	@Test
	public void testList() {
		assertEquals(Question.TYPE_LIST, new ListDomainModelAdapter().getAnswerType());
		setListProperty("list", null);
		assertEquals("", getListProperty("list"));
		setListProperty("list", "");
		assertEquals("", getListProperty("list"));
		setListProperty("list", "foo");
		assertEquals("foo", getListProperty("list"));
		setListProperty("list", "foo,bar");
		assertEquals("foo,bar", getListProperty("list"));
		setListProperty("list", "a,b,c,d");
		assertEquals("a,b,c,d", getListProperty("list"));
	}

	private void setTextProperty(String property, String value) {
		try {
			logger.debug("Setting " + property);
			Class<?> propertyClass = PropertyUtils.getPropertyType(data, property);
			Object v = DomainModelSupport.answerToObject(Question.TYPE_TEXT, value, propertyClass);
			PropertyUtils.setProperty(data, property, v);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String getTextProperty(String property) {
		try {
			logger.debug("Getting " + property);
			Object propertyValue = PropertyUtils.getProperty(data, property);
			String value = (String) DomainModelSupport.objectToAnswer(propertyValue, Question.TYPE_TEXT);
			return value;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void setNumberProperty(String property, Long value) {
		try {
			logger.debug("Setting " + property);
			Class<?> propertyClass = PropertyUtils.getPropertyType(data, property);
			Object v = DomainModelSupport.answerToObject(Question.TYPE_NUMBER, value, propertyClass);
			PropertyUtils.setProperty(data, property, v);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private Long getNumberProperty(String property) {
		try {
			logger.debug("Getting " + property);
			Object propertyValue = PropertyUtils.getProperty(data, property);
			Long value = (Long) DomainModelSupport.objectToAnswer(propertyValue, Question.TYPE_NUMBER);
			return value;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void setDecimalProperty(String property, BigDecimal value) {
		try {
			logger.debug("Setting " + property);
			Class<?> propertyClass = PropertyUtils.getPropertyType(data, property);
			Object v = DomainModelSupport.answerToObject(Question.TYPE_DECIMAL, value, propertyClass);
			PropertyUtils.setProperty(data, property, v);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private BigDecimal getDecimalProperty(String property) {
		try {
			logger.debug("Getting " + property);
			Object propertyValue = PropertyUtils.getProperty(data, property);
			BigDecimal value = (BigDecimal) DomainModelSupport.objectToAnswer(propertyValue, Question.TYPE_DECIMAL);
			return value;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void setBooleanProperty(String property, Boolean value) {
		try {
			logger.debug("Setting " + property);
			Class<?> propertyClass = PropertyUtils.getPropertyType(data, property);
			Object v = DomainModelSupport.answerToObject(Question.TYPE_BOOLEAN, value, propertyClass);
			PropertyUtils.setProperty(data, property, v);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private Boolean getBooleanProperty(String property) {
		try {
			logger.debug("Getting " + property);
			Object propertyValue = PropertyUtils.getProperty(data, property);
			Boolean value = (Boolean) DomainModelSupport.objectToAnswer(propertyValue, Question.TYPE_BOOLEAN);
			return value;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void setDateProperty(String property, Date value) {
		try {
			logger.debug("Setting " + property);
			Class<?> propertyClass = PropertyUtils.getPropertyType(data, property);
			Object v = DomainModelSupport.answerToObject(Question.TYPE_DATE, value, propertyClass);
			PropertyUtils.setProperty(data, property, v);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private Date getDateProperty(String property) {
		try {
			logger.debug("Getting " + property);
			Object propertyValue = PropertyUtils.getProperty(data, property);
			Date value = (Date) DomainModelSupport.objectToAnswer(propertyValue, Question.TYPE_DATE);
			return value;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private void setListProperty(String property, String value) {
		try {
			logger.debug("Setting " + property);
			Class<?> propertyClass = PropertyUtils.getPropertyType(data, property);
			Object v = DomainModelSupport.answerToObject(Question.TYPE_LIST, value, propertyClass);
			PropertyUtils.setProperty(data, property, v);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private String getListProperty(String property) {
		try {
			logger.debug("Getting " + property);
			Object propertyValue = PropertyUtils.getProperty(data, property);
			String value = (String) DomainModelSupport.objectToAnswer(propertyValue, Question.TYPE_LIST);
			return value;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
