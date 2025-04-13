error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18306.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18306.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18306.java
text:
```scala
private static final L@@ogger log = LoggingManager.getLoggerForClass();

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

/*
 * Created on Jun 13, 2003
 */
package org.apache.jmeter.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JOrphanUtils;
import org.apache.log.Logger;

public final class NameUpdater {
	private static Properties nameMap;

	private static Logger log = LoggingManager.getLoggerForClass();

	static {
		nameMap = new Properties();
		FileInputStream fis = null;
		File f = new File(JMeterUtils.getJMeterHome(),
				JMeterUtils.getPropDefault("upgrade_properties", // $NON-NLS-1$
						"/bin/upgrade.properties")); // $NON-NLS-1$
		try {
			fis = new FileInputStream(f);
			nameMap.load(fis);
		} catch (FileNotFoundException e) {
			log.error("Could not find upgrade file: ", e);
		} catch (IOException e) {
			log.error("Error processing upgrade file: "+f.getPath(), e);
		} finally {
			JOrphanUtils.closeQuietly(fis);
		}
	}

	public static String getCurrentName(String className) {
		if (nameMap.containsKey(className)) {
			String newName = nameMap.getProperty(className);
			log.info("Upgrading class " + className + " to " + newName);
			return newName;
		}
		return className;
	}
    /**
     * Looks up test element / gui class combination; if that
     * does not exist in the map, then defaults to getCurrentName.
     * 
     * @param testClassName - test element class name
     * @param guiClassName - associated gui class name
     * @return new test class name
     */

    public static String getCurrentTestName(String testClassName, String guiClassName) {
        String key = testClassName + "|" + guiClassName;
        if (nameMap.containsKey(key)) {
            String newName = nameMap.getProperty(key);
            log.info("Upgrading " + key + " to " + newName);
            return newName;
        }
        return getCurrentName(testClassName);
    }

	public static String getCurrentName(String propertyName, String className) {
		String key = className + "/" + propertyName;
		if (nameMap.containsKey(key)) {
			String newName = nameMap.getProperty(key);
			log.info("Upgrading property " + propertyName + " to " + newName);
			return newName;
		}
		return propertyName;
	}

	public static String getCurrentName(String value, String propertyName, String className) {
		String key = className + "." + propertyName + "/" + value;
		if (nameMap.containsKey(key)) {
			String newValue = nameMap.getProperty(key);
			log.info("Upgrading value " + value + " to " + newValue);
			return newValue;
		}
		return value;
	}

	/**
	 * Private constructor to prevent instantiation.
	 */
	private NameUpdater() {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18306.java