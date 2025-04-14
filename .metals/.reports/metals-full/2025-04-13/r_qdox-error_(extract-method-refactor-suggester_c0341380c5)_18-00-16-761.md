error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18212.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18212.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18212.java
text:
```scala
i@@f (!World.compareLocations && careAboutShadowMungers) {

/* *******************************************************************
 * Copyright (c) 2002 Palo Alto Research Center, Incorporated (PARC).
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     PARC     initial implementation 
 * ******************************************************************/


package org.aspectj.weaver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.aspectj.weaver.patterns.CflowPointcut;
import org.aspectj.weaver.patterns.DeclareParents;
import org.aspectj.weaver.patterns.IVerificationRequired;

/**
 * This holds on to all CrosscuttingMembers for a world.  It handles 
 * management of change.
 * 
 * @author Jim Hugunin
 */
public class CrosscuttingMembersSet {
	private World world;
	//FIXME AV - ? we may need a sequencedHashMap there to ensure source based precedence for @AJ advice
    private Map /* ResolvedType (the aspect) > CrosscuttingMembers */members = new HashMap();
	
	private List shadowMungers             = null;
	private List typeMungers               = null;
    private List lateTypeMungers           = null;
	private List declareSofts              = null;
	private List declareParents            = null;
	private List declareAnnotationOnTypes  = null;
	private List declareAnnotationOnFields = null; 
	private List declareAnnotationOnMethods= null; // includes ctors
	private List declareDominates          = null;
	private boolean changedSinceLastReset = false;

	private List /*IVerificationRequired*/ verificationList = null; // List of things to be verified once the type system is 'complete'
	
	public CrosscuttingMembersSet(World world) {
		this.world = world;
	}

	public boolean addOrReplaceAspect(ResolvedType aspectType) {
		return addOrReplaceAspect(aspectType,true);
	}

	/**
	 * @return whether or not that was a change to the global signature
	 * 			XXX for efficiency we will need a richer representation than this
	 */
	public boolean addOrReplaceAspect(ResolvedType aspectType,boolean careAboutShadowMungers) {
		boolean change = false;
		CrosscuttingMembers xcut = (CrosscuttingMembers)members.get(aspectType);
		if (xcut == null) {
			members.put(aspectType, aspectType.collectCrosscuttingMembers());
			clearCaches();
			CflowPointcut.clearCaches(aspectType);
			change = true;
		} else {
			if (xcut.replaceWith(aspectType.collectCrosscuttingMembers(),careAboutShadowMungers)) {
				clearCaches();

				CflowPointcut.clearCaches(aspectType);
				change = true;
			} else {
				if (careAboutShadowMungers) {
					// bug 134541 - even though we haven't changed we may have updated the 
					// sourcelocation for the shadowMunger which we need to pick up
					shadowMungers = null;
				}
				change = false;
			}
		}
		if (aspectType.isAbstract()) {
			// we might have sub-aspects that need to re-collect their crosscutting members from us
			boolean ancestorChange = addOrReplaceDescendantsOf(aspectType,careAboutShadowMungers); 
			change = change || ancestorChange;
		}
		changedSinceLastReset = changedSinceLastReset || change;
		return change;
	}
    
	private boolean addOrReplaceDescendantsOf(ResolvedType aspectType,boolean careAboutShadowMungers) {
		//System.err.println("Looking at descendants of "+aspectType.getName());
		Set knownAspects = members.keySet();
		Set toBeReplaced = new HashSet();
		for(Iterator it = knownAspects.iterator(); it.hasNext(); ) {
			ResolvedType candidateDescendant = (ResolvedType)it.next();
			if ((candidateDescendant != aspectType) && (aspectType.isAssignableFrom(candidateDescendant))) {
				toBeReplaced.add(candidateDescendant);
			}
		}
		boolean change = false;
		for (Iterator it = toBeReplaced.iterator(); it.hasNext(); ) {
			ResolvedType next = (ResolvedType)it.next();
			boolean thisChange = addOrReplaceAspect(next,careAboutShadowMungers);
			change = change || thisChange;
		}
		return change;
	}
	
    public void addAdviceLikeDeclares(ResolvedType aspectType) {
        CrosscuttingMembers xcut = (CrosscuttingMembers)members.get(aspectType);
        xcut.addDeclares(aspectType.collectDeclares(true));
    }
	
	public boolean deleteAspect(UnresolvedType aspectType) {
		boolean isAspect = members.remove(aspectType) != null;
		clearCaches();
		return isAspect;
	}
	
	public boolean containsAspect(UnresolvedType aspectType) {
		return members.containsKey(aspectType);
	}
    
	//XXX only for testing
	public void addFixedCrosscuttingMembers(ResolvedType aspectType) {
		members.put(aspectType, aspectType.crosscuttingMembers);
		clearCaches();
	}
	
	
	private void clearCaches() {
		shadowMungers = null;
		typeMungers = null;
        lateTypeMungers = null;
		declareSofts = null;
		declareParents = null;
		declareAnnotationOnFields=null;
		declareAnnotationOnMethods=null;
		declareAnnotationOnTypes=null;
		declareDominates = null;
	}
	
	
	public List getShadowMungers() {
		if (shadowMungers == null) {
			ArrayList ret = new ArrayList();
			for (Iterator i = members.values().iterator(); i.hasNext(); ) {
				ret.addAll(((CrosscuttingMembers)i.next()).getShadowMungers());
			}
			shadowMungers = ret;
		}
		return shadowMungers;
	}
	
	public List getTypeMungers() {
		if (typeMungers == null) {
			ArrayList ret = new ArrayList();
			for (Iterator i = members.values().iterator(); i.hasNext(); ) {
				ret.addAll(((CrosscuttingMembers)i.next()).getTypeMungers());
			}
			typeMungers = ret;
		}
		return typeMungers;
	}

    public List getLateTypeMungers() {
        if (lateTypeMungers == null) {
            ArrayList ret = new ArrayList();
            for (Iterator i = members.values().iterator(); i.hasNext(); ) {
                ret.addAll(((CrosscuttingMembers)i.next()).getLateTypeMungers());
            }
            lateTypeMungers = ret;
        }
        return lateTypeMungers;
    }

	public List getDeclareSofts() {
		if (declareSofts == null) {
			Set ret = new HashSet();
			for (Iterator i = members.values().iterator(); i.hasNext(); ) {
				ret.addAll(((CrosscuttingMembers)i.next()).getDeclareSofts());
			}
			declareSofts = new ArrayList();
			declareSofts.addAll(ret);
		}
		return declareSofts;
	}
	
	public List getDeclareParents() {
		if (declareParents == null) {
			Set ret = new HashSet();
			for (Iterator i = members.values().iterator(); i.hasNext(); ) {
				ret.addAll(((CrosscuttingMembers)i.next()).getDeclareParents());
			}
			declareParents = new ArrayList();
			declareParents.addAll(ret);
		}
		return declareParents;
	}
	
	// DECAT Merge multiple together
	public List getDeclareAnnotationOnTypes() {
		if (declareAnnotationOnTypes == null) {
			Set ret = new HashSet();
			for (Iterator i = members.values().iterator(); i.hasNext(); ) {
				ret.addAll(((CrosscuttingMembers)i.next()).getDeclareAnnotationOnTypes());
			}
			declareAnnotationOnTypes = new ArrayList();
			declareAnnotationOnTypes.addAll(ret);
		}
		return declareAnnotationOnTypes;
	}
	
	public List getDeclareAnnotationOnFields() {
		if (declareAnnotationOnFields == null) {
			Set ret = new HashSet();
			for (Iterator i = members.values().iterator(); i.hasNext(); ) {
				ret.addAll(((CrosscuttingMembers)i.next()).getDeclareAnnotationOnFields());
			}
			declareAnnotationOnFields = new ArrayList();
			declareAnnotationOnFields.addAll(ret);
		}
		return declareAnnotationOnFields;
	}
	
	/**
	 * Return an amalgamation of the declare @method/@constructor statements.
	 */
	public List getDeclareAnnotationOnMethods() {
		if (declareAnnotationOnMethods == null) {
			Set ret = new HashSet();
			for (Iterator i = members.values().iterator(); i.hasNext(); ) {
				ret.addAll(((CrosscuttingMembers)i.next()).getDeclareAnnotationOnMethods());
			}
			declareAnnotationOnMethods = new ArrayList();
			declareAnnotationOnMethods.addAll(ret);
		}
		return declareAnnotationOnMethods;
	}
	
	public List getDeclareDominates() {
		if (declareDominates == null) {
			ArrayList ret = new ArrayList();
			for (Iterator i = members.values().iterator(); i.hasNext(); ) {
				ret.addAll(((CrosscuttingMembers)i.next()).getDeclareDominates());
			}
			declareDominates = ret;
		}
		return declareDominates;
	}


	public ResolvedType findAspectDeclaringParents(DeclareParents p) {
		Set keys = this.members.keySet();
		for (Iterator iter = keys.iterator(); iter.hasNext();) {
			ResolvedType element = (ResolvedType) iter.next();
			for (Iterator i = ((CrosscuttingMembers)members.get(element)).getDeclareParents().iterator(); i.hasNext(); ) {
				DeclareParents dp = (DeclareParents)i.next();
				if (dp.equals(p)) return element;
			}
		}
		return null;
	}

	public void reset() {
		verificationList=null;
		changedSinceLastReset = false;
	}
	
	public boolean hasChangedSinceLastReset() {
		return changedSinceLastReset;
	}

	/**
	 * Record something that needs verifying when we believe the type system is complete.
	 * Used for things that can't be verified as we go along - for example some
	 * recursive type variable references (pr133307)
	 */
	public void recordNecessaryCheck(IVerificationRequired verification) {
		if (verificationList==null) verificationList = new ArrayList();
		verificationList.add(verification);
	}
	
	
	/**
	 * Called when type bindings are complete - calls all registered verification
	 * objects in turn.
	 */
	public void verify() {
		if (verificationList==null) return;
		for (Iterator iter = verificationList.iterator(); iter.hasNext();) {
			IVerificationRequired element = (IVerificationRequired) iter.next();
			element.verify();
		}
		verificationList = null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18212.java