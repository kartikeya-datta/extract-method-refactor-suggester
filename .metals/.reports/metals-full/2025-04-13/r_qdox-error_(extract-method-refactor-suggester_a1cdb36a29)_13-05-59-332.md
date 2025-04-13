error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6801.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6801.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6801.java
text:
```scala
R@@epository repository = (Repository) getCheckedRef(Repository.class,

/*
 * Copyright  2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.apache.tools.ant.taskdefs.optional.repository;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.DataType;

import java.io.IOException;

/**
 * This type represents a repository; a place that stores libraries for
 * retrieval. To use this type, you must use a non-abstract class, either one
 * that ships with Ant, or one you implement and declare yourself.
 * <p/>
 * The &lt;getlibraries&gt; task lets you supply a repository by reference
 * inline {@link GetLibraries#add(Repository)} or on the command line {@link
 * GetLibraries#setRepositoryRef(org.apache.tools.ant.types.Reference)}
 *
 * @since Ant1.7
 */
public abstract class Repository extends DataType {


    /**
     * validate yourself
     *
     * @throws BuildException if unhappy
     */
    public void validate() {
    }

    /**
     * recursively resolve any references to get the real repository
     *
     * @return
     */
    public final Repository resolve() {
        if (getRefid() == null) {
            return this;
        } else {
            Repository repository = (Repository) getCheckedRef(this.getClass(),
                    "Repository");
            return repository;
        }
    }

    /**
     * override point: connection is called at the start of the retrieval
     * process
     *
     * @param owner owner of the libraries
     *
     * @throws BuildException
     */
    public void connect(GetLibraries owner) {

    }

    /**
     * override point: connection is called at the start of the retrieval
     * process
     *
     * @throws BuildException
     */

    public void disconnect() {

    }


    /**
     * Test for a repository being reachable. This method is called after {@link
     * #connect(GetLibraries)} is called, before any files are to be retrieved.
     * <p/>
     * If it returns false the repository considers itself offline. Similarly,
     * any ioexception is interpreted as being offline.
     *
     * @return true if the repository is online.
     *
     * @throws IOException
     */
    public abstract boolean checkRepositoryReachable() throws IOException;

    /**
     * fetch a library from the repository
     *
     * @param library
     *
     * @return
     */
    public abstract boolean fetch(Library library) throws IOException;


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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6801.java