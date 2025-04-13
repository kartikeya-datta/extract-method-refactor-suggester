error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2879.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2879.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2879.java
text:
```scala
S@@ystem.getProperty("mrj.version"),mrjversion,false) < 0)

/* 
 * :tabSize=8:indentSize=8:noTabs=false:
 * :folding=explicit:collapseFolds=1:
 *
 * MacOSPlugin.java - Main class Mac OS Plugin
 * Copyright (C) 2001, 2002 Kris Kopicki
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package macos;

//{{{ Imports
import java.util.Vector;
import javax.swing.*;
import org.gjt.sp.jedit.*;
import org.gjt.sp.jedit.gui.*;
import org.gjt.sp.jedit.msg.*;
import org.gjt.sp.util.Log;
import macos.menu.*;
import macos.script.*;
import com.apple.cocoa.application.*;
import com.apple.eawt.Application;
//}}}

public class MacOSPlugin extends EBPlugin
{
	//{{{ Variables
	static boolean started = false;
	private boolean osok;
	private Delegate delegate;
	//}}}
	
	//{{{ start() method
	public void start()
	{
		if(osok = osok())
		{
			delegate = new Delegate();
			NSApplication app = NSApplication.sharedApplication();
			
			Macros.registerHandler(new AppleScriptHandler());
			Application app2 = new Application();
			app2.addApplicationListener(delegate);
			app2.setEnabledPreferencesMenu(true);
			app2.setEnabledAboutMenu(true);
			
			app.setDelegate(delegate);
			//app.setServicesProvider(delegate);
		}
	} //}}}
	
	//{{{ handleMessage() method
	public void handleMessage(EBMessage message)
	{
		if (osok)
		{
			// Set type/creator codes for files
			if (message instanceof BufferUpdate)
				delegate.handleFileCodes((BufferUpdate)message);
			else if (message instanceof PropertiesChanged)
			{
				boolean b = jEdit.getBooleanProperty("MacOSPlugin.useSelection",
					jEdit.getBooleanProperty("MacOSPlugin.default.useSelection"));
				if (b)
					jEdit.setColorProperty("view.selectionColor",
						UIManager.getColor("textHighlight"));
			}
			// This is necessary to have a file opened from the Finder
			// before jEdit is running set as the currently active
			// buffer.
			else if (!started && message instanceof ViewUpdate)
				delegate.handleOpenFile((ViewUpdate)message);
		}
	}//}}}
	
	//{{{ osok() method
	private boolean osok()
	{
		final String osname = jEdit.getProperty("MacOSPlugin.depend.os.name");
		final String mrjversion = jEdit.getProperty("MacOSPlugin.depend.mrj.version");
		
		if (!System.getProperty("os.name").equals(osname))
		{
			// According to Slava this is better
			Log.log(Log.ERROR,this,jEdit.getProperty("MacOSPlugin.dialog.osname.message"));
			return false;
		}
		if (MiscUtilities.compareStrings(
			System.getProperty("mrj.version"),mrjversion) < 0)
		{
			SwingUtilities.invokeLater( new Runnable() { public void run() {
				GUIUtilities.error(null,"MacOSPlugin.dialog.mrjversion",new Object[] {mrjversion});
			}});
			return false;
		}

		return true;
	}//}}}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2879.java