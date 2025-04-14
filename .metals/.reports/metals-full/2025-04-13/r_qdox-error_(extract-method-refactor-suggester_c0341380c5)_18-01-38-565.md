error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4388.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4388.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4388.java
text:
```scala
S@@tringBuilder putBodyBuffer = new StringBuilder();

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

package org.apache.jmeter.protocol.http.sampler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLConnection;

import org.apache.jmeter.protocol.http.util.HTTPArgument;
import org.apache.jmeter.protocol.http.util.HTTPConstants;
import org.apache.jmeter.protocol.http.util.HTTPFileArg;
import org.apache.jmeter.testelement.property.PropertyIterator;

/**
 * Class for setting the necessary headers for a PUT request, and sending the
 * body of the PUT.
 */
public class PutWriter extends PostWriter {
    /**
     * Constructor for PutWriter.
     */
    public PutWriter() {
        // Put request does not use multipart, so no need for boundary
        super(null);
    }

    @Override
    public void setHeaders(URLConnection connection, HTTPSampler sampler) throws IOException {
        // Get the encoding to use for the request
        String contentEncoding = sampler.getContentEncoding();
        if(contentEncoding == null || contentEncoding.length() == 0) {
            contentEncoding = ENCODING;
        }
        long contentLength = 0L;
        boolean hasPutBody = false;

        // Check if the header manager had a content type header
        // This allows the user to specify his own content-type for a PUT request
        String contentTypeHeader = connection.getRequestProperty(HTTPConstants.HEADER_CONTENT_TYPE);
        boolean hasContentTypeHeader = contentTypeHeader != null && contentTypeHeader.length() > 0;

        HTTPFileArg files[] = sampler.getHTTPFiles();

        // If there are no arguments, we can send a file as the body of the request
         if(sampler.getArguments() != null && sampler.getArguments().getArgumentCount() == 0 && sampler.getSendFileAsPostBody()) {
            // If getSendFileAsPostBody returned true, it's sure that file is not null
            HTTPFileArg file = files[0];
            hasPutBody = true;
            if(!hasContentTypeHeader) {
                // Allow the mimetype of the file to control the content type
                if(file.getMimeType().length() > 0) {
                    connection.setRequestProperty(HTTPConstants.HEADER_CONTENT_TYPE, file.getMimeType());
                }
            }

            // Create the content length we are going to write
            File inputFile = new File(file.getPath());
            contentLength = inputFile.length();
        }
        else if(sampler.getSendParameterValuesAsPostBody()) {
            hasPutBody = true;
            // Allow the mimetype of the file to control the content type
            // This is not obvious in GUI if you are not uploading any files,
            // but just sending the content of nameless parameters
            if(!hasContentTypeHeader && files.length == 1 && files[0].getMimeType().length() > 0) {
                connection.setRequestProperty(HTTPConstants.HEADER_CONTENT_TYPE, files[0].getMimeType());
            }

            // We create the post body content now, so we know the size
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            // Just append all the parameter values, and use that as the put body
            StringBuffer putBodyBuffer = new StringBuffer();
            PropertyIterator args = sampler.getArguments().iterator();
            while (args.hasNext()) {
                HTTPArgument arg = (HTTPArgument) args.next().getObjectValue();
                putBodyBuffer.append(arg.getEncodedValue(contentEncoding));
            }

            bos.write(putBodyBuffer.toString().getBytes(contentEncoding));
            bos.flush();
            bos.close();

            // Keep the content, will be sent later
            formDataUrlEncoded = bos.toByteArray();
            contentLength = bos.toByteArray().length;
        }
        if(hasPutBody) {
            // Set the content length
            connection.setRequestProperty(HTTPConstants.HEADER_CONTENT_LENGTH, Long.toString(contentLength));

            // Make the connection ready for sending post data
            connection.setDoOutput(true);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4388.java