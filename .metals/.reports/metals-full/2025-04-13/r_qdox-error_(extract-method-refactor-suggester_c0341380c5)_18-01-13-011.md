error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7908.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7908.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7908.java
text:
```scala
b@@yte[] msg = new byte[0];

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

/*
 * TCP Sampler Client implementation which reads and writes length-prefixed binary data.  
 * 
 * Input/Output strings are passed as hex-encoded binary strings.
 * 
 * 2-Byte or 4-Byte length prefixes are supported.
 * 
 * Length prefix is binary of length specified by property "tcp.length.prefix.length".
 *
 */
package org.apache.jmeter.protocol.tcp.sampler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.SocketTimeoutException;

import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JOrphanUtils;
import org.apache.log.Logger;

/**
 * Implements binary length-prefixed binary data.
 * This is used in ISO8583 for example.
 */
public class LengthPrefixedBinaryTCPClientImpl extends TCPClientDecorator {
    private static final Logger log = LoggingManager.getLoggerForClass();
    
    private final int lengthPrefixLen = JMeterUtils.getPropDefault("tcp.binarylength.prefix.length", 2); // $NON-NLS-1$

    public LengthPrefixedBinaryTCPClientImpl() {
        super(new BinaryTCPClientImpl());
    }


    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.jmeter.protocol.tcp.sampler.TCPClient#write(java.io.OutputStream
     * , java.lang.String)
     */
    public void write(OutputStream os, String s) {
        try {
            os.write(intToByteArray(s.length()/2,lengthPrefixLen));
            this.tcpClient.write(os, s);
        } catch (IOException e) {
            log.warn("Write error", e);
        }
        log.debug("Wrote: " + s);
        return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.jmeter.protocol.tcp.sampler.TCPClient#write(java.io.OutputStream
     * , java.io.InputStream)
     */
    public void write(OutputStream os, InputStream is) {
        this.tcpClient.write(os, is);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.jmeter.protocol.tcp.sampler.TCPClient#read(java.io.InputStream
     * )
     */
    public String read(InputStream is) {
        byte[] msg = null;
        int msgLen = 0;
        try {
            byte[] lengthBuffer = new byte[lengthPrefixLen];
            if (is.read(lengthBuffer, 0, lengthPrefixLen) == lengthPrefixLen) {
                msgLen = byteArrayToInt(lengthBuffer);
                msg = new byte[msgLen];
                is.read(msg);
            }
            /*
             * Timeout is reported as follows: JDK1.3: InterruptedIOException
             * JDK1.4: SocketTimeoutException, which extends
             * InterruptedIOException
             * 
             * So to make the code work on both, just check for
             * InterruptedIOException
             * 
             * If 1.3 support is dropped, can change to using
             * SocketTimeoutException
             * 
             * For more accurate detection of timeouts under 1.3, one could
             * perhaps examine the Exception message text...
             */
        } catch (SocketTimeoutException e) {
            // drop out to handle buffer
        } catch (InterruptedIOException e) {
            // drop out to handle buffer
        } catch (IOException e) {
            log.warn("Read error:" + e);
            return JOrphanUtils.baToHexString(msg);
        }

        // do we need to close byte array (or flush it?)
        log.debug("Read: " + msgLen + "\n" + msg.toString());
        return JOrphanUtils.baToHexString(msg);
    }

    /**
     * @return Returns the eolByte.
     */
    public byte getEolByte() {
        return tcpClient.getEolByte();
    }

    /**
     * @param eolByte
     *            The eolByte to set.
     */
    public void setEolByte(byte eolByte) {
        tcpClient.setEolByte(eolByte);
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7908.java