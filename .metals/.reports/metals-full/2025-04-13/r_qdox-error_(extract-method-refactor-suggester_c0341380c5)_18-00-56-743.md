error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1194.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1194.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1194.java
text:
```scala
I@@nputStream in = getClass().getResourceAsStream("/installer/install.props");

/*
 * Install.java - Main class of the installer
 *
 * Originally written by Slava Pestov for the jEdit installer project. This work
 * has been placed into the public domain. You may use this work in any way and
 * for any purpose you wish.
 *
 * THIS SOFTWARE IS PROVIDED AS-IS WITHOUT WARRANTY OF ANY KIND, NOT EVEN THE
 * IMPLIED WARRANTY OF MERCHANTABILITY. THE AUTHOR OF THIS SOFTWARE, ASSUMES
 * _NO_ RESPONSIBILITY FOR ANY CONSEQUENCE RESULTING FROM THE USE, MODIFICATION,
 * OR REDISTRIBUTION OF THIS SOFTWARE.
 */

package installer;

import javax.swing.plaf.metal.MetalLookAndFeel;
import java.io.*;
import java.util.Properties;

public class Install
{
	public static void main(String[] args)
	{
		String javaVersion = System.getProperty("java.version");
		if(javaVersion.compareTo("1.3") < 0)
		{
			System.err.println("You are running Java version "
				+ javaVersion + ".");
			System.err.println("This installer requires Java 1.3 or later.");
			System.exit(1);
		}

		if(args.length == 0)
		{
			MetalLookAndFeel.setCurrentTheme(new JEditMetalTheme());
			new SwingInstall();
		}
		else if(args.length == 1 && args[0].equals("text"))
			new ConsoleInstall();
		else if(args.length >= 2 && args[0].equals("auto"))
		{
			new NonInteractiveInstall(args);
		}
		else
		{
			System.err.println("Usage:");
			System.err.println("java -jar <installer JAR>");
			System.err.println("java -jar <installer JAR> text");
			System.err.println("java -jar <installer JAR> auto"
				+ " <install dir> [unix-script=<dir>] [unix-man=<dir>]");
			System.err.println("text parameter starts installer in text-only mode.");
			System.err.println("auto parameter starts installer in non-interactive mode.");
		}
	}

	public Install()
	{
		props = new Properties();
		try
		{
			InputStream in = getClass().getResourceAsStream("install.props");
			props.load(in);
			in.close();
		}
		catch(IOException io)
		{
			System.err.println("Error loading 'install.props':");
			io.printStackTrace();
		}

		buf = new byte[32768];
	}

	public String getProperty(String name)
	{
		return props.getProperty(name);
	}

	public int getIntegerProperty(String name)
	{
		try
		{
			return Integer.parseInt(props.getProperty(name));
		}
		catch(Exception e)
		{
			return -1;
		}
	}

	public void copy(InputStream in, String outfile, Progress progress)
		throws IOException
	{
		File outFile = new File(outfile);

		OperatingSystem.getOperatingSystem().mkdirs(outFile.getParent());

		BufferedOutputStream out = new BufferedOutputStream(
			new FileOutputStream(outFile));

		int count;

		for(;;)
		{
			count = in.read(buf,0,Math.min(in.available(),buf.length));
			if(count == -1 || count == 0)
				break;

			out.write(buf,0,count);
			if(progress != null)
				progress.advance(count);
		}

		//in.close();
		out.close();
	}

	// private members
	private Properties props;
	private byte[] buf;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1194.java