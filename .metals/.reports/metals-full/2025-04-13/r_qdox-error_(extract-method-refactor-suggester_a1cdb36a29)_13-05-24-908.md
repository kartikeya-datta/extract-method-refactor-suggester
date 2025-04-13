error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8435.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8435.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[57,1]

error in qdox parser
file content:
```java
offset: 2618
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8435.java
text:
```scala
public abstract static class FilterElement {

/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Ant", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */

p@@ackage org.apache.tools.ant.taskdefs.optional.sitraka;

import java.util.Vector;

import org.apache.tools.ant.util.regexp.RegexpMatcher;
import org.apache.tools.ant.util.regexp.RegexpMatcherFactory;

/**
 * Filters information from coverage, somewhat similar to a <tt>FileSet</tt>.
 *
 * @author <a href="mailto:sbailliez@imediation.com">Stephane Bailliez</a>
 */
public class ReportFilters {

    /** user defined filters */
    protected Vector filters = new Vector();

    /** cached matcher for each filter */
    protected Vector matchers = null;

    public ReportFilters() {
    }

    public void addInclude(Include incl) {
        filters.addElement(incl);
    }

    public void addExclude(Exclude excl) {
        filters.addElement(excl);
    }

    public int size() {
        return filters.size();
    }

    /**
     * Check whether a given &lt;classname&gt;&lt;method&gt;() is accepted by the list
     * of filters or not.
     * @param methodname the full method name in the format &lt;classname&gt;&lt;method&gt;()
     */
    public boolean accept(String methodname) {
        // I'm deferring matcher instantiations at runtime to avoid computing
        // the filters at parsing time
        if (matchers == null) {
            createMatchers();
        }
        boolean result = false;
        // assert filters.size() == matchers.size()
        final int size = filters.size();
        for (int i = 0; i < size; i++) {
            FilterElement filter = (FilterElement) filters.elementAt(i);
            RegexpMatcher matcher = (RegexpMatcher) matchers.elementAt(i);
            if (filter instanceof Include) {
                result = result || matcher.matches(methodname);
            } else if (filter instanceof Exclude) {
                result = result && !matcher.matches(methodname);
            } else {
                //not possible
                throw new IllegalArgumentException("Invalid filter element: " + filter.getClass().getName());
            }
        }
        return result;
    }

    /** should be called only once to cache matchers */
    protected void createMatchers() {
        RegexpMatcherFactory factory = new RegexpMatcherFactory();
        final int size = filters.size();
        matchers = new Vector();
        for (int i = 0; i < size; i++) {
            FilterElement filter = (FilterElement) filters.elementAt(i);
            RegexpMatcher matcher = factory.newRegexpMatcher();
            String pattern = filter.getAsPattern();
            matcher.setPattern(pattern);
            matchers.addElement(matcher);
        }
    }


    /** default abstract filter element class */
    abstract public static class FilterElement {
        protected String clazz = "*"; // default is all classes
        protected String method = "*"; // default is all methods

        public void setClass(String value) {
            clazz = value;
        }

        public void setMethod(String value) {
            method = value;
        }

        public String getAsPattern() {
            StringBuffer buf = new StringBuffer(toString());
            StringUtil.replace(buf, ".", "\\.");
            StringUtil.replace(buf, "*", ".*");
            StringUtil.replace(buf, "(", "\\(");
            StringUtil.replace(buf, ")", "\\)");
            return buf.toString();
        }

        public String toString() {
            return clazz + "." + method + "()";
        }
    }

    /** concrete include class */
    public static class Include extends FilterElement {
    }

    /** concrete exclude class */
    public static class Exclude extends FilterElement {
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8435.java