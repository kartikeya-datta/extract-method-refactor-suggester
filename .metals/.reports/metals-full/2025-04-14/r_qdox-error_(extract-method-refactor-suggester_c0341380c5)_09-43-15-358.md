error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14237.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14237.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14237.java
text:
```scala
e@@xe.setCommandline( cmd );

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.antlib.security;

import java.io.IOException;
import org.apache.myrmidon.api.AbstractTask;
import org.apache.myrmidon.api.TaskException;
import org.apache.tools.ant.taskdefs.exec.Execute2;
import org.apache.tools.ant.types.Commandline;

/**
 * Generates a key.
 *
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 */
public class GenerateKey
    extends AbstractTask
{
    /**
     * The alias of signer.
     */
    private String m_alias;
    private String m_dname;
    private DistinguishedName m_expandedDname;
    private String m_keyalg;
    private String m_keypass;
    private int m_keysize;

    /**
     * The name of keystore file.
     */
    private String m_keystore;

    private String m_sigalg;
    private String m_storepass;
    private String m_storetype;
    private int m_validity;
    private boolean m_verbose;

    public void setAlias( final String alias )
    {
        m_alias = alias;
    }

    public void setDname( final String dname )
        throws TaskException
    {
        m_dname = dname;
    }

    public void setKeyalg( final String keyalg )
    {
        m_keyalg = keyalg;
    }

    public void setKeypass( final String keypass )
    {
        m_keypass = keypass;
    }

    public void setKeysize( final int keysize )
    {
        m_keysize = keysize;
    }

    public void setKeystore( final String keystore )
    {
        m_keystore = keystore;
    }

    public void setSigalg( final String sigalg )
    {
        m_sigalg = sigalg;
    }

    public void setStorepass( final String storepass )
    {
        m_storepass = storepass;
    }

    public void setStoretype( final String storetype )
    {
        m_storetype = storetype;
    }

    public void setValidity( final int validity )
        throws TaskException
    {
        m_validity = validity;
    }

    public void setVerbose( final boolean verbose )
    {
        m_verbose = verbose;
    }

    public void addDname( final DistinguishedName distinguishedName )
        throws TaskException
    {
        if( null != m_expandedDname )
        {
            final String message = "DName sub-element can only be specified once.";
            throw new TaskException( message );
        }
        m_expandedDname = distinguishedName;
    }

    public void execute()
        throws TaskException
    {
        validate();

        final String message = "Generating Key for " + m_alias;
        getLogger().info( message );

        final Commandline cmd = createCommand();
        final Execute2 exe = new Execute2();
        exe.setWorkingDirectory( getBaseDirectory() );
        exe.setCommandline( cmd.getCommandline() );
        try
        {
            exe.execute();
        }
        catch( final IOException ioe )
        {
            throw new TaskException( ioe.getMessage(), ioe );
        }
    }

    private Commandline createCommand()
    {
        final Commandline cmd = new Commandline();
        cmd.setExecutable( "keytool" );

        cmd.addArgument( "-genkey " );

        if( m_verbose )
        {
            cmd.addArgument( "-v " );
        }

        cmd.addArgument( "-alias" );
        cmd.addArgument( m_alias );

        if( null != m_dname )
        {
            cmd.addArgument( "-dname" );
            cmd.addArgument( m_dname );
        }

        if( null != m_expandedDname )
        {
            cmd.addArgument( "-dname" );
            cmd.addArgument( m_expandedDname.toString() );
        }

        if( null != m_keystore )
        {
            cmd.addArgument( "-keystore" );
            cmd.addArgument( m_keystore );
        }

        if( null != m_storepass )
        {
            cmd.addArgument( "-storepass" );
            cmd.addArgument( m_storepass );
        }

        if( null != m_storetype )
        {
            cmd.addArgument( "-storetype" );
            cmd.addArgument( m_storetype );
        }

        cmd.addArgument( "-keypass" );
        if( null != m_keypass )
        {
            cmd.addArgument( m_keypass );
        }
        else
        {
            cmd.addArgument( m_storepass );
        }

        if( null != m_sigalg )
        {
            cmd.addArgument( "-sigalg" );
            cmd.addArgument( m_sigalg );
        }

        if( null != m_keyalg )
        {
            cmd.addArgument( "-keyalg" );
            cmd.addArgument( m_keyalg );
        }

        if( 0 < m_keysize )
        {
            cmd.addArgument( "-keysize" );
            cmd.addArgument( "" + m_keysize );
        }

        if( 0 < m_validity )
        {
            cmd.addArgument( "-validity" );
            cmd.addArgument( "" + m_validity );
        }
        return cmd;
    }

    private void validate()
        throws TaskException
    {
        if( null == m_alias )
        {
            final String message = "alias attribute must be set";
            throw new TaskException( message );
        }

        if( null == m_storepass )
        {
            final String message = "storepass attribute must be set";
            throw new TaskException( message );
        }

        if( null == m_dname && null == m_expandedDname )
        {
            final String message = "dname must be set";
            throw new TaskException( message );
        }
        else if( null != m_expandedDname && null != m_dname )
        {
            final String message = "It is not possible to specify dname both " +
                "as attribute and element.";
            throw new TaskException( message );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14237.java