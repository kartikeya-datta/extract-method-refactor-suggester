this(threadID, IChatMessage.Type.CHAT, subject, body);

/****************************************************************************
 * Copyright (c) 2004 Composent, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Composent, Inc. - initial API and implementation
 *****************************************************************************/

package org.eclipse.ecf.presence.im;

import org.eclipse.ecf.core.identity.ID;

/**
 * Chat message concrete class. Implements IChatMessage.
 */
public class ChatMessage implements IChatMessage {

	private static final long serialVersionUID = 483032454041915204L;
	
	protected ID threadID;
	
	protected IChatMessage.Type type;
	
	protected String subject;
	
	protected String body;

	public ChatMessage(ID threadID, IChatMessage.Type type, String subject,
			String body) {
		this.threadID = threadID;
		this.type = type;
		this.subject = subject;
		this.body = body;
	}

	public ChatMessage(ID threadID, String subject, String body) {
		this(threadID, IChatMessage.Type.NORMAL, subject, body);
	}

	public ChatMessage(IChatMessage.Type type, String subject, String body) {
		this(null, type, subject, body);
	}

	public ChatMessage(String subject, String body) {
		this((ID) null, subject, body);
	}

	public ChatMessage(String body) {
		this(null, body);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.presence.im.IChatMessage#getBody()
	 */
	public String getBody() {
		return body;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.presence.im.IChatMessage#getSubject()
	 */
	public String getSubject() {
		return subject;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.presence.im.IChatMessage#getThreadID()
	 */
	public ID getThreadID() {
		return threadID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.presence.im.IChatMessage#getType()
	 */
	public Type getType() {
		return type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		return null;
	}

}