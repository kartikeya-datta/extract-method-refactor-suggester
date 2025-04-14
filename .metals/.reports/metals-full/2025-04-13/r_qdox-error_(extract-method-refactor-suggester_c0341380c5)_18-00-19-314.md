error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1423.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1423.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1423.java
text:
```scala
a@@fter() returning(TheObject obj): execution(new()) {

import org.aspectj.testing.Tester;

privileged aspect TheAspect perthis(this(TheObject)) {
     private TheObject theObject;
     private int myPrivate_int=-1, myPackage_int=-1, myProtected_int=-1, myPublic_int=-1;

     after() returning(TheObject obj): execution(new()) { // CW 7 nonmatching target
          theObject = obj;
     }

     after() returning(): call(* realMain(..)) {
         test_eq();
         test_timeseq();
         test_diveq();
         test_modeq();
         test_pluseq();
         test_minuseq();
         test_leftShifteq();
         test_rightShifteq();
         test_xoreq();
         test_andeq();
         test_unsignedRightShifteq();
         test_postinc();
         test_postdec();
         test_preinc();
         test_predec();         
         test_oreq();
         test_gets();
         test_calls();
     }

     public void test_eq() {
         theObject.private_int   = 1;
         theObject.protected_int = 2;
         theObject.package_int   = 3;
         theObject.public_int    = 4;
         Tester.checkEqual(theObject.private_int,1,"set private_int");
         Tester.checkEqual(theObject.protected_int,2,"set protected_int");
         Tester.checkEqual(theObject.package_int,3,"set package_int");
         Tester.checkEqual(theObject.public_int,4,"set public_int");         
     }

     public void test_timeseq() {
         theObject.private_int   *= 4;
         theObject.protected_int *= 4;
         theObject.package_int   *= 4;
         theObject.public_int    *= 4;
         Tester.checkEqual(theObject.private_int,4,"times private_int");
         Tester.checkEqual(theObject.protected_int,8,"times protected_int");
         Tester.checkEqual(theObject.package_int,12,"times package_int");
         Tester.checkEqual(theObject.public_int,16,"times public_int");          
     }

     public void test_diveq() {
         theObject.private_int   /= 2;
         theObject.protected_int /= 2;
         theObject.package_int   /= 2;
         theObject.public_int    /= 2;
         Tester.checkEqual(theObject.private_int,2,"div private_int");
         Tester.checkEqual(theObject.protected_int,4,"div protected_int");
         Tester.checkEqual(theObject.package_int,6,"div package_int");
         Tester.checkEqual(theObject.public_int,8,"div public_int");          
     }

     public void test_modeq() {
         theObject.private_int   %= 2;
         theObject.protected_int %= 3;
         theObject.package_int   %= 4;
         theObject.public_int    %= 5;
         Tester.checkEqual(theObject.private_int,0,"mod private_int");
         Tester.checkEqual(theObject.protected_int,1,"mod protected_int");
         Tester.checkEqual(theObject.package_int,2,"mod package_int");
         Tester.checkEqual(theObject.public_int,3,"mod public_int");
     }

     public void test_pluseq() {
         theObject.private_int   += 2;
         theObject.protected_int += 2;
         theObject.package_int   += 2;
         theObject.public_int    += 2;
         Tester.checkEqual(theObject.private_int,2,"plus private_int");
         Tester.checkEqual(theObject.protected_int,3,"plus protected_int");
         Tester.checkEqual(theObject.package_int,4,"plus package_int");
         Tester.checkEqual(theObject.public_int,5,"plus public_int");
     }

     public void test_minuseq() {
         theObject.private_int   -= 1;
         theObject.protected_int -= 1;
         theObject.package_int   -= 1;
         theObject.public_int    -= 1;
         Tester.checkEqual(theObject.private_int,1,"minus private_int");
         Tester.checkEqual(theObject.protected_int,2,"minus protected_int");
         Tester.checkEqual(theObject.package_int,3,"minus package_int");
         Tester.checkEqual(theObject.public_int,4,"minus public_int");          
     }

     public void test_leftShifteq() {
         theObject.private_int   <<= 1;
         theObject.protected_int <<= 1;
         theObject.package_int   <<= 1;
         theObject.public_int    <<= 1;
         Tester.checkEqual(theObject.private_int,2,"left shift private_int");
         Tester.checkEqual(theObject.protected_int,4,"left shift protected_int");
         Tester.checkEqual(theObject.package_int,6,"left shift package_int");
         Tester.checkEqual(theObject.public_int,8,"left shift public_int");          
     }

     public void test_rightShifteq() {
         theObject.private_int   >>= 1;
         theObject.protected_int >>= 1;
         theObject.package_int   >>= 1;
         theObject.public_int    >>= 1;
         Tester.checkEqual(theObject.private_int,1,"right shift private_int");
         Tester.checkEqual(theObject.protected_int,2,"right shift protected_int");
         Tester.checkEqual(theObject.package_int,3,"right shift package_int");
         Tester.checkEqual(theObject.public_int,4,"right shift public_int");          
     }

     public void test_xoreq() {
         theObject.private_int   ^= 0;
         theObject.protected_int ^= 1;
         theObject.package_int   ^= 1;
         theObject.public_int    ^= 1;
         Tester.checkEqual(theObject.private_int,1,"xor private_int");
         Tester.checkEqual(theObject.protected_int,3,"xor protected_int");
         Tester.checkEqual(theObject.package_int,2,"xor package_int");
         Tester.checkEqual(theObject.public_int,5,"xor public_int");          
     }

     public void test_andeq() {
         theObject.private_int   &= 3;
         theObject.protected_int &= 6;
         theObject.package_int   &= 3;
         theObject.public_int    &= 4;
         Tester.checkEqual(theObject.private_int,1,"and private_int");
         Tester.checkEqual(theObject.protected_int,2,"and protected_int");
         Tester.checkEqual(theObject.package_int,2,"and package_int");
         Tester.checkEqual(theObject.public_int,4,"and public_int");          
     }

     public void test_unsignedRightShifteq() {
         theObject.private_int   >>>= 0;
         theObject.protected_int >>>= 1;
         theObject.package_int   >>>= 1;
         theObject.public_int    >>>= 2;
         Tester.checkEqual(theObject.private_int,1,"unsigned right shift private_int");
         Tester.checkEqual(theObject.protected_int,1,"unsigned right shift protected_int");
         Tester.checkEqual(theObject.package_int,1,"unsigned right shift package_int");
         Tester.checkEqual(theObject.public_int,1,"unsigned right shift public_int");          
     }

     public void test_postinc() {
         theObject.private_int   ++;
         theObject.protected_int ++;
         theObject.package_int   ++;
         theObject.public_int    ++;
         Tester.checkEqual(theObject.private_int,2,"post ++ private_int");
         Tester.checkEqual(theObject.protected_int,2,"post ++ protected_int");
         Tester.checkEqual(theObject.package_int,2,"post ++ package_int");
         Tester.checkEqual(theObject.public_int,2,"post ++ public_int");          
     }

     public void test_postdec() {
         theObject.private_int   --;
         theObject.protected_int --;
         theObject.package_int   --;
         theObject.public_int    --;
         Tester.checkEqual(theObject.private_int,1,"post -- private_int");
         Tester.checkEqual(theObject.protected_int,1,"post -- protected_int");
         Tester.checkEqual(theObject.package_int,1,"post -- package_int");
         Tester.checkEqual(theObject.public_int,1,"post -- public_int");          
     }

     public void test_preinc() {
         ++ theObject.private_int;
         ++ theObject.protected_int;
         ++ theObject.package_int;
         ++ theObject.public_int;
         Tester.checkEqual(theObject.private_int,2,"pre ++ private_int");
         Tester.checkEqual(theObject.protected_int,2,"pre ++ protected_int");
         Tester.checkEqual(theObject.package_int,2,"pre ++ package_int");
         Tester.checkEqual(theObject.public_int,2,"pre ++ public_int");          
     }

     public void test_predec() {
         -- theObject.private_int;
         -- theObject.protected_int;
         -- theObject.package_int;
         -- theObject.public_int;
         Tester.checkEqual(theObject.private_int,1,"pre -- private_int");
         Tester.checkEqual(theObject.protected_int,1,"pre -- protected_int");
         Tester.checkEqual(theObject.package_int,1,"pre -- package_int");
         Tester.checkEqual(theObject.public_int,1,"pre -- public_int");          
     }     

     public void test_oreq() {
         theObject.private_int   |= 8;
         theObject.protected_int |= 8;
         theObject.package_int   |= 8;
         theObject.public_int    |= 8;
         Tester.checkEqual(theObject.private_int,9,"or private_int");
         Tester.checkEqual(theObject.protected_int,9,"or protected_int");
         Tester.checkEqual(theObject.package_int,9,"or package_int");
         Tester.checkEqual(theObject.public_int,9,"or public_int");          
     }

     public void test_gets() {
         myPrivate_int   = theObject.private_int;
         myProtected_int = theObject.protected_int;
         myPackage_int   = theObject.package_int;
         myPublic_int    = theObject.public_int;
     }

     public void test_calls() {
         theObject.private_call();
         theObject.protected_call();
         theObject.package_call();
         theObject.public_call();         
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1423.java