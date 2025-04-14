error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3332.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3332.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3332.java
text:
```scala
r@@eturn CLASSNAMES[getIndex()];

/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2002 The Apache Software Foundation.  All rights
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
package org.apache.tools.ant.taskdefs.optional.junit;

import java.io.OutputStream;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.optional.junit.formatter.BriefFormatter;
import org.apache.tools.ant.taskdefs.optional.junit.formatter.Formatter;
import org.apache.tools.ant.taskdefs.optional.junit.formatter.XMLFormatter;
import org.apache.tools.ant.types.EnumeratedAttribute;

/**
 * An element representing a <tt>Formatter</tt>
 *
 * <pre>
 * <!ELEMENT formatter (filter)*>
 * <!ATTLIST formatter type (plain|xml|brief) #REQUIRED>
 * <!ATTLIST formatter classname CDATA #REQUIRED>
 * <!ATTLIST formatter extension CDATA #IMPLIED>
 * <!ATTLIST formatter usefile (yes|no) no>
 * </pre>
 *
 * @author <a href="mailto:sbailliez@apache.org">Stephane Bailliez</a>
 *
 * @see JUnitTask
 * @see Formatter
 */
public class FormatterElement {

    /** output stream for the formatter */
    private OutputStream out = new KeepAliveOutputStream(System.out);

    /** formatter classname */
    private String classname;

    /** the filters to apply to this formatter */
    private Vector filters = new Vector();

    /**
     * set an existing type of formatter.
     * @see TypeAttribute
     * @see #setClassname(String)
     */
    public void setType(TypeAttribute type) {
        setClassname(type.getClassName());
    }

    /**
     * <p> Set name of class to be used as the formatter.
     *
     * <p> This class must implement <code>Formatter</code>
     */
    public void setClassname(String classname) {
        this.classname = classname;
    }

    /**
     * Setting a comma separated list of filters in the specified order.
     * @see #addFilter(FilterAttribute)
     * @see FilterAttribute
     */
    public void setFilters(String filters) {
        StringTokenizer st = new StringTokenizer(filters, ",");
        while (st.hasMoreTokens()) {
            FilterElement fe = new FilterElement();
            FilterElement.FilterAttribute fa = new FilterElement.FilterAttribute();
            fa.setValue(st.nextToken());
            fe.setType(fa);
            addFilter(fe);
        }
    }

    /**
     * Add a filter to this formatter.
     */
    public void addFilter(FilterElement fe) {
        filters.addElement(fe);
    }

    /**
     * Set whether the formatter should log to file.
     */
    public void setOutput(OutputAttribute output) {
        this.out = output.getOutputStream();
    }

    /**
     * create the Formatter corresponding to this element.
     */
    protected Formatter createFormatter() throws BuildException {
        if (classname == null) {
            throw new BuildException("you must specify type or classname");
        }
        Formatter f = null;
        try {
            Class clazz = Class.forName(classname);
            if (!Formatter.class.isAssignableFrom(clazz)) {
                throw new BuildException(clazz + " is not a Formatter");
            }
            f = (Formatter) clazz.newInstance();
        } catch (BuildException e) {
            throw e;
        } catch (Exception e) {
            throw new BuildException(e);
        }

        f.setOutput(out);

        // wrap filters in the reverse order: first = top, last = bottom.
        for (int i = filters.size() - 1; i >= 0; i--) {
            FilterElement fe = (FilterElement) filters.elementAt(i);
            f = fe.createFilterFormatter(f);
        }

        return f;
    }

    /**
     * <p> Enumerated attribute with the values "plain", "xml" and "brief".
     * <p> Use to enumerate options for <tt>type</tt> attribute.
     */
    public static class TypeAttribute extends EnumeratedAttribute {
        private final static String[] VALUES = {"plain", "xml", "brief"};
        private final static String[] CLASSNAMES = {"xxx", XMLFormatter.class.getName(), BriefFormatter.class.getName()};

        public String[] getValues() {
            return VALUES;
        }

        public String getClassName() {
            return CLASSNAMES[index];
        }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3332.java