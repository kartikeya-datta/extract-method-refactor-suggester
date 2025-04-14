error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12974.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12974.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12974.java
text:
```scala
final C@@harSequence text = xmlParser.getInputFromPositionMarker(-1);

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

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.ApplicationSettings;
import wicket.markup.parser.IMarkupFilter;
import wicket.markup.parser.IXmlPullParser;
import wicket.markup.parser.XmlPullParser;
import wicket.markup.parser.filter.AutolinkHandler;
import wicket.markup.parser.filter.HtmlHandler;
import wicket.markup.parser.filter.PreviewComponentTagRemover;
import wicket.markup.parser.filter.WicketComponentTagIdentifier;
import wicket.markup.parser.filter.WicketParamTagHandler;
import wicket.util.resource.IResource;
import wicket.util.resource.ResourceNotFoundException;


/**
 * This is a Wicket MarkupParser specifically for (X)HTML. It makes use of a 
 * streaming XML parser to read the markup and IMarkupFilters to remove
 * comments, identify Wicket relevant tags, apply html specific treatments
 * etc.. <p>
 * The result will be an Markup object, which is basically a list, containing
 * Wicket relevant tags and RawMarkup.      
 * 
 * @author Jonathan Locke
 */
public final class MarkupParser 
{
    /** Logging */
    private static final Log log = LogFactory.getLog(MarkupParser.class);

    /** Name of desired componentName tag attribute. 
     * E.g. &lt;tag id="wicket-..."&gt; or &lt;tag wicket=..&gt; */
    private String componentNameAttribute = ComponentTag.DEFAULT_COMPONENT_NAME_ATTRIBUTE;

    /** True to strip out HTML comments. */
    private boolean stripComments;

    /** True to compress multiple spaces/tabs or line endings to a single space or line ending. */
    private boolean compressWhitespace;

    /** if true, <wicket:param ..> tags will be removed from markup */
    private boolean stripWicketTag;

    /** if true, "wicket-" will be removed from id="wicket-xxx" */
    private boolean stripWicketFromComponentTag = false;
    
    /** If true, MarkupParser will automatically create a ComponentWicketTag for
     * all tags surrounding a href attribute with a relative path to a
     * html file. E.g. &lt;a href="Home.html"&gt;
     */
    private boolean automaticLinking = false;

    private IXmlPullParser xmlParser = new XmlPullParser();
    
    /**
     * Constructor.
     * @param xmlParser The streaming xml parser to read and parse the markup
     * @param componentNameAttribute The name of the componentName attribute
     */
    public MarkupParser(final IXmlPullParser xmlParser, final String componentNameAttribute)
    {
        this.xmlParser = xmlParser;
        this.componentNameAttribute = componentNameAttribute;
    }
    
    /**
     * Constructor.
     * @param xmlParser The streaming xml parser to read and parse the markup
     */
    public MarkupParser(final IXmlPullParser xmlParser)
    {
        this.xmlParser = xmlParser;
    }
    
    /**
	 * Configure the markup parser based on Wicket application settings
	 * @param settings Wicket application settings
	 */
	public void configure(ApplicationSettings settings)
	{
        this.componentNameAttribute = settings.getComponentNameAttribute();
        this.stripWicketTag = settings.getStripWicketTag();
        this.stripComments = settings.getStripComments();
        this.compressWhitespace = settings.getCompressWhitespace();
        this.automaticLinking = settings.getAutomaticLinking();
        this.stripWicketFromComponentTag = settings.getStripComponentNames();
	}
    
    /**
     * Return the encoding used while reading the markup file.
     * You need to call @see #read(Resource) first to initialise
     * the value.
     * 
     * @return if null, than JVM default is used.
     */
    public String getEncoding()
    {
        return xmlParser.getEncoding();
    }
    
    /**
     * Reads and parses markup from a file.
     * @param resource The file
     * @return The markup
     * @throws ParseException
     * @throws IOException
     * @throws ResourceNotFoundException
     */
    public Markup readAndParse(final IResource resource) throws ParseException, IOException,
            ResourceNotFoundException
    {
        xmlParser.parse(resource);
        return new Markup(resource, parseMarkup());
    }
    
    /**
     * Parse the markup.
     * @param string The markup
     * @return The markup
     * @throws ParseException
     */
    Markup parse(final String string) throws ParseException
    {
        xmlParser.parse(string);
        return new Markup(null, parseMarkup());
    }

    /**
     * Scans the given markup string and extracts balancing tags.
     * @return An immutable list of immutable MarkupElement elements
     * @throws ParseException Thrown if markup is malformed or tags don't balance
     */
    private List parseMarkup() throws ParseException
    {
        // List to return
        final List list = new ArrayList();

        final WicketComponentTagIdentifier detectWicketComponents = new WicketComponentTagIdentifier(xmlParser);
        detectWicketComponents.setComponentNameAttribute(this.componentNameAttribute);
        detectWicketComponents.setStripWicketFromComponentTag(this.stripWicketFromComponentTag);

        final WicketParamTagHandler wicketParamTagHandler = new WicketParamTagHandler(
                new HtmlHandler(detectWicketComponents));
        wicketParamTagHandler.setStripWicketTag(this.stripWicketTag);

        final PreviewComponentTagRemover previewComponentTagRemover = new PreviewComponentTagRemover(wicketParamTagHandler);
        
        final AutolinkHandler autolinkHandler = new AutolinkHandler(previewComponentTagRemover);
        autolinkHandler.setAutomaticLinking(this.automaticLinking);
        
        final IMarkupFilter parser = autolinkHandler;
        
        // Loop through tags
        for (ComponentTag tag; null != (tag = (ComponentTag)parser.nextTag());)
        {
            boolean add = (tag.getComponentName() != null);
            if ((add == false) && tag.getXmlTag().isClose())
            {
                add = ((tag.getOpenTag() != null) && (tag.getOpenTag().getComponentName() != null));
            }
            
            // Add tag to list?
            if (add == true)
            {
                final CharSequence text = 
                    	xmlParser.getInputFromPositionMarker(tag.getPos());
                
                // Add text from last position to tag position
                if (text.length() > 0)
                {
                    String rawMarkup = text.toString();

                    if (stripComments)
                    {
                        rawMarkup = rawMarkup.replaceAll("<!--(.|\n|\r)*?-->", "");
                    }

                    if (compressWhitespace)
                    {
                        rawMarkup = rawMarkup.replaceAll("[ \\t]+", " ");
                        rawMarkup = rawMarkup.replaceAll("( ?[\\r\\n] ?)+", "\n");
                    }

                    list.add(new RawMarkup(rawMarkup));
                }

                if (!"_ignore_".equals(tag.getComponentName()))
                {
	                // Add immutable tag
	                tag.makeImmutable();
	                list.add(tag);
                }
                
                // Position is after tag
                xmlParser.setPositionMarker();
            }
        }

        // Add tail?
        final CharSequence text = ((XmlPullParser)xmlParser).getInputFromPositionMarker(-1);
        if (text.length() > 0)
        {
            list.add(new RawMarkup(text));
        }
        
        // Return immutable list of all MarkupElements
        return Collections.unmodifiableList(list);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12974.java