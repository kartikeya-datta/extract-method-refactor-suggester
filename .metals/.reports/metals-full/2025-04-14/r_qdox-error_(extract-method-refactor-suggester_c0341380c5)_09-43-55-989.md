error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4678.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4678.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4678.java
text:
```scala
i@@f (curTerms.hasPayloads() && (currentPayloads[i].length() > 0)) {

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

package org.elasticsearch.action.termvector;

import com.google.common.collect.Iterators;
import org.apache.lucene.index.DocsAndPositionsEnum;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.util.ArrayUtil;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.CharsRef;
import org.apache.lucene.util.UnicodeUtil;
import org.elasticsearch.ElasticSearchIllegalStateException;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.termvector.TermVectorRequest.Flag;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.io.stream.BytesStreamOutput;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentBuilderString;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;

public class TermVectorResponse extends ActionResponse implements ToXContent {

    private static class FieldStrings {
        // term statistics strings
        public static final XContentBuilderString TTF = new XContentBuilderString("ttf");
        public static final XContentBuilderString DOC_FREQ = new XContentBuilderString("doc_freq");
        public static final XContentBuilderString TERM_FREQ = new XContentBuilderString("term_freq");

        // field statistics strings
        public static final XContentBuilderString FIELD_STATISTICS = new XContentBuilderString("field_statistics");
        public static final XContentBuilderString DOC_COUNT = new XContentBuilderString("doc_count");
        public static final XContentBuilderString SUM_DOC_FREQ = new XContentBuilderString("sum_doc_freq");
        public static final XContentBuilderString SUM_TTF = new XContentBuilderString("sum_ttf");

        public static final XContentBuilderString TOKENS = new XContentBuilderString("tokens");
        public static final XContentBuilderString POS = new XContentBuilderString("position");
        public static final XContentBuilderString START_OFFSET = new XContentBuilderString("start_offset");
        public static final XContentBuilderString END_OFFSET = new XContentBuilderString("end_offset");
        public static final XContentBuilderString PAYLOAD = new XContentBuilderString("payload");
        public static final XContentBuilderString _INDEX = new XContentBuilderString("_index");
        public static final XContentBuilderString _TYPE = new XContentBuilderString("_type");
        public static final XContentBuilderString _ID = new XContentBuilderString("_id");
        public static final XContentBuilderString _VERSION = new XContentBuilderString("_version");
        public static final XContentBuilderString EXISTS = new XContentBuilderString("exists");
        public static final XContentBuilderString TERMS = new XContentBuilderString("terms");
        public static final XContentBuilderString TERM_VECTORS = new XContentBuilderString("term_vectors");

    }

    private BytesReference termVectors;
    private BytesReference headerRef;
    private String index;
    private String type;
    private String id;
    private long docVersion;
    private boolean exists = false;

    private boolean sourceCopied = false;

    int[] curentPositions = new int[0];
    int[] currentStartOffset = new int[0];
    int[] currentEndOffset = new int[0];
    BytesReference[] currentPayloads = new BytesReference[0];

    public TermVectorResponse(String index, String type, String id) {
        this.index = index;
        this.type = type;
        this.id = id;
    }

    TermVectorResponse() {
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeString(index);
        out.writeString(type);
        out.writeString(id);
        out.writeVLong(docVersion);
        final boolean docExists = isExists();
        out.writeBoolean(docExists);
        out.writeBoolean(hasTermVectors());
        if (hasTermVectors()) {
            out.writeBytesReference(headerRef);
            out.writeBytesReference(termVectors);
        }
    }

    private boolean hasTermVectors() {
        assert (headerRef == null && termVectors == null) || (headerRef != null && termVectors != null);
        return headerRef != null;
    }

    @Override
    public void readFrom(StreamInput in) throws IOException {
        index = in.readString();
        type = in.readString();
        id = in.readString();
        docVersion = in.readVLong();
        exists = in.readBoolean();
        if (in.readBoolean()) {
            headerRef = in.readBytesReference();
            termVectors = in.readBytesReference();
        }
    }

    public Fields getFields() throws IOException {
        if (hasTermVectors() && isExists()) {
            if (!sourceCopied) { // make the bytes safe
                headerRef = headerRef.copyBytesArray();
                termVectors = termVectors.copyBytesArray();
            }
            return new TermVectorFields(headerRef, termVectors);
        } else {
            return new Fields() {
                @Override
                public Iterator<String> iterator() {
                    return Iterators.emptyIterator();
                }

                @Override
                public Terms terms(String field) throws IOException {
                    return null;
                }

                @Override
                public int size() {
                    return 0;
                }
            };
        }

    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        assert index != null;
        assert type != null;
        assert id != null;
        builder.startObject();
        builder.field(FieldStrings._INDEX, index);
        builder.field(FieldStrings._TYPE, type);
        builder.field(FieldStrings._ID, id);
        builder.field(FieldStrings._VERSION, docVersion);
        builder.field(FieldStrings.EXISTS, isExists());
        if (!isExists()) {
            builder.endObject();
            return builder;
        }
        builder.startObject(FieldStrings.TERM_VECTORS);
        final CharsRef spare = new CharsRef();
        Fields theFields = getFields();
        Iterator<String> fieldIter = theFields.iterator();
        while (fieldIter.hasNext()) {
            buildField(builder, spare, theFields, fieldIter);
        }
        builder.endObject();
        builder.endObject();
        return builder;

    }

    private void buildField(XContentBuilder builder, final CharsRef spare, Fields theFields, Iterator<String> fieldIter) throws IOException {
        String fieldName = fieldIter.next();
        builder.startObject(fieldName);
        Terms curTerms = theFields.terms(fieldName);
        // write field statistics
        buildFieldStatistics(builder, curTerms);
        builder.startObject(FieldStrings.TERMS);
        TermsEnum termIter = curTerms.iterator(null);
        for (int i = 0; i < curTerms.size(); i++) {
            buildTerm(builder, spare, curTerms, termIter);
        }
        builder.endObject();
        builder.endObject();
    }

    private void buildTerm(XContentBuilder builder, final CharsRef spare, Terms curTerms, TermsEnum termIter) throws IOException {
        // start term, optimized writing
        BytesRef term = termIter.next();
        UnicodeUtil.UTF8toUTF16(term, spare);
        builder.startObject(spare.toString());
        buildTermStatistics(builder, termIter);
        // finally write the term vectors
        DocsAndPositionsEnum posEnum = termIter.docsAndPositions(null, null);
        int termFreq = posEnum.freq();
        builder.field(FieldStrings.TERM_FREQ, termFreq);
        initMemory(curTerms, termFreq);
        initValues(curTerms, posEnum, termFreq);
        buildValues(builder, curTerms, termFreq);
        builder.endObject();
    }

    private void buildTermStatistics(XContentBuilder builder, TermsEnum termIter) throws IOException {
        // write term statistics. At this point we do not naturally have a
        // boolean that says if these values actually were requested.
        // However, we can assume that they were not if the statistic values are
        // <= 0.
        assert (((termIter.docFreq() > 0) && (termIter.totalTermFreq() > 0)) || ((termIter.docFreq() == -1) && (termIter.totalTermFreq() == -1)));
        int docFreq = termIter.docFreq();
        if (docFreq > 0) {
            builder.field(FieldStrings.DOC_FREQ, docFreq);
            builder.field(FieldStrings.TTF, termIter.totalTermFreq());
        }
    }

    private void buildValues(XContentBuilder builder, Terms curTerms, int termFreq) throws IOException {
        if (!(curTerms.hasPayloads() || curTerms.hasOffsets() || curTerms.hasPositions())) {
            return;
        }

        builder.startArray(FieldStrings.TOKENS);
        for (int i = 0; i < termFreq; i++) {
            builder.startObject();
            if (curTerms.hasPositions()) {
                builder.field(FieldStrings.POS, curentPositions[i]);
            }
            if (curTerms.hasOffsets()) {
                builder.field(FieldStrings.START_OFFSET, currentStartOffset[i]);
                builder.field(FieldStrings.END_OFFSET, currentEndOffset[i]);
            }
            if (curTerms.hasPayloads() && (currentPayloads[i] != null)) {
                builder.field(FieldStrings.PAYLOAD, currentPayloads[i]);
            }
            builder.endObject();
        }
        builder.endArray();

    }

    private void initValues(Terms curTerms, DocsAndPositionsEnum posEnum, int termFreq) throws IOException {
        for (int j = 0; j < termFreq; j++) {
            int nextPos = posEnum.nextPosition();
            if (curTerms.hasPositions()) {
                curentPositions[j] = nextPos;
            }
            if (curTerms.hasOffsets()) {
                currentStartOffset[j] = posEnum.startOffset();
                currentEndOffset[j] = posEnum.endOffset();
            }
            if (curTerms.hasPayloads()) {
                BytesRef curPayload = posEnum.getPayload();
                if (curPayload != null) {
                    currentPayloads[j] = new BytesArray(curPayload.bytes, 0, curPayload.length);
                } else {
                    currentPayloads[j] = null;
                }

            }
        }
    }

    private void initMemory(Terms curTerms, int termFreq) {
        // init memory for performance reasons
        if (curTerms.hasPositions()) {
            curentPositions = ArrayUtil.grow(curentPositions, termFreq);
        }
        if (curTerms.hasOffsets()) {
            currentStartOffset = ArrayUtil.grow(currentStartOffset, termFreq);
            currentEndOffset = ArrayUtil.grow(currentEndOffset, termFreq);
        }
        if (curTerms.hasPayloads()) {
            currentPayloads = new BytesArray[termFreq];
        }
    }

    private void buildFieldStatistics(XContentBuilder builder, Terms curTerms) throws IOException {
        long sumDocFreq = curTerms.getSumDocFreq();
        int docCount = curTerms.getDocCount();
        long sumTotalTermFrequencies = curTerms.getSumTotalTermFreq();
        if (docCount > 0) {
            assert ((sumDocFreq > 0)) : "docCount >= 0 but sumDocFreq ain't!";
            assert ((sumTotalTermFrequencies > 0)) : "docCount >= 0 but sumTotalTermFrequencies ain't!";
            builder.startObject(FieldStrings.FIELD_STATISTICS);
            builder.field(FieldStrings.SUM_DOC_FREQ, sumDocFreq);
            builder.field(FieldStrings.DOC_COUNT, docCount);
            builder.field(FieldStrings.SUM_TTF, sumTotalTermFrequencies);
            builder.endObject();
        } else if (docCount == -1) { // this should only be -1 if the field
            // statistics were not requested at all. In
            // this case all 3 values should be -1
            assert ((sumDocFreq == -1)) : "docCount was -1 but sumDocFreq ain't!";
            assert ((sumTotalTermFrequencies == -1)) : "docCount was -1 but sumTotalTermFrequencies ain't!";
        } else {
            throw new ElasticSearchIllegalStateException(
                    "Something is wrong with the field statistics of the term vector request: Values are " + "\n"
                            + FieldStrings.SUM_DOC_FREQ + " " + sumDocFreq + "\n" + FieldStrings.DOC_COUNT + " " + docCount + "\n"
                            + FieldStrings.SUM_TTF + " " + sumTotalTermFrequencies);
        }
    }

    public boolean isExists() {
        return exists;
    }
    
    public void setExists(boolean exists) {
         this.exists = exists;
    }

    public void setFields(Fields termVectorsByField, Set<String> selectedFields, EnumSet<Flag> flags, Fields topLevelFields) throws IOException {
        TermVectorWriter tvw = new TermVectorWriter(this);

        if (termVectorsByField != null) {
            tvw.setFields(termVectorsByField, selectedFields, flags, topLevelFields);
        }

    }

    public void setTermVectorField(BytesStreamOutput output) {
        termVectors = output.bytes();
    }

    public void setHeader(BytesReference header) {
        headerRef = header;

    }

    public void setDocVersion(long version) {
        this.docVersion = version;

    }

    public String getIndex() {
        return index;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4678.java