error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15940.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15940.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15940.java
text:
```scala
private final i@@nt uid;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.protocol.http.request.urlcompressing;

import java.io.IOException;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Iterator;

import org.apache.wicket.Component;
import org.apache.wicket.IClusterable;
import org.apache.wicket.util.collections.IntHashMap;
import org.apache.wicket.util.collections.IntHashMap.Entry;


/**
 * This class generates UID for Component/Interface combinations when used in
 * conjunction with {@link UrlCompressingWebCodingStrategy}
 * 
 * Use it like this:
 * 
 * <pre>
 * protected IRequestCycleProcessor newRequestCycleProcessor()
 * {
 * 	return new UrlCompressingWebRequestProcessor();
 * }
 * </pre>
 * 
 * @since 1.2
 * 
 * @see URLCompressingWebCodingStrategy
 * @see UrlCompressingWebRequestProcessor
 * 
 * @author jcompagner
 */
public class UrlCompressor implements IClusterable
{
	/**
	 * @author jcompagner
	 */
	public static class ComponentAndInterface
	{
		private static final long serialVersionUID = 1L;

		private final IntKeyWeakReference ref;
		private final String interfaceName;

		private ComponentAndInterface(IntKeyWeakReference ref, String interfaceName)
		{
			this.ref = ref;
			this.interfaceName = interfaceName;
		}

		/**
		 * @return Component The component that should be used to call the
		 *         interface
		 */
		public Component getComponent()
		{
			return (Component)ref.get();
		}

		/**
		 * @return String The interface name which should be called on the
		 *         component
		 */
		public String getInterfaceName()
		{
			return interfaceName;
		}
	}

	private static class IntKeyWeakReference extends WeakReference
	{
		private int uid;

		/**
		 * @param uid
		 * @param referent
		 * @param q
		 */
		public IntKeyWeakReference(int uid, Object referent, ReferenceQueue q)
		{
			super(referent, q);
			this.uid = uid;
		}
	}

	private static final long serialVersionUID = 1L;

	private transient ReferenceQueue queue = new ReferenceQueue();

	private transient IntHashMap directComponentRefs = new IntHashMap(); // uid->component/interface

	private int uid = 1;

	/**
	 * Gets the combination
	 * 
	 * @param uidString
	 * @return ComponentAndInterface
	 */
	public ComponentAndInterface getComponentAndInterfaceForUID(String uidString)
	{
		IntKeyWeakReference ref = null;
		while ((ref = (IntKeyWeakReference)queue.poll()) != null)
		{
			directComponentRefs.remove(ref.uid);
		}
		int uid = Integer.parseInt(uidString);
		ComponentAndInterface cai = (ComponentAndInterface)directComponentRefs.get(uid);
		return cai;
	}

	/**
	 * @return the next uid for this url compressor
	 */
	public int getNewUID()
	{
		return uid++;
	}

	/**
	 * Returns a uid for the combination component and the to call interface.
	 * Will return the same uid if it was already called for this specific
	 * combination.
	 * 
	 * @param component
	 *            The Component
	 * @param interfaceName
	 *            The interface name
	 * @return int The uid for the component/interfaceName combination
	 */
	public int getUIDForComponentAndInterface(Component component, String interfaceName)
	{
		int uid = 0;
		Iterator it = directComponentRefs.entrySet().iterator();
		while (it.hasNext())
		{
			IntHashMap.Entry entry = (IntHashMap.Entry)it.next();
			ComponentAndInterface cai = (ComponentAndInterface)entry.getValue();
			if (cai.getInterfaceName().equals(interfaceName) && cai.getComponent() == component)
			{
				uid = entry.getKey();
				break;
			}
		}
		if (uid == 0)
		{
			uid = getNewUID();
			IntKeyWeakReference ref = new IntKeyWeakReference(uid, component, queue);
			directComponentRefs.put(uid, new ComponentAndInterface(ref, interfaceName));
		}
		return uid;
	}

	private void readObject(java.io.ObjectInputStream s) throws IOException, ClassNotFoundException
	{
		s.defaultReadObject();

		int size = s.readInt();
		queue = new ReferenceQueue();
		directComponentRefs = new IntHashMap((int)(size * 1.25));

		while (--size >= 0)
		{
			int uid = s.readInt();
			Component component = (Component)s.readObject();
			String interfaceName = s.readUTF();

			IntKeyWeakReference ref = new IntKeyWeakReference(uid, component, queue);
			directComponentRefs.put(uid, new ComponentAndInterface(ref, interfaceName));
		}

	}

	private void writeObject(java.io.ObjectOutputStream s) throws IOException
	{
		IntKeyWeakReference ref = null;
		while ((ref = (IntKeyWeakReference)queue.poll()) != null)
		{
			directComponentRefs.remove(ref.uid);
		}

		s.defaultWriteObject();

		s.writeInt(directComponentRefs.size());

		Iterator it = directComponentRefs.entrySet().iterator();
		while (it.hasNext())
		{
			IntHashMap.Entry entry = (Entry)it.next();

			s.writeInt(entry.getKey());
			ComponentAndInterface cai = (ComponentAndInterface)entry.getValue();
			s.writeObject(cai.getComponent());
			s.writeUTF(cai.getInterfaceName());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15940.java