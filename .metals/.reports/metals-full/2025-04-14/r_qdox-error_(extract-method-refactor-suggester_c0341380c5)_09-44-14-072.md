error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17567.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17567.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[224,2]

error in qdox parser
file content:
```java
offset: 8500
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17567.java
text:
```scala
{ // TODO finalize javadoc

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

import java.util.regex.Matcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.Container;
import wicket.Page;
import wicket.PageParameters;
import wicket.RenderException;
import wicket.RequestCycle;
import wicket.markup.html.link.BookmarkablePageLink;
import wicket.markup.html.link.Link;
import wicket.util.parse.metapattern.Group;
import wicket.util.parse.metapattern.MetaPattern;
import wicket.util.parse.metapattern.OptionalMetaPattern;
import wicket.util.string.Strings;
import wicket.util.value.ValueMap;

/**
 * The AutolinkComponentNameResolver is responsible to deal with 
 * [autolink] components which are used in hrefs like 
 * &lt;a wicket="[autolink] href="Home.html"&gt;Home&lt;/a&gt;
 * 
 * It automatically creates a BookmarkablePageLink component using the tag syntax:
 * "[autolink:parameters]", where parameters can be a list of comma separated key
 * value pairs, such as "x=9,y=foo".
 * 
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

    /** Parse "[AutomaticComponentName:parameters]-<number>". */
    private static final Group automaticCommand = new Group(MetaPattern.WORD);

    /** pattern group for automatic parameters .*/
    private static final Group automaticParameters =
        new Group(MetaPattern.ANYTHING_NON_EMPTY);

    /** pattern group for automatic parameters .*/
    private static final Group automaticIndexPrefix =
        new Group(MetaPattern.DIGITS);

    /** pattern for automatic components. */
    private static final MetaPattern automaticComponentPattern =
        new MetaPattern(new MetaPattern[] {
            MetaPattern.LEFT_SQUARE, automaticCommand,
            new OptionalMetaPattern(new MetaPattern[] {
                    MetaPattern.COLON, automaticParameters}),
            MetaPattern.RIGHT_SQUARE});

    /** Used to create page-unique component name like [autolink]-<autoIndex> */
    private static int autoIndex;

    /** Automatic link destination information. */
    private Class automaticLinkPageClass;

    /** page parameters for automatic links. */
    private PageParameters automaticLinkPageParameters;

    /**
     * Automatically creates a BookmarkablePageLink component using the tag syntax:
     * "[autolink:parameters]", where parameters can be a list of comma separated key
     * value pairs, such as "x=9,y=foo".
     * 
     * @see wicket.markup.IComponentResolver#resolve(RequestCycle, MarkupStream, ComponentTag, Container)
     * @param cycle The current RequestCycle 
     * @param markupStream The current markupStream
     * @param tag The current component tag while parsing the markup
     * @param container The container parsing its markup
     * @return true, if componentName was handle by the resolver. False, otherwise  
     */
	public boolean resolve(final RequestCycle cycle, final MarkupStream markupStream,
			final ComponentTag tag, final Container container)
	{
	    // Get the component name to handle
	    final String componentName = tag.getComponentName();
	    
	    // The componentName may be [autolink] or [autolink:key=value, key2=value, ...]
        if (componentName.startsWith("[autolink")
                && ((componentName.charAt(9) == ']') || (componentName.charAt(9) == ':')))
        {
            // Autolinks are only supported with anchor tags
            if (!tag.getName().equalsIgnoreCase("a"))
            {
                markupStream.throwMarkupException(
                        "Automatic link can only be attached to an anchor tag");
            }
            
            // Try to find the Page matching the href
            resolveAutomaticLink(cycle.getPage(), markupStream, componentName, tag);

	        // Make the compnentName (page-)unique
            final String id = "[autolink]-" + autoIndex;
	        autoIndex ++;
	        
	        // Modify the tag's id-attribute
	        tag.attributes = new ValueMap(tag.attributes);
	        tag.attributes.put("id", id);
	        tag.attributes.makeImmutable();

	        // Create an external (bookmarkable) Link
	        final Link link = new BookmarkablePageLink(id, automaticLinkPageClass,
	                automaticLinkPageParameters);
			
	        // Add the link to the container
			container.add(link);
			if (log.isDebugEnabled()) 
			{
				log.debug("Added autolink " + link);
			}

			// Render the Link
			link.render(cycle);

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
     */
    private void resolveAutomaticLink(final Page page, final MarkupStream markupStream,
    		final String componentName, final ComponentTag tag)
    {
        // Get any automaticLink component
        final Matcher matcher = automaticComponentPattern.matcher(componentName);

        if (matcher.matches())
        {
            final String command = automaticCommand.get(matcher);
            final String parameters = automaticParameters.get(matcher);

            if (command.equals("autolink"))
            {

                // Must have href value
                final String href = tag.getAttributes().getString("href");

                if (href == null)
                {
                    markupStream.throwMarkupException(
                            "Automatic link requires href attribute");
                }

                // Find class relative to current package
                final String path = Strings.stripEnding(href, ".html");

                try
                {
                    automaticLinkPageClass = RequestCycle.get().getApplication()
                    	.getSettings().getPageFactory().getClassInstance(
                    	        page.getClass().getPackage().getName() + "." + path);

                    if (parameters != null)
                    {
                        automaticLinkPageParameters = new PageParameters(parameters);
                    }
                    else
                    {
                        automaticLinkPageParameters = PageParameters.NULL;
                    }
                }
                catch (RenderException e)
                {
                    markupStream.throwMarkupException("Could not find page at " + path);
                }
            }
            else
            {
                if (!command.equals("children") && !command.equals("border"))
                {
                    markupStream.throwMarkupException(
                            "Special component names include [border], [body] and [autolink], "
                            + "but not '" + command + "'");
                }
            }
        }
        else
        {
            markupStream.throwMarkupException("Invalid syntax for automaticLink component");
        }
    }
	
}@@
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17567.java