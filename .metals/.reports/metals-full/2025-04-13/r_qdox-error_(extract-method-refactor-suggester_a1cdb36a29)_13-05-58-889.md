error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17600.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17600.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[76,1]

error in qdox parser
file content:
```java
offset: 2631
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17600.java
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

import wicket.Component;
import wicket.FeedbackMessages;
import wicket.markup.html.HtmlContainer;
import wicket.markup.html.border.Border;
import wicket.markup.html.form.validation.IValidationErrorHandler;


/**
 * A border that can be placed around a form bordered to indicate when the bordered has
 * a validation error.
 *
 * @author Jonathan Locke
 * @author Eelco Hillenius
 */
public final class FormComponentFeedbackBorder extends Border implements IValidationErrorHandler
{
    /** Serial Version ID. */
	private static final long serialVersionUID = -7070716217601930304L;
	
	/** The error indicator child which should be shown if an error occurs. */
    private final HtmlContainer errorIndicator;

    /** the bordered to border; is used to get whether there is an error for it. */
    private final Component bordered;

    /**
     * Constructor.
     * @param componentName The name of the bordered
     * @param bordered the bordered to border
     */
    public FormComponentFeedbackBorder(final String componentName, Component bordered)
    {
        super(componentName);
        this.bordered = bordered;
        add(bordered);
        // Create invisible error indicator bordered that will be shown
        // when a validation error occurs
        errorIndicator = new HtmlContainer("errorIndicator");
        errorIndicator.setVisible(false);
        add(errorIndicator);
    }

    /**
     * Handles validation errors. If any errors were registered, the decorated error
     * indicator will be set to invisible.
     * @param errors list with {@link wicket.markup.html.form.validation.ValidationErrorMessage}s
     * @see wicket.markup.html.form.validation.IValidationErrorHandler#validationError(wicket.FeedbackMessages)
     */
    public void validationError(final FeedbackMessages errors)
    {
        errorIndicator.setVisible(errors.hasErrorMessageFor(bordered));
    }
}
@@
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17600.java