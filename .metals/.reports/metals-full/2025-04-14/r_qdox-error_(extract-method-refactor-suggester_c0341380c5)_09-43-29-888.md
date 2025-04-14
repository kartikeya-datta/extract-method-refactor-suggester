error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17732.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17732.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[129,80]

error in qdox parser
file content:
```java
offset: 2888
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17732.java
text:
```scala
{ // TODO finalize javadoc

/*
 * $Id$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wicket.util.file;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import wicket.util.string.StringList;

/**
 * Mantains a list of folders as a path.
 * @author Jonathan Locke
 */
public final class Path
{
    // The list of folders
    private final List folders = new ArrayList();

    /**
     * Constructor
     */
    public Path()
    {
    }

    /**
     * Constructor
     * @param folder A single folder to add to the path
     */
    public Path(final Folder folder)
    {
        add(folder);
    }

    /**
     * Constructor
     * @param folders An array of folders to add to the path
     */
    public Path(final Folder[] folders)
    {
        for (int i = 0; i < folders.length; i++)
        {
            add(folders[i]);
        }
    }

    /**
     * @param folder Folder to add to path
     * @return The path, for invocation chaining
     */
    public Path add(final Folder folder)
    {
        if (!folder.exists())
        {
            throw new IllegalArgumentException("Folder " + folder + " does not exist");
        }

        folders.add(folder);

        return this;
    }

    /**
     * Looks for a given pathname along this path
     * @param pathname The filename with possible path
     * @return The file located on the path
     */
    public File find(final String pathname)
    {
        for (final Iterator iterator = folders.iterator(); iterator.hasNext();)
        {
            final Folder folder = (Folder) iterator.next();
            final File file = new File(folder, pathname);

            if (file.exists())
            {
                return file;
            }
        }

        return null;
    }

    /**
     * @return Returns the folders.
     */
    public List getFolders()
    {
        return folders;
    }

    /**
     * @return Number of folders on the path.
     */
    public int size()
    {
        return folders.size();
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return "[folders = " + StringList.valueOf(folders) + "]";
    }
}

///////////////////////////////// End of File /////////////////////////////////@@
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17732.java