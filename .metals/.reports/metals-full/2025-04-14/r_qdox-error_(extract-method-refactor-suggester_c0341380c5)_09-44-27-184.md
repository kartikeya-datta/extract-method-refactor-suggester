error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17320.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17320.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17320.java
text:
```scala
D@@eclarePrecedence d = (DeclarePrecedence)i.next();

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

import java.util.*;

import org.aspectj.weaver.patterns.*;
import org.aspectj.weaver.patterns.Pointcut;
import org.aspectj.asm.StructureModel;
import org.aspectj.bridge.*;
import org.aspectj.bridge.IMessage.Kind;

public abstract class World {
	protected IMessageHandler messageHandler = IMessageHandler.SYSTEM_ERR;

    protected Map typeMap = new HashMap(); // Signature to ResolvedType
    
    protected CrosscuttingMembersSet crosscuttingMembersSet = new CrosscuttingMembersSet(this);
    
    protected StructureModel model = null;
    
    protected Lint lint = new Lint(this);
    
    protected boolean XnoInline;
	
    protected World() {
        super();
        typeMap.put("B", ResolvedTypeX.BYTE);
        typeMap.put("S", ResolvedTypeX.SHORT);
        typeMap.put("I", ResolvedTypeX.INT);
        typeMap.put("J", ResolvedTypeX.LONG);
        typeMap.put("F", ResolvedTypeX.FLOAT);
        typeMap.put("D", ResolvedTypeX.DOUBLE);
        typeMap.put("C", ResolvedTypeX.CHAR);
        typeMap.put("Z", ResolvedTypeX.BOOLEAN);
        typeMap.put("V", ResolvedTypeX.VOID);
    }

    public ResolvedTypeX[] resolve(TypeX[] types) {
        int len = types.length;
        ResolvedTypeX[] ret = new ResolvedTypeX[len];
        for (int i=0; i<len; i++) {
            ret[i] = resolve(types[i]);
        }
        return ret;
    }
    
    public ResolvedTypeX resolve(TypeX ty) {
    	return resolve(ty, false);
    }

    public ResolvedTypeX resolve(TypeX ty, boolean allowMissing) {
    	//System.out.println("resolve: " + ty + " world " + typeMap.keySet());
        String signature = ty.getSignature();
        ResolvedTypeX ret = (ResolvedTypeX)typeMap.get(signature);
        if (ret != null) return ret;
        
        if (ty.isArray()) {
            ret = new ResolvedTypeX.Array(signature, this, resolve(ty.getComponentType(), allowMissing));
        } else {
            ret = resolveObjectType(ty);
            if (!allowMissing && ret == ResolvedTypeX.MISSING) {
            	//Thread.currentThread().dumpStack();
                MessageUtil.error(messageHandler, "can't find type " + ty.getName());
                // + " on classpath " + classPath);
            }
        }
        //System.out.println("ret: " + ret);
        typeMap.put(signature, ret);
        return ret;
    }
    
    //XXX helper method might be bad
    public ResolvedTypeX resolve(String name) {
    	return resolve(TypeX.forName(name));
    }
    protected abstract ResolvedTypeX resolveObjectType(TypeX ty);

    protected final boolean isCoerceableFrom(TypeX type, TypeX other) {
        return resolve(type).isCoerceableFrom(other);
    }

    protected final boolean isAssignableFrom(TypeX type, TypeX other) {
        return resolve(type).isAssignableFrom(other);
    }

    public boolean needsNoConversionFrom(TypeX type, TypeX other) {
        return resolve(type).needsNoConversionFrom(other);
    }
    
    protected final boolean isInterface(TypeX type) {
        return resolve(type).isInterface();
    }

    protected final ResolvedTypeX getSuperclass(TypeX type) {
        return resolve(type).getSuperclass();
    }

    protected final TypeX[] getDeclaredInterfaces(TypeX type) {
        return resolve(type).getDeclaredInterfaces();
    }

    protected final int getModifiers(TypeX type) {
        return resolve(type).getModifiers();
    }

    protected final ResolvedMember[] getDeclaredFields(TypeX type) {
        return resolve(type).getDeclaredFields();
    }

    protected final ResolvedMember[] getDeclaredMethods(TypeX type) {
        return resolve(type).getDeclaredMethods();
    }

    protected final ResolvedMember[] getDeclaredPointcuts(TypeX type) {
        return resolve(type).getDeclaredPointcuts();
    }

    // ---- members


    // XXX should we worry about dealing with context and looking up access?
    public ResolvedMember resolve(Member member) {
        ResolvedTypeX declaring = member.getDeclaringType().resolve(this);
        ResolvedMember ret;
        if (member.getKind() == Member.FIELD) {
            ret = declaring.lookupField(member);
        } else {
            ret = declaring.lookupMethod(member);
        }
        
        if (ret != null) return ret;
        
        return declaring.lookupSyntheticMember(member);   
    }

    protected int getModifiers(Member member) {
    	ResolvedMember r = resolve(member);
    	if (r == null) throw new BCException("bad resolve of " + member);
        return r.getModifiers();
    }

    protected String[] getParameterNames(Member member) {
        return resolve(member).getParameterNames();
    }

    protected TypeX[] getExceptions(Member member) {
        return resolve(member).getExceptions();
    }

    // ---- pointcuts

    public ResolvedPointcutDefinition findPointcut(TypeX typeX, String name) {
        throw new RuntimeException("not implemented yet");
    }
    
    /**
     * Get the shadow mungers of this world.
     * 
     * @return a list of {@link IShadowMunger}s appropriate for this world.
     */      
    //public abstract List getShadowMungers();
    
    // ---- empty world
    
    public static final World EMPTY = new World() {
        public List getShadowMungers() { return Collections.EMPTY_LIST; }
        public ResolvedTypeX resolveObjectType(TypeX ty) {
            return ResolvedTypeX.MISSING;
        }
        public Advice concreteAdvice(AjAttribute.AdviceAttribute attribute, Pointcut p, Member m) {
            throw new RuntimeException("unimplemented");
        }
        public ConcreteTypeMunger concreteTypeMunger(ResolvedTypeMunger munger, ResolvedTypeX aspectType) {
            throw new RuntimeException("unimplemented");
        }        
    };
    
    
    public abstract Advice concreteAdvice(
    	AjAttribute.AdviceAttribute attribute,
    	Pointcut pointcut,
        Member signature);
        
    public final Advice concreteAdvice(
     	AdviceKind kind,
        Pointcut p,
        Member signature,
        int extraParameterFlags,
        IHasSourceLocation loc)
    {
    	AjAttribute.AdviceAttribute attribute = 
    		new AjAttribute.AdviceAttribute(kind, p, extraParameterFlags, loc.getStart(), loc.getEnd(), loc.getSourceContext());
		return concreteAdvice(attribute, p, signature);
    }
        
        

	public ConcreteTypeMunger makeCflowStackFieldAdder(ResolvedMember cflowField) {
		throw new RuntimeException("unimplemented");
	}

    public abstract ConcreteTypeMunger concreteTypeMunger(ResolvedTypeMunger munger, ResolvedTypeX aspectType);

	/**
	 * Nobody should hold onto a copy of this message handler, or setMessageHandler won't
	 * work right.
	 */
	public IMessageHandler getMessageHandler() {
		return messageHandler;
	}

	public void setMessageHandler(IMessageHandler messageHandler) {
		this.messageHandler = messageHandler;
	}
	
	public void showMessage(
		Kind kind,
		String message,
		ISourceLocation loc1,
		ISourceLocation loc2)
	{
		if (loc1 != null) {
			messageHandler.handleMessage(new Message(message, kind, null, loc1));
			if (loc2 != null) {
				messageHandler.handleMessage(new Message(message, kind, null, loc2));
			}
		} else {
			messageHandler.handleMessage(new Message(message, kind, null, loc2));
		}
	}


	

//	public void addDeclare(ResolvedTypeX onType, Declare declare, boolean forWeaving) {
//		// this is not extensible, oh well
//		if (declare instanceof DeclareErrorOrWarning) {
//			ShadowMunger m = new Checker((DeclareErrorOrWarning)declare);
//			onType.addShadowMunger(m);
//		} else if (declare instanceof DeclareDominates) {
//			declareDominates.add(declare);
//		} else if (declare instanceof DeclareParents) {
//			declareParents.add(declare);
//		} else if (declare instanceof DeclareSoft) {
//			DeclareSoft d = (DeclareSoft)declare;
//			declareSoft.add(d);
//			if (forWeaving) {
//				ShadowMunger m = Advice.makeSoftener(this, d.getPointcut().concretize(onType, 0), d.getException());
//				onType.addShadowMunger(m);
//			}
//		} else {
//			throw new RuntimeException("unimplemented");
//		}
//	}


	/**
	 * Same signature as org.aspectj.util.PartialOrder.PartialComparable.compareTo
	 */
	public int compareByDominates(ResolvedTypeX aspect1, ResolvedTypeX aspect2) {
		//System.out.println("dom compare: " + aspect1 + " with " + aspect2);
		//System.out.println(crosscuttingMembersSet.getDeclareDominates());
		
		//??? We probably want to cache this result.  This is order N where N is the
		//??? number of dominates declares in the whole system.
		//??? This method can be called a large number of times.
		int order = 0;
		for (Iterator i = crosscuttingMembersSet.getDeclareDominates().iterator(); i.hasNext(); ) {
			DeclareDominates d = (DeclareDominates)i.next();
			int thisOrder = d.compare(aspect1, aspect2);
			//System.out.println("comparing: " + thisOrder + ": " + d);
			if (thisOrder != 0) {
				if (order != 0 && order != thisOrder) {
					throw new BCException("conflicting dominates orders");
				} else {
					order = thisOrder;
				}
			}
		}
		
		
		return order; 
	}
	
	
	public int comparePrecedence(ResolvedTypeX aspect1, ResolvedTypeX aspect2) {
		//System.err.println("compare precedence " + aspect1 + ", " + aspect2);
		if (aspect1.equals(aspect2)) return 0;
		
		int ret = compareByDominates(aspect1, aspect2);
		if (ret != 0) return ret;
		
		if (aspect1.isAssignableFrom(aspect2)) return -1;
		else if (aspect2.isAssignableFrom(aspect1)) return +1;

		return 0;
	}


	
	
	public List getDeclareParents() {
		return crosscuttingMembersSet.getDeclareParents();
	}

	public List getDeclareSoft() {
		return crosscuttingMembersSet.getDeclareSofts();
	}

	public CrosscuttingMembersSet getCrosscuttingMembersSet() {
		return crosscuttingMembersSet;
	}

	public StructureModel getModel() {
		return model;
	}

	public void setModel(StructureModel model) {
		this.model = model;
	}

	public Lint getLint() {
		return lint;
	}

	public void setLint(Lint lint) {
		this.lint = lint;
	}

	public boolean isXnoInline() {
		return XnoInline;
	}

	public void setXnoInline(boolean xnoInline) {
		XnoInline = xnoInline;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17320.java