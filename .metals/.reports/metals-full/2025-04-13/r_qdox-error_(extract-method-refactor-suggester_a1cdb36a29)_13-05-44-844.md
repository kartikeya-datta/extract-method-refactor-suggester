error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5933.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5933.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5933.java
text:
```scala
r@@eturn programElement.toLabelString();

/* *******************************************************************
 * Copyright (c) 1999-2001 Xerox Corporation, 
 *               2002 Palo Alto Research Center, Incorporated (PARC).
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     Xerox/PARC     initial implementation 
 * ******************************************************************/


package org.aspectj.ajde.ui.swing;

import java.util.*;

import javax.swing.tree.DefaultMutableTreeNode;

import org.aspectj.ajde.ui.*;
import org.aspectj.ajde.ui.IStructureViewNode.Kind;
import org.aspectj.asm.*;

/**
 * @author Mik Kersten
 */
public class SwingTreeViewNode extends DefaultMutableTreeNode implements IStructureViewNode {

	private String relationshipName;
	private IProgramElement programElement;
	private AbstractIcon icon;
	private IStructureViewNode.Kind kind;

	/**
	 * Create a declaration node.
	 */	
	public SwingTreeViewNode(IProgramElement programElement, AbstractIcon icon, List children) {
		super(programElement, true);
		this.programElement = programElement;
		this.icon = icon;
		this.kind = Kind.DECLARATION;
		
		if (children != null) {
			for (Iterator it = children.iterator(); it.hasNext(); ) { 
				super.add((SwingTreeViewNode)it.next());	
			}
		}
	}

	/**
	 * Create a relationship node.
	 */	
	public SwingTreeViewNode(IRelationship relationship, AbstractIcon icon) {
		super(null, true);
		this.icon = icon;
		this.kind = Kind.RELATIONSHIP;
		this.relationshipName = relationship.getName();
	}
	
	/**
	 * Create a link.
	 */	
	public SwingTreeViewNode(IProgramElement programElement, AbstractIcon icon) {
		super(programElement, false);
		this.programElement = programElement;
		this.kind = Kind.LINK;
		this.icon = icon;
	}
	
	public IProgramElement getStructureNode() {
		return programElement;	
	}
	
	public AbstractIcon getIcon() {
		return icon;
	}	

	public void add(IStructureViewNode child) { 
		super.add((DefaultMutableTreeNode)child);
	}

	public void add(IStructureViewNode child, int position) { 
		super.insert((DefaultMutableTreeNode)child, position);
	}
	
	public void remove(IStructureViewNode child) { 
		super.remove((DefaultMutableTreeNode)child);
	}
	
	public List getChildren() {
		if (children == null) {
			return new ArrayList();
		} else {
			return children;
		}	
	}
	
	public Kind getKind() {
		return kind;
	}

	public String getRelationshipName() {
		return relationshipName;
	}
	
	public String toString() {
		if (kind == IStructureViewNode.Kind.RELATIONSHIP) {
			return relationshipName;
		} else {
			return programElement.getName();
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5933.java