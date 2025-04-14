error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1764.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1764.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1764.java
text:
```scala
o@@ut.close();

/*
 * ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the
 * distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 * if any, must include the following acknowledgment:
 * "This product includes software developed by the
 * Apache Software Foundation (http://www.apache.org/)."
 * Alternately, this acknowledgment may appear in the software itself,
 * if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" and
 * "Apache JMeter" must not be used to endorse or promote products
 * derived from this software without prior written permission. For
 * written permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 * "Apache JMeter", nor may "Apache" appear in their name, without
 * prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */

package org.apache.jmeter.protocol.http.sampler;

import java.io.*;
import java.net.*;
import java.util.*;

import org.apache.jmeter.protocol.http.config.MultipartUrlConfig;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.config.Argument;

/**
 * Title:        JMeter
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:      Apache
 * @author Michael Stover
 * @version 1.0
 */

public class PostWriter
{

	protected final static String BOUNDARY = "---------------------------7d159c1302d0y0";
	protected final static byte[] CRLF = {0x0d,0x0A};
	protected static int fudge = -20;
	protected static String encoding = "iso-8859-1";


	/************************************************************
	 *  Send POST data from Entry to the open connection.
	 *
	 *@param  connection       Description of Parameter
	 *@param  url              !ToDo (Parameter description)
	 *@exception  IOException  Description of Exception
	 ***********************************************************/
	public void sendPostData(URLConnection connection, HTTPSampler sampler)
			 throws IOException
	{
		// If filename was specified then send the post using multipart syntax
		String filename = sampler.getFileField();
		if ((filename != null) && (filename.trim().length() > 0))
		{
			OutputStream out = connection.getOutputStream();//new FileOutputStream("c:\\data\\experiment.txt");//new ByteArrayOutputStream();//
			writeln(out,"--"+BOUNDARY);
			Iterator args = sampler.getArguments().iterator();
			while (args.hasNext())
			{
				Argument arg = (Argument)args.next();
				writeFormMultipartStyle(out, arg.getName(), (String)arg.getValue());
				writeln(out,"--" + BOUNDARY);
			}
			writeFileToURL(out, filename, sampler.getFileField(),
					 getFileStream(filename),sampler.getMimetype());

			writeln(out,"--" + BOUNDARY+"--");
			out.flush();
			//out.close();
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
	
	public void setHeaders(URLConnection connection,HTTPSampler sampler) throws IOException
	{
		((HttpURLConnection)connection).setRequestMethod("POST");

		// If filename was specified then send the post using multipart syntax
		String filename = sampler.getFileField();
		if ((filename != null) && (filename.trim().length() > 0))
		{
			connection.setRequestProperty("Content-type", "multipart/form-data; boundary=" + BOUNDARY);
			connection.setDoOutput(true);
			connection.setDoInput(true);
		}

		// No filename specified, so send the post using normal syntax
		else
		{
			String postData = sampler.getQueryString();
			connection.setRequestProperty("Content-length", "" + postData.length());
			connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
			connection.setDoOutput(true);
		}
	}
	
	private InputStream getFileStream(String filename) throws IOException
	{
		return new BufferedInputStream(new FileInputStream(filename));
	}

	private String getContentLength(MultipartUrlConfig config)
	{
		long size = 0;
		size += BOUNDARY.length()+2;
		Iterator iter = config.getArguments().iterator();
		while (iter.hasNext())
		{
			Argument item = (Argument)iter.next();
			size += item.getName().length() + item.getValue().toString().length();
			size += CRLF.length * 4;
			size += BOUNDARY.length()+2;
			size += 39;
		}
		size += new File(config.getFilename()).length();
		size += CRLF.length * 5;
		size += BOUNDARY.length()+2;
		size += encode(config.getFileFieldName()).length();
		size += encode(config.getFilename()).length();
		size += config.getMimeType().length();
		size += 66;
		size += 2+(CRLF.length*1);
		return Long.toString(size);
	}

	/************************************************************
	 *  Writes out the contents of a file in correct multipart format.
	 *
	 *@param  o                Description of Parameter
	 *@param  filename         Description of Parameter
	 *@param  fieldname        Description of Parameter
	 *@param  in               Description of Parameter
	 *@param  mimetype         Description of Parameter
	 *@exception  IOException  Description of Exception
	 ***********************************************************/
	private void writeFileToURL(OutputStream out, String filename, String fieldname,
			InputStream in, String mimetype) throws IOException
	{
		writeln(out,"Content-Disposition: form-data; name=\"" + encode(fieldname) + "\"; filename=\"" +
				encode(filename) + "\"");
		writeln(out,"Content-Type: " + mimetype);
		out.write(CRLF);

		byte[] buf = new byte[1024 * 100];
		//100k
		int read;
		while ((read = in.read(buf)) > 0)
		{
			out.write(buf, 0, read);
		}
		out.write(CRLF);
		in.close();
	}

		/************************************************************
	 *  Writes form data in multipart format.
	 *
	 *@param  out    Description of Parameter
	 *@param  name   Description of Parameter
	 *@param  value  Description of Parameter
	 ***********************************************************/
	private void writeFormMultipartStyle(OutputStream out, String name, String value) throws IOException
	{
		writeln(out,"Content-Disposition: form-data; name=\"" + name + "\"");
		out.write(CRLF);
		writeln(out,value);
	}

	private String encode(String value)
	{
		StringBuffer newValue = new StringBuffer();
		char[] chars = value.toCharArray();
		for(int i = 0;i < chars.length;i++)
		{
			if(chars[i] == '\\')
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

	private void write(OutputStream out,String value) throws UnsupportedEncodingException,IOException
	{
		out.write(value.getBytes(encoding));
	}

	private void writeln(OutputStream out,String value) throws UnsupportedEncodingException, IOException
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1764.java