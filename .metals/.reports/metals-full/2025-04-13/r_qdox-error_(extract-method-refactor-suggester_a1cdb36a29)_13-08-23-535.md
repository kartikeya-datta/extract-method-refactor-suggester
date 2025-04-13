error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12740.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12740.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12740.java
text:
```scala
d@@ictSize |= (coder.properties[i + 1] & 0xffl) << (8 * i);

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
package org.apache.commons.compress.archivers.sevenz;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.tukaani.xz.LZMAInputStream;

class Coders {
    static InputStream addDecoder(final InputStream is,
            final Coder coder, final byte[] password) throws IOException {
        for (final CoderId coderId : coderTable) {
            if (Arrays.equals(coderId.method.getId(), coder.decompressionMethodId)) {
                return coderId.coder.decode(is, coder, password);
            }
        }
        throw new IOException("Unsupported compression method " +
                Arrays.toString(coder.decompressionMethodId));
    }
    
    static OutputStream addEncoder(final OutputStream out, final SevenZMethod method,
                                   final byte[] password) throws IOException {
        for (final CoderId coderId : coderTable) {
            if (coderId.method.equals(method)) {
                return coderId.coder.encode(out, password);
            }
        }
        throw new IOException("Unsupported compression method " + method);
    }

    static CoderId[] coderTable = new CoderId[] {
        new CoderId(SevenZMethod.COPY, new CopyDecoder()),
        new CoderId(SevenZMethod.LZMA, new LZMADecoder()),
        new CoderId(SevenZMethod.LZMA2, new LZMA2Decoder()),
        new CoderId(SevenZMethod.DEFLATE, new DeflateDecoder()),
        new CoderId(SevenZMethod.BZIP2, new BZIP2Decoder()),
        new CoderId(SevenZMethod.AES256SHA256, new AES256SHA256Decoder())
    };
    
    static class CoderId {
        CoderId(SevenZMethod method, final CoderBase coder) {
            this.method = method;
            this.coder = coder;
        }

        final SevenZMethod method;
        final CoderBase coder;
    }
    
    static abstract class CoderBase {
        abstract InputStream decode(final InputStream in, final Coder coder,
                byte[] password) throws IOException;
        OutputStream encode(final OutputStream out, final byte[] password)
            throws IOException {
            throw new UnsupportedOperationException("method doesn't support writing");
        }
    }
    
    static class CopyDecoder extends CoderBase {
        @Override
        InputStream decode(final InputStream in, final Coder coder,
                byte[] password) throws IOException {
            return in; 
        }
        @Override
        OutputStream encode(final OutputStream out, final byte[] password) {
            return out;
        }
    }

    static class LZMADecoder extends CoderBase {
        @Override
        InputStream decode(final InputStream in, final Coder coder,
                byte[] password) throws IOException {
            byte propsByte = coder.properties[0];
            long dictSize = coder.properties[1];
            for (int i = 1; i < 4; i++) {
                dictSize |= (coder.properties[i + 1] << (8 * i));
            }
            if (dictSize > LZMAInputStream.DICT_SIZE_MAX) {
                throw new IOException("Dictionary larger than 4GiB maximum size");
            }
            return new LZMAInputStream(in, -1, propsByte, (int) dictSize);
        }
    }
    
    static class DeflateDecoder extends CoderBase {
        @Override
        InputStream decode(final InputStream in, final Coder coder, final byte[] password)
            throws IOException {
            return new InflaterInputStream(new DummyByteAddingInputStream(in),
                                           new Inflater(true));
        }
        @Override
        OutputStream encode(final OutputStream out, final byte[] password) {
            return new DeflaterOutputStream(out, new Deflater(9, true));
        }
    }

    static class BZIP2Decoder extends CoderBase {
        @Override
        InputStream decode(final InputStream in, final Coder coder, final byte[] password)
                throws IOException {
            return new BZip2CompressorInputStream(in);
        }
        @Override
        OutputStream encode(final OutputStream out, final byte[] password)
                throws IOException {
            return new BZip2CompressorOutputStream(out);
        }
    }

    static class AES256SHA256Decoder extends CoderBase {
        @Override
        InputStream decode(final InputStream in, final Coder coder,
                final byte[] passwordBytes) throws IOException {
            return new InputStream() {
                private boolean isInitialized = false;
                private CipherInputStream cipherInputStream = null;
                
                private CipherInputStream init() throws IOException {
                    if (isInitialized) {
                        return cipherInputStream;
                    }
                    final int byte0 = 0xff & coder.properties[0];
                    final int numCyclesPower = byte0 & 0x3f;
                    final int byte1 = 0xff & coder.properties[1];
                    final int ivSize = ((byte0 >> 6) & 1) + (byte1 & 0x0f);
                    final int saltSize = ((byte0 >> 7) & 1) + (byte1 >> 4);
                    if (2 + saltSize + ivSize > coder.properties.length) {
                        throw new IOException("Salt size + IV size too long");
                    }
                    final byte[] salt = new byte[saltSize];
                    System.arraycopy(coder.properties, 2, salt, 0, saltSize);
                    final byte[] iv = new byte[16];
                    System.arraycopy(coder.properties, 2 + saltSize, iv, 0, ivSize);
                    
                    if (passwordBytes == null) {
                        throw new IOException("Cannot read encrypted files without a password");
                    }
                    final byte[] aesKeyBytes;
                    if (numCyclesPower == 0x3f) {
                        aesKeyBytes = new byte[32];
                        System.arraycopy(salt, 0, aesKeyBytes, 0, saltSize);
                        System.arraycopy(passwordBytes, 0, aesKeyBytes, saltSize,
                                Math.min(passwordBytes.length, aesKeyBytes.length - saltSize));
                    } else {
                        final MessageDigest digest;
                        try {
                            digest = MessageDigest.getInstance("SHA-256");
                        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                            IOException ioe = new IOException("SHA-256 is unsupported by your Java implementation");
                            ioe.initCause(noSuchAlgorithmException);
                            throw ioe;
        // TODO: simplify when Compress requires Java 1.6                
//                            throw new IOException("SHA-256 is unsupported by your Java implementation",
//                                    noSuchAlgorithmException);
                        }
                        final byte[] extra = new byte[8];
                        for (long j = 0; j < (1L << numCyclesPower); j++) {
                            digest.update(salt);
                            digest.update(passwordBytes);
                            digest.update(extra);
                            for (int k = 0; k < extra.length; k++) {
                                ++extra[k];
                                if (extra[k] != 0) {
                                    break;
                                }
                            }
                        }
                        aesKeyBytes = digest.digest();
                    }
                    
                    final SecretKey aesKey = new SecretKeySpec(aesKeyBytes, "AES");
                    try {
                        final Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
                        cipher.init(Cipher.DECRYPT_MODE, aesKey, new IvParameterSpec(iv));
                        cipherInputStream = new CipherInputStream(in, cipher);
                        isInitialized = true;
                        return cipherInputStream;
                    } catch (GeneralSecurityException generalSecurityException) {
                        IOException ioe = new IOException("Decryption error " +
                                "(do you have the JCE Unlimited Strength Jurisdiction Policy Files installed?)");
                        ioe.initCause(generalSecurityException);
                        throw ioe;
        // TODO: simplify when Compress requires Java 1.6                
//                        throw new IOException("Decryption error " +
//                                "(do you have the JCE Unlimited Strength Jurisdiction Policy Files installed?)",
//                                generalSecurityException);
                    }
                }
                
                @Override
                public int read() throws IOException {
                    return init().read();
                }
                
                @Override
                public int read(byte[] b, int off, int len) throws IOException {
                    return init().read(b, off, len);
                }
                
                @Override
                public void close() {
                }
            };
        }
    }

    /**
     * ZLIB requires an extra dummy byte.
     *
     * @see java.util.zip.Inflater#Inflater(boolean)
     * @see org.apache.commons.compress.archivers.zip.ZipFile.BoundedInputStream
     */
    private static class DummyByteAddingInputStream extends FilterInputStream {
        private boolean addDummyByte = true;

        private DummyByteAddingInputStream(InputStream in) {
            super(in);
        }

        @Override
        public int read() throws IOException {
            int result = super.read();
            if (result == -1 && addDummyByte) {
                addDummyByte = false;
                result = 0;
            }
            return result;
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            int result = super.read(b, off, len);
            if (result == -1 && addDummyByte) {
                addDummyByte = false;
                b[off] = 0;
                return 1;
            }
            return result;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12740.java