error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2353.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2353.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2353.java
text:
```scala
.@@getInstance().getExtensionHandler(IExtensionHandlerKeys.ORG_COLUMBA_CORE_THEME);

// The contents of this file are subject to the Mozilla Public License Version
// 1.1
//(the "License"); you may not use this file except in compliance with the
//License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
//
//Software distributed under the License is distributed on an "AS IS" basis,
//WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
//for the specific language governing rights and
//limitations under the License.
//
//The Original Code is "The Columba Project"
//
//The Initial Developers of the Original Code are Frederik Dietz and Timo
// Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003.
//
//All Rights Reserved.Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
// 02111-1307, USA.
package org.columba.core.gui.themes;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.columba.api.plugin.IExtension;
import org.columba.api.plugin.IExtensionHandler;
import org.columba.api.plugin.IExtensionHandlerKeys;
import org.columba.core.base.OSInfo;
import org.columba.core.config.Config;
import org.columba.core.gui.themes.plugin.AbstractThemePlugin;
import org.columba.core.logging.Logging;
import org.columba.core.plugin.PluginManager;
import org.columba.core.xml.XmlElement;

/**
 * Switch between Look and Feels.
 * <p>
 * L&F and feels are loaded as plugins.
 * <p>
 * 
 * @see org.columba.core.gui.themes.plugin.AbstractThemePlugin
 * 
 * @author fdietz
 * 
 */
public class ThemeSwitcher {

	private static final String DEFAULT_LF = "Plastic";

	public static void setTheme() {
		// get configuration
		XmlElement themeConfig = Config.getInstance().get("options")
				.getElement("/options/gui/theme");

		if (themeConfig == null) {
			XmlElement themeParent = Config.getInstance().get("options")
					.getElement("/options/gui");
			themeConfig = new XmlElement("theme");
			themeParent.addElement(themeConfig);
		}

		String pluginName = null;
		try {
			// get plugin-handler
			IExtensionHandler handler =  PluginManager
					.getInstance().getHandler(IExtensionHandlerKeys.ORG_COLUMBA_CORE_THEME);

			pluginName = themeConfig.getAttribute("name");

			// if no theme available -> set "Plastic" as default
			if (pluginName == null) {
				pluginName = ThemeSwitcher.getPlatformDefaultTheme();
				themeConfig.addAttribute("name", DEFAULT_LF);
				themeConfig.addAttribute("theme", "Experience Blue");
			}

			AbstractThemePlugin theme = null;

			IExtension extension = handler.getExtension(pluginName);

			// instanciate theme
			theme = (AbstractThemePlugin) extension.instanciateExtension(null);

			// apply theme
			theme.setLookAndFeel();
		} catch (Exception ex) {

			if (Logging.DEBUG)
				ex.printStackTrace();

			JOptionPane.showMessageDialog(null, "Error while trying to load "
					+ pluginName
					+ " Look and Feel.\nSwitching back to default.");

			try {
				// fall-back
				UIManager.setLookAndFeel(UIManager
						.getCrossPlatformLookAndFeelClassName());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	} /*
		 * Gets the platform specific default theme. This is in all cases but
		 * MacOS X the Plastic theme. On MacOs X we use the System L&F.
		 * 
		 */

	public static String getPlatformDefaultTheme() {
		if (OSInfo.isMac()) {
			return UIManager.getSystemLookAndFeelClassName();
		} else {
			return DEFAULT_LF;
		}
	}

	public static void updateFrame(JFrame frame) {
		final JFrame f = frame;

		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				SwingUtilities.updateComponentTreeUI(f);
			}
		});
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2353.java