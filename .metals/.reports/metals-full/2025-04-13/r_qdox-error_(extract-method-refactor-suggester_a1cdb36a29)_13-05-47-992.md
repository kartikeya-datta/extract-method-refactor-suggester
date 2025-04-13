error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14505.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14505.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14505.java
text:
```scala
S@@ystem.getProperty("sun.boot.class.path") + File.pathSeparator +  "../runtime/bin" + File.pathSeparator + "../aspectj5rt/bin" +

/* *******************************************************************
 * Copyright (c) 2002 Palo Alto Research Center, Incorporated (PARC).
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     Xerox/PARC     initial implementation 
 * 	   AMC 01.20.2003 extended to support AspectJ 1.1 options
 * ******************************************************************/


package org.aspectj.ajde;

import java.io.*;
import java.util.*;

import org.aspectj.util.FileUtil;

/**
 * @author	Mik Kersten
 */
public class NullIdeProperties implements ProjectPropertiesAdapter {

	private String testProjectPath = "";
	private List buildConfigFiles = new ArrayList();

	private Set inJars;
	private Set inpath;
	private Set sourceRoots;
	private Set aspectPath;
	private String outJar;

	public NullIdeProperties(String testProjectPath) {
		this.testProjectPath = testProjectPath;
	}

	public List getBuildConfigFiles() {
		return buildConfigFiles;
	}
	
	public String getLastActiveBuildConfigFile() {
		return null;	
	}
	
	public String getDefaultBuildConfigFile() {
		return null;	
	}

    public String getProjectName() {
    	return "test";	
    }

    public String getRootProjectDir() {
    	return testProjectPath;
    }

    public List getProjectSourceFiles() {
    	return null;	
    }

    public String getProjectSourcePath() {
		return testProjectPath + "/src";   	
    }

    public String getClasspath() {
    	//XXX
    	// AMC - added in path separator since absence was causing
    	// build failures with invalid classpath
    	// AMC - subsequently added value of "aspectjrt.path property so that
    	// when testing with a non-development jar the version tests find the right one. 
    	return testProjectPath + File.pathSeparator +
    		System.getProperty("sun.boot.class.path") + File.pathSeparator +  "../runtime/bin" +
    		File.pathSeparator + System.getProperty("aspectjrt.path");	
    }

    public String getOutputPath() {
    	return testProjectPath + "/bin"; 
    }

    public String getAjcWorkingDir() {
    	return testProjectPath + "/ajworkingdir";	
    }
 
    public String getBootClasspath() {
    	return null;
    }
    
    public String getClassToExecute() {
    	return "figures.Main";	
    }

    public String getExecutionArgs() {
    	return null;
    }

    public String getVmArgs() {
    	return null;	
    }
    
    public void setInJars( Set jars ) { this.inJars = jars; }
    
    public void setInpath( Set path) { this.inpath = path; }
    
    public Set getInJars( ) {
    	return inJars;
    }
    
    public Set getInpath( ) {
    	return inpath;
    }
    
	public Map getSourcePathResources() {
		File srcBase = new File(getProjectSourcePath());
		File[] fromResources = FileUtil.listFiles(srcBase, new FileFilter() {
			public boolean accept(File pathname) {
				String name = pathname.getName().toLowerCase();
				return !name.endsWith(".class") && !name.endsWith(".java") && !name.endsWith(".aj");
			}
		});
		Map map = new HashMap();
		for (int i = 0; i < fromResources.length; i++) {
			String normPath = FileUtil.normalizedPath(fromResources[i] ,srcBase);
			map.put(normPath, fromResources[i]);

		}
		return map;
	}

	public void setOutJar( String jar ){ this.outJar = jar; }

    public String getOutJar() {
    	return outJar;
    }
    
    public void setSourceRoots( Set roots ) { this.sourceRoots = roots; }

    public Set getSourceRoots() {
    	return sourceRoots;
    }

	public void setAspectPath( Set path ) { this.aspectPath = path; }
	    
    public Set getAspectPath() {
    	return aspectPath;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14505.java