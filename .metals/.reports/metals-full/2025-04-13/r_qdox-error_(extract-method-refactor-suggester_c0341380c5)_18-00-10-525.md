error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9968.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9968.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9968.java
text:
```scala
u@@rl = ((RequestCycle)requestCycle).mapUrlFor(handler).toString();

package org.apache.wicket.protocol.https;

import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.util.lang.Checks;

/**
 * Request handler that performs redirects across http and https
 */
class SwitchProtocolRequestHandler implements IRequestHandler
{

	/**
	 * Protocols
	 */
	public enum Protocol {
		/*** HTTP */
		HTTP,
		/** HTTPS */
		HTTPS,
		/** CURRENT */
		PRESERVE_CURRENT
	}

	/** the protocol this request handler is going to switch to */
	private final Protocol protocol;

	/** the original request handler */
	private final IRequestHandler handler;

	private final HttpsConfig httpsConfig;

	/**
	 * Constructor
	 * 
	 * @param protocol
	 *            required protocol
	 * @param httpsConfig
	 *            the https configuration
	 */
	SwitchProtocolRequestHandler(Protocol protocol, HttpsConfig httpsConfig)
	{
		this(protocol, null, httpsConfig);
	}

	/**
	 * Constructor
	 * 
	 * @param protocol
	 *            required protocol
	 * @param handler
	 *            target to redirect to, or {@code null} to replay the current url
	 * @param httpsConfig
	 *            the https configuration
	 */
	SwitchProtocolRequestHandler(Protocol protocol, IRequestHandler handler,
		final HttpsConfig httpsConfig)
	{
		Checks.argumentNotNull(protocol, "protocol");
		Checks.argumentNotNull(httpsConfig, "httpsConfig");

		if (protocol == Protocol.PRESERVE_CURRENT)
		{
			throw new IllegalArgumentException("Argument 'protocol' may not have value '" +
				Protocol.PRESERVE_CURRENT.toString() + "'.");
		}

		this.protocol = protocol;
		this.handler = handler;
		this.httpsConfig = httpsConfig;
	}

	/**
	 * Rewrite the url using the specified protocol
	 * 
	 * @param protocol
	 * @param port
	 * @param request
	 * @return url
	 */
	protected String getUrl(String protocol, Integer port, HttpServletRequest request)
	{
		StringBuilder result = new StringBuilder();
		result.append(protocol);
		result.append("://");
		result.append(request.getServerName());
		if (port != null)
		{
			result.append(":");
			result.append(port);
		}
		result.append(request.getRequestURI());
		if (request.getQueryString() != null)
		{
			result.append("?");
			result.append(request.getQueryString());
		}
		return result.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	public void respond(IRequestCycle requestCycle)
	{
		WebRequest webRequest = (WebRequest)requestCycle.getRequest();
		HttpServletRequest request = ((ServletWebRequest)webRequest).getHttpServletRequest();

		Integer port = null;
		if (protocol == Protocol.HTTP)
		{
			if (httpsConfig.getHttpPort() != 80)
			{
				port = httpsConfig.getHttpPort();
			}
		}
		else if (protocol == Protocol.HTTPS)
		{
			if (httpsConfig.getHttpsPort() != 443)
			{
				port = httpsConfig.getHttpsPort();
			}
		}

		final String url;
		if (handler == null)
		{
			url = getUrl(protocol.toString().toLowerCase(), port, request);
		}
		else
		{
			url = ((RequestCycle)requestCycle).urlFor(handler).toString();
		}

		WebResponse response = (WebResponse)requestCycle.getResponse();

		response.sendRedirect(url);
	}

	/**
	 * Returns a target that can be used to redirect to the specified protocol. If no change is
	 * required {@code null} will be returned.
	 * 
	 * @param protocol
	 *            required protocol
	 * @param httpsConfig
	 *            the https configuration
	 * @return request target or {@code null}
	 */
	public static IRequestHandler requireProtocol(Protocol protocol, final HttpsConfig httpsConfig)
	{
		return requireProtocol(protocol, null, httpsConfig);
	}

	/**
	 * Returns a target that can be used to redirect to the specified protocol. If no change is
	 * required {@code null} will be returned.
	 * 
	 * @param protocol
	 *            required protocol
	 * @param handler
	 *            request target to redirect to or {@code null} to redirect to current url
	 * @param httpsConfig
	 *            the https configuration
	 * @return request handler or {@code null}
	 */
	public static IRequestHandler requireProtocol(Protocol protocol, IRequestHandler handler,
		final HttpsConfig httpsConfig)
	{
		IRequestCycle requestCycle = RequestCycle.get();
		WebRequest webRequest = (WebRequest)requestCycle.getRequest();
		HttpServletRequest request = ((ServletWebRequest)webRequest).getHttpServletRequest();
		if (protocol == null || protocol == Protocol.PRESERVE_CURRENT ||
			request.getScheme().equals(protocol.toString().toLowerCase()))
		{
			return null;
		}
		else
		{
			return new SwitchProtocolRequestHandler(protocol, handler, httpsConfig);
		}
	}

	/** {@inheritDoc} */
	public void detach(IRequestCycle requestCycle)
	{
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9968.java