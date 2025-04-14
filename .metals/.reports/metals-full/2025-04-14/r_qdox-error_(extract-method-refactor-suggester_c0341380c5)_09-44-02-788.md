error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7115.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7115.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7115.java
text:
```scala
v@@oid setString(int /*long*/ psz, String string) {

/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.accessibility;

import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.internal.ole.win32.*;

class Relation {
	Accessible accessible;
	COMObject objIAccessibleRelation = null;
	int refCount;
	int type;
	Accessible[] targets;
	static final String[] relationTypeString = {
		"controlledBy",		//$NON-NLS-1$
		"controllerFor",	//$NON-NLS-1$
		"describedBy",		//$NON-NLS-1$
		"descriptionFor",	//$NON-NLS-1$
		"embeddedBy",		//$NON-NLS-1$
		"embeds",			//$NON-NLS-1$
		"flowsFrom",		//$NON-NLS-1$
		"flowsTo",			//$NON-NLS-1$
		"labelFor",			//$NON-NLS-1$
		"labelledBy",		//$NON-NLS-1$
		"memberOf",			//$NON-NLS-1$
		"nodeChildOf",		//$NON-NLS-1$
		"parentWindowOf",	//$NON-NLS-1$
		"popupFor",			//$NON-NLS-1$
		"subwindowOf",		//$NON-NLS-1$
	};
	static final String[] localizedRelationTypeString = {
		"controlled by",
		"controller for",
		"described by",
		"description for",
		"embedded by",
		"embeds",
		"flows from",
		"flows to",
		"label for",
		"labelled by",
		"member of",
		"node child of",
		"parent window of",
		"popup for",
		"subwindow of",
	};

	Relation(Accessible accessible, int type) {
		this.accessible = accessible;
		this.type = type;
		this.targets = new Accessible[0];
	}
	
	/* QueryInterface([in] iid, [out] ppvObject)
	 * Ownership of ppvObject transfers from callee to caller so reference count on ppvObject 
	 * must be incremented before returning.  Caller is responsible for releasing ppvObject.
	 */
	int QueryInterface(COMObject comObject, int /*long*/ iid, int /*long*/ ppvObject) {
		GUID guid = new GUID();
		COM.MoveMemory(guid, iid, GUID.sizeof);

		if (COM.IsEqualGUID(guid, COM.IIDIUnknown)) {
			COM.MoveMemory(ppvObject, new int /*long*/[] { comObject.getAddress() }, OS.PTR_SIZEOF);
			AddRef();
			return COM.S_OK;
		}

		if (COM.IsEqualGUID(guid, COM.IIDIAccessibleRelation)) {
			COM.MoveMemory(ppvObject, new int /*long*/[] { objIAccessibleRelation.getAddress() }, OS.PTR_SIZEOF);
			AddRef();
			return COM.S_OK;
		}

		return COM.E_NOINTERFACE;
	}

	int AddRef() {
		if (refCount == 0) {
			/* Create the COMObject on the first AddRef. */
			objIAccessibleRelation = new COMObject(new int[] {2,0,0,1,1,1,2,3}) {
				public int /*long*/ method0(int /*long*/[] args) {return QueryInterface(objIAccessibleRelation, args[0], args[1]);}
				public int /*long*/ method1(int /*long*/[] args) {return AddRef();}
				public int /*long*/ method2(int /*long*/[] args) {return Release();}
				public int /*long*/ method3(int /*long*/[] args) {return get_relationType(args[0]);}
				public int /*long*/ method4(int /*long*/[] args) {return get_localizedRelationType(args[0]);}
				public int /*long*/ method5(int /*long*/[] args) {return get_nTargets(args[0]);}
				public int /*long*/ method6(int /*long*/[] args) {return get_target((int)/*64*/args[0], args[1]);}
				public int /*long*/ method7(int /*long*/[] args) {return get_targets((int)/*64*/args[0], args[1], args[2]);}
			};
		}
		refCount++;
		return refCount;
	}

	int Release() {
		refCount--;

		if (refCount == 0) {
			if (objIAccessibleRelation != null)
				objIAccessibleRelation.dispose();
			objIAccessibleRelation = null;
		}
		return refCount;
	}

	/* get_relationType([out] pbstrRelationType) */
	int get_relationType(int /*long*/ pbstrRelationType) {
		setString(pbstrRelationType, relationTypeString[type]);
		return COM.S_OK;
	}

	/* get_localizedRelationType([out] pbstrLocalizedRelationType) */
	int get_localizedRelationType(int /*long*/ pbstrLocalizedRelationType) {
		setString(pbstrLocalizedRelationType, localizedRelationTypeString[type]);
		return COM.S_OK;
	}

	/* get_nTargets([out] pNTargets) */
	int get_nTargets(int /*long*/ pNTargets) {
		COM.MoveMemory(pNTargets, new int [] { targets.length }, 4);
		return COM.S_OK;
	}

	/* get_target([in] targetIndex, [out] ppTarget) */
	int get_target(int targetIndex, int /*long*/ ppTarget) {
		if (targetIndex < 0 || targetIndex >= targets.length) return COM.E_INVALIDARG;
		Accessible target = targets[targetIndex];
		target.AddRef();
		COM.MoveMemory(ppTarget, new int /*long*/[] { target.objIAccessible.getAddress() }, OS.PTR_SIZEOF);
		return COM.S_OK;
	}

	/* get_targets([in] maxTargets, [out] ppTargets, [out] pNTargets) */
	int get_targets(int maxTargets, int /*long*/ ppTargets, int /*long*/ pNTargets) {
		int count = Math.min(targets.length, maxTargets);
		for (int i = 0; i < count; i++) {
			Accessible target = targets[i];
			target.AddRef();
			COM.MoveMemory(ppTargets + i * OS.PTR_SIZEOF, new int /*long*/[] { target.objIAccessible.getAddress() }, OS.PTR_SIZEOF);
		}
		COM.MoveMemory(pNTargets, new int [] { count }, 4);
		return COM.S_OK;
	}

	void addTarget(Accessible target) {
		Accessible[] newTargets = new Accessible[targets.length + 1];
		System.arraycopy(targets, 0, newTargets, 0, targets.length);
		newTargets[targets.length] = target;
		targets = newTargets;
	}

	void removeTarget(Accessible target) {
		Accessible[] newTargets = new Accessible[targets.length - 1];
		int j = 0;
		for (int i = 0; i < targets.length; i++) {
			if (targets[i] != target) {
				newTargets[j++] = targets[i];
			}
		}
		targets = newTargets;
	}

	boolean hasTargets() {
		return targets.length > 0;
	}

	// setString copied from Accessible class
	void setString(int psz, String string) {
		char[] data = (string + "\0").toCharArray();
		int /*long*/ ptr = COM.SysAllocString(data);
		COM.MoveMemory(psz, new int /*long*/ [] { ptr }, OS.PTR_SIZEOF);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7115.java