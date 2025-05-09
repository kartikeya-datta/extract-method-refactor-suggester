error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14675.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14675.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14675.java
text:
```scala
p@@ages.remove();

/*
 * Copyright Teachscape
 */
package wicket.protocol.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import wicket.Application;
import wicket.Component;
import wicket.IRequestTarget;
import wicket.IResponseFilter;
import wicket.Page;
import wicket.PageMap;
import wicket.Request;
import wicket.RequestCycle;
import wicket.Session;
import wicket.WicketRuntimeException;
import wicket.Component.IVisitor;
import wicket.markup.html.form.Form;
import wicket.request.target.component.BookmarkablePageRequestTarget;
import wicket.request.target.component.IPageRequestTarget;
import wicket.session.pagemap.IPageMapEntry;
import wicket.util.crypt.Base64;
import wicket.util.string.AppendingStringBuffer;
import wicket.util.string.JavascriptUtils;

/**
 * TODO implement or remove
 * 
 * WARNING: THIS CURRENTLY IS EXPERIMENTAL, AND THERE IS A FAIR CHANCE WE
 * WILL NOT ACTUALLY FINISH THIS IMPLEMENTATION. DO NOT DEPEND ON THIS, THOUGH
 * YOU MIGHT LOOK AT IT FOR INSPIRATION.
 * BE SURE TO <a href="http://www.nabble.com/Nice-Url-tf2473105.html#a6931630">CHECK OUT THIS DISCUSSION</a>
 * AS WELL.
 */
public class ClientPageSavingSessionStore extends HttpSessionStore
{
	private ThreadLocal<Map<String, Object>> pages = new ThreadLocal<Map<String, Object>>()
	{
		@Override
		protected Map<String, Object> initialValue()
		{
			return new HashMap<String, Object>();
		}
	};

	/**
	 * Construct.
	 */
	public ClientPageSavingSessionStore()
	{
		Application.get().getRequestCycleSettings().addResponseFilter(new StateWriter());
		Application.get().getPageSettings().setAutomaticMultiWindowSupport(false);
	}

	/**
	 * @see wicket.protocol.http.AbstractHttpSessionStore#onBeginRequest(wicket.Request)
	 */
	@Override
	public void onBeginRequest(Request request)
	{
		Map<String, Object> map = pages.get();

		String wicketState = request.getParameter("wicketState");
		if (wicketState != null)
		{
			byte[] bytes = Base64.decodeBase64(wicketState.getBytes());
			ByteArrayInputStream in = new ByteArrayInputStream(bytes);
			try
			{
				GZIPInputStream gzip = new GZIPInputStream(in);
				ObjectInputStream ois = new ObjectInputStream(gzip);
				Page page = (Page)ois.readObject();
				ois.close();
				String name = page.getPageMap().attributeForId(page.getNumericId());
				map.put(name, page);
			}
			catch (Exception ex)
			{
				throw new RuntimeException(ex);
			}
		}

	}

	/**
	 * @see wicket.protocol.http.AbstractHttpSessionStore#onEndRequest(wicket.Request)
	 */
	@Override
	public void onEndRequest(Request request)
	{
		pages.set(null);
	}

	/**
	 * @see wicket.session.ISessionStore#getAttribute(wicket.Request,
	 *      java.lang.String)
	 */
	@Override
	public final Object getAttribute(Request request, String name)
	{
		Map<String, Object> store = pages.get();
		Object o = store.get(name);
		if (o == null)
		{
			return super.getAttribute(request, name);
		}
		return o;
	}

	/**
	 * @see wicket.session.ISessionStore#getAttributeNames(wicket.Request)
	 */
	@Override
	public final List<String> getAttributeNames(Request request)
	{
		List<String> lst = super.getAttributeNames(request);
		lst.addAll(pages.get().keySet());
		return lst;
	}

	/**
	 * @see wicket.session.ISessionStore#removeAttribute(wicket.Request,
	 *      java.lang.String)
	 */
	@Override
	public final void removeAttribute(Request request, String name)
	{
		Map<String, Object> store = pages.get();
		if (store.remove(name) == null)
		{
			super.removeAttribute(request, name);
		}
	}

	/**
	 * @see wicket.session.ISessionStore#setAttribute(wicket.Request,
	 *      java.lang.String, java.lang.Object)
	 */
	@Override
	public final void setAttribute(Request request, String name, Object value)
	{
		if (value instanceof Page)
		{
			// set the page to none versioning. this is not needed for client
			// page saving.
			((Page)value).setVersioned(false);
			Map<String, Object> store = pages.get();
			store.put(name, value);
		}
		else
		{
			super.setAttribute(request, name, value);
		}
	}

	/**
	 * @see wicket.protocol.http.HttpSessionStore#createPageMap(java.lang.String,
	 *      wicket.Session)
	 */
	@Override
	public PageMap createPageMap(String name, Session session)
	{
		return new ClientPageSessionStorePageMap(name, session);
	}

	/**
	 * @author jcompagner
	 */
	private static final class ClientPageSessionStorePageMap extends PageMap
	{
		private static final long serialVersionUID = 1L;

		/**
		 * Construct.
		 * 
		 * @param name
		 * @param session
		 */
		private ClientPageSessionStorePageMap(String name, Session session)
		{
			super(name, session);
		}

		@Override
		protected void removeEntry(IPageMapEntry entry)
		{
			// ignored for client state saving.
		}

		@Override
		protected void put(Page page)
		{
			// ignored for client state saving.
		}

		@Override
		protected Page get(int id, int versionNumber)
		{
			RequestCycle rc = RequestCycle.get();
			if (rc != null)
			{
				final IPageMapEntry entry = (IPageMapEntry)Application.get().getSessionStore()
						.getAttribute(rc.getRequest(), attributeForId(id));
				if (entry != null)
				{
					// version should be able to be ignored.. but calling it
					// anyway
					return entry.getPage().getVersion(versionNumber);
				}
			}
			return null;
		}
	}

	/**
	 * Writes session state in the client header.
	 * 
	 * @author eelcohillenius
	 */
	static final class StateWriter implements IResponseFilter
	{
		/**
		 * @see wicket.IResponseFilter#filter(wicket.util.string.AppendingStringBuffer)
		 */
		public AppendingStringBuffer filter(AppendingStringBuffer responseBuffer)
		{
			IRequestTarget rt = RequestCycle.get().getRequestTarget();

			int index = responseBuffer.indexOf("<head>");
			int bodyIndex = responseBuffer.indexOf("</body>");
			if (index != -1 && bodyIndex != -1)
			{
				Page page = null;
				if (rt instanceof IPageRequestTarget)
				{
					page = ((IPageRequestTarget)rt).getPage();
				}
				else if (rt instanceof BookmarkablePageRequestTarget)
				{
					page = ((BookmarkablePageRequestTarget)rt).getPage();
				}

				if (page == null || page.isPageStateless())
				{
					return responseBuffer;
				}
				page.detachModels();
				String encodedState;
				try
				{
					ByteArrayOutputStream out = new ByteArrayOutputStream(256);
					GZIPOutputStream gzip = new GZIPOutputStream(out);
					ObjectOutputStream oos = new ObjectOutputStream(gzip);
					oos.writeObject(page);
					oos.close();
					byte[] unencoded = out.toByteArray();
					byte[] encoded = Base64.encodeBase64(unencoded);
					encodedState = new String(encoded);
				}
				catch (IOException e)
				{
					throw new WicketRuntimeException("Internal error serializing object", e);
				}
				AppendingStringBuffer response = new AppendingStringBuffer(
						encodedState.length() + 100);
				response.append(JavascriptUtils.SCRIPT_OPEN_TAG);
				response.append("\n");
				response.append("var wicketState = '");
				response.append(encodedState);
				response.append("';\n");
				response.append(JavascriptUtils.SCRIPT_CLOSE_TAG);

				final AppendingStringBuffer forms = new AppendingStringBuffer(64);
				forms.append(JavascriptUtils.SCRIPT_OPEN_TAG);
				page.visitChildren(Form.class, new IVisitor()
				{
					public Object component(Component component)
					{
						forms.append("document.getElementById('");
						Form form = (Form)component;
						forms.append(form.getHiddenFieldId(Form.HIDDEN_FIELD_WICKET_STATE));
						forms.append("').value=wicketState;\n");
						return null;
					}
				});
				forms.append(JavascriptUtils.SCRIPT_CLOSE_TAG);
				// log.info("wrote state to client " + encodedState);
				responseBuffer.insert(bodyIndex, forms);
				responseBuffer.insert(index + 6, response);
			}
			return responseBuffer;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14675.java