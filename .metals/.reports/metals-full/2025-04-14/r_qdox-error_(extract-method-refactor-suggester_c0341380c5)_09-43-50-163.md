error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/424.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/424.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/424.java
text:
```scala
t@@hrow new WicketSerializeableException("No Serializable constructor found for " + cls);

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
package wicket.util.io;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sun.misc.Unsafe;
import sun.reflect.ReflectionFactory;

/**
 * TODO DOC ME!
 * 
 * @author jcompagner
 */
public final class ClassStreamHandler
{
	private static Unsafe unsafe;

	static
	{
		try
		{
			Class[] classes = ObjectStreamClass.class.getDeclaredClasses();
			for (int i = 0; i < classes.length; i++)
			{
				if (classes[i].getName().equals("java.io.ObjectStreamClass$FieldReflector"))
				{
					Field unsafeField = classes[i].getDeclaredField("unsafe");
					unsafeField.setAccessible(true);

					unsafe = (Unsafe)unsafeField.get(null);
					break;
				}
			}
		}
		catch (Throwable e)
		{
			// e.printStackTrace()
		}
	}

	private static final ReflectionFactory reflFactory = (ReflectionFactory)AccessController
			.doPrivileged(new ReflectionFactory.GetReflectionFactoryAction());


	private static Map handlesClasses = new HashMap();
	
	private static short classCounter = 0;

	/**
	 * 
	 */
	public static final byte HANDLE = 1;

	/**
	 * 
	 */
	public static final byte NULL = 0;

	/**
	 * 
	 */
	public static final byte CLASS_DEF = 2;

	/**
	 * 
	 */
	public static final byte ARRAY = 3;

	/**
	 * 
	 */
	public static final byte PRIMITIVE_ARRAY = 4;

	/**
	 * 
	 */
	public static final int CLASS = 5;

	static ClassStreamHandler lookup(Class cls) throws NotSerializableException
	{
		ClassStreamHandler classHandler = (ClassStreamHandler)handlesClasses.get(cls.getName());
		if (classHandler == null)
		{
			classHandler = new ClassStreamHandler(cls);
			handlesClasses.put(cls.getName(), classHandler);
			handlesClasses.put(new Short(classHandler.getClassId()), classHandler);
		}
		return classHandler;
	}

	static ClassStreamHandler lookup(short s)
	{
		ClassStreamHandler classHandler = (ClassStreamHandler)handlesClasses.get(new Short(s));
		if (classHandler == null)
		{
			throw new RuntimeException("class not found for: " + s);
		}
		return classHandler;
	}

	/**
	 * 
	 */
	private final Class clz;
	private final List fields;
	private final short classId;

	private final Constructor cons;
	
	private final Method writeReplaceMethod;
	private final Method readResolveMethod;

	private final List writeObjectMethods;

	private final List readObjectMethods;


	private final PrimitiveArray primitiveArray;


	private boolean isProxy;

	/**
	 * Construct.
	 * 
	 * @param cls
	 * @param wicketObjectOutputStream
	 *            TODO
	 * @throws WicketSerializeableException 
	 */
	private ClassStreamHandler(Class cls) throws WicketSerializeableException
	{
		this.classId = classCounter++;
		this.clz = cls;
		if (cls.isPrimitive())
		{
			fields = null;
			cons = null;
			writeObjectMethods = null;
			readObjectMethods = null;
			
			writeReplaceMethod = null;
			readResolveMethod = null;
			
			if (clz == boolean.class)
			{
				primitiveArray = new BooleanPrimitiveArray();
			}
			else if (clz == byte.class)
			{
				primitiveArray = new BytePrimitiveArray();
			}
			else if (clz == short.class)
			{
				primitiveArray = new ShortPrimitiveArray();
			}
			else if (clz == char.class)
			{
				primitiveArray = new CharPrimitiveArray();
			}
			else if (clz == int.class)
			{
				primitiveArray = new IntPrimitiveArray();
			}
			else if (clz == long.class)
			{
				primitiveArray = new LongPrimitiveArray();
			}
			else if (clz == float.class)
			{
				primitiveArray = new FloatPrimitiveArray();
			}
			else if (clz == double.class)
			{
				primitiveArray = new DoublePrimitiveArray();
			}
			else
			{
				throw new RuntimeException("Unsupported primitive " + cls);
			}
		}
		else if (cls.isInterface())
		{
			fields = null;
			cons = null;
			writeObjectMethods = null;
			readObjectMethods = null;
			writeReplaceMethod = null;
			readResolveMethod = null;
			primitiveArray = null;
			isProxy = false;
		}
		else if (Proxy.isProxyClass(clz))
		{
			isProxy = true;
			fields = null;
			cons = null;
			writeObjectMethods = null;
			writeReplaceMethod = null;
			readResolveMethod = null;
			readObjectMethods = null;
			primitiveArray = null;
		}
		else
		{
			fields = new ArrayList();
			primitiveArray = null;
	    	writeObjectMethods = new ArrayList(2);
			readObjectMethods = new ArrayList(2);
			isProxy = false;
			
		    writeReplaceMethod = getInheritableMethod( clz, "writeReplace", null, Object.class);
		    
		    readResolveMethod = getInheritableMethod(clz, "readResolve", null, Object.class);
		    if (readResolveMethod == null)
		    {
				cons = getSerializableConstructor(clz);
				if (cons == null)
				{
					throw new WicketSerializeableException("No serializeable constructor found for " + cls);
				}
		    }
		    else
		    {
		    	cons = null;
		    }

			Class parent = cls;
			while(parent != Object.class)
			{
				Method method = getPrivateMethod(parent, "writeObject", new Class[] { ObjectOutputStream.class }, Void.TYPE);
				if (method != null) writeObjectMethods.add(method);
				method = getPrivateMethod(parent, "readObject", new Class[] { ObjectInputStream.class }, Void.TYPE);
				if (method != null) readObjectMethods.add(method);
				
				parent = parent.getSuperclass();
			}
			if (writeReplaceMethod == null)
			{
				fillFields(cls);
			}
		}
	}

	/**
	 * @return
	 */
	public Class getStreamClass()
	{
		return clz;
	}

	/**
	 * @return
	 */
	public short getClassId()
	{
		return classId;
	}

	/**
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws IllegalArgumentException
	 */
	public Object createObject() throws IllegalArgumentException, InstantiationException,
			IllegalAccessException, InvocationTargetException
	{
		return cons.newInstance(null);
	}

	/**
	 * @param cls
	 */
	private void fillFields(Class cls)
	{
		if (cls == null)
		{
			return;
		}
		Field[] fields = cls.getDeclaredFields();
		for (int i = 0; i < fields.length; i++)
		{
			Field field = fields[i];
			field.setAccessible(true);
			if (!Modifier.isStatic(field.getModifiers())
					&& !Modifier.isTransient(field.getModifiers()))
			{
				FieldAndIndex fai = null;
				Class clz = field.getType();
				long offset = unsafe.objectFieldOffset(field);
				if (clz == boolean.class)
				{
					fai = new BooleanFieldAndIndex(field);
				}
				else if (clz == byte.class)
				{
					fai = new ByteFieldAndIndex(field);
				}
				else if (clz == short.class)
				{
					fai = new ShortFieldAndIndex(field);
				}
				else if (clz == char.class)
				{
					fai = new CharFieldAndIndex(field);
				}
				else if (clz == int.class)
				{
					fai = new IntFieldAndIndex(field);
				}
				else if (clz == long.class)
				{
					fai = new LongFieldAndIndex(field);
				}
				else if (clz == float.class)
				{
					fai = new FloatFieldAndIndex(field);
				}
				else if (clz == double.class)
				{
					fai = new DoubleFieldAndIndex(field);
				}
				else
				{
					fai = new ObjectFieldAndIndex(field);
				}
				this.fields.add(fai);
			}
		}
		cls = cls.getSuperclass();
		if (cls != Object.class)
		{
			fillFields(cls);
		}
		return;
	}

	/**
	 * @param woos
	 * @param obj
	 * @param out
	 * @throws WicketSerializeableException 
	 */
	public void writeFields(WicketObjectOutputStream woos, Object obj) throws WicketSerializeableException
	{
		FieldAndIndex fai = null;
		try
		{
			for (int i = 0; fields != null && i < fields.size(); i++)
			{
				fai = (FieldAndIndex)fields.get(i);
				fai.writeField(obj, woos);
			}
		}
		catch (WicketSerializeableException wse)
		{
			wse.addTrace(fai.field.getName());
			throw wse;
		}
		catch (Exception ex)
		{
			String field = fai == null || fai.field == null? "" : fai.field.getName();
			String msg = "Error writing field: " + field + " for object class: " + obj.getClass();
			throw new WicketSerializeableException(msg, ex);
		}

	}

	/**
	 * @param wois
	 * @throws WicketSerializeableException 
	 */
	public void readFields(WicketObjectInputStream wois, Object object)  throws WicketSerializeableException
	{
		FieldAndIndex fai = null;
		try
		{
			for (int i = 0; i < fields.size(); i++)
			{
				fai = (FieldAndIndex)fields.get(i);
				fai.readField(object, wois);
			}
		}
		catch (WicketSerializeableException wse)
		{
			wse.addTrace(fai.field.getName());
			throw wse;
		}
		catch (Exception ex)
		{
			throw new WicketSerializeableException("Error reading field: " + fai.field.getName() + " for object class: " + object.getClass(),ex);
		}
	}
	
	public void writeArray(Object obj, WicketObjectOutputStream wois) throws IOException
	{
		primitiveArray.writeArray(obj,wois);
	}

	public Object readArray(WicketObjectInputStream wois) throws IOException
	{
		return primitiveArray.readArray(wois);
	}
	
	/**
	 * @param woos
	 * @param obj
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public boolean invokeWriteMethod(WicketObjectOutputStream woos, Object obj)
	{
		if ((writeObjectMethods != null) && writeObjectMethods.size() > 0)
		{
			for (int i = writeObjectMethods.size(); --i >= 0;)
			{
				Method method = (Method)writeObjectMethods.get(i);
				
				try
				{
					method.invoke(obj, new Object[] { woos });
				}
				catch (IllegalArgumentException ex)
				{
					throw new RuntimeException(ex);
				}
				catch (IllegalAccessException ex)
				{
					throw new RuntimeException(ex);
				}
				catch (InvocationTargetException ex)
				{
					throw new RuntimeException(ex);
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * @param wois
	 * @return
	 */
	public boolean invokeReadMethod(WicketObjectInputStream wois, Object obj)
	{
		if ((readObjectMethods != null) && readObjectMethods.size() > 0)
		{
			for (int i = readObjectMethods.size(); --i >= 0;)
			{
				Method method = (Method)readObjectMethods.get(i);
				try
				{
					method.invoke(obj, new Object[] { wois });
				}
				catch (IllegalArgumentException ex)
				{
					throw new RuntimeException(ex);
				}
				catch (IllegalAccessException ex)
				{
					throw new RuntimeException(ex);
				}
				catch (InvocationTargetException ex)
				{
					throw new RuntimeException(ex);
				}
			}
			return true;
		}
		return false;
	}

	private static Constructor getSerializableConstructor(Class cl)
	{
		Class initCl = cl;
		while (Serializable.class.isAssignableFrom(initCl))
		{
			if ((initCl = initCl.getSuperclass()) == null)
			{
				return null;
			}
		}
		try
		{
			Constructor cons = initCl.getDeclaredConstructor((Class[])null);
			int mods = cons.getModifiers();
			if ((mods & Modifier.PRIVATE) != 0
 ((mods & (Modifier.PUBLIC | Modifier.PROTECTED)) == 0 && !packageEquals(cl,
							initCl)))
			{
				return null;
			}
			cons = reflFactory.newConstructorForSerialization(cl, cons);
			cons.setAccessible(true);
			return cons;
		}
		catch (NoSuchMethodException ex)
		{
			return null;
		}
	}

	/**
	 * Returns non-static private method with given signature defined by given
	 * class, or null if none found. Access checks are disabled on the returned
	 * method (if any).
	 */
	private static Method getPrivateMethod(Class cl, String name, Class[] argTypes, Class returnType)
	{
		try
		{
			Method meth = cl.getDeclaredMethod(name, argTypes);
			meth.setAccessible(true);
			int mods = meth.getModifiers();
			return ((meth.getReturnType() == returnType) && ((mods & Modifier.STATIC) == 0) && ((mods & Modifier.PRIVATE) != 0))
					? meth
					: null;
		}
		catch (NoSuchMethodException ex)
		{
			return null;
		}
	}
	
	/**
     * Returns non-static, non-abstract method with given signature provided it
     * is defined by or accessible (via inheritance) by the given class, or
     * null if no match found.  Access checks are disabled on the returned
     * method (if any).
     */
    private static Method getInheritableMethod(Class cl, String name,
					       Class[] argTypes,
					       Class returnType)
    {
		Method meth = null;
		Class defCl = cl;
		while (defCl != null)
		{
			try
			{
				meth = defCl.getDeclaredMethod(name, argTypes);
				break;
			}
			catch (NoSuchMethodException ex)
			{
				defCl = defCl.getSuperclass();
			}
		}

		if ((meth == null) || (meth.getReturnType() != returnType))
		{
			return null;
		}
		meth.setAccessible(true);
		int mods = meth.getModifiers();
		if ((mods & (Modifier.STATIC | Modifier.ABSTRACT)) != 0)
		{
			return null;
		}
		else if ((mods & (Modifier.PUBLIC | Modifier.PROTECTED)) != 0)
		{
			return meth;
		}
		else if ((mods & Modifier.PRIVATE) != 0)
		{
			return (cl == defCl) ? meth : null;
		}
		else
		{
			return packageEquals(cl, defCl) ? meth : null;
		}
	}

	private static boolean packageEquals(Class cl1, Class cl2)
	{
		return (cl1.getClassLoader() == cl2.getClassLoader() && getPackageName(cl1).equals(
				getPackageName(cl2)));
	}

	/**
	 * Returns package name of given class.
	 */
	private static String getPackageName(Class cl)
	{
		String s = cl.getName();
		int i = s.lastIndexOf('[');
		if (i >= 0)
		{
			s = s.substring(i + 2);
		}
		i = s.lastIndexOf('.');
		return (i >= 0) ? s.substring(0, i) : "";
	}

	private abstract class FieldAndIndex
	{
		final Field field;
		final long index;

		FieldAndIndex(Field field)
		{
			this.field = field;
			this.index = unsafe.objectFieldOffset(field);
		}
		
		public abstract void writeField(Object object, WicketObjectOutputStream dos)  throws IOException;
		
		public abstract void readField(Object object, WicketObjectInputStream dos)  throws IOException, ClassNotFoundException;
	}
	
	private final class BooleanFieldAndIndex extends FieldAndIndex
	{
		BooleanFieldAndIndex(Field field)
		{
			super(field);
		}
		
		/**
		 * @throws IOException 
		 * @see wicket.util.io.ClassStreamHandler.FieldAndIndex#writeField(java.lang.Object)
		 */
		public void writeField(Object object, WicketObjectOutputStream dos) throws IOException
		{
			dos.writeBoolean(unsafe.getBoolean(object, index));
		}
		
		/**
		 * @see wicket.util.io.ClassStreamHandler.FieldAndIndex#readField(java.lang.Object, java.io.WicketObjectInputStream)
		 */
		public void readField(Object object, WicketObjectInputStream dos) throws IOException
		{
			unsafe.putBoolean(object, index, dos.readBoolean());
		}
	}
	
	private final class ByteFieldAndIndex extends FieldAndIndex
	{
		ByteFieldAndIndex(Field field)
		{
			super(field);
		}
		
		/**
		 * @throws IOException 
		 * @see wicket.util.io.ClassStreamHandler.FieldAndIndex#writeField(java.lang.Object)
		 */
		public void writeField(Object object, WicketObjectOutputStream dos) throws IOException
		{
			dos.writeByte(unsafe.getByte(object, index));
		}
		
		/**
		 * @see wicket.util.io.ClassStreamHandler.FieldAndIndex#readField(java.lang.Object, java.io.WicketObjectInputStream)
		 */
		public void readField(Object object, WicketObjectInputStream dos) throws IOException
		{
			unsafe.putByte(object, index, dos.readByte());
		}
	}	

	private final class ShortFieldAndIndex extends FieldAndIndex
	{
		ShortFieldAndIndex(Field field)
		{
			super(field);
		}
		
		/**
		 * @throws IOException 
		 * @see wicket.util.io.ClassStreamHandler.FieldAndIndex#writeField(java.lang.Object)
		 */
		public void writeField(Object object, WicketObjectOutputStream dos) throws IOException
		{
			dos.writeShort(unsafe.getShort(object, index));
		}
		
		/**
		 * @see wicket.util.io.ClassStreamHandler.FieldAndIndex#readField(java.lang.Object, java.io.WicketObjectInputStream)
		 */
		public void readField(Object object, WicketObjectInputStream dos) throws IOException
		{
			unsafe.putShort(object, index, dos.readShort());
		}
	}	
	
	private final class CharFieldAndIndex extends FieldAndIndex
	{
		CharFieldAndIndex(Field field)
		{
			super(field);
		}
		
		/**
		 * @throws IOException 
		 * @see wicket.util.io.ClassStreamHandler.FieldAndIndex#writeField(java.lang.Object)
		 */
		public void writeField(Object object, WicketObjectOutputStream dos) throws IOException
		{
			dos.writeChar(unsafe.getChar(object, index));
		}
		
		/**
		 * @see wicket.util.io.ClassStreamHandler.FieldAndIndex#readField(java.lang.Object, java.io.WicketObjectInputStream)
		 */
		public void readField(Object object, WicketObjectInputStream dos) throws IOException
		{
			unsafe.putChar(object, index, dos.readChar());
		}
	}	
	
	private final class IntFieldAndIndex extends FieldAndIndex
	{
		IntFieldAndIndex(Field field)
		{
			super(field);
		}
		
		/**
		 * @throws IOException 
		 * @see wicket.util.io.ClassStreamHandler.FieldAndIndex#writeField(java.lang.Object)
		 */
		public void writeField(Object object, WicketObjectOutputStream dos) throws IOException
		{
			dos.writeInt(unsafe.getInt(object, index));
		}
		
		/**
		 * @see wicket.util.io.ClassStreamHandler.FieldAndIndex#readField(java.lang.Object, java.io.WicketObjectInputStream)
		 */
		public void readField(Object object, WicketObjectInputStream dos) throws IOException
		{
			unsafe.putInt(object, index, dos.readInt());
		}
	}	
	
	private final class LongFieldAndIndex extends FieldAndIndex
	{
		LongFieldAndIndex(Field field)
		{
			super(field);
		}
		
		/**
		 * @throws IOException 
		 * @see wicket.util.io.ClassStreamHandler.FieldAndIndex#writeField(java.lang.Object)
		 */
		public void writeField(Object object, WicketObjectOutputStream dos) throws IOException
		{
			dos.writeLong(unsafe.getLong(object, index));
		}
		
		/**
		 * @see wicket.util.io.ClassStreamHandler.FieldAndIndex#readField(java.lang.Object, java.io.WicketObjectInputStream)
		 */
		public void readField(Object object, WicketObjectInputStream dos) throws IOException
		{
			unsafe.putLong(object, index, dos.readLong());
		}
	}	
	
	private final class FloatFieldAndIndex extends FieldAndIndex
	{
		FloatFieldAndIndex(Field field)
		{
			super(field);
		}
		
		/**
		 * @throws IOException 
		 * @see wicket.util.io.ClassStreamHandler.FieldAndIndex#writeField(java.lang.Object)
		 */
		public void writeField(Object object, WicketObjectOutputStream dos) throws IOException
		{
			dos.writeFloat(unsafe.getFloat(object, index));
		}
		
		/**
		 * @see wicket.util.io.ClassStreamHandler.FieldAndIndex#readField(java.lang.Object, java.io.WicketObjectInputStream)
		 */
		public void readField(Object object, WicketObjectInputStream dos) throws IOException
		{
			unsafe.putFloat(object, index, dos.readFloat());
		}
	}	
	
	private final class DoubleFieldAndIndex extends FieldAndIndex
	{
		DoubleFieldAndIndex(Field field)
		{
			super(field);
		}
		
		/**
		 * @throws IOException 
		 * @see wicket.util.io.ClassStreamHandler.FieldAndIndex#writeField(java.lang.Object)
		 */
		public void writeField(Object object, WicketObjectOutputStream dos) throws IOException
		{
			dos.writeDouble(unsafe.getDouble(object, index));
		}
		
		/**
		 * @see wicket.util.io.ClassStreamHandler.FieldAndIndex#readField(java.lang.Object, java.io.WicketObjectInputStream)
		 */
		public void readField(Object object, WicketObjectInputStream dos) throws IOException
		{
			unsafe.putDouble(object, index, dos.readDouble());
		}
	}
	
	private final class ObjectFieldAndIndex extends FieldAndIndex
	{
		ObjectFieldAndIndex(Field field)
		{
			super(field);
		}
		
		/**
		 * @throws IOException 
		 * @see wicket.util.io.ClassStreamHandler.FieldAndIndex#writeField(java.lang.Object)
		 */
		public void writeField(Object object, WicketObjectOutputStream dos) throws IOException
		{
			dos.writeObject(unsafe.getObject(object, index));
		}
		
		/**
		 * @throws ClassNotFoundException 
		 * @see wicket.util.io.ClassStreamHandler.FieldAndIndex#readField(java.lang.Object, java.io.WicketObjectInputStream)
		 */
		public void readField(Object object, WicketObjectInputStream dos) throws IOException, ClassNotFoundException
		{
			unsafe.putObject(object, index, dos.readObject());
		}
	}
	

	private abstract class PrimitiveArray
	{
		public abstract void writeArray(Object object, WicketObjectOutputStream dos)  throws IOException;
		
		public abstract Object readArray(WicketObjectInputStream dos)  throws IOException;
	}
	
	private final class BooleanPrimitiveArray extends PrimitiveArray
	{
		/**
		 * @throws IOException 
		 * @see wicket.util.io.ClassStreamHandler.PrimitiveArray#writeArray(java.lang.Object)
		 */
		public void writeArray(Object object, WicketObjectOutputStream dos) throws IOException
		{
			int length = Array.getLength(object);
			dos.writeInt(length);
			for (int i = 0; i < length; i++)
			{
				dos.writeBoolean(Array.getBoolean(object, i));
			}
		}
		
		/**
		 * @see wicket.util.io.ClassStreamHandler.PrimitiveArray#readArray(java.lang.Object, java.io.WicketObjectInputStream)
		 */
		public Object readArray(WicketObjectInputStream dos) throws IOException
		{
			int length = dos.readInt();
			Object array = Array.newInstance(getStreamClass(), length);
			for (int i = 0; i < length; i++)
			{
				Array.setBoolean(array, i, dos.readBoolean());
			}
			return array;
		}
	}
	
	private final class BytePrimitiveArray extends PrimitiveArray
	{
		/**
		 * @throws IOException 
		 * @see wicket.util.io.ClassStreamHandler.PrimitiveArray#writeArray(java.lang.Object)
		 */
		public void writeArray(Object object, WicketObjectOutputStream dos) throws IOException
		{
			int length = Array.getLength(object);
			dos.writeInt(length);
			for (int i = 0; i < length; i++)
			{
				dos.writeByte(Array.getByte(object, i));
			}
		}
		
		/**
		 * @see wicket.util.io.ClassStreamHandler.PrimitiveArray#readArray(java.lang.Object, java.io.WicketObjectInputStream)
		 */
		public Object readArray(WicketObjectInputStream dos) throws IOException
		{
			int length = dos.readInt();
			Object array = Array.newInstance(getStreamClass(), length);
			for (int i = 0; i < length; i++)
			{
				Array.setByte(array, i, dos.readByte());
			}
			return array;
		}
	}	

	private final class ShortPrimitiveArray extends PrimitiveArray
	{
		/**
		 * @throws IOException 
		 * @see wicket.util.io.ClassStreamHandler.PrimitiveArray#writeArray(java.lang.Object)
		 */
		public void writeArray(Object object, WicketObjectOutputStream dos) throws IOException
		{
			int length = Array.getLength(object);
			dos.writeInt(length);
			for (int i = 0; i < length; i++)
			{
				dos.writeShort(Array.getShort(object, i));
			}
		}
		
		/**
		 * @see wicket.util.io.ClassStreamHandler.PrimitiveArray#readArray(java.lang.Object, java.io.WicketObjectInputStream)
		 */
		public Object readArray(WicketObjectInputStream dos) throws IOException
		{
			int length = dos.readInt();
			Object array = Array.newInstance(getStreamClass(), length);
			for (int i = 0; i < length; i++)
			{
				Array.setShort(array, i, dos.readShort());
			}
			return array;
		}
	}	
	
	private final class CharPrimitiveArray extends PrimitiveArray
	{
	
		/**
		 * @throws IOException 
		 * @see wicket.util.io.ClassStreamHandler.PrimitiveArray#writeArray(java.lang.Object)
		 */
		public void writeArray(Object object, WicketObjectOutputStream dos) throws IOException
		{
			int length = Array.getLength(object);
			dos.writeInt(length);
			for (int i = 0; i < length; i++)
			{
				dos.writeChar(Array.getChar(object, i));
			}
		}
		
		/**
		 * @see wicket.util.io.ClassStreamHandler.PrimitiveArray#readArray(java.lang.Object, java.io.WicketObjectInputStream)
		 */
		public Object readArray(WicketObjectInputStream dos) throws IOException
		{
			int length = dos.readInt();
			Object array = Array.newInstance(getStreamClass(), length);
			for (int i = 0; i < length; i++)
			{
				Array.setChar(array, i, dos.readChar());
			}
			return array;
		}
	}	
	
	private final class IntPrimitiveArray extends PrimitiveArray
	{
		/**
		 * @throws IOException 
		 * @see wicket.util.io.ClassStreamHandler.PrimitiveArray#writeArray(java.lang.Object)
		 */
		public void writeArray(Object object, WicketObjectOutputStream dos) throws IOException
		{
			int length = Array.getLength(object);
			dos.writeInt(length);
			for (int i = 0; i < length; i++)
			{
				dos.writeInt(Array.getInt(object, i));
			}
		}
		
		/**
		 * @see wicket.util.io.ClassStreamHandler.PrimitiveArray#readArray(java.lang.Object, java.io.WicketObjectInputStream)
		 */
		public Object readArray(WicketObjectInputStream dos) throws IOException
		{
			int length = dos.readInt();
			Object array = Array.newInstance(getStreamClass(), length);
			for (int i = 0; i < length; i++)
			{
				Array.setInt(array, i, dos.readInt());
			}
			return array;
		}
	}	
	
	private final class LongPrimitiveArray extends PrimitiveArray
	{
		/**
		 * @throws IOException 
		 * @see wicket.util.io.ClassStreamHandler.PrimitiveArray#writeArray(java.lang.Object)
		 */
		public void writeArray(Object object, WicketObjectOutputStream dos) throws IOException
		{
			int length = Array.getLength(object);
			dos.writeInt(length);
			for (int i = 0; i < length; i++)
			{
				dos.writeLong(Array.getLong(object, i));
			}
		}
		
		/**
		 * @see wicket.util.io.ClassStreamHandler.PrimitiveArray#readArray(java.lang.Object, java.io.WicketObjectInputStream)
		 */
		public Object readArray(WicketObjectInputStream dos) throws IOException
		{
			int length = dos.readInt();
			Object array = Array.newInstance(getStreamClass(), length);
			for (int i = 0; i < length; i++)
			{
				Array.setLong(array, i, dos.readLong());
			}
			return array;
		}
	}	
	
	private final class FloatPrimitiveArray extends PrimitiveArray
	{
		/**
		 * @throws IOException 
		 * @see wicket.util.io.ClassStreamHandler.PrimitiveArray#writeArray(java.lang.Object)
		 */
		public void writeArray(Object object, WicketObjectOutputStream dos) throws IOException
		{
			int length = Array.getLength(object);
			dos.writeInt(length);
			for (int i = 0; i < length; i++)
			{
				dos.writeFloat(Array.getFloat(object, i));
			}
		}
		
		/**
		 * @see wicket.util.io.ClassStreamHandler.PrimitiveArray#readArray(java.lang.Object, java.io.WicketObjectInputStream)
		 */
		public Object readArray(WicketObjectInputStream dos) throws IOException
		{
			int length = dos.readInt();
			Object array = Array.newInstance(getStreamClass(), length);
			for (int i = 0; i < length; i++)
			{
				Array.setFloat(array, i, dos.readFloat());
			}
			return array;
		}
	}	
	
	private final class DoublePrimitiveArray extends PrimitiveArray
	{
		/**
		 * @throws IOException 
		 * @see wicket.util.io.ClassStreamHandler.PrimitiveArray#writeArray(java.lang.Object)
		 */
		public void writeArray(Object object, WicketObjectOutputStream dos) throws IOException
		{
			int length = Array.getLength(object);
			dos.writeInt(length);
			for (int i = 0; i < length; i++)
			{
				dos.writeDouble(Array.getDouble(object, i));
			}
		}
		
		/**
		 * @see wicket.util.io.ClassStreamHandler.PrimitiveArray#readArray(java.lang.Object, java.io.WicketObjectInputStream)
		 */
		public Object readArray(WicketObjectInputStream dos) throws IOException
		{
			int length = dos.readInt();
			Object array = Array.newInstance(getStreamClass(), length);
			for (int i = 0; i < length; i++)
			{
				Array.setDouble(array, i, dos.readDouble());
			}
			return array;
		}
	}

	/**
	 * @return
	 * @throws NotSerializableException 
	 */
	public Object writeReplace(Object o) throws NotSerializableException
	{
		if (writeReplaceMethod != null)
		{
			try
			{
				return writeReplaceMethod.invoke(o, null);
			}
			catch (Exception ex)
			{
				throw new NotSerializableException(ex.getMessage());
			} 
		}
		return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/424.java