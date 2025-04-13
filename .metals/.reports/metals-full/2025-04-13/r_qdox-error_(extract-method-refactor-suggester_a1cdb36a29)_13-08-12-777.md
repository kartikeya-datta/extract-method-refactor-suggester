error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14616.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14616.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14616.java
text:
```scala
f@@ield.detach();

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
package wicket.markup.html.form.upload;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import wicket.Page;
import wicket.RequestCycle;
import wicket.WicketTestCase;
import wicket.protocol.http.MockHttpServletRequest;
import wicket.protocol.http.servlet.MultipartServletWebRequest;
import wicket.util.file.File;
import wicket.util.lang.Bytes;
import wicket.util.tester.ITestPageSource;

/**
 * Test of FileUploadField
 * 
 * @author Frank Bille (billen)
 */
public class FileUploadFieldTest extends WicketTestCase
{

	/**
	 * Construct.
	 */
	public FileUploadFieldTest()
	{
		super("Test of FileUploadField");
	}

	/**
	 * Test that detach closes the streams
	 * 
	 * @throws Exception 
	 */
	public void testInternalDetach() throws Exception
	{
		final MockPageWithFormAndUploadField page = new MockPageWithFormAndUploadField();
		
		FileUploadField field = new FileUploadField("upload");
		page.getForm().add(field);

		tester.startPage(new ITestPageSource() 
		{
			private static final long serialVersionUID = 1L;

			public Page getTestPage()
			{
				return page;
			}
		});

		// Setup the request. It should be a IMultipartWebRequest
		RequestCycle requestCycle = tester.createRequestCycle();
		MockHttpServletRequest servletRequest = tester.getServletRequest();
		servletRequest.setMethod("POST");
		servletRequest.setParameter("form2:hf:fs", "");
		servletRequest.setParameter("wicketState", "");
		
		File tmp = null;
		try {
			// Write out a large text file. We need to make this file reasonably sizable,
			// because things get handled using input streams, and we want to check to make
			// sure they're closed properly if we abort mid-request.
			
			// We create a temp file because we don't want to depend on a file we might not
			// know the path of (e.g. the big DTD this test used previously). This enables
			// us to run the test out of a JAR file if need be, and also with an unknown
			// running directory (e.g. when run from wicket-parent).
			tmp = new File(File.createTempFile(this.getClass().getName(), ".txt"));
			OutputStream os = new BufferedOutputStream(new FileOutputStream(tmp));
			for (int i = 0; i < 1000; i++)
			{
				os.write("test test test test test\n".getBytes());
			}
			os.close();
		
			// Let's upload the dtd file. It's large enough to avoid being in memory.
			servletRequest.addFile("upload", tmp, "text/plain");
	
			requestCycle.setRequest(new MultipartServletWebRequest(servletRequest, Bytes.MAX));
			
			// Get the file upload
			FileUpload fileUpload = field.getFileUpload();
			
			assertNotNull(fileUpload);
			
			// Get an input stream from the file upload
			InputStream is = fileUpload.getInputStream();
			
			// We should be able to read a byte
			assertTrue(is.read() != -1);
			
			field.internalOnDetach();
			
			// The input stream should be closed so we shouldn't be able to read any more bytes
			try 
			{
				is.read();
				fail();
			} 
			catch (IOException e)
			{
				// Expected
			}
			catch (Exception e)
			{
				fail();
			}
		}
		finally
		{
			if (tmp != null && tmp.exists())
			{
				tmp.delete();
			}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14616.java