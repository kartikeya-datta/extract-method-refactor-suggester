error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/485.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/485.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,17]

error in qdox parser
file content:
```java
offset: 17
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/485.java
text:
```scala
protected final v@@oid setInitialSeekTerm(BytesRef term) {

package org.apache.lucene.index;

/*
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

import java.io.IOException;
import java.util.Comparator;

import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.AttributeSource;
import org.apache.lucene.util.Bits;

/**
 * Abstract class for enumerating a subset of all terms. 
 * 
 * <p>Term enumerations are always ordered by
 * {@link #getComparator}.  Each term in the enumeration is
 * greater than all that precede it.</p>
 * <p><em>Please note:</em> Consumers of this enum cannot
 * call {@code seek()}, it is forward only; it throws
 * {@link UnsupportedOperationException} when a seeking method
 * is called.
 */
public abstract class FilteredTermsEnum extends TermsEnum {

  private BytesRef initialSeekTerm = null;
  private boolean doSeek;
  private BytesRef actualTerm = null;

  private final TermsEnum tenum;

  /** Return value, if term should be accepted or the iteration should
   * {@code END}. The {@code *_SEEK} values denote, that after handling the current term
   * the enum should call {@link #nextSeekTerm} and step forward.
   * @see #accept(BytesRef)
   */
  protected static enum AcceptStatus {YES, YES_AND_SEEK, NO, NO_AND_SEEK, END};
  
  /** Return if term is accepted, not accepted or the iteration should ended
   * (and possibly seek).
   */
  protected abstract AcceptStatus accept(BytesRef term) throws IOException;

  /**
   * Creates a filtered {@link TermsEnum} on a terms enum.
   * @param tenum the terms enumeration to filter.
   */
  public FilteredTermsEnum(final TermsEnum tenum) {
    this(tenum, true);
  }

  /**
   * Creates a filtered {@link TermsEnum} on a terms enum.
   * @param tenum the terms enumeration to filter.
   */
  public FilteredTermsEnum(final TermsEnum tenum, final boolean startWithSeek) {
    assert tenum != null;
    this.tenum = tenum;
    doSeek = startWithSeek;
  }

  /**
   * Use this method to set the initial {@link BytesRef}
   * to seek before iterating. This is a convenience method for
   * subclasses that do not override {@link #nextSeekTerm}.
   * If the initial seek term is {@code null} (default),
   * the enum is empty.
   * <P>You can only use this method, if you keep the default
   * implementation of {@link #nextSeekTerm}.
   */
  protected final void setInitialSeekTerm(BytesRef term) throws IOException {
    this.initialSeekTerm = term;
  }
  
  /** On the first call to {@link #next} or if {@link #accept} returns
   * {@link AcceptStatus#YES_AND_SEEK} or {@link AcceptStatus#NO_AND_SEEK},
   * this method will be called to eventually seek the underlying TermsEnum
   * to a new position.
   * On the first call, {@code currentTerm} will be {@code null}, later
   * calls will provide the term the underlying enum is positioned at.
   * This method returns per default only one time the initial seek term
   * and then {@code null}, so no repositioning is ever done.
   * <p>Override this method, if you want a more sophisticated TermsEnum,
   * that repositions the iterator during enumeration.
   * If this method always returns {@code null} the enum is empty.
   * <p><em>Please note:</em> This method should always provide a greater term
   * than the last enumerated term, else the behaviour of this enum
   * violates the contract for TermsEnums.
   */
  protected BytesRef nextSeekTerm(final BytesRef currentTerm) throws IOException {
    final BytesRef t = initialSeekTerm;
    initialSeekTerm = null;
    return t;
  }

  /**
   * Returns the related attributes, the returned {@link AttributeSource}
   * is shared with the delegate {@code TermsEnum}.
   */
  @Override
  public AttributeSource attributes() {
    return tenum.attributes();
  }
  
  @Override
  public BytesRef term() throws IOException {
    return tenum.term();
  }

  @Override
  public Comparator<BytesRef> getComparator() {
    return tenum.getComparator();
  }
    
  @Override
  public int docFreq() throws IOException {
    return tenum.docFreq();
  }

  @Override
  public long totalTermFreq() throws IOException {
    return tenum.totalTermFreq();
  }

  /** This enum does not support seeking!
   * @throws UnsupportedOperationException
   */
  @Override
  public boolean seekExact(BytesRef term, boolean useCache) throws IOException {
    throw new UnsupportedOperationException(getClass().getName()+" does not support seeking");
  }

  /** This enum does not support seeking!
   * @throws UnsupportedOperationException
   */
  @Override
  public SeekStatus seekCeil(BytesRef term, boolean useCache) throws IOException {
    throw new UnsupportedOperationException(getClass().getName()+" does not support seeking");
  }

  /** This enum does not support seeking!
   * @throws UnsupportedOperationException
   */
  @Override
  public void seekExact(long ord) throws IOException {
    throw new UnsupportedOperationException(getClass().getName()+" does not support seeking");
  }

  @Override
  public long ord() throws IOException {
    return tenum.ord();
  }

  @Override
  public DocsEnum docs(Bits bits, DocsEnum reuse, boolean needsFreqs) throws IOException {
    return tenum.docs(bits, reuse, needsFreqs);
  }
    
  @Override
  public DocsAndPositionsEnum docsAndPositions(Bits bits, DocsAndPositionsEnum reuse, boolean needsOffsets) throws IOException {
    return tenum.docsAndPositions(bits, reuse, needsOffsets);
  }
  
  /** This enum does not support seeking!
   * @throws UnsupportedOperationException
   */
  @Override
  public void seekExact(BytesRef term, TermState state) throws IOException {
    throw new UnsupportedOperationException(getClass().getName()+" does not support seeking");
  }
  
  /**
   * Returns the filtered enums term state 
   */
  @Override
  public TermState termState() throws IOException {
    assert tenum != null;
    return tenum.termState();
  }

  @SuppressWarnings("fallthrough")
  @Override
  public BytesRef next() throws IOException {
    //System.out.println("FTE.next doSeek=" + doSeek);
    //new Throwable().printStackTrace(System.out);
    for (;;) {
      // Seek or forward the iterator
      if (doSeek) {
        doSeek = false;
        final BytesRef t = nextSeekTerm(actualTerm);
        //System.out.println("  seek to t=" + (t == null ? "null" : t.utf8ToString()) + " tenum=" + tenum);
        // Make sure we always seek forward:
        assert actualTerm == null || t == null || getComparator().compare(t, actualTerm) > 0: "curTerm=" + actualTerm + " seekTerm=" + t;
        if (t == null || tenum.seekCeil(t, false) == SeekStatus.END) {
          // no more terms to seek to or enum exhausted
          //System.out.println("  return null");
          return null;
        }
        actualTerm = tenum.term();
        //System.out.println("  got term=" + actualTerm.utf8ToString());
      } else {
        actualTerm = tenum.next();
        if (actualTerm == null) {
          // enum exhausted
          return null;
        }
      }
      
      // check if term is accepted
      switch (accept(actualTerm)) {
        case YES_AND_SEEK:
          doSeek = true;
          // term accepted, but we need to seek so fall-through
        case YES:
          // term accepted
          return actualTerm;
        case NO_AND_SEEK:
          // invalid term, seek next time
          doSeek = true;
          break;
        case END:
          // we are supposed to end the enum
          return null;
      }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/485.java