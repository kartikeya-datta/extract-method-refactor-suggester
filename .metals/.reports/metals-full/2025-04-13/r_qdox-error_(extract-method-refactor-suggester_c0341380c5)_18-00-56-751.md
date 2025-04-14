error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15589.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15589.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15589.java
text:
```scala
private S@@tring vmLevel = "1.3";

/* *******************************************************************
 * Copyright (c) 2004 IBM Corporation
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     Adrian Colyer, 
 * ******************************************************************/
package org.aspectj.testing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.aspectj.tools.ajc.AjcTestCase;

/**
 * @author colyer
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AjcTest {
	
	private static boolean is13VMOrGreater = true;
	private static boolean is14VMOrGreater = true;
	private static boolean is15VMOrGreater = false;
	
	static {
		String vm = System.getProperty("java.runtime.version");
        if (vm == null) {
            vm = System.getProperty("java.vm.version");
        }
		if (vm.startsWith("1.3")) {
			is14VMOrGreater = false;
		} else if (vm.startsWith("1.5")) {
			is15VMOrGreater = true;
		}
	}

	private List testSteps = new ArrayList();
	
	private String dir;
	private String pr;
	private String title;
	private String keywords;
	private String comment;
	private String vmLevel = "1.4";

	public AjcTest() {
	}
	
	public void addTestStep(ITestStep step) {
		testSteps.add(step);
		step.setTest(this);
	}
	
	public void runTest(AjcTestCase testCase) {
		if (!canRunOnThisVM()) return;
		try {
			System.out.print("TEST: " + getTitle() + "\t");			
			for (Iterator iter = testSteps.iterator(); iter.hasNext();) {
				ITestStep step = (ITestStep) iter.next();
				step.setBaseDir(getDir());
				System.out.print(".");
				step.execute(testCase);
			}
		} finally {
			System.out.println("DONE");
		}
	}
	
	public boolean canRunOnThisVM() {		
		if (vmLevel.equals("1.3")) return true;
		boolean canRun = true;
		if (vmLevel.equals("1.4")) canRun = is14VMOrGreater;
		if (vmLevel.equals("1.5")) canRun = is15VMOrGreater;
		if (!canRun) {
			System.out.println("***SKIPPING TEST***" + getTitle()+ " needs " + getVmLevel() 
					+ ", currently running on " + System.getProperty("java.vm.version"));
		}
		return canRun;
	}
	
	/**
	 * @return Returns the comment.
	 */
	public String getComment() {
		return comment;
	}
	/**
	 * @param comment The comment to set.
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	/**
	 * @return Returns the dir.
	 */
	public String getDir() {
		return dir;
	}
	/**
	 * @param dir The dir to set.
	 */
	public void setDir(String dir) {
		dir = "../tests/" + dir;
		this.dir = dir;
	}
	/**
	 * @return Returns the keywords.
	 */
	public String getKeywords() {
		return keywords;
	}
	/**
	 * @param keywords The keywords to set.
	 */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	/**
	 * @return Returns the pr.
	 */
	public String getPr() {
		return pr;
	}
	/**
	 * @param pr The pr to set.
	 */
	public void setPr(String pr) {
		this.pr = pr;
	}
	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @param vmLevel The vmLevel to set.
	 */
	public void setVm(String vmLevel) {
		this.vmLevel = vmLevel;
	}
	
	/**
	 * @return Returns the vmLevel.
	 */
	public String getVmLevel() {
		return vmLevel;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15589.java