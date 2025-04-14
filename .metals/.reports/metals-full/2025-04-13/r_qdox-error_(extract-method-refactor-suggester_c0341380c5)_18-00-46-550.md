error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/783.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/783.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/783.java
text:
```scala
i@@f (!ResolvedType.isMissing(otherType)) {

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


package org.aspectj.weaver.patterns;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.AjAttribute;
import org.aspectj.weaver.BCException;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.TypeVariableReference;
import org.aspectj.weaver.TypeVariableReferenceType;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.VersionedDataInputStream;

public class ExactTypePattern extends TypePattern {
	protected UnresolvedType type;

	public static final Map primitiveTypesMap;
	public static final Map boxedPrimitivesMap;
	private static final Map boxedTypesMap;
	
	static {
		primitiveTypesMap = new HashMap();
		primitiveTypesMap.put("int",int.class);
		primitiveTypesMap.put("short",short.class);
		primitiveTypesMap.put("long",long.class);
		primitiveTypesMap.put("byte",byte.class);
		primitiveTypesMap.put("char",char.class);
		primitiveTypesMap.put("float",float.class);
		primitiveTypesMap.put("double",double.class);

		boxedPrimitivesMap = new HashMap();
		boxedPrimitivesMap.put("java.lang.Integer",Integer.class);
		boxedPrimitivesMap.put("java.lang.Short",Short.class);
		boxedPrimitivesMap.put("java.lang.Long",Long.class);
		boxedPrimitivesMap.put("java.lang.Byte",Byte.class);
		boxedPrimitivesMap.put("java.lang.Character",Character.class);
		boxedPrimitivesMap.put("java.lang.Float",Float.class);
		boxedPrimitivesMap.put("java.lang.Double",Double.class);

		
		boxedTypesMap = new HashMap();
		boxedTypesMap.put("int",Integer.class);
		boxedTypesMap.put("short",Short.class);
		boxedTypesMap.put("long",Long.class);
		boxedTypesMap.put("byte",Byte.class);
		boxedTypesMap.put("char",Character.class);
		boxedTypesMap.put("float",Float.class);
		boxedTypesMap.put("double",Double.class);

	}
	
	public ExactTypePattern(UnresolvedType type, boolean includeSubtypes,boolean isVarArgs) {
		super(includeSubtypes,isVarArgs);
		this.type = type;
	}
	
	public boolean isArray() { 
		return type.isArray();
	}
	
	/* (non-Javadoc)
	 * @see org.aspectj.weaver.patterns.TypePattern#couldEverMatchSameTypesAs(org.aspectj.weaver.patterns.TypePattern)
	 */
	protected boolean couldEverMatchSameTypesAs(TypePattern other) {
		if (super.couldEverMatchSameTypesAs(other)) return true;
		// false is necessary but not sufficient
		UnresolvedType otherType = other.getExactType();
		if (otherType != ResolvedType.MISSING) {
			return type.equals(otherType);
		} 
		if (other instanceof WildTypePattern) {
			WildTypePattern owtp = (WildTypePattern) other;
			String yourSimpleNamePrefix = owtp.getNamePatterns()[0].maybeGetSimpleName();
			if (yourSimpleNamePrefix != null) {
				return (type.getName().startsWith(yourSimpleNamePrefix));
			}
		}
		return true;
	}
	
	protected boolean matchesExactly(ResolvedType matchType) {
		boolean typeMatch = this.type.equals(matchType);
		if (!typeMatch && (matchType.isParameterizedType() || matchType.isGenericType())) {
			typeMatch = this.type.equals(matchType.getRawType());
		}
		if (!typeMatch && matchType.isTypeVariableReference()) {
			typeMatch = matchesTypeVariable((TypeVariableReferenceType)matchType);
		}
		annotationPattern.resolve(matchType.getWorld());
		boolean annMatch = this.annotationPattern.matches(matchType).alwaysTrue();
		return (typeMatch && annMatch);
	}
	
	private boolean matchesTypeVariable(TypeVariableReferenceType matchType) {
		return false;
	}
	
	protected boolean matchesExactly(ResolvedType matchType, ResolvedType annotatedType) {
		boolean typeMatch = this.type.equals(matchType);
		if (!typeMatch && (matchType.isParameterizedType() || matchType.isGenericType())) {
			typeMatch = this.type.equals(matchType.getRawType());
		}
		if (!typeMatch && matchType.isTypeVariableReference()) {
			typeMatch = matchesTypeVariable((TypeVariableReferenceType)matchType);
		}
		annotationPattern.resolve(matchType.getWorld());
		boolean annMatch = this.annotationPattern.matches(annotatedType).alwaysTrue();
		return (typeMatch && annMatch);		
	}
	
	public UnresolvedType getType() { return type; }

	// true if (matchType instanceof this.type)
	public FuzzyBoolean matchesInstanceof(ResolvedType matchType) {
		// in our world, Object is assignable from anything
		if (type.equals(ResolvedType.OBJECT)) 
		    return FuzzyBoolean.YES.and(annotationPattern.matches(matchType));
		
		if (type.resolve(matchType.getWorld()).isAssignableFrom(matchType)) {
			return FuzzyBoolean.YES.and(annotationPattern.matches(matchType));
		}
		
		// fix for PR 64262 - shouldn't try to coerce primitives
		if (type.isPrimitiveType()) {
			return FuzzyBoolean.NO;
		} else {
		    return matchType.isCoerceableFrom(type.resolve(matchType.getWorld())) ? FuzzyBoolean.MAYBE : FuzzyBoolean.NO;
		}
	}
		
    public boolean equals(Object other) {
    	if (!(other instanceof ExactTypePattern)) return false;
    	if (other instanceof BindingTypePattern) return false;
    	ExactTypePattern o = (ExactTypePattern)other;
    	if (includeSubtypes != o.includeSubtypes) return false;
    	if (isVarArgs != o.isVarArgs) return false;   	
    	if (!typeParameters.equals(o.typeParameters)) return false;
    	return (o.type.equals(this.type) && o.annotationPattern.equals(this.annotationPattern));
    }
    
    public int hashCode() {
        int result = 17;
        result = 37*result + type.hashCode();
        result = 37*result + new Boolean(includeSubtypes).hashCode();
        result = 37*result + new Boolean(isVarArgs).hashCode();
        result = 37*result + typeParameters.hashCode();
        result = 37*result + annotationPattern.hashCode();
        return result;
    }

    private static final byte EXACT_VERSION = 1; // rev if changed
	public void write(DataOutputStream out) throws IOException {
		out.writeByte(TypePattern.EXACT);
		out.writeByte(EXACT_VERSION);
		type.write(out);
		out.writeBoolean(includeSubtypes);
		out.writeBoolean(isVarArgs);
		annotationPattern.write(out);
		typeParameters.write(out);
		writeLocation(out);
	}
	
	public static TypePattern read(VersionedDataInputStream s, ISourceContext context) throws IOException {
		if (s.getMajorVersion()>=AjAttribute.WeaverVersionInfo.WEAVER_VERSION_MAJOR_AJ150) {
			return readTypePattern150(s,context);
		} else {
			return readTypePatternOldStyle(s,context);
	    }
    }
	
	public static TypePattern readTypePattern150(VersionedDataInputStream s, ISourceContext context) throws IOException {
		byte version = s.readByte();
		if (version > EXACT_VERSION) throw new BCException("ExactTypePattern was written by a more recent version of AspectJ");
		TypePattern ret = new ExactTypePattern(UnresolvedType.read(s), s.readBoolean(), s.readBoolean());
		ret.setAnnotationTypePattern(AnnotationTypePattern.read(s,context));
		ret.setTypeParameters(TypePatternList.read(s,context));
		ret.readLocation(context, s);
		return ret;
	}

	public static TypePattern readTypePatternOldStyle(DataInputStream s, ISourceContext context) throws IOException {
		TypePattern ret = new ExactTypePattern(UnresolvedType.read(s), s.readBoolean(),false);
		ret.readLocation(context, s);
		return ret;
	}

	public String toString() {
		StringBuffer buff = new StringBuffer();
		if (annotationPattern != AnnotationTypePattern.ANY) {
			buff.append('(');
			buff.append(annotationPattern.toString());
			buff.append(' ');
		}
		String typeString = type.toString();
		if (isVarArgs) typeString = typeString.substring(0,typeString.lastIndexOf('['));
		buff.append(typeString);
    	if (includeSubtypes) buff.append('+');
    	if (isVarArgs) buff.append("...");
		if (annotationPattern != AnnotationTypePattern.ANY) {
			buff.append(')');
		}
		return buff.toString();
    }
	public TypePattern resolveBindings(IScope scope, Bindings bindings, 
    								boolean allowBinding, boolean requireExactType)
    { 
		throw new BCException("trying to re-resolve");
		
	}
	
	/**
	 * return a version of this type pattern with all type variables references replaced
	 * by the corresponding entry in the map.
	 */
	public TypePattern parameterizeWith(Map typeVariableMap) {
		UnresolvedType newType = type;
		if (type.isTypeVariableReference()) {
			TypeVariableReference t = (TypeVariableReference) type;
			String key = t.getTypeVariable().getName();
			if (typeVariableMap.containsKey(key)) {
				newType = (UnresolvedType) typeVariableMap.get(key);
			}
		} else if (type.isParameterizedType()) {
			newType = type.parameterize(typeVariableMap);
		}
		ExactTypePattern ret = new ExactTypePattern(newType,includeSubtypes,isVarArgs);
		ret.annotationPattern = annotationPattern.parameterizeWith(typeVariableMap);
		ret.copyLocationFrom(this);
		return ret;
	}
	
    public Object accept(PatternNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/783.java