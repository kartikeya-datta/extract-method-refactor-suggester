error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6601.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6601.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[23,17]

error in qdox parser
file content:
```java
offset: 572
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6601.java
text:
```scala
import org.apache.tomcat.helper.RequestUtil;

package tadm;
import java.util.Vector;
import java.util.Enumeration;
import java.io.File;
import java.net.URL;
import javax.servlet.http.*;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import org.apache.tomcat.core.Request;
import org.apache.tomcat.core.FacadeManager;
import org.apache.tomcat.core.Context;
import org.apache.tomcat.core.ContextManager;
import org.apache.tomcat.util.RequestUtil;

/**
 *
 */
public class TomcatIterate extends BodyTagSupport {
    PageContext pageContext;
    Enumeration e@@num;
    Object array[];
    int pos=0;
    String name;
    String type;
    
    public TomcatIterate() {}

    public void setEnumeration( Enumeration e ) {
	enum=e;
    }

    public void setArray( Object array[] ) {
	this.array=array;
	pos=0;
    }

    public void setName( String n ) {
	name=n;
    }

    public String getName() {
	return name;
    }

    public String getType() {
	return type;
    }
    
    public void setType( String type ) {
	this.type=type;
    }
    
    public int doStartTag() throws JspException {
	if( enum == null && array == null ) 
	    return SKIP_BODY;
	if( enum !=null ) {
	    if( ! enum.hasMoreElements() )
		return SKIP_BODY;
	    pageContext.setAttribute( name , enum.nextElement(),
				      PageContext.PAGE_SCOPE );
	    return EVAL_BODY_TAG;
	}
	if( array != null ) {
	    if( array.length==0 )
		return SKIP_BODY;
	    pageContext.setAttribute( name , array[ pos ],
				      PageContext.PAGE_SCOPE );
	    pos++;
	    return EVAL_BODY_TAG;
	}
	return SKIP_BODY;
    }

    public int doAfterBody() throws JspException {
	if( enum!=null )
	    if( enum.hasMoreElements() ) {
		pageContext.setAttribute( name , enum.nextElement(),
					  PageContext.PAGE_SCOPE );
		return EVAL_BODY_TAG;
	}
	if( array!=null ) {
	    if( pos<array.length ) {
		pageContext.setAttribute( name , array[pos++],
					  PageContext.PAGE_SCOPE );
		return EVAL_BODY_TAG;

	    }
	}
	return SKIP_BODY;
    }

    public int doEndTag() throws JspException {
	try {
	    if( bodyContent != null )
		bodyContent.writeOut( bodyContent.getEnclosingWriter());
	} catch (Exception ex ) {
	    ex.printStackTrace();
	}
	return EVAL_PAGE;
    }
    
    public void setPageContext(PageContext pctx ) {
	this.pageContext=pctx;
    }

    public void setParent( Tag parent ) {
	super.setParent( parent);
    }

    public void release() {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6601.java