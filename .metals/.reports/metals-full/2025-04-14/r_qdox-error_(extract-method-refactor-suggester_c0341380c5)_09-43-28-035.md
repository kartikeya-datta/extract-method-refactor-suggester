error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1467.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1467.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1467.java
text:
```scala
T@@erms terms = lfields == null ? null : lfields.terms(field);

package org.apache.solr.handler.component;
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

import org.apache.lucene.index.*;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.StringHelper;
import org.apache.noggit.CharArr;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.*;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.SimpleOrderedMap;
import org.apache.solr.common.util.StrUtils;
import org.apache.solr.schema.FieldType;
import org.apache.solr.schema.StrField;
import org.apache.solr.request.SimpleFacets.CountPair;
import org.apache.solr.search.SolrIndexReader;
import org.apache.solr.util.BoundedTreeSet;

import org.apache.solr.client.solrj.response.TermsResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Return TermEnum information, useful for things like auto suggest.
 *
 * @see org.apache.solr.common.params.TermsParams
 *      See Lucene's TermEnum class
 */
public class TermsComponent extends SearchComponent {
  public static final int UNLIMITED_MAX_COUNT = -1;
  public static final String COMPONENT_NAME = "terms";

  @Override
  public void prepare(ResponseBuilder rb) throws IOException {
    SolrParams params = rb.req.getParams();
    if (params.getBool(TermsParams.TERMS, false)) {
      rb.doTerms = true;
    }

    // TODO: temporary... this should go in a different component.
    String shards = params.get(ShardParams.SHARDS);
    if (shards != null) {
      if (params.get(ShardParams.SHARDS_QT) == null) {
        throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, "No shards.qt parameter specified");
      }
      List<String> lst = StrUtils.splitSmart(shards, ",", true);
      rb.shards = lst.toArray(new String[lst.size()]);
    }
  }

  public void process(ResponseBuilder rb) throws IOException {
    SolrParams params = rb.req.getParams();
    if (!params.getBool(TermsParams.TERMS, false)) return;

    String[] fields = params.getParams(TermsParams.TERMS_FIELD);

    NamedList termsResult = new SimpleOrderedMap();
    rb.rsp.add("terms", termsResult);

    if (fields == null || fields.length==0) return;

    int limit = params.getInt(TermsParams.TERMS_LIMIT, 10);
    if (limit < 0) {
      limit = Integer.MAX_VALUE;
    }

    String lowerStr = params.get(TermsParams.TERMS_LOWER);
    String upperStr = params.get(TermsParams.TERMS_UPPER);
    boolean upperIncl = params.getBool(TermsParams.TERMS_UPPER_INCLUSIVE, false);
    boolean lowerIncl = params.getBool(TermsParams.TERMS_LOWER_INCLUSIVE, true);
    boolean sort = !TermsParams.TERMS_SORT_INDEX.equals(
        params.get(TermsParams.TERMS_SORT, TermsParams.TERMS_SORT_COUNT));
    int freqmin = params.getInt(TermsParams.TERMS_MINCOUNT, 1);
    int freqmax = params.getInt(TermsParams.TERMS_MAXCOUNT, UNLIMITED_MAX_COUNT);
    if (freqmax<0) {
      freqmax = Integer.MAX_VALUE;
    }
    String prefix = params.get(TermsParams.TERMS_PREFIX_STR);
    String regexp = params.get(TermsParams.TERMS_REGEXP_STR);
    Pattern pattern = regexp != null ? Pattern.compile(regexp, resolveRegexpFlags(params)) : null;

    boolean raw = params.getBool(TermsParams.TERMS_RAW, false);


    SolrIndexReader sr = rb.req.getSearcher().getReader();
    Fields lfields = MultiFields.getFields(sr);

    for (String field : fields) {
      NamedList fieldTerms = new NamedList();
      termsResult.add(field, fieldTerms);

      Terms terms = lfields.terms(field);
      if (terms == null) {
        // no terms for this field
        continue;
      }

      FieldType ft = raw ? null : rb.req.getSchema().getFieldTypeNoEx(field);
      if (ft==null) ft = new StrField();

      // prefix must currently be text
      BytesRef prefixBytes = prefix==null ? null : new BytesRef(prefix);

      BytesRef upperBytes = null;
      if (upperStr != null) {
        upperBytes = new BytesRef();
        ft.readableToIndexed(upperStr, upperBytes);
      }

      BytesRef lowerBytes;
      if (lowerStr == null) {
        // If no lower bound was specified, use the prefix
        lowerBytes = prefixBytes;
      } else {
        lowerBytes = new BytesRef();
        if (raw) {
          // TODO: how to handle binary? perhaps we don't for "raw"... or if the field exists
          // perhaps we detect if the FieldType is non-character and expect hex if so?
          lowerBytes = new BytesRef(lowerStr);
        } else {
          lowerBytes = new BytesRef();
          ft.readableToIndexed(lowerStr, lowerBytes);
        }
      }


     TermsEnum termsEnum = terms.iterator();
     BytesRef term = null;

      if (lowerBytes != null) {
        if (termsEnum.seek(lowerBytes, true) == TermsEnum.SeekStatus.END) {
          termsEnum = null;
        } else {
          term = termsEnum.term();
          //Only advance the enum if we are excluding the lower bound and the lower Term actually matches
          if (lowerIncl == false && term.equals(lowerBytes)) {
            term = termsEnum.next();
          }
        }
      } else {
        // position termsEnum on first term
        term = termsEnum.next();
      }

      int i = 0;
      BoundedTreeSet<CountPair<BytesRef, Integer>> queue = (sort ? new BoundedTreeSet<CountPair<BytesRef, Integer>>(limit) : null);
      CharArr external = new CharArr();

      while (term != null && (i<limit || sort)) {
        boolean externalized = false; // did we fill in "external" yet for this term?

        // stop if the prefix doesn't match
        if (prefixBytes != null && !term.startsWith(prefixBytes)) break;

        if (pattern != null) {
          // indexed text or external text?
          // TODO: support "raw" mode?
          external.reset();
          ft.indexedToReadable(term, external);
          if (!pattern.matcher(external).matches()) {
            term = termsEnum.next();
            continue;
          }
        }

        if (upperBytes != null) {
          int upperCmp = term.compareTo(upperBytes);
          // if we are past the upper term, or equal to it (when don't include upper) then stop.
          if (upperCmp>0 || (upperCmp==0 && !upperIncl)) break;
        }

        // This is a good term in the range.  Check if mincount/maxcount conditions are satisfied.
        int docFreq = termsEnum.docFreq();
        if (docFreq >= freqmin && docFreq <= freqmax) {
          // add the term to the list
          if (sort) {
            queue.add(new CountPair<BytesRef, Integer>(new BytesRef(term), docFreq));
          } else {

            // TODO: handle raw somehow
            if (!externalized) {
              external.reset();
              ft.indexedToReadable(term, external);
            }
            String label = external.toString();
            

            fieldTerms.add(label, docFreq);
            i++;
          }
        }

        term = termsEnum.next();
      }

      if (sort) {
        for (CountPair<BytesRef, Integer> item : queue) {
          if (i >= limit) break;
          external.reset();
          ft.indexedToReadable(item.key, external);          
          fieldTerms.add(external.toString(), item.val);
          i++;
        }
      }
    }
  }

  int resolveRegexpFlags(SolrParams params) {
      String[] flagParams = params.getParams(TermsParams.TERMS_REGEXP_FLAG);
      if (flagParams == null) {
          return 0;
      }
      int flags = 0;
      for (String flagParam : flagParams) {
          try {
            flags |= TermsParams.TermsRegexpFlag.valueOf(flagParam.toUpperCase(Locale.ENGLISH)).getValue();
          } catch (IllegalArgumentException iae) {
              throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, "Unknown terms regex flag '" + flagParam + "'");
          }
      }
      return flags;
  }

  @Override
  public int distributedProcess(ResponseBuilder rb) throws IOException {
    if (!rb.doTerms) {
      return ResponseBuilder.STAGE_DONE;
    }

    if (rb.stage == ResponseBuilder.STAGE_EXECUTE_QUERY) {
      TermsHelper th = rb._termsHelper;
      if (th == null) {
        th = rb._termsHelper = new TermsHelper();
        th.init(rb.req.getParams());
      }
      ShardRequest sreq = createShardQuery(rb.req.getParams());
      rb.addRequest(this, sreq);
    }

    if (rb.stage < ResponseBuilder.STAGE_EXECUTE_QUERY) {
      return ResponseBuilder.STAGE_EXECUTE_QUERY;
    } else {
      return ResponseBuilder.STAGE_DONE;
    }
  }

  @Override
  public void handleResponses(ResponseBuilder rb, ShardRequest sreq) {
    if (!rb.doTerms || (sreq.purpose & ShardRequest.PURPOSE_GET_TERMS) == 0) {
      return;
    }
    TermsHelper th = rb._termsHelper;
    if (th != null) {
      for (ShardResponse srsp : sreq.responses) {
        th.parse((NamedList) srsp.getSolrResponse().getResponse().get("terms"));
      }
    }
  }

  @Override
  public void finishStage(ResponseBuilder rb) {
    if (!rb.doTerms || rb.stage != ResponseBuilder.STAGE_EXECUTE_QUERY) {
      return;
    }

    TermsHelper ti = rb._termsHelper;
    NamedList terms = ti.buildResponse();

    rb.rsp.add("terms", terms);
    rb._termsHelper = null;
  }

  private ShardRequest createShardQuery(SolrParams params) {
    ShardRequest sreq = new ShardRequest();
    sreq.purpose = ShardRequest.PURPOSE_GET_TERMS;

    // base shard request on original parameters
    sreq.params = new ModifiableSolrParams(params);

    // don't pass through the shards param
    sreq.params.remove(ShardParams.SHARDS);

    // remove any limits for shards, we want them to return all possible
    // responses
    // we want this so we can calculate the correct counts
    // dont sort by count to avoid that unnecessary overhead on the shards
    sreq.params.remove(TermsParams.TERMS_MAXCOUNT);
    sreq.params.remove(TermsParams.TERMS_MINCOUNT);
    sreq.params.set(TermsParams.TERMS_LIMIT, -1);
    sreq.params.set(TermsParams.TERMS_SORT, TermsParams.TERMS_SORT_INDEX);

    // TODO: is there a better way to handle this?
    String qt = params.get(CommonParams.QT);
    if (qt != null) {
      sreq.params.add(CommonParams.QT, qt);
    }
    return sreq;
  }

  public class TermsHelper {
    // map to store returned terms
    private HashMap<String, HashMap<String, TermsResponse.Term>> fieldmap;
    private SolrParams params;

    public TermsHelper() {
      fieldmap = new HashMap<String, HashMap<String, TermsResponse.Term>>(5);
    }

    public void init(SolrParams params) {
      this.params = params;
      String[] fields = params.getParams(TermsParams.TERMS_FIELD);
      if (fields != null) {
        for (String field : fields) {
          // TODO: not sure 128 is the best starting size
          // It use it because that is what is used for facets
          fieldmap.put(field, new HashMap<String, TermsResponse.Term>(128));
        }
      }
    }

    public void parse(NamedList terms) {
      // exit if there is no terms
      if (terms == null) {
        return;
      }

      TermsResponse termsResponse = new TermsResponse(terms);
      
      // loop though each field and add each term+freq to map
      for (String key : fieldmap.keySet()) {
        HashMap<String, TermsResponse.Term> termmap = fieldmap.get(key);
        List<TermsResponse.Term> termlist = termsResponse.getTerms(key); 

        // skip this field if there are no terms
        if (termlist == null) {
          continue;
        }

        // loop though each term
        for (TermsResponse.Term tc : termlist) {
          String term = tc.getTerm();
          if (termmap.containsKey(term)) {
            TermsResponse.Term oldtc = termmap.get(term);
            oldtc.addFrequency(tc.getFrequency());
            termmap.put(term, oldtc);
          } else {
            termmap.put(term, tc);
          }
        }
      }
    }

    public NamedList buildResponse() {
      NamedList response = new SimpleOrderedMap();

      // determine if we are going index or count sort
      boolean sort = !TermsParams.TERMS_SORT_INDEX.equals(params.get(
          TermsParams.TERMS_SORT, TermsParams.TERMS_SORT_COUNT));

      // init minimum frequency
      long freqmin = 1;
      String s = params.get(TermsParams.TERMS_MINCOUNT);
      if (s != null)  freqmin = Long.parseLong(s);

      // init maximum frequency, default to max int
      long freqmax = -1;
      s = params.get(TermsParams.TERMS_MAXCOUNT);
      if (s != null)  freqmax = Long.parseLong(s);
      if (freqmax < 0) {
        freqmax = Long.MAX_VALUE;
      }

      // init limit, default to max int
      long limit = 10;
      s = params.get(TermsParams.TERMS_LIMIT);
      if (s != null)  limit = Long.parseLong(s);
      if (limit < 0) {
        limit = Long.MAX_VALUE;
      }

      // loop though each field we want terms from
      for (String key : fieldmap.keySet()) {
        NamedList fieldterms = new SimpleOrderedMap();
        TermsResponse.Term[] data = null;
        if (sort) {
          data = getCountSorted(fieldmap.get(key));
        } else {
          data = getLexSorted(fieldmap.get(key));
        }

        // loop though each term until we hit limit
        int cnt = 0;
        for (TermsResponse.Term tc : data) {
          if (tc.getFrequency() >= freqmin && tc.getFrequency() <= freqmax) {
            fieldterms.add(tc.getTerm(), num(tc.getFrequency()));
            cnt++;
          }

          if (cnt >= limit) {
            break;
          }
        }

        response.add(key, fieldterms);
      }

      return response;
    }

    // use <int> tags for smaller facet counts (better back compatibility)
    private Number num(long val) {
      if (val < Integer.MAX_VALUE) return (int) val;
      else return val;
    }

    // based on code from facets
    public TermsResponse.Term[] getLexSorted(HashMap<String, TermsResponse.Term> data) {
      TermsResponse.Term[] arr = data.values().toArray(new TermsResponse.Term[data.size()]);

      Arrays.sort(arr, new Comparator<TermsResponse.Term>() {
        public int compare(TermsResponse.Term o1, TermsResponse.Term o2) {
          return o1.getTerm().compareTo(o2.getTerm());
        }
      });

      return arr;
    }

    // based on code from facets
    public TermsResponse.Term[] getCountSorted(HashMap<String, TermsResponse.Term> data) {
      TermsResponse.Term[] arr = data.values().toArray(new TermsResponse.Term[data.size()]);

      Arrays.sort(arr, new Comparator<TermsResponse.Term>() {
        public int compare(TermsResponse.Term o1, TermsResponse.Term o2) {
          long freq1 = o1.getFrequency();
          long freq2 = o2.getFrequency();
          
          if (freq2 < freq1) {
            return -1;
          } else if (freq1 < freq2) {
            return 1;
          } else {
            return o1.getTerm().compareTo(o2.getTerm());
          }
        }
      });

      return arr;
    }
  }

  public String getVersion() {
    return "$Revision$";
  }

  public String getSourceId() {
    return "$Id$";
  }

  public String getSource() {
    return "$URL$";
  }

  public String getDescription() {
    return "A Component for working with Term Enumerators";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1467.java