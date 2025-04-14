error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3054.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3054.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3054.java
text:
```scala
i@@ndexWriter.shutdown();

package org.apache.lucene.demo.facet;

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
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.facet.DrillDownQuery;
import org.apache.lucene.facet.FacetResult;
import org.apache.lucene.facet.Facets;
import org.apache.lucene.facet.FacetsCollector;
import org.apache.lucene.facet.FacetsConfig;
import org.apache.lucene.facet.taxonomy.FloatAssociationFacetField;
import org.apache.lucene.facet.taxonomy.IntAssociationFacetField;
import org.apache.lucene.facet.taxonomy.TaxonomyFacetSumFloatAssociations;
import org.apache.lucene.facet.taxonomy.TaxonomyFacetSumIntAssociations;
import org.apache.lucene.facet.taxonomy.TaxonomyReader;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyReader;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyWriter;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

/** Shows example usage of category associations. */
public class AssociationsFacetsExample {

  private final Directory indexDir = new RAMDirectory();
  private final Directory taxoDir = new RAMDirectory();
  private final FacetsConfig config;

  /** Empty constructor */
  public AssociationsFacetsExample() {
    config = new FacetsConfig();
    config.setMultiValued("tags", true);
    config.setIndexFieldName("tags", "$tags");
    config.setMultiValued("genre", true);
    config.setIndexFieldName("genre", "$genre");
  }
  
  /** Build the example index. */
  private void index() throws IOException {
    IndexWriterConfig iwc = new IndexWriterConfig(FacetExamples.EXAMPLES_VER, 
                                                  new WhitespaceAnalyzer(FacetExamples.EXAMPLES_VER));
    IndexWriter indexWriter = new IndexWriter(indexDir, iwc);

    // Writes facet ords to a separate directory from the main index
    DirectoryTaxonomyWriter taxoWriter = new DirectoryTaxonomyWriter(taxoDir);

    Document doc = new Document();
    // 3 occurrences for tag 'lucene'
    doc.add(new IntAssociationFacetField(3, "tags", "lucene"));
    // 87% confidence level of genre 'computing'
    doc.add(new FloatAssociationFacetField(0.87f, "genre", "computing"));
    indexWriter.addDocument(config.build(taxoWriter, doc));

    doc = new Document();
    // 1 occurrence for tag 'lucene'
    doc.add(new IntAssociationFacetField(1, "tags", "lucene"));
    // 2 occurrence for tag 'solr'
    doc.add(new IntAssociationFacetField(2, "tags", "solr"));
    // 75% confidence level of genre 'computing'
    doc.add(new FloatAssociationFacetField(0.75f, "genre", "computing"));
    // 34% confidence level of genre 'software'
    doc.add(new FloatAssociationFacetField(0.34f, "genre", "software"));
    indexWriter.addDocument(config.build(taxoWriter, doc));

    indexWriter.close();
    taxoWriter.close();
  }

  /** User runs a query and aggregates facets by summing their association values. */
  private List<FacetResult> sumAssociations() throws IOException {
    DirectoryReader indexReader = DirectoryReader.open(indexDir);
    IndexSearcher searcher = new IndexSearcher(indexReader);
    TaxonomyReader taxoReader = new DirectoryTaxonomyReader(taxoDir);
    
    FacetsCollector fc = new FacetsCollector();
    
    // MatchAllDocsQuery is for "browsing" (counts facets
    // for all non-deleted docs in the index); normally
    // you'd use a "normal" query:
    FacetsCollector.search(searcher, new MatchAllDocsQuery(), 10, fc);
    
    Facets tags = new TaxonomyFacetSumIntAssociations("$tags", taxoReader, config, fc);
    Facets genre = new TaxonomyFacetSumFloatAssociations("$genre", taxoReader, config, fc);

    // Retrieve results
    List<FacetResult> results = new ArrayList<>();
    results.add(tags.getTopChildren(10, "tags"));
    results.add(genre.getTopChildren(10, "genre"));

    indexReader.close();
    taxoReader.close();
    
    return results;
  }
  
  /** User drills down on 'tags/solr'. */
  private FacetResult drillDown() throws IOException {
    DirectoryReader indexReader = DirectoryReader.open(indexDir);
    IndexSearcher searcher = new IndexSearcher(indexReader);
    TaxonomyReader taxoReader = new DirectoryTaxonomyReader(taxoDir);

    // Passing no baseQuery means we drill down on all
    // documents ("browse only"):
    DrillDownQuery q = new DrillDownQuery(config);

    // Now user drills down on Publish Date/2010:
    q.add("tags", "solr");
    FacetsCollector fc = new FacetsCollector();
    FacetsCollector.search(searcher, q, 10, fc);

    // Retrieve results
    Facets facets = new TaxonomyFacetSumFloatAssociations("$genre", taxoReader, config, fc);
    FacetResult result = facets.getTopChildren(10, "genre");

    indexReader.close();
    taxoReader.close();
    
    return result;
  }
  
  /** Runs summing association example. */
  public List<FacetResult> runSumAssociations() throws IOException {
    index();
    return sumAssociations();
  }
  
  /** Runs the drill-down example. */
  public FacetResult runDrillDown() throws IOException {
    index();
    return drillDown();
  }

  /** Runs the sum int/float associations examples and prints the results. */
  public static void main(String[] args) throws Exception {
    System.out.println("Sum associations example:");
    System.out.println("-------------------------");
    List<FacetResult> results = new AssociationsFacetsExample().runSumAssociations();
    System.out.println("tags: " + results.get(0));
    System.out.println("genre: " + results.get(1));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3054.java