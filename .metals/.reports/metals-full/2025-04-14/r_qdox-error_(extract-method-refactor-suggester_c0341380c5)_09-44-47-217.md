error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3306.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3306.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3306.java
text:
```scala
t@@his.context.setBoost(0.0f);                      // ignore context-only matches

package org.apache.lucene.search;
import java.io.IOException;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.DefaultSimilarity;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Similarity;
/**
 * The BoostingQuery class can be used to effectively demote results that match a given query. 
 * Unlike the "NOT" clause, this still selects documents that contain undesirable terms, 
 * but reduces their overall score:
 *
 *     Query balancedQuery = new BoostingQuery(positiveQuery, negativeQuery, 0.01f);
 * In this scenario the positiveQuery contains the mandatory, desirable criteria which is used to 
 * select all matching documents, and the negativeQuery contains the undesirable elements which 
 * are simply used to lessen the scores. Documents that match the negativeQuery have their score 
 * multiplied by the supplied "boost" parameter, so this should be less than 1 to achieve a 
 * demoting effect
 * 
 * This code was originally made available here: [WWW] http://marc.theaimsgroup.com/?l=lucene-user&m=108058407130459&w=2
 * and is documented here: http://wiki.apache.org/lucene-java/CommunityContributions
 */
public class BoostingQuery extends Query {
    private float boost;                            // the amount to boost by
    private Query match;                            // query to match
    private Query context;                          // boost when matches too

    public BoostingQuery(Query match, Query context, float boost) {
      this.match = match;
      this.context = (Query)context.clone();        // clone before boost
      this.boost = boost;

      context.setBoost(0.0f);                      // ignore context-only matches
    }

    public Query rewrite(IndexReader reader) throws IOException {
      BooleanQuery result = new BooleanQuery() {

        public Similarity getSimilarity(Searcher searcher) {
          return new DefaultSimilarity() {

            public float coord(int overlap, int max) {
              switch (overlap) {

              case 1:                               // matched only one clause
                return 1.0f;                        // use the score as-is

              case 2:                               // matched both clauses
                return boost;                       // multiply by boost

              default:
                return 0.0f;
                
              }
            }
          };
        }
      };

      result.add(match, BooleanClause.Occur.MUST);
      result.add(context, BooleanClause.Occur.SHOULD);

      return result;
    }

    public String toString(String field) {
      return match.toString(field) + "/" + context.toString(field);
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3306.java