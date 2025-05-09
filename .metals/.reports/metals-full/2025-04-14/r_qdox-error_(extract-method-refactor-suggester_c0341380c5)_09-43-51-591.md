error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12843.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12843.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12843.java
text:
```scala
public static v@@oid readFully(final InputStream in, byte[] b, int off, int len) throws IOException {

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.as.process;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UTFDataFormatException;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public final class StreamUtils {

    private StreamUtils() {
    }

    public static Status readWord(final InputStream input, final StringBuilder dest) throws IOException {
        dest.setLength(0);
        int c;
        for (;;) {
            c = readChar(input);
            switch (c) {
                case -1: return Status.END_OF_STREAM;
                case 0: return Status.MORE;
                case '\n': return Status.END_OF_LINE;
                default: dest.append((char) c);
            }
        }
    }

    private static final String INVALID_BYTE = "Invalid byte";

    public static int readChar(final InputStream input) throws IOException {
        final int a = input.read();
        //System.err.println((char)a + "(" + a + ")");
        if (a < 0) {
            return -1;
        } else if (a == 0) {
            return -1;
        } else if (a < 0x80) {
            return (char)a;
        } else if (a < 0xc0) {
            throw new UTFDataFormatException(INVALID_BYTE);
        } else if (a < 0xe0) {
            final int b = input.read();
            if (b == -1) {
                throw new EOFException();
            } else if ((b & 0xc0) != 0x80) {
                throw new UTFDataFormatException(INVALID_BYTE + ":" + (char)a + "(" + a + ")");
            }
            return (a & 0x1f) << 6 | b & 0x3f;
        } else if (a < 0xf0) {
            final int b = input.read();
            if (b == -1) {
                throw new EOFException();
            } else if ((b & 0xc0) != 0x80) {
                throw new UTFDataFormatException(INVALID_BYTE);
            }
            final int c = input.read();
            if (c == -1) {
                throw new EOFException();
            } else if ((c & 0xc0) != 0x80) {
                throw new UTFDataFormatException(INVALID_BYTE);
            }
            return (a & 0x0f) << 12 | (b & 0x3f) << 6 | c & 0x3f;
        } else {
            throw new UTFDataFormatException(INVALID_BYTE);
        }
    }

    public static void readToEol(final InputStream input) throws IOException {
        for (;;) {
            switch (input.read()) {
                case -1: return;
                case '\n': return;
            }
        }
    }

    public static byte[] readBytesWithLength(final InputStream in) throws IOException {
        int expectedLength = readInt(in);
        byte[] bytes = new byte[expectedLength];
        readFully(in, bytes, 0, expectedLength);
        return bytes;
    }

    public static Status readStatus(final InputStream in) throws IOException{
        int c = readChar(in);

        switch (c) {
            case -1: {
                return Status.END_OF_STREAM;
            }
            case 0:  {
                return Status.MORE;
            }
            case '\n': {
                return Status.END_OF_LINE;
            }
            default: {
                throw new IllegalStateException("unexpected char " + c);
            }
        }
    }

//    public static CheckedBytes readCheckedBytes(final InputStream input) throws IOException {
//        return new CheckedBytes(input);
//    }

    public static int readInt(final InputStream in) throws IOException {
        int ch1 = in.read();
        int ch2 = in.read();
        int ch3 = in.read();
        int ch4 = in.read();
        if ((ch1 | ch2 | ch3 | ch4) < 0)
            throw new EOFException();
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
    }

    public static void readFully(final InputStream in, byte b[], int off, int len) throws IOException {
        if (len < 0)
            throw new IndexOutOfBoundsException();
        int n = 0;
        while (n < len) {
            int count = in.read(b, off + n, len - n);
            if (count < 0)
            throw new EOFException();
            n += count;
        }
    }

    public static long readLong(final InputStream in) throws IOException {
        byte[] bytes = new byte[8];
        readFully(in, bytes, 0, 8);
        return (((long)bytes[0] << 56) +
                ((long)(bytes[1] & 255) << 48) +
                ((long)(bytes[2] & 255) << 40) +
                ((long)(bytes[3] & 255) << 32) +
                ((long)(bytes[4] & 255) << 24) +
                ((bytes[5] & 255) << 16) +
                ((bytes[6] & 255) <<  8) +
                ((bytes[7] & 255) <<  0));
    }

    public static void writeString(final OutputStream output, final Object o) throws IOException {
        writeString(output, o.toString());
    }

    public static void writeString(final OutputStream output, final String s) throws IOException {
        final int length = s.length();

        int strIdx = 0;
        while (strIdx < length) {
            writeChar(output, s.charAt(strIdx ++));
        }
    }

    public static void writeChar(final OutputStream output, final char c) throws IOException {

        if (c >= 0x20 && c <= 0x7f) {
            output.write((byte)c);
        } else if (c <= 0x07ff) {
            output.write((byte)(0xc0 | 0x1f & c >> 6));
            output.write((byte)(0x80 | 0x3f & c));
        } else {
            output.write((byte)(0xe0 | 0x0f & c >> 12));
            output.write((byte)(0x80 | 0x3f & c >> 6));
            output.write((byte)(0x80 | 0x3f & c));
        }
    }

    public static void writeInt(final OutputStream out, final int v) throws IOException {
        out.write((v >>> 24) & 0xFF);
        out.write((v >>> 16) & 0xFF);
        out.write((v >>>  8) & 0xFF);
        out.write((v >>>  0) & 0xFF);
    }

    public static void writeLong(final OutputStream out, final long v) throws IOException {
        out.write((byte) (v >>> 56) & 0xFF);
        out.write((byte) (v >>> 48) & 0xFF);
        out.write((byte) (v >>> 40) & 0xFF);
        out.write((byte) (v >>> 32) & 0xFF);
        out.write((byte) (v >>> 24) & 0xFF);
        out.write((byte) (v >>> 16) & 0xFF);
        out.write((byte) (v >>>  8) & 0xFF);
        out.write((byte) (v >>>  0) & 0xFF);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12843.java