error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9181.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9181.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9181.java
text:
```scala
public static S@@tring[] split (String s) {

/* *******************************************************************
 * Copyright (c) 2004 IBM Corporation
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     Noel Markham, Matthew Webster     initial implementation 
 * ******************************************************************/

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author Noel Markham
 */
public class RunWeaveTests {

	private static boolean RUN_SMALL = true;
	private static boolean RUN_MED = true;
	private static boolean RUN_LARGE = true;
		
	private static final String SMALL_PROGRAM_ARGS = "-c 5 -m 100 -l 10 "; // must end on a space in case any more arguments are added later in the script
	private static final String MEDIUM_PROGRAM_ARGS = "-c 25 -m 100 -l 10 ";
	private static final String LARGE_PROGRAM_ARGS = "-c 125 -m 100 -l 10 ";
	
	private static final String WARMUP_ARGS = "-c 3 -m 100 -l 10 ";

	private static final int ITERATIONS = 3;
	
	private static String filename;
		
	private static long[][] times = new long[7][3 * ITERATIONS];
		
	public static void main(String[] args) throws IOException {
		
		parseArgs(args);
		
		// Purely as a warm-up...
		System.out.println("Warming up...");
		WeaveTests.main(split(WARMUP_ARGS + "-i " + ITERATIONS));

		if (RUN_SMALL) {
			WeaveTests.main(
				split(SMALL_PROGRAM_ARGS + "-echo -i " + ITERATIONS));

			for (int i = 0; i < ITERATIONS; i++)
				times[0][i] = WeaveTests.compileTimes[i];

			for (int i = 0; i < ITERATIONS; i++)
				times[1][i] = WeaveTests.executionFastTimes[i];
			for (int i = 0; i < ITERATIONS; i++)
				times[2][i] = WeaveTests.executionMedTimes[i];
			for (int i = 0; i < ITERATIONS; i++)
				times[3][i] = WeaveTests.executionSlowTimes[i];

			for (int i = 0; i < ITERATIONS; i++)
				times[4][i] = WeaveTests.getFastTimes[i];
			for (int i = 0; i < ITERATIONS; i++)
				times[5][i] = WeaveTests.getMedTimes[i];
			for (int i = 0; i < ITERATIONS; i++)
				times[6][i] = WeaveTests.getSlowTimes[i]; 
		}		
		
		if (RUN_MED) {
			WeaveTests.main(
				split(MEDIUM_PROGRAM_ARGS + "-echo -i " + ITERATIONS));

			for (int i = ITERATIONS; i < (2 * ITERATIONS); i++)
				times[0][i] = WeaveTests.compileTimes[i - ITERATIONS];

			for (int i = ITERATIONS; i < (2 * ITERATIONS); i++)
				times[1][i] = WeaveTests.executionFastTimes[i - ITERATIONS];
			for (int i = ITERATIONS; i < (2 * ITERATIONS); i++)
				times[2][i] = WeaveTests.executionMedTimes[i - ITERATIONS];
			for (int i = ITERATIONS; i < (2 * ITERATIONS); i++)
				times[3][i] = WeaveTests.executionSlowTimes[i - ITERATIONS];

			for (int i = ITERATIONS; i < (2 * ITERATIONS); i++)
				times[4][i] = WeaveTests.getFastTimes[i - ITERATIONS];
			for (int i = ITERATIONS; i < (2 * ITERATIONS); i++)
				times[5][i] = WeaveTests.getMedTimes[i - ITERATIONS];
			for (int i = ITERATIONS; i < (2 * ITERATIONS); i++)
				times[6][i] = WeaveTests.getSlowTimes[i - ITERATIONS];
			
		}		
		
		if (RUN_LARGE) {
			WeaveTests.main(
				split(LARGE_PROGRAM_ARGS + "-echo -i " + ITERATIONS));

			for (int i = (2 * ITERATIONS); i < (3 * ITERATIONS); i++)
				times[0][i] = WeaveTests.compileTimes[i - (2 * ITERATIONS)];

			for (int i = (2 * ITERATIONS); i < (3 * ITERATIONS); i++)
				times[1][i] =
					WeaveTests.executionFastTimes[i - (2 * ITERATIONS)];
			for (int i = (2 * ITERATIONS); i < (3 * ITERATIONS); i++)
				times[2][i] =
					WeaveTests.executionMedTimes[i - (2 * ITERATIONS)];
			for (int i = (2 * ITERATIONS); i < (3 * ITERATIONS); i++)
				times[3][i] =
					WeaveTests.executionSlowTimes[i - (2 * ITERATIONS)];

			for (int i = (2 * ITERATIONS); i < (3 * ITERATIONS); i++)
				times[4][i] = WeaveTests.getFastTimes[i - (2 * ITERATIONS)];
			for (int i = (2 * ITERATIONS); i < (3 * ITERATIONS); i++)
				times[5][i] = WeaveTests.getMedTimes[i - (2 * ITERATIONS)];
			for (int i = (2 * ITERATIONS); i < (3 * ITERATIONS); i++)
				times[6][i] = WeaveTests.getSlowTimes[i - (2 * ITERATIONS)];
			
		}		

		createCSV();
	}
	
	/* Compatibility with JDK 1.3.1 */
	private static String[] split (String s) {
		List list = new ArrayList();
		StringTokenizer st = new StringTokenizer(s," ");
		while (st.hasMoreTokens()) {
			list.add(st.nextToken());
		}
		
		String[] result = new String[list.size()];
		list.toArray(result);
		return result;
	}
	
	private static void parseArgs(String args[]) {
		
		if(args == null || args.length <= 0) {
			System.out.println("Running all program tests");
			return;
		}
			
		if(args[0].equals("-small")) {
			System.out.println("Running small-sized program test only");
			RUN_SMALL = true;
			RUN_MED = false;
			RUN_LARGE = false;
		}
		else if(args[0].equals("-medium")) {
			System.out.println("Running small- and medium -sized program tests");
			RUN_SMALL = true;
			RUN_MED = true;
			RUN_LARGE = false;
		}
		else if(args[0].equals("-large")) {
			System.out.println("Running all program tests");
			RUN_SMALL = true;
			RUN_MED = true;
			RUN_LARGE = true;
		}
		else usage();
	}
	
	private static void usage() {
		System.err.println("Usage:");
		System.err.println("\tjava RunWeaveTests [-small|-medium|-large]");
		System.exit(-1);
	}
	
	private static void createCSV() {
		String NL = System.getProperty("line.separator", "\n");
		StringBuffer csv = new StringBuffer(1000);
		 
		csv.append("Test Results");
		
		if(RUN_SMALL) for(int i = 0; i < ITERATIONS; i++) csv.append("," + SMALL_PROGRAM_ARGS);
		if(RUN_MED) for(int i = 0; i < ITERATIONS; i++) csv.append("," + MEDIUM_PROGRAM_ARGS);
		if(RUN_LARGE) for(int i = 0; i < ITERATIONS; i++) csv.append("," + LARGE_PROGRAM_ARGS);
		
		csv.append(NL);
		csv.append("Compile");
		if(RUN_SMALL) for(int i = 0; i < ITERATIONS; i++) csv.append("," + times[0][i]);
		if(RUN_MED) for(int i = ITERATIONS; i < (2 * ITERATIONS); i++) csv.append("," + times[0][i]);
		if(RUN_LARGE) for(int i = (2 * ITERATIONS); i < (3 * ITERATIONS); i++) csv.append("," + times[0][i]);

		csv.append(NL);

		csv.append(WeaveTests.EXECUTION_FAST);
		if(RUN_SMALL) for(int i = 0; i < ITERATIONS; i++) csv.append("," + times[1][i]);
		if(RUN_MED) for(int i = ITERATIONS; i < (2 * ITERATIONS); i++) csv.append("," + times[1][i]);
		if(RUN_LARGE) for(int i = (2 * ITERATIONS); i < (3 * ITERATIONS); i++) csv.append("," + times[1][i]);
		csv.append(NL);
		
		csv.append(WeaveTests.EXECUTION_MED);
		if(RUN_SMALL) for(int i = 0; i < ITERATIONS; i++) csv.append("," + times[2][i]);
		if(RUN_MED) for(int i = ITERATIONS; i < (2 * ITERATIONS); i++) csv.append("," + times[2][i]);
		if(RUN_LARGE) for(int i = (2 * ITERATIONS); i < (3 * ITERATIONS); i++) csv.append("," + times[2][i]);
		csv.append(NL);

		csv.append(WeaveTests.EXECUTION_SLOW);
		if(RUN_SMALL) for(int i = 0; i < ITERATIONS; i++) csv.append("," + times[3][i]);
		if(RUN_MED) for(int i = ITERATIONS; i < (2 * ITERATIONS); i++) csv.append("," + times[3][i]);
		if(RUN_LARGE) for(int i = (2 * ITERATIONS); i < (3 * ITERATIONS); i++) csv.append("," + times[3][i]);
		csv.append(NL);

		csv.append(WeaveTests.GET_FAST);
		if(RUN_SMALL) for(int i = 0; i < ITERATIONS; i++) csv.append("," + times[4][i]);
		if(RUN_MED) for(int i = ITERATIONS; i < (2 * ITERATIONS); i++) csv.append("," + times[4][i]);
		if(RUN_LARGE) for(int i = (2 * ITERATIONS); i < (3 * ITERATIONS); i++) csv.append("," + times[4][i]);
		csv.append(NL);

		csv.append(WeaveTests.GET_MED);
		if(RUN_SMALL) for(int i = 0; i < ITERATIONS; i++) csv.append("," + times[5][i]);
		if(RUN_MED) for(int i = ITERATIONS; i < (2 * ITERATIONS); i++) csv.append("," + times[5][i]);
		if(RUN_LARGE) for(int i = (2 * ITERATIONS); i < (3 * ITERATIONS); i++) csv.append("," + times[5][i]);
		csv.append(NL);

		csv.append(WeaveTests.GET_SLOW);
		if(RUN_SMALL) for(int i = 0; i < ITERATIONS; i++) csv.append("," + times[6][i]);
		if(RUN_MED) for(int i = ITERATIONS; i < (2 * ITERATIONS); i++) csv.append("," + times[6][i]);
		if(RUN_LARGE) for(int i = (2 * ITERATIONS); i < (3 * ITERATIONS); i++) csv.append("," + times[6][i]);
		csv.append(NL);
		
		filename = createFilename();
		try {
			FileOutputStream fos = new FileOutputStream(filename);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			
			bos.write(new String(csv).getBytes());
			
			bos.close();
			fos.close();
		} catch (IOException e) {
			System.err.println("Could not print to file " + filename);
			System.err.println(csv);
		}
		System.out.println("Written: " + filename);
	}
	
	private static String createFilename() {
		
		return (getVMInfo() + "_" + getDateAndTime()).replace('.', '_') + ".csv";
	}
	
	private static StringBuffer getDateAndTime() {
		Calendar c = Calendar.getInstance();
		
		StringBuffer sb = new StringBuffer(15);
		sb.append(c.get(Calendar.YEAR));
		int month = (c.get(Calendar.MONTH)) + 1;
		if(month < 10) sb.append(0);		
		sb.append(month);
		int date = c.get(Calendar.DAY_OF_MONTH);
		if(date < 10) sb.append(0);
		sb.append(date);
		
		sb.append("_");
		
		int hour = c.get(Calendar.HOUR_OF_DAY);
		if(hour < 10) sb.append(0);
		sb.append(hour);
		int minute = c.get(Calendar.MINUTE);	
		if(minute < 10) sb.append(0);
		sb.append(minute);
		int second = c.get(Calendar.SECOND);
		if(second < 10) sb.append(0);
		sb.append(second);
		
		
		return sb;
	}
	
	private static StringBuffer getVMInfo() {
		StringBuffer sb = new StringBuffer(40);
		
		String vm = System.getProperty("java.vm.vendor");
		
		if(vm.equals("IBM Corporation")) {
			String vminfo = System.getProperty("java.vm.info");
			String[] vminfoComponents = split(vminfo);
			
			sb.append(vminfoComponents[2] + ("_"));
			sb.append(vminfoComponents[3] + ("_"));
			sb.append(vminfoComponents[4] + ("_"));
			sb.append(vminfoComponents[6]);
		}
		else if(vm.equals("Sun Microsystems Inc.")) {
			String vminfo = System.getProperty("java.vm.name");
			String[] vminfoComponents = split(vminfo);

			sb.append("Sun_");
			sb.append(System.getProperty("java.vm.version") + "_");
			sb.append(vminfoComponents[2]);
		}

		return sb;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9181.java