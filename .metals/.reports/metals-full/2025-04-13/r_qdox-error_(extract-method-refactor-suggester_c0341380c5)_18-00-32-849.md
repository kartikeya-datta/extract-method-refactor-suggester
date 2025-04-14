error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5792.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5792.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5792.java
text:
```scala
r@@eturn new byte[0];

/****************************************************************************
 * Copyright (c) 2006, 2007 Remy Suen, Composent Inc., and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Remy Suen <remy.suen@gmail.com> - initial API and implementation
 *****************************************************************************/
package org.eclipse.ecf.internal.provider.msn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDCreateException;
import org.eclipse.ecf.core.identity.Namespace;
import org.eclipse.ecf.core.user.IUser;
import org.eclipse.ecf.presence.IPresence;
import org.eclipse.ecf.presence.roster.IRoster;
import org.eclipse.ecf.presence.roster.IRosterEntry;
import org.eclipse.ecf.presence.roster.IRosterItem;
import org.eclipse.ecf.protocol.msn.Contact;
import org.eclipse.ecf.protocol.msn.Status;

final class MSNRosterEntry implements IPresence, IRosterEntry, IUser {

	private static final long serialVersionUID = 5358415024505371809L;

	private Collection groups;

	private IRosterItem parent;

	private final Contact contact;

	private MSNID id;

	private IRoster roster;

	private Map properties;

	MSNRosterEntry(IRoster roster, Contact contact, Namespace namespace) {
		this.roster = roster;
		this.contact = contact;
		groups = Collections.EMPTY_LIST;
		properties = new HashMap(1);
		try {
			id = (MSNID) namespace.createInstance(new Object[] { contact
					.getEmail() });
		} catch (IDCreateException e) {
			// ignored since this is not possible
		}
	}

	void updatePersonalMessage() {
		String message = contact.getPersonalMessage();
		if (message.equals("")) { //$NON-NLS-1$
			properties.remove(Messages.MSNRosterEntry_Message);
		} else {
			properties.put(Messages.MSNRosterEntry_Message, message);
		}
	}

	Contact getContact() {
		return contact;
	}

	public String getName() {
		return contact.getDisplayName();
	}

	public Mode getMode() {
		Status status = contact.getStatus();
		if (status == Status.ONLINE) {
			return Mode.AVAILABLE;
		} else if (status == Status.BUSY) {
			return Mode.DND;
		} else if (status == Status.APPEAR_OFFLINE) {
			return Mode.INVISIBLE;
		} else {
			return Mode.AWAY;
		}
	}

	public Map getProperties() {
		return properties;
	}

	public String getStatus() {
		return contact.getPersonalMessage();
	}

	public Type getType() {
		return contact.getStatus() == Status.OFFLINE ? Type.UNAVAILABLE
				: Type.AVAILABLE;
	}

	public Object getAdapter(Class adapter) {
		if (adapter != null && adapter.isInstance(this)) {
			return this;
		} else {
			return null;
		}
	}

	public Collection getGroups() {
		return groups;
	}

	public IPresence getPresence() {
		return this;
	}

	public IUser getUser() {
		return this;
	}

	void setParent(IRosterItem parent) {
		this.parent = parent;
		if (parent instanceof IRoster) {
			groups = Collections.EMPTY_LIST;
		} else {
			ArrayList list = new ArrayList(1);
			list.add(parent);
			groups = Collections.unmodifiableCollection(list);
		}
	}

	public IRosterItem getParent() {
		return parent;
	}

	public byte[] getPictureData() {
		// TODO: update this when avatars have been implemented
		return null;
	}

	public ID getID() {
		return id;
	}

	public String getNickname() {
		return contact.getDisplayName();
	}

	public IRoster getRoster() {
		return roster;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:169)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5792.java