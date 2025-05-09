error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1221.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1221.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1221.java
text:
```scala
i@@f (matchers[i].run(term.bytes, term.offset, term.length)) {

package org.apache.lucene.search;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.lucene.index.DocsAndPositionsEnum;
import org.apache.lucene.index.DocsEnum;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.util.Attribute;
import org.apache.lucene.util.AttributeImpl;
import org.apache.lucene.util.AttributeSource;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.IntsRef;
import org.apache.lucene.util.UnicodeUtil;
import org.apache.lucene.util.automaton.Automaton;
import org.apache.lucene.util.automaton.BasicAutomata;
import org.apache.lucene.util.automaton.BasicOperations;
import org.apache.lucene.util.automaton.ByteRunAutomaton;
import org.apache.lucene.util.automaton.LevenshteinAutomata;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Subclass of TermsEnum for enumerating all terms that are similar
 * to the specified filter term.
 *
 * <p>Term enumerations are always ordered by
 * {@link #getComparator}.  Each term in the enumeration is
 * greater than all that precede it.</p>
 */
public final class FuzzyTermsEnum extends TermsEnum {
  private TermsEnum actualEnum;
  private BoostAttribute actualBoostAtt;
  
  private final BoostAttribute boostAtt =
    attributes().addAttribute(BoostAttribute.class);
  
  private final MaxNonCompetitiveBoostAttribute maxBoostAtt;
  private final LevenshteinAutomataAttribute dfaAtt;
  
  private float bottom;
  private BytesRef bottomTerm;

  // TODO: chicken-and-egg
  private final Comparator<BytesRef> termComparator = BytesRef.getUTF8SortedAsUnicodeComparator();
  
  private final float minSimilarity;
  private final float scale_factor;
  
  private final int termLength;
  
  private int maxEdits;
  private final boolean raw;

  private final TermsEnum tenum;
  private final Term term;
  private final int termText[];
  private final int realPrefixLength;
  
  /**
   * Constructor for enumeration of all terms from specified <code>reader</code> which share a prefix of
   * length <code>prefixLength</code> with <code>term</code> and which have a fuzzy similarity &gt;
   * <code>minSimilarity</code>.
   * <p>
   * After calling the constructor the enumeration is already pointing to the first 
   * valid term if such a term exists. 
   * 
   * @param tenum Delivers terms.
   * @param atts {@link AttributeSource} created by the rewrite method of {@link MultiTermQuery}
   * thats contains information about competitive boosts during rewrite. It is also used
   * to cache DFAs between segment transitions.
   * @param term Pattern term.
   * @param minSimilarity Minimum required similarity for terms from the reader.
   * @param prefixLength Length of required common prefix. Default value is 0.
   * @throws IOException
   */
  public FuzzyTermsEnum(TermsEnum tenum, AttributeSource atts, Term term, 
      final float minSimilarity, final int prefixLength) throws IOException {
    if (minSimilarity >= 1.0f && minSimilarity != (int)minSimilarity)
      throw new IllegalArgumentException("fractional edit distances are not allowed");
    if (minSimilarity < 0.0f)
      throw new IllegalArgumentException("minimumSimilarity cannot be less than 0");
    if(prefixLength < 0)
      throw new IllegalArgumentException("prefixLength cannot be less than 0");
    this.tenum = tenum;
    this.term = term;

    // convert the string into a utf32 int[] representation for fast comparisons
    final String utf16 = term.text();
    this.termText = new int[utf16.codePointCount(0, utf16.length())];
    for (int cp, i = 0, j = 0; i < utf16.length(); i += Character.charCount(cp))
           termText[j++] = cp = utf16.codePointAt(i);
    this.termLength = termText.length;
    this.dfaAtt = atts.addAttribute(LevenshteinAutomataAttribute.class);

    //The prefix could be longer than the word.
    //It's kind of silly though.  It means we must match the entire word.
    this.realPrefixLength = prefixLength > termLength ? termLength : prefixLength;
    // if minSimilarity >= 1, we treat it as number of edits
    if (minSimilarity >= 1f) {
      this.minSimilarity = 1 - (minSimilarity+1) / this.termLength;
      maxEdits = (int) minSimilarity;
      raw = true;
    } else {
      this.minSimilarity = minSimilarity;
      // calculate the maximum k edits for this similarity
      maxEdits = initialMaxDistance(this.minSimilarity, termLength);
      raw = false;
    }
    this.scale_factor = 1.0f / (1.0f - this.minSimilarity);

    this.maxBoostAtt = atts.addAttribute(MaxNonCompetitiveBoostAttribute.class);
    bottom = maxBoostAtt.getMaxNonCompetitiveBoost();
    bottomTerm = maxBoostAtt.getCompetitiveTerm();
    bottomChanged(null, true);
  }
  
  /**
   * return an automata-based enum for matching up to editDistance from
   * lastTerm, if possible
   */
  private TermsEnum getAutomatonEnum(int editDistance, BytesRef lastTerm)
      throws IOException {
    final List<ByteRunAutomaton> runAutomata = initAutomata(editDistance);
    if (editDistance < runAutomata.size()) {
      return new AutomatonFuzzyTermsEnum(runAutomata.subList(0, editDistance + 1)
          .toArray(new ByteRunAutomaton[editDistance + 1]), lastTerm);
    } else {
      return null;
    }
  }

  /** initialize levenshtein DFAs up to maxDistance, if possible */
  private List<ByteRunAutomaton> initAutomata(int maxDistance) {
    final List<ByteRunAutomaton> runAutomata = dfaAtt.automata();
    if (runAutomata.size() <= maxDistance && 
        maxDistance <= LevenshteinAutomata.MAXIMUM_SUPPORTED_DISTANCE) {
      LevenshteinAutomata builder = 
        new LevenshteinAutomata(UnicodeUtil.newString(termText, realPrefixLength, termText.length - realPrefixLength));

      for (int i = runAutomata.size(); i <= maxDistance; i++) {
        Automaton a = builder.toAutomaton(i);
        // constant prefix
        if (realPrefixLength > 0) {
          Automaton prefix = BasicAutomata.makeString(
            UnicodeUtil.newString(termText, 0, realPrefixLength));
          a = BasicOperations.concatenate(prefix, a);
        }
        runAutomata.add(new ByteRunAutomaton(a));
      }
    }
    return runAutomata;
  }

  /** swap in a new actual enum to proxy to */
  private void setEnum(TermsEnum actualEnum) {
    this.actualEnum = actualEnum;
    this.actualBoostAtt = actualEnum.attributes().addAttribute(BoostAttribute.class);
  }
  
  /**
   * fired when the max non-competitive boost has changed. this is the hook to
   * swap in a smarter actualEnum
   */
  private void bottomChanged(BytesRef lastTerm, boolean init)
      throws IOException {
    int oldMaxEdits = maxEdits;
    
    // true if the last term encountered is lexicographically equal or after the bottom term in the PQ
    boolean termAfter = bottomTerm == null || (lastTerm != null && termComparator.compare(lastTerm, bottomTerm) >= 0);

    // as long as the max non-competitive boost is >= the max boost
    // for some edit distance, keep dropping the max edit distance.
    while (maxEdits > 0 && (termAfter ? bottom >= calculateMaxBoost(maxEdits) : bottom > calculateMaxBoost(maxEdits)))
      maxEdits--;
    
    if (oldMaxEdits != maxEdits || init) { // the maximum n has changed
      TermsEnum newEnum = getAutomatonEnum(maxEdits, lastTerm);
      if (newEnum != null) {
        setEnum(newEnum);
      } else if (init) {
        setEnum(new LinearFuzzyTermsEnum());      
      }
    }
  }

  // for some raw min similarity and input term length, the maximum # of edits
  private int initialMaxDistance(float minimumSimilarity, int termLen) {
    return (int) ((1D-minimumSimilarity) * termLen);
  }
  
  // for some number of edits, the maximum possible scaled boost
  private float calculateMaxBoost(int nEdits) {
    final float similarity = 1.0f - ((float) nEdits / (float) (termLength));
    return (similarity - minSimilarity) * scale_factor;
  }

  private BytesRef queuedBottom = null;
  
  @Override
  public BytesRef next() throws IOException {
    if (queuedBottom != null) {
      bottomChanged(queuedBottom, false);
      queuedBottom = null;
    }
    
    BytesRef term = actualEnum.next();
    boostAtt.setBoost(actualBoostAtt.getBoost());
    
    final float bottom = maxBoostAtt.getMaxNonCompetitiveBoost();
    final BytesRef bottomTerm = maxBoostAtt.getCompetitiveTerm();
    if (term != null && (bottom != this.bottom || bottomTerm != this.bottomTerm)) {
      this.bottom = bottom;
      this.bottomTerm = bottomTerm;
      // clone the term before potentially doing something with it
      // this is a rare but wonderful occurrence anyway
      queuedBottom = new BytesRef(term);
    }
    
    return term;
  }
  
  // proxy all other enum calls to the actual enum
  @Override
  public int docFreq() {
    return actualEnum.docFreq();
  }
  
  @Override
  public void cacheCurrentTerm() throws IOException {
    actualEnum.cacheCurrentTerm();
  }

  @Override
  public DocsEnum docs(Bits skipDocs, DocsEnum reuse) throws IOException {
    return actualEnum.docs(skipDocs, reuse);
  }
  
  @Override
  public DocsAndPositionsEnum docsAndPositions(Bits skipDocs,
      DocsAndPositionsEnum reuse) throws IOException {
    return actualEnum.docsAndPositions(skipDocs, reuse);
  }
  
  @Override
  public Comparator<BytesRef> getComparator() throws IOException {
    return actualEnum.getComparator();
  }
  
  @Override
  public long ord() throws IOException {
    return actualEnum.ord();
  }
  
  @Override
  public SeekStatus seek(BytesRef text, boolean useCache) throws IOException {
    return actualEnum.seek(text, useCache);
  }
  
  @Override
  public SeekStatus seek(long ord) throws IOException {
    return actualEnum.seek(ord);
  }
  
  @Override
  public BytesRef term() throws IOException {
    return actualEnum.term();
  }
  
  /**
   * Implement fuzzy enumeration with automaton.
   * <p>
   * This is the fastest method as opposed to LinearFuzzyTermsEnum:
   * as enumeration is logarithmic to the number of terms (instead of linear)
   * and comparison is linear to length of the term (rather than quadratic)
   */
  private class AutomatonFuzzyTermsEnum extends AutomatonTermsEnum {
    private final ByteRunAutomaton matchers[];
    
    private final BytesRef termRef;
    
    private final BytesRef lastTerm;
    private final BoostAttribute boostAtt =
      attributes().addAttribute(BoostAttribute.class);
    
    public AutomatonFuzzyTermsEnum(ByteRunAutomaton matchers[], 
        BytesRef lastTerm) throws IOException {
      super(matchers[matchers.length - 1], tenum, true, null);
      this.matchers = matchers;
      this.lastTerm = lastTerm;
      termRef = new BytesRef(term.text());
    }
    
    /** finds the smallest Lev(n) DFA that accepts the term. */
    @Override
    protected AcceptStatus accept(BytesRef term) {
      if (term.equals(termRef)) { // ed = 0
        boostAtt.setBoost(1.0F);
        return AcceptStatus.YES_AND_SEEK;
      }
      
      int codePointCount = -1;
      
      // TODO: benchmark doing this backwards
      for (int i = 1; i < matchers.length; i++)
        if (matchers[i].run(term.bytes, 0, term.length)) {
          // this sucks, we convert just to score based on length.
          if (codePointCount == -1) {
            codePointCount = UnicodeUtil.codePointCount(term);
          }
          final float similarity = 1.0f - ((float) i / (float) 
              (Math.min(codePointCount, termLength)));
          if (similarity > minSimilarity) {
            boostAtt.setBoost((similarity - minSimilarity) * scale_factor);
            return AcceptStatus.YES_AND_SEEK;
          } else {
            return AcceptStatus.NO_AND_SEEK;
          }
        }
      
      return AcceptStatus.NO_AND_SEEK;
    }
    
    /** defers to superclass, except can start at an arbitrary location */
    @Override
    protected BytesRef nextSeekTerm(BytesRef term) throws IOException {
      if (term == null)
        term = lastTerm;
      return super.nextSeekTerm(term);
    }
  }
  
  /**
   * Implement fuzzy enumeration with linear brute force.
   */
  private class LinearFuzzyTermsEnum extends FilteredTermsEnum {
    /* Allows us save time required to create a new array
     * every time similarity is called.
     */
    private int[] d;
    private int[] p;
    
    // this is the text, minus the prefix
    private final int[] text;
    
    private final BoostAttribute boostAtt =
      attributes().addAttribute(BoostAttribute.class);
    
    /**
     * Constructor for enumeration of all terms from specified <code>reader</code> which share a prefix of
     * length <code>prefixLength</code> with <code>term</code> and which have a fuzzy similarity &gt;
     * <code>minSimilarity</code>.
     * <p>
     * After calling the constructor the enumeration is already pointing to the first 
     * valid term if such a term exists. 
     * 
     * @param reader Delivers terms.
     * @param term Pattern term.
     * @param minSimilarity Minimum required similarity for terms from the reader. Default value is 0.5f.
     * @param prefixLength Length of required common prefix. Default value is 0.
     * @throws IOException
     */
    public LinearFuzzyTermsEnum() throws IOException {
      super(tenum);

      this.text = new int[termLength - realPrefixLength];
      System.arraycopy(termText, realPrefixLength, text, 0, text.length);
      final String prefix = UnicodeUtil.newString(termText, 0, realPrefixLength);
      prefixBytesRef = new BytesRef(prefix);
      this.d = new int[this.text.length + 1];
      this.p = new int[this.text.length + 1];
      
      setInitialSeekTerm(prefixBytesRef);
    }
    
    private final BytesRef prefixBytesRef;
    // used for unicode conversion from BytesRef byte[] to int[]
    private final IntsRef utf32 = new IntsRef(20);
    
    /**
     * The termCompare method in FuzzyTermEnum uses Levenshtein distance to 
     * calculate the distance between the given term and the comparing term. 
     */
    @Override
    protected final AcceptStatus accept(BytesRef term) {
      if (term.startsWith(prefixBytesRef)) {
        UnicodeUtil.UTF8toUTF32(term, utf32);
        final float similarity = similarity(utf32.ints, realPrefixLength, utf32.length - realPrefixLength);
        if (similarity > minSimilarity) {
          boostAtt.setBoost((similarity - minSimilarity) * scale_factor);
          return AcceptStatus.YES;
        } else return AcceptStatus.NO;
      } else {
        return AcceptStatus.END;
      }
    }
    
    /******************************
     * Compute Levenshtein distance
     ******************************/
    
    /**
     * <p>Similarity returns a number that is 1.0f or less (including negative numbers)
     * based on how similar the Term is compared to a target term.  It returns
     * exactly 0.0f when
     * <pre>
     *    editDistance &gt; maximumEditDistance</pre>
     * Otherwise it returns:
     * <pre>
     *    1 - (editDistance / length)</pre>
     * where length is the length of the shortest term (text or target) including a
     * prefix that are identical and editDistance is the Levenshtein distance for
     * the two words.</p>
     *
     * <p>Embedded within this algorithm is a fail-fast Levenshtein distance
     * algorithm.  The fail-fast algorithm differs from the standard Levenshtein
     * distance algorithm in that it is aborted if it is discovered that the
     * minimum distance between the words is greater than some threshold.
     *
     * <p>To calculate the maximum distance threshold we use the following formula:
     * <pre>
     *     (1 - minimumSimilarity) * length</pre>
     * where length is the shortest term including any prefix that is not part of the
     * similarity comparison.  This formula was derived by solving for what maximum value
     * of distance returns false for the following statements:
     * <pre>
     *   similarity = 1 - ((float)distance / (float) (prefixLength + Math.min(textlen, targetlen)));
     *   return (similarity > minimumSimilarity);</pre>
     * where distance is the Levenshtein distance for the two words.
     * </p>
     * <p>Levenshtein distance (also known as edit distance) is a measure of similarity
     * between two strings where the distance is measured as the number of character
     * deletions, insertions or substitutions required to transform one string to
     * the other string.
     * @param target the target word or phrase
     * @return the similarity,  0.0 or less indicates that it matches less than the required
     * threshold and 1.0 indicates that the text and target are identical
     */
    private final float similarity(final int[] target, int offset, int length) {
      final int m = length;
      final int n = text.length;
      if (n == 0)  {
        //we don't have anything to compare.  That means if we just add
        //the letters for m we get the new word
        return realPrefixLength == 0 ? 0.0f : 1.0f - ((float) m / realPrefixLength);
      }
      if (m == 0) {
        return realPrefixLength == 0 ? 0.0f : 1.0f - ((float) n / realPrefixLength);
      }
      
      final int maxDistance = calculateMaxDistance(m);
      
      if (maxDistance < Math.abs(m-n)) {
        //just adding the characters of m to n or vice-versa results in
        //too many edits
        //for example "pre" length is 3 and "prefixes" length is 8.  We can see that
        //given this optimal circumstance, the edit distance cannot be less than 5.
        //which is 8-3 or more precisely Math.abs(3-8).
        //if our maximum edit distance is 4, then we can discard this word
        //without looking at it.
        return Float.NEGATIVE_INFINITY;
      }
      
      // init matrix d
      for (int i = 0; i <=n; ++i) {
        p[i] = i;
      }
      
      // start computing edit distance
      for (int j = 1; j<=m; ++j) { // iterates through target
        int bestPossibleEditDistance = m;
        final int t_j = target[offset+j-1]; // jth character of t
        d[0] = j;

        for (int i=1; i<=n; ++i) { // iterates through text
          // minimum of cell to the left+1, to the top+1, diagonally left and up +(0|1)
          if (t_j != text[i-1]) {
            d[i] = Math.min(Math.min(d[i-1], p[i]),  p[i-1]) + 1;
          } else {
            d[i] = Math.min(Math.min(d[i-1]+1, p[i]+1),  p[i-1]);
          }
          bestPossibleEditDistance = Math.min(bestPossibleEditDistance, d[i]);
        }

        //After calculating row i, the best possible edit distance
        //can be found by found by finding the smallest value in a given column.
        //If the bestPossibleEditDistance is greater than the max distance, abort.

        if (j > maxDistance && bestPossibleEditDistance > maxDistance) {  //equal is okay, but not greater
          //the closest the target can be to the text is just too far away.
          //this target is leaving the party early.
          return Float.NEGATIVE_INFINITY;
        }

        // copy current distance counts to 'previous row' distance counts: swap p and d
        int _d[] = p;
        p = d;
        d = _d;
      }
      
      // our last action in the above loop was to switch d and p, so p now
      // actually has the most recent cost counts

      // this will return less than 0.0 when the edit distance is
      // greater than the number of characters in the shorter word.
      // but this was the formula that was previously used in FuzzyTermEnum,
      // so it has not been changed (even though minimumSimilarity must be
      // greater than 0.0)
      return 1.0f - ((float)p[n] / (float) (realPrefixLength + Math.min(n, m)));
    }
    
    /**
     * The max Distance is the maximum Levenshtein distance for the text
     * compared to some other value that results in score that is
     * better than the minimum similarity.
     * @param m the length of the "other value"
     * @return the maximum levenshtein distance that we care about
     */
    private int calculateMaxDistance(int m) {
      return raw ? maxEdits : Math.min(maxEdits, 
          (int)((1-minSimilarity) * (Math.min(text.length, m) + realPrefixLength)));
    }
  }
  
  /** @lucene.internal */
  public float getMinSimilarity() {
    return minSimilarity;
  }
  
  /** @lucene.internal */
  public float getScaleFactor() {
    return scale_factor;
  }
  
  /** @lucene.internal */
  public static interface LevenshteinAutomataAttribute extends Attribute {
    public List<ByteRunAutomaton> automata();
  }
    
  /** @lucene.internal */
  public static final class LevenshteinAutomataAttributeImpl extends AttributeImpl implements LevenshteinAutomataAttribute {
    private final List<ByteRunAutomaton> automata = new ArrayList<ByteRunAutomaton>();
      
    public List<ByteRunAutomaton> automata() {
      return automata;
    }

    @Override
    public void clear() {
      automata.clear();
    }

    @Override
    public int hashCode() {
      return automata.hashCode();
    }

    @Override
    public boolean equals(Object other) {
      if (this == other)
        return true;
      if (!(other instanceof LevenshteinAutomataAttributeImpl))
        return false;
      return automata.equals(((LevenshteinAutomataAttributeImpl) other).automata);
    }

    @Override
    public void copyTo(AttributeImpl target) {
      final List<ByteRunAutomaton> targetAutomata =
        ((LevenshteinAutomataAttribute) target).automata();
      targetAutomata.clear();
      targetAutomata.addAll(automata);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1221.java