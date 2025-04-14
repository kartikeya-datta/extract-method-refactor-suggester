error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1494.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1494.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1494.java
text:
```scala
D@@efaultMutableTreeNode htmlSrcAppGwtNode = nodes.get("prj-html/src/GwtDefinition.gwt.xml");

package aurelienribon.libgdx.ui;

import aurelienribon.libgdx.LibraryDef;
import aurelienribon.libgdx.ProjectConfiguration;
import java.awt.Component;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import org.apache.commons.io.FilenameUtils;
import res.Res;

/**
 * @author Aurelien Ribon | http://www.aurelienribon.com/
 */
public class ResultTree extends JTree {
	private final ProjectConfiguration cfg = AppContext.inst().getConfig();
	private final Map<String, DefaultMutableTreeNode> nodes = new TreeMap<String, DefaultMutableTreeNode>();
	private final DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();

	public ResultTree() {
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setRootVisible(false);
		setShowsRootHandles(true);
		setCellRenderer(treeCellRenderer);
		setOpaque(false);

		AppContext.inst().addListener(new AppContext.Listener() {
			@Override public void configChanged() {
				update();
			}
		});

		build();
		update();
	}

	private void build() {
		try {
			ZipInputStream zis = new ZipInputStream(Res.getStream("projects.zip"));
			ZipEntry entry;

			while ((entry = zis.getNextEntry()) != null) {
				String name = entry.getName();
				name = entry.isDirectory() ? "#DIR#" + name : name; // this makes name sorting easier :p
				name = entry.isDirectory() ? name.substring(0, name.length()-1) : name;

				DefaultMutableTreeNode node = new DefaultMutableTreeNode(name);
				nodes.put(name, node);
			}

			zis.close();

			for (String name : nodes.keySet()) {
				String pName = name.startsWith("#DIR#") ? name : "#DIR#" + name;
				pName = FilenameUtils.getPath(pName);
				pName = pName.endsWith("/") ? pName.substring(0, pName.length()-1) : pName;

				DefaultMutableTreeNode node = nodes.get(name);
				DefaultMutableTreeNode pNode = nodes.get(pName);

				if (pNode != null) pNode.add(node);
				else rootNode.add(node);
			}

		} catch (IOException ex) {
			assert false;
		}
	}

	private void update() {
		DefaultMutableTreeNode commonPrjNode = nodes.get("#DIR#prj-common");
		DefaultMutableTreeNode desktopPrjNode = nodes.get("#DIR#prj-desktop");
		DefaultMutableTreeNode androidPrjNode = nodes.get("#DIR#prj-android");
		DefaultMutableTreeNode htmlPrjNode = nodes.get("#DIR#prj-html");

		rootNode.removeAllChildren();
		rootNode.add(commonPrjNode);
		if (cfg.isDesktopIncluded) rootNode.add(desktopPrjNode);
		if (cfg.isAndroidIncluded) rootNode.add(androidPrjNode);
		if (cfg.isHtmlIncluded) rootNode.add(htmlPrjNode);

		updateSrc();
		updateLibs();

		setModel(new DefaultTreeModel(rootNode));
	}

	private void updateSrc() {
		DefaultMutableTreeNode previousNode;

		// common

		DefaultMutableTreeNode commonSrcNode = nodes.get("#DIR#prj-common/src");
		DefaultMutableTreeNode commonSrcAppNode = nodes.get("prj-common/src/MyGame.java");
		DefaultMutableTreeNode commonSrcAppGwtNode = nodes.get("prj-common/src/MyGame.gwt.xml");

		commonSrcNode.removeAllChildren();
		previousNode = commonSrcNode;

		if (!cfg.getPackageName().trim().equals("")) {
			String[] paths = cfg.getPackageName().split("\\.");
			for (String path : paths) {
				DefaultMutableTreeNode node = new DefaultMutableTreeNode("#DIR#prj-common/src/" + path);
				previousNode.add(node);
				previousNode = node;
			}
			previousNode.add(commonSrcAppNode);
			commonSrcNode.add(commonSrcAppGwtNode);
		} else {
			commonSrcNode.add(commonSrcAppNode);
			commonSrcNode.add(commonSrcAppGwtNode);
		}

		// desktop

		DefaultMutableTreeNode desktopSrcNode = nodes.get("#DIR#prj-desktop/src");
		DefaultMutableTreeNode desktopSrcMainNode = nodes.get("prj-desktop/src/Main.java");

		desktopSrcNode.removeAllChildren();
		previousNode = desktopSrcNode;

		if (!cfg.getPackageName().trim().equals("")) {
			String[] paths = cfg.getPackageName().split("\\.");
			for (String path : paths) {
				DefaultMutableTreeNode node = new DefaultMutableTreeNode("#DIR#prj-desktop/src/" + path);
				previousNode.add(node);
				previousNode = node;
			}
			previousNode.add(desktopSrcMainNode);
		} else {
			desktopSrcNode.add(desktopSrcMainNode);
		}

		// android

		DefaultMutableTreeNode androidSrcNode = nodes.get("#DIR#prj-android/src");
		DefaultMutableTreeNode androidSrcActivityNode = nodes.get("prj-android/src/MainActivity.java");

		androidSrcNode.removeAllChildren();
		previousNode = androidSrcNode;

		if (!cfg.getPackageName().trim().equals("")) {
			String[] paths = cfg.getPackageName().split("\\.");
			for (String path : paths) {
				DefaultMutableTreeNode node = new DefaultMutableTreeNode("#DIR#prj-android/src/" + path);
				previousNode.add(node);
				previousNode = node;
			}
			previousNode.add(androidSrcActivityNode);
		} else {
			androidSrcNode.add(androidSrcActivityNode);
		}

		// html

		DefaultMutableTreeNode htmlSrcNode = nodes.get("#DIR#prj-html/src");
		DefaultMutableTreeNode htmlSrcAppGwtNode = nodes.get("prj-html/src/MyGame.gwt.xml");
		DefaultMutableTreeNode htmlSrcClientDirNode = nodes.get("#DIR#prj-html/src/client");

		htmlSrcNode.removeAllChildren();
		previousNode = htmlSrcNode;

		if (!cfg.getPackageName().trim().equals("")) {
			String[] paths = cfg.getPackageName().split("\\.");
			for (String path : paths) {
				DefaultMutableTreeNode node = new DefaultMutableTreeNode("#DIR#prj-html/src/" + path);
				previousNode.add(node);
				previousNode = node;
			}
			previousNode.add(htmlSrcClientDirNode);
			previousNode.add(htmlSrcAppGwtNode);
		} else {
			commonSrcNode.add(htmlSrcClientDirNode);
			commonSrcNode.add(htmlSrcAppGwtNode);
		}
	}

	private void updateLibs() {
		DefaultMutableTreeNode commonLibsNode = nodes.get("#DIR#prj-common/libs");
		DefaultMutableTreeNode desktopLibsNode = nodes.get("#DIR#prj-desktop/libs");
		DefaultMutableTreeNode androidLibsNode = nodes.get("#DIR#prj-android/libs");
		DefaultMutableTreeNode htmlLibsNode = nodes.get("#DIR#prj-html/war/WEB-INF/lib");

		for (String libraryName : cfg.getLibraryNames()) {
			LibraryDef def = cfg.getLibraryDef(libraryName);
			if (!def.isUsed) continue;
			for (String path : def.libsCommon) pathToNodes(path, commonLibsNode);
			for (String path : def.libsDesktop) pathToNodes(path, desktopLibsNode);
			for (String path : def.libsAndroid) pathToNodes(path, androidLibsNode);
			for (String path : def.libsHtml) pathToNodes(path, htmlLibsNode);
		}
	}

	private void pathToNodes(String path, DefaultMutableTreeNode parentNode) {
		String parentPath = (String) parentNode.getUserObject();
		String[] names = path.split("/");

		for (int i=0; i<names.length; i++) {
			if (i == 0) names[i] = parentPath + "/" + names[i];
			else names[i] = names[i-1] + "/" + names[i];

			if (i == names.length-1) names[i] = names[i].replaceFirst("#DIR#", "");

			DefaultMutableTreeNode node = nodes.get(names[i]);
			if (node == null) {
				node = new DefaultMutableTreeNode(names[i]);
				nodes.put(names[i], node);
				if (i == 0) parentNode.add(node);
				else nodes.get(names[i-1]).add(node);
			}
		}
	}

	private final TreeCellRenderer treeCellRenderer = new TreeCellRenderer() {
		private final JLabel label = new JLabel();

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
			if (node.getUserObject() instanceof String) {
				String name = (String) node.getUserObject();
				boolean isDir = name.startsWith("#DIR#");

				name = name.replaceFirst("#DIR#", "");
				name = name.replace("MyGame", cfg.getMainClassName());

				if (isDir && name.equals("prj-common")) name = cfg.getCommonPrjName();
				if (isDir && name.equals("prj-desktop")) name = cfg.getDesktopPrjName();
				if (isDir && name.equals("prj-android")) name = cfg.getAndroidPrjName();
				if (isDir && name.equals("prj-html")) name = cfg.getHtmlPrjName();

				label.setText(FilenameUtils.getName(name));
				label.setIcon(isDir ? Res.getImage("ic_folder.png") : Res.getImage("ic_file.png"));
			}

			return label;
		}
	};
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1494.java