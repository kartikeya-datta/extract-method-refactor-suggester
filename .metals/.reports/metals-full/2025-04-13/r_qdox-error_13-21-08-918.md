error id: <WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7481.java
<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7481.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7481.java
text:
```scala
.@@setHtml("<div style='font-family:heading;text-align:center'>TWL TextAreaTest</div><a href='badlogic'><img src='badlogic' id='badlogic' style='float:right; margin:10px'/></a>Lorem ipsum dolor sit amet, douchebagus joglus. Sed fermentum gravida turpis, sit amet gravida justo laoreet non. Donec ultrices suscipit metus a mollis. Mollis varius egestas quisque feugiat pellentesque mi, quis scelerisque velit bibendum eget. Nulla orci in enim nisl mattis varius dignissim fringilla.<br/><br/><img src='twllogo' style='float:left; margin:10px'/>Curabitur purus leo, ultricies ut cursus eget, adipiscing in quam. Duis non velit vel mauris vulputate fringilla et quis.<br/><br/>Suspendisse lobortis iaculis tellus id fermentum. Integer fermentum varius pretium. Nullam libero magna, mattis vel placerat ac, dignissim sed lacus. Mauris varius libero id neque auctor a auctor odio fringilla.<br/><br/><div>Mauris orci arcu, porta eget porttitor luctus, malesuada nec metus. Nunc fermentum viverra leo eu pretium. Curabitur vitae nibh massa, imperdiet egestas lectus. Nulla odio quam, lobortis eget fermentum non, faucibus ac mi. Morbi et libero nulla. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Aliquam sit amet rhoncus nulla. Morbi consectetur ante convallis ante tristique et porta ligula hendrerit. Donec rhoncus ornare augue, sit amet lacinia nulla auctor venenatis.</div><br/><div>Etiam semper egestas porta. Proin luctus porta faucibus. Curabitur sagittis, lorem nec imperdiet ullamcorper, sem risus consequat purus, non faucibus turpis lorem ut arcu. Nunc tempus lobortis enim vitae facilisis. Morbi posuere quam nec sem aliquam eleifend.</div>");


package com.badlogic.gdx.twl.tests;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.RenderListener;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.twl.renderer.TwlRenderer;

import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.FPSCounter;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.ScrollPane;
import de.matthiasmann.twl.TextArea;
import de.matthiasmann.twl.Timer;
import de.matthiasmann.twl.textarea.HTMLTextAreaModel;
import de.matthiasmann.twl.textarea.Style;
import de.matthiasmann.twl.textarea.StyleAttribute;
import de.matthiasmann.twl.textarea.TextAreaModel.Element;
import de.matthiasmann.twl.textarea.Value;

public class TextAreaTest implements RenderListener {
	GUI gui;

	public void surfaceCreated () {
		if (gui != null) return;

		final HTMLTextAreaModel htmlText = new HTMLTextAreaModel();
		TextArea textArea = new TextArea(htmlText);
		htmlText
			.setHtml("<div style='font-family:heading;text-align:center'>TWL TextAreaTest</div><a href='badlogic'><img src='badlogic' id='badlogic' style='float:right; margin:10px'/></a>Lorem ipsum dolor sit amet, douchebagus joglus. Sed fermentum gravida turpis, sit amet gravida justo laoreet non. Donec ultrices suscipit metus a mollis. Mollis varius egestas quisque feugiat pellentesque mi, quis scelerisque velit bibendum eget. Nulla orci in enim nisl mattis varius dignissim fringilla.<br/><br/><img src='twllogo' style='float:left; margin:10px'/>Curabitur purus leo, ultricies ut cursus eget, adipiscing in quam. Duis non velit vel mauris vulputate fringilla et quis.<br/><br/><div>Suspendisse lobortis iaculis tellus id fermentum. Integer fermentum varius pretium. Nullam libero magna, mattis vel placerat ac, dignissim sed lacus. Mauris varius libero id neque auctor a auctor odio fringilla.</div><br/><div>Mauris orci arcu, porta eget porttitor luctus, malesuada nec metus. Nunc fermentum viverra leo eu pretium. Curabitur vitae nibh massa, imperdiet egestas lectus. Nulla odio quam, lobortis eget fermentum non, faucibus ac mi. Morbi et libero nulla. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Aliquam sit amet rhoncus nulla. Morbi consectetur ante convallis ante tristique et porta ligula hendrerit. Donec rhoncus ornare augue, sit amet lacinia nulla auctor venenatis.</div><br/><div>Etiam semper egestas porta. Proin luctus porta faucibus. Curabitur sagittis, lorem nec imperdiet ullamcorper, sem risus consequat purus, non faucibus turpis lorem ut arcu. Nunc tempus lobortis enim vitae facilisis. Morbi posuere quam nec sem aliquam eleifend.</div>");
		ScrollPane scrollPane = new ScrollPane(textArea);
		scrollPane.setFixed(ScrollPane.Fixed.HORIZONTAL);
		FPSCounter fpsCounter = new FPSCounter(4, 2);

		DialogLayout layout = new DialogLayout();
		layout.setTheme("");
		layout.setHorizontalGroup(layout.createParallelGroup().addWidgets(scrollPane, fpsCounter));
		layout.setVerticalGroup(layout.createSequentialGroup().addWidget(scrollPane).addGap(5).addWidget(fpsCounter).addGap(5));

		gui = TwlRenderer.createGUI(layout, "data/widgets.xml", FileType.Internal);

		textArea.addCallback(new TextArea.Callback() {
			Timer timer;
			int speed = 8, size = 256;

			public void handleLinkClicked (String href) {
				final Element element = htmlText.getElementById("badlogic");
				if (timer == null) {
					timer = gui.createTimer();
					timer.setDelay(32);
					timer.setContinuous(true);
					timer.setCallback(new Runnable() {
						public void run () {
							size += speed;
							if (size == 256 || size == 128) timer.stop();
							Style style = element.getStyle();
							style = style.with(StyleAttribute.WIDTH, new Value(size, Value.Unit.PX));
							style = style.with(StyleAttribute.HEIGHT, new Value(size, Value.Unit.PX));
							element.setStyle(style);
							htmlText.domModified();
						}
					});
				}
				if (timer.isRunning()) return;
				timer.start();
				speed = -speed;
			}
		});
	}

	public void render () {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gui.update();
	}

	public void surfaceChanged (int width, int height) {
		gui.setSize();
	}

	public void dispose () {
		gui.destroy();
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
	scala.meta.internal.mtags.MtagsIndexer.index(MtagsIndexer.scala:21)
	scala.meta.internal.mtags.MtagsIndexer.index$(MtagsIndexer.scala:20)
	scala.meta.internal.mtags.JavaMtags.index(JavaMtags.scala:38)
	scala.meta.internal.tvp.IndexedSymbols.javaSymbols(IndexedSymbols.scala:111)
	scala.meta.internal.tvp.IndexedSymbols.workspaceSymbolsFromPath(IndexedSymbols.scala:120)
	scala.meta.internal.tvp.IndexedSymbols.$anonfun$workspaceSymbols$2(IndexedSymbols.scala:146)
	scala.collection.concurrent.TrieMap.getOrElseUpdate(TrieMap.scala:960)
	scala.meta.internal.tvp.IndexedSymbols.$anonfun$workspaceSymbols$1(IndexedSymbols.scala:146)
	scala.meta.internal.tvp.IndexedSymbols.withTimer(IndexedSymbols.scala:71)
	scala.meta.internal.tvp.IndexedSymbols.workspaceSymbols(IndexedSymbols.scala:143)
	scala.meta.internal.tvp.FolderTreeViewProvider.$anonfun$projects$9(MetalsTreeViewProvider.scala:306)
	scala.collection.Iterator$$anon$9.next(Iterator.scala:584)
	scala.collection.Iterator$$anon$10.nextCur(Iterator.scala:594)
	scala.collection.Iterator$$anon$10.hasNext(Iterator.scala:608)
	scala.collection.Iterator$$anon$6.hasNext(Iterator.scala:477)
	scala.collection.Iterator$$anon$10.hasNext(Iterator.scala:601)
	scala.collection.Iterator$$anon$8.hasNext(Iterator.scala:562)
	scala.collection.immutable.List.prependedAll(List.scala:155)
	scala.collection.immutable.List$.from(List.scala:685)
	scala.collection.immutable.List$.from(List.scala:682)
	scala.collection.SeqFactory$Delegate.from(Factory.scala:306)
	scala.collection.immutable.Seq$.from(Seq.scala:42)
	scala.collection.IterableOnceOps.toSeq(IterableOnce.scala:1473)
	scala.collection.IterableOnceOps.toSeq$(IterableOnce.scala:1473)
	scala.collection.AbstractIterator.toSeq(Iterator.scala:1306)
	scala.meta.internal.tvp.ClasspathTreeView.children(ClasspathTreeView.scala:62)
	scala.meta.internal.tvp.FolderTreeViewProvider.getProjectRoot(MetalsTreeViewProvider.scala:390)
	scala.meta.internal.tvp.MetalsTreeViewProvider.$anonfun$children$1(MetalsTreeViewProvider.scala:84)
	scala.collection.immutable.List.map(List.scala:247)
	scala.meta.internal.tvp.MetalsTreeViewProvider.children(MetalsTreeViewProvider.scala:84)
	scala.meta.internal.metals.WorkspaceLspService.$anonfun$treeViewChildren$1(WorkspaceLspService.scala:705)
	scala.concurrent.Future$.$anonfun$apply$1(Future.scala:687)
	scala.concurrent.impl.Promise$Transformation.run(Promise.scala:467)
	java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
	java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
	java.base/java.lang.Thread.run(Thread.java:840)
```
#### Short summary: 

QDox parse error in <WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7481.java