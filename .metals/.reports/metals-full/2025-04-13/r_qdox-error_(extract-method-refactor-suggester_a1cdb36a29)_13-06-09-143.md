error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1486.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1486.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,26]

error in qdox parser
file content:
```java
offset: 26
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1486.java
text:
```scala
transient private static L@@ogger log = Hierarchy.getDefaultHierarchy().getLoggerFor(

package org.apache.jmeter.save.old.handlers;

import java.io.IOException;
import java.io.Writer;

import org.apache.jmeter.assertions.ResponseAssertion;
import org.apache.jmeter.save.old.SaveHandler;
import org.apache.jmeter.save.old.Saveable;
import org.apache.jmeter.save.old.xml.TagHandler;
import org.apache.jmeter.testelement.TestElement;
import org.apache.log.Hierarchy;
import org.apache.log.Logger;
import org.xml.sax.Attributes;

/**
 * Title:        Jakarta-JMeter
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      Apache
 * @author Michael Stover
 * @version 1.0
 */

public class AssertionHandler extends TagHandler implements SaveHandler
{
	private static Logger log = Hierarchy.getDefaultHierarchy().getLoggerFor(
			"jmeter.util");
	ResponseAssertion model;

	public AssertionHandler() {
	}

	public String getPrimaryTagName()
	{
		return "assertion";
	}

	public void save(Saveable objectToSave, Writer out) throws IOException {
		ResponseAssertion saved = (ResponseAssertion)objectToSave;
		out.write("<");
		out.write(getPrimaryTagName());
		out.write(" name=\"");
		out.write(saved.getName());
		out.write("\" class=\"");
		out.write(saved.getClass().getName());
		out.write("\" testType=\"");
		out.write(""+saved.getTestType());
		out.write("\" testField=\"");
		out.write(saved.getTestField());
		out.write("\">");
		writeTestStrings(saved,out);
		out.write("\n</");
		out.write(getPrimaryTagName());
		out.write(">");
	}

	public Object getModel()
	{
		return model;
	}

	public void setAtts(Attributes atts) throws ClassNotFoundException,IllegalAccessException,InstantiationException
	{
		try {
			model = (ResponseAssertion)Class.forName(JMeterHandler.getComponentConversion(atts.getValue("class"))).newInstance();
			model.setName(atts.getValue("name"));
			model.setTestType(Integer.parseInt(atts.getValue("testType")));
			model.setTestField(JMeterHandler.convertProperty(atts.getValue("testField")));
			model.setProperty(TestElement.GUI_CLASS,JMeterHandler.getGuiClass(atts.getValue("class")));
		} catch (Exception e) {
			log.error("",e);
		} 
	}

	public void testString(String data)
	{
		model.addTestString(data);
	}

	private void writeTestStrings(ResponseAssertion saved, Writer out) throws IOException
	{
		/*Iterator iter = saved.getTestStringList().iterator();
		while (iter.hasNext())
		{
			out.write("\n<testString>");
			out.write(JMeterHandler.convertToXML(iter.next().toString()));
			out.write("</testString>");
		}*/
	}
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1486.java