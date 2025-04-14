error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16785.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16785.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16785.java
text:
```scala
S@@tringBuilder b = new StringBuilder("[RequestParameters ");

/*
 * $Id: RequestParameters.java 5798 2006-05-20 15:55:29 +0000 (Sat, 20 May 2006)
 * joco01 $ $Revision$ $Date: 2006-05-20 15:55:29 +0000 (Sat, 20 May
 * 2006) $
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
package wicket.request;

import java.io.Serializable;
import java.util.Map;

import wicket.RequestListenerInterface;
import wicket.markup.html.link.ILinkListener;

/**
 * <p>
 * Object that abstracts common request parameters. It consists of possible
 * optional parameters that can be translated from e.g. servlet request
 * parameters and serves of a strongly typed variant of these that is to be
 * created by the {@link wicket.request.IRequestCycleProcessor}'s
 * {@link wicket.request.IRequestCodingStrategy}.
 * </p>
 * <p>
 * Though this object can be extended and hence more parameter options can be
 * used, anything other than in this implementation must be supported by
 * specific {@link wicket.request.IRequestCycleProcessor} implementations and
 * thus are not supported by default implementations.
 * </p>
 * 
 * @author Eelco Hillenius
 */
public class RequestParameters implements Serializable
{
	private static final long serialVersionUID = 1L;

	/** the full path to a component (might be just the page). */
	private String componentPath;

	/** any name of the page map. */
	private String pageMapName;

	/** any version number; 0 for no version. */
	private int versionNumber;

	/** any callable interface name (e.g. {@link ILinkListener}). */
	private String interfaceName;

	/**
	 * in case this request points to a dispatched call to a behavior that is
	 * coupled to a component, this is the registration id of the behavior.
	 */
	private String behaviorId;

	/** any id of a non-page target component. */
	private String componentId;

	/** any bookmarkable page class. */
	private String bookmarkablePageClass;

	/** bookmarkable form name */
	private String bookmarkableFormName;

	/** free-to-use map of non-reserved parameters. */
	private Map<String, ? extends Object> parameters;

	/** any resource key. */
	private String resourceKey;

	/** the path info. */
	private String path;

	/**
	 * Construct.
	 */
	public RequestParameters()
	{

	}

	/**
	 * Gets any bookmarkable page class.
	 * 
	 * @return any bookmarkable page class
	 */
	public String getBookmarkablePageClass()
	{
		return bookmarkablePageClass;
	}

	/**
	 * Sets any bookmarkable page class.
	 * 
	 * @param bookmarkablePageClass
	 *            any bookmarkable page class
	 */
	public void setBookmarkablePageClass(String bookmarkablePageClass)
	{
		this.bookmarkablePageClass = bookmarkablePageClass;
	}

	/**
	 * Gets any id of a non-page target component.
	 * 
	 * @return any id of a non-page target component
	 */
	public String getComponentId()
	{
		return componentId;
	}

	/**
	 * Sets any id of a non-page target component.
	 * 
	 * @param componentId
	 *            any id of a non-page target component
	 */
	public void setComponentId(String componentId)
	{
		this.componentId = componentId;
	}

	/**
	 * Gets the full path to a component (might be just the page)..
	 * 
	 * @return the full path to a component (might be just the page).
	 */
	public String getComponentPath()
	{
		return componentPath;
	}

	/**
	 * Sets the full path to a component (might be just the page)..
	 * 
	 * @param componentPath
	 *            the full path to a component (might be just the page).
	 */
	public void setComponentPath(String componentPath)
	{
		this.componentPath = componentPath;
	}

	/**
	 * @return The interface named by these request parameters
	 */
	public RequestListenerInterface getInterface()
	{
		return RequestListenerInterface.forName(getInterfaceName());
	}

	/**
	 * Gets any callable interface name (e.g. {@link ILinkListener}).
	 * 
	 * @return any callable interface name (e.g. {@link ILinkListener})
	 */
	public String getInterfaceName()
	{
		return interfaceName;
	}

	/**
	 * Sets any callable interface name (e.g. {@link ILinkListener}).
	 * 
	 * @param interfaceName
	 *            any callable interface name (e.g. {@link ILinkListener})
	 */
	public void setInterfaceName(String interfaceName)
	{
		this.interfaceName = interfaceName;
	}

	/**
	 * Gets any name of the page map.
	 * 
	 * @return any name of the page map
	 */
	public String getPageMapName()
	{
		return pageMapName;
	}

	/**
	 * Sets any name of the page map.
	 * 
	 * @param pageMapName
	 *            any name of the page map
	 */
	public void setPageMapName(String pageMapName)
	{
		this.pageMapName = pageMapName;
	}

	/**
	 * Gets free-to-use map of non-reserved parameters.
	 * 
	 * @return free-to-use map of non-reserved parameters
	 */
	public Map<String, ? extends Object> getParameters()
	{
		return parameters;
	}

	/**
	 * Sets free-to-use map of non-reserved parameters.
	 * 
	 * @param parameters
	 *            free-to-use map of non-reserved parameters
	 */
	public void setParameters(Map<String, ? extends Object> parameters)
	{
		this.parameters = parameters;
	}

	/**
	 * Gets any resource key.
	 * 
	 * @return any resource key
	 */
	public String getResourceKey()
	{
		return resourceKey;
	}

	/**
	 * Sets any resource key.
	 * 
	 * @param resourceKey
	 *            any resource key
	 */
	public void setResourceKey(String resourceKey)
	{
		this.resourceKey = resourceKey;
	}

	/**
	 * Gets any version information string.
	 * 
	 * @return any version information string
	 */
	public int getVersionNumber()
	{
		return versionNumber;
	}

	/**
	 * Sets any version information string.
	 * 
	 * @param versionNumber
	 *            any version information string
	 */
	public void setVersionNumber(int versionNumber)
	{
		this.versionNumber = versionNumber;
	}

	/**
	 * Gets path info.
	 * 
	 * @return path info
	 */
	public String getPath()
	{
		return path;
	}

	/**
	 * Sets path info.
	 * 
	 * @param pathInfo
	 *            path info
	 */
	public void setPath(String pathInfo)
	{
		this.path = pathInfo;
	}

	/**
	 * Gets the component registration id of any behavior.
	 * 
	 * @return behaviorId the id
	 */
	public String getBehaviorId()
	{
		return behaviorId;
	}

	/**
	 * Sets the component registration id of any behavior.
	 * 
	 * @param behaviorId
	 *            the id
	 */
	public void setBehaviorId(String behaviorId)
	{
		this.behaviorId = behaviorId;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuffer b = new StringBuffer("[RequestParameters ");
		if (getComponentPath() != null)
		{
			b.append(" componentPath=").append(getComponentPath());
			b.append(" pageMapName=").append(getPageMapName());
			b.append(" versionNumber=").append(getVersionNumber());
			b.append(" interfaceName=").append(getInterfaceName());
			b.append(" componentId=").append(getComponentId());
			b.append(" behaviorId=").append(getBehaviorId());
		}
		if (getBookmarkablePageClass() != null)
		{
			b.append(" bookmarkablePageClass=").append(getBookmarkablePageClass());
		}
		if (getParameters() != null)
		{
			b.append(" parameters=").append(getParameters());
		}
		if (getResourceKey() != null)
		{
			b.append(" resourceKey=").append(getResourceKey());
		}
		b.append("]");
		return b.toString();
	}

	/**
	 * Gets the bookmarkable form name if this was a request from a bookmarkable
	 * form.
	 * 
	 * @return String the bookmarkable form name
	 */
	public String getBookmarkableFormName()
	{
		return bookmarkableFormName;
	}

	/**
	 * @param bookmarkableFormName
	 */
	public void setBookmarkableFormName(String bookmarkableFormName)
	{
		this.bookmarkableFormName = bookmarkableFormName;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16785.java