error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1142.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1142.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[16,1]

error in qdox parser
file content:
```java
offset: 597
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1142.java
text:
```scala
public class DeclareDeclaration extends MethodDeclaration {

/* *******************************************************************
 * Copyright (c) 2002 Palo Alto Research Center, Incorporated (PARC).
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     Xerox/PARC     initial implementation 
 * ******************************************************************/


p@@ackage org.aspectj.ajdt.internal.compiler.ast;

import org.aspectj.ajdt.internal.compiler.lookup.*;
import org.aspectj.ajdt.internal.compiler.lookup.EclipseScope;
import org.aspectj.weaver.*;
import org.aspectj.weaver.AjAttribute;
import org.aspectj.weaver.patterns.*;
import org.aspectj.weaver.patterns.Declare;
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.lookup.ClassScope;
import org.eclipse.jdt.internal.compiler.parser.Parser;

public class DeclareDeclaration extends MethodDeclaration implements IAjDeclaration {
	public Declare declare;

	/**
	 * Constructor for IntraTypeDeclaration.
	 */
	static int counter = 0; //XXX evil
	public DeclareDeclaration(CompilationResult result, Declare symbolicDeclare) {
		super(result);
		this.declare = symbolicDeclare;
		if (declare != null) {
			sourceStart = declare.getStart();
			sourceEnd = declare.getEnd();
		}
		//??? we might need to set parameters to be empty
		this.returnType = TypeReference.baseTypeReference(T_void, 0);
		this.selector = ("ajc$declare_"+counter++).toCharArray(); //??? performance
	}


	/**
	 * A pointcut declaration exists in a classfile only as an attibute on the
	 * class.  Unlike advice and inter-type declarations, it has no corresponding
	 * method.
	 */
	public void generateCode(ClassScope classScope, ClassFile classFile) {
		classFile.extraAttributes.add(new EclipseAttributeAdapter(new AjAttribute.DeclareAttribute(declare)));
		return;
	}

	public void parseStatements(
		Parser parser,
		CompilationUnitDeclaration unit) {
			// do nothing
	}
	
	public void resolveStatements(ClassScope upperScope) {
		// do nothing 
	}
	
//	public boolean finishResolveTypes(SourceTypeBinding sourceTypeBinding) {
//		// there's nothing for our super to resolve usefully
//		//if (!super.finishResolveTypes(sourceTypeBinding)) return false;
////		if (declare == null) return true;
////        
////        EclipseScope scope = new EclipseScope(new FormalBinding[0], this.scope);
////
////        declare.resolve(scope);
////        return true;
//	}

	
	public void build(ClassScope classScope, CrosscuttingMembers xcut) {
		if (declare == null) return;
        
        EclipseScope scope = new EclipseScope(new FormalBinding[0], classScope);

        declare.resolve(scope);
        xcut.addDeclare(declare);

		//EclipseWorld world = EclipseWorld.fromScopeLookupEnvironment(classScope);
		//XXX need to work out the eclipse side of all this state
//XXX		world.addDeclare(world.resolve(EclipseWorld.fromBinding(classScope.referenceContext.binding)),
//XXX						declare, false);




//		binding = makeMethodBinding(classScope);
//		world.addTypeMunger(new EclipseNewMethodTypeMunger(binding));
//		//??? what do we need to know
//		munger = new NewMethodTypeMunger(
//			EclipseWorld.makeResolvedMember(binding.introducedMethod),
//			EclipseWorld.makeResolvedMember(super.binding), null);
	}






    public String toString(int tab) {
    	if (declare == null) return tabString(tab) + "<declare>";
    	else return tabString(tab) + declare.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1142.java