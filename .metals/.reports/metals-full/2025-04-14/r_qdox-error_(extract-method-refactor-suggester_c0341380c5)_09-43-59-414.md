error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10088.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10088.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10088.java
text:
```scala
i@@f (munger.getConcreteAspect()!=null && munger.getConcreteAspect().isAnnotationStyleAspect()) {

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
 *     Alexandre Vasseur    support for @AJ aspects
 * ******************************************************************/


package org.aspectj.weaver.bcel;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.aspectj.apache.bcel.Constants;
import org.aspectj.apache.bcel.classfile.Field;
import org.aspectj.apache.bcel.generic.ACONST_NULL;
import org.aspectj.apache.bcel.generic.ALOAD;
import org.aspectj.apache.bcel.generic.ANEWARRAY;
import org.aspectj.apache.bcel.generic.ArrayType;
import org.aspectj.apache.bcel.generic.BranchInstruction;
import org.aspectj.apache.bcel.generic.ConstantPoolGen;
import org.aspectj.apache.bcel.generic.DUP;
import org.aspectj.apache.bcel.generic.DUP_X1;
import org.aspectj.apache.bcel.generic.FieldInstruction;
import org.aspectj.apache.bcel.generic.INVOKEINTERFACE;
import org.aspectj.apache.bcel.generic.INVOKESPECIAL;
import org.aspectj.apache.bcel.generic.INVOKESTATIC;
import org.aspectj.apache.bcel.generic.Instruction;
import org.aspectj.apache.bcel.generic.InstructionConstants;
import org.aspectj.apache.bcel.generic.InstructionFactory;
import org.aspectj.apache.bcel.generic.InstructionHandle;
import org.aspectj.apache.bcel.generic.InstructionList;
import org.aspectj.apache.bcel.generic.InstructionTargeter;
import org.aspectj.apache.bcel.generic.InvokeInstruction;
import org.aspectj.apache.bcel.generic.LoadInstruction;
import org.aspectj.apache.bcel.generic.MULTIANEWARRAY;
import org.aspectj.apache.bcel.generic.NEW;
import org.aspectj.apache.bcel.generic.ObjectType;
import org.aspectj.apache.bcel.generic.PUSH;
import org.aspectj.apache.bcel.generic.ReturnInstruction;
import org.aspectj.apache.bcel.generic.SWAP;
import org.aspectj.apache.bcel.generic.StoreInstruction;
import org.aspectj.apache.bcel.generic.TargetLostException;
import org.aspectj.apache.bcel.generic.Type;
import org.aspectj.apache.bcel.generic.ReferenceType;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.bridge.Message;
import org.aspectj.weaver.Advice;
import org.aspectj.weaver.AdviceKind;
import org.aspectj.weaver.AjcMemberMaker;
import org.aspectj.weaver.BCException;
import org.aspectj.weaver.IntMap;
import org.aspectj.weaver.Member;
import org.aspectj.weaver.NameMangler;
import org.aspectj.weaver.NewConstructorTypeMunger;
import org.aspectj.weaver.NewFieldTypeMunger;
import org.aspectj.weaver.NewMethodTypeMunger;
import org.aspectj.weaver.ResolvedMember;
import org.aspectj.weaver.ResolvedTypeX;
import org.aspectj.weaver.Shadow;
import org.aspectj.weaver.ShadowMunger;
import org.aspectj.weaver.TypeX;
import org.aspectj.weaver.WeaverMessages;
import org.aspectj.weaver.World;
import org.aspectj.weaver.ast.Var;


/*
 * Some fun implementation stuff:
 * 
 *   * expressionKind advice is non-execution advice
 *     * may have a target. 
 *     * if the body is extracted, it will be extracted into 
 *       a static method.  The first argument to the static 
 *       method is the target
 *     * advice may expose a this object, but that's the advice's
 *       consideration, not ours.  This object will NOT be cached in another
 *       local, but will always come from frame zero.
 * 
 *   * non-expressionKind advice is execution advice
 *     * may have a this.
 *     * target is same as this, and is exposed that way to advice
 *       (i.e., target will not be cached, will always come from frame zero)
 *     * if the body is extracted, it will be extracted into a method
 *       with same static/dynamic modifier as enclosing method.  If non-static, 
 *       target of callback call will be this. 
 *
 *   * because of these two facts, the setup of the actual arguments (including 
 *     possible target) callback method is the same for both kinds of advice:
 *     push the targetVar, if it exists (it will not exist for advice on static
 *     things), then push all the argVars.  
 * 
 * Protected things:
 *
 *   * the above is sufficient for non-expressionKind advice for protected things,
 *     since the target will always be this.
 * 
 *   * For expressionKind things, we have to modify the signature of the callback
 *     method slightly.  For non-static expressionKind things, we modify
 *     the first argument of the callback method NOT to be the type specified
 *     by the method/field signature (the owner), but rather we type it to
 *     the currentlyEnclosing type. We are guaranteed this will be fine,
 *     since the verifier verifies that the target is a subtype of the currently
 *     enclosingType. 
 * 
 * Worries:
 * 
 *    * ConstructorCalls will be weirder than all of these, since they
 *      supposedly don't have a target (according to AspectJ), but they clearly
 *      do have a target of sorts, just one that needs to be pushed on the stack,
 *      dupped, and not touched otherwise until the constructor runs.
 * 
 * @author Jim Hugunin
 * @author Erik Hilsdale
 * 
 */

public class BcelShadow extends Shadow {
    	
    private ShadowRange range;    
    private final BcelWorld world;  
    private final LazyMethodGen enclosingMethod;
	private boolean fallsThrough;  //XXX not used anymore

	// ---- initialization
	
	/**
	 *  This generates an unassociated shadow, rooted in a particular method but not rooted
	 * to any particular point in the code.  It should be given to a rooted ShadowRange
	 * in the {@link ShadowRange#associateWithShadow(BcelShadow)} method.
	 */
	public BcelShadow(
		BcelWorld world,
		Kind kind,
		Member signature,
		LazyMethodGen enclosingMethod,
		BcelShadow enclosingShadow) 
	{
		super(kind, signature, enclosingShadow);
		this.world = world;
		this.enclosingMethod = enclosingMethod;
		fallsThrough = kind.argsOnStack();
	}

	// ---- copies all state, including Shadow's mungers...

	public BcelShadow copyInto(LazyMethodGen recipient, BcelShadow enclosing) {
		BcelShadow s = new BcelShadow(world, getKind(), getSignature(), recipient, enclosing);
		List src = mungers;
		List dest = s.mungers;
		
		for (Iterator i = src.iterator(); i.hasNext(); ) {
			dest.add(i.next());
		}
		return s;
	}

    // ---- overridden behaviour

	public World getIWorld() {
		return world;
	}



	private void deleteNewAndDup() {
		final ConstantPoolGen cpg = getEnclosingClass().getConstantPoolGen();
		int depth = 1;
		InstructionHandle ih = range.getStart();

		while (true) {
			Instruction inst = ih.getInstruction();
			if (inst instanceof INVOKESPECIAL
				&& ((INVOKESPECIAL) inst).getName(cpg).equals("<init>")) {
				depth++;
			} else if (inst instanceof NEW) {
				depth--;
				if (depth == 0) break;
			}
			ih = ih.getPrev();
		}
		// now IH points to the NEW.  We're followed by the DUP, and that is followed
		// by the actual instruciton we care about.  
		InstructionHandle newHandle = ih;
		InstructionHandle endHandle = newHandle.getNext();
		InstructionHandle nextHandle;
		if (endHandle.getInstruction() instanceof DUP) {
			nextHandle = endHandle.getNext();			
			retargetFrom(newHandle, nextHandle);
			retargetFrom(endHandle, nextHandle);
		} else if (endHandle.getInstruction() instanceof DUP_X1) {
			InstructionHandle dupHandle = endHandle;
			endHandle = endHandle.getNext();
			nextHandle = endHandle.getNext();
			if (endHandle.getInstruction() instanceof SWAP) {}
			else {
				// XXX see next XXX comment
				throw new RuntimeException("Unhandled kind of new " + endHandle);
			}
			retargetFrom(newHandle, nextHandle);
			retargetFrom(dupHandle, nextHandle);
			retargetFrom(endHandle, nextHandle);
		} else {
			endHandle = newHandle;
			nextHandle = endHandle.getNext();
			retargetFrom(newHandle, nextHandle);
			// add a POP here... we found a NEW w/o a dup or anything else, so
			// we must be in statement context.
			getRange().insert(InstructionConstants.POP, Range.OutsideAfter);
		}
		// assert (dupHandle.getInstruction() instanceof DUP);

		try {
			range.getBody().delete(newHandle, endHandle);
		} catch (TargetLostException e) {
			throw new BCException("shouldn't happen");
		}
	}
	private void retargetFrom(InstructionHandle old, InstructionHandle fresh) {
		InstructionTargeter[] sources = old.getTargeters();
		if (sources != null) {
			for (int i = sources.length - 1; i >= 0; i--) {
				sources[i].updateTarget(old, fresh);
			}
		}
	}
	
 	protected void prepareForMungers() {
		// if we're a constructor call, we need to remove the new:dup or the new:dup_x1:swap, 
		// and store all our
		// arguments on the frame.
 		
		// ??? This is a bit of a hack (for the Java langauge).  We do this because
		// we sometime add code "outsideBefore" when dealing with weaving join points.  We only
		// do this for exposing state that is on the stack.  It turns out to just work for 
		// everything except for constructor calls and exception handlers.  If we were to clean
		// this up, every ShadowRange would have three instructionHandle points, the start of 
		// the arg-setup code, the start of the running code, and the end of the running code.
		if (getKind() == ConstructorCall) {
			deleteNewAndDup();
			initializeArgVars();
		} else if (getKind() == PreInitialization) { // pr74952
			ShadowRange range = getRange();
			range.insert(InstructionConstants.NOP,Range.InsideAfter); 
		} else if (getKind() == ExceptionHandler) {
			
			ShadowRange range = getRange();
			InstructionList body = range.getBody();
			InstructionHandle start = range.getStart();		
			
			// Create a store instruction to put the value from the top of the 
			// stack into a local variable slot.  This is a trimmed version of
			// what is in initializeArgVars() (since there is only one argument
			// at a handler jp and only before advice is supported) (pr46298)
	        argVars = new BcelVar[1];
			int positionOffset = (hasTarget() ? 1 : 0) + ((hasThis() && !getKind().isTargetSameAsThis()) ? 1 : 0);
            TypeX tx = getArgType(0);
            argVars[0] = genTempVar(tx, "ajc$arg0");
            InstructionHandle insertedInstruction = 
            	range.insert(argVars[0].createStore(getFactory()), Range.OutsideBefore);

            // Now the exception range starts just after our new instruction.
            // The next bit of code changes the exception range to point at
            // the store instruction
			InstructionTargeter[] targeters = start.getTargeters();
			for (int i = 0; i < targeters.length; i++) {
				InstructionTargeter t = targeters[i];
				if (t instanceof ExceptionRange) {
					ExceptionRange er = (ExceptionRange) t;
					er.updateTarget(start, insertedInstruction, body);
				}
			}
		}

		// now we ask each munger to request our state
		isThisJoinPointLazy = world.isXlazyTjp();
		
		for (Iterator iter = mungers.iterator(); iter.hasNext();) {
			ShadowMunger munger = (ShadowMunger) iter.next();
			munger.specializeOn(this);
		}
		
		initializeThisJoinPoint();

	    // If we are an expression kind, we require our target/arguments on the stack
	    // before we do our actual thing.  However, they may have been removed
	    // from the stack as the shadowMungers have requested state.  
	    // if any of our shadowMungers requested either the arguments or target, 
	    // the munger will have added code
	    // to pop the target/arguments into temporary variables, represented by 
	    // targetVar and argVars.  In such a case, we must make sure to re-push the 
	    // values.  
	
	    // If we are nonExpressionKind, we don't expect arguments on the stack
	    // so this is moot.  If our argVars happen to be null, then we know that
	    // no ShadowMunger has squirrelled away our arguments, so they're still
	    // on the stack.		
		InstructionFactory fact = getFactory();
		if (getKind().argsOnStack() && argVars != null) {
			
			// Special case first (pr46298).  If we are an exception handler and the instruction
			// just after the shadow is a POP then we should remove the pop.  The code 
			// above which generated the store instruction has already cleared the stack.
			// We also don't generate any code for the arguments in this case as it would be
			// an incorrect aload.
			if (getKind() == ExceptionHandler 
				&& range.getEnd().getNext().getInstruction().equals(InstructionConstants.POP)) {
				// easier than deleting it ...
				range.getEnd().getNext().setInstruction(InstructionConstants.NOP);
			} else {
			  range.insert(
				BcelRenderer.renderExprs(fact, world, argVars),
				Range.InsideBefore);
			  if (targetVar != null) {
				range.insert(
					BcelRenderer.renderExpr(fact, world, targetVar),
					Range.InsideBefore);
			  }
			  if (getKind() == ConstructorCall) {
				range.insert((Instruction) InstructionFactory.createDup(1), Range.InsideBefore);
				range.insert(
					fact.createNew(
						(ObjectType) BcelWorld.makeBcelType(
							getSignature().getDeclaringType())),
					Range.InsideBefore);
			  }
			}
		}
 	}

    // ---- getters

    public ShadowRange getRange() {
        return range;
    }    
	public void setRange(ShadowRange range) {
		this.range = range;
	}
	
    public int getSourceLine() {
    	// if the kind of join point for which we are a shadow represents
    	// a method or constructor execution, then the best source line is
    	// the one from the enclosingMethod declarationLineNumber if available.
    	Kind kind = getKind();
    	if ( (kind == MethodExecution)  ||
    		 (kind == ConstructorExecution) ||
			 (kind == AdviceExecution) || 
			 (kind == StaticInitialization) ||
			 (kind == PreInitialization) ||
			 (kind == Initialization)) {
    		if (getEnclosingMethod().hasDeclaredLineNumberInfo()) {
    			return getEnclosingMethod().getDeclarationLineNumber();
    		}
    	}
    	
    	if (range == null) {
    		if (getEnclosingMethod().hasBody()) {
    			return Utility.getSourceLine(getEnclosingMethod().getBody().getStart());
    		} else {
    			return 0;
    		}
    	}
    	int ret = Utility.getSourceLine(range.getStart());
    	if (ret < 0) return 0;
    	return ret;
    }
    
    // overrides
    public TypeX getEnclosingType() {
    	return getEnclosingClass().getType();
    }

    public LazyClassGen getEnclosingClass() {
        return enclosingMethod.getEnclosingClass();
    }

    public BcelWorld getWorld() {
        return world;
    }
    
    // ---- factory methods
	
    public static BcelShadow makeConstructorExecution(
    	BcelWorld world,
    	LazyMethodGen enclosingMethod,
    	InstructionHandle justBeforeStart)
	{
		final InstructionList body = enclosingMethod.getBody();
		BcelShadow s = 
			new BcelShadow(
				world,
				ConstructorExecution,
				world.makeMethodSignature(enclosingMethod),
				enclosingMethod,
				null);
		ShadowRange r = new ShadowRange(body);
		r.associateWithShadow(s);
		r.associateWithTargets(
			Range.genStart(body, justBeforeStart.getNext()),
			Range.genEnd(body));
		return s;
	}

    public static BcelShadow makeStaticInitialization(
            BcelWorld world,
            LazyMethodGen enclosingMethod) 
    {
        InstructionList body = enclosingMethod.getBody();
        // move the start past ajc$preClinit
        InstructionHandle clinitStart = body.getStart();
        if (clinitStart.getInstruction() instanceof InvokeInstruction) {
        	InvokeInstruction ii = (InvokeInstruction)clinitStart.getInstruction();
			if (ii
				.getName(enclosingMethod.getEnclosingClass().getConstantPoolGen())
				.equals(NameMangler.AJC_PRE_CLINIT_NAME)) {
				clinitStart = clinitStart.getNext();
			}
        }
        
        InstructionHandle clinitEnd = body.getEnd();
        
        //XXX should move the end before the postClinit, but the return is then tricky...
//        if (clinitEnd.getInstruction() instanceof InvokeInstruction) {
//        	InvokeInstruction ii = (InvokeInstruction)clinitEnd.getInstruction();
//        	if (ii.getName(enclosingMethod.getEnclosingClass().getConstantPoolGen()).equals(NameMangler.AJC_POST_CLINIT_NAME)) {
//        		clinitEnd = clinitEnd.getPrev();
//        	}
//        }
        
        
        
        BcelShadow s =
            new BcelShadow(
                world,
                StaticInitialization,
                world.makeMethodSignature(enclosingMethod),
                enclosingMethod,
                null);
        ShadowRange r = new ShadowRange(body);
        r.associateWithShadow(s);
        r.associateWithTargets(
            Range.genStart(body, clinitStart),
            Range.genEnd(body, clinitEnd));
        return s;
    }
    
	/** Make the shadow for an exception handler.  Currently makes an empty shadow that
	 * only allows before advice to be woven into it.
	 */


	public static BcelShadow makeExceptionHandler(
		BcelWorld world,
		ExceptionRange exceptionRange,
		LazyMethodGen enclosingMethod,
		InstructionHandle startOfHandler,
		BcelShadow enclosingShadow) 
	{
		InstructionList body = enclosingMethod.getBody();
		TypeX catchType = exceptionRange.getCatchType();
		TypeX inType = enclosingMethod.getEnclosingClass().getType();
		
		ResolvedMember sig = Member.makeExceptionHandlerSignature(inType, catchType);
		
			
		sig.parameterNames = new String[] {findHandlerParamName(startOfHandler)};
        BcelShadow s =
            new BcelShadow(
                world,
                ExceptionHandler,
                sig,
                enclosingMethod,
                enclosingShadow);
        ShadowRange r = new ShadowRange(body);
        r.associateWithShadow(s);
        InstructionHandle start = Range.genStart(body, startOfHandler);
        InstructionHandle end = Range.genEnd(body, start);
        
        r.associateWithTargets(start, end);
        exceptionRange.updateTarget(startOfHandler, start, body);
        return s;
	}
	
	private static String findHandlerParamName(InstructionHandle startOfHandler) {		
		if (startOfHandler.getInstruction() instanceof StoreInstruction &&
			startOfHandler.getNext() != null)
		{
			int slot = ((StoreInstruction)startOfHandler.getInstruction()).getIndex();
			//System.out.println("got store: " + startOfHandler.getInstruction() + ", " + index);
			InstructionTargeter[] targeters = startOfHandler.getNext().getTargeters();
			if (targeters!=null) {
				for (int i=targeters.length-1; i >= 0; i--) {
					if (targeters[i] instanceof LocalVariableTag) {
						LocalVariableTag t = (LocalVariableTag)targeters[i];
						if (t.getSlot() == slot) {
							return t.getName();
						}
						//System.out.println("tag: " + targeters[i]);
					}
				}
 			}
		}
		
		return "<missing>";
	}
    
    /** create an init join point associated w/ an interface in the body of a constructor */
    
	public static BcelShadow makeIfaceInitialization(
		BcelWorld world,
		LazyMethodGen constructor,
		Member interfaceConstructorSignature) 
	{
		InstructionList body = constructor.getBody();
		// TypeX inType = constructor.getEnclosingClass().getType();
        BcelShadow s =
            new BcelShadow(
                world,
                Initialization,
                interfaceConstructorSignature,
                constructor,
                null);
        s.fallsThrough = true;
//        ShadowRange r = new ShadowRange(body);
//        r.associateWithShadow(s);
//        InstructionHandle start = Range.genStart(body, handle);
//        InstructionHandle end = Range.genEnd(body, handle);
//        
//        r.associateWithTargets(start, end);
        return s;			
	}
	
	public void initIfaceInitializer(InstructionHandle end) {
		final InstructionList body = enclosingMethod.getBody();
		ShadowRange r = new ShadowRange(body);
		r.associateWithShadow(this);
		InstructionHandle nop = body.insert(end, InstructionConstants.NOP);
		
		r.associateWithTargets(
			Range.genStart(body, nop),
			Range.genEnd(body, nop));
	}

//	public static BcelShadow makeIfaceConstructorExecution(
//		BcelWorld world,
//		LazyMethodGen constructor,
//		InstructionHandle next,
//		Member interfaceConstructorSignature) 
//	{
//		// final InstructionFactory fact = constructor.getEnclosingClass().getFactory();
//		InstructionList body = constructor.getBody();
//		// TypeX inType = constructor.getEnclosingClass().getType();
//        BcelShadow s =
//            new BcelShadow(
//                world,
//                ConstructorExecution,
//                interfaceConstructorSignature,
//                constructor,
//                null);
//        s.fallsThrough = true;
//        ShadowRange r = new ShadowRange(body);
//        r.associateWithShadow(s);
//		// ??? this may or may not work
//        InstructionHandle start = Range.genStart(body, next);
//        //InstructionHandle end = Range.genEnd(body, body.append(start, fact.NOP));
//        InstructionHandle end = Range.genStart(body, next);
//        //body.append(start, fact.NOP);
//        
//        r.associateWithTargets(start, end);
//        return s;			
//	}

    
	/** Create an initialization join point associated with a constructor, but not
		 * with any body of code yet.  If this is actually matched, it's range will be set
		 * when we inline self constructors.
		 * 
		 * @param constructor The constructor starting this initialization. 
		 */
	public static BcelShadow makeUnfinishedInitialization(
		BcelWorld world,
		LazyMethodGen constructor) 
	{
		return new BcelShadow(
			world,
			Initialization,
			world.makeMethodSignature(constructor),
			constructor,
			null);
	}

	public static BcelShadow makeUnfinishedPreinitialization(
		BcelWorld world,
		LazyMethodGen constructor) 
	{
		BcelShadow ret =  new BcelShadow(
			world,
			PreInitialization,
			world.makeMethodSignature(constructor),
			constructor,
			null);
		ret.fallsThrough = true;
		return ret;
	}
	
	
	public static BcelShadow makeMethodExecution(
			BcelWorld world,
			LazyMethodGen enclosingMethod,
			boolean lazyInit) 
	{
		if (!lazyInit) return makeMethodExecution(world, enclosingMethod);
		
		BcelShadow s =
			new BcelShadow(
				world,
				MethodExecution,
				enclosingMethod.getMemberView(),
				enclosingMethod,
				null);
		                
		return s;
	}
	
	
	public void init() {
		if (range != null) return;
		
		final InstructionList body = enclosingMethod.getBody();
		ShadowRange r = new ShadowRange(body);
		r.associateWithShadow(this);
		r.associateWithTargets(
			Range.genStart(body),
			Range.genEnd(body));
	}
	
    public static BcelShadow makeMethodExecution(
            BcelWorld world,
            LazyMethodGen enclosingMethod) 
    {
    	return makeShadowForMethod(world, enclosingMethod, MethodExecution,
    			enclosingMethod.getMemberView()); //world.makeMethodSignature(enclosingMethod));
    }	
    	
    	
    public static BcelShadow makeShadowForMethod(BcelWorld world,
            LazyMethodGen enclosingMethod, Shadow.Kind kind, Member sig)
    {
        final InstructionList body = enclosingMethod.getBody();
        BcelShadow s =
            new BcelShadow(
                world,
                kind,
                sig,
                enclosingMethod,
                null);
        ShadowRange r = new ShadowRange(body);
        r.associateWithShadow(s);
        r.associateWithTargets(
            Range.genStart(body),
            Range.genEnd(body));                  
        return s;
    }


    
    public static BcelShadow makeAdviceExecution(
        BcelWorld world,
        LazyMethodGen enclosingMethod) 
	{
		final InstructionList body = enclosingMethod.getBody();
		BcelShadow s =
			new BcelShadow(
				world,
				AdviceExecution,
				world.makeMethodSignature(enclosingMethod, Member.ADVICE),
				enclosingMethod,
				null);
		ShadowRange r = new ShadowRange(body);
		r.associateWithShadow(s);
		r.associateWithTargets(Range.genStart(body), Range.genEnd(body));
		return s;
	}


	// constructor call shadows are <em>initially</em> just around the 
	// call to the constructor.  If ANY advice gets put on it, we move
	// the NEW instruction inside the join point, which involves putting 
	// all the arguments in temps.
    public static BcelShadow makeConstructorCall(
        BcelWorld world,
        LazyMethodGen enclosingMethod,
        InstructionHandle callHandle, 
        BcelShadow enclosingShadow) 
    {
        final InstructionList body = enclosingMethod.getBody();
        
        Member sig = BcelWorld.makeMethodSignature(
                    	enclosingMethod.getEnclosingClass(),
                    	(InvokeInstruction) callHandle.getInstruction());
                    
		BcelShadow s = 
			new BcelShadow(
				world,
				ConstructorCall,
				sig,
                enclosingMethod, 
                enclosingShadow);
        ShadowRange r = new ShadowRange(body);
        r.associateWithShadow(s);
        r.associateWithTargets(
        	Range.genStart(body, callHandle),
            Range.genEnd(body, callHandle));
        retargetAllBranches(callHandle, r.getStart());                
        return s;
    }

    public static BcelShadow makeMethodCall(
            BcelWorld world,
            LazyMethodGen enclosingMethod,
            InstructionHandle callHandle,
            BcelShadow enclosingShadow) 
    {
        final InstructionList body = enclosingMethod.getBody();
        BcelShadow s =
            new BcelShadow(
                world,
                MethodCall,
                BcelWorld.makeMethodSignature(
                    enclosingMethod.getEnclosingClass(),
                    (InvokeInstruction) callHandle.getInstruction()),
                enclosingMethod,
                enclosingShadow);
        ShadowRange r = new ShadowRange(body);
        r.associateWithShadow(s);
        r.associateWithTargets(
        	Range.genStart(body, callHandle),
            Range.genEnd(body, callHandle));                
        retargetAllBranches(callHandle, r.getStart());
        return s;
    }
    
	
	public static BcelShadow makeShadowForMethodCall(
		BcelWorld world,
		LazyMethodGen enclosingMethod,
		InstructionHandle callHandle,
		BcelShadow enclosingShadow,
		Kind kind,
		ResolvedMember sig)
	{
        final InstructionList body = enclosingMethod.getBody();
        BcelShadow s =
            new BcelShadow(
                world,
                kind,
                sig,
                enclosingMethod,
                enclosingShadow);
        ShadowRange r = new ShadowRange(body);
        r.associateWithShadow(s);
        r.associateWithTargets(
        	Range.genStart(body, callHandle),
            Range.genEnd(body, callHandle));                
        retargetAllBranches(callHandle, r.getStart());
        return s;
	}
    

    public static BcelShadow makeFieldGet(
            BcelWorld world,
            LazyMethodGen enclosingMethod,
            InstructionHandle getHandle,
            BcelShadow enclosingShadow) 
    {
        final InstructionList body = enclosingMethod.getBody();
        BcelShadow s =
            new BcelShadow(
                world,
                FieldGet,
                BcelWorld.makeFieldSignature(
                    enclosingMethod.getEnclosingClass(),
                    (FieldInstruction) getHandle.getInstruction()),
                enclosingMethod,
                enclosingShadow);
        ShadowRange r = new ShadowRange(body);
        r.associateWithShadow(s);
        r.associateWithTargets(
            Range.genStart(body, getHandle),
            Range.genEnd(body, getHandle));
        retargetAllBranches(getHandle, r.getStart());
        return s;
    }
    
    public static BcelShadow makeFieldSet(
            BcelWorld world,
            LazyMethodGen enclosingMethod,
            InstructionHandle setHandle,
            BcelShadow enclosingShadow) 
    {
        final InstructionList body = enclosingMethod.getBody();
        BcelShadow s =
            new BcelShadow(
                world,
                FieldSet,
                BcelWorld.makeFieldSignature(
                    enclosingMethod.getEnclosingClass(),
                    (FieldInstruction) setHandle.getInstruction()),
                enclosingMethod,
                enclosingShadow);
        ShadowRange r = new ShadowRange(body);
        r.associateWithShadow(s);
        r.associateWithTargets(
            Range.genStart(body, setHandle),
            Range.genEnd(body, setHandle));                
        retargetAllBranches(setHandle, r.getStart());
        return s;
    }    

	public static void retargetAllBranches(InstructionHandle from, InstructionHandle to) {
		InstructionTargeter[] sources = from.getTargeters();
		if (sources != null) {
			for (int i = sources.length - 1; i >= 0; i--) {
				InstructionTargeter source = sources[i];
				if (source instanceof BranchInstruction) {
					source.updateTarget(from, to);
				}
			}
		}
	}

//    // ---- type access methods
//    private ObjectType getTargetBcelType() {
//        return (ObjectType) BcelWorld.makeBcelType(getTargetType());
//    }
//    private Type getArgBcelType(int arg) {
//        return BcelWorld.makeBcelType(getArgType(arg));
//    }

    // ---- kinding

	/**
	 * If the end of my range has no real instructions following then
	 * my context needs a return at the end.
	 */
    public boolean terminatesWithReturn() {
    	return getRange().getRealNext() == null;
    }
    
    /**
	 * Is arg0 occupied with the value of this
	 */
    public boolean arg0HoldsThis() {
    	if (getKind().isEnclosingKind()) {
    		return !getSignature().isStatic();
    	} else if (enclosingShadow == null) {
    		//XXX this is mostly right
    		// this doesn't do the right thing for calls in the pre part of introduced constructors.
    		return !enclosingMethod.isStatic();
    	} else {
    		return ((BcelShadow)enclosingShadow).arg0HoldsThis();
    	}
    }

    // ---- argument getting methods

    private BcelVar thisVar = null;
    private BcelVar targetVar = null;
    private BcelVar[] argVars = null;
    private Map/*<TypeX,BcelVar>*/ kindedAnnotationVars = null;
    private Map/*<TypeX,BcelVar>*/ thisAnnotationVars = null;
    private Map/*<TypeX,BcelVar>*/ targetAnnotationVars = null;
    private Map/*<TypeX,BcelVar>*/[] argAnnotationVars = null;
    private Map/*<TypeX,BcelVar>*/ withinAnnotationVars = null;
    private Map/*<TypeX,BcelVar>*/ withincodeAnnotationVars = null;

    public Var getThisVar() {
        if (!hasThis()) {
            throw new IllegalStateException("no this");
        }
        initializeThisVar();
        return thisVar;
    }
	public Var getThisAnnotationVar(TypeX forAnnotationType) {
        if (!hasThis()) {
            throw new IllegalStateException("no this");
        }
        initializeThisAnnotationVars(); // FIXME asc Why bother with this if we always return one?
        // Even if we can't find one, we have to return one as we might have this annotation at runtime
        Var v = (Var) thisAnnotationVars.get(forAnnotationType);
	    if (v==null)
	      v = new TypeAnnotationAccessVar(forAnnotationType.resolve(world),(BcelVar)getThisVar()); 
        return v;
	}
    public Var getTargetVar() {
        if (!hasTarget()) {
            throw new IllegalStateException("no target");
        }
	    initializeTargetVar();
	    return targetVar;
    }
    public Var getTargetAnnotationVar(TypeX forAnnotationType) {
        if (!hasTarget()) {
            throw new IllegalStateException("no target");
        }
	    initializeTargetAnnotationVars(); // FIXME asc why bother with this if we always return one?
	    Var v  =(Var) targetAnnotationVars.get(forAnnotationType);
	    // Even if we can't find one, we have to return one as we might have this annotation at runtime
	    if (v==null)
	      v = new TypeAnnotationAccessVar(forAnnotationType.resolve(world),(BcelVar)getTargetVar()); 
		return v;
   }
   public Var getArgVar(int i) {
        initializeArgVars();
        return argVars[i];
   }
   public Var getArgAnnotationVar(int i,TypeX forAnnotationType) {
		initializeArgAnnotationVars();
		
		Var v= (Var) argAnnotationVars[i].get(forAnnotationType);
		if (v==null) 
		      v = new TypeAnnotationAccessVar(forAnnotationType.resolve(world),(BcelVar)getArgVar(i)); 
		return v;
   }   
   public Var getKindedAnnotationVar(TypeX forAnnotationType) {
   		initializeKindedAnnotationVars();
   		return (Var) kindedAnnotationVars.get(forAnnotationType);
   }
	public Var getWithinAnnotationVar(TypeX forAnnotationType) {
		initializeWithinAnnotationVars();
		return (Var) withinAnnotationVars.get(forAnnotationType);
	}	
	public Var getWithinCodeAnnotationVar(TypeX forAnnotationType) {
		initializeWithinCodeAnnotationVars();
		return (Var) withincodeAnnotationVars.get(forAnnotationType);
	}
    
    // reflective thisJoinPoint support
    private BcelVar thisJoinPointVar = null;
    private boolean isThisJoinPointLazy;
    private int lazyTjpConsumers = 0;
    private BcelVar thisJoinPointStaticPartVar = null;  
    // private BcelVar thisEnclosingJoinPointStaticPartVar = null;  
    
	public final Var getThisJoinPointStaticPartVar() {
		return getThisJoinPointStaticPartBcelVar();
	}
	public final Var getThisEnclosingJoinPointStaticPartVar() {
		return getThisEnclosingJoinPointStaticPartBcelVar();
	}
    
    public void requireThisJoinPoint(boolean hasGuardTest) {
    	if (!hasGuardTest) {
    		isThisJoinPointLazy = false;
    	} else {
    		lazyTjpConsumers++;
    	}
    	if (thisJoinPointVar == null) {
			thisJoinPointVar = genTempVar(TypeX.forName("org.aspectj.lang.JoinPoint"));
    	}
    }
    
    
    public Var getThisJoinPointVar() {
    	requireThisJoinPoint(false);
    	return thisJoinPointVar;
    }
    
    void initializeThisJoinPoint() {
    	if (thisJoinPointVar == null) return;
    	
    	if (isThisJoinPointLazy) {
    		isThisJoinPointLazy = checkLazyTjp();
    	}
    		
		if (isThisJoinPointLazy) {
			createThisJoinPoint(); // make sure any state needed is initialized, but throw the instructions out
			
			if (lazyTjpConsumers == 1) return; // special case only one lazyTjpUser
			
			InstructionFactory fact = getFactory();
			InstructionList il = new InstructionList();
			il.append(InstructionConstants.ACONST_NULL);
			il.append(thisJoinPointVar.createStore(fact));
			range.insert(il, Range.OutsideBefore);
		} else {
			InstructionFactory fact = getFactory();
			InstructionList il = createThisJoinPoint();
			il.append(thisJoinPointVar.createStore(fact));
			range.insert(il, Range.OutsideBefore);
		}
    }
    
    private boolean checkLazyTjp() {    	
    	// check for around advice
    	for (Iterator i = mungers.iterator(); i.hasNext();) {
			ShadowMunger munger = (ShadowMunger) i.next();
			if (munger instanceof Advice) {
				if ( ((Advice)munger).getKind() == AdviceKind.Around) {
					world.getLint().canNotImplementLazyTjp.signal(
					    new String[] {toString()},
					    getSourceLocation(),
					    new ISourceLocation[] { munger.getSourceLocation() }
					);
					return false;
				}
			}
		}
    	
    	return true;
    }
    
    InstructionList loadThisJoinPoint() {
		InstructionFactory fact = getFactory();
		InstructionList il = new InstructionList();

    	if (isThisJoinPointLazy) {
    		il.append(createThisJoinPoint());
    		
    		if (lazyTjpConsumers > 1) {
				il.append(thisJoinPointVar.createStore(fact));
				
				InstructionHandle end = il.append(thisJoinPointVar.createLoad(fact));
				
				il.insert(InstructionFactory.createBranchInstruction(Constants.IFNONNULL, end));
				il.insert(thisJoinPointVar.createLoad(fact));
    		}
    	} else {			
			thisJoinPointVar.appendLoad(il, fact);
    	}
    	
		return il;
    }

	InstructionList createThisJoinPoint() {
		InstructionFactory fact = getFactory();
		InstructionList il = new InstructionList();
		
		BcelVar staticPart = getThisJoinPointStaticPartBcelVar();
		staticPart.appendLoad(il, fact);
		if (hasThis()) {
			((BcelVar)getThisVar()).appendLoad(il, fact);
		} else {
			il.append(new ACONST_NULL());
		}
		if (hasTarget()) {
			((BcelVar)getTargetVar()).appendLoad(il, fact);
		} else {
			il.append(new ACONST_NULL());
		}
		
		switch(getArgCount()) {
			case 0:
				il.append(fact.createInvoke("org.aspectj.runtime.reflect.Factory", 
									"makeJP", LazyClassGen.tjpType,
									new Type[] { LazyClassGen.staticTjpType,
											Type.OBJECT, Type.OBJECT},
									Constants.INVOKESTATIC));
				break;
			case 1:
				((BcelVar)getArgVar(0)).appendLoadAndConvert(il, fact, world.getCoreType(ResolvedTypeX.OBJECT));
				il.append(fact.createInvoke("org.aspectj.runtime.reflect.Factory", 
									"makeJP", LazyClassGen.tjpType,
									new Type[] { LazyClassGen.staticTjpType,
											Type.OBJECT, Type.OBJECT, Type.OBJECT},
									Constants.INVOKESTATIC));
				break;
			case 2:
				((BcelVar)getArgVar(0)).appendLoadAndConvert(il, fact, world.getCoreType(ResolvedTypeX.OBJECT));
				((BcelVar)getArgVar(1)).appendLoadAndConvert(il, fact, world.getCoreType(ResolvedTypeX.OBJECT));
				il.append(fact.createInvoke("org.aspectj.runtime.reflect.Factory", 
									"makeJP", LazyClassGen.tjpType,
									new Type[] { LazyClassGen.staticTjpType,
											Type.OBJECT, Type.OBJECT, Type.OBJECT, Type.OBJECT},
									Constants.INVOKESTATIC));
				break;
			default:
				il.append(makeArgsObjectArray());
				il.append(fact.createInvoke("org.aspectj.runtime.reflect.Factory", 
									"makeJP", LazyClassGen.tjpType,
									new Type[] { LazyClassGen.staticTjpType,
											Type.OBJECT, Type.OBJECT, new ArrayType(Type.OBJECT, 1)},
									Constants.INVOKESTATIC));
				break;
		}
		
		return il;
	}

    /**
     * Get the Var for the jpStaticPart
     * @return
     */
    public BcelVar getThisJoinPointStaticPartBcelVar() {
        return getThisJoinPointStaticPartBcelVar(false);
    }

    /**
     * Get the Var for the xxxxJpStaticPart, xxx = this or enclosing
     * @param isEnclosingJp true to have the enclosingJpStaticPart
     * @return
     */
    public BcelVar getThisJoinPointStaticPartBcelVar(final boolean isEnclosingJp) {
    	if (thisJoinPointStaticPartVar == null) {
    		Field field = getEnclosingClass().getTjpField(this, isEnclosingJp);
    		thisJoinPointStaticPartVar =
    			new BcelFieldRef(
    				isEnclosingJp?
                        world.getCoreType(TypeX.forName("org.aspectj.lang.JoinPoint$EnclosingStaticPart")):
                        world.getCoreType(TypeX.forName("org.aspectj.lang.JoinPoint$StaticPart")),
    				getEnclosingClass().getClassName(),
    				field.getName());
//    		getEnclosingClass().warnOnAddedStaticInitializer(this,munger.getSourceLocation());
    	}
    	return thisJoinPointStaticPartVar;
    }

    /**
     * Get the Var for the enclosingJpStaticPart
     * @return
     */
    public BcelVar getThisEnclosingJoinPointStaticPartBcelVar() {
    	if (enclosingShadow == null) {
    		// the enclosing of an execution is itself
    		return getThisJoinPointStaticPartBcelVar(true);
    	} else {
    		return ((BcelShadow)enclosingShadow).getThisJoinPointStaticPartBcelVar(true);
    	}
    }
    
    //??? need to better understand all the enclosing variants
    public Member getEnclosingCodeSignature() {
    	if (getKind().isEnclosingKind()) {
    		return getSignature();
    	} else if (getKind() == Shadow.PreInitialization) {
          // PreInit doesn't enclose code but its signature
          // is correctly the signature of the ctor.
    	  return getSignature();
    	} else if (enclosingShadow == null) {
    		return getEnclosingMethod().getMemberView();
    	} else {
    		return enclosingShadow.getSignature();
    	}
    }


    private InstructionList makeArgsObjectArray() {
    	InstructionFactory fact = getFactory();
        BcelVar arrayVar = genTempVar(TypeX.OBJECTARRAY);
        final InstructionList il = new InstructionList();
        int alen = getArgCount() ;
        il.append(Utility.createConstant(fact, alen));
        il.append((Instruction)fact.createNewArray(Type.OBJECT, (short)1));
        arrayVar.appendStore(il, fact);

        int stateIndex = 0;     
        for (int i = 0, len = getArgCount(); i<len; i++) {
            arrayVar.appendConvertableArrayStore(il, fact, stateIndex, (BcelVar)getArgVar(i));
            stateIndex++;
        }
        arrayVar.appendLoad(il, fact);
        return il;
    }

    // ---- initializing var tables

    /* initializing this is doesn't do anything, because this 
     * is protected from side-effects, so we don't need to copy its location
     */

    private void initializeThisVar() {
        if (thisVar != null) return;
        thisVar = new BcelVar(getThisType().resolve(world), 0);
        thisVar.setPositionInAroundState(0);
    }
    public void initializeTargetVar() {
    	InstructionFactory fact = getFactory();    	
        if (targetVar != null) return;
        if (getKind().isTargetSameAsThis()) {
            if (hasThis()) initializeThisVar();
            targetVar = thisVar;
        } else {
            initializeArgVars(); // gotta pop off the args before we find the target
            TypeX type = getTargetType();
            type = ensureTargetTypeIsCorrect(type);
            targetVar = genTempVar(type, "ajc$target");
            range.insert(targetVar.createStore(fact), Range.OutsideBefore); 
	        targetVar.setPositionInAroundState(hasThis() ? 1 : 0);            
        }
    }
    
    /* PR 72528
     * This method double checks the target type under certain conditions.  The Java 1.4
     * compilers seem to take calls to clone methods on array types and create bytecode that
     * looks like clone is being called on Object.  If we advise a clone call with around
     * advice we extract the call into a helper method which we can then refer to.  Because the
     * type in the bytecode for the call to clone is Object we create a helper method with
     * an Object parameter - this is not correct as we have lost the fact that the actual
     * type is an array type.  If we don't do the check below we will create code that fails
     * java verification.  This method checks for the peculiar set of conditions and if they
     * are true, it has a sneak peek at the code before the call to see what is on the stack.
     */
    public TypeX ensureTargetTypeIsCorrect(TypeX tx) {
    	if (tx.equals(ResolvedTypeX.OBJECT) && getKind() == MethodCall && 
    	    getSignature().getReturnType().equals(ResolvedTypeX.OBJECT) && 
			getSignature().getArity()==0 && 
			getSignature().getName().charAt(0) == 'c' &&
			getSignature().getName().equals("clone")) {
    		
    		// Lets go back through the code from the start of the shadow
            InstructionHandle searchPtr = range.getStart().getPrev();
            while (Range.isRangeHandle(searchPtr) || 
            	   searchPtr.getInstruction() instanceof StoreInstruction) { // ignore this instruction - it doesnt give us the info we want
            	searchPtr = searchPtr.getPrev();  
            }
            
            // A load instruction may tell us the real type of what the clone() call is on
            if (searchPtr.getInstruction() instanceof LoadInstruction) {
            	LoadInstruction li = (LoadInstruction)searchPtr.getInstruction();
            	li.getIndex();
            	LocalVariableTag lvt = LazyMethodGen.getLocalVariableTag(searchPtr,li.getIndex());
            	return lvt.getType();
            }
            // A field access instruction may tell us the real type of what the clone() call is on
            if (searchPtr.getInstruction() instanceof FieldInstruction) {
            	FieldInstruction si = (FieldInstruction)searchPtr.getInstruction();
            	Type t = si.getFieldType(getEnclosingClass().getConstantPoolGen());
            	return BcelWorld.fromBcel(t);
            } 
            // A new array instruction obviously tells us it is an array type !
            if (searchPtr.getInstruction() instanceof ANEWARRAY) {
            	//ANEWARRAY ana = (ANEWARRAY)searchPoint.getInstruction();
            	//Type t = ana.getType(getEnclosingClass().getConstantPoolGen());
            	// Just use a standard java.lang.object array - that will work fine
            	return BcelWorld.fromBcel(new ArrayType(Type.OBJECT,1));
            }
            // A multi new array instruction obviously tells us it is an array type !
            if (searchPtr.getInstruction() instanceof MULTIANEWARRAY) {
            	MULTIANEWARRAY ana = (MULTIANEWARRAY)searchPtr.getInstruction();
                // Type t = ana.getType(getEnclosingClass().getConstantPoolGen());
            	// t = new ArrayType(t,ana.getDimensions());
            	// Just use a standard java.lang.object array - that will work fine
            	return BcelWorld.fromBcel(new ArrayType(Type.OBJECT,ana.getDimensions()));
            }
            throw new BCException("Can't determine real target of clone() when processing instruction "+
              searchPtr.getInstruction());
    	}
    	return tx;
    }
    
    
    public void initializeArgVars() {
        if (argVars != null) return;
    	InstructionFactory fact = getFactory();
        int len = getArgCount();
        argVars = new BcelVar[len];
        int positionOffset = (hasTarget() ? 1 : 0) + 
        		((hasThis() && !getKind().isTargetSameAsThis()) ? 1 : 0);
                     
        if (getKind().argsOnStack()) {
            // we move backwards because we're popping off the stack
            for (int i = len - 1; i >= 0; i--) {
                TypeX type = getArgType(i);
                BcelVar tmp = genTempVar(type, "ajc$arg" + i);
                range.insert(tmp.createStore(getFactory()), Range.OutsideBefore);
                int position = i;
                position += positionOffset;
                tmp.setPositionInAroundState(position);
                argVars[i] = tmp;
            }
        } else {
            int index = 0;
            if (arg0HoldsThis()) index++;
            
            for (int i = 0; i < len; i++) {
                TypeX type = getArgType(i); 
                BcelVar tmp = genTempVar(type, "ajc$arg" + i);
                range.insert(tmp.createCopyFrom(fact, index), Range.OutsideBefore);
                argVars[i] = tmp;
                int position = i;
                position += positionOffset;
//                System.out.println("set position: " + tmp + ", " + position + " in " + this);
//                System.out.println("   hasThis: " + hasThis() + ", hasTarget: " + hasTarget());
                tmp.setPositionInAroundState(position);
                index += type.getSize();
            }       
        }
    }   
    public void initializeForAroundClosure() {
        initializeArgVars();
        if (hasTarget()) initializeTargetVar();
        if (hasThis()) initializeThisVar();
//        System.out.println("initialized: " + this + " thisVar = " + thisVar);
    }

    public void initializeThisAnnotationVars() {
    	if (thisAnnotationVars != null) return;
    	thisAnnotationVars = new HashMap();
    	// populate..
    }
    public void initializeTargetAnnotationVars() {
    	if (targetAnnotationVars != null) return;
        if (getKind().isTargetSameAsThis()) {
            if (hasThis()) initializeThisAnnotationVars();
            targetAnnotationVars = thisAnnotationVars;
        } else {
        	targetAnnotationVars = new HashMap();
        	ResolvedTypeX[] rtx = this.getTargetType().resolve(world).getAnnotationTypes(); // what about annotations we havent gotten yet but we will get in subclasses?
        	for (int i = 0; i < rtx.length; i++) {
				ResolvedTypeX typeX = rtx[i];
				targetAnnotationVars.put(typeX,new TypeAnnotationAccessVar(typeX,(BcelVar)getTargetVar()));
			}
        	// populate.
        }
    }
    public void initializeArgAnnotationVars() {
    	if (argAnnotationVars != null) return;
    	int numArgs = getArgCount();
    	argAnnotationVars = new Map[numArgs];
    	for (int i = 0; i < argAnnotationVars.length; i++) {
			argAnnotationVars[i] = new HashMap();
			//FIXME asc just delete this logic - we always build the Var on demand, as we don't know at weave time
			// what the full set of annotations could be (due to static/dynamic type differences...)
		}
    }
	
    public void initializeKindedAnnotationVars() {
    	if (kindedAnnotationVars != null) return;
    	kindedAnnotationVars = new HashMap();
    	// by determining what "kind" of shadow we are, we can find out the
    	// annotations on the appropriate element (method, field, constructor, type).
    	// Then create one BcelVar entry in the map for each annotation, keyed by
    	// annotation type (TypeX).
    	
    	// FIXME asc Refactor this code, there is duplication
    	ResolvedTypeX[] annotations = null;
    	ResolvedMember itdMember =null;
    	Member relevantMember = getSignature();
    	TypeX  relevantType   = null;
    	TypeX aspect = null;
    	
    	if (getKind() == Shadow.StaticInitialization) {
    		relevantType = getSignature().getDeclaringType();
    		annotations  = relevantType.resolve(world).getAnnotationTypes();
    		
    	} else if (getKind() == Shadow.MethodCall  || getKind() == Shadow.ConstructorCall) {
    		relevantType = getSignature().getDeclaringType();
            relevantMember = findMethod2(relevantType.getDeclaredMethods(world),getSignature());
    		
			if (relevantMember == null) {
				// check the ITD'd dooberries
				List mungers = relevantType.resolve(world).getInterTypeMungers();
				for (Iterator iter = mungers.iterator(); iter.hasNext();) {
					BcelTypeMunger typeMunger = (BcelTypeMunger) iter.next();
					if (typeMunger.getMunger() instanceof NewMethodTypeMunger ||
						typeMunger.getMunger() instanceof NewConstructorTypeMunger) {
					  ResolvedMember fakerm = typeMunger.getSignature();
					  //if (fakerm.hasAnnotations()) 
					   
					  ResolvedMember ajcMethod = (getSignature().getKind()==ResolvedMember.CONSTRUCTOR?
						  AjcMemberMaker.postIntroducedConstructor(typeMunger.getAspectType(),fakerm.getDeclaringType(),fakerm.getParameterTypes()):
						  AjcMemberMaker.interMethodBody(fakerm,typeMunger.getAspectType()));
				  	  ResolvedMember rmm       = findMethod(typeMunger.getAspectType(),ajcMethod);
					  if (fakerm.getName().equals(getSignature().getName()) &&
						  fakerm.getParameterSignature().equals(getSignature().getParameterSignature())) {
						relevantType = typeMunger.getAspectType();
						relevantMember = rmm;
					  }
					}
				}
			}
    		annotations = relevantMember.getAnnotationTypes();
    		
    	} else if (getKind() == Shadow.FieldSet || getKind() == Shadow.FieldGet) {
    		relevantType = getSignature().getDeclaringType();
    		relevantMember = findField(relevantType.getDeclaredFields(world),getSignature());
    		
			if (relevantMember==null) {
              // check the ITD'd dooberries
				List mungers = relevantType.resolve(world).getInterTypeMungers();
				for (Iterator iter = mungers.iterator(); iter.hasNext();) {
					BcelTypeMunger typeMunger = (BcelTypeMunger) iter.next();
					if (typeMunger.getMunger() instanceof NewFieldTypeMunger) {
					  ResolvedMember fakerm = typeMunger.getSignature();
					  //if (fakerm.hasAnnotations()) 
					  ResolvedMember ajcMethod = AjcMemberMaker.interFieldInitializer(fakerm,typeMunger.getAspectType());
				  	  ResolvedMember rmm       = findMethod(typeMunger.getAspectType(),ajcMethod);
					  if (fakerm.equals(getSignature())) {
						relevantType = typeMunger.getAspectType();
						relevantMember = rmm;
					  }
					}
				}	
			}
    		annotations = relevantMember.getAnnotationTypes();
    		
    	} else if (getKind() == Shadow.MethodExecution || getKind() == Shadow.ConstructorExecution || 
    		        getKind() == Shadow.AdviceExecution) {
    		relevantType = getSignature().getDeclaringType();
    		ResolvedMember rm[] = relevantType.getDeclaredMethods(world);
    		relevantMember = findMethod2(relevantType.getDeclaredMethods(world),getSignature());
    		
			if (relevantMember == null) {
				// check the ITD'd dooberries
				List mungers = relevantType.resolve(world).getInterTypeMungers();
				for (Iterator iter = mungers.iterator(); iter.hasNext();) {
					BcelTypeMunger typeMunger = (BcelTypeMunger) iter.next();
					if (typeMunger.getMunger() instanceof NewMethodTypeMunger ||
						typeMunger.getMunger() instanceof NewConstructorTypeMunger) {
					  ResolvedMember fakerm = typeMunger.getSignature();
					  //if (fakerm.hasAnnotations()) 
					  
					  ResolvedMember ajcMethod = (getSignature().getKind()==ResolvedMember.CONSTRUCTOR?
						  AjcMemberMaker.postIntroducedConstructor(typeMunger.getAspectType(),fakerm.getDeclaringType(),fakerm.getParameterTypes()):
						  AjcMemberMaker.interMethodBody(fakerm,typeMunger.getAspectType()));
				  	  ResolvedMember rmm       = findMethod(typeMunger.getAspectType(),ajcMethod);
					  if (fakerm.getName().equals(getSignature().getName()) &&
						  fakerm.getParameterSignature().equals(getSignature().getParameterSignature())) {
						relevantType = typeMunger.getAspectType();
						relevantMember = rmm;
					  }
					}
				}
			}
    		annotations = relevantMember.getAnnotationTypes();
    		
    	} else if (getKind() == Shadow.ExceptionHandler) {
    		relevantType = getSignature().getParameterTypes()[0];
    		annotations  =  relevantType.resolve(world).getAnnotationTypes();
    		
    	} else if (getKind() == Shadow.PreInitialization || getKind() == Shadow.Initialization) {
    		relevantType = getSignature().getDeclaringType();
    		ResolvedMember found = findMethod2(relevantType.getDeclaredMethods(world),getSignature());
    		annotations = found.getAnnotationTypes();
    	}
    	
    	if (annotations == null) {
    		// We can't have recognized the shadow - should blow up now to be on the safe side
    		throw new BCException("Couldn't discover annotations for shadow: "+getKind());
    	}
    	
		for (int i = 0; i < annotations.length; i++) {
			ResolvedTypeX aTX = annotations[i];
			KindedAnnotationAccessVar kaav =  new KindedAnnotationAccessVar(getKind(),aTX.resolve(world),relevantType,relevantMember);
    		kindedAnnotationVars.put(aTX,kaav);
		}
    }
    
//FIXME asc whats the real diff between this one and the version in findMethod()?
	 ResolvedMember findMethod2(ResolvedMember rm[], Member sig) {
		ResolvedMember found = null;
		// String searchString = getSignature().getName()+getSignature().getParameterSignature();
		for (int i = 0; i < rm.length && found==null; i++) {
			ResolvedMember member = rm[i];
			if (member.getName().equals(sig.getName()) && member.getParameterSignature().equals(sig.getParameterSignature()))
			  found = member;
		}
		return found;
	 }
    
    private ResolvedMember findMethod(ResolvedTypeX aspectType, ResolvedMember ajcMethod) {
       ResolvedMember decMethods[] = aspectType.getDeclaredMethods();
       for (int i = 0; i < decMethods.length; i++) {
		ResolvedMember member = decMethods[i];
		if (member.equals(ajcMethod)) return member;
	   }
		return null;
	}

	
   
	private ResolvedMember findField(ResolvedMember[] members,Member lookingFor) {
	  for (int i = 0; i < members.length; i++) {
		ResolvedMember member = members[i];
		if ( member.getName().equals(getSignature().getName()) &&
		     member.getType().equals(getSignature().getType()))  {
		     return member;
		}
	  }
	  return null;
	}
	
	
	public void initializeWithinAnnotationVars() {
    	if (withinAnnotationVars != null) return;
    	withinAnnotationVars = new HashMap();
    	
    	ResolvedTypeX[] annotations = getEnclosingType().getAnnotationTypes();
		for (int i = 0; i < annotations.length; i++) {
			ResolvedTypeX ann = annotations[i];
			Kind k = Shadow.StaticInitialization;
			withinAnnotationVars.put(ann,new KindedAnnotationAccessVar(k,ann,getEnclosingType(),null));
		}
    }
    
    public void initializeWithinCodeAnnotationVars() {
    	if (withincodeAnnotationVars != null) return;
    	withincodeAnnotationVars = new HashMap();
    
    	// For some shadow we are interested in annotations on the method containing that shadow.
		ResolvedTypeX[] annotations = getEnclosingMethod().getMemberView().getAnnotationTypes();
		for (int i = 0; i < annotations.length; i++) {
			ResolvedTypeX ann = annotations[i];
			Kind k = (getEnclosingMethod().getMemberView().getKind()==Member.CONSTRUCTOR?
					  Shadow.ConstructorExecution:Shadow.MethodExecution);
			withincodeAnnotationVars.put(ann,
					new KindedAnnotationAccessVar(k,ann,getEnclosingType(),getEnclosingCodeSignature()));
		}
    }
    
            
    // ---- weave methods

    void weaveBefore(BcelAdvice munger) {
        range.insert(
            munger.getAdviceInstructions(this, null, range.getRealStart()), 
            Range.InsideBefore);
    }
    
    public void weaveAfter(BcelAdvice munger) {
        weaveAfterThrowing(munger, TypeX.THROWABLE);
        weaveAfterReturning(munger);
    }
	
	/**
	 * We guarantee that the return value is on the top of the stack when
	 * munger.getAdviceInstructions() will be run
	 * (Unless we have a void return type in which case there's nothing)
	 */
    public void weaveAfterReturning(BcelAdvice munger) {
        // InstructionFactory fact = getFactory();
        List returns = new ArrayList();
        Instruction ret = null;
        for (InstructionHandle ih = range.getStart(); ih != range.getEnd(); ih = ih.getNext()) {
            if (ih.getInstruction() instanceof ReturnInstruction) {
                returns.add(ih);
                ret = Utility.copyInstruction(ih.getInstruction());
            }
        }
        InstructionList retList;
        InstructionHandle afterAdvice;
        if (ret != null) {
            retList = new InstructionList(ret);
            afterAdvice = retList.getStart();
        } else /* if (munger.hasDynamicTests()) */ {
        	/*
        	 * 
 27:  getstatic       #72; //Field ajc$cflowCounter$0:Lorg/aspectj/runtime/internal/CFlowCounter;
 30:  invokevirtual   #87; //Method org/aspectj/runtime/internal/CFlowCounter.dec:()V
 33:  aload   6
 35:  athrow
 36:  nop
 37:  getstatic       #72; //Field ajc$cflowCounter$0:Lorg/aspectj/runtime/internal/CFlowCounter;
 40:  invokevirtual   #87; //Method org/aspectj/runtime/internal/CFlowCounter.dec:()V
 43:  d2i
 44:  invokespecial   #23; //Method java/lang/Object."<init>":()V
        	 */
            retList = new InstructionList(InstructionConstants.NOP);            
            afterAdvice = retList.getStart();
//        } else {
//        	retList = new InstructionList();
//        	afterAdvice = null;
        }

        InstructionList advice = new InstructionList();
        BcelVar tempVar = null;
        if (munger.hasExtraParameter()) {
            TypeX tempVarType = getReturnType();
            if (tempVarType.equals(ResolvedTypeX.VOID)) {
            	tempVar = genTempVar(TypeX.OBJECT);
            	advice.append(InstructionConstants.ACONST_NULL);
            	tempVar.appendStore(advice, getFactory());
            } else {
	            tempVar = genTempVar(tempVarType);
	            advice.append(InstructionFactory.createDup(tempVarType.getSize()));
	            tempVar.appendStore(advice, getFactory());
            }
        }
        advice.append(munger.getAdviceInstructions(this, tempVar, afterAdvice));            

        if (ret != null) {
            InstructionHandle gotoTarget = advice.getStart();           
			for (Iterator i = returns.iterator(); i.hasNext();) {
				InstructionHandle ih = (InstructionHandle) i.next();
				Utility.replaceInstruction(
					ih,
					InstructionFactory.createBranchInstruction(
						Constants.GOTO,
						gotoTarget),
					enclosingMethod);
			}
            range.append(advice);
            range.append(retList);
        } else {            
            range.append(advice);
            range.append(retList);
        }
    }
    
    public void weaveAfterThrowing(BcelAdvice munger, TypeX catchType) {
    	// a good optimization would be not to generate anything here
    	// if the shadow is GUARANTEED empty (i.e., there's NOTHING, not even
    	// a shadow, inside me).
    	if (getRange().getStart().getNext() == getRange().getEnd()) return;
        InstructionFactory fact = getFactory();        
        InstructionList handler = new InstructionList();        
        BcelVar exceptionVar = genTempVar(catchType);
        exceptionVar.appendStore(handler, fact);

        // pr62642
        // I will now jump through some firey BCEL hoops to generate a trivial bit of code:
        // if (exc instanceof ExceptionInInitializerError) 
        //    throw (ExceptionInInitializerError)exc;
        if (this.getEnclosingMethod().getName().equals("<clinit>")) {
            ResolvedTypeX eiieType = world.resolve("java.lang.ExceptionInInitializerError");
            ObjectType eiieBcelType = (ObjectType)BcelWorld.makeBcelType(eiieType);
        	InstructionList ih = new InstructionList(InstructionConstants.NOP);
        	handler.append(exceptionVar.createLoad(fact));
        	handler.append(fact.createInstanceOf(eiieBcelType));
        	BranchInstruction bi = 
                InstructionFactory.createBranchInstruction(Constants.IFEQ,ih.getStart());
        	handler.append(bi);
        	handler.append(exceptionVar.createLoad(fact));
        	handler.append(fact.createCheckCast(eiieBcelType));
        	handler.append(InstructionConstants.ATHROW);
        	handler.append(ih);
        }
        
        InstructionList endHandler = new InstructionList(
            exceptionVar.createLoad(fact));
        handler.append(munger.getAdviceInstructions(this, exceptionVar, endHandler.getStart()));
        handler.append(endHandler);
        handler.append(InstructionConstants.ATHROW);        
        InstructionHandle handlerStart = handler.getStart();
                                    
        if (isFallsThrough()) {
            InstructionHandle jumpTarget = handler.append(InstructionConstants.NOP);
            handler.insert(InstructionFactory.createBranchInstruction(Constants.GOTO, jumpTarget));
        }
		InstructionHandle protectedEnd = handler.getStart();
        range.insert(handler, Range.InsideAfter);       

        enclosingMethod.addExceptionHandler(range.getStart().getNext(), protectedEnd.getPrev(),
                                 handlerStart, (ObjectType)BcelWorld.makeBcelType(catchType), //???Type.THROWABLE, 
                                 // high priority if our args are on the stack
                                 getKind().hasHighPriorityExceptions());
    }      


	//??? this shares a lot of code with the above weaveAfterThrowing
	//??? would be nice to abstract that to say things only once
    public void weaveSoftener(BcelAdvice munger, TypeX catchType) {
    	// a good optimization would be not to generate anything here
    	// if the shadow is GUARANTEED empty (i.e., there's NOTHING, not even
    	// a shadow, inside me).
    	if (getRange().getStart().getNext() == getRange().getEnd()) return;
				
        InstructionFactory fact = getFactory();
        InstructionList handler = new InstructionList();        
		InstructionList rtExHandler = new InstructionList();
        BcelVar exceptionVar = genTempVar(catchType);
		        
		handler.append(fact.createNew(NameMangler.SOFT_EXCEPTION_TYPE));
		handler.append(InstructionFactory.createDup(1));   
        handler.append(exceptionVar.createLoad(fact));
        handler.append(fact.createInvoke(NameMangler.SOFT_EXCEPTION_TYPE, "<init>", 
        					Type.VOID, new Type[] { Type.THROWABLE }, Constants.INVOKESPECIAL));  //??? special
        handler.append(InstructionConstants.ATHROW);        

		// ENH 42737
        exceptionVar.appendStore(rtExHandler, fact);
		// aload_1
		rtExHandler.append(exceptionVar.createLoad(fact));
		// instanceof class java/lang/RuntimeException
		rtExHandler.append(fact.createInstanceOf(new ObjectType("java.lang.RuntimeException")));
		// ifeq go to new SOFT_EXCEPTION_TYPE instruction
		rtExHandler.append(InstructionFactory.createBranchInstruction(Constants.IFEQ,handler.getStart()));
		// aload_1
		rtExHandler.append(exceptionVar.createLoad(fact));
		// athrow
		rtExHandler.append(InstructionFactory.ATHROW);

		InstructionHandle handlerStart = rtExHandler.getStart();

		if (isFallsThrough()) {
            InstructionHandle jumpTarget = range.getEnd();//handler.append(fact.NOP);
            rtExHandler.insert(InstructionFactory.createBranchInstruction(Constants.GOTO, jumpTarget));
        }

		rtExHandler.append(handler);

		InstructionHandle protectedEnd = rtExHandler.getStart();
        range.insert(rtExHandler, Range.InsideAfter);    
		
        enclosingMethod.addExceptionHandler(range.getStart().getNext(), protectedEnd.getPrev(),
                                 handlerStart, (ObjectType)BcelWorld.makeBcelType(catchType), 
                                 // high priority if our args are on the stack
                                 getKind().hasHighPriorityExceptions());    
    }      


	public void weavePerObjectEntry(final BcelAdvice munger, final BcelVar onVar) {
        final InstructionFactory fact = getFactory();        


		InstructionList entryInstructions = new InstructionList();
		InstructionList entrySuccessInstructions = new InstructionList();
		onVar.appendLoad(entrySuccessInstructions, fact);

  		entrySuccessInstructions.append(
  			Utility.createInvoke(fact, world, 
  				AjcMemberMaker.perObjectBind(munger.getConcreteAspect())));
		
		InstructionList testInstructions = 
			munger.getTestInstructions(this, entrySuccessInstructions.getStart(), 
								range.getRealStart(), 
								entrySuccessInstructions.getStart());
					
		entryInstructions.append(testInstructions);
		entryInstructions.append(entrySuccessInstructions);
		
		range.insert(entryInstructions, Range.InsideBefore);
	}
	
	// PTWIMPL Create static initializer to call the aspect factory 
	/**
	 * Causes the aspect instance to be *set* for later retrievable through localAspectof()/aspectOf()
	 */
	public void weavePerTypeWithinAspectInitialization(final BcelAdvice munger,TypeX t) {
		
		if (t.isInterface(world)) return; // Don't initialize statics in 
        final InstructionFactory fact = getFactory();        

		InstructionList entryInstructions = new InstructionList();
		InstructionList entrySuccessInstructions = new InstructionList();
		
		BcelObjectType aspectType = BcelWorld.getBcelObjectType(munger.getConcreteAspect());
		String aspectname = munger.getConcreteAspect().getName();
		
		String ptwField = NameMangler.perTypeWithinFieldForTarget(munger.getConcreteAspect());
		entrySuccessInstructions.append(new PUSH(fact.getConstantPool(),t.getName()));
		
		entrySuccessInstructions.append(fact.createInvoke(aspectname,"ajc$createAspectInstance",new ObjectType(aspectname),
				new Type[]{new ObjectType("java.lang.String")},Constants.INVOKESTATIC));
		entrySuccessInstructions.append(fact.createPutStatic(t.getName(),ptwField,
				new ObjectType(aspectname)));
		
		entryInstructions.append(entrySuccessInstructions);
		
		range.insert(entryInstructions, Range.InsideBefore);
	}
    
	
	public void weaveCflowEntry(final BcelAdvice munger, final Member cflowField) {
		final boolean isPer = munger.getKind() == AdviceKind.PerCflowBelowEntry || 
								munger.getKind() == AdviceKind.PerCflowEntry;
		
		final Type objectArrayType = new ArrayType(Type.OBJECT, 1);
        final InstructionFactory fact = getFactory();        

		final BcelVar testResult = genTempVar(ResolvedTypeX.BOOLEAN);

		InstructionList entryInstructions = new InstructionList();
		{
			InstructionList entrySuccessInstructions = new InstructionList();
			
			if (munger.hasDynamicTests()) {
				entryInstructions.append(Utility.createConstant(fact, 0));
				testResult.appendStore(entryInstructions, fact);
			
				entrySuccessInstructions.append(Utility.createConstant(fact, 1));
				testResult.appendStore(entrySuccessInstructions, fact);
			}

			if (isPer) {
	      		entrySuccessInstructions.append(
	      			fact.createInvoke(munger.getConcreteAspect().getName(), 
	      						NameMangler.PERCFLOW_PUSH_METHOD, 
	      						Type.VOID, 
	      						new Type[] { }, 
	      						Constants.INVOKESTATIC));
			} else {
				BcelVar[] cflowStateVars = munger.getExposedStateAsBcelVars();
	
				if (cflowStateVars.length == 0) {
					// This should be getting managed by a counter - lets make sure.
					if (!cflowField.getType().getName().endsWith("CFlowCounter")) 
						throw new RuntimeException("Incorrectly attempting counter operation on stacked cflow");
					entrySuccessInstructions.append(
			      			Utility.createGet(fact, cflowField));
					//arrayVar.appendLoad(entrySuccessInstructions, fact);
					entrySuccessInstructions.append(fact.createInvoke(NameMangler.CFLOW_COUNTER_TYPE,"inc",Type.VOID,new Type[] { },Constants.INVOKEVIRTUAL));
				} else {
				    BcelVar arrayVar = genTempVar(TypeX.OBJECTARRAY);
	
				    int alen = cflowStateVars.length;
				    entrySuccessInstructions.append(Utility.createConstant(fact, alen));
				    entrySuccessInstructions.append(
				    		(Instruction) fact.createNewArray(Type.OBJECT, (short) 1));
				    arrayVar.appendStore(entrySuccessInstructions, fact);
		 
				    for (int i = 0; i < alen; i++) {
				    	arrayVar.appendConvertableArrayStore(
				    			entrySuccessInstructions,
								fact,
								i,
								cflowStateVars[i]);
				    }		
	
				    entrySuccessInstructions.append(
				    		Utility.createGet(fact, cflowField));
				    arrayVar.appendLoad(entrySuccessInstructions, fact);

				    entrySuccessInstructions.append(
				    		fact.createInvoke(NameMangler.CFLOW_STACK_TYPE, "push", Type.VOID, 
	      						new Type[] { objectArrayType }, 
	      					Constants.INVOKEVIRTUAL));
				}
			}

			
			InstructionList testInstructions = 
				munger.getTestInstructions(this, entrySuccessInstructions.getStart(), 
									range.getRealStart(), 
									entrySuccessInstructions.getStart());
			entryInstructions.append(testInstructions);
			entryInstructions.append(entrySuccessInstructions);
		}
		
		// this is the same for both per and non-per
		weaveAfter(new BcelAdvice(null, null, null, 0, 0, 0, null, null) {
			public InstructionList getAdviceInstructions(
				BcelShadow s,
				BcelVar extraArgVar,
				InstructionHandle ifNoAdvice) {
				InstructionList exitInstructions = new InstructionList();
				if (munger.hasDynamicTests()) {
					testResult.appendLoad(exitInstructions, fact);
					exitInstructions.append(
						InstructionFactory.createBranchInstruction(
							Constants.IFEQ,
							ifNoAdvice));
				}
				exitInstructions.append(Utility.createGet(fact, cflowField));
				if (munger.getKind() != AdviceKind.PerCflowEntry &&
					munger.getKind() != AdviceKind.PerCflowBelowEntry &&
					munger.getExposedStateAsBcelVars().length==0) {
					exitInstructions
					.append(
						fact
						.createInvoke(
							NameMangler.CFLOW_COUNTER_TYPE,
							"dec",
							Type.VOID,
							new Type[] {
				}, Constants.INVOKEVIRTUAL));
				} else {
				exitInstructions
					.append(
						fact
						.createInvoke(
							NameMangler.CFLOW_STACK_TYPE,
							"pop",
							Type.VOID,
							new Type[] {
				}, Constants.INVOKEVIRTUAL));
				}
				return exitInstructions;
			}
		});
		
		
		range.insert(entryInstructions, Range.InsideBefore);
	}
    
    public void weaveAroundInline(
    	BcelAdvice munger,
    	boolean hasDynamicTest)
	{
		/* Implementation notes:
		 * 
		 * AroundInline still extracts the instructions of the original shadow into 
		 * an extracted method.  This allows inlining of even that advice that doesn't
		 * call proceed or calls proceed more than once. 
		 * 
		 * It extracts the instructions of the original shadow into a method.
		 * 
		 * Then it extracts the instructions of the advice into a new method defined on
		 * this enclosing class.  This new method can then be specialized as below.
		 * 
		 * Then it searches in the instructions of the advice for any call to the
		 * proceed method.
		 * 
		 *   At such a call, there is stuff on the stack representing the arguments to
		 *   proceed.  Pop these into the frame.
		 * 
		 *   Now build the stack for the call to the extracted method, taking values 
		 *   either from the join point state or from the new frame locs from proceed.
		 *   Now call the extracted method.  The right return value should be on the
		 *   stack, so no cast is necessary.
		 *
		 * If only one call to proceed is made, we can re-inline the original shadow.
		 * We are not doing that presently.
		 * 
		 * If the body of the advice can be determined to not alter the stack, or if
		 * this shadow doesn't care about the stack, i.e. method-execution, then the
		 * new method for the advice can also be re-lined.  We are not doing that
		 * presently.
		 */
		 
		// !!! THIS BLOCK OF CODE SHOULD BE IN A METHOD CALLED weaveAround(...);
        Member mungerSig = munger.getSignature();
        ResolvedTypeX declaringType = world.resolve(mungerSig.getDeclaringType(),true);
        if (declaringType == ResolvedTypeX.MISSING) {
          IMessage msg = new Message(
                WeaverMessages.format(WeaverMessages.CANT_FIND_TYPE_DURING_AROUND_WEAVE,declaringType.getClassName()),
                "",IMessage.ERROR,getSourceLocation(),null,
                new ISourceLocation[]{ munger.getSourceLocation()});
          world.getMessageHandler().handleMessage(msg);
        }
        //??? might want some checks here to give better errors
        BcelObjectType ot = BcelWorld.getBcelObjectType(declaringType); 
        
		LazyMethodGen adviceMethod = ot.getLazyClassGen().getLazyMethodGen(mungerSig);
		if (!adviceMethod.getCanInline()) {
			weaveAroundClosure(munger, hasDynamicTest);
			return;
		}

        // specific test for @AJ proceedInInners
        if (munger.getConcreteAspect().isAnnotationStyleAspect()) {
            // if we can't find one proceed()
            // we suspect that the call is happening in an inner class
            // so we don't inline it.
            // Note: for code style, this is done at Aspect compilation time.
            boolean canSeeProceedPassedToOther = false;
            InstructionHandle curr = adviceMethod.getBody().getStart();
            InstructionHandle end = adviceMethod.getBody().getEnd();
            ConstantPoolGen cpg = adviceMethod.getEnclosingClass().getConstantPoolGen();
            while (curr != end) {
                InstructionHandle next = curr.getNext();
                Instruction inst = curr.getInstruction();
                if ((inst instanceof InvokeInstruction)
                    && ((InvokeInstruction)inst).getSignature(cpg).indexOf("Lorg/aspectj/lang/ProceedingJoinPoint;") > 0) {
                    // we may want to refine to exclude stuff returning jp ?
                    // does code style skip inline if i write dump(thisJoinPoint) ?
                    canSeeProceedPassedToOther = true;// we see one pjp passed around - dangerous
                    break;
                }
                curr = next;
            }
            if (canSeeProceedPassedToOther) {
                // remember this decision to avoid re-analysis
                adviceMethod.setCanInline(false);
                weaveAroundClosure(munger, hasDynamicTest);
                return;
            }
        }



		// We can't inline around methods if they have around advice on them, this
		// is because the weaving will extract the body and hence the proceed call.
		//??? should consider optimizations to recognize simple cases that don't require body extraction
		enclosingMethod.setCanInline(false);
		
		// start by exposing various useful things into the frame
		final InstructionFactory fact = getFactory();
		
		// now generate the aroundBody method
        LazyMethodGen extractedMethod = 
        	extractMethod(
        		NameMangler.aroundCallbackMethodName(
        			getSignature(),
        			getEnclosingClass()),
				Modifier.PRIVATE,
				munger
            );
        			
        			
        // now extract the advice into its own method
        String adviceMethodName =
			NameMangler.aroundCallbackMethodName(
							getSignature(),
							getEnclosingClass()) + "$advice";
        
		List argVarList = new ArrayList();
		List proceedVarList = new ArrayList();
		int extraParamOffset = 0;
		
		// Create the extra parameters that are needed for passing to proceed
		// This code is very similar to that found in makeCallToCallback and should
		// be rationalized in the future
		if (thisVar != null) {
			argVarList.add(thisVar);
			proceedVarList.add(new BcelVar(thisVar.getType(), extraParamOffset));
			extraParamOffset += thisVar.getType().getSize();
		}
		
		if (targetVar != null && targetVar != thisVar) {
			argVarList.add(targetVar);
			proceedVarList.add(new BcelVar(targetVar.getType(), extraParamOffset));
			extraParamOffset += targetVar.getType().getSize();
		}
		for (int i = 0, len = getArgCount(); i < len; i++) {
			argVarList.add(argVars[i]);
			proceedVarList.add(new BcelVar(argVars[i].getType(), extraParamOffset));
			extraParamOffset += argVars[i].getType().getSize();
		}
		if (thisJoinPointVar != null) {
			argVarList.add(thisJoinPointVar);
			proceedVarList.add(new BcelVar(thisJoinPointVar.getType(), extraParamOffset));
			extraParamOffset += thisJoinPointVar.getType().getSize();
		}
        
        Type[] adviceParameterTypes = adviceMethod.getArgumentTypes();
        Type[] extractedMethodParameterTypes = extractedMethod.getArgumentTypes();
		Type[] parameterTypes =
			new Type[extractedMethodParameterTypes.length
				+ adviceParameterTypes.length
				+ 1];
		int parameterIndex = 0;
		System.arraycopy(
			extractedMethodParameterTypes,
			0,
			parameterTypes,
			parameterIndex,
			extractedMethodParameterTypes.length);
		parameterIndex += extractedMethodParameterTypes.length;

		parameterTypes[parameterIndex++] =
			BcelWorld.makeBcelType(adviceMethod.getEnclosingClass().getType());
		System.arraycopy(
			adviceParameterTypes,
			0,
			parameterTypes,
			parameterIndex,
			adviceParameterTypes.length);

        LazyMethodGen localAdviceMethod =
					new LazyMethodGen(
						Modifier.PRIVATE | Modifier.FINAL | Modifier.STATIC, 
						adviceMethod.getReturnType(), 
						adviceMethodName,
						parameterTypes,
						new String[0],
						getEnclosingClass());
 
		String donorFileName = adviceMethod.getEnclosingClass().getInternalFileName();
		String recipientFileName = getEnclosingClass().getInternalFileName();
//		System.err.println("donor " + donorFileName);
//		System.err.println("recip " + recipientFileName);
		if (! donorFileName.equals(recipientFileName)) {
			localAdviceMethod.fromFilename = donorFileName;
			getEnclosingClass().addInlinedSourceFileInfo(
				donorFileName,
				adviceMethod.highestLineNumber);
		}
    
		getEnclosingClass().addMethodGen(localAdviceMethod);
		
		// create a map that will move all slots in advice method forward by extraParamOffset
		// in order to make room for the new proceed-required arguments that are added at
		// the beginning of the parameter list
		int nVars = adviceMethod.getMaxLocals() + extraParamOffset;
		IntMap varMap = IntMap.idMap(nVars);
		for (int i=extraParamOffset; i < nVars; i++) {
			varMap.put(i-extraParamOffset, i);
		}

		localAdviceMethod.getBody().insert(
			BcelClassWeaver.genInlineInstructions(adviceMethod,
					localAdviceMethod, varMap, fact, true));


					
		localAdviceMethod.setMaxLocals(nVars);
					
		//System.err.println(localAdviceMethod);
		
    
    	// the shadow is now empty.  First, create a correct call
    	// to the around advice.  This includes both the call (which may involve 
    	// value conversion of the advice arguments) and the return
    	// (which may involve value conversion of the return value).  Right now
    	// we push a null for the unused closure.  It's sad, but there it is.
    	    	
    	InstructionList advice = new InstructionList();
        // InstructionHandle adviceMethodInvocation;
        {
			for (Iterator i = argVarList.iterator(); i.hasNext(); ) {
				BcelVar var = (BcelVar)i.next();
				var.appendLoad(advice, fact);
			}       	
        	// ??? we don't actually need to push NULL for the closure if we take care
			advice.append(
				munger.getAdviceArgSetup(
					this,
					null,
                    (munger.getConcreteAspect().isAnnotationStyleAspect())?
                        this.loadThisJoinPoint():
					    new InstructionList(InstructionConstants.ACONST_NULL)));
		    // adviceMethodInvocation =
		        advice.append(
		        	Utility.createInvoke(fact, localAdviceMethod)); //(fact, getWorld(), munger.getSignature()));
			advice.append(
		        Utility.createConversion(
		            getFactory(), 
		            BcelWorld.makeBcelType(munger.getSignature().getReturnType()), 
		            extractedMethod.getReturnType()));
		    if (! isFallsThrough()) {
		        advice.append(InstructionFactory.createReturn(extractedMethod.getReturnType()));
		    }
        }
        
		// now, situate the call inside the possible dynamic tests,
		// and actually add the whole mess to the shadow
        if (! hasDynamicTest) {
            range.append(advice);
        } else {
        	InstructionList afterThingie = new InstructionList(InstructionConstants.NOP);
            InstructionList callback = makeCallToCallback(extractedMethod);
			if (terminatesWithReturn()) {
				callback.append(
					InstructionFactory.createReturn(extractedMethod.getReturnType()));
			} else {
				//InstructionHandle endNop = range.insert(fact.NOP, Range.InsideAfter);
				advice.append(
					InstructionFactory.createBranchInstruction(
						Constants.GOTO,
						afterThingie.getStart()));
			}
			range.append(
				munger.getTestInstructions(
					this,
					advice.getStart(),
					callback.getStart(),
					advice.getStart()));
            range.append(advice);
            range.append(callback);
            range.append(afterThingie);          
        }        


        // now search through the advice, looking for a call to PROCEED.  
        // Then we replace the call to proceed with some argument setup, and a 
        // call to the extracted method.

        // inlining support for code style aspects
        if (!munger.getConcreteAspect().isAnnotationStyleAspect()) {
            String proceedName =
                NameMangler.proceedMethodName(munger.getSignature().getName());

            InstructionHandle curr = localAdviceMethod.getBody().getStart();
            InstructionHandle end = localAdviceMethod.getBody().getEnd();
            ConstantPoolGen cpg = localAdviceMethod.getEnclosingClass().getConstantPoolGen();
            while (curr != end) {
                InstructionHandle next = curr.getNext();
                Instruction inst = curr.getInstruction();
                if ((inst instanceof INVOKESTATIC)
                    && proceedName.equals(((INVOKESTATIC) inst).getMethodName(cpg))) {

                    localAdviceMethod.getBody().append(
                        curr,
                        getRedoneProceedCall(
                            fact,
                            extractedMethod,
                            munger,
                            localAdviceMethod,
                            proceedVarList));
                    Utility.deleteInstruction(curr, localAdviceMethod);
                }
                curr = next;
            }
            // and that's it.
        } else {
            //ATAJ inlining support for @AJ aspects
            // [TODO document @AJ code rule: don't manipulate 2 jps proceed at the same time.. in an advice body]
            InstructionHandle curr = localAdviceMethod.getBody().getStart();
            InstructionHandle end = localAdviceMethod.getBody().getEnd();
            ConstantPoolGen cpg = localAdviceMethod.getEnclosingClass().getConstantPoolGen();
            while (curr != end) {
                InstructionHandle next = curr.getNext();
                Instruction inst = curr.getInstruction();
                if ((inst instanceof INVOKEINTERFACE)
                    && "proceed".equals(((INVOKEINTERFACE) inst).getMethodName(cpg))) {
                    final boolean isProceedWithArgs;
                    if (((INVOKEINTERFACE) inst).getArgumentTypes(cpg).length == 1) {
                        // proceed with args as a boxed Object[]
                        isProceedWithArgs = true;
                    } else {
                        isProceedWithArgs = false;
                    }
                    InstructionList insteadProceedIl = getRedoneProceedCallForAnnotationStyle(
                            fact,
                            extractedMethod,
                            munger,
                            localAdviceMethod,
                            proceedVarList,
                            isProceedWithArgs
                    );
                    localAdviceMethod.getBody().append(curr, insteadProceedIl);
                    Utility.deleteInstruction(curr, localAdviceMethod);
                }
                curr = next;
            }
        }
	}

	private InstructionList getRedoneProceedCall(
		InstructionFactory fact,
		LazyMethodGen callbackMethod,
		BcelAdvice munger,
		LazyMethodGen localAdviceMethod,
		List argVarList) 
	{
		InstructionList ret = new InstructionList();
		// we have on stack all the arguments for the ADVICE call.
		// we have in frame somewhere all the arguments for the non-advice call.
		
		BcelVar[] adviceVars = munger.getExposedStateAsBcelVars();		
		IntMap proceedMap =  makeProceedArgumentMap(adviceVars);

//		System.out.println(proceedMap + " for " + this);
//		System.out.println(argVarList);
		
		ResolvedTypeX[] proceedParamTypes =
			world.resolve(munger.getSignature().getParameterTypes());
		// remove this*JoinPoint* as arguments to proceed
		if (munger.getBaseParameterCount()+1 < proceedParamTypes.length) {
			int len = munger.getBaseParameterCount()+1;
			ResolvedTypeX[] newTypes = new ResolvedTypeX[len];
			System.arraycopy(proceedParamTypes, 0, newTypes, 0, len);
			proceedParamTypes = newTypes;
		}

		//System.out.println("stateTypes: " + Arrays.asList(stateTypes));
		BcelVar[] proceedVars =
			Utility.pushAndReturnArrayOfVars(proceedParamTypes, ret, fact, localAdviceMethod);

		Type[] stateTypes = callbackMethod.getArgumentTypes();
//		System.out.println("stateTypes: " + Arrays.asList(stateTypes));
		
		for (int i=0, len=stateTypes.length; i < len; i++) {
            Type stateType = stateTypes[i];
            ResolvedTypeX stateTypeX = BcelWorld.fromBcel(stateType).resolve(world);
            if (proceedMap.hasKey(i)) {
            	//throw new RuntimeException("unimplemented");
				proceedVars[proceedMap.get(i)].appendLoadAndConvert(ret, fact, stateTypeX);
            } else {
				((BcelVar) argVarList.get(i)).appendLoad(ret, fact);
            }
		}
				
		ret.append(Utility.createInvoke(fact, callbackMethod));
		ret.append(Utility.createConversion(fact, callbackMethod.getReturnType(), 
				BcelWorld.makeBcelType(munger.getSignature().getReturnType())));
		return ret;
	}

    /**
     * ATAJ Handle the inlining for @AJ aspects
     *
     * @param fact
     * @param callbackMethod
     * @param munger
     * @param localAdviceMethod
     * @param argVarList
     * @param isProceedWithArgs
     * @return
     */
    private InstructionList getRedoneProceedCallForAnnotationStyle(
        InstructionFactory fact,
        LazyMethodGen callbackMethod,
        BcelAdvice munger,
        LazyMethodGen localAdviceMethod,
        List argVarList,
        boolean isProceedWithArgs)
    {
        // Notes:
        // proceedingjp is on stack (since user was calling pjp.proceed(...)
        // the boxed args to proceed() are on stack as well (Object[]) unless
        // the call is to pjp.proceed(<noarg>)

        // new Object[]{new Integer(argAdvice1-1)};// arg of proceed
        // call to proceed(..) is NOT made
        // instead we do
        // itar callback args i
        //     get from array i, convert it to the callback arg i
        //     if ask for JP, push the one we got on the stack
        // invoke callback, create conversion back to Object/Integer

        // rest of method -- (hence all those conversions)
        // intValue() from original code
        // int res = .. from original code

        //Note: we just don't care about the proceed map etc

        InstructionList ret = new InstructionList();

        // store the Object[] array on stack if proceed with args
        if (isProceedWithArgs) {
            Type objectArrayType = Type.getType("[Ljava/lang/Object;");
            int localProceedArgArray = localAdviceMethod.allocateLocal(objectArrayType);
            ret.append(InstructionFactory.createStore(objectArrayType, localProceedArgArray));

            Type proceedingJpType = Type.getType("Lorg/aspectj/lang/ProceedingJoinPoint;");
            int localJp = localAdviceMethod.allocateLocal(proceedingJpType);
            ret.append(InstructionFactory.createStore(proceedingJpType, localJp));

            // push on stack each element of the object array
            // that is assumed to be consistent with the callback argument (ie munger args)
            // TODO do we want to try catch ClassCast and AOOBE exception ?

            // special logic when withincode is static or not
            int startIndex = 0;
            if (thisVar != null) {
                startIndex = 1;
                //TODO this logic is actually depending on target as well - test me
                ret.append(new ALOAD(0));//thisVar
            }
            for (int i = startIndex, len=callbackMethod.getArgumentTypes().length; i < len; i++) {
                Type stateType = callbackMethod.getArgumentTypes()[i];
                ResolvedTypeX stateTypeX = BcelWorld.fromBcel(stateType).resolve(world);
                if ("Lorg/aspectj/lang/JoinPoint;".equals(stateType.getSignature())) {
                    ret.append(new ALOAD(localJp));// from localAdvice signature
                } else {
                    ret.append(InstructionFactory.createLoad(objectArrayType, localProceedArgArray));
                    ret.append(Utility.createConstant(fact, i-startIndex));
                    ret.append(InstructionFactory.createArrayLoad(Type.OBJECT));
                    ret.append(Utility.createConversion(
                            fact,
                            Type.OBJECT,
                            stateType
                    ));
                }
            }
        } else {
            Type proceedingJpType = Type.getType("Lorg/aspectj/lang/ProceedingJoinPoint;");
            int localJp = localAdviceMethod.allocateLocal(proceedingJpType);
            ret.append(InstructionFactory.createStore(proceedingJpType, localJp));

            for (int i = 0, len=callbackMethod.getArgumentTypes().length; i < len; i++) {
                Type stateType = callbackMethod.getArgumentTypes()[i];
                ResolvedTypeX stateTypeX = BcelWorld.fromBcel(stateType).resolve(world);
                if ("Lorg/aspectj/lang/JoinPoint;".equals(stateType.getSignature())) {
                    ret.append(new ALOAD(localJp));// from localAdvice signature
//                } else if ("Lorg/aspectj/lang/ProceedingJoinPoint;".equals(stateType.getSignature())) {
//                    //FIXME ALEX?
//                        ret.append(new ALOAD(localJp));// from localAdvice signature
////                        ret.append(fact.createCheckCast(
////                                (ReferenceType) BcelWorld.makeBcelType(stateTypeX)
////                    ));
//                        // cast ?
//
                } else {
                    ret.append(InstructionFactory.createLoad(stateType, i));
                }
            }
        }

        // do the callback invoke
        ret.append(Utility.createInvoke(fact, callbackMethod));

        // box it again. Handles cases where around advice does return something else than Object
        if (!TypeX.OBJECT.equals(munger.getSignature().getReturnType())) {
            ret.append(Utility.createConversion(
                    fact,
                    callbackMethod.getReturnType(),
                    Type.OBJECT
            ));
        }
        ret.append(Utility.createConversion(
                fact,
                callbackMethod.getReturnType(),
                BcelWorld.makeBcelType(munger.getSignature().getReturnType())
        ));

        return ret;

//
//
//
//            if (proceedMap.hasKey(i)) {
//                ret.append(new ALOAD(i));
//                //throw new RuntimeException("unimplemented");
//                //proceedVars[proceedMap.get(i)].appendLoadAndConvert(ret, fact, stateTypeX);
//            } else {
//                //((BcelVar) argVarList.get(i)).appendLoad(ret, fact);
//                //ret.append(new ALOAD(i));
//                if ("Lorg/aspectj/lang/JoinPoint;".equals(stateType.getSignature())) {
//                    ret.append(new ALOAD(i));
//                } else {
//                    ret.append(new ALOAD(i));
//                }
//            }
//        }
//
//        ret.append(Utility.createInvoke(fact, callbackMethod));
//        ret.append(Utility.createConversion(fact, callbackMethod.getReturnType(),
//                BcelWorld.makeBcelType(munger.getSignature().getReturnType())));
//
//        //ret.append(new ACONST_NULL());//will be POPed
//        if (true) return ret;
//
//
//
//        // we have on stack all the arguments for the ADVICE call.
//        // we have in frame somewhere all the arguments for the non-advice call.
//
//        BcelVar[] adviceVars = munger.getExposedStateAsBcelVars();
//        IntMap proceedMap =  makeProceedArgumentMap(adviceVars);
//
//        System.out.println(proceedMap + " for " + this);
//        System.out.println(argVarList);
//
//        ResolvedTypeX[] proceedParamTypes =
//            world.resolve(munger.getSignature().getParameterTypes());
//        // remove this*JoinPoint* as arguments to proceed
//        if (munger.getBaseParameterCount()+1 < proceedParamTypes.length) {
//            int len = munger.getBaseParameterCount()+1;
//            ResolvedTypeX[] newTypes = new ResolvedTypeX[len];
//            System.arraycopy(proceedParamTypes, 0, newTypes, 0, len);
//            proceedParamTypes = newTypes;
//        }
//
//        //System.out.println("stateTypes: " + Arrays.asList(stateTypes));
//        BcelVar[] proceedVars =
//            Utility.pushAndReturnArrayOfVars(proceedParamTypes, ret, fact, localAdviceMethod);
//
//        Type[] stateTypes = callbackMethod.getArgumentTypes();
////		System.out.println("stateTypes: " + Arrays.asList(stateTypes));
//
//        for (int i=0, len=stateTypes.length; i < len; i++) {
//            Type stateType = stateTypes[i];
//            ResolvedTypeX stateTypeX = BcelWorld.fromBcel(stateType).resolve(world);
//            if (proceedMap.hasKey(i)) {
//                //throw new RuntimeException("unimplemented");
//                proceedVars[proceedMap.get(i)].appendLoadAndConvert(ret, fact, stateTypeX);
//            } else {
//                ((BcelVar) argVarList.get(i)).appendLoad(ret, fact);
//            }
//        }
//
//        ret.append(Utility.createInvoke(fact, callbackMethod));
//        ret.append(Utility.createConversion(fact, callbackMethod.getReturnType(),
//                BcelWorld.makeBcelType(munger.getSignature().getReturnType())));
//        return ret;
    }

    public void weaveAroundClosure(BcelAdvice munger, boolean hasDynamicTest) {
    	InstructionFactory fact = getFactory();

		enclosingMethod.setCanInline(false);

        // MOVE OUT ALL THE INSTRUCTIONS IN MY SHADOW INTO ANOTHER METHOD!
        LazyMethodGen callbackMethod = 
        	extractMethod(
        		NameMangler.aroundCallbackMethodName(
        			getSignature(),
        			getEnclosingClass()),
        		    0,
        		    munger);
        			    
    	BcelVar[] adviceVars = munger.getExposedStateAsBcelVars();
    	
    	String closureClassName = 
    		NameMangler.makeClosureClassName(
    			getEnclosingClass().getType(),
    			getEnclosingClass().getNewGeneratedNameTag());
    			
    	Member constructorSig = new Member(Member.CONSTRUCTOR, 
    								TypeX.forName(closureClassName), 0, "<init>", 
    								"([Ljava/lang/Object;)V");
    	
    	BcelVar closureHolder = null;
    	
    	// This is not being used currently since getKind() == preinitializaiton
    	// cannot happen in around advice
    	if (getKind() == PreInitialization) {
    		closureHolder = genTempVar(AjcMemberMaker.AROUND_CLOSURE_TYPE);
    	}
    	
        InstructionList closureInstantiation =
            makeClosureInstantiation(constructorSig, closureHolder);

        /*LazyMethodGen constructor = */ 
            makeClosureClassAndReturnConstructor(
            	closureClassName,
                callbackMethod, 
                makeProceedArgumentMap(adviceVars)
                );

        InstructionList returnConversionCode;
		if (getKind() == PreInitialization) {
			returnConversionCode = new InstructionList();
			
			BcelVar stateTempVar = genTempVar(TypeX.OBJECTARRAY);
			closureHolder.appendLoad(returnConversionCode, fact);
			
			returnConversionCode.append(
				Utility.createInvoke(
					fact, 
					world, 
					AjcMemberMaker.aroundClosurePreInitializationGetter()));
			stateTempVar.appendStore(returnConversionCode, fact);
			
			Type[] stateTypes = getSuperConstructorParameterTypes();
			
			returnConversionCode.append(InstructionConstants.ALOAD_0); // put "this" back on the stack
			for (int i = 0, len = stateTypes.length; i < len; i++) {
                TypeX bcelTX = BcelWorld.fromBcel(stateTypes[i]);
                ResolvedTypeX stateRTX = world.resolve(bcelTX,true);
                if (stateRTX == ResolvedTypeX.MISSING) {
                    IMessage msg = new Message(
                             WeaverMessages.format(WeaverMessages.CANT_FIND_TYPE_DURING_AROUND_WEAVE_PREINIT,bcelTX.getClassName()),
                              "",IMessage.ERROR,getSourceLocation(),null,
                              new ISourceLocation[]{ munger.getSourceLocation()});
                    world.getMessageHandler().handleMessage(msg);
                }
				stateTempVar.appendConvertableArrayLoad(
					returnConversionCode, 
					fact, 
					i, 
					stateRTX);
			}
		} else {
	        returnConversionCode = 
	            Utility.createConversion(
	                getFactory(), 
	                BcelWorld.makeBcelType(munger.getSignature().getReturnType()), 
	                callbackMethod.getReturnType());
			if (!isFallsThrough()) {
				returnConversionCode.append(
					InstructionFactory.createReturn(callbackMethod.getReturnType()));
			}
		}

        // ATAJ for @AJ aspect we need to link the closure with the joinpoint instance
        if (munger.getConcreteAspect().isAnnotationStyleAspect()) {
            closureInstantiation.append(Utility.createInvoke(
                    getFactory(),
                    getWorld(),
                    new Member(
                            Member.METHOD,
                            TypeX.forName("org.aspectj.runtime.internal.AroundClosure"),
                            Modifier.PUBLIC,
                            "linkClosureAndJoinPoint",
                            "()Lorg/aspectj/lang/ProceedingJoinPoint;"
                            )
            ));
        }

        InstructionList advice = new InstructionList();
        advice.append(munger.getAdviceArgSetup(this, null, closureInstantiation));

        // invoke the advice
        advice.append(munger.getNonTestAdviceInstructions(this));
        advice.append(returnConversionCode);
        
		if (!hasDynamicTest) {
			range.append(advice);
		} else {
			InstructionList callback = makeCallToCallback(callbackMethod);
			InstructionList postCallback = new InstructionList();
			if (terminatesWithReturn()) {
				callback.append(
					InstructionFactory.createReturn(callbackMethod.getReturnType()));
			} else {
				advice.append(
					InstructionFactory.createBranchInstruction(
						Constants.GOTO,
						postCallback.append(InstructionConstants.NOP)));
			}
			range.append(
				munger.getTestInstructions(
					this,
					advice.getStart(),
					callback.getStart(),
					advice.getStart()));
			range.append(advice);
			range.append(callback);
			range.append(postCallback);
		}
    }
    
    // exposed for testing
    InstructionList makeCallToCallback(LazyMethodGen callbackMethod) {
    	InstructionFactory fact = getFactory();
        InstructionList callback = new InstructionList();
        if (thisVar != null) {
        	callback.append(InstructionConstants.ALOAD_0); 
        }
        if (targetVar != null && targetVar != thisVar) {
            callback.append(BcelRenderer.renderExpr(fact, world, targetVar));
        }
        callback.append(BcelRenderer.renderExprs(fact, world, argVars));
        // remember to render tjps
        if (thisJoinPointVar != null) {
        	callback.append(BcelRenderer.renderExpr(fact, world, thisJoinPointVar));
        }
        callback.append(Utility.createInvoke(fact, callbackMethod));
        return callback;
    }

	/** side-effect-free */
    private InstructionList makeClosureInstantiation(Member constructor, BcelVar holder) {
    
//    LazyMethodGen constructor) {
    	InstructionFactory fact = getFactory();
        BcelVar arrayVar = genTempVar(TypeX.OBJECTARRAY);
        //final Type objectArrayType = new ArrayType(Type.OBJECT, 1);
        final InstructionList il = new InstructionList();
        int alen = getArgCount() + (thisVar == null ? 0 : 1) + 
        			((targetVar != null && targetVar != thisVar) ? 1 : 0) + 
        			(thisJoinPointVar == null ? 0 : 1);
        il.append(Utility.createConstant(fact, alen));
        il.append((Instruction)fact.createNewArray(Type.OBJECT, (short)1));
        arrayVar.appendStore(il, fact);

        int stateIndex = 0;     
        if (thisVar != null) {
            arrayVar.appendConvertableArrayStore(il, fact, stateIndex, thisVar);
			thisVar.setPositionInAroundState(stateIndex);
            stateIndex++;
        }       	
        if (targetVar != null && targetVar != thisVar) {
            arrayVar.appendConvertableArrayStore(il, fact, stateIndex, targetVar);
			targetVar.setPositionInAroundState(stateIndex);
            stateIndex++;
        }       
        for (int i = 0, len = getArgCount(); i<len; i++) {
            arrayVar.appendConvertableArrayStore(il, fact, stateIndex, argVars[i]);
            argVars[i].setPositionInAroundState(stateIndex);
            stateIndex++;
        }
        if (thisJoinPointVar != null) {
        	arrayVar.appendConvertableArrayStore(il, fact, stateIndex, thisJoinPointVar);
        	thisJoinPointVar.setPositionInAroundState(stateIndex);
        	stateIndex++;
        }
        il.append(fact.createNew(new ObjectType(constructor.getDeclaringType().getName())));
        il.append(new DUP());
        arrayVar.appendLoad(il, fact);
        il.append(Utility.createInvoke(fact, world, constructor));
        if (getKind() == PreInitialization) {
			il.append(InstructionConstants.DUP);
			holder.appendStore(il, fact);
        }
        return il;
    }
    

    private IntMap makeProceedArgumentMap(BcelVar[] adviceArgs) {
        //System.err.println("coming in with " + Arrays.asList(adviceArgs));

        IntMap ret = new IntMap();
        for(int i = 0, len = adviceArgs.length; i < len; i++) {
            BcelVar v = (BcelVar) adviceArgs[i];
            if (v == null) continue; // XXX we don't know why this is required
            int pos = v.getPositionInAroundState();
            if (pos >= 0) {  // need this test to avoid args bound via cflow
            	ret.put(pos, i);
            }
        }
        //System.err.println("returning " + ret);
        
        return ret;
    }

	/**
	 * 
	 * 
	 * @param callbackMethod the method we will call back to when our run method gets called.
	 * 
	 * @param proceedMap A map from state position to proceed argument position.  May be
	 *     non covering on state position.
	 */

	private LazyMethodGen makeClosureClassAndReturnConstructor(
		String closureClassName,
        LazyMethodGen callbackMethod, 
        IntMap proceedMap)
    {
		String superClassName = "org.aspectj.runtime.internal.AroundClosure";
        Type objectArrayType = new ArrayType(Type.OBJECT, 1);
        
		LazyClassGen closureClass = new LazyClassGen(closureClassName,
			                                     superClassName, 
                                                 getEnclosingClass().getFileName(),
			                                     Modifier.PUBLIC,
			                                     new String[] {});
        InstructionFactory fact = new InstructionFactory(closureClass.getConstantPoolGen());
        				
        // constructor
        LazyMethodGen constructor = new LazyMethodGen(Modifier.PUBLIC, 
                                                      Type.VOID, 
                                                      "<init>", 
                                                      new Type[] {objectArrayType},
                                                      new String[] {}, 
                                                      closureClass);        
        InstructionList cbody = constructor.getBody();        
		cbody.append(InstructionFactory.createLoad(Type.OBJECT, 0));
		cbody.append(InstructionFactory.createLoad(objectArrayType, 1));
		cbody.append(fact.createInvoke(superClassName, "<init>", Type.VOID, 
			new Type[] {objectArrayType}, Constants.INVOKESPECIAL));
		cbody.append(InstructionFactory.createReturn(Type.VOID));

        closureClass.addMethodGen(constructor);
        
        // method		
        LazyMethodGen runMethod = new LazyMethodGen(Modifier.PUBLIC, 
                                        Type.OBJECT, 
                                        "run", 
                                        new Type[] {objectArrayType}, 
                                        new String[] {},
                                        closureClass); 
        InstructionList mbody = runMethod.getBody();
        BcelVar proceedVar = new BcelVar(TypeX.OBJECTARRAY.resolve(world), 1);
    //        int proceedVarIndex = 1;
		BcelVar stateVar =
			new BcelVar(TypeX.OBJECTARRAY.resolve(world), runMethod.allocateLocal(1));
    //        int stateVarIndex = runMethod.allocateLocal(1);
		mbody.append(InstructionFactory.createThis());
		mbody.append(fact.createGetField(superClassName, "state", objectArrayType));
        mbody.append(stateVar.createStore(fact));
    //		mbody.append(fact.createStore(objectArrayType, stateVarIndex));
		
        Type[] stateTypes = callbackMethod.getArgumentTypes();
        		
		for (int i=0, len=stateTypes.length; i < len; i++) {
            Type stateType = stateTypes[i];
            ResolvedTypeX stateTypeX = BcelWorld.fromBcel(stateType).resolve(world);
            if (proceedMap.hasKey(i)) {
                mbody.append(
                    proceedVar.createConvertableArrayLoad(fact, proceedMap.get(i),
                        stateTypeX));
            } else {
                mbody.append(
                    stateVar.createConvertableArrayLoad(fact, i, 
                        stateTypeX));
            }
		}
		
		
		mbody.append(Utility.createInvoke(fact, callbackMethod));
		
		if (getKind() == PreInitialization) {
			mbody.append(Utility.createSet(
				fact, 
				AjcMemberMaker.aroundClosurePreInitializationField()));
			mbody.append(InstructionConstants.ACONST_NULL);
		} else {
			mbody.append(
	            Utility.createConversion(
	                fact, 
	                callbackMethod.getReturnType(), 
	                Type.OBJECT));
		}
		mbody.append(InstructionFactory.createReturn(Type.OBJECT));

		closureClass.addMethodGen(runMethod);
				
		// class
		getEnclosingClass().addGeneratedInner(closureClass);
		
		return constructor;
	}
    
    // ---- extraction methods
    

    public LazyMethodGen extractMethod(String newMethodName, int visibilityModifier, ShadowMunger munger) {
		LazyMethodGen.assertGoodBody(range.getBody(), newMethodName);
        if (!getKind().allowsExtraction()) throw new BCException();
        LazyMethodGen freshMethod = createMethodGen(newMethodName,visibilityModifier);

//        System.err.println("******");
//        System.err.println("ABOUT TO EXTRACT METHOD for" + this);
//        enclosingMethod.print(System.err);
//        System.err.println("INTO");
//        freshMethod.print(System.err);
//        System.err.println("WITH REMAP");
//        System.err.println(makeRemap());
        
        range.extractInstructionsInto(freshMethod, makeRemap(), 
        	(getKind() != PreInitialization) && 
	        isFallsThrough());
   		if (getKind() == PreInitialization) {
			addPreInitializationReturnCode(
				freshMethod,
				getSuperConstructorParameterTypes());
		}
        getEnclosingClass().addMethodGen(freshMethod,munger.getSourceLocation());
        
        return freshMethod;
    }

	private void addPreInitializationReturnCode(
		LazyMethodGen extractedMethod,
		Type[] superConstructorTypes) 
	{
		InstructionList body = extractedMethod.getBody();
		final InstructionFactory fact = getFactory();	
		
		BcelVar arrayVar = new BcelVar(
			world.getCoreType(TypeX.OBJECTARRAY),
			extractedMethod.allocateLocal(1));
		
		int len = superConstructorTypes.length;
		
        body.append(Utility.createConstant(fact, len));

        body.append((Instruction)fact.createNewArray(Type.OBJECT, (short)1));
        arrayVar.appendStore(body, fact);

        for (int i = len - 1; i >= 0; i++) {
        	// convert thing on top of stack to object
			body.append(
				Utility.createConversion(fact, superConstructorTypes[i], Type.OBJECT));
			// push object array
			arrayVar.appendLoad(body, fact);
			// swap
			body.append(InstructionConstants.SWAP);
        	// do object array store.
			body.append(Utility.createConstant(fact, i));
			body.append(InstructionConstants.SWAP);
			body.append(InstructionFactory.createArrayStore(Type.OBJECT));
        }
        arrayVar.appendLoad(body, fact);
        body.append(InstructionConstants.ARETURN);
	}

	private Type[] getSuperConstructorParameterTypes() {
		// assert getKind() == PreInitialization	
		InstructionHandle superCallHandle = getRange().getEnd().getNext();
		InvokeInstruction superCallInstruction = 
			(InvokeInstruction) superCallHandle.getInstruction();
		return superCallInstruction.getArgumentTypes(
			getEnclosingClass().getConstantPoolGen());
	}


    /** make a map from old frame location to new frame location.  Any unkeyed frame
     * location picks out a copied local */
    private IntMap makeRemap() {
        IntMap ret = new IntMap(5);
        int reti = 0;
		if (thisVar != null) {
			ret.put(0, reti++);  // thisVar guaranteed to be 0
		}
        if (targetVar != null && targetVar != thisVar) {
            ret.put(targetVar.getSlot(), reti++);
        }            
        for (int i = 0, len = argVars.length; i < len; i++) {
            ret.put(argVars[i].getSlot(), reti);
            reti += argVars[i].getType().getSize();            
        }
        if (thisJoinPointVar != null) {
        	ret.put(thisJoinPointVar.getSlot(), reti++);
        }
        // we not only need to put the arguments, we also need to remap their 
        // aliases, which we so helpfully put into temps at the beginning of this join 
        // point.
        if (! getKind().argsOnStack()) {
        	int oldi = 0;
        	int newi = 0;
        	// if we're passing in a this and we're not argsOnStack we're always 
        	// passing in a target too
	        if (arg0HoldsThis()) { ret.put(0, 0); oldi++; newi+=1; }
	        //assert targetVar == thisVar
	        for (int i = 0; i < getArgCount(); i++) {
	            TypeX type = getArgType(i); 
				ret.put(oldi, newi);
	            oldi += type.getSize();
	            newi += type.getSize();
	        }   
        }      
        
//        System.err.println("making remap for : " + this);
//        if (targetVar != null) System.err.println("target slot : " + targetVar.getSlot());
//        if (thisVar != null) System.err.println("  this slot : " + thisVar.getSlot());
//        System.err.println(ret);
        
        return ret;
    }

    /**
     * The new method always static.
     * It may take some extra arguments:  this, target.
     * If it's argsOnStack, then it must take both this/target
     * If it's argsOnFrame, it shares this and target.
     * ??? rewrite this to do less array munging, please
     */
    private LazyMethodGen createMethodGen(String newMethodName, int visibilityModifier) {
        Type[] parameterTypes = BcelWorld.makeBcelTypes(getArgTypes()); 
        int modifiers = Modifier.FINAL | visibilityModifier;

        // XXX some bug
//        if (! isExpressionKind() && getSignature().isStrict(world)) {
//            modifiers |= Modifier.STRICT;
//        }
        modifiers |= Modifier.STATIC;
        if (targetVar != null && targetVar != thisVar) {
            TypeX targetType = getTargetType();
            targetType = ensureTargetTypeIsCorrect(targetType);
            ResolvedMember resolvedMember = getSignature().resolve(world);
            
            if (resolvedMember != null && Modifier.isProtected(resolvedMember.getModifiers()) && 
            	!samePackage(targetType.getPackageName(), getEnclosingType().getPackageName()) &&
				!resolvedMember.getName().equals("clone"))
            {
            	if (!targetType.isAssignableFrom(getThisType(), world)) {
            		throw new BCException("bad bytecode");
            	}
            	targetType = getThisType();
            }
            parameterTypes = addType(BcelWorld.makeBcelType(targetType), parameterTypes);
        }
        if (thisVar != null) {
        	TypeX thisType = getThisType();
        	parameterTypes = addType(BcelWorld.makeBcelType(thisType), parameterTypes);
        }
        
        // We always want to pass down thisJoinPoint in case we have already woven
        // some advice in here.  If we only have a single piece of around advice on a
        // join point, it is unnecessary to accept (and pass) tjp.
        if (thisJoinPointVar != null) {
        	parameterTypes = addTypeToEnd(LazyClassGen.tjpType, parameterTypes);
            //FIXME ALEX? which one            
            //parameterTypes = addTypeToEnd(LazyClassGen.proceedingTjpType, parameterTypes);
        }
        
        TypeX returnType;
        if (getKind() == PreInitialization) {
        	returnType = TypeX.OBJECTARRAY;
        } else {
        	returnType = getReturnType();
        }
        return
            new LazyMethodGen(
                modifiers, 
                BcelWorld.makeBcelType(returnType), 
                newMethodName,
                parameterTypes,
                new String[0],
    // XXX again, we need to look up methods!
//                TypeX.getNames(getSignature().getExceptions(world)),
                getEnclosingClass());
    }

	private boolean samePackage(String p1, String p2) {
		if (p1 == null) return p2 == null;
		if (p2 == null) return false;
		return p1.equals(p2);
	}

   
    private Type[] addType(Type type, Type[] types) {
        int len = types.length;
        Type[] ret = new Type[len+1];
        ret[0] = type;
        System.arraycopy(types, 0, ret, 1, len);
        return ret;
    }
    
    private Type[] addTypeToEnd(Type type, Type[] types) {
        int len = types.length;
        Type[] ret = new Type[len+1];
        ret[len] = type;
        System.arraycopy(types, 0, ret, 0, len);
        return ret;
    }
    
    public BcelVar genTempVar(TypeX typeX) {
    	return new BcelVar(typeX.resolve(world), genTempVarIndex(typeX.getSize()));
    }

//	public static final boolean CREATE_TEMP_NAMES = true;

    public BcelVar genTempVar(TypeX typeX, String localName) {
		BcelVar tv = genTempVar(typeX);

//		if (CREATE_TEMP_NAMES) {
//			for (InstructionHandle ih = range.getStart(); ih != range.getEnd(); ih = ih.getNext()) {
//				if (Range.isRangeHandle(ih)) continue;
//				ih.addTargeter(new LocalVariableTag(typeX, localName, tv.getSlot()));
//			}
//		}
		return tv;
    }
    
    // eh doesn't think we need to garbage collect these (64K is a big number...)
    private int genTempVarIndex(int size) {
        return enclosingMethod.allocateLocal(size);
    }
   
    public InstructionFactory getFactory() {
        return getEnclosingClass().getFactory();
    }
    
	public ISourceLocation getSourceLocation() {
		int sourceLine = getSourceLine();
		if (sourceLine == 0 || sourceLine == -1) {
//			Thread.currentThread().dumpStack();
//			System.err.println(this + ": " + range);
			return getEnclosingClass().getType().getSourceLocation();
		} else {
		    // For staticinitialization, if we have a nice offset, don't build a new source loc
			if (getKind()==Shadow.StaticInitialization && getEnclosingClass().getType().getSourceLocation().getOffset()!=0)
				return getEnclosingClass().getType().getSourceLocation();
			else 
			    return getEnclosingClass().getType().getSourceContext().makeSourceLocation(sourceLine);
		}
	}

	public Shadow getEnclosingShadow() {
		return enclosingShadow;
	}

	public LazyMethodGen getEnclosingMethod() {
		return enclosingMethod;
	}

	public boolean isFallsThrough() {
		return !terminatesWithReturn(); //fallsThrough;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10088.java