error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7207.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7207.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7207.java
text:
```scala
R@@emoteConstants.DISCOVERY_SERVICE_TYPE });

/*******************************************************************************
 * Copyright (c) 2010-2011 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.internal.osgi.services.remoteserviceadmin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.eclipse.ecf.osgi.services.remoteserviceadmin.RemoteConstants;
import org.eclipse.ecf.remoteservice.IRemoteServiceReference;
import org.eclipse.ecf.remoteservice.IRemoteServiceRegistration;
import org.osgi.framework.ServiceReference;

public class PropertiesUtil {

	protected static final List osgiProperties = Arrays
			.asList(new String[] {
					// OSGi properties
					org.osgi.framework.Constants.OBJECTCLASS,
					org.osgi.framework.Constants.SERVICE_ID,
					org.osgi.service.remoteserviceadmin.RemoteConstants.ENDPOINT_FRAMEWORK_UUID,
					org.osgi.service.remoteserviceadmin.RemoteConstants.ENDPOINT_ID,
					org.osgi.service.remoteserviceadmin.RemoteConstants.ENDPOINT_SERVICE_ID,
					org.osgi.service.remoteserviceadmin.RemoteConstants.REMOTE_CONFIGS_SUPPORTED,
					org.osgi.service.remoteserviceadmin.RemoteConstants.REMOTE_INTENTS_SUPPORTED,
					org.osgi.service.remoteserviceadmin.RemoteConstants.SERVICE_EXPORTED_CONFIGS,
					org.osgi.service.remoteserviceadmin.RemoteConstants.SERVICE_EXPORTED_INTENTS,
					org.osgi.service.remoteserviceadmin.RemoteConstants.SERVICE_EXPORTED_INTENTS_EXTRA,
					org.osgi.service.remoteserviceadmin.RemoteConstants.SERVICE_EXPORTED_INTERFACES,
					org.osgi.service.remoteserviceadmin.RemoteConstants.SERVICE_IMPORTED,
					org.osgi.service.remoteserviceadmin.RemoteConstants.SERVICE_IMPORTED_CONFIGS,
					org.osgi.service.remoteserviceadmin.RemoteConstants.SERVICE_INTENTS });

	protected static final List ecfProperties = Arrays.asList(new String[] {
			// ECF properties
			org.eclipse.ecf.remoteservice.Constants.OBJECTCLASS,
			org.eclipse.ecf.remoteservice.Constants.SERVICE_ID,
			RemoteConstants.DISCOVERY_DEFAULT_SERVICE_NAME_PREFIX,
			RemoteConstants.DISCOVERY_NAMING_AUTHORITY,
			RemoteConstants.DISCOVERY_PROTOCOLS,
			RemoteConstants.DISCOVERY_SCOPE,
			RemoteConstants.DISCOVERY_SERVICE_NAME,
			RemoteConstants.ENDPOINT_CONNECTTARGET_ID,
			RemoteConstants.ENDPOINT_CONTAINER_ID_NAMESPACE,
			RemoteConstants.ENDPOINT_IDFILTER_IDS,
			RemoteConstants.ENDPOINT_REMOTESERVICE_FILTER,
			RemoteConstants.ENDPOINT_SERVICE_IMPORTED_CONFIGS_VALUE,
			RemoteConstants.SERVICE_EXPORTED_CONTAINER_CONNECT_CONTEXT,
			RemoteConstants.SERVICE_EXPORTED_CONTAINER_FACTORY_ARGS,
			RemoteConstants.SERVICE_EXPORTED_CONTAINER_ID,
			RemoteConstants.SERVICE_IMPORTED_VALUETYPE,
			RemoteConstants.SERVICE_TYPE });

	public static String verifyStringProperty(Map properties, String propName) {
		Object r = properties.get(propName);
		try {
			return (String) r;
		} catch (ClassCastException e) {
			IllegalArgumentException iae = new IllegalArgumentException(
					"property value is not a String: " + propName); //$NON-NLS-1$
			iae.initCause(e);
			throw iae;
		}
	}

	public static Object convertToStringPlusValue(List<String> values) {
		if (values == null)
			return null;
		int valuesSize = values.size();
		switch (valuesSize) {
		case 0:
			return null;
		case 1:
			return values.get(0);
		default:
			return values.toArray(new String[valuesSize]);
		}
	}

	public static String[] getStringArrayFromPropertyValue(Object value) {
		if (value == null)
			return null;
		else if (value instanceof String)
			return new String[] { (String) value };
		else if (value instanceof String[])
			return (String[]) value;
		else if (value instanceof Collection)
			return (String[]) ((Collection) value).toArray(new String[] {});
		else
			return null;
	}

	public static String[] getExportedInterfaces(
			ServiceReference serviceReference) {
		// Get the OSGi 4.2 specified required service property value
		Object propValue = serviceReference
				.getProperty(org.osgi.service.remoteserviceadmin.RemoteConstants.SERVICE_EXPORTED_INTERFACES);
		// If the required property is not set then it's not being registered
		// as a remote service so we return null
		if (propValue == null)
			return null;
		boolean wildcard = propValue.equals("*"); //$NON-NLS-1$
		if (wildcard)
			return (String[]) serviceReference
					.getProperty(org.osgi.framework.Constants.OBJECTCLASS);
		else {
			final String[] stringValue = getStringArrayFromPropertyValue(propValue);
			if (stringValue != null && stringValue.length == 1
					&& stringValue[0].equals("*")) { //$NON-NLS-1$
				LogUtility
						.logWarning(
								"getExportedInterfaces", //$NON-NLS-1$
								DebugOptions.TOPOLOGY_MANAGER,
								PropertiesUtil.class,
								"Service Exported Interfaces Wildcard does not accept String[\"*\"]"); //$NON-NLS-1$
			}
			return stringValue;
		}
	}

	public static String[] getServiceIntents(ServiceReference serviceReference,
			Map overridingProperties) {
		List results = new ArrayList();

		String[] intents = getStringArrayFromPropertyValue(overridingProperties
				.get(org.osgi.service.remoteserviceadmin.RemoteConstants.SERVICE_INTENTS));
		if (intents == null) {
			intents = getStringArrayFromPropertyValue(serviceReference
					.getProperty(org.osgi.service.remoteserviceadmin.RemoteConstants.SERVICE_INTENTS));
		}
		if (intents != null)
			results.addAll(Arrays.asList(intents));

		String[] exportedIntents = getStringArrayFromPropertyValue(overridingProperties
				.get(org.osgi.service.remoteserviceadmin.RemoteConstants.SERVICE_EXPORTED_INTENTS));
		if (exportedIntents == null) {
			exportedIntents = getStringArrayFromPropertyValue(serviceReference
					.getProperty(org.osgi.service.remoteserviceadmin.RemoteConstants.SERVICE_EXPORTED_INTENTS));
		}
		if (exportedIntents != null)
			results.addAll(Arrays.asList(exportedIntents));

		String[] extraIntents = getStringArrayFromPropertyValue(overridingProperties
				.get(org.osgi.service.remoteserviceadmin.RemoteConstants.SERVICE_EXPORTED_INTENTS_EXTRA));
		if (extraIntents == null) {
			extraIntents = getStringArrayFromPropertyValue(serviceReference
					.getProperty(org.osgi.service.remoteserviceadmin.RemoteConstants.SERVICE_EXPORTED_INTENTS_EXTRA));
		}
		if (extraIntents != null)
			results.addAll(Arrays.asList(extraIntents));

		if (results.size() == 0)
			return null;
		return (String[]) results.toArray(new String[results.size()]);
	}

	public static List getStringPlusProperty(Map properties, String key) {
		Object value = properties.get(key);
		if (value == null) {
			return Collections.EMPTY_LIST;
		}

		if (value instanceof String) {
			return Collections.singletonList((String) value);
		}

		if (value instanceof String[]) {
			String[] values = (String[]) value;
			List result = new ArrayList(values.length);
			for (int i = 0; i < values.length; i++) {
				if (values[i] != null) {
					result.add(values[i]);
				}
			}
			return Collections.unmodifiableList(result);
		}

		if (value instanceof Collection) {
			Collection values = (Collection) value;
			List result = new ArrayList(values.size());
			for (Iterator iter = values.iterator(); iter.hasNext();) {
				Object v = iter.next();
				if (v instanceof String) {
					result.add((String) v);
				}
			}
			return Collections.unmodifiableList(result);
		}

		return Collections.EMPTY_LIST;
	}

	public static Object getPropertyValue(ServiceReference serviceReference,
			String key) {
		return (serviceReference == null) ? null : serviceReference
				.getProperty(key);
	}

	public static Object getPropertyValue(ServiceReference serviceReference,
			Map<String, Object> overridingProperties, String key) {
		Object result = null;
		if (overridingProperties != null)
			result = overridingProperties.get(key);
		return (result != null) ? result : getPropertyValue(serviceReference,
				key);
	}

	public static boolean isOSGiProperty(String key) {
		return osgiProperties.contains(key)
 key.startsWith(org.osgi.service.remoteserviceadmin.RemoteConstants.ENDPOINT_PACKAGE_VERSION_);
	}

	public static boolean isECFProperty(String key) {
		return ecfProperties.contains(key);
	}

	public static boolean isReservedProperty(String key) {
		return isOSGiProperty(key) || isECFProperty(key);
	}

	public static Map createMapFromDictionary(Dictionary input) {
		if (input == null)
			return null;
		Map result = new HashMap();
		for (Enumeration e = input.keys(); e.hasMoreElements();) {
			Object key = e.nextElement();
			Object val = input.get(key);
			result.put(key, val);
		}
		return result;
	}

	public static Dictionary createDictionaryFromMap(Map propMap) {
		if (propMap == null)
			return null;
		Dictionary result = new Properties();
		for (Iterator i = propMap.keySet().iterator(); i.hasNext();) {
			Object key = i.next();
			Object val = propMap.get(key);
			result.put(key, val);
		}
		return result;
	}

	public static Long getLongWithDefault(Map props, String key, Long def) {
		Object o = props.get(key);
		if (o instanceof Long)
			return (Long) o;
		if (o instanceof String)
			return Long.valueOf((String) o);
		return def;
	}

	public static String[] getStringArrayWithDefault(
			Map<String, Object> properties, String key, String[] def) {
		Object o = properties.get(key);
		if (o instanceof String) {
			return new String[] { (String) o };
		} else if (o instanceof String[]) {
			return (String[]) o;
		} else if (o instanceof List) {
			List l = (List) o;
			return (String[]) l.toArray(new String[l.size()]);
		}
		return def;
	}

	public static String getStringWithDefault(Map props, String key, String def) {
		Object o = props.get(key);
		if (o == null || (!(o instanceof String)))
			return def;
		return (String) o;
	}

	public static Map<String, Object> copyProperties(
			IRemoteServiceRegistration rsRegistration,
			Map<String, Object> target) {
		String[] keys = rsRegistration.getPropertyKeys();
		for (int i = 0; i < keys.length; i++)
			target.put(keys[i], rsRegistration.getProperty(keys[i]));
		return target;
	}

	public static Map<String, Object> copyProperties(
			Map<String, Object> source, Map<String, Object> target) {
		for (String key : source.keySet())
			target.put(key, source.get(key));
		return target;
	}

	public static Map<String, Object> copyNonECFProperties(
			Map<String, Object> source, Map<String, Object> target) {
		for (String key : source.keySet())
			if (!isECFProperty(key))
				target.put(key, source.get(key));
		return target;
	}

	public static Map<String, Object> copyNonReservedProperties(
			Map<String, Object> source, Map<String, Object> target) {
		for (String key : source.keySet())
			if (!isReservedProperty(key))
				target.put(key, source.get(key));
		return target;
	}

	public static Map<String, Object> copyNonECFProperties(
			ServiceReference serviceReference, Map<String, Object> target) {
		String[] keys = serviceReference.getPropertyKeys();
		for (int i = 0; i < keys.length; i++)
			if (!isECFProperty(keys[i]))
				target.put(keys[i], serviceReference.getProperty(keys[i]));
		return target;
	}

	public static Map<String, Object> copyNonReservedProperties(
			ServiceReference serviceReference, Map<String, Object> target) {
		String[] keys = serviceReference.getPropertyKeys();
		for (int i = 0; i < keys.length; i++)
			if (!isReservedProperty(keys[i]))
				target.put(keys[i], serviceReference.getProperty(keys[i]));
		return target;
	}

	public static Map<String, Object> copyNonReservedProperties(
			IRemoteServiceReference rsReference, Map<String, Object> target) {
		String[] keys = rsReference.getPropertyKeys();
		for (int i = 0; i < keys.length; i++)
			if (!isReservedProperty(keys[i]))
				target.put(keys[i], rsReference.getProperty(keys[i]));
		return target;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7207.java