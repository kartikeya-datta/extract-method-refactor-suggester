error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/632.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/632.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/632.java
text:
```scala
public v@@oid engineInit(ContextManager cm) throws TomcatException {

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
import org.apache.tomcat.util.*;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 *  Prepare a context manager - expand wars in webapps and
 *  automaticly install contexts
 *
 *  This happens _before_ Context.init()  
 * 
 * @author costin@dnt.ro
 */
public class AutoSetup extends BaseInterceptor {
    int debug=0;
    Hashtable definedContexts=new Hashtable();

    public AutoSetup() {
    }

    /** This will add all contexts to the default host.
     *	We need a mechanism ( or convention ) to configure
     *  virtual hosts too
     */
    public void engineStart(ContextManager cm) throws TomcatException {
	String home=cm.getHome();
	File webappD=new File(home + "/webapps");
	if (! webappD.exists() || ! webappD.isDirectory()) {
	    log("No webapps/ directory " + webappD );
	    return ; // nothing to set up
	}

	Enumeration en=cm.getContexts();
	while (en.hasMoreElements()){
	    Context ctx=(Context)en.nextElement();
	    if( ctx.getHost()== null ) {
		// this is a context that goes into the default server
		// we care only about the root context for autosetup
		// until we define a pattern for automatic vhost setup.
		definedContexts.put( ctx.getPath(), ctx );
		if(debug>0) log("Register explicit context " + ctx.getPath());
	    }
	}

	String[] list = webappD.list();
	if( list.length==0 ) {
	    log("No apps in webapps/ ");
	}
	for (int i = 0; i < list.length; i++) {
	    String name = list[i];
            boolean expanded = false;
	    if( name.endsWith(".war") ) {
		String fname=name.substring(0, name.length()-4);
		File appDir=new File( home + "/webapps/" + fname);
		if( ! appDir.exists() ) {
		    // no check if war file is "newer" than directory
		    // To update you need to "remove" the context first!!!
		    appDir.mkdirs();
		    // Expand war file
		    try {
			FileUtil.expand(home + "/webapps/" + name,
			       home + "/webapps/" + fname);
                        expanded=true;
		    } catch( IOException ex) {
			log("expanding webapp " + name, ex);
			// do what ?
		    }
		}
		// we will add the directory to the path
		name=fname;
	    }

	    // XXX XXX Add a .xml case
	    // If a "path.xml" file is found in webapps/, it will be loaded
	    // as a <context> fragment ( what will allow setting options
	    // for contexts or automatic config for contexts with different base)

	    // Decode path

	    // Path will be based on the War name
	    // Current code supports only one level, we
	    // need to decide an encoding scheme for multi-level
	    String path="/" + name; // decode(name)
	    //	    log("XXX : " + path );
	    if( path.equals("/ROOT") )
		    path="";

	    Context ctx = (Context)definedContexts.get(path);
	    // if context is defined and was expanded
	    if( ctx != null && expanded ) {
		// we need to reload the context since it was initialized
		// before its directories existed. At minimum, its classloader
		// needs updating.
		if ( ctx.getReloadable() ) {
		    ctx.setReload( true );
		    if( debug > 0 )
			log("setting context " + ctx.toString() + "to reload");
		} else {
		    log("Warning: predefined context " + ctx.toString() +
	" is not reloadable, recreating instead. Some settings may be lost!");
		    cm.removeContext(ctx);
		    // XXX Make sure ctx is destroyed - we may have
		    // undetected leaks 
		    ctx=null;
		}
	    }

            // if context not defined
	    if( ctx  == null ) {
		// if no explicit set up and is a directory
		File f=new File( webappD, name);
		if (f.isDirectory()) {
		    if ( ctx != null )
			cm.removeContext(ctx);
		    ctx=new Context();
		    ctx.setContextManager( cm );
		    ctx.setPath(path);
		    definedContexts.put( path, ctx );
		    // use absolute filename based on CM home instead of relative
		    // don't assume HOME==TOMCAT_HOME
		    ctx.setDocBase( f.getAbsolutePath() );
		    if( debug > 0 )
			log("automatic add " + ctx.toString() + " " + path);
		    cm.addContext(ctx);
		    ctx.init();
		} else {
		    if( debug>0)
			log("Already set up: " + path + " "
				+ definedContexts.get(path));
		}
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/632.java