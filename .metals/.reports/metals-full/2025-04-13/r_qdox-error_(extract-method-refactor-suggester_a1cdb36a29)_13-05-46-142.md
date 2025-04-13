error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17305.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17305.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17305.java
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

import java.util.regex.Pattern;

import wicket.markup.html.form.FormComponent;
import wicket.util.parse.metapattern.MetaPattern;

/**
 * Validates component by matching the component's value against a regular expression
 * pattern.  A PatternValidator can be constructed with either a Java regular expression 
 * (compiled or not) or a MetaPattern.  If the pattern matches against the value of the 
 * component it is attached to when validate() is called by the framework, then that 
 * input value is considered valid.  If the pattern does not match, the errorMessage() 
 * method will be called.
 * <p>
 * For example, to restrict a field to only digits, you might add a PatternValidator
 * constructed with the pattern "\d+".  Another way to do the same thing would 
 * be to construct the PatternValidator passing in MetaPattern.DIGITS.  The advantages
 * of using MetaPattern over straight Java regular expressions are that the patterns
 * are easier to construct and easier to combine into complex patterns.  They are 
 * also more readable and more reusable.  See {@link wicket.util.parse.metapattern.MetaPattern}
 * for details.
 * 
 * @see java.util.regex.Pattern
 * @see wicket.util.parse.metapattern.MetaPattern
 * @author Jonathan Locke
 */
public final class PatternValidator extends AbstractValidator
{
    /** The regexp pattern. */
    private final Pattern pattern;

    /**
     * Constructor.
     * @param pattern Regular expression pattern
     */
    public PatternValidator(final String pattern)
    {
        this(Pattern.compile(pattern));
    }

    /**
     * Constructor.
     * @param pattern Regular expression pattern
     * @param flags Compile flags for java.util.regex.Pattern
     */
    public PatternValidator(final String pattern, final int flags)
    {
        this(Pattern.compile(pattern, flags));
    }

    /**
     * Constructor.
     * @param pattern Java regex pattern
     */
    public PatternValidator(final Pattern pattern)
    {
        this.pattern = pattern;
    }

    /**
     * Constructor.
     * @param pattern MetaPattern pattern
     */
    public PatternValidator(final MetaPattern pattern)
    {
        this(pattern.pattern());
    }

    /**
     * Validates the given form component.
     * @param component The component to validate
     * @return Error for component or NO_ERROR if none
     */
    public ValidationErrorMessage validate(final FormComponent component)
    {
        // Get component value
        final String value = component.getStringValue();
        
        // Check value against pattern
        if (!pattern.matcher(value).matches())
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
        return "[pattern = " + pattern + "]";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17305.java