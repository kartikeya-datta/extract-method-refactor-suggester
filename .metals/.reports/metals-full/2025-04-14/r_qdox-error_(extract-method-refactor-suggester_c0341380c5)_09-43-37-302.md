error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5320.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5320.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5320.java
text:
```scala
S@@tringBuffer buffer = new StringBuffer(200);

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.env;

import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.util.Util;

/**
 * Definition of an access restriction rule used to flag forbidden references to non API code.
 * A restriction can chain to further ones, the first violated restriction taking precedence.
 */
public class AccessRestriction {

	private char[][] inclusionPatterns;
	private char[][] exclusionPatterns;
	protected String messageTemplate;
	AccessRestriction furtherRestriction; // subsequent restriction
	
	
	public AccessRestriction(String messageTemplate, char[][] inclusionPatterns, char[][] exclusionPatterns, AccessRestriction furtherRestriction) {
		this.messageTemplate = messageTemplate;
		this.inclusionPatterns = inclusionPatterns;
		this.exclusionPatterns = exclusionPatterns;
		this.furtherRestriction = furtherRestriction;
	}
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object object) {
		if (this == object) 
			return true;
		if (!(object instanceof AccessRestriction))
			return false;
		AccessRestriction otherRestriction = (AccessRestriction) object;
		if (!this.messageTemplate.equals(otherRestriction.messageTemplate)) 
			return false;
		if (this.inclusionPatterns != otherRestriction.inclusionPatterns) {
			int length = this.inclusionPatterns == null ? 0 : this.inclusionPatterns.length;
			int otherLength = otherRestriction.inclusionPatterns == null ? 0 : otherRestriction.inclusionPatterns.length;
			if (length != otherLength)
				return false;
			for (int i = 0; i < length; i++) {
				if (!CharOperation.equals(this.inclusionPatterns[i], otherRestriction.inclusionPatterns[i]))
						return false;
			}
		}
		if (this.exclusionPatterns != otherRestriction.exclusionPatterns) {
			int length = this.exclusionPatterns == null ? 0 : this.exclusionPatterns.length;
			int otherLength = otherRestriction.exclusionPatterns == null ? 0 : otherRestriction.exclusionPatterns.length;
			if (length != otherLength)
				return false;
			for (int i = 0; i < length; i++) {
				if (!CharOperation.equals(this.exclusionPatterns[i], otherRestriction.exclusionPatterns[i]))
						return false;
			}
		}
		if (this.furtherRestriction != otherRestriction.furtherRestriction) {
			if (this.furtherRestriction == null || otherRestriction.furtherRestriction == null) 
				return false;
			if (!this.furtherRestriction.equals(otherRestriction.furtherRestriction))
				return false;
		}
		return true;
	}
	/**
	 * Select the first restriction which is violated when accessing a given type, or null if no restriction applies.
	 * Type name is formed as: "java/lang/Object".
	 */
	public AccessRestriction getViolatedRestriction(char[] targetTypeName, char[] referringTypeName) {
		
		// check local inclusion/exclusion rules
		if (this.inclusionPatterns != null || this.exclusionPatterns != null) {
			if (Util.isExcluded(targetTypeName, this.inclusionPatterns, this.exclusionPatterns, false)) {
				return this;
			}
		}		
	// then check further restrictions
		return this.furtherRestriction != null 
						? this.furtherRestriction.getViolatedRestriction(targetTypeName, referringTypeName)
						: null;
	}
	public char[][] getExclusionPatterns() {
			return this.exclusionPatterns;
	}
	public char[][] getInclusionPatterns() {
			return this.inclusionPatterns;
	}
	/**
	 * Returns readable description for problem reporting, 
	 * message is expected to contain room for restricted type name
	 * e.g. "{0} has restricted access"
	 */
	public String getMessageTemplate() {
			return this.messageTemplate;
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer(20);
		buffer
			.append("AccessRestriction [includes:\"") //$NON-NLS-1$
			.append(CharOperation.concatWith(this.inclusionPatterns,'/'))
			.append("\"][excludes:\"") //$NON-NLS-1$
			.append(CharOperation.concatWith(this.exclusionPatterns,'/'))
			.append("\"][template:\"") //$NON-NLS-1$
			.append(this.messageTemplate)
			.append("\"]"); //$NON-NLS-1$
		if (this.furtherRestriction != null) {
			buffer.append('\n').append(this.furtherRestriction);
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5320.java