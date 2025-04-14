error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10131.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10131.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[102,2]

error in qdox parser
file content:
```java
offset: 3787
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10131.java
text:
```scala
import org.apache.commons.lang3.StringUtils;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.jmeter.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JOrphanUtils;
import org.apache.log.Logger;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.PrefixResolverDefault;
import org.w3c.dom.Node;

/**
 * {@link PrefixResolver} implementation that loads prefix configuration from jmeter property xpath.namespace.config
 */
public class PropertiesBasedPrefixResolver extends PrefixResolverDefault {
	private static final Logger logger = LoggingManager.getLoggerForClass();
	private static final String XPATH_NAMESPACE_CONFIG = "xpath.namespace.config";
	private static final Map<String, String> NAMESPACE_MAP = new HashMap<String, String>();
	static {
		String pathToNamespaceConfig = JMeterUtils.getPropDefault(XPATH_NAMESPACE_CONFIG, "");
		if(!StringUtils.isEmpty(pathToNamespaceConfig)) {
			Properties properties = new Properties();
			InputStream inputStream = null;
			try {
				File pathToNamespaceConfigFile = JMeterUtils.findFile(pathToNamespaceConfig);
				if(!pathToNamespaceConfigFile.exists()) {
					logger.error("Cannot find configured file:'"+
							pathToNamespaceConfig+"' in property:'"+XPATH_NAMESPACE_CONFIG+"', file does not exist");
				} else { 
					if(!pathToNamespaceConfigFile.canRead()) {
						logger.error("Cannot read configured file:'"+
								pathToNamespaceConfig+"' in property:'"+XPATH_NAMESPACE_CONFIG+"'");
					} else {
						inputStream = new BufferedInputStream(new FileInputStream(pathToNamespaceConfigFile));
						properties.load(inputStream);
						properties.entrySet();
						for (Map.Entry<Object, Object> entry : properties.entrySet()) {
							NAMESPACE_MAP.put((String) entry.getKey(), (String) entry.getValue());					
						}
						logger.info("Read following XPath namespace configuration "+ 
								NAMESPACE_MAP);
					}
				}
			} catch(IOException e) {
				logger.error("Error loading namespaces from file:'"+
						pathToNamespaceConfig+"', message:"+e.getMessage(),e);
			} finally {
				JOrphanUtils.closeQuietly(inputStream);
			}
		}
	}
	/**
	 * @param xpathExpressionContext Node
	 */
	public PropertiesBasedPrefixResolver(Node xpathExpressionContext) {
		super(xpathExpressionContext);
	}

	/**
	 * Searches prefix in NAMESPACE_MAP, if it fails to find it defaults to parent implementation
	 * @param prefix Prefix
	 * @param namespaceContext Node
	 */
	@Override
	public String getNamespaceForPrefix(String prefix, Node namespaceContext) {
		String namespace = NAMESPACE_MAP.get(prefix);
		if(namespace==null) {
			return super.getNamespaceForPrefix(prefix, namespaceContext);
		} else {
			return namespace;
		}
	}
}
 N@@o newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10131.java