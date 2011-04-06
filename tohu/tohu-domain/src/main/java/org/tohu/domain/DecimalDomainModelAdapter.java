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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.tohu.Question;

/**
 * Domain Model Adapter for the built-in decimal types:
 * 
 * <ul>
 * <li><code>float</code></li>
 * <li><code>double</code></li>
 * <li><code>java.lang.Float</code></li>
 * <li><code>java.lang.Double</code></li>
 * <li><code>java.math.BigDecimal</code></li>
 * </ul>
 * 
 * @author Damon Horrell
 */
public class DecimalDomainModelAdapter implements DomainModelAdapter {

	/**
	 * @see org.tohu.domain.DomainModelAdapter#getSupportedClasses()
	 */
	public Set<Class<?>> getSupportedClasses() {
		return new HashSet<Class<?>>(Arrays.asList(new Class<?>[] { float.class, double.class, Float.class, Double.class,
				BigDecimal.class }));
	}

	/**
	 * @see org.tohu.domain.DomainModelAdapter#getAnswerType()
	 */
	public String getAnswerType() {
		return Question.TYPE_DECIMAL;
	}

	/**
	 * @see org.tohu.domain.DomainModelAdapter#answerToObject(java.lang.Object, java.lang.Class)
	 */
	public Object answerToObject(Object answer, Class<?> clazz) {
		if (answer == null) {
			if (clazz.isPrimitive()) {
				answer = 0;
			} else {
				return null;
			}
		}
		if (clazz.isPrimitive()) {
			if (clazz.equals(float.class)) {
				clazz = Float.class;
			} else if (clazz.equals(double.class)) {
				clazz = Double.class;
			}
		}
		try {
			return clazz.getConstructor(String.class).newInstance(answer.toString());
		} catch (Exception e) {
			throw new UnsupportedOperationException(e);
		}
	}

	/**
	 * @see org.tohu.domain.DomainModelAdapter#objectToAnswer(java.lang.Object)
	 */
	public Object objectToAnswer(Object object) {
		return new BigDecimal(object.toString());
	}

}
