error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10577.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10577.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10577.java
text:
```scala
t@@hrow new RuntimeException(e);

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

package org.apache.commons.codec.digest;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;

/**
 * Operations to simplify common {@link java.security.MessageDigest} tasks. This class is thread safe.
 *
 * @version $Id$
 */
public class DigestUtils {

    private static final int STREAM_BUFFER_LENGTH = 1024;

    /**
     * Read through an InputStream and returns the digest for the data
     *
     * @param digest
     *            The MessageDigest to use (e.g. MD5)
     * @param data
     *            Data to digest
     * @return MD5 digest
     * @throws IOException
     *             On error reading from the stream
     */
    private static byte[] digest(MessageDigest digest, InputStream data) throws IOException {
        byte[] buffer = new byte[STREAM_BUFFER_LENGTH];
        int read = data.read(buffer, 0, STREAM_BUFFER_LENGTH);

        while (read > -1) {
            digest.update(buffer, 0, read);
            read = data.read(buffer, 0, STREAM_BUFFER_LENGTH);
        }

        return digest.digest();
    }

    /**
     * Calls {@link StringUtils#getBytesUtf8(String)}
     *
     * @param data
     *            the String to encode
     * @return encoded bytes
     */
    private static byte[] getBytesUtf8(String data) {
        return StringUtils.getBytesUtf8(data);
    }

    /**
     * Returns a <code>MessageDigest</code> for the given <code>algorithm</code>.
     *
     * @param algorithm
     *            the name of the algorithm requested. See <a
     *            href="http://java.sun.com/j2se/1.3/docs/guide/security/CryptoSpec.html#AppA">Appendix A in the Java
     *            Cryptography Architecture API Specification & Reference</a> for information about standard algorithm
     *            names.
     * @return An MD5 digest instance.
     * @see MessageDigest#getInstance(String)
     * @throws RuntimeException
     *             when a {@link java.security.NoSuchAlgorithmException} is caught.
     */
    public static MessageDigest getDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Returns an MD5 MessageDigest.
     *
     * @return An MD5 digest instance.
     * @throws RuntimeException
     *             when a {@link java.security.NoSuchAlgorithmException} is caught.
     */
    public static MessageDigest getMd5Digest() {
        return getDigest(MessageDigestAlgorithms.MD5);
    }

    /**
     * Returns an SHA-256 digest.
     * <p>
     * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
     * </p>
     *
     * @return An SHA-256 digest instance.
     * @throws RuntimeException
     *             when a {@link java.security.NoSuchAlgorithmException} is caught.
     */
    public static MessageDigest getSha256Digest() {
        return getDigest(MessageDigestAlgorithms.SHA_256);
    }

    /**
     * Returns an SHA-384 digest.
     * <p>
     * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
     * </p>
     *
     * @return An SHA-384 digest instance.
     * @throws RuntimeException
     *             when a {@link java.security.NoSuchAlgorithmException} is caught.
     */
    public static MessageDigest getSha384Digest() {
        return getDigest(MessageDigestAlgorithms.SHA_384);
    }

    /**
     * Returns an SHA-512 digest.
     * <p>
     * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
     * </p>
     *
     * @return An SHA-512 digest instance.
     * @throws RuntimeException
     *             when a {@link java.security.NoSuchAlgorithmException} is caught.
     */
    public static MessageDigest getSha512Digest() {
        return getDigest(MessageDigestAlgorithms.SHA_512);
    }

    /**
     * Returns an SHA-1 digest.
     *
     * @return An SHA-1 digest instance.
     * @throws RuntimeException
     *             when a {@link java.security.NoSuchAlgorithmException} is caught.
     */
    public static MessageDigest getShaDigest() {
        return getDigest("SHA");
    }

    /**
     * Calculates the MD5 digest and returns the value as a 16 element <code>byte[]</code>.
     *
     * @param data
     *            Data to digest
     * @return MD5 digest
     */
    public static byte[] md5(byte[] data) {
        return getMd5Digest().digest(data);
    }

    /**
     * Calculates the MD5 digest and returns the value as a 16 element <code>byte[]</code>.
     *
     * @param data
     *            Data to digest
     * @return MD5 digest
     * @throws IOException
     *             On error reading from the stream
     * @since 1.4
     */
    public static byte[] md5(InputStream data) throws IOException {
        return digest(getMd5Digest(), data);
    }

    /**
     * Calculates the MD5 digest and returns the value as a 16 element <code>byte[]</code>.
     *
     * @param data
     *            Data to digest
     * @return MD5 digest
     */
    public static byte[] md5(String data) {
        return md5(getBytesUtf8(data));
    }

    /**
     * Calculates the MD5 digest and returns the value as a 32 character hex string.
     *
     * @param data
     *            Data to digest
     * @return MD5 digest as a hex string
     */
    public static String md5Hex(byte[] data) {
        return Hex.encodeHexString(md5(data));
    }

    /**
     * Calculates the MD5 digest and returns the value as a 32 character hex string.
     *
     * @param data
     *            Data to digest
     * @return MD5 digest as a hex string
     * @throws IOException
     *             On error reading from the stream
     * @since 1.4
     */
    public static String md5Hex(InputStream data) throws IOException {
        return Hex.encodeHexString(md5(data));
    }

    /**
     * Calculates the MD5 digest and returns the value as a 32 character hex string.
     *
     * @param data
     *            Data to digest
     * @return MD5 digest as a hex string
     */
    public static String md5Hex(String data) {
        return Hex.encodeHexString(md5(data));
    }

    /**
     * Calculates the SHA-1 digest and returns the value as a <code>byte[]</code>.
     *
     * @param data
     *            Data to digest
     * @return SHA-1 digest
     */
    public static byte[] sha(byte[] data) {
        return getShaDigest().digest(data);
    }

    /**
     * Calculates the SHA-1 digest and returns the value as a <code>byte[]</code>.
     *
     * @param data
     *            Data to digest
     * @return SHA-1 digest
     * @throws IOException
     *             On error reading from the stream
     * @since 1.4
     */
    public static byte[] sha(InputStream data) throws IOException {
        return digest(getShaDigest(), data);
    }

    /**
     * Calculates the SHA-1 digest and returns the value as a <code>byte[]</code>.
     *
     * @param data
     *            Data to digest
     * @return SHA-1 digest
     */
    public static byte[] sha(String data) {
        return sha(getBytesUtf8(data));
    }

    /**
     * Calculates the SHA-256 digest and returns the value as a <code>byte[]</code>.
     * <p>
     * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
     * </p>
     *
     * @param data
     *            Data to digest
     * @return SHA-256 digest
     * @since 1.4
     */
    public static byte[] sha256(byte[] data) {
        return getSha256Digest().digest(data);
    }

    /**
     * Calculates the SHA-256 digest and returns the value as a <code>byte[]</code>.
     * <p>
     * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
     * </p>
     *
     * @param data
     *            Data to digest
     * @return SHA-256 digest
     * @throws IOException
     *             On error reading from the stream
     * @since 1.4
     */
    public static byte[] sha256(InputStream data) throws IOException {
        return digest(getSha256Digest(), data);
    }

    /**
     * Calculates the SHA-256 digest and returns the value as a <code>byte[]</code>.
     * <p>
     * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
     * </p>
     *
     * @param data
     *            Data to digest
     * @return SHA-256 digest
     * @since 1.4
     */
    public static byte[] sha256(String data) {
        return sha256(getBytesUtf8(data));
    }

    /**
     * Calculates the SHA-256 digest and returns the value as a hex string.
     * <p>
     * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
     * </p>
     *
     * @param data
     *            Data to digest
     * @return SHA-256 digest as a hex string
     * @since 1.4
     */
    public static String sha256Hex(byte[] data) {
        return Hex.encodeHexString(sha256(data));
    }

    /**
     * Calculates the SHA-256 digest and returns the value as a hex string.
     * <p>
     * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
     * </p>
     *
     * @param data
     *            Data to digest
     * @return SHA-256 digest as a hex string
     * @throws IOException
     *             On error reading from the stream
     * @since 1.4
     */
    public static String sha256Hex(InputStream data) throws IOException {
        return Hex.encodeHexString(sha256(data));
    }

    /**
     * Calculates the SHA-256 digest and returns the value as a hex string.
     * <p>
     * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
     * </p>
     *
     * @param data
     *            Data to digest
     * @return SHA-256 digest as a hex string
     * @since 1.4
     */
    public static String sha256Hex(String data) {
        return Hex.encodeHexString(sha256(data));
    }

    /**
     * Calculates the SHA-384 digest and returns the value as a <code>byte[]</code>.
     * <p>
     * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
     * </p>
     *
     * @param data
     *            Data to digest
     * @return SHA-384 digest
     * @since 1.4
     */
    public static byte[] sha384(byte[] data) {
        return getSha384Digest().digest(data);
    }

    /**
     * Calculates the SHA-384 digest and returns the value as a <code>byte[]</code>.
     * <p>
     * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
     * </p>
     *
     * @param data
     *            Data to digest
     * @return SHA-384 digest
     * @throws IOException
     *             On error reading from the stream
     * @since 1.4
     */
    public static byte[] sha384(InputStream data) throws IOException {
        return digest(getSha384Digest(), data);
    }

    /**
     * Calculates the SHA-384 digest and returns the value as a <code>byte[]</code>.
     * <p>
     * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
     * </p>
     *
     * @param data
     *            Data to digest
     * @return SHA-384 digest
     * @since 1.4
     */
    public static byte[] sha384(String data) {
        return sha384(getBytesUtf8(data));
    }

    /**
     * Calculates the SHA-384 digest and returns the value as a hex string.
     * <p>
     * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
     * </p>
     *
     * @param data
     *            Data to digest
     * @return SHA-384 digest as a hex string
     * @since 1.4
     */
    public static String sha384Hex(byte[] data) {
        return Hex.encodeHexString(sha384(data));
    }

    /**
     * Calculates the SHA-384 digest and returns the value as a hex string.
     * <p>
     * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
     * </p>
     *
     * @param data
     *            Data to digest
     * @return SHA-384 digest as a hex string
     * @throws IOException
     *             On error reading from the stream
     * @since 1.4
     */
    public static String sha384Hex(InputStream data) throws IOException {
        return Hex.encodeHexString(sha384(data));
    }

    /**
     * Calculates the SHA-384 digest and returns the value as a hex string.
     * <p>
     * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
     * </p>
     *
     * @param data
     *            Data to digest
     * @return SHA-384 digest as a hex string
     * @since 1.4
     */
    public static String sha384Hex(String data) {
        return Hex.encodeHexString(sha384(data));
    }

    /**
     * Calculates the SHA-512 digest and returns the value as a <code>byte[]</code>.
     * <p>
     * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
     * </p>
     *
     * @param data
     *            Data to digest
     * @return SHA-512 digest
     * @since 1.4
     */
    public static byte[] sha512(byte[] data) {
        return getSha512Digest().digest(data);
    }

    /**
     * Calculates the SHA-512 digest and returns the value as a <code>byte[]</code>.
     * <p>
     * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
     * </p>
     *
     * @param data
     *            Data to digest
     * @return SHA-512 digest
     * @throws IOException
     *             On error reading from the stream
     * @since 1.4
     */
    public static byte[] sha512(InputStream data) throws IOException {
        return digest(getSha512Digest(), data);
    }

    /**
     * Calculates the SHA-512 digest and returns the value as a <code>byte[]</code>.
     * <p>
     * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
     * </p>
     *
     * @param data
     *            Data to digest
     * @return SHA-512 digest
     * @since 1.4
     */
    public static byte[] sha512(String data) {
        return sha512(getBytesUtf8(data));
    }

    /**
     * Calculates the SHA-512 digest and returns the value as a hex string.
     * <p>
     * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
     * </p>
     *
     * @param data
     *            Data to digest
     * @return SHA-512 digest as a hex string
     * @since 1.4
     */
    public static String sha512Hex(byte[] data) {
        return Hex.encodeHexString(sha512(data));
    }

    /**
     * Calculates the SHA-512 digest and returns the value as a hex string.
     * <p>
     * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
     * </p>
     *
     * @param data
     *            Data to digest
     * @return SHA-512 digest as a hex string
     * @throws IOException
     *             On error reading from the stream
     * @since 1.4
     */
    public static String sha512Hex(InputStream data) throws IOException {
        return Hex.encodeHexString(sha512(data));
    }

    /**
     * Calculates the SHA-512 digest and returns the value as a hex string.
     * <p>
     * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
     * </p>
     *
     * @param data
     *            Data to digest
     * @return SHA-512 digest as a hex string
     * @since 1.4
     */
    public static String sha512Hex(String data) {
        return Hex.encodeHexString(sha512(data));
    }

    /**
     * Calculates the SHA-1 digest and returns the value as a hex string.
     *
     * @param data
     *            Data to digest
     * @return SHA-1 digest as a hex string
     */
    public static String shaHex(byte[] data) {
        return Hex.encodeHexString(sha(data));
    }

    /**
     * Calculates the SHA-1 digest and returns the value as a hex string.
     *
     * @param data
     *            Data to digest
     * @return SHA-1 digest as a hex string
     * @throws IOException
     *             On error reading from the stream
     * @since 1.4
     */
    public static String shaHex(InputStream data) throws IOException {
        return Hex.encodeHexString(sha(data));
    }

    /**
     * Calculates the SHA-1 digest and returns the value as a hex string.
     *
     * @param data
     *            Data to digest
     * @return SHA-1 digest as a hex string
     */
    public static String shaHex(String data) {
        return Hex.encodeHexString(sha(data));
    }

    /**
     * Updates the given {@link MessageDigest}.
     *
     * @param messageDigest
     *            the {@link MessageDigest} to update
     * @param valueToDigest
     *            the value to update the {@link MessageDigest} with
     * @return the updated {@link MessageDigest}
     * @since 1.7
     */
    public static MessageDigest updateDigest(final MessageDigest messageDigest, final String valueToDigest) {
        messageDigest.update(getBytesUtf8(valueToDigest));
        return messageDigest;
    }

    /**
     * Updates the given {@link MessageDigest}.
     *
     * @param messageDigest
     *            the {@link MessageDigest} to update
     * @param valueToDigest
     *            the value to update the {@link MessageDigest} with
     * @return the updated {@link MessageDigest}
     * @since 1.7
     */
    public static MessageDigest updateDigest(final MessageDigest messageDigest, byte[] valueToDigest) {
        messageDigest.update(valueToDigest);
        return messageDigest;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10577.java