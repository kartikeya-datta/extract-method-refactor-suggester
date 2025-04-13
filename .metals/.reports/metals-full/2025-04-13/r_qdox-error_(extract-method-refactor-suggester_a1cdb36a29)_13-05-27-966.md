error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5976.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5976.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5976.java
text:
```scala
S@@tringBuilder buffer = new StringBuilder();

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.Test;

/**
 * URL codec test cases
 * 
 * @version $Id$
 */
public class URLCodecTest {
    
    static final int SWISS_GERMAN_STUFF_UNICODE [] = {
        0x47, 0x72, 0xFC, 0x65, 0x7A, 0x69, 0x5F, 0x7A, 0xE4, 0x6D, 0xE4
    };
    
    static final int RUSSIAN_STUFF_UNICODE [] = {
        0x412, 0x441, 0x435, 0x43C, 0x5F, 0x43F, 0x440, 0x438, 
        0x432, 0x435, 0x442 
    }; 

    private void validateState(URLCodec urlCodec) {
        // no tests for now.
    }
    
    private String constructString(int [] unicodeChars) {
        StringBuffer buffer = new StringBuffer();
        if (unicodeChars != null) {
            for (int unicodeChar : unicodeChars) {
                buffer.append((char)unicodeChar); 
            }
        }
        return buffer.toString();
    }
    
    @Test
    public void testUTF8RoundTrip() throws Exception {

        String ru_msg = constructString(RUSSIAN_STUFF_UNICODE); 
        String ch_msg = constructString(SWISS_GERMAN_STUFF_UNICODE); 
        
        URLCodec urlCodec = new URLCodec();
        this.validateState(urlCodec);
        
        assertEquals(
            "%D0%92%D1%81%D0%B5%D0%BC_%D0%BF%D1%80%D0%B8%D0%B2%D0%B5%D1%82", 
            urlCodec.encode(ru_msg, CharEncoding.UTF_8)
        );
        assertEquals("Gr%C3%BCezi_z%C3%A4m%C3%A4", urlCodec.encode(ch_msg, CharEncoding.UTF_8));
        
        assertEquals(ru_msg, urlCodec.decode(urlCodec.encode(ru_msg, CharEncoding.UTF_8), CharEncoding.UTF_8));
        assertEquals(ch_msg, urlCodec.decode(urlCodec.encode(ch_msg, CharEncoding.UTF_8), CharEncoding.UTF_8));
        this.validateState(urlCodec);
    }

    @Test
    public void testBasicEncodeDecode() throws Exception {
        URLCodec urlCodec = new URLCodec();
        String plain = "Hello there!";
        String encoded = urlCodec.encode(plain);
        assertEquals("Basic URL encoding test", 
            "Hello+there%21", encoded);
        assertEquals("Basic URL decoding test", 
            plain, urlCodec.decode(encoded));
        this.validateState(urlCodec);
    }


    @Test
    public void testSafeCharEncodeDecode() throws Exception {
        URLCodec urlCodec = new URLCodec();
        String plain = "abc123_-.*";
        String encoded = urlCodec.encode(plain);
        assertEquals("Safe chars URL encoding test", 
            plain, encoded);
        assertEquals("Safe chars URL decoding test", 
            plain, urlCodec.decode(encoded));
        this.validateState(urlCodec);
    }


    @Test
    public void testUnsafeEncodeDecode() throws Exception {
        URLCodec urlCodec = new URLCodec();
        String plain = "~!@#$%^&()+{}\"\\;:`,/[]";
        String encoded = urlCodec.encode(plain);
        assertEquals("Unsafe chars URL encoding test", 
            "%7E%21%40%23%24%25%5E%26%28%29%2B%7B%7D%22%5C%3B%3A%60%2C%2F%5B%5D", encoded);
        assertEquals("Unsafe chars URL decoding test", 
            plain, urlCodec.decode(encoded));
        this.validateState(urlCodec);
    }


    @Test
    public void testEncodeDecodeNull() throws Exception {
        URLCodec urlCodec = new URLCodec();
        assertNull("Null string URL encoding test", 
            urlCodec.encode((String)null));
        assertNull("Null string URL decoding test", 
            urlCodec.decode((String)null));
        this.validateState(urlCodec);
    }


    @Test
    public void testDecodeInvalid() throws Exception {
        URLCodec urlCodec = new URLCodec();
        try {
            urlCodec.decode("%");
            fail("DecoderException should have been thrown");
        } catch (DecoderException e) {
            // Expected. Move on
        }
        try {
            urlCodec.decode("%A");
            fail("DecoderException should have been thrown");
        } catch (DecoderException e) {
            // Expected. Move on
        }        
        try {
            // Bad 1st char after %
            urlCodec.decode("%WW");
            fail("DecoderException should have been thrown");
        } catch (DecoderException e) {
            // Expected. Move on
        }
        try {
            // Bad 2nd char after %
            urlCodec.decode("%0W");
            fail("DecoderException should have been thrown");
        } catch (DecoderException e) {
            // Expected. Move on
        }
        this.validateState(urlCodec);
    }

    @Test
    public void testDecodeInvalidContent() throws UnsupportedEncodingException, DecoderException {
        String ch_msg = constructString(SWISS_GERMAN_STUFF_UNICODE); 
        URLCodec urlCodec = new URLCodec();
        byte[] input = ch_msg.getBytes("ISO-8859-1");
        byte[] output = urlCodec.decode(input);
        assertEquals(input.length, output.length);
        for (int i = 0; i < input.length; i++) {
            assertEquals(input[i], output[i]);
        }
        this.validateState(urlCodec);
    }

    @Test
    public void testEncodeNull() throws Exception {
        URLCodec urlCodec = new URLCodec();
        byte[] plain = null;
        byte[] encoded = urlCodec.encode(plain);
        assertEquals("Encoding a null string should return null", 
            null, encoded);
        this.validateState(urlCodec);
    }
    
    @Test
    public void testEncodeUrlWithNullBitSet() throws Exception {
        URLCodec urlCodec = new URLCodec();
        String plain = "Hello there!";
        String encoded = new String( URLCodec.encodeUrl(null, plain.getBytes(Charsets.UTF_8)));
        assertEquals("Basic URL encoding test", 
            "Hello+there%21", encoded);
        assertEquals("Basic URL decoding test", 
            plain, urlCodec.decode(encoded));
        this.validateState(urlCodec);        
    }

    @Test
    public void testDecodeWithNullArray() throws Exception {
        byte[] plain = null;
        byte[] result = URLCodec.decodeUrl( plain );
        assertEquals("Result should be null", null, result);
    }

    @Test
    public void testEncodeStringWithNull() throws Exception {
        URLCodec urlCodec = new URLCodec();
        String test = null;
        String result = urlCodec.encode( test, "charset" );
        assertEquals("Result should be null", null, result);
    }

    @Test
    public void testDecodeStringWithNull() throws Exception {
        URLCodec urlCodec = new URLCodec();
        String test = null;
        String result = urlCodec.decode( test, "charset" );
        assertEquals("Result should be null", null, result);
    }
    
    @Test
    public void testEncodeObjects() throws Exception {
        URLCodec urlCodec = new URLCodec();
        String plain = "Hello there!";
        String encoded = (String) urlCodec.encode((Object) plain);
        assertEquals("Basic URL encoding test", 
            "Hello+there%21", encoded);

        byte[] plainBA = plain.getBytes(Charsets.UTF_8);
        byte[] encodedBA = (byte[]) urlCodec.encode((Object) plainBA);
        encoded = new String(encodedBA);
        assertEquals("Basic URL encoding test", 
            "Hello+there%21", encoded);
            
        Object result = urlCodec.encode((Object) null);
        assertEquals( "Encoding a null Object should return null", null, result);
        
        try {
            Object dObj = new Double(3.0);
            urlCodec.encode( dObj );
            fail( "Trying to url encode a Double object should cause an exception.");
        } catch (EncoderException ee) {
            // Exception expected, test segment passes.
        }
        this.validateState(urlCodec);
    }
    
    @Test
    public void testInvalidEncoding() {
        URLCodec urlCodec = new URLCodec("NONSENSE");
        String plain = "Hello there!";
        try {
            urlCodec.encode(plain);
            fail("We set the encoding to a bogus NONSENSE vlaue, this shouldn't have worked.");
        } catch (EncoderException ee) {
            // Exception expected, test segment passes.
        }
        try {
            urlCodec.decode(plain);
            fail("We set the encoding to a bogus NONSENSE vlaue, this shouldn't have worked.");
        } catch (DecoderException ee) {
            // Exception expected, test segment passes.
        }
        this.validateState(urlCodec);
    }

    @Test
    public void testDecodeObjects() throws Exception {
        URLCodec urlCodec = new URLCodec();
        String plain = "Hello+there%21";
        String decoded = (String) urlCodec.decode((Object) plain);
        assertEquals("Basic URL decoding test", 
            "Hello there!", decoded);

        byte[] plainBA = plain.getBytes(Charsets.UTF_8);
        byte[] decodedBA = (byte[]) urlCodec.decode((Object) plainBA);
        decoded = new String(decodedBA);
        assertEquals("Basic URL decoding test", 
            "Hello there!", decoded);
            
        Object result = urlCodec.decode((Object) null);
        assertEquals( "Decoding a null Object should return null", null, result);
        
        try {
            Object dObj = new Double(3.0);
            urlCodec.decode( dObj );
            fail( "Trying to url encode a Double object should cause an exception.");
        } catch (DecoderException ee) {
            // Exception expected, test segment passes.
        }
        this.validateState(urlCodec);
    }

    @Test
    public void testDefaultEncoding() throws Exception {
        String plain = "Hello there!";
        URLCodec urlCodec = new URLCodec("UnicodeBig");
        urlCodec.encode(plain); // To work around a weird quirk in Java 1.2.2
        String encoded1 = urlCodec.encode(plain, "UnicodeBig");
        String encoded2 = urlCodec.encode(plain);
        assertEquals(encoded1, encoded2);
        this.validateState(urlCodec);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5976.java