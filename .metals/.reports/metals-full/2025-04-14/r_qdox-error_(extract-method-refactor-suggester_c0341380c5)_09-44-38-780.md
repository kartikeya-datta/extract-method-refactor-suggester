error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18065.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18065.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18065.java
text:
```scala
i@@f (url.toString().toLowerCase(Locale.ENGLISH).endsWith(".xml")) {

/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;

import org.apache.tools.ant.AntTypeDefinition;
import org.apache.tools.ant.ComponentHelper;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.MagicNames;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.types.EnumeratedAttribute;

/**
 * Base class for Taskdef and Typedef - handles all
 * the attributes for Typedef. The uri and class
 * handling is handled by DefBase
 *
 * @since Ant 1.4
 */
public abstract class Definer extends DefBase {

    /**
     * the extension of an antlib file for autoloading.
     * {@value[
     */
    private static final String ANTLIB_XML = "/antlib.xml";

    private static class ResourceStack extends ThreadLocal {
        public Object initialValue() {
            return new HashMap();
        }
        Map getStack() {
            return (Map) get();
        }
    }
    private static ResourceStack resourceStack = new ResourceStack();
    private String name;
    private String classname;
    private File file;
    private String resource;
    private boolean restrict = false;

    private   int    format = Format.PROPERTIES;
    private   boolean definerSet = false;
    private   int         onError = OnError.FAIL;
    private   String      adapter;
    private   String      adaptTo;

    private   Class       adapterClass;
    private   Class       adaptToClass;

    /**
     * Enumerated type for onError attribute
     *
     * @see EnumeratedAttribute
     */
    public static class OnError extends EnumeratedAttribute {
        /** Enumerated values */
        public static final int  FAIL = 0, REPORT = 1, IGNORE = 2, FAIL_ALL = 3;

        /**
         * text value of onerror option {@value}
         */
        public static final String POLICY_FAIL = "fail";
        /**
         * text value of onerror option {@value}
         */
        public static final String POLICY_REPORT = "report";
        /**
         * text value of onerror option {@value}
         */
        public static final String POLICY_IGNORE = "ignore";
        /**
         * text value of onerror option {@value}
         */
        public static final String POLICY_FAILALL = "failall";

        /**
         * Constructor
         */
        public OnError() {
            super();
        }

        /**
         * Constructor using a string.
         * @param value the value of the attribute
         */
        public OnError(String value) {
            setValue(value);
        }

        /**
         * get the values
         * @return an array of the allowed values for this attribute.
         */
        public String[] getValues() {
            return new String[] {POLICY_FAIL, POLICY_REPORT, POLICY_IGNORE, POLICY_FAILALL};
        }
    }

    /**
     * Enumerated type for format attribute
     *
     * @see EnumeratedAttribute
     */
    public static class Format extends EnumeratedAttribute {
        /** Enumerated values */
        public static final int PROPERTIES = 0, XML = 1;

        /**
         * get the values
         * @return an array of the allowed values for this attribute.
         */
        public String[] getValues() {
            return new String[] {"properties", "xml"};
        }
    }

    /**
     * The restrict attribute.
     * If this is true, only use this definition in add(X).
     * @param restrict the value to set.
     */
     protected void setRestrict(boolean restrict) {
         this.restrict = restrict;
     }


    /**
     * What to do if there is an error in loading the class.
     * <dl>
     *   <li>error - throw build exception</li>
     *   <li>report - output at warning level</li>
     *   <li>ignore - output at debug level</li>
     * </dl>
     *
     * @param onError an <code>OnError</code> value
     */
    public void setOnError(OnError onError) {
        this.onError = onError.getIndex();
    }

    /**
     * Sets the format of the file or resource
     * @param format the enumerated value - xml or properties
     */
    public void setFormat(Format format) {
        this.format = format.getIndex();
    }

    /**
     * @return the name for this definition
     */
    public String getName() {
        return name;
    }

    /**
     * @return the file containing definitions
     */
    public File getFile() {
        return file;
    }

    /**
     * @return the resource containing definitions
     */
    public String getResource() {
        return resource;
    }


    /**
     * Run the definition.
     *
     * @exception BuildException if an error occurs
     */
    public void execute() throws BuildException {
        ClassLoader al = createLoader();

        if (!definerSet) {
            //we arent fully defined yet. this is an error unless
            //we are in an antlib, in which case the resource name is determined
            //automatically.
            //NB: URIs in the ant core package will be "" at this point.
            if (getURI() == null) {
                throw new BuildException(
                        "name, file or resource attribute of "
                                + getTaskName() + " is undefined",
                        getLocation());
            }

            if (getURI().startsWith(MagicNames.ANTLIB_PREFIX)) {
                //convert the URI to a resource
                String uri1 = getURI();
                setResource(makeResourceFromURI(uri1));
            } else {
                throw new BuildException(
                        "Only antlib URIs can be located from the URI alone,"
                                + "not the URI " + getURI());
            }
        }

        if (name != null) {
            if (classname == null) {
                throw new BuildException(
                    "classname attribute of " + getTaskName() + " element "
                    + "is undefined", getLocation());
            }
            addDefinition(al, name, classname);
        } else {
            if (classname != null) {
                String msg = "You must not specify classname "
                    + "together with file or resource.";
                throw new BuildException(msg, getLocation());
            }
            Enumeration/*<URL>*/ urls = null;
            if (file != null) {
                final URL url = fileToURL();
                if (url == null) {
                    return;
                }
                urls = new Enumeration() {
                    private boolean more = true;
                    public boolean hasMoreElements() {
                        return more;
                    }
                    public Object nextElement() throws NoSuchElementException {
                        if (more) {
                            more = false;
                            return url;
                        } else {
                            throw new NoSuchElementException();
                        }
                    }
                };
            } else {
                urls = resourceToURLs(al);
            }

            while (urls.hasMoreElements()) {
                URL url = (URL) urls.nextElement();

                int fmt = this.format;
                if (url.toString().toLowerCase(Locale.US).endsWith(".xml")) {
                    fmt = Format.XML;
                }

                if (fmt == Format.PROPERTIES) {
                    loadProperties(al, url);
                    break;
                } else {
                    if (resourceStack.getStack().get(url) != null) {
                        log("Warning: Recursive loading of " + url
                            + " ignored"
                            + " at " + getLocation()
                            + " originally loaded at "
                            + resourceStack.getStack().get(url),
                            Project.MSG_WARN);
                    } else {
                        try {
                            resourceStack.getStack().put(url, getLocation());
                            loadAntlib(al, url);
                        } finally {
                            resourceStack.getStack().remove(url);
                        }
                    }
                }
            }
        }
    }

    /**
     * This is where the logic to map from a URI to an antlib resource
     * is kept.
     * @param uri the xml namespace uri that to convert.
     * @return the name of a resource. It may not exist
     */

    public static String makeResourceFromURI(String uri) {
        String path = uri.substring(MagicNames.ANTLIB_PREFIX.length());
        String resource;
        if (path.startsWith("//")) {
            //handle new style full paths to an antlib, in which
            //all but the forward slashes are allowed.
            resource = path.substring("//".length());
            if (!resource.endsWith(".xml")) {
                //if we haven't already named an XML file, it gets antlib.xml
                resource = resource + ANTLIB_XML;
            }
        } else {
            //convert from a package to a path
            resource = path.replace('.', '/') + ANTLIB_XML;
        }
        return resource;
    }

    /**
     * Convert a file to a file: URL.
     *
     * @return the URL, or null if it isn't valid and the active error policy
     * is not to raise a fault
     * @throws BuildException if the file is missing/not a file and the
     * policy requires failure at this point.
     */
    private URL fileToURL() {
        String message = null;
        if (!(file.exists())) {
            message = "File " + file + " does not exist";
        }
        if (message == null && !(file.isFile())) {
            message = "File " + file + " is not a file";
        }
        if (message == null) {
            try {
                return FileUtils.getFileUtils().getFileURL(file);
            } catch (Exception ex) {
                message =
                    "File " + file + " cannot use as URL: "
                    + ex.toString();
            }
        }
        // Here if there is an error
        switch (onError) {
            case OnError.FAIL_ALL:
                throw new BuildException(message);
            case OnError.FAIL:
                // Fall Through
            case OnError.REPORT:
                log(message, Project.MSG_WARN);
                break;
            case OnError.IGNORE:
                // log at a lower level
                log(message, Project.MSG_VERBOSE);
                break;
            default:
                // Ignore the problem
                break;
        }
        return null;
    }

    private Enumeration/*<URL>*/ resourceToURLs(ClassLoader classLoader) {
        Enumeration ret;
        try {
            ret = classLoader.getResources(resource);
        } catch (IOException e) {
            throw new BuildException(
                "Could not fetch resources named " + resource,
                e, getLocation());
        }
        if (!ret.hasMoreElements()) {
            String message = "Could not load definitions from resource "
                + resource + ". It could not be found.";
            switch (onError) {
                case OnError.FAIL_ALL:
                    throw new BuildException(message);
                case OnError.FAIL:
                case OnError.REPORT:
                    log(message, Project.MSG_WARN);
                    break;
                case OnError.IGNORE:
                    log(message, Project.MSG_VERBOSE);
                    break;
                default:
                    // Ignore the problem
                    break;
            }
        }
        return ret;
    }

    /**
     * Load type definitions as properties from a URL.
     *
     * @param al the classloader to use
     * @param url the url to get the definitions from
     */
    protected void loadProperties(ClassLoader al, URL url) {
        InputStream is = null;
        try {
            is = url.openStream();
            if (is == null) {
                log("Could not load definitions from " + url,
                    Project.MSG_WARN);
                return;
            }
            Properties props = new Properties();
            props.load(is);
            Enumeration keys = props.keys();
            while (keys.hasMoreElements()) {
                name = ((String) keys.nextElement());
                classname = props.getProperty(name);
                addDefinition(al, name, classname);
            }
        } catch (IOException ex) {
            throw new BuildException(ex, getLocation());
        } finally {
            FileUtils.close(is);
        }
    }

    /**
     * Load an antlib from a URL.
     *
     * @param classLoader the classloader to use.
     * @param url the url to load the definitions from.
     */
    private void loadAntlib(ClassLoader classLoader, URL url) {
        try {
            Antlib antlib = Antlib.createAntlib(getProject(), url, getURI());
            antlib.setClassLoader(classLoader);
            antlib.setURI(getURI());
            antlib.execute();
        } catch (BuildException ex) {
            throw ProjectHelper.addLocationToBuildException(
                ex, getLocation());
        }
    }

    /**
     * Name of the property file  to load
     * ant name/classname pairs from.
     * @param file the file
     */
    public void setFile(File file) {
        if (definerSet) {
            tooManyDefinitions();
        }
        definerSet = true;
        this.file = file;
    }

    /**
     * Name of the property resource to load
     * ant name/classname pairs from.
     * @param res the resource to use
     */
    public void setResource(String res) {
        if (definerSet) {
            tooManyDefinitions();
        }
        definerSet = true;
        this.resource = res;
    }

    /**
     * Antlib attribute, sets resource and uri.
     * uri is set the antlib value and, resource is set
     * to the antlib.xml resource in the classpath.
     * For example antlib="antlib:org.acme.bland.cola"
     * corresponds to uri="antlib:org.acme.bland.cola"
     * resource="org/acme/bland/cola/antlib.xml".
     * ASF Bugzilla Bug 31999
     * @param antlib the value to set.
     */
    public void setAntlib(String antlib) {
        if (definerSet) {
            tooManyDefinitions();
        }
        if (!antlib.startsWith("antlib:")) {
            throw new BuildException(
                "Invalid antlib attribute - it must start with antlib:");
        }
        setURI(antlib);
        this.resource = antlib.substring("antlib:".length()).replace('.', '/')
            + "/antlib.xml";
        definerSet = true;
    }

    /**
     * Name of the definition
     * @param name the name of the definition
     */
    public void setName(String name) {
        if (definerSet) {
            tooManyDefinitions();
        }
        definerSet = true;
        this.name = name;
    }

    /**
     * Returns the classname of the object we are defining.
     * May be <code>null</code>.
     * @return the class name
     */
    public String getClassname() {
        return classname;
    }

    /**
     * The full class name of the object being defined.
     * Required, unless file or resource have
     * been specified.
     * @param classname the name of the class
     */
    public void setClassname(String classname) {
        this.classname = classname;
    }

    /**
     * Set the class name of the adapter class.
     * An adapter class is used to proxy the
     * definition class. It is used if the
     * definition class is not assignable to
     * the adaptto class, or if the adaptto
     * class is not present.
     *
     * @param adapter the name of the adapter class
     */

    public void setAdapter(String adapter) {
        this.adapter = adapter;
    }

    /**
     * Set the adapter class.
     *
     * @param adapterClass the class to use to adapt the definition class
     */
    protected void setAdapterClass(Class adapterClass) {
        this.adapterClass = adapterClass;
    }

    /**
     * Set the classname of the class that the definition
     * must be compatible with, either directly or
     * by use of the adapter class.
     *
     * @param adaptTo the name of the adaptto class
     */
    public void setAdaptTo(String adaptTo) {
        this.adaptTo = adaptTo;
    }

    /**
     * Set the class for adaptToClass, to be
     * used by derived classes, used instead of
     * the adaptTo attribute.
     *
     * @param adaptToClass the class for adapto.
     */
    protected void setAdaptToClass(Class adaptToClass) {
        this.adaptToClass = adaptToClass;
    }


    /**
     * Add a definition using the attributes of Definer
     *
     * @param al the ClassLoader to use
     * @param name the name of the definition
     * @param classname the classname of the definition
     * @exception BuildException if an error occurs
     */
    protected void addDefinition(ClassLoader al, String name, String classname)
        throws BuildException {
        Class cl = null;
        try {
            try {
                name = ProjectHelper.genComponentName(getURI(), name);

                if (onError != OnError.IGNORE) {
                    cl = Class.forName(classname, true, al);
                }

                if (adapter != null) {
                    adapterClass = Class.forName(adapter, true, al);
                }

                if (adaptTo != null) {
                    adaptToClass = Class.forName(adaptTo, true, al);
                }

                AntTypeDefinition def = new AntTypeDefinition();
                def.setName(name);
                def.setClassName(classname);
                def.setClass(cl);
                def.setAdapterClass(adapterClass);
                def.setAdaptToClass(adaptToClass);
                def.setRestrict(restrict);
                def.setClassLoader(al);
                if (cl != null) {
                    def.checkClass(getProject());
                }
                ComponentHelper.getComponentHelper(getProject())
                        .addDataTypeDefinition(def);
            } catch (ClassNotFoundException cnfe) {
                String msg = getTaskName() + " class " + classname
                        + " cannot be found"
                        + "\n using the classloader " + al;
                throw new BuildException(msg, cnfe, getLocation());
            } catch (NoClassDefFoundError ncdfe) {
                String msg = getTaskName() + " A class needed by class "
                        + classname + " cannot be found: " + ncdfe.getMessage()
                        + "\n using the classloader " + al;
                throw new BuildException(msg, ncdfe, getLocation());
            }
        } catch (BuildException ex) {
            switch (onError) {
                case OnError.FAIL_ALL:
                case OnError.FAIL:
                    throw ex;
                case OnError.REPORT:
                    log(ex.getLocation() + "Warning: " + ex.getMessage(),
                        Project.MSG_WARN);
                    break;
                default:
                    log(ex.getLocation() + ex.getMessage(),
                        Project.MSG_DEBUG);
            }
        }
    }

    /**
     * handle too many definitions by raising an exception.
     * @throws BuildException always.
     */
    private void tooManyDefinitions() {
        throw new BuildException(
            "Only one of the attributes name, file and resource"
            + " can be set", getLocation());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18065.java