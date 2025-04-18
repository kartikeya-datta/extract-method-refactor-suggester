error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12694.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12694.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 695
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12694.java
text:
```scala
public class BinaryCodec implements BinaryDecoder, BinaryEncoder {

/*
 * Copyright 2001-2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 

p@@ackage org.apache.commons.codec.binary;

import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;

/**
 * Encodes and decodes byte arrays to and from ASCII bit Strings.
 * 
 * @todo may want to add more bit vector functions like and/or/xor/nand 
 * @todo also might be good to generate boolean[]
 * from byte[] et. cetera.
 * 
 * @author Apache Software Foundation
 * @since 1.3
 * @version $Id $
 */
public class Binary implements BinaryDecoder, BinaryEncoder {
    /*
     * tried to avoid using ArrayUtils to minimize dependencies while using these empty arrays - dep is just not worth
     * it.
     */
    /** Empty char array. */
    private static final char[] EMPTY_CHAR_ARRAY = new char[0];

    /** Empty byte array. */
    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    /** Mask for bit 0 of a byte. */
    private static final int BIT_0 = 1;

    /** Mask for bit 1 of a byte. */
    private static final int BIT_1 = 0x02;

    /** Mask for bit 2 of a byte. */
    private static final int BIT_2 = 0x04;

    /** Mask for bit 3 of a byte. */
    private static final int BIT_3 = 0x08;

    /** Mask for bit 4 of a byte. */
    private static final int BIT_4 = 0x10;

    /** Mask for bit 5 of a byte. */
    private static final int BIT_5 = 0x20;

    /** Mask for bit 6 of a byte. */
    private static final int BIT_6 = 0x40;

    /** Mask for bit 7 of a byte. */
    private static final int BIT_7 = 0x80;

    private static final int[] BITS = {BIT_0, BIT_1, BIT_2, BIT_3, BIT_4, BIT_5, BIT_6, BIT_7};

    /**
     * Converts an array of raw binary data into an array of ascii 0 and 1 characters.
     * 
     * @param raw
     *                  the raw binary data to convert
     * @return 0 and 1 ascii character bytes one for each bit of the argument
     * @see org.apache.commons.codec.BinaryEncoder#encode(byte[])
     */
    public byte[] encode(byte[] raw) {
        return toAsciiBytes(raw);
    }

    /**
     * Converts an array of raw binary data into an array of ascii 0 and 1 chars.
     * 
     * @param raw
     *                  the raw binary data to convert
     * @return 0 and 1 ascii character chars one for each bit of the argument
     * @throws EncoderException
     *                  if the argument is not a byte[]
     * @see org.apache.commons.codec.Encoder#encode(java.lang.Object)
     */
    public Object encode(Object raw) throws EncoderException {
        if (!(raw instanceof byte[])) {
            throw new EncoderException("argument not a byte array");
        }
        return toAsciiChars((byte[]) raw);
    }

    /**
     * Decodes a byte array where each byte represents an ascii '0' or '1'.
     * 
     * @param ascii
     *                  each byte represents an ascii '0' or '1'
     * @return the raw encoded binary where each bit corresponds to a byte in the byte array argument
     * @throws DecoderException
     *                  if argument is not a byte[], char[] or String
     * @see org.apache.commons.codec.Decoder#decode(java.lang.Object)
     */
    public Object decode(Object ascii) throws DecoderException {
        if (ascii == null) {
            return EMPTY_BYTE_ARRAY;
        }
        if (ascii instanceof byte[]) {
            return fromAscii((byte[]) ascii);
        }
        if (ascii instanceof char[]) {
            return fromAscii((char[]) ascii);
        }
        if (ascii instanceof String) {
            return fromAscii(((String) ascii).toCharArray());
        }
        throw new DecoderException("argument not a byte array");
    }

    /**
     * Decodes a byte array where each byte represents an ascii '0' or '1'.
     * 
     * @param ascii
     *                  each byte represents an ascii '0' or '1'
     * @return the raw encoded binary where each bit corresponds to a byte in the byte array argument
     * @see org.apache.commons.codec.Decoder#decode(Object)
     */
    public byte[] decode(byte[] ascii) {
        return fromAscii(ascii);
    }

    /**
     * Decodes a String where each char of the String represents an ascii '0' or '1'.
     * 
     * @param ascii
     *                  String of '0' and '1' characters
     * @return the raw encoded binary where each bit corresponds to a byte in the byte array argument
     * @see org.apache.commons.codec.Decoder#decode(Object)
     */
    public byte[] toByteArray(String ascii) {
        if (ascii == null) {
            return EMPTY_BYTE_ARRAY;
        }
        return fromAscii(ascii.toCharArray());
    }

    // ------------------------------------------------------------------------
    //
    // static codec operations
    //
    // ------------------------------------------------------------------------
    /**
     * Decodes a byte array where each char represents an ascii '0' or '1'.
     * 
     * @param ascii
     *                  each char represents an ascii '0' or '1'
     * @return the raw encoded binary where each bit corresponds to a char in the char array argument
     */
    public static byte[] fromAscii(char[] ascii) {
        if (ascii == null || ascii.length == 0) {
            return EMPTY_BYTE_ARRAY;
        }
        // get length/8 times bytes with 3 bit shifts to the right of the length
        byte[] l_raw = new byte[ascii.length >> 3];
        /*
         * We decr index jj by 8 as we go along to not recompute indices using multiplication every time inside the
         * loop.
         */
        for (int ii = 0, jj = ascii.length - 1; ii < l_raw.length; ii++, jj -= 8) {
            for (int bits = 0; bits < BITS.length; ++bits) {
                if (ascii[jj - bits] == '1') {
                    l_raw[ii] |= BITS[bits];
                }
            }
        }
        return l_raw;
    }

    /**
     * Decodes a byte array where each byte represents an ascii '0' or '1'.
     * 
     * @param ascii
     *                  each byte represents an ascii '0' or '1'
     * @return the raw encoded binary where each bit corresponds to a byte in the byte array argument
     */
    public static byte[] fromAscii(byte[] ascii) {
        if (ascii == null || ascii.length == 0) {
            return EMPTY_BYTE_ARRAY;
        }
        // get length/8 times bytes with 3 bit shifts to the right of the length
        byte[] l_raw = new byte[ascii.length >> 3];
        /*
         * We decr index jj by 8 as we go along to not recompute indices using multiplication every time inside the
         * loop.
         */
        for (int ii = 0, jj = ascii.length - 1; ii < l_raw.length; ii++, jj -= 8) {
            for (int bits = 0; bits < BITS.length; ++bits) {
                if (ascii[jj - bits] == '1') {
                    l_raw[ii] |= BITS[bits];
                }
            }
        }
        return l_raw;
    }

    /**
     * Converts an array of raw binary data into an array of ascii 0 and 1 character bytes - each byte is a truncated
     * char.
     * 
     * @param raw
     *                  the raw binary data to convert
     * @return an array of 0 and 1 character bytes for each bit of the argument
     * @see org.apache.commons.codec.BinaryEncoder#encode(byte[])
     */
    public static byte[] toAsciiBytes(byte[] raw) {
        if (raw == null || raw.length == 0) {
            return EMPTY_BYTE_ARRAY;
        }
        // get 8 times the bytes with 3 bit shifts to the left of the length
        byte[] l_ascii = new byte[raw.length << 3];
        /*
         * We decr index jj by 8 as we go along to not recompute indices using multiplication every time inside the
         * loop.
         */
        for (int ii = 0, jj = l_ascii.length - 1; ii < raw.length; ii++, jj -= 8) {
            for (int bits = 0; bits < BITS.length; ++bits) {
                if ((raw[ii] & BITS[bits]) == 0) {
                    l_ascii[jj - bits] = '0';
                } else {
                    l_ascii[jj - bits] = '1';
                }
            }
        }
        return l_ascii;
    }

    /**
     * Converts an array of raw binary data into an array of ascii 0 and 1 characters.
     * 
     * @param raw
     *                  the raw binary data to convert
     * @return an array of 0 and 1 characters for each bit of the argument
     * @see org.apache.commons.codec.BinaryEncoder#encode(byte[])
     */
    public static char[] toAsciiChars(byte[] raw) {
        if (raw == null || raw.length == 0) {
            return EMPTY_CHAR_ARRAY;
        }
        // get 8 times the bytes with 3 bit shifts to the left of the length
        char[] l_ascii = new char[raw.length << 3];
        /*
         * We decr index jj by 8 as we go along to not recompute indices using multiplication every time inside the
         * loop.
         */
        for (int ii = 0, jj = l_ascii.length - 1; ii < raw.length; ii++, jj -= 8) {
            for (int bits = 0; bits < BITS.length; ++bits) {
                if ((raw[ii] & BITS[bits]) == 0) {
                    l_ascii[jj - bits] = '0';
                } else {
                    l_ascii[jj - bits] = '1';
                }
            }
        }
        return l_ascii;
    }

    /**
     * Converts an array of raw binary data into a String of ascii 0 and 1 characters.
     * 
     * @param raw
     *                  the raw binary data to convert
     * @return a String of 0 and 1 characters representing the binary data
     * @see org.apache.commons.codec.BinaryEncoder#encode(byte[])
     */
    public static String toAsciiString(byte[] raw) {
        return new String(toAsciiChars(raw));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12694.java