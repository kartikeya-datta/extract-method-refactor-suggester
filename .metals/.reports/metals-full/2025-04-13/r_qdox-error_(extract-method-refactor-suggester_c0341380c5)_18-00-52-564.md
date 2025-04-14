error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1766.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1766.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1766.java
text:
```scala
r@@eturn provisionTo.getLocation() + "!/" + ResourceHelper.getLocation(resource);

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

import org.apache.aries.subsystem.core.archive.DeployedContentHeader;
import org.apache.aries.subsystem.core.archive.DeploymentManifest;
import org.osgi.framework.namespace.IdentityNamespace;
import org.osgi.resource.Resource;
import org.osgi.service.coordinator.Coordination;
import org.osgi.service.coordinator.Participant;
import org.osgi.service.subsystem.SubsystemConstants;
import org.osgi.service.subsystem.SubsystemException;

public abstract class ResourceInstaller {
	public static ResourceInstaller newInstance(Coordination coordination, Resource resource, AriesSubsystem subsystem) {
		String type = ResourceHelper.getTypeAttribute(resource);
		if (SubsystemConstants.SUBSYSTEM_TYPE_APPLICATION.equals(type)
 SubsystemConstants.SUBSYSTEM_TYPE_COMPOSITE.equals(type)
 SubsystemConstants.SUBSYSTEM_TYPE_FEATURE.equals(type))
			return new SubsystemResourceInstaller(coordination, resource, subsystem);
		else if (IdentityNamespace.TYPE_BUNDLE.equals(type) || IdentityNamespace.TYPE_FRAGMENT.equals(type))
			return new BundleResourceInstaller(coordination, resource, subsystem);
		else
			throw new SubsystemException("No installer exists for resource type: " + type);
	}
	
	protected final Coordination coordination;
	protected final AriesSubsystem provisionTo;
	protected final Resource resource;
	protected final AriesSubsystem subsystem;
	
	public ResourceInstaller(Coordination coordination, Resource resource, AriesSubsystem subsystem) {
		this.coordination = coordination;
		this.resource = resource;
		this.subsystem = subsystem;
		if (isDependency()) {
			if (Utils.isInstallableResource(resource))
				provisionTo = Utils.findFirstSubsystemAcceptingDependenciesStartingFrom(subsystem);
			else
				provisionTo = null;
		}
		else
			provisionTo = subsystem;
	}
	
	public abstract Resource install() throws Exception;
	
	protected void addConstituent(final Resource resource) {
		// Don't let a resource become a constituent of itself.
		if (provisionTo == null || resource.equals(provisionTo))
			return;
		Activator.getInstance().getSubsystems().addConstituent(provisionTo, resource, isReferencedProvisionTo());
		coordination.addParticipant(new Participant() {
			@Override
			public void ended(Coordination arg0) throws Exception {
				// Nothing
			}

			@Override
			public void failed(Coordination arg0) throws Exception {
				Activator.getInstance().getSubsystems().removeConstituent(provisionTo, resource);
			}
		});
	}
	
	protected void addReference(final Resource resource) {
		// Don't let a resource reference itself.
		if (resource.equals(subsystem))
			return;
		// The following check protects against resources posing as content
		// during a restart since the Deployed-Content header is currently used
		// to track all constituents for persistence purposes, which includes
		// resources that were provisioned to the subsystem as dependencies of
		// other resources.
		if (isReferencedSubsystem())
			Activator.getInstance().getSubsystems().addReference(subsystem, resource);
		coordination.addParticipant(new Participant() {
			@Override
			public void ended(Coordination arg0) throws Exception {
				// Nothing
			}

			@Override
			public void failed(Coordination arg0) throws Exception {
				Activator.getInstance().getSubsystems().removeReference(subsystem, resource);
			}
		});
	}
	
	protected String getLocation() {
		return provisionTo.getSubsystemId() + "@" + provisionTo.getSymbolicName() + "@" + ResourceHelper.getSymbolicNameAttribute(resource);
	}
	
	protected boolean isContent() {
		return Utils.isContent(subsystem, resource);
	}
	
	protected boolean isDependency() {
		return Utils.isDependency(subsystem, resource);
	}
	
	protected boolean isReferencedProvisionTo() {
		DeploymentManifest manifest = subsystem.getDeploymentManifest();
		if (manifest != null) {
			DeployedContentHeader header = manifest.getDeployedContentHeader();
			if (header != null && header.contains(resource))
				return subsystem.isReferenced(resource);
		}
		if (subsystem.equals(provisionTo))
			return isReferencedSubsystem();
		return false;
	}
	
	protected boolean isReferencedSubsystem() {
		DeploymentManifest manifest = subsystem.getDeploymentManifest();
		if (manifest != null) {
			DeployedContentHeader header = manifest.getDeployedContentHeader();
			if (header != null && header.contains(resource))
				return subsystem.isReferenced(resource);
		}
		return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1766.java