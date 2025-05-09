error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/526.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/526.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/526.java
text:
```scala
s@@witch (accessRule.getProblemId()) {

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.env;

import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.core.compiler.IProblem;

/**
 * Definition of a set of access rules used to flag forbidden references to non API code.
 */
public class AccessRuleSet {

	private AccessRule[] accessRules;
	public String[] messageTemplates;
	public static final int MESSAGE_TEMPLATES_LENGTH = 4;
	
	
	public AccessRuleSet(AccessRule[] accessRules) {
		this(accessRules, null);
	}
	
	/**
	 * Make a new set of access rules.
	 * @param accessRules the access rules to be contained by the new set
	 * @param messageTemplates a Sting[4] array specifying the messages for type, 
	 * constructor, method and field access violation; each should contain as many
	 * placeholders as expected by the respective access violation message (that is,
	 * one for type and constructor, two for method and field).
	 */
	// TODO (maxime) move to better support
	public AccessRuleSet(AccessRule[] accessRules, String[] messageTemplates) {
		this.accessRules = accessRules;
		if (messageTemplates != null && messageTemplates.length == MESSAGE_TEMPLATES_LENGTH)
			this.messageTemplates = messageTemplates;
		else
			this.messageTemplates = new String[] {"{0}", "{0}", "{0} {1}", "{0} {1}"};  //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object object) {
		if (this == object) 
			return true;
		if (!(object instanceof AccessRuleSet))
			return false;
		AccessRuleSet otherRuleSet = (AccessRuleSet) object;
		if (this.messageTemplates.length != MESSAGE_TEMPLATES_LENGTH ||
				otherRuleSet.messageTemplates.length != MESSAGE_TEMPLATES_LENGTH)
			return false; // guard
		for (int i = 0; i < MESSAGE_TEMPLATES_LENGTH; i++) 
			if (!this.messageTemplates[i].equals(otherRuleSet.messageTemplates[i])) 
				return false;
		int rulesLength = this.accessRules.length;
		if (rulesLength != otherRuleSet.accessRules.length) return false;
		for (int i = 0; i < rulesLength; i++)
			if (!this.accessRules[i].equals(otherRuleSet.accessRules[i]))
				return false;
		return true;
	}
	
	public AccessRule[] getAccessRules() {
		return this.accessRules;
	}
	
/**
 * Select the first access rule which is violated when accessing a given type, 
 * or null if no 'non accessible' access rule applies.
 * @param targetTypeFilePath the target type file path, formed as: 
 * "org/eclipse/jdt/core/JavaCore"
 * @return the first access restriction that applies if any, null else
 */
public AccessRestriction getViolatedRestriction(char[] targetTypeFilePath) {
	for (int i = 0, length = this.accessRules.length; i < length; i++) {
		AccessRule accessRule = this.accessRules[i];
		if (CharOperation.pathMatch(accessRule.pattern, targetTypeFilePath, 
				true/*case sensitive*/, '/')) {
			switch (accessRule.problemId) {
				case IProblem.ForbiddenReference:
				case IProblem.DiscouragedReference:
					return new AccessRestriction(accessRule, this.messageTemplates);
				default:
					return null;
			}
		}
	}
	return null;
}
	
	public String toString() {
		return toString(true/*wrap lines*/);
	}
	
	public String toString(boolean wrap) {
		StringBuffer buffer = new StringBuffer(200);
		buffer.append("AccessRuleSet {"); //$NON-NLS-1$
		if (wrap)
			buffer.append('\n');
		for (int i = 0, length = this.accessRules.length; i < length; i++) {
			if (wrap)
				buffer.append('\t');
			AccessRule accessRule = this.accessRules[i];
			buffer.append(accessRule);
			if (wrap)
				buffer.append('\n');
			else if (i < length-1)
				buffer.append(", "); //$NON-NLS-1$
		}
		buffer.append("} [templates:\""); //$NON-NLS-1$
		for (int i = 0; i < messageTemplates.length; i++)
			buffer.append(this.messageTemplates[i]);
		buffer.append("\"]"); //$NON-NLS-1$
		return buffer.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/526.java