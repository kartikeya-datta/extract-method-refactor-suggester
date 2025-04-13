error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17740.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17740.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[185,80]

error in qdox parser
file content:
```java
offset: 4116
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17740.java
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
package wicket.util.parse;

import wicket.util.value.ValueMap;

/**
 * Holds information for a parsed tag, such as an HTML tag.
 * @author Jonathan Locke
 */
public final class Tag
{
    int pos;

    int length;

    String name;

    String text;

    ValueMap attributes = new ValueMap();

    private boolean isMutable;

    boolean isClose;

    boolean isOpen;

    /**
     * Returns a mutable copy of this tag
     * @param tag The tag to copy
     * @return The mutable copy
     */
    public static Tag mutableCopy(final Tag tag)
    {
        final Tag newTag = new Tag();

        newTag.name = tag.name;
        newTag.pos = tag.pos;
        newTag.length = tag.length;
        newTag.attributes = new ValueMap(tag.attributes);
        newTag.isOpen = tag.isOpen;
        newTag.isClose = tag.isClose;
        newTag.isMutable = false;

        return newTag;
    }

    /**
     * Gets the name of the tag, for example the tag <b>'s name would be 'b'
     * @return The tag's name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Makes this tag object immutable by making the attribute map unmodifiable
     */
    public void makeImmutable()
    {
        if (isMutable)
        {
            isMutable = false;
            attributes.makeImmutable();
        }
    }

    /**
     * Gets a hashmap of this tag's attributes.
     * @return The tag's attributes
     */
    public ValueMap getAttributes()
    {
        return attributes;
    }

    /**
     * The location of the tag in the input string
     * @return Tag location (index in input string)
     */
    public int getPos()
    {
        return pos;
    }

    /**
     * Gets the length of the tag in characters
     * @return The tag's length
     */
    public int getLength()
    {
        return length;
    }

    /**
     * Returns true if the tag is a close tag
     * @return True if the tag is a close tag
     */
    public boolean isClose()
    {
        return isClose;
    }

    /**
     * Returns true if the tag is a open tag
     * @return True if the tag is a open tag
     */
    public boolean isOpen()
    {
        return isOpen;
    }

    /**
     * Converts this object to a string representation
     * @return String version of this object
     */
    public String toDebugString()
    {
        return "[Tag name="
                + name + ", pos=" + pos + ", length=" + length + ", attributes=[" + attributes
                + "], isClose=" + isClose + ", isOpen=" + isOpen + "]";
    }

    /**
     * Converts this object to a string representation
     * @return String version of this object
     */
    public String toString()
    {
        if (!isMutable)
        {
            return text;
        }
        else
        {
            final StringBuffer buffer = new StringBuffer();

            buffer.append('<');

            if (isClose && !isOpen)
            {
                buffer.append('/');
            }

            buffer.append(name);

            if (attributes.size() > 0)
            {
                buffer.append(' ');
                buffer.append(attributes);
            }

            if (isClose && isOpen)
            {
                buffer.append('/');
            }

            buffer.append('>');

            return buffer.toString();
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17740.java