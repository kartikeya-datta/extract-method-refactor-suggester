error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1735.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1735.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1735.java
text:
```scala
S@@tringBuilder result = new StringBuilder();

/*
                    Egothor Software License version 1.00
                    Copyright (C) 1997-2004 Leo Galambos.
                 Copyright (C) 2002-2004 "Egothor developers"
                      on behalf of the Egothor Project.
                             All rights reserved.

   This  software  is  copyrighted  by  the "Egothor developers". If this
   license applies to a single file or document, the "Egothor developers"
   are the people or entities mentioned as copyright holders in that file
   or  document.  If  this  license  applies  to the Egothor project as a
   whole,  the  copyright holders are the people or entities mentioned in
   the  file CREDITS. This file can be found in the same location as this
   license in the distribution.

   Redistribution  and  use  in  source and binary forms, with or without
   modification, are permitted provided that the following conditions are
   met:
    1. Redistributions  of  source  code  must retain the above copyright
       notice, the list of contributors, this list of conditions, and the
       following disclaimer.
    2. Redistributions  in binary form must reproduce the above copyright
       notice, the list of contributors, this list of conditions, and the
       disclaimer  that  follows  these  conditions  in the documentation
       and/or other materials provided with the distribution.
    3. The name "Egothor" must not be used to endorse or promote products
       derived  from  this software without prior written permission. For
       written permission, please contact Leo.G@seznam.cz
    4. Products  derived  from this software may not be called "Egothor",
       nor  may  "Egothor"  appear  in  their name, without prior written
       permission from Leo.G@seznam.cz.

   In addition, we request that you include in the end-user documentation
   provided  with  the  redistribution  and/or  in the software itself an
   acknowledgement equivalent to the following:
   "This product includes software developed by the Egothor Project.
    http://egothor.sf.net/"

   THIS  SOFTWARE  IS  PROVIDED  ``AS  IS''  AND ANY EXPRESSED OR IMPLIED
   WARRANTIES,  INCLUDING,  BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
   MERCHANTABILITY  AND  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
   IN  NO  EVENT  SHALL THE EGOTHOR PROJECT OR ITS CONTRIBUTORS BE LIABLE
   FOR   ANY   DIRECT,   INDIRECT,  INCIDENTAL,  SPECIAL,  EXEMPLARY,  OR
   CONSEQUENTIAL  DAMAGES  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
   SUBSTITUTE  GOODS  OR  SERVICES;  LOSS  OF  USE,  DATA, OR PROFITS; OR
   BUSINESS  INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
   WHETHER  IN  CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
   OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
   IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

   This  software  consists  of  voluntary  contributions  made  by  many
   individuals  on  behalf  of  the  Egothor  Project  and was originally
   created by Leo Galambos (Leo.G@seznam.cz).
 */
package org.egothor.stemmer;

/**
 * The Diff object generates a patch string.
 * <p>
 * A patch string is actually a command to a stemmer telling it how to reduce a
 * word to its root. For example, to reduce the word teacher to its root teach
 * the patch string Db would be generated. This command tells the stemmer to
 * delete the last 2 characters from the word teacher to reach the stem (the
 * patch commands are applied starting from the last character in order to save
 */
public class Diff {
  int sizex = 0;
  int sizey = 0;
  int net[][];
  int way[][];
  
  int INSERT;
  int DELETE;
  int REPLACE;
  int NOOP;
  
  /**
   * Constructor for the Diff object.
   */
  public Diff() {
    this(1, 1, 1, 0);
  }
  
  /**
   * Constructor for the Diff object
   * 
   * @param ins Description of the Parameter
   * @param del Description of the Parameter
   * @param rep Description of the Parameter
   * @param noop Description of the Parameter
   */
  public Diff(int ins, int del, int rep, int noop) {
    INSERT = ins;
    DELETE = del;
    REPLACE = rep;
    NOOP = noop;
  }
  
  /**
   * Apply the given patch string <tt>diff</tt> to the given string <tt>
   * dest</tt>.
   * 
   * @param dest Destination string
   * @param diff Patch string
   */
  public static void apply(StringBuilder dest, CharSequence diff) {
    try {
      
      if (diff == null) {
        return;
      }

      int pos = dest.length() - 1;
      if (pos < 0) {
        return;
      }
      // orig == ""
      for (int i = 0; i < diff.length() / 2; i++) {
        char cmd = diff.charAt(2 * i);
        char param = diff.charAt(2 * i + 1);
        int par_num = (param - 'a' + 1);
        switch (cmd) {
          case '-':
            pos = pos - par_num + 1;
            break;
          case 'R':
            dest.setCharAt(pos, param);
            break;
          case 'D':
            int o = pos;
            pos -= par_num - 1;
            /*
             * delete par_num chars from index pos
             */
            // String s = orig.toString();
            // s = s.substring( 0, pos ) + s.substring( o + 1 );
            // orig = new StringBuffer( s );
            dest.delete(pos, o + 1);        
            break;
          case 'I':
            dest.insert(pos += 1, param);
            break;
        }
        pos--;
      }
    } catch (StringIndexOutOfBoundsException x) {
      // x.printStackTrace();
    } catch (ArrayIndexOutOfBoundsException x) {
      // x.printStackTrace();
    }
  }
  
  /**
   * Construct a patch string that transforms a to b.
   * 
   * @param a String 1st string
   * @param b String 2nd string
   * @return String
   */
  public synchronized String exec(String a, String b) {
    if (a == null || b == null) {
      return null;
    }
    
    int x;
    int y;
    int maxx;
    int maxy;
    int go[] = new int[4];
    final int X = 1;
    final int Y = 2;
    final int R = 3;
    final int D = 0;
    
    /*
     * setup memory if needed => processing speed up
     */
    maxx = a.length() + 1;
    maxy = b.length() + 1;
    if ((maxx >= sizex) || (maxy >= sizey)) {
      sizex = maxx + 8;
      sizey = maxy + 8;
      net = new int[sizex][sizey];
      way = new int[sizex][sizey];
    }
    
    /*
     * clear the network
     */
    for (x = 0; x < maxx; x++) {
      for (y = 0; y < maxy; y++) {
        net[x][y] = 0;
      }
    }
    
    /*
     * set known persistent values
     */
    for (x = 1; x < maxx; x++) {
      net[x][0] = x;
      way[x][0] = X;
    }
    for (y = 1; y < maxy; y++) {
      net[0][y] = y;
      way[0][y] = Y;
    }
    
    for (x = 1; x < maxx; x++) {
      for (y = 1; y < maxy; y++) {
        go[X] = net[x - 1][y] + DELETE;
        // way on x costs 1 unit
        go[Y] = net[x][y - 1] + INSERT;
        // way on y costs 1 unit
        go[R] = net[x - 1][y - 1] + REPLACE;
        go[D] = net[x - 1][y - 1]
            + ((a.charAt(x - 1) == b.charAt(y - 1)) ? NOOP : 100);
        // diagonal costs 0, when no change
        short min = D;
        if (go[min] >= go[X]) {
          min = X;
        }
        if (go[min] > go[Y]) {
          min = Y;
        }
        if (go[min] > go[R]) {
          min = R;
        }
        way[x][y] = min;
        net[x][y] = (short) go[min];
      }
    }
    
    // read the patch string
    StringBuffer result = new StringBuffer();
    final char base = 'a' - 1;
    char deletes = base;
    char equals = base;
    for (x = maxx - 1, y = maxy - 1; x + y != 0;) {
      switch (way[x][y]) {
        case X:
          if (equals != base) {
            result.append("-" + (equals));
            equals = base;
          }
          deletes++;
          x--;
          break;
        // delete
        case Y:
          if (deletes != base) {
            result.append("D" + (deletes));
            deletes = base;
          }
          if (equals != base) {
            result.append("-" + (equals));
            equals = base;
          }
          result.append('I');
          result.append(b.charAt(--y));
          break;
        // insert
        case R:
          if (deletes != base) {
            result.append("D" + (deletes));
            deletes = base;
          }
          if (equals != base) {
            result.append("-" + (equals));
            equals = base;
          }
          result.append('R');
          result.append(b.charAt(--y));
          x--;
          break;
        // replace
        case D:
          if (deletes != base) {
            result.append("D" + (deletes));
            deletes = base;
          }
          equals++;
          x--;
          y--;
          break;
        // no change
      }
    }
    if (deletes != base) {
      result.append("D" + (deletes));
      deletes = base;
    }
    
    return result.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1735.java