error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3451.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3451.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3451.java
text:
```scala
h@@itsDocuments[i] = hits.doc(i);

import org.apache.log4j.Category;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Hits;
import search.SearchResultFactory;

import java.io.IOException;

/**
 * <p>
 * Encapsulates the results of a search. After a SearchResults has
 * been constructed from a Hits object, the IndexSearcher can be
 * safely closed.
 * </p>
 * <p>
 * SearchResults also provides a way of retrieving Java objects from
 * Documents (via {@link search.SearchResultsFactory}).
 * </p>
 * <p>
 * <b>Note that this implementation uses code from
 * /projects/appex/search.</b>
 * </p>
 */
public class SearchResults
{
    private static Category cat = Category.getInstance(SearchResults.class);
    private Document[] hitsDocuments;
    private Object[] objectResults;
    private int totalNumberOfResults;

    public SearchResults(Hits hits) throws IOException
    {
        this(hits, 0, hits.length());
    }

    public SearchResults(Hits hits, int from, int to) throws IOException
    {
        hitsDocuments = new Document[hits.length()];
        totalNumberOfResults = hits.length();
        if (to > totalNumberOfResults)
        {
            to = totalNumberOfResults;
        }
        for (int i = from; i < to; i++)
        {
            hitsDocuments[i] = hits.doc(i));
        }
    }

    public int getTotalNumberOfResults()
    {
        return totalNumberOfResults;
    }

    /**
     * Obtain the results of the search as objects.
     */
    public Object[] getResultsAsObjects()
    {
        if (objectResults == null)
        {
            objectResults = new Object[hitsDocuments.length];
            for (int i = 0; i < hitsDocuments.length; i++)
            {
                try
                {
                    objectResults[i] = SearchResultFactory.
                            getDocAsObject(hitsDocuments[i]);
                }
                catch (Exception e)
                {
                    cat.error("Error instantiating an object from a document.", e);
                }
            }
        }
        return objectResults;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3451.java