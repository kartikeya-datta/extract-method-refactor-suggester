error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5846.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5846.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5846.java
text:
```scala
S@@impleHashtable attributes=new SimpleHashtable();

package org.apache.tomcat.startup;

import java.beans.*;
import java.io.*;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.Hashtable;
import java.util.*;
import java.net.*;
import org.apache.tomcat.util.res.StringManager;
import org.apache.tomcat.modules.config.*;
import org.apache.tomcat.util.xml.*;
import org.apache.tomcat.core.*;
import org.apache.tomcat.util.log.*;
import org.xml.sax.*;
import org.apache.tomcat.util.collections.*;

/**
 * Starter for Tomcat using XML.
 * Based on Ant.
 *
 * @author costin@dnt.ro
 */
public class Tomcat {

    private static StringManager sm =
	StringManager.getManager("org.apache.tomcat.resources");

    private String action="start";

    String home=null;
    String args[];
    ClassLoader parentClassLoader;
    boolean sandbox=false;
    
    // null means user didn't set one
    String configFile=null;
    // relative to TOMCAT_HOME
    static final String DEFAULT_CONFIG="conf/server.xml";
    SimpleHashtable attributes=new SimpleHashtable();;
    static Log log=Log.getLog( "tc_log", "Tomcat" );
    
    public Tomcat() {
    }
    //-------------------- Properties --------------------
    
    public void setHome(String home) {
	this.home=home;
    }
    
    public void setArgs(String args[]) {
	this.args=args;
    }
    

    public void setAction(String s ) {
	action=s;
    }

    public void setSandbox( boolean b ) {
	sandbox=b;
    }
    
    public void setParentClassLoader( ClassLoader cl ) {
	parentClassLoader=cl;
    }
    // -------------------- main/execute --------------------
    
    public static void main(String args[] ) {
	try {
	    Tomcat tomcat=new Tomcat();
	    tomcat.setArgs( args );
            tomcat.execute();
	} catch(Exception ex ) {
	    log.log(sm.getString("tomcat.fatal"), ex);
	    System.exit(1);
	}
    }

    public void execute() throws Exception {
	//	String[] args=(String[])attributes.get("args");
        if ( args == null || ! processArgs( args )) {
	    setAction("help");
	}
	if( "stop".equals( action )){
	    stop();
	} else if( "enableAdmin".equals( action )){
	    enableAdmin();
	} else if( "help".equals( action )) {
	    printUsage();
	} else if( "start".equals( action )) {
	    start();
	}
    }

    // -------------------- Actions --------------------

    public void enableAdmin() throws IOException
    {
	System.out.println("Overriding apps-admin settings ");
	FileWriter fw=new FileWriter( home + File.separator +
				      "conf" + File.separator +
				      "apps-admin.xml" );
	PrintWriter pw=new PrintWriter( fw );
	pw.println( "<webapps>" );
	pw.println( "  <Context path=\"/admin\"");
	pw.println( "           docBase=\"webapps/admin\"");
	pw.println( "           trusted=\"true\">");
	pw.println( "    <SimpleRealm");
        pw.println( "      filename=\"conf/users/admin-users.xml\" />");
	pw.println( "  </Context>");
	pw.println( "</webapps>" );
	pw.close();
    }
	
    public void stop() throws Exception {
	System.out.println(sm.getString("tomcat.stop"));
	try {
	    StopTomcat task=
		new  StopTomcat();

	    task.execute();     
	}
	catch (TomcatException te) {
	    if (te.getRootCause() instanceof java.net.ConnectException)
		System.out.println(sm.getString("tomcat.connectexception"));
	    else
		throw te;
	}
	return;
    }

    public void start() throws Exception {
	EmbededTomcat tcat=new EmbededTomcat();

	PathSetter pS=new PathSetter();
	tcat.addInterceptor( pS );
	
	ServerXmlReader sxmlConf=new ServerXmlReader();
	sxmlConf.setConfig( configFile );
	tcat.addInterceptor( sxmlConf );
        ClassLoader cl=parentClassLoader;

        if (cl==null) cl=this.getClass().getClassLoader();

        tcat.getContextManager().setParentLoader(cl);
	if( sandbox )
	    tcat.getContextManager().setProperty( "sandbox", "true");
	tcat.initContextManager();

	tcat.start();
    }

    // -------------------- Command-line args processing --------------------


    public static void printUsage() {
	//System.out.println(sm.getString("tomcat.usage"));
	System.out.println("Usage: java org.apache.tomcat.startup.Tomcat {options}");
	System.out.println("  Options are:");
	System.out.println("    -config file (or -f file)  Use this fileinstead of server.xml");
	System.out.println("    -help (or help)            Show this usage report");
	System.out.println("    -home dir (or -h dir)      Use this directory as tomcat.home");
	System.out.println("    -stop                      Shut down currently running Tomcat");
    }

    /** Process arguments - set object properties from the list of args.
     */
    public  boolean processArgs(String[] args) {
	for (int i = 0; i < args.length; i++) {
	    String arg = args[i];

	    if (arg.equals("-help") || arg.equals("help")) {
		action="help";
		return false;
	    } else if (arg.equals("-stop")) {
		action="stop";
	    } else if (arg.equals("-sandbox")) {
		sandbox=true;
	    } else if (arg.equals("-security")) {
		sandbox=true;
	    } else if (arg.equals("-enableAdmin")) {
		action="enableAdmin";
	    } else if (arg.equals("-g") || arg.equals("-generateConfigs")) {
		// config generation is now a module. //doGenerate=true;
	    } else if (arg.equals("-f") || arg.equals("-config")) {
		i++;
		if( i < args.length )
		    configFile = args[i];
		else
		    return false;
	    } else if (arg.equals("-h") || arg.equals("-home")) {
		i++;
		if (i < args.length)
		    System.getProperties().put("tomcat.home", args[i]);
		else
		    return false;
	    }
	}
	return true;
    }

    // Hack for Main.java, will be replaced with calling the setters directly
    public void setAttribute(String s,Object o) {
	if( "home".equals( s ) )
	    setHome( (String)o);
	else if("args".equals( s ) ) 
	    setArgs((String[])o);
	else if( "parentClassLoader".equals( s ) ) {
	    setParentClassLoader((ClassLoader)o);
	} else {
	    System.out.println("Tomcat: setAttribute " + s + "=" + o);
	    attributes.put(s,o);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5846.java