error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2792.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2792.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2792.java
text:
```scala
a@@dd(writingStylesLabel.add(italic));

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
package wicket.examples.library;

import java.util.Iterator;

import wicket.Model;
import wicket.PageParameters;
import wicket.RequestCycle;
import wicket.markup.ComponentTagAttributeModifier;
import wicket.markup.html.basic.Label;
import wicket.markup.html.link.ExternalPageLink;
import wicket.util.string.StringList;
import wicket.util.string.StringValueConversionException;


/**
 * A book details page.  Shows information about a book.
 * @author Jonathan Locke
 */
public final class BookDetails extends AuthenticatedHtmlPage
{
    /**
     * Constructor for calls from external page links
     * @param parameters Page parameters
     * @throws StringValueConversionException
     */
    public BookDetails(final PageParameters parameters)
        throws StringValueConversionException
    {
        this(Book.get(parameters.getLong("id")));
    }

    /**
     * Constructor
     * @param book The model
     */
    public BookDetails(final Book book)
    {
        Model bookModel = new Model(book);

        add(new Label("title", book.getTitle()));
        add(new Label("author", book.getAuthor()));
        add(new Label("fiction", Boolean.toString(book.getFiction())));
        add(BookDetails.link("companion", book.getCompanionBook(),
                getLocalizer().getString("noBookTitle", this)));
        add(BookDetails.link("related", book.getRelatedBook(),
                getLocalizer().getString("noBookTitle", this)));

        String writingStyles;
        final boolean hasStyles = (book.getWritingStyles() != null)
            && (book.getWritingStyles().size() > 0);

        if (hasStyles)
        {
            StringList styles = new StringList();

            for (Iterator iterator = book.getWritingStyles().iterator();
                iterator.hasNext();)
            {
                Book.WritingStyle style = (Book.WritingStyle) iterator.next();

                styles.add(getLocalizer().getString(style.toString(), this));
            }

            writingStyles = styles.toString();
        }
        else
        {
            writingStyles = getLocalizer().getString("noWritingStyles", this);
        }

        Label writingStylesLabel = new Label("writingStyles", writingStyles);

        final ComponentTagAttributeModifier italic = new ComponentTagAttributeModifier("class", new Model("italic"));
        italic.setEnabled(!hasStyles);

        add(writingStylesLabel.addAttributeModifier(italic));
        add(EditBook.link("edit", book.getId()));
    }

    public static void setPage(final RequestCycle cycle, final Book book)
    {
        PageParameters parameters = new PageParameters();

        parameters.put("id", new Long(book.getId()));
        cycle.setPage(cycle.getPageFactory().newPage(BookDetails.class, parameters));
        cycle.setRedirect(true);
    }

    /**
     * Creates an external page link
     * @param name The name of the link component to create
     * @param book The book to link to
     * @param noBookTitle The title to show if book is null
     * @return The external page link
     */
    public static ExternalPageLink link(final String name, final Book book,
        final String noBookTitle)
    {
        final ExternalPageLink link = new ExternalPageLink(name,
                BookDetails.class);

        if (book != null)
        {
            link.setParameter("id", book.getId());
            link.add(new Label("title", book));
        }
        else
        {
            link.add(new Label("title", noBookTitle));
            link.setEnabled(false);
        }

        return link;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2792.java