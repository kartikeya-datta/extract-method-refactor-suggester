error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5135.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5135.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5135.java
text:
```scala
a@@ssertNotNull(tree.getTree("key").get("value"));

/*
 * ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the
 * distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 * if any, must include the following acknowledgment:
 * "This product includes software developed by the
 * Apache Software Foundation (http://www.apache.org/)."
 * Alternately, this acknowledgment may appear in the software itself,
 * if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" and
 * "Apache JMeter" must not be used to endorse or promote products
 * derived from this software without prior written permission. For
 * written permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 * "Apache JMeter", nor may "Apache" appear in their name, without
 * prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
 
package org.apache.jorphan.collections;
import java.io.*;
import java.util.*;

/****************************************
 * ListedHashTree is a different implementation of the {@link HashTree} collection class. 
 * In the ListedHashTree,
 * the order in which values are added is preserved (not to be confused with
 * {@link SortedHashTree}, which sorts the order of the values using the compare()
 * function).  Any listing of nodes or iteration through the list of nodes of a
 * ListedHashTree will be given in the order in which the nodes were added to the
 * tree.
 *
 *@author    mstover1 at apache.org
 * @see HashTree
 ***************************************/
public class ListedHashTree extends HashTree implements Serializable,Cloneable
{

	private List order;

	public ListedHashTree()
	{
		data = new HashMap();
		order = new LinkedList();
	}
	
	public Object clone()
	{
		ListedHashTree newTree = new ListedHashTree();
		newTree.data = (Map)((HashMap)data).clone();
		newTree.order = (List)((LinkedList)order).clone();
		return newTree;
	}

	public ListedHashTree(Object key)
	{
		data = new HashMap();
		order = new LinkedList();
		data.put(key, new ListedHashTree());
		order.add(key);
	}

	public ListedHashTree(Collection keys)
	{
		data = new HashMap();
		order = new LinkedList();
		Iterator it = keys.iterator();
		while(it.hasNext())
		{
			Object temp = it.next();
			data.put(temp, new ListedHashTree());
			order.add(temp);
		}
	}

	public ListedHashTree(Object[] keys)
	{
		data = new HashMap();
		order = new LinkedList();
		for(int x = 0; x < keys.length; x++)
		{
			data.put(keys[x], new ListedHashTree());
			order.add(keys[x]);
		}
	}

	public void set(Object key, Object value)
	{
		if(!data.containsKey(key))
		{
			order.add(key);
		}
		super.set(key,value);
	}

	public void set(Object key, HashTree t)
	{
		if(!data.containsKey(key))
		{
			order.add(key);
		}
		super.set(key,t);
	}

	public void set(Object key, Object[] values)
	{		
		if(!data.containsKey(key))
		{
			order.add(key);
		}
		super.set(key,values);
	}

	public void set(Object key, Collection values)
	{
		if(!data.containsKey(key))
		{
			order.add(key);
		}
		super.set(key,values);
	}

	public void replace(Object currentKey, Object newKey)
	{
		HashTree tree = getTree(currentKey);
		data.remove(currentKey);
		data.put(newKey, tree);
		order.set(order.indexOf(currentKey), newKey);
	}
	
	public HashTree createNewTree()
	{
		return new ListedHashTree();
	}
	
	public HashTree createNewTree(Object key)
	{
		return new ListedHashTree(key);
	}
	
	public HashTree createNewTree(Collection values)
	{
		return new ListedHashTree(values);
	}

	public void add(Object key)
	{
		if(!data.containsKey(key))
		{
			data.put(key, createNewTree());
			order.add(key);
		}
	}

	public Collection list()
	{
		return order;
	}

	public Object remove(Object key)
	{
		order.remove(key);
		return data.remove(key);
	}

	public Object[] getArray()
	{
		return order.toArray();
	}

	public int hashCode()
	{
		return data.hashCode() * 7 + 3;
	}

	public boolean equals(Object o)
	{
		boolean flag = true;
		if(o instanceof ListedHashTree)
		{
			ListedHashTree oo = (ListedHashTree)o;
			Iterator it = order.iterator();
			Iterator it2 = oo.order.iterator();
			if(size() != oo.size())
			{
				flag = false;
			}
			while(it.hasNext() && it2.hasNext() && flag)
			{
				if(!it.next().equals(it2.next()))
				{
					flag = false;
				}
			}
			if(flag)
			{
				it = order.iterator();
				while(it.hasNext() && flag)
				{
					Object temp = it.next();
					flag = get(temp).equals(oo.get(temp));
				}
			}
		}
		else
		{
			flag = false;
		}
		return flag;
	}

	public Set keySet()
	{
		return data.keySet();
	}

	public int size()
	{
		return data.size();
	}

	void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException
	{
		ois.defaultReadObject();
	}

	void writeObject(ObjectOutputStream oos) throws IOException
	{
		oos.defaultWriteObject();
	}

	public static class Test extends junit.framework.TestCase
	{
		/****************************************
		 * !ToDo (Constructor description)
		 *
		 *@param name  !ToDo (Parameter description)
		 ***************************************/
		public Test(String name)
		{
			super(name);
		}

		/****************************************
		 * !ToDo
		 *
		 *@exception Exception  !ToDo (Exception description)
		 ***************************************/
		public void testAddObjectAndTree() throws Exception
		{
			ListedHashTree tree = new ListedHashTree("key");
			ListedHashTree newTree = new ListedHashTree("value");
			tree.add("key", newTree);
			assertEquals(tree.list().size(), 1);
			assertEquals("key",tree.getArray()[0]);
			assertEquals(1,tree.getTree("key").list().size());
			assertEquals(0,tree.getTree("key").getTree("value").size());
			assertEquals(tree.getTree("key").getArray()[0], "value");
			this.assertNotNull(tree.getTree("key").get("value"));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5135.java