error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1765.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1765.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1765.java
text:
```scala
b@@undle = provisionTo.getRegion().installBundleAtLocation(getLocation(), is);

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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.aries.util.io.IOUtils;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.Version;
import org.osgi.framework.startlevel.BundleStartLevel;
import org.osgi.framework.wiring.BundleCapability;
import org.osgi.framework.wiring.BundleRequirement;
import org.osgi.framework.wiring.BundleRevision;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.namespace.service.ServiceNamespace;
import org.osgi.resource.Capability;
import org.osgi.resource.Requirement;
import org.osgi.resource.Resource;
import org.osgi.service.coordinator.Coordination;
import org.osgi.service.coordinator.Participant;
import org.osgi.service.repository.RepositoryContent;
import org.osgi.service.subsystem.SubsystemException;

public class BundleResourceInstaller extends ResourceInstaller {
	/*
	 * Maps a BundleResource to a BundleRevision for the purpose of tracking
	 * any service requirements or capabilities. The instance is given to the
	 * Subsystems data structure as the constituent object.
	 * 
	 * The resource variable is allowed to be null so this class can be used
	 * when removing constituents from the data structure; however, note that
	 * service capabilities and requirements will not be available.
	 */
	static class BundleConstituent implements BundleRevision {
		private final Resource resource;
		private final BundleRevision revision;
		
		public BundleConstituent(Resource resource, BundleRevision revision) {
			this.resource = resource;
			this.revision = revision;
		}

		@Override
		public List<Capability> getCapabilities(String namespace) {
			List<Capability> result = new ArrayList<Capability>(revision.getCapabilities(namespace));
			if (resource != null && (namespace == null || ServiceNamespace.SERVICE_NAMESPACE.equals(namespace)))
				for (Capability capability : resource.getCapabilities(ServiceNamespace.SERVICE_NAMESPACE))
					result.add(new BasicCapability.Builder()
								.namespace(capability.getNamespace())
								.attributes(capability.getAttributes())
								.directives(capability.getDirectives())
								// Use the BundleRevision as the resource so it can be identified as a
								// runtime resource within the system repository.
								.resource(revision)
								.build());
			return Collections.unmodifiableList(result);
		}

		@Override
		public List<Requirement> getRequirements(String namespace) {
			List<Requirement> result = new ArrayList<Requirement>(revision.getRequirements(namespace));
			if (resource != null && (namespace == null || ServiceNamespace.SERVICE_NAMESPACE.equals(namespace)))
				for (Requirement requiremnet : resource.getRequirements(ServiceNamespace.SERVICE_NAMESPACE))
					result.add(new BasicRequirement.Builder()
								.namespace(requiremnet.getNamespace())
								.attributes(requiremnet.getAttributes())
								.directives(requiremnet.getDirectives())
								// Use the BundleRevision as the resource so it can be identified as a
								// runtime resource within the system repository.
								.resource(revision)
								.build());
			return Collections.unmodifiableList(result);
		}
		
		@Override
		public boolean equals(Object o) {
			if (o == this)
				return true;
			if (!(o instanceof BundleConstituent))
				return false;
			BundleConstituent that = (BundleConstituent)o;
			return revision.equals(that.revision);
		}
		
		@Override
		public int hashCode() {
			int result = 17;
			result = 31 * result + revision.hashCode();
			return result;
		}

		@Override
		public Bundle getBundle() {
			return revision.getBundle();
		}

		@Override
		public String getSymbolicName() {
			return revision.getSymbolicName();
		}

		@Override
		public Version getVersion() {
			return revision.getVersion();
		}

		@Override
		public List<BundleCapability> getDeclaredCapabilities(String namespace) {
			return revision.getDeclaredCapabilities(namespace);
		}

		@Override
		public List<BundleRequirement> getDeclaredRequirements(String namespace) {
			return revision.getDeclaredRequirements(namespace);
		}

		@Override
		public int getTypes() {
			return revision.getTypes();
		}

		@Override
		public BundleWiring getWiring() {
			return revision.getWiring();
		}
		
		@Override
		public String toString() {
			return revision.toString();
		}
	}
	
	public BundleResourceInstaller(Coordination coordination, Resource resource, AriesSubsystem subsystem) {
		super(coordination, resource, subsystem);
	}
	
	public Resource install() {
		BundleRevision revision;
		if (resource instanceof BundleRevision)
			revision = (BundleRevision)resource;
		else {
			ThreadLocalSubsystem.set(provisionTo);
			revision = installBundle();
		}
		addReference(revision);
		addConstituent(new BundleConstituent(resource, revision));
		return revision;
	}
	
	private BundleRevision installBundle() {
		final Bundle bundle;
		InputStream is = ((RepositoryContent)resource).getContent();
		try {
			bundle = provisionTo.getRegion().installBundle(getLocation(), is);
		}
		catch (BundleException e) {
			throw new SubsystemException(e);
		}
		finally {
			// Although Region.installBundle ultimately calls BundleContext.install,
			// which closes the input stream, an exception may occur before this
			// happens. Also, the Region API does not guarantee the stream will
			// be closed.
			IOUtils.close(is);
		}
		coordination.addParticipant(new Participant() {
			public void ended(Coordination coordination) throws Exception {
				// Nothing
			}

			public void failed(Coordination coordination) throws Exception {
				provisionTo.getRegion().removeBundle(bundle);
			}
		});
		// Set the start level of all bundles managed (i.e. installed) by the
		// subsystems implementation to 1 in case the framework's default bundle
		// start level has been changed. Otherwise, start failures will occur
		// if a subsystem is started at a lower start level than the default.
		// Setting the start level does no harm since all managed bundles are 
		// started transiently anyway.
		bundle.adapt(BundleStartLevel.class).setStartLevel(1);
		return bundle.adapt(BundleRevision.class);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1765.java