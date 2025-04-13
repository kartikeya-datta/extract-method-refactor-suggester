error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4159.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4159.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4159.java
text:
```scala
a@@rg.getValue());

/*
 * Copyright 2001-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLConnection;

import org.apache.jmeter.config.Argument;
import org.apache.jmeter.testelement.property.PropertyIterator;

/**
 */

public class PostWriter
{
    protected final static String BOUNDARY =
        "---------------------------7d159c1302d0y0";
    private final static byte[] CRLF = { 0x0d, 0x0A };
    //protected static int fudge = -20;
    protected static final String encoding = "iso-8859-1";

    /**
     * Send POST data from Entry to the open connection.
     */
    public void sendPostData(URLConnection connection, HTTPSampler sampler)
        throws IOException
    {
        // If filename was specified then send the post using multipart syntax
        String filename = sampler.getFilename();
        if ((filename != null) && (filename.trim().length() > 0))
        {
            OutputStream out = connection.getOutputStream();
            //new FileOutputStream("c:\\data\\experiment.txt");
            //new ByteArrayOutputStream();
            writeln(out, "--" + BOUNDARY);
            PropertyIterator args = sampler.getArguments().iterator();
            while (args.hasNext())
            {
                Argument arg = (Argument) args.next().getObjectValue();
                writeFormMultipartStyle(
                    out,
                    arg.getName(),
                    (String) arg.getValue());
                writeln(out, "--" + BOUNDARY);
            }
            writeFileToURL(
                out,
                filename,
                sampler.getFileField(),
                getFileStream(filename),
                sampler.getMimetype());

            writeln(out, "--" + BOUNDARY + "--");
            out.flush();
            out.close();
        }

        // No filename specified, so send the post using normal syntax
        else
        {
            String postData = sampler.getQueryString();
            PrintWriter out = new PrintWriter(connection.getOutputStream());
            out.print(postData);
            out.flush();
        }
    }

    public void setHeaders(URLConnection connection, HTTPSampler sampler)
        throws IOException
    {
        ((HttpURLConnection) connection).setRequestMethod("POST");

        // If filename was specified then send the post using multipart syntax
        String filename = sampler.getFileField();
        if ((filename != null) && (filename.trim().length() > 0))
        {
            connection.setRequestProperty(
                "Content-Type",
                "multipart/form-data; boundary=" + BOUNDARY);
            connection.setDoOutput(true);
            connection.setDoInput(true);
        }

        // No filename specified, so send the post using normal syntax
        else
        {
            String postData = sampler.getQueryString();
            connection.setRequestProperty(
                "Content-Length",
                "" + postData.length());
            connection.setRequestProperty(
                "Content-Type",
                "application/x-www-form-urlencoded");
            connection.setDoOutput(true);
        }
    }

    private InputStream getFileStream(String filename) throws IOException
    {
        return new BufferedInputStream(new FileInputStream(filename));
    }

	/* NOTUSED
    private String getContentLength(MultipartUrlConfig config)
    {
        long size = 0;
        size += BOUNDARY.length() + 2;
        PropertyIterator iter = config.getArguments().iterator();
        while (iter.hasNext())
        {
            Argument item = (Argument) iter.next().getObjectValue();
            size += item.getName().length()
                + item.getValue().toString().length();
            size += CRLF.length * 4;
            size += BOUNDARY.length() + 2;
            size += 39;
        }
        size += new File(config.getFilename()).length();
        size += CRLF.length * 5;
        size += BOUNDARY.length() + 2;
        size += encode(config.getFileFieldName()).length();
        size += encode(config.getFilename()).length();
        size += config.getMimeType().length();
        size += 66;
        size += 2 + (CRLF.length * 1);
        return Long.toString(size);
    }
	*/
	
    /**
     *  Writes out the contents of a file in correct multipart format.
     */
    private void writeFileToURL(
        OutputStream out,
        String filename,
        String fieldname,
        InputStream in,
        String mimetype)
        throws IOException
    {
        writeln(
            out,
            "Content-Disposition: form-data; name=\""
                + encode(fieldname)
                + "\"; filename=\""
                + encode(filename)
                + "\"");
        writeln(out, "Content-Type: " + mimetype);
        out.write(CRLF);

        byte[] buf = new byte[1024];
	        //1k - the previous 100k made no sense (there's tons of buffers
	        // elsewhere in the chain) and it caused OOM when many concurrent 
	        // uploads were being done. Could be fixed by increasing the evacuation
	        // ratio in bin/jmeter[.bat], but this is better.
        int read;
        while ((read = in.read(buf)) > 0)
        {
            out.write(buf, 0, read);
        }
        out.write(CRLF);
        in.close();
    }

    /**
     *  Writes form data in multipart format.
     */
    private void writeFormMultipartStyle(
        OutputStream out,
        String name,
        String value)
        throws IOException
    {
        writeln(out, "Content-Disposition: form-data; name=\"" + name + "\"");
        out.write(CRLF);
        writeln(out, value);
    }

    private String encode(String value)
    {
        StringBuffer newValue = new StringBuffer();
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++)
        {
            if (chars[i] == '\\')
            {
                newValue.append("\\\\");
            }
            else
            {
                newValue.append(chars[i]);
            }
        }
        return newValue.toString();
    }

	/* NOTUSED
    private void write(OutputStream out, String value)
        throws UnsupportedEncodingException, IOException
    {
        out.write(value.getBytes(encoding));
    }
    */

    private void writeln(OutputStream out, String value)
        throws UnsupportedEncodingException, IOException
    {
        out.write(value.getBytes(encoding));
        out.write(CRLF);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4159.java