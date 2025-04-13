error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4738.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4738.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4738.java
text:
```scala
public b@@oolean getSecure()

/*
 * $Id: CookieValuePersisterSettings.java,v 1.1 2005/01/26 01:41:18
 * jonathanlocke Exp $ $Revision$ $Date$
 * 
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.markup.html.form.persistence;

/**
 * This class provides default values that are used by the CookieValuePersister
 * class when it creates cookies.
 * 
 * @author Juergen Donnerstag
 */
public class CookieValuePersisterSettings
{
	/** Max age that the component will be persisted in seconds. */
	private int maxAge = 3600 * 24 * 30; // 30 days

	/** Cookie comment. */
	private String comment;

	/** Cookie domain. */
	private String domain;

	/** Whether the cookie is secure. */
	private boolean secure;

	/** Cookie version. */
	private int version;

	/**
	 * Gets the max age. After
	 * 
	 * @return the max age
	 */
	public int getMaxAge()
	{
		return maxAge;
	}

	/**
	 * Sets the maximum age of the cookie in seconds.
	 * 
	 * @param maxAge
	 *            the max age in secs.
	 */
	public void setMaxAge(int maxAge)
	{
		this.maxAge = maxAge;
	}

	/**
	 * Gets the cookie comment.
	 * 
	 * @return the cookie comment
	 */
	public String getComment()
	{
		return comment;
	}

	/**
	 * Sets the cookie comment.
	 * 
	 * @param comment
	 *            the cookie comment
	 */
	public void setComment(String comment)
	{
		this.comment = comment;
	}

	/**
	 * Gets the cookie domain name.
	 * 
	 * @return the cookie domain name
	 */
	public String getDomain()
	{
		return domain;
	}

	/**
	 * Sets the cookie domain name.
	 * 
	 * @param domain
	 *            the cookie domain name
	 */
	public void setDomain(String domain)
	{
		this.domain = domain;
	}

	/**
	 * Returns true if the browser is sending cookies only over a secure
	 * protocol, or false if the browser can send cookies using any protocol.
	 * 
	 * @return whether this cookie is secure
	 */
	public boolean isSecure()
	{
		return secure;
	}

	/**
	 * Indicates to the browser whether the cookie should only be sent using a
	 * secure protocol, such as HTTPS or SSL.
	 * 
	 * @param secure
	 *            if true, sends the cookie from the browser to the server using
	 *            only when using a secure protocol; if false, sent on any
	 *            protocol
	 */
	public void setSecure(boolean secure)
	{
		this.secure = secure;
	}

	/**
	 * Returns the version of the protocol this cookie complies with. Version 1
	 * complies with RFC 2109, and version 0 complies with the original cookie
	 * specification drafted by Netscape. Cookies provided by a browser use and
	 * identify the browser's cookie version.
	 * 
	 * @return 0 if the cookie complies with the original Netscape
	 *         specification; 1 if the cookie complies with RFC 2109
	 */
	public int getVersion()
	{
		return version;
	}

	/**
	 * Sets the version of the cookie protocol this cookie complies with.
	 * Version 0 complies with the original Netscape cookie specification.
	 * Version 1 complies with RFC 2109. <br/>Since RFC 2109 is still somewhat
	 * new, consider version 1 as experimental; do not use it yet on production
	 * sites.
	 * 
	 * @param version
	 *            0 if the cookie should comply with the original Netscape
	 *            specification; 1 if the cookie should comply with RFC 2109
	 */
	public void setVersion(int version)
	{
		this.version = version;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4738.java