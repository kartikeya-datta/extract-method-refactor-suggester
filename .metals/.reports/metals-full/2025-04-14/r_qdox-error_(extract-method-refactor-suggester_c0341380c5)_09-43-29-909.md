error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17374.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17374.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17374.java
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
package org.apache.antlib.cvslib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import java.util.ArrayList;
import java.util.Date;
import org.apache.avalon.excalibur.i18n.ResourceManager;
import org.apache.avalon.excalibur.i18n.Resources;
import org.apache.avalon.excalibur.io.IOUtil;
import org.apache.myrmidon.api.AbstractTask;
import org.apache.myrmidon.api.TaskException;
import org.apache.myrmidon.framework.Execute;
import org.apache.tools.todo.types.Commandline;

/**
 * Change log task.
 * The task will examine the output of cvs log and group related changes together.
 * It produces an XML output representing the list of changes.
 * <PRE>
 * <FONT color=#0000ff>&lt;!-- Root element --&gt;</FONT>
 * <FONT color=#6a5acd>&lt;!ELEMENT</FONT> changelog <FONT color=#ff00ff>(entry</FONT><FONT color=#ff00ff>+</FONT><FONT color=#ff00ff>)</FONT><FONT color=#6a5acd>&gt;</FONT>
 * <FONT color=#0000ff>&lt;!-- CVS Entry --&gt;</FONT>
 * <FONT color=#6a5acd>&lt;!ELEMENT</FONT> entry <FONT color=#ff00ff>(date,author,file</FONT><FONT color=#ff00ff>+</FONT><FONT color=#ff00ff>,msg)</FONT><FONT color=#6a5acd>&gt;</FONT>
 * <FONT color=#0000ff>&lt;!-- Date of cvs entry --&gt;</FONT>
 * <FONT color=#6a5acd>&lt;!ELEMENT</FONT> date <FONT color=#ff00ff>(#PCDATA)</FONT><FONT color=#6a5acd>&gt;</FONT>
 * <FONT color=#0000ff>&lt;!-- Author of change --&gt;</FONT>
 * <FONT color=#6a5acd>&lt;!ELEMENT</FONT> author <FONT color=#ff00ff>(#PCDATA)</FONT><FONT color=#6a5acd>&gt;</FONT>
 * <FONT color=#0000ff>&lt;!-- List of files affected --&gt;</FONT>
 * <FONT color=#6a5acd>&lt;!ELEMENT</FONT> msg <FONT color=#ff00ff>(#PCDATA)</FONT><FONT color=#6a5acd>&gt;</FONT>
 * <FONT color=#0000ff>&lt;!-- File changed --&gt;</FONT>
 * <FONT color=#6a5acd>&lt;!ELEMENT</FONT> file <FONT color=#ff00ff>(name,revision,prevrevision</FONT><FONT color=#ff00ff>?</FONT><FONT color=#ff00ff>)</FONT><FONT color=#6a5acd>&gt;</FONT>
 * <FONT color=#0000ff>&lt;!-- Name of the file --&gt;</FONT>
 * <FONT color=#6a5acd>&lt;!ELEMENT</FONT> name <FONT color=#ff00ff>(#PCDATA)</FONT><FONT color=#6a5acd>&gt;</FONT>
 * <FONT color=#0000ff>&lt;!-- Revision number --&gt;</FONT>
 * <FONT color=#6a5acd>&lt;!ELEMENT</FONT> revision <FONT color=#ff00ff>(#PCDATA)</FONT><FONT color=#6a5acd>&gt;</FONT>
 * <FONT color=#0000ff>&lt;!-- Previous revision number --&gt;</FONT>
 * <FONT color=#6a5acd>&lt;!ELEMENT</FONT> prevrevision <FONT color=#ff00ff>(#PCDATA)</FONT><FONT color=#6a5acd>&gt;</FONT>
 * </PRE>
 *
 * @author <a href="mailto:jeff.martin@synamic.co.uk">Jeff Martin</a>
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 * @version $Revision$ $Date$
 * @ant.task name="changelog"
 */
public class ChangeLog
    extends AbstractTask
{
    private final static Resources REZ =
        ResourceManager.getPackageResources( ChangeLog.class );

    /** User list */
    private File m_usersFile;

    /** User list */
    private Vector m_cvsUsers = new Vector();

    /** Input dir */
    private File m_basedir;

    /** Output file */
    private File m_destfile;

    /**
     * The earliest date at which to start processing entrys.
     */
    private Date m_start;

    /**
     * The latest date at which to stop processing entrys.
     */
    private Date m_stop;

    /**
     * Set the base dir for cvs.
     */
    public void setBasedir( final File basedir )
    {
        m_basedir = basedir;
    }

    /**
     * Set the output file for the log.
     */
    public void setDestfile( final File destfile )
    {
        m_destfile = destfile;
    }

    /**
     * Set a lookup list of user names & addresses
     */
    public void setUsersfile( final File usersFile )
    {
        m_usersFile = usersFile;
    }

    /**
     * Add a user to list changelog knows about.
     *
     * @param user the user
     */
    public void addUser( final CvsUser user )
    {
        m_cvsUsers.addElement( user );
    }

    /**
     * Set the date at which the changelog should start.
     *
     * @param start The date at which the changelog should start.
     */
    public void setStart( final Date start )
    {
        m_start = start;
    }

    /**
     * Set the date at which the changelog should stop.
     *
     * @param stop The date at which the changelog should stop.
     */
    public void setEnd( final Date stop )
    {
        m_stop = stop;
    }

    /**
     * Execute task
     */
    public void execute() throws TaskException
    {
        validate();

        final Properties userList = new Properties();

        loadUserlist( userList );

        for( Enumeration e = m_cvsUsers.elements(); e.hasMoreElements(); )
        {
            final CvsUser user = (CvsUser)e.nextElement();
            user.validate();
            userList.put( user.getUserID(), user.getDisplayname() );
        }

        final Commandline command = new Commandline();
        command.setExecutable( "cvs" );
        command.addArgument( "log" );

        final ChangeLogParser parser = new ChangeLogParser( userList );
        final Execute exe = new Execute();
        exe.setWorkingDirectory( m_basedir );
        exe.setCommandline( command );
        exe.setExecOutputHandler( parser );
        exe.execute( getContext() );

        final CVSEntry[] entrySet = parser.getEntrySetAsArray();
        final CVSEntry[] filteredEntrySet = filterEntrySet( entrySet );
        writeChangeLog( filteredEntrySet );
    }

    /**
     * Validate the parameters specified for task.
     *
     * @throws TaskException if fails validation checks
     */
    private void validate()
        throws TaskException
    {
        if( null == m_basedir )
        {
            final String message = REZ.getString( "changelog.missing-basedir.error" );
            throw new TaskException( message );
        }
        if( null == m_destfile )
        {
            final String message = REZ.getString( "changelog.missing-destfile.error" );
            throw new TaskException( message );
        }
        if( !m_basedir.exists() )
        {
            final String message =
                REZ.getString( "changelog.bad-basedir.error", m_basedir.getAbsolutePath() );
            throw new TaskException( message );
        }
        if( null != m_usersFile && !m_usersFile.exists() )
        {
            final String message =
                REZ.getString( "changelog.bad-userlist.error", m_usersFile.getAbsolutePath() );
            throw new TaskException( message );
        }
    }

    /**
     * Load the userli4st from the userList file (if specified) and
     * add to list of users.
     *
     * @throws TaskException if file can not be loaded for some reason
     */
    private void loadUserlist( final Properties userList )
        throws TaskException
    {
        if( null != m_usersFile )
        {
            try
            {
                userList.load( new FileInputStream( m_usersFile ) );
            }
            catch( final IOException ioe )
            {
                throw new TaskException( ioe.toString(), ioe );
            }
        }
    }

    /**
     * Filter the specified entrys accoridn to an appropriate
     * rule.
     *
     * @param entrySet the entry set to filter
     * @return the filtered entry set
     */
    private CVSEntry[] filterEntrySet( final CVSEntry[] entrySet )
    {
        final ArrayList results = new ArrayList();
        for( int i = 0; i < entrySet.length; i++ )
        {
            final CVSEntry cvsEntry = entrySet[i ];
            final Date date = cvsEntry.getDate();
            if( null != m_start && m_start.after( date ) )
            {
                //Skip dates that are too early
                continue;
            }
            if( null != m_stop && m_stop.before( date ) )
            {
                //Skip dates that are too late
                continue;
            }
            results.add( cvsEntry );
        }

        return (CVSEntry[])results.toArray( new CVSEntry[results.size() ] );
    }

    /**
     * Print changelog to file specified in task.
     *
     * @throws TaskException if theres an error writing changelog
     */
    private void writeChangeLog( final CVSEntry[] entrySet )
        throws TaskException
    {
        FileOutputStream output = null;
        try
        {
            output = new FileOutputStream( m_destfile );
            final PrintWriter writer =
                new PrintWriter( new OutputStreamWriter( output, "UTF-8" ) );

            ChangeLogWriter serializer = new ChangeLogWriter();
            serializer.printChangeLog( writer, entrySet );
        }
        catch( final UnsupportedEncodingException uee )
        {
            getContext().error( uee.toString(), uee );
        }
        catch( final IOException ioe )
        {
            throw new TaskException( ioe.toString(), ioe );
        }
        finally
        {
            IOUtil.shutdownStream( output );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17374.java