error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11216.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11216.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11216.java
text:
```scala
t@@hrow new DecoderException(e.getMessage(), e);

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

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;

import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.RequiredCharsetNames;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.StringBytesUtils;

/**
 * <p>Implements the 'www-form-urlencoded' encoding scheme, 
 * also misleadingly known as URL encoding.</p>
 *  
 * <p>For more detailed information please refer to 
 * <a href="http://www.w3.org/TR/html4/interact/forms.html#h-17.13.4.1">
 * Chapter 17.13.4 'Form content types'</a> of the 
 * <a href="http://www.w3.org/TR/html4/">HTML 4.01 Specification<a></p>
 * 
 * <p> 
 * This codec is meant to be a replacement for standard Java classes
 * {@link java.net.URLEncoder} and {@link java.net.URLDecoder} 
 * on older Java platforms, as these classes in Java versions below 
 * 1.4 rely on the platform's default charset encoding.
 * </p>
 * 
 * @author Apache Software Foundation
 * @since 1.2
 * @version $Id$
 */
public class URLCodec implements BinaryEncoder, BinaryDecoder, StringEncoder, StringDecoder {
    
    /**
     * Radix used in encoding and decoding.
     */
    private static final int RADIX = 16;
    
    /**
     * The default charset used for string decoding and encoding. Consider this field final. The next major release may
     * break compatibility and make this field be final.
     */
    protected String charset;
    
    /**
     * Consider this field final. The next major release may break compatibility and make this field be final.
     */
    protected static byte ESCAPE_CHAR = '%';
    /**
     * BitSet of www-form-url safe characters.
     */
    protected static final BitSet WWW_FORM_URL = new BitSet(256);
    
    // Static initializer for www_form_url
    static {
        // alpha characters
        for (int i = 'a'; i <= 'z'; i++) {
            WWW_FORM_URL.set(i);
        }
        for (int i = 'A'; i <= 'Z'; i++) {
            WWW_FORM_URL.set(i);
        }
        // numeric characters
        for (int i = '0'; i <= '9'; i++) {
            WWW_FORM_URL.set(i);
        }
        // special chars
        WWW_FORM_URL.set('-');
        WWW_FORM_URL.set('_');
        WWW_FORM_URL.set('.');
        WWW_FORM_URL.set('*');
        // blank to be replaced with +
        WWW_FORM_URL.set(' ');
    }


    /**
     * Default constructor.
     */
    public URLCodec() {
        this(RequiredCharsetNames.UTF_8);
    }

    /**
     * Constructor which allows for the selection of a default charset
     * 
     * @param charset the default string charset to use.
     */
    public URLCodec(String charset) {
        super();
        this.charset = charset;
    }

    /**
     * Encodes an array of bytes into an array of URL safe 7-bit 
     * characters. Unsafe characters are escaped.
     *
     * @param urlsafe bitset of characters deemed URL safe
     * @param bytes array of bytes to convert to URL safe characters
     * @return array of bytes containing URL safe characters
     */
    public static final byte[] encodeUrl(BitSet urlsafe, byte[] bytes) 
    {
        if (bytes == null) {
            return null;
        }
        if (urlsafe == null) {
            urlsafe = WWW_FORM_URL;
        }
        
        ByteArrayOutputStream buffer = new ByteArrayOutputStream(); 
        for (int i = 0; i < bytes.length; i++) {
            int b = bytes[i];
            if (b < 0) {
                b = 256 + b;
            }
            if (urlsafe.get(b)) {
                if (b == ' ') {
                    b = '+';
                }
                buffer.write(b);
            } else {
                buffer.write('%');
                char hex1 = Character.toUpperCase(
                  Character.forDigit((b >> 4) & 0xF, RADIX));
                char hex2 = Character.toUpperCase(
                  Character.forDigit(b & 0xF, RADIX));
                buffer.write(hex1);
                buffer.write(hex2);
            }
        }
        return buffer.toByteArray(); 
    }


    /**
     * Decodes an array of URL safe 7-bit characters into an array of 
     * original bytes. Escaped characters are converted back to their 
     * original representation.
     *
     * @param bytes array of URL safe characters
     * @return array of original bytes 
     * @throws DecoderException Thrown if URL decoding is unsuccessful
     */
    public static final byte[] decodeUrl(byte[] bytes) throws DecoderException {
        if (bytes == null) {
            return null;
        }
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        for (int i = 0; i < bytes.length; i++) {
            int b = bytes[i];
            if (b == '+') {
                buffer.write(' ');
            } else if (b == '%') {
                try {
                    int u = toCharacterDigit(bytes[++i]);
                    int l = toCharacterDigit(bytes[++i]);
                    buffer.write((char) ((u << 4) + l));
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new DecoderException("Invalid URL encoding: ", e);
                }
            } else {
                buffer.write(b);
            }
        }
        return buffer.toByteArray();
    }

    private static int toCharacterDigit(byte b) throws DecoderException {
        int i = Character.digit((char) b, RADIX);
        if (i == -1) {
            throw new DecoderException("Invalid URL encoding: not a valid digit (radix " + RADIX + "): " + b);
        }
        return i;
    }
    /**
     * Encodes an array of bytes into an array of URL safe 7-bit 
     * characters. Unsafe characters are escaped.
     *
     * @param bytes array of bytes to convert to URL safe characters
     * @return array of bytes containing URL safe characters
     */
    public byte[] encode(byte[] bytes) {
        return encodeUrl(WWW_FORM_URL, bytes);
    }


    /**
     * Decodes an array of URL safe 7-bit characters into an array of 
     * original bytes. Escaped characters are converted back to their 
     * original representation.
     *
     * @param bytes array of URL safe characters
     * @return array of original bytes 
     * @throws DecoderException Thrown if URL decoding is unsuccessful
     */
    public byte[] decode(byte[] bytes) throws DecoderException {
        return decodeUrl(bytes);
    }

    /**
     * Encodes a string into its URL safe form using the specified string charset. Unsafe characters are escaped.
     * 
     * @param pString
     *            string to convert to a URL safe form
     * @param charset
     *            the charset for pString
     * @return URL safe string
     * @throws UnsupportedEncodingException
     *             Thrown if charset is not supported
     */
    public String encode(String pString, String charset) throws UnsupportedEncodingException {
        if (pString == null) {
            return null;
        }
        return StringBytesUtils.newStringUsAscii(encode(pString.getBytes(charset)));
    }

    /**
     * Encodes a string into its URL safe form using the default string 
     * charset. Unsafe characters are escaped.
     *
     * @param pString string to convert to a URL safe form
     * @return URL safe string
     * @throws EncoderException Thrown if URL encoding is unsuccessful
     * 
     * @see #getDefaultCharset()
     */
    public String encode(String pString) throws EncoderException {
        if (pString == null) {
            return null;
        }
        try {
            return encode(pString, getDefaultCharset());
        } catch (UnsupportedEncodingException e) {
            throw new EncoderException(e.getMessage());
        }
    }


    /**
     * Decodes a URL safe string into its original form using the 
     * specified encoding. Escaped characters are converted back 
     * to their original representation.
     *
     * @param pString URL safe string to convert into its original form
     * @param charset the original string charset
     * @return original string 
     * @throws DecoderException Thrown if URL decoding is unsuccessful
     * @throws UnsupportedEncodingException Thrown if charset is not
     *                                      supported 
     */
    public String decode(String pString, String charset) throws DecoderException, UnsupportedEncodingException {
        if (pString == null) {
            return null;
        }
        return new String(decode(StringBytesUtils.getBytesUsAscii(pString)), charset);
    }

    /**
     * Decodes a URL safe string into its original form using the default
     * string charset. Escaped characters are converted back to their 
     * original representation.
     *
     * @param pString URL safe string to convert into its original form
     * @return original string 
     * @throws DecoderException Thrown if URL decoding is unsuccessful
     * 
     * @see #getDefaultCharset()
     */
    public String decode(String pString) throws DecoderException {
        if (pString == null) {
            return null;
        }
        try {
            return decode(pString, getDefaultCharset());
        } catch (UnsupportedEncodingException e) {
            throw new DecoderException(e.getMessage());
        }
    }

    /**
     * Encodes an object into its URL safe form. Unsafe characters are 
     * escaped.
     *
     * @param pObject string to convert to a URL safe form
     * @return URL safe object
     * @throws EncoderException Thrown if URL encoding is not 
     *                          applicable to objects of this type or
     *                          if encoding is unsuccessful
     */
    public Object encode(Object pObject) throws EncoderException {
        if (pObject == null) {
            return null;
        } else if (pObject instanceof byte[]) {
            return encode((byte[])pObject);
        } else if (pObject instanceof String) {
            return encode((String)pObject);
        } else {
            throw new EncoderException("Objects of type " +
                pObject.getClass().getName() + " cannot be URL encoded"); 
              
        }
    }

    /**
     * Decodes a URL safe object into its original form. Escaped characters are converted back to their original
     * representation.
     * 
     * @param pObject
     *                  URL safe object to convert into its original form
     * @return original object
     * @throws DecoderException
     *                  Thrown if the argument is not a <code>String</code> or <code>byte[]</code>. Thrown if a failure condition is
     *                  encountered during the decode process.
     */
    public Object decode(Object pObject) throws DecoderException {
        if (pObject == null) {
            return null;
        } else if (pObject instanceof byte[]) {
            return decode((byte[]) pObject);
        } else if (pObject instanceof String) {
            return decode((String) pObject);
        } else {
            throw new DecoderException("Objects of type " + pObject.getClass().getName() + " cannot be URL decoded");

        }
    }

    /**
     * The <code>String</code> encoding used for decoding and encoding.
     * 
     * @return Returns the encoding.
     * 
     * @deprecated use #getDefaultCharset()
     */
    public String getEncoding() {
        return this.charset;
    }

    /**
     * The default charset used for string decoding and encoding.
     *
     * @return the default string charset.
     */
    public String getDefaultCharset() {
        return this.charset;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11216.java