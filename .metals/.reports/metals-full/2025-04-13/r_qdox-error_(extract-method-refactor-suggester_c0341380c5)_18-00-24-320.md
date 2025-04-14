error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3509.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3509.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3509.java
text:
```scala
l@@og.info(new StringBuffer("Analyzing text").toString());

package org.apache.solr.uima.processor;

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

import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrException.ErrorCode;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.core.SolrCore;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.uima.processor.SolrUIMAConfiguration.MapField;
import org.apache.lucene.analysis.uima.ae.AEProvider;
import org.apache.lucene.analysis.uima.ae.AEProviderFactory;
import org.apache.solr.update.AddUpdateCommand;
import org.apache.solr.update.processor.UpdateRequestProcessor;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * Update document(s) to be indexed with UIMA extracted information
 *
 *
 */
public class UIMAUpdateRequestProcessor extends UpdateRequestProcessor {

  private final Logger log = LoggerFactory.getLogger(UIMAUpdateRequestProcessor.class);

  SolrUIMAConfiguration solrUIMAConfiguration;

  private AEProvider aeProvider;
  
  private SolrCore solrCore;

  public UIMAUpdateRequestProcessor(UpdateRequestProcessor next, SolrCore solrCore,
      SolrUIMAConfiguration config) {
    super(next);
    initialize(solrCore, config);
  }

  private void initialize(SolrCore solrCore, SolrUIMAConfiguration config) {
    this.solrCore = solrCore;
    solrUIMAConfiguration = config;
    aeProvider = AEProviderFactory.getInstance().getAEProvider(solrCore.getName(),
            solrUIMAConfiguration.getAePath(), solrUIMAConfiguration.getRuntimeParameters());
  }

  @Override
  public void processAdd(AddUpdateCommand cmd) throws IOException {
    String text = null;
    try {
      /* get Solr document */
      SolrInputDocument solrInputDocument = cmd.getSolrInputDocument();

      /* get the fields to analyze */
      String[] texts = getTextsToAnalyze(solrInputDocument);
      for (int i = 0; i < texts.length; i++) {
        text = texts[i];
        if (text != null && text.length()>0) {
          /* process the text value */
          JCas jcas = processText(text);

          UIMAToSolrMapper uimaToSolrMapper = new UIMAToSolrMapper(solrInputDocument, jcas);
          /* get field mapping from config */
          Map<String, Map<String, MapField>> typesAndFeaturesFieldsMap = solrUIMAConfiguration
                  .getTypesFeaturesFieldsMapping();
          /* map type features on fields */
          for (String typeFQN : typesAndFeaturesFieldsMap.keySet()) {
            uimaToSolrMapper.map(typeFQN, typesAndFeaturesFieldsMap.get(typeFQN));
          }
        }
      }
    } catch (Exception e) {
      String logField = solrUIMAConfiguration.getLogField();
      if(logField == null){
        SchemaField uniqueKeyField = solrCore.getSchema().getUniqueKeyField();
        if(uniqueKeyField != null){
          logField = uniqueKeyField.getName();
        }
      }
      String optionalFieldInfo = logField == null ? "." :
        new StringBuilder(". ").append(logField).append("=")
        .append((String)cmd.getSolrInputDocument().getField(logField).getValue())
        .append(", ").toString();
      int len = Math.min(text.length(), 100);
      if (solrUIMAConfiguration.isIgnoreErrors()) {
        log.warn(new StringBuilder("skip the text processing due to ")
          .append(e.getLocalizedMessage()).append(optionalFieldInfo)
          .append(" text=\"").append(text.substring(0, len)).append("...\"").toString());
      } else {
        throw new SolrException(ErrorCode.SERVER_ERROR,
            new StringBuilder("processing error: ")
              .append(e.getLocalizedMessage()).append(optionalFieldInfo)
              .append(" text=\"").append(text.substring(0, len)).append("...\"").toString(), e);
      }
    }
    super.processAdd(cmd);
  }

  /*
   * get the texts to analyze from the corresponding fields
   */
  private String[] getTextsToAnalyze(SolrInputDocument solrInputDocument) {
    String[] fieldsToAnalyze = solrUIMAConfiguration.getFieldsToAnalyze();
    boolean merge = solrUIMAConfiguration.isFieldsMerging();
    String[] textVals;
    if (merge) {
      StringBuilder unifiedText = new StringBuilder("");
      for (int i = 0; i < fieldsToAnalyze.length; i++) {
        unifiedText.append(String.valueOf(solrInputDocument.getFieldValue(fieldsToAnalyze[i])));
      }
      textVals = new String[1];
      textVals[0] = unifiedText.toString();
    } else {
      textVals = new String[fieldsToAnalyze.length];
      for (int i = 0; i < fieldsToAnalyze.length; i++) {
        textVals[i] = String.valueOf(solrInputDocument.getFieldValue(fieldsToAnalyze[i]));
      }
    }
    return textVals;
  }

  /* process a field value executing UIMA the CAS containing it as document text */
  private JCas processText(String textFieldValue) throws ResourceInitializationException,
          AnalysisEngineProcessException {
    log.info(new StringBuffer("Analazying text").toString());
    /* get the UIMA analysis engine */
    AnalysisEngine ae = aeProvider.getAE();

    /* create a JCas which contain the text to analyze */
    JCas jcas = ae.newJCas();
    jcas.setDocumentText(textFieldValue);

    /* perform analysis on text field */
    ae.process(jcas);
    log.info(new StringBuilder("Text processing completed").toString());
    return jcas;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3509.java