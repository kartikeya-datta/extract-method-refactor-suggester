error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/892.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/892.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/892.java
text:
```scala
H@@ashMap<String,QRelJudgement> missingQueries = new HashMap<String, QRelJudgement>(judgements);

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
package org.apache.lucene.benchmark.quality.trec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.apache.lucene.benchmark.quality.Judge;
import org.apache.lucene.benchmark.quality.QualityQuery;

/**
 * Judge if given document is relevant to given quality query, based on Trec format for judgements.
 */
public class TrecJudge implements Judge {

  HashMap<String,QRelJudgement> judgements;
  
  /**
   * Constructor from a reader.
   * <p>
   * Expected input format:
   * <pre>
   *     qnum  0   doc-name     is-relevant
   * </pre> 
   * Two sample lines:
   * <pre> 
   *     19    0   doc303       1
   *     19    0   doc7295      0
   * </pre> 
   * @param reader where judgments are read from.
   * @throws IOException 
   */
  public TrecJudge (BufferedReader reader) throws IOException {
    judgements = new HashMap<String,QRelJudgement>();
    QRelJudgement curr = null;
    String zero = "0";
    String line;
    
    try {
      while (null!=(line=reader.readLine())) {
        line = line.trim();
        if (line.length()==0 || '#'==line.charAt(0)) {
          continue;
        }
        StringTokenizer st = new StringTokenizer(line);
        String queryID = st.nextToken();
        st.nextToken();
        String docName = st.nextToken();
        boolean relevant = !zero.equals(st.nextToken());
        assert !st.hasMoreTokens() : "wrong format: "+line+"  next: "+st.nextToken();
        if (relevant) { // only keep relevant docs
          if (curr==null || !curr.queryID.equals(queryID)) {
            curr = judgements.get(queryID);
            if (curr==null) {
              curr = new QRelJudgement(queryID);
              judgements.put(queryID,curr);
            }
          }
          curr.addRelevandDoc(docName);
        }
      }
    } finally {
      reader.close();
    }
  }
  
  // inherit javadocs
  public boolean isRelevant(String docName, QualityQuery query) {
    QRelJudgement qrj = judgements.get(query.getQueryID());
    return qrj!=null && qrj.isRelevant(docName);
  }

  /** single Judgement of a trec quality query */
  private static class QRelJudgement {
    private String queryID;
    private HashMap<String,String> relevantDocs;
    
    QRelJudgement(String queryID) {
      this.queryID = queryID;
      relevantDocs = new HashMap<String,String>();
    }
    
    public void addRelevandDoc(String docName) {
      relevantDocs.put(docName,docName);
    }

    boolean isRelevant(String docName) {
      return relevantDocs.containsKey(docName);
    }

    public int maxRecall() {
      return relevantDocs.size();
    }
  }

  // inherit javadocs
  public boolean validateData(QualityQuery[] qq, PrintWriter logger) {
    HashMap<String,QRelJudgement> missingQueries = (HashMap<String, QRelJudgement>) judgements.clone();
    ArrayList<String> missingJudgements = new ArrayList<String>();
    for (int i=0; i<qq.length; i++) {
      String id = qq[i].getQueryID();
      if (missingQueries.containsKey(id)) {
        missingQueries.remove(id);
      } else {
        missingJudgements.add(id);
      }
    }
    boolean isValid = true;
    if (missingJudgements.size()>0) {
      isValid = false;
      if (logger!=null) {
        logger.println("WARNING: "+missingJudgements.size()+" queries have no judgments! - ");
        for (int i=0; i<missingJudgements.size(); i++) {
          logger.println("   "+ missingJudgements.get(i));
        }
      }
    }
    if (missingQueries.size()>0) {
      isValid = false;
      if (logger!=null) {
        logger.println("WARNING: "+missingQueries.size()+" judgments match no query! - ");
        for (final String id : missingQueries.keySet()) {
          logger.println("   "+id);
        }
      }
    }
    return isValid;
  }

  // inherit javadocs
  public int maxRecall(QualityQuery query) {
    QRelJudgement qrj = judgements.get(query.getQueryID());
    if (qrj!=null) {
      return qrj.maxRecall();
    }
    return 0;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/892.java