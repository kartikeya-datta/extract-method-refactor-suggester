error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3230.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3230.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3230.java
text:
```scala
a@@fter() returning : call(void Game.clockTick()) {

/*

Copyright (c) Xerox Corporation 1998-2002.  All rights reserved.

Use and copying of this software and preparation of derivative works based
upon this software are permitted.  Any distribution of this software or
derivative works must comply with all applicable United States export control
laws.

This software is made available AS IS, and Xerox Corporation makes no warranty
about the software, its performance or its conformity to any specification.

<---            this code is formatted to fit into 80 columns             --->|
<---            this code is formatted to fit into 80 columns             --->|
<---            this code is formatted to fit into 80 columns             --->|


Display.java
Part of the Spacewar system.
*/

package spacewar;

import java.util.Vector;
import java.util.Enumeration;
import java.awt.Graphics;
import java.awt.Canvas;
import java.awt.Image;
import java.awt.event.KeyListener;

/**
 * The display aspects capture the look and feel of the Game in modular
 * pluggable units.
 *
 * The model is that constructing a concrete subclass of Display attaches that
 * kind of display to the game.  It will Display the game as it goes along.
 * A game can have any number of displays.  Any of the displays will accept
 * keyboard input.
 *
 */

class Display  extends Canvas {

    private static Vector DISPLAYS = new Vector(2);
    private static Vector PLAYERS  = new Vector(2);
    private static Pilot  pilot1, pilot2;

    Game     game;
    SWFrame  frame;
    Image    offImage;
    Graphics offGraphics;

    Game  getGame()   { return game; }
    static Pilot getPilot1() { return pilot1; }
    static Pilot getPilot2() { return pilot2; }

    Display(Game g) {
        super();
        game = g;

        frame = new SWFrame(game, this);
        DISPLAYS.addElement(this);
    }


    void noticeSizeChange() {
        initializeOffImage();
    }

    private void initializeOffImage () {
        int w = getSize().width;
        int h = getSize().height;
        if ( w > 0 & h > 0) {
            offImage = createImage(w, h);
            offGraphics = offImage.getGraphics();
        }
    }

    /*
     * In our double buffering scheme, painting just means copying the buffer
     * to the screen.  The Display aspect draws into the buffer.
     */
    public void paint(Graphics g) {
        if (offImage != null)
            g.drawImage(offImage, 0, 0, null);
    }

    public void update(Graphics g) {
        /*
         * There are 4 steps to this:
         *  - clear the double buffer
         *  - paint the objects into the double buffer
         *  - paint the status into the double buffer
         *  - paint the doublebuffer into the buffer
         */
        offGraphics.setColor(getBackground());
        offGraphics.fillRect(0, 0, getBounds().width, getBounds().height);
        paintObjects(offGraphics);
        paintStatus(offGraphics);
        g.drawImage(offImage, 0, 0, null);
    }

    void paintObjects(Graphics g) { }
    void paintStatus(Graphics g) {}

    static aspect DisplayAspect {

        after (String mode) returning (Game game): call(Game+.new(String)) && args(mode) {
            new Display1(game);
            new Display2(game);

            if ( mode.equals("1") ) {
                pilot1 = game.newPlayer(1);
            }
            else if ( mode.equals("2") ) {
                pilot1 = game.newPlayer(1);
                pilot2 = game.newPlayer(2);
            }
            else if (mode. equals("demo")) {
                pilot1 = game.newRobot(1);
                pilot2 = game.newRobot(2);
            } else {
                game.error("Invalid mode: " + mode);
                game.quit();
            }
        }


        /*
         * I'm not really sure this belongs here.
         *
         * Being here what it does is makes the Display aspect
         * responsible for having the Players couple up to it.  That's
         * kind of nice, but its a bit incomplete, since Player is
         * really part of the GUI, not part of the core Game.
         *
         * In a future re-factoring this will get worked out better.
         * What will happen is that GUI will be an aspect that has the
         * core GUI.  Each of the different kinds of displays will be
         * aspects that tie themselves in.
         */
        after () returning (Player player): call(Player+.new(..)) {
            Enumeration elements = DISPLAYS.elements();
            while ( elements.hasMoreElements() ) {
                Display display = (Display)elements.nextElement();
                display.addKeyListener(player);
            }
        }

        after() returning (Display display): call(Display+.new(..)) {
            display.noticeSizeChange();
        }

        after(Display display) returning (): call(void setSize(..)) && target(display) {
            display.noticeSizeChange();
        }

        after(): call(void Game.clockTick()) {
            Enumeration elements = DISPLAYS.elements();
            while ( elements.hasMoreElements() ) {
                Display display = (Display)elements.nextElement();
                display.repaint();
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3230.java