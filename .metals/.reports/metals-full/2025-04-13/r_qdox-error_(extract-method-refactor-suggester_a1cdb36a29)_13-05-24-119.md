error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15541.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15541.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15541.java
text:
```scala
a@@s = AtAjAttributes.readAj5MethodAttributes(method, this, world.resolve(getDeclaringType()), preResolvedPointcut,getSourceContext(world), world.getMessageHandler());

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


package org.aspectj.weaver.bcel;

import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.List;

import org.aspectj.apache.bcel.classfile.ExceptionTable;
import org.aspectj.apache.bcel.classfile.LocalVariable;
import org.aspectj.apache.bcel.classfile.LocalVariableTable;
import org.aspectj.apache.bcel.classfile.Method;
import org.aspectj.apache.bcel.classfile.annotation.Annotation;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.bridge.SourceLocation;
import org.aspectj.weaver.AjAttribute;
import org.aspectj.weaver.AnnotationX;
import org.aspectj.weaver.BCException;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.ResolvedMember;
import org.aspectj.weaver.ResolvedPointcutDefinition;
import org.aspectj.weaver.ResolvedTypeX;
import org.aspectj.weaver.ShadowMunger;
import org.aspectj.weaver.TypeX;
import org.aspectj.weaver.World;

final class BcelMethod extends ResolvedMember {

	private Method method;
	private boolean isAjSynthetic;
	private ShadowMunger associatedShadowMunger;
	private ResolvedPointcutDefinition preResolvedPointcut;  // used when ajc has pre-resolved the pointcut of some @Advice
	
    private ResolvedTypeX[] annotationTypes = null;
    private AnnotationX[] annotations = null;
	
	private AjAttribute.EffectiveSignatureAttribute effectiveSignature;
	private AjAttribute.MethodDeclarationLineNumberAttribute declarationLineNumber;
	private ResolvedTypeX[] resolvedAnnotations;
	private World world;

	BcelMethod(BcelObjectType declaringType, Method method) {
		super(
			method.getName().equals("<init>") ? CONSTRUCTOR : 
				(method.getName().equals("<clinit>") ? STATIC_INITIALIZATION : METHOD), 
			declaringType.getResolvedTypeX(),
			declaringType.isInterface() 
				? method.getAccessFlags() | Modifier.INTERFACE
				: method.getAccessFlags(),
			method.getName(), 
			method.getSignature());
		this.method = method;
		this.sourceContext = declaringType.getResolvedTypeX().getSourceContext();
		this.world = declaringType.getResolvedTypeX().getWorld();
		unpackAjAttributes(world);
		unpackJavaAttributes();
	}

	// ----

	private void unpackJavaAttributes() {
		ExceptionTable exnTable = method.getExceptionTable();
		checkedExceptions = (exnTable == null) 
			? TypeX.NONE
			: TypeX.forNames(exnTable.getExceptionNames());
			
		LocalVariableTable varTable = method.getLocalVariableTable();
		int len = getArity();
		if (varTable == null) {
			this.parameterNames = Utility.makeArgNames(len);
		} else {
			TypeX[] paramTypes = getParameterTypes();
			String[] paramNames = new String[len];
			int index = isStatic() ? 0 : 1;
			for (int i = 0; i < len; i++) {
				LocalVariable lv = varTable.getLocalVariable(index);
				if (lv == null) {
					paramNames[i] = "arg" + i;
				} else {
					paramNames[i] = lv.getName();
				}
				index += paramTypes[i].getSize();
			}
			this.parameterNames = paramNames;
		}
	}

	private void unpackAjAttributes(World world) {
		associatedShadowMunger = null;
        List as = BcelAttributes.readAjAttributes(getDeclaringType().getClassName(),method.getAttributes(), getSourceContext(world),world.getMessageHandler());
		processAttributes(world, as);
		as = AtAjAttributes.readAj5MethodAttributes(method, world.resolve(getDeclaringType()), preResolvedPointcut,getSourceContext(world), world.getMessageHandler());
		processAttributes(world,as);
	}

	private void processAttributes(World world, List as) {
		for (Iterator iter = as.iterator(); iter.hasNext();) {
			AjAttribute a = (AjAttribute) iter.next();
			if (a instanceof AjAttribute.MethodDeclarationLineNumberAttribute) {
				declarationLineNumber = (AjAttribute.MethodDeclarationLineNumberAttribute)a;
			} else if (a instanceof AjAttribute.AdviceAttribute) {
				associatedShadowMunger = ((AjAttribute.AdviceAttribute)a).reify(this, world);
				// return;
			} else if (a instanceof AjAttribute.AjSynthetic) {
				isAjSynthetic = true;
			} else if (a instanceof AjAttribute.EffectiveSignatureAttribute) {
				//System.out.println("found effective: " + this);
				effectiveSignature = (AjAttribute.EffectiveSignatureAttribute)a;
			} else if (a instanceof AjAttribute.PointcutDeclarationAttribute) {
				// this is an @AspectJ annotated advice method, with pointcut pre-resolved by ajc
				preResolvedPointcut = ((AjAttribute.PointcutDeclarationAttribute)a).reify();
			} else {
				throw new BCException("weird method attribute " + a);
			}
		}
	}

	public boolean isAjSynthetic() {
		return isAjSynthetic; // || getName().startsWith(NameMangler.PREFIX);
	}
	
	//FIXME ??? needs an isSynthetic method
	
	public ShadowMunger getAssociatedShadowMunger() {
		return associatedShadowMunger;
	}
	
	public AjAttribute.EffectiveSignatureAttribute getEffectiveSignature() {
		return effectiveSignature;
	}
	
	public boolean hasDeclarationLineNumberInfo() {
		return declarationLineNumber != null;
	}
	
	public int getDeclarationLineNumber() {
		if (declarationLineNumber != null) {
			return declarationLineNumber.getLineNumber();
		} else {
			return -1;
		}
	}
    
    public ISourceLocation getSourceLocation() {
      ISourceLocation ret = super.getSourceLocation(); 
      if ((ret == null || ret.getLine()==0) && hasDeclarationLineNumberInfo()) {
        // lets see if we can do better
        ISourceContext isc = getSourceContext();
        if (isc !=null) ret = isc.makeSourceLocation(getDeclarationLineNumber());
        else            ret = new SourceLocation(null,getDeclarationLineNumber());
      }
      return ret;
    }
	
	public Kind getKind() {
		if (associatedShadowMunger != null) {
			return ADVICE;
		} else {
			return super.getKind();
		}
	}
	
	public boolean hasAnnotation(TypeX ofType) {
		ensureAnnotationTypesRetrieved();
		for (int i=0; i<annotationTypes.length; i++) {
			ResolvedTypeX aType = annotationTypes[i];
			if (aType.equals(ofType)) return true;
		}
		return false;
	}
	
	public AnnotationX[] getAnnotations() {
		ensureAnnotationTypesRetrieved();
		return annotations;
	}
	
	 public ResolvedTypeX[] getAnnotationTypes() {
	    ensureAnnotationTypesRetrieved();
	    return annotationTypes;
     }
	 
	 public void addAnnotation(AnnotationX annotation) {
	    ensureAnnotationTypesRetrieved();	
		// Add it to the set of annotations
		int len = annotations.length;
		AnnotationX[] ret = new AnnotationX[len+1];
		System.arraycopy(annotations, 0, ret, 0, len);
		ret[len] = annotation;
		annotations = ret;
		
		// Add it to the set of annotation types
		len = annotationTypes.length;
		ResolvedTypeX[] ret2 = new ResolvedTypeX[len+1];
		System.arraycopy(annotationTypes,0,ret2,0,len);
		ret2[len] =world.resolve(TypeX.forName(annotation.getTypeName()));
		annotationTypes = ret2;
		// FIXME asc looks like we are managing two 'bunches' of annotations, one
		// here and one in the real 'method' - should we reduce it to one layer?
		method.addAnnotation(annotation.getBcelAnnotation());
	 }
	 
	 private void ensureAnnotationTypesRetrieved() {
		if (annotationTypes == null || method.getAnnotations().length!=annotations.length) { // sometimes the list changes underneath us!
    		Annotation annos[] = method.getAnnotations();
    		annotationTypes = new ResolvedTypeX[annos.length];
    		annotations = new AnnotationX[annos.length];
    		for (int i = 0; i < annos.length; i++) {
				Annotation annotation = annos[i];
				ResolvedTypeX rtx = world.resolve(TypeX.forName(annotation.getTypeName()));
				annotationTypes[i] = rtx;
				annotations[i] = new AnnotationX(annotation,world);
			}
    	}
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15541.java