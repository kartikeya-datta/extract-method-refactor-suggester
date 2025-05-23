error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6496.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6496.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6496.java
text:
```scala
I@@Presence newPresence = new org.eclipse.ecf.presence.Presence(

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
package org.eclipse.ecf.provider.xmpp.container;

import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Vector;

import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.Namespace;
import org.eclipse.ecf.core.sharedobject.ISharedObject;
import org.eclipse.ecf.core.sharedobject.ISharedObjectConfig;
import org.eclipse.ecf.core.sharedobject.ISharedObjectContext;
import org.eclipse.ecf.core.sharedobject.SharedObjectInitException;
import org.eclipse.ecf.core.util.Event;
import org.eclipse.ecf.presence.IMessageListener;
import org.eclipse.ecf.presence.IPresence;
import org.eclipse.ecf.presence.chat.IChatParticipantListener;
import org.eclipse.ecf.provider.xmpp.Trace;
import org.eclipse.ecf.provider.xmpp.events.ChatMembershipEvent;
import org.eclipse.ecf.provider.xmpp.events.MessageEvent;
import org.eclipse.ecf.provider.xmpp.events.PresenceEvent;
import org.eclipse.ecf.provider.xmpp.identity.XMPPID;
import org.eclipse.ecf.provider.xmpp.identity.XMPPRoomID;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smack.packet.Presence.Type;

public class XMPPGroupChatSharedObject implements ISharedObject {

    public static Trace trace = Trace.create("xmppgroupchatsharedobject");

    ISharedObjectConfig config = null;
    
    Vector messageListeners = new Vector();
	Namespace usernamespace = null;
	XMPPConnection connection = null;
    Vector participantListeners = new Vector();
    
    protected void debug(String msg) {
    	if (config == null) return;
        if (Trace.ON && trace != null) {
            trace.msg(config.getSharedObjectID() + ":" + msg);
        }
    }
    protected void dumpStack(String msg, Throwable e) {
        if (Trace.ON && trace != null) {
            trace.dumpStack(e, config.getSharedObjectID() + ":" + msg);
        }
    }

    protected void addChatParticipantListener(IChatParticipantListener listener) {
        participantListeners.add(listener);
    }
    protected void removeChatParticipantListener(IChatParticipantListener listener) {
        participantListeners.remove(listener);
    }
    protected void addMessageListener(IMessageListener listener) {
        messageListeners.add(listener);
    }
    protected void removeMessageListener(IMessageListener listener) {
        messageListeners.add(listener);
    }

    public XMPPGroupChatSharedObject(Namespace usernamespace, XMPPConnection conn) {
        super();
        this.usernamespace = usernamespace;
        this.connection = conn;
    }
    
    protected ISharedObjectContext getContext() {
        return config.getContext();
    }

    /* (non-Javadoc)
     * @see org.eclipse.ecf.core.ISharedObject#init(org.eclipse.ecf.core.ISharedObjectConfig)
     */
    public void init(ISharedObjectConfig initData)
            throws SharedObjectInitException {
        this.config = initData;
    }

    protected ID createUserIDFromName(String name) {
        ID result = null;
        try {
            result = new XMPPID(usernamespace,name);
            return result;
        } catch (Exception e) {
            dumpStack("Exception in createIDFromName", e);
            return null;
        }
    }

    protected Message.Type [] ALLOWED_MESSAGES = { Message.Type.GROUP_CHAT };
    protected Message filterMessageType(Message msg) {
    	for(int i=0; i < ALLOWED_MESSAGES.length; i++) {
    		if (ALLOWED_MESSAGES[i].equals(msg.getType())) {
    			return msg;
    		}
    	}
    	return null;
    }
    protected String canonicalizeRoomFrom(String from) {
        if (from == null)
            return null;
        int atIndex = from.indexOf('@');
        String hostname = null;
        String username = null;
        int index = from.indexOf("/");
        if (atIndex > 0 && index > 0) {
        	hostname = from.substring(atIndex+1,index);
            username = from.substring(index+1);
            return username+"@"+hostname;
        }
        return from;
    }
    protected IMessageListener.Type createMessageType(Message.Type type) {
        if (type == null)
            return IMessageListener.Type.NORMAL;
        if (type == Message.Type.CHAT) {
            return IMessageListener.Type.CHAT;
        } else if (type == Message.Type.NORMAL) {
            return IMessageListener.Type.NORMAL;
        } else if (type == Message.Type.GROUP_CHAT) {
            return IMessageListener.Type.GROUP_CHAT;
        } else if (type == Message.Type.HEADLINE) {
            return IMessageListener.Type.HEADLINE;
        } else if (type == Message.Type.HEADLINE) {
            return IMessageListener.Type.HEADLINE;
        } else
            return IMessageListener.Type.NORMAL;
    }
    protected void fireMessage(ID from, ID to, IMessageListener.Type type,
            String subject, String body) {
        for (Iterator i = messageListeners.iterator(); i.hasNext();) {
            IMessageListener l = (IMessageListener) i.next();
            l.handleMessage(from, to, type, subject, body);
        }
    }
    protected String canonicalizeRoomTo(String to) {
        if (to == null)
            return null;
        int index = to.indexOf("/");
        if (index > 0) {
            return to.substring(0, index);
        } else
            return to;
    }

    protected ID createRoomIDFromName(String from) {
    	try {
    		return new XMPPRoomID(usernamespace,connection,from);
    	} catch (URISyntaxException e) {
            dumpStack("Exception in createRoomIDFromName", e);
            return null;
    	}
    }

    protected void handleMessageEvent(MessageEvent evt) {
        Message msg = evt.getMessage();
        String from = msg.getFrom();
        String to = msg.getTo();
        String body = msg.getBody();
        String subject = msg.getSubject();
        ID fromID = createUserIDFromName(canonicalizeRoomFrom(from));
        ID toID = createUserIDFromName(canonicalizeRoomTo(to));
        msg = filterMessageType(msg);
        if (msg != null) fireMessage(fromID, toID, createMessageType(msg.getType()), subject, body);
    }

    protected IPresence.Type createIPresenceType(Presence xmppPresence) {
        if (xmppPresence == null)
            return IPresence.Type.AVAILABLE;
        Type type = xmppPresence.getType();
        if (type == Presence.Type.AVAILABLE) {
            return IPresence.Type.AVAILABLE;
        } else if (type == Presence.Type.ERROR) {
            return IPresence.Type.ERROR;
        } else if (type == Presence.Type.SUBSCRIBE) {
            return IPresence.Type.SUBSCRIBE;
        } else if (type == Presence.Type.SUBSCRIBED) {
            return IPresence.Type.SUBSCRIBED;
        } else if (type == Presence.Type.UNSUBSCRIBE) {
            return IPresence.Type.UNSUBSCRIBE;
        } else if (type == Presence.Type.UNSUBSCRIBED) {
            return IPresence.Type.UNSUBSCRIBED;
        } else if (type == Presence.Type.UNAVAILABLE) {
            return IPresence.Type.UNAVAILABLE;
        }
        return IPresence.Type.AVAILABLE;
    }

    protected IPresence.Mode createIPresenceMode(Presence xmppPresence) {
        if (xmppPresence == null)
            return IPresence.Mode.AVAILABLE;
        Mode mode = xmppPresence.getMode();
        if (mode == Presence.Mode.AVAILABLE) {
            return IPresence.Mode.AVAILABLE;
        } else if (mode == Presence.Mode.AWAY) {
            return IPresence.Mode.AWAY;
        } else if (mode == Presence.Mode.CHAT) {
            return IPresence.Mode.CHAT;
        } else if (mode == Presence.Mode.DO_NOT_DISTURB) {
            return IPresence.Mode.DND;
        } else if (mode == Presence.Mode.EXTENDED_AWAY) {
            return IPresence.Mode.EXTENDED_AWAY;
        } else if (mode == Presence.Mode.INVISIBLE) {
            return IPresence.Mode.INVISIBLE;
        }
        return IPresence.Mode.AVAILABLE;
    }
    protected IPresence createIPresence(Presence xmppPresence) {
        int priority = xmppPresence.getPriority();
        String status = xmppPresence.getStatus();
        IPresence newPresence = new org.eclipse.ecf.presence.impl.Presence(
                createIPresenceType(xmppPresence), priority, status,
                createIPresenceMode(xmppPresence));
        return newPresence;
    }

    protected void handlePresenceEvent(PresenceEvent evt) {
        Presence xmppPresence = evt.getPresence();
        String from = canonicalizeRoomFrom(xmppPresence.getFrom());
        IPresence newPresence = createIPresence(xmppPresence);
        ID fromID = createUserIDFromName(from);
		fireParticipant(fromID, newPresence);
    }

    protected void handleChatMembershipEvent(ChatMembershipEvent evt) {
    	String from = canonicalizeRoomFrom(evt.getFrom());
        ID fromID = createUserIDFromName(from);
        fireChatParticipant(fromID,evt.isAdd());
    }
    protected void fireParticipant(ID fromID, IPresence presence) {
        for (Iterator i = participantListeners.iterator(); i.hasNext();) {
            IChatParticipantListener l = (IChatParticipantListener) i.next();
            l.handlePresence(fromID, presence);
        }
    }
    protected void fireChatParticipant(ID fromID, boolean join) {
        for (Iterator i = participantListeners.iterator(); i.hasNext();) {
            IChatParticipantListener l = (IChatParticipantListener) i.next();
            if (join) {
            	l.handleArrivedInChat(fromID);
            } else {
            	l.handleDepartedFromChat(fromID);
            }
        }
    }
    /* (non-Javadoc)
     * @see org.eclipse.ecf.core.ISharedObject#handleEvent(org.eclipse.ecf.core.util.Event)
     */
    public void handleEvent(Event event) {
        debug("handleEvent(" + event + ")");
        if (event instanceof MessageEvent) {
            handleMessageEvent((MessageEvent) event);
        } else if (event instanceof PresenceEvent) {
        	handlePresenceEvent((PresenceEvent) event);
        } else if (event instanceof ChatMembershipEvent) {
        	handleChatMembershipEvent((ChatMembershipEvent) event);
        } else {	
        	debug("unrecognized event " + event);
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.ecf.core.ISharedObject#handleEvents(org.eclipse.ecf.core.util.Event[])
     */
    public void handleEvents(Event[] events) {
        for(int i=0; i < events.length; i++) {
            this.handleEvent(events[i]);
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.ecf.core.ISharedObject#dispose(org.eclipse.ecf.core.identity.ID)
     */
    public void dispose(ID containerID) {
        messageListeners.clear();
        participantListeners.clear();
        this.config = null;
        this.connection = null;
        this.usernamespace = null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ecf.core.ISharedObject#getAdapter(java.lang.Class)
     */
    public Object getAdapter(Class clazz) {
        return null;
    }
}
```

```



#### Error stacktrace:

```
com.thoughtworks.qdox.parser.impl.Parser.yyerror(Parser.java:2025)
	com.thoughtworks.qdox.parser.impl.Parser.yyparse(Parser.java:2147)
	com.thoughtworks.qdox.parser.impl.Parser.parse(Parser.java:2006)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:232)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:190)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:94)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:89)
	com.thoughtworks.qdox.library.SortedClassLibraryBuilder.addSource(SortedClassLibraryBuilder.java:162)
	com.thoughtworks.qdox.JavaProjectBuilder.addSource(JavaProjectBuilder.java:174)
	scala.meta.internal.mtags.JavaMtags.indexRoot(JavaMtags.scala:48)
	scala.meta.internal.metals.SemanticdbDefinition$.foreachWithReturnMtags(SemanticdbDefinition.scala:97)
	scala.meta.internal.metals.Indexer.indexSourceFile(Indexer.scala:489)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7(Indexer.scala:361)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7$adapted(Indexer.scala:356)
	scala.collection.IterableOnceOps.foreach(IterableOnce.scala:619)
	scala.collection.IterableOnceOps.foreach$(IterableOnce.scala:617)
	scala.collection.AbstractIterator.foreach(Iterator.scala:1306)
	scala.collection.parallel.ParIterableLike$Foreach.leaf(ParIterableLike.scala:938)
	scala.collection.parallel.Task.$anonfun$tryLeaf$1(Tasks.scala:52)
	scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
	scala.util.control.Breaks$$anon$1.catchBreak(Breaks.scala:97)
	scala.collection.parallel.Task.tryLeaf(Tasks.scala:55)
	scala.collection.parallel.Task.tryLeaf$(Tasks.scala:49)
	scala.collection.parallel.ParIterableLike$Foreach.tryLeaf(ParIterableLike.scala:935)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6496.java