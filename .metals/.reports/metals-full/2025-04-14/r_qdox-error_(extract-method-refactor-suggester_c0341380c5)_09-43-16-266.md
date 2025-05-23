error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3579.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3579.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3579.java
text:
```scala
t@@ry {tmpUrl = new URL("http://libgdx.badlogicgames.com/nightlies/config/config.txt");}

package aurelienribon.libgdx.ui;

import aurelienribon.ui.components.PaintedPanel;
import aurelienribon.ui.css.Style;
import aurelienribon.utils.HttpUtils;
import aurelienribon.utils.ParseUtils;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import res.Res;

/**
 * @author Aurelien Ribon | http://www.aurelienribon.com/
 */
public class MainPanel extends PaintedPanel {
    public MainPanel() {
        initComponents();

		Style.registerCssClasses(this, ".rootPanel");
		Style.registerCssClasses(librarySetupPanel, ".groupPanel", "#librarySetupPanel");
		Style.registerCssClasses(configPanel, ".groupPanel", "#configPanel");
		Style.registerCssClasses(resultPanel, ".groupPanel", "#resultPanel");
		Style.registerCssClasses(goPanel, ".groupPanel", "#goPanel");
		Style.registerCssClasses(versionLabel, "#versionLabel");
		Style.apply(this, new Style(Res.getUrl("css/style.css")));

		librarySetupPanel.init();
		goPanel.init();

		checkUpdates();
    }

	private void checkUpdates() {
		final String version = "2.0.1";
		versionLabel.setText("v" + version + " (...)");

		URL tmpUrl;
		//try {tmpUrl = new URL("http://www.aurelienribon.com/libgdx-setup/config.txt");}
		try {tmpUrl = new URL("http://libgdx.googlecode.com/svn/trunk/extensions/gdx-setup-ui/config/config.txt");}
		catch (MalformedURLException ex) {throw new RuntimeException(ex);}

		final URL url = tmpUrl;
		final ByteArrayOutputStream stream = new ByteArrayOutputStream();

		final HttpUtils.Callback callback = new HttpUtils.Callback() {
			@Override public void canceled() {}
			@Override public void updated(int length, int totalLength) {}
			@Override public void completed() {
				try {testUpdate(version, stream.toString("UTF-8"));}
				catch (UnsupportedEncodingException ex) {System.err.println("[error] " + ex.getMessage());
				}
			}
			@Override public void error(IOException ex) {
				versionLabel.setText("v" + version + " (error1)");
				Style.registerCssClasses(versionLabel, ".versionLabelError");
				Style.apply(versionLabel, new Style(Res.getUrl("css/style.css")));
			}
		};

		HttpUtils.downloadAsync(url, stream, callback);
	}

	private void testUpdate(String version, String str) {
		List<String> versions = ParseUtils.parseBlockAsList(str, "versions");
		int versionIdx = versions.indexOf(version);

		MouseListener mouseListener = new MouseAdapter() {
			@Override public void mousePressed(MouseEvent e) {
				if (Desktop.isDesktopSupported()) {
					try {
						URI uri = new URI("http://code.google.com/p/libgdx/");
						Desktop.getDesktop().browse(uri);
					} catch (IOException ex) {
					} catch (URISyntaxException ex) {
					}
				}
			}
		};

		if (versionIdx == 0) {
			versionLabel.setText("v" + version + " (ok)");
			Style.registerCssClasses(versionLabel, ".versionLabelNoUpdate");
			Style.apply(versionLabel, new Style(Res.getUrl("css/style.css")));
		} else if (versionIdx > 0) {
			versionLabel.setText("v" + version + " (update!)");
			versionLabel.addMouseListener(mouseListener);
			versionLabel.setToolTipText("Update found: v" + versions.get(0));
			Style.registerCssClasses(versionLabel, ".versionLabelUpdateFound");
			Style.apply(versionLabel, new Style(Res.getUrl("css/style.css")));
		} else {
			versionLabel.setText("v" + version + " (error2)");
			Style.registerCssClasses(versionLabel, ".versionLabelError");
			Style.apply(versionLabel, new Style(Res.getUrl("css/style.css")));
		}
	}

	// -------------------------------------------------------------------------
	// Generated stuff
	// -------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        groupsWrapper = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        configPanel = new aurelienribon.libgdx.ui.ConfigPanel();
        logoLabel = new javax.swing.JLabel();
        versionLabel = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        librarySetupPanel = new aurelienribon.libgdx.ui.LibrarySetupPanel();
        jPanel1 = new javax.swing.JPanel();
        goPanel = new aurelienribon.libgdx.ui.GoPanel();
        resultPanel = new aurelienribon.libgdx.ui.ResultPanel();

        setLayout(new java.awt.BorderLayout());

        groupsWrapper.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        groupsWrapper.setOpaque(false);
        groupsWrapper.setLayout(new java.awt.GridLayout(1, 0, 15, 0));

        jPanel4.setOpaque(false);

        logoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/gfx/logo.png"))); // NOI18N

        versionLabel.setText("v1.0.0");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(logoLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(versionLabel))
            .addComponent(configPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(configPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 134, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(logoLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(versionLabel, javax.swing.GroupLayout.Alignment.TRAILING)))
        );

        groupsWrapper.add(jPanel4);

        jPanel3.setOpaque(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(librarySetupPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(librarySetupPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE)
        );

        groupsWrapper.add(jPanel3);

        jPanel1.setOpaque(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(resultPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
            .addComponent(goPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(resultPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(goPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        groupsWrapper.add(jPanel1);

        add(groupsWrapper, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private aurelienribon.libgdx.ui.ConfigPanel configPanel;
    private aurelienribon.libgdx.ui.GoPanel goPanel;
    private javax.swing.JPanel groupsWrapper;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private aurelienribon.libgdx.ui.LibrarySetupPanel librarySetupPanel;
    private javax.swing.JLabel logoLabel;
    private aurelienribon.libgdx.ui.ResultPanel resultPanel;
    private javax.swing.JLabel versionLabel;
    // End of variables declaration//GEN-END:variables

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3579.java