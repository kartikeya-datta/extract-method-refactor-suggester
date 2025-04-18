error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13676.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13676.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13676.java
text:
```scala
M@@ember isAnnotationPresent = Member.method(TypeX.forName("java/lang/Class"),0,

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

import org.aspectj.apache.bcel.Constants;
import org.aspectj.apache.bcel.generic.InstructionFactory;
import org.aspectj.apache.bcel.generic.InstructionHandle;
import org.aspectj.apache.bcel.generic.InstructionList;
import org.aspectj.apache.bcel.generic.LDC_W;
import org.aspectj.apache.bcel.generic.ReferenceType;
import org.aspectj.apache.bcel.generic.Type;
import org.aspectj.weaver.BCException;
import org.aspectj.weaver.Member;
import org.aspectj.weaver.TypeX;
import org.aspectj.weaver.ast.And;
import org.aspectj.weaver.ast.Call;
import org.aspectj.weaver.ast.CallExpr;
import org.aspectj.weaver.ast.Expr;
import org.aspectj.weaver.ast.FieldGet;
import org.aspectj.weaver.ast.FieldGetCall;
import org.aspectj.weaver.ast.HasAnnotation;
import org.aspectj.weaver.ast.IExprVisitor;
import org.aspectj.weaver.ast.ITestVisitor;
import org.aspectj.weaver.ast.Instanceof;
import org.aspectj.weaver.ast.Literal;
import org.aspectj.weaver.ast.Not;
import org.aspectj.weaver.ast.Or;
import org.aspectj.weaver.ast.Test;
import org.aspectj.weaver.ast.Var;

// we generate right to left, btw.
public class BcelRenderer implements ITestVisitor, IExprVisitor {

    private InstructionList instructions;
    private InstructionFactory fact;
    private BcelWorld world;

    InstructionHandle sk, fk, next = null;

    private BcelRenderer(InstructionFactory fact, BcelWorld world) {
        super();
        this.fact = fact;
        this.world = world;
        this.instructions = new InstructionList();
    }

    // ---- renderers
    
    public static InstructionList renderExpr(
        InstructionFactory fact,
        BcelWorld world,
        Expr e) 
    {
        BcelRenderer renderer = new BcelRenderer(fact, world);
        e.accept(renderer);
        return renderer.instructions;
    }
    public static InstructionList renderExpr(
        InstructionFactory fact,
        BcelWorld world,
        Expr e,
        Type desiredType) 
    {
        BcelRenderer renderer = new BcelRenderer(fact, world);
        e.accept(renderer);
        InstructionList il = renderer.instructions;
        il.append(Utility.createConversion(fact, BcelWorld.makeBcelType(e.getType()), desiredType));
        return il;
    }

    public static InstructionList renderExprs(
        InstructionFactory fact,
        BcelWorld world,
        Expr[] es) 
    {
        BcelRenderer renderer = new BcelRenderer(fact, world);
        for (int i = es.length - 1; i >= 0; i--) {
            es[i].accept(renderer);
        }
        return renderer.instructions;
    }

    /*
     * Get the instructions representing this test.
     * 
     * @param e test to render
     * @param sk instructionHandle to jump to if our rendered check succeeds (typically start of advice)
     * @param fk instructionHandle to jump to if our rendered check fails (typically after end of advice)
     * @param next instructionHandle that will follow this generated code.  Passing in null will generate
     *             one unnecessary GOTO instruction.
     * 
     * @returns the instruction list representing this expression
     */
    public static InstructionList renderTest(
        InstructionFactory fact,
        BcelWorld world,
        Test e,
        InstructionHandle sk,
        InstructionHandle fk,
        InstructionHandle next) 
    {
        BcelRenderer renderer = new BcelRenderer(fact, world);
        renderer.recur(e, sk, fk, next);
        return renderer.instructions;
    }

    /*
     * Get the instructions representing this test.
     * 
     * @param e test to render
     * @param sk instructionHandle to jump to if our rendered check succeeds (typically start of advice)
     * @param fk instructionHandle to jump to if our rendered check fails (typically after end of advice)
     * 
     * @returns the instruction list representing this expression
     */
    public static InstructionList renderTest(
        InstructionFactory fact,
        BcelWorld world,
        Test e,
        InstructionHandle sk,
        InstructionHandle fk) 
    {
        return renderTest(fact, world, e, sk, fk, null);
    }

    // ---- recurrers

    private void recur(
        Test e,
        InstructionHandle sk,
        InstructionHandle fk,
        InstructionHandle next) 
    {
        this.sk = sk;
        this.fk = fk;
        this.next = next;
        e.accept(this);
    }

    // ---- test visitors

    public void visit(And e) {
        InstructionHandle savedFk = fk;
        recur(e.getRight(), sk, fk, next);
        InstructionHandle ning = instructions.getStart();
        recur(e.getLeft(), ning, savedFk, ning);
    }

    public void visit(Or e) {
        InstructionHandle savedSk = sk;
        recur(e.getRight(), sk, fk, next);
        recur(e.getLeft(), savedSk, instructions.getStart(), instructions.getStart());
    }

    public void visit(Not e) {
        recur(e.getBody(), fk, sk, next);
    }

    public void visit(Instanceof i) {
        instructions.insert(createJumpBasedOnBooleanOnStack());
        instructions.insert(
            Utility.createInstanceof(fact, (ReferenceType) BcelWorld.makeBcelType(i.getType())));
        i.getVar().accept(this);
    }

    public void visit(HasAnnotation hasAnnotation) {
        // in Java:
        // foo.class.isAnnotationPresent(annotationClass);
        // in bytecode:
        // load var onto the stack  (done for us later)
        // invokevirtual java/lang/Object.getClass:()Ljava/lang/Class
        // ldc_w annotationClass
        // invokevirtual java/lang/Class.isAnnotationPresent:(Ljava/lang/Class;)Z
        InstructionList il = new InstructionList();
        Member getClass = Member.method(TypeX.OBJECT, 0, "getClass", "()Ljava/lang/Class;");
        il.append(Utility.createInvoke(fact, world, getClass));
        // aload annotationClass
        int annClassIndex = fact.getConstantPool().addClass(hasAnnotation.getAnnotationType().getSignature());
        il.append(new LDC_W(annClassIndex));
        Member isAnnotationPresent = Member.method(TypeX.forName("Ljava/lang/Class"),0,
                "isAnnotationPresent","(Ljava/lang/Class;)Z");
        il.append(Utility.createInvoke(fact,world,isAnnotationPresent));
        il.append(createJumpBasedOnBooleanOnStack());
        instructions.insert(il);
        hasAnnotation.getVar().accept(this);
    }
    
	private InstructionList createJumpBasedOnBooleanOnStack() {
		InstructionList il = new InstructionList();
        if (sk == fk) {
            // don't bother generating if it doesn't matter
            if (sk != next) {
                il.insert(InstructionFactory.createBranchInstruction(Constants.GOTO, sk));
            }
            return il;
        }

        if (fk == next) {
            il.insert(InstructionFactory.createBranchInstruction(Constants.IFNE, sk));
        } else if (sk == next) {
            il.insert(InstructionFactory.createBranchInstruction(Constants.IFEQ, fk));
        } else {
            il.insert(InstructionFactory.createBranchInstruction(Constants.GOTO, sk));
            il.insert(InstructionFactory.createBranchInstruction(Constants.IFEQ, fk));
        }
        return il;		
	}


    public void visit(Literal literal) {
        if (literal == Literal.FALSE)
            throw new BCException("bad");
    }

	public void visit(Call call) {
		Member method = call.getMethod();
		// assert method.isStatic()
		Expr[] args = call.getArgs();
		//System.out.println("args: " + Arrays.asList(args));
		InstructionList callIl = new InstructionList();
		for (int i=0, len=args.length; i < len; i++) {
			//XXX only correct for static method calls
			Type desiredType = BcelWorld.makeBcelType(method.getParameterTypes()[i]);
			callIl.append(renderExpr(fact, world, args[i], desiredType));
		}
		//System.out.println("rendered args: " + callIl);
		callIl.append(Utility.createInvoke(fact, world, method));
		callIl.append(createJumpBasedOnBooleanOnStack());
		instructions.insert(callIl);		
	}

	public void visit(FieldGetCall fieldGetCall) {
		Member field = fieldGetCall.getField();
		Member method = fieldGetCall.getMethod();
		InstructionList il = new InstructionList();
		il.append(Utility.createGet(fact, field));
		// assert !method.isStatic()
		Expr[] args = fieldGetCall.getArgs();
		//System.out.println("args: " + Arrays.asList(args));
		il.append(renderExprs(fact, world, args));	
		//System.out.println("rendered args: " + callIl);
		il.append(Utility.createInvoke(fact, world, method));
		il.append(createJumpBasedOnBooleanOnStack());
		instructions.insert(il);	
	}

    // ---- expr visitors

    public void visit(Var var) {
        BcelVar bvar = (BcelVar) var;
        bvar.insertLoad(instructions, fact);
    }   
    
    public void visit(FieldGet fieldGet) {
		Member field = fieldGet.getField();
		// assert field.isStatic()
		instructions.insert(Utility.createGet(fact, field));		
    }
    
	public void visit(CallExpr call) {
		Member method = call.getMethod();
		// assert method.isStatic()
		Expr[] args = call.getArgs();
		InstructionList callIl = renderExprs(fact, world, args);	
		callIl.append(Utility.createInvoke(fact, world, method));
		instructions.insert(callIl);		
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13676.java