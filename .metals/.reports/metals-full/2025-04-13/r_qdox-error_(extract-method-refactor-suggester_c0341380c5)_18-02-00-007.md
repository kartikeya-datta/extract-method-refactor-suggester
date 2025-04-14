error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2066.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2066.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2066.java
text:
```scala
d@@ocsEnum = termsEnum.docs(null, docsEnum, false);

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
package org.apache.solr.search.function;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.apache.lucene.index.DocsEnum;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.IndexReaderContext;
import org.apache.lucene.index.ReaderUtil;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.queries.function.FunctionValues;
import org.apache.lucene.queries.function.ValueSource;
import org.apache.lucene.queries.function.docvalues.FloatDocValues;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.IOUtils;
import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.RequestHandlerBase;
import org.apache.solr.handler.RequestHandlerUtils;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.schema.FieldType;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.search.QParser;
import org.apache.solr.update.processor.UpdateRequestProcessor;
import org.apache.solr.util.VersionedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Obtains float field values from an external file.
 *
 */

public class FileFloatSource extends ValueSource {
  private SchemaField field;
  private final SchemaField keyField;
  private final float defVal;

  private final String dataDir;

  public FileFloatSource(SchemaField field, SchemaField keyField, float defVal, QParser parser) {
    this.field = field;
    this.keyField = keyField;
    this.defVal = defVal;
    this.dataDir = parser.getReq().getCore().getDataDir();
  }

  @Override
  public String description() {
    return "float(" + field + ')';
  }

  @Override
  public FunctionValues getValues(Map context, AtomicReaderContext readerContext) throws IOException {
    final int off = readerContext.docBase;
    IndexReaderContext topLevelContext = ReaderUtil.getTopLevelContext(readerContext);

    final float[] arr = getCachedFloats(topLevelContext.reader());
    return new FloatDocValues(this) {
      @Override
      public float floatVal(int doc) {
        return arr[doc + off];
      }

      @Override
      public Object objectVal(int doc) {
        return floatVal(doc);   // TODO: keep track of missing values
      }
    };
  }

  @Override
  public boolean equals(Object o) {
    if (o.getClass() !=  FileFloatSource.class) return false;
    FileFloatSource other = (FileFloatSource)o;
    return this.field.getName().equals(other.field.getName())
            && this.keyField.getName().equals(other.keyField.getName())
            && this.defVal == other.defVal
            && this.dataDir.equals(other.dataDir);
  }

  @Override
  public int hashCode() {
    return FileFloatSource.class.hashCode() + field.getName().hashCode();
  };

  @Override
  public String toString() {
    return "FileFloatSource(field="+field.getName()+",keyField="+keyField.getName()
            + ",defVal="+defVal+",dataDir="+dataDir+")";

  }
  
  public static void resetCache(){
    floatCache.resetCache();
  }

  private final float[] getCachedFloats(IndexReader reader) {
    return (float[])floatCache.get(reader, new Entry(this));
  }

  static Cache floatCache = new Cache() {
    @Override
    protected Object createValue(IndexReader reader, Object key) {
      return getFloats(((Entry)key).ffs, reader);
    }
  };

  /** Internal cache. (from lucene FieldCache) */
  abstract static class Cache {
    private final Map readerCache = new WeakHashMap();

    protected abstract Object createValue(IndexReader reader, Object key);

    public Object get(IndexReader reader, Object key) {
      Map innerCache;
      Object value;
      synchronized (readerCache) {
        innerCache = (Map) readerCache.get(reader);
        if (innerCache == null) {
          innerCache = new HashMap();
          readerCache.put(reader, innerCache);
          value = null;
        } else {
          value = innerCache.get(key);
        }
        if (value == null) {
          value = new CreationPlaceholder();
          innerCache.put(key, value);
        }
      }
      if (value instanceof CreationPlaceholder) {
        synchronized (value) {
          CreationPlaceholder progress = (CreationPlaceholder) value;
          if (progress.value == null) {
            progress.value = createValue(reader, key);
            synchronized (readerCache) {
              innerCache.put(key, progress.value);
              onlyForTesting = progress.value;
            }
          }
          return progress.value;
        }
      }

      return value;
    }
    
    public void resetCache(){
      synchronized(readerCache){
        // Map.clear() is optional and can throw UnsipportedOperationException,
        // but readerCache is WeakHashMap and it supports clear().
        readerCache.clear();
      }
    }
  }

  static Object onlyForTesting; // set to the last value

  static final class CreationPlaceholder {
    Object value;
  }

    /** Expert: Every composite-key in the internal cache is of this type. */
  private static class Entry {
    final FileFloatSource ffs;
    public Entry(FileFloatSource ffs) {
      this.ffs = ffs;
    }

    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Entry)) return false;
      Entry other = (Entry)o;
      return ffs.equals(other.ffs);
    }

    @Override
    public int hashCode() {
      return ffs.hashCode();
    }
  }



  private static float[] getFloats(FileFloatSource ffs, IndexReader reader) {
    float[] vals = new float[reader.maxDoc()];
    if (ffs.defVal != 0) {
      Arrays.fill(vals, ffs.defVal);
    }
    InputStream is;
    String fname = "external_" + ffs.field.getName();
    try {
      is = VersionedFile.getLatestFile(ffs.dataDir, fname);
    } catch (IOException e) {
      // log, use defaults
      SolrCore.log.error("Error opening external value source file: " +e);
      return vals;
    }

    BufferedReader r = new BufferedReader(new InputStreamReader(is, IOUtils.CHARSET_UTF_8));

    String idName = ffs.keyField.getName();
    FieldType idType = ffs.keyField.getType();

    // warning: lucene's termEnum.skipTo() is not optimized... it simply does a next()
    // because of this, simply ask the reader for a new termEnum rather than
    // trying to use skipTo()

    List<String> notFound = new ArrayList<String>();
    int notFoundCount=0;
    int otherErrors=0;

    char delimiter='=';

    BytesRef internalKey = new BytesRef();

    try {
      TermsEnum termsEnum = MultiFields.getTerms(reader, idName).iterator(null);
      DocsEnum docsEnum = null;

      // removing deleted docs shouldn't matter
      // final Bits liveDocs = MultiFields.getLiveDocs(reader);

      for (String line; (line=r.readLine())!=null;) {
        int delimIndex = line.lastIndexOf(delimiter);
        if (delimIndex < 0) continue;

        int endIndex = line.length();
        String key = line.substring(0, delimIndex);
        String val = line.substring(delimIndex+1, endIndex);

        float fval;
        try {
          idType.readableToIndexed(key, internalKey);
          fval=Float.parseFloat(val);
        } catch (Exception e) {
          if (++otherErrors<=10) {
            SolrCore.log.error( "Error loading external value source + fileName + " + e
              + (otherErrors<10 ? "" : "\tSkipping future errors for this file.")
            );
          }
          continue;  // go to next line in file.. leave values as default.
        }

        if (!termsEnum.seekExact(internalKey, false)) {
          if (notFoundCount<10) {  // collect first 10 not found for logging
            notFound.add(key);
          }
          notFoundCount++;
          continue;
        }

        docsEnum = termsEnum.docs(null, docsEnum, 0);
        int doc;
        while ((doc = docsEnum.nextDoc()) != DocIdSetIterator.NO_MORE_DOCS) {
          vals[doc] = fval;
        }
      }

    } catch (IOException e) {
      // log, use defaults
      SolrCore.log.error("Error loading external value source: " +e);
    } finally {
      // swallow exceptions on close so we don't override any
      // exceptions that happened in the loop
      try{r.close();}catch(Exception e){}
    }

    SolrCore.log.info("Loaded external value source " + fname
      + (notFoundCount==0 ? "" : " :"+notFoundCount+" missing keys "+notFound)
    );

    return vals;
  }

  public static class ReloadCacheRequestHandler extends RequestHandlerBase {
    
    static final Logger log = LoggerFactory.getLogger(ReloadCacheRequestHandler.class);

    @Override
    public void handleRequestBody(SolrQueryRequest req, SolrQueryResponse rsp)
        throws Exception {
      FileFloatSource.resetCache();
      log.debug("readerCache has been reset.");

      UpdateRequestProcessor processor =
        req.getCore().getUpdateProcessingChain(null).createProcessor(req, rsp);
      try{
        RequestHandlerUtils.handleCommit(req, processor, req.getParams(), true);
      }
      finally{
        processor.finish();
      }
    }

    @Override
    public String getDescription() {
      return "Reload readerCache request handler";
    }

    @Override
    public String getSource() {
      return "$URL$";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2066.java