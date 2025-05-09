error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4449.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4449.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4449.java
text:
```scala
R@@equestCycle.registerRequestListenerInterface(IRedirectListener.class);

/*
 * $Id$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wicket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.markup.MarkupStream;
import wicket.markup.html.form.Form;
import wicket.protocol.http.HttpResponse;


/**
 * Abstract base class for pages.  As a Container subclass, a Page can contain
 * a component hierarchy and markup in some markup language such as HTML.
 * <p>
 * When a page is constructed, it is automatically added to the user's session
 * and assigned the next page id available from the session.  The session that
 * a page is contained in can be retrieved by calling getPageSession().  Page
 * identifiers start at 0 for each session and increment as new pages are added
 * to the session.  The session-unique identifier assigned to a page can be
 * retrieved by calling getId().  This id serves as the Page's component name.
 * So the first page added to a new user session will always be named "0".
 *
 * @see Container
 * @author Jonathan Locke
 * @author Chris Turner
 */
public abstract class Page extends Container implements IRedirectListener
{ // TODO finalize javadoc
    /** Log. */
    private static final Log log = LogFactory.getLog(Page.class);

    /** static for access allowed flag (value == true). */
    protected static final boolean ACCESS_ALLOWED = true;
    
    /** static for access denied flag (value == false). */
    protected static final boolean ACCESS_DENIED = false;
    
    /** This page's identifier. */
    private int id = -1;

    /** The session that this page is in. */
    private Session session;

    /** True if this page is stale. */
    private boolean stale = false;

    /** The rendering before which all pages are stale. */
    private int staleRendering = 0;

    /** temporary reference to the messages in case we are redirecting. */
    FeedbackMessages messages;

    static
    {
        // Allow calls through the IRedirectListener interface
        RequestCycle.registerListenerInterface(IRedirectListener.class);
    }

    /**
     * Constructor.
     */
    protected Page()
    {
        // A page's componentName is its id, which is not determined until
        // setId is called when the page is added to the session
        super(null);

        // Get thread-local session and add this page.  This ensures that
        // all the nice attributes of a page, such as its session and application
        // are accessible in the page constructor.
        Session.get().addPage(this);
    }

    /**
     * Get the identifier for this page.
     * @return The identifier for this page
     */
    public final int getId()
    {
        return id;
    }

    /**
     * Get the name of this page instance is its unique id.
     * @return The name of this page instance is its unique id
     * @see wicket.Component#getName()
     */
    public final String getName()
    {
        return Integer.toString(id);
    }

    /**
     * Get the session for this page.
     * @return Returns the session for this page.
     */
    public final Session getSession()
    {
        return session;
    }

    /**
     * Checks a rendering number against the stale rendering threshold for this page.
     * If the rendering occurred before the stale-rendering number, then the rendering
     * is considered stale.
     * @param rendering The rendering number to check against this page
     * @return Returns true if the given rendering of the page is stale.
     */
    public final boolean isRenderingStale(final int rendering)
    {
        return rendering < staleRendering;
    }

    /**
     * Whether this page has been marked as stale.
     * @return True if this page has been marked as stale
     */
    public final boolean isStale()
    {
        return stale;
    }

    /**
     * Redirect to this page.
     * @see wicket.IRedirectListener#redirect(wicket.RequestCycle)
     */
    public final void redirect(final RequestCycle cycle)
    {
        // This method is used when redirecting to a page
        cycle.setPage(this);
    }

    /**
     * Get the string representation of this container.
     * @return String representation of this container
     */
    public String toString()
    {
        return "[class = " + getClass().getName() + ", id = " + id + "]";
    }

    /**
     * Whether access is allowed to this page.
     * @param cycle request cycle
     * @return true if access is allowed, false otherwise
     */
    protected boolean checkAccess(final RequestCycle cycle)
    {
        return ACCESS_ALLOWED;
    }

    /**
     * Performs a render of this component.
     * @param cycle The response to render to
     */
    public void render(final RequestCycle cycle)
    {
        try
        {
            initUIMessages();
            super.render(cycle);

            // If the application wants component uses checked and 
            // the response is not a redirect
            if (getApplicationSettings().getComponentUseCheck()
                    && !cycle.getResponse().isRedirect())
            {
                // Visit components on page
                checkRendering(this);
            }
        }
        finally // be sure to have models detached
        {
            // Visit components on page
            final Page page = (Page)this;
            detachModels(page);
        }
    }

    /**
     * Looks if any messages were set as a temporary variable on the page
     * and, if so, sets these messages as the current.
     */
    private void initUIMessages()
    {
        if(this.messages != null) 
        {
            // so, we are comming from a redirect;
            // these are the saved messages from the thread that issued 
            // the redirect. Set as the current threads' messages
            FeedbackMessages.set(this.messages);
            
            // reset the page variable
            this.messages = null;
        }
    }

    /**
     * Renders this container to the given response object.
     * @param cycle The response to write to
     */
    protected void handleRender(final RequestCycle cycle)
    {
    	configureResponse(cycle);
    	
        // Check access to page
        if (checkAccess(cycle))
        {
            // Set page's associated markup stream
            final MarkupStream markupStream = getAssociatedMarkupStream();
            setMarkupStream(markupStream);

            // Render all the page's markup
            renderAll(cycle, markupStream);
        }
    }

    /**
     * Set-up response header
     * 
     * @param cycle
     */
    protected void configureResponse(final RequestCycle cycle)
    {
    	cycle.response.setContentType("text/" + getMarkupType());
    	
    	((HttpResponse)cycle.response).setLocale(cycle.getSession().getLocale());
    }

    /**
     * Set the id.
     * @param id The id to set.
     */
    final void setId(final int id)
    {
        this.id = id;
    }

    /**
     * Set the session.
     * @param session The session to set.
     */
    void setSession(Session session)
    {
        this.session = session;
    }

    /**
     * Set whether this page is stale.
     * @param stale whether this page is stale
     */
    final void setStale(final boolean stale)
    {
        this.stale = stale;
    }

    /**
     * Set rendering before which all renderings are stale for this page.
     * @param staleRendering Rendering before which all renderings are stale for this page
     */
    final void setStaleRendering(final int staleRendering)
    {
        this.staleRendering = staleRendering;
    }
	
	/**
	 * Convinience method. Search for children of type fromClass and 
	 * invoke their respectiv removePersistedFormData() method.
	 * 
	 * @see Form#removePersistedFormComponentData(RequestCycle, boolean)
	 * 
	 * @param cycle Current RequestCycle (may belong to another page though)
	 * @param formClass Form to be selected. Pages may have more than one Form.
	 * @param disablePersistence if true, disable persistence for all FormComponents on that page. If false, it will remain unchanged. 
	 */
	final public void removePersistedFormData(final RequestCycle cycle, final Class formClass, final boolean disablePersistence)
	{
		// Visit all children which are an instance of formClass
		visitChildren(formClass, new IVisitor()
        {
            public Object component(final Component component)
            {
            	// They must be of type Form as well
            	if (component instanceof Form)
            	{
            		// Delete persistet FormComponent data and disable persistence
            		((Form)component).removePersistedFormComponentData(cycle, disablePersistence);
            	}
                return CONTINUE_TRAVERSAL;
            }
        });
	}
}

///////////////////////////////// End of File /////////////////////////////////
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4449.java