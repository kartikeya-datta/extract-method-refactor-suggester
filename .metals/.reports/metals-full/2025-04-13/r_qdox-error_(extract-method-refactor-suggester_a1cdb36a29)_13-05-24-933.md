error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8308.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8308.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8308.java
text:
```scala
a@@ssertSlices(targets, pc, conf.getActiveSliceNames(), policy);

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */
package org.apache.openjpa.slice;

import java.util.List;

import org.apache.openjpa.conf.OpenJPAConfiguration;
import org.apache.openjpa.kernel.OpenJPAStateManager;
import org.apache.openjpa.lib.util.Localizer;
import org.apache.openjpa.meta.ClassMetaData;
import org.apache.openjpa.util.UserException;

/**
 * Utility methods to determine the target slices for a persistence capable
 * instance by calling back to user-specified distribution policy.
 * 
 * @author Pinaki Poddar
 *
 */
public class SliceImplHelper {
	private static final Localizer _loc =
		Localizer.forPackage(SliceImplHelper.class);
	
	/**
	 * Gets the target slices by calling user-specified 
	 * {@link DistributionPolicy} or {@link ReplicationPolicy} 
	 * depending on whether the given instance is {@link Replicated replicated}.
	 */
	public static SliceInfo getSlicesByPolicy(Object pc, 
			DistributedConfiguration conf, Object ctx) {
		List<String> actives = conf.getActiveSliceNames();
		Object policy = null;
		String[] targets = null;
		boolean replicated = isReplicated(pc, conf);
		if (replicated) {
			policy = conf.getReplicationPolicyInstance();
			targets = ((ReplicationPolicy)policy).replicate(pc, actives, ctx);
		} else {
			policy = conf.getDistributionPolicyInstance();
			targets = new String[]{((DistributionPolicy)policy).distribute 
				(pc, actives, ctx)};
		}
		assertSlices(targets, pc, actives, policy);
		return new SliceInfo(replicated, targets);
	}
	
	private static void assertSlices(String[] targets, Object pc, 
	    List<String> actives, Object policy) {
	    if (targets == null || targets.length == 0)
            throw new UserException(_loc.get("no-policy-slice", new Object[] {
                policy.getClass().getName(), pc, actives}));
        for (String target : targets) 
            if (!actives.contains(target))
                throw new UserException(_loc.get("bad-policy-slice", 
                   new Object[] {policy.getClass().getName(), target, pc, 
                    actives}));
	}
	
    /**
     * Gets the target slices for the given StateManager.
     */
    public static SliceInfo getSlicesByPolicy(OpenJPAStateManager sm, 
        DistributedConfiguration conf, Object ctx) {
        return getSlicesByPolicy(sm.getPersistenceCapable(), conf, ctx);
    }
    
    
	/**
	 * Affirms if the given instance be replicated to multiple slices.
	 */
	public static boolean isReplicated(Object pc, OpenJPAConfiguration conf) {
		if (pc == null)
			return false;
		ClassMetaData meta = conf.getMetaDataRepositoryInstance()
			.getMetaData(pc.getClass(), null, false);
		return (meta == null) ? false : meta.isReplicated();
	}

	/**
	 * Affirms if the given instance be replicated to multiple slices.
	 */
	public static boolean isReplicated(OpenJPAStateManager sm) {
		if (sm == null)
			return false;
		return sm.getMetaData().isReplicated();
	}
	
	/**
	 * Affirms if the given StateManager has an assigned slice.
	 */
	public static boolean isSliceAssigned(OpenJPAStateManager sm) {
	     return sm != null && sm.getImplData() != null 
	         && sm.getImplData() instanceof SliceInfo;
	}

    /**
     * Gets the assigned slice information, if any, from the given StateManager.
     */
    public static SliceInfo getSliceInfo(OpenJPAStateManager sm) {
        return isSliceAssigned(sm) ? (SliceInfo) sm.getImplData() : null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8308.java