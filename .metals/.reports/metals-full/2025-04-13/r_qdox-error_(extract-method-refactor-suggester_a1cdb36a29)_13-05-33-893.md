error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4611.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4611.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4611.java
text:
```scala
t@@argets.removeAllElements();

package tadm;
import java.util.*;
import java.io.*;
import java.net.URL;
import javax.servlet.http.*;
import javax.servlet.*;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import org.apache.tools.ant.*;

/**
 * This tag will run ant tasks
 * 
 */
public class AntTag extends TagSupport {
    
    public AntTag() {}

    public int doStartTag() throws JspException {
	try {
            args.clear();
            targets.clear();
	    pageContext.setAttribute("antProperties",
				     args);
	} catch (Exception ex ) {
	    ex.printStackTrace();
	}
	return EVAL_BODY_INCLUDE;
    }

    public int doEndTag() throws JspException {
	runTest();
	return EVAL_PAGE;
    }

    // -------------------- child tag support --------------------
    Properties args=new Properties();
    Vector targets=new Vector();
    
    public void setProperty( String name, String value ) {
	System.out.println("Adding property " + name + "=" + value );
	args.put(name, value );
    }

    public String getProperty( String name ) {
	System.out.println("Getting property " + name  );
	return args.getProperty(name );
    }

    public void addTarget( String n ) {
	System.out.println("Adding target " + n );
	targets.addElement( n );
    }
    
    //-------------------- Properties --------------------

    /** Set the name of the test.xml, relative to the base dir.
     *  For example, /WEB-INF/test-tomcat.xml
     */
    public void setTestFile( String s ) {
	args.put("ant.file", s);
    }

    /** Set the target - a subset of tests to be run
     */
    public void setTarget( String s ) {
	addTarget(s);
    }

    public void setDebug( String s ) {
	args.put( "debug", s);
    }

    // -------------------- Implementation methods --------------------
    
    private void runTest() throws JspException {
	PrintWriter out=null;
	try {
	    out=pageContext.getResponse().getWriter();
	    pageContext.getOut().flush();
	    out.flush(); // we need a writer for ant
	    
	    Project project=new Project();
	    
	    AntServletLogger log=new AntServletLogger();
	    log.setWriter( out );
	    project.addBuildListener( log );
	    
	    project.init();

	    Enumeration argsE=args.propertyNames();
	    while( argsE.hasMoreElements() ) {
		String k=(String)argsE.nextElement();
		String v=args.getProperty( k );
		if( k!=null && v!= null )
		    project.setUserProperty( k, v );
	    }

	    String antFileN=args.getProperty("ant.file");
	    if( antFileN==null )
		throw new JspException( "ant.file not specified");
	    File antF=new File(antFileN);
	    ProjectHelper.configureProject( project,
					   antF );

	    // pre-execution properties
	    Hashtable antProperties=project.getProperties();
	    argsE=antProperties.keys();
	    while( argsE.hasMoreElements() ) {
		String k=(String)argsE.nextElement();
		String v=(String)antProperties.get( k );
		if( k!=null && v!= null )
		    args.put( k, v ); // includes "revision"
	    }
	    
	    if( targets.size()==0 ) {
		//targets.addElement("client");
	    }

	    project.executeTargets( targets );

	    // post-execution properties
	    antProperties=project.getProperties();

	    argsE=antProperties.keys();
	    while( argsE.hasMoreElements() ) {
		String k=(String)argsE.nextElement();
		String v=(String)antProperties.get( k );
		if( k!=null && v!= null )
		    args.put( k, v ); 
	    }
	    
	} catch( BuildException ex ) {
	    if( out==null ) out=new PrintWriter(System.out);
	    ex.printStackTrace(out);
	    Throwable ex1=ex.getException();
	    out.println("Root cause: " );
	    if( ex1!=null)
		ex1.printStackTrace(out);
	    out.flush();
	    throw new JspException( ex.toString() );
	} catch( IOException ioex ) {
	    if( out==null ) out=new PrintWriter(System.out);
	    ioex.printStackTrace(out);
	    throw new JspException( ioex.toString() );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4611.java