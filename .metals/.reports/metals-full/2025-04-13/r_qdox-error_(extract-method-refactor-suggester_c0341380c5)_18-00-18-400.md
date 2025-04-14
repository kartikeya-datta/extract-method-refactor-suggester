error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2180.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2180.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2180.java
text:
```scala
S@@ystem.getProperties().put("tomcat.home", cm.getHome());

/*
 * ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999 The Apache Software Foundation.  All rights 
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
 * 4. The names "The Jakarta Project", "Tomcat", and "Apache Software
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
 *
 * [Additional notices, if required by prior licensing conditions]
 *
 */

package org.apache.tomcat.modules.config;

import org.apache.tomcat.core.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.security.*;

import org.apache.tomcat.util.*;
import org.apache.tomcat.util.log.*;

/**
 * Set ( and guess ) the paths to absolute ( and canonical ) directories.
 * This module must be added first ( before even ServerXmlReader ).
 * 
 * If tomcat is embeded _and_ you are sure that all paths you set
 * are OK - you may not need this ( but better to be safe and add it ).
 *
 * You don't have to insert this in server.xml - it's better to add it
 * manually, to be sure it is first.
 * 
 * ( based on DefaultCMSetter )
 *
 * @author Costin Manolache
 */
public final class PathSetter extends BaseInterceptor {
    private static StringManager sm =
	StringManager.getManager("org.apache.tomcat.resources");

    /** Default work dir, relative to home
     */
    public static final String DEFAULT_WORK_DIR="work";
    
    public PathSetter() {
    }

    /** Adjust context manager paths. This happens before anything
     * 	else. 
     */
    public void addInterceptor(ContextManager cm, Context ctx,
			       BaseInterceptor module)
	throws TomcatException
    {
	if( this != module ) return;

	// Adjust paths in CM
	String home=cm.getHome();
	if( home==null ) {
	    // try system property
	    home=System.getProperty(ContextManager.TOMCAT_HOME);
	}

	if( home==null ) {
	    home=IntrospectionUtils.guessHome( "tomcat.home",
					       "tomcat_core.jar",
				       "org/apache/tomcat/core/Request.class");
	}

	if (home == null) {
	    System.out.println(sm.getString("tomcat.nohome"));
	    home = ".";
	    // Assume current working directory
	}

	// Make it absolute
	home=FileUtil.getCanonicalPath( home );
	cm.setHome( home );
	log( "engineInit: home= " + home );
	
	String installDir=cm.getInstallDir();
	if( installDir!= null ) {
	    installDir=FileUtil.getCanonicalPath( installDir );
	    cm.setInstallDir( installDir );
	    log( "engineInit: install= " + installDir );
	}

	// if only one is set home==installDir

	if( home!=null && installDir == null ) {
	    cm.setInstallDir( home );
	    installDir=home;
	}

	if( home==null && installDir != null ) {
	    cm.setHome( installDir );
	    home=installDir;
	}

	// if neither home or install is set,
	// and no system property, try "."
	if( home==null && installDir==null ) {
	    home=FileUtil.getCanonicalPath( "." );
	    installDir=home;

	    cm.setHome( home );
	    cm.setInstallDir( home );
	}

	System.setProperty("tomcat.home", cm.getHome());
    }

    /** After server.xml is read - make sure the workDir is absolute,
     *  and all global loggers are set to absolute paths and open.
     */
    public void engineInit( ContextManager cm )
	throws TomcatException
    {
	// Adjust work dir
	String workDir=cm.getWorkDir();
	if( workDir==null ) {
	    workDir= DEFAULT_WORK_DIR;
	}

	if( ! FileUtil.isAbsolute( workDir )) {
	    workDir=FileUtil.
		getCanonicalPath(cm.getHome() + File.separator+
				 workDir);
	}
	cm.setWorkDir( workDir );
        initLoggers(cm.getLoggers());

	if(debug>1) log( "Setting: " + cm.getInstallDir() + " " +
			 cm.getHome() + " " + workDir);
    }

    private void initLoggers(Hashtable Loggers){
        if( Loggers!=null ){
            Enumeration el=Loggers.elements();
            while( el.hasMoreElements()){
                Logger l=(Logger)el.nextElement();
                String path=l.getPath();
                if( path!=null ) {
                    File f=new File( path );
                    if( ! f.isAbsolute() ) {
                        File wd= new File(cm.getHome(), f.getPath());
                        l.setPath( wd.getAbsolutePath() );
                    }
                    // create the files, ready to log.
                }
                l.open();
            }
        }
    }

    /** Adjust paths for a context - make the base and all loggers
     *  point to canonical paths.
     */
    public void addContext( ContextManager cm, Context ctx)
	throws TomcatException
    {
	// adjust context paths and loggers

	String docBase=ctx.getDocBase();
	String absPath=ctx.getAbsolutePath();
	if( absPath==null ) {
	    if (FileUtil.isAbsolute( docBase ) )
		absPath=docBase;
	    else
		absPath = cm.getHome() + File.separator + docBase;
	    try {
		absPath = new File(absPath).getCanonicalPath();
	    } catch (IOException npe) {
	    }
	    ctx.setAbsolutePath( absPath );
	}
	if( debug > 0 ) {
	    String h=ctx.getHost();
	    log( "addContext: " + ((h==null) ? "":h) + ":" +
		 ctx.getPath() + " " + docBase + " " + absPath + " " +
		 cm.getHome());
	}
	
	// this would belong to a logger interceptor ?
	Log loghelper=ctx.getLog();
	Log loghelperServlet=ctx.getServletLog();
	
	if( loghelper!=null && loghelper.getLogger() != null )
	    cm.addLogger( loghelper.getLogger() );
	if( loghelperServlet != null &&
	    loghelperServlet.getLogger() != null)
	    cm.addLogger( loghelperServlet.getLogger() );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2180.java