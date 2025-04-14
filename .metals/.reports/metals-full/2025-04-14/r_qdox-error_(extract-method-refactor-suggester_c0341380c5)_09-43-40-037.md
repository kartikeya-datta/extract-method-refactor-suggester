error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/169.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/169.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/169.java
text:
```scala
R@@equirement requirement = new OsgiIdentityRequirement(content.getName(), content.getVersionRange(), content.getType(), false);

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
package org.apache.aries.subsystem.core.archive;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.aries.subsystem.core.internal.Activator;
import org.apache.aries.subsystem.core.internal.OsgiIdentityRequirement;
import org.apache.aries.subsystem.core.obr.SubsystemEnvironment;
import org.osgi.framework.wiring.Requirement;
import org.osgi.framework.wiring.Resource;
import org.osgi.framework.wiring.Wire;

public class DeploymentManifest extends Manifest {
	public static DeploymentManifest newInstance(SubsystemManifest manifest, SubsystemEnvironment environment) {
		DeploymentManifest result = new DeploymentManifest();
		result.headers.put(ManifestVersionHeader.NAME, manifest.getManifestVersion());
		Collection<Requirement> requirements = new ArrayList<Requirement>();
		for (SubsystemContentHeader.Content content : manifest.getSubsystemContent().getContents()) {
			Requirement requirement = OsgiIdentityRequirement.newInstance(content);
			requirements.add(requirement);
		}
		// TODO This does not validate that all content bundles were found.
		Map<Resource, List<Wire>> resolution = Activator.getResolver().resolve(environment, requirements.toArray(new Requirement[requirements.size()]));
		// TODO Once we have a resolver that actually returns lists of wires, we can use them to compute other manifest headers such as Import-Package.
		Collection<Resource> deployedContent = new ArrayList<Resource>();
		Collection<Resource> provisionResource = new ArrayList<Resource>();
		for (Resource resource : resolution.keySet()) {
			if (environment.isContentResource(resource))
				deployedContent.add(resource);
			else
				provisionResource.add(resource);
		}
		result.headers.put(DeployedContentHeader.NAME, DeployedContentHeader.newInstance(deployedContent));
		if (!provisionResource.isEmpty())
			result.headers.put(ProvisionResourceHeader.NAME, ProvisionResourceHeader.newInstance(provisionResource));
		result.headers.put(SubsystemSymbolicNameHeader.NAME, manifest.getSubsystemSymbolicName());
		result.headers.put(SubsystemVersionHeader.NAME, manifest.getSubsystemVersion());
		SubsystemTypeHeader typeHeader = manifest.getSubsystemType();
		result.headers.put(SubsystemTypeHeader.NAME, typeHeader);
		// TODO Add to constants.
		if ("osgi.application".equals(typeHeader.getValue())) {
			// TODO Compute additional headers for an application.
		}
		// TODO Add to constants.
		else if ("osgi.composite".equals(typeHeader.getValue())) {
			// TODO Compute additional headers for a composite. 
		}
		// Features require no additional headers.
		return result;
	}
	
	public DeploymentManifest(File manifestFile) throws IOException {
		super(manifestFile);
	}
	
	public DeploymentManifest(InputStream in) throws IOException {
		super(in);
	}
	
	private DeploymentManifest() {}
	
	public DeploymentManifest(Collection<Resource> deployedContent, Collection<Resource> provisionResource) {
		headers.put(ManifestVersionHeader.NAME, new ManifestVersionHeader("1.0"));
		headers.put(DeployedContentHeader.NAME, DeployedContentHeader.newInstance(deployedContent));
		if (!provisionResource.isEmpty())
			headers.put(ProvisionResourceHeader.NAME, ProvisionResourceHeader.newInstance(provisionResource));
	}
	
	public DeployedContentHeader getDeployedContent() {
		return (DeployedContentHeader)getHeader(DeployedContentHeader.NAME);
	}
	
	public ProvisionResourceHeader getProvisionResource() {
		return (ProvisionResourceHeader)getHeader(ProvisionResourceHeader.NAME);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/169.java