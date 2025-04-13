error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1463.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1463.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,26]

error in qdox parser
file content:
```java
offset: 26
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1463.java
text:
```scala
transient private static L@@ogger log = Hierarchy.getDefaultHierarchy().getLoggerFor(LoggingManager.ELEMENTS);

package org.apache.jmeter.functions;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.util.LoggingManager;
import org.apache.jmeter.util.StringUtilities;
import org.apache.log.Hierarchy;
import org.apache.log.Logger;

/**
 * @author Administrator
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 */
public class ValueReplacer
{
	private static Logger log = Hierarchy.getDefaultHierarchy().getLoggerFor(LoggingManager.ELEMENTS);
	CompoundFunction masterFunction = new CompoundFunction();
	Map variables = new HashMap();
	
	public ValueReplacer()
	{
	}

	public ValueReplacer(Map variables)
	{
		setUserDefinedVariables(variables);
	}
	
	public void setUserDefinedVariables(Map variables)
	{
		masterFunction.setUserDefinedVariables(variables);
		this.variables = variables;
	}
	
	public void replaceValues(TestElement el) throws InvalidVariableException
	{
		Iterator iter = el.getPropertyNames().iterator();
		while(iter.hasNext())
		{
			String propName = (String)iter.next();
			Object propValue = el.getProperty(propName);
			if(propValue instanceof String)
			{
				Object newValue = getNewValue((String)propValue);
				el.setProperty(propName,newValue);
			}
			else if(propValue instanceof TestElement)
			{
				replaceValues((TestElement)propValue);
			}
			else if(propValue instanceof Collection)
			{
				el.setProperty(propName,replaceValues((Collection)propValue));
			}
		}
	}
	
	private Object getNewValue(String propValue) throws InvalidVariableException
	{
		Object newValue = propValue;
				masterFunction.clear();
				masterFunction.setParameters((String)propValue);
				if(masterFunction.hasFunction())
				{
					newValue = masterFunction.getFunction();
				}
				else if(masterFunction.hasStatics())
				{
					newValue = masterFunction.getStaticSubstitution();
				}
				return newValue;
	}
	
	public Collection replaceValues(Collection values) throws InvalidVariableException
	{
		Collection newColl = null;
		try {
			newColl = (Collection)values.getClass().newInstance();
		} catch(Exception e) {
			log.error("",e);
			return values;
		} 
		Iterator iter = values.iterator();
		while(iter.hasNext())
		{
			Object val = iter.next();
			if(val instanceof TestElement)
			{
				replaceValues((TestElement)val);
			}
			else if(val instanceof String)
			{
				val = getNewValue((String)val);
			}
			else if(val instanceof Collection)
			{
				val = replaceValues((Collection)val);
			}
			newColl.add(val);
		}
		return newColl;
	}
	
	/**
	 * Replaces raw values with user-defined variable names.
	 */
	public Collection reverseReplace(Collection values)
	{
		Collection newColl = null;
		try {
			newColl = (Collection)values.getClass().newInstance();
		} catch(Exception e) {
			log.error("",e);
			return values;
		} 
		Iterator iter = values.iterator();
		while(iter.hasNext())
		{
			Object val = iter.next();
			if(val instanceof TestElement)
			{
				reverseReplace((TestElement)val);
			}
			else if(val instanceof String)
			{
				val = substituteValues((String)val);
			}
			else if(val instanceof Collection)
			{
				val = reverseReplace((Collection)val);
			}
			newColl.add(val);
		}
		return newColl;
	} 
	
	/**
	 * Replaces raw values with user-defined variable names.
	 */
	public void reverseReplace(TestElement el)
	{
		Iterator iter = el.getPropertyNames().iterator();
		while(iter.hasNext())
		{
			String propName = (String)iter.next();
			Object propValue = el.getProperty(propName);
			if(propValue instanceof String)
			{
				Object newValue = substituteValues((String)propValue);
				el.setProperty(propName,newValue);
			}
			else if(propValue instanceof TestElement)
			{
				reverseReplace((TestElement)propValue);
			}
			else if(propValue instanceof Collection)
			{
				el.setProperty(propName,reverseReplace((Collection)propValue));
			}
		}
	}
	
	private String substituteValues(String input)
	{
		Iterator iter = variables.keySet().iterator();
		while(iter.hasNext())
		{
			String key = (String)iter.next();
			String value = (String)variables.get(key);
			input = StringUtilities.substitute(input,value,"${"+key+"}");
		}
		return input;
	}
			
			
	
	public static class Test extends TestCase
	{
		Map variables;
		
		public Test(String name)
		{
			super(name);
		}
		
		public void setUp()
		{
			variables = new HashMap();
			variables.put("server","jakarta.apache.org");
			variables.put("username","jack");
			variables.put("password","jacks_password");
			variables.put("regex",".*");
		}
		
		public void testReverseReplacement() throws Exception
		{
			ValueReplacer replacer = new ValueReplacer(variables);
			TestElement element = new TestPlan();
			element.setProperty("domain","jakarta.apache.org");
			List args = new LinkedList();
			args.add("username is jack");
			args.add("jacks_password");
			element.setProperty("args",args);
			replacer.reverseReplace(element);
			assertEquals("${server}",element.getProperty("domain"));
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:169)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1463.java