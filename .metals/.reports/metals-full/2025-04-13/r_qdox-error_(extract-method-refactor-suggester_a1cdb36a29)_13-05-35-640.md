error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6466.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6466.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,3]

error in qdox parser
file content:
```java
offset: 3
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6466.java
text:
```scala
+ j@@ar + "\" org.gjt.sp.jedit.jEdit $@\n");

/*
 * OperatingSystem.java
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

import java.io.*;
import java.util.Vector;

/*
 * Abstracts away operating-specific stuff, like finding out the installation
 * directory, creating a shortcut to start to program, and such.
 */
public abstract class OperatingSystem
{
	public abstract String getInstallDirectory(String name, String version);

	public abstract static class OSTask
	{
		protected Install installer;
		protected String name;
		protected String label;
		protected String directory;
		protected boolean enabled;

		public OSTask(Install installer, String name)
		{
			this.installer = installer;
			this.name = name;
			this.label = installer.getProperty("ostask." + name + ".label");
			this.directory = getDefaultDirectory(installer);

			// on by default
			enabled = true;
		}

		public String getName()
		{
			return name;
		}

		public String getLabel()
		{
			return label;
		}

		public String getDefaultDirectory(Install installer)
		{
			return null;
		}

		public String getDirectory()
		{
			return directory;
		}

		public boolean isEnabled()
		{
			return enabled;
		}

		public void setEnabled(boolean enabled)
		{
			this.enabled = enabled;
		}

		public void setDirectory(String directory)
		{
			this.directory = directory;
		}

		public abstract void perform(String installDir,
			Vector filesets) throws IOException;
	}

	public OSTask[] getOSTasks(Install installer)
	{
		return new OSTask[0];
	}

	public void mkdirs(String directory) throws IOException
	{
		File file = new File(directory);
		if(!file.exists())
			file.mkdirs();
	}

	public static OperatingSystem getOperatingSystem()
	{
		if(os != null)
			return os;

		if(System.getProperty("mrj.version") != null)
			os = new MacOS();
		else
		{
			String osName = System.getProperty("os.name");
			if(osName.indexOf("Windows") != -1)
				os = new Windows();
			else if(osName.indexOf("OS/2") != -1)
				os = new HalfAnOS();
			else if(osName.indexOf("VMS") != -1)
				os = new VMS();
			else
				os = new Unix();
		}

		return os;
	}

	public static class Unix extends OperatingSystem
	{
		public String getInstallDirectory(String name, String version)
		{
			String dir = "/usr/local/share/";
			if(!new File(dir).canWrite())
				dir = System.getProperty("user.home");

			return new File(dir,name.toLowerCase() + "/" + version).getPath();
		}

		public String getExtraClassPath()
		{
			return "";
		}

		public class ScriptOSTask extends OSTask
		{
			public ScriptOSTask(Install installer)
			{
				super(installer,"unix-script");
			}

			public String getDefaultDirectory(Install installer)
			{
				String dir = "/usr/local/";
				if(!new File(dir).canWrite())
					dir = System.getProperty("user.home");

				return new File(dir,"bin").getPath();
			}

			public void perform(String installDir,
				Vector filesets) throws IOException
			{
				if(!enabled)
					return;

				mkdirs(directory);

				String name = installer.getProperty("app.name");

				// create app start script
				String script = directory + File.separatorChar
					+ name.toLowerCase();

				// Delete existing copy
				new File(script).delete();

				// Write simple script
				FileWriter out = new FileWriter(script);
				out.write("#!/bin/sh\n");
				out.write("# Java heap size, in megabytes\n");
				out.write("JAVA_HEAP_SIZE=128\n");
				out.write("DEFAULT_JAVA_HOME=\""
					+ System.getProperty("java.home")
					+ "\"\n");
				out.write("if [ \"$JAVA_HOME\" = \"\" ]; then\n");
				out.write("JAVA_HOME=\"$DEFAULT_JAVA_HOME\"\n");
				out.write("fi\n");

				out.write("exec \"$JAVA_HOME"
					+ "/bin/java\" -server -mx${JAVA_HEAP_SIZE}m ${"
					+ name.toUpperCase() + "} ");

				String jar = installDir + File.separator
					+ name.toLowerCase() + ".jar";

				out.write("-classpath \"" + getExtraClassPath()
					+ jar + "\" org.gjt.sp.jedit.jEdit -reuseview $@\n");

				out.close();

				// Make it executable
				String[] chmodArgs = { "chmod", "755", script };
				exec(chmodArgs);
			}
		}

		public class ManPageOSTask extends OSTask
		{
			public ManPageOSTask(Install installer)
			{
				super(installer,"unix-man");
			}

			public String getDefaultDirectory(Install installer)
			{
				String dir = "/usr/local/";
				if(!new File(dir).canWrite())
					dir = System.getProperty("user.home");

				return new File(dir,"man/man1").getPath();
			}

			public void perform(String installDir,
				Vector filesets) throws IOException
			{
				if(!enabled)
					return;

				mkdirs(directory);

				String name = installer.getProperty("app.name");

				// install man page
				String manpage = installer.getProperty("ostask.unix-man.manpage");

				InputStream in = getClass().getResourceAsStream("/" + manpage);
				installer.copy(in,new File(directory,manpage).getPath(),
					null);
			}
		}

		public OSTask[] getOSTasks(Install installer)
		{
			return new OSTask[] { new ScriptOSTask(installer),
				new ManPageOSTask(installer) };
		}

		public void mkdirs(String directory) throws IOException
		{
			File file = new File(directory);
			if(!file.exists())
			{
				String[] mkdirArgs = { "mkdir", "-m", "755",
					"-p", directory };
				exec(mkdirArgs);
			}
		}

		public void exec(String[] args) throws IOException
		{
			Process proc = Runtime.getRuntime().exec(args);
			proc.getInputStream().close();
			proc.getOutputStream().close();
			proc.getErrorStream().close();
			try
			{
				proc.waitFor();
			}
			catch(InterruptedException ie)
			{
			}
		}
	}

	public static class MacOS extends Unix
	{
		public String getInstallDirectory(String name, String version)
		{
			return "/Applications/" + name + " " + version;
		}

		public String getExtraClassPath()
		{
			return "/System/Library/Java/:";
		}
	}

	public static class Windows extends OperatingSystem
	{
		public String getInstallDirectory(String name, String version)
		{
			return "C:\\Program Files\\" + name + " " + version;
		}

		public class JEditLauncherOSTask extends OSTask
		{
			public JEditLauncherOSTask(Install installer)
			{
				super(installer,"jedit-launcher");
			}

			public String getDefaultDirectory(Install installer)
			{
				return null;
			}

			public void perform(String installDir,
				Vector filesets)
			{
				if(!enabled
 !filesets.contains("jedit-windows"))
					return;

				// run jEditLauncher installation
				File executable = new File(installDir,"jedit.exe");
				if(!executable.exists())
					return;

				String[] args = { executable.getPath(), "/i",
					System.getProperty("java.home")
					+ File.separator
					+ "bin" };

				try
				{
					Runtime.getRuntime().exec(args).waitFor();
				}
				catch(IOException io)
				{
				}
				catch(InterruptedException ie)
				{
				}
			}
		}

		public OSTask[] getOSTasks(Install installer)
		{
			return new OSTask[] { /* new JEditLauncherOSTask(installer) */ };
		}
	}

	public static class HalfAnOS extends OperatingSystem
	{
		public String getInstallDirectory(String name, String version)
		{
			return "C:\\" + name + " " + version;
		}
	}

	public static class VMS extends OperatingSystem
	{
		public String getInstallDirectory(String name, String version)
		{
			return "./" + name.toLowerCase() + "/" + version;
		}
	}

	// private members
	private static OperatingSystem os;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6466.java