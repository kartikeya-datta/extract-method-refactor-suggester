error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4032.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4032.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4032.java
text:
```scala
s@@etIDLName(getIDLName()); // Fix operation names

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.as.jacorb.rmi;

import java.lang.reflect.Method;
import java.rmi.Remote;

import org.omg.CORBA.AttributeMode;

/**
 * Attribute analysis.
 * <p/>
 * Routines here are conforming to the "Java(TM) Language to IDL Mapping
 * Specification", version 1.1 (01-06-07).
 *
 * @author <a href="mailto:osh@sparre.dk">Ole Husgaard</a>
 * @version $Revision: 81018 $
 */
public class AttributeAnalysis extends AbstractAnalysis {

    /**
     * Attribute mode.
     */
    private AttributeMode mode;

    /**
     * Java type.
     */
    private Class cls;

    /**
     * Accessor Method.
     */
    private Method accessor = null;

    /**
     * Mutator Method.
     * This is null for read-only attributes.
     */
    private Method mutator = null;

    /**
     * Accessor method analysis.
     */
    private OperationAnalysis accessorAnalysis = null;

    /**
     * Mutator method analysis.
     * This is null for read-only attributes.
     */
    private OperationAnalysis mutatorAnalysis = null;

    /**
     * Create an attribute analysis.
     */
    private AttributeAnalysis(String javaName, AttributeMode mode, Method accessor, Method mutator)
            throws RMIIIOPViolationException {
        super(Util.javaToIDLName(javaName), javaName);

        this.mode = mode;
        this.cls = accessor.getReturnType();
        this.accessor = accessor;
        this.mutator = mutator;

        // Only do operation analysis if the attribute is in a remote interface.
        if (accessor.getDeclaringClass().isInterface() && Remote.class.isAssignableFrom(accessor.getDeclaringClass())) {
            accessorAnalysis = new OperationAnalysis(accessor);
            if (mutator != null) {
                mutatorAnalysis = new OperationAnalysis(mutator);
            }

            setIDLName(getIDLName()); // Fixup operation names
        }
    }


    /**
     * Create an attribute analysis for a read-only attribute.
     */
    AttributeAnalysis(String javaName, Method accessor)
            throws RMIIIOPViolationException {
        this(javaName, AttributeMode.ATTR_READONLY, accessor, null);
    }

    /**
     * Create an attribute analysis for a read-write attribute.
     */
    AttributeAnalysis(String javaName, Method accessor, Method mutator)
            throws RMIIIOPViolationException {
        this(javaName, AttributeMode.ATTR_NORMAL, accessor, mutator);
    }

    /**
     * Return my attribute mode.
     */
    public AttributeMode getMode() {
        return mode;
    }

    /**
     * Return my Java type.
     */
    public Class getCls() {
        return cls;
    }

    /**
     * Return my accessor method
     */
    public Method getAccessor() {
        return accessor;
    }

    /**
     * Return my mutator method
     */
    public Method getMutator() {
        return mutator;
    }

    /**
     * Return my accessor operation analysis
     */
    public OperationAnalysis getAccessorAnalysis() {
        return accessorAnalysis;
    }

    /**
     * Return my mutator operation analysis
     */
    public OperationAnalysis getMutatorAnalysis() {
        return mutatorAnalysis;
    }

    /**
     * Set my unqualified IDL name.
     * This also sets the names of the associated operations.
     */
    void setIDLName(String idlName) {
        super.setIDLName(idlName);

        // If the first char is an uppercase letter and the second char is not
        // an uppercase letter, then convert the first char to lowercase.
        if (idlName.charAt(0) >= 0x41 && idlName.charAt(0) <= 0x5a
                && (idlName.length() <= 1
 idlName.charAt(1) < 0x41 || idlName.charAt(1) > 0x5a)) {
            idlName =
                    idlName.substring(0, 1).toLowerCase() + idlName.substring(1);
        }

        if (accessorAnalysis != null)
            accessorAnalysis.setIDLName("_get_" + idlName);
        if (mutatorAnalysis != null)
            mutatorAnalysis.setIDLName("_set_" + idlName);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4032.java