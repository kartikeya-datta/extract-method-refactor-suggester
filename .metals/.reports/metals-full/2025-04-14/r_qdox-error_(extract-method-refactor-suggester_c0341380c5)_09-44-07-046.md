error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7189.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7189.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7189.java
text:
```scala
f@@ield.getName();

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

import java.io.Externalizable;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.ObjectStreamField;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Date;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.util.lang.Generics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Utility class that analyzes objects for non-serializable nodes. Construct, then call
 * {@link #check(Object)} with the object you want to check. When a non-serializable object is
 * found, a {@link WicketNotSerializableException} is thrown with a message that shows the trace up
 * to the not-serializable object. The exception is thrown for the first non-serializable instance
 * it encounters, so multiple problems will not be shown.
 * <p>
 * As this class depends heavily on JDK's serialization internals using introspection, analyzing may
 * not be possible, for instance when the runtime environment does not have sufficient rights to set
 * fields accessible that would otherwise be hidden. You should call
 * {@link SerializableChecker#isAvailable()} to see whether this class can operate properly. If it
 * doesn't, you should fall back to e.g. re-throwing/ printing the {@link NotSerializableException}
 * you probably got before using this class.
 * </p>
 * 
 * @author eelcohillenius
 * @author Al Maw
 */
public final class SerializableChecker extends ObjectOutputStream
{
	private static final Logger logger = LoggerFactory.getLogger(SerializableChecker.class);

	/**
	 * Exception that is thrown when a non-serializable object was found.
	 */
	public static final class WicketNotSerializableException extends WicketRuntimeException
	{
		private static final long serialVersionUID = 1L;

		WicketNotSerializableException(String message, Throwable cause)
		{
			super(message, cause);
		}
	}

	/**
	 * Does absolutely nothing.
	 */
	private static class NoopOutputStream extends OutputStream
	{
		@Override
		public void close()
		{
		}

		@Override
		public void flush()
		{
		}

		@Override
		public void write(byte[] b)
		{
		}

		@Override
		public void write(byte[] b, int i, int l)
		{
		}

		@Override
		public void write(int b)
		{
		}
	}

	private static abstract class ObjectOutputAdaptor implements ObjectOutput
	{

		public void close() throws IOException
		{
		}

		public void flush() throws IOException
		{
		}

		public void write(byte[] b) throws IOException
		{
		}

		public void write(byte[] b, int off, int len) throws IOException
		{
		}

		public void write(int b) throws IOException
		{
		}

		public void writeBoolean(boolean v) throws IOException
		{
		}

		public void writeByte(int v) throws IOException
		{
		}

		public void writeBytes(String s) throws IOException
		{
		}

		public void writeChar(int v) throws IOException
		{
		}

		public void writeChars(String s) throws IOException
		{
		}

		public void writeDouble(double v) throws IOException
		{
		}

		public void writeFloat(float v) throws IOException
		{
		}

		public void writeInt(int v) throws IOException
		{
		}

		public void writeLong(long v) throws IOException
		{
		}

		public void writeShort(int v) throws IOException
		{
		}

		public void writeUTF(String str) throws IOException
		{
		}
	}

	/** Holds information about the field and the resulting object being traced. */
	private static final class TraceSlot
	{
		private final String fieldDescription;

		private final Object object;

		TraceSlot(Object object, String fieldDescription)
		{
			super();
			this.object = object;
			this.fieldDescription = fieldDescription;
		}

		@Override
		public String toString()
		{
			return object.getClass() + " - " + fieldDescription;
		}
	}

	private static final NoopOutputStream DUMMY_OUTPUT_STREAM = new NoopOutputStream();

	/** log. */
	private static final Logger log = LoggerFactory.getLogger(SerializableChecker.class);

	/** Whether we can execute the tests. If false, check will just return. */
	private static boolean available = true;

	// this hack - accessing the serialization API through introspection - is
	// the only way to use Java serialization for our purposes without writing
	// the whole thing from scratch (and even then, it would be limited). This
	// way of working is of course fragile for internal API changes, but as we
	// do an extra check on availability and we report when we can't use this
	// introspection fu, we'll find out soon enough and clients on this class
	// can fall back on Java's default exception for serialization errors (which
	// sucks and is the main reason for this attempt).
	private static Method LOOKUP_METHOD;

	private static Method GET_CLASS_DATA_LAYOUT_METHOD;

	private static Method GET_NUM_OBJ_FIELDS_METHOD;

	private static Method GET_OBJ_FIELD_VALUES_METHOD;

	private static Method GET_FIELD_METHOD;

	private static Method HAS_WRITE_REPLACE_METHOD_METHOD;

	private static Method INVOKE_WRITE_REPLACE_METHOD;

	static
	{
		try
		{
			LOOKUP_METHOD = ObjectStreamClass.class.getDeclaredMethod("lookup", new Class[] {
					Class.class, Boolean.TYPE });
			LOOKUP_METHOD.setAccessible(true);

			GET_CLASS_DATA_LAYOUT_METHOD = ObjectStreamClass.class.getDeclaredMethod(
				"getClassDataLayout", (Class[])null);
			GET_CLASS_DATA_LAYOUT_METHOD.setAccessible(true);

			GET_NUM_OBJ_FIELDS_METHOD = ObjectStreamClass.class.getDeclaredMethod(
				"getNumObjFields", (Class[])null);
			GET_NUM_OBJ_FIELDS_METHOD.setAccessible(true);

			GET_OBJ_FIELD_VALUES_METHOD = ObjectStreamClass.class.getDeclaredMethod(
				"getObjFieldValues", new Class[] { Object.class, Object[].class });
			GET_OBJ_FIELD_VALUES_METHOD.setAccessible(true);

			GET_FIELD_METHOD = ObjectStreamField.class.getDeclaredMethod("getField", (Class[])null);
			GET_FIELD_METHOD.setAccessible(true);

			HAS_WRITE_REPLACE_METHOD_METHOD = ObjectStreamClass.class.getDeclaredMethod(
				"hasWriteReplaceMethod", (Class[])null);
			HAS_WRITE_REPLACE_METHOD_METHOD.setAccessible(true);

			INVOKE_WRITE_REPLACE_METHOD = ObjectStreamClass.class.getDeclaredMethod(
				"invokeWriteReplace", new Class[] { Object.class });
			INVOKE_WRITE_REPLACE_METHOD.setAccessible(true);
		}
		catch (Exception e)
		{
			logger.warn("SerializableChecker not available", e);
			available = false;
		}
	}

	/**
	 * Gets whether we can execute the tests. If false, calling {@link #check(Object)} will just
	 * return and you are advised to rely on the {@link NotSerializableException}. Clients are
	 * advised to call this method prior to calling the check method.
	 * 
	 * @return whether security settings and underlying API etc allow for accessing the
	 *         serialization API using introspection
	 */
	public static boolean isAvailable()
	{
		return available;
	}

	/** object stack that with the trace path. */
	private final LinkedList<TraceSlot> traceStack = new LinkedList<TraceSlot>();

	/** set for checking circular references. */
	private final Map<Object, Object> checked = new IdentityHashMap<Object, Object>();

	/** string stack with current names pushed. */
	private final LinkedList<String> nameStack = new LinkedList<String>();

	/** root object being analyzed. */
	private Object root;

	/** cache for classes - writeObject methods. */
	private final Map<Class<?>, Object> writeObjectMethodCache = Generics.newHashMap();

	/** current simple field name. */
	private String simpleName = "";

	/** current full field description. */
	private String fieldDescription;

	/** Exception that should be set as the cause when throwing a new exception. */
	private final NotSerializableException exception;

	/**
	 * Construct.
	 * 
	 * @param exception
	 *            exception that should be set as the cause when throwing a new exception
	 * 
	 * @throws IOException
	 */
	public SerializableChecker(NotSerializableException exception) throws IOException
	{
		this.exception = exception;
	}

	/**
	 * @see java.io.ObjectOutputStream#reset()
	 */
	@Override
	public void reset() throws IOException
	{
		root = null;
		checked.clear();
		fieldDescription = null;
		simpleName = null;
		traceStack.clear();
		nameStack.clear();
		writeObjectMethodCache.clear();
	}

	private void check(Object obj)
	{
		if (obj == null)
		{
			return;
		}

		Class<?> cls = obj.getClass();
		nameStack.add(simpleName);
		traceStack.add(new TraceSlot(obj, fieldDescription));

		if (!(obj instanceof Serializable) && (!Proxy.isProxyClass(cls)))
		{
			throw new WicketNotSerializableException(
				toPrettyPrintedStack(obj.getClass().getName()), exception);
		}

		ObjectStreamClass desc;
		for (;;)
		{
			try
			{
				desc = (ObjectStreamClass)LOOKUP_METHOD.invoke(null, new Object[] { cls,
						Boolean.TRUE });
				Class<?> repCl;
				if (!((Boolean)HAS_WRITE_REPLACE_METHOD_METHOD.invoke(desc, (Object[])null)).booleanValue() ||
					(obj = INVOKE_WRITE_REPLACE_METHOD.invoke(desc, new Object[] { obj })) == null ||
					(repCl = obj.getClass()) == cls)
				{
					break;
				}
				cls = repCl;
			}
			catch (IllegalAccessException e)
			{
				throw new RuntimeException(e);
			}
			catch (InvocationTargetException e)
			{
				throw new RuntimeException(e);
			}
		}

		if (cls.isPrimitive())
		{
			// skip
		}
		else if (cls.isArray())
		{
			checked.put(obj, null);
			Class<?> ccl = cls.getComponentType();
			if (!(ccl.isPrimitive()))
			{
				Object[] objs = (Object[])obj;
				for (int i = 0; i < objs.length; i++)
				{
					String arrayPos = "[" + i + "]";
					simpleName = arrayPos;
					fieldDescription += arrayPos;
					check(objs[i]);
				}
			}
		}
		else if (obj instanceof Externalizable && (!Proxy.isProxyClass(cls)))
		{
			Externalizable extObj = (Externalizable)obj;
			try
			{
				extObj.writeExternal(new ObjectOutputAdaptor()
				{
					private int count = 0;

					public void writeObject(Object streamObj) throws IOException
					{
						// Check for circular reference.
						if (checked.containsKey(streamObj))
						{
							return;
						}

						checked.put(streamObj, null);
						String arrayPos = "[write:" + count++ + "]";
						simpleName = arrayPos;
						fieldDescription += arrayPos;

						check(streamObj);
					}
				});
			}
			catch (Exception e)
			{
				if (e instanceof WicketNotSerializableException)
				{
					throw (WicketNotSerializableException)e;
				}
				log.warn("error delegating to Externalizable : " + e.getMessage() + ", path: " +
					currentPath());
			}
		}
		else
		{
			Method writeObjectMethod = null;
			Object o = writeObjectMethodCache.get(cls);
			if (o != null)
			{
				if (o instanceof Method)
				{
					writeObjectMethod = (Method)o;
				}
			}
			else
			{
				try
				{
					writeObjectMethod = cls.getDeclaredMethod("writeObject",
						new Class[] { java.io.ObjectOutputStream.class });
				}
				catch (SecurityException e)
				{
					// we can't access/ set accessible to true
					writeObjectMethodCache.put(cls, Boolean.FALSE);
				}
				catch (NoSuchMethodException e)
				{
					// cls doesn't have that method
					writeObjectMethodCache.put(cls, Boolean.FALSE);
				}
			}

			final Object original = obj;
			if (writeObjectMethod != null)
			{
				class InterceptingObjectOutputStream extends ObjectOutputStream
				{
					private int counter;

					InterceptingObjectOutputStream() throws IOException
					{
						super(DUMMY_OUTPUT_STREAM);
						enableReplaceObject(true);
					}

					@Override
					protected Object replaceObject(Object streamObj) throws IOException
					{
						if (streamObj == original)
						{
							return streamObj;
						}

						counter++;
						// Check for circular reference.
						if (checked.containsKey(streamObj))
						{
							return null;
						}

						checked.put(original, null);
						String arrayPos = "[write:" + counter + "]";
						simpleName = arrayPos;
						fieldDescription += arrayPos;
						check(streamObj);
						return streamObj;
					}
				}
				try
				{
					InterceptingObjectOutputStream ioos = new InterceptingObjectOutputStream();
					ioos.writeObject(obj);
				}
				catch (Exception e)
				{
					if (e instanceof WicketNotSerializableException)
					{
						throw (WicketNotSerializableException)e;
					}
					log.warn("error delegating to writeObject : " + e.getMessage() + ", path: " +
						currentPath());
				}
			}
			else
			{
				Object[] slots;
				try
				{
					slots = (Object[])GET_CLASS_DATA_LAYOUT_METHOD.invoke(desc, (Object[])null);
				}
				catch (Exception e)
				{
					throw new RuntimeException(e);
				}
				for (int i = 0; i < slots.length; i++)
				{
					ObjectStreamClass slotDesc;
					try
					{
						Field descField = slots[i].getClass().getDeclaredField("desc");
						descField.setAccessible(true);
						slotDesc = (ObjectStreamClass)descField.get(slots[i]);
					}
					catch (Exception e)
					{
						throw new RuntimeException(e);
					}
					checked.put(obj, null);
					checkFields(obj, slotDesc);
				}
			}
		}

		traceStack.removeLast();
		nameStack.removeLast();
	}

	private void checkFields(Object obj, ObjectStreamClass desc)
	{
		int numFields;
		try
		{
			numFields = ((Integer)GET_NUM_OBJ_FIELDS_METHOD.invoke(desc, (Object[])null)).intValue();
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
		catch (InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}

		if (numFields > 0)
		{
			int numPrimFields;
			ObjectStreamField[] fields = desc.getFields();
			Object[] objVals = new Object[numFields];
			numPrimFields = fields.length - objVals.length;
			try
			{
				GET_OBJ_FIELD_VALUES_METHOD.invoke(desc, new Object[] { obj, objVals });
			}
			catch (IllegalAccessException e)
			{
				throw new RuntimeException(e);
			}
			catch (InvocationTargetException e)
			{
				throw new RuntimeException(e);
			}
			for (int i = 0; i < objVals.length; i++)
			{
				if (objVals[i] instanceof String || objVals[i] instanceof Number ||
					objVals[i] instanceof Date || objVals[i] instanceof Boolean ||
					objVals[i] instanceof Class)
				{
					// filter out common cases
					continue;
				}

				// Check for circular reference.
				if (checked.containsKey(objVals[i]))
				{
					continue;
				}

				ObjectStreamField fieldDesc = fields[numPrimFields + i];
				Field field;
				try
				{
					field = (Field)GET_FIELD_METHOD.invoke(fieldDesc, (Object[])null);
				}
				catch (IllegalAccessException e)
				{
					throw new RuntimeException(e);
				}
				catch (InvocationTargetException e)
				{
					throw new RuntimeException(e);
				}

				String fieldName = field.getName();
				simpleName = field.getName();
				fieldDescription = field.toString();
				check(objVals[i]);
			}
		}
	}

	/**
	 * @return name from root to current node concatenated with slashes
	 */
	private StringBuffer currentPath()
	{
		StringBuffer b = new StringBuffer();
		for (Iterator<String> it = nameStack.iterator(); it.hasNext();)
		{
			b.append(it.next());
			if (it.hasNext())
			{
				b.append('/');
			}
		}
		return b;
	}

	/**
	 * Dump with indentation.
	 * 
	 * @param type
	 *            the type that couldn't be serialized
	 * @return A very pretty dump
	 */
	private final String toPrettyPrintedStack(String type)
	{
		StringBuffer result = new StringBuffer();
		StringBuffer spaces = new StringBuffer();
		result.append("Unable to serialize class: ");
		result.append(type);
		result.append("\nField hierarchy is:");
		for (Iterator<TraceSlot> i = traceStack.listIterator(); i.hasNext();)
		{
			spaces.append("  ");
			TraceSlot slot = i.next();
			result.append("\n").append(spaces).append(slot.fieldDescription);
			result.append(" [class=").append(slot.object.getClass().getName());
			if (slot.object instanceof Component)
			{
				Component component = (Component)slot.object;
				result.append(", path=").append(component.getPath());
			}
			result.append("]");
		}
		result.append(" <----- field that is not serializable");
		return result.toString();
	}

	/**
	 * @see java.io.ObjectOutputStream#writeObjectOverride(java.lang.Object)
	 */
	@Override
	protected final void writeObjectOverride(Object obj) throws IOException
	{
		if (!available)
		{
			return;
		}
		root = obj;
		if (fieldDescription == null)
		{
			fieldDescription = (root instanceof Component) ? ((Component)root).getPath() : "";
		}

		check(root);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7189.java