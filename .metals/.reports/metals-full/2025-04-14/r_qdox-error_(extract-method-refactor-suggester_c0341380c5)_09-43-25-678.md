error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12118.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12118.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12118.java
text:
```scala
c@@onfig.setMethod(HTTPSampler.GET);

package org.apache.jmeter.junit.protocol.http.parser;

import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.TestCase;

import org.apache.jmeter.protocol.http.modifier.AnchorModifier;
import org.apache.jmeter.protocol.http.sampler.HTTPSampler;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterContextService;

/************************************************************
 *  Title: Description: Copyright: Copyright (c) 2001 Company:
 *
 *@author
 *@created    June 14, 2001
 *@version    1.0
 ***********************************************************/

public class HtmlParserTester extends TestCase
{

	AnchorModifier parser = new AnchorModifier();

	/************************************************************
	 *  Constructor for the HtmlParserTester object
	 *
	 *@param  name  Description of Parameter
	 ***********************************************************/
	public HtmlParserTester(String name)
	{
		super(name);
	}
    

	/************************************************************
	 *  A unit test for JUnit
	 *
	 *@exception  Exception  Description of Exception
	 ***********************************************************/
	public void testSimpleParse() throws Exception
	{
		HTTPSampler config = makeUrlConfig(".*/index\\.html");
		HTTPSampler context = makeContext("http://www.apache.org/subdir/previous.html");
		String responseText = "<html><head><title>Test page</title></head><body>" +
				"<a href=\"index.html\">Goto index page</a></body></html>";
		SampleResult result = new SampleResult();
        JMeterContextService.getContext().setCurrentSampler(context);
        JMeterContextService.getContext().setCurrentSampler(config);
		result.setResponseData(responseText.getBytes());
		result.setSampleLabel(context.toString());
		result.setSamplerData(context.toString());
        JMeterContextService.getContext().setPreviousResult(result);
		parser.process();
		assertEquals("http://www.apache.org:80/subdir/index.html",
				config.getUrl().toString());
	}

	public void testSimpleParse2() throws Exception
	{
		HTTPSampler config = makeUrlConfig("/index\\.html");
		HTTPSampler context = makeContext("http://www.apache.org/subdir/previous.html");
		String responseText = "<html><head><title>Test page</title></head><body>" +
				"<a href=\"/index.html\">Goto index page</a>hfdfjiudfjdfjkjfkdjf"+
				"<b>bold text</b><a href=lowerdir/index.html>lower</a></body></html>";
		SampleResult result = new SampleResult();
		result.setResponseData(responseText.getBytes());
		result.setSampleLabel(context.toString());
        JMeterContextService.getContext().setCurrentSampler(context);
        JMeterContextService.getContext().setCurrentSampler(config);
        JMeterContextService.getContext().setPreviousResult(result);
        parser.process();
		String newUrl = config.getUrl().toString();
		assertTrue("http://www.apache.org:80/index.html".equals(newUrl)
 "http://www.apache.org:80/subdir/lowerdir/index.html".equals(newUrl));

	}

	public void testSimpleParse3() throws Exception
	{
		HTTPSampler config = makeUrlConfig(".*index.*");
		config.getArguments().addArgument("param1","value1");
		HTTPSampler context = makeContext("http://www.apache.org/subdir/previous.html");
		String responseText = "<html><head><title>Test page</title></head><body>" +
				"<a href=\"/home/index.html?param1=value1\">Goto index page</a></body></html>";
		SampleResult result = new SampleResult();
		result.setResponseData(responseText.getBytes());
		result.setSampleLabel(context.toString());
        JMeterContextService.getContext().setCurrentSampler(context);
        JMeterContextService.getContext().setCurrentSampler(config);
        JMeterContextService.getContext().setPreviousResult(result);
        parser.process();
		String newUrl = config.getUrl().toString();
		assertEquals("http://www.apache.org:80/home/index.html?param1=value1",newUrl);
	}

	public void testSimpleParse4() throws Exception
	{
		HTTPSampler config = makeUrlConfig("/subdir/index\\..*");
		HTTPSampler context = makeContext("http://www.apache.org/subdir/previous.html");
		String responseText = "<html><head><title>Test page</title></head><body>" +
				"<A HREF=\"index.html\">Goto index page</A></body></html>";
		SampleResult result = new SampleResult();
		result.setResponseData(responseText.getBytes());
		result.setSampleLabel(context.toString());
        JMeterContextService.getContext().setCurrentSampler(context);
        JMeterContextService.getContext().setCurrentSampler(config);
        JMeterContextService.getContext().setPreviousResult(result);
        parser.process();
		String newUrl = config.getUrl().toString();
		assertEquals("http://www.apache.org:80/subdir/index.html",newUrl);
	}

	public void testSimpleParse5() throws Exception
	{
		HTTPSampler config = makeUrlConfig("/subdir/index\\.h.*");
		HTTPSampler context = makeContext("http://www.apache.org/subdir/one/previous.html");
		String responseText = "<html><head><title>Test page</title></head><body>" +
				"<a href=\"../index.html\">Goto index page</a></body></html>";
		SampleResult result = new SampleResult();
		result.setResponseData(responseText.getBytes());
		result.setSampleLabel(context.toString());
        JMeterContextService.getContext().setCurrentSampler(context);
        JMeterContextService.getContext().setCurrentSampler(config);
        JMeterContextService.getContext().setPreviousResult(result);
        parser.process();
		String newUrl = config.getUrl().toString();
		assertEquals("http://www.apache.org:80/subdir/index.html",newUrl);
	}

	public void testFailSimpleParse1() throws Exception
	{
		HTTPSampler config = makeUrlConfig(".*index.*?param2=.+1");
		HTTPSampler context = makeContext("http://www.apache.org/subdir/previous.html");
		String responseText = "<html><head><title>Test page</title></head><body>" +
				"<a href=\"/home/index.html?param1=value1\">Goto index page</a></body></html>";
		SampleResult result = new SampleResult();
		String newUrl = config.getUrl().toString();
		result.setResponseData(responseText.getBytes());
		result.setSampleLabel(context.toString());
        JMeterContextService.getContext().setCurrentSampler(context);
        JMeterContextService.getContext().setCurrentSampler(config);
        JMeterContextService.getContext().setPreviousResult(result);
        parser.process();
		assertEquals(newUrl,config.getUrl().toString());
	}
	
	public void testFailSimpleParse3() throws Exception
	{
		HTTPSampler config = makeUrlConfig("/home/index.html");
		HTTPSampler context = makeContext("http://www.apache.org/subdir/previous.html");
		String responseText = "<html><head><title>Test page</title></head><body>" +
				"<a href=\"/home/index.html?param1=value1\">Goto index page</a></body></html>";
		SampleResult result = new SampleResult();
		String newUrl = config.getUrl().toString();
		result.setResponseData(responseText.getBytes());
		result.setSampleLabel(context.toString());
        JMeterContextService.getContext().setCurrentSampler(context);
        JMeterContextService.getContext().setCurrentSampler(config);
        JMeterContextService.getContext().setPreviousResult(result);
        parser.process();
		assertEquals(newUrl+"?param1=value1",config.getUrl().toString());
	}

	public void testFailSimpleParse2() throws Exception
	{
		HTTPSampler config = makeUrlConfig(".*login\\.html");
		HTTPSampler context = makeContext("http://www.apache.org/subdir/previous.html");
		String responseText = "<html><head><title>Test page</title></head><body>" +
				"<a href=\"/home/index.html?param1=value1\">Goto index page</a></body></html>";
		SampleResult result = new SampleResult();
		result.setResponseData(responseText.getBytes());
		result.setSampleLabel(context.toString());
        JMeterContextService.getContext().setCurrentSampler(context);
        JMeterContextService.getContext().setPreviousResult(result);
        parser.process();
		String newUrl = config.getUrl().toString();
		assertTrue(!"http://www.apache.org:80/home/index.html?param1=value1".equals(newUrl));
		assertEquals(config.getUrl().toString(),newUrl);
	}

	/************************************************************
	 *  A unit test for JUnit
	 *
	 *@exception  Exception  Description of Exception
	 ***********************************************************/
	public void testSimpleFormParse() throws Exception
	{
		HTTPSampler config = makeUrlConfig(".*index.html");
		config.addArgument("test","g.*");
		config.setMethod(HTTPSampler.POST);
		HTTPSampler context = makeContext("http://www.apache.org/subdir/previous.html");
		String responseText = "<html><head><title>Test page</title></head><body>" +
				"<form action=\"index.html\" method=\"POST\"><input type=\"checkbox\" name=\"test\""+
				" value=\"goto\">Goto index page</form></body></html>";
		SampleResult result = new SampleResult();
		result.setResponseData(responseText.getBytes());
		result.setSampleLabel(context.toString());
        JMeterContextService.getContext().setCurrentSampler(context);
        JMeterContextService.getContext().setCurrentSampler(config);
        JMeterContextService.getContext().setPreviousResult(result);
        parser.process();
		assertEquals("http://www.apache.org:80/subdir/index.html",
				config.getUrl().toString());
		assertEquals("test=goto",config.getQueryString());
	}
	
	/************************************************************
	 *  A unit test for JUnit
	 *
	 *@exception  Exception  Description of Exception
	 ***********************************************************/
	public void testBadCharParse() throws Exception
	{
		HTTPSampler config = makeUrlConfig(".*index.html");
		config.addArgument("te$st","g.*");
		config.setMethod(HTTPSampler.POST);
		HTTPSampler context = makeContext("http://www.apache.org/subdir/previous.html");
		String responseText = "<html><head><title>Test page</title></head><body>" +
				"<form action=\"index.html\" method=\"POST\"><input type=\"checkbox\" name=\"te$st\""+
				" value=\"goto\">Goto index page</form></body></html>";
		SampleResult result = new SampleResult();
		result.setResponseData(responseText.getBytes());
		result.setSampleLabel(context.toString());        
        JMeterContextService.getContext().setCurrentSampler(context);
        JMeterContextService.getContext().setCurrentSampler(config);
        JMeterContextService.getContext().setPreviousResult(result);
        parser.process();
		assertEquals("http://www.apache.org:80/subdir/index.html",
				config.getUrl().toString());
		assertEquals("te%24st=goto",config.getQueryString());
	}
	
	private HTTPSampler makeContext(String url) throws MalformedURLException
	{
		URL u = new URL(url);
		HTTPSampler context = new HTTPSampler();
		context.setDomain(u.getHost());
		context.setPath(u.getPath());
		context.setPort(u.getPort());
		context.setProtocol(u.getProtocol());
		context.parseArguments(u.getQuery());
		return context;
	}

	private HTTPSampler makeUrlConfig(String path)
	{
		HTTPSampler config = new HTTPSampler();
		config.setDomain("www.apache.org");
		config.setMethod(config.GET);
		config.setPath(path);
		config.setPort(80);
		config.setProtocol("http");
		return config;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12118.java