error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5929.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5929.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5929.java
text:
```scala
D@@igestUtils.sha1Hex(new ByteArrayInputStream(testData)));

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Random;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.junit.Test;

/**
 * Tests DigestUtils methods.
 *
 * @version $Id$
 */
public class DigestUtilsTest {

    private final byte[] testData = new byte[1024*1024];

    private byte[] getBytesUtf8(String hashMe) {
        return StringUtils.getBytesUtf8(hashMe);
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        new Random().nextBytes(testData);
    }

    @Test
    public void testConstructable() {
        assertNotNull(new DigestUtils());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testInternalNoSuchAlgorithmException() {
        DigestUtils.getDigest("Bogus Bogus");
    }

    @Test
    public void testMd2Hex() throws IOException {
        // Examples from RFC 1319
        assertEquals("8350e5a3e24c153df2275c9f80692773", DigestUtils.md2Hex(""));

        assertEquals("32ec01ec4a6dac72c0ab96fb34c0b5d1", DigestUtils.md2Hex("a"));

        assertEquals("da853b0d3f88d99b30283a69e6ded6bb", DigestUtils.md2Hex("abc"));

        assertEquals("ab4f496bfb2a530b219ff33031fe06b0", DigestUtils.md2Hex("message digest"));

        assertEquals("4e8ddff3650292ab5a4108c3aa47940b", DigestUtils.md2Hex("abcdefghijklmnopqrstuvwxyz"));

        assertEquals(
            "da33def2a42df13975352846c30338cd",
            DigestUtils.md2Hex("ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvwxyz" + "0123456789"));

        assertEquals(
            "d5976f79d83d3a0dc9806c3c66f3efd8",
            DigestUtils.md2Hex("1234567890123456789012345678901234567890" + "1234567890123456789012345678901234567890"));

        assertEquals(DigestUtils.md2Hex(testData),
                DigestUtils.md2Hex(new ByteArrayInputStream(testData)));
    }

    @Test
    public void testMd5Hex() throws IOException {
        // Examples from RFC 1321
        assertEquals("d41d8cd98f00b204e9800998ecf8427e", DigestUtils.md5Hex(""));

        assertEquals("0cc175b9c0f1b6a831c399e269772661", DigestUtils.md5Hex("a"));

        assertEquals("900150983cd24fb0d6963f7d28e17f72", DigestUtils.md5Hex("abc"));

        assertEquals("f96b697d7cb7938d525a2f31aaf161d0", DigestUtils.md5Hex("message digest"));

        assertEquals("c3fcd3d76192e4007dfb496cca67e13b", DigestUtils.md5Hex("abcdefghijklmnopqrstuvwxyz"));

        assertEquals(
            "d174ab98d277d9f5a5611c2c9f419d9f",
            DigestUtils.md5Hex("ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvwxyz" + "0123456789"));

        assertEquals(
            "57edf4a22be3c955ac49da2e2107b67a",
            DigestUtils.md5Hex("1234567890123456789012345678901234567890" + "1234567890123456789012345678901234567890"));

        assertEquals(DigestUtils.md5Hex(testData),
                DigestUtils.md5Hex(new ByteArrayInputStream(testData)));
    }

    /**
     * An MD2 hash converted to hex should always be 32 characters.
     */
    @Test
    public void testMd2HexLength() {
        String hashMe = "this is some string that is longer than 32 characters";
        String hash = DigestUtils.md2Hex(getBytesUtf8(hashMe));
        assertEquals(32, hash.length());

        hashMe = "length < 32";
        hash = DigestUtils.md2Hex(getBytesUtf8(hashMe));
        assertEquals(32, hash.length());
    }

    /**
     * An MD5 hash converted to hex should always be 32 characters.
     */
    @Test
    public void testMd5HexLength() {
        String hashMe = "this is some string that is longer than 32 characters";
        String hash = DigestUtils.md5Hex(getBytesUtf8(hashMe));
        assertEquals(32, hash.length());

        hashMe = "length < 32";
        hash = DigestUtils.md5Hex(getBytesUtf8(hashMe));
        assertEquals(32, hash.length());
    }

    /**
     * An MD2 hash should always be a 16 element byte[].
     */
    @Test
    public void testMd2Length() {
        String hashMe = "this is some string that is longer than 16 characters";
        byte[] hash = DigestUtils.md2(getBytesUtf8(hashMe));
        assertEquals(16, hash.length);

        hashMe = "length < 16";
        hash = DigestUtils.md2(getBytesUtf8(hashMe));
        assertEquals(16, hash.length);
    }

    /**
     * An MD5 hash should always be a 16 element byte[].
     */
    @Test
    public void testMd5Length() {
        String hashMe = "this is some string that is longer than 16 characters";
        byte[] hash = DigestUtils.md5(getBytesUtf8(hashMe));
        assertEquals(16, hash.length);

        hashMe = "length < 16";
        hash = DigestUtils.md5(getBytesUtf8(hashMe));
        assertEquals(16, hash.length);
    }

    @Test
    public void testSha256() throws IOException {
    // Examples from FIPS 180-2
    assertEquals("ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad",
             DigestUtils.sha256Hex("abc"));
    assertEquals("ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad",
             DigestUtils.sha256Hex(getBytesUtf8("abc")));
    assertEquals("248d6a61d20638b8e5c026930c3e6039a33ce45964ff2167f6ecedd419db06c1",
             DigestUtils.sha256Hex("abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq"));

    assertEquals(DigestUtils.sha256Hex(testData),
            DigestUtils.sha256Hex(new ByteArrayInputStream(testData)));
    }

    @Test
    public void testSha384() throws IOException {
    // Examples from FIPS 180-2
    assertEquals("cb00753f45a35e8bb5a03d699ac65007272c32ab0eded1631a8b605a43ff5bed" +
             "8086072ba1e7cc2358baeca134c825a7",
             DigestUtils.sha384Hex("abc"));
    assertEquals("cb00753f45a35e8bb5a03d699ac65007272c32ab0eded1631a8b605a43ff5bed" +
             "8086072ba1e7cc2358baeca134c825a7",
             DigestUtils.sha384Hex(getBytesUtf8("abc")));
    assertEquals("09330c33f71147e83d192fc782cd1b4753111b173b3b05d22fa08086e3b0f712" +
            "fcc7c71a557e2db966c3e9fa91746039",
             DigestUtils.sha384Hex("abcdefghbcdefghicdefghijdefghijkefghijklfghijklmghijklmn" +
                       "hijklmnoijklmnopjklmnopqklmnopqrlmnopqrsmnopqrstnopqrstu"));
    assertEquals(DigestUtils.sha384Hex(testData),
            DigestUtils.sha384Hex(new ByteArrayInputStream(testData)));
    }

    @Test
    public void testSha512() throws IOException {
    // Examples from FIPS 180-2
    assertEquals("ddaf35a193617abacc417349ae20413112e6fa4e89a97ea20a9eeee64b55d39a" +
            "2192992a274fc1a836ba3c23a3feebbd454d4423643ce80e2a9ac94fa54ca49f",
             DigestUtils.sha512Hex("abc"));
    assertEquals("ddaf35a193617abacc417349ae20413112e6fa4e89a97ea20a9eeee64b55d39a" +
             "2192992a274fc1a836ba3c23a3feebbd454d4423643ce80e2a9ac94fa54ca49f",
             DigestUtils.sha512Hex(getBytesUtf8("abc")));
    assertEquals("8e959b75dae313da8cf4f72814fc143f8f7779c6eb9f7fa17299aeadb6889018" +
             "501d289e4900f7e4331b99dec4b5433ac7d329eeb6dd26545e96e55b874be909",
             DigestUtils.sha512Hex("abcdefghbcdefghicdefghijdefghijkefghijklfghijklmghijklmn" +
                       "hijklmnoijklmnopjklmnopqklmnopqrlmnopqrsmnopqrstnopqrstu"));
    assertEquals(DigestUtils.sha512Hex(testData),
            DigestUtils.sha512Hex(new ByteArrayInputStream(testData)));
}

    @Test
    public void testShaHex() throws IOException {
        // Examples from FIPS 180-1
        assertEquals("a9993e364706816aba3e25717850c26c9cd0d89d", DigestUtils.shaHex("abc"));

        assertEquals("a9993e364706816aba3e25717850c26c9cd0d89d", DigestUtils.shaHex(getBytesUtf8("abc")));

        assertEquals(
            "84983e441c3bd26ebaae4aa1f95129e5e54670f1",
            DigestUtils.shaHex("abcdbcdecdefdefgefghfghighij" + "hijkijkljklmklmnlmnomnopnopq"));
        assertEquals(DigestUtils.shaHex(testData),
                DigestUtils.shaHex(new ByteArrayInputStream(testData)));
    }

    @Test
    public void testSha1Hex() throws IOException {
        // Examples from FIPS 180-1
        assertEquals("a9993e364706816aba3e25717850c26c9cd0d89d", DigestUtils.sha1Hex("abc"));

        assertEquals("a9993e364706816aba3e25717850c26c9cd0d89d", DigestUtils.sha1Hex(getBytesUtf8("abc")));

        assertEquals(
            "84983e441c3bd26ebaae4aa1f95129e5e54670f1",
            DigestUtils.shaHex("abcdbcdecdefdefgefghfghighij" + "hijkijkljklmklmnlmnomnopnopq"));
        assertEquals(DigestUtils.shaHex(testData),
                DigestUtils.shaHex(new ByteArrayInputStream(testData)));
    }

    @Test
    public void testSha1UpdateWithByteArray(){
        final String d1 = "C'est un homme qui rentre dans un café, et plouf";
        final String d2 = "C'est un homme, c'est qu'une tête, on lui offre un cadeau: 'oh... encore un chapeau!'";

        MessageDigest messageDigest = DigestUtils.getSha1Digest();
        messageDigest.update(d1.getBytes());
        messageDigest.update(d2.getBytes());
        final String expectedResult = Hex.encodeHexString(messageDigest.digest());

        messageDigest = DigestUtils.getSha1Digest();
        DigestUtils.updateDigest(messageDigest, d1.getBytes());
        DigestUtils.updateDigest(messageDigest, d2.getBytes());
        final String actualResult = Hex.encodeHexString(messageDigest.digest());

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testShaUpdateWithByteArray(){
        final String d1 = "C'est un homme qui rentre dans un café, et plouf";
        final String d2 = "C'est un homme, c'est qu'une tête, on lui offre un cadeau: 'oh... encore un chapeau!'";

        MessageDigest messageDigest = DigestUtils.getShaDigest();
        messageDigest.update(d1.getBytes());
        messageDigest.update(d2.getBytes());
        final String expectedResult = Hex.encodeHexString(messageDigest.digest());

        messageDigest = DigestUtils.getShaDigest();
        DigestUtils.updateDigest(messageDigest, d1.getBytes());
        DigestUtils.updateDigest(messageDigest, d2.getBytes());
        final String actualResult = Hex.encodeHexString(messageDigest.digest());

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testSha1UpdateWithString(){
        final String d1 = "C'est un homme qui rentre dans un café, et plouf";
        final String d2 = "C'est un homme, c'est qu'une tête, on lui offre un cadeau: 'oh... encore un chapeau!'";

        MessageDigest messageDigest = DigestUtils.getSha1Digest();
        messageDigest.update(StringUtils.getBytesUtf8(d1));
        messageDigest.update(StringUtils.getBytesUtf8(d2));
        final String expectedResult = Hex.encodeHexString(messageDigest.digest());

        messageDigest = DigestUtils.getSha1Digest();
        DigestUtils.updateDigest(messageDigest, d1);
        DigestUtils.updateDigest(messageDigest, d2);
        final String actualResult = Hex.encodeHexString(messageDigest.digest());

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testShaUpdateWithString(){
        final String d1 = "C'est un homme qui rentre dans un café, et plouf";
        final String d2 = "C'est un homme, c'est qu'une tête, on lui offre un cadeau: 'oh... encore un chapeau!'";

        MessageDigest messageDigest = DigestUtils.getShaDigest();
        messageDigest.update(StringUtils.getBytesUtf8(d1));
        messageDigest.update(StringUtils.getBytesUtf8(d2));
        final String expectedResult = Hex.encodeHexString(messageDigest.digest());

        messageDigest = DigestUtils.getShaDigest();
        DigestUtils.updateDigest(messageDigest, d1);
        DigestUtils.updateDigest(messageDigest, d2);
        final String actualResult = Hex.encodeHexString(messageDigest.digest());

        assertEquals(expectedResult, actualResult);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5929.java