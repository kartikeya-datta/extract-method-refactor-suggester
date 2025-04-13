error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16952.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16952.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16952.java
text:
```scala
final S@@tring id = componentName + page.getAutoIndex();

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
package wicket.markup;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.Container;
import wicket.Page;
import wicket.PageParameters;
import wicket.RequestCycle;
import wicket.markup.html.link.BookmarkablePageLink;
import wicket.markup.html.link.Link;
import wicket.util.value.ValueMap;

/**
 * The AutolinkComponentNameResolver is responsible to handle automatic link
 * resolution. Autolink components are automatically created by MarkupParser 
 * for anchor tags with no explicit wicket component. 
 * E.g. &lt;a href="Home.html"&gt;
 * <p>
 * For each such tag a BookmarkablePageLink will be automatically created.
 * <p>
 * It resolves the given URL by searching for a page class at
 * the relative URL specified by the href attribute of the tag. The href URL is
 * relative to the package containing the page where this component is contained.
 * 
 * @author Juergen Donnerstag
 */
public class AutolinkComponentResolver implements IComponentResolver
{
    /** Logging */
    private static Log log = LogFactory.getLog(AutolinkComponentResolver.class);

    /**
     * Automatically creates a ExternalPageLink component.
     * 
     * @see wicket.markup.IComponentResolver#resolve(RequestCycle, MarkupStream, ComponentTag, Container)
     * @param cycle The current RequestCycle 
     * @param markupStream The current markupStream
     * @param tag The current component tag while parsing the markup
     * @param container The container parsing its markup
     * @return true, if componentName was handle by the resolver. False, otherwise  
     */
	public boolean resolve(final Container container, final MarkupStream markupStream,
			final ComponentTag tag)
	{
	    // Must be marked as autolink tag
        if (tag.isAutolinkEnabled())
        {
            // Autolinks are only supported with anchor tags
            if (!tag.getName().equalsIgnoreCase("a"))
            {
                markupStream.throwMarkupException(
                        "Automatic link can only be attached to an anchor tag");
            }
            
            // Try to find the Page matching the href
    	    final String componentName = tag.getComponentName();
            final Link link = resolveAutomaticLink(container.getPage(), markupStream, componentName, tag);

	        // Add the link to the container
			container.add(link);
			if (log.isDebugEnabled()) 
			{
				log.debug("Added autolink " + link);
			}

			// Render the Link
			link.render();

			// Tell the container, we handled the componentName
			return true;
		}
	
        // We were not able to handle the componentName
        return false;
	}
	
    /**
     * Resolves the given tag's automaticLinkPageClass and automaticLinkPageParameters
     * variables by parsing the tag component name and then searching for a page class at
     * the relative URL specified by the href attribute of the tag. The href URL is
     * relative to the package containing the page where this component is contained.
     * @param page The page where the link is
     * @param markupStream Markup stream to use when throwing any exceptions
     * @param componentName the name of the component
     * @param tag the component tag
     * @return A BookmarkablePageLink to handle the href
     */
    private Link resolveAutomaticLink(final Page page, final MarkupStream markupStream,
    		final String componentName, final ComponentTag tag)
    {
        final String originalHref = tag.getAttributes().getString("href");

        final int pos = originalHref.indexOf(".html");
        if (pos <= 0)
        {
            markupStream.throwMarkupException(
            	"Expected to find '*.html' in href: " + originalHref);
        }
        
        String classPath = originalHref.substring(0, pos);
        PageParameters pageParameters = null;
        
        // ".html?" => 6 chars
        if ((classPath.length() + 6) < originalHref.length())
        {
            String queryString = originalHref.substring(classPath.length() + 6);
            pageParameters = new PageParameters(new ValueMap(queryString, "&"));
        }
        
        classPath = classPath.replaceAll("/", ".");
        classPath = page.getClass().getPackage().getName() + "." + classPath;
        
        Class clazz = page.getApplicationSettings().getDefaultClassResolver().resolveClass(classPath);

        // Make the componentName (page-)unique
        final String id = componentName + "-" + page.getAutoIndex();
        
        return new BookmarkablePageLink(id, clazz, pageParameters);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16952.java