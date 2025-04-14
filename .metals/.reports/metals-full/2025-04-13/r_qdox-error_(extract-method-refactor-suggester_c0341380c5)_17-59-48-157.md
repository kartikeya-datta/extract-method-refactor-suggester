error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6443.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6443.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6443.java
text:
```scala
R@@ender a {@link javax.jms.Message}.

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software
 * License version 1.1, a copy of which has been included with this
 * distribution in the LICENSE.txt file.  */

package org.apache.log4j.or.jms;

import org.apache.log4j.Layout;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.or.ObjectRenderer;

import javax.jms.Message;
import javax.jms.JMSException;
import javax.jms.DeliveryMode;
import java.util.Enumeration;

/**
   Render <code>javax.jms.Message</code> objects.

   @author Ceki G&uuml;lc&uuml;
   @since 1.0 */
public class MessageRenderer implements ObjectRenderer {

  public
  MessageRenderer() {
  }

   
  /**
     Render a {@link Message}.
  */
  public
  String  doRender(Object o) {
    if(o instanceof Message) {  
      StringBuffer sbuf = new StringBuffer();
      Message m = (Message) o;
      try {
	sbuf.append("DeliveryMode=");
	switch(m.getJMSDeliveryMode()) {
	case DeliveryMode.NON_PERSISTENT : 	
	  sbuf.append("NON_PERSISTENT");
	  break;
	case DeliveryMode.PERSISTENT : 	
	  sbuf.append("PERSISTENT");
	  break;
	default: sbuf.append("UNKNOWN");
	}
	sbuf.append(", CorrelationID=");
	sbuf.append(m.getJMSCorrelationID());

	sbuf.append(", Destination=");
	sbuf.append(m.getJMSDestination());

	sbuf.append(", Expiration=");
	sbuf.append(m.getJMSExpiration());

	sbuf.append(", MessageID=");
	sbuf.append(m.getJMSMessageID());

	sbuf.append(", Priority=");
	sbuf.append(m.getJMSPriority());

	sbuf.append(", Redelivered=");
	sbuf.append(m.getJMSRedelivered());

	sbuf.append(", ReplyTo=");
	sbuf.append(m.getJMSReplyTo());

	sbuf.append(", Timestamp=");
	sbuf.append(m.getJMSTimestamp());

	sbuf.append(", Type=");
	sbuf.append(m.getJMSType());

	//Enumeration enum = m.getPropertyNames();
	//while(enum.hasMoreElements()) {
	//  String key = (String) enum.nextElement();
	//  sbuf.append("; "+key+"=");
	//  sbuf.append(m.getStringProperty(key));
	//}

      } catch(JMSException e) {
	LogLog.error("Could not parse Message.", e);
      }
      return sbuf.toString();
    } else {
      return o.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6443.java