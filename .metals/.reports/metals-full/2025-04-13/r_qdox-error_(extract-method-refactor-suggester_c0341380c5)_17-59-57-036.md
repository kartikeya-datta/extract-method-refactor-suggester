error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17692.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17692.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[132,2]

error in qdox parser
file content:
```java
offset: 3165
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17692.java
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
package wicket.protocol.http.documentvalidation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Class representing an element in a document.
 *
 * @author Chris Turner
 */
public class Tag implements DocumentElement
{

    private String tag;

    private List expectedChildren = new ArrayList();

    private Map expectedAttributes = new HashMap();

    private Set illegalAttributes = new HashSet();

    /**
     * Create the tag element.
     * @param tag The tag name
     */
    public Tag(final String tag)
    {
        this.tag = tag.toLowerCase();
    }

    /**
     * Get the tag that this element represents.
     * @return The tag
     */
    public String getTag()
    {
        return tag;
    }

    /**
     * Add an expected child to this tag. Children must be added in the order they are
     * expected to appear.
     * @param e The element to add
     * @return This
     */
    public Tag addExpectedChild(final DocumentElement e)
    {
        expectedChildren.add(e);
        return this;
    }

    /**
     * Get the list of expected children.
     * @return The expected children
     */
    public List getExpectedChildren()
    {
        return expectedChildren;
    }

    /**
     * Add an expected attribute to this tag. The second parameter is a regexp pattern on
     * which to match the value of the tag.
     * @param name The name of the attribute
     * @param pattern The pattern to match
     */
    public void addExpectedAttribute(final String name, final String pattern)
    {
        expectedAttributes.put(name.toLowerCase(), pattern);
    }

    /**
     * Get the map of expected attributes.
     * @return The expected attribute map
     */
    public Map getExpectedAttributes()
    {
        return expectedAttributes;
    }

    /**
     * Add the name of an attribute that is NOT expected for this tag.
     * @param name The name of the attribute
     */
    public void addIllegalAttribute(final String name)
    {
        illegalAttributes.add(name.toLowerCase());
    }

    /**
     * Get the set of illegal attributes.
     * @return The illegal attributes
     */
    public Set getIllegalAttributes()
    {
        return illegalAttributes;
    }

    /**
     * Output a descriptive string.
     * @return The string
     */
    public String toString()
    {
        return "TAG: " + tag;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17692.java