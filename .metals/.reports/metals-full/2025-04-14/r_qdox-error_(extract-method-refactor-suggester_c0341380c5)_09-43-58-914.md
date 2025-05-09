error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8945.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8945.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8945.java
text:
```scala
r@@oleManagerEvent = new RoleManagerEvent(this, false);

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

package org.eclipse.ui.internal.csm.roles;

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
import org.eclipse.ui.roles.IActivityBinding;
import org.eclipse.ui.roles.IRole;
import org.eclipse.ui.roles.IRoleManager;
import org.eclipse.ui.roles.IRoleManagerEvent;
import org.eclipse.ui.roles.IRoleManagerListener;

public final class RoleManager implements IRoleManager {

	private Map activityBindingsByRoleId = new HashMap();
	private Set definedRoleIds = new HashSet();
	private Map roleDefinitionsById = new HashMap();
	private IRoleManagerEvent roleManagerEvent;
	private List roleManagerListeners;	
	private IRoleRegistry roleRegistry;
	private Map rolesById = new HashMap();	

	public RoleManager() {
		this(new ExtensionRoleRegistry(Platform.getExtensionRegistry()));
	}	
	
	public RoleManager(IRoleRegistry roleRegistry) {
		if (roleRegistry == null)
			throw new NullPointerException();

		this.roleRegistry = roleRegistry;	
		
		this.roleRegistry.addRoleRegistryListener(new IRoleRegistryListener() {
			public void roleRegistryChanged(IRoleRegistryEvent roleRegistryEvent) {
				readRegistry();
			}
		});

		readRegistry();
	}

	public void addRoleManagerListener(IRoleManagerListener roleManagerListener) {
		if (roleManagerListener == null)
			throw new NullPointerException();
			
		if (roleManagerListeners == null)
			roleManagerListeners = new ArrayList();
		
		if (!roleManagerListeners.contains(roleManagerListener))
			roleManagerListeners.add(roleManagerListener);
	}

	public IRole getRole(String roleId) {
		if (roleId == null)
			throw new NullPointerException();
			
		Role role = (Role) rolesById.get(roleId);
		
		if (role == null) {
			role = new Role(roleId);
			updateRole(role);
			rolesById.put(roleId, role);
		}
		
		return role;
	}
	
	public Set getDefinedRoleIds() {
		return Collections.unmodifiableSet(definedRoleIds);
	}

	public void removeRoleManagerListener(IRoleManagerListener roleManagerListener) {
		if (roleManagerListener == null)
			throw new NullPointerException();
			
		if (roleManagerListeners != null)
			roleManagerListeners.remove(roleManagerListener);
	}

	private void fireRoleManagerChanged() {
		if (roleManagerListeners != null)
			for (int i = 0; i < roleManagerListeners.size(); i++) {
				if (roleManagerEvent == null)
					roleManagerEvent = new RoleManagerEvent(this);
								
				((IRoleManagerListener) roleManagerListeners.get(i)).roleManagerChanged(roleManagerEvent);
			}				
	}
	
	private void notifyRoles(Collection roleIds) {	
		for (Iterator iterator = roleIds.iterator(); iterator.hasNext();) {	
			String roleId = (String) iterator.next();					
			Role role = (Role) rolesById.get(roleId);
			
			if (role != null)
				role.fireRoleChanged();
		}
	}

	private void readRegistry() {
		Collection roleDefinitions = new ArrayList();
		roleDefinitions.addAll(roleRegistry.getRoleDefinitions());				
		Map roleDefinitionsById = new HashMap(RoleDefinition.roleDefinitionsById(roleDefinitions, false));

		for (Iterator iterator = roleDefinitionsById.values().iterator(); iterator.hasNext();) {
			IRoleDefinition roleDefinition = (IRoleDefinition) iterator.next();
			String name = roleDefinition.getName();
				
			if (name == null || name.length() == 0)
				iterator.remove();
		}

		Map roleActivityBindingDefinitionsByRoleId = RoleActivityBindingDefinition.roleActivityBindingDefinitionsByRoleId(roleRegistry.getRoleActivityBindingDefinitions());
		Map activityBindingsByRoleId = new HashMap();		

		for (Iterator iterator = roleActivityBindingDefinitionsByRoleId.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry entry = (Map.Entry) iterator.next();
			String roleId = (String) entry.getKey();
			
			if (roleDefinitionsById.containsKey(roleId)) {			
				Collection roleActivityBindingDefinitions = (Collection) entry.getValue();
				
				if (roleActivityBindingDefinitions != null)
					for (Iterator iterator2 = roleActivityBindingDefinitions.iterator(); iterator2.hasNext();) {
						IRoleActivityBindingDefinition roleActivityBindingDefinition = (IRoleActivityBindingDefinition) iterator2.next();
						String activityId = roleActivityBindingDefinition.getActivityId();
					
						if (activityId != null && activityId.length() != 0) {
							IActivityBinding activityBinding = new ActivityBinding(activityId);	
							Set activityBindings = (Set) activityBindingsByRoleId.get(roleId);
							
							if (activityBindings == null) {
								activityBindings = new HashSet();
								activityBindingsByRoleId.put(roleId, activityBindings);
							}
							
							activityBindings.add(activityBinding);
						}
					}
			}
		}		
		
		this.activityBindingsByRoleId = activityBindingsByRoleId;			
		this.roleDefinitionsById = roleDefinitionsById;
		boolean roleManagerChanged = false;			
		Set definedRoleIds = new HashSet(roleDefinitionsById.keySet());		

		if (!definedRoleIds.equals(this.definedRoleIds)) {
			this.definedRoleIds = definedRoleIds;
			roleManagerChanged = true;	
		}

		Collection updatedRoleIds = updateRoles(rolesById.keySet());	
		
		if (roleManagerChanged)
			fireRoleManagerChanged();

		if (updatedRoleIds != null)
			notifyRoles(updatedRoleIds);		
	}

	private boolean updateRole(Role role) {
		boolean updated = false;
		IRoleDefinition roleDefinition = (IRoleDefinition) roleDefinitionsById.get(role.getId());
		updated |= role.setDefined(roleDefinition != null);
		updated |= role.setDescription(roleDefinition != null ? roleDefinition.getDescription() : null);		
		updated |= role.setName(roleDefinition != null ? roleDefinition.getName() : null);
		Set activityBindings = (Set) activityBindingsByRoleId.get(role.getId());
		updated |= role.setActivityBindings(activityBindings != null ? activityBindings : Collections.EMPTY_SET);
		return updated;
	}

	private Collection updateRoles(Collection roleIds) {
		Collection updatedIds = new TreeSet();
		
		for (Iterator iterator = roleIds.iterator(); iterator.hasNext();) {		
			String roleId = (String) iterator.next();					
			Role role = (Role) rolesById.get(roleId);
			
			if (role != null && updateRole(role))
				updatedIds.add(roleId);			
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8945.java