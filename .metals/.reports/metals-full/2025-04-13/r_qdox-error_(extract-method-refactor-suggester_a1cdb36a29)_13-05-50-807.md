error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13752.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13752.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13752.java
text:
```scala
private static final S@@tring SERVICE_ID_NAME = "x-" + ECLIPSE_ENTERPRISE_NUMBER + "-SERVICEIDNAME"; //$NON-NLS-1$ //$NON-NLS-2$

/*******************************************************************************
 * Copyright (c) 2008 Versant Corp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Markus Kuppe (mkuppe <at> versant <dot> com) - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.internal.provider.jslp;

import java.util.*;
import org.eclipse.core.runtime.Assert;
import org.eclipse.ecf.core.util.StringUtils;
import org.eclipse.ecf.core.util.Trace;
import org.eclipse.ecf.discovery.*;
import org.eclipse.ecf.discovery.identity.IServiceID;

/**
 * Adapts SLP's service properties to ECF's ServiceProperties and vice versa
 * @see "http://www.ietf.org/rfc/rfc2608.txt page. 10ff"
 */
public class ServicePropertiesAdapter {
	// http://www.iana.org/assignments/enterprise-numbers
	private static final String ECLIPSE_ENTERPRISE_NUMBER = "28392"; //$NON-NLS-1$

	/**
	 * SLP attribute key for org.eclipse.ecf.discovery.identity.IServiceID.getServiceName()
	 */
	private static final String SERVICE_ID_NAME = "x-" + ECLIPSE_ENTERPRISE_NUMBER + "-SERVICE_ID_NAME"; //$NON-NLS-1$ //$NON-NLS-2$
	/**
	 * SLP attribute key for org.eclipse.ecf.discovery.IServiceInfo.getPriority()
	 */
	private static final String PRIORITY = "x-" + ECLIPSE_ENTERPRISE_NUMBER + "-PRIORITY"; //$NON-NLS-1$ //$NON-NLS-2$
	/**
	 * SLP attribute key for org.eclipse.ecf.discovery.IServiceInfo.getWeight()
	 */
	private static final String WEIGHT = "x-" + ECLIPSE_ENTERPRISE_NUMBER + "-WEIGHT"; //$NON-NLS-1$ //$NON-NLS-2$

	private static final String SLP_BYTE_PREFIX = "\\FF"; //$NON-NLS-1$
	private IServiceProperties serviceProperties;

	private String serviceName;
	private int priority = ServiceInfo.DEFAULT_PRIORITY;
	private int weight = ServiceInfo.DEFAULT_WEIGHT;

	public ServicePropertiesAdapter(List aList) {
		Assert.isNotNull(aList);
		serviceProperties = new ServiceProperties();
		for (Iterator itr = aList.iterator(); itr.hasNext();) {
			String[] str = StringUtils.split((String) itr.next(), "="); //$NON-NLS-1$
			if (str.length != 2) {
				Trace.trace(Activator.PLUGIN_ID, "/debug/methods/tracing", this.getClass(), "ServicePropertiesAdapter(List)", "Skipped broken service attribute " + str); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
				continue;
			}
			// remove the brackets "( )" from the string value which are added by jSLP for the LDAP style string representation
			String key = str[0].substring(1);
			String value = str[1].substring(0, str[1].length() - 1);
			if (key.equalsIgnoreCase(SERVICE_ID_NAME)) {
				serviceName = value;
			} else if (key.equalsIgnoreCase(PRIORITY)) {
				priority = Integer.parseInt(value);
			} else if (key.equalsIgnoreCase(WEIGHT)) {
				weight = Integer.parseInt(value);
			} else if (value.startsWith(SLP_BYTE_PREFIX)) {
				String[] strs = StringUtils.split(value.substring(4), "\\"); //$NON-NLS-1$
				byte[] b = new byte[strs.length];
				for (int i = 0; i < strs.length; i++) {
					b[i] = new Byte(strs[i]).byteValue();
				}
				serviceProperties.setPropertyBytes(key, b);
			} else if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) { //$NON-NLS-1$ //$NON-NLS-2$
				serviceProperties.setProperty(key, new Boolean(value));
			} else if (isInteger(value)) {
				serviceProperties.setProperty(key, new Integer(value));
			} else {
				serviceProperties.setProperty(key, value);
			}
		}
	}

	public ServicePropertiesAdapter(IServiceInfo sInfo) {
		Assert.isNotNull(sInfo);
		IServiceID sID = sInfo.getServiceID();
		Assert.isNotNull(sID);
		IServiceProperties sp = sInfo.getServiceProperties();
		Assert.isNotNull(sp);

		serviceProperties = new ServiceProperties(sp);
		if (sInfo.getPriority() >= 0) {
			serviceProperties.setPropertyString(PRIORITY, new Integer(sInfo.getPriority()).toString());
		}
		if (sInfo.getWeight() >= 0) {
			serviceProperties.setPropertyString(WEIGHT, new Integer(sInfo.getWeight()).toString());
		}
		if (sID.getServiceName() != null) {
			serviceProperties.setPropertyString(SERVICE_ID_NAME, sID.getServiceName());
		}
	}

	private boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public IServiceProperties toServiceProperties() {
		return serviceProperties;
	}

	public Dictionary toProperties() {
		Dictionary dict = new Properties();
		Enumeration propertyNames = serviceProperties.getPropertyNames();
		while (propertyNames.hasMoreElements()) {
			String key = (String) propertyNames.nextElement();
			byte[] propertyBytes = serviceProperties.getPropertyBytes(key);
			if (propertyBytes != null) {
				StringBuffer buf = new StringBuffer();
				buf.append(SLP_BYTE_PREFIX);
				for (int i = 0; i < propertyBytes.length; i++) {
					buf.append("\\"); //$NON-NLS-1$
					buf.append(propertyBytes[i]);
				}
				dict.put(key, buf.toString());
			} else {
				dict.put(key, serviceProperties.getProperty(key).toString());
			}
		}
		return dict;
	}

	/**
	 * @return weight or -1 for unset
	 */
	public int getWeight() {
		return weight;
	}

	/**
	 * @return priority or -1 for unset
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * @return Service name or null
	 */
	public String getServiceName() {
		return serviceName;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13752.java