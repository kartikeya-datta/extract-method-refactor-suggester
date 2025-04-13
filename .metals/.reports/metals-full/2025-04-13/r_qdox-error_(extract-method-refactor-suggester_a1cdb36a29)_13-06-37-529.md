error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15562.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15562.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,24]

error in qdox parser
file content:
```java
offset: 24
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15562.java
text:
```scala
protected static final S@@tring BASE_CONTAINER_TYPE_NAME = "ecf.base";

/****************************************************************************
* Copyright (c) 2004 Composent, Inc. and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    Composent, Inc. - initial API and implementation
*****************************************************************************/

package org.eclipse.ecf.tests.core;

import org.eclipse.ecf.core.AbstractContainer;
import org.eclipse.ecf.core.ContainerConnectException;
import org.eclipse.ecf.core.ContainerCreateException;
import org.eclipse.ecf.core.ContainerFactory;
import org.eclipse.ecf.core.ContainerTypeDescription;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.identity.Namespace;
import org.eclipse.ecf.core.provider.IContainerInstantiator;
import org.eclipse.ecf.core.security.IConnectContext;

public class ContainerFactoryServiceCreateTest extends ContainerFactoryServiceAbstractTestCase {

	protected static final String CONTAINER_TYPE_NAME = ContainerFactoryServiceCreateTest.class.getName();

	protected static final String BASE_CONTAINER_TYPE_NAME = "ecf.root";

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.tests.core.ContainerFactoryAbstractTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		getFixture().addDescription(createContainerTypeDescription());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.tests.core.ContainerFactoryAbstractTestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		getFixture().removeDescription(createContainerTypeDescription());
		super.tearDown();
	}

	protected ContainerTypeDescription createContainerTypeDescription() {
		return new ContainerTypeDescription(CONTAINER_TYPE_NAME, new IContainerInstantiator() {
			public IContainer createInstance(ContainerTypeDescription description, Object[] parameters) throws ContainerCreateException {
				return new AbstractContainer() {
					public void connect(ID targetID, IConnectContext connectContext) throws ContainerConnectException {
					}

					public void disconnect() {
					}

					public Namespace getConnectNamespace() {
						return null;
					}

					public ID getConnectedID() {
						return null;
					}

					public ID getID() {
						return null;
					}

				};
			}

			public String[] getSupportedAdapterTypes(ContainerTypeDescription description) {
				return new String[] {"one"};
			}

			public Class[][] getSupportedParameterTypes(ContainerTypeDescription description) {
				return new Class[][] {{String.class, Class.class}};
			}
		}, DESCRIPTION);
	}

	public void testCreateContainer0() throws Exception {
		final IContainer container = ContainerFactory.getDefault().createContainer();
		assertNotNull(container);
	}

	public void testCreateContainer1() throws Exception {
		final IContainer container = ContainerFactory.getDefault().createContainer(CONTAINER_TYPE_NAME);
		assertNotNull(container);
	}

	public void testCreateContainer2() throws Exception {
		final IContainer container = ContainerFactory.getDefault().createContainer(CONTAINER_TYPE_NAME, null);
		assertNotNull(container);
	}

	public void testCreateContainer3() throws Exception {
		final ContainerTypeDescription desc = ContainerFactory.getDefault().getDescriptionByName(CONTAINER_TYPE_NAME);
		assertNotNull(desc);
		final IContainer container = ContainerFactory.getDefault().createContainer(desc, null);
		assertNotNull(container);
	}

	public void testCreateContainer4() throws Exception {
		try {
			ContainerFactory.getDefault().createContainer((String) null, null);
			fail();
		} catch (final ContainerCreateException e) {
		}
	}

	public void testCreateContainer5() throws Exception {
		try {
			ContainerFactory.getDefault().createContainer((ContainerTypeDescription) null, null);
			fail();
		} catch (final ContainerCreateException e) {
		}
	}

	public void testCreateBaseContainer() throws Exception {
		final IContainer base = ContainerFactory.getDefault().createContainer();
		assertNotNull(base);
	}

	public void testCreateBaseContainer0() throws Exception {
		final ContainerTypeDescription desc = ContainerFactory.getDefault().getDescriptionByName(BASE_CONTAINER_TYPE_NAME);
		assertNotNull(desc);
		final IContainer base = ContainerFactory.getDefault().createContainer(desc, new Object[] {IDFactory.getDefault().createGUID()});
		assertNotNull(base);
	}

	public void testCreateBaseContainer1() throws Exception {
		final ContainerTypeDescription desc = ContainerFactory.getDefault().getDescriptionByName(BASE_CONTAINER_TYPE_NAME);
		assertNotNull(desc);
		final IContainer base = ContainerFactory.getDefault().createContainer(desc, new Object[] {IDFactory.getDefault().createGUID().getName()});
		assertNotNull(base);
	}

	public void testContainerTypeDescriptionGetName() {
		final ContainerTypeDescription desc = ContainerFactory.getDefault().getDescriptionByName(CONTAINER_TYPE_NAME);
		assertTrue(desc.getName().equals(CONTAINER_TYPE_NAME));
	}

	public void testContainerTypeDescriptionGetDescription() {
		final ContainerTypeDescription desc = ContainerFactory.getDefault().getDescriptionByName(CONTAINER_TYPE_NAME);
		assertTrue(desc.getDescription().equals(DESCRIPTION));
	}

	public void testContainerTypeDescriptionGetSupportedAdapterTypes() {
		final ContainerTypeDescription desc = ContainerFactory.getDefault().getDescriptionByName(CONTAINER_TYPE_NAME);
		final String[] adapterTypes = desc.getSupportedAdapterTypes();
		assertTrue(adapterTypes.length == 1);
		assertTrue(adapterTypes[0] == "one");
	}

	public void testContainerTypeDescriptionGetSupportedParemeterTypes() {
		final ContainerTypeDescription desc = ContainerFactory.getDefault().getDescriptionByName(CONTAINER_TYPE_NAME);
		final Class[][] parameterTypes = desc.getSupportedParameterTypes();
		assertTrue(parameterTypes.length == 1);
		assertTrue(parameterTypes[0].length == 2);
		assertTrue(parameterTypes[0][0].equals(String.class));
		assertTrue(parameterTypes[0][1].equals(Class.class));

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15562.java