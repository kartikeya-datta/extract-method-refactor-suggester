error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17329.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17329.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17329.java
text:
```scala
c@@har[][] splitName = WildTypePattern.splitNames(name,true);

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


package org.aspectj.ajdt.internal.compiler.lookup;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.IMessageHandler;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.bridge.Message;
import org.aspectj.weaver.IHasPosition;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.World;
import org.aspectj.weaver.patterns.*;
import org.aspectj.weaver.patterns.FormalBinding;
import org.aspectj.weaver.patterns.IScope;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.ClassScope;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.CompilationUnitScope;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.ImportBinding;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.Scope;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.aspectj.org.eclipse.jdt.core.compiler.CharOperation;

/**
 * Adaptor from org.eclipse.jdt.internal.compiler.lookup.Scope to org.aspectj.weaver.IScope
 * 
 * @author Jim Hugunin
 */
public class EclipseScope implements IScope {
	private Scope scope;
	private EclipseFactory world;
	private ResolvedType enclosingType;
	private FormalBinding[] bindings;
	
	private String[] importedPrefixes = null;
	private String[] importedNames = null;


	public EclipseScope(FormalBinding[] bindings, Scope scope) {
		this.bindings = bindings;
		
		this.scope = scope;
		this.world = EclipseFactory.fromScopeLookupEnvironment(scope);
		
		this.enclosingType = world.fromEclipse(scope.enclosingSourceType());
	}
	
	
	public UnresolvedType lookupType(String name, IHasPosition location) {
		char[][] splitName = WildTypePattern.splitNames(name);
		TypeBinding b = scope.getType(splitName,splitName.length);
		//FIXME ??? need reasonable error handling...
		if (!b.isValidBinding()) {
			return ResolvedType.MISSING;
		}
		
		//System.err.println("binding: " + b);
		//  Binding(tokens, bits & RestrictiveFlagMASK, this)
		return world.fromBinding(b);
		
		/*
		computeImports();
		
//		System.out.println("lookup: " + name + " in " + 
//			Arrays.asList(importedPrefixes));
		
		ResolvedType ret = null;
		String dotName = "." + name;
		for (int i=0; i<importedNames.length; i++) {
			String importedName = importedNames[i];
			//??? can this be right
			if (importedName.endsWith(name) && 
				((importedName.length() == name.length()) ||
				 (importedName.endsWith(dotName))))
			{
				ResolvedType found = resolveVisible(importedName);
				if (found == ResolvedType.MISSING) continue;
				if (ret != null) {
					message(IMessage.ERROR, location, 
						"ambiguous type reference, both " + ret.getName() + " and " + importedName);
					return ResolvedType.MISSING;
				} else {
					ret = found;
				}
			}
		}
		
		if (ret != null) return ret;
		
		//XXX need to handle ambiguous references here
		for (int i=0; i<importedPrefixes.length; i++) {
			String importedPrefix = importedPrefixes[i];
			ResolvedType tryType = resolveVisible(importedPrefix + name);
			if (tryType != ResolvedType.MISSING) {
				return tryType;
			}
		}

		return resolveVisible(name);
		*/
	}
	
	
//	private ResolvedType resolveVisible(String name) {
//		ResolvedType found = world.getWorld().resolve(UnresolvedType.forName(name), true);
//		if (found == ResolvedType.MISSING) return found;
//		if (ResolvedType.isVisible(found.getModifiers(), found, enclosingType)) return found;
//		return ResolvedType.MISSING; 
//	}
	

//	public UnresolvedType lookupType(String name, IHasPosition location) {
//		char[][] namePieces = CharOperation.splitOn('.', name.toCharArray());
//		TypeBinding binding;
//		if (namePieces.length == 1) {
//			binding = scope.getType(namePieces[0]);
//		} else {
//			binding = scope.getType(namePieces);
//		}
//		
//		
//		if (!binding.isValidBinding()) {
//			//XXX do we do this always or sometimes
//			System.err.println("error: " + binding);
//			scope.problemReporter().invalidType(EclipseWorld.astForLocation(location), binding);
//			return ResolvedType.MISSING;
//		}
//		//??? do we want this too
////		if (AstNode.isTypeUseDeprecated(binding, scope))
////			scope.problemReporter().deprecatedType(binding, EclipseWorld.astForLocation(location));
//		
//		return EclipseWorld.fromBinding(binding);
//	}
	
	
	
	private void computeImports() {
		if (importedNames != null) return;
		
		List importedNamesList = new ArrayList();
		List importedPrefixesList = new ArrayList();
		
		
		Scope currentScope = scope;
		//add any enclosing types to this list
		while (!(currentScope instanceof CompilationUnitScope)) {
			if (currentScope == null) {
				throw new RuntimeException("unimplemented");
			}
			if (currentScope instanceof ClassScope) {
				addClassAndParentsToPrefixes(((ClassScope)currentScope).referenceType().binding, importedPrefixesList);
			}
			currentScope = currentScope.parent;
		}
		
		CompilationUnitScope cuScope = (CompilationUnitScope)currentScope;

		String packageName = 
			new String(CharOperation.concatWith(cuScope.currentPackageName, '.'));
		//System.err.println("package: " + packageName);
		if (packageName.length() > 0) {
			importedPrefixesList.add(packageName + ".");
		}
		

		ImportBinding[] imports = cuScope.imports;
		for (int i = 0; i < imports.length; i++) {
			ImportBinding importBinding = imports[i];
			String importName =
			  new String(CharOperation.concatWith(importBinding.compoundName, '.'));
			
			//XXX wrong behavior for java.util.Map.*
			if (importBinding.onDemand) {
				importedPrefixesList.add(importName + ".");
			} else {
				importedNamesList.add(importName);
			}
		}
		
		TypeBinding[] topTypes = cuScope.topLevelTypes;
		for (int i = 0; i < topTypes.length; i++) {
			importedNamesList.add(world.fromBinding(topTypes[i]).getName());
		}
		
		importedNames =
			(String[])importedNamesList.toArray(new String[importedNamesList.size()]);
		
		importedPrefixes =
			(String[])importedPrefixesList.toArray(new String[importedPrefixesList.size()]);
	}

	private void addClassAndParentsToPrefixes(
		ReferenceBinding binding,
		List importedPrefixesList)
	{
		if (binding == null) return;
		importedPrefixesList.add(world.fromBinding(binding).getName()+"$");
		
		addClassAndParentsToPrefixes(binding.superclass(), importedPrefixesList);
		ReferenceBinding[] superinterfaces = binding.superInterfaces();
		if (superinterfaces != null) {
			for (int i = 0; i < superinterfaces.length; i++) {
				addClassAndParentsToPrefixes(superinterfaces[i], importedPrefixesList);
			}
		}
	}
	

	public String[] getImportedNames() {
		computeImports();
		return importedNames;
	}

	public String[] getImportedPrefixes() {
		computeImports();
		//System.err.println("prefixes: " + Arrays.asList(importedPrefixes));
		return importedPrefixes;
	}


	//XXX add good errors when would bind to extra parameters
    public FormalBinding lookupFormal(String name) {
        for (int i = 0, len = bindings.length; i < len; i++) {
            if (bindings[i].getName().equals(name)) return bindings[i];
        }
        return null;
    }


	public FormalBinding getFormal(int i) {
		return bindings[i];
	}

    public int getFormalCount() {
    	return bindings.length;
    }

	public ISourceLocation makeSourceLocation(IHasPosition location) {
		return new EclipseSourceLocation(scope.problemReporter().referenceContext.compilationResult(),
						location.getStart(), location.getEnd());
	}

	public IMessageHandler getMessageHandler() {
		return world.getWorld().getMessageHandler();
	}


	public void message(
		IMessage.Kind kind,
		IHasPosition location1,
		IHasPosition location2,
		String message) {
			message(kind, location1, message);
			message(kind, location2, message);
	}

	public void message(
		IMessage.Kind kind,
		IHasPosition location,
		String message) {
			//System.out.println("message: " + message + " loc: " + makeSourceLocation(location));
			getMessageHandler()
			.handleMessage(new Message(message, kind, null, makeSourceLocation(location)));

	}
	
	public void message(IMessage aMessage) {
		getMessageHandler().handleMessage(aMessage);
	}

	public World getWorld() {
		return world.getWorld();
	}

	public ResolvedType getEnclosingType() {
		return enclosingType;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17329.java