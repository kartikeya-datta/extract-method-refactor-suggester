error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12715.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12715.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12715.java
text:
```scala
S@@impleEncodingHolder(char [] highChars) {

/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.apache.commons.compress.archivers.zip;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Static helper functions for robustly encoding filenames in zip files. 
 */
abstract class ZipEncodingHelper {

    /**
     * A class, which holds the high characters of a simple encoding
     * and lazily instantiates a Simple8BitZipEncoding instance in a
     * thread-safe manner.
     */
    private static class SimpleEncodingHolder {

        private final char [] highChars;
        private Simple8BitZipEncoding encoding;

        /**
         * Instantiate a simple encoding holder.
         * 
         * @param highChars The characters for byte codes 128 to 255.
         * 
         * @see Simple8BitZipEncoding#Simple8BitZipEncoding(char[])
         */
        SimpleEncodingHolderxx(char [] highChars) {
            this.highChars = highChars;
        }

        /**
         * @return The associated {@link Simple8BitZipEncoding}, which
         *         is instantiated if not done so far.
         */
        public synchronized Simple8BitZipEncoding getEncoding() {
            if (this.encoding == null) {
                this.encoding = new Simple8BitZipEncoding(this.highChars);
            }
            return this.encoding;
        }
    }

    private static final Map simpleEncodings;

    static {
        simpleEncodings = new HashMap();

        char[] cp437_high_chars =
            new char[] { 0x00c7, 0x00fc, 0x00e9, 0x00e2, 0x00e4, 0x00e0,
                         0x00e5, 0x00e7, 0x00ea, 0x00eb, 0x00e8, 0x00ef,
                         0x00ee, 0x00ec, 0x00c4, 0x00c5, 0x00c9, 0x00e6,
                         0x00c6, 0x00f4, 0x00f6, 0x00f2, 0x00fb, 0x00f9,
                         0x00ff, 0x00d6, 0x00dc, 0x00a2, 0x00a3, 0x00a5,
                         0x20a7, 0x0192, 0x00e1, 0x00ed, 0x00f3, 0x00fa,
                         0x00f1, 0x00d1, 0x00aa, 0x00ba, 0x00bf, 0x2310,
                         0x00ac, 0x00bd, 0x00bc, 0x00a1, 0x00ab, 0x00bb,
                         0x2591, 0x2592, 0x2593, 0x2502, 0x2524, 0x2561,
                         0x2562, 0x2556, 0x2555, 0x2563, 0x2551, 0x2557,
                         0x255d, 0x255c, 0x255b, 0x2510, 0x2514, 0x2534,
                         0x252c, 0x251c, 0x2500, 0x253c, 0x255e, 0x255f,
                         0x255a, 0x2554, 0x2569, 0x2566, 0x2560, 0x2550,
                         0x256c, 0x2567, 0x2568, 0x2564, 0x2565, 0x2559,
                         0x2558, 0x2552, 0x2553, 0x256b, 0x256a, 0x2518,
                         0x250c, 0x2588, 0x2584, 0x258c, 0x2590, 0x2580,
                         0x03b1, 0x00df, 0x0393, 0x03c0, 0x03a3, 0x03c3,
                         0x00b5, 0x03c4, 0x03a6, 0x0398, 0x03a9, 0x03b4,
                         0x221e, 0x03c6, 0x03b5, 0x2229, 0x2261, 0x00b1,
                         0x2265, 0x2264, 0x2320, 0x2321, 0x00f7, 0x2248,
                         0x00b0, 0x2219, 0x00b7, 0x221a, 0x207f, 0x00b2,
                         0x25a0, 0x00a0 };

        SimpleEncodingHolder cp437 = new SimpleEncodingHolder(cp437_high_chars);

        simpleEncodings.put("CP437",cp437);
        simpleEncodings.put("Cp437",cp437);
        simpleEncodings.put("cp437",cp437);
        simpleEncodings.put("IBM437",cp437);
        simpleEncodings.put("ibm437",cp437);

        char[] cp850_high_chars =
            new char[] { 0x00c7, 0x00fc, 0x00e9, 0x00e2, 0x00e4, 0x00e0,
                         0x00e5, 0x00e7, 0x00ea, 0x00eb, 0x00e8, 0x00ef,
                         0x00ee, 0x00ec, 0x00c4, 0x00c5, 0x00c9, 0x00e6,
                         0x00c6, 0x00f4, 0x00f6, 0x00f2, 0x00fb, 0x00f9,
                         0x00ff, 0x00d6, 0x00dc, 0x00f8, 0x00a3, 0x00d8,
                         0x00d7, 0x0192, 0x00e1, 0x00ed, 0x00f3, 0x00fa,
                         0x00f1, 0x00d1, 0x00aa, 0x00ba, 0x00bf, 0x00ae,
                         0x00ac, 0x00bd, 0x00bc, 0x00a1, 0x00ab, 0x00bb,
                         0x2591, 0x2592, 0x2593, 0x2502, 0x2524, 0x00c1,
                         0x00c2, 0x00c0, 0x00a9, 0x2563, 0x2551, 0x2557,
                         0x255d, 0x00a2, 0x00a5, 0x2510, 0x2514, 0x2534,
                         0x252c, 0x251c, 0x2500, 0x253c, 0x00e3, 0x00c3,
                         0x255a, 0x2554, 0x2569, 0x2566, 0x2560, 0x2550,
                         0x256c, 0x00a4, 0x00f0, 0x00d0, 0x00ca, 0x00cb,
                         0x00c8, 0x0131, 0x00cd, 0x00ce, 0x00cf, 0x2518,
                         0x250c, 0x2588, 0x2584, 0x00a6, 0x00cc, 0x2580,
                         0x00d3, 0x00df, 0x00d4, 0x00d2, 0x00f5, 0x00d5,
                         0x00b5, 0x00fe, 0x00de, 0x00da, 0x00db, 0x00d9,
                         0x00fd, 0x00dd, 0x00af, 0x00b4, 0x00ad, 0x00b1,
                         0x2017, 0x00be, 0x00b6, 0x00a7, 0x00f7, 0x00b8,
                         0x00b0, 0x00a8, 0x00b7, 0x00b9, 0x00b3, 0x00b2,
                         0x25a0, 0x00a0 };

        SimpleEncodingHolder cp850 = new SimpleEncodingHolder(cp850_high_chars);

        simpleEncodings.put("CP850",cp850);
        simpleEncodings.put("Cp850",cp850);
        simpleEncodings.put("cp850",cp850);
        simpleEncodings.put("IBM850",cp850);
        simpleEncodings.put("ibm850",cp850);
    }

    /**
     * Grow a byte buffer, so it has a minimal capacity or at least
     * the double capacity of the original buffer 
     * 
     * @param b The original buffer.
     * @param newCapacity The minimal requested new capacity.
     * @return A byte buffer <code>r</code> with
     *         <code>r.capacity() = max(b.capacity()*2,newCapacity)</code> and
     *         all the data contained in <code>b</code> copied to the beginning
     *         of <code>r</code>.
     *
     */
    static ByteBuffer growBuffer(ByteBuffer b, int newCapacity) {
        b.limit(b.position());
        b.rewind();

        int c2 = b.capacity() * 2;
        ByteBuffer on = ByteBuffer.allocate(c2 < newCapacity ? newCapacity : c2);

        on.put(b);
        return on;
    }

 
    /**
     * The hexadecimal digits <code>0,...,9,A,...,F</code> encoded as
     * ASCII bytes.
     */
    private static final byte[] HEX_DIGITS =
        new byte [] {
        0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x41,
        0x42, 0x43, 0x44, 0x45, 0x46
    };

    /**
     * Append <code>%Uxxxx</code> to the given byte buffer.
     * The caller must assure, that <code>bb.remaining()&gt;=6</code>.
     * 
     * @param bb The byte buffer to write to.
     * @param c The character to write.
     */
    static void appendSurrogate(ByteBuffer bb, char c) {

        bb.put((byte) '%');
        bb.put((byte) 'U');

        bb.put(HEX_DIGITS[(c >> 12)&0x0f]);
        bb.put(HEX_DIGITS[(c >> 8)&0x0f]);
        bb.put(HEX_DIGITS[(c >> 4)&0x0f]);
        bb.put(HEX_DIGITS[c & 0x0f]);
    }


    /**
     * name of the encoding UTF-8
     */
    static final String UTF8 = "UTF8";

    /**
     * name of the encoding UTF-8
     */
    static final ZipEncoding UTF8_ZIP_ENCODING = new FallbackZipEncoding(UTF8);

    /**
     * Instantiates a zip encoding.
     * 
     * @param name The name of the zip encoding. Specify <code>null</code> for
     *             the platform's default encoding.
     * @return A zip encoding for the given encoding name.
     */
    static ZipEncoding getZipEncoding(String name) {
 
        // fallback encoding is good enough for utf-8.
        if (isUTF8(name)) {
            return UTF8_ZIP_ENCODING;
        }

        if (name == null) {
            return new FallbackZipEncoding();
        }

        SimpleEncodingHolder h =
            (SimpleEncodingHolder) simpleEncodings.get(name);

        if (h!=null) {
            return h.getEncoding();
        }

        try {

            Charset cs = Charset.forName(name);
            return new NioZipEncoding(cs);

        } catch (UnsupportedCharsetException e) {
            return new FallbackZipEncoding(name);
        }
    }

    /**
     * Whether a given encoding - or the platform's default encoding
     * if the parameter is null - is UTF-8.
     */
    static boolean isUTF8(String encoding) {
        if (encoding == null) {
            // check platform's default encoding
            encoding = System.getProperty("file.encoding");
        }
        return UTF8.equalsIgnoreCase(encoding)
 "utf-8".equalsIgnoreCase(encoding);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12715.java