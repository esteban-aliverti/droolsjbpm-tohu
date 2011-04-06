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
package org.drools.executionserver;

import javax.servlet.http.HttpSession;

import org.drools.agent.KnowledgeAgent;
import org.drools.agent.KnowledgeAgentFactory;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;

/**
 * TODO this functionality should be available in the Execution Server itself
 *
 * @author Damon Horrell
 */
public class ExecutionServerHelper {

	private static final String KNOWLEDGE_SESSION = "knowledge.session";

	private static final String AGENT_CONFIG_DIRECTORY = "agent-config-directory";

	private HttpSession session;

	public ExecutionServerHelper(HttpSession session) {
		this.session = session;
	}

	public StatefulKnowledgeSession getKnowledgeSession() {
		return (StatefulKnowledgeSession) session.getAttribute(KNOWLEDGE_SESSION);
	}

	public void removeKnowledgeSession() {
		StatefulKnowledgeSession knowledgeSession = getKnowledgeSession();
		if (knowledgeSession != null) {
			knowledgeSession.dispose();
			session.removeAttribute(KNOWLEDGE_SESSION);
		}
	}

	public StatefulKnowledgeSession newKnowledgeSession(String agentName) {
		removeKnowledgeSession();
		String agentFile = "/" + agentName + ".xml";
		String agentConfigDir = session.getServletContext().getInitParameter(AGENT_CONFIG_DIRECTORY);
		KnowledgeAgent knowledgeAgent = KnowledgeAgentFactory.newKnowledgeAgent(agentFile);
		if (agentConfigDir.startsWith("classpath:")) {
			knowledgeAgent.applyChangeSet(ResourceFactory.newClassPathResource(agentConfigDir.replace("classpath:", "")
					+ agentFile));
		} else {
			knowledgeAgent.applyChangeSet(ResourceFactory.newUrlResource(agentConfigDir + agentFile));
		}
		StatefulKnowledgeSession knowledgeSession = knowledgeAgent.getKnowledgeBase().newStatefulKnowledgeSession();
		session.setAttribute(KNOWLEDGE_SESSION, knowledgeSession);
		return knowledgeSession;
	}
}
