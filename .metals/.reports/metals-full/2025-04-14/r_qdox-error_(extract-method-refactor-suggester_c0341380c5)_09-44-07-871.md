error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4234.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4234.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4234.java
text:
```scala
t@@hrow new JHotDrawRuntimeException("Iconkit instance isn't set");

/*
 * @(#)ToolButton.java 5.2
 *
 */

package CH.ifa.draw.standard;

import javax.swing.*;
import java.awt.*;
import CH.ifa.draw.util.*;
import CH.ifa.draw.framework.*;

/**
 * A PaletteButton that is associated with a tool.
 * @see Tool
 */
public class ToolButton extends PaletteButton {

    private String          fName;
    private Tool            fTool;
    private PaletteIcon     fIcon;

    public ToolButton(PaletteListener listener, String iconName, String name, Tool tool) {
        super(listener);
        // use a Mediatracker to ensure that all the images are initially loaded
        Iconkit kit = Iconkit.instance();
        if (kit == null)
            throw new HJDError("Iconkit instance isn't set");

        Image im[] = new Image[3];
        im[0] = kit.loadImageResource(iconName+"1.gif");
        im[1] = kit.loadImageResource(iconName+"2.gif");
        im[2] = kit.loadImageResource(iconName+"3.gif");

        MediaTracker tracker = new MediaTracker(this);
        for (int i = 0; i < 3; i++) {
            tracker.addImage(im[i], i);
        }
        try {
            tracker.waitForAll();
        } catch (Exception e) {  }

        fIcon = new PaletteIcon(new Dimension(24,24), im[0], im[1], im[2]);
        fTool = tool;
        fName = name;

        setIcon(new ImageIcon(im[0]));
        setPressedIcon(new ImageIcon(im[1]));
        setSelectedIcon(new ImageIcon(im[2]));
        setToolTipText(name);
    }

    public Tool tool() {
        return fTool;
    }

    public String name() {
        return fName;
    }

    public Object attributeValue() {
        return tool();
    }

    public Dimension getMinimumSize() {
        return new Dimension(fIcon.getWidth(), fIcon.getHeight());
    }

    public Dimension getPreferredSize() {
        return new Dimension(fIcon.getWidth(), fIcon.getHeight());
    }
    
    public Dimension getMaximumSize() {
        return new Dimension(fIcon.getWidth(), fIcon.getHeight());
    }

//  Not necessary anymore in JFC due to the support of Icons in JButton
/*
    public void paintBackground(Graphics g) { }

    public void paintNormal(Graphics g) {
        if (fIcon.normal() != null)
            g.drawImage(fIcon.normal(), 0, 0, this);
    }

    public void paintPressed(Graphics g) {
        if (fIcon.pressed() != null)
            g.drawImage(fIcon.pressed(), 0, 0, this);
    }
*/
    public void paintSelected(Graphics g) {
        if (fIcon.selected() != null)
            g.drawImage(fIcon.selected(), 0, 0, this);
    }

    public void paint(Graphics g) {
    	// selecting does not work as expected with JFC1.1
    	// see JavaBug: 4228035, 4233965
    	if (isSelected()) {
        	paintSelected(g);
    	}
    	else {
	    	super.paint(g);
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4234.java