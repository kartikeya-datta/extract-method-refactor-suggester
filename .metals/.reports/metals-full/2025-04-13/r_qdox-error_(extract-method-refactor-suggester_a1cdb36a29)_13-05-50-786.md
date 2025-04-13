error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3121.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3121.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,10]

error in qdox parser
file content:
```java
offset: 10
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3121.java
text:
```scala
@version $@@Revision: 1.2 $

package org.jboss.web.tomcat.tc4;

import java.util.Stack;

import org.xml.sax.AttributeList;
import org.xml.sax.Attributes;

import org.apache.catalina.Engine;
import org.apache.catalina.startup.Embedded;
import org.apache.commons.digester.Rule;
import org.apache.commons.digester.Digester;

/** This action creates an Engine and sets the Realm and Logger from the
 Embedded object on the stack.

 TOMCAT 4.1.12 UPDATE: Extends org.apache.jakarta.commons.Rule

@author Scott.Stark@jboss.org
@version $Revision: 1.1 $
 */
public class EngineCreateAction extends Rule
{
   String className;
   String attrName;
   
   /**
    * Create an object of the specified class name.
    *
    * @param classN Fully qualified name of the Java class to instantiate
    */
   public EngineCreateAction(String className)
   {
      this.className = className;
   }
   
   /**
    * Create an object of the specified default class name, unless an
    * attribute with the specified name is present, in which case the value
    * of this attribute overrides the default class name.
    *
    * @param classN Fully qualified name of the Java class to instantiate
    *  if the specified attribute name is not present
    * @param attrib Name of the attribute that may contain a fully qualified
    *  name of a Java class that overrides the default
    */
   public EngineCreateAction(String className, String attrName)
   {
      this.className = className;
      this.attrName = attrName;
   }

    public void begin(Attributes attributes) throws Exception {
        String classN = className;
        Object service = digester.pop();
        Embedded catalina = (Embedded) digester.peek();
        digester.push(service);
        if( attrName != null ) {
           if (attributes.getValue(attrName) != null)
              classN = attributes.getValue(attrName);
        }
        Class c = Class.forName( classN );
        Engine e = (Engine) c.newInstance();
        e.setLogger(catalina.getLogger());
        e.setRealm(catalina.getRealm());
        digester.push(e);
    }

    public void end() throws Exception {
        digester.pop();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3121.java