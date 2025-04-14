error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5604.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5604.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5604.java
text:
```scala
i@@f (!input.oldButtons[Input.ESCAPE] && input.buttons[Input.ESCAPE]) {

package com.mojang.metagun.screen;

import com.badlogic.gdx.Gdx;
import com.mojang.metagun.Art;
import com.mojang.metagun.Input;

public class SignReadScreen extends Screen {
    private Screen parent;
    
    private String[][] signs = {
            {
                "READING",
                "", 
                "PRESS UP TO READ SIGNS"
            },
            {
                "JUMPING",
                "", 
                "PRESS Z TO JUMP",
                "YOU CAN JUMP HIGHER BY",
                "GETTING A RUNNING START",
                "OR HOLDING DOWN Z",
            },
            {
                "PROGRESSING",
                "", 
                "LEAVE A ROOM THROUGH ANY",
                "EXIT TO CONTINUE YOUR",
                "ADVENTURE",
            },
            {
                "DYING",
                "", 
                "IF YOU DIE, YOU RESTART",
                "AT THE BEGINNING OF THE",
                "CURRENT ROOM",
            },
            {
                "DODGING",
                "", 
                "THE GUNNERS DON'T LIKE YOU",
                "AND SHOOT AT YOU.",
                "IT WOULD BE WISE TO STAY AWAY",
            },    
            {
                "THE LAUNCHER",
                "", 
                "AS YOU PICK UP THE LAUNCHER,",
                "YOU REALIZE IT'S NOT YOUR",
                "AVERAGE LAUNCHER.",
                "",
                "PRESS UP AND DOWN TO AIM",
                "PRESS X TO FIRE THE LAUNCHER",
            },      
            {
                "JONESING",
                "", 
                "DON'T FORGET YOUR FEDORA!",
            },
            {
                "EXPLODING",
                "", 
                "TNT BLOCKS ARE HIGHLY",
                "EXPLOSIVE, AND WILL",
                "REACT POORLY TO BEING",
                "SHOT.",
            },              
            {
                "PUSHING",
                "", 
                "THE CAMARADERIE BOX IS",
                "SOMETHING SOMETHING",
                "",
                "IT'S FROM PORTAL.",
            },              
            {
                "BATTLING",
                "", 
                "THE GREMLIN IS LARGE",
                "AND IN YOUR WAY.",
                "OVERHEAT IT TO DESTROY",
                "IT AND CLAIM YOUR PRIZE",
            },      
            {
                "EVADING",
                "", 
                "THE GUNNERS SHOTS WILL",
                "PASS THROUGH GLASS.",
                "YOU, HOWEVER, WILL NOT",
            },         
            {
                "SWEATING",
                "", 
                "THESE SLIGHTLY MORE",
                "SOPHISTICATED GREMLINS",
                "HAVE LEARNED A NEW",
                "TRICK",
            },
            {
                "CONVEYING",
                "", 
                "TIME TO BURN OFF SOME",
                "FAT AND HAVE FUN WHILE",
                "DOING IT!",
            },          
            {
                "BOSSFIGHTING",
                "", 
                "BEHIND THIS DOOR, MEGAN",
                "AWAITS! WHO IS MEGAN?",
                "ARE YOU MEGAN?",
            },            
            {
                "THE NEW LAUNCHER",
                "",
                "WELL, THIS IS BAD."
            },               
            {
                "FEEDING",
                "",
                "THE JABBERWOCKY IS",
                "HUNGRY, AND WILL EAT",
                "WAY MORE THAN IT SHOULD",
                "",
                "PLEASE DO NOT FEED!",
            },               
            {
                "HOVERING",
                "",
                "THE RECOIL ON THE NEW",
                "LAUNCHER SURE IS",
                "POWERFUL!",
            },
            {
                "FLYING",
                "",
                "SERIOUSLY, THE RECOIL",
                "IS OUT OF THIS WORLD!",
            },             
            {
                "WINNING",
                "",
                "YOUR FINAL CHALLENGE",
                "IS RIGHT DOWN THIS",
                "HALLWAY.",
            }, 
            {
                "FRESHERERST",
                "",
                "BIG ADAM, GIANT SISTER.",
                "IT IS KNOWN BY MANY NAMES",
                "BUT JUDITH 4HRPG BLUEBERRY.",
                "",
                "FISSION MAILED!",
            }, 
    };
    
    private int delay = 15;
    private int id;
    public SignReadScreen(Screen parent, int id) {
        this.parent = parent;
        this.id = id;
    }
    
    public void render() {
        parent.render();
        spriteBatch.begin();
        int xs = 0;
        int ys = signs[id].length+3;
        for (int y=0; y<signs[id].length; y++) {
            int s = signs[id][y].length();
            if (s>xs) xs = s;
        }
        int xp = 160-xs*3;
        int yp = 120-ys*3;
        for (int x=0-1; x<xs+1; x++) {
            for (int y=0-1; y<ys+1; y++) {
                int xf = 1;
                int yf = 12;
                if (x<0) xf--;
                if (y<0) yf--;
                if (x>=xs) xf++;
                if (y>=ys) yf++;
                draw(Art.guys[xf][yf], xp+x*6, yp+y*6);
            }
        }
        for (int y=0; y<signs[id].length; y++) {
            drawString(signs[id][y], xp, yp+y*6);
        }
        if (delay==0)
        drawString("PRESS X", xp+(xs-8)*6, yp+(signs[id].length+2)*6);
        spriteBatch.end();
    }
    
    public void tick(Input input) {
        if (!input.oldButtons[Input.ESCAPE] && input.buttons[Input.ESCAPE] || Gdx.input.isTouched()) {
            setScreen(parent);
            return;
        }
        if (delay>0) delay--;
        if (delay==0 && input.buttons[Input.SHOOT] && !input.oldButtons[Input.SHOOT]) {
            setScreen(parent);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5604.java