error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6224.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6224.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6224.java
text:
```scala
i@@f (inputRegex.size > 0) {


package com.badlogic.gdx.tools;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import com.badlogic.gdx.utils.Array;

public class FileProcessor {
	FilenameFilter inputFilter;
	Comparator<File> comparator;
	Array<Pattern> inputRegex = new Array();
	String outputSuffix;
	ArrayList<InputFile> outputFiles = new ArrayList();
	boolean recursive = true;
	boolean flattenOutput;

	Comparator<InputFile> inputFileComparator = new Comparator<InputFile>() {
		public int compare (InputFile o1, InputFile o2) {
			return comparator.compare(o1.inputFile, o2.inputFile);
		}
	};

	public FileProcessor setInputFilter (FilenameFilter inputFilter) {
		this.inputFilter = inputFilter;
		return this;
	}

	public FileProcessor setComparator (Comparator<File> comparator) {
		this.comparator = comparator;
		return this;
	}

	public FileProcessor addInputSuffix (String... suffixes) {
		for (String suffix : suffixes)
			addInputRegex(".*" + Pattern.quote(suffix));
		return this;
	}

	public FileProcessor addInputRegex (String... regexex) {
		for (String regex : regexex)
			inputRegex.add(Pattern.compile(regex));
		return this;
	}

	public FileProcessor setOutputSuffix (String outputSuffix) {
		this.outputSuffix = outputSuffix;
		return this;
	}

	public FileProcessor setFlattenOutput (boolean flattenOutput) {
		this.flattenOutput = flattenOutput;
		return this;
	}

	public FileProcessor setRecursive (boolean recursive) {
		this.recursive = recursive;
		return this;
	}

	/** @return the processed files added with {@link #addProcessedFile(InputFile)}. */
	public ArrayList<InputFile> process (File inputFile, File outputRoot) throws Exception {
		if (inputFile.isFile())
			return process(new File[] {inputFile}, outputRoot);
		else
			return process(inputFile.listFiles(), outputRoot);
	}

	/** @return the processed files added with {@link #addProcessedFile(InputFile)}. */
	public ArrayList<InputFile> process (File[] files, File outputRoot) throws Exception {
		outputFiles.clear();

		HashMap<File, ArrayList<InputFile>> dirToEntries = new HashMap();
		process(files, outputRoot, outputRoot, dirToEntries, 0);

		ArrayList<InputFile> allInputFiles = new ArrayList();
		for (Entry<File, ArrayList<InputFile>> entry : dirToEntries.entrySet()) {
			ArrayList<InputFile> dirInputFiles = entry.getValue();
			if (comparator != null) Collections.sort(dirInputFiles, inputFileComparator);

			File inputDir = entry.getKey();
			File newOutputDir = flattenOutput ? outputRoot : dirInputFiles.get(0).outputDir;
			String outputName = inputDir.getName();
			if (outputSuffix != null) outputName += outputSuffix;

			InputFile inputFile = new InputFile();
			inputFile.inputFile = entry.getKey();
			inputFile.outputDir = newOutputDir;
			inputFile.outputFile = new File(newOutputDir, outputName);

			processDir(inputFile, dirInputFiles);
			allInputFiles.addAll(dirInputFiles);
		}

		if (comparator != null) Collections.sort(allInputFiles, inputFileComparator);
		for (InputFile inputFile : allInputFiles)
			processFile(inputFile);

		return outputFiles;
	}

	private void process (File[] files, File outputRoot, File outputDir, HashMap<File, ArrayList<InputFile>> dirToEntries,
		int depth) {
		for (File file : files) {
			if (file.isFile()) {
				if (inputRegex != null) {
					boolean found = false;
					for (Pattern pattern : inputRegex) {
						if (pattern.matcher(file.getName()).matches()) {
							found = true;
							continue;
						}
					}
					if (!found) continue;
				}

				File dir = file.getParentFile();
				if (inputFilter != null && !inputFilter.accept(dir, file.getName())) continue;

				String outputName = file.getName();
				if (outputSuffix != null) outputName = outputName.replaceAll("(.*)\\..*", "$1") + outputSuffix;

				InputFile inputFile = new InputFile();
				inputFile.depth = depth;
				inputFile.inputFile = file;
				inputFile.outputDir = outputDir;
				inputFile.outputFile = flattenOutput ? new File(outputRoot, outputName) : new File(outputDir, outputName);
				ArrayList<InputFile> inputFiles = dirToEntries.get(dir);
				if (inputFiles == null) {
					inputFiles = new ArrayList();
					dirToEntries.put(dir, inputFiles);
				}
				inputFiles.add(inputFile);
			}
			if (recursive && file.isDirectory())
				process(file.listFiles(inputFilter), outputRoot, new File(outputDir, file.getName()), dirToEntries, depth + 1);
		}
	}

	protected void processFile (InputFile inputFile) throws Exception {
	}

	protected void processDir (InputFile inputDir, ArrayList<InputFile> value) throws Exception {
	}

	protected void addProcessedFile (InputFile inputFile) {
		outputFiles.add(inputFile);
	}

	static public class InputFile {
		public File inputFile;
		public File outputDir;
		public File outputFile;
		public int depth;

		public InputFile () {
		}

		public InputFile (File inputFile, File outputFile) {
			this.inputFile = inputFile;
			this.outputFile = outputFile;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:169)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6224.java