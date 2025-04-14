error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16687.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16687.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16687.java
text:
```scala
public v@@oid addChecksum(Checksum c) {conditions.addElement(c);}

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

package org.apache.tools.ant.taskdefs.condition;

import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Vector;


import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.taskdefs.Available;
import org.apache.tools.ant.taskdefs.Checksum;
import org.apache.tools.ant.taskdefs.UpToDate;

/**
 * Baseclass for the &lt;condition&gt; task as well as several
 * conditions - ensures that the types of conditions inside the task
 * and the "container" conditions are in sync.
 *
 * @author <a href="mailto:stefan.bodewig@epost.de>Stefan Bodewig</a>
 * @version $Revision$
 */
public abstract class ConditionBase extends ProjectComponent {
    private Vector conditions = new Vector();

    /**
     * Count the conditions.
     *
     * @since 1.1
     */
    protected int countConditions() {return conditions.size();}

    /**
     * Iterate through all conditions.
     *
     * @since 1.1
     */
    protected final Enumeration getConditions() {
        return new ConditionEnumeration();
    }

    /**
     * Add an &lt;available&gt; condition.
     *
     * @since 1.1
     */
    public void addAvailable(Available a) {conditions.addElement(a);}

    /**
     * Add an &lt;checksum&gt; condition.
     *
     * @since 1.4
     */
    public void addAvailable(Checksum c) {conditions.addElement(c);}

    /**
     * Add an &lt;uptodate&gt; condition.
     *
     * @since 1.1
     */
    public void addUptodate(UpToDate u) {conditions.addElement(u);}

    /**
     * Add an &lt;not&gt; condition "container".
     *
     * @since 1.1
     */
    public void addNot(Not n) {conditions.addElement(n);}

    /**
     * Add an &lt;and&gt; condition "container".
     *
     * @since 1.1
     */
    public void addAnd(And a) {conditions.addElement(a);}

    /**
     * Add an &lt;or&gt; condition "container".
     *
     * @since 1.1
     */
    public void addOr(Or o) {conditions.addElement(o);}

    /**
     * Add an &lt;equals&gt; condition.
     *
     * @since 1.1
     */
    public void addEquals(Equals e) {conditions.addElement(e);}

    /**
     * Add an &lt;os&gt; condition.
     *
     * @since 1.1
     */
    public void addOs(Os o) {conditions.addElement(o);}

    /**
     * Add an &lt;isset&gt; condition.
     *
     * @since 1.1
     */
    public void addIsSet(IsSet i) {conditions.addElement(i);}

    /**
     * Inner class that configures those conditions with a project
     * instance that need it.
     *
     * @since 1.1
     */
    private class ConditionEnumeration implements Enumeration {
        private int currentElement = 0;

        public boolean hasMoreElements() {
            return countConditions() > currentElement;
        }

        public Object nextElement() throws NoSuchElementException {
            Object o = null;
            try {
                o = conditions.elementAt(currentElement++);
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new NoSuchElementException();
            }
            
            if (o instanceof ProjectComponent) {
                ((ProjectComponent) o).setProject(getProject());
            }
            return o;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16687.java