error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4757.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4757.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4757.java
text:
```scala
i@@f (acceptableExtensions.contains(getExtension(pFile)))

/*
 * Copyright(c) 2000 Soltima, Inc.
 * Soltima Wireless Publishing Platform (WPP)
 *
 * @author S.Coleman
 */
package org.apache.jmeter.config;

import java.io.File;
import java.util.ArrayList;

import javax.swing.filechooser.FileFilter;

/**
 * This FileFilter allows for a list of file extensions
 * to be set that it will filter on. This design was taken
 * from the ExampleFileFilter used in
 * <a href="http://java.sun.com/docs/books/tutorial/uiswing/components/filechooser.html">How
 * to Use File Choosers</a>, a section in <em>The Java Tutorial</em>.
 *
 * Here is an example of how to use the file filter for JMX files,
 * that have the .jmx extension.
 * <pre>
 *      ExtensionsFileFilter filter = new ExtensionsFileFilter();
 *      filter.addExtension("jmx");
 *      filter.setDescription("JMeter (*.jmx)");
 *      FileChooser fileChooser = new FileChooser();
 *      fileChooser.setFileFilter(fileFilter);
 * </pre>
 */
public class ExtensionsFileFilter extends FileFilter
{
    /**
     * A description of the extensions being filtered on.
     */
    private String mDescription = "";

    /**
     * The list of extensions being filtered on.
     */
    private ArrayList acceptableExtensions = new ArrayList(3);

    /**
     * Default Constructor.
     */
    public ExtensionsFileFilter()
    {
    }

    /**
     * Add an extension to the list of extensions to filter on.
     *
     * @param pExtension The extension to add.
     */
    public void addExtension(String pExtension)
    {
        acceptableExtensions.add(pExtension);
    }

    /**
     * Removes an extension from the list of extensions to filter on.
     *
     * @param pExtension The extension to remove.
     */
    public void removeExtension(String pExtension)
    {
        acceptableExtensions.remove(pExtension);
    }

    /**
     * Set the description of the extensions being filtered on.
     *
     * @param pDescription the detailed description of the extensions being
     *                     filtered on.
     */
    public void setDescription(String pDescription)
    {
        mDescription = pDescription;
    }

    /**
     * Returns a descriptive string detailing the file extensions being
     * filtered.
     *
     * @return The description of the extensions being filtered.
     * @see javax.swing.filechooser.FileFilter#getDescription()
     */
    public String getDescription()
    {
        return mDescription;
    }

    /**
     * Determines whether to accept the passed File or not.
     *
     * @param pFile The file to check whether we should accept or not.
     * @return true if the file is accepted, false if not.
     */
    public boolean accept(File pFile)
    {
        // Always accept directories or the user will not be able to navigate
        // around the file system.
        if (pFile.isDirectory())
        {
            return true;
        }
        if (acceptableExtensions.contains(getExtension(pFile)) == true)
        {
            return true;
        }

        return false;
    }

    /**
     * Finds the file extension for the passed File. If there
     * is no extension then an empty string is returned.
     *
     * @param pFile The file to find the extension of.
     */
    private String getExtension(File pFile)
    {
        String name = pFile.getName();
        int index = name.lastIndexOf('.');
        if (index == -1 || index == name.length())
        {
            return "";
        }
        else
        {
            return name.substring(index + 1);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4757.java