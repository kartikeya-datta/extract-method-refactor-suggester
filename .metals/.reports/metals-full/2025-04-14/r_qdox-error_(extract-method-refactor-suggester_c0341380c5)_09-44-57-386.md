error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5780.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5780.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5780.java
text:
```scala
r@@eturn this.changes.size() != 0;

/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.core.hierarchy;

import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.JavaElement;
import org.eclipse.jdt.internal.core.SimpleDelta;

/*
 * Collects changes (reported through fine-grained deltas) that can affect a type hierarchy.
 */
public class ChangeCollector {
	
	/*
	 * A table from ITypes to TypeDeltas
	 */
	HashMap changes = new HashMap();
	
	TypeHierarchy hierarchy;
	
	public ChangeCollector(TypeHierarchy hierarchy) {
		this.hierarchy = hierarchy;
	}
	
	/*
	 * Adds the children of the given delta to the list of changes.
	 */
	private void addAffectedChildren(IJavaElementDelta delta) throws JavaModelException {
		IJavaElementDelta[] children = delta.getAffectedChildren();
		for (int i = 0, length = children.length; i < length; i++) {
			IJavaElementDelta child = children[i];
			IJavaElement childElement = child.getElement();
			switch (childElement.getElementType()) {
				case IJavaElement.IMPORT_CONTAINER:
					addChange((IImportContainer)childElement, child);
					break;
				case IJavaElement.IMPORT_DECLARATION:
					addChange((IImportDeclaration)childElement, child);
					break;
				case IJavaElement.TYPE:
					addChange((IType)childElement, child);
					break;
				case IJavaElement.INITIALIZER:
				case IJavaElement.FIELD:
				case IJavaElement.METHOD:
					addChange((IMember)childElement, child);
					break;
			}
		}
	}
	
	/*
	 * Adds the given delta on a compilation unit to the list of changes.
	 */
	public void addChange(ICompilationUnit cu, IJavaElementDelta newDelta) throws JavaModelException {
		int newKind = newDelta.getKind();
		switch (newKind) {
			case IJavaElementDelta.ADDED:
				ArrayList allTypes = new ArrayList();
				getAllTypesFromElement(cu, allTypes);
				for (int i = 0, length = allTypes.size(); i < length; i++) {
					IType type = (IType)allTypes.get(i);
					addTypeAddition(type, (SimpleDelta)this.changes.get(type));
				}
				break;
			case IJavaElementDelta.REMOVED:
				allTypes = new ArrayList();
				getAllTypesFromHierarchy((JavaElement)cu, allTypes);
				for (int i = 0, length = allTypes.size(); i < length; i++) {
					IType type = (IType)allTypes.get(i);
					addTypeRemoval(type, (SimpleDelta)this.changes.get(type));
				}
				break;
			case IJavaElementDelta.CHANGED:
				addAffectedChildren(newDelta);
				break;
		}
	}
	
	private void addChange(IImportContainer importContainer, IJavaElementDelta newDelta) throws JavaModelException {
		int newKind = newDelta.getKind();
		if (newKind == IJavaElementDelta.CHANGED) {
			addAffectedChildren(newDelta);
			return;
		}
		SimpleDelta existingDelta = (SimpleDelta)this.changes.get(importContainer);
		if (existingDelta != null) {
			switch (newKind) {
				case IJavaElementDelta.ADDED:
					if (existingDelta.getKind() == IJavaElementDelta.REMOVED) {
						// REMOVED then ADDED
						this.changes.remove(importContainer);
					}
					break;
				case IJavaElementDelta.REMOVED:
					if (existingDelta.getKind() == IJavaElementDelta.ADDED) {
						// ADDED then REMOVED
						this.changes.remove(importContainer);
					}
					break;
					// CHANGED handled above
			}
		} else {
			SimpleDelta delta = new SimpleDelta();
			switch (newKind) {
				case IJavaElementDelta.ADDED:
					delta.added();
					break;
				case IJavaElementDelta.REMOVED:
					delta.removed();
					break;
			}
			this.changes.put(importContainer, delta);
		}
	}

	private void addChange(IImportDeclaration importDecl, IJavaElementDelta newDelta) {
		SimpleDelta existingDelta = (SimpleDelta)this.changes.get(importDecl);
		int newKind = newDelta.getKind();
		if (existingDelta != null) {
			switch (newKind) {
				case IJavaElementDelta.ADDED:
					if (existingDelta.getKind() == IJavaElementDelta.REMOVED) {
						// REMOVED then ADDED
						this.changes.remove(importDecl);
					}
					break;
				case IJavaElementDelta.REMOVED:
					if (existingDelta.getKind() == IJavaElementDelta.ADDED) {
						// ADDED then REMOVED
						this.changes.remove(importDecl);
					}
					break;
				// CHANGED cannot happen for import declaration
			}
		} else {
			SimpleDelta delta = new SimpleDelta();
			switch (newKind) {
				case IJavaElementDelta.ADDED:
					delta.added();
					break;
				case IJavaElementDelta.REMOVED:
					delta.removed();
					break;
			}
			this.changes.put(importDecl, delta);
		}
	}
	
	/*
	 * Adds a change for the given member (a method, a field or an initializer) and the types it defines.
	 */
	private void addChange(IMember member, IJavaElementDelta newDelta) throws JavaModelException {
		int newKind = newDelta.getKind();
		switch (newKind) {
			case IJavaElementDelta.ADDED:
				ArrayList allTypes = new ArrayList();
				getAllTypesFromElement(member, allTypes);
				for (int i = 0, length = allTypes.size(); i < length; i++) {
					IType innerType = (IType)allTypes.get(i);
					addTypeAddition(innerType, (SimpleDelta)this.changes.get(innerType));
				}
				break;
			case IJavaElementDelta.REMOVED:
				allTypes = new ArrayList();
				getAllTypesFromHierarchy((JavaElement)member, allTypes);
				for (int i = 0, length = allTypes.size(); i < length; i++) {
					IType type = (IType)allTypes.get(i);
					addTypeRemoval(type, (SimpleDelta)this.changes.get(type));
				}
				break;
			case IJavaElementDelta.CHANGED:
				addAffectedChildren(newDelta);
				break;
		}
	}
	
	/*
	 * Adds a change for the given type and the types it defines.
	 */
	private void addChange(IType type, IJavaElementDelta newDelta) throws JavaModelException {
		 int newKind = newDelta.getKind();
		SimpleDelta existingDelta = (SimpleDelta)this.changes.get(type);
		switch (newKind) {
			case IJavaElementDelta.ADDED:
				addTypeAddition(type, existingDelta);
				ArrayList allTypes = new ArrayList();
				getAllTypesFromElement(type, allTypes);
				for (int i = 0, length = allTypes.size(); i < length; i++) {
					IType innerType = (IType)allTypes.get(i);
					addTypeAddition(innerType, (SimpleDelta)this.changes.get(innerType));
				}
				break;
			case IJavaElementDelta.REMOVED:
				addTypeRemoval(type, existingDelta);
				allTypes = new ArrayList();
				getAllTypesFromHierarchy((JavaElement)type, allTypes);
				for (int i = 0, length = allTypes.size(); i < length; i++) {
					IType innerType = (IType)allTypes.get(i);
					addTypeRemoval(innerType, (SimpleDelta)this.changes.get(innerType));
				}
				break;
			case IJavaElementDelta.CHANGED:
				addTypeChange(type, newDelta.getFlags(), existingDelta);
				addAffectedChildren(newDelta);
				break;
		}
	}

	private void addTypeAddition(IType type, SimpleDelta existingDelta) throws JavaModelException {
		if (existingDelta != null) {
			switch (existingDelta.getKind()) {
				case IJavaElementDelta.REMOVED:
					// REMOVED then ADDED
					boolean hasChange = false;
					if (hasSuperTypeChange(type)) {
						existingDelta.superTypes();
						hasChange = true;
					} 
					if (hasVisibilityChange(type)) {
						existingDelta.modifiers();
						hasChange = true;
					}
					if (!hasChange) {
						this.changes.remove(type);
					}
					break;
					// CHANGED then ADDED
					// or ADDED then ADDED: should not happen
			}
		} else {
			// check whether the type addition affects the hierarchy
			String typeName = type.getElementName();
			if (this.hierarchy.hasSupertype(typeName) 
 this.hierarchy.subtypesIncludeSupertypeOf(type) 
 this.hierarchy.missingTypes.contains(typeName)) {
				SimpleDelta delta = new SimpleDelta();
				delta.added();
				this.changes.put(type, delta);
			}
		}
	}
	
	private void addTypeChange(IType type, int newFlags, SimpleDelta existingDelta) throws JavaModelException {
		if (existingDelta != null) {
			switch (existingDelta.getKind()) {
				case IJavaElementDelta.CHANGED:
					// CHANGED then CHANGED
					int existingFlags = existingDelta.getFlags();
					boolean hasChange = false;
					if ((existingFlags & IJavaElementDelta.F_SUPER_TYPES) != 0
							&& hasSuperTypeChange(type)) {
						existingDelta.superTypes();
						hasChange = true;
					} 
					if ((existingFlags & IJavaElementDelta.F_MODIFIERS) != 0
							&& hasVisibilityChange(type)) {
						existingDelta.modifiers();
						hasChange = true;
					}
					if (!hasChange) {
						// super types and visibility are back to the ones in the existing hierarchy
						this.changes.remove(type);
					}
					break;
					// ADDED then CHANGED: leave it as ADDED
					// REMOVED then CHANGED: should not happen
			}
		} else {
			// check whether the type change affects the hierarchy
			SimpleDelta typeDelta = null;
			if ((newFlags & IJavaElementDelta.F_SUPER_TYPES) != 0 
					&& this.hierarchy.includesTypeOrSupertype(type)) {
				typeDelta = new SimpleDelta();
				typeDelta.superTypes();
			}
			if ((newFlags & IJavaElementDelta.F_MODIFIERS) != 0
					&& (this.hierarchy.hasSupertype(type.getElementName())
 type.equals(this.hierarchy.focusType))) {
				if (typeDelta == null) {
					typeDelta = new SimpleDelta();
				}
				typeDelta.modifiers();
			}
			if (typeDelta != null) {
				this.changes.put(type, typeDelta);
			}
		}
	}

	private void addTypeRemoval(IType type, SimpleDelta existingDelta) {
		if (existingDelta != null) {
			switch (existingDelta.getKind()) {
				case IJavaElementDelta.ADDED:
					// ADDED then REMOVED
					this.changes.remove(type);
					break;
				case IJavaElementDelta.CHANGED:
					// CHANGED then REMOVED
					existingDelta.removed();
					break;
					// REMOVED then REMOVED: should not happen
			}
		} else {
			// check whether the type removal affects the hierarchy
			if (this.hierarchy.contains(type)) {
				SimpleDelta typeDelta = new SimpleDelta();
				typeDelta.removed();
				this.changes.put(type, typeDelta);
			}
		}
	}
	
	/*
	 * Returns all types defined in the given element excluding the given element.
	 */
	private void getAllTypesFromElement(IJavaElement element, ArrayList allTypes) throws JavaModelException {
		switch (element.getElementType()) {
			case IJavaElement.COMPILATION_UNIT:
				IType[] types = ((ICompilationUnit)element).getTypes();
				for (int i = 0, length = types.length; i < length; i++) {
					IType type = types[i];
					allTypes.add(type);
					getAllTypesFromElement(type, allTypes);
				}
				break;
			case IJavaElement.TYPE:
				types = ((IType)element).getTypes();
				for (int i = 0, length = types.length; i < length; i++) {
					IType type = types[i];
					allTypes.add(type);
					getAllTypesFromElement(type, allTypes);
				}
				break;
			case IJavaElement.INITIALIZER:
			case IJavaElement.FIELD:
			case IJavaElement.METHOD:
				IJavaElement[] children = ((IMember)element).getChildren();
				for (int i = 0, length = children.length; i < length; i++) {
					IType type = (IType)children[i];
					allTypes.add(type);
					getAllTypesFromElement(type, allTypes);
				}
				break;
		}
	}
	
	/*
	 * Returns all types in the existing hierarchy that have the given element as a parent.
	 */
	private void getAllTypesFromHierarchy(JavaElement element, ArrayList allTypes) {
		switch (element.getElementType()) {
			case IJavaElement.COMPILATION_UNIT:
				ArrayList types = (ArrayList)this.hierarchy.files.get(element);
				if (types != null) {
					allTypes.addAll(types);
				}
				break;
			case IJavaElement.TYPE:
			case IJavaElement.INITIALIZER:
			case IJavaElement.FIELD:
			case IJavaElement.METHOD:
				types = (ArrayList)this.hierarchy.files.get(((IMember)element).getCompilationUnit());
				if (types != null) {
					for (int i = 0, length = types.size(); i < length; i++) {
						IType type = (IType)types.get(i);
						if (element.isAncestorOf(type)) {
							allTypes.add(type);
						}
					}
				}
				break;
		}
	}
	
	private boolean hasSuperTypeChange(IType type) throws JavaModelException {
		// check super class
		IType superclass = this.hierarchy.getSuperclass(type);
		String existingSuperclassName = superclass == null ? null : superclass.getElementName();
		String newSuperclassName = type.getSuperclassName();
		if (existingSuperclassName != null && !existingSuperclassName.equals(newSuperclassName)) {
			return true;
		}
		
		// check super interfaces
		IType[] existingSuperInterfaces = this.hierarchy.getSuperInterfaces(type);
		String[] newSuperInterfaces = type.getSuperInterfaceNames();
		if (existingSuperInterfaces.length != newSuperInterfaces.length) {
			return true;
		}
		for (int i = 0, length = newSuperInterfaces.length; i < length; i++) {
			String superInterfaceName = newSuperInterfaces[i];
			if (!superInterfaceName.equals(newSuperInterfaces[i])) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean hasVisibilityChange(IType type) throws JavaModelException {
		int existingFlags = this.hierarchy.getCachedFlags(type);
		int newFlags = type.getFlags();
		return existingFlags != newFlags;
	}

	/*
	 * Whether the hierarchy needs refresh according to the changes collected so far.
	 */
	public boolean needsRefresh() {
		return changes.size() != 0;
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		Iterator iterator = this.changes.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry)iterator.next();
			buffer.append(((JavaElement)entry.getKey()).toDebugString());
			buffer.append(entry.getValue());
			if (iterator.hasNext()) {
				buffer.append('\n');
			}
		}
		return buffer.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5780.java