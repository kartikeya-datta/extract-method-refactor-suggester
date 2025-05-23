error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3383.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3383.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3383.java
text:
```scala
i@@f (!text.startsWith(PREFIX) || !text.endsWith(POSTFIX)) {

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

package org.apache.commons.codec.net;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.StringUtils;

/**
 * <p>
 * Implements methods common to all codecs defined in RFC 1522.
 * </p>
 * 
 * <p>
 * <a href="http://www.ietf.org/rfc/rfc1522.txt">RFC 1522</a> 
 * describes techniques to allow the encoding of non-ASCII text in 
 * various portions of a RFC 822 [2] message header, in a manner which
 * is unlikely to confuse existing message handling software.
 * </p>

 * @see <a href="http://www.ietf.org/rfc/rfc1522.txt">
 * MIME (Multipurpose Internet Mail Extensions) Part Two:
 * Message Header Extensions for Non-ASCII Text</a>
 * </p>
 * 
 * @author Apache Software Foundation
 * @since 1.3
 * @version $Id$
 */
abstract class RFC1522Codec {
    
    /**
     * Separator.
     */
    protected static final char SEP = '?';

    /**
     * Prefix
     */
    protected static final String POSTFIX = "?=";

    /**
     * Postfix
     */
    protected static final String PREFIX = "=?";

    /**
     * Applies an RFC 1522 compliant encoding scheme to the given string of text with the 
     * given charset. This method constructs the "encoded-word" header common to all the 
     * RFC 1522 codecs and then invokes {@link #doEncoding(byte [])} method of a concrete 
     * class to perform the specific encoding.
     * 
     * @param text a string to encode
     * @param charset a charset to be used
     * 
     * @return RFC 1522 compliant "encoded-word"
     * 
     * @throws EncoderException thrown if there is an error condition during the Encoding 
     *  process.
     * @see <a href="http://download.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
     */
    protected String encodeText(final String text, final Charset charset)
     throws EncoderException  
    {
        if (text == null) {
            return null;
        }
        StringBuffer buffer = new StringBuffer();
        buffer.append(PREFIX); 
        buffer.append(charset);
        buffer.append(SEP);
        buffer.append(this.getEncoding());
        buffer.append(SEP);
        byte [] rawData = this.doEncoding(text.getBytes(charset)); 
        buffer.append(StringUtils.newStringUsAscii(rawData));
        buffer.append(POSTFIX); 
        return buffer.toString();
    }
    
    /**
     * Applies an RFC 1522 compliant encoding scheme to the given string of text with the 
     * given charset. This method constructs the "encoded-word" header common to all the 
     * RFC 1522 codecs and then invokes {@link #doEncoding(byte [])} method of a concrete 
     * class to perform the specific encoding.
     * 
     * @param text a string to encode
     * @param charsetName the charset to use
     * 
     * @return RFC 1522 compliant "encoded-word"
     * 
     * @throws EncoderException thrown if there is an error condition during the Encoding 
     *  process.
     * @throws UnsupportedEncodingException if charset is not available 
     * 
     * @see <a href="http://download.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
     */
    protected String encodeText(final String text, final String charsetName)
     throws EncoderException, UnsupportedEncodingException  
    {
        if (text == null) {
            return null;
        }
        return this.encodeText(text, Charset.forName(charsetName));
    }
    
    /**
     * Applies an RFC 1522 compliant decoding scheme to the given string of text. This method 
     * processes the "encoded-word" header common to all the RFC 1522 codecs and then invokes 
     * {@link #doEncoding(byte [])} method of a concrete class to perform the specific decoding.
     * 
     * @param text a string to decode
     * @return A new decoded String or {@code null} if the input is {@code null}.
     * 
     * @throws DecoderException thrown if there is an error condition during the decoding 
     *  process.
     * @throws UnsupportedEncodingException thrown if charset specified in the "encoded-word" 
     *  header is not supported 
     */
    protected String decodeText(final String text)
     throws DecoderException, UnsupportedEncodingException  
    {
        if (text == null) {
            return null;
        }
        if ((!text.startsWith(PREFIX)) || (!text.endsWith(POSTFIX))) {
            throw new DecoderException("RFC 1522 violation: malformed encoded content");
        }
        int terminator = text.length() - 2;
        int from = 2;
        int to = text.indexOf(SEP, from);
        if (to == terminator) {
            throw new DecoderException("RFC 1522 violation: charset token not found");
        }
        String charset = text.substring(from, to);
        if (charset.equals("")) {
            throw new DecoderException("RFC 1522 violation: charset not specified");
        }
        from = to + 1;
        to = text.indexOf(SEP, from);
        if (to == terminator) {
            throw new DecoderException("RFC 1522 violation: encoding token not found");
        }
        String encoding = text.substring(from, to);
        if (!getEncoding().equalsIgnoreCase(encoding)) {
            throw new DecoderException("This codec cannot decode " + 
                encoding + " encoded content");
        }
        from = to + 1;
        to = text.indexOf(SEP, from);
        byte[] data = StringUtils.getBytesUsAscii(text.substring(from, to));
        data = doDecoding(data); 
        return new String(data, charset);
    }

    /**
     * Returns the codec name (referred to as encoding in the RFC 1522)
     * 
     * @return name of the codec
     */    
    protected abstract String getEncoding();

    /**
     * Encodes an array of bytes using the defined encoding scheme
     * 
     * @param bytes Data to be encoded
     *
     * @return A byte array containing the encoded data
     * 
     * @throws EncoderException thrown if the Encoder encounters a failure condition 
     *  during the encoding process.
     */    
    protected abstract byte[] doEncoding(byte[] bytes) throws EncoderException;

    /**
     * Decodes an array of bytes using the defined encoding scheme
     * 
     * @param bytes Data to be decoded
     *
     * @return a byte array that contains decoded data
     * 
     * @throws DecoderException A decoder exception is thrown if a Decoder encounters a 
     *  failure condition during the decode process.
     */    
    protected abstract byte[] doDecoding(byte[] bytes) throws DecoderException;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3383.java