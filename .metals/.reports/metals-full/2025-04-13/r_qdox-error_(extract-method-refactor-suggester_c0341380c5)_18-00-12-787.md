error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17303.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17303.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17303.java
text:
```scala
final S@@tring value = component.getRequestString();

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

import wicket.markup.html.form.FormComponent;

/**
 * Ensures that the form component has a numeric value in a given range. 
 * The range static factory method constructs a DecimalValidator with
 * minimum and maximum values specified as Java longs.  Convenience fields
 * exist for INT, POSITIVE_INT, LONG and POSITIVE_LONG which match the
 * appropriate ranges of numbers.
 *
 * @author Jonathan Locke
 */
public final class DecimalValidator extends AbstractValidator
{
    /**
     * Validator that ensures int value.
     */
    public static final DecimalValidator INT =
        new DecimalValidator(Integer.MIN_VALUE, Integer.MAX_VALUE);

    /**
     * Validator that ensures positive int value.
     */
    public static final DecimalValidator POSITIVE_INT =
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
     * Gets a decimal validator with a given range.
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
     * Ensures that the form component has a numeric value. If min and max arguments are
     * given, this validator also ensures the value is in bounds.
     * @param component The component to validate
     * @return Error for component or NO_ERROR if none
     */
    public ValidationErrorMessage validate(final FormComponent component)
    {
        // Get component value
        final String value = component.getStringValue();
        
        try
        {
            // Get long value
            final long longValue = Long.parseLong(value);
            
            // Check range
            if (longValue < min || longValue > max)
            {
                return errorMessage(value, component);
            }
        }
        catch (NumberFormatException e)
        {
            return errorMessage(value, component);
        }

        return NO_ERROR;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17303.java