error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8806.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8806.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8806.java
text:
```scala
r@@eturn null;

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
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.node.ObjectNode;
import org.elasticsearch.index.analysis.NamedAnalyzer;
import org.elasticsearch.index.analysis.NumericDoubleAnalyzer;
import org.elasticsearch.index.mapper.MapperParsingException;
import org.elasticsearch.util.Numbers;
import org.elasticsearch.util.json.JsonBuilder;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import static org.elasticsearch.index.mapper.json.JsonMapperBuilders.*;
import static org.elasticsearch.index.mapper.json.JsonTypeParsers.*;
import static org.elasticsearch.util.json.JacksonNodes.*;

/**
 * @author kimchy (shay.banon)
 */
public class JsonDoubleFieldMapper extends JsonNumberFieldMapper<Double> {

    public static final String JSON_TYPE = "double";

    public static class Defaults extends JsonNumberFieldMapper.Defaults {
        public static final Double NULL_VALUE = null;
    }

    public static class Builder extends JsonNumberFieldMapper.Builder<Builder, JsonDoubleFieldMapper> {

        protected Double nullValue = Defaults.NULL_VALUE;

        public Builder(String name) {
            super(name);
            builder = this;
        }

        public Builder nullValue(double nullValue) {
            this.nullValue = nullValue;
            return this;
        }

        @Override public JsonDoubleFieldMapper build(BuilderContext context) {
            JsonDoubleFieldMapper fieldMapper = new JsonDoubleFieldMapper(buildNames(context),
                    precisionStep, index, store, boost, omitNorms, omitTermFreqAndPositions, nullValue);
            fieldMapper.includeInAll(includeInAll);
            return fieldMapper;
        }
    }

    public static class TypeParser implements JsonTypeParser {
        @Override public JsonMapper.Builder parse(String name, JsonNode node, ParserContext parserContext) throws MapperParsingException {
            ObjectNode doubleNode = (ObjectNode) node;
            JsonDoubleFieldMapper.Builder builder = doubleField(name);
            parseNumberField(builder, name, doubleNode, parserContext);
            for (Iterator<Map.Entry<String, JsonNode>> propsIt = doubleNode.getFields(); propsIt.hasNext();) {
                Map.Entry<String, JsonNode> entry = propsIt.next();
                String propName = entry.getKey();
                JsonNode propNode = entry.getValue();
                if (propName.equals("nullValue")) {
                    builder.nullValue(nodeDoubleValue(propNode));
                }
            }
            return builder;
        }
    }


    private final Double nullValue;

    private final String nullValueAsString;

    protected JsonDoubleFieldMapper(Names names, int precisionStep,
                                    Field.Index index, Field.Store store,
                                    float boost, boolean omitNorms, boolean omitTermFreqAndPositions,
                                    Double nullValue) {
        super(names, precisionStep, index, store, boost, omitNorms, omitTermFreqAndPositions,
                new NamedAnalyzer("_double/" + precisionStep, new NumericDoubleAnalyzer(precisionStep)),
                new NamedAnalyzer("_double/max", new NumericDoubleAnalyzer(Integer.MAX_VALUE)));
        this.nullValue = nullValue;
        this.nullValueAsString = nullValue == null ? null : nullValue.toString();
    }

    @Override protected int maxPrecisionStep() {
        return 64;
    }

    @Override public Double value(Fieldable field) {
        byte[] value = field.getBinaryValue();
        if (value == null) {
            return Double.NaN;
        }
        return Numbers.bytesToDouble(value);
    }

    @Override public String indexedValue(String value) {
        return indexedValue(Double.parseDouble(value));
    }

    @Override public String indexedValue(Double value) {
        return NumericUtils.doubleToPrefixCoded(value);
    }

    @Override public Object valueFromTerm(String term) {
        final int shift = term.charAt(0) - NumericUtils.SHIFT_START_LONG;
        if (shift > 0 && shift <= 63) {
            return null;
        }
        return NumericUtils.prefixCodedToDouble(term);
    }

    @Override public Object valueFromString(String text) {
        return Double.parseDouble(text);
    }

    @Override public Query rangeQuery(String lowerTerm, String upperTerm, boolean includeLower, boolean includeUpper) {
        return NumericRangeQuery.newDoubleRange(names.indexName(), precisionStep,
                lowerTerm == null ? null : Double.parseDouble(lowerTerm),
                upperTerm == null ? null : Double.parseDouble(upperTerm),
                includeLower, includeUpper);
    }

    @Override public Filter rangeFilter(String lowerTerm, String upperTerm, boolean includeLower, boolean includeUpper) {
        return NumericRangeFilter.newDoubleRange(names.indexName(), precisionStep,
                lowerTerm == null ? null : Double.parseDouble(lowerTerm),
                upperTerm == null ? null : Double.parseDouble(upperTerm),
                includeLower, includeUpper);
    }

    @Override protected Field parseCreateField(JsonParseContext jsonContext) throws IOException {
        double value;
        if (jsonContext.externalValueSet()) {
            Object externalValue = jsonContext.externalValue();
            if (externalValue == null) {
                if (nullValue == null) {
                    return null;
                }
                value = nullValue;
            } else {
                value = ((Number) externalValue).doubleValue();
            }
            if (includeInAll == null || includeInAll) {
                jsonContext.allEntries().addText(names.fullName(), Double.toString(value), boost);
            }
        } else {
            if (jsonContext.jp().getCurrentToken() == JsonToken.VALUE_NULL) {
                if (nullValue == null) {
                    return null;
                }
                value = nullValue;
                if (nullValueAsString != null && (includeInAll == null || includeInAll)) {
                    jsonContext.allEntries().addText(names.fullName(), nullValueAsString, boost);
                }
            } else {
                if (jsonContext.jp().getCurrentToken() == JsonToken.VALUE_STRING) {
                    value = Double.parseDouble(jsonContext.jp().getText());
                } else {
                    value = jsonContext.jp().getDoubleValue();
                }
                if (includeInAll == null || includeInAll) {
                    jsonContext.allEntries().addText(names.fullName(), jsonContext.jp().getText(), boost);
                }
            }
        }

        Field field = null;
        if (stored()) {
            field = new Field(names.indexName(), Numbers.doubleToBytes(value), store);
            if (indexed()) {
                field.setTokenStream(popCachedStream(precisionStep).setDoubleValue(value));
            }
        } else if (indexed()) {
            field = new Field(names.indexName(), popCachedStream(precisionStep).setDoubleValue(value));
        }
        return field;
    }

    @Override public int sortType() {
        return SortField.DOUBLE;
    }

    @Override protected String jsonType() {
        return JSON_TYPE;
    }

    @Override protected void doJsonBody(JsonBuilder builder) throws IOException {
        super.doJsonBody(builder);
        if (nullValue != null) {
            builder.field("nullValue", nullValue);
        }
        if (includeInAll != null) {
            builder.field("includeInAll", includeInAll);
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8806.java