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

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.tohu.Question;

/**
 * Domain Model Adapter for List<String>
 * 
 * <ul>
 * <li><code>java.util.List&lt;String&gt;</code></li>
 * </ul>
 * 
 * @author David Plumpton
 */
public class ListDomainModelAdapter implements DomainModelAdapter {

	/**
	 * @see org.tohu.domain.DomainModelAdapter#getSupportedClasses()
	 */
	public Set<Class<?>> getSupportedClasses() {
		return new HashSet<Class<?>>(Arrays.asList(new Class<?>[] { java.util.List.class }));
	}

	/**
	 * @see org.tohu.domain.DomainModelAdapter#getAnswerType()
	 */
	public String getAnswerType() {
		return Question.TYPE_LIST;
	}

	/**
	 * @see org.tohu.domain.DomainModelAdapter#answerToObject(java.lang.Object, java.lang.Class)
	 */
	public Object answerToObject(Object answer, Class<?> clazz) {
		return answer == null ? new ArrayList<String>() : (List<String>) Arrays.asList(String.valueOf(answer).split(","));
	}

	/**
	 * @see org.tohu.domain.DomainModelAdapter#objectToAnswer(java.lang.Object)
	 */
	public Object objectToAnswer(Object object) {
		StringBuilder b = new StringBuilder();
		List<String> l = (List<String>) object;
		String delimiter = "";
		for (String s : l) {
			b.append(delimiter);
			b.append(s);
			delimiter = ",";
		}
		return b.toString();
	}

}
