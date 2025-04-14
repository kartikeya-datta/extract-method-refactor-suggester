error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7693.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7693.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7693.java
text:
```scala
t@@ask.log( "Could not find class mapping for " + filepath, Project.MSG_WARN );

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE file.
 */
package org.apache.tools.ant.taskdefs.optional.metamata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.ExecuteStreamHandler;
import org.apache.tools.ant.util.DOMElementWriter;
import org.apache.tools.ant.util.regexp.RegexpMatcher;
import org.apache.tools.ant.util.regexp.RegexpMatcherFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * This is a very bad stream handler for the MAudit task. All report to stdout
 * that does not match a specific report pattern is dumped to the Ant output as
 * warn level. The report that match the pattern is stored in a map with the key
 * being the filepath that caused the error report. <p>
 *
 * The limitation with the choosen implementation is clear:
 * <ul>
 *   <li> it does not handle multiline report( message that has \n ). the part
 *   until the \n will be stored and the other part (which will not match the
 *   pattern) will go to Ant output in Warn level.
 *   <li> it does not report error that goes to stderr.
 * </ul>
 *
 *
 * @author <a href="sbailliez@imediation.com">Stephane Bailliez</a>
 */
class MAuditStreamHandler implements ExecuteStreamHandler
{

    /**
     * this is where the XML output will go, should mostly be a file the caller
     * is responsible for flushing and closing this stream
     */
    protected OutputStream xmlOut = null;

    /**
     * the multimap. The key in the map is the filepath that caused the audit
     * error and the value is a vector of MAudit.Violation entries.
     */
    protected Hashtable auditedFiles = new Hashtable();

    /**
     * reader for stdout
     */
    protected BufferedReader br;

    /**
     * matcher that will be used to extract the info from the line
     */
    protected RegexpMatcher matcher;

    protected MAudit task;

    MAuditStreamHandler( MAudit task, OutputStream xmlOut )
    {
        this.task = task;
        this.xmlOut = xmlOut;
        /**
         * the matcher should be the Oro one. I don't know about the other one
         */
        matcher = ( new RegexpMatcherFactory() ).newRegexpMatcher();
        matcher.setPattern( MAudit.AUDIT_PATTERN );
    }

    protected static DocumentBuilder getDocumentBuilder()
    {
        try
        {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder();
        }
        catch( Exception exc )
        {
            throw new ExceptionInInitializerError( exc );
        }
    }

    /**
     * Ignore.
     *
     * @param is The new ProcessErrorStream value
     */
    public void setProcessErrorStream( InputStream is )
    {
    }

    /**
     * Ignore.
     *
     * @param os The new ProcessInputStream value
     */
    public void setProcessInputStream( OutputStream os )
    {
    }

    /**
     * Set the inputstream
     *
     * @param is The new ProcessOutputStream value
     * @exception IOException Description of Exception
     */
    public void setProcessOutputStream( InputStream is )
        throws IOException
    {
        br = new BufferedReader( new InputStreamReader( is ) );
    }

    /**
     * Invokes parseOutput. This will block until the end :-(
     *
     * @exception IOException Description of Exception
     */
    public void start()
        throws IOException
    {
        parseOutput( br );
    }

    /**
     * Pretty dangerous business here. It serializes what was extracted from the
     * MAudit output and write it to the output.
     */
    public void stop()
    {
        // serialize the content as XML, move this to another method
        // this is the only code that could be needed to be overrided
        Document doc = getDocumentBuilder().newDocument();
        Element rootElement = doc.createElement( "classes" );
        Enumeration keys = auditedFiles.keys();
        Hashtable filemapping = task.getFileMapping();
        rootElement.setAttribute( "audited", String.valueOf( filemapping.size() ) );
        rootElement.setAttribute( "reported", String.valueOf( auditedFiles.size() ) );
        int errors = 0;
        while( keys.hasMoreElements() )
        {
            String filepath = (String)keys.nextElement();
            Vector v = (Vector)auditedFiles.get( filepath );
            String fullclassname = (String)filemapping.get( filepath );
            if( fullclassname == null )
            {
                task.getProject().log( "Could not find class mapping for " + filepath, Project.MSG_WARN );
                continue;
            }
            int pos = fullclassname.lastIndexOf( '.' );
            String pkg = ( pos == -1 ) ? "" : fullclassname.substring( 0, pos );
            String clazzname = ( pos == -1 ) ? fullclassname : fullclassname.substring( pos + 1 );
            Element clazz = doc.createElement( "class" );
            clazz.setAttribute( "package", pkg );
            clazz.setAttribute( "name", clazzname );
            clazz.setAttribute( "violations", String.valueOf( v.size() ) );
            errors += v.size();
            for( int i = 0; i < v.size(); i++ )
            {
                MAudit.Violation violation = (MAudit.Violation)v.elementAt( i );
                Element error = doc.createElement( "violation" );
                error.setAttribute( "line", String.valueOf( violation.line ) );
                error.setAttribute( "message", violation.error );
                clazz.appendChild( error );
            }
            rootElement.appendChild( clazz );
        }
        rootElement.setAttribute( "violations", String.valueOf( errors ) );

        // now write it to the outputstream, not very nice code
        if( xmlOut != null )
        {
            Writer wri = null;
            try
            {
                wri = new OutputStreamWriter( xmlOut, "UTF-8" );
                wri.write( "<?xml version=\"1.0\"?>\n" );
                ( new DOMElementWriter() ).write( rootElement, wri, 0, "  " );
                wri.flush();
            }
            catch( IOException exc )
            {
                task.log( "Unable to write log file", Project.MSG_ERR );
            }
            finally
            {
                if( xmlOut != System.out && xmlOut != System.err )
                {
                    if( wri != null )
                    {
                        try
                        {
                            wri.close();
                        }
                        catch( IOException e )
                        {
                        }
                    }
                }
            }
        }

    }

    /**
     * add a violation entry for the file
     *
     * @param file The feature to be added to the ViolationEntry attribute
     * @param entry The feature to be added to the ViolationEntry attribute
     */
    protected void addViolationEntry( String file, MAudit.Violation entry )
    {
        Vector violations = (Vector)auditedFiles.get( file );
        // if there is no decl for this file yet, create it.
        if( violations == null )
        {
            violations = new Vector();
            auditedFiles.put( file, violations );
        }
        violations.add( entry );
    }

    /**
     * read each line and process it
     *
     * @param br Description of Parameter
     * @exception IOException Description of Exception
     */
    protected void parseOutput( BufferedReader br )
        throws IOException
    {
        String line = null;
        while( ( line = br.readLine() ) != null )
        {
            processLine( line );
        }
    }

    // we suppose here that there is only one report / line.
    // There will obviouslly be a problem if the message is on several lines...
    protected void processLine( String line )
    {
        Vector matches = matcher.getGroups( line );
        if( matches != null )
        {
            String file = (String)matches.elementAt( 1 );
            int lineNum = Integer.parseInt( (String)matches.elementAt( 2 ) );
            String msg = (String)matches.elementAt( 3 );
            addViolationEntry( file, MAudit.createViolation( lineNum, msg ) );
        }
        else
        {
            // this doesn't match..report it as info, it could be
            // either the copyright, summary or a multiline message (damn !)
            task.log( line, Project.MSG_INFO );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7693.java