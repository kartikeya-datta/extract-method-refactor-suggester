error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3475.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3475.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,16]

error in qdox parser
file content:
```java
offset: 16
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3475.java
text:
```scala
private static i@@nt DELAY = 100;

// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Library General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

package org.columba.core.gui.statusbar;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.columba.core.config.Config;
import org.columba.core.config.ConfigPath;
import org.columba.core.config.ThemeItem;
import org.columba.core.gui.util.ImageLoader;
import org.columba.core.gui.util.ToolbarButton;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */
public class ImageSequenceTimer extends ToolbarButton implements ActionListener {
	private javax.swing.Timer timer;
	private ImageIcon[] images;
	private ImageIcon restImage;
	private int frameNumber;
	private int frameCount;

	private Dimension scale;

	private static int DELAY = 50;

	private int imageWidth;
	private int imageHeight;

	public ImageSequenceTimer() {
		super();

		timer = new javax.swing.Timer(DELAY, this);
		timer.setInitialDelay(0);
		timer.setCoalesce(true);
		setMargin(new Insets(0, 0, 0, 0));
		setRolloverEnabled(true);
		setBorder(null);
		setContentAreaFilled(false);
		

		setRequestFocusEnabled(false);
		init();

	}
	
	
	public boolean isFocusTraversable() {
		return isRequestFocusEnabled();
	}


	protected void initDefault() {
		frameCount = 60;
		frameNumber = 1;

		imageWidth = 36;
		imageHeight = 36;

		//setPreferredSize(new Dimension(imageWidth, imageHeight));
		/*
		setMinimumSize(new Dimension(width, height));
		*/
		//setMaximumSize(new Dimension(imageWidth, imageHeight));

		images = new ImageIcon[frameCount];

		for (int i = 0; i < frameCount; i++) {
			StringBuffer buf = new StringBuffer();

			if (i < 10)
				buf.append("00");
			if ((i >= 10) && (i < 100))
				buf.append("0");

			buf.append(Integer.toString(i));

			buf.append(".png");

			images[i] = ImageLoader.getImageIcon(buf.toString());
		}

		restImage = ImageLoader.getImageIcon("rest.png");

		setIcon(restImage);

	}

	protected void init() {
		ThemeItem item = Config.getOptionsConfig().getThemeItem();
		//String pulsator = item.getPulsator();
		String pulsator = "default";

		if (pulsator.toLowerCase().equals("default"))
			initDefault();
		else {
			try {
				File zipFile =
					new File(
						ConfigPath.getConfigDirectory()
							+ "/pulsators/"
							+ pulsator
							+ ".jar");

				String zipFileEntry =
					new String(pulsator + "/pulsator.properties");
				//System.out.println("zipfileentry:"+zipFileEntry );

				Properties properties =
					ImageLoader.loadProperties(zipFile, zipFileEntry);

				String frameCountStr = (String) properties.getProperty("count");
				frameCount = Integer.parseInt(frameCountStr);

				String widthStr = (String) properties.getProperty("width");
				imageWidth = Integer.parseInt(widthStr);

				String heightStr = (String) properties.getProperty("height");
				imageHeight = Integer.parseInt(heightStr);

				/*
				setPreferredSize(new Dimension(width, height));
				setMinimumSize(new Dimension(width, height));
				setMaximumSize(new Dimension(width, height));
				*/

				images = new ImageIcon[frameCount];
				for (int i = 0; i < frameCount; i++) {
					String istr = (new Integer(i)).toString();
					String image = (String) properties.getProperty(istr);

					zipFile =
						new File(
							ConfigPath.getConfigDirectory()
								+ "/pulsators/"
								+ pulsator
								+ ".jar");

					zipFileEntry = new String(pulsator + "/" + image);

					//System.out.println("zuifileentry:"+zipFileEntry);
					images[i] =
						new ImageIcon(
							ImageLoader.loadImage(zipFile, zipFileEntry));
				}

				String image = (String) properties.getProperty("rest");
				zipFileEntry = new String(pulsator + "/" + image);

				restImage =
					new ImageIcon(ImageLoader.loadImage(zipFile, zipFileEntry));
			} catch (Exception ex) {
				StringBuffer buf = new StringBuffer();
				buf.append("Error while loading pulsator icons!");
				JOptionPane.showMessageDialog(null, buf.toString());

				//Config.getOptionsConfig().getThemeItem().setPulsator("default");

				initDefault();
			}
		}

	}

	public void start() {
		if (!timer.isRunning())
			timer.start();
	}

	public void stop() {
		if (timer.isRunning())
			timer.stop();

		frameNumber = 0;

		setIcon(restImage);
	}

	public void actionPerformed(ActionEvent ev) {
		String action = ev.getActionCommand();

		frameNumber++;

		if (timer.isRunning())
			setIcon(new ImageIcon(images[frameNumber % frameCount].getImage()));
		else
			setIcon(restImage);

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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3475.java