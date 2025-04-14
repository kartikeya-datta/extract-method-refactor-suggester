error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2921.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2921.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2921.java
text:
```scala
w@@aitForStateChange(state);

/*
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
package org.apache.aries.subsystem.core.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import org.apache.aries.subsystem.core.archive.DeploymentManifest;
import org.apache.aries.subsystem.core.archive.SubsystemContentHeader;
import org.osgi.framework.BundleException;
import org.osgi.framework.namespace.IdentityNamespace;
import org.osgi.framework.wiring.BundleRevision;
import org.osgi.resource.Resource;
import org.osgi.service.subsystem.Subsystem.State;
import org.osgi.service.subsystem.SubsystemConstants;
import org.osgi.service.subsystem.SubsystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StopAction extends AbstractAction {
	private static final Logger logger = LoggerFactory.getLogger(StopAction.class);
	
	public StopAction(BasicSubsystem requestor, BasicSubsystem target, boolean disableRootCheck) {
		super(requestor, target, disableRootCheck);
	}
	
	@Override
	public Object run() {
		checkRoot();
		State state = target.getState();
		if (EnumSet.of(State.INSTALLED, State.RESOLVED).contains(state))
			return null;
		else if (EnumSet.of(State.INSTALL_FAILED, State.UNINSTALLING, State.UNINSTALLED).contains(state))
			throw new IllegalStateException("Cannot stop from state " + state);
		else if (EnumSet.of(State.INSTALLING, State.RESOLVING, State.STARTING, State.STOPPING).contains(state)) {
			waitForStateChange();
			return new StopAction(requestor, target, disableRootCheck).run();
		}
		target.setState(State.STOPPING);
		List<Resource> resources = new ArrayList<Resource>(Activator.getInstance().getSubsystems().getResourcesReferencedBy(target));
		SubsystemContentHeader header = target.getSubsystemManifest().getSubsystemContentHeader();
		if (header != null) {
			Collections.sort(resources, new StartResourceComparator(target.getSubsystemManifest().getSubsystemContentHeader()));
			Collections.reverse(resources);
		}
		for (Resource resource : resources) {
			// Don't stop the region context bundle.
			if (Utils.isRegionContextBundle(resource))
				continue;
			try {
				stopResource(resource);
			} 
			catch (Exception e) {
				logger.error("An error occurred while stopping resource " + resource + " of subsystem " + target, e);
			}
		}
		// TODO Can we automatically assume it actually is resolved?
		target.setState(State.RESOLVED);
		try {
			target.setDeploymentManifest(new DeploymentManifest(
					target.getDeploymentManifest(),
					null,
					target.isAutostart(),
					target.getSubsystemId(),
					SubsystemIdentifier.getLastId(),
					target.getLocation(),
					false,
					false));
		}
		catch (Exception e) {
			throw new SubsystemException(e);
		}
		return null;
	}
	
	private void stopBundleResource(Resource resource) throws BundleException {
		if (target.isRoot())
			return;
		((BundleRevision)resource).getBundle().stop();
	}
	
	private void stopResource(Resource resource) throws BundleException, IOException {
		if (Utils.getActiveUseCount(resource) > 0)
			return;
		String type = ResourceHelper.getTypeAttribute(resource);
		if (SubsystemConstants.SUBSYSTEM_TYPE_APPLICATION.equals(type)
 SubsystemConstants.SUBSYSTEM_TYPE_COMPOSITE.equals(type)
 SubsystemConstants.SUBSYSTEM_TYPE_FEATURE.equals(type))
			stopSubsystemResource(resource);
		else if (IdentityNamespace.TYPE_BUNDLE.equals(type))
			stopBundleResource(resource);
		else if (IdentityNamespace.TYPE_FRAGMENT.equals(type))
			return;
		else
			throw new SubsystemException("Unsupported resource type: " + type);
	}
	
	private void stopSubsystemResource(Resource resource) throws IOException {
		new StopAction(target, (BasicSubsystem)resource, !((BasicSubsystem)resource).isRoot()).run();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2921.java