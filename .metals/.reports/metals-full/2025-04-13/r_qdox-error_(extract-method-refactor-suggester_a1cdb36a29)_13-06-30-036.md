error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8152.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8152.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8152.java
text:
```scala
private final static R@@esources REZ =

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.myrmidon.components.deployer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.avalon.excalibur.i18n.ResourceManager;
import org.apache.avalon.excalibur.i18n.Resources;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.SAXConfigurationHandler;
import org.apache.myrmidon.interfaces.deployer.DeploymentException;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * This class deploys a .tsk file into a registry.
 *
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 */
public class Deployment
{
    private static final Resources REZ =
        ResourceManager.getPackageResources( Deployment.class );

    public final static String DESCRIPTOR_NAME = "META-INF/ant-descriptor.xml";

    private File m_file;

    private Configuration m_descriptor;

    public Deployment( final File file )
    {
        m_file = file;
    }

    public Configuration getDescriptor()
        throws DeploymentException
    {
        if( null == m_descriptor )
        {
            m_descriptor = buildDescriptor();
        }

        return m_descriptor;
    }

    public URL getURL()
        throws DeploymentException
    {
        try
        {
            return m_file.getCanonicalFile().toURL();
        }
        catch( final IOException ioe )
        {
            final String message = REZ.getString( "bad-url.error", m_file );
            throw new DeploymentException( message, ioe );
        }
    }

    private Configuration buildDescriptor()
        throws DeploymentException
    {
        final String systemID = "jar:" + getURL() + "!/" + DESCRIPTOR_NAME;

        try
        {
            final SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            final SAXParser saxParser = saxParserFactory.newSAXParser();
            final XMLReader parser = saxParser.getXMLReader();
            //parser.setFeature( "http://xml.org/sax/features/namespace-prefixes", false );

            final SAXConfigurationHandler handler = new SAXConfigurationHandler();
            parser.setContentHandler( handler );
            parser.setErrorHandler( handler );

            parser.parse( systemID );
            return handler.getConfiguration();
        }
        catch( final SAXException se )
        {
            final String message = REZ.getString( "bad-descriptor.error" );
            throw new DeploymentException( message, se );
        }
        catch( final ParserConfigurationException pce )
        {
            final String message = REZ.getString( "bad-parser.error" );
            throw new DeploymentException( message, pce );
        }
        catch( final IOException ioe )
        {
            final String message = REZ.getString( "bad-read.error" );
            throw new DeploymentException( message, ioe );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8152.java