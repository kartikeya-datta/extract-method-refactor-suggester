error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13711.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13711.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13711.java
text:
```scala
S@@tring ROLE = Project.class.getName();

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.myrmidon.interfaces.model;

import java.io.File;
import org.apache.avalon.framework.component.Component;

/**
 * Abstraction used to interact with projects.
 * Implementations may choose to structure it anyway they choose.
 *
 * TODO: Determine if projects should carry their own name. Breaks IOC but
 * Can be useful as project files embed own name (or should that be description).
 *
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 */
public interface Project
    extends Component
{
    String ROLE = "org.apache.myrmidon.interfaces.model.Project";

    // the name of currently executing project
    String PROJECT = "myrmidon.project.name";

    // the name of currently executing project
    //String PROJECT_FILE     = "myrmidon.project.file";

    // the name of currently executing target
    //String TARGET           = "myrmidon.target.name";

    /**
     * Get the imports for project.
     *
     * @return the imports
     */
    TypeLib[] getTypeLibs();

    /**
     * Get names of projects referred to by this project.
     *
     * @return the names
     */
    String[] getProjectNames();

    /**
     * Retrieve project reffered to by this project.
     *
     * @param name the project name
     * @return the Project or null if none by that name
     */
    Project getProject( String name );

    /**
     * Get name of default target.
     *
     * @return the default target name
     */
    String getDefaultTargetName();

    /**
     * Retrieve implicit target.
     * The implicit target is top level tasks.
     * Currently restricted to property tasks.
     *
     * @return the Target
     */
    Target getImplicitTarget();

    /**
     * Retrieve a target by name.
     *
     * @param name the name of target
     * @return the Target or null if no target exists with name
     */
    Target getTarget( String name );

    /**
     * Retrieve names of all targets in project.
     *
     * @return the iterator of project names
     */
    String[] getTargetNames();

    /**
     * Retrieve base directory of project.
     *
     * @return the projects base directory
     */
    File getBaseDirectory();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13711.java