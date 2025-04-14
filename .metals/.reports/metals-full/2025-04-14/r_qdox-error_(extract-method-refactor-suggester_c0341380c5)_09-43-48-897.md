error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11159.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11159.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11159.java
text:
```scala
b@@oolean matches = p.matches(member, world,false);

/* *******************************************************************
 * Copyright (c) 2002 Palo Alto Research Center, Incorporated (PARC).
 *               2005 Contributors
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

import java.io.*;

import junit.framework.*;

import org.aspectj.weaver.*;
import org.aspectj.weaver.bcel.*;

public class SignaturePatternTestCase extends TestCase {		
	/**
	 * Constructor for PatternTestCase.
	 * @param name
	 */
	public SignaturePatternTestCase(String name) {
		super(name);
	}
	
	BcelWorld world = new BcelWorld();

	public void testThrowsMatch() throws IOException {
		Member onlyDerivedOnDerived = MemberImpl.methodFromString("static void fluffy.Derived.onlyDerived()");
		Member mOnBase = MemberImpl.methodFromString("void fluffy.Base.m()");
		Member mOnDerived = MemberImpl.methodFromString("void fluffy.Derived.m()");
		
		checkMatch(makeMethodPat("* fluffy.Base.*(..) throws java.lang.CloneNotSupportedException"),
					new Member[] { mOnBase, mOnDerived },
					new Member[] { });
					
		checkMatch(makeMethodPat("* fluffy.Derived.*(..) throws java.lang.CloneNotSupportedException"),
					new Member[] { },
					new Member[] { mOnBase, mOnDerived });
					
		//XXX need pattern checks
		Member[] NONE = new Member[] {};
		Member[] M = new Member[] { onlyDerivedOnDerived };
		Member[] NO_EXCEPTIONS = new Member[] { mOnDerived };
		Member[] BOTH = new Member[] {mOnDerived, onlyDerivedOnDerived};
		
		checkMatch(makeMethodPat("* *(..)"), M, NONE);
		checkMatch(makeMethodPat("* *(..) throws !*"), NO_EXCEPTIONS, M);
		checkMatch(makeMethodPat("* *(..) throws *"), BOTH, NONE);
		checkMatch(makeMethodPat("* *(..) throws *, !*"), NONE, BOTH);
		
		checkMatch(makeMethodPat("* *(..) throws (!*)"), NONE, BOTH);		
		checkMatch(makeMethodPat("* *(..) throws !(!*)"), BOTH, NONE);
		
		checkMatch(makeMethodPat("* *(..) throws *..IOException"), M, NO_EXCEPTIONS);
		checkMatch(makeMethodPat("* *(..) throws *..IOException, *..Clone*"), M, NO_EXCEPTIONS);
		checkMatch(makeMethodPat("* *(..) throws *..IOException, !*..Clone*"), NONE, BOTH);
		checkMatch(makeMethodPat("* *(..) throws !*..IOException"), NO_EXCEPTIONS, M);
	}

	public void testInstanceMethodMatch() throws IOException {
		Member objectToString = MemberImpl.methodFromString("java.lang.String java.lang.Object.toString()");
		Member integerToString = MemberImpl.methodFromString("java.lang.String java.lang.Integer.toString()");
		Member integerIntValue = MemberImpl.methodFromString("int java.lang.Integer.intValue()");
		//Member objectToString = Member.methodFromString("java.lang.String java.lang.Object.toString()");
		
		checkMatch(makeMethodPat("* java.lang.Object.*(..)"),
					new Member[] { objectToString, integerToString },
					new Member[] { integerIntValue });
					
		checkMatch(makeMethodPat("* java.lang.Integer.*(..)"),
					new Member[] { integerIntValue, integerToString },
					new Member[] { objectToString });
	}

	
	public void testStaticMethodMatch() throws IOException {
		Member onlyBaseOnBase = MemberImpl.methodFromString("static void fluffy.Base.onlyBase()");
		Member onlyBaseOnDerived = MemberImpl.methodFromString("static void fluffy.Derived.onlyBase()");
		Member onlyDerivedOnDerived = MemberImpl.methodFromString("static void fluffy.Derived.onlyDerived()");
		Member bothOnBase = MemberImpl.methodFromString("static void fluffy.Base.both()");
		Member bothOnDerived = MemberImpl.methodFromString("static void fluffy.Derived.both()");
		
		checkMatch(makeMethodPat("* fluffy.Base.*(..)"),
					new Member[] { onlyBaseOnBase, onlyBaseOnDerived, bothOnBase },
					new Member[] { onlyDerivedOnDerived, bothOnDerived });
					
		checkMatch(makeMethodPat("* fluffy.Derived.*(..)"),
					new Member[] { onlyBaseOnDerived, bothOnDerived, onlyDerivedOnDerived },
					new Member[] { onlyBaseOnBase, bothOnBase });
	}
	
	public void testFieldMatch() throws IOException {
		Member onlyBaseOnBase = MemberImpl.fieldFromString("int fluffy.Base.onlyBase");
		Member onlyBaseOnDerived = MemberImpl.fieldFromString("int fluffy.Derived.onlyBase");
		Member onlyDerivedOnDerived = MemberImpl.fieldFromString("int fluffy.Derived.onlyDerived");
		Member bothOnBase = MemberImpl.fieldFromString("int fluffy.Base.both");
		Member bothOnDerived = MemberImpl.fieldFromString("int fluffy.Derived.both");
		
		checkMatch(makeFieldPat("* fluffy.Base.*"),
					new Member[] { onlyBaseOnBase, onlyBaseOnDerived, bothOnBase },
					new Member[] { onlyDerivedOnDerived, bothOnDerived });
					
		checkMatch(makeFieldPat("* fluffy.Derived.*"),
					new Member[] { onlyBaseOnDerived, bothOnDerived, onlyDerivedOnDerived },
					new Member[] { onlyBaseOnBase, bothOnBase });
	}
	
	public void testConstructorMatch() throws IOException {
		Member onBase = MemberImpl.methodFromString("void fluffy.Base.<init>()");
		Member onDerived = MemberImpl.methodFromString("void fluffy.Derived.<init>()");
		Member onBaseWithInt = MemberImpl.methodFromString("void fluffy.Base.<init>(int)");

		
		checkMatch(makeMethodPat("fluffy.Base.new(..)"),
					new Member[] { onBase, onBaseWithInt },
					new Member[] { onDerived });
					
		checkMatch(makeMethodPat("fluffy.Derived.new(..)"),
					new Member[] { onDerived},
					new Member[] { onBase, onBaseWithInt });
	}
	
	
	
	
		
	public void checkMatch(SignaturePattern p, Member[] yes, Member[] no) throws IOException {
		p = p.resolveBindings(new TestScope(world, new FormalBinding[0]), new Bindings(0));
		
		for (int i=0; i < yes.length; i++) {
			checkMatch(p, yes[i], true);
		}
		
		for (int i=0; i < no.length; i++) {
			checkMatch(p, no[i], false);
		}
		
		checkSerialization(p);
	}

	private void checkMatch(SignaturePattern p, Member member, boolean b) {
		boolean matches = p.matches(member, world);
		assertEquals(p.toString() + " matches " + member.toString(), b, matches);
	}


	private SignaturePattern makeMethodPat(String pattern) {
		return new PatternParser(pattern).parseMethodOrConstructorSignaturePattern();
	}
	
	private SignaturePattern makeFieldPat(String pattern) {
		return new PatternParser(pattern).parseFieldSignaturePattern();
	}
	
	private void checkSerialization(SignaturePattern p) throws IOException {
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(bo);
		p.write(out);
		out.close();
		
		ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
		VersionedDataInputStream in = new VersionedDataInputStream(bi);
		SignaturePattern newP = SignaturePattern.read(in, null);
		
		assertEquals("write/read", p, newP);	
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11159.java