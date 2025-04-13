error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2900.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2900.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2900.java
text:
```scala
I@@ndexWriter writer = new IndexWriter( directory, new IndexWriterConfig(Version.LUCENE_42,new StandardAnalyzer(Version.LUCENE_42)));

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

package org.apache.mahout.clustering.dirichlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.hadoop.conf.Configuration;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.apache.mahout.clustering.Cluster;
import org.apache.mahout.clustering.Model;
import org.apache.mahout.clustering.ModelDistribution;
import org.apache.mahout.clustering.classify.ClusterClassifier;
import org.apache.mahout.clustering.dirichlet.models.DistanceMeasureClusterDistribution;
import org.apache.mahout.clustering.dirichlet.models.DistributionDescription;
import org.apache.mahout.clustering.dirichlet.models.GaussianClusterDistribution;
import org.apache.mahout.clustering.iterator.ClusterIterator;
import org.apache.mahout.clustering.iterator.DirichletClusteringPolicy;
import org.apache.mahout.common.distance.CosineDistanceMeasure;
import org.apache.mahout.common.distance.ManhattanDistanceMeasure;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;
import org.apache.mahout.utils.MahoutTestCase;
import org.apache.mahout.utils.vectors.TermInfo;
import org.apache.mahout.utils.vectors.lucene.CachedTermInfo;
import org.apache.mahout.utils.vectors.lucene.LuceneIterable;
import org.apache.mahout.vectorizer.TFIDF;
import org.apache.mahout.vectorizer.Weight;
import org.junit.Test;

import com.google.common.collect.Lists;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;

public final class TestL1ModelClustering extends MahoutTestCase {
  
  private class MapElement implements Comparable<MapElement> {
    
    private final Double pdf;
    private final String doc;
    
    MapElement(double pdf, String doc) {
      this.pdf = pdf;
      this.doc = doc;
    }
    
    @Override
    // reverse compare to sort in reverse order
    public int compareTo(MapElement e) {
      if (e.pdf > pdf) {
        return 1;
      } else if (e.pdf < pdf) {
        return -1;
      } else {
        return 0;
      }
    }
    
    @Override
    public int hashCode() {
      int hash = pdf == null ? 0 : pdf.hashCode();
      hash ^= doc == null ? 0 : doc.hashCode();
      return hash;
    }
    
    @Override
    public boolean equals(Object other) {
      if (!(other instanceof MapElement)) {
        return false;
      }
      MapElement otherElement = (MapElement) other;
      if ((pdf == null && otherElement.pdf != null) || (pdf != null && !pdf.equals(otherElement.pdf))) {
        return false;
      }
      return doc == null ? otherElement.doc == null : doc.equals(otherElement.doc);
    }
    
    @Override
    public String toString() {
      return pdf.toString() + ' ' + doc;
    }
    
  }
  
  private static final String[] DOCS = {"The quick red fox jumped over the lazy brown dogs.",
      "The quick brown fox jumped over the lazy red dogs.", "The quick red cat jumped over the lazy brown dogs.",
      "The quick brown cat jumped over the lazy red dogs.", "Mary had a little lamb whose fleece was white as snow.",
      "Moby Dick is a story of a whale and a man obsessed.",
      "The robber wore a black fleece jacket and a baseball cap.",
      "The English Springer Spaniel is the best of all dogs."};
  
  private static final String[] DOCS2 = {"The quick red fox jumped over the lazy brown dogs.",
      "The quick brown fox jumped over the lazy red dogs.", "The quick red cat jumped over the lazy brown dogs.",
      "The quick brown cat jumped over the lazy red dogs.", "Mary had a little lamb whose fleece was white as snow.",
      "Mary had a little goat whose fleece was white as snow.",
      "Mary had a little lamb whose fleece was black as tar.",
      "Dick had a little goat whose fleece was white as snow.", "Moby Dick is a story of a whale and a man obsessed.",
      "Moby Bob is a story of a walrus and a man obsessed.", "Moby Dick is a story of a whale and a crazy man.",
      "The robber wore a black fleece jacket and a baseball cap.",
      "The robber wore a red fleece jacket and a baseball cap.",
      "The robber wore a white fleece jacket and a baseball cap.",
      "The English Springer Spaniel is the best of all dogs."};
  
  private List<Vector> sampleData;
  
  private void getSampleData(String[] docs2) throws IOException {
    System.out.println();
    sampleData = Lists.newArrayList();
    RAMDirectory directory = new RAMDirectory();
    IndexWriter writer = new IndexWriter( directory, new IndexWriterConfig(Version.LUCENE_41,new StandardAnalyzer(Version.LUCENE_41)));

    FieldType customType = new FieldType(TextField.TYPE_NOT_STORED);
    customType.setStoreTermVectors(true);
    
    try {
      for (int i = 0; i < docs2.length; i++) {
        Document doc = new Document();
        Field id = new Field("id", "doc_" + i, StringField.TYPE_STORED);
        doc.add(id);
        // Store both position and offset information
        Field text = new Field("content", docs2[i], customType);
        doc.add(text);
        writer.addDocument(doc);
        writer.commit();
      }
    } finally {
      writer.close();
    }
    
    IndexReader reader = DirectoryReader.open(directory);
    System.out.println("Number of documents: \t"+reader.numDocs());
    
    
    Weight weight = new TFIDF();
    TermInfo termInfo = new CachedTermInfo(reader, "content", 1, 100);
    Iterable<Vector> iterable = new LuceneIterable(reader, "id", "content", termInfo,weight);
    
    int i = 0;
    for (Vector vector : iterable) {
      assertNotNull(vector);
      System.out.println(i + ". " + docs2[i++]);
      System.out.println('\t' + formatVector(vector));
      sampleData.add(vector);
    }
  }
  
  private static String formatVector(Vector v) {
    StringBuilder buf = new StringBuilder();
    int nzero = 0;
    Iterator<Vector.Element> iterateNonZero = v.iterateNonZero();
    while (iterateNonZero.hasNext()) {
      iterateNonZero.next();
      nzero++;
    }
    buf.append('(').append(nzero);
    buf.append("nz) [");
    // handle sparse Vectors gracefully, suppressing zero values
    int nextIx = 0;
    for (int i = 0; i < v.size(); i++) {
      double elem = v.get(i);
      if (elem == 0.0) {
        continue;
      }
      if (i > nextIx) {
        buf.append("..{").append(i).append("}=");
      }
      buf.append(String.format(Locale.ENGLISH, "%.2f", elem)).append(", ");
      nextIx = i + 1;
    }
    buf.append(']');
    return buf.toString();
  }
  
  private void printClusters(List<Cluster> models, String[] docs) {
    for (int m = 0; m < models.size(); m++) {
      Cluster model = models.get(m);
      long total = model.getTotalObservations();
      if (total == 0) {
        continue;
      }
      System.out.println();
      System.out.println("Model[" + m + "] had " + total + " observations");
      System.out.println("pdf           document");
      MapElement[] map = new MapElement[sampleData.size()];
      // sort the samples by pdf
      double maxPdf = Double.MIN_NORMAL;
      for (int i = 0; i < sampleData.size(); i++) {
        VectorWritable sample = new VectorWritable(sampleData.get(i));
        double pdf = Math.abs(model.pdf(sample));
        maxPdf = Math.max(maxPdf, pdf);
        map[i] = new MapElement(pdf, docs[i]);
      }
      Arrays.sort(map);
      for (MapElement aMap : map) {
        Double pdf = aMap.pdf;
        double norm = pdf / maxPdf;
        System.out.println(String.format(Locale.ENGLISH, "%.3f", norm) + ' ' + aMap.doc);
      }
    }
  }
  
  @Test
  public void testDocs() throws Exception {
    getSampleData(DOCS);
    DistributionDescription description = new DistributionDescription(GaussianClusterDistribution.class.getName(),
        RandomAccessSparseVector.class.getName(), ManhattanDistanceMeasure.class.getName(), sampleData.get(0).size());
    
    List<Cluster> models = Lists.newArrayList();
    ModelDistribution<VectorWritable> modelDist = description.createModelDistribution(new Configuration());
    for (Model<VectorWritable> cluster : modelDist.sampleFromPrior(15)) {
      models.add((Cluster) cluster);
    }
    
    ClusterClassifier classifier = new ClusterClassifier(models, new DirichletClusteringPolicy(15, 1.0));
    ClusterClassifier posterior = ClusterIterator.iterate(sampleData, classifier, 10);
    
    printClusters(posterior.getModels(), DOCS);
  }
  
  @Test
  public void testDMDocs() throws Exception {
    
    getSampleData(DOCS);
    DistributionDescription description = new DistributionDescription(
        DistanceMeasureClusterDistribution.class.getName(), RandomAccessSparseVector.class.getName(),
        CosineDistanceMeasure.class.getName(), sampleData.get(0).size());
    
    List<Cluster> models = Lists.newArrayList();
    ModelDistribution<VectorWritable> modelDist = description.createModelDistribution(new Configuration());
    for (Model<VectorWritable> cluster : modelDist.sampleFromPrior(15)) {
      models.add((Cluster) cluster);
    }
    
    ClusterClassifier classifier = new ClusterClassifier(models, new DirichletClusteringPolicy(15, 1.0));
    ClusterClassifier posterior = ClusterIterator.iterate(sampleData, classifier, 10);
    
    printClusters(posterior.getModels(), DOCS);
  }
  
  @Test
  public void testDocs2() throws Exception {
    getSampleData(DOCS2);
    DistributionDescription description = new DistributionDescription(GaussianClusterDistribution.class.getName(),
        RandomAccessSparseVector.class.getName(), ManhattanDistanceMeasure.class.getName(), sampleData.get(0).size());
    
    List<Cluster> models = Lists.newArrayList();
    ModelDistribution<VectorWritable> modelDist = description.createModelDistribution(new Configuration());
    for (Model<VectorWritable> cluster : modelDist.sampleFromPrior(15)) {
      models.add((Cluster) cluster);
    }
    
    ClusterClassifier classifier = new ClusterClassifier(models, new DirichletClusteringPolicy(15, 1.0));
    ClusterClassifier posterior = ClusterIterator.iterate(sampleData, classifier, 10);
    
    printClusters(posterior.getModels(), DOCS2);
  }
  
  @Test
  public void testDMDocs2() throws Exception {
    
    getSampleData(DOCS);
    DistributionDescription description = new DistributionDescription(
        DistanceMeasureClusterDistribution.class.getName(), RandomAccessSparseVector.class.getName(),
        CosineDistanceMeasure.class.getName(), sampleData.get(0).size());
    
    List<Cluster> models = Lists.newArrayList();
    ModelDistribution<VectorWritable> modelDist = description.createModelDistribution(new Configuration());
    for (Model<VectorWritable> cluster : modelDist.sampleFromPrior(15)) {
      models.add((Cluster) cluster);
    }
    
    ClusterClassifier classifier = new ClusterClassifier(models, new DirichletClusteringPolicy(15, 1.0));
    ClusterClassifier posterior = ClusterIterator.iterate(sampleData, classifier, 10);
    
    printClusters(posterior.getModels(), DOCS2);
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2900.java