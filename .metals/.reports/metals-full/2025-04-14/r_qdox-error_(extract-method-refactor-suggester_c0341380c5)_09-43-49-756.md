error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/787.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/787.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/787.java
text:
```scala
i@@f (!ResolvedType.isMissing(t)) ret.add(t);

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

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.IntMap;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.VersionedDataInputStream;

public class TypePatternList extends PatternNode {
	private TypePattern[] typePatterns;
	int ellipsisCount = 0;
	
	public static final TypePatternList EMPTY =
		new TypePatternList(new TypePattern[] {});
	
	public static final TypePatternList ANY =
	    new TypePatternList(new TypePattern[] {new EllipsisTypePattern()});  //can't use TypePattern.ELLIPSIS because of circular static dependency that introduces
	
	public TypePatternList() {
		typePatterns = new TypePattern[0];
		ellipsisCount = 0;
	}

	public TypePatternList(TypePattern[] arguments) {
		this.typePatterns = arguments;
		for (int i=0; i<arguments.length; i++) {
			if (arguments[i] == TypePattern.ELLIPSIS) ellipsisCount++;
		}
	}
	
	public TypePatternList(List l) {
		this((TypePattern[]) l.toArray(new TypePattern[l.size()]));
	}
	
	public int size() { return typePatterns.length; }
	
	public TypePattern get(int index) {
		return typePatterns[index];
	}

    public String toString() {
    	StringBuffer buf = new StringBuffer();
    	buf.append("(");
    	for (int i=0, len=typePatterns.length; i < len; i++) {
    		TypePattern type = typePatterns[i];
    		if (i > 0) buf.append(", ");
    		if (type == TypePattern.ELLIPSIS) {
    			buf.append("..");
    		} else {
    			buf.append(type.toString());
    		}
    	}
    	buf.append(")");
    	return buf.toString();
    }
       
    /*
     * return true iff this pattern could ever match a signature with the
     * given number of parameters
     */
    public boolean canMatchSignatureWithNParameters(int numParams) {
    	if (ellipsisCount == 0) {
    		return numParams == size();
    	} else {
    		return (size() -ellipsisCount) <= numParams;
    	}
    }
    
    //XXX shares much code with WildTypePattern and with NamePattern
    /**
     * When called with TypePattern.STATIC this will always return either
     * FuzzyBoolean.YES or FuzzyBoolean.NO.
     * 
     * When called with TypePattern.DYNAMIC this could return MAYBE if
     * at runtime it would be possible for arguments of the given static
     * types to dynamically match this, but it is not known for certain.
     * 
     * This method will never return FuzzyBoolean.NEVER
     */ 
    public FuzzyBoolean matches(ResolvedType[] types, TypePattern.MatchKind kind) {
    	int nameLength = types.length;
		int patternLength = typePatterns.length;
		
		int nameIndex = 0;
		int patternIndex = 0;
		
		if (ellipsisCount == 0) {
			if (nameLength != patternLength) return FuzzyBoolean.NO;
			FuzzyBoolean finalReturn = FuzzyBoolean.YES;
			while (patternIndex < patternLength) {
				FuzzyBoolean ret = typePatterns[patternIndex++].matches(types[nameIndex++], kind);
				if (ret == FuzzyBoolean.NO) return ret;
				if (ret == FuzzyBoolean.MAYBE) finalReturn = ret;
			}
			return finalReturn;
		} else if (ellipsisCount == 1) {
			if (nameLength < patternLength-1) return FuzzyBoolean.NO;
			FuzzyBoolean finalReturn = FuzzyBoolean.YES;
			while (patternIndex < patternLength) {
				TypePattern p = typePatterns[patternIndex++];
				if (p == TypePattern.ELLIPSIS) {
					nameIndex = nameLength - (patternLength-patternIndex);
				} else {
					FuzzyBoolean ret = p.matches(types[nameIndex++], kind);
				    if (ret == FuzzyBoolean.NO) return ret;
				    if (ret == FuzzyBoolean.MAYBE) finalReturn = ret;
				}
			}
			return finalReturn;
		} else {
//            System.err.print("match(" + arguments + ", " + types + ") -> ");
            FuzzyBoolean b =  outOfStar(typePatterns, types, 0, 0, patternLength - ellipsisCount, nameLength, ellipsisCount, kind);
//            System.err.println(b);
            return b;
    	}
    }
    
    private static FuzzyBoolean outOfStar(final TypePattern[] pattern, final ResolvedType[] target, 
                                                  int           pi,            int    ti, 
                                                  int           pLeft,         int    tLeft,
                                           final int           starsLeft, TypePattern.MatchKind kind) {
        if (pLeft > tLeft) return FuzzyBoolean.NO;
        FuzzyBoolean finalReturn = FuzzyBoolean.YES;
        while (true) {
            // invariant: if (tLeft > 0) then (ti < target.length && pi < pattern.length) 
            if (tLeft == 0) return finalReturn;
            if (pLeft == 0) {
                if (starsLeft > 0) {
                    return finalReturn;
                } else {
                    return FuzzyBoolean.NO;
                }
            }
            if (pattern[pi] == TypePattern.ELLIPSIS) {
                return inStar(pattern, target, pi+1, ti, pLeft, tLeft, starsLeft-1, kind);
            }
            FuzzyBoolean ret = pattern[pi].matches(target[ti], kind);
            if (ret == FuzzyBoolean.NO) return ret;
            if (ret == FuzzyBoolean.MAYBE) finalReturn = ret;
            pi++; ti++; pLeft--; tLeft--;
        }
    }    
    private static FuzzyBoolean inStar(final TypePattern[] pattern, final ResolvedType[] target, 
                                               int           pi,             int    ti, 
                                         final int           pLeft,         int    tLeft,
                                               int    starsLeft,     TypePattern.MatchKind kind) {
        // invariant: pLeft > 0, so we know we'll run out of stars and find a real char in pattern
        TypePattern patternChar = pattern[pi];
        while (patternChar == TypePattern.ELLIPSIS) {
            starsLeft--;
            patternChar = pattern[++pi];
        }
        while (true) {
            // invariant: if (tLeft > 0) then (ti < target.length)
            if (pLeft > tLeft) return FuzzyBoolean.NO;
            FuzzyBoolean ff = patternChar.matches(target[ti], kind);
            if (ff.maybeTrue()) {
                FuzzyBoolean xx = outOfStar(pattern, target, pi+1, ti+1, pLeft-1, tLeft-1, starsLeft, kind);
                if (xx.maybeTrue()) return ff.and(xx);
            } 
            ti++; tLeft--;
        }
    }
      
    /**
     * Return a version of this type pattern list in which all type variable references
     * are replaced by their corresponding entry in the map
     * @param typeVariableMap
     * @return
     */
    public TypePatternList parameterizeWith(Map typeVariableMap) {
    	TypePattern[] parameterizedPatterns = new TypePattern[typePatterns.length];
    	for (int i = 0; i < parameterizedPatterns.length; i++) {
			parameterizedPatterns[i] = typePatterns[i].parameterizeWith(typeVariableMap);
		}
    	return new TypePatternList(parameterizedPatterns);
    }
    
	public TypePatternList resolveBindings(IScope scope, Bindings bindings, boolean allowBinding, boolean requireExactType) {
		for (int i=0; i<typePatterns.length; i++) {
			TypePattern p = typePatterns[i];
			if (p != null) {
				typePatterns[i] = typePatterns[i].resolveBindings(scope, bindings, allowBinding, requireExactType);
			}
		}
		return this;
	}
	
	public TypePatternList resolveReferences(IntMap bindings) {
		int len = typePatterns.length;
		TypePattern[] ret = new TypePattern[len];
		for (int i=0; i < len; i++) {
			ret[i] = typePatterns[i].remapAdviceFormals(bindings);
		}
		return new TypePatternList(ret);
	}

	public void postRead(ResolvedType enclosingType) {
		for (int i=0; i<typePatterns.length; i++) {
			TypePattern p = typePatterns[i];
			p.postRead(enclosingType);
		}
	}
	

	public boolean equals(Object other) {
		if (!(other instanceof TypePatternList)) return false;
		TypePatternList o = (TypePatternList)other;
		int len = o.typePatterns.length;
		if (len != this.typePatterns.length) return false;
		for (int i=0; i<len; i++) {
			if (!this.typePatterns[i].equals(o.typePatterns[i])) return false;
		}
		return true;
	}
    public int hashCode() {
        int result = 41;
        for (int i = 0, len = typePatterns.length; i < len; i++) {
            result = 37*result + typePatterns[i].hashCode();
        }
        return result;
    }
    

	public static TypePatternList read(VersionedDataInputStream s, ISourceContext context) throws IOException  {
		short len = s.readShort();
		TypePattern[] arguments = new TypePattern[len];
		for (int i=0; i<len; i++) {
			arguments[i] = TypePattern.read(s, context);
		}
		TypePatternList ret = new TypePatternList(arguments);
		ret.readLocation(context, s);
		return ret;
	}


	public void write(DataOutputStream s) throws IOException {
		s.writeShort(typePatterns.length);
		for (int i=0; i<typePatterns.length; i++) {
			typePatterns[i].write(s);
		}
		writeLocation(s);
	}
    public TypePattern[] getTypePatterns() {
        return typePatterns;
    }

	public Collection getExactTypes() {
		ArrayList ret = new ArrayList();
		for (int i=0; i<typePatterns.length; i++) {
			UnresolvedType t = typePatterns[i].getExactType();
			if (t != ResolvedType.MISSING) ret.add(t);
		}
		return ret;
	}

    public Object accept(PatternNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
	
	public Object traverse(PatternNodeVisitor visitor, Object data) {
		Object ret = accept(visitor,data);
		for (int i = 0; i < typePatterns.length; i++) {
			typePatterns[i].traverse(visitor,ret);
		}
		return ret;
	}

	public boolean areAllExactWithNoSubtypesAllowed() {
		for (int i = 0; i < typePatterns.length; i++) {
			TypePattern array_element = typePatterns[i];
			if (!(array_element instanceof ExactTypePattern)) {
				return false;
			} else {
				ExactTypePattern etp = (ExactTypePattern) array_element;
				if (etp.isIncludeSubtypes()) return false;
			}
		}
		return true;
	}

	public String[] maybeGetCleanNames() {
      String[] theParamNames = new String[typePatterns.length];
	  for (int i = 0; i < typePatterns.length; i++) {
		TypePattern string = typePatterns[i];
		if (!(string instanceof ExactTypePattern)) return null;
		theParamNames[i] = ((ExactTypePattern)string).getExactType().getName();
	  }
	  return theParamNames;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/787.java