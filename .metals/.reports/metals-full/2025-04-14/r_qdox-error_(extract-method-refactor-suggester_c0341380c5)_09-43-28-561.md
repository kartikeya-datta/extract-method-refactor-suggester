error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12434.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12434.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12434.java
text:
```scala
public final static S@@tring BASEDIR = "ReportPlan.basedir";

//$Header$
/*
 * Copyright 2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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

package org.apache.jmeter.testelement;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.ConfigElement;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.services.FileServer;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.StringProperty;
import org.apache.jmeter.testelement.property.TestElementProperty;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * @author Peter Lin created 8/11
 * @version $Revision$ Last updated: $Date$
 */
public class ReportPlan extends AbstractTestElement implements Serializable, TestListener {
	private static Logger log = LoggingManager.getLoggerForClass();

	public final static String REPORT_PAGE = "ReportPlan.report_page";

	public final static String USER_DEFINED_VARIABLES = "ReportPlan.user_defined_variables";

	public final static String COMMENTS = "ReportPlan.comments";

	public final static String BASEDIR = "basedir";

	private transient List reportPages = new LinkedList();

	private transient List configs = new LinkedList();

	private static List itemsCanAdd = new LinkedList();

	private static ReportPlan plan;

	// There's only 1 test plan, so can cache the mode here
	private static boolean functionalMode = false;

	static {
		itemsCanAdd.add(JMeterUtils.getResString("report_page"));
	}

	public ReportPlan() {
		this(JMeterUtils.getResString("report_plan"));
	}

	public ReportPlan(String name) {
		setName(name);
		setProperty(new CollectionProperty(REPORT_PAGE, reportPages));
	}

	public void setUserDefinedVariables(Arguments vars) {
		setProperty(new TestElementProperty(USER_DEFINED_VARIABLES, vars));
	}

	public String getBasedir() {
		return getPropertyAsString(BASEDIR);
	}

	public void setBasedir(String b) {
		setProperty(BASEDIR, b);
	}

	public Map getUserDefinedVariables() {
		Arguments args = getVariables();
		return args.getArgumentsAsMap();
	}

	private Arguments getVariables() {
		Arguments args = (Arguments) getProperty(USER_DEFINED_VARIABLES).getObjectValue();
		if (args == null) {
			args = new Arguments();
			setUserDefinedVariables(args);
		}
		return args;
	}

	/**
	 * Gets the static copy of the functional mode
	 * 
	 * @return mode
	 */
	public static boolean getFunctionalMode() {
		return functionalMode;
	}

	public void addParameter(String name, String value) {
		getVariables().addArgument(name, value);
	}

	public static ReportPlan createReportPlan(String name) {
		if (plan == null) {
			if (name == null) {
				plan = new ReportPlan();
			} else {
				plan = new ReportPlan(name);
			}
			plan.setProperty(new StringProperty(TestElement.GUI_CLASS, "org.apache.jmeter.control.gui.ReportGui"));
		}
		return plan;
	}

	public void addTestElement(TestElement tg) {
		super.addTestElement(tg);
		if (tg instanceof ThreadGroup && !isRunningVersion()) {
			addReportPage((ThreadGroup) tg);
		}
	}

	public void addJMeterComponent(TestElement child) {
		if (child instanceof ThreadGroup) {
			addReportPage((ThreadGroup) child);
		}
	}

	/**
	 * Gets the ThreadGroups attribute of the TestPlan object.
	 * 
	 * @return the ThreadGroups value
	 */
	public Collection getReportPages() {
		return reportPages;
	}

	/**
	 * Adds a feature to the ConfigElement attribute of the TestPlan object.
	 * 
	 * @param c
	 *            the feature to be added to the ConfigElement attribute
	 */
	public void addConfigElement(ConfigElement c) {
		configs.add(c);
	}

	/**
	 * Adds a feature to the ThreadGroup attribute of the TestPlan object.
	 * 
	 * @param group
	 *            the feature to be added to the ThreadGroup attribute
	 */
	public void addReportPage(ThreadGroup group) {
		reportPages.add(group);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.jmeter.testelement.TestListener#testEnded()
	 */
	public void testEnded() {
		try {
			FileServer.getFileServer().closeFiles();
		} catch (IOException e) {
			log.error("Problem closing files at end of test", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.jmeter.testelement.TestListener#testEnded(java.lang.String)
	 */
	public void testEnded(String host) {
		testEnded();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.jmeter.testelement.TestListener#testIterationStart(org.apache.jmeter.engine.event.LoopIterationEvent)
	 */
	public void testIterationStart(LoopIterationEvent event) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.jmeter.testelement.TestListener#testStarted()
	 */
	public void testStarted() {
		if (getBasedir() != null && getBasedir().length() > 0) {
			try {
				FileServer.getFileServer().setBasedir(FileServer.getFileServer().getBaseDir() + getBasedir());
			} catch (IOException e) {
				log.error("Failed to set file server base dir with " + getBasedir(), e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.jmeter.testelement.TestListener#testStarted(java.lang.String)
	 */
	public void testStarted(String host) {
		testStarted();
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12434.java