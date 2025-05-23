error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10226.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10226.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10226.java
text:
```scala
t@@his.classpath = classpath.concatSystemClasspath("ignore");

/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Ant", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */

package org.apache.tools.ant;

import java.util.*;
import java.util.zip.*;
import java.io.*;
import org.apache.tools.ant.types.Path;

/**
 * Used to load classes within ant with a different claspath from that used to start ant.
 * Note that it is possible to force a class into this loader even when that class is on the
 * system classpath by using the forceLoadClass method. Any subsequent classes loaded by that
 * class will then use this loader rather than the system class loader.
 *
 * @author Conor MacNeill
 */
public class AntClassLoader  extends ClassLoader {
    /**
     * The size of buffers to be used in this classloader.
     */
    static private final int BUFFER_SIZE = 1024;
    
    /**
     * The classpath that is to be used when loading classes using this class loader.
     */ 
    private Path classpath;
    
    /**
     * The project to which this class loader belongs.
     */
    private Project project;

    /**
     * Indicates whether the system class loader should be 
     * consulted before trying to load with this class loader. 
     */
    private boolean systemFirst = true;

    /**
     * These are the package roots that are to be loaded by the system class loader
     * regardless of whether the system class loader is being searched first or not.
     */
    private Vector systemPackages = new Vector();
    
    /**
     * These are the package roots that are to be loaded by this class loader
     * regardless of whether the system class loader is being searched first or not.
     */
    private Vector loaderPackages = new Vector();
    
    /**
     * Create a classloader for the given project using the classpath given.
     *
     * @param project the project to ehich this classloader is to belong.
     * @param classpath the classpath to use to load the classes.  This
     *                is combined with the system classpath in a manner
     *                determined by the value of ${build.sysclasspath}
     */
    public AntClassLoader(Project project, Path classpath) {
        this.project = project;
        this.classpath = classpath.concatSystemClasspath();

        // JDK > 1.1 should add these by default, but some VMs don't
        addSystemPackageRoot("java");
        addSystemPackageRoot("javax");
    }

    /**
     * Create a classloader for the given project using the classpath given.
     *
     * @param project the project to which this classloader is to belong.
     * @param classpath the classpath to use to load the classes.
     */
    public AntClassLoader(Project project, Path classpath, boolean systemFirst) {
        this(project, classpath);
        this.systemFirst = systemFirst;
    }
    
    /**
     * Add a package root to the list of packages which must be loaded on the 
     * system loader.
     *
     * All subpackages are also included.
     *
     * @param packageRoot the root of all packages to be included.
     */
    public void addSystemPackageRoot(String packageRoot) {
        systemPackages.addElement(packageRoot + ".");
    }
    
    /**
     * Add a package root to the list of packages which must be loaded using
     * this loader.
     *
     * All subpackages are also included.
     *
     * @param packageRoot the root of akll packages to be included.
     */
    public void addLoaderPackageRoot(String packageRoot) {
        loaderPackages.addElement(packageRoot + ".");
    }
    


    /**
     * Load a class through this class loader even if that class is available on the
     * system classpath.
     *
     * This ensures that any classes which are loaded by the returned class will use this
     * classloader.
     *
     * @param classname the classname to be loaded.
     * 
     * @return the required Class object
     *
     * @throws ClassNotFoundException if the requested class does not exist on
     * this loader's classpath.
     */
    public Class forceLoadClass(String classname) throws ClassNotFoundException {
        project.log("force loading " + classname, Project.MSG_DEBUG);
        Class theClass = findLoadedClass(classname);

        if (theClass == null) {
            theClass = findClass(classname);
        }
        
        return theClass;
    }

    /**
     * Load a class through this class loader but defer to the system class loader
     *
     * This ensures that instances of the returned class will be compatible with instances which
     * which have already been loaded on the system loader.
     *
     * @param classname the classname to be loaded.
     * 
     * @return the required Class object
     *
     * @throws ClassNotFoundException if the requested class does not exist on
     * this loader's classpath.
     */
    public Class forceLoadSystemClass(String classname) throws ClassNotFoundException {
        project.log("force system loading " + classname, Project.MSG_DEBUG);
        Class theClass = findLoadedClass(classname);

        if (theClass == null) {
            theClass = findSystemClass(classname);
        }
        
        return theClass;
    }

    /**
     * Get a stream to read the requested resource name.
     *
     * @param name the name of the resource for which a stream is required.
     *
     * @return a stream to the required resource or null if the resource cannot be
     * found on the loader's classpath.
     */
    public InputStream getResourceAsStream(String name) {
        // we need to search the components of the path to see if we can find the 
        // class we want. 
        InputStream stream = null;
 
        String[] pathElements = classpath.list();
        for (int i = 0; i < pathElements.length && stream == null; ++i) {
            File pathComponent = project.resolveFile((String)pathElements[i]);
            stream = getResourceStream(pathComponent, name);
        }

        return stream;
    }

    /**
     * Get an inputstream to a given resource in the given file which may
     * either be a directory or a zip file.
     *
     * @param file the file (directory or jar) in which to search for the resource.
     * @param resourceName the name of the resource for which a stream is required.
     *
     * @return a stream to the required resource or null if the resource cannot be
     * found in the given file object
     */
    private InputStream getResourceStream(File file, String resourceName) {
        try {
            if (!file.exists()) {
                return null;
            }
            
            if (file.isDirectory()) {
                File resource = new File(file, resourceName); 
                
                if (resource.exists()) {   
                    return new FileInputStream(resource);
                }
            }
            else {
                ZipFile zipFile = null;
                try {
                    zipFile = new ZipFile(file);
        
                    ZipEntry entry = zipFile.getEntry(resourceName);
                    if (entry != null) {
                        // we need to read the entry out of the zip file into
                        // a baos and then 
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte[] buffer = new byte[BUFFER_SIZE];
                        int bytesRead;
                        InputStream stream = zipFile.getInputStream(entry);
                        while ((bytesRead = stream.read(buffer, 0, BUFFER_SIZE)) != -1) {
                            baos.write(buffer, 0, bytesRead);
                        }
                        return new ByteArrayInputStream(baos.toByteArray());   
                    }
                }
                finally {
                    if (zipFile != null) {
                        zipFile.close();
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;   
    }

    /**
     * Load a class with this class loader.
     *
     * This method will load a class. 
     *
     * This class attempts to load the class firstly using the parent class loader. For
     * JDK 1.1 compatability, this uses the findSystemClass method.
     *
     * @param classname the name of the class to be loaded.
     * @param resolve true if all classes upon which this class depends are to be loaded.
     * 
     * @return the required Class object
     *
     * @throws ClassNotFoundException if the requested class does not exist on
     * the system classpath or this loader's classpath.
     */
    protected Class loadClass(String classname, boolean resolve) throws ClassNotFoundException {

        // default to the global setting and then see
        // if this class belongs to a package which has been
        // designated to use a specific loader first (this one or the system one)
        boolean useSystemFirst = systemFirst; 

        for (Enumeration e = systemPackages.elements(); e.hasMoreElements();) {
            String packageName = (String)e.nextElement();
            if (classname.startsWith(packageName)) {
                useSystemFirst = true;
                break;
            }
        }

        for (Enumeration e = loaderPackages.elements(); e.hasMoreElements();) {
            String packageName = (String)e.nextElement();
            if (classname.startsWith(packageName)) {
                useSystemFirst = false;
                break;
            }
        }

        Class theClass = findLoadedClass(classname);
        if (theClass == null) {
            if (useSystemFirst) {
                try {
                    theClass = findSystemClass(classname);
                    project.log("Class " + classname + " loaded from system loader", Project.MSG_DEBUG);
                }
                catch (ClassNotFoundException cnfe) {
                    theClass = findClass(classname);
                    project.log("Class " + classname + " loaded from ant loader", Project.MSG_DEBUG);
                }
            }
            else {
                try {
                    theClass = findClass(classname);
                    project.log("Class " + classname + " loaded from ant loader", Project.MSG_DEBUG);
                }
                catch (ClassNotFoundException cnfe) {
                    theClass = findSystemClass(classname);
                    project.log("Class " + classname + " loaded from system loader", Project.MSG_DEBUG);
                }
            }
        }
            
        if (resolve) {
            resolveClass(theClass);
        }
        
        return theClass;
    }

    /**
     * Convert the class dot notation to a file system equivalent for
     * searching purposes.
     *
     * @param classname the class name in dot format (ie java.lang.Integer)
     *
     * @return the classname in file system format (ie java/lang/Integer.class)
     */
    private String getClassFilename(String classname) {
        return classname.replace('.', '/') + ".class";
    }

    /**
     * Read a class definition from a stream.
     *
     * @param stream the stream from which the class is to be read.
     * @param classname the class name of the class in the stream.
     *
     * @return the Class object read from the stream.
     *
     * @throws IOException if there is a problem reading the class from the
     * stream.
     */
    private Class getClassFromStream(InputStream stream, String classname) 
                throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int bytesRead = -1;
        byte[] buffer = new byte[1024];
        
        while ((bytesRead = stream.read(buffer, 0, 1024)) != -1) {
            baos.write(buffer, 0, bytesRead);
        }
        
        byte[] classData = baos.toByteArray();

        return defineClass(classname, classData, 0, classData.length); 
    }


    /**
     * Search for and load a class on the classpath of this class loader.
     *
     * @param name the classname to be loaded.
     * 
     * @return the required Class object
     *
     * @throws ClassNotFoundException if the requested class does not exist on
     * this loader's classpath.
     */
    public Class findClass(String name) throws ClassNotFoundException {
        project.log("Finding class " + name, Project.MSG_DEBUG);

        try {
            return findClass(name, classpath);
        }
        catch (ClassNotFoundException e) {
            throw e;
        }
    }


    /**
     * Find a class on the given classpath.
     */
    private Class findClass(String name, Path path) throws ClassNotFoundException {
        // we need to search the components of the path to see if we can find the 
        // class we want. 
        InputStream stream = null;
        String classFilename = getClassFilename(name);
        try {
            String[] pathElements = path.list();
            for (int i = 0; i < pathElements.length && stream == null; ++i) {
                File pathComponent = project.resolveFile((String)pathElements[i]);
                stream = getResourceStream(pathComponent, classFilename);
            }
        
            if (stream == null) {
                throw new ClassNotFoundException();
            }
                
            return getClassFromStream(stream, name);
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
            throw new ClassNotFoundException();
        }
        finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            }
            catch (IOException e) {}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10226.java