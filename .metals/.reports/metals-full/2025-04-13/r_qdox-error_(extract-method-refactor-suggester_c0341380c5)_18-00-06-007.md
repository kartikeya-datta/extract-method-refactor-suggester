error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16563.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16563.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16563.java
text:
```scala
public S@@tring getAccountCreationInstructions() throws ECFException;

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
package org.eclipse.ecf.presence;

import java.util.Map;

import org.eclipse.ecf.core.util.ECFException;

/**
 * Presence account management. Access to instances implementing this interface
 * is provided by calling {@link IPresenceContainerAdapter#getAccountManager()}
 * 
 * @see IPresenceContainerAdapter
 */
public interface IAccountManager {

	/**
	 * Change account password to use new password. Upon next authenticated
	 * login, new password will be required for accessing account
	 * 
	 * @param newpassword
	 *            new password to use for this account
	 * @return true if password changed, false if not changed
	 * @throws ECFException
	 *             thrown if not connected, or if password change fails due to
	 *             network failure or server failure
	 */
	public boolean changePassword(String newpassword) throws ECFException;

	/**
	 * Determine whether account creation is supported for this account manager
	 * 
	 * @return true if account creation supported, false otherwise
	 * @throws ECFException
	 *             thrown if not connected, or if query fails due to network
	 *             failure or server failure
	 */
	public boolean isAccountCreationSupported() throws ECFException;

	/**
	 * Create a new account. Create a new account using given username,
	 * password, and attributes. If account creation succeeds, the method will
	 * return successfully. If fails, or is not supported, ECFException will be
	 * thrown
	 * 
	 * @param username
	 *            the fully qualified username to use for the new account
	 * @param password
	 *            the password to use with the new account
	 * @param attributes
	 *            attributes to associate with the new account
	 * @return true if account created, false if not created
	 * @throws ECFException
	 *             thrown if account creation is not supported, or if fails for
	 *             some reason (network failure or server failure)
	 */
	public boolean createAccount(String username, String password,
			Map attributes) throws ECFException;

	/**
	 * Delete an account. Deletes this account. If successful deletion, the
	 * method will return successfully. If account deletion is not supported,
	 * network failure, or some server error then an ECFException will be
	 * thrown.
	 * 
	 * @return true if account deleted, false if not deleted
	 * @throws ECFException
	 *             thrown if account deletion is not supported, or if fails for
	 *             some reason (network failure or server failure)
	 */
	public boolean deleteAccount() throws ECFException;

	/**
	 * Get any instructions for account
	 * 
	 * @return instructions for account
	 * @throws ECFException
	 *             thrown if account account instructions not supported, or if
	 *             fails for some reason (network failure or server failure)
	 */
	public String getAccountInstructions() throws ECFException;

	/**
	 * Get account attribute names for this account
	 * 
	 * @return String[] attribute names. Will return empty array if no attribute
	 *         names for account.
	 * @throws ECFException
	 *             thrown if get account attribute names not supported, or if
	 *             fails for some reason (network failure or server failure)
	 */
	public String[] getAccountAttributeNames() throws ECFException;

	/**
	 * Get the value of given
	 * 
	 * @param attributeName
	 *            the attribute name to return the value for
	 * @return Object value for the given attribute
	 * @throws ECFException
	 *             thrown if get account attribute not supported, or if fails
	 *             for some reason (network failure or server failure)
	 */
	public Object getAccountAttribute(String attributeName) throws ECFException;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16563.java