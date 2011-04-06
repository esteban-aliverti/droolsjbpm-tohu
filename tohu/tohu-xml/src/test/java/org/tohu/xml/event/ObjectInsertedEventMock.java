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
package org.tohu.xml.event;

import org.drools.event.rule.ObjectInsertedEvent;

/**
 * @author Damon Horrell
 */
public class ObjectInsertedEventMock extends WorkingMemoryEventMock implements ObjectInsertedEvent {

	private Object object;

	public ObjectInsertedEventMock(String factHandle, Object object) {
		super(factHandle);
		this.object = object;
	}

	/**
	 * @see org.drools.event.rule.ObjectInsertedEvent#getObject()
	 */
	public Object getObject() {
		return object;
	}

}
