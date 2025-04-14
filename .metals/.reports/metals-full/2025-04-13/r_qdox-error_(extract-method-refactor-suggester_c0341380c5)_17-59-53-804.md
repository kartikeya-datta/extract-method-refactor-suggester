error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/423.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/423.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/423.java
text:
```scala
private v@@oid addInternal(CharsRef synset[], int size) {

package org.apache.lucene.analysis.synonym;

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
import java.io.LineNumberReader;
import java.io.Reader;
import java.text.ParseException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.util.CharsRef;

/**
 * Parser for wordnet prolog format
 * <p>
 * See http://wordnet.princeton.edu/man/prologdb.5WN.html for a description of the format.
 * @lucene.experimental
 */
// TODO: allow you to specify syntactic categories (e.g. just nouns, etc)
public class WordnetSynonymParser extends SynonymMap.Builder {
  private final boolean expand;
  private final Analyzer analyzer;
  
  public WordnetSynonymParser(boolean dedup, boolean expand, Analyzer analyzer) {
    super(dedup);
    this.expand = expand;
    this.analyzer = analyzer;
  }
  
  public void add(Reader in) throws IOException, ParseException {
    LineNumberReader br = new LineNumberReader(in);
    try {
      String line = null;
      String lastSynSetID = "";
      CharsRef synset[] = new CharsRef[8];
      int synsetSize = 0;
      
      while ((line = br.readLine()) != null) {
        String synSetID = line.substring(2, 11);

        if (!synSetID.equals(lastSynSetID)) {
          addInternal(synset, synsetSize);
          synsetSize = 0;
        }

        if (synset.length <= synsetSize+1) {
          CharsRef larger[] = new CharsRef[synset.length * 2];
          System.arraycopy(synset, 0, larger, 0, synsetSize);
          synset = larger;
        }
        
        synset[synsetSize] = parseSynonym(line, synset[synsetSize]);
        synsetSize++;
        lastSynSetID = synSetID;
      }
      
      // final synset in the file
      addInternal(synset, synsetSize);
    } catch (IllegalArgumentException e) {
      ParseException ex = new ParseException("Invalid synonym rule at line " + br.getLineNumber(), 0);
      ex.initCause(e);
      throw ex;
    } finally {
      br.close();
    }
  }
 
  private CharsRef parseSynonym(String line, CharsRef reuse) throws IOException {
    if (reuse == null) {
      reuse = new CharsRef(8);
    }
    
    int start = line.indexOf('\'')+1;
    int end = line.lastIndexOf('\'');
    
    String text = line.substring(start, end).replace("''", "'");
    return analyze(analyzer, text, reuse);
  }
  
  private void addInternal(CharsRef synset[], int size) throws IOException {
    if (size <= 1) {
      return; // nothing to do
    }
    
    if (expand) {
      for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
          add(synset[i], synset[j], false);
        }
      }
    } else {
      for (int i = 0; i < size; i++) {
        add(synset[i], synset[0], false);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/423.java