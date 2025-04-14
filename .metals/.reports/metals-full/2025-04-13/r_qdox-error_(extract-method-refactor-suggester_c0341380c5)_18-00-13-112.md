error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15880.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15880.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15880.java
text:
```scala
A@@jcTestCase.fail(ex.toString());

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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.aspectj.tools.ajc.AjcTestCase;
import org.aspectj.util.FileUtil;

/**
 * @author colyer
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RunSpec implements ITestStep {

	private List expected = new ArrayList();
	private String classToRun;
	private String baseDir;
	private String options;
	private String cpath;
	private AjcTest myTest;
	private OutputSpec stdErrSpec;
	private OutputSpec stdOutSpec;
	private String ltwFile;
	
	public RunSpec() {
	}
	
	/* (non-Javadoc)
	 * @see org.aspectj.testing.ITestStep#execute(org.aspectj.tools.ajc.AjcTestCase)
	 */
	public void execute(AjcTestCase inTestCase) {
		if (!expected.isEmpty()) {
			System.err.println("Warning, message spec for run command is currently ignored (org.aspectj.testing.RunSpec)");
		}
		String[] args = buildArgs();
//		System.err.println("? execute() inTestCase='" + inTestCase + "', ltwFile=" + ltwFile);
		boolean useLtw = copyLtwFile(inTestCase.getSandboxDirectory());
		AjcTestCase.RunResult rr = inTestCase.run(getClassToRun(),args,getClasspath(),useLtw);
		if (stdErrSpec != null) {
			stdErrSpec.matchAgainst(rr.getStdErr());
		}
		if (stdOutSpec != null) {
			stdOutSpec.matchAgainst(rr.getStdOut());
		}
	}

	public void addExpectedMessage(ExpectedMessageSpec message) {
		expected.add(message);
	}

	public void setBaseDir(String dir) {
		this.baseDir = dir;
	}

	public void setTest(AjcTest test) {
		this.myTest = test;
	}
	
	public String getOptions() {
		return options;
	}
	
	public void setOptions(String options) {
		this.options = options;
	}
	
	public String getClasspath() {
		if (cpath == null) return null;
		return this.cpath.replace('/', File.separatorChar);
	}
 	
	public void setClasspath(String cpath) {
		this.cpath = cpath;
	}
	
	public void addStdErrSpec(OutputSpec spec) {
		this.stdErrSpec = spec;
	}
	public void addStdOutSpec(OutputSpec spec) {
		this.stdOutSpec = spec;
	}
	/**
	 * @return Returns the classToRun.
	 */
	public String getClassToRun() {
		return classToRun;
	}
	/**
	 * @param classToRun The classToRun to set.
	 */
	public void setClassToRun(String classToRun) {
		this.classToRun = classToRun;
	}

	public String getLtwFile() {
		return ltwFile;
	}

	public void setLtwFile(String ltwFile) {
		this.ltwFile = ltwFile;
	}
	
	private String[] buildArgs() {
		if (options == null) return new String[0];
		StringTokenizer strTok = new StringTokenizer(options,",");
		String[] ret = new String[strTok.countTokens()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = strTok.nextToken();
		}
		return ret;
	}
	
	private boolean copyLtwFile (File sandboxDirectory) {
		boolean useLtw = false;
		
		if (ltwFile != null) {
            // TODO maw use flag rather than empty file name
			if (ltwFile.trim().length() == 0) return true;
			
			File from = new File(baseDir,ltwFile);
			File to = new File(sandboxDirectory,"META-INF" + File.separator + "aop.xml");
//			System.out.println("RunSpec.copyLtwFile() from=" + from.getAbsolutePath() + " to=" + to.getAbsolutePath());
			try {
				FileUtil.copyFile(from,to);
				useLtw = true;
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		return useLtw;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15880.java