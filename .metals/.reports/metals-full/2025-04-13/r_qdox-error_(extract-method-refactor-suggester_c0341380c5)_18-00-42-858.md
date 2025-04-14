error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/707.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/707.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[3,1]

error in qdox parser
file content:
```java
offset: 58
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/707.java
text:
```scala
protected class BoostingSpanScorer extends SpanScorer {

p@@ackage org.apache.lucene.search.payloads;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermPositions;
import org.apache.lucene.search.*;
import org.apache.lucene.search.spans.SpanScorer;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.search.spans.SpanWeight;
import org.apache.lucene.search.spans.TermSpans;

import java.io.IOException;

/**
 * Copyright 2004 The Apache Software Foundation
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * The BoostingTermQuery is very similar to the {@link org.apache.lucene.search.spans.SpanTermQuery} except
 * that it factors in the value of the payload located at each of the positions where the
 * {@link org.apache.lucene.index.Term} occurs.
 * <p>
 * In order to take advantage of this, you must override {@link org.apache.lucene.search.Similarity#scorePayload(String, byte[],int,int)}
 * which returns 1 by default.
 * <p>
 * Payload scores are averaged across term occurrences in the document.  
 * 
 * @see org.apache.lucene.search.Similarity#scorePayload(String, byte[], int, int)
 */
public class BoostingTermQuery extends SpanTermQuery{


  public BoostingTermQuery(Term term) {
    super(term);
  }


  protected Weight createWeight(Searcher searcher) throws IOException {
    return new BoostingTermWeight(this, searcher);
  }

  protected class BoostingTermWeight extends SpanWeight implements Weight {


    public BoostingTermWeight(BoostingTermQuery query, Searcher searcher) throws IOException {
      super(query, searcher);
    }




    public Scorer scorer(IndexReader reader) throws IOException {
      return new BoostingSpanScorer((TermSpans)query.getSpans(reader), this, similarity,
              reader.norms(query.getField()));
    }

    class BoostingSpanScorer extends SpanScorer {

      //TODO: is this the best way to allocate this?
      byte[] payload = new byte[256];
      private TermPositions positions;
      protected float payloadScore;
      private int payloadsSeen;

      public BoostingSpanScorer(TermSpans spans, Weight weight,
                                Similarity similarity, byte[] norms) throws IOException {
        super(spans, weight, similarity, norms);
        positions = spans.getPositions();

      }

      protected boolean setFreqCurrentDoc() throws IOException {
        if (!more) {
          return false;
        }
        doc = spans.doc();
        freq = 0.0f;
        payloadScore = 0;
        payloadsSeen = 0;
        Similarity similarity1 = getSimilarity();
        while (more && doc == spans.doc()) {
          int matchLength = spans.end() - spans.start();

          freq += similarity1.sloppyFreq(matchLength);
          processPayload(similarity1);

          more = spans.next();//this moves positions to the next match in this document
        }
        return more || (freq != 0);
      }


      protected void processPayload(Similarity similarity) throws IOException {
        if (positions.isPayloadAvailable()) {
          payload = positions.getPayload(payload, 0);
          payloadScore += similarity.scorePayload(term.field(), payload, 0, positions.getPayloadLength());
          payloadsSeen++;

        } else {
          //zero out the payload?
        }

      }

      public float score() throws IOException {

        return super.score() * (payloadsSeen > 0 ? (payloadScore / payloadsSeen) : 1);
      }


      public Explanation explain(final int doc) throws IOException {
        Explanation result = new Explanation();
        Explanation nonPayloadExpl = super.explain(doc);
        result.addDetail(nonPayloadExpl);
        //QUESTION: Is there a wau to avoid this skipTo call?  We need to know whether to load the payload or not
        
        Explanation payloadBoost = new Explanation();
        result.addDetail(payloadBoost);
/*
        if (skipTo(doc) == true) {
          processPayload();
        }
*/

        float avgPayloadScore =  (payloadsSeen > 0 ? (payloadScore / payloadsSeen) : 1); 
        payloadBoost.setValue(avgPayloadScore);
        //GSI: I suppose we could toString the payload, but I don't think that would be a good idea 
        payloadBoost.setDescription("scorePayload(...)");
        result.setValue(nonPayloadExpl.getValue() * avgPayloadScore);
        result.setDescription("btq, product of:");
        return result;
      }
    }

  }


  public boolean equals(Object o) {
    if (!(o instanceof BoostingTermQuery))
      return false;
    BoostingTermQuery other = (BoostingTermQuery) o;
    return (this.getBoost() == other.getBoost())
            && this.term.equals(other.term);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/707.java