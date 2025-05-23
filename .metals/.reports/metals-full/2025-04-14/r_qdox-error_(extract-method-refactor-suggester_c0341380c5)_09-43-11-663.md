error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3655.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3655.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3655.java
text:
```scala
i@@f (!key.equals("proxy-connection") && !key.equals("content-length") /*&& !key.equals("accept-encoding")*/)

package org.apache.jmeter.protocol.http.proxy;
/****************************************
 * File HttpRequestHdr.java
 ***************************************/
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import junit.framework.TestCase;

import org.apache.jmeter.protocol.http.config.MultipartUrlConfig;
import org.apache.jmeter.protocol.http.control.Header;
import org.apache.jmeter.protocol.http.control.HeaderManager;
import org.apache.jmeter.protocol.http.control.gui.HttpTestSampleGui;
import org.apache.jmeter.protocol.http.gui.HeaderPanel;
import org.apache.jmeter.protocol.http.sampler.HTTPSampler;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.log.Logger;
import org.apache.jorphan.logging.LoggingManager;

//
// Class:     HttpRequestHdr
// Abstract:  The headers of the client HTTP request.
//
/****************************************
 * !ToDo (Class description)
 *
 *@author    $Author$
 *@created   $Date$
 *@version   $Revision$
 ***************************************/
public class HttpRequestHdr
{
	Logger log =
		LoggingManager.getLoggerFor(JMeterUtils.HTTP);
	/****************************************
	 * Http Request method. Such as get or post.
	 ***************************************/
	public String method = new String();
	/****************************************
	 * The requested url. The universal resource locator that hopefully uniquely
	 * describes the object or service the client is requesting.
	 ***************************************/
	public String url = new String();
	/****************************************
	 * Version of http being used. Such as HTTP/1.0
	 ***************************************/
	public String version = new String();
	/****************************************
	 * !ToDo (Field description)
	 ***************************************/
	public String postData = "";
	static String CR = "\r\n";
	private Map headers = new HashMap();
	/****************************************
	 * Parses a http header from a stream.
	 *
	 *@param in  The stream to parse.
	 *@return    Array of bytes from client.
	 ***************************************/
	public byte[] parse(InputStream in) throws IOException
	{
		boolean inHeaders = true;
		int readLength = 0;
		int dataLength = 0;
		String CR = "\r\n";
		boolean first = true;
		ByteArrayOutputStream clientRequest = new ByteArrayOutputStream();
		ByteArrayOutputStream line = new ByteArrayOutputStream();
		int x;
		while ((inHeaders || readLength < dataLength) && ((x = in.read()) != -1))
		{
			line.write(x);
			clientRequest.write(x);
			if (inHeaders && (byte) x == (byte) '\n')
			{
				if (line.size() < 3)
				{
					inHeaders = false;
				}
				if (first)
				{
					parseFirstLine(line.toString());
					first = false;
				}
				else
				{
					dataLength = Math.max(parseLine(line.toString()), dataLength);
				}
				line.reset();
			}
			else if (!inHeaders)
			{
				readLength++;
			}
		}
		postData = line.toString().trim();
		return clientRequest.toByteArray();
	}
	public void parseFirstLine(String firstLine)
	{
		log.debug("browser request: " + firstLine);
		StringTokenizer tz = new StringTokenizer(firstLine);
		method = getToken(tz).toUpperCase();
		url = getToken(tz);
		log.debug("parsed url: " + url);
		version = getToken(tz);
	}
	public int parseLine(String nextLine)
	{
		StringTokenizer tz;
		tz = new StringTokenizer(nextLine);
		String token = getToken(tz);
		// look for termination of HTTP command
		if (0 == token.length())
		{
			return 0;
		}
		else
		{
			String name = token.trim().substring(0, token.trim().length() - 1);
			String value = getRemainder(tz);
			headers.put(name.toLowerCase(), new Header(name, value));
			if (name.equalsIgnoreCase("content-length"))
			{
				return Integer.parseInt(value);
			}
		}
		return 0;
	}
	public HeaderManager getHeaderManager()
	{
		HeaderManager manager = new HeaderManager();
		Iterator keys = headers.keySet().iterator();
		while (keys.hasNext())
		{
			String key = (String) keys.next();
			if (!key.equals("proxy-connection") && !key.equals("content-length") && !key.equals("accept-encoding"))
			{
				manager.add((Header) headers.get(key));
			}
		}
		manager.setName("Browser-derived headers");
		manager.setProperty(TestElement.TEST_CLASS, HeaderManager.class.getName());
		manager.setProperty(TestElement.GUI_CLASS, HeaderPanel.class.getName());
		return manager;
	}
	public HTTPSampler getSampler()
		throws MalformedURLException, IOException, ProtocolException
	{
		HttpTestSampleGui tempGui = new HttpTestSampleGui();
		tempGui.configure(createSampler());
		HTTPSampler result = (HTTPSampler) tempGui.createTestElement();
		result.setFollowRedirects(false);
		result.setUseKeepAlive(true);
		return result;
	}
	public String getContentType()
	{
		Header contentTypeHeader = (Header) headers.get("content-type");
		if (contentTypeHeader != null)
		{
			return contentTypeHeader.getValue();
		}
		return "";
	}
	public static MultipartUrlConfig isMultipart(String contentType)
	{
		if (contentType != null
			&& contentType.startsWith(MultipartUrlConfig.MULTIPART_FORM))
		{
			return new MultipartUrlConfig(
				contentType.substring(contentType.indexOf("oundary=") + 8));
		}
		else
		{
			return null;
		}
	}
	private HTTPSampler createSampler()
	{
		MultipartUrlConfig urlConfig = null;
		HTTPSampler sampler = new HTTPSampler();
		sampler.setDomain(serverName());
		log.debug("Proxy: setting server: "+sampler.getDomain());
		sampler.setMethod(method);
		log.debug("Proxy: method server: "+sampler.getMethod());
		sampler.setPath(serverUrl());
		log.debug("Proxy: setting path: "+sampler.getPath());
		sampler.setName(sampler.getPath());
		sampler.setPort(serverPort());
		log.debug("Proxy: setting port: "+sampler.getPort());
		if (url.indexOf("//") > -1)
		{
			String protocol = url.substring(0, url.indexOf(":"));
			log.debug("Proxy: setting protocol to : "+protocol);
			sampler.setProtocol(protocol);
		}
		else if(sampler.getPort() == 443)
		{
			sampler.setProtocol("https");
			log.debug("Proxy: setting protocol to https");
		}
		else
		{
			log.debug("Proxy setting default protocol to: http");
			sampler.setProtocol("http");
		}
		if ((urlConfig = isMultipart(getContentType())) != null)
		{
			urlConfig.parseArguments(postData);
			sampler.setArguments(urlConfig.getArguments());
			sampler.setFileField(urlConfig.getFileFieldName());
			sampler.setFilename(urlConfig.getFilename());
			sampler.setMimetype(urlConfig.getMimeType());
		}
		else
		{
			sampler.parseArguments(postData);
		}
		return sampler;
	}
	//
	// Parsing Methods
	//
	/****************************************
	 * Find the //server.name from an url.
	 *
	 *@return   Servers internet name
	 ***************************************/
	public String serverName()
	{
		// chop to "server.name:x/thing"
		String str = url;
		int i = str.indexOf("//");
		if (i > 0)
		{
			str = str.substring(i + 2);
		}
		// chop to  server.name:xx
		i = str.indexOf("/");
		if (0 < i)
		{
			str = str.substring(0, i);
		}
		// chop to server.name
		i = str.indexOf(":");
		if (0 < i)
		{
			str = str.substring(0, i);
		}
		return str;
	}
	/****************************************
	 * Find the :PORT form http://server.ect:PORT/some/file.xxx
	 *
	 *@return   Servers internet name
	 ***************************************/
	public int serverPort()
	{
		String str = url;
		// chop to "server.name:x/thing"
		int i = str.indexOf("//");
		if (i > 0)
		{
			str = str.substring(i + 2);
		}		
		// chop to  server.name:xx
		i = str.indexOf("/");
		if (0 < i)
		{
			str = str.substring(0, i);
		}
		// chop XX
		i = str.indexOf(":");
		if (0 < i)
		{
			return Integer.parseInt(str.substring(i + 1).trim());
		}
		return 80;
	}
	/****************************************
	 * Find the /some/file.xxxx form http://server.ect:PORT/some/file.xxx
	 *
	 *@return   the deproxied url
	 ***************************************/
	public String serverUrl()
	{
		String str = url;
		int i = str.indexOf("//");
		if (i > 0)
		{
			str = str.substring(i + 2);
		}		
		i = str.indexOf("/");
		if (i < 0)
		{
			return "";
		}
		return str.substring(i);
	}
	/****************************************
	 * Returns the next token in a string
	 *
	 *@param tk  String that is partially tokenized.
	 *@return    !ToDo (Return description)
	 *@returns   The remainder
	 ***************************************/
	String getToken(StringTokenizer tk)
	{
		String str = "";
		if (tk.hasMoreTokens())
		{
			str = tk.nextToken();
		}
		return str;
	}
	/****************************************
	 * Returns the remainder of a tokenized string
	 *
	 *@param tk  String that is partially tokenized.
	 *@return    !ToDo (Return description)
	 *@returns   The remainder
	 ***************************************/
	String getRemainder(StringTokenizer tk)
	{
		String str = "";
		if (tk.hasMoreTokens())
		{
			str = tk.nextToken();
		}
		while (tk.hasMoreTokens())
		{
			str += " " + tk.nextToken();
		}
		return str;
	}

        public static class Test extends TestCase
	{
                public Test(String name)
		{
			super(name);
		}
	
		public void setUp() {
		}

                public void testRepeatedArguments() throws Exception {
                    String TEST_REQ=
                        "GET http://localhost/matrix.html?update=yes&d=1&d=2&d=&d=&d=&d=&d=&d=1&d=2&d=1&d=&d= HTTP/1.0\n\n";
                    HttpRequestHdr req= new HttpRequestHdr();
                    req.parse(new java.io.ByteArrayInputStream(
                            TEST_REQ.getBytes()));
                    HTTPSampler s= req.getSampler();
                    assertEquals(s.getArguments().getArguments().size(), 13);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3655.java