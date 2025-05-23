error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1443.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1443.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1443.java
text:
```scala
b@@uilder.field("null_value", nullValue);

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

package org.elasticsearch.index.mapper.json;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.search.*;
import org.apache.lucene.util.NumericUtils;
import org.codehaus.jackson.JsonToken;
import org.elasticsearch.index.analysis.NamedAnalyzer;
import org.elasticsearch.index.analysis.NumericFloatAnalyzer;
import org.elasticsearch.index.mapper.BoostFieldMapper;
import org.elasticsearch.index.mapper.MergeMappingException;
import org.elasticsearch.util.Numbers;
import org.elasticsearch.util.json.JsonBuilder;

import java.io.IOException;

/**
 * @author kimchy (shay.banon)
 */
public class JsonBoostFieldMapper extends JsonNumberFieldMapper<Float> implements BoostFieldMapper {

    public static final String JSON_TYPE = "_boost";

    public static class Defaults extends JsonNumberFieldMapper.Defaults {
        public static final String NAME = "_boost";
        public static final Float NULL_VALUE = null;
        public static final Field.Index INDEX = Field.Index.NO;
        public static final Field.Store STORE = Field.Store.NO;
    }

    public static class Builder extends JsonNumberFieldMapper.Builder<Builder, JsonBoostFieldMapper> {

        protected Float nullValue = Defaults.NULL_VALUE;

        public Builder(String name) {
            super(name);
            builder = this;
            index = Defaults.INDEX;
            store = Defaults.STORE;
        }

        public Builder nullValue(float nullValue) {
            this.nullValue = nullValue;
            return this;
        }

        @Override public JsonBoostFieldMapper build(BuilderContext context) {
            return new JsonBoostFieldMapper(name, buildIndexName(context),
                    precisionStep, index, store, boost, omitNorms, omitTermFreqAndPositions, nullValue);
        }
    }


    private final Float nullValue;

    protected JsonBoostFieldMapper() {
        this(Defaults.NAME, Defaults.NAME);
    }

    protected JsonBoostFieldMapper(String name, String indexName) {
        this(name, indexName, Defaults.PRECISION_STEP, Defaults.INDEX, Defaults.STORE,
                Defaults.BOOST, Defaults.OMIT_NORMS, Defaults.OMIT_TERM_FREQ_AND_POSITIONS, Defaults.NULL_VALUE);
    }

    protected JsonBoostFieldMapper(String name, String indexName, int precisionStep, Field.Index index, Field.Store store,
                                   float boost, boolean omitNorms, boolean omitTermFreqAndPositions,
                                   Float nullValue) {
        super(new Names(name, indexName, indexName, name), precisionStep, index, store, boost, omitNorms, omitTermFreqAndPositions,
                new NamedAnalyzer("_float/" + precisionStep, new NumericFloatAnalyzer(precisionStep)),
                new NamedAnalyzer("_float/max", new NumericFloatAnalyzer(Integer.MAX_VALUE)));
        this.nullValue = nullValue;
    }

    @Override protected int maxPrecisionStep() {
        return 32;
    }

    @Override public Float value(Fieldable field) {
        byte[] value = field.getBinaryValue();
        if (value == null) {
            return null;
        }
        return Numbers.bytesToFloat(value);
    }

    @Override public String indexedValue(String value) {
        return indexedValue(Float.parseFloat(value));
    }

    @Override public String indexedValue(Float value) {
        return NumericUtils.floatToPrefixCoded(value);
    }

    @Override public Object valueFromTerm(String term) {
        final int shift = term.charAt(0) - NumericUtils.SHIFT_START_INT;
        if (shift > 0 && shift <= 31) {
            return null;
        }
        return NumericUtils.prefixCodedToFloat(term);
    }

    @Override public Object valueFromString(String text) {
        return Float.parseFloat(text);
    }

    @Override public Query rangeQuery(String lowerTerm, String upperTerm, boolean includeLower, boolean includeUpper) {
        return NumericRangeQuery.newFloatRange(names.indexName(), precisionStep,
                lowerTerm == null ? null : Float.parseFloat(lowerTerm),
                upperTerm == null ? null : Float.parseFloat(upperTerm),
                includeLower, includeUpper);
    }

    @Override public Filter rangeFilter(String lowerTerm, String upperTerm, boolean includeLower, boolean includeUpper) {
        return NumericRangeFilter.newFloatRange(names.indexName(), precisionStep,
                lowerTerm == null ? null : Float.parseFloat(lowerTerm),
                upperTerm == null ? null : Float.parseFloat(upperTerm),
                includeLower, includeUpper);
    }

    @Override public void parse(JsonParseContext jsonContext) throws IOException {
        // we override parse since we want to handle cases where it is not indexed and not stored (the default)
        float value = parseFloatValue(jsonContext);
        if (!Float.isNaN(value)) {
            jsonContext.doc().setBoost(value);
        }
        super.parse(jsonContext);
    }

    @Override protected Field parseCreateField(JsonParseContext jsonContext) throws IOException {
        float value = parseFloatValue(jsonContext);
        if (Float.isNaN(value)) {
            return null;
        }
        jsonContext.doc().setBoost(value);
        Field field = null;
        if (stored()) {
            field = new Field(names.indexName(), Numbers.floatToBytes(value), store);
            if (indexed()) {
                field.setTokenStream(popCachedStream(precisionStep).setFloatValue(value));
            }
        } else if (indexed()) {
            field = new Field(names.indexName(), popCachedStream(precisionStep).setFloatValue(value));
        }
        return field;
    }

    private float parseFloatValue(JsonParseContext jsonContext) throws IOException {
        float value;
        if (jsonContext.jp().getCurrentToken() == JsonToken.VALUE_NULL) {
            if (nullValue == null) {
                return Float.NaN;
            }
            value = nullValue;
        } else {
            if (jsonContext.jp().getCurrentToken() == JsonToken.VALUE_STRING) {
                value = Float.parseFloat(jsonContext.jp().getText());
            } else {
                value = jsonContext.jp().getFloatValue();
            }
        }
        return value;
    }

    @Override public int sortType() {
        return SortField.FLOAT;
    }

    @Override protected String jsonType() {
        return JSON_TYPE;
    }

    @Override public void toJson(JsonBuilder builder, Params params) throws IOException {
        builder.startObject(jsonType());
        builder.field("name", name());
        if (nullValue != null) {
            builder.field("nullValue", nullValue);
        }
        builder.endObject();
    }

    @Override public void merge(JsonMapper mergeWith, JsonMergeContext mergeContext) throws MergeMappingException {
        // do nothing here, no merging, but also no exception
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1443.java