error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16986.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16986.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,17]

error in qdox parser
file content:
```java
offset: 17
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16986.java
text:
```scala
protected final v@@oid writeObjectOverride(final Object obj) throws IOException

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
package org.apache.wicket.util.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;

import org.apache.wicket.Application;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.application.IClassResolver;
import org.apache.wicket.settings.IApplicationSettings;
import org.apache.wicket.util.lang.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Interface for serializing and deserializing so that we can vary the implementation of the
 * {@link ObjectOutputStream} and {@link ObjectInputStream} implementations.
 * 
 * @see Objects#objectToByteArray(Object)
 * @see Objects#byteArrayToObject(byte[])
 * 
 * @author eelcohillenius
 */
public interface IObjectStreamFactory
{
	/**
	 * Default implementation that uses the JDK's plain implementations.
	 */
	public static final class DefaultObjectStreamFactory implements IObjectStreamFactory
	{
		private static final Logger log = LoggerFactory.getLogger(DefaultObjectStreamFactory.class);

		/**
		 * @see org.apache.wicket.util.io.IObjectStreamFactory#newObjectInputStream(java.io.InputStream)
		 */
		public ObjectInputStream newObjectInputStream(InputStream in) throws IOException
		{
			return new ObjectInputStream(in)
			{
				// This override is required to resolve classes inside in different bundle, i.e.
				// The classes can be resolved by OSGI classresolver implementation
				protected Class resolveClass(ObjectStreamClass desc) throws IOException,
					ClassNotFoundException
				{
					String className = desc.getName();

					try
					{
						return super.resolveClass(desc);
					}
					catch (ClassNotFoundException ex1)
					{
						// ignore this exception.
						log.debug("Class not found by the object outputstream itself, trying the IClassResolver");
					}


					Class candidate = null;
					try
					{
						// Can the application always be taken??
						Application application = Application.get();
						IApplicationSettings applicationSettings = application.getApplicationSettings();
						IClassResolver classResolver = applicationSettings.getClassResolver();

						candidate = classResolver.resolveClass(className);
						if (candidate == null)
						{
							candidate = super.resolveClass(desc);
						}
					}
					catch (WicketRuntimeException ex)
					{
						if (ex.getCause() instanceof ClassNotFoundException)
						{
							throw (ClassNotFoundException)ex.getCause();
						}
					}
					return candidate;
				}
			};
		}

		/**
		 * @see org.apache.wicket.util.io.IObjectStreamFactory#newObjectOutputStream(java.io.OutputStream)
		 */
		public ObjectOutputStream newObjectOutputStream(final OutputStream out) throws IOException
		{
			final ObjectOutputStream oos = new ObjectOutputStream(out);
			return new ObjectOutputStream()
			{
				protected void writeObjectOverride(final Object obj) throws IOException
				{
					try
					{
						oos.writeObject(obj);
					}
					catch (IOException e)
					{
						if (SerializableChecker.isAvailable())
						{
							// trigger serialization again, but this time gather
							// some more info
							new SerializableChecker((NotSerializableException)e).writeObject(obj);
							// if we get here, we didn't fail, while we
							// should;
							throw e;
						}
						throw e;
					}
					catch (RuntimeException e)
					{
						log.error("error writing object " + obj + ": " + e.getMessage(), e);
						throw e;
					}
				}

				public void flush() throws IOException
				{
					oos.flush();
					super.flush();
				}

				public void close() throws IOException
				{
					oos.close();
					super.close();
				}
			};
		}
	}

	/**
	 * Gets a new instance of an {@link ObjectInputStream} with the provided {@link InputStream}.
	 * 
	 * @param in
	 *            The input stream that should be used for the reading
	 * @return a new object input stream instance
	 * @throws IOException
	 *             if an I/O error occurs while reading stream header
	 */
	ObjectInputStream newObjectInputStream(InputStream in) throws IOException;

	/**
	 * Gets a new instance of an {@link ObjectOutputStream} with the provided {@link OutputStream}.
	 * 
	 * @param out
	 *            The output stream that should be used for the writing
	 * @return a new object output stream instance
	 * @throws IOException
	 *             if an I/O error occurs while writing stream header
	 */
	ObjectOutputStream newObjectOutputStream(OutputStream out) throws IOException;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16986.java