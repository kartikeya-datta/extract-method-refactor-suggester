error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5895.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5895.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5895.java
text:
```scala
a@@ssertEventEquals(SubsystemState.Activation.EAGER.name(), false, SubsystemState.ChangeType.ACTIVATION, event);

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
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
package org.jboss.as.osgi.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author David Bosschaert
 */
public class SubsystemStateTestCase {

    @Test
    public void testProperties() {
        SubsystemState state = new SubsystemState();

        final List<Observable> observables = new ArrayList<Observable>();
        final List<Object> arguments = new ArrayList<Object>();
        Observer o = new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                observables.add(o);
                arguments.add(arg);
            }
        };
        state.addObserver(o);

        Assert.assertEquals("Precondition", Collections.emptyMap(), state.getProperties());

        Assert.assertEquals("Precondition", 0, arguments.size());
        Assert.assertNull(state.setProperty("a", "aaa"));
        Assert.assertNull(state.setProperty("b", "bbb"));
        Assert.assertEquals("bbb", state.setProperty("b", "ccc"));

        Assert.assertEquals(3, observables.size());
        Assert.assertEquals(Collections.nCopies(3, state), observables);
        Assert.assertEquals(3, arguments.size());

        SubsystemState.ChangeEvent event = (SubsystemState.ChangeEvent) arguments.get(0);
        assertEventEquals("a", false, SubsystemState.ChangeType.PROPERTY, event);
        SubsystemState.ChangeEvent event2 = (SubsystemState.ChangeEvent) arguments.get(1);
        assertEventEquals("b", false, SubsystemState.ChangeType.PROPERTY, event2);
        SubsystemState.ChangeEvent event3 = (SubsystemState.ChangeEvent) arguments.get(2);
        assertEventEquals("b", false, SubsystemState.ChangeType.PROPERTY, event3);

        Assert.assertEquals("aaa", state.getProperties().get("a"));
        Assert.assertEquals("ccc", state.getProperties().get("b"));

        Assert.assertEquals("aaa", state.setProperty("a", null));
        Assert.assertEquals(4, observables.size());
        Assert.assertEquals(Collections.nCopies(4, state), observables);
        Assert.assertEquals(4, arguments.size());
        SubsystemState.ChangeEvent event4 = (SubsystemState.ChangeEvent) arguments.get(3);
        assertEventEquals("a", true, SubsystemState.ChangeType.PROPERTY, event4);

        Assert.assertNull(state.getProperties().get("a"));
        Assert.assertEquals("ccc", state.getProperties().get("b"));
    }

    @Test
    public void testModules() {
        SubsystemState state = new SubsystemState();

        final List<Observable> observables = new ArrayList<Observable>();
        final List<Object> arguments = new ArrayList<Object>();
        Observer o = new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                observables.add(o);
                arguments.add(arg);
            }
        };
        state.addObserver(o);

        Assert.assertEquals("Precondition", 0, state.getCapabilities().size());

        Assert.assertEquals("Precondition", 0, arguments.size());
        SubsystemState.OSGiCapability m = new SubsystemState.OSGiCapability("hi", 3);
        state.addCapability(m);

        Assert.assertEquals(1, arguments.size());
        SubsystemState.ChangeEvent event = (SubsystemState.ChangeEvent) arguments.get(0);
        assertEventEquals("hi", false, SubsystemState.ChangeType.CAPABILITY, event);

        Assert.assertEquals(Collections.singletonList(m), state.getCapabilities());

        Assert.assertNull(state.removeCapability("abc"));
        Assert.assertEquals(Collections.singletonList(m), state.getCapabilities());

        Assert.assertEquals(m, state.removeCapability("hi"));

        Assert.assertEquals(2, arguments.size());
        SubsystemState.ChangeEvent event2 = (SubsystemState.ChangeEvent) arguments.get(1);
        assertEventEquals("hi", true, SubsystemState.ChangeType.CAPABILITY, event2);

        Assert.assertEquals(0, state.getCapabilities().size());
    }

    @Test
    public void testActivation() {
        SubsystemState state = new SubsystemState();

        final List<Observable> observables = new ArrayList<Observable>();
        final List<Object> arguments = new ArrayList<Object>();
        Observer o = new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                observables.add(o);
                arguments.add(arg);
            }
        };
        state.addObserver(o);

        Assert.assertEquals("Default", SubsystemState.Activation.LAZY, state.getActivationPolicy());

        Assert.assertEquals("Precondition", 0, arguments.size());
        state.setActivation(SubsystemState.Activation.LAZY);
        Assert.assertEquals(0, arguments.size());

        state.setActivation(SubsystemState.Activation.EAGER);
        Assert.assertEquals(1, arguments.size());

        SubsystemState.ChangeEvent event = (SubsystemState.ChangeEvent) arguments.get(0);
        assertEventEquals(SubsystemState.Activation.EAGER.toString(), false, SubsystemState.ChangeType.ACTIVATION, event);
    }

    private void assertEventEquals(String id, boolean isRemoved, SubsystemState.ChangeType type, SubsystemState.ChangeEvent event) {
        Assert.assertEquals(id, event.getId());
        Assert.assertEquals(isRemoved, event.isRemoved());
        Assert.assertEquals(type, event.getType());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5895.java