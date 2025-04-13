error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/618.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/618.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/618.java
text:
```scala
B@@yteBufferUtil.string(bytes);

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

package org.apache.cassandra.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.util.Arrays;

import com.google.common.base.Charsets;
import org.junit.Test;

public class ByteBufferUtilTest
{
    private static final String s = "cassandra";

    private ByteBuffer fromStringWithPosition(String s, int pos, boolean direct)
    {
        int l = s.length();
        ByteBuffer bb;
        if (direct)
        {
            bb = ByteBuffer.allocateDirect(l + pos);
        }
        else
        {
            ByteBuffer tmp = ByteBuffer.allocate(l + pos + 3);
            tmp.position(3);
            bb = tmp.slice(); // make bb have a non null arrayOffset
        }
        bb.position(pos);
        bb.mark();
        bb.put(s.getBytes());
        bb.reset();
        assert bb.position() == pos;
        return bb;
    }

    @Test
    public void testString() throws Exception
    {
        assert s.equals(ByteBufferUtil.string(ByteBufferUtil.bytes(s)));

        int pos = 10;
        ByteBuffer bb = fromStringWithPosition(s, 10, false);
        assert s.equals(ByteBufferUtil.string(bb, 10, s.length()));

        bb = fromStringWithPosition(s, 10, true);
        assert s.equals(ByteBufferUtil.string(bb, 10, s.length()));
    }

    @Test
    public void testGetArray()
    {
        byte[] t = s.getBytes();

        ByteBuffer bb = ByteBufferUtil.bytes(s);
        assertArrayEquals(t, ByteBufferUtil.getArray(bb));

        bb = fromStringWithPosition(s, 10, false);
        assertArrayEquals(t, ByteBufferUtil.getArray(bb));

        bb = fromStringWithPosition(s, 10, true);
        assertArrayEquals(t, ByteBufferUtil.getArray(bb));
    }

    @Test
    public void testLastIndexOf()
    {
        ByteBuffer bb = ByteBufferUtil.bytes(s);
        checkLastIndexOf(bb);

        bb = fromStringWithPosition(s, 10, false);
        checkLastIndexOf(bb);

        bb = fromStringWithPosition(s, 10, true);
        checkLastIndexOf(bb);
    }

    private void checkLastIndexOf(ByteBuffer bb)
    {
        assert bb.position() + 8 == ByteBufferUtil.lastIndexOf(bb, (byte)'a', bb.position() + 8);
        assert bb.position() + 4 == ByteBufferUtil.lastIndexOf(bb, (byte)'a', bb.position() + 7);
        assert bb.position() + 3 == ByteBufferUtil.lastIndexOf(bb, (byte)'s', bb.position() + 8);
        assert -1 == ByteBufferUtil.lastIndexOf(bb, (byte)'o', bb.position() + 8);
        assert -1 == ByteBufferUtil.lastIndexOf(bb, (byte)'d', bb.position() + 5);
    }

    @Test
    public void testClone()
    {
        ByteBuffer bb = ByteBufferUtil.bytes(s);
        ByteBuffer clone1 = ByteBufferUtil.clone(bb);
        assert bb != clone1;
        assert bb.equals(clone1);
        assert bb.array() != clone1.array();

        bb = fromStringWithPosition(s, 10, false);
        ByteBuffer clone2 = ByteBufferUtil.clone(bb);
        assert bb != clone2;
        assert bb.equals(clone2);
        assert clone1.equals(clone2);
        assert bb.array() != clone2.array();

        bb = fromStringWithPosition(s, 10, true);
        ByteBuffer clone3 = ByteBufferUtil.clone(bb);
        assert bb != clone3;
        assert bb.equals(clone3);
        assert clone1.equals(clone3);
    }

    @Test
    public void testArrayCopy()
    {
        ByteBuffer bb = ByteBufferUtil.bytes(s);
        checkArrayCopy(bb);

        bb = fromStringWithPosition(s, 10, false);
        checkArrayCopy(bb);

        bb = fromStringWithPosition(s, 10, true);
        checkArrayCopy(bb);
    }

    private void checkArrayCopy(ByteBuffer bb)
    {

        byte[] bytes = new byte[s.length()];
        ByteBufferUtil.arrayCopy(bb, bb.position(), bytes, 0, s.length());
        assertArrayEquals(s.getBytes(), bytes);

        bytes = new byte[5];
        ByteBufferUtil.arrayCopy(bb, bb.position() + 3, bytes, 1, 4);
        assertArrayEquals(Arrays.copyOfRange(s.getBytes(), 3, 7), Arrays.copyOfRange(bytes, 1, 5));
    }

    @Test
    public void testReadWrite() throws IOException
    {
        ByteBuffer bb = ByteBufferUtil.bytes(s);
        checkReadWrite(bb);

        bb = fromStringWithPosition(s, 10, false);
        checkReadWrite(bb);

        bb = fromStringWithPosition(s, 10, true);
        checkReadWrite(bb);
    }

    private void checkReadWrite(ByteBuffer bb) throws IOException
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bos);
        ByteBufferUtil.writeWithLength(bb, out);
        ByteBufferUtil.writeWithShortLength(bb, out);

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
        assert bb.equals(ByteBufferUtil.readWithLength(in));
        assert bb.equals(ByteBufferUtil.readWithShortLength(in));
    }

    @Test
    public void testInputStream() throws IOException
    {
        ByteBuffer bb = ByteBuffer.allocate(13);
        bb.putInt(255);
        bb.put((byte) -3);
        bb.putLong(42L);
        bb.clear();

        DataInputStream in = new DataInputStream(ByteBufferUtil.inputStream(bb));
        assert in.readInt() == 255;
        assert in.readByte() == (byte)-3;
        assert in.readLong() == 42L;
    }

    @Test
    public void testIntBytesConversions()
    {
        // positive, negative, 1 and 2 byte cases, including a few edges that would foul things up unless you're careful
        // about masking away sign extension.
        int[] ints = new int[]
        {
            -20, -127, -128, 0, 1, 127, 128, 65534, 65535, -65534, -65535
        };

        for (int i : ints) {
            ByteBuffer ba = ByteBufferUtil.bytes(i);
            int actual = ByteBufferUtil.toInt(ba);
            assertEquals(i, actual);
        }
    }

    @Test(expected=CharacterCodingException.class)
    public void testDecode() throws IOException
    {
        ByteBuffer bytes = ByteBuffer.wrap(new byte[]{(byte)0xff, (byte)0xfe});
        ByteBufferUtil.string(bytes, Charsets.UTF_8);
    }

    @Test
    public void testHexBytesConversion()
    {
        for (int i = Byte.MIN_VALUE; i <= Byte.MAX_VALUE; i++)
        {
            ByteBuffer bb = ByteBuffer.allocate(1);
            bb.put((byte)i);
            bb.clear();
            String s = ByteBufferUtil.bytesToHex(bb);
            ByteBuffer bb2 = ByteBufferUtil.hexToBytes(s);
            assert bb.equals(bb2);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/618.java