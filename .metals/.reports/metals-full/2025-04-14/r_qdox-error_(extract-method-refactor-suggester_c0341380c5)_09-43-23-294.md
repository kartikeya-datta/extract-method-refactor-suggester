error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2156.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2156.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2156.java
text:
```scala
J@@Label topCenter = new JLabel("ArgoUML v" + ArgoVersion.getVersion(),

// Copyright (c) 1996-2002 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.ui;
import org.argouml.application.ArgoVersion;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

import org.tigris.gef.util.ResourceLoader;

class SplashPanel extends JPanel {

  ImageIcon splashImage = null;
  public SplashPanel(String iconName) {
    super();
    splashImage = ResourceLoader.lookupIconResource(iconName);

    //
    // JWindow does not allow setting title or icon.
    //
    // ImageIcon argoImage = ResourceLoader.lookupIconResource("Model");
    // this.setIconImage(argoImage.getImage());
    // if (title != null) setTitle(title);

    JPanel topNorth = new JPanel(new BorderLayout());
    topNorth.setPreferredSize(new Dimension(6,6));
    topNorth.setBorder(new BevelBorder(BevelBorder.RAISED));
    topNorth.add(new JLabel(""), BorderLayout.CENTER);

    JPanel topSouth = new JPanel(new BorderLayout());
    topSouth.setPreferredSize(new Dimension(6,6));
    topSouth.setBorder(new BevelBorder(BevelBorder.RAISED));
    topSouth.add(new JLabel(""), BorderLayout.CENTER);

    JLabel topCenter = new JLabel("ArgoUML v" + ArgoVersion.VERSION,
		                  SwingConstants.CENTER);
    // 40 works for 0.10
    topCenter.setFont(new Font("SansSerif", Font.BOLD, 35));
    topCenter.setPreferredSize(new Dimension(60, 60));
    topCenter.setOpaque(false);
    topCenter.setForeground(Color.white);

    JPanel top = new JPanel(new BorderLayout());
    top.setBackground(Color.darkGray);
    top.add(topNorth, BorderLayout.NORTH);
    top.add(topCenter, BorderLayout.CENTER);
    top.add(topSouth, BorderLayout.SOUTH);

    JLabel splashButton = new JLabel("");
    if (splashImage != null) {
      // int imgWidth = splashImage.getIconWidth();
      // int imgHeight = splashImage.getIconHeight();
      // Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
      // setLocation(scrSize.width/2 - imgWidth/2,
		       // scrSize.height/2 - imgHeight/2);
      splashButton.setIcon(splashImage);
    }
    // setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    setLayout(new BorderLayout(0, 0));
    //splashButton.setMargin(new Insets(0, 0, 0, 0));
    // JPanel main = new JPanel(new BorderLayout());
    // setBorder(new EtchedBorder(EtchedBorder.RAISED));
    add(top, BorderLayout.NORTH);
    add(splashButton, BorderLayout.CENTER);
    // add(_statusBar, BorderLayout.SOUTH);
  }

  public ImageIcon getImage() {
     return splashImage;
  }

} /* end class SplashPanel */
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2156.java