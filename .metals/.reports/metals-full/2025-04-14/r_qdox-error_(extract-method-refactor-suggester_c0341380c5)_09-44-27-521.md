error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1405.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1405.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1405.java
text:
```scala
T@@estUtil.assertMultiLineStringEquals(expectedFile/*"classes"*/,

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
import java.io.*;
import java.util.*;

import junit.framework.*;

import org.apache.bcel.Constants;
import org.apache.bcel.generic.*;
import org.aspectj.weaver.*;
import org.aspectj.weaver.patterns.*;
import org.aspectj.testing.util.TestUtil;
import org.aspectj.util.FileUtil;

public abstract class WeaveTestCase extends TestCase {

	public boolean regenerate = false;
	public boolean runTests = true;

    File outDir;
    String outDirPath;
    	
	public BcelWorld world = new BcelWorld();

    public WeaveTestCase(String name) {
        super(name);
    }
    
    public void setUp() {
        outDir = BcweaverTests.getOutdir();
        outDirPath = outDir.getAbsolutePath();
    }
    public void tearDown() {
        BcweaverTests.removeOutDir();
        outDir = null;
        outDirPath = null;
    }

	public static InstructionList getAdviceTag(BcelShadow shadow, String where) {
		String methodName =
			"ajc_" + where + "_" + shadow.getKind().toLegalJavaIdentifier();

		InstructionFactory fact = shadow.getFactory();
		InvokeInstruction il =
			fact.createInvoke("Aspect", methodName, Type.VOID, new Type[] {
		}, Constants.INVOKESTATIC);
        return new InstructionList(il);
	}
    
	public void weaveTest(String name, String outName, ShadowMunger planner) throws IOException {
        List l = new ArrayList(1);
        l.add(planner);
        weaveTest(name, outName, l);
    }
    
    //static String classDir = "../weaver/bin";
	static String classDir = BcweaverTests.TESTDATA_PATH + File.separator + "bin";
    
    
	public void weaveTest(String name, String outName, List planners) throws IOException {
        BcelWeaver weaver = new BcelWeaver(world);
        
        UnwovenClassFile classFile = makeUnwovenClassFile(classDir, name, outDirPath); 
        
        weaver.addClassFile(classFile);
        weaver.setShadowMungers(planners);
        weaveTestInner(weaver, classFile, name, outName);
	}
        
        
	protected void weaveTestInner(
		BcelWeaver weaver,
		UnwovenClassFile classFile,
		String name,
		String outName)
		throws IOException 
	{
		//int preErrors = currentResult.errorCount();
		BcelObjectType classType =
			BcelWorld.getBcelObjectType(world.resolve(classFile.getClassName()));
		LazyClassGen gen = weaver.weave(classFile, classType);
		if (gen == null) {
			// we didn't do any weaving, but let's make a gen anyway
			gen = classType.getLazyClassGen(); //new LazyClassGen(classType);
		}
		try {
			checkClass(gen, outDirPath, outName + ".txt");
			if (runTests) {
				System.out.println(
					"*******RUNNING: " + outName + "  " + name + " *******");
                TestUtil.runMain(makeClassPath(outDirPath), name);
			}
		} catch (Error e) {
			gen.print(System.err);
			throw e;
		} catch (RuntimeException e) {
			gen.print(System.err);
			throw e;
		}
	}
   	
   	public String makeClassPath(String outDir) {
   		return outDir
			+ File.pathSeparator
			+ getTraceJar() 
			+ File.pathSeparator
			+ System.getProperty("java.class.path");
   	}
   	

	/** '/' in the name indicates the location of the class
	 */
	public static UnwovenClassFile makeUnwovenClassFile(
		String classDir,
		String name,
		String outDir) throws IOException {
		File outFile = new File(outDir, name+".class");
		if (classDir.endsWith(".jar")) {
			String fname = name+".class";
			UnwovenClassFile ret =
				 new UnwovenClassFile(outFile.getAbsolutePath(), 
				 	FileUtil.readAsByteArray(FileUtil.getStreamFromZip(classDir, fname)));
		    return ret;
		} else {
			File inFile = new File(classDir, name+".class");
			return new UnwovenClassFile(outFile.getAbsolutePath(), FileUtil.readAsByteArray(inFile));
		}
	}

    public void checkClass(LazyClassGen gen, String outDir, String expectedFile) throws IOException {
        if (regenerate) genClass(gen, outDir, expectedFile);
        else realCheckClass(gen, outDir, expectedFile);
    }
    static final File TESTDATA_DIR = new File(BcweaverTests.TESTDATA_PATH);    				
    void genClass(LazyClassGen gen, String outDir, String expectedFile) throws IOException {
    	//ClassGen b = getJavaClass(outDir, className);
    	FileOutputStream out = new FileOutputStream(new File(TESTDATA_DIR, expectedFile));
    	PrintStream ps = new PrintStream(out);
    	gen.print(ps);
    	ps.flush();
				
    }

    void realCheckClass(LazyClassGen gen, String outDir, String expectedFile) throws IOException {
    	TestUtil.assertMultiLineStringEquals("classes", 
    	             FileUtil.readAsString(new File(TESTDATA_DIR, expectedFile)),
    	             gen.toLongString());
    }


	// ----
    public ShadowMunger makeConcreteAdvice(String mungerString) {
    	return makeConcreteAdvice(mungerString, 0, null);
    }

    public ShadowMunger makeConcreteAdvice(String mungerString, int extraArgFlag) {
		return makeConcreteAdvice(mungerString, extraArgFlag, null);
    }

    protected ShadowMunger makeConcreteAdvice(String mungerString, int extraArgFlag, PerClause perClause) {
        Advice myMunger = 
            world.shadowMunger(mungerString, extraArgFlag);
            
//        PerSingleton s = new PerSingleton();
//        s.concretize(world.resolve("Aspect"));
        //System.err.println(((KindedPointcut)myMunger.getPointcut().getPointcut()).getKind());
        Advice cm = (Advice) myMunger.concretize(myMunger.getDeclaringAspect().resolve(world), 
        						world, perClause);
        return cm;
    }

    public ShadowMunger makeAdviceField(String kind, String extraArgType) {
        return makeConcreteAdvice(
            kind
                + "(): get(* *.*) -> static void Aspect.ajc_"
                + kind
                + "_field_get("
                + extraArgType
                + ")",
            1);
    }

    public List makeAdviceAll(String kind, boolean matchOnlyPrintln) {
        List ret = new ArrayList();
        if (matchOnlyPrintln) {
            ret.add(
                makeConcreteAdvice(
                    kind
                        + "(): call(* *.println(..)) -> static void Aspect.ajc_"
                        + kind
                        + "_method_execution()"));
        } else {
            ret.add(
                makeConcreteAdvice(
                    kind
                        + "(): call(* *.*(..)) -> static void Aspect.ajc_"
                        + kind
                        + "_method_call()"));
            ret.add(
                makeConcreteAdvice(
                    kind
                        + "(): call(*.new(..)) -> static void Aspect.ajc_"
                        + kind
                        + "_constructor_call()"));
            ret.add(
                makeConcreteAdvice(
                    kind
                        + "(): execution(* *.*(..)) -> static void Aspect.ajc_"
                        + kind
                        + "_method_execution()"));
            ret.add(
                makeConcreteAdvice(
                    kind
                        + "(): execution(*.new(..)) -> static void Aspect.ajc_"
                        + kind
                        + "_constructor_execution()"));
//            ret.add(
//                makeConcreteMunger(
//                    kind
//                        + "(): staticinitialization(*) -> static void Aspect.ajc_"
//                        + kind
//                        + "_staticinitialization()"));
            ret.add(
                makeConcreteAdvice(
                    kind + "(): get(* *.*) -> static void Aspect.ajc_" + kind + "_field_get()"));
//            ret.add(
//                makeConcreteMunger(
//                    kind + "(): set(* *.*) -> static void Aspect.ajc_" + kind + "_field_set()"));
			// XXX no test for advice execution, staticInitialization or (god help us) preInitialization
        }
        return ret;
    }
    
    public List makeAdviceAll(final String kind) {
        return makeAdviceAll(kind, false);
    }

	public Pointcut makePointcutAll() {
		return makeConcretePointcut("get(* *.*) || call(* *.*(..)) || execution(* *.*(..)) || call(*.new(..)) || execution(*.new(..))");
	}
	public Pointcut makePointcutNoZeroArg() {
		return makeConcretePointcut("call(* *.*(*, ..)) || execution(* *.*(*, ..)) || call(*.new(*, ..)) || execution(*.new(*, ..))");
	}

	public Pointcut makePointcutPrintln() {
		return makeConcretePointcut("call(* *.println(..))");
	}
	
	
	public Pointcut makeConcretePointcut(String s) {
		return makeResolvedPointcut(s).concretize(null, 0);
	}
	
	public Pointcut makeResolvedPointcut(String s) {
		Pointcut pointcut0 = Pointcut.fromString(s);
		return pointcut0.resolve(new SimpleScope(world, FormalBinding.NONE));
	}


	// ----

	public String[] getStandardTargets() {
		return new String[] {"HelloWorld", "FancyHelloWorld"};
	}

	public String getTraceJar() {
		return BcweaverTests.TESTDATA_PATH + "/tracing.jar";
	}

	// ----

	protected void weaveTest(
    		String[] inClassNames,
    		String outKind,
    		ShadowMunger patternMunger) throws IOException {
		for (int i = 0; i < inClassNames.length; i++) {
			String inFileName = inClassNames[i];
			weaveTest(inFileName, outKind + inFileName, patternMunger);
		}
	}
    protected void weaveTest(
            String[] inClassNames,
            String outKind,
            List patternMungers) throws IOException {
        for (int i = 0; i < inClassNames.length; i++) {
            String inFileName = inClassNames[i];
            weaveTest(inFileName, outKind + inFileName, patternMungers);
        }
    }

	protected List addLexicalOrder(List l) {
		int i = 10;
		for (Iterator iter = l.iterator(); iter.hasNext();) {
			Advice element = (Advice) iter.next();
			element.setLexicalPosition(i+=10);
		}
		return l;
	}

	//XXX cut-and-paster from IdWeaveTestCase
    public void checkShadowSet(List l, String[] ss) {
    	outer:
    	for (int i = 0, len = ss.length; i < len; i++) {
    		inner:
    		for (Iterator j = l.iterator(); j.hasNext(); ) {
    			BcelShadow shadow = (BcelShadow) j.next();
    			String shadowString = shadow.toString();
    			if (shadowString.equals(ss[i])) {
    				j.remove();
    				continue outer;
    			}
    		}
    		assertTrue("didn't find " + ss[i] + " in " + l, false);
    	}
    	assertTrue("too many things in " + l, l.size() ==  0);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1405.java