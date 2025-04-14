error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17304.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17304.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17304.java
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
import wicket.util.string.StringList;

/**
 * Validates that a form component's value is of a certain min/max length.
 * Validators are constructed by calling the min, max and range static
 * factory methods.  For example, LengthValidator.min(6) would return a
 * validator valid only when the input of the component it is attached
 * to is at least 6 characters long.  Likewise, LengthValidator.range(3, 5)
 * would only validate a component containing between 3 and 5 characters
 * (inclusive).
 *
 * @author Jonathan Locke
 */
public final class LengthValidator extends AbstractValidator
{
    /** True if minimum bound should be checked. */
    private final boolean checkMin;

    /** True if maximum bound should be checked. */
    private final boolean checkMax;

    /** Lower bound on valid length. */
    private final int min;

    /** Upper bound on valid length. */
    private final int max;

    /**
     * Private constructor forces use of static factory method and static instances.
     * @param checkMin True if minimum bound should be checked
     * @param min Lower bound on valid length
     * @param checkMax True if maximum bound should be checked
     * @param max Upper bound on valid length
     */
    private LengthValidator(final boolean checkMin, final int min,
    		final boolean checkMax, final int max)
    {
        this.min = min;
        this.max = max;
        this.checkMin = checkMin;
        this.checkMax = checkMax;
    }

    /**
     * Gets a length validator object that requires a minimum number of characters.
     * @param min Minimum number of characters
     * @return Validator object
     */
    public static LengthValidator min(final int min)
    {
        return new LengthValidator(true, min, false, 0);
    }

    /**
     * Gets a length validator object that requires a maximum number of characters.
     * @param max Maximum number of characters
     * @return Validator object
     */
    public static LengthValidator max(final int max)
    {
        return new LengthValidator(false, 0, true, max);
    }

    /**
     * Gets a length validator object that requires a minimum and maximum number of
     * characters.
     * @param min Minimum number of characters
     * @param max Maximum number of characters
     * @return Validator object
     */
    public static LengthValidator range(final int min, final int max)
    {
        return new LengthValidator(true, min, true, max);
    }

    /**
     * Validates the given form component.
     * Validates that a form component's value is of a certain minimum 
     * and/or maximum length.
     * @param component The component to validate
     * @return Error for component or NO_ERROR if none
     */
    public ValidationErrorMessage validate(final FormComponent component)
    {
        // Get component value
        final String value = component.getStringValue();
        
        // Check length
        if ((checkMin && value.length() < min) || 
            (checkMax && value.length() > max))
        {
            return errorMessage(value, component);
        }

        return NO_ERROR;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        final StringList list = new StringList();

        if (checkMin)
        {
            list.add("min = " + min);
        }

        if (checkMax)
        {
            list.add("max = " + max);
        }

        return list.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17304.java