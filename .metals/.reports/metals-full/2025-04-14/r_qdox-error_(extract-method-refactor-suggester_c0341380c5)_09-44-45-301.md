error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17416.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17416.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17416.java
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
package org.apache.myrmidon.components.builder;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.avalon.excalibur.i18n.ResourceManager;
import org.apache.avalon.excalibur.i18n.Resources;
import org.apache.avalon.framework.configuration.SAXConfigurationHandler;
import org.apache.avalon.framework.parameters.Parameterizable;
import org.apache.avalon.framework.parameters.Parameters;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * Default implementation to construct project from a build file.
 *
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 * @version $Revision$ $Date$
 * @ant.type type="project-builder" name="ati"
 */
public class ATIProjectBuilder
    extends DefaultProjectBuilder
    implements Parameterizable
{
    private final static Resources REZ =
        ResourceManager.getPackageResources( ATIProjectBuilder.class );

    private Parameters m_parameters;

    public void parameterize( final Parameters parameters )
    {
        m_parameters = parameters;
    }

    protected void process( final URL sourceID,
                            final SAXConfigurationHandler handler )
        throws Exception
    {
        final SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        final SAXParser saxParser = saxParserFactory.newSAXParser();
        final XMLReader reader = saxParser.getXMLReader();
        reader.setFeature( "http://xml.org/sax/features/validation", false );
        reader.setErrorHandler( handler );

        final ReactorPIHandler reactorHandler = new ReactorPIHandler();
        reader.setContentHandler( reactorHandler );

        try
        {
            reader.parse( sourceID.toString() );
        }
        catch( final StopParsingException spe )
        {
            //Ignore me
        }

        Transformer transformer = null;

        final int size = reactorHandler.getPICount();
        for( int i = 0; i < size; i++ )
        {
            final String target = reactorHandler.getTarget( i );
            final String data = reactorHandler.getData( i );

            if( target.equals( "xsl-param" ) )
            {
                handleParameter( data );
            }
            else if( target.equals( "xsl-params" ) )
            {
                handleParameters( data, sourceID );
            }
            else if( target.equals( "xsl-stylesheet" ) )
            {
                if( null != transformer )
                {
                    final String message = REZ.getString( "ati.two.stylesheet.pis" );
                    throw new SAXException( message );
                }

                final TransformerFactory factory = TransformerFactory.newInstance();
                final String stylesheet = getStylesheet( data, sourceID );
                transformer = factory.newTransformer( new StreamSource( stylesheet ) );
            }
        }

        if( null == transformer )
        {
            reader.setContentHandler( handler );
            reader.parse( sourceID.toString() );
        }
        else
        {
            final String[] names = m_parameters.getNames();
            for( int i = 0; i < names.length; i++ )
            {
                final String name = names[ i ];
                final String value = m_parameters.getParameter( name );
                transformer.setParameter( name, value );
            }

            final SAXResult result = new SAXResult( handler );
            transformer.transform( new StreamSource( sourceID.toString() ), result );
            //transformer.transform( new StreamSource( sourceID.toString() ),
            //new StreamResult( System.out ) );
        }
    }

    private void handleParameter( final String data )
        throws SAXException
    {
        int index = data.indexOf( '\"' );
        if( -1 == index )
        {
            final String message = REZ.getString( "ati.param.error" );
            throw new SAXException( message );
        }

        index = data.indexOf( '\"', index + 1 );
        if( -1 == index )
        {
            final String message = REZ.getString( "ati.param.error" );
            throw new SAXException( message );
        }

        //split between two "attributes" occurs on index
        final String[] name = parseAttribute( data.substring( 0, index + 1 ) );
        final String[] value = parseAttribute( data.substring( index + 1 ).trim() );

        if( !name[ 0 ].equals( "name" ) || !value[ 0 ].equals( "value" ) )
        {
            final String message = REZ.getString( "ati.param.error" );
            throw new SAXException( message );
        }

        m_parameters.setParameter( name[ 1 ], value[ 1 ] );
    }

    private void handleParameters( final String data, final URL baseSource )
        throws SAXException
    {
        final String[] params = parseAttribute( data );
        if( !params[ 0 ].equals( "location" ) )
        {
            final String message = REZ.getString( "ati.params.error" );
            throw new SAXException( message );
        }

        try
        {
            final Properties properties = new Properties();
            final URL url = new URL( baseSource, params[ 1 ] );
            final InputStream input = url.openStream();
            properties.load( input );
            final Parameters parameters = Parameters.fromProperties( properties );
            m_parameters.merge( parameters );
        }
        catch( final Exception e )
        {
            final String message = REZ.getString( "ati.loading-params.error", params[ 1 ], e );
            throw new SAXException( message );
        }
    }

    private String getStylesheet( final String data, final URL baseSource )
        throws SAXException
    {
        final String[] stylesheet = parseAttribute( data );
        if( !stylesheet[ 0 ].equals( "href" ) )
        {
            final String message = REZ.getString( "ati.style.error" );
            throw new SAXException( message );
        }

        try
        {
            return new URL( baseSource, stylesheet[ 1 ] ).toString();
        }
        catch( final Exception e )
        {
            final String message =
                REZ.getString( "ati.loading-style.error", stylesheet[ 1 ], e );
            throw new SAXException( message );
        }
    }

    private String[] parseAttribute( final String data )
        throws SAXException
    {
        //name="value"
        int index = data.indexOf( '=' );
        if( -1 == index )
        {
            final String message = REZ.getString( "ati.attribue-expected.error", data );
            throw new SAXException( message );
        }

        final int size = data.length();
        if( '\"' != data.charAt( index + 1 ) ||
            '\"' != data.charAt( size - 1 ) ||
            size - 1 == index )
        {
            final String message =
                REZ.getString( "ati.attribue-unquoted.error", data.substring( 0, index ) );
            throw new SAXException( message );
        }

        final String[] result = new String[ 2 ];
        result[ 0 ] = data.substring( 0, index );
        result[ 1 ] = data.substring( index + 2, size - 1 );

        return result;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17416.java