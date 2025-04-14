error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2623.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2623.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2623.java
text:
```scala
public static S@@tring formatCommandLine( final String[] arguments )

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.tools.todo.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Stack;
import java.util.StringTokenizer;
import org.apache.avalon.excalibur.io.FileUtil;
import org.apache.myrmidon.api.TaskException;

/**
 * This class also encapsulates methods which allow Files to be refered to using
 * abstract path names which are translated to native system file paths at
 * runtime as well as copying files or setting there last modification time.
 *
 * @author duncan@x180.com
 * @author Conor MacNeill
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 * @version $Revision$
 */
public class FileUtils
{
    /**
     * Parse out a path as appropriate for current OS.
     */
    public static String[] parsePath( final String path )
    {
        final PathTokenizer elements = new PathTokenizer( path );

        final ArrayList result = new ArrayList();
        while( elements.hasNext() )
        {
            result.add( elements.next() );
        }

        return (String[])result.toArray( new String[ result.size() ] );
    }

    /**
     * &quot;normalize&quot; the given absolute path. <p>
     *
     * This includes:
     * <ul>
     *   <li> Uppercase the drive letter if there is one.</li>
     *   <li> Remove redundant slashes after the drive spec.</li>
     *   <li> resolve all ./, .\, ../ and ..\ sequences.</li>
     *   <li> DOS style paths that start with a drive letter will have \ as the
     *   separator.</li>
     * </ul>
     *
     *
     * @param path Description of Parameter
     * @return Description of the Returned Value
     * @throws java.lang.NullPointerException if the file path is equal to null.
     */
    public static File normalize( String path )
        throws TaskException
    {
        String orig = path;

        path = path.replace( '/', File.separatorChar )
            .replace( '\\', File.separatorChar );

        // make sure we are dealing with an absolute path
        if( !path.startsWith( File.separator ) &&
            !( path.length() >= 2 &&
            Character.isLetter( path.charAt( 0 ) ) &&
            path.charAt( 1 ) == ':' )
        )
        {
            String msg = path + " is not an absolute path";
            throw new TaskException( msg );
        }

        boolean dosWithDrive = false;
        String root = null;
        // Eliminate consecutive slashes after the drive spec
        if( path.length() >= 2 &&
            Character.isLetter( path.charAt( 0 ) ) &&
            path.charAt( 1 ) == ':' )
        {

            dosWithDrive = true;

            char[] ca = path.replace( '/', '\\' ).toCharArray();
            StringBuffer sb = new StringBuffer();
            sb.append( Character.toUpperCase( ca[ 0 ] ) ).append( ':' );

            for( int i = 2; i < ca.length; i++ )
            {
                if( ( ca[ i ] != '\\' ) ||
                    ( ca[ i ] == '\\' && ca[ i - 1 ] != '\\' )
                )
                {
                    sb.append( ca[ i ] );
                }
            }

            path = sb.toString().replace( '\\', File.separatorChar );
            if( path.length() == 2 )
            {
                root = path;
                path = "";
            }
            else
            {
                root = path.substring( 0, 3 );
                path = path.substring( 3 );
            }

        }
        else
        {
            if( path.length() == 1 )
            {
                root = File.separator;
                path = "";
            }
            else if( path.charAt( 1 ) == File.separatorChar )
            {
                // UNC drive
                root = File.separator + File.separator;
                path = path.substring( 2 );
            }
            else
            {
                root = File.separator;
                path = path.substring( 1 );
            }
        }

        Stack s = new Stack();
        s.push( root );
        StringTokenizer tok = new StringTokenizer( path, File.separator );
        while( tok.hasMoreTokens() )
        {
            String thisToken = tok.nextToken();
            if( ".".equals( thisToken ) )
            {
                continue;
            }
            else if( "..".equals( thisToken ) )
            {
                if( s.size() < 2 )
                {
                    throw new TaskException( "Cannot resolve path " + orig );
                }
                else
                {
                    s.pop();
                }
            }
            else
            {// plain component
                s.push( thisToken );
            }
        }

        StringBuffer sb = new StringBuffer();
        for( int i = 0; i < s.size(); i++ )
        {
            if( i > 1 )
            {
                // not before the filesystem root and not after it, since root
                // already contains one
                sb.append( File.separatorChar );
            }
            sb.append( s.get( i ) );
        }

        path = sb.toString();
        if( dosWithDrive )
        {
            path = path.replace( '/', '\\' );
        }
        return new File( path );
    }

    /**
     * Builds a command-line from an array of individual arguments, quoting
     * the arguments as necessary.
     *
     * @todo Move to {@link org.apache.aut.nativelib.Os}, and get rid of the
     * exception.
     */
    public static String buildCommandLine( final String[] arguments )
        throws TaskException
    {
        final StringBuffer cmd = new StringBuffer();
        for( int i = 0; i < arguments.length; i++ )
        {
            String arg = arguments[ i ];
            if( i > 0 )
            {
                cmd.append( ' ' );
            }
            cmd.append( quoteArgument( arg ) );
        }

        return cmd.toString();
    }

    /**
     * Put quotes around the given String if necessary. <p>
     *
     * If the argument doesn't include spaces or quotes, return it as is. If it
     * contains double quotes, use single quotes - else surround the argument by
     * double quotes.</p>
     *
     * @todo Move to {@link org.apache.aut.nativelib.Os}, and get rid of the
     * exception.
     */
    public static String quoteArgument( final String argument )
        throws TaskException
    {
        if( argument.indexOf( "\"" ) > -1 )
        {
            if( argument.indexOf( "\'" ) > -1 )
            {
                throw new TaskException( "Can\'t handle single and double quotes in same argument" );
            }
            else
            {
                return '\'' + argument + '\'';
            }
        }
        else if( argument.indexOf( "\'" ) > -1 || argument.indexOf( " " ) > -1 )
        {
            return '\"' + argument + '\"';
        }
        else
        {
            return argument;
        }
    }

    public static String[] translateCommandline( final String to_process )
        throws TaskException
    {
        if( to_process == null || to_process.length() == 0 )
        {
            return new String[ 0 ];
        }

        // parse with a simple finite state machine

        final int normal = 0;
        final int inQuote = 1;
        final int inDoubleQuote = 2;
        int state = normal;
        StringTokenizer tok = new StringTokenizer( to_process, "\"\' ", true );
        ArrayList v = new ArrayList();
        StringBuffer current = new StringBuffer();

        while( tok.hasMoreTokens() )
        {
            String nextTok = tok.nextToken();
            switch( state )
            {
                case inQuote:
                    if( "\'".equals( nextTok ) )
                    {
                        state = normal;
                    }
                    else
                    {
                        current.append( nextTok );
                    }
                    break;
                case inDoubleQuote:
                    if( "\"".equals( nextTok ) )
                    {
                        state = normal;
                    }
                    else
                    {
                        current.append( nextTok );
                    }
                    break;
                default:
                    if( "\'".equals( nextTok ) )
                    {
                        state = inQuote;
                    }
                    else if( "\"".equals( nextTok ) )
                    {
                        state = inDoubleQuote;
                    }
                    else if( " ".equals( nextTok ) )
                    {
                        if( current.length() != 0 )
                        {
                            v.add( current.toString() );
                            current.setLength( 0 );
                        }
                    }
                    else
                    {
                        current.append( nextTok );
                    }
                    break;
            }
        }

        if( current.length() != 0 )
        {
            v.add( current.toString() );
        }

        if( state == inQuote || state == inDoubleQuote )
        {
            throw new TaskException( "unbalanced quotes in " + to_process );
        }

        final String[] args = new String[ v.size() ];
        return (String[])v.toArray( args );
    }

    /**
     * Returns its argument with all file separator characters replaced so that
     * they match the local OS conventions.
     */
    public static String translateFile( final String source )
    {
        if( source == null )
        {
            return "";
        }

        final StringBuffer result = new StringBuffer( source );
        translateFileSep( result );
        return result.toString();
    }

    /**
     * Translates all occurrences of / or \ to correct separator of the current
     * platform and returns whether it had to do any replacements.
     */
    public static void translateFileSep( StringBuffer buffer )
    {
        int len = buffer.length();
        for( int pos = 0; pos < len; pos++ )
        {
            char ch = buffer.charAt( pos );
            if( ch == '/' || ch == '\\' )
            {
                buffer.setCharAt( pos, File.separatorChar );
            }
        }
    }

    /**
     * Splits a PATH (with : or ; as separators) into its parts.
     */
    public static String[] translatePath( final File baseDirectory,
                                          String source )
        throws TaskException
    {
        final ArrayList result = new ArrayList();
        if( source == null )
        {
            return new String[ 0 ];
        }

        final String[] elements = parsePath( source );
        StringBuffer element = new StringBuffer();
        for( int i = 0; i < elements.length; i++ )
        {
            // Resolve the file relative to the base directory
            element.setLength( 0 );
            final String pathElement = elements[ i ];
            element.append( resolveFile( baseDirectory, pathElement ) );

            // Tidy up the separators
            translateFileSep( element );
            result.add( element.toString() );
        }

        return (String[])result.toArray( new String[ result.size() ] );
    }

    /**
     * Resolve a filename with Project's help - if we know one that is. <p>
     *
     * Assume the filename is absolute if project is null.</p>
     */
    public static String resolveFile( final File baseDirectory, final String relativeName )
        throws TaskException
    {
        if( null != baseDirectory )
        {
            final File file = FileUtil.resolveFile( baseDirectory, relativeName );
            return file.getAbsolutePath();
        }
        return relativeName;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2623.java