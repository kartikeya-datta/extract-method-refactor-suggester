error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1285.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1285.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1285.java
text:
```scala
r@@eturn reader.read(ob, dec);

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.cassandra.io;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collections;

import org.apache.avro.Schema;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.ipc.ByteBufferInputStream;
import org.apache.avro.generic.GenericArray;
import org.apache.avro.generic.GenericData;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecord;
import org.apache.avro.util.Utf8;

import org.apache.cassandra.io.util.OutputBuffer;

/**
 * Static serialization/deserialization utility functions, intended to eventually replace ICompactSerializers.
 */
public final class SerDeUtils
{
    // unbuffered decoders
    private final static DecoderFactory DIRECT_DECODERS = new DecoderFactory().configureDirectDecoder(true);

    public static byte[] copy(ByteBuffer buff)
    {
        byte[] bytes = new byte[buff.remaining()];
        buff.get(bytes);
        buff.rewind();
        return bytes;
    }

	/**
     * Deserializes a single object based on the given Schema.
     * @param writer writer's schema
     * @param bytes Array to deserialize from
     * @param ob An empty object to deserialize into (must not be null).
     * @throws IOException
     */
    public static <T extends SpecificRecord> T deserialize(Schema writer, ByteBuffer bytes, T ob) throws IOException
    {
        BinaryDecoder dec = DIRECT_DECODERS.createBinaryDecoder(bytes.array(),bytes.position()+bytes.arrayOffset(),bytes.remaining(), null);
        SpecificDatumReader<T> reader = new SpecificDatumReader<T>(writer);
        reader.setExpected(ob.getSchema());
        return reader.read(ob, dec);
    }

	/**
     * Serializes a single object.
     * @param o Object to serialize
     */
    public static <T extends SpecificRecord> ByteBuffer serialize(T o) throws IOException
    {
        OutputBuffer buff = new OutputBuffer();
        BinaryEncoder enc = new BinaryEncoder(buff);
        SpecificDatumWriter<T> writer = new SpecificDatumWriter<T>(o.getSchema());
        writer.write(o, enc);
        enc.flush();
        return ByteBuffer.wrap(buff.asByteArray());
    }

	/**
     * Deserializes a single object as stored along with its Schema by serialize(T). NB: See warnings on serialize(T).
     * @param ob An empty object to deserialize into (must not be null).
     * @param bytes Array to deserialize from
     * @throws IOException
     */
    public static <T extends SpecificRecord> T deserializeWithSchema(ByteBuffer bytes, T ob) throws IOException
    {
        BinaryDecoder dec = DIRECT_DECODERS.createBinaryDecoder(bytes.array(),bytes.position()+bytes.arrayOffset(), bytes.remaining(), null);
        Schema writer = Schema.parse(dec.readString(new Utf8()).toString());
        SpecificDatumReader<T> reader = new SpecificDatumReader<T>(writer);
        reader.setExpected(ob.getSchema());
        return new SpecificDatumReader<T>(writer).read(ob, dec);
    }

	/**
     * Serializes a single object along with its Schema. NB: For performance critical areas, it is <b>much</b>
     * more efficient to store the Schema independently.
     * @param o Object to serialize
     */
    public static <T extends SpecificRecord> ByteBuffer serializeWithSchema(T o) throws IOException
    {
        OutputBuffer buff = new OutputBuffer();
        BinaryEncoder enc = new BinaryEncoder(buff);
        enc.writeString(new Utf8(o.getSchema().toString()));
        SpecificDatumWriter<T> writer = new SpecificDatumWriter<T>(o.getSchema());
        writer.write(o, enc);
        enc.flush();
        return ByteBuffer.wrap(buff.asByteArray());
    }

    /**
     * @return a DataInputStream wrapping the given buffer.
     */
    public static DataInputStream createDataInputStream(ByteBuffer buff)
    {
        ByteBufferInputStream bbis = new ByteBufferInputStream(Collections.singletonList(buff));
        return new DataInputStream(bbis);
    }

    /**
     * Create a generic array of the given type and size. Mostly to minimize imports.
     */
    public static <T> GenericArray<T> createArray(int size, Schema schema)
    {
        return new GenericData.Array<T>(size, Schema.createArray(schema));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1285.java