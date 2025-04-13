error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5005.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5005.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5005.java
text:
```scala
a@@ssertFalse(CacheMutator.isMutable(Currency.getInstance(Locale.US)));

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2013, Red Hat, Inc., and individual contributors
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
package org.wildfly.clustering.web.infinispan;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

import org.infinispan.AdvancedCache;
import org.infinispan.context.Flag;
import org.jboss.as.clustering.infinispan.invoker.SimpleCacheInvoker;
import org.junit.Test;
import org.wildfly.clustering.annotation.Immutable;
import org.wildfly.clustering.web.sso.AuthenticationType;

public class CacheMutatorTestCase {

    @Test
    public void mutate() {
        @SuppressWarnings("unchecked")
        AdvancedCache<Object, Object> cache = mock(AdvancedCache.class);
        Object id = new Object();
        Object value = new Object();
        
        Mutator mutator = new CacheMutator<Object, Object>(cache, new SimpleCacheInvoker(), id, value);
        
        when(cache.getAdvancedCache()).thenReturn(cache);
        when(cache.withFlags(Flag.IGNORE_RETURN_VALUES)).thenReturn(cache);
        
        mutator.mutate();
        
        verify(cache).replace(same(id), same(value));
        
        mutator.mutate();
        
        verify(cache, atMost(1)).replace(same(id), same(value));
    }

    @Test
    public void isMutable() {
        assertTrue(CacheMutator.isMutable(new Object()));
        assertTrue(CacheMutator.isMutable(new Date()));
        assertFalse(CacheMutator.isMutable(Boolean.TRUE));
        assertFalse(CacheMutator.isMutable(Character.valueOf('a')));
        assertFalse(CacheMutator.isMutable(Currency.getInstance(Locale.getDefault())));
        assertFalse(CacheMutator.isMutable(AuthenticationType.BASIC));
        assertFalse(CacheMutator.isMutable(Locale.getDefault()));
        assertFalse(CacheMutator.isMutable(Byte.valueOf(Integer.valueOf(1).byteValue())));
        assertFalse(CacheMutator.isMutable(Short.valueOf(Integer.valueOf(1).shortValue())));
        assertFalse(CacheMutator.isMutable(Integer.valueOf(1)));
        assertFalse(CacheMutator.isMutable(Long.valueOf(1)));
        assertFalse(CacheMutator.isMutable(Float.valueOf(1)));
        assertFalse(CacheMutator.isMutable(Double.valueOf(1)));
        assertFalse(CacheMutator.isMutable(BigInteger.valueOf(1)));
        assertFalse(CacheMutator.isMutable(BigDecimal.valueOf(1)));
        assertFalse(CacheMutator.isMutable("test"));
        assertFalse(CacheMutator.isMutable(TimeZone.getDefault()));
        assertFalse(CacheMutator.isMutable(UUID.randomUUID()));
        assertFalse(CacheMutator.isMutable(new ImmutableObject()));
    }

    @Immutable
    static class ImmutableObject {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5005.java