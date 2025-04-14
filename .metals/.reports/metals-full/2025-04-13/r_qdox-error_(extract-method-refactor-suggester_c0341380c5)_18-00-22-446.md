error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11944.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11944.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11944.java
text:
```scala
S@@tring ROLE = FileSystemManager.class.getName();

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.aut.vfs;

import org.apache.avalon.framework.component.Component;

/**
 * A FileSystemManager is manages a set of file systems.  This interface is
 * used to locate a {@link FileObject} by name from one of those file systems.
 *
 * <p>To locate a {@link FileObject}, use one of the <code>resolveFile()</code>
 * methods.</p>
 *
 * <h4><a name="naming">File Naming</a></h4>
 *
 * <p>A file system manager can recognise several types of file names:
 *
 * <ul>
 *
 * <li><p>Absolute URI.  These must start with a scheme, such as
 * <code>file:</code> or <code>ftp:</code>, followed by a scheme dependent
 * file name.  Some examples:</p>
 * <pre>
 * file:/c:/somefile
 * ftp://somewhere.org/somefile
 * </pre>
 *
 * <li><p>Absolute local file name.  For example,
 * <code>/home/someuser/a-file</code> or <code>c:\dir\somefile.html</code>.
 * Elements in the name can be separated using any of the following
 * characters: <code>/</code>, <code>\</code>, or the native file separator
 * character. For example, the following file names are the same:</p>
 * <pre>
 * c:\somedir\somefile.xml
 * c:/somedir/somefile.xml
 * </pre>
 *
 * <li><p>Relative path.  For example: <code>../somefile</code> or
 * <code>somedir/file.txt</code>.   The file system manager resolves relative
 * paths against its <i>base file</i>.  Elements in the relative path can be
 * separated using <code>/</code>, <code>\</code>, or file system specific
 * separator characters.  Relative paths may also contain <code>..</code> and
 * <code>.</code> elements.  See {@link FileObject#resolveFile} for more details.</p>
 *
 * </ul>
 *
 * @author Adam Murdoch
 */
public interface FileSystemManager
    extends Component
{
    String ROLE = "org.apache.aut.vfs.FileSystemManager";

    /**
     * Returns the base file used to resolve relative paths.
     */
    FileObject getBaseFile();

    /**
     * Locates a file by name.  Equivalent to calling
     * <code>resolveFile(uri, getBaseName())</code>.
     *
     * @param name
     *          The name of the file.
     *
     * @throws FileSystemException
     *          On error parsing the file name.
     */
    FileObject resolveFile( String name ) throws FileSystemException;

    /**
     * Locates a file by name.  The name is resolved as described
     * <a href="#naming">above</a>.  That is, the name can be either
     * an absolute URI, an absolute file name, or a relative path to
     * be resolved against <code>baseFile</code>.
     *
     * <p>Note that the file does not have to exist when this method is called.
     *
     * @param name
     *          The name of the file.
     *
     * @param baseFile
     *          The base file to use to resolve paths.
     *
     * @throws FileSystemException
     *          On error parsing the file name.
     */
    FileObject resolveFile( FileObject baseFile, String name ) throws FileSystemException;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11944.java