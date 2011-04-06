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
package org.tohu.support;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * Represents a Data point that may be represented a number of ways.
 * </p>
 * 
 * <p>
 * <code>DataItem</code> has an <code>answerType</code> which must be one of:
 * </p>
 * 
 * <ul>
 * <li><code>text</code></li>
 * <li><code>number</code></li>
 * <li><code>decimal</code></li>
 * <li><code>boolean</code></li>
 * <li><code>date</code></li>
 * </ul>
 * 
 * <p>
 * or an extension of one of these using the notation <code>&lt;type&gt;.&lt;extension type&gt; </code> e.g.
 * <code>text.url</code> or <code>decimal.currency</code>.
 * </p>
 * 
 * <p>
 * This is similar but separate from Question as, unlike Question, this has no hooks for field
 * processing in the UI (is not meant for UI handling as such).
 * </p>
 * 
 * <p>
 * The answer to a <code>DataItem</code> is maintained internally by the object. use <code>DomainModelAssociation</code> to
 * map the answers to a real domain model.
 * </p>
 * 
 * @author Derek Rendall
 */

public class TohuDataItemObject implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final String TYPE_TEXT = "text";

	public static final String TYPE_NUMBER = "number";

	public static final String TYPE_DECIMAL = "decimal";

	public static final String TYPE_BOOLEAN = "boolean";

	public static final String TYPE_DATE = "date";

	
	private String id;

	/**
	 * This is a way of optionally abstractly grouping elements for use outside Tohu.
	 */
	private String category;

	private String answerType;

	private String textAnswer;

	private Long numberAnswer;

	private BigDecimal decimalAnswer;

	private Boolean booleanAnswer;

	private Date dateAnswer;
	
	private String reason;
	
	private String name;
	

	public TohuDataItemObject() {
		this(null);
	}

	public TohuDataItemObject(String id) {
		super();
		setId(id);
	}
	
	/**
	 * @see org.tohu.TohuObject#getId()
	 */
	public String getId() {
		return id;
	}

	/**
	 * Return an optional abstract grouping identifying string
	 * 
	 * @return
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Sets an (optional) abstract grouping identifying string.
	 * 
	 * To be used outside of Tohu.
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	
	
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	/**
	 * Sets the unique id for this item which must be non-null and cannot contain any commas or dots.
	 * 
	 * @param id
	 */
	public void setId(String id) {
		if (id == null || id.contains(",") || id.contains(".")) {
			throw new IllegalArgumentException("Invalid item id");
		}
		if (this.id != null && !this.id.equals(id)) {
			throw new IllegalStateException("id may not be changed");
		}
		this.id = id;
	}


	public String getAnswerType() {
		return answerType;
	}

	public void setAnswerType(String answerType) {
		String previousBasicAnswerType = answerTypeToBasicAnswerType(this.answerType);
		String basicAnswerType = answerTypeToBasicAnswerType(answerType);
		if (basicAnswerType == null
				|| (!basicAnswerType.equals(TYPE_TEXT) && !basicAnswerType.equals(TYPE_NUMBER)
						&& !basicAnswerType.equals(TYPE_DECIMAL) && !basicAnswerType.equals(TYPE_BOOLEAN) && !basicAnswerType
						.equals(TYPE_DATE))) {
			throw new IllegalArgumentException("answerType " + answerType + " is invalid");
		}
		this.answerType = answerType;
		if (!basicAnswerType.equals(previousBasicAnswerType)) {
			clearAnswer();
		}
	}

	/**
	 * Returns the basic answer type.
	 * 
	 * @return
	 */
	public String getBasicAnswerType() {
		return answerTypeToBasicAnswerType(answerType);
	}

	protected String answerTypeToBasicAnswerType(String answerType) {
		if (answerType == null) {
			return null;
		}
		int i = answerType.indexOf('.');
		if (i >= 0) {
			return answerType.substring(0, i);
		}
		return answerType;
	}


	public void setAnswer(Object answer) {
		if (answerType == null) {
			throw new IllegalStateException("answerType has not been specified");
		}
		//System.out.println("Setting C Answer " + answer + " for " + getId());
		String basicAnswerType = getBasicAnswerType();
		if (basicAnswerType.equals(TYPE_TEXT)) {
			setTextAnswer((String) answer);
		}
		if (basicAnswerType.equals(TYPE_NUMBER)) {
			setNumberAnswer((Long) answer);
		}
		if (basicAnswerType.equals(TYPE_DECIMAL)) {
			setDecimalAnswer((BigDecimal) answer);
		}
		if (basicAnswerType.equals(TYPE_BOOLEAN)) {
			setBooleanAnswer((Boolean) answer);
		}
		if (basicAnswerType.equals(TYPE_DATE)) {
			setDateAnswer((Date) answer);
		}
	}

	/**
	 * Checks that the supplied answer type is correct.
	 * 
	 * @param answerType
	 */
	public void checkType(String answerType) {
		if (this.answerType == null) {
			throw new IllegalStateException("answerType has not been specified");
		}
		String basicAnswerType = getBasicAnswerType();
		if (!basicAnswerType.equals(answerType)) {
			throw new IllegalStateException("Supplied answer type " + answerType + " differs from the expected type "
					+ basicAnswerType + " for " + getId());
		}
	}
	
	public Object getAnswer() {
		if (answerType == null) {
			throw new IllegalStateException("answerType has not been specified");
		}
		String basicAnswerType = getBasicAnswerType();
		if (basicAnswerType.equals(TYPE_TEXT)) {
			return textAnswer;
		}
		if (basicAnswerType.equals(TYPE_NUMBER)) {
			return numberAnswer;
		}
		if (basicAnswerType.equals(TYPE_DECIMAL)) {
			return decimalAnswer;
		}
		if (basicAnswerType.equals(TYPE_BOOLEAN)) {
			return booleanAnswer;
		}
		if (basicAnswerType.equals(TYPE_DATE)) {
			return dateAnswer;
		}
		throw new IllegalStateException();
	}



	public String getTextAnswer() {
		checkType(TYPE_TEXT);
		return textAnswer;
	}

	public void setTextAnswer(String textAnswer) {
		checkType(TYPE_TEXT);
		this.textAnswer = textAnswer;
	}

	public Long getNumberAnswer() {
		checkType(TYPE_NUMBER);
		return numberAnswer;
	}

	public void setNumberAnswer(Long numberAnswer) {
		checkType(TYPE_NUMBER);
		this.numberAnswer = numberAnswer;
	}

	public BigDecimal getDecimalAnswer() {
		checkType(TYPE_DECIMAL);
		return decimalAnswer;
	}

	public void setDecimalAnswer(BigDecimal decimalAnswer) {
		checkType(TYPE_DECIMAL);
		this.decimalAnswer = decimalAnswer;
	}

	public Boolean getBooleanAnswer() {
		checkType(TYPE_BOOLEAN);
		return booleanAnswer;
	}

	public void setBooleanAnswer(Boolean booleanAnswer) {
		checkType(TYPE_BOOLEAN);
		this.booleanAnswer = booleanAnswer;
	}

	public Date getDateAnswer() {
		checkType(TYPE_DATE);
		return dateAnswer;
	}

	public void setDateAnswer(Date dateAnswer) {
		checkType(TYPE_DATE);
		this.dateAnswer = dateAnswer;
	}


	public boolean isAnswered() {
		return getAnswer() != null;
	}

	/**
	 * Clears any previous answer (which may be of a different data type).
	 */
	protected void clearAnswer() { 
		textAnswer = null;
		numberAnswer = null;
		decimalAnswer = null;
		booleanAnswer = null;
		dateAnswer = null;
	}

	/**
	 * For debugging purposes.
	 */
	@Override
	public String toString() {
		return this.getClass().getName() + ": id=" + id + " name=" + name + " answerType="
				+ getAnswerType() + " answer=" + getAnswer() + " category=" + getCategory();
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final TohuDataItemObject other = (TohuDataItemObject) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
