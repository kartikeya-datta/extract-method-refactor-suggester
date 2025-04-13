error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2295.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2295.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2295.java
text:
```scala
i@@f (!delDocs.get(d)) {

package org.apache.lucene.misc;

/**
 * Copyright 2006 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.index.DocsEnum;
import org.apache.lucene.search.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.StringHelper;
import org.apache.lucene.util.Bits;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Given a directory, a Similarity, and a list of fields, updates the
 * fieldNorms in place for every document using the Similarity.lengthNorm.
 *
 * <p>
 * NOTE: This only works if you do <b>not</b> use field/document boosts in your
 * index.
 * </p>
 *
 * @version $Id$
 * @deprecated Use {@link org.apache.lucene.index.FieldNormModifier}
 */
@Deprecated
public class LengthNormModifier {
  
  /**
   * Command Line Execution method.
   *
   * <pre>
   * Usage: LengthNormModifier /path/index package.SimilarityClassName field1 field2 ...
   * </pre>
   */
  public static void main(String[] args) throws IOException {
    if (args.length < 3) {
      System.err.println("Usage: LengthNormModifier <index> <package.SimilarityClassName> <field1> [field2] ...");
      System.exit(1);
    }
    
    Similarity s = null;
    try {
      s = Class.forName(args[1]).asSubclass(Similarity.class).newInstance();
    } catch (Exception e) {
      System.err.println("Couldn't instantiate similarity with empty constructor: " + args[1]);
      e.printStackTrace(System.err);
    }
    
    File index = new File(args[0]);
    Directory d = FSDirectory.open(index);
    
    LengthNormModifier lnm = new LengthNormModifier(d, s);
    
    for (int i = 2; i < args.length; i++) {
      System.out.print("Updating field: " + args[i] + " " + (new Date()).toString() + " ... ");
      lnm.reSetNorms(args[i]);
      System.out.println(new Date().toString());
    }
    
    d.close();
  }
  
  
  private Directory dir;
  private Similarity sim;
  
  /**
   * Constructor for code that wishes to use this class progaomatically.
   *
   * @param d The Directory to modify
   * @param s The Similarity to use in <code>reSetNorms</code>
   */
  public LengthNormModifier(Directory d, Similarity s) {
    dir = d;
    sim = s;
  }
  
  /**
   * Resets the norms for the specified field.
   *
   * <p>
   * Opens a new IndexReader on the Directory given to this instance,
   * modifies the norms using the Similarity given to this instance,
   * and closes the IndexReader.
   * </p>
   *
   * @param field the field whose norms should be reset
   */
  public void reSetNorms(String field) throws IOException {
    String fieldName = StringHelper.intern(field);
    int[] termCounts = new int[0];
    
    IndexReader reader = IndexReader.open(dir, false);
    try {

      termCounts = new int[reader.maxDoc()];
      Bits delDocs = MultiFields.getDeletedDocs(reader);
      DocsEnum docs = null;

      Terms terms = MultiFields.getTerms(reader, field);
      if (terms != null) {
        TermsEnum termsEnum = terms.iterator();
        while(termsEnum.next() != null) {
          docs = termsEnum.docs(delDocs, docs);
          int doc;
          while ((doc = docs.nextDoc()) != DocsEnum.NO_MORE_DOCS) {
            termCounts[doc] += docs.freq();
          }
        }
      }

      for (int d = 0; d < termCounts.length; d++) {
        if (! reader.isDeleted(d)) {
          byte norm = Similarity.encodeNorm(sim.lengthNorm(fieldName, termCounts[d]));
          reader.setNorm(d, fieldName, norm);
        }
      }
    } finally {
      reader.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2295.java