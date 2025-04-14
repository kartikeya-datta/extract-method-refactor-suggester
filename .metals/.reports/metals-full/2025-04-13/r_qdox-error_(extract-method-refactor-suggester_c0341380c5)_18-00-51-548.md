error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17425.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17425.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17425.java
text:
```scala
private static final R@@esources REZ =

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included  with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.myrmidon.components.property;

import org.apache.aut.converter.Converter;
import org.apache.aut.converter.ConverterException;
import org.apache.avalon.excalibur.i18n.ResourceManager;
import org.apache.avalon.excalibur.i18n.Resources;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.myrmidon.api.TaskContext;
import org.apache.myrmidon.api.TaskException;
import org.apache.myrmidon.interfaces.property.PropertyResolver;

/**
 * Base class for PropertyResolver implementations.
 *
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 * @author <a href="mailto:darrell@apache.org">Darrell DeBoer</a>
 * @version $Revision$ $Date$
 *
 * @ant.type type="property-resolver" name="default"
 */
public class DefaultPropertyResolver
    implements PropertyResolver, Serviceable
{
    private final static Resources REZ =
        ResourceManager.getPackageResources( DefaultPropertyResolver.class );

    private Converter m_converter;

    public void service( final ServiceManager serviceManager ) throws ServiceException
    {
        m_converter = (Converter)serviceManager.lookup( Converter.ROLE );
    }

    /**
     * Resolve a string property. This evaluates all property
     * substitutions based on specified context.
     *
     * If the content contains a single property reference, then the property value
     * <code>Object</code> itself is returned.
     * Otherwise, a <code>String</code> is returned, comprising the supplied
     * content, with all property references replaced with the result of
     * <code>toString()</code> called on the property value.
     *
     * @param content the property to resolve
     * @param context the context in which to resolve property
     * @return the reolved property
     * @exception TaskException if an error occurs
     */
    public Object resolveProperties( final String content,
                                     final TaskContext context )
        throws TaskException
    {
        int start = findNextProperty( content, 0 );
        if( -1 == start )
        {
            return content;
        }

        int end = findEnding( content, start );

        final int length = content.length();

        if( 0 == start && end == ( length - 1 ) )
        {
            return getPropertyValue( content.substring( start + 2, end ),
                                     context );
        }

        final StringBuffer sb = new StringBuffer( length * 2 );
        int lastPlace = 0;

        while( true )
        {
            final String propertyValue =
                getPropertyStringValue( content.substring( start + 2, end ),
                                        context );

            sb.append( content.substring( lastPlace, start ) );
            sb.append( propertyValue );

            lastPlace = end + 1;

            start = findNextProperty( content, lastPlace );
            if( -1 == start )
            {
                break;
            }

            end = findEnding( content, start );
        }

        sb.append( content.substring( lastPlace, length ) );

        return sb.toString();
    }

    /**
     * Resolve a string property. This recursively evaluates all property
     * substitutions based on specified context.
     *
     * @param content the property to resolve
     * @param context the context in which to resolve property
     * @return the reolved property
     * @exception TaskException if an error occurs
     */
    private Object recursiveResolveProperty( final String content,
                                             final TaskContext context )
        throws TaskException
    {
        int start = findNextProperty( content, 0 );
        if( -1 == start )
        {
            return content;
        }

        int end = findNestedEnding( content, start );

        final int length = content.length();

        if( 0 == start && end == ( length - 1 ) )
        {
            final String propertyName = content.substring( start + 2, end );
            final Object key = recursiveResolveProperty( propertyName, context );
            return getPropertyValue( key.toString(), context );
        }

        final StringBuffer sb = new StringBuffer( length * 2 );

        int lastPlace = 0;

        while( true )
        {
            final String propertyName = content.substring( start + 2, end );
            final Object key = recursiveResolveProperty( propertyName, context );
            final String value = getPropertyStringValue( key.toString(), context );

            sb.append( content.substring( lastPlace, start ) );
            sb.append( value );

            lastPlace = end + 1;

            start = findNextProperty( content, lastPlace );
            if( -1 == start )
            {
                break;
            }

            end = findNestedEnding( content, start );
        }

        sb.append( content.substring( lastPlace, length ) );

        return sb.toString();
    }

    /**
     * Finds the next occurrance of the start of a Property identifier.
     * @param content the String to search
     * @param currentPosition start location of the search
     * @return the position of the next occurrence, or <code>-1</code> if none
     *          was found.
     */
    private int findNextProperty( final String content, final int currentPosition )
    {
        //TODO: Check if it is commented out
        return content.indexOf( "${", currentPosition );
    }

    /**
     * Finds the next occurrence of the end of a Property identifier.
     * @param property the String to search
     * @param currentPosition start location of the search
     * @return the position of the next occurrence
     * @throws TaskException if no end was found
     */
    private int findEnding( final String property, final int currentPosition )
        throws TaskException
    {
        //TODO: Check if it is commented out
        final int index = property.indexOf( '}', currentPosition );
        if( -1 == index )
        {
            final String message = REZ.getString( "prop.mismatched-braces.error" );
            throw new TaskException( message );
        }

        return index;
    }

    /**
     * Finds the end of the property identifier at the currentPosition,
     * taking into account nested property identifiers.
     * @param property the String to search
     * @param currentPosition location of the property
     * @return the position of the propery ending.
     * @throws TaskException if the property is not properly ended.
     */
    private int findNestedEnding( final String property, final int currentPosition )
        throws TaskException
    {
        final int length = property.length();
        final int start = currentPosition + 2;

        int weight = 1;
        for( int i = start; ( weight > 0 ) && ( i < length ); i++ )
        {
            final char ch = property.charAt( i );
            switch( ch )
            {
                case '}':
                    //TODO: Check if it is commented out
                    weight--;
                    if( weight == 0 )
                    {
                        return i;
                    }
                    break;

                case '$':
                    {
                        //TODO: Check if it is commented out
                        final int next = i + 1;
                        if( next < length && '{' == property.charAt( next ) )
                        {
                            weight++;
                        }
                    }
                    break;
            }
        }

        final String message = REZ.getString( "prop.mismatched-braces.error" );
        throw new TaskException( message );
    }

    /**
     * Returns a property's value, converted to a String.
     */
    private String getPropertyStringValue( final String propertyName,
                                           final TaskContext context )
        throws TaskException
    {
        final Object value = getPropertyValue( propertyName, context );
        if( value instanceof String )
        {
            return (String)value;
        }
        try
        {
            return (String)m_converter.convert( String.class, value, context );
        }
        catch( final ConverterException e )
        {
            throw new TaskException( e.getMessage(), e );
        }
    }

    /**
     * Retrieve a value from the specified context using the specified key.
     *
     * @param propertyName the key of value in context
     * @param context the set of known properties
     * @return the object retrieved from context
     * @exception TaskException if the property is undefined
     */
    protected Object getPropertyValue( final String propertyName,
                                       final TaskContext context )
        throws TaskException
    {
        Object propertyValue = context.getProperty( propertyName );
        if ( propertyValue == null )
        {
            final String message = REZ.getString( "prop.missing-value.error", propertyName );
            throw new TaskException( message );
        }
        else
        {
            return propertyValue;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17425.java