error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7078.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7078.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7078.java
text:
```scala
public v@@oid test_ContructorIsNotIncludedInWebMethods() throws CoreException

/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.sync;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingParameterStyle;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingStyle;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingUse;
import org.eclipse.jst.ws.jaxws.testutils.jobs.JobUtils;
import org.eclipse.jst.ws.jaxws.utils.JaxWsUtils;

public class WsSynchronizationTest extends SynchronizationTestFixture 
{
	public void test_changedWSnameSynched() throws JavaModelException, CoreException
	{
		target.startSynchronizing();
		final ICompilationUnit ws1CU = testPrj1.getJavaProject().findType(ws1.getImplementation()).getCompilationUnit();
		testUtil.setContents(ws1CU, "@javax.jws.WebService(serviceName=\"WS1NewName\", endpointInterface=\"com.sap.test.modelsync1.Sei1\") public class WS1 {}");
		JobUtils.waitForJobs();
		
		assertEquals("WS1NewName", ws1.getName());
		assertSame(sei1, ws1.getServiceEndpoint());
		assertSame(ws1, domUtil.findWsByImplName(wsPrj1, ws1.getImplementation()));
	}	
	
	public void test_changedNameOfImplicitInterface() throws Exception
	{
		target.startSynchronizing();
		final IPackageFragment modelSync3 = testPrj1.createPackage("com.sap.test.modelsync3");
		final ICompilationUnit ws3CU = testPrj1.createType(modelSync3, "WS3.java", "@javax.jws.WebService(serviceName=\"WS3Name\", name=\"ImplicitInterfaceName\") public class WS3 {}").getCompilationUnit();
		JobUtils.waitForJobs();
		
		final IWebService ws3 = domUtil.findWsByImplName(wsPrj1, ws3CU.findPrimaryType().getFullyQualifiedName());
		final IServiceEndpointInterface implSei = ws3.getServiceEndpoint();
		assertTrue(implSei.isImplicit());
		testUtil.setContents(ws3CU, "@javax.jws.WebService(serviceName=\"WS3Name\", name=\"ImplicitInterfaceNewName\") public class WS3 {}");
		JobUtils.waitForJobs();
		
		assertSame(implSei, ws3.getServiceEndpoint());
		assertTrue(implSei.isImplicit());
		assertEquals("ImplicitInterfaceNewName", implSei.getName());
	}
	
	public void test_changedTargetNamespaceSynched() throws Exception
	{
		target.startSynchronizing();
		assertEquals(JaxWsUtils.composeJaxWsTargetNamespaceByPackage(prj1PckName), ws1.getTargetNamespace());
		
		final ICompilationUnit wsCU = testPrj1.getJavaProject().findType(ws1.getImplementation()).getCompilationUnit();
		testUtil.setContents(wsCU, "@javax.jws.WebService(serviceName=\"WS1Name\", targetNamespace=\"http://com.sap./test\") public class WS1 {}");
		JobUtils.waitForJobs();
		
		assertEquals("targetNamespace not synchronized", "http://com.sap./test", ws1.getTargetNamespace());
	}
	
	public void test_changedPortNameSynched() throws Exception
	{
		target.startSynchronizing();
		final ICompilationUnit wsCU = testPrj1.getJavaProject().findType(ws1.getImplementation()).getCompilationUnit();
		assertEquals("WS1Port", ws1.getPortName());
		
		testUtil.setContents(wsCU, "@javax.jws.WebService(portName=\"ChangedPortName\") public class WS1 {}");;
		JobUtils.waitForJobs();
		
		assertEquals("portName not synchronized", "ChangedPortName", ws1.getPortName());
	}
	
	public void test_changedWsdlLocationSynched() throws Exception
	{
		target.startSynchronizing();
		final ICompilationUnit wsCU = testPrj1.getJavaProject().findType(ws1.getImplementation()).getCompilationUnit();
		assertEquals(null, ws1.getWsdlLocation());
		
		testUtil.setContents(wsCU, "@javax.jws.WebService(wsdlLocation=\"C:/changed/wsdl/location\") public class WS1 {}");;
		JobUtils.waitForJobs();
		
		assertEquals("wsdlLocation not synchronized", "C:/changed/wsdl/location", ws1.getWsdlLocation());
	}	
	
	public void test_changedImplicitSeiSoapBinding() throws Exception
	{
		target.startSynchronizing();
		final ICompilationUnit wsCU = testPrj1.getJavaProject().findType(ws1.getImplementation()).getCompilationUnit();
		
		testUtil.setContents(wsCU, "@javax.jws.WebService() public class WS1 {}");
		JobUtils.waitForJobs();
		
		final IServiceEndpointInterface sei = ws1.getServiceEndpoint();
		assertEquals(SOAPBindingStyle.DOCUMENT, sei.getSoapBindingStyle());
		assertEquals(SOAPBindingUse.LITERAL, sei.getSoapBindingUse());
		assertEquals(SOAPBindingParameterStyle.WRAPPED, sei.getSoapBindingParameterStyle());

		testUtil.setContents(wsCU, "@javax.jws.soap.SOAPBinding(style=Style.RPC, use=Use.ENCODED, parameterStyle=ParameterStyle.BARE)\n" + 
				"@javax.jws.WebService() public class WS1 {}");
		JobUtils.waitForJobs();
		
		assertEquals(SOAPBindingStyle.RPC, sei.getSoapBindingStyle());
		assertEquals(SOAPBindingUse.ENCODED, sei.getSoapBindingUse());
		assertEquals(SOAPBindingParameterStyle.BARE, sei.getSoapBindingParameterStyle());	
	}
	
	public void test_ContructorIsNotIncludedInWebMethods() throws JavaModelException
	{
		target.startSynchronizing();
		final ICompilationUnit wsCU = testPrj1.getJavaProject().findType(ws1.getImplementation()).getCompilationUnit();
		
		testUtil.setContents(wsCU, "@javax.jws.WebService()public class WS1{public WS1(){};public WS1(String s){}public void myMethod(){}}");
		JobUtils.waitForJobs();
		
		final IServiceEndpointInterface sei = ws1.getServiceEndpoint();
		final EList<IWebMethod> methods = sei.getWebMethods();
		assertEquals("One method expected in the SEI", 1, methods.size());
		assertEquals("Unpexpected method name in SEI", "myMethod", methods.iterator().next().getName());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7078.java