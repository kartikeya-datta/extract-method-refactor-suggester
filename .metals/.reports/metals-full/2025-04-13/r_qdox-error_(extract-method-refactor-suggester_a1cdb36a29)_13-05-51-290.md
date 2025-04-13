error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1583.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1583.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1583.java
text:
```scala
v@@.addElement("FULL DOC BASE: " + context.getAbsolutePath());

import java.util.Vector;
import java.util.Enumeration;
import java.io.File;
import java.net.URL;
import javax.servlet.http.*;

import org.apache.tomcat.core.Request;
import org.apache.tomcat.core.FacadeManager;
import org.apache.tomcat.core.Context;
import org.apache.tomcat.core.ContextManager;
import org.apache.tomcat.util.RequestUtil;

/**
 * A context administration class. Contexts can be
 * viewed, added, and removed from the context manager.
 *
 */

public class ContextAdmin {
    private ContextManager cm;
    private Request realRequest;

    private String submit = null;
    private String addContextPath = null;
    private String addContextDocBase = null;
    private String removeContextName = null;

    public void setSubmit(String s) {
	submit = s;
    }

    public void setAddContextPath(String s) {
	addContextPath = s;
    }

    public void setAddContextDocBase(String s) {
	addContextDocBase = s;
    }

    public void setRemoveContextName(String s) {
	removeContextName = s;
    }

    public void init(HttpServletRequest request) {
	FacadeManager facadeM=(FacadeManager)request.getAttribute( FacadeManager.FACADE_ATTRIBUTE);
	realRequest = facadeM.getRealRequest(request);
	cm = realRequest.getContext().getContextManager();
    }

    public Enumeration getContextNames() {
        return (Enumeration) cm.getContextNames();
    }

    public String[] getContextInfo(String contextName) {
	Enumeration enum;
	String key;
        Context context;
        Vector v = new Vector();


	context = cm.getContext(contextName);

	v.addElement("DOC BASE: " + context.getDocBase());
	v.addElement("FULL DOC BASE: " + context.getDocumentBase().toString());
	v.addElement("PATH: " + context.getPath());
	if (context.getWorkDir() != null)
	   v.addElement("WORK DIR: " + RequestUtil.URLDecode(context.getWorkDir().getName()));

	v.addElement("DESCRIPTION: " + context.getDescription());
	v.addElement("SESSION TIMEOUT: " + new Integer(context.getSessionTimeOut()).toString());

        enum = context.getInitParameterNames();
	while (enum.hasMoreElements()) {
	    key = (String)enum.nextElement();
	    v.addElement("INIT PARAMETER NAME: " + key);
	    v.addElement("INIT PARAMETER: " + context.getInitParameter(key));
	}

        enum = context.getAttributeNames();
	while (enum.hasMoreElements()) {
	    key = (String)enum.nextElement();
	    v.addElement("ATTRIBUTE NAME: " + key);
	    v.addElement("ATTRIBUTE: " + RequestUtil.URLDecode(context.getAttribute(key).toString()));
	}

	v.addElement("SERVER INFO: " + context.getEngineHeader());

	String[] s = new String[v.size()];
	v.copyInto(s);
	return s;
    }

    public String addContext() {
	if ((addContextPath != null) && (addContextDocBase != null)) {
            Context context = new Context();
            context.setContextManager(cm);
            context.setPath(addContextPath);
            context.setDocBase(addContextDocBase);

	    try {
                cm.addContext(context);
                cm.initContext(context);
	    }
	    catch(org.apache.tomcat.core.TomcatException ex) {
	        ex.printStackTrace();
	    }
	    return ("Added New Context: " + addContextPath);
	}
	else return ("ERROR: Null Context Values");
    }

    public String removeContext() {
        if (removeContextName != null) {
            Enumeration enum = cm.getContextNames();
            while (enum.hasMoreElements()) {
	        String name = (String)enum.nextElement();
		if (removeContextName.equals(name)) {
	            try {
		        cm.removeContext(removeContextName);
		    }
	            catch(org.apache.tomcat.core.TomcatException ex) {
	                ex.printStackTrace();
	            }
		    return("Context Name: " + removeContextName + " Removed");
		}
	    }
	    return("Context Name: " + removeContextName + " Not Found");
	}
	else return("ERROR: Null Context Name");

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1583.java