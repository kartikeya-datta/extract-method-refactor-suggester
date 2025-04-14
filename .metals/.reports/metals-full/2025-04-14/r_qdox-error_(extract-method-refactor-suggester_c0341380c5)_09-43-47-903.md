error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17405.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17405.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17405.java
text:
```scala
private static final R@@esources REZ

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.aut.vfs.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.apache.aut.vfs.FileObject;
import org.apache.aut.vfs.FileSystemException;
import org.apache.aut.vfs.FileSystemManager;
import org.apache.aut.vfs.provider.FileSystemProvider;
import org.apache.aut.vfs.provider.UriParser;
import org.apache.aut.vfs.provider.local.LocalFileSystemProvider;
import org.apache.avalon.excalibur.i18n.ResourceManager;
import org.apache.avalon.excalibur.i18n.Resources;

/**
 * A default file system manager implementation.
 *
 * @author <a href="mailto:adammurdoch@apache.org">Adam Murdoch</a>
 * @version $Revision$ $Date$
 */
public class DefaultFileSystemManager
    implements FileSystemManager
{
    private final static Resources REZ
        = ResourceManager.getPackageResources( DefaultFileSystemManager.class );

    /** The default provider. */
    private final LocalFileSystemProvider m_localFileProvider;

    /** Mapping from URI scheme to FileSystemProvider. */
    private final Map m_providers = new HashMap();

    /** The provider context. */
    private final DefaultProviderContext m_context = new DefaultProviderContext( this );

    /** The base file to use for relative URI. */
    private FileObject m_baseFile;

    public DefaultFileSystemManager()
    {
        // Create the local provider
        m_localFileProvider = new LocalFileSystemProvider();
        m_providers.put( "file", m_localFileProvider );
        m_localFileProvider.setContext( m_context );
    }

    /**
     * Registers a file system provider.
     */
    public void addProvider( final String urlScheme,
                             final FileSystemProvider provider )
        throws FileSystemException
    {
        addProvider( new String[] { urlScheme }, provider );
    }

    /**
     * Registers a file system provider.
     */
    public void addProvider( final String[] urlSchemes,
                             final FileSystemProvider provider )
        throws FileSystemException
    {
        // Check for duplicates
        for( int i = 0; i < urlSchemes.length; i++ )
        {
            final String scheme = urlSchemes[i ];
            if( m_providers.containsKey( scheme ) )
            {
                final String message = REZ.getString( "multiple-providers-for-scheme.error", scheme );
                throw new FileSystemException( message );
            }
        }

        // Contextualise
        provider.setContext( m_context );

        // Add to map
        for( int i = 0; i < urlSchemes.length; i++ )
        {
            final String scheme = urlSchemes[ i ];
            m_providers.put( scheme, provider );
        }
    }

    /**
     * Closes all file systems created by this file system manager.
     */
    public void close()
    {
        // TODO - implement this
    }

    /**
     * Sets the base file to use when resolving relative URI.
     */
    public void setBaseFile( final FileObject baseFile ) throws FileSystemException
    {
        m_baseFile = baseFile;
    }

    /**
     * Sets the base file to use when resolving relative URI.
     */
    public void setBaseFile( final File baseFile ) throws FileSystemException
    {
        m_baseFile = m_localFileProvider.findLocalFile( baseFile.getAbsolutePath() );
    }

    /**
     * Returns the base file used to resolve relative URI.
     */
    public FileObject getBaseFile()
    {
        return m_baseFile;
    }

    /**
     * Locates a file by URI.
     */
    public FileObject resolveFile( final String uri ) throws FileSystemException
    {
        return resolveFile( m_baseFile, uri );
    }

    /**
     * Locates a file by URI.
     */
    public FileObject resolveFile( final File baseFile, final String uri )
        throws FileSystemException
    {
        final FileObject baseFileObj = m_localFileProvider.findLocalFile( baseFile );
        return resolveFile( baseFileObj, uri );
    }

    /**
     * Resolves a URI, relative to a base file.
     */
    public FileObject resolveFile( final FileObject baseFile, final String uri )
        throws FileSystemException
    {
        // Extract the scheme
        final String scheme = UriParser.extractScheme( uri );
        if( scheme != null )
        {
            // An absolute URI - locate the provider
            final FileSystemProvider provider = (FileSystemProvider)m_providers.get( scheme );
            if( provider != null )
            {
                return provider.findFile( baseFile, uri );
            }
        }

        // Decode the URI (remove %nn encodings)
        final String decodedUri = UriParser.decode( uri );

        // Handle absolute file names
        if( m_localFileProvider.isAbsoluteLocalName( decodedUri ) )
        {
            return m_localFileProvider.findLocalFile( decodedUri );
        }

        // Assume a bad scheme
        if( scheme != null )
        {
            final String message = REZ.getString( "unknown-scheme.error", scheme, uri );
            throw new FileSystemException( message );
        }

        // Use the supplied base file
        if( baseFile == null )
        {
            final String message = REZ.getString( "find-rel-file.error", uri );
            throw new FileSystemException( message );
        }
        return baseFile.resolveFile( decodedUri );
    }

    /**
     * Converts a local file into a {@link FileObject}.
     */
    public FileObject convert( final File file )
        throws FileSystemException
    {
        return m_localFileProvider.findLocalFile( file );
    }

    /**
     * Creates a layered file system.
     */
    public FileObject createFileSystem( final String scheme,
                                        final FileObject file )
        throws FileSystemException
    {
        FileSystemProvider provider = (FileSystemProvider)m_providers.get( scheme );
        if( provider == null )
        {
            final String message = REZ.getString( "unknown-provider.error", scheme );
            throw new FileSystemException( message );
        }
        return provider.createFileSystem( scheme, file );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17405.java