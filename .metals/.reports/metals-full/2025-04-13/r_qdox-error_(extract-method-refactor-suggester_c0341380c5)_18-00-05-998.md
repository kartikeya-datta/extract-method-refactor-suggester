error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3194.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3194.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3194.java
text:
```scala
r@@eturn Application.get().getRequestCycleSettings().getResponseRequestEncoding();

/*
 * $Id$ $Revision:
 * 1.6 $ $Date$
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
package wicket;

import java.io.OutputStream;
import java.util.Locale;

import wicket.markup.ComponentTag;
import wicket.util.time.Time;

/**
 * Abstract base class for different implementations of response writing. A
 * subclass must implement write(String) to write a String to the response
 * destination (whether it be a browser, a file, a test harness or some other
 * place). A subclass may optionally implement close(), encodeURL(String),
 * redirect(String), isRedirect() or setContentType(String) as appropriate.
 * 
 * @author Jonathan Locke
 */
public abstract class Response
{
    /** Default encoding of output stream */
    private String defaultEncoding;

    /**
     * Construct.
     */
    public Response()
    {
    }

	/**
	 * Closes the response output stream
	 */
	public void close()
	{
	}

	/**
	 * An implementation of this method is only required if a subclass wishes to
	 * support sessions via URL rewriting. This default implementation simply
	 * returns the URL String it is passed.
	 * 
	 * @param url
	 *            The URL to encode
	 * @return The encoded url
	 */
	public String encodeURL(final String url)
	{
		return url;
	}

	/**
	 * @return The output stream for this response
	 */
	public abstract OutputStream getOutputStream();

	/**
	 * Returns true if a redirection has occurred. The default implementation
	 * always returns false since redirect is not implemented by default.
	 * 
	 * @return True if the redirect method has been called, making this response
	 *         a redirect.
	 */
	public boolean isRedirect()
	{
		return false;
	}

	/**
	 * A subclass may override this method to implement redirection. Subclasses
	 * which have no need to do redirection may choose not to override this
	 * default implementation, which does nothing. For example, if a subclass
	 * wishes to write output to a file or is part of a testing harness, there
	 * may be no meaning to redirection.
	 * 
	 * @param url
	 *            The URL to redirect to
	 */
	public void redirect(final String url)
	{
	}

	/**
	 * Set the content length on the response, if appropriate in the subclass.
	 * This default implementation does nothing.
	 * 
	 * @param length
	 *            The length of the content
	 */
	public void setContentLength(final long length)
	{
	}

	/**
	 * Set the content type on the response, if appropriate in the subclass.
	 * This default implementation does nothing.
	 * 
	 * @param mimeType
	 *            The mime type
	 */
	public void setContentType(final String mimeType)
	{
	}
	
	/**
	 * Set the contents last modified time, if appropriate in the subclass.
	 * This default implementation does nothing.
	 * @param time 
	 *				The time object 
	 */
	public void setLastModifiedTime(Time time)
	{
	}

	/**
	 * @param locale
	 *            Locale to use for this response
	 */
	public void setLocale(final Locale locale)
	{
	}

	/**
	 * Set the default encoding for the output. 
	 * Note: It is up to the derived class to make use of the information.
	 * Class Respsonse simply stores the value, but does not apply
	 * it anywhere automatically.
	 * 
	 * @param encoding
	 */
	public void setCharacterEncoding(final String encoding)
	{
	    this.defaultEncoding = encoding;
	}
	
	/**
	 * Get the default encoding
	 * 
	 * @return default encoding
	 */
	public String getCharacterEncoding()
	{
		if (this.defaultEncoding == null)
		{
			return Application.get().getSettings().getResponseRequestEncoding();
		}
		else
		{
			return this.defaultEncoding;
		}
	}
	
	/**
	 * Writes the given tag to via the write(String) abstract method.
	 * 
	 * @param tag
	 *            The tag to write
	 */
	public final void write(final ComponentTag tag)
	{
		write(tag.toString());
	}

	/**
	 * Writes the given string to the Response subclass output destination.
	 * 
	 * @param string
	 *            The string to write
	 */
	public abstract void write(final String string);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3194.java