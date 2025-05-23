error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14194.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14194.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14194.java
text:
```scala
final O@@bject ranking = (props == null) ? null : props.get(RemoteServiceRegistryImpl.REMOTESERVICE_RANKING);

/*******************************************************************************
 * Copyright (c) 2004 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.provider.remoteservice.generic;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.remoteservice.*;

/**
 * @since 3.0
 */
public class RemoteServiceRegistrationImpl implements IRemoteServiceRegistration, Serializable {

	private static final long serialVersionUID = -3206899332723536545L;

	transient Object service;

	/** service classes for this registration. */
	protected String[] clazzes;

	/** properties for this registration. */
	protected Properties properties;

	/** service ranking. */
	protected int serviceranking;

	/* internal object to use for synchronization */
	transient protected Object registrationLock = new Object();

	/** The registration state */
	protected int state = REGISTERED;

	public static final int REGISTERED = 0x00;

	public static final int UNREGISTERING = 0x01;

	public static final int UNREGISTERED = 0x02;

	protected transient RemoteServiceReferenceImpl reference = null;

	protected transient RegistrySharedObject sharedObject = null;

	/**
	 * @since 3.0
	 */
	protected IRemoteServiceID remoteServiceID;

	public RemoteServiceRegistrationImpl() {
		//

	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (!(o.getClass().equals(this.getClass())))
			return false;
		return getID().equals(((RemoteServiceRegistrationImpl) o).getID());
	}

	public int hashCode() {
		return getID().hashCode();
	}

	public void publish(RegistrySharedObject sharedObject1, RemoteServiceRegistryImpl registry, Object svc, String[] clzzes, Dictionary props) {
		this.sharedObject = sharedObject1;
		this.service = svc;
		this.clazzes = clzzes;
		this.reference = new RemoteServiceReferenceImpl(this);
		synchronized (registry) {
			this.remoteServiceID = registry.createRemoteServiceID(registry.getNextServiceId());
			this.properties = createProperties(props);
			registry.publishService(this);
		}
	}

	public Object getService() {
		return service;
	}

	public ID getContainerID() {
		return (remoteServiceID == null) ? null : remoteServiceID.getContainerID();
	}

	protected String[] getClasses() {
		return clazzes;
	}

	public IRemoteServiceReference getReference() {
		if (reference == null) {
			synchronized (this) {
				reference = new RemoteServiceReferenceImpl(this);
			}
		}
		return reference;
	}

	public void setProperties(Dictionary properties) {
		synchronized (registrationLock) {
			/* in the process of unregistering */
			if (state != REGISTERED) {
				throw new IllegalStateException("Service already registered"); //$NON-NLS-1$
			}
			this.properties = createProperties(properties);
		}

		// XXX Need to notify that registration modified
	}

	public void unregister() {
		if (sharedObject != null) {
			sharedObject.sendUnregister(this);
		}
	}

	/**
	 * Construct a properties object from the dictionary for this
	 * ServiceRegistration.
	 * 
	 * @param props
	 *            The properties for this service.
	 * @return A Properties object for this ServiceRegistration.
	 */
	protected Properties createProperties(Dictionary props) {
		final Properties resultProps = new Properties(props);

		resultProps.setProperty(RemoteServiceRegistryImpl.REMOTEOBJECTCLASS, clazzes);

		resultProps.setProperty(RemoteServiceRegistryImpl.REMOTESERVICE_ID, new Long(getID().getContainerRelativeID()));

		final Object ranking = props.get(RemoteServiceRegistryImpl.REMOTESERVICE_RANKING);

		serviceranking = (ranking instanceof Integer) ? ((Integer) ranking).intValue() : 0;

		return (resultProps);
	}

	static class Properties extends Hashtable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -3684607010228779249L;

		/**
		 * Create a properties object for the service.
		 * 
		 * @param props
		 *            The properties for this service.
		 */
		private Properties(int size, Dictionary props) {
			super((size << 1) + 1);

			if (props != null) {
				synchronized (props) {
					final Enumeration keysEnum = props.keys();

					while (keysEnum.hasMoreElements()) {
						final Object key = keysEnum.nextElement();

						if (key instanceof String) {
							final String header = (String) key;

							setProperty(header, props.get(header));
						}
					}
				}
			}
		}

		/**
		 * Create a properties object for the service.
		 * 
		 * @param props
		 *            The properties for this service.
		 */
		protected Properties(Dictionary props) {
			this((props == null) ? 2 : Math.max(2, props.size()), props);
		}

		/**
		 * Get a clone of the value of a service's property.
		 * 
		 * @param key
		 *            header name.
		 * @return Clone of the value of the property or <code>null</code> if
		 *         there is no property by that name.
		 */
		protected Object getProperty(String key) {
			return (cloneValue(get(key)));
		}

		/**
		 * Get the list of key names for the service's properties.
		 * 
		 * @return The list of property key names.
		 */
		protected synchronized String[] getPropertyKeys() {
			final int size = size();

			final String[] keynames = new String[size];

			final Enumeration keysEnum = keys();

			for (int i = 0; i < size; i++) {
				keynames[i] = (String) keysEnum.nextElement();
			}

			return (keynames);
		}

		/**
		 * Put a clone of the property value into this property object.
		 * 
		 * @param key
		 *            Name of property.
		 * @param value
		 *            Value of property.
		 * @return previous property value.
		 */
		protected synchronized Object setProperty(String key, Object value) {
			return (put(key, cloneValue(value)));
		}

		/**
		 * Attempt to clone the value if necessary and possible.
		 * 
		 * For some strange reason, you can test to see of an Object is
		 * Cloneable but you can't call the clone method since it is protected
		 * on Object!
		 * 
		 * @param value
		 *            object to be cloned.
		 * @return cloned object or original object if we didn't clone it.
		 */
		protected static Object cloneValue(Object value) {
			if (value == null) {
				return null;
			}
			if (value instanceof String) {
				return (value);
			}

			final Class clazz = value.getClass();
			if (clazz.isArray()) {
				// Do an array copy
				final Class type = clazz.getComponentType();
				final int len = Array.getLength(value);
				final Object clonedArray = Array.newInstance(type, len);
				System.arraycopy(value, 0, clonedArray, 0, len);
				return clonedArray;
			}
			// must use reflection because Object clone method is protected!!
			try {
				return (clazz.getMethod("clone", (Class[]) null).invoke(value, (Object[]) null)); //$NON-NLS-1$
			} catch (final Exception e) {
				/* clone is not a public method on value's class */
			} catch (final Error e) {
				/* JCL does not support reflection; try some well known types */
				if (value instanceof Vector) {
					return (((Vector) value).clone());
				}
				if (value instanceof Hashtable) {
					return (((Hashtable) value).clone());
				}
			}
			return (value);
		}

		public synchronized String toString() {
			final String keys[] = getPropertyKeys();

			final int size = keys.length;

			final StringBuffer sb = new StringBuffer(20 * size);

			sb.append('{');

			int n = 0;
			for (int i = 0; i < size; i++) {
				final String key = keys[i];
				if (!key.equals(RemoteServiceRegistryImpl.REMOTEOBJECTCLASS)) {
					if (n > 0) {
						sb.append(", "); //$NON-NLS-1$
					}

					sb.append(key);
					sb.append('=');
					final Object value = get(key);
					if (value.getClass().isArray()) {
						sb.append('[');
						final int length = Array.getLength(value);
						for (int j = 0; j < length; j++) {
							if (j > 0) {
								sb.append(',');
							}
							sb.append(Array.get(value, j));
						}
						sb.append(']');
					} else {
						sb.append(value);
					}
					n++;
				}
			}

			sb.append('}');

			return (sb.toString());
		}
	}

	public Object getProperty(String key) {
		return properties.getProperty(key);
	}

	public String[] getPropertyKeys() {
		return properties.getPropertyKeys();
	}

	public long getServiceId() {
		IRemoteServiceID rsID = getID();
		if (rsID == null)
			return 0L;
		return rsID.getContainerRelativeID();
	}

	public Object callService(RemoteCallImpl call) throws Exception {
		return call.invoke(service);
	}

	public String toString() {
		StringBuffer buf = new StringBuffer("RemoteServiceRegistrationImpl["); //$NON-NLS-1$
		buf.append("remoteServiceID=").append(getID()).append(";"); //$NON-NLS-1$ //$NON-NLS-2$
		buf.append("containerID=").append(getContainerID()).append(";"); //$NON-NLS-1$ //$NON-NLS-2$
		buf.append("serviceid=").append(getID().getContainerRelativeID()).append(";"); //$NON-NLS-1$ //$NON-NLS-2$
		buf.append("serviceranking=").append(serviceranking).append(";"); //$NON-NLS-1$ //$NON-NLS-2$
		buf.append("classes=").append(Arrays.asList(clazzes)).append(";"); //$NON-NLS-1$ //$NON-NLS-2$
		buf.append("state=").append(state).append(";"); //$NON-NLS-1$ //$NON-NLS-2$
		buf.append("properties=").append(properties).append("]"); //$NON-NLS-1$ //$NON-NLS-2$
		return buf.toString();
	}

	/**
	 * @since 3.0
	 */
	public IRemoteServiceID getID() {
		return this.remoteServiceID;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14194.java