error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1594.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1594.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1594.java
text:
```scala
l@@ong skipped = 0;

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

package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

/**
 * {@link InputStream} implementation that can read from String, StringBuffer,
 * StringBuilder or CharBuffer.
 * <p>
 * <strong>Note:</strong> Supports {@link #mark(int)} and {@link #reset()}.
 *
 * @since 2.2
 */
public class CharSequenceInputStream extends InputStream {

    private static final int BUFFER_SIZE = 2048;
    
    private static final int EOS = -1;

    private static final int NO_MARK = -1;

    private final CharsetEncoder encoder;
    private final CharBuffer cbuf;
    private final ByteBuffer bbuf;

    private int mark;
    
    /**
     * Constructor.
     * 
     * @param cs the input character sequence
     * @param charset the character set name to use
     * @param bufferSize the buffer size to use.
     * @throws IllegalArgumentException if the buffer is not large enough to hold a complete character
     */
    public CharSequenceInputStream(final CharSequence cs, final Charset charset, final int bufferSize) {
        super();
        this.encoder = charset.newEncoder()
            .onMalformedInput(CodingErrorAction.REPLACE)
            .onUnmappableCharacter(CodingErrorAction.REPLACE);
        // Ensure that buffer is long enough to hold a complete character
        final float maxBytesPerChar = encoder.maxBytesPerChar();
        if (bufferSize < maxBytesPerChar) {
            throw new IllegalArgumentException("Buffer size " + bufferSize + " is less than maxBytesPerChar " + maxBytesPerChar);
        }
        this.bbuf = ByteBuffer.allocate(bufferSize);
        this.bbuf.flip();
        this.cbuf = CharBuffer.wrap(cs);
        this.mark = NO_MARK;
    }

    /**
     * Constructor, calls {@link #CharSequenceInputStream(CharSequence, Charset, int)}.
     * 
     * @param cs the input character sequence
     * @param charset the character set name to use
     * @param bufferSize the buffer size to use.
     * @throws IllegalArgumentException if the buffer is not large enough to hold a complete character
     */
    public CharSequenceInputStream(final CharSequence cs, final String charset, final int bufferSize) {
        this(cs, Charset.forName(charset), bufferSize);
    }

    /**
     * Constructor, calls {@link #CharSequenceInputStream(CharSequence, Charset, int)}
     * with a buffer size of 2048.
     * 
     * @param cs the input character sequence
     * @param charset the character set name to use
     * @throws IllegalArgumentException if the buffer is not large enough to hold a complete character
     */
    public CharSequenceInputStream(final CharSequence cs, final Charset charset) {
        this(cs, charset, BUFFER_SIZE);
    }

    /**
     * Constructor, calls {@link #CharSequenceInputStream(CharSequence, String, int)}
     * with a buffer size of 2048.
     * 
     * @param cs the input character sequence
     * @param charset the character set name to use
     * @throws IllegalArgumentException if the buffer is not large enough to hold a complete character
     */
    public CharSequenceInputStream(final CharSequence cs, final String charset) {
        this(cs, charset, BUFFER_SIZE);
    }

    /**
     * Fills the byte output buffer from the input char buffer.
     * 
     * @throws CharacterCodingException
     *             an error encoding data
     */
    private void fillBuffer() throws CharacterCodingException {
        this.bbuf.compact();
        final CoderResult result = this.encoder.encode(this.cbuf, this.bbuf, true);
        if (result.isError()) {
            result.throwException();
        }
        this.bbuf.flip();
    }
    
    @Override
    public int read(final byte[] b, int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException("Byte array is null");
        }
        if (len < 0 || (off + len) > b.length) {
            throw new IndexOutOfBoundsException("Array Size=" + b.length +
                    ", offset=" + off + ", length=" + len);
        }
        if (len == 0) {
            return 0; // must return 0 for zero length read
        }
        if (!this.bbuf.hasRemaining() && !this.cbuf.hasRemaining()) {
            return EOS;
        }
        int bytesRead = 0;
        while (len > 0) {
            if (this.bbuf.hasRemaining()) {
                final int chunk = Math.min(this.bbuf.remaining(), len);
                this.bbuf.get(b, off, chunk);
                off += chunk;
                len -= chunk;
                bytesRead += chunk;
            } else {
                fillBuffer();
                if (!this.bbuf.hasRemaining() && !this.cbuf.hasRemaining()) {
                    break;
                }
            }
        }
        return bytesRead == 0 && !this.cbuf.hasRemaining() ? EOS : bytesRead;
    }

    @Override
    public int read() throws IOException {
        for (;;) {
            if (this.bbuf.hasRemaining()) {
                return this.bbuf.get() & 0xFF;
            } else {
                fillBuffer();
                if (!this.bbuf.hasRemaining() && !this.cbuf.hasRemaining()) {
                    return EOS;
                }
            }
        }
    }

    @Override
    public int read(final byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override
    public long skip(long n) throws IOException {
        int skipped = 0;
        while (n > 0 && this.cbuf.hasRemaining()) {
            this.cbuf.get();
            n--;
            skipped++;
        }
        return skipped;
    }

    @Override
    public int available() throws IOException {
        return this.cbuf.remaining();
    }

    @Override
    public void close() throws IOException {
    }

    /**
     * {@inheritDoc}
     * @param readlimit max read limit (ignored)
     */
    @Override
    public synchronized void mark(final int readlimit) {
        this.mark = this.cbuf.position();
    }

    @Override
    public synchronized void reset() throws IOException {
        if (this.mark != NO_MARK) {
            this.cbuf.position(this.mark);
            this.mark = NO_MARK;
            this.bbuf.limit(0);
            this.encoder.reset();
        }
    }

    @Override
    public boolean markSupported() {
        return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1594.java