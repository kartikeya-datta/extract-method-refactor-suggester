error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9227.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9227.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9227.java
text:
```scala
C@@lass<?> simpleTriggerClass;

/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.scheduling.quartz;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.Constants;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

/**
 * A Spring {@link FactoryBean} for creating a Quartz {@link org.quartz.SimpleTrigger}
 * instance, supporting bean-style usage for trigger configuration.
 *
 * <p>{@code SimpleTrigger(Impl)} itself is already a JavaBean but lacks sensible defaults.
 * This class uses the Spring bean name as job name, the Quartz default group ("DEFAULT")
 * as job group, the current time as start time, and indefinite repetition, if not specified.
 *
 * <p>This class will also register the trigger with the job name and group of
 * a given {@link org.quartz.JobDetail}. This allows {@link SchedulerFactoryBean}
 * to automatically register a trigger for the corresponding JobDetail,
 * instead of registering the JobDetail separately.
 *
 * <p><b>NOTE:</b> This FactoryBean works against both Quartz 1.x and Quartz 2.x,
 * in contrast to the older {@link SimpleTriggerBean} class.
 *
 * @author Juergen Hoeller
 * @since 3.1
 * @see #setName
 * @see #setGroup
 * @see #setStartDelay
 * @see #setJobDetail
 * @see SchedulerFactoryBean#setTriggers
 * @see SchedulerFactoryBean#setJobDetails
 * @see CronTriggerBean
 */
public class SimpleTriggerFactoryBean implements FactoryBean<SimpleTrigger>, BeanNameAware, InitializingBean {

	/** Constants for the SimpleTrigger class */
	private static final Constants constants = new Constants(SimpleTrigger.class);


	private String name;

	private String group;

	private JobDetail jobDetail;

	private JobDataMap jobDataMap = new JobDataMap();

	private Date startTime;

	private long startDelay;

	private long repeatInterval;

	private int repeatCount = -1;

	private int priority;

	private int misfireInstruction;

	private String beanName;

	private SimpleTrigger simpleTrigger;


	/**
	 * Specify the trigger's name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Specify the trigger's group.
	 */
	public void setGroup(String group) {
		this.group = group;
	}

	/**
	 * Set the JobDetail that this trigger should be associated with.
	 */
	public void setJobDetail(JobDetail jobDetail) {
		this.jobDetail = jobDetail;
	}

	/**
	 * Set the trigger's JobDataMap.
	 * @see #setJobDataAsMap
	 */
	public void setJobDataMap(JobDataMap jobDataMap) {
		this.jobDataMap = jobDataMap;
	}

	/**
	 * Return the trigger's JobDataMap.
	 */
	public JobDataMap getJobDataMap() {
		return this.jobDataMap;
	}

	/**
	 * Register objects in the JobDataMap via a given Map.
	 * <p>These objects will be available to this Trigger only,
	 * in contrast to objects in the JobDetail's data map.
	 * @param jobDataAsMap Map with String keys and any objects as values
	 * (for example Spring-managed beans)
	 * @see org.springframework.scheduling.quartz.JobDetailBean#setJobDataAsMap
	 */
	public void setJobDataAsMap(Map<String, ?> jobDataAsMap) {
		this.jobDataMap.putAll(jobDataAsMap);
	}

	/**
	 * Set the start delay in milliseconds.
	 * <p>The start delay is added to the current system time (when the bean starts)
	 * to control the start time of the trigger.
	 */
	public void setStartDelay(long startDelay) {
		Assert.isTrue(startDelay >= 0, "Start delay cannot be negative");
		this.startDelay = startDelay;
	}

	/**
	 * Specify the interval between execution times of this trigger.
	 */
	public void setRepeatInterval(long repeatInterval) {
		this.repeatInterval = repeatInterval;
	}

	/**
	 * Specify the number of times this trigger is supposed to fire.
	 * <p>Default is to repeat indefinitely.
	 */
	public void setRepeatCount(int repeatCount) {
		this.repeatCount = repeatCount;
	}

	/**
	 * Specify the priority of this trigger.
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * Specify a misfire instruction for this trigger.
	 */
	public void setMisfireInstruction(int misfireInstruction) {
		this.misfireInstruction = misfireInstruction;
	}

	/**
	 * Set the misfire instruction via the name of the corresponding
	 * constant in the {@link org.quartz.SimpleTrigger} class.
	 * Default is {@code MISFIRE_INSTRUCTION_SMART_POLICY}.
	 * @see org.quartz.SimpleTrigger#MISFIRE_INSTRUCTION_FIRE_NOW
	 * @see org.quartz.SimpleTrigger#MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_EXISTING_COUNT
	 * @see org.quartz.SimpleTrigger#MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT
	 * @see org.quartz.SimpleTrigger#MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_EXISTING_REPEAT_COUNT
	 * @see org.quartz.SimpleTrigger#MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_REMAINING_REPEAT_COUNT
	 * @see org.quartz.Trigger#MISFIRE_INSTRUCTION_SMART_POLICY
	 */
	public void setMisfireInstructionName(String constantName) {
		this.misfireInstruction = constants.asNumber(constantName).intValue();
	}

	@Override
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}


	@Override
	public void afterPropertiesSet() throws ParseException {
		if (this.name == null) {
			this.name = this.beanName;
		}
		if (this.group == null) {
			this.group = Scheduler.DEFAULT_GROUP;
		}
		if (this.jobDetail != null) {
			this.jobDataMap.put(JobDetailAwareTrigger.JOB_DETAIL_KEY, this.jobDetail);
		}
		if (this.startDelay > 0) {
			this.startTime = new Date(System.currentTimeMillis() + this.startDelay);
		}
		else if (this.startTime == null) {
			this.startTime = new Date();
		}

		/*
		SimpleTriggerImpl sti = new SimpleTriggerImpl();
		sti.setName(this.name);
		sti.setGroup(this.group);
		sti.setJobKey(this.jobDetail.getKey());
		sti.setJobDataMap(this.jobDataMap);
		sti.setStartTime(this.startTime);
		sti.setRepeatInterval(this.repeatInterval);
		sti.setRepeatCount(this.repeatCount);
		sti.setPriority(this.priority);
		sti.setMisfireInstruction(this.misfireInstruction);
		this.simpleTrigger = sti;
		*/

		Class simpleTriggerClass;
		Method jobKeyMethod;
		try {
			simpleTriggerClass = getClass().getClassLoader().loadClass("org.quartz.impl.triggers.SimpleTriggerImpl");
			jobKeyMethod = JobDetail.class.getMethod("getKey");
		}
		catch (ClassNotFoundException ex) {
			simpleTriggerClass = SimpleTrigger.class;
			jobKeyMethod = null;
		}
		catch (NoSuchMethodException ex) {
			throw new IllegalStateException("Incompatible Quartz version");
		}
		BeanWrapper bw = new BeanWrapperImpl(simpleTriggerClass);
		MutablePropertyValues pvs = new MutablePropertyValues();
		pvs.add("name", this.name);
		pvs.add("group", this.group);
		if (jobKeyMethod != null) {
			pvs.add("jobKey", ReflectionUtils.invokeMethod(jobKeyMethod, this.jobDetail));
		}
		else {
			pvs.add("jobName", this.jobDetail.getName());
			pvs.add("jobGroup", this.jobDetail.getGroup());
		}
		pvs.add("jobDataMap", this.jobDataMap);
		pvs.add("startTime", this.startTime);
		pvs.add("repeatInterval", this.repeatInterval);
		pvs.add("repeatCount", this.repeatCount);
		pvs.add("priority", this.priority);
		pvs.add("misfireInstruction", this.misfireInstruction);
		bw.setPropertyValues(pvs);
		this.simpleTrigger = (SimpleTrigger) bw.getWrappedInstance();
	}


	@Override
	public SimpleTrigger getObject() {
		return this.simpleTrigger;
	}

	@Override
	public Class<?> getObjectType() {
		return SimpleTrigger.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9227.java