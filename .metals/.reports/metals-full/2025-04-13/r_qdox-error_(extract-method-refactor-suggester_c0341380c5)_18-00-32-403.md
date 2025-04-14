error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/513.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/513.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/513.java
text:
```scala
i@@f (method.equalsIgnoreCase(HttpMethods.POST) || method.equalsIgnoreCase(HttpMethods.PUT)) {

/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
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
 ******************************************************************************/

package com.badlogic.gdx.backends.ios;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cli.MonoTouch.Foundation.NSUrl;
import cli.MonoTouch.UIKit.UIApplication;
import cli.System.IO.Stream;
import cli.System.IO.StreamReader;
import cli.System.Net.HttpWebRequest;
import cli.System.Net.HttpWebResponse;
import cli.System.Net.WebHeaderCollection;
import cli.System.Net.WebRequest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.utils.StreamUtils;

public class IOSNet implements Net {

	public static class InputStreamNetStreamImpl extends InputStream {
		private final Stream stream;

		public InputStreamNetStreamImpl (Stream stream) {
			this.stream = stream;
		}

		@Override
		public int read () throws IOException {
			return stream.ReadByte();
		}
	}

	public static class OutputStreamNetStreamImpl extends OutputStream {

		private Stream stream;

		public OutputStreamNetStreamImpl (Stream stream) {
			this.stream = stream;
		}

		@Override
		public void write (int b) throws IOException {
			// should be the first 8bits of the 32bits int.
			stream.WriteByte((byte)b);
		}

	}

	static class IosHttpResponse implements HttpResponse {

		private HttpWebResponse webResponse;

		public IosHttpResponse (HttpWebResponse webResponse) {
			this.webResponse = webResponse;
		}

		@Override
		public HttpStatus getStatus () {
			return new HttpStatus(webResponse.get_StatusCode().Value);
		}

		@Override
		public String getResultAsString () {
			StreamReader reader = new StreamReader(webResponse.GetResponseStream());
			return reader.ReadToEnd();
		}

		@Override
		public InputStream getResultAsStream () {
			return new InputStreamNetStreamImpl(webResponse.GetResponseStream());
		}

		@Override
		public byte[] getResult () {
			int length = (int)webResponse.get_ContentLength();
			byte[] result = new byte[length];
			webResponse.GetResponseStream().Read(result, 0, length);
			return result;
		}

	}

	final UIApplication uiApp;
	final ExecutorService executorService;

	public IOSNet (IOSApplication app) {
		uiApp = app.uiApp;
		executorService = Executors.newCachedThreadPool();
	}

	@Override
	public void sendHttpRequest (final HttpRequest httpRequest, final HttpResponseListener httpResultListener) {

		Future<?> processHttpRequestFuture = executorService.submit(new Runnable() {
			@Override
			public void run () {

				try {

					String url = httpRequest.getUrl();
					String method = httpRequest.getMethod();

					if (method.equalsIgnoreCase(HttpMethods.GET)) {
						String value = httpRequest.getContent();
						if (value != null && !"".equals(value)) url += "?" + value;
					}

					HttpWebRequest httpWebRequest = (HttpWebRequest)WebRequest.Create(url);

					int timeOut = httpRequest.getTimeOut();
					if (timeOut > 0)
						httpWebRequest.set_Timeout(timeOut);
					else
						httpWebRequest.set_Timeout(-1); // the value of the Infinite constant (see
// http://msdn.microsoft.com/en-us/library/system.threading.timeout.infinite.aspx)
					
					httpWebRequest.set_Method(method);

					Map<String, String> headers = httpRequest.getHeaders();
					WebHeaderCollection webHeaderCollection = new WebHeaderCollection();
					for (String key : headers.keySet())
						webHeaderCollection.Add(key, headers.get(key));
					httpWebRequest.set_Headers(webHeaderCollection);

					if (method.equalsIgnoreCase(HttpMethods.POST)) {
						InputStream contentAsStream = httpRequest.getContentStream();
						String contentAsString = httpRequest.getContent();

						if (contentAsStream != null) {
							httpWebRequest.set_ContentLength(contentAsStream.available());
							
							Stream stream = httpWebRequest.GetRequestStream();
							StreamUtils.copyStream(contentAsStream, new OutputStreamNetStreamImpl(stream));
							stream.Close();
						} else if (contentAsString != null) {
							byte[] data = contentAsString.getBytes();
							httpWebRequest.set_ContentLength(data.length);
							
							Stream stream = httpWebRequest.GetRequestStream();
							stream.Write(data, 0, data.length);
							stream.Close();
						}
						
					}

					final HttpWebResponse httpWebResponse = (HttpWebResponse)httpWebRequest.GetResponse();

					Gdx.app.postRunnable(new Runnable() {
						@Override
						public void run () {
							httpResultListener.handleHttpResponse(new IosHttpResponse(httpWebResponse));
						}
					});
				} catch (final Exception e) {
					Gdx.app.postRunnable(new Runnable() {
						@Override
						public void run () {
							httpResultListener.failed(e);
						}
					});
				}
			}
		});
	}

	@Override
	public ServerSocket newServerSocket (Protocol protocol, int port, ServerSocketHints hints) {
		return new IOSServerSocket(protocol, port, hints);
	}

	@Override
	public Socket newClientSocket (Protocol protocol, String host, int port, SocketHints hints) {
		return new IOSSocket(protocol, host, port, hints);
	}

	@Override
	public void openURI (String URI) {
		uiApp.OpenUrl(new NSUrl(URI));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/513.java