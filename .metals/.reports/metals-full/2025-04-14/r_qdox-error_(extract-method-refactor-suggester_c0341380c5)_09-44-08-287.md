error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7434.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7434.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7434.java
text:
```scala
S@@tring yourSimpleNamePrefix = owtp.getNamePatterns()[0].maybeGetSimpleName();

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
import org.aspectj.weaver.ResolvedTypeX;
import org.aspectj.weaver.TypeX;
import org.aspectj.weaver.VersionedDataInputStream;

public class ExactTypePattern extends TypePattern {
	protected TypeX type;

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
	
	public ExactTypePattern(TypeX type, boolean includeSubtypes,boolean isVarArgs) {
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
		TypeX otherType = other.getExactType();
		if (otherType != ResolvedTypeX.MISSING) {
			return type.equals(otherType);
		} 
		if (other instanceof WildTypePattern) {
			WildTypePattern owtp = (WildTypePattern) other;
			String yourSimpleNamePrefix = owtp.namePatterns[0].maybeGetSimpleName();
			if (yourSimpleNamePrefix != null) {
				return (type.getName().startsWith(yourSimpleNamePrefix));
			}
		}
		return true;
	}
	
	protected boolean matchesExactly(ResolvedTypeX matchType) {
		boolean typeMatch = this.type.equals(matchType);
		annotationPattern.resolve(matchType.getWorld());
		boolean annMatch = this.annotationPattern.matches(matchType).alwaysTrue();
		return (typeMatch && annMatch);
	}
	
	protected boolean matchesExactly(ResolvedTypeX matchType, ResolvedTypeX annotatedType) {
		boolean typeMatch = this.type.equals(matchType);
		annotationPattern.resolve(matchType.getWorld());
		boolean annMatch = this.annotationPattern.matches(annotatedType).alwaysTrue();
		return (typeMatch && annMatch);		
	}
	
	public TypeX getType() { return type; }

	public FuzzyBoolean matchesInstanceof(ResolvedTypeX matchType) {
		// in our world, Object is assignable from anything
		if (type.equals(ResolvedTypeX.OBJECT)) 
		    return FuzzyBoolean.YES.and(annotationPattern.matches(matchType));
		
		if (type.isAssignableFrom(matchType, matchType.getWorld())) {
			return FuzzyBoolean.YES.and(annotationPattern.matches(matchType));
		}
		
		// fix for PR 64262 - shouldn't try to coerce primitives
		if (type.isPrimitive()) {
			return FuzzyBoolean.NO;
		} else {
		    return matchType.isCoerceableFrom(type) ? FuzzyBoolean.MAYBE : FuzzyBoolean.NO;
		}
	}
	
	public boolean matchesExactly(Class matchType) {
		try {
			Class toMatchAgainst = getClassFor(type.getName());
			return matchType == toMatchAgainst;
		} catch (ClassNotFoundException cnfEx) {
			return false;			
		}
	}
	
	public FuzzyBoolean matchesInstanceof(Class matchType) {
		if (matchType.equals(Object.class)) return FuzzyBoolean.YES;
		
		try {
			String typeName = type.getName();
			Class toMatchAgainst = getClassFor(typeName);
			FuzzyBoolean ret = FuzzyBoolean.fromBoolean(toMatchAgainst.isAssignableFrom(matchType));
			if (ret == FuzzyBoolean.NO) {
				if (boxedTypesMap.containsKey(typeName)) {
					// try again with 'boxed' alternative
					toMatchAgainst = (Class) boxedTypesMap.get(typeName);
					ret = FuzzyBoolean.fromBoolean(toMatchAgainst.isAssignableFrom(matchType));
				}
			}
			return ret;
		} catch (ClassNotFoundException cnfEx) {
			return FuzzyBoolean.NO;			
		}
	}
	
	/**
	 * Return YES if any subtype of the static type would match,
	 *        MAYBE if some subtypes could match
	 *        NO if there could never be a match
	 * @param staticType
	 * @return
	 */
	public FuzzyBoolean willMatchDynamically(Class staticType) {
		if (matchesExactly(staticType)) return FuzzyBoolean.YES;
		if (matchesInstanceof(staticType) == FuzzyBoolean.YES) return FuzzyBoolean.YES;
		
		try {
			String typeName = type.getName();
			Class toMatchAgainst = getClassFor(typeName);
			if (toMatchAgainst.isInterface()) return FuzzyBoolean.MAYBE;
			if (staticType.isAssignableFrom(toMatchAgainst)) return FuzzyBoolean.MAYBE;
			return FuzzyBoolean.NO;
		} catch (ClassNotFoundException cnfEx) {
			return FuzzyBoolean.NO;			
		}
	}
	
	private Class getClassFor(String typeName) throws ClassNotFoundException {
		Class ret = null;
		ret = (Class) primitiveTypesMap.get(typeName);
		if (ret == null) ret = Class.forName(typeName);
		return ret;
	}
	
    public boolean equals(Object other) {
    	if (!(other instanceof ExactTypePattern)) return false;
    	ExactTypePattern o = (ExactTypePattern)other;
    	if (includeSubtypes != o.includeSubtypes) return false;
    	if (isVarArgs != o.isVarArgs) return false;   	
    	return (o.type.equals(this.type) && o.annotationPattern.equals(this.annotationPattern));
    }
    
    public int hashCode() {
        int result = 17;
        result = 37*result + type.hashCode();
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
		TypePattern ret = new ExactTypePattern(TypeX.read(s), s.readBoolean(), s.readBoolean());
		ret.setAnnotationTypePattern(AnnotationTypePattern.read(s,context));
		ret.readLocation(context, s);
		return ret;
	}

	public static TypePattern readTypePatternOldStyle(DataInputStream s, ISourceContext context) throws IOException {
		TypePattern ret = new ExactTypePattern(TypeX.read(s), s.readBoolean(),false);
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
	
	public TypePattern resolveBindingsFromRTTI(boolean allowBinding, boolean requireExactType) {
		throw new IllegalStateException("trying to re-resolve");
	}

    public Object accept(PointcutVisitor visitor, Object data) {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7434.java