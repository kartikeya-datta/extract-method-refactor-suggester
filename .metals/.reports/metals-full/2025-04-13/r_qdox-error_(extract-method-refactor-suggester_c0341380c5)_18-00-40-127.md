error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/407.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/407.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/407.java
text:
```scala
i@@f (o instanceof Collection<?>)

/*******************************************************************************
 * Copyright (c) 2005 - 2009 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/

package org.eclipse.xtend.typesystem.xsd.tests.reloadschemas;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;
import org.eclipse.emf.mwe.core.monitor.NullProgressMonitor;
import org.eclipse.xsd.XSDConcreteComponent;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDSchemaDirective;
import org.eclipse.xtend.typesystem.emf.EcoreUtil2;
import org.eclipse.xtend.typesystem.xsd.builder.OawXSDResource;
import org.eclipse.xtend.typesystem.xsd.builder.OawXSDResourceSet;
import org.eclipse.xtend.typesystem.xsd.builder.XSDManager;
import org.eclipse.xtend.typesystem.xsd.tests.AbstractTestCase;
import org.eclipse.xtend.typesystem.xsd.util.Msg;

/**
 * @author Moritz Eysholdt - Initial contribution
 */
public class ReloadSchemasTest extends AbstractTestCase {

	private final URI Ns1_A = uri("/Ns1-A.xsd");
	private final URI Ns1_B = uri("/Ns1-B.xsd");
	private final URI Ns1_C_AB = uri("/Ns1-C-AB.xsd");
	private final URI Ns2_K_C = uri("/Ns2-K-C.xsd");
	private final URI Ns2_L_K = uri("/Ns2-L-K.xsd");
	private final URI Ns3_M_C = uri("/Ns3-M-C.xsd");

	private void assertNoReferencesToPackage(XSDManager rs, EPackage p) {
		HashSet<EPackage> allowedPackages = new HashSet<EPackage>(rs
				.getPackages());
		assertFalse(allowedPackages.contains(p));
		allowedPackages.add(XMLTypePackage.eINSTANCE);
		for (EPackage s : rs.getPackages()) {
			assertNoReferencesToPackage(allowedPackages, s);
			TreeIterator<EObject> ti = s.eAllContents();
			while (ti.hasNext())
				assertNoReferencesToPackage(allowedPackages, ti.next());
		}
	}

	private void assertNoReferencesToPackage(Set<EPackage> allowedPackages,
			EObject obj) {
		for (EReference ref : obj.eClass().getEAllReferences()) {
			Object o = obj.eGet(ref);
			if (obj instanceof EPackage
					&& !allowedPackages.contains(obj))
				fail2(Msg.create("Supplied object is a forbidden package:")
						.pkg((EPackage) obj));
			/*
			 * if (o instanceof EObject) { EObject eo = (EObject) o; if
			 * (eo.eResource() == null || eo.eResource().getURI() == null)
			 * fail2(Msg.create("").uri(obj).txt(": Resource is null:")
			 * .path(obj).txt("->").txt(ref.getName())); }
			 */
			if (o instanceof EClassifier) {
				EClassifier cc = (EClassifier) o;
				if (!allowedPackages.contains(cc.getEPackage()))
					fail2(Msg.create("Reference to ").pkg(cc.getEPackage())
							.txt(" found:").path(obj).txt("->").txt(
									ref.getName()));
			}
		}
	}

	private void assertNoReferencesToSchema(EObject obj, EReference ref,
			Object o, XSDSchema schema) {
		if (o instanceof EObject) {
			EObject eo = (EObject) o;
			if (eo.eResource() == null || eo.eResource().getURI() == null)
				fail2(Msg.create("").uri(obj).txt(": Resource is null:").path(
						obj).txt("->").txt(ref.getName()));
		}
		if (o instanceof XSDConcreteComponent) {
			XSDConcreteComponent cc = (XSDConcreteComponent) o;
			if (cc.getSchema() == schema)
				fail2(Msg.create("").uri(obj).txt(": Reference to ").schema(
						schema).txt(" found:").path(obj).txt("->").txt(
						ref.getName()));
		}
	}

	private void assertNoReferencesToSchema(OawXSDResourceSet rs,
			XSDSchema schema) {
		for (XSDSchema s : rs.getSchemas()) {
			assertNoReferencesToSchemaFromFeature(s, schema);
			TreeIterator<EObject> ti = s.eAllContents();
			for (XSDSchemaDirective ref : s.getReferencingDirectives())
				assertNoReferencesToSchemaFromFeature(ref, schema);
			while (ti.hasNext())
				assertNoReferencesToSchemaFromFeature(ti.next(), schema);
		}
	}

	private void assertNoReferencesToSchemaFromFeature(EObject obj,
			XSDSchema schema) {
		if (obj == schema)
			fail2(Msg.create("Supplied object is forbidden schema"));
		for (EReference ref : obj.eClass().getEAllReferences()) {
			Object o = obj.eGet(ref);
			if (o instanceof Collection)
				for (Object p : (Collection<?>) o)
					assertNoReferencesToSchema(obj, ref, p, schema);
			else
				assertNoReferencesToSchema(obj, ref, o, schema);
		}
	}

	private void assertSchemaLoaded(OawXSDResourceSet rs, URI uri,
			boolean expectEcore) {
		OawXSDResource r = rs.getXsdResource(uri, false);
		assertNotNull(r);
		assertTrue(r.isLoaded());
		assertEquals(expectEcore, r.isEcorePackageGenerated());
	}

	private void fail2(Msg msg) {
		System.out.println("Fail: " + msg);
	}

	private void reloadPackage(URI uri) {
		OawXSDResourceSet rs = new OawXSDResourceSet();
		rs.markDirty(Ns2_L_K);
		rs.reloadDirty(new NullProgressMonitor());
		OawXSDResource r = rs.getXsdResource(uri, false);
		EPackage p = r.getEPackage();
		XSDSchema s = r.getSchema();
		rs.markDirty(uri);
		rs.reloadDirty(new NullProgressMonitor());
		assertSchemaLoaded(rs, uri, true);
		assertNoReferencesToPackage(rs, p);
		assertNoReferencesToSchema(rs, s);
	}

	public void testLoadAll() {
		OawXSDResourceSet rs = new OawXSDResourceSet();
		rs.markDirty(Ns2_L_K);
		rs.markDirty(Ns1_C_AB);
		rs.reloadDirty(new NullProgressMonitor());
		assertSchemaLoaded(rs, Ns1_A, false);
		assertSchemaLoaded(rs, Ns1_B, false);
		assertSchemaLoaded(rs, Ns1_C_AB, true);
		assertSchemaLoaded(rs, Ns2_K_C, false);
		assertSchemaLoaded(rs, Ns2_L_K, true);
	}

	public void testReloadPackageA() {
		reloadPackage(Ns1_A);
	}

	public void testReloadPackageB() {
		reloadPackage(Ns1_B);
	}

	public void testReloadPackageCAB() {
		reloadPackage(Ns1_C_AB);
	}

	public void testReloadPackageKC() {
		reloadPackage(Ns2_K_C);
	}

	public void testReloadPackageLK() {
		reloadPackage(Ns2_L_K);
	}

	public void testRemove() {
		OawXSDResourceSet rs = new OawXSDResourceSet();
		rs.loadAndGenerate(Ns2_L_K);
		OawXSDResource r = rs.getXsdResource(Ns2_L_K, false);
		XSDSchema s = r.getSchema();
		EPackage p = r.getEPackage();
		rs.remove(Ns2_L_K);
		assertNoReferencesToSchema(rs, s);
		assertNoReferencesToPackage(rs, p);
		assertNull(rs.getXsdResource(Ns2_L_K, false));
	}

	public void testUnloadRootPackage() {
		OawXSDResourceSet rs = new OawXSDResourceSet();
		rs.markDirty(Ns2_L_K);
		rs.reloadDirty(new NullProgressMonitor());
		OawXSDResource r = rs.getXsdResource(Ns2_L_K, false);
		EPackage p = r.getEPackage();
		r.unload();
		assertNoReferencesToPackage(rs, p);
	}

	public void testUnloadRootSchema() {
		OawXSDResourceSet rs = new OawXSDResourceSet();
		rs.markDirty(Ns2_L_K);
		rs.reloadDirty(new NullProgressMonitor());
		OawXSDResource r = rs.getXsdResource(Ns2_L_K, false);
		XSDSchema s = r.getSchema();
		r.unload();
		assertNoReferencesToSchema(rs, s);
	}

	private URI uri(String file) {
		return EcoreUtil2.getURI(getSrcDir() + file);
	}

	public void testLoadIncludes() {
		// usually included schemas are not loaded if there is no element that
		// references them. bit the OawXSDResourceSet is supposed to load them
		// anyway.
		OawXSDResourceSet rs = new OawXSDResourceSet();
		rs.markDirty(Ns3_M_C);
		rs.reloadDirty(new NullProgressMonitor());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/407.java