error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8025.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8025.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8025.java
text:
```scala
S@@et definedActivityIds = new HashSet(activityDefinitionsById.keySet());

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.ui.internal.csm.activities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.activities.IActivity;
import org.eclipse.ui.activities.IActivityManager;
import org.eclipse.ui.activities.IActivityManagerEvent;
import org.eclipse.ui.activities.IActivityManagerListener;
import org.eclipse.ui.activities.IPatternBinding;
import org.eclipse.ui.internal.util.Util;

public final class ActivityManager implements IActivityManager {

	static boolean isActivityDefinitionChildOf(String ancestor, String id, Map activityDefinitionsById) {
		Collection visited = new HashSet();

		while (id != null && !visited.contains(id)) {
			IActivityDefinition activityDefinition = (IActivityDefinition) activityDefinitionsById.get(id);				
			visited.add(id);

			if (activityDefinition != null && Util.equals(id = activityDefinition.getParentId(), ancestor))
				return true;
		}

		return false;
	}	

	private Set activeActivityIds = new HashSet();
	private Map activitiesById = new HashMap();
	private Map activityDefinitionsById = new HashMap();
	private IActivityManagerEvent activityManagerEvent;
	private List activityManagerListeners;
	private Set definedActivityIds = new HashSet();
	private Set enabledActivityIds = new HashSet();	
	private ExtensionActivityRegistry extensionActivityRegistry;	
	private Map patternBindingsByActivityId = new HashMap();

	public ActivityManager() {
		if (extensionActivityRegistry == null)
			extensionActivityRegistry = new ExtensionActivityRegistry(Platform.getExtensionRegistry());

		extensionActivityRegistry.addActivityRegistryListener(new IActivityRegistryListener() {
			public void activityRegistryChanged(IActivityRegistryEvent activityRegistryEvent) {
				readRegistry();
			}
		});

		readRegistry();
	}

	public void addActivityManagerListener(IActivityManagerListener activityManagerListener) {
		if (activityManagerListener == null)
			throw new NullPointerException();
			
		if (activityManagerListeners == null)
			activityManagerListeners = new ArrayList();
		
		if (!activityManagerListeners.contains(activityManagerListener))
			activityManagerListeners.add(activityManagerListener);
	}

	public Set getActiveActivityIds() {
		return Collections.unmodifiableSet(activeActivityIds);
	}

	public IActivity getActivity(String activityId) {
		if (activityId == null)
			throw new NullPointerException();
			
		Activity activity = (Activity) activitiesById.get(activityId);
		
		if (activity == null) {
			activity = new Activity(activityId);
			updateActivity(activity);
			activitiesById.put(activityId, activity);
		}
		
		return activity;
	}
	
	public Set getDefinedActivityIds() {
		return Collections.unmodifiableSet(definedActivityIds);
	}

	public Set getEnabledActivityIds() {
		return Collections.unmodifiableSet(enabledActivityIds);
	}	

	public boolean match(String string, Set activityIds) {
		activityIds = Util.safeCopy(activityIds, String.class);
		
		for (Iterator iterator = activityIds.iterator(); iterator.hasNext();) {			
			IActivity activity = getActivity((String) iterator.next());
						
			if (activity.match(string))
				return true;
		}
			
		return false;
	}	

	public void removeActivityManagerListener(IActivityManagerListener activityManagerListener) {
		if (activityManagerListener == null)
			throw new NullPointerException();
			
		if (activityManagerListeners != null)
			activityManagerListeners.remove(activityManagerListener);
	}

	public void setActiveActivityIds(Set activeActivityIds) {
		activeActivityIds = Util.safeCopy(activeActivityIds, String.class);
		boolean activityManagerChanged = false;
		Collection updatedActivityIds = null;

		if (!this.activeActivityIds.equals(activeActivityIds)) {
			this.activeActivityIds = activeActivityIds;
			activityManagerChanged = true;	
			updatedActivityIds = updateActivities(activitiesById.keySet());	
		}
		
		if (activityManagerChanged)
			fireActivityManagerChanged();

		if (updatedActivityIds != null)
			notifyActivities(updatedActivityIds);	
	}
	
	public void setEnabledActivityIds(Set enabledActivityIds) {	
		enabledActivityIds = Util.safeCopy(enabledActivityIds, String.class);
		boolean activityManagerChanged = false;
		Collection updatedActivityIds = null;

		if (!this.enabledActivityIds.equals(enabledActivityIds)) {
			this.enabledActivityIds = enabledActivityIds;
			activityManagerChanged = true;	
			updatedActivityIds = updateActivities(this.definedActivityIds);	
		}
		
		if (activityManagerChanged)
			fireActivityManagerChanged();

		if (updatedActivityIds != null)
			notifyActivities(updatedActivityIds);	
	}	
	
	private void fireActivityManagerChanged() {
		if (activityManagerListeners != null)
			for (int i = 0; i < activityManagerListeners.size(); i++) {
				if (activityManagerEvent == null)
					activityManagerEvent = new ActivityManagerEvent(this);
								
				((IActivityManagerListener) activityManagerListeners.get(i)).activityManagerChanged(activityManagerEvent);
			}				
	}
	
	private void notifyActivities(Collection activityIds) {	
		for (Iterator iterator = activityIds.iterator(); iterator.hasNext();) {	
			String activityId = (String) iterator.next();					
			Activity activity = (Activity) activitiesById.get(activityId);
			
			if (activity != null)
				activity.fireActivityChanged();
		}
	}

	private void readRegistry() {
		Collection activityDefinitions = new ArrayList();
		activityDefinitions.addAll(extensionActivityRegistry.getActivityDefinitions());				
		Map activityDefinitionsById = new HashMap(ActivityDefinition.activityDefinitionsById(activityDefinitions, false));

		for (Iterator iterator = activityDefinitionsById.values().iterator(); iterator.hasNext();) {
			IActivityDefinition activityDefinition = (IActivityDefinition) iterator.next();
			String name = activityDefinition.getName();
				
			if (name == null || name.length() == 0)
				iterator.remove();
		}

		for (Iterator iterator = activityDefinitionsById.keySet().iterator(); iterator.hasNext();)
			if (!isActivityDefinitionChildOf(null, (String) iterator.next(), activityDefinitionsById))
				iterator.remove();

		Map activityPatternBindingDefinitionsByActivityId = ActivityPatternBindingDefinition.activityPatternBindingDefinitionsByActivityId(extensionActivityRegistry.getActivityPatternBindingDefinitions());
		Map patternBindingsByActivityId = new HashMap();		

		for (Iterator iterator = activityPatternBindingDefinitionsByActivityId.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry entry = (Map.Entry) iterator.next();
			String activityId = (String) entry.getKey();
			
			if (activityDefinitionsById.containsKey(activityId)) {			
				Collection activityPatternBindingDefinitions = (Collection) entry.getValue();
				
				if (activityPatternBindingDefinitions != null)
					for (Iterator iterator2 = activityPatternBindingDefinitions.iterator(); iterator2.hasNext();) {
						IActivityPatternBindingDefinition activityPatternBindingDefinition = (IActivityPatternBindingDefinition) iterator2.next();
						String pattern = activityPatternBindingDefinition.getPattern();
					
						if (pattern != null && pattern.length() != 0) {
							IPatternBinding patternBinding = new PatternBinding(activityPatternBindingDefinition.isInclusive(), pattern);	
							List patternBindings = (List) patternBindingsByActivityId.get(activityId);
							
							if (patternBindings == null) {
								patternBindings = new ArrayList();
								patternBindingsByActivityId.put(activityId, patternBindings);
							}
							
							patternBindings.add(patternBinding);
						}
					}
			}
		}		
		
		this.activityDefinitionsById = activityDefinitionsById;
		this.patternBindingsByActivityId = patternBindingsByActivityId;			
		boolean activityManagerChanged = false;			
		Set definedActivityIds = new TreeSet(activityDefinitionsById.keySet());		

		if (!definedActivityIds.equals(this.definedActivityIds)) {
			this.definedActivityIds = definedActivityIds;
			activityManagerChanged = true;	
		}

		Collection updatedActivityIds = updateActivities(activitiesById.keySet());	
		
		if (activityManagerChanged)
			fireActivityManagerChanged();

		if (updatedActivityIds != null)
			notifyActivities(updatedActivityIds);		
	}

	private boolean updateActivity(Activity activity) {
		boolean updated = false;
		updated |= activity.setActive(activeActivityIds.contains(activity.getId()));		
		IActivityDefinition activityDefinition = (IActivityDefinition) activityDefinitionsById.get(activity.getId());
		updated |= activity.setDefined(activityDefinition != null);
		updated |= activity.setDescription(activityDefinition != null ? activityDefinition.getDescription() : null);		
		updated |= activity.setEnabled(enabledActivityIds.contains(activity.getId()));
		updated |= activity.setName(activityDefinition != null ? activityDefinition.getName() : null);
		updated |= activity.setParentId(activityDefinition != null ? activityDefinition.getParentId() : null);				
		List patternBindings = (List) patternBindingsByActivityId.get(activity.getId());
		updated |= activity.setPatternBindings(patternBindings != null ? patternBindings : Collections.EMPTY_LIST);
		return updated;
	}

	private Collection updateActivities(Collection activityIds) {
		Collection updatedIds = new TreeSet();
		
		for (Iterator iterator = activityIds.iterator(); iterator.hasNext();) {		
			String activityId = (String) iterator.next();					
			Activity activity = (Activity) activitiesById.get(activityId);
			
			if (activity != null && updateActivity(activity))
				updatedIds.add(activityId);			
		}
		
		return updatedIds;			
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8025.java