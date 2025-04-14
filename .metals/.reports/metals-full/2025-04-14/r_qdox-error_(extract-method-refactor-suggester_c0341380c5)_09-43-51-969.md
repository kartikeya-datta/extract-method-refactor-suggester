error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18251.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18251.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18251.java
text:
```scala
t@@ypeManager.registerType( ServiceFactory.ROLE, serviceRoleName, typeFactory );

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included  with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.myrmidon.components.service;

import org.apache.avalon.excalibur.i18n.ResourceManager;
import org.apache.avalon.excalibur.i18n.Resources;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.myrmidon.components.AbstractComponentTest;
import org.apache.myrmidon.interfaces.role.RoleInfo;
import org.apache.myrmidon.interfaces.role.RoleManager;
import org.apache.myrmidon.interfaces.service.ServiceFactory;
import org.apache.myrmidon.interfaces.type.DefaultTypeFactory;
import org.apache.myrmidon.interfaces.type.TypeManager;

/**
 * Test cases for the default service manager.
 *
 * @author <a href="mailto:adammurdoch@apache.org">Adam Murdoch</a>
 * @version $Revision$ $Date$
 */
public class InstantiatingServiceManagerTest
    extends AbstractComponentTest
{
    private final static Resources REZ
        = ResourceManager.getPackageResources( InstantiatingServiceManagerTest.class );

    private InstantiatingServiceManager m_serviceManager;
    private Parameters m_parameters = new Parameters();

    public InstantiatingServiceManagerTest( final String name )
    {
        super( name );
    }

    /**
     * Setup the test case - prepares the set of components.
     */
    protected void setUp()
        throws Exception
    {
        // Set-up the service manager
        m_serviceManager = new InstantiatingServiceManager();
        m_serviceManager.enableLogging( getLogger() );
        m_serviceManager.service( getServiceManager() );
        m_serviceManager.parameterize( m_parameters );
    }

    /**
     * Tests service instantiation.
     */
    public void testCreateService() throws Exception
    {
        final String serviceRoleName = "test-service";

        // Setup the test service
        registerFactory( serviceRoleName, TestService.class, TestServiceFactory1.class );

        // Create the service
        Object service = m_serviceManager.lookup( serviceRoleName );

        // Check service is of the expected class (don't use instanceof)
        assertTrue( service.getClass() == TestServiceImpl1.class );
    }

    /**
     * Tests service lookup.
     */
    public void testLookup() throws Exception
    {
        final String serviceRoleName = "test-service";

        // Setup the test service
        registerFactory( serviceRoleName, TestService.class, TestServiceFactory1.class );

        // Check whether the service can be instantiated
        boolean hasService = m_serviceManager.hasService( serviceRoleName );
        assertTrue( hasService );
    }

    /**
     * Tests that a service factory and service instance are taken through
     * the lifecycle steps.
     */
    public void testLifecycle() throws Exception
    {
        final String serviceRoleName = "test-service";

        // Setup the test service
        registerFactory( serviceRoleName, TestService.class, TestServiceFactory2.class );

        // Create the service
        TestService service = (TestService)m_serviceManager.lookup( serviceRoleName );

        // Check service is of the expected class (don't use instanceof)
        assertTrue( service.getClass() == TestServiceImpl2.class );

        // Assert the service has been setup correctly
        service.doWork();
    }

    /**
     * Tests looking up an unknown service.
     */
    public void testUnknownService() throws Exception
    {
        // Make sure that hasService() works correctly
        final String serviceRole = "some-unknown-service";
        assertTrue( !m_serviceManager.hasService( serviceRole ) );

        // Make sure that lookup() fails
        try
        {
            m_serviceManager.lookup( serviceRole );
            fail();
        }
        catch( ServiceException e )
        {
            final String message = REZ.getString( "create-service.error", serviceRole );
            assertSameMessage( message, e );
        }
    }

    /**
     * Registers a service factory.
     */
    private void registerFactory( final String serviceRoleName,
                                  final Class serviceType,
                                  final Class factoryClass )
        throws Exception
    {
        // TODO - add stuff to TypeDeployer to do this instead
        final RoleManager roleManager = (RoleManager)getServiceManager().lookup( RoleManager.ROLE );
        roleManager.addRole( new RoleInfo( serviceRoleName, null, serviceType ) );
        final DefaultTypeFactory typeFactory = new DefaultTypeFactory( getClass().getClassLoader() );
        typeFactory.addNameClassMapping( serviceRoleName, factoryClass.getName() );
        final TypeManager typeManager = (TypeManager)getServiceManager().lookup( TypeManager.ROLE );
        typeManager.registerType( ServiceFactory.class, serviceRoleName, typeFactory );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18251.java