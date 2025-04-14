error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/511.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/511.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/511.java
text:
```scala
private b@@oolean filterRoles = true;

/*******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.roles;

/**
 * RoleManager is the type that defines and filters based on
 * role.
 */
public class RoleManager {

	private static RoleManager singleton;
	private boolean filterRoles = false;

	private Role[] roles;

	//The patterns for the mappings

	public static String JDT_PATTERN = "org.eclipse.jdt.*";
	public static String DEBUG_PATTERN = "org.eclipse.debug.*";
	public static String PDE_PATTERN = "org.eclipse.pde.*";
	public static String TEAM_PATTERN = "org.eclipse.team.*";
	public static String ANT_PATTERN = "org.eclipse.ant.*";
	public static String EXTERNAL_TOOLS_PATTERN = "org.eclipse.ui.externaltools";

	public static RoleManager getInstance() {
		if (singleton == null)
			singleton = new RoleManager();
		return singleton;

	}

	private RoleManager() {
		createDefaultRoles();
	}

	/**
	 * Create the hardcoded roles for the reciever.
	 * NOTE: These will be replaced by a proper sdk based
	 * extension system.
	 *
	 */
	private void createDefaultRoles() {
		roles = new Role[4];
		roles[0] =
			new Role(
				"Java Role",
				"org.eclipse.roles.javaRole",
				new String[] { JDT_PATTERN, DEBUG_PATTERN });
		roles[1] = new Role("PDE Role", "org.eclipse.roles.pdeRole", new String[] { PDE_PATTERN });
		roles[2] =
			new Role("Team Role", "org.eclipse.roles.teamRole", new String[] { TEAM_PATTERN });
		roles[3] =
			new Role(
				"External Tools Role",
				"org.eclipse.roles.externalToolsRole",
				new String[] { EXTERNAL_TOOLS_PATTERN, ANT_PATTERN });
	}

	/**
	 * Return whether or not the id is enabled. If there is a role
	 * whose pattern matches the id return whether or not the role is
	 * enabled. If there is no match return true;
	 * @param id
	 * @return
	 */
	public boolean isEnabledId(String id) {
		
		if(!filterRoles)
			return true;
		for (int i = 0; i < roles.length; i++) {
			if (roles[i].patternMatches(id))
				return roles[i].enabled;
		}
		return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/511.java