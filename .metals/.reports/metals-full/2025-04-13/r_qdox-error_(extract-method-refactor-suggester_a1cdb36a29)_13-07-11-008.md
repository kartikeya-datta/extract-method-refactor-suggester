error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10178.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10178.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10178.java
text:
```scala
a@@RightState.successfulCompile(oldConfig,true);

/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.aspectj.ajdt.internal.core.builder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;


public class AjStateTest extends TestCase {

    private AjState aRightState;
    private AjBuildConfig oldConfig;
    private AjBuildConfig newConfig;
    
    public void testNoChange() {
        assertTrue("Can do incremental",aRightState.prepareForNextBuild(newConfig));
    }
    
    public void testAddEntryToClasspath() {
        newConfig.getClasspath().add("anotherEntry");
        assertFalse("Can do incremental",aRightState.prepareForNextBuild(newConfig));
    }
    
    public void testRemoveEntryFromClasspath() {
        newConfig.getClasspath().remove(0);
        assertFalse("Can do incremental",aRightState.prepareForNextBuild(newConfig));               
    }
    
    public void testReorderClasspath() {
        Object o = newConfig.getClasspath().remove(0);
        newConfig.getClasspath().add(o);
        assertFalse("Can do incremental",aRightState.prepareForNextBuild(newConfig));                       
    }

    public void testAddEntryToAspectpath() {
        newConfig.getAspectpath().add(new File("anotherEntry.jar"));
        assertFalse("Can do incremental",aRightState.prepareForNextBuild(newConfig));
    }
    
    public void testRemoveEntryFromAspectpath() {
        newConfig.getAspectpath().remove(0);
        assertFalse("Can do incremental",aRightState.prepareForNextBuild(newConfig));               
    }
    
    public void testReorderAspectpath() {
        Object o = newConfig.getClasspath().remove(0);
        newConfig.getAspectpath().add(o);
        assertFalse("Can do incremental",aRightState.prepareForNextBuild(newConfig));                       
    }

    public void testAddEntryToInpath() {
        newConfig.getInpath().add(new File("anotherEntry"));
        assertFalse("Can do incremental",aRightState.prepareForNextBuild(newConfig));
    }
    
    public void testRemoveEntryFromInpath() {
        newConfig.getInpath().remove(0);
        assertFalse("Can do incremental",aRightState.prepareForNextBuild(newConfig));               
    }
    
    public void testReorderInpath() {
        Object o = newConfig.getClasspath().remove(0);
        newConfig.getInpath().add(o);
        assertFalse("Can do incremental",aRightState.prepareForNextBuild(newConfig));                       
    }
    
    public void testAddEntryToInjars() {
        newConfig.getInJars().add(new File("anotherEntry.jar"));
        assertFalse("Can do incremental",aRightState.prepareForNextBuild(newConfig));
    }
    
    public void testRemoveEntryFromInjars() {
        newConfig.getInJars().remove(0);
        assertFalse("Can do incremental",aRightState.prepareForNextBuild(newConfig));               
    }
    
    public void testReorderInjars() {
        Object o = newConfig.getClasspath().remove(0);
        newConfig.getInJars().add(o);
        assertFalse("Can do incremental",aRightState.prepareForNextBuild(newConfig));                       
    }
    
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        aRightState = new AjState(null);
        oldConfig = new AjBuildConfig();
        newConfig = new AjBuildConfig();
        List cp = new ArrayList();
        cp.add("adir");
        cp.add("ajar.jar");
        oldConfig.setClasspath(cp);
        newConfig.setClasspath(new ArrayList(cp));
        List ap = new ArrayList();
        ap.add(new File("aLib.jar"));
        ap.add(new File("anotherLib.jar"));
        oldConfig.setAspectpath(ap);
        newConfig.setAspectpath(new ArrayList(ap));
        List ip = new ArrayList();
        ip.add(new File("adir"));
        ip.add(new File("ajar.jar"));
        oldConfig.setInPath(ip);
        newConfig.setInPath(new ArrayList(ip));
        List ij = new ArrayList();
        ij.add(new File("aLib.jar"));
        ij.add(new File("anotherLib.jar"));
        oldConfig.setInJars(ij);
        newConfig.setInJars(new ArrayList(ij));
        aRightState.prepareForNextBuild(oldConfig);
        aRightState.successfulCompile(oldConfig);
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10178.java