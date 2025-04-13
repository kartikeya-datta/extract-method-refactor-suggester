error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10430.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10430.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10430.java
text:
```scala
f@@ieldName = fieldMappers.mappers().get(0).names().indexName();

/*
 * Licensed to Elastic Search and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Elastic Search licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.search.query;

import com.google.common.collect.Lists;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.elasticsearch.index.mapper.FieldMappers;
import org.elasticsearch.search.SearchParseElement;
import org.elasticsearch.search.SearchParseException;
import org.elasticsearch.search.internal.SearchContext;
import org.elasticsearch.util.gnu.trove.TObjectIntHashMap;
import org.elasticsearch.util.trove.ExtTObjectIntHasMap;

import java.io.IOException;
import java.util.List;

/**
 * @author kimchy (Shay Banon)
 */
public class SortParseElement implements SearchParseElement {

    private final TObjectIntHashMap<String> sortFieldTypesMapper = new ExtTObjectIntHasMap<String>().defaultReturnValue(-1);

    private static final SortField SORT_SCORE = new SortField(null, SortField.SCORE);
    private static final SortField SORT_SCORE_REVERSE = new SortField(null, SortField.SCORE, true);
    private static final SortField SORT_DOC = new SortField(null, SortField.DOC);
    private static final SortField SORT_DOC_REVERSE = new SortField(null, SortField.DOC, true);

    public SortParseElement() {
        sortFieldTypesMapper.put("string", SortField.STRING);
        sortFieldTypesMapper.put("int", SortField.INT);
        sortFieldTypesMapper.put("float", SortField.FLOAT);
        sortFieldTypesMapper.put("long", SortField.LONG);
        sortFieldTypesMapper.put("double", SortField.DOUBLE);
        sortFieldTypesMapper.put("short", SortField.SHORT);
        sortFieldTypesMapper.put("byte", SortField.BYTE);
        sortFieldTypesMapper.put("string_val", SortField.STRING_VAL);
    }

    @Override public void parse(JsonParser jp, SearchContext context) throws Exception {
        JsonToken token = jp.getCurrentToken();
        List<SortField> sortFields = Lists.newArrayListWithCapacity(2);
        if (token == JsonToken.START_ARRAY) {
            while ((token = jp.nextToken()) != JsonToken.END_ARRAY) {
                if (token == JsonToken.START_OBJECT) {
                    addCompoundSortField(jp, context, sortFields);
                } else if (token == JsonToken.VALUE_STRING) {
                    addSortField(context, sortFields, jp.getText(), false, -1);
                }
            }
        } else {
            addCompoundSortField(jp, context, sortFields);
        }
        if (!sortFields.isEmpty()) {
            context.sort(new Sort(sortFields.toArray(new SortField[sortFields.size()])));
        }
    }

    private void addCompoundSortField(JsonParser jp, SearchContext context, List<SortField> sortFields) throws IOException {
        JsonToken token;
        while ((token = jp.nextToken()) != JsonToken.END_OBJECT) {
            if (token == JsonToken.FIELD_NAME) {
                String fieldName = jp.getCurrentName();
                boolean reverse = false;
                String innerJsonName = null;
                int type = -1;
                while ((token = jp.nextToken()) != JsonToken.END_OBJECT) {
                    if (token == JsonToken.FIELD_NAME) {
                        innerJsonName = jp.getCurrentName();
                    } else if (token == JsonToken.VALUE_NUMBER_INT) {
                        if ("reverse".equals(innerJsonName)) {
                            reverse = jp.getIntValue() != 0;
                        }
                    } else if (token == JsonToken.VALUE_TRUE) {
                        if ("reverse".equals(innerJsonName)) {
                            reverse = true;
                        }
                    } else {
                        if ("type".equals(innerJsonName)) {
                            type = sortFieldTypesMapper.get(jp.getText());
                            if (type == -1) {
                                throw new SearchParseException(context, "No sort type for [" + jp.getText() + "] with field [" + fieldName + "]");
                            }
                        }
                    }
                }
                addSortField(context, sortFields, fieldName, reverse, type);
            }
        }
    }

    private void addSortField(SearchContext context, List<SortField> sortFields, String fieldName, boolean reverse, int type) {
        if ("score".equals(fieldName)) {
            if (reverse) {
                sortFields.add(SORT_SCORE_REVERSE);
            } else {
                sortFields.add(SORT_SCORE);
            }
        } else if ("doc".equals(fieldName)) {
            if (reverse) {
                sortFields.add(SORT_DOC_REVERSE);
            } else {
                sortFields.add(SORT_DOC);
            }
        } else {
            FieldMappers fieldMappers = context.mapperService().smartNameFieldMappers(fieldName);
            if (fieldMappers == null || fieldMappers.mappers().isEmpty()) {
                if (type == -1) {
                    throw new SearchParseException(context, "No built in mapping found for [" + fieldName + "], and no explicit type defined");
                }
            } else {
                fieldName = fieldMappers.mappers().get(0).indexName();
                if (type == -1) {
                    type = fieldMappers.mappers().get(0).sortType();
                }
            }
            sortFields.add(new SortField(fieldName, type, reverse));
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10430.java