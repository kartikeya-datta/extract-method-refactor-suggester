error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17616.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17616.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[163,80]

error in qdox parser
file content:
```java
offset: 4842
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17616.java
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
package wicket.markup.html.form;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import wicket.RequestCycle;
import wicket.markup.ComponentTag;
import wicket.markup.MarkupStream;
import wicket.util.lang.EnumeratedType;

/**
 * Produces a set of radio input options for a collection of values. When the form is
 * submitted, the RadioOptionSet object updates the parent RadioChoice component with the
 * selected value.
 * @author Jonathan Locke
 */
public final class RadioOptionSet extends FormComponent
{
    /** Serial Version ID */
	private static final long serialVersionUID = 2552126944567296644L;

	/** line break markup. */
	public static final Style LINE_BREAK = new Style("<br>");

	/** paragraph markup. */
    public static final Style PARAGRAPH = new Style("<p>");

    /** space markup. */
    public static final Style SPACE = new Style(" ");

    // The style to render in
    private final Style style;

    /**
     * Constructor.
     * @param componentName The component name
     * @param values The option values
     */
    public RadioOptionSet(final String componentName, final Collection values)
    {
        this(componentName, values, LINE_BREAK);
    }

    /**
     * Constructor.
     * @param componentName The component name
     * @param values The option values
     * @param style The style of layout
     */
    public RadioOptionSet(final String componentName, final Collection values, final Style style)
    {
        super(componentName, new ArrayList(values));
        this.style = style;
    }

    /**
     * @see wicket.Component#handleComponentTag(RequestCycle, ComponentTag)
     */
    protected void handleComponentTag(final RequestCycle cycle, final ComponentTag tag)
    {
        checkTag(tag, "span");
        super.handleComponentTag(cycle, tag);
    }

    /**
     * @see wicket.Component#handleBody(RequestCycle, MarkupStream,
     *      ComponentTag)
     */
    protected void handleBody(final RequestCycle cycle, final MarkupStream markupStream,
            final ComponentTag openTag)
    {
        // Buffer to hold generated body
        final StringBuffer options = new StringBuffer();

        // Get the parent RadioChoice
        final RadioChoice parent = (RadioChoice) findParent(RadioChoice.class);

        // Get currently seleced value of radio choice
        final Object selected = parent.getModelObject();

        // Iterate through values
        List values = (List) getModelObject();

        for (final Iterator iterator = values.iterator(); iterator.hasNext();)
        {
            // Get next value
            final Object value = iterator.next();

            // Add choice to parent
            final int index = parent.addRadioOption(value);

            options.append("<input name=\""
                    + parent.getPath() + "\"" + " type=\"radio\""
                    + ((selected == value) ? " checked" : "") + " value=\"" + index + "\">");

            // Add label for radio button
            //TODO support custom labels in future
            final String label = String.valueOf(value.toString());

            options.append(getLocalizer().getString(
                    getName() + "." + label, this, label));

            // Append separator
            if (iterator.hasNext())
            {
                options.append(style.toString());
            }

            // For HTML readability
            options.append('\n');
        }

        // Replace body
        replaceBody(cycle, markupStream, openTag, options.toString());
    }

    /**
     * @see wicket.markup.html.form.FormComponent#updateModel(wicket.RequestCycle)
     */
    public void updateModel(final RequestCycle cycle)
    {
    }

    /**
     * Typesafe enum for layout of radio options.
     */
    public static final class Style extends EnumeratedType
    {
        /**
         * Construct.
         * @param name style name
         */
        Style(final String name)
        {
            super(name);
        }
    }
}

///////////////////////////////// End of File /////////////////////////////////@@
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17616.java