error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16444.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16444.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16444.java
text:
```scala
public v@@oid setConnection(XMPPConnection connection) {

/*******************************************************************************
 * Copyright (c) 2004 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.internal.provider.xmpp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.ecf.core.util.ECFException;
import org.eclipse.ecf.presence.IAccountManager;
import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class XMPPContainerAccountManager implements IAccountManager {

	private static final String NOT_CONNECTED = "not connected";

	AccountManager accountManager = null;

	protected void traceAndThrow(String msg, Throwable t) throws ECFException {
		throw new ECFException(msg, t);
	}

	protected AccountManager getAccountManagerOrThrowIfNull()
			throws ECFException {
		if (accountManager == null)
			throw new ECFException(NOT_CONNECTED);
		return accountManager;
	}

	public XMPPContainerAccountManager() {
	}

	public void dispose() {
		accountManager = null;
	}

	protected void setConnection(XMPPConnection connection) {
		this.accountManager = (connection == null) ? null : new AccountManager(
				connection);
	}

	public boolean changePassword(String newpassword) throws ECFException {
		try {
			getAccountManagerOrThrowIfNull().changePassword(newpassword);
		} catch (XMPPException e) {
			traceAndThrow("server exception changing password", e);
		}
		return true;
	}

	public boolean createAccount(String username, String password,
			Map attributes) throws ECFException {
		try {
			getAccountManagerOrThrowIfNull().createAccount(username, password,
					attributes);
		} catch (XMPPException e) {
			traceAndThrow("server exception creating account for " + username,
					e);
		}
		return true;
	}

	public boolean deleteAccount() throws ECFException {
		try {
			getAccountManagerOrThrowIfNull().deleteAccount();
		} catch (XMPPException e) {
			traceAndThrow("server exception deleting account", e);
		}
		return true;
	}

	public String getAccountCreationInstructions() {
		if (accountManager == null)
			return "";
		return accountManager.getAccountInstructions();
	}

	public String[] getAccountAttributeNames() {
		if (accountManager == null)
			return new String[0];
		Iterator i = accountManager.getAccountAttributes();
		List l = new ArrayList();
		for (; i.hasNext();) {
			l.add(i.next());
		}
		return (String[]) l.toArray(new String[] {});
	}

	public Object getAccountAttribute(String name) {
		if (accountManager == null)
			return null;
		return accountManager.getAccountAttribute(name);
	}

	public boolean isAccountCreationSupported() {
		if (accountManager == null)
			return false;
		return accountManager.supportsAccountCreation();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16444.java