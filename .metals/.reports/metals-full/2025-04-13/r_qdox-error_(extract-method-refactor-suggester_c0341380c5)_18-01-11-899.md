error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3662.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3662.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3662.java
text:
```scala
L@@og loghelper = Log.getLog("tc_log", this);

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
import org.apache.tomcat.util.io.FileUtil;
import org.apache.tomcat.util.log.*;
import java.io.*;
import java.net.*;
import java.util.*;


/**
 * Used by ContextManager to generate automatic Netscape configurations
 *
 * @author Gal Shachor shachor@il.ibm.com
 */
public class NSConfig  extends BaseInterceptor { 

    public static final String WORKERS_CONFIG = "/conf/jk/workers.properties";
    public static final String NS_CONFIG = "/conf/jk/obj.conf";
    public static final String JK_LOG_LOCATION = "/logs/netscape_redirect.log";

    Log loghelper = new Log("tc_log", this);
    
    public NSConfig() 
    {
    }

    public void engineInit(ContextManager cm) throws TomcatException
    {
	execute( cm );
    }
    
    public void execute(ContextManager cm) throws TomcatException 
    {
	    try {
	        String tomcatHome = cm.getHome();

            PrintWriter objfile = new PrintWriter(new FileWriter(tomcatHome + NS_CONFIG + "-auto"));
           
            objfile.println("###################################################################");		    
            objfile.println("# Auto generated configuration. Dated: " +  new Date());
            objfile.println("###################################################################");		    
            objfile.println();

            objfile.println("#");        
            objfile.println("# You will need to merge the content of this file with your ");
            objfile.println("# regular obj.conf and then restart (=stop + start) your Netscape server. ");
            objfile.println("#");        
            objfile.println();
            
            objfile.println("#");                    
            objfile.println("# Loading the redirector into your server");
            objfile.println("#");        
            objfile.println();            
            objfile.println("Init fn=\"load-modules\" funcs=\"jk_init,jk_service\" shlib=\"<put full path to the redirector here>\"");
            objfile.println("Init fn=\"jk_init\" worker_file=\"" + 
                            new File(tomcatHome, WORKERS_CONFIG).toString().replace('\\', '/') +  
                            "\" log_level=\"debug\" log_file=\"" + 
                            new File(tomcatHome, JK_LOG_LOCATION).toString().replace('\\', '/') + 
                            "\"");
            objfile.println();
            
            objfile.println("<Object name=default>");            
            objfile.println("#");                    
            objfile.println("# Redirecting the root context requests to tomcat.");
            objfile.println("#");        
            objfile.println("NameTrans fn=\"assign-name\" from=\"/servlet/*\" name=\"servlet\""); 
            objfile.println("NameTrans fn=\"assign-name\" from=\"/*.jsp\" name=\"servlet\""); 
            objfile.println();

	        // Set up contexts
	        // XXX deal with Virtual host configuration !!!!
	        Enumeration enum = cm.getContexts();
	        while (enum.hasMoreElements()) {
		        Context context = (Context)enum.nextElement();
		        String path  = context.getPath();
		        String vhost = context.getHost();

		        if(vhost != null) {
		            // Vhosts are not supported yet for IIS
		            continue;
		        }
		        if(path.length() > 1) {            
		            // Calculate the absolute path of the document base
		            String docBase = context.getDocBase();
		            if (!FileUtil.isAbsolute(docBase))
			        docBase = tomcatHome + "/" + docBase;
		            docBase = FileUtil.patch(docBase).replace('\\', '/');
		            
                    // Static files will be served by Apache
                    objfile.println("#########################################################");		    
                    objfile.println("# Auto configuration for the " + path + " context starts.");
                    objfile.println("#########################################################");		    
                    objfile.println();
            
                    objfile.println("#");		    
                    objfile.println("# The following line mounts all JSP file and the /servlet/ uri to tomcat");
                    objfile.println("#");                        
                    objfile.println("NameTrans fn=\"assign-name\" from=\"" + path + "/servlet/*\" name=\"servlet\""); 
                    objfile.println("NameTrans fn=\"assign-name\" from=\"" + path + "/*.jsp\" name=\"servlet\""); 
                    objfile.println("NameTrans fn=pfx2dir from=\"" + path + "\" dir=\"" + docBase + "\"");
                    objfile.println();            
                    objfile.println("#######################################################");		    
                    objfile.println("# Auto configuration for the " + path + " context ends.");
                    objfile.println("#######################################################");		    
                    objfile.println();
		        }
	        }

            objfile.println("#######################################################");		    
            objfile.println("# Protecting the web inf directory.");
            objfile.println("#######################################################");		    
            objfile.println("PathCheck fn=\"deny-existence\" path=\"*/WEB-INF/*\""); 
            objfile.println();
            
            objfile.println("</Object>");            
            objfile.println();
            
            
            objfile.println("#######################################################");		    
            objfile.println("# New object to execute your servlet requests.");
            objfile.println("#######################################################");		    
            objfile.println("<Object name=servlet>");
            objfile.println("ObjectType fn=force-type type=text/html");
            objfile.println("Service fn=\"jk_service\" worker=\"ajp12\" path=\"/*\"");
            objfile.println("</Object>");
            objfile.println();

	        
	        objfile.close();	        
	    } catch(Exception ex) {
	        loghelper.log("Error generating automatic Netscape configuration", ex);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3662.java