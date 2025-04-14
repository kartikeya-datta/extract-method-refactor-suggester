error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6694.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6694.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6694.java
text:
```scala
private T@@ypeDefinition createTypeDefinition( final Configuration configuration )

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included  with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.myrmidon.components.deployer;

import org.apache.avalon.excalibur.i18n.ResourceManager;
import org.apache.avalon.excalibur.i18n.Resources;
import org.apache.avalon.framework.Version;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.myrmidon.interfaces.deployer.ConverterDefinition;
import org.apache.myrmidon.interfaces.deployer.DeploymentException;
import org.apache.myrmidon.interfaces.deployer.TypeDefinition;

/**
 * Builds typelib type descriptors.
 *
 * @author <a href="mailto:adammurdoch@apache.org">Adam Murdoch</a>
 * @version $Revision$ $Date$
 */
class TypeDescriptorBuilder
    implements DescriptorBuilder
{
    private final static Resources REZ =
        ResourceManager.getPackageResources( TypeDescriptorBuilder.class );

    private final static Version TYPE_DESCRIPTOR_VERSION = new Version( 1, 0, 0 );

    /**
     * Builds a descriptor from a set of configuration.
     */
    public TypelibDescriptor createDescriptor( final Configuration config,
                                               final String url )
        throws DeploymentException
    {
        try
        {
            // Check version
            final String versionString = config.getAttribute( "version" );
            final Version version = Version.getVersion( versionString );
            if( !TYPE_DESCRIPTOR_VERSION.complies( version ) )
            {
                final String message = REZ.getString( "type-descriptor-version.error", version, TYPE_DESCRIPTOR_VERSION );
                throw new DeploymentException( message );
            }

            // Assemble the descriptor
            final TypeDescriptor descriptor = new TypeDescriptor( url );

            // Extract each of the types elements
            final Configuration[] typeEntries = config.getChild( "types" ).getChildren();
            for( int i = 0; i < typeEntries.length; i++ )
            {
                final Configuration typeEntry = typeEntries[ i ];
                final TypeDefinition typeDef = createTypeDefinition( typeEntry );
                descriptor.addDefinition( typeDef );
            }

            return descriptor;
        }
        catch( Exception e )
        {
            final String message = REZ.getString( "build-type-descriptor.error", url );
            throw new DeploymentException( message, e );
        }
    }

    /**
     * Creates a type definition.
     */
    public TypeDefinition createTypeDefinition( final Configuration configuration )
        throws ConfigurationException
    {
        final String roleShorthand = configuration.getName();
        if( roleShorthand.equals( "converter" ) )
        {
            // A converter definition
            final String className = configuration.getAttribute( "classname" );
            final String source = configuration.getAttribute( "source" );
            final String destination = configuration.getAttribute( "destination" );
            return new ConverterDefinition( className, source, destination );
        }
        else
        {
            // A type definition
            final String typeName = configuration.getAttribute( "name" );
            final String className = configuration.getAttribute( "classname" );
            return new TypeDefinition( typeName, roleShorthand, className );
        }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6694.java