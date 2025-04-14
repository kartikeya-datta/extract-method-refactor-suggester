error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15528.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15528.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15528.java
text:
```scala
s@@uite.addTestSuite(WpValidationTest.class);

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
package org.eclipse.jst.ws.jaxws.dom.runtime.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.jst.ws.jaxws.dom.runtime.tests.annotations.AnnotationFactoryTest;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.annotations.AnnotationsTest;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.annotations.ArrayValueImplTest;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.annotations.ComplexAnnotationImplTest;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.annotations.ParamValuePairImplTest;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.annotations.QualifiedNameValueImplTest;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.WsDOMRuntimeManagerTest;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.WsDOMRuntimeRegistryTest;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.JaxWsDefaultsCalculatorTest;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.JaxWsWorkspaceResourceTests;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.Jee5WsDomRuntimeExtensionTest;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.ProjectAddingTests;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.WorkspaceCUFinderTest;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.annotation.AnnotationAdapterFactoryTest;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.annotation.impl.AnnotationAdapterTest;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.defaults.MethodPropertyDefaultsAdapterTest;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.defaults.ParameterPropertyDefaultsAdapterTest;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.defaults.SeiPropertyDefaultsAdapterTest;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.defaults.WsPropertyDefaultsAdapterTest;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.serializer.AbstractSerializerAdapterTest;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.serializer.MethodSerializerAdapterTest;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.serializer.ParameterSerializerAdapterTest;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.serializer.SeiSerializerAdapterTest;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.serializer.SerializerAdapterFactoryTest;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.serializer.WsSerializerAdapterTest;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.state.MethodPropertyStateAdapterTest;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.state.ParameterPropertyStateAdapterTest;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.state.PropertyStateAdapterFactoryTest;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.state.SeiPropertyStateAdapterTest;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.state.WsPropertyStateAdapterTest;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.sync.AbstractModelSynchronizerTest;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.sync.ImplicitSeiMethodSynchronizationTests;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.sync.MethodParamsSynchronizationTests;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.sync.ModelSynchronizationTests;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.sync.OnEventModelSyncronizerTest;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.sync.SeiMethodSyncronizationTest;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.sync.SeiSyncronizationTest;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.sync.WsSynchronizationTest;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.util.Dom2ResourceMapperTest;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.util.DomUtilTest;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.validation.EndpointIsSessionBeanRuleTest;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.validation.SeiValidationTest;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.validation.WmValidationTest;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.validation.WpValidationTest;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.validation.WsValidationTest;

public class AllTestsSuite
{
    public static Test suite()
    {
        TestSuite suite = new TestSuite();

        suite.addTestSuite(AnnotationFactoryTest.class);
        suite.addTestSuite(AnnotationsTest.class);
        suite.addTestSuite(ParamValuePairImplTest.class);
        suite.addTestSuite(ComplexAnnotationImplTest.class);
        suite.addTestSuite(QualifiedNameValueImplTest.class);
        suite.addTestSuite(ArrayValueImplTest.class);
        suite.addTestSuite(WsDOMRuntimeRegistryTest.class);
        suite.addTestSuite(WsDOMRuntimeManagerTest.class);
        suite.addTestSuite(Dom2ResourceMapperTest.class);
        suite.addTestSuite(DomUtilTest.class);

        // DOM tests
        suite.addTestSuite(Jee5WsDomRuntimeExtensionTest.class);
        suite.addTestSuite(JaxWsWorkspaceResourceTests.class);
        suite.addTestSuite(ProjectAddingTests.class);
        suite.addTestSuite(WorkspaceCUFinderTest.class);
        // DOM sync tests
        suite.addTestSuite(AbstractModelSynchronizerTest.class);
        suite.addTestSuite(OnEventModelSyncronizerTest.class);
        suite.addTestSuite(ImplicitSeiMethodSynchronizationTests.class);
        suite.addTestSuite(MethodParamsSynchronizationTests.class);
        suite.addTestSuite(ModelSynchronizationTests.class);
        suite.addTestSuite(SeiMethodSyncronizationTest.class);
        suite.addTestSuite(SeiSyncronizationTest.class);
        suite.addTestSuite(WsSynchronizationTest.class);
        suite.addTestSuite(JaxWsDefaultsCalculatorTest.class);
        // state adapters tests
        suite.addTestSuite(MethodPropertyStateAdapterTest.class);
        suite.addTestSuite(PropertyStateAdapterFactoryTest.class);
        suite.addTestSuite(SeiPropertyStateAdapterTest.class);
        suite.addTestSuite(WsPropertyStateAdapterTest.class);
        suite.addTestSuite(ParameterPropertyStateAdapterTest.class);
        // serialize adapter tests
        suite.addTestSuite(AbstractSerializerAdapterTest.class);
        suite.addTestSuite(MethodSerializerAdapterTest.class);
        suite.addTestSuite(SeiSerializerAdapterTest.class);
        suite.addTestSuite(SerializerAdapterFactoryTest.class);
        suite.addTestSuite(WsSerializerAdapterTest.class);
        suite.addTestSuite(ParameterSerializerAdapterTest.class);
        // default values adapter tests
        suite.addTestSuite(MethodPropertyDefaultsAdapterTest.class);
        suite.addTestSuite(WsPropertyDefaultsAdapterTest.class);
        suite.addTestSuite(SeiPropertyDefaultsAdapterTest.class);
        suite.addTestSuite(ParameterPropertyDefaultsAdapterTest.class);
        // other adapters tests
        suite.addTestSuite(AnnotationAdapterTest.class);
        suite.addTestSuite(AnnotationAdapterFactoryTest.class);
        // validation tests
        suite.addTestSuite(SeiValidationTest.class);
        suite.addTestSuite(WsValidationTest.class);
        suite.addTestSuite(WmValidationTest.class);
        //	suite.addTestSuite(WpValidationTest.class);
        suite.addTestSuite(EndpointIsSessionBeanRuleTest.class);

        return suite;
    }
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15528.java