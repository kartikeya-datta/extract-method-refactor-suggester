error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5547.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5547.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,16]

error in qdox parser
file content:
```java
offset: 16
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5547.java
text:
```scala
private static i@@nt dL=0;

package org.apache.tomcat.startup;

import java.beans.*;
import java.io.*;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.Hashtable;
import java.util.*;
import java.net.*;
import org.apache.tomcat.util.res.StringManager;
import org.apache.tomcat.util.xml.*;
import org.apache.tomcat.util.compat.*;
import org.apache.tomcat.util.log.*;
import org.xml.sax.*;
import org.apache.tomcat.util.collections.*;
import org.apache.tomcat.util.IntrospectionUtils;

/**
 * 
 * @author Costin Manolache
 */
public class Jspc {

    Hashtable attributes=new Hashtable();
    String args[];
    String installDir;
    ClassLoader parentL;

    public Jspc() {
    }
    
    //-------------------- Properties --------------------

    public void setArgs( String args[]) {
	this.args=args;
    }

    public void setInstall( String s ) {
	installDir=s;
    }
    
    // -------------------- execute --------------------
    static Jdk11Compat jdk11Compat=Jdk11Compat.getJdkCompat();
    
    public void execute() throws Exception
    {
	if( args!=null )
	    processArgs( args );
	Vector v=new Vector();
	String commonDir=installDir + File.separator + "lib" +
	    File.separator + "common";
	IntrospectionUtils.addToClassPath( v, commonDir);
	IntrospectionUtils.addToolsJar(v);
	String containerDir=installDir + File.separator + "lib" +
	    File.separator + "container";
	IntrospectionUtils.addToClassPath( v, containerDir);
	String appsDir=installDir + File.separator + "lib" +
	    File.separator + "apps";
	IntrospectionUtils.addToClassPath( v, appsDir);
	URL commonCP[]=
	    IntrospectionUtils.getClassPath( v );
	ClassLoader commonCL=
	    jdk11Compat.newClassLoaderInstance(commonCP, parentL);

	Class jspcClass=commonCL.loadClass( "org.apache.jasper.JspC");
	IntrospectionUtils.callMain( jspcClass, args );
    }
	
    // -------------------- Command-line args processing --------------------

    /** Process arguments - set object properties from the list of args.
     */
    public  boolean processArgs(String[] args) {
	try {
	    if( args.length > 0  && "jspc".equalsIgnoreCase( args[0])) {
		String args1[]=new String[args.length-1];
		System.arraycopy( args,1, args1, 0, args.length-1);
		args=args1;
	    }
	    setArgs(args);	    
	    // return IntrospectionUtils.processArgs( this, args,getOptions1(),
	    // 		   null, getOptionAliases());
	} catch( Exception ex ) {
	    ex.printStackTrace();
	}
	return false;
    }

    /** Callback from argument processing
     */
    public void setProperty(String s,Object v) {
	if ( dL > 0 ) debug( "Generic property " + s );
	attributes.put(s,v);
    }

    /** Called by Main to set non-string properties
     */
    public void setAttribute(String s,Object o) {
	if( "install".equals( s ) ) {
	    setInstall( (String)o);
	}
	
        if ( "args".equals(s) ) {
	    args=(String[])o;
	}
        if ( "parentClassLoader".equals(s) ) {
	    parentL=(ClassLoader)o;
	}


	attributes.put(s,o);
    }

    // -------------------- Main --------------------

    public static void main(String args[] ) {
	try {
	    Jspc task=new Jspc();
	    task.setArgs( args );
            task.execute();
	} catch(Exception ex ) {
	    ex.printStackTrace();
	    System.exit(1);
	}
    }

    private static int dL=10;
    private void debug( String s ) {
	System.out.println("Jspc: " + s );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5547.java