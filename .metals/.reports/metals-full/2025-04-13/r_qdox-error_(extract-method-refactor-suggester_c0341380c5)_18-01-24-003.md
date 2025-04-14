error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3384.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3384.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3384.java
text:
```scala
public static S@@tring generate(String id, String INDENT){

/*
 * Section.java
 *
 * Created on 24. Februar 2002, 15:30
 */

/**
 *
 *Reading and writing preserved sections from the code
 *
 * @author  Marian
 */
package org.argouml.language.php.generator;

import java.util.*;
import java.io.*;
import java.lang.String;


public class Section {
    private Map m_ary;
    /** Creates a new instance of Section */
    public Section() {
        m_ary = new HashMap();
        m_ary.clear();
    }

    public String generate(String id, String INDENT){
        String s = "";
        s += INDENT + "// section " + id + " begin\n";
        s += INDENT + "// section " + id + " end\n";
        return s;
    }

    // write todo:
    // check if sections are not used within the file and put them as comments
    // at the end of the file.
    // hint: use a second Map to compare with the used keys
    // =======================================================================

    public void write(String filename, String INDENT) {
        try{
            System.out.println("Start reading");
            FileReader f = new FileReader(filename);
            BufferedReader fr = new BufferedReader(f);
            FileWriter fw = new FileWriter(filename + ".out");
            System.out.println("Total size of Map: " + m_ary.size());
            String line = "";
            while (line != null){
                line = fr.readLine();
                if (line != null){
                    String section_id = get_sect_id(line);
                    if (section_id != null){
                        String content = (String)m_ary.get(section_id);
                        fw.write(line + "\n");
                        if (content != null){
                            fw.write(content);
                            // System.out.println(line);
                            // System.out.print(content);
                        }
                        line = fr.readLine(); // read end section;
                        m_ary.remove(section_id);
                    }
                    fw.write(line + "\n");
                    // System.out.println(line);
                }
            }
            if (m_ary.isEmpty() != true){
                fw.write("/* lost code following: \n");
                Set map_entries = m_ary.entrySet();
                Iterator itr = map_entries.iterator();
                while (itr.hasNext()){
                    Map.Entry entry = (Map.Entry)itr.next();
                    fw.write(INDENT + "// section " + entry.getKey() + " begin\n");
                    fw.write((String)entry.getValue());
                    fw.write(INDENT + "// section " + entry.getKey() + " end\n");
                }
            }

            fr.close();
            fw.close();
        } catch (IOException e){
            System.out.println("Error: " + e.toString());
        }
    }

    public void read(String filename) {
        try{
            System.out.println("Start reading");
            FileReader f = new FileReader(filename);
            BufferedReader fr = new BufferedReader(f);

            String line = "";
            String content = "";
            boolean in_section = false;
            while (line != null){
                line = fr.readLine();
                if (line != null) {
                    if (in_section){
                        String section_id = get_sect_id(line);
                        if (section_id != null){
                            in_section = false;
                            m_ary.put(section_id, content);
                            content = "";
                        } else{
                            content += line + "\n";
                        }
                    } else {
                        String section_id = get_sect_id(line);
                        if (section_id != null){
                            in_section = true;
                        }
                    }
                }
            }
            fr.close();

        } catch (IOException e){
            System.out.println("Error: " + e.toString());
        }



    }

    public static String get_sect_id(String line){
        final String BEGIN = "// section ";
        final String END1 = " begin";
        final String END2 = " end";
        int first = line.indexOf(BEGIN);
        int second = line.indexOf(END1);
        if (second < 0){
            second = line.indexOf(END2);
        }
        String s = null;
        if ( (first > 0) && (second > 0) ){
            first = first + new String(BEGIN).length();
            s = line.substring(first, second);
        }
        return s;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3384.java