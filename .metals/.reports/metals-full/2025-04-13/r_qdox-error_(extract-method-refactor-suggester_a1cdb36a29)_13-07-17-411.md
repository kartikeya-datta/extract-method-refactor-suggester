error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3870.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3870.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3870.java
text:
```scala
o@@ut.writeBoolean(true);

/*
 * Licensed to Elastic Search and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Elastic Search licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.action.admin.indices.analyze;

import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.io.stream.Streamable;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author kimchy (shay.banon)
 */
public class AnalyzeResponse implements ActionResponse, Iterable<AnalyzeResponse.AnalyzeToken>, ToXContent {

    public static class AnalyzeToken implements Streamable {
        private String term;
        private int startOffset;
        private int endOffset;
        private int position;
        private String type;

        AnalyzeToken() {
        }

        public AnalyzeToken(String term, int position, int startOffset, int endOffset, String type) {
            this.term = term;
            this.position = position;
            this.startOffset = startOffset;
            this.endOffset = endOffset;
            this.type = type;
        }

        public String term() {
            return this.term;
        }

        public String getTerm() {
            return term();
        }

        public int startOffset() {
            return this.startOffset;
        }

        public int getStartOffset() {
            return startOffset();
        }

        public int endOffset() {
            return this.endOffset;
        }

        public int getEndOffset() {
            return endOffset();
        }

        public int position() {
            return this.position;
        }

        public int getPosition() {
            return position();
        }

        public String type() {
            return this.type;
        }

        public String getType() {
            return this.type;
        }

        public static AnalyzeToken readAnalyzeToken(StreamInput in) throws IOException {
            AnalyzeToken analyzeToken = new AnalyzeToken();
            analyzeToken.readFrom(in);
            return analyzeToken;
        }

        @Override public void readFrom(StreamInput in) throws IOException {
            term = in.readUTF();
            startOffset = in.readInt();
            endOffset = in.readInt();
            position = in.readVInt();
            if (in.readBoolean()) {
                type = in.readUTF();
            }
        }

        @Override public void writeTo(StreamOutput out) throws IOException {
            out.writeUTF(term);
            out.writeInt(startOffset);
            out.writeInt(endOffset);
            out.writeVInt(position);
            if (type == null) {
                out.writeBoolean(false);
            } else {
                out.writeBoolean(false);
                out.writeUTF(type);
            }
        }
    }

    private List<AnalyzeToken> tokens;

    AnalyzeResponse() {
    }

    public AnalyzeResponse(List<AnalyzeToken> tokens) {
        this.tokens = tokens;
    }

    public List<AnalyzeToken> tokens() {
        return this.tokens;
    }

    public List<AnalyzeToken> getTokens() {
        return tokens();
    }

    @Override public Iterator<AnalyzeToken> iterator() {
        return tokens.iterator();
    }

    @Override public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        String format = params.param("format", "detailed");
        if ("detailed".equals(format)) {
            builder.startArray("tokens");
            for (AnalyzeToken token : tokens) {
                builder.startObject();
                builder.field("token", token.term());
                builder.field("start_offset", token.startOffset());
                builder.field("end_offset", token.endOffset());
                builder.field("type", token.type());
                builder.field("position", token.position());
                builder.endObject();
            }
            builder.endArray();
        } else if ("text".equals(format)) {
            StringBuilder sb = new StringBuilder();
            int lastPosition = 0;
            for (AnalyzeToken token : tokens) {
                if (lastPosition != token.position()) {
                    if (lastPosition != 0) {
                        sb.append("\n").append(token.position()).append(": \n");
                    }
                    lastPosition = token.position();
                }
                sb.append('[')
                        .append(token.term()).append(":")
                        .append(token.startOffset()).append("->").append(token.endOffset()).append(":")
                        .append(token.type())
                        .append("]\n");
            }
            builder.field("tokens", sb);
        }
        return builder;
    }

    @Override public void readFrom(StreamInput in) throws IOException {
        int size = in.readVInt();
        tokens = new ArrayList<AnalyzeToken>(size);
        for (int i = 0; i < size; i++) {
            tokens.add(AnalyzeToken.readAnalyzeToken(in));
        }
    }

    @Override public void writeTo(StreamOutput out) throws IOException {
        out.writeVInt(tokens.size());
        for (AnalyzeToken token : tokens) {
            token.writeTo(out);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3870.java