error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13811.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13811.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13811.java
text:
```scala
protected v@@oid handleBody(final MarkupStream markupStream, final ComponentTag openTag)

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
package wicket.markup.html.image;

import javax.servlet.http.HttpServletResponse;

import wicket.IResourceListener;
import wicket.Page;
import wicket.WicketRuntimeException;
import wicket.RequestCycle;
import wicket.markup.ComponentTag;
import wicket.markup.MarkupStream;
import wicket.markup.html.HtmlComponent;
import wicket.model.IModel;
import wicket.protocol.http.HttpResponse;
import wicket.util.io.Streams;
import wicket.util.lang.Classes;
import wicket.util.resource.IResource;
import wicket.util.resource.Resource;
import wicket.util.resource.ResourceNotFoundException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * An image component represents a localizable image resource. The image name 
 * comes from the src attribute of the image tag that the component is attached 
 * to.  The image component responds to requests made via IResourceListener's
 * resourceRequested method.  The image or subclass responds by returning an
 * IResource from getImageResource(String), where String is the source attribute
 * of the image tag.
 * 
 * @author Jonathan Locke
 */
public class Image extends HtmlComponent implements IResourceListener
{
    /** Serial Version ID */
	private static final long serialVersionUID = 555385780092173403L;

    /** The image resource. */
    private IResource image;

    /**
     * @see wicket.Component#Component(String)
     */
    public Image(final String name)
    {
        super(name);
    }

    /**
     * @see wicket.Component#Component(String, IModel)
     */
    public Image(final String name, final IModel model)
    {
        super(name, model);
    }

    /**
     * @see wicket.Component#Component(String, IModel, String)
     */
    public Image(final String name, final IModel model, final String expression)
    {
        super(name, model, expression);
    }

    /**
     * @see wicket.Component#Component(String, Serializable)
     */
    public Image(final String name, final Serializable object)
    {
        super(name, object);
    }

    /**
     * @see wicket.Component#Component(String, Serializable, String)
     */
    public Image(final String name, final Serializable object, final String expression)
    {
        super(name, object, expression);
    }

    /**
     * Implementation of IResourceListener.  Renders resource back to requester.
     * @see wicket.IResourceListener#resourceRequested()
     */
    public void resourceRequested()
    {
        // Get request cycle
        final RequestCycle cycle = getRequestCycle();
        
        // The cycle's page is set to null so that it won't be rendered back to
        // the client since the resource being requested has nothing to do with pages
        cycle.setPage((Page)null);

        // Respond with image
        final HttpServletResponse response = ((HttpResponse)cycle.getResponse()).getServletResponse();
        response.setContentType("image/" + image.getExtension());

        try
        {
            final OutputStream out = new BufferedOutputStream(response.getOutputStream());
            try
            {
                Streams.writeStream(new BufferedInputStream(image.getInputStream()), out);
            }
            finally
            {
                image.close();
                out.flush();
            }
        }
        catch (IOException e)
        {
            throw new WicketRuntimeException("Unable to render resource " + image, e);
        }
        catch (ResourceNotFoundException e)
        {
            throw new WicketRuntimeException("Unable to render resource " + image, e);
        }
    }

    /**
     * @param source The source attribute of the image tag
     * @return Gets the image resource for the component.
     */
    protected IResource getResource(final String source)
    {
        if (source.indexOf("..") != -1 || source.indexOf("/") != -1)
        {
            throw new WicketRuntimeException("Source for image resource cannot contain a path");
        }

        final String path = Classes.packageName(getPage().getClass()) + "." + source;
        return Resource.locate
        (
            getApplicationSettings().getSourcePath(), 
            getPage().getClass().getClassLoader(), 
            path, 
            getStyle(), 
            getLocale(), 
            null
        );
    }

    /**
     * @see wicket.Component#handleBody(MarkupStream, ComponentTag)
     */
    protected void handleBody(MarkupStream markupStream, ComponentTag openTag)
    {
    }

    /**
     * @see wicket.Component#handleComponentTag(ComponentTag)
     */
    protected void handleComponentTag(final ComponentTag tag)
    {
        checkTag(tag, "img");
        super.handleComponentTag(tag);

        final String resourceToLoad;
        final String imageResource = (String)getModelObject();

        if (imageResource != null)
        {
            resourceToLoad = imageResource;
        }
        else
        {
            resourceToLoad = tag.getString("src");
        }

        this.image = getResource(resourceToLoad);

        if (this.image == null)
        {
            throw new WicketRuntimeException("Could not find image resource " + resourceToLoad);
        }

        final String url = getRequestCycle().urlFor(this, IResourceListener.class);
		tag.put("src", url.replaceAll("&", "&amp;"));
    }

	static
    {
        RequestCycle.registerRequestListenerInterface(IResourceListener.class);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13811.java