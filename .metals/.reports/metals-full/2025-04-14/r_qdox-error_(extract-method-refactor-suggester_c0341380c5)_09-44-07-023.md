error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13671.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13671.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13671.java
text:
```scala
r@@unner.addBean("self", instance);

/*
 * Copyright  2000-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
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
package org.apache.tools.ant.taskdefs.optional.script;

import org.apache.tools.ant.AntTypeDefinition;
import org.apache.tools.ant.ComponentHelper;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.MagicNames;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.taskdefs.DefBase;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import java.io.File;

import org.apache.tools.ant.util.ScriptRunner;

/**
 * Define a task using a script
 *
 * @since Ant 1.6
 */
public class ScriptDef extends DefBase {
    /** Used to run the script */
    private ScriptRunner runner = new ScriptRunner();

    /** the name by which this script will be activated */
    private String name;

    /** Attributes definitions of this script */
    private List attributes = new ArrayList();

    /** Nested Element definitions of this script */
    private List nestedElements = new ArrayList();

    /** The attribute names as a set */
    private Set attributeSet;

    /** The nested element definitions indexed by their names */
    private Map nestedElementMap;

    /**
     * set the name under which this script will be activated in a build
     * file
     *
     * @param name the name of the script
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Indicates whether the task supports a given attribute name
     *
     * @param attributeName the name of the attribute.
     *
     * @return true if the attribute is supported by the script.
     */
    public boolean isAttributeSupported(String attributeName) {
        return attributeSet.contains(attributeName);
    }

    /**
     * Class representing an attribute definition
     */
    public static class Attribute {
        /** The attribute name */
        private String name;

        /**
         * Set the attribute name
         *
         * @param name the attribute name
         */
        public void setName(String name) {
            this.name = name.toLowerCase(Locale.US);
        }
    }

    /**
     * Add an attribute definition to this script.
     *
     * @param attribute the attribute definition.
     */
    public void addAttribute(Attribute attribute) {
        attributes.add(attribute);
    }

    /**
     * Class to represent a nested element definition
     */
    public static class NestedElement {
        /** The name of the neseted element */
        private String name;

        /** The Ant type to which this nested element corresponds. */
        private String type;

        /** The class to be created for this nested element */
        private String className;

        /**
         * set the tag name for this nested element
         *
         * @param name the name of this nested element
         */
        public void setName(String name) {
            this.name = name.toLowerCase(Locale.US);
        }

        /**
         * Set the type of this element. This is the name of an
         * Ant task or type which is to be used when this element is to be
         * created. This is an alternative to specifying the class name directly
         *
         * @param type the name of an Ant type, or task, to use for this nested
         * element.
         */
        public void setType(String type) {
            this.type = type;
        }

        /**
         * Set the classname of the class to be used for the nested element.
         * This specifies the class directly and is an alternative to specifying
         * the Ant type name.
         *
         * @param className the name of the class to use for this nested
         * element.
         */
        public void setClassName(String className) {
            this.className = className;
        }
    }

    /**
     * Add a nested element definition.
     *
     * @param nestedElement the nested element definition.
     */
    public void addElement(NestedElement nestedElement) {
        nestedElements.add(nestedElement);
    }

    /**
     * Define the script.
     */
    public void execute() {
        if (name == null) {
            throw new BuildException("scriptdef requires a name attribute to "
                + "name the script");
        }

        if (runner.getLanguage() == null) {
            throw new BuildException("<scriptdef> requires a language attribute "
                + "to specify the script language");
        }

        attributeSet = new HashSet();
        for (Iterator i = attributes.iterator(); i.hasNext();) {
            Attribute attribute = (Attribute) i.next();
            if (attribute.name == null) {
                throw new BuildException("scriptdef <attribute> elements "
                    + "must specify an attribute name");
            }

            if (attributeSet.contains(attribute.name)) {
                throw new BuildException("scriptdef <" + name + "> declares "
                    + "the " + attribute.name + " attribute more than once");
            }
            attributeSet.add(attribute.name);
        }

        nestedElementMap = new HashMap();
        for (Iterator i = nestedElements.iterator(); i.hasNext();) {
            NestedElement nestedElement = (NestedElement) i.next();
            if (nestedElement.name == null) {
                throw new BuildException("scriptdef <element> elements "
                    + "must specify an element name");
            }
            if (nestedElementMap.containsKey(nestedElement.name)) {
                throw new BuildException("scriptdef <" + name + "> declares "
                    + "the " + nestedElement.name + " nested element more "
                    + "than once");
            }

            if (nestedElement.className == null
                && nestedElement.type == null) {
                throw new BuildException("scriptdef <element> elements "
                    + "must specify either a classname or type attribute");
            }
            if (nestedElement.className != null
                && nestedElement.type != null) {
                throw new BuildException("scriptdef <element> elements "
                    + "must specify only one of the classname and type "
                    + "attributes");
            }


            nestedElementMap.put(nestedElement.name, nestedElement);
        }

        // find the script repository - it is stored in the project
        Map scriptRepository = null;
        Project project = getProject();
        synchronized (project) {
            scriptRepository =
                (Map) project.getReference(MagicNames.SCRIPT_REPOSITORY);
            if (scriptRepository == null) {
                scriptRepository = new HashMap();
                project.addReference(MagicNames.SCRIPT_REPOSITORY,
                    scriptRepository);
            }
        }

        name = ProjectHelper.genComponentName(getURI(), name);
        scriptRepository.put(name, this);
        AntTypeDefinition def = new AntTypeDefinition();
        def.setName(name);
        def.setClass(ScriptDefBase.class);
        ComponentHelper.getComponentHelper(
            getProject()).addDataTypeDefinition(def);
    }

    /**
     * Create a nested element to be configured.
     *
     * @param elementName the name of the nested element.
     * @return object representing the element name.
     */
    public Object createNestedElement(String elementName) {
        NestedElement definition
            = (NestedElement) nestedElementMap.get(elementName);
        if (definition == null) {
            throw new BuildException("<" + name + "> does not support "
                + "the <" + elementName + "> nested element");
        }

        Object instance = null;
        String classname = definition.className;
        if (classname == null) {
            instance = getProject().createTask(definition.type);
            if (instance == null) {
                instance = getProject().createDataType(definition.type);
            }
        } else {
            /*
            // try the context classloader
            ClassLoader loader
                = Thread.currentThread().getContextClassLoader();
            */
            ClassLoader loader = createLoader();

            Class instanceClass = null;
            try {
                instanceClass = Class.forName(classname, true, loader);
            } catch (Throwable e) {
                // try normal method
                try {
                    instanceClass = Class.forName(classname);
                } catch (Throwable e2) {
                    throw new BuildException("scriptdef: Unable to load "
                        + "class " + classname + " for nested element <"
                        + elementName + ">", e2);
                }
            }

            try {
                instance = instanceClass.newInstance();
            } catch (Throwable e) {
                throw new BuildException("scriptdef: Unable to create "
                    + "element of class " + classname + " for nested "
                    + "element <" + elementName + ">", e);
            }
            getProject().setProjectReference(instance);
        }

        if (instance == null) {
            throw new BuildException("<" + name + "> is unable to create "
                + "the <" + elementName + "> nested element");
        }
        return instance;
    }

    /**
     * Execute the script.
     *
     * @param attributes collection of attributes
     * @param elements a list of nested element values.
     * @deprecated use executeScript(attribute, elements, instance) instead
     */
    public void executeScript(Map attributes, Map elements) {
        runner.addBean("attributes", attributes);
        runner.addBean("elements", elements);
        runner.addBean("project", getProject());
        runner.executeScript("scriptdef_" + name);
    }

    /**
     * Execute the script.
     * This is called by the script instance to execute the script for this
     * definition.
     *
     * @param attributes collection of attributes
     * @param elements   a list of nested element values.
     * @param instance   the script instance
     */
    public void executeScript(Map attributes, Map elements, ScriptDefBase instance) {
        runner.addBean("attributes", attributes);
        runner.addBean("elements", elements);
        runner.addBean("project", getProject());
        runner.addBean("self", self);
        runner.executeScript("scriptdef_" + name);
    }


    /**
     * Defines the language (required).
     *
     * @param language the scripting language name for the script.
     */
    public void setLanguage(String language) {
        runner.setLanguage(language);
    }

    /**
     * Load the script from an external file ; optional.
     *
     * @param file the file containing the script source.
     */
    public void setSrc(File file) {
        runner.setSrc(file);
    }

    /**
     * Set the script text.
     *
     * @param text a component of the script text to be added.
     */
    public void addText(String text) {
        runner.addText(text);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13671.java