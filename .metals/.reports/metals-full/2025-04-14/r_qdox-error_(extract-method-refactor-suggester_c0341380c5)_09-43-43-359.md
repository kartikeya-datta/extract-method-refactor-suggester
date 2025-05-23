error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4897.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4897.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4897.java
text:
```scala
m@@_status = GET_DATE;

/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2002 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Ant", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
package org.apache.tools.ant.taskdefs.cvslib;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

/**
 * A class used to parse the output of the CVS log command.
 *
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 * @version $Revision$ $Date$
 */
class ChangeLogParser
{
    //private static final int GET_ENTRY = 0;
    private static final int GET_FILE = 1;
    private static final int GET_DATE = 2;
    private static final int GET_COMMENT = 3;
    private static final int GET_REVISION = 4;
    private static final int GET_PREVIOUS_REV = 5;

    /** input format for dates read in from cvs log */
    private static final SimpleDateFormat c_inputDate = new SimpleDateFormat( "yyyy/MM/dd" );

    //The following is data used while processing stdout of CVS command
    private String m_file;
    private String m_date;
    private String m_author;
    private String m_comment;
    private String m_revision;
    private String m_previousRevision;

    private int m_status = GET_FILE;

    /** rcs entries */
    private final Hashtable m_entries = new Hashtable();

    private final Properties m_userList;

    /**
     * Construct a parser that uses specified user list.
     *
     * @param userList the userlist
     */
    public ChangeLogParser( Properties userList )
    {
        m_userList = userList;
    }

    /**
     * Get a list of rcs entrys as an array.
     *
     * @return a list of rcs entrys as an array
     */
    CVSEntry[] getEntrySetAsArray()
    {
        final CVSEntry[] array = new CVSEntry[ m_entries.size() ];
        Enumeration enum = m_entries.elements();
        int i = 0;
        while (enum.hasMoreElements()) {
            array[i++] = (CVSEntry) enum.nextElement();
        }
        return array;
    }

    /**
     * Receive notification about the process writing
     * to standard output.
     */
    public void stdout( final String line )
    {
        switch( m_status )
        {
            case GET_FILE:
                processFile( line );
                break;
            case GET_REVISION:
                processRevision( line );
                break;

            case GET_DATE:
                processDate( line );
                break;

            case GET_COMMENT:
                processComment( line );
                break;

            case GET_PREVIOUS_REV:
                processGetPreviousRevision( line );
                break;
        }
    }

    /**
     * Process a line while in "GET_COMMENT" state.
     *
     * @param line the line
     */
    private void processComment( final String line )
    {
        final String lineSeparator = System.getProperty( "line.separator" );
        if( line.startsWith( "======" ) )
        {
            //We have ended changelog for that particular file
            //so we can save it
            final int end = m_comment.length() - lineSeparator.length(); //was -1
            m_comment = m_comment.substring( 0, end );
            saveEntry();
            m_status = GET_FILE;
        }
        else if( line.startsWith( "------" ) )
        {
            final int end = m_comment.length() - lineSeparator.length(); //was -1
            m_comment = m_comment.substring( 0, end );
            m_status = GET_PREVIOUS_REV;
        }
        else
        {
            m_comment += line + lineSeparator;
        }
    }

    /**
     * Process a line while in "GET_FILE" state.
     *
     * @param line the line
     */
    private void processFile( final String line )
    {
        if( line.startsWith( "Working file:" ) )
        {
            m_file = line.substring( 14, line.length() );
            m_status = GET_REVISION;
        }
    }

    /**
     * Process a line while in "REVISION" state.
     *
     * @param line the line
     */
    private void processRevision( final String line )
    {
        if( line.startsWith( "revision" ) )
        {
            m_revision = line.substring( 9 );
            m_status = GET_DATE;
        }
        else if( line.startsWith( "======" ) )
        {
            //There was no revisions in this changelog
            //entry so lets move unto next file
            m_status = GET_FILE;
        }
    }

    /**
     * Process a line while in "DATE" state.
     *
     * @param line the line
     */
    private void processDate( final String line )
    {
        if( line.startsWith( "date:" ) )
        {
            m_date = line.substring( 6, 16 );
            String lineData = line.substring( line.indexOf( ";" ) + 1 );
            m_author = lineData.substring( 10, lineData.indexOf( ";" ) );

            if( m_userList.containsKey( m_author ) )
            {
                m_author = m_userList.getProperty( m_author );
            }

            m_status = GET_COMMENT;

            //Reset comment to empty here as we can accumulate multiple lines
            //in the processComment method
            m_comment = "";
        }
    }

    /**
     * Process a line while in "GET_PREVIOUS_REVISION" state.
     *
     * @param line the line
     */
    private void processGetPreviousRevision( final String line )
    {
        if( !line.startsWith( "revision" ) )
        {
            throw new IllegalStateException( "Unexpected line from CVS: " + line );
        }
        m_previousRevision = line.substring( 9 );

        saveEntry();

        m_revision = m_previousRevision;
        m_status = GET_COMMENT;
    }

    /**
     * Utility method that saves the current entry.
     */
    private void saveEntry()
    {
        final String entryKey = m_date + m_author + m_comment;
        CVSEntry entry;
        if( !m_entries.containsKey( entryKey ) )
        {
            entry = new CVSEntry( parseDate( m_date ), m_author, m_comment );
            m_entries.put( entryKey, entry );
        }
        else
        {
            entry = (CVSEntry)m_entries.get( entryKey );
        }

        entry.addFile( m_file, m_revision, m_previousRevision );
    }

    /**
     * Parse date out from expected format.
     *
     * @param date the string holding dat
     * @return the date object or null if unknown date format
     */
    private Date parseDate( final String date )
    {
        try
        {
            return c_inputDate.parse( date );
        }
        catch( ParseException e )
        {
            //final String message = REZ.getString( "changelog.bat-date.error", date );
            //getContext().error( message );
            return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4897.java