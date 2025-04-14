error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17622.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17622.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[115,80]

error in qdox parser
file content:
```java
offset: 3281
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17622.java
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
package wicket.markup.html.form.validation;

import java.io.Serializable;

import wicket.markup.html.form.FormComponent;


/**
 * Ensures form component has numeric value.
 *
 * @author Jonathan Locke
 */
public final class DecimalValidator extends AbstractValidator
{
    /**
     * Validator that ensures int value.
     */
    public static final DecimalValidator INTEGER =
        new DecimalValidator(Integer.MIN_VALUE, Integer.MAX_VALUE);

    /**
     * Validator that ensures positive int value.
     */
    public static final DecimalValidator POSITIVE_INTEGER =
        new DecimalValidator(0, Integer.MAX_VALUE);

    /**
     * Validator that ensures long value.
     */
    public static final DecimalValidator LONG =
        new DecimalValidator(Long.MIN_VALUE, Long.MAX_VALUE);

    /**
     * Validator that ensures positive long value.
     */
    public static final DecimalValidator POSITIVE_LONG =
        new DecimalValidator(0, Long.MAX_VALUE);

    /** Lower bound on valid decimal number. */
    private final long min;

    /** Upper bound on valid decimal number. */
    private final long max;

    /**
     * Private constructor forces use of static factory method and static instances.
     * @param min Lower bound on valid decimal number
     * @param max Upper bound on valid decimal number
     */
    private DecimalValidator(final long min, final long max)
    {
        this.min = min;
        this.max = max;
    }

    /**
     * Gets a ecimal validator with a given range.
     * @param min Lower bound on valid decimal number
     * @param max Upper bound on valid decimal number
     * @return Validator object
     */
    public static DecimalValidator range(final long min, final long max)
    {
        return new DecimalValidator(min, max);
    }

    /**
     * Validates the given form component.
     * @param input the input
     * @param component The component to validate
     * @return Error for component or NO_ERROR if none
     */
    public ValidationErrorMessage validate(
            final Serializable input, final FormComponent component)
    {
        try
        {
            // Get long value
            final long value = Long.parseLong((String)input);

            if ((value < min) || (value > max))
            {
                return errorMessage(input, component);
            }
        }
        catch (NumberFormatException e)
        {
            return errorMessage(input, component);
        }

        return NO_ERROR;
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17622.java