error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/235.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/235.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[7,1]

error in qdox parser
file content:
```java
offset: 127
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/235.java
text:
```scala
@since 1.0


// Log4j uses the JUnit framework for internal unit testing. JUnit
// is available from "http://www.junit.org".

p@@ackage org.apache.log4j.test;

import org.apache.log4j.helpers.OptionConverter;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;
import java.util.Properties;

/**
   Test variable substitution code.   
   @author Ceki G&uuml;lc&uuml;
   
   @since 0.9.2
*/
public class UnitTestVarSubst extends TestCase {

  Properties props;
  
  public UnitTestVarSubst(String name) {
    super(name);
  }

  public
  void setUp() {
    props = new Properties();
    props.put("TOTO", "wonderful");
    props.put("key1", "value1");
    props.put("key2", "value2");
    System.setProperties(props);
  }  
  
  public
  void tearDown() {
    props = null;
  }

  public
  void varSubstTest1() {
    String r;

    r = OptionConverter.substVars("hello world.", null);
    assertEquals("hello world.", r);
    
    r = OptionConverter.substVars("hello ${TOTO} world.", null);
    
    assertEquals("hello wonderful world.", r);
  }


  public
  void varSubstTest2() {
    String r;

    r = OptionConverter.substVars("Test2 ${key1} mid ${key2} end.", null);
    assertEquals("Test2 value1 mid value2 end.", r);
  }

  public
  void varSubstTest3() {
    String r;

    r = OptionConverter.substVars(
				     "Test3 ${unset} mid ${key1} end.", null);
    assertEquals("Test3  mid value1 end.", r);
  }

  public
  void varSubstTest4() {
    String res;
    String val = "Test4 ${incomplete ";
    try {
      res = OptionConverter.substVars(val, null);
    }
    catch(IllegalArgumentException e) {
      String errorMsg = e.getMessage();
      //System.out.println('['+errorMsg+']');
      assertEquals('"'+val+ "\" has no closing brace. Opening brace at position 6.", errorMsg);
    }
  }

  public
  static
  Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTest(new UnitTestVarSubst("varSubstTest1"));
    suite.addTest(new UnitTestVarSubst("varSubstTest2"));
    suite.addTest(new UnitTestVarSubst("varSubstTest3"));
    suite.addTest(new UnitTestVarSubst("varSubstTest4"));
    return suite;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/235.java