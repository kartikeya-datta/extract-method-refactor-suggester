error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17422.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17422.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17422.java
text:
```scala
private static final R@@esources REZ =

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.myrmidon.components.deployer;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.apache.aut.converter.Converter;
import org.apache.avalon.excalibur.i18n.ResourceManager;
import org.apache.avalon.excalibur.i18n.Resources;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.myrmidon.interfaces.classloader.ClassLoaderManager;
import org.apache.myrmidon.interfaces.converter.ConverterRegistry;
import org.apache.myrmidon.interfaces.deployer.ConverterDefinition;
import org.apache.myrmidon.interfaces.deployer.Deployer;
import org.apache.myrmidon.interfaces.deployer.DeploymentException;
import org.apache.myrmidon.interfaces.deployer.TypeDefinition;
import org.apache.myrmidon.interfaces.deployer.TypeDeployer;
import org.apache.myrmidon.interfaces.role.RoleInfo;
import org.apache.myrmidon.interfaces.role.RoleManager;
import org.apache.myrmidon.interfaces.service.ServiceFactory;
import org.apache.myrmidon.interfaces.type.DefaultTypeFactory;
import org.apache.myrmidon.interfaces.type.TypeManager;

/**
 * This class deploys roles, types and services from a typelib.
 *
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 * @author <a href="mailto:adammurdoch@apache.org">Adam Murdoch</a>
 * @version $Revision$ $Date$
 */
public class DefaultDeployer
    extends AbstractLogEnabled
    implements Deployer, Serviceable
{
    private final static Resources REZ =
        ResourceManager.getPackageResources( DefaultDeployer.class );

    // The components used to deploy
    private ConverterRegistry m_converterRegistry;
    private TypeManager m_typeManager;
    private RoleManager m_roleManager;
    private ClassLoaderManager m_classLoaderManager;

    /** Map from ClassLoader to the deployer for that class loader. */
    private final Map m_classLoaderDeployers = new HashMap();

    /**
     * Retrieve relevent services needed to deploy.
     *
     * @param serviceManager the ServiceManager
     * @exception ServiceException if an error occurs
     */
    public void service( final ServiceManager serviceManager )
        throws ServiceException
    {
        m_converterRegistry = (ConverterRegistry)serviceManager.lookup( ConverterRegistry.ROLE );
        m_typeManager = (TypeManager)serviceManager.lookup( TypeManager.ROLE );
        m_roleManager = (RoleManager)serviceManager.lookup( RoleManager.ROLE );
        m_classLoaderManager = (ClassLoaderManager)serviceManager.lookup( ClassLoaderManager.ROLE );
    }

    /**
     * Creates a child deployer.
     */
    public Deployer createChildDeployer( final ServiceManager componentManager )
        throws ServiceException
    {
        final DefaultDeployer child = new DefaultDeployer();
        setupLogger( child );
        child.service( componentManager );
        return child;
    }

    /**
     * Returns the deployer for a ClassLoader, creating the deployer if
     * necessary.
     */
    public TypeDeployer createDeployer( final ClassLoader loader )
        throws DeploymentException
    {
        try
        {
            return createDeployment( loader, null );
        }
        catch( Exception e )
        {
            final String message = REZ.getString( "deploy-from-classloader.error", loader );
            throw new DeploymentException( message, e );
        }
    }

    /**
     * Returns the deployer for a type library, creating the deployer if
     * necessary.
     */
    public TypeDeployer createDeployer( final File file )
        throws DeploymentException
    {
        try
        {
            final ClassLoader classLoader = m_classLoaderManager.createClassLoader( file );
            return createDeployment( classLoader, file.toURL() );
        }
        catch( final Exception e )
        {
            final String message = REZ.getString( "deploy-from-file.error", file );
            throw new DeploymentException( message, e );
        }
    }

    /**
     * Creates a deployer for a ClassLoader.
     */
    private Deployment createDeployment( final ClassLoader loader,
                                         final URL jarUrl )
        throws Exception
    {
        // Locate cached deployer, creating it if necessary
        Deployment deployment = (Deployment)m_classLoaderDeployers.get( loader );
        if( deployment == null )
        {
            deployment = new Deployment( this, loader );
            setupLogger( deployment );
            deployment.loadDescriptors( jarUrl );
            m_classLoaderDeployers.put( loader, deployment );
        }

        return deployment;
    }

    /**
     * Deploys a service.
     */
    public void deployService( final Deployment deployment,
                               final ServiceDefinition definition )
        throws Exception
    {
        final String roleShorthand = definition.getRoleShorthand();
        final String roleName = getRole( roleShorthand ).getName();
        final String factoryClassName = definition.getFactoryClass();
        handleType( deployment, ServiceFactory.ROLE, roleName, factoryClassName );
    }

    /**
     * Handles a type definition.
     */
    public void deployType( final Deployment deployment,
                            final TypeDefinition typeDef )
        throws Exception
    {
        final String typeName = typeDef.getName();
        final String roleShorthand = typeDef.getRole();

        final String className = typeDef.getClassname();
        if( null == className )
        {
            final String message = REZ.getString( "typedef.no-classname.error" );
            throw new DeploymentException( message );
        }

        if( typeDef instanceof ConverterDefinition )
        {
            // Validate the definition
            final ConverterDefinition converterDef = (ConverterDefinition)typeDef;
            final String srcClass = converterDef.getSourceType();
            final String destClass = converterDef.getDestinationType();
            if( null == srcClass )
            {
                final String message = REZ.getString( "converterdef.no-source.error" );
                throw new DeploymentException( message );
            }
            if( null == destClass )
            {
                final String message = REZ.getString( "converterdef.no-destination.error" );
                throw new DeploymentException( message );
            }

            // Deploy the converter
            handleConverter( deployment, className, srcClass, destClass );
        }
        else
        {
            // Validate the definition
            if( null == roleShorthand )
            {
                final String message = REZ.getString( "typedef.no-role.error" );
                throw new DeploymentException( message );
            }
            else if( null == typeName )
            {
                final String message = REZ.getString( "typedef.no-name.error" );
                throw new DeploymentException( message );
            }

            // Deploy general-purpose type
            final String roleName = getRole( roleShorthand ).getName();
            handleType( deployment, roleName, typeName, className );

            if( getLogger().isDebugEnabled() )
            {
                final String message =
                    REZ.getString( "register-type.notice", roleShorthand, typeName );
                getLogger().debug( message );
            }
        }
    }

    /**
     * Handles a type definition.
     */
    private void handleType( final Deployment deployment,
                             final String roleName,
                             final String typeName,
                             final String className )
        throws Exception
    {
        // TODO - detect duplicates
        final DefaultTypeFactory factory = deployment.getFactory( roleName );
        factory.addNameClassMapping( typeName, className );
        m_typeManager.registerType( roleName, typeName, factory );
    }

    /**
     * Handles a converter definition.
     */
    private void handleConverter( final Deployment deployment,
                                  final String className,
                                  final String source,
                                  final String destination )
        throws Exception
    {
        m_converterRegistry.registerConverter( className, source, destination );
        final DefaultTypeFactory factory = deployment.getFactory( Converter.ROLE );
        factory.addNameClassMapping( className, className );
        m_typeManager.registerType( Converter.ROLE, className, factory );

        if( getLogger().isDebugEnabled() )
        {
            final String message =
                REZ.getString( "register-converter.notice", source, destination );
            getLogger().debug( message );
        }
    }

    /**
     * Handles a role definition.
     */
    public void deployRole( final Deployment deployment,
                            final RoleDefinition roleDef )
        throws Exception
    {
        final String name = roleDef.getShortHand();
        final String role = roleDef.getRoleName();
        final Class type = deployment.getClassLoader().loadClass( role );
        final RoleInfo roleInfo = new RoleInfo( role, name, type, null );
        m_roleManager.addRole( roleInfo );

        if( getLogger().isDebugEnabled() )
        {
            final String debugMessage = REZ.getString( "register-role.notice", role, name );
            getLogger().debug( debugMessage );
        }
    }

    /**
     * Locates a role, from its shorthand.
     */
    private RoleInfo getRole( final String roleShorthand )
        throws DeploymentException
    {
        final RoleInfo roleInfo = m_roleManager.getRoleByShorthandName( roleShorthand );
        if( null == roleInfo )
        {
            final String message = REZ.getString( "unknown-role4name.error", roleShorthand );
            throw new DeploymentException( message );
        }
        return roleInfo;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17422.java