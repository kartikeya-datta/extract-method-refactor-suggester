error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14587.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14587.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14587.java
text:
```scala
m@@ax = fParticles[0].maxEffectiveTotalRange();

/*
 * Copyright 2002,2003-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.xerces.impl.xs;

import org.apache.xerces.xs.*;
import org.apache.xerces.impl.xs.util.XSObjectListImpl;

/**
 * Store schema model group declaration.
 *
 * @author Sandy Gao, IBM
 *
 * @version $Id$
 */
public class XSModelGroupImpl implements XSModelGroup {

    // types of model groups
    // REVISIT: can't use same constants as those for particles, because
    // there are place where the constants are used together. For example,
    // to check whether the content is an element or a sequence.
    public static final short MODELGROUP_CHOICE       = 101;
    public static final short MODELGROUP_SEQUENCE     = 102;
    public static final short MODELGROUP_ALL          = 103;

    // compositor of the model group
    public short fCompositor;

    // particles
    public XSParticleDecl[] fParticles = null;
    public int fParticleCount = 0;

    // this particle's optional annotation
    public XSAnnotationImpl fAnnotation;

    // whether this model group contains nothing
    public boolean isEmpty() {
        for (int i = 0; i < fParticleCount; i++) {
            if (!fParticles[i].isEmpty())
                return false;
        }
        return true;
    }

    /**
     * 3.8.6 Effective Total Range (all and sequence) and
     *       Effective Total Range (choice)
     * The following methods are used to return min/max range for a particle.
     * They are not exactly the same as it's described in the spec, but all the
     * values from the spec are retrievable by these methods.
     */
    public int minEffectiveTotalRange() {
        if (fCompositor == MODELGROUP_CHOICE)
            return minEffectiveTotalRangeChoice();
        else
            return minEffectiveTotalRangeAllSeq();
    }

    // return the sum of all min values of the particles
    private int minEffectiveTotalRangeAllSeq() {
        int total = 0;
        for (int i = 0; i < fParticleCount; i++)
            total += fParticles[i].minEffectiveTotalRange();
        return total;
    }

    // return the min of all min values of the particles
    private int minEffectiveTotalRangeChoice() {
        int min = 0, one;
        if (fParticleCount > 0)
            min = fParticles[0].minEffectiveTotalRange();

        for (int i = 1; i < fParticleCount; i++) {
            one = fParticles[i].minEffectiveTotalRange();
            if (one < min)
                min = one;
        }

        return min;
    }

    public int maxEffectiveTotalRange() {
        if (fCompositor == MODELGROUP_CHOICE)
            return maxEffectiveTotalRangeChoice();
        else
            return maxEffectiveTotalRangeAllSeq();
    }

    // if one of the max value of the particles is unbounded, return unbounded;
    // otherwise return the sum of all max values
    private int maxEffectiveTotalRangeAllSeq() {
        int total = 0, one;
        for (int i = 0; i < fParticleCount; i++) {
            one = fParticles[i].maxEffectiveTotalRange();
            if (one == SchemaSymbols.OCCURRENCE_UNBOUNDED)
                return SchemaSymbols.OCCURRENCE_UNBOUNDED;
            total += one;
        }
        return total;
    }

    // if one of the max value of the particles is unbounded, return unbounded;
    // otherwise return the max of all max values
    private int maxEffectiveTotalRangeChoice() {
        int max = 0, one;
        if (fParticleCount > 0) {
            max = fParticles[0].minEffectiveTotalRange();
            if (max == SchemaSymbols.OCCURRENCE_UNBOUNDED)
                return SchemaSymbols.OCCURRENCE_UNBOUNDED;
        }

        for (int i = 1; i < fParticleCount; i++) {
            one = fParticles[i].maxEffectiveTotalRange();
            if (one == SchemaSymbols.OCCURRENCE_UNBOUNDED)
                return SchemaSymbols.OCCURRENCE_UNBOUNDED;
            if (one > max)
                max = one;
        }
        return max;
    }

    /**
     * get the string description of this particle
     */
    private String fDescription = null;
    public String toString() {
        if (fDescription == null) {
            StringBuffer buffer = new StringBuffer();
            if (fCompositor == MODELGROUP_ALL)
                buffer.append("all(");
            else
                buffer.append('(');
            if (fParticleCount > 0)
                buffer.append(fParticles[0].toString());
            for (int i = 1; i < fParticleCount; i++) {
                if (fCompositor == MODELGROUP_CHOICE)
                    buffer.append('|');
                else
                    buffer.append(',');
                buffer.append(fParticles[i].toString());
            }
            buffer.append(')');
            fDescription = buffer.toString();
        }
        return fDescription;
    }

    public void reset(){
        fCompositor = MODELGROUP_SEQUENCE;
        fParticles = null;
        fParticleCount = 0;
        fDescription = null;
        fAnnotation = null;
    }

    /**
     * Get the type of the object, i.e ELEMENT_DECLARATION.
     */
    public short getType() {
        return XSConstants.MODEL_GROUP;
    }

    /**
     * The <code>name</code> of this <code>XSObject</code> depending on the
     * <code>XSObject</code> type.
     */
    public String getName() {
        return null;
    }

    /**
     * The namespace URI of this node, or <code>null</code> if it is
     * unspecified.  defines how a namespace URI is attached to schema
     * components.
     */
    public String getNamespace() {
        return null;
    }

    /**
     * {compositor} One of all, choice or sequence. The valid constants values
     * are: ALL, CHOICE, SEQUENCE.
     */
    public short getCompositor() {
        if (fCompositor == MODELGROUP_CHOICE)
            return XSModelGroup.COMPOSITOR_CHOICE;
        else if (fCompositor == MODELGROUP_SEQUENCE)
            return XSModelGroup.COMPOSITOR_SEQUENCE;
        else
            return XSModelGroup.COMPOSITOR_ALL;
    }

    /**
     * {particles} A list of particles
     */
    public XSObjectList getParticles() {
        return new XSObjectListImpl(fParticles, fParticleCount);
    }

    /**
     * Optional. Annotation.
     */
    public XSAnnotation getAnnotation() {
        return fAnnotation;
    }

    /**
     * @see org.apache.xerces.xs.XSObject#getNamespaceItem()
     */
    public XSNamespaceItem getNamespaceItem() {
        return null;
    }

} // class XSModelGroupImpl
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14587.java