error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2135.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2135.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2135.java
text:
```scala
r@@oleEvent = new RoleEvent(this, false, false, false, false);

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
import java.util.List;
import java.util.Set;

import org.eclipse.ui.internal.util.Util;
import org.eclipse.ui.roles.IActivityBinding;
import org.eclipse.ui.roles.IRole;
import org.eclipse.ui.roles.IRoleEvent;
import org.eclipse.ui.roles.IRoleListener;
import org.eclipse.ui.roles.RoleNotDefinedException;

final class Role implements IRole {

	private final static int HASH_FACTOR = 89;
	private final static int HASH_INITIAL = Role.class.getName().hashCode();

	private IRoleEvent roleEvent;
	private List roleListeners;
	private boolean defined;
	private String description;
	private String id;
	private String name;
	private Set activityBindings;

	private transient int hashCode;
	private transient boolean hashCodeComputed;
	private transient IActivityBinding[] activityBindingsAsArray;
	private transient String string;
	
	Role(String id) {	
		if (id == null)
			throw new NullPointerException();

		this.id = id;
	}

	public void addRoleListener(IRoleListener roleListener) {
		if (roleListener == null)
			throw new NullPointerException();
		
		if (roleListeners == null)
			roleListeners = new ArrayList();
		
		if (!roleListeners.contains(roleListener))
			roleListeners.add(roleListener);
	}

	public int compareTo(Object object) {
		Role role = (Role) object;
		int compareTo = Util.compare(defined, role.defined);

		if (compareTo == 0) {
			compareTo = Util.compare(description, role.description);

			if (compareTo == 0) {		
				compareTo = Util.compare(id, role.id);			
					
				if (compareTo == 0) {
					compareTo = Util.compare(name, role.name);

					if (compareTo == 0) 
						compareTo = Util.compare((Comparable[]) activityBindingsAsArray, (Comparable[]) role.activityBindingsAsArray); 
				}
			}
		}
		
		return compareTo;	
	}
	
	public boolean equals(Object object) {
		if (!(object instanceof Role))
			return false;

		Role role = (Role) object;	
		boolean equals = true;
		equals &= Util.equals(defined, role.defined);
		equals &= Util.equals(description, role.description);
		equals &= Util.equals(id, role.id);
		equals &= Util.equals(name, role.name);
		equals &= Util.equals(activityBindings, role.activityBindings);		
		return equals;
	}

	public Set getActivityBindings() {
		return activityBindings;
	}			
	
	public String getDescription()
		throws RoleNotDefinedException {
		if (!defined)
			throw new RoleNotDefinedException();
			
		return description;	
	}
	
	public String getId() {
		return id;	
	}
	
	public String getName()
		throws RoleNotDefinedException {
		if (!defined)
			throw new RoleNotDefinedException();

		return name;
	}	
	
	public int hashCode() {
		if (!hashCodeComputed) {
			hashCode = HASH_INITIAL;
			hashCode = hashCode * HASH_FACTOR + Util.hashCode(defined);	
			hashCode = hashCode * HASH_FACTOR + Util.hashCode(description);
			hashCode = hashCode * HASH_FACTOR + Util.hashCode(id);
			hashCode = hashCode * HASH_FACTOR + Util.hashCode(name);
			hashCode = hashCode * HASH_FACTOR + Util.hashCode(activityBindings);
			hashCodeComputed = true;
		}
			
		return hashCode;		
	}

	public boolean isDefined() {
		return defined;
	}

	public void removeRoleListener(IRoleListener roleListener) {
		if (roleListener == null)
			throw new NullPointerException();

		if (roleListeners != null)
			roleListeners.remove(roleListener);
	}

	public String toString() {
		if (string == null) {
			final StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append('[');
			stringBuffer.append(defined);
			stringBuffer.append(',');
			stringBuffer.append(description);
			stringBuffer.append(',');
			stringBuffer.append(id);
			stringBuffer.append(',');
			stringBuffer.append(name);
			stringBuffer.append(',');
			stringBuffer.append(activityBindings);
			stringBuffer.append(']');
			string = stringBuffer.toString();
		}
	
		return string;		
	}
	
	void fireRoleChanged() {
		if (roleListeners != null) {
			for (int i = 0; i < roleListeners.size(); i++) {
				if (roleEvent == null)
					roleEvent = new RoleEvent(this);
							
				((IRoleListener) roleListeners.get(i)).roleChanged(roleEvent);
			}				
		}			
	}
	
	boolean setActivityBindings(Set activityBindings) {
		activityBindings = Util.safeCopy(activityBindings, IActivityBinding.class);
		
		if (!Util.equals(activityBindings, this.activityBindings)) {
			this.activityBindings = activityBindings;
			this.activityBindingsAsArray = (IActivityBinding[]) this.activityBindings.toArray(new IActivityBinding[this.activityBindings.size()]);
			hashCodeComputed = false;
			hashCode = 0;
			string = null;
			return true;
		}		
	
		return false;
	}		
	
	boolean setDefined(boolean defined) {
		if (defined != this.defined) {
			this.defined = defined;
			hashCodeComputed = false;
			hashCode = 0;
			string = null;
			return true;
		}		

		return false;
	}

	boolean setDescription(String description) {
		if (!Util.equals(description, this.description)) {
			this.description = description;
			hashCodeComputed = false;
			hashCode = 0;
			string = null;
			return true;
		}		

		return false;
	}

	boolean setName(String name) {
		if (!Util.equals(name, this.name)) {
			this.name = name;
			hashCodeComputed = false;
			hashCode = 0;
			string = null;
			return true;
		}		

		return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2135.java