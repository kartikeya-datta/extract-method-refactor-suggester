error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7421.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7421.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7421.java
text:
```scala
r@@eturn match(string, activityIds);

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

package org.eclipse.ui.internal.activities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.WeakHashMap;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.activities.ActivityEvent;
import org.eclipse.ui.activities.ActivityManagerEvent;
import org.eclipse.ui.activities.CategoryEvent;
import org.eclipse.ui.activities.IActivity;
import org.eclipse.ui.activities.IActivityActivityBinding;
import org.eclipse.ui.activities.IActivityPatternBinding;
import org.eclipse.ui.activities.ICategory;
import org.eclipse.ui.activities.ICategoryActivityBinding;
import org.eclipse.ui.activities.IIdentifier;
import org.eclipse.ui.activities.IMutableActivityManager;
import org.eclipse.ui.activities.IdentifierEvent;
import org.eclipse.ui.internal.util.Util;

public final class MutableActivityManager
	extends AbstractActivityManager
	implements IMutableActivityManager {

	static boolean isActivityDefinitionChildOf(
		String ancestor,
		String id,
		Map activityDefinitionsById) {
		Collection visited = new HashSet();

		while (id != null && !visited.contains(id)) {
			IActivityDefinition activityDefinition =
				(IActivityDefinition) activityDefinitionsById.get(id);
			visited.add(id);

			if (activityDefinition != null
				&& Util.equals(id = activityDefinition.getParentId(), ancestor))
				return true;
		}

		return false;
	}

	private Map activitiesById = new WeakHashMap();
	private Map activityActivityBindingsByParentActivityId = new HashMap();
	private Map activityDefinitionsById = new HashMap();
	private Map activityPatternBindingsByActivityId = new HashMap();
	private IActivityRegistry activityRegistry;
	private Map categoriesById = new WeakHashMap();
	private Map categoryActivityBindingsByCategoryId = new HashMap();
	private Map categoryDefinitionsById = new HashMap();
	private Set definedActivityIds = new HashSet();
	private Set definedCategoryIds = new HashSet();
	private Set enabledActivityIds = new HashSet();
	private Map identifiersById = new WeakHashMap();

	public MutableActivityManager() {
		this(new ExtensionActivityRegistry(Platform.getExtensionRegistry()));
	}

	public MutableActivityManager(IActivityRegistry activityRegistry) {
		if (activityRegistry == null)
			throw new NullPointerException();

		this.activityRegistry = activityRegistry;

		this
			.activityRegistry
			.addActivityRegistryListener(new IActivityRegistryListener() {
			public void activityRegistryChanged(ActivityRegistryEvent activityRegistryEvent) {
				readRegistry();
			}
		});

		readRegistry();
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

	public ICategory getCategory(String categoryId) {
		if (categoryId == null)
			throw new NullPointerException();

		Category category = (Category) categoriesById.get(categoryId);

		if (category == null) {
			category = new Category(categoryId);
			updateCategory(category);
			categoriesById.put(categoryId, category);
		}

		return category;
	}

	public Set getDefinedActivityIds() {
		return Collections.unmodifiableSet(definedActivityIds);
	}

	public Set getDefinedCategoryIds() {
		return Collections.unmodifiableSet(definedCategoryIds);
	}

	public Set getEnabledActivityIds() {
		return Collections.unmodifiableSet(enabledActivityIds);
	}

	public IIdentifier getIdentifier(String identifierId) {
		if (identifierId == null)
			throw new NullPointerException();

		Identifier identifier = (Identifier) identifiersById.get(identifierId);

		if (identifier == null) {
			identifier = new Identifier(identifierId);
			updateIdentifier(identifier);
			identifiersById.put(identifierId, identifier);
		}

		return identifier;
	}

	private void getRequiredActivityIds(
		Set activityIds,
		Set requiredActivityIds) {
		for (Iterator iterator = activityIds.iterator(); iterator.hasNext();) {
			String activityId = (String) iterator.next();
			IActivity activity = getActivity(activityId);
			Set childActivityIds = new HashSet();
			Set activityActivityBindings =
				activity.getActivityActivityBindings();

			for (Iterator iterator2 = activityActivityBindings.iterator();
				iterator2.hasNext();
				) {
				IActivityActivityBinding activityActivityBinding =
					(IActivityActivityBinding) iterator2.next();
				childActivityIds.add(
					activityActivityBinding.getChildActivityId());
			}

			childActivityIds.removeAll(requiredActivityIds);
			requiredActivityIds.addAll(childActivityIds);
			getRequiredActivityIds(childActivityIds, requiredActivityIds);
		}
	}
	
	public boolean isMatch(String string, Set activityIds) {
		return isMatch(string, activityIds);
	}

	public boolean match(String string, Set activityIds) {
		activityIds = Util.safeCopy(activityIds, String.class);

		for (Iterator iterator = activityIds.iterator(); iterator.hasNext();) {
			String activityId = (String) iterator.next();
			IActivity activity = getActivity(activityId);

			if (activity.match(string))
				return true;
		}

		return false;
	}

	private void notifyActivities(Map activityEventsByActivityId) {
		for (Iterator iterator =
			activityEventsByActivityId.entrySet().iterator();
			iterator.hasNext();
			) {
			Map.Entry entry = (Map.Entry) iterator.next();
			String activityId = (String) entry.getKey();
			ActivityEvent activityEvent = (ActivityEvent) entry.getValue();
			Activity activity = (Activity) activitiesById.get(activityId);

			if (activity != null)
				activity.fireActivityChanged(activityEvent);
		}
	}

	private void notifyCategories(Map categoryEventsByCategoryId) {
		for (Iterator iterator =
			categoryEventsByCategoryId.entrySet().iterator();
			iterator.hasNext();
			) {
			Map.Entry entry = (Map.Entry) iterator.next();
			String categoryId = (String) entry.getKey();
			CategoryEvent categoryEvent = (CategoryEvent) entry.getValue();
			Category category = (Category) categoriesById.get(categoryId);

			if (category != null)
				category.fireCategoryChanged(categoryEvent);
		}
	}

	private void notifyIdentifiers(Map identifierEventsByIdentifierId) {
		for (Iterator iterator =
			identifierEventsByIdentifierId.entrySet().iterator();
			iterator.hasNext();
			) {
			Map.Entry entry = (Map.Entry) iterator.next();
			String identifierId = (String) entry.getKey();
			IdentifierEvent identifierEvent =
				(IdentifierEvent) entry.getValue();
			Identifier identifier =
				(Identifier) categoriesById.get(identifierId);

			if (identifier != null)
				identifier.fireIdentifierChanged(identifierEvent);
		}
	}

	private void readRegistry() {
		Collection activityDefinitions = new ArrayList();
		activityDefinitions.addAll(activityRegistry.getActivityDefinitions());
		Map activityDefinitionsById =
			new HashMap(
				ActivityDefinition.activityDefinitionsById(
					activityDefinitions,
					false));

		for (Iterator iterator = activityDefinitionsById.values().iterator();
			iterator.hasNext();
			) {
			IActivityDefinition activityDefinition =
				(IActivityDefinition) iterator.next();
			String name = activityDefinition.getName();

			if (name == null || name.length() == 0)
				iterator.remove();
		}

		for (Iterator iterator = activityDefinitionsById.keySet().iterator();
			iterator.hasNext();
			)
			if (!isActivityDefinitionChildOf(null,
				(String) iterator.next(),
				activityDefinitionsById))
				iterator.remove();

		Collection categoryDefinitions = new ArrayList();
		categoryDefinitions.addAll(activityRegistry.getCategoryDefinitions());
		Map categoryDefinitionsById =
			new HashMap(
				CategoryDefinition.categoryDefinitionsById(
					categoryDefinitions,
					false));

		for (Iterator iterator = categoryDefinitionsById.values().iterator();
			iterator.hasNext();
			) {
			ICategoryDefinition categoryDefinition =
				(ICategoryDefinition) iterator.next();
			String name = categoryDefinition.getName();

			if (name == null || name.length() == 0)
				iterator.remove();
		}

		Map activityActivityBindingDefinitionsByParentActivityId =
			ActivityActivityBindingDefinition
				.activityActivityBindingDefinitionsByParentActivityId(
				activityRegistry.getActivityActivityBindingDefinitions());
		Map activityActivityBindingsByParentActivityId = new HashMap();

		for (Iterator iterator =
			activityActivityBindingDefinitionsByParentActivityId
				.entrySet()
				.iterator();
			iterator.hasNext();
			) {
			Map.Entry entry = (Map.Entry) iterator.next();
			String parentActivityId = (String) entry.getKey();

			if (activityDefinitionsById.containsKey(parentActivityId)) {
				Collection activityActivityBindingDefinitions =
					(Collection) entry.getValue();

				if (activityActivityBindingDefinitions != null)
					for (Iterator iterator2 =
						activityActivityBindingDefinitions.iterator();
						iterator2.hasNext();
						) {
						IActivityActivityBindingDefinition activityActivityBindingDefinition =
							(IActivityActivityBindingDefinition) iterator2
								.next();
						String childActivityId =
							activityActivityBindingDefinition
								.getChildActivityId();

						if (activityDefinitionsById
							.containsKey(childActivityId)) {
							IActivityActivityBinding activityActivityBinding =
								new ActivityActivityBinding(
									childActivityId,
									parentActivityId);
							Set activityActivityBindings =
								(
									Set) activityActivityBindingsByParentActivityId
										.get(
									parentActivityId);

							if (activityActivityBindings == null) {
								activityActivityBindings = new HashSet();
								activityActivityBindingsByParentActivityId.put(
									parentActivityId,
									activityActivityBindings);
							}

							activityActivityBindings.add(
								activityActivityBinding);
						}
					}
			}
		}

		Map activityPatternBindingDefinitionsByActivityId =
			ActivityPatternBindingDefinition
				.activityPatternBindingDefinitionsByActivityId(
				activityRegistry.getActivityPatternBindingDefinitions());
		Map activityPatternBindingsByActivityId = new HashMap();

		for (Iterator iterator =
			activityPatternBindingDefinitionsByActivityId.entrySet().iterator();
			iterator.hasNext();
			) {
			Map.Entry entry = (Map.Entry) iterator.next();
			String activityId = (String) entry.getKey();

			if (activityDefinitionsById.containsKey(activityId)) {
				Collection activityPatternBindingDefinitions =
					(Collection) entry.getValue();

				if (activityPatternBindingDefinitions != null)
					for (Iterator iterator2 =
						activityPatternBindingDefinitions.iterator();
						iterator2.hasNext();
						) {
						IActivityPatternBindingDefinition activityPatternBindingDefinition =
							(IActivityPatternBindingDefinition) iterator2
								.next();
						String pattern =
							activityPatternBindingDefinition.getPattern();

						if (pattern != null && pattern.length() != 0) {
							IActivityPatternBinding activityPatternBinding =
								new ActivityPatternBinding(
									activityId,
									Pattern.compile(pattern));
							Set activityPatternBindings =
								(Set) activityPatternBindingsByActivityId.get(
									activityId);

							if (activityPatternBindings == null) {
								activityPatternBindings = new HashSet();
								activityPatternBindingsByActivityId.put(
									activityId,
									activityPatternBindings);
							}

							activityPatternBindings.add(activityPatternBinding);
						}
					}
			}
		}

		Map categoryActivityBindingDefinitionsByCategoryId =
			CategoryActivityBindingDefinition
				.categoryActivityBindingDefinitionsByCategoryId(
				activityRegistry.getCategoryActivityBindingDefinitions());
		Map categoryActivityBindingsByCategoryId = new HashMap();

		for (Iterator iterator =
			categoryActivityBindingDefinitionsByCategoryId
				.entrySet()
				.iterator();
			iterator.hasNext();
			) {
			Map.Entry entry = (Map.Entry) iterator.next();
			String categoryId = (String) entry.getKey();

			if (categoryDefinitionsById.containsKey(categoryId)) {
				Collection categoryActivityBindingDefinitions =
					(Collection) entry.getValue();

				if (categoryActivityBindingDefinitions != null)
					for (Iterator iterator2 =
						categoryActivityBindingDefinitions.iterator();
						iterator2.hasNext();
						) {
						ICategoryActivityBindingDefinition categoryActivityBindingDefinition =
							(ICategoryActivityBindingDefinition) iterator2
								.next();
						String activityId =
							categoryActivityBindingDefinition.getActivityId();

						if (activityDefinitionsById.containsKey(activityId)) {
							ICategoryActivityBinding categoryActivityBinding =
								new CategoryActivityBinding(
									activityId,
									categoryId);
							Set categoryActivityBindings =
								(Set) categoryActivityBindingsByCategoryId.get(
									categoryId);

							if (categoryActivityBindings == null) {
								categoryActivityBindings = new HashSet();
								categoryActivityBindingsByCategoryId.put(
									categoryId,
									categoryActivityBindings);
							}

							categoryActivityBindings.add(
								categoryActivityBinding);
						}
					}
			}
		}

		this.activityActivityBindingsByParentActivityId =
			activityActivityBindingsByParentActivityId;
		this.activityDefinitionsById = activityDefinitionsById;
		this.activityPatternBindingsByActivityId =
			activityPatternBindingsByActivityId;
		this.categoryActivityBindingsByCategoryId =
			categoryActivityBindingsByCategoryId;
		this.categoryDefinitionsById = categoryDefinitionsById;
		boolean definedActivityIdsChanged = false;
		Set definedActivityIds = new HashSet(activityDefinitionsById.keySet());

		if (!definedActivityIds.equals(this.definedActivityIds)) {
			this.definedActivityIds = definedActivityIds;
			definedActivityIdsChanged = true;
		}

		boolean definedCategoryIdsChanged = false;
		Set definedCategoryIds = new HashSet(categoryDefinitionsById.keySet());

		if (!definedCategoryIds.equals(this.definedCategoryIds)) {
			this.definedCategoryIds = definedCategoryIds;
			definedCategoryIdsChanged = true;
		}

		Set enabledActivityIds = new HashSet(this.enabledActivityIds);
		getRequiredActivityIds(this.enabledActivityIds, enabledActivityIds);
		boolean enabledActivityIdsChanged = false;

		if (!this.enabledActivityIds.equals(enabledActivityIds)) {
			this.enabledActivityIds = enabledActivityIds;
			enabledActivityIdsChanged = true;
		}

		Map activityEventsByActivityId =
			updateActivities(activitiesById.keySet());

		Map categoryEventsByCategoryId =
			updateCategories(categoriesById.keySet());

		Map identifierEventsByIdentifierId =
			updateIdentifiers(identifiersById.keySet());

		if (definedActivityIdsChanged
 definedCategoryIdsChanged
 enabledActivityIdsChanged)
			fireActivityManagerChanged(
				new ActivityManagerEvent(
					this,
					definedActivityIdsChanged,
					definedCategoryIdsChanged,
					enabledActivityIdsChanged));

		if (activityEventsByActivityId != null)
			notifyActivities(activityEventsByActivityId);

		if (categoryEventsByCategoryId != null)
			notifyCategories(categoryEventsByCategoryId);

		if (identifierEventsByIdentifierId != null)
			notifyIdentifiers(identifierEventsByIdentifierId);
	}

	public void setEnabledActivityIds(Set enabledActivityIds) {
		enabledActivityIds = Util.safeCopy(enabledActivityIds, String.class);
		Set requiredActivityIds = new HashSet(enabledActivityIds);
		getRequiredActivityIds(enabledActivityIds, requiredActivityIds);
		enabledActivityIds = requiredActivityIds;
		boolean activityManagerChanged = false;
		Map activityEventsByActivityId = null;

		if (!this.enabledActivityIds.equals(enabledActivityIds)) {
			this.enabledActivityIds = enabledActivityIds;
			activityManagerChanged = true;
			activityEventsByActivityId =
				updateActivities(this.definedActivityIds);
		}

		Map identifierEventsByIdentifierId =
			updateIdentifiers(identifiersById.keySet());

		if (activityManagerChanged)
			fireActivityManagerChanged(
				new ActivityManagerEvent(this, false, false, true));

		if (activityEventsByActivityId != null)
			notifyActivities(activityEventsByActivityId);

		if (identifierEventsByIdentifierId != null)
			notifyIdentifiers(identifierEventsByIdentifierId);
	}

	private Map updateActivities(Collection activityIds) {
		Map activityEventsByActivityId = new TreeMap();

		for (Iterator iterator = activityIds.iterator(); iterator.hasNext();) {
			String activityId = (String) iterator.next();
			Activity activity = (Activity) activitiesById.get(activityId);

			if (activity != null) {
				ActivityEvent activityEvent = updateActivity(activity);

				if (activityEvent != null)
					activityEventsByActivityId.put(activityId, activityEvent);
			}
		}

		return activityEventsByActivityId;
	}

	private ActivityEvent updateActivity(Activity activity) {
		Set activityActivityBindings =
			(Set) activityActivityBindingsByParentActivityId.get(
				activity.getId());
		boolean activityActivityBindingsChanged =
			activity.setActivityActivityBindings(
				activityActivityBindings != null
					? activityActivityBindings
					: Collections.EMPTY_SET);
		Set activityPatternBindings =
			(Set) activityPatternBindingsByActivityId.get(activity.getId());
		boolean activityPatternBindingsChanged =
			activity.setActivityPatternBindings(
				activityPatternBindings != null
					? activityPatternBindings
					: Collections.EMPTY_SET);
		IActivityDefinition activityDefinition =
			(IActivityDefinition) activityDefinitionsById.get(activity.getId());
		boolean definedChanged =
			activity.setDefined(activityDefinition != null);
		boolean descriptionChanged =
			activity.setDescription(
				activityDefinition != null
					? activityDefinition.getDescription()
					: null);
		boolean enabledChanged =
			activity.setEnabled(enabledActivityIds.contains(activity.getId()));
		boolean nameChanged =
			activity.setName(
				activityDefinition != null
					? activityDefinition.getName()
					: null);
		boolean parentIdChanged =
			activity.setParentId(
				activityDefinition != null
					? activityDefinition.getParentId()
					: null);

		if (activityActivityBindingsChanged
 activityPatternBindingsChanged
 definedChanged
 descriptionChanged
 enabledChanged
 nameChanged
 parentIdChanged)
			return new ActivityEvent(
				activity,
				activityActivityBindingsChanged,
				activityPatternBindingsChanged,
				definedChanged,
				descriptionChanged,
				enabledChanged,
				nameChanged,
				parentIdChanged);
		else
			return null;
	}

	private Map updateCategories(Collection categoryIds) {
		Map categoryEventsByCategoryId = new TreeMap();

		for (Iterator iterator = categoryIds.iterator(); iterator.hasNext();) {
			String categoryId = (String) iterator.next();
			Category category = (Category) categoriesById.get(categoryId);

			if (category != null) {
				CategoryEvent categoryEvent = updateCategory(category);

				if (categoryEvent != null)
					categoryEventsByCategoryId.put(categoryId, categoryEvent);
			}
		}

		return categoryEventsByCategoryId;
	}

	private CategoryEvent updateCategory(Category category) {
		Set categoryActivityBindings =
			(Set) categoryActivityBindingsByCategoryId.get(category.getId());
		boolean categoryActivityBindingsChanged =
			category.setCategoryActivityBindings(
				categoryActivityBindings != null
					? categoryActivityBindings
					: Collections.EMPTY_SET);
		ICategoryDefinition categoryDefinition =
			(ICategoryDefinition) categoryDefinitionsById.get(category.getId());
		boolean definedChanged =
			category.setDefined(categoryDefinition != null);
		boolean descriptionChanged =
			category.setDescription(
				categoryDefinition != null
					? categoryDefinition.getDescription()
					: null);
		boolean nameChanged =
			category.setName(
				categoryDefinition != null
					? categoryDefinition.getName()
					: null);

		if (categoryActivityBindingsChanged
 definedChanged
 descriptionChanged
 nameChanged)
			return new CategoryEvent(
				category,
				categoryActivityBindingsChanged,
				definedChanged,
				descriptionChanged,
				nameChanged);
		else
			return null;
	}

	private IdentifierEvent updateIdentifier(Identifier identifier) {
		// TODO review performance characteristics
		String id = identifier.getId();
		Set activityIds = new HashSet();

		for (Iterator iterator = definedActivityIds.iterator();
			iterator.hasNext();
			) {
			String activityId = (String) iterator.next();
			IActivity activity = getActivity(activityId);

			if (activity.match(id))
				activityIds.add(activityId);
		}

		boolean enabled =
			match(id, enabledActivityIds) || !match(id, definedActivityIds);
		boolean activityIdsChanged = identifier.setActivityIds(activityIds);
		boolean enabledChanged = identifier.setEnabled(enabled);

		if (activityIdsChanged || enabledChanged)
			return new IdentifierEvent(
				identifier,
				activityIdsChanged,
				enabledChanged);
		else
			return null;
	}

	private Map updateIdentifiers(Collection identifierIds) {
		Map identifierEventsByIdentifierId = new TreeMap();

		for (Iterator iterator = identifierIds.iterator();
			iterator.hasNext();
			) {
			String identifierId = (String) iterator.next();
			Identifier identifier =
				(Identifier) identifiersById.get(identifierId);

			if (identifier != null) {
				IdentifierEvent identifierEvent = updateIdentifier(identifier);

				if (identifierEvent != null)
					identifierEventsByIdentifierId.put(
						identifierId,
						identifierEvent);
			}
		}

		return identifierEventsByIdentifierId;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7421.java