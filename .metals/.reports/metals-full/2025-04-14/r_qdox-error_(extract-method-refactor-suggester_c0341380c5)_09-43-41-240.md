error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4413.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4413.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4413.java
text:
```scala
p@@ublic class FilterChain extends DataType

/*
 * Copyright  2002-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.apache.tools.ant.types;

import java.util.Vector;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.filters.ChainableReader;
import org.apache.tools.ant.filters.ClassConstants;
import org.apache.tools.ant.filters.EscapeUnicode;
import org.apache.tools.ant.filters.ExpandProperties;
import org.apache.tools.ant.filters.HeadFilter;
import org.apache.tools.ant.filters.LineContains;
import org.apache.tools.ant.filters.LineContainsRegExp;
import org.apache.tools.ant.filters.PrefixLines;
import org.apache.tools.ant.filters.ReplaceTokens;
import org.apache.tools.ant.filters.StripJavaComments;
import org.apache.tools.ant.filters.StripLineBreaks;
import org.apache.tools.ant.filters.StripLineComments;
import org.apache.tools.ant.filters.TabsToSpaces;
import org.apache.tools.ant.filters.TailFilter;
import org.apache.tools.ant.filters.TokenFilter;


/**
 * FilterChain may contain a chained set of filter readers.
 *
 */
public final class FilterChain extends DataType
    implements Cloneable {

    private Vector filterReaders = new Vector();

    public final void addFilterReader(final AntFilterReader filterReader) {
        filterReaders.addElement(filterReader);
    }

    public final Vector getFilterReaders() {
        return filterReaders;
    }

    public final void addClassConstants(final ClassConstants classConstants) {
        filterReaders.addElement(classConstants);
    }

    public final void addExpandProperties(final ExpandProperties expandProperties) {
        filterReaders.addElement(expandProperties);
    }

    public final void addHeadFilter(final HeadFilter headFilter) {
        filterReaders.addElement(headFilter);
    }

    public final void addLineContains(final LineContains lineContains) {
        filterReaders.addElement(lineContains);
    }

    public final void addLineContainsRegExp(final LineContainsRegExp
                                                lineContainsRegExp) {
        filterReaders.addElement(lineContainsRegExp);
    }

    public final void addPrefixLines(final PrefixLines prefixLines) {
        filterReaders.addElement(prefixLines);
    }

    public final void addReplaceTokens(final ReplaceTokens replaceTokens) {
        filterReaders.addElement(replaceTokens);
    }

    public final void addStripJavaComments(final StripJavaComments
                                                stripJavaComments) {
        filterReaders.addElement(stripJavaComments);
    }

    public final void addStripLineBreaks(final StripLineBreaks
                                                stripLineBreaks) {
        filterReaders.addElement(stripLineBreaks);
    }

    public final void addStripLineComments(final StripLineComments
                                                stripLineComments) {
        filterReaders.addElement(stripLineComments);
    }

    public final void addTabsToSpaces(final TabsToSpaces tabsToSpaces) {
        filterReaders.addElement(tabsToSpaces);
    }

    public final void addTailFilter(final TailFilter tailFilter) {
        filterReaders.addElement(tailFilter);
    }

    /**
     * @since Ant 1.6
     */
    public final void addEscapeUnicode(final EscapeUnicode escapeUnicode) {
        filterReaders.addElement(escapeUnicode);
    }

    /**
     * @since Ant 1.6
     */
    public final void addTokenFilter(final TokenFilter tokenFilter) {
        filterReaders.addElement(tokenFilter);
    }

    /**
     * delete characters filter
     * @since Ant 1.6
     */
    public void addDeleteCharacters(TokenFilter.DeleteCharacters filter) {
        filterReaders.addElement(filter);
    }

    /**
     * containsregex
     * @since Ant 1.6
     */
    public void addContainsRegex(TokenFilter.ContainsRegex filter) {
        filterReaders.addElement(filter);
    }

    /**
     * replaceregex
     * @since Ant 1.6
     */
    public void addReplaceRegex(TokenFilter.ReplaceRegex filter) {
        filterReaders.addElement(filter);
    }

    /**
     * trim
     * @since Ant 1.6
     */
    public void addTrim(TokenFilter.Trim filter) {
        filterReaders.addElement(filter);
    }

    /**
     * replacestring
     * @since Ant 1.6
     */
    public void addReplaceString(
        TokenFilter.ReplaceString filter) {
        filterReaders.addElement(filter);
    }

    /**
     * ignoreBlank
     * @since Ant 1.6
     */
    public void addIgnoreBlank(
        TokenFilter.IgnoreBlank filter) {
        filterReaders.addElement(filter);
    }


    /**
     * Makes this instance in effect a reference to another FilterChain
     * instance.
     *
     * <p>You must not set another attribute or nest elements inside
     * this element if you make it a reference.</p>
     *
     * @param r the reference to which this instance is associated
     * @exception BuildException if this instance already has been configured.
     */
    public void setRefid(Reference r) throws BuildException {
        if (!filterReaders.isEmpty()) {
            throw tooManyAttributes();
        }
        // change this to get the objects from the other reference
        Object o = r.getReferencedObject(getProject());
        if (o instanceof FilterChain) {
            FilterChain fc = (FilterChain) o;
            filterReaders = fc.getFilterReaders();
        } else {
            String msg = r.getRefId() + " doesn\'t refer to a FilterChain";
            throw new BuildException(msg);
        }

        super.setRefid(r);
    }

    /**
     * add a chainfilter
     * @since Ant 1.6
     */

    public void add(ChainableReader filter) {
        filterReaders.addElement(filter);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4413.java