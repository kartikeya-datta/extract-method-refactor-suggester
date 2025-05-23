error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10257.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10257.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10257.java
text:
```scala
i@@f (!aBaseType.isRawType()) throw new IllegalStateException("Expecting raw type, not: "+aBaseType);

/* *******************************************************************
 * Copyright (c) 2005 Contributors.
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://eclipse.org/legal/epl-v10.html 
 *  
 * Contributors: 
 *   Adrian Colyer			Initial implementation
 * ******************************************************************/
package org.aspectj.weaver;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.weaver.UnresolvedType.TypeKind;

/**
 * @author colyer
 *
 */
public class TypeFactory {

	/**
	 * Create a parameterized version of a generic type.
	 * @param aGenericType
	 * @param someTypeParameters note, in the case of an inner type of a parameterized type,
	 *         this parameter may legitimately be null
	 * @param inAWorld
	 * @return
	 */
	public static ReferenceType createParameterizedType(
		ResolvedType aBaseType,
		UnresolvedType[] someTypeParameters,
		World inAWorld
	) {
		ResolvedType baseType = aBaseType;
		if (!aBaseType.isGenericType()) {
			// try and find the generic type...
			if (someTypeParameters != null && someTypeParameters.length>0) {
				if (!aBaseType.isRawType()) throw new IllegalStateException("Expecting raw type");
				baseType = baseType.getGenericType();
				if (baseType == null) throw new IllegalStateException("Raw type does not have generic type set");
			} // else if someTypeParameters is null, then the base type is allowed to be non-generic, it's an inner
		}
		ResolvedType[] resolvedParameters = inAWorld.resolve(someTypeParameters);
		ReferenceType pType = new ReferenceType(baseType,resolvedParameters,inAWorld);
//		pType.setSourceContext(aBaseType.getSourceContext());
		return (ReferenceType) pType.resolve(inAWorld);
	}
	
	/**
	 * Create an *unresolved* parameterized version of a generic type.
	 */
	public static UnresolvedType createUnresolvedParameterizedType(String sig,String erasuresig,UnresolvedType[] arguments) {
	  return new UnresolvedType(sig,erasuresig,arguments);
	}

	public static ReferenceType createRawType(
			ResolvedType aBaseType,
			World inAWorld
		) {
		if (aBaseType.isRawType()) return (ReferenceType) aBaseType;
		if (!aBaseType.isGenericType()) {
			if (!aBaseType.isRawType()) throw new IllegalStateException("Expecting generic type");
		}
		ReferenceType rType = new ReferenceType(aBaseType,inAWorld);
		//rType.setSourceContext(aBaseType.getSourceContext());
		return (ReferenceType) rType.resolve(inAWorld);
	}
	
	/**
	 * Creates a sensible unresolvedtype from some signature, for example:
	 * signature = LIGuard<TT;>;
	 * bound =  toString=IGuard<T>   sig=PIGuard<TT;>;     sigErasure=LIGuard;  kind=parameterized
	 */
	private static UnresolvedType convertSigToType(String aSignature) {	
	    UnresolvedType bound = null;
		int startOfParams = aSignature.indexOf('<');
		if (startOfParams==-1) {
			bound = UnresolvedType.forSignature(aSignature);
		} else {
			int endOfParams = aSignature.lastIndexOf('>');
			String signatureErasure = "L" + aSignature.substring(1,startOfParams) + ";";
			UnresolvedType[] typeParams = createTypeParams(aSignature.substring(startOfParams +1, endOfParams));
			bound = new UnresolvedType("P"+aSignature.substring(1),signatureErasure,typeParams);
		}
		return bound;
	}
	
	/**
	 * Used by UnresolvedType.read, creates a type from a full signature.
	 * @param signature
	 * @return
	 */
	public static UnresolvedType createTypeFromSignature(String signature) {
		if (signature.equals(ResolvedType.MISSING_NAME)) return ResolvedType.MISSING;
		char firstChar = signature.charAt(0);
		if (firstChar=='P') {
			// parameterized type, calculate signature erasure and type parameters
			// (see pr122458) It is possible for a parameterized type to have *no* type parameters visible in its signature.
			// This happens for an inner type of a parameterized type which simply inherits the type parameters
			// of its parent.  In this case it is parameterized but theres no < in the signature.
			
			int startOfParams = signature.indexOf('<');
			int endOfParams = signature.lastIndexOf('>');
			if (startOfParams==-1) {
				// Should be an inner type of a parameterized type - could assert there is a '$' in the signature....
				String signatureErasure = "L" + signature.substring(1);
				UnresolvedType[] typeParams = new UnresolvedType[0];
				return new UnresolvedType(signature,signatureErasure,typeParams);
			} else {
				String signatureErasure = "L" + signature.substring(1,startOfParams) + ";";
				UnresolvedType[] typeParams = createTypeParams(signature.substring(startOfParams +1, endOfParams));
				return new UnresolvedType(signature,signatureErasure,typeParams);
			}
			// can't replace above with convertSigToType - leads to stackoverflow
		} else if (signature.equals("?")){
			UnresolvedType ret = UnresolvedType.SOMETHING;
			ret.typeKind = TypeKind.WILDCARD;
			return ret;
		} else if(firstChar=='+') { 
			// ? extends ...
			UnresolvedType ret = new UnresolvedType(signature);
			ret.typeKind = TypeKind.WILDCARD;
			
//			UnresolvedType bound1 = UnresolvedType.forSignature(signature.substring(1));
//			UnresolvedType bound2 = convertSigToType(signature.substring(1));
			ret.setUpperBound(convertSigToType(signature.substring(1)));
			return ret;
		} else if (firstChar=='-') { 
			// ? super ...
//			UnresolvedType bound = UnresolvedType.forSignature(signature.substring(1));
//			UnresolvedType bound2 = convertSigToType(signature.substring(1));
			UnresolvedType ret = new UnresolvedType(signature);
			ret.typeKind = TypeKind.WILDCARD;
			ret.setLowerBound(convertSigToType(signature.substring(1)));
			return ret;
		} else if (firstChar=='T') {
			String typeVariableName = signature.substring(1);
			if (typeVariableName.endsWith(";")) {
				typeVariableName = typeVariableName.substring(0, typeVariableName.length() -1);
			}
			return new UnresolvedTypeVariableReferenceType(new TypeVariable(typeVariableName));
		} else if (firstChar=='[') { 
			int dims = 0;
			while (signature.charAt(dims)=='[') dims++;
			UnresolvedType componentType = createTypeFromSignature(signature.substring(dims));
			return new UnresolvedType(signature,
					signature.substring(0,dims)+componentType.getErasureSignature());
		} else if (signature.length()==1) { // could be a primitive
		  switch (firstChar) {
		  	  case 'V': return ResolvedType.VOID;
		  	  case 'Z': return ResolvedType.BOOLEAN;
			  case 'B': return ResolvedType.BYTE;
			  case 'C': return ResolvedType.CHAR;
			  case 'D': return ResolvedType.DOUBLE;
			  case 'F': return ResolvedType.FLOAT;
			  case 'I': return ResolvedType.INT;
			  case 'J': return ResolvedType.LONG;
			  case 'S': return ResolvedType.SHORT;
		  }
		}
		return new UnresolvedType(signature);
	}
	
	private static UnresolvedType[] createTypeParams(String typeParameterSpecification) {
		String remainingToProcess = typeParameterSpecification;
		List types = new ArrayList();
		while(!remainingToProcess.equals("")) {
			int endOfSig = 0;
			int anglies = 0;
			boolean sigFound = false;
			for (endOfSig = 0; (endOfSig < remainingToProcess.length()) && !sigFound; endOfSig++) {
				char thisChar = remainingToProcess.charAt(endOfSig);
				switch(thisChar) {
				case '<' : anglies++; break;
				case '>' : anglies--; break;
				case ';' : 
					if (anglies == 0) {
						sigFound = true;
						break;
					}
				}
			}
			types.add(createTypeFromSignature(remainingToProcess.substring(0,endOfSig)));
			remainingToProcess = remainingToProcess.substring(endOfSig);
		}
		UnresolvedType[] typeParams = new UnresolvedType[types.size()];
		types.toArray(typeParams);
		return typeParams;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10257.java