error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5522.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5522.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5522.java
text:
```scala
c@@ontentLength = Integer.valueOf(contentLengthHeaderValue).intValue();

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

package org.apache.jmeter.protocol.http.control;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JOrphanUtils;
import org.apache.log.Logger;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

/**
 * Thread to handle one client request. Gets the request from the client and
 * sends the response back to the client.
 */
public class HttpMirrorThread extends Thread {
    private static final Logger log = LoggingManager.getLoggerForClass();

    private static final String ISO_8859_1 = "ISO-8859-1"; //$NON-NLS-1$
    private static final byte[] CRLF = { 0x0d, 0x0a };

    /** Socket to client. */
    private final Socket clientSocket;

    public HttpMirrorThread(Socket _clientSocket) {
        this.clientSocket=_clientSocket;
    }

    /**
     * Main processing method for the HttpMirror object
     */
    @Override
    public void run() {
        log.info("Starting thread");
        BufferedInputStream in = null;
        BufferedOutputStream out = null;

        try {
            in = new BufferedInputStream(clientSocket.getInputStream());
            out = new BufferedOutputStream(clientSocket.getOutputStream());
            // The headers are written using ISO_8859_1 encoding
            out.write("HTTP/1.0 200 OK".getBytes(ISO_8859_1)); //$NON-NLS-1$
            out.write(CRLF);
            out.write("Content-Type: text/plain".getBytes(ISO_8859_1)); //$NON-NLS-1$
            out.write(CRLF);
            out.write(CRLF);
            out.flush();

            // Read the header part, we will be looking for a content-length
            // header, so we know how much we should read.
            // We assume headers are in ISO_8859_1
            // If we do not find such a header, we will just have to read until
            // we have to block to read more, until we support chunked transfer
            int contentLength = -1;
            boolean isChunked = false;
            byte[] buffer = new byte[1024];
            StringBuilder headers = new StringBuilder();
            int length = 0;
            int positionOfBody = 0;
            while(positionOfBody <= 0 && ((length = in.read(buffer)) != -1)) {
                out.write(buffer, 0, length); // echo back
                headers.append(new String(buffer, 0, length, ISO_8859_1));
                // Check if we have read all the headers
                positionOfBody = getPositionOfBody(headers.toString());
            }

            // Check if we have found a content-length header
            final String headerString = headers.toString();
            String contentLengthHeaderValue = getRequestHeaderValue(headerString, "Content-Length"); //$NON-NLS-1$
            if(contentLengthHeaderValue != null) {
                contentLength = new Integer(contentLengthHeaderValue).intValue();
            }
            String sleepHeaderValue = getRequestHeaderValue(headerString, "X-Sleep"); //$NON-NLS-1$
            if(sleepHeaderValue != null) {
                Thread.sleep(Integer.parseInt(sleepHeaderValue));
            }
            String transferEncodingHeaderValue = getRequestHeaderValue(headerString, "Transfer-Encoding"); //$NON-NLS-1$
            if(transferEncodingHeaderValue != null) {
                isChunked = transferEncodingHeaderValue.equalsIgnoreCase("chunked"); //$NON-NLS-1$
                // We only support chunked transfer encoding
                if(!isChunked) {
                    log.error("Transfer-Encoding header set, the value is not supported : " + transferEncodingHeaderValue);
                }
            }

            // If we know the content length, we can allow the reading of
            // the request to block until more data arrives.
            // If it is chunked transfer, we cannot allow the reading to
            // block, because we do not know when to stop reading, because
            // the chunked transfer is not properly supported yet
            length = 0;
            if(contentLength > 0) {
                // Check how much of the body we have already read as part of reading
                // the headers
                // We subtract two bytes for the crlf divider between header and body
                int totalReadBytes = headerString.length() - positionOfBody - 2;

                // We know when to stop reading, so we can allow the read method to block
                while((totalReadBytes < contentLength) && ((length = in.read(buffer)) != -1)) {
                    out.write(buffer, 0, length);

                    totalReadBytes += length;
                }
            }
            else if (isChunked) {
                // It is chunked transfer encoding, which we do not really support yet.
                // So we just read without blocking, because we do not know when to
                // stop reading, so we cannot block
                // TODO propery implement support for chunked transfer, i.e. to
                // know when we have read the whole request, and therefore allow
                // the reading to block
                while(in.available() > 0 && ((length = in.read(buffer)) != -1)) {
                    out.write(buffer, 0, length);
                }
            }
            else {
                // The reqest has no body, or it has a transfer encoding we do not support.
                // In either case, we read any data available
                while(in.available() > 0 && ((length = in.read(buffer)) != -1)) {
                    out.write(buffer, 0, length);
                }
            }
            out.flush();
        } catch (IOException e) {
            log.error("", e);
        } catch (InterruptedException e) {
            log.error("", e);
        } finally {
            JOrphanUtils.closeQuietly(out);
            JOrphanUtils.closeQuietly(in);
            JOrphanUtils.closeQuietly(clientSocket);
        }
        log.info("End of Thread");
    }

    private static String getRequestHeaderValue(String requestHeaders, String headerName) {
        Perl5Matcher localMatcher = JMeterUtils.getMatcher();
        // We use multi-line mask so can prefix the line with ^
        // also match \w+ to catch Transfer-Encoding: chunked
        // TODO: may need to be extended to allow for other header values with non-word contents
        String expression = "^" + headerName + ":\\s+(\\w+)"; // $NON-NLS-1$ $NON-NLS-2$
        Pattern pattern = JMeterUtils.getPattern(expression, Perl5Compiler.READ_ONLY_MASK | Perl5Compiler.CASE_INSENSITIVE_MASK | Perl5Compiler.MULTILINE_MASK);
        if(localMatcher.contains(requestHeaders, pattern)) {
            // The value is in the first group, group 0 is the whole match
            return localMatcher.getMatch().group(1);
        }
        else {
            return null;
        }
    }

    private static int getPositionOfBody(String stringToCheck) {
        Perl5Matcher localMatcher = JMeterUtils.getMatcher();
        // The headers and body are divided by a blank line (the \r is to allow for the CR before LF)
        String regularExpression = "^\\r$"; // $NON-NLS-1$
        Pattern pattern = JMeterUtils.getPattern(regularExpression, Perl5Compiler.READ_ONLY_MASK | Perl5Compiler.CASE_INSENSITIVE_MASK | Perl5Compiler.MULTILINE_MASK);

        PatternMatcherInput input = new PatternMatcherInput(stringToCheck);
        if(localMatcher.contains(input, pattern)) {
            MatchResult match = localMatcher.getMatch();
            return match.beginOffset(0);
        }
        // No divider was found
        return -1;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5522.java