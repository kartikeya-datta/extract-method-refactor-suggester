error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10161.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10161.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[207,2]

error in qdox parser
file content:
```java
offset: 4775
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10161.java
text:
```scala
package wicket.examples.displaytag.export;

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
package displaytag.export;

import java.util.Iterator;

import org.apache.commons.collections.iterators.ArrayIterator;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Enumeration for media types.
 * 
 * @author Fabrizio Giustina
 * @version $Revision$ ($Author$)
 */
public final class MediaTypeEnum
{
    /**
     * Media type CSV = 1.
     */
    public static final MediaTypeEnum CSV = new MediaTypeEnum(1, "csv");

    /**
     * media type EXCEL = 2.
     */
    public static final MediaTypeEnum EXCEL = new MediaTypeEnum(2, "excel");

    /**
     * media type XML = 3.
     */
    public static final MediaTypeEnum XML = new MediaTypeEnum(3, "xml");

    /**
     * media type HTML = 4.
     */
    public static final MediaTypeEnum HTML = new MediaTypeEnum(4, "html");

    /**
     * array containing all the export types.
     */
    public static final MediaTypeEnum[] ALL = { EXCEL, XML, CSV, HTML };

    /**
     * Code; this is the primary key for these objects.
     */
    private final int enumCode;

    /**
     * description.
     */
    private final String enumName;

    /**
     * private constructor. Use only constants.
     * 
     * @param code int code
     * @param name description of media type
     */
    private MediaTypeEnum(int code, String name)
    {
        this.enumCode = code;
        this.enumName = name;
    }

    /**
     * returns the int code.
     * 
     * @return int code
     */
    public int getCode()
    {
        return this.enumCode;
    }

    /**
     * returns the description.
     * 
     * @return String description of the media type ("excel", "xml", "csv",
     *         "html")
     */
    public String getName()
    {
        return this.enumName;
    }

    /**
     * lookup a media type by key.
     * 
     * @param key int code
     * @return MediaTypeEnum or null if no mediaType is found with the given key
     */
    public static MediaTypeEnum fromCode(int key)
    {
        for (int i = 0; i < ALL.length; i++)
        {
            if (key == ALL[i].getCode())
            {
                return ALL[i];
            }
        }
        // lookup failed
        return null;
    }

    /**
     * lookup a media type by an Integer key.
     * 
     * @param key Integer code - null safe: a null key returns a null Enum
     * @return MediaTypeEnum or null if no mediaType is found with the given key
     */
    public static MediaTypeEnum fromIntegerCode(Integer key)
    {
        if (key == null)
        {
            return null;
        } 
        else
        {
            return fromCode(key.intValue());
        }
    }

    /**
     * Lookup a media type by a String key.
     * 
     * @param code
     *            String code - null safe: a null key returns a null Enum
     * @return MediaTypeEnum or null if no mediaType is found with the given key
     */
    public static MediaTypeEnum fromName(String code)
    {
        for (int i = 0; i < ALL.length; i++)
        {
            if (ALL[i].getName().equals(code))
            {
                return ALL[i];
            }
        }
        // lookup failed
        return null;
    }

    /**
     * returns an iterator on all the media type.
     * 
     * @return iterator
     */
    public static Iterator iterator()
    {
        return new ArrayIterator(ALL);
    }

    /**
     * returns the media type description.
     * 
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return getName();
    }

    /**
     * Only a single instance of a specific MediaTypeEnum can be created, so we
     * can check using ==.
     * 
     * @param o the object to compare to
     * @return hashCode
     */
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }

        return false;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        return new HashCodeBuilder(1188997057, -1289297553)
        	.append(this.enumCode).toHashCode();
    }
}
 N@@o newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10161.java