error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1359.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1359.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1359.java
text:
```scala
B@@ootstrap.runCommand(cmdarray, args);

// -------------------------------------------------------------------------------
// Copyright (c)2000 Apache Software Foundation
// -------------------------------------------------------------------------------

import java.io.*;
import java.util.*;
import java.util.jar.*;
import java.util.zip.*;

/**
 * Second stage bootstrap. This is where the majority of the work happens.
 *
 * @author James Duncan Davidson (duncan@apache.org);
 */
public class Bootstrap2 {
   
    private static String base = "../";
    private static String crimsonSources = "../../../xml-crimson/src"; // relative to base
    private static String[] modules = new String[]{"copy", "echo", "jar", "javac"};

    /**
     * Command line entry point.
     */
    public static void main(String[] args) throws Exception {
    
        long startTime = System.currentTimeMillis();
    
        System.out.println("Starting Bootstrap2....");

        // ------------------------------------------------------------
        // first create dirs that we need for strapping
        // ------------------------------------------------------------

        mkdir(base + "bootstrap/temp");
        mkdir(base + "bootstrap/temp/crimson");
        mkdir(base + "bootstrap/temp/main");
        mkdir(base + "bootstrap/temp/tasks");
        mkdir(base + "bootstrap/temp/taskjars");
        
        for (int i = 0; i < modules.length; i++) {
            mkdir(base + "bootstrap/temp/tasks/" + modules[i]);
        }
      
        // ------------------------------------------------------------
        // build crimson
        // ------------------------------------------------------------       
        
        Vector v1 = getSources(base + crimsonSources);
        doCompile(base + "bootstrap/temp/crimson", v1);
        
        // ------------------------------------------------------------
        // build the main thing
        // ------------------------------------------------------------        
        
        Vector v2 = getSources(base + "source/main");
        doCompile(base + "bootstrap/temp/main", v2);
        
        // ------------------------------------------------------------
        // now build each of the needed peices into their
        // areas within the strapping area
        // ------------------------------------------------------------

        for (int i = 0; i < modules.length; i++) {
            buildModule(modules[i]);
        }

        // ------------------------------------------------------------
        // now, set classpaths and launch an Ant build to
        // have Ant build itself nicely
        // ------------------------------------------------------------

        System.out.println();
        System.out.println("-------------------------------------------");
        System.out.println("STARTING REAL BUILD");
        System.out.println("-------------------------------------------");
        System.out.println();     
        
        String[] cmdarray = new String[9];
        cmdarray[0] = "java";
        cmdarray[1] = "-cp";
        cmdarray[2] = base + "bootstrap/temp/main" + File.pathSeparator +
                      base + "bootstrap/temp/crimson";
        cmdarray[3] = "org.apache.ant.cli.Main";
        cmdarray[4] = "-taskpath";
        cmdarray[5] = base + "bootstrap/temp/taskjars";
        cmdarray[6] = "-buildfile";
        cmdarray[7] = base + "source/main.ant"; 
        cmdarray[8] = "default";
        
        Bootstrap.runCommand(cmdarray);
        
        System.out.println();
        System.out.println("-------------------------------------------");
        System.out.println("FINISHED WITH REAL BUILD");
        System.out.println("-------------------------------------------");
        System.out.println();
        
        // ------------------------------------------------------------
        // Remove Temporary classes
        // ------------------------------------------------------------

        // delete(tempDirName);

        // ------------------------------------------------------------
        // Print Closer
        // ------------------------------------------------------------

        long endTime = System.currentTimeMillis();
        long elapsd = endTime - startTime;
        System.out.println("Bootstrap Time: " + (elapsd/1000) + "." + (elapsd%1000) + 
                           " seconds");
    }

    private static void mkdir(String arg) {
        File dir = new File(arg);
        if (dir.exists() && !dir.isDirectory()) {
            System.out.println("Oh, horrors! Dir " + arg + " " +
                               "doesn't seem to be a dir... Stop!");
            System.exit(1);
        }
        if (!dir.exists()) {
            System.out.println("Making dir: " + arg);
            dir.mkdir();
        }
    }

    private static void buildModule(String arg) {
        System.out.println("Building " + arg);
     
        // get all sources and hand them off to the compiler to
        // build over into destination

        Vector v = getSources(base + "source/coretasks/" + arg);
        if (v.size() > 0) {
            doCompile(base + "bootstrap/temp/tasks/" + arg, v);
        }
        

        // move taskdef.properties for the module

        copyfile(base + "source/coretasks/" + arg + "/taskdef.properties",
                 base + "bootstrap/temp/tasks/" + arg + "/taskdef.properties");
                 
        // jar up tasks
        try {
            jarDir(new File(base + "bootstrap/temp/tasks/" + arg), 
                new File(base + "bootstrap/temp/taskjars/" + arg + ".jar"));
        } catch(IOException ioe) {
            System.out.println("problem jar'ing: " + arg);
        }
    }

    private static Vector getSources(String arg) {

        File sourceDir = new File(arg);
        
        Vector v = new Vector();
        scanDir(sourceDir, v, ".java");
        return v;
    }

    private static void jarDir(File dir, File jarfile) throws IOException {
        String[] files = dir.list();
        if (files.length > 0) {
            System.out.println("Jaring: " + jarfile);        
            
            FileOutputStream fos = new FileOutputStream(jarfile);
            JarOutputStream jos = new JarOutputStream(fos, new Manifest());
            jarDir(dir, "", jos);
            jos.close();      
        }
    }
    
    private static void jarDir(File dir, String prefix, JarOutputStream jos) throws 
        IOException 
    {
        String[] files = dir.list();
        for (int i = 0; i < files.length; i++) {
            File f = new File(dir, files[i]);
            if (f.isDirectory()) {
                String zipEntryName;
                if (!prefix.equals("")) {
                    zipEntryName = prefix + "/" + files[i];
                } else {
                    zipEntryName = files[i];
                }
                ZipEntry ze = new ZipEntry(zipEntryName);
                jos.putNextEntry(ze);
                jarDir(f, zipEntryName, jos);
            } else {
                String zipEntryName;
                if (!prefix.equals("")) {
                    zipEntryName = prefix + "/" + files[i];
                } else {
                    zipEntryName = files[i];
                }
                ZipEntry ze = new ZipEntry(zipEntryName);
                jos.putNextEntry(ze);
                FileInputStream fis = new FileInputStream(f);
                int count = 0;
                byte[] buf = new byte[8 * 1024];
                count = fis.read(buf, 0, buf.length);
                while (count != -1) {
                    jos.write(buf, 0, count);
                    count = fis.read(buf, 0, buf.length);
                }
                fis.close();
            }
        }
    }

    private static void scanDir(File dir, Vector v, String endsWith) {
        String[] files = dir.list();
        if (files == null) {
            return;
        }
        for (int i = 0; i < files.length; i++) {
            File f = new File(dir, files[i]);
            if (f.isDirectory()) {
                scanDir(f, v, endsWith);
            } else {
                if (files[i].endsWith(endsWith)) {
                    v.addElement(f);
                }
            }
        }
    }

    private static void doCompile(String dest, Vector sources) {
        System.out.println("   Compiling " + sources.size() + " files to " + dest);
        
        // XXX This should be more forgiving about compiling wherever
        // under whatever compiler, but this works so...
        
        sun.tools.javac.Main compiler = new sun.tools.javac.Main(System.out, 
                                                                 "javac");        
        String[] args = new String[sources.size() + 4];
        args[0] = "-classpath";
        args[1] = base + "bootstrap/temp/main" + File.pathSeparator +  
                  base + "bootstrap/temp/crimson";
        args[2] = "-d";
        args[3] = dest;
        for (int i = 0; i < sources.size(); i++) {
            args[4+i] = ((File)sources.elementAt(i)).toString();
        }
        
        // System.out.print("javac ");
        // for (int i = 0; i < args.length; i++) {
        //     System.out.print(args[i] + " ");
        // }
        // System.out.println();
        
        compiler.compile(args);
    }

    private static void copyfile(String from, String dest) {
        File fromF = new File(from);
        File destF = new File(dest);
        if (fromF.exists()) {
            System.out.println("   Copying " + from);
            try {
                FileInputStream in = new FileInputStream(fromF);
                FileOutputStream out = new FileOutputStream(destF);
                byte[] buf = new byte[1024 * 16];
                int count = 0;
                count = in.read(buf, 0, buf.length);
                if (count != -1) {
                    out.write(buf, 0, count);
                    count = in.read(buf, 0, buf.length);
                }
                
                in.close();
                out.close();
            } catch (IOException ioe) {
                System.out.println("OUCH: " + from);
                System.out.println(ioe);
            }
        }
    }
}
 
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1359.java