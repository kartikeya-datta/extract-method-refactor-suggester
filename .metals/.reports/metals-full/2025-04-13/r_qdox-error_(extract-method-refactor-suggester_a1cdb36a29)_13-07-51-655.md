error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12245.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12245.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12245.java
text:
```scala
D@@ebugOptions.ENDPOINT_DESCRIPTION_READER,

/*******************************************************************************
 * Copyright (c) 2010-2011 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.osgi.services.remoteserviceadmin;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDCreateException;
import org.eclipse.ecf.core.identity.Namespace;
import org.eclipse.ecf.internal.osgi.services.remoteserviceadmin.DebugOptions;
import org.eclipse.ecf.internal.osgi.services.remoteserviceadmin.EndpointDescriptionParser;
import org.eclipse.ecf.internal.osgi.services.remoteserviceadmin.IDUtil;
import org.eclipse.ecf.internal.osgi.services.remoteserviceadmin.LogUtility;
import org.eclipse.ecf.internal.osgi.services.remoteserviceadmin.PropertiesUtil;
import org.osgi.framework.Constants;

public class EndpointDescriptionReader implements IEndpointDescriptionReader {

	public org.osgi.service.remoteserviceadmin.EndpointDescription[] readEndpointDescriptions(
			InputStream input) throws IOException {
		// First create parser
		EndpointDescriptionParser parser = new EndpointDescriptionParser();
		// Parse input stream
		parser.parse(input);
		// Get possible endpoint descriptions
		List<EndpointDescriptionParser.EndpointDescription> parsedDescriptions = parser
				.getEndpointDescriptions();
		List<org.osgi.service.remoteserviceadmin.EndpointDescription> results = new ArrayList<org.osgi.service.remoteserviceadmin.EndpointDescription>();
		// For each one parsed, get properties and
		for (EndpointDescriptionParser.EndpointDescription ed : parsedDescriptions) {
			Map parsedProperties = ed.getProperties();
			org.osgi.service.remoteserviceadmin.EndpointDescription result = null;
			try {
				// OSGI required properties
				// objectClass/String+
				List<String> objectClasses = PropertiesUtil
						.getStringPlusProperty(parsedProperties,
								Constants.OBJECTCLASS);
				// Must have at least one objectClass
				if (objectClasses == null || objectClasses.size() == 0)
					throw new EndpointDescriptionParseException(
							Constants.OBJECTCLASS
									+ " is not set in endpoint description.  It must be set to String+ value");
				parsedProperties.put(Constants.OBJECTCLASS,
						(String[]) objectClasses
								.toArray(new String[objectClasses.size()]));

				// endpoint.id
				String endpointId = PropertiesUtil
						.getStringWithDefault(
								parsedProperties,
								org.osgi.service.remoteserviceadmin.RemoteConstants.ENDPOINT_ID,
								null);
				// Must have endpoint id, so throw if it's not found
				if (endpointId == null)
					throw new EndpointDescriptionParseException(
							org.osgi.service.remoteserviceadmin.RemoteConstants.ENDPOINT_ID
									+ " is not set in endpoint description.  It must be set to String value");
				parsedProperties
						.put(org.osgi.service.remoteserviceadmin.RemoteConstants.ENDPOINT_ID,
								endpointId);

				// endpoint.service.id. Default is set to Long(0), which means
				// not an OSGi endpoint description
				Long endpointServiceId = PropertiesUtil
						.getLongWithDefault(
								parsedProperties,
								org.osgi.service.remoteserviceadmin.RemoteConstants.ENDPOINT_SERVICE_ID,
								new Long(0));
				parsedProperties
						.put(org.osgi.service.remoteserviceadmin.RemoteConstants.ENDPOINT_SERVICE_ID,
								endpointServiceId);

				// service.imported.configs
				List<String> configurationTypes = PropertiesUtil
						.getStringPlusProperty(
								parsedProperties,
								org.osgi.service.remoteserviceadmin.RemoteConstants.SERVICE_IMPORTED_CONFIGS);
				// Must have at least one service.imported.config
				if (configurationTypes == null
 configurationTypes.size() == 0)
					throw new EndpointDescriptionParseException(
							org.osgi.service.remoteserviceadmin.RemoteConstants.SERVICE_IMPORTED_CONFIGS
									+ " is not set in endpoint description.  It must be set to String+ value)");

				// Create OSGi endpoint description to verify that all OSGi
				// properties are correct/set
				result = new org.osgi.service.remoteserviceadmin.EndpointDescription(
						parsedProperties);

				// If ECF endpoint description, then an endpoint container ID
				// will be non-null
				ID endpointContainerID = getContainerID(parsedProperties);
				// if the endpointContainerID is not found, then this is not an
				// ECF endpoint description
				if (endpointContainerID != null) {
					result = createECFEndpointDescription(endpointContainerID,
							parsedProperties);
				}
				results.add(result);
			} catch (Exception e) {
				LogUtility.logError("readEndpointDescriptions",
						DebugOptions.ENDPOINTDESCRIPTIONREADER,
						this.getClass(),
						"Exception parsing endpoint description properties", e);
			}
		}
		return results.toArray(new EndpointDescription[results.size()]);
	}

	private org.osgi.service.remoteserviceadmin.EndpointDescription createECFEndpointDescription(
			ID endpointContainerID, Map parsedProperties)
			throws EndpointDescriptionParseException {
		Map<String, Object> endointDescriptionProperties = PropertiesUtil
				.copyNonECFProperties(parsedProperties,
						new TreeMap<String, Object>());
		// we get the remote service id...default 0 means that it's not an ECF
		// remote service
		Long remoteServiceId = PropertiesUtil.getLongWithDefault(
				parsedProperties,
				org.eclipse.ecf.remoteservice.Constants.SERVICE_ID, null);
		if (remoteServiceId == null)
			throw new EndpointDescriptionParseException(
					org.eclipse.ecf.remoteservice.Constants.SERVICE_ID
							+ " is not set in endpoint description.  It must be set to Long value");
		endointDescriptionProperties.put(
				org.eclipse.ecf.remoteservice.Constants.SERVICE_ID,
				remoteServiceId);

		// target ID
		ID targetID = null;
		String targetName = (String) parsedProperties
				.get(RemoteConstants.ENDPOINT_CONNECTTARGET_ID);
		String targetNamespace = (String) parsedProperties
				.get(RemoteConstants.ENDPOINT_CONNECTTARGET_ID_NAMESPACE);
		if (targetName != null)
			targetID = IDUtil.createID(targetNamespace, targetName);

		// id filter
		ID[] idFilter = getIDFilter(endpointContainerID.getNamespace(),
				parsedProperties);
		// rs filter
		String rsFilter = (String) parsedProperties
				.get(RemoteConstants.ENDPOINT_REMOTESERVICE_FILTER);

		return new EndpointDescription(endointDescriptionProperties,
				endpointContainerID.getNamespace().getName(), targetID,
				idFilter, rsFilter);
	}

	private ID[] getIDFilter(Namespace namespace, Map<String, Object> properties) {
		List<ID> resultList = new ArrayList();
		Object o = properties.get(RemoteConstants.ENDPOINT_IDFILTER_IDS);
		if (o != null && o instanceof List<?>) {
			// Assumed to be list of strings
			for (String i : (List<String>) o) {
				ID id = IDUtil.createID(namespace, i);
				if (id != null)
					resultList.add(id);
			}
		} else {
			Number countInt = null;
			Object counto = properties
					.get(RemoteConstants.ENDPOINT_IDFILTER_IDARRAY_COUNT);
			if (counto instanceof Number)
				countInt = (Number) counto;
			if (countInt == null)
				return null;
			int count = countInt.intValue();
			if (count <= 0)
				return null;
			for (int i = 0; i < count; i++) {
				// decode string as name
				String name = (String) properties
						.get(RemoteConstants.ENDPOINT_IDFILTER_IDARRAY_NAME_
								+ i);
				if (name != null) {
					String ns = (String) properties
							.get(RemoteConstants.ENDPOINT_IDFILTER_IDARRAY_NAMESPACE_
									+ i);
					if (ns == null)
						ns = namespace.getName();
					ID id = IDUtil.createID(ns, name);
					if (id != null)
						resultList.add(id);
				}
			}
		}
		return (resultList.size() > 0) ? (ID[]) resultList
				.toArray(new ID[resultList.size()]) : null;
	}

	private ID getContainerID(Map<String, Object> properties)
			throws IDCreateException {
		// We try to get the ID from the OSGi id
		String osgiId = PropertiesUtil
				.verifyStringProperty(
						properties,
						org.osgi.service.remoteserviceadmin.RemoteConstants.ENDPOINT_ID);
		if (osgiId == null)
			throw new IDCreateException(
					org.osgi.service.remoteserviceadmin.RemoteConstants.ENDPOINT_ID
							+ " must not be null");
		String containerIDNamespace = PropertiesUtil.verifyStringProperty(
				properties, RemoteConstants.ENDPOINT_CONTAINER_ID_NAMESPACE);
		return IDUtil.createID(properties, containerIDNamespace);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12245.java