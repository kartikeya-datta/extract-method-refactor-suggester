error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1439.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1439.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1439.java
text:
```scala
t@@his.base64 = new Base64(false);

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.codec.binary;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Provides Base64 encoding and decoding in a streaming fashion (unlimited size). When encoding the default lineLength
 * is 76 characters and the default lineEnding is CRLF, but these can be overridden by using the appropriate
 * constructor.
 * <p>
 * The default behaviour of the Base64InputStream is to DECODE, whereas the default behaviour of the Base64OutputStream
 * is to ENCODE, but this behaviour can be overridden by using a different constructor.
 * </p>
 * <p>
 * This class implements section <cite>6.8. Base64 Content-Transfer-Encoding</cite> from RFC 2045 <cite>Multipurpose
 * Internet Mail Extensions (MIME) Part One: Format of Internet Message Bodies</cite> by Freed and Borenstein.
 * </p>
 * <p>
 * Since this class operates directly on byte streams, and not character streams, it is hard-coded to only encode/decode
 * character encodings which are compatible with the lower 127 ASCII chart (ISO-8859-1, Windows-1252, UTF-8, etc).
 * </p>
 * 
 * @author Apache Software Foundation 
 * @version $Id$
 * @see <a href="http://www.ietf.org/rfc/rfc2045.txt">RFC 2045</a>
 * @since 1.4
 */
public class Base64InputStream extends FilterInputStream {

    private final boolean doEncode;

    private final Base64 base64;

    private final byte[] singleByte = new byte[1];

    /**
     * Creates a Base64InputStream such that all data read is Base64-decoded from the original provided InputStream.
     * 
     * @param in
     *            InputStream to wrap.
     */
    public Base64InputStream(InputStream in) {
        this(in, false);
    }

    /**
     * Creates a Base64InputStream such that all data read is either Base64-encoded or Base64-decoded from the original
     * provided InputStream.
     * 
     * @param in
     *            InputStream to wrap.
     * @param doEncode
     *            true if we should encode all data read from us, false if we should decode.
     */
    public Base64InputStream(InputStream in, boolean doEncode) {
        super(in);
        this.doEncode = doEncode;
        this.base64 = new Base64();
    }

    /**
     * Creates a Base64InputStream such that all data read is either Base64-encoded or Base64-decoded from the original
     * provided InputStream.
     * 
     * @param in
     *            InputStream to wrap.
     * @param doEncode
     *            true if we should encode all data read from us, false if we should decode.
     * @param lineLength
     *            If doEncode is true, each line of encoded data will contain lineLength characters (rounded down to
     *            nearest multiple of 4). If lineLength <=0, the encoded data is not divided into lines. If doEncode is
     *            false, lineLength is ignored.
     * @param lineSeparator
     *            If doEncode is true, each line of encoded data will be terminated with this byte sequence (e.g. \r\n).
     *            If lineLength <= 0, the lineSeparator is not used. If doEncode is false lineSeparator is ignored.
     */
    public Base64InputStream(InputStream in, boolean doEncode, int lineLength, byte[] lineSeparator) {
        super(in);
        this.doEncode = doEncode;
        this.base64 = new Base64(lineLength, lineSeparator);
    }

    /**
     * Reads one <code>byte</code> from this input stream.
     * 
     * @return the byte as an integer in the range 0 to 255. Returns -1 if EOF has been reached.
     * @throws IOException
     *             if an I/O error occurs.
     */
    public int read() throws IOException {
        int r = read(singleByte, 0, 1);
        while (r == 0) {
            r = read(singleByte, 0, 1);
        }
        if (r > 0) {
            return singleByte[0] < 0 ? 256 + singleByte[0] : singleByte[0];
        }
        return -1;
    }

    /**
     * Attempts to read <code>len</code> bytes into the specified <code>b</code> array starting at <code>offset</code>
     * from this InputStream.
     * 
     * @param b
     *            destination byte array
     * @param offset
     *            where to start writing the bytes
     * @param len
     *            maximum number of bytes to read
     * 
     * @return number of bytes read
     * @throws IOException
     *             if an I/O error occurs.
     * @throws NullPointerException
     *             if the byte array parameter is null
     * @throws IndexOutOfBoundsException
     *             if offset, len or buffer size are invalid
     */
    public int read(byte b[], int offset, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        } else if (offset < 0 || len < 0) {
            throw new IndexOutOfBoundsException();
        } else if (offset > b.length || offset + len > b.length) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        } else {
            if (!base64.hasData()) {
                byte[] buf = new byte[doEncode ? 4096 : 8192];
                int c = in.read(buf);
                // A little optimization to avoid System.arraycopy()
                // when possible.
                if (c > 0 && b.length == len) {
                    base64.setInitialBuffer(b, offset, len);
                }
                if (doEncode) {
                    base64.encode(buf, 0, c);
                } else {
                    base64.decode(buf, 0, c);
                }
            }
            return base64.readResults(b, offset, len);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @return false
     */
    public boolean markSupported() {
        return false; // not an easy job to support marks
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1439.java