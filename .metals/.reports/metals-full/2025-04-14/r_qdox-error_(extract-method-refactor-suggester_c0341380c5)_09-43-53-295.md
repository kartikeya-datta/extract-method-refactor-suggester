error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7622.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7622.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,14]

error in qdox parser
file content:
```java
offset: 14
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7622.java
text:
```scala
public final P@@ageMap getDefaultPageMap()

/*
 * $Id$ $Revision:
 * 1.104 $ $Date$
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.application.IClassResolver;
import wicket.authorization.IAuthorizationStrategy;
import wicket.request.ClientInfo;
import wicket.session.ISessionStore;
import wicket.session.ISessionStoreFactory;
import wicket.util.convert.IConverter;
import wicket.util.lang.Objects;
import wicket.util.string.Strings;

/**
 * Holds information about a user session, including some fixed number of most
 * recent pages (and all their nested component information).
 * <ul>
 * <li><b>Access via RequestCycle </b>- The Session for a {@link RequestCycle}
 * can be retrieved by calling {@link RequestCycle#getSession()}.
 * 
 * <li><b>Access via Component </b>- If a RequestCycle object is not available,
 * the Session can be retrieved for a Component by calling
 * {@link Component#getSession()}. As currently implemented, each Component
 * does not itself have a reference to the session that contains it. However,
 * the Page component at the root of the containment hierarchy does have a
 * reference to the Session that holds the Page. So
 * {@link Component#getSession()} traverses the component hierarchy to the root
 * Page and then calls {@link Page#getSession()}.
 * 
 * <li><b>Access via Thread Local </b>- In the odd case where neither a
 * RequestCycle nor a Component is available, the currently active Session for
 * the calling thread can be retrieved by calling the static method
 * Session.get(). This last form should only be used if the first two forms
 * cannot be used since thread local access can involve a potentially more
 * expensive hash map lookup.
 * 
 * <li><b>Locale </b>- A session has a Locale property to support localization.
 * The Locale for a session can be set by calling
 * {@link Session#setLocale(Locale)}. The Locale for a Session determines how
 * localized resources are found and loaded.
 * 
 * <li><b>Style </b>- Besides having an appearance based on locale, resources
 * can also have different looks in the same locale (a.k.a. "skins"). The style
 * for a session determines the look which is used within the appopriate locale.
 * The session style ("skin") can be set with the setStyle() method.
 * 
 * <li><b>Resource Loading </b>- Based on the Session locale and style,
 * searching for resources occurs in the following order (where sourcePath is
 * set via the ApplicationSettings object for the current Application, and style
 * and locale are Session properties):
 * <ul>
 * 1. [sourcePath]/name[style][locale].[extension] <br>
 * 2. [sourcePath]/name[locale].[extension] <br>
 * 3. [sourcePath]/name[style].[extension] <br>
 * 4. [sourcePath]/name.[extension] <br>
 * 5. [classPath]/name[style][locale].[extension] <br>
 * 6. [classPath]/name[locale].[extension] <br>
 * 7. [classPath]/name[style].[extension] <br>
 * 8. [classPath]/name.[extension] <br>
 * </ul>
 * 
 * <li><b>Session Properties </b>- Arbitrary objects can be attached to a
 * Session by installing a session factory on your Application class which
 * creates custom Session subclasses that have typesafe properties specific to
 * the application (see {@link Application} for details). To discourage
 * non-typesafe access to Session properties, no setProperty() or getProperty()
 * method is provided. In a clustered environment, you should take care to
 * 
 * 
 * <li><b>Class Resolver </b>- Sessions have a class resolver (
 * {@link IClassResolver}) implementation that is used to locate classes for
 * components such as pages.
 * 
 * <li><b>Page Factory </b>- A pluggable implementation of {@link IPageFactory}
 * is used to instantiate pages for the session.
 * 
 * <li><b>Removal </b>- Pages can be removed from the Session forcibly by
 * calling remove(Page) or removeAll(), although such an action should rarely be
 * necessary.
 * 
 * @author Jonathan Locke
 * @author Eelco Hillenius
 */
public abstract class Session implements Serializable
{
	/** Name of session attribute under which this session is stored */
	public static final String SESSION_ATTRIBUTE_NAME = "session";

	/** Prefix for attributes holding page map entries */
	static final String pageMapEntryAttributePrefix = "p:";

	/** Thread-local current session. */
	private static final ThreadLocal current = new ThreadLocal();

	/** A store for dirty objects for one request */
	private static final ThreadLocal dirtyObjects  = new ThreadLocal();

	/** Logging object */
	private static final Log log = LogFactory.getLog(Session.class);

	/** Attribute prefix for page maps stored in the session */
	private static final String pageMapAttributePrefix = "m:";

	private static final long serialVersionUID = 1L;

	/** Application that this is a session of. */
	private transient Application application;

	/**
	 * Cached instance of agent info which is typically designated by calling
	 * {@link RequestCycle#newClientInfo()}.
	 */
	private ClientInfo clientInfo;

	/** The converter instance. */
	private transient IConverter converter;

	/** The current pagemap for this request */
	private transient PageMap currentPageMap;

	/** True if session state has been changed */
	private transient boolean dirty = false;


	/** The locale to use when loading resources for this session. */
	private Locale locale;

	/** Factory for constructing Pages for this Session */
	private transient IPageFactory pageFactory;

	/** Number of pagemaps in this session */
	private int pageMaps = 0;

	/** The session store of this session. */
	private transient ISessionStore sessionStore;

	/** Any special "skin" style to use when loading resources. */
	private String style;

	/**
	 * Visitor interface for visiting page maps
	 * 
	 * @author Jonathan Locke
	 */
	public static interface IPageMapVisitor
	{
		/**
		 * @param pageMap
		 *            The page map
		 */
		public void pageMap(final PageMap pageMap);
	}

	/**
	 * Get the session for the calling thread.
	 * 
	 * @return Session for calling thread
	 */
	public static Session get()
	{
		Session session = (Session)current.get();
		if (session == null)
		{
			throw new WicketRuntimeException("there is not session attached to current thread "
					+ Thread.currentThread().getName());
		}
		return session;
	}

	/**
	 * THIS METHOD IS NOT PART OF THE WICKET PUBLIC API. DO NOT CALL IT.
	 * <p>
	 * Sets session for calling thread.
	 * 
	 * @param session
	 *            The session
	 */
	public static void set(final Session session)
	{
		if (session == null)
		{
			throw new IllegalArgumentException("Argument session must me not null");
		}
		current.set(session);
	}

	/**
	 * Constructor.
	 * 
	 * @param application
	 *            The application that this is a session of
	 */
	protected Session(final Application application)
	{
		// Save application
		this.application = application;

		// Set locale to default locale
		setLocale(application.getApplicationSettings().getDefaultLocale());
	}

	/**
	 * Removes all pages from the session. Although this method should rarely be
	 * needed, it is available (possibly for security reasons).
	 */
	public final void clear()
	{
		visitPageMaps(new IPageMapVisitor()
		{
			public void pageMap(PageMap pageMap)
			{
				pageMap.clear();
			}
		});
	}

	/**
	 * Redirects to any intercept page previously specified by a call to
	 * redirectToInterceptPage.
	 * 
	 * @return True if an original destination was redirected to
	 * @see PageMap#redirectToInterceptPage(Page)
	 */
	public final boolean continueToOriginalDestination()
	{
		return currentPageMap.continueToOriginalDestination();
	}

	/**
	 * Get the application that is currently working with this session.
	 * 
	 * @return Returns the application.
	 */
	public final Application getApplication()
	{
		return application;
	}

	/**
	 * @return The authorization strategy for this session
	 */
	public IAuthorizationStrategy getAuthorizationStrategy()
	{
		return application.getSecuritySettings().getAuthorizationStrategy();
	}

	/**
	 * @return The class resolver for this Session
	 */
	public final IClassResolver getClassResolver()
	{
		return application.getApplicationSettings().getClassResolver();
	}

	/**
	 * Gets the client info object for this session. This method lazily gets the
	 * new agent info object for this session. It uses any cached or set ({@link #setClientInfo(ClientInfo)})
	 * client info object or uses {@link RequestCycle#newClientInfo()} to get
	 * the info object based on the current request when no client info object
	 * was set yet, and then caches the returned object; we can expect the
	 * client to stay the same for the whole session, and implementations of
	 * {@link RequestCycle#newClientInfo()} might be relatively expensive.
	 * 
	 * @return the client info object based on this request
	 */
	public ClientInfo getClientInfo()
	{
		if (clientInfo == null)
		{
			this.clientInfo = getRequestCycle().newClientInfo();
		}
		return clientInfo;
	}
	
	/**
	 * @return The current pagemap
	 */
	public PageMap getCurrentPageMap()
	{
		return currentPageMap == null ? getDefaultPageMap() : currentPageMap;
	}
	
	/**
	 * @return The default page map
	 */
	public PageMap getDefaultPageMap()
	{
		return getPageMap(PageMap.DEFAULT_NAME);
	}

	/**
	 * Gets the unique id for this session from the underlying SessionStore
	 * 
	 * @return The unique id for this session
	 */
	public String getId()
	{
		return getSessionStore().getId();
	}

	/**
	 * Get this session's locale.
	 * 
	 * @return This session's locale
	 */
	public Locale getLocale()
	{
		return locale;
	}

	/**
	 * THIS METHOD IS NOT PART OF THE WICKET PUBLIC API. DO NOT CALL IT.
	 * 
	 * Get the page for the given path.
	 * 
	 * @param pageMapName
	 *            The name of the page map where the page is
	 * @param path
	 *            Component path
	 * @param versionNumber
	 *            The version of the page required
	 * @return The page based on the first path component (the page id), or null
	 *         if the requested version of the page cannot be found.
	 */
	public final Page getPage(final String pageMapName, final String path, final int versionNumber)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Getting page [path = " + path + ", versionNumber = " + versionNumber + "]");
		}

		// Get page map by name
		currentPageMap = getPageMap(pageMapName);
		if (currentPageMap != null)
		{
			// Get page entry for id and version
			final String id = Strings.firstPathComponent(path, Component.PATH_SEPARATOR);
			return currentPageMap.get(Integer.parseInt(id), versionNumber);
		}
		return null;
	}

	/**
	 * @return The page factory for this session
	 */
	public final IPageFactory getPageFactory()
	{
		if (pageFactory == null)
		{
			pageFactory = application.getSessionSettings().getPageFactory();
		}
		return pageFactory;
	}

	/**
	 * @param page
	 *            The page, or null if no page context is available
	 * @return The page factory for the page, or the default page factory if
	 *         page was null
	 */
	public final IPageFactory getPageFactory(final Page page)
	{
		if (page != null)
		{
			return page.getPageFactory();
		}
		return getPageFactory();
	}

	/**
	 * @param pageMapName
	 *            Name of page map, or null for default page map
	 * @return PageMap for name
	 */
	public final PageMap getPageMap(String pageMapName)
	{
		return (PageMap)getAttribute(attributeForPageMapName(pageMapName));
	}

	/**
	 * @return A list of all PageMaps in this session.
	 */
	public final List getPageMaps()
	{
		final List list = new ArrayList();
		for (final Iterator iterator = getAttributeNames().iterator(); iterator.hasNext();)
		{
			final String attribute = (String)iterator.next();
			if (attribute.startsWith(pageMapAttributePrefix))
			{
				list.add((PageMap)getAttribute(attribute));
			}
		}
		return list;
	}

	/**
	 * THIS METHOD IS NOT PART OF THE WICKET PUBLIC API. DO NOT CALL IT.
	 * 
	 * @return The currently active request cycle for this session
	 */
	public final RequestCycle getRequestCycle()
	{
		return RequestCycle.get();
	}

	/**
	 * @return Size of this session, including all the pagemaps it contains
	 */
	public final long getSizeInBytes()
	{
		long size = Objects.sizeof(this);
		for (final Iterator iterator = getPageMaps().iterator(); iterator.hasNext();)
		{
			final PageMap pageMap = (PageMap)iterator.next();
			size += pageMap.getSizeInBytes();
		}
		return size;
	}

	/**
	 * Get the style (see {@link wicket.Session}).
	 * 
	 * @return Returns the style (see {@link wicket.Session})
	 */
	public final String getStyle()
	{
		return style;
	}

	/**
	 * Set the session for each PageMap
	 */
	public final void init()
	{
		visitPageMaps(new IPageMapVisitor()
		{
			public void pageMap(PageMap pageMap)
			{
				if (log.isDebugEnabled())
				{
					log.debug("updateSession(): Attaching session to PageMap " + pageMap);
				}
				pageMap.setSession(Session.this);
			}
		});
	}

	/**
	 * Invalidates this session.
	 */
	public void invalidate()
	{
		getSessionStore().invalidate();
	}

	/**
	 * Creates a new page map with a given name
	 * 
	 * @param name
	 *            The name for the new page map
	 * @return The newly created page map
	 */
	public final PageMap newPageMap(final String name)
	{
		// Check that session doesn't have too many page maps already
		final int maxPageMaps = getApplication().getSessionSettings().getMaxPageMaps();
		if (++pageMaps > maxPageMaps)
		{
			throw new IllegalStateException("Session cannot contain more than " + maxPageMaps
					+ " page maps");
		}
		
		// Create new page map
		final PageMap pageMap = new PageMap(name, this);
		setAttribute(attributeForPageMapName(name), pageMap);
		return pageMap;
	}

	/**
	 * THIS METHOD IS NOT PART OF THE WICKET PUBLIC API. DO NOT CALL IT.
	 * 
	 * Creates a new RequestCycle for the given request and response using the
	 * session's request cycle factory.
	 * 
	 * @param request
	 *            The request
	 * @param response
	 *            The response
	 * @return The new request cycle.
	 */
	public final RequestCycle newRequestCycle(final Request request, final Response response)
	{
		return getRequestCycleFactory().newRequestCycle(this, request, response);
	}
	
	/**
	 * @param page
	 *            The page to redirect to
	 */
	public final void redirectToInterceptPage(final Page page)
	{
		PageMap pageMap = currentPageMap;
		if (pageMap == null) 
		{
			pageMap = getPageMap(PageMap.DEFAULT_NAME);
		}
		pageMap.redirectToInterceptPage(page);
	}

	/**
	 * @param pageMap
	 *            Page map to remove
	 */
	public final void removePageMap(final PageMap pageMap)
	{
		removeAttribute(attributeForPageMapName(pageMap.getName()));
	}

	/**
	 * THIS METHOD IS NOT PART OF THE WICKET PUBLIC API. DO NOT CALL IT.
	 * <p>
	 * Sets the application that this session is associated with.
	 * 
	 * @param application
	 *            The application
	 */
	public final void setApplication(final Application application)
	{
		this.application = application;
	}

	/**
	 * THIS METHOD IS NOT PART OF THE WICKET PUBLIC API. DO NOT CALL IT.
	 * <p>
	 * Sets the client info object for this session. This will only work when
	 * {@link #getClientInfo()} is not overriden.
	 * 
	 * @param clientInfo
	 *            the client info object
	 */
	public final void setClientInfo(ClientInfo clientInfo)
	{
		this.clientInfo = clientInfo;
		dirty();
	}

	/**
	 * Set the locale for this session.
	 * 
	 * @param locale
	 *            New locale
	 */
	public final void setLocale(final Locale locale)
	{
		this.locale = locale;
		this.converter = null;
		dirty();
	}

	/**
	 * Set the style (see {@link wicket.Session}).
	 * 
	 * @param style
	 *            The style to set.
	 */
	public final void setStyle(final String style)
	{
		this.style = style;
		dirty();
	}

	/**
	 * THIS METHOD IS NOT PART OF THE WICKET PUBLIC API. DO NOT CALL IT.
	 * <p>
	 * The page will be 'touched' in the session. If it wasn't added yet to the
	 * pagemap, it will be added to the page map else it will set this page to
	 * the front.
	 * 
	 * If another page was removed because of this it will be cleaned up.
	 * 
	 * @param page
	 */
	public final void touch(Page page)
	{
		// Touch the page in its pagemap.
		page.getPageMap().put(page);
	}

	/**
	 * @param visitor
	 *            The visitor to call at each Page in this PageMap.
	 */
	public final void visitPageMaps(final IPageMapVisitor visitor)
	{
		for (final Iterator iterator = getAttributeNames().iterator(); iterator.hasNext();)
		{
			final String attribute = (String)iterator.next();
			if (attribute.startsWith(pageMapAttributePrefix))
			{
				visitor.pageMap((PageMap)getAttribute(attribute));
			}
		}
	}

	/**
	 * Any detach logic for session subclasses.
	 */
	protected void detach()
	{
	}

	/**
	 * Marks session state as dirty
	 */
	protected final void dirty()
	{
		this.dirty = true;
	}

	/**
	 * Gets the attribute value with the given name
	 * 
	 * @param name
	 *            The name of the attribute to store
	 * @return The value of the attribute
	 */
	protected final Object getAttribute(final String name)
	{
		return getSessionStore().getAttribute(name);
	}

	/**
	 * @return List of attributes for this session
	 */
	protected final List getAttributeNames()
	{
		return getSessionStore().getAttributeNames();
	}

	/**
	 * @return Request cycle factory for this kind of session.
	 */
	protected abstract IRequestCycleFactory getRequestCycleFactory();

	/**
	 * Gets the session store.
	 * 
	 * @return the session store
	 */
	protected final ISessionStore getSessionStore()
	{
		if (sessionStore == null)
		{
			ISessionStoreFactory sessionStoreFactory = application.getSessionSettings()
					.getSessionStoreFactory();
			sessionStore = sessionStoreFactory.newSessionStore(this);

			// Still null?
			if (sessionStore == null)
			{
				throw new IllegalStateException(sessionStoreFactory.getClass().getName()
						+ " did not produce a session store");
			}
		}
		return sessionStore;
	}

	/**
	 * Removes the attribute with the given name.
	 * 
	 * @param name
	 *            the name of the attribute to remove
	 */
	protected final void removeAttribute(String name)
	{
		getSessionStore().removeAttribute(name);
	}

	/**
	 * Adds or replaces the attribute with the given name and value.
	 * 
	 * @param name
	 *            The name of the attribute
	 * @param value
	 *            The value of the attribute
	 */
	protected final void setAttribute(String name, Object value)
	{
		// Get the old value if any
		Object oldValue = getAttribute(name);

		// Set the actual attribute
		getSessionStore().setAttribute(name, value);
	}

	/**
	 * Updates the session, e.g. for replication purposes.
	 */
	protected void update()
	{
		// If state is dirty
		if (dirty)
		{
			if (log.isDebugEnabled())
			{
				log.debug("update: Session is dirty.  Replicating.");
			}

			// State is no longer dirty
			this.dirty = false;

			// Set attribute.
			setAttribute(SESSION_ATTRIBUTE_NAME, this);
		}
		else
		{
			if (log.isDebugEnabled())
			{
				log.debug("update: Session not dirty.");
			}
		}

		// Go through all dirty entries, replicating any dirty objects
		List dirtyObjects = (List)Session.dirtyObjects.get();
		if (dirtyObjects != null)
		{
			for (final Iterator iterator = dirtyObjects.iterator(); iterator.hasNext();)
			{
				String attribute = null;
				Object object = iterator.next();
				if (object instanceof Page)
				{
					final Page page = (Page)object;
					attribute = page.getPageMap().attributeForId(page.getNumericId());
					object = page.getPageMapEntry();
				}
				else if (object instanceof PageMap)
				{
					attribute = attributeForPageMapName(((PageMap)object).getName());
				}
	
				// only replicate if the object was really already in the map.
				// for example stateless pages will not be in the map so they
				// shouldn't be added
				Object previous = getAttribute(attribute);
				if (previous != null)
				{
					setAttribute(attribute, object);
				}
			}
			Session.dirtyObjects.set(null);
		}
	}
	/**
	 * @param page
	 */
	void dirtyPage(final Page page)
	{
		List dirtyObjects = getDirtyObjectsList();
		
		// FIXME General: Doesn't this mean that dirtyObjects is really a Set?
		// Since it is threadlocal and can be cleared at end of request, there's
		// no reason to worry about memory here.
		if (!dirtyObjects.contains(page))
		{
			dirtyObjects.add(page);
		}
	}

	/**
	 * @param map
	 */
	void dirtyPageMap(final PageMap map)
	{
		List dirtyObjects = getDirtyObjectsList();
		
		if (!dirtyObjects.contains(map))
		{
			dirtyObjects.add(map);
		}
	}

	/**
	 * Gets the converter instance.
	 * 
	 * @return the converter
	 */
	final IConverter getConverter()
	{
		if (converter == null)
		{
			// Let the factory create a new converter
			converter = getApplication().getApplicationSettings().getConverterFactory()
					.newConverter(getLocale());
		}
		return converter;
	}

	/**
	 * @param pageMapName
	 *            Name of page map
	 * @return Session attribute holding page map
	 */
	private final String attributeForPageMapName(final String pageMapName)
	{
		return pageMapAttributePrefix + pageMapName;
	}

	/**
	 * @return The current thread dirty objects list
	 */
	private List getDirtyObjectsList()
	{
		List lst = (List)dirtyObjects.get();
		if (lst == null)
		{
			lst = new ArrayList(4);
			dirtyObjects.set(lst);
		}
		return lst;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7622.java