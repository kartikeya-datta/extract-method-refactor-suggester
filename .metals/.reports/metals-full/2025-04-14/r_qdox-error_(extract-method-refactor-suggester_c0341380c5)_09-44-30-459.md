error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1024.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1024.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1024.java
text:
```scala
public static v@@oid checkMethods(String clazzname,String[] expectedMethods) {

import java.lang.reflect.*;
import java.util.*;

public class Util {
	
	public static void dumpMethods(String clazzname,String[] expectedMethods) {
		List methodsFound = new ArrayList();
		try {
			java.lang.Class clz = Class.forName(clazzname);
			Method m[] = clz.getDeclaredMethods();
			System.err.println("Number of methods defined for "+clazzname+" is "+(m==null?0:m.length));
			if (m!=null) {
				for (int i =0;i<m.length;i++) {
					String methodString = m[i].getReturnType().getName()+" "+m[i].getDeclaringClass().getName()+"."+
							           m[i].getName()+"("+stringify(m[i].getParameterTypes())+")"+
							           (m[i].isBridge()?" [BridgeMethod]":"");
					methodsFound.add(methodString);
				}
			}
		} catch (Throwable e) {e.printStackTrace();}
		
		StringBuffer diagnosticInfo = new StringBuffer();
		diagnosticInfo.append("\nExpected:\n").append(dumparray(expectedMethods));
		diagnosticInfo.append("\nFound:\n").append(dumplist(methodsFound));
		
		for (int i = 0; i < expectedMethods.length; i++) {
			String string = expectedMethods[i];
			if (!methodsFound.contains(string)) {
				throw new RuntimeException("Expecting method '"+string+"' but didnt find it\n"+diagnosticInfo.toString());
			}
			methodsFound.remove(string);
		}
		if (methodsFound.size()>0) {
			throw new RuntimeException("Found more methods than expected: "+dumplist(methodsFound));
		}
	}
	
	private static String dumparray(String[] arr) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			String string = arr[i];
			sb.append(string).append("\n");
		}
		return sb.toString();
	}
	
	private static String dumplist(List l) {
		StringBuffer sb = new StringBuffer();
		for (Iterator iter = l.iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			sb.append(element).append("\n");
		}
		return sb.toString();
	}
	
	private static String stringify(Class[] clazzes) {
		if (clazzes==null) return "";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < clazzes.length; i++) {
			Class class1 = clazzes[i];
			if (i>0) sb.append(",");
			sb.append(clazzes[i].getName());
		}
		return sb.toString();
	}
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1024.java