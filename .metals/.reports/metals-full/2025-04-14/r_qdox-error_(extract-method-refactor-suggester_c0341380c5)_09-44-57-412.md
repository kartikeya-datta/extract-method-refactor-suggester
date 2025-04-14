error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7224.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7224.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7224.java
text:
```scala
final L@@ist<String> list = new ArrayList<>();

/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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

package org.elasticsearch.action.search;

import com.google.common.collect.Lists;
import org.elasticsearch.ElasticsearchIllegalArgumentException;
import org.elasticsearch.ElasticsearchParseException;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionRequestValidationException;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.xcontent.XContent;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.action.ValidateActions.addValidationError;

/**
 * A multi search API request.
 */
public class MultiSearchRequest extends ActionRequest<MultiSearchRequest> {

    private List<SearchRequest> requests = Lists.newArrayList();

    private IndicesOptions indicesOptions = IndicesOptions.strict();

    /**
     * Add a search request to execute. Note, the order is important, the search response will be returned in the
     * same order as the search requests.
     */
    public MultiSearchRequest add(SearchRequestBuilder request) {
        requests.add(request.request());
        return this;
    }

    /**
     * Add a search request to execute. Note, the order is important, the search response will be returned in the
     * same order as the search requests.
     */
    public MultiSearchRequest add(SearchRequest request) {
        requests.add(request);
        return this;
    }

    public MultiSearchRequest add(byte[] data, int from, int length, boolean contentUnsafe,
                                  @Nullable String[] indices, @Nullable String[] types, @Nullable String searchType) throws Exception {
        return add(new BytesArray(data, from, length), contentUnsafe, indices, types, searchType, null, IndicesOptions.strict(), true);
    }

    public MultiSearchRequest add(BytesReference data, boolean contentUnsafe, @Nullable String[] indices, @Nullable String[] types, @Nullable String searchType, IndicesOptions indicesOptions) throws Exception {
        return add(data, contentUnsafe, indices, types, searchType, null, indicesOptions, true);
    }

    public MultiSearchRequest add(BytesReference data, boolean contentUnsafe, @Nullable String[] indices, @Nullable String[] types, @Nullable String searchType, @Nullable String routing, IndicesOptions indicesOptions, boolean allowExplicitIndex) throws Exception {
        XContent xContent = XContentFactory.xContent(data);
        int from = 0;
        int length = data.length();
        byte marker = xContent.streamSeparator();
        while (true) {
            int nextMarker = findNextMarker(marker, from, data, length);
            if (nextMarker == -1) {
                break;
            }
            // support first line with \n
            if (nextMarker == 0) {
                from = nextMarker + 1;
                continue;
            }

            SearchRequest searchRequest = new SearchRequest();
            if (indices != null) {
                searchRequest.indices(indices);
            }
            if (indicesOptions != null) {
                searchRequest.indicesOptions(indicesOptions);
            }
            if (types != null && types.length > 0) {
                searchRequest.types(types);
            }
            if (routing != null) {
                searchRequest.routing(routing);
            }
            searchRequest.searchType(searchType);

            boolean ignoreUnavailable = IndicesOptions.strict().ignoreUnavailable();
            boolean allowNoIndices = IndicesOptions.strict().allowNoIndices();
            boolean expandWildcardsOpen = IndicesOptions.strict().expandWildcardsOpen();
            boolean expandWildcardsClosed = IndicesOptions.strict().expandWildcardsClosed();

            // now parse the action
            if (nextMarker - from > 0) {
                XContentParser parser = xContent.createParser(data.slice(from, nextMarker - from));
                try {
                    // Move to START_OBJECT, if token is null, its an empty data
                    XContentParser.Token token = parser.nextToken();
                    if (token != null) {
                        assert token == XContentParser.Token.START_OBJECT;
                        String currentFieldName = null;
                        while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
                            if (token == XContentParser.Token.FIELD_NAME) {
                                currentFieldName = parser.currentName();
                            } else if (token.isValue()) {
                                if ("index".equals(currentFieldName) || "indices".equals(currentFieldName)) {
                                    if (!allowExplicitIndex) {
                                        throw new ElasticsearchIllegalArgumentException("explicit index in multi search is not allowed");
                                    }
                                    searchRequest.indices(Strings.splitStringByCommaToArray(parser.text()));
                                } else if ("type".equals(currentFieldName) || "types".equals(currentFieldName)) {
                                    searchRequest.types(Strings.splitStringByCommaToArray(parser.text()));
                                } else if ("search_type".equals(currentFieldName) || "searchType".equals(currentFieldName)) {
                                    searchRequest.searchType(parser.text());
                                } else if ("preference".equals(currentFieldName)) {
                                    searchRequest.preference(parser.text());
                                } else if ("routing".equals(currentFieldName)) {
                                    searchRequest.routing(parser.text());
                                } else if ("ignore_unavailable".equals(currentFieldName) || "ignoreUnavailable".equals(currentFieldName)) {
                                    ignoreUnavailable = parser.booleanValue();
                                } else if ("allow_no_indices".equals(currentFieldName) || "allowNoIndices".equals(currentFieldName)) {
                                    allowNoIndices = parser.booleanValue();
                                } else if ("expand_wildcards".equals(currentFieldName) || "expandWildcards".equals(currentFieldName)) {
                                    String[] wildcards = Strings.splitStringByCommaToArray(parser.text());
                                    for (String wildcard : wildcards) {
                                        if ("open".equals(wildcard)) {
                                            expandWildcardsOpen = true;
                                        } else if ("closed".equals(wildcard)) {
                                            expandWildcardsClosed = true;
                                        } else {
                                            throw new ElasticsearchIllegalArgumentException("No valid expand wildcard value [" + wildcard + "]");
                                        }
                                    }
                                }
                            } else if (token == XContentParser.Token.START_ARRAY) {
                                if ("index".equals(currentFieldName) || "indices".equals(currentFieldName)) {
                                    if (!allowExplicitIndex) {
                                        throw new ElasticsearchIllegalArgumentException("explicit index in multi search is not allowed");
                                    }
                                    searchRequest.indices(parseArray(parser));
                                } else if ("type".equals(currentFieldName) || "types".equals(currentFieldName)) {
                                    searchRequest.types(parseArray(parser));
                                } else if ("expand_wildcards".equals(currentFieldName) || "expandWildcards".equals(currentFieldName)) {
                                    String[] wildcards = parseArray(parser);
                                    for (String wildcard : wildcards) {
                                        if ("open".equals(wildcard)) {
                                            expandWildcardsOpen = true;
                                        } else if ("closed".equals(wildcard)) {
                                            expandWildcardsClosed = true;
                                        } else {
                                            throw new ElasticsearchIllegalArgumentException("No valid expand wildcard value [" + wildcard + "]");
                                        }
                                    }
                                } else {
                                    throw new ElasticsearchParseException(currentFieldName + " doesn't support arrays");
                                }
                            }
                        }
                    }
                } finally {
                    parser.close();
                }
            }
            searchRequest.indicesOptions(IndicesOptions.fromOptions(ignoreUnavailable, allowNoIndices, expandWildcardsOpen, expandWildcardsClosed));

            // move pointers
            from = nextMarker + 1;
            // now for the body
            nextMarker = findNextMarker(marker, from, data, length);
            if (nextMarker == -1) {
                break;
            }

            searchRequest.source(data.slice(from, nextMarker - from), contentUnsafe);
            // move pointers
            from = nextMarker + 1;

            add(searchRequest);
        }

        return this;
    }

    private String[] parseArray(XContentParser parser) throws IOException {
        final List<String> list = new ArrayList<String>();
        assert parser.currentToken() == XContentParser.Token.START_ARRAY;
        while (parser.nextToken() != XContentParser.Token.END_ARRAY) {
            list.add(parser.text());
        }
        return list.toArray(new String[list.size()]);
    }

    private int findNextMarker(byte marker, int from, BytesReference data, int length) {
        for (int i = from; i < length; i++) {
            if (data.get(i) == marker) {
                return i;
            }
        }
        return -1;
    }

    public List<SearchRequest> requests() {
        return this.requests;
    }

    @Override
    public ActionRequestValidationException validate() {
        ActionRequestValidationException validationException = null;
        if (requests.isEmpty()) {
            validationException = addValidationError("no requests added", validationException);
        }
        for (int i = 0; i < requests.size(); i++) {
            ActionRequestValidationException ex = requests.get(i).validate();
            if (ex != null) {
                if (validationException == null) {
                    validationException = new ActionRequestValidationException();
                }
                validationException.addValidationErrors(ex.validationErrors());
            }
        }

        return validationException;
    }

    public IndicesOptions indicesOptions() {
        return indicesOptions;
    }

    public MultiSearchRequest indicesOptions(IndicesOptions indicesOptions) {
        this.indicesOptions = indicesOptions;
        return this;
    }

    @Override
    public void readFrom(StreamInput in) throws IOException {
        super.readFrom(in);
        int size = in.readVInt();
        for (int i = 0; i < size; i++) {
            SearchRequest request = new SearchRequest();
            request.readFrom(in);
            requests.add(request);
        }
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        super.writeTo(out);
        out.writeVInt(requests.size());
        for (SearchRequest request : requests) {
            request.writeTo(out);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7224.java