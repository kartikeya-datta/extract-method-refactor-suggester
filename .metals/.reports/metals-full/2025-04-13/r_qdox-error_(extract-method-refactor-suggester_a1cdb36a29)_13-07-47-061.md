error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3709.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3709.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3709.java
text:
```scala
r@@eturn new LZFCompressedStreamOutput(out);

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

package org.elasticsearch.common.compress.lzf;

import com.ning.compress.lzf.ChunkDecoder;
import com.ning.compress.lzf.ChunkEncoder;
import com.ning.compress.lzf.LZFChunk;
import com.ning.compress.lzf.LZFEncoder;
import com.ning.compress.lzf.util.ChunkDecoderFactory;
import com.ning.compress.lzf.util.ChunkEncoderFactory;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.util.Constants;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.compress.CompressedIndexInput;
import org.elasticsearch.common.compress.CompressedStreamInput;
import org.elasticsearch.common.compress.CompressedStreamOutput;
import org.elasticsearch.common.compress.Compressor;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.Settings;
import org.jboss.netty.buffer.ChannelBuffer;

import java.io.IOException;

/**
 */
public class LZFCompressor implements Compressor {

    static final byte[] LUCENE_HEADER = {'L', 'Z', 'F', 0};

    public static final String TYPE = "lzf";

    private ChunkEncoder encoder;

    private ChunkDecoder decoder;

    public LZFCompressor() {
        this.encoder = ChunkEncoderFactory.safeInstance();
        this.decoder = ChunkDecoderFactory.safeInstance();
        Loggers.getLogger(LZFCompressor.class).debug("using encoder [{}] and decoder[{}] ",
                this.encoder.getClass().getSimpleName(),
                this.decoder.getClass().getSimpleName());
    }

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public void configure(Settings settings) {}

    @Override
    public boolean isCompressed(BytesReference bytes) {
        return bytes.length() >= 3 &&
                bytes.get(0) == LZFChunk.BYTE_Z &&
                bytes.get(1) == LZFChunk.BYTE_V &&
                (bytes.get(2) == LZFChunk.BLOCK_TYPE_COMPRESSED || bytes.get(2) == LZFChunk.BLOCK_TYPE_NON_COMPRESSED);
    }

    @Override
    public boolean isCompressed(byte[] data, int offset, int length) {
        return length >= 3 &&
                data[offset] == LZFChunk.BYTE_Z &&
                data[offset + 1] == LZFChunk.BYTE_V &&
                (data[offset + 2] == LZFChunk.BLOCK_TYPE_COMPRESSED || data[offset + 2] == LZFChunk.BLOCK_TYPE_NON_COMPRESSED);
    }

    @Override
    public boolean isCompressed(ChannelBuffer buffer) {
        int offset = buffer.readerIndex();
        return buffer.readableBytes() >= 3 &&
                buffer.getByte(offset) == LZFChunk.BYTE_Z &&
                buffer.getByte(offset + 1) == LZFChunk.BYTE_V &&
                (buffer.getByte(offset + 2) == LZFChunk.BLOCK_TYPE_COMPRESSED || buffer.getByte(offset + 2) == LZFChunk.BLOCK_TYPE_NON_COMPRESSED);
    }

    @Override
    public boolean isCompressed(IndexInput in) throws IOException {
        long currentPointer = in.getFilePointer();
        // since we have some metdata before the first compressed header, we check on our specific header
        if (in.length() - currentPointer < (LUCENE_HEADER.length)) {
            return false;
        }
        for (int i = 0; i < LUCENE_HEADER.length; i++) {
            if (in.readByte() != LUCENE_HEADER[i]) {
                in.seek(currentPointer);
                return false;
            }
        }
        in.seek(currentPointer);
        return true;
    }

    @Override
    public byte[] uncompress(byte[] data, int offset, int length) throws IOException {
        return decoder.decode(data, offset, length);
    }

    @Override
    public byte[] compress(byte[] data, int offset, int length) throws IOException {
        return LZFEncoder.encode(encoder, data, offset, length);
    }

    @Override
    public CompressedStreamInput streamInput(StreamInput in) throws IOException {
        return new LZFCompressedStreamInput(in, decoder);
    }

    @Override
    public CompressedStreamOutput streamOutput(StreamOutput out) throws IOException {
        return new LZFCompressedStreamOutput(out, encoder);
    }

    @Override
    public CompressedIndexInput indexInput(IndexInput in) throws IOException {
        return new LZFCompressedIndexInput(in, decoder);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3709.java