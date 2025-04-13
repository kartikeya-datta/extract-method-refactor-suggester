error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2843.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2843.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2843.java
text:
```scala
i@@f (terms.size() >= maxExpansions) {

/*
 * Licensed to Elastic Search and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Elastic Search licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.common.lucene.search;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.search.MultiPhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.ToStringUtils;

import java.io.IOException;
import java.util.*;

public class MultiPhrasePrefixQuery extends Query {

    private String field;
    private ArrayList<Term[]> termArrays = new ArrayList<Term[]>();
    private ArrayList<Integer> positions = new ArrayList<Integer>();
    private int maxExpansions = Integer.MAX_VALUE;

    private int slop = 0;

    /**
     * Sets the phrase slop for this query.
     *
     * @see org.apache.lucene.search.PhraseQuery#setSlop(int)
     */
    public void setSlop(int s) {
        slop = s;
    }

    public void setMaxExpansions(int maxExpansions) {
        this.maxExpansions = maxExpansions;
    }

    /**
     * Sets the phrase slop for this query.
     *
     * @see org.apache.lucene.search.PhraseQuery#getSlop()
     */
    public int getSlop() {
        return slop;
    }

    /**
     * Add a single term at the next position in the phrase.
     *
     * @see org.apache.lucene.search.PhraseQuery#add(Term)
     */
    public void add(Term term) {
        add(new Term[]{term});
    }

    /**
     * Add multiple terms at the next position in the phrase.  Any of the terms
     * may match.
     *
     * @see org.apache.lucene.search.PhraseQuery#add(Term)
     */
    public void add(Term[] terms) {
        int position = 0;
        if (positions.size() > 0)
            position = positions.get(positions.size() - 1).intValue() + 1;

        add(terms, position);
    }

    /**
     * Allows to specify the relative position of terms within the phrase.
     *
     * @param terms
     * @param position
     * @see org.apache.lucene.search.PhraseQuery#add(Term, int)
     */
    public void add(Term[] terms, int position) {
        if (termArrays.size() == 0)
            field = terms[0].field();

        for (int i = 0; i < terms.length; i++) {
            if (terms[i].field() != field) {
                throw new IllegalArgumentException(
                        "All phrase terms must be in the same field (" + field + "): "
                                + terms[i]);
            }
        }

        termArrays.add(terms);
        positions.add(Integer.valueOf(position));
    }

    /**
     * Returns a List of the terms in the multiphrase.
     * Do not modify the List or its contents.
     */
    public List<Term[]> getTermArrays() {
        return Collections.unmodifiableList(termArrays);
    }

    /**
     * Returns the relative positions of terms in this phrase.
     */
    public int[] getPositions() {
        int[] result = new int[positions.size()];
        for (int i = 0; i < positions.size(); i++)
            result[i] = positions.get(i).intValue();
        return result;
    }

    @Override public Query rewrite(IndexReader reader) throws IOException {
        if (termArrays.isEmpty()) {
            return MatchNoDocsQuery.INSTANCE;
        }
        MultiPhraseQuery query = new MultiPhraseQuery();
        query.setSlop(slop);
        int sizeMinus1 = termArrays.size() - 1;
        for (int i = 0; i < sizeMinus1; i++) {
            query.add(termArrays.get(i), positions.get(i));
        }
        Term[] suffixTerms = termArrays.get(sizeMinus1);
        int position = positions.get(sizeMinus1);
        List<Term> terms = new ArrayList<Term>();
        for (Term term : suffixTerms) {
            getPrefixTerms(terms, term, reader);
            if (terms.size() > maxExpansions) {
                break;
            }
        }
        if (terms.isEmpty()) {
            return MatchNoDocsQuery.INSTANCE;
        }
        query.add(terms.toArray(new Term[terms.size()]), position);
        return query.rewrite(reader);
    }

    private void getPrefixTerms(List<Term> terms, final Term prefix, final IndexReader reader) throws IOException {
        TermEnum enumerator = reader.terms(prefix);
        try {
            do {
                Term term = enumerator.term();
                if (term != null
                        && term.text().startsWith(prefix.text())
                        && term.field().equals(field)) {
                    terms.add(term);
                } else {
                    break;
                }
                if (terms.size() > maxExpansions) {
                    break;
                }
            } while (enumerator.next());
        } finally {
            enumerator.close();
        }
    }

    @Override
    public final String toString(String f) {
        StringBuilder buffer = new StringBuilder();
        if (field == null || !field.equals(f)) {
            buffer.append(field);
            buffer.append(":");
        }

        buffer.append("\"");
        Iterator<Term[]> i = termArrays.iterator();
        while (i.hasNext()) {
            Term[] terms = i.next();
            if (terms.length > 1) {
                buffer.append("(");
                for (int j = 0; j < terms.length; j++) {
                    buffer.append(terms[j].text());
                    if (j < terms.length - 1)
                        buffer.append(" ");
                }
                buffer.append(")");
            } else {
                buffer.append(terms[0].text());
            }
            if (i.hasNext())
                buffer.append(" ");
        }
        buffer.append("\"");

        if (slop != 0) {
            buffer.append("~");
            buffer.append(slop);
        }

        buffer.append(ToStringUtils.boost(getBoost()));

        return buffer.toString();
    }

    /**
     * Returns true if <code>o</code> is equal to this.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MultiPhrasePrefixQuery)) return false;
        MultiPhrasePrefixQuery other = (MultiPhrasePrefixQuery) o;
        return this.getBoost() == other.getBoost()
                && this.slop == other.slop
                && termArraysEquals(this.termArrays, other.termArrays)
                && this.positions.equals(other.positions);
    }

    /**
     * Returns a hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Float.floatToIntBits(getBoost())
                ^ slop
                ^ termArraysHashCode()
                ^ positions.hashCode()
                ^ 0x4AC65113;
    }

    // Breakout calculation of the termArrays hashcode
    private int termArraysHashCode() {
        int hashCode = 1;
        for (final Term[] termArray : termArrays) {
            hashCode = 31 * hashCode
                    + (termArray == null ? 0 : Arrays.hashCode(termArray));
        }
        return hashCode;
    }

    // Breakout calculation of the termArrays equals
    private boolean termArraysEquals(List<Term[]> termArrays1, List<Term[]> termArrays2) {
        if (termArrays1.size() != termArrays2.size()) {
            return false;
        }
        ListIterator<Term[]> iterator1 = termArrays1.listIterator();
        ListIterator<Term[]> iterator2 = termArrays2.listIterator();
        while (iterator1.hasNext()) {
            Term[] termArray1 = iterator1.next();
            Term[] termArray2 = iterator2.next();
            if (!(termArray1 == null ? termArray2 == null : Arrays.equals(termArray1,
                    termArray2))) {
                return false;
            }
        }
        return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2843.java