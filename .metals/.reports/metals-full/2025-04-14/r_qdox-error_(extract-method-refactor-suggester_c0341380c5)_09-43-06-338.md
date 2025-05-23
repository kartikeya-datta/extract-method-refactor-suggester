error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7655.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7655.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7655.java
text:
```scala
public i@@nt freq() throws IOException {

package org.elasticsearch.index.search.nested;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.FixedBitSet;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

/**
 * A special query that accepts a top level parent matching query, and returns the nested docs of the matching parent
 * doc as well. This is handy when deleting by query.
 */
public class IncludeNestedDocsQuery extends Query {

    private final Filter parentFilter;
    private final Query parentQuery;

    // If we are rewritten, this is the original childQuery we
    // were passed; we use this for .equals() and
    // .hashCode().  This makes rewritten query equal the
    // original, so that user does not have to .rewrite() their
    // query before searching:
    private final Query origParentQuery;


    public IncludeNestedDocsQuery(Query parentQuery, Filter parentFilter) {
        this.origParentQuery = parentQuery;
        this.parentQuery = parentQuery;
        this.parentFilter = parentFilter;
    }

    // For rewritting
    IncludeNestedDocsQuery(Query rewrite, Query originalQuery, IncludeNestedDocsQuery previousInstance) {
        this.origParentQuery = originalQuery;
        this.parentQuery = rewrite;
        this.parentFilter = previousInstance.parentFilter;
        setBoost(previousInstance.getBoost());
    }

    // For cloning
    IncludeNestedDocsQuery(Query originalQuery, IncludeNestedDocsQuery previousInstance) {
        this.origParentQuery = originalQuery;
        this.parentQuery = originalQuery;
        this.parentFilter = previousInstance.parentFilter;
    }

    @Override
    public Weight createWeight(IndexSearcher searcher) throws IOException {
        return new IncludeNestedDocsWeight(parentQuery, parentQuery.createWeight(searcher), parentFilter);
    }

    static class IncludeNestedDocsWeight extends Weight {

        private final Query parentQuery;
        private final Weight parentWeight;
        private final Filter parentsFilter;

        IncludeNestedDocsWeight(Query parentQuery, Weight parentWeight, Filter parentsFilter) {
            this.parentQuery = parentQuery;
            this.parentWeight = parentWeight;
            this.parentsFilter = parentsFilter;
        }

        @Override
        public Query getQuery() {
            return parentQuery;
        }

        @Override
        public void normalize(float norm, float topLevelBoost) {
            parentWeight.normalize(norm, topLevelBoost);
        }

        @Override
        public float getValueForNormalization() throws IOException {
            return parentWeight.getValueForNormalization(); // this query is never boosted so just delegate...
        }

        @Override
        public Scorer scorer(AtomicReaderContext context, boolean scoreDocsInOrder, boolean topScorer, Bits acceptDocs) throws IOException {
            final Scorer parentScorer = parentWeight.scorer(context, true, false, acceptDocs);

            // no matches
            if (parentScorer == null) {
                return null;
            }

            DocIdSet parents = parentsFilter.getDocIdSet(context, acceptDocs);
            if (parents == null) {
                // No matches
                return null;
            }
            if (!(parents instanceof FixedBitSet)) {
                throw new IllegalStateException("parentFilter must return FixedBitSet; got " + parents);
            }

            int firstParentDoc = parentScorer.nextDoc();
            if (firstParentDoc == DocIdSetIterator.NO_MORE_DOCS) {
                // No matches
                return null;
            }
            return new IncludeNestedDocsScorer(this, parentScorer, (FixedBitSet) parents, firstParentDoc);
        }

        @Override
        public Explanation explain(AtomicReaderContext context, int doc) throws IOException {
            return null; //Query is used internally and not by users, so explain can be empty
        }

        @Override
        public boolean scoresDocsOutOfOrder() {
            return false;
        }
    }

    static class IncludeNestedDocsScorer extends Scorer {

        final Scorer parentScorer;
        final FixedBitSet parentBits;

        int currentChildPointer = -1;
        int currentParentPointer = -1;
        int currentDoc = -1;

        IncludeNestedDocsScorer(Weight weight, Scorer parentScorer, FixedBitSet parentBits, int currentParentPointer) {
            super(weight);
            this.parentScorer = parentScorer;
            this.parentBits = parentBits;
            this.currentParentPointer = currentParentPointer;
            if (currentParentPointer == 0) {
                currentChildPointer = 0;
            } else {
                this.currentChildPointer = parentBits.prevSetBit(currentParentPointer - 1);
                if (currentChildPointer == -1) {
                    // no previous set parent, we delete from doc 0
                    currentChildPointer = 0;
                } else {
                    currentChildPointer++; // we only care about children
                }
            }

            currentDoc = currentChildPointer;
        }

        @Override
        public Collection<ChildScorer> getChildren() {
            return parentScorer.getChildren();
        }

        public int nextDoc() throws IOException {
            if (currentParentPointer == NO_MORE_DOCS) {
                return (currentDoc = NO_MORE_DOCS);
            }

            if (currentChildPointer == currentParentPointer) {
                // we need to return the current parent as well, but prepare to return
                // the next set of children
                currentDoc = currentParentPointer;
                currentParentPointer = parentScorer.nextDoc();
                if (currentParentPointer != NO_MORE_DOCS) {
                    currentChildPointer = parentBits.prevSetBit(currentParentPointer - 1);
                    if (currentChildPointer == -1) {
                        // no previous set parent, just set the child to the current parent
                        currentChildPointer = currentParentPointer;
                    } else {
                        currentChildPointer++; // we only care about children
                    }
                }
            } else {
                currentDoc = currentChildPointer++;
            }

            assert currentDoc != -1;
            return currentDoc;
        }

        public int advance(int target) throws IOException {
            if (target == NO_MORE_DOCS) {
                return (currentDoc = NO_MORE_DOCS);
            }

            if (target == 0) {
                return nextDoc();
            }

            if (target < currentParentPointer) {
                currentDoc = currentParentPointer = parentScorer.advance(target);
                if (currentParentPointer == NO_MORE_DOCS) {
                    return (currentDoc = NO_MORE_DOCS);
                }
                if (currentParentPointer == 0) {
                    currentChildPointer = 0;
                } else {
                    currentChildPointer = parentBits.prevSetBit(currentParentPointer - 1);
                    if (currentChildPointer == -1) {
                        // no previous set parent, just set the child to 0 to delete all up to the parent
                        currentChildPointer = 0;
                    } else {
                        currentChildPointer++; // we only care about children
                    }
                }
            } else {
                currentDoc = currentChildPointer++;
            }

            return currentDoc;
        }

        public float score() throws IOException {
            return parentScorer.score();
        }

        public float freq() throws IOException {
            return parentScorer.freq();
        }

        public int docID() {
            return currentDoc;
        }
    }

    @Override
    public void extractTerms(Set<Term> terms) {
        parentQuery.extractTerms(terms);
    }

    @Override
    public Query rewrite(IndexReader reader) throws IOException {
        final Query parentRewrite = parentQuery.rewrite(reader);
        if (parentRewrite != parentQuery) {
            return new IncludeNestedDocsQuery(parentRewrite, parentQuery, this);
        } else {
            return this;
        }
    }

    @Override
    public String toString(String field) {
        return "IncludeNestedDocsQuery (" + parentQuery.toString() + ")";
    }

    @Override
    public boolean equals(Object _other) {
        if (_other instanceof IncludeNestedDocsQuery) {
            final IncludeNestedDocsQuery other = (IncludeNestedDocsQuery) _other;
            return origParentQuery.equals(other.origParentQuery) && parentFilter.equals(other.parentFilter);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 1;
        hash = prime * hash + origParentQuery.hashCode();
        hash = prime * hash + parentFilter.hashCode();
        return hash;
    }

    @Override
    public Query clone() {
        Query clonedQuery = origParentQuery.clone();
        return new IncludeNestedDocsQuery(clonedQuery, this);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7655.java