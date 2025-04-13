error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1491.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1491.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,26]

error in qdox parser
file content:
```java
offset: 26
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1491.java
text:
```scala
transient private static L@@ogger log = Hierarchy.getDefaultHierarchy().getLoggerFor(

package org.apache.jmeter.testelement;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log.Hierarchy;
import org.apache.log.Logger;

/****************************************
 * Title: JMeter Description: Copyright: Copyright (c) 2000 Company: Apache
 *
 *@author    Michael Stover
 *@created   $Date$
 *@version   1.0
 ***************************************/

public abstract class AbstractTestElement implements TestElement,Serializable
{
	private Map testInfo = new HashMap();
	private static Logger log = Hierarchy.getDefaultHierarchy().getLoggerFor(
			"jmeter.elements");

	/****************************************
	 * !ToDo (Method description)
	 *
	 *@return   !ToDo (Return description)
	 ***************************************/
	public Object clone()
	{
		try
		{
			TestElement newObject = (TestElement)this.getClass().newInstance();
			configureClone(newObject);
			return newObject;
		}
		catch(Exception e)
		{
			log.error("",e);
		}
		return null;
	}

	public void removeProperty(String key)
	{
		testInfo.remove(key);
	}

	public boolean equals(Object o)
	{
		if(o instanceof AbstractTestElement)
		{
			return ((AbstractTestElement)o).testInfo.equals(testInfo);
		}
		else
		{
			return false;
		}
	}

	/****************************************
	 * !ToDo
	 *
	 *@param el  !ToDo
	 ***************************************/
	public void addTestElement(TestElement el)
	{
		if(el.getClass().equals(this.getClass()))
		{
			mergeIn(el);
		}
	}

	/****************************************
	 * !ToDo (Method description)
	 *
	 *@param name  !ToDo (Parameter description)
	 ***************************************/
	public void setName(String name)
	{
		setProperty(TestElement.NAME, name);
	}

	/****************************************
	 * !ToDoo (Method description)
	 *
	 *@return   !ToDo (Return description)
	 ***************************************/
	public String getName()
	{
		return (String)getProperty(TestElement.NAME);
	}

	/****************************************
	 * !ToDo (Method description)
	 *
	 *@param key   !ToDo (Parameter description)
	 *@param prop  !ToDo (Parameter description)
	 ***************************************/
	public void setProperty(String key, Object prop)
	{
		testInfo.put(key, prop);
	}

	/****************************************
	 * !ToDoo (Method description)
	 *
	 *@param key  !ToDo (Parameter description)
	 *@return     !ToDo (Return description)
	 ***************************************/
	public Object getProperty(String key)
	{
		return testInfo.get(key);
	}

	/****************************************
	 * !ToDoo (Method description)
	 *
	 *@return   !ToDo (Return description)
	 ***************************************/
	public Collection getPropertyNames()
	{
		return testInfo.keySet();
	}

	/****************************************
	 * !ToDo (Method description)
	 *
	 *@param newObject  !ToDo (Parameter description)
	 ***************************************/
	protected void configureClone(TestElement newObject)
	{
		Iterator iter = getPropertyNames().iterator();
		while(iter.hasNext())
		{
			String key = (String)iter.next();
			Object value = getProperty(key);
			if(value instanceof TestElement)
			{
				newObject.setProperty(key, ((TestElement)value).clone());
			}
			else if(value instanceof Collection)
			{
				try {
					newObject.setProperty(key,cloneCollection(value));
				} catch(Exception e) {
					log.error("",e);
				}
			}
			else
			{
				newObject.setProperty(key, value);
			}
		}
	}

	protected Collection cloneCollection(Object value) throws InstantiationException,
			IllegalAccessException,ClassNotFoundException
	{
		Iterator collIter = ((Collection)value).iterator();
		Collection newColl = (Collection)value.getClass().newInstance();
		while(collIter.hasNext())
		{
			Object val = collIter.next();
			if(val instanceof TestElement)
			{
				val = ((TestElement)val).clone();
			}
			else if(val instanceof Collection)
			{
				try {
					val = cloneCollection(val);
				} catch(Exception e) {
					continue;
				}
			}
			newColl.add(val);
		}
		return newColl;
	}

	private long getLongValue(Object bound)
	{
		if (bound == null)
		{
			return (long)0;
		} else if (bound instanceof Long)
		{
			return ((Long) bound).longValue();
		} else
		{
			return Long.parseLong((String) bound);
		}
	}

	private float getFloatValue(Object bound)
	{
		if (bound == null)
		{
			return (float)0;
		} else if (bound instanceof Float)
		{
			return ((Float) bound).floatValue();
		} else
		{
			return Float.parseFloat((String) bound);
		}
	}

	private double getDoubleValue(Object bound)
	{
		if (bound == null)
		{
			return (double)0;
		} else if (bound instanceof Double)
		{
			return ((Double) bound).doubleValue();
		} else
		{
			return Double.parseDouble((String) bound);
		}
	}

	private String getStringValue(Object bound)
	{
		if (bound == null)
		{
			return "";
		} else return bound.toString();
	}

	private int getIntValue(Object bound)
	{
		if (bound == null)
		{
			return (int)0;
		} else if (bound instanceof Integer)
		{
			return ((Integer) bound).intValue();
		} else
		{
			try
			{
				return Integer.parseInt((String) bound);
			}
			catch(NumberFormatException e)
			{
				return 0;
			}
		}
	}

	private boolean getBooleanValue(Object bound)
	{
		if (bound == null)
		{
			return false;
		} else if (bound instanceof Boolean)
		{
			return ((Boolean) bound).booleanValue();
		}
		else
		{
			return new Boolean((String) bound).booleanValue();
		}
	}

	public int getPropertyAsInt(String key)
	{
		return getIntValue(getProperty(key));
	}

	public boolean getPropertyAsBoolean(String key)
	{
		return getBooleanValue(getProperty(key));
	}

	public float getPropertyAsFloat(String key)
	{
		return getFloatValue(getProperty(key));
	}

	public long getPropertyAsLong(String key)
	{
		return getLongValue(getProperty(key));
	}

	public double getPropertyAsDouble(String key)
	{
		return getDoubleValue(getProperty(key));
	}

	public String getPropertyAsString(String key)
	{
		return getStringValue(getProperty(key));
	}

	/****************************************
	 * !ToDo (Method description)
	 *
	 *@param element  !ToDo (Parameter description)
	 ***************************************/
	protected void mergeIn(TestElement element)
	{
		Iterator iter = element.getPropertyNames().iterator();
		while(iter.hasNext())
		{
			String key = (String)iter.next();
			Object value = element.getProperty(key);
			if(getProperty(key) == null || getProperty(key).equals(""))
			{
				setProperty(key, value);
				continue;
			}
			if(value instanceof TestElement)
			{
				if(getProperty(key) == null)
				{
					setProperty(key,value);
				}
				else if(getProperty(key) instanceof TestElement)
				{
					((TestElement)getProperty(key)).addTestElement((TestElement)value);
				}
			}
			else if(value instanceof Collection)
			{
				Iterator iter2 = ((Collection)value).iterator();
				Collection localCollection = (Collection)getProperty(key);
				if(localCollection == null)
				{
					setProperty(key,value);
				}
				else
				{
					while(iter2.hasNext())
					{
						Object item = iter2.next();
						if(!localCollection.contains(item))
						{
							localCollection.add(item);
						}
					}
				}
			}
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1491.java