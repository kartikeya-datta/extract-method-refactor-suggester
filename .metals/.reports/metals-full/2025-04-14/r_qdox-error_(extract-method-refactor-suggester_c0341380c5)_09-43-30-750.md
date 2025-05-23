error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3243.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3243.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3243.java
text:
```scala
N@@amedList sdebug = (NamedList)srsp.getSolrResponse().getResponse().get("debug");

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

package org.apache.solr.handler.component;

import static org.apache.solr.common.params.CommonParams.FQ;
import org.apache.solr.common.params.HighlightParams;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import org.apache.lucene.search.Query;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.SimpleOrderedMap;
import org.apache.solr.search.QueryParsing;
import org.apache.solr.util.SolrPluginUtils;

/**
 * Adds debugging information to a request.
 * 
 * @version $Id$
 * @since solr 1.3
 */
public class DebugComponent extends SearchComponent
{
  public static final String COMPONENT_NAME = "debug";
  
  @Override
  public void prepare(ResponseBuilder rb) throws IOException
  {
    
  }

  @SuppressWarnings("unchecked")
  @Override
  public void process(ResponseBuilder rb) throws IOException
  {
    if( rb.isDebug() ) {
      NamedList stdinfo = SolrPluginUtils.doStandardDebug( rb.req,
          rb.getQueryString(), rb.getQuery(), rb.getResults().docList);
      
      NamedList info = rb.getDebugInfo();
      if( info == null ) {
        rb.setDebugInfo( stdinfo );
        info = stdinfo;
      }
      else {
        info.addAll( stdinfo );
      }
      
      if (rb.getQparser() != null) {
        rb.getQparser().addDebugInfo(rb.getDebugInfo());
      }

      if (null != rb.getDebugInfo() ) {
        if (null != rb.getFilters() ) {
          info.add("filter_queries",rb.req.getParams().getParams(FQ));
          List<String> fqs = new ArrayList<String>(rb.getFilters().size());
          for (Query fq : rb.getFilters()) {
            fqs.add(QueryParsing.toString(fq, rb.req.getSchema()));
          }
          info.add("parsed_filter_queries",fqs);
        }
        
        // Add this directly here?
        rb.rsp.add("debug", rb.getDebugInfo() );
      }
    }
  }


  public void modifyRequest(ResponseBuilder rb, SearchComponent who, ShardRequest sreq) {
    if (!rb.isDebug()) return;

    // Turn on debug to get explain only when retrieving fields
    if ((sreq.purpose & ShardRequest.PURPOSE_GET_FIELDS) != 0) {
        sreq.purpose |= ShardRequest.PURPOSE_GET_DEBUG;
        sreq.params.set("debugQuery", "true");
    } else {
      sreq.params.set("debugQuery", "false");
    }
  }

  @Override
  public void handleResponses(ResponseBuilder rb, ShardRequest sreq) {
  }

  private Set<String> excludeSet = new HashSet<String>(Arrays.asList("explain"));

  @Override
  public void finishStage(ResponseBuilder rb) {
    if (rb.isDebug() && rb.stage == ResponseBuilder.STAGE_GET_FIELDS) {
      NamedList info = null;
      NamedList explain = new SimpleOrderedMap();

      Object[] arr = new Object[rb.resultIds.size() * 2];

      for (ShardRequest sreq : rb.finished) {
        if ((sreq.purpose & ShardRequest.PURPOSE_GET_DEBUG) == 0) continue;
        for (ShardResponse srsp : sreq.responses) {
          NamedList sdebug = (NamedList)srsp.rsp.getResponse().get("debug");
          info = (NamedList)merge(sdebug, info, excludeSet);

          NamedList sexplain = (NamedList)sdebug.get("explain");

          for (int i=0; i<sexplain.size(); i++) {
            String id = sexplain.getName(i);
            // TODO: lookup won't work for non-string ids... String vs Float
            ShardDoc sdoc = rb.resultIds.get(id);
            int idx = sdoc.positionInResponse;
            arr[idx<<1] = id;
            arr[(idx<<1)+1] = sexplain.getVal(i);
          }
        }
      }

      explain = HighlightComponent.removeNulls(new SimpleOrderedMap(Arrays.asList(arr)));

      if (info == null) {
        info = new SimpleOrderedMap();
      }
      int idx = info.indexOf("explain",0);
      if (idx>=0) {
        info.setVal(idx, explain);
      } else {
        info.add("explain", explain);
      }
      
      rb.setDebugInfo(info);
      rb.rsp.add("debug", rb.getDebugInfo() );      
    }
  }


  Object merge(Object source, Object dest, Set<String> exclude) {
    if (source == null) return dest;
    if (dest == null) {
      if (source instanceof NamedList) {
        dest = source instanceof SimpleOrderedMap ? new SimpleOrderedMap() : new NamedList();
      } else {
        return source;
      }
    } else {

      if (dest instanceof Collection) {
        if (source instanceof Collection) {
          ((Collection)dest).addAll((Collection)source);
        } else {
          ((Collection)dest).add(source);
        }
        return dest;
      } else if (source instanceof Number) {
        if (dest instanceof Number) {
          if (source instanceof Double || dest instanceof Double) {
            return ((Number)source).doubleValue() + ((Number)dest).doubleValue();
          }
          return ((Number)source).longValue() + ((Number)dest).longValue();
        }
        // fall through
      } else if (source instanceof String) {
        if (source.equals(dest)) {
          return dest;
        }
        // fall through
      }
    }


    if (source instanceof NamedList && dest instanceof NamedList) {
      NamedList tmp = new NamedList();
      NamedList sl = (NamedList)source;
      NamedList dl = (NamedList)dest;
      for (int i=0; i<sl.size(); i++) {
        String skey = sl.getName(i);
        if (exclude != null && exclude.contains(skey)) continue;
        Object sval = sl.getVal(i);
        int didx = -1;

        // optimize case where elements are in same position
        if (i < dl.size()) {
          String dkey = dl.getName(i);
          if (skey == dkey || (skey!=null && skey.equals(dkey))) {
            didx = i;
          }
        }

        if (didx == -1) {
          didx = dl.indexOf(skey, 0);
        }

        if (didx == -1) {
          tmp.add(skey, merge(sval, null, null));
        } else {
          dl.setVal(didx, merge(sval, dl.getVal(didx), null));
        }
      }
      dl.addAll(tmp);
      return dl;
    }

    // merge unlike elements in a list
    List t = new ArrayList();
    t.add(dest);
    t.add(source);
    return t;
  }


  
  /////////////////////////////////////////////
  ///  SolrInfoMBean
  ////////////////////////////////////////////

  @Override
  public String getDescription() {
    return "Debug Information";
  }

  @Override
  public String getVersion() {
    return "$Revision$";
  }

  @Override
  public String getSourceId() {
    return "$Id$";
  }

  @Override
  public String getSource() {
    return "$URL$";
  }

  @Override
  public URL[] getDocs() {
    return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3243.java