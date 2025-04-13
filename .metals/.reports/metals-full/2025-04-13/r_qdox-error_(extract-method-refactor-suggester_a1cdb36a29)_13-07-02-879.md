error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9626.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9626.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9626.java
text:
```scala
d@@omain = tempDomain;

/*
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.jmx;

import java.lang.management.ManagementFactory;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.IInitializer;
import wicket.WicketRuntimeException;

/**
 * Registers Wicket's MBeans.
 * 
 * @author eelcohillenius
 */
public class Initializer implements IInitializer
{
	private static Log log = LogFactory.getLog(Initializer.class);

	/**
	 * @see wicket.IInitializer#init(wicket.Application)
	 */
	public void init(wicket.Application application)
	{
		try
		{
			String name = application.getName();
			MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

			// register top level application object, but first check whether
			// multiple instances of the same application (name) are running and
			// if so adjust the name
			String domain = "wicket.app." + name;
			ObjectName appBeanName = new ObjectName(domain + ":type=Application");
			String tempDomain = domain;
			int i = 0;
			while (mbs.isRegistered(appBeanName))
			{
				tempDomain = name + "-" + i++;
				appBeanName = new ObjectName(tempDomain + ":type=Application");
			}
			name = tempDomain;

			Application appBean = new Application(application);
			mbs.registerMBean(appBean, appBeanName);

			mbs.registerMBean(new ApplicationSettings(application), new ObjectName(domain
					+ ":type=Application,name=ApplicationSettings"));
			mbs.registerMBean(new DebugSettings(application), new ObjectName(domain
					+ ":type=Application,name=DebugSettings"));
			mbs.registerMBean(new MarkupSettings(application), new ObjectName(domain
					+ ":type=Application,name=MarkupSettings"));
			mbs.registerMBean(new ResourceSettings(application), new ObjectName(domain
					+ ":type=Application,name=ResourceSettings"));
			mbs.registerMBean(new PageSettings(application), new ObjectName(domain
					+ ":type=Application,name=PageSettings"));
			mbs.registerMBean(new RequestCycleSettings(application), new ObjectName(domain
					+ ":type=Application,name=RequestCycleSettings"));
			mbs.registerMBean(new SecuritySettings(application), new ObjectName(domain
					+ ":type=Application,name=SecuritySettings"));
			mbs.registerMBean(new SessionSettings(application), new ObjectName(domain
					+ ":type=Application,name=SessionSettings"));
			mbs.registerMBean(new CookieValuePersisterSettings(application), new ObjectName(domain
					+ ":type=Application,name=CookieValuePersisterSettings"));

			RequestLogger sessionsBean = new RequestLogger(application);
			ObjectName sessionsBeanName = new ObjectName(domain + ":type=RequestLogger");
			mbs.registerMBean(sessionsBean, sessionsBeanName);
		}
		catch (MalformedObjectNameException e)
		{
			throw new WicketRuntimeException(e);
		}
		catch (InstanceAlreadyExistsException e)
		{
			throw new WicketRuntimeException(e);
		}
		catch (MBeanRegistrationException e)
		{
			throw new WicketRuntimeException(e);
		}
		catch (NotCompliantMBeanException e)
		{
			throw new WicketRuntimeException(e);
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9626.java