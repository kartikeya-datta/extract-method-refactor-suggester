error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14346.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14346.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14346.java
text:
```scala
r@@esult.put(entryKey, entryVal);

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
package org.apache.tools.ant;

import java.lang.reflect.Modifier;
import java.lang.reflect.InvocationTargetException;
import java.io.InputStream;
import java.io.IOException;
import java.io.File;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.apache.tools.ant.taskdefs.Typedef;
import org.apache.tools.ant.taskdefs.Definer;
import org.apache.tools.ant.launch.Launcher;
import org.apache.tools.ant.util.FileUtils;

/**
 * Component creation and configuration.
 *
 * The class is based around handing component
 * definitions in an AntTypeTable.
 *
 * The old task/type methods have been kept
 * for backward compatibly.
 * Project will just delegate its calls to this class.
 *
 * A very simple hook mechanism is provided that allows users to plug
 * in custom code. It is also possible to replace the default behavior
 * ( for example in an app embedding ant )
 *
 * @since Ant1.6
 */
public class ComponentHelper  {
    /** Map of component name to lists of restricted definitions */
    private Map          restrictedDefinitions = new HashMap();

    /** Map from component name to anttypedefinition */
    private AntTypeTable antTypeTable;

    /** Map of tasks generated from antTypeTable */
    private final Hashtable taskClassDefinitions = new Hashtable();

    /** flag to rebuild taskClassDefinitions */
    private boolean rebuildTaskClassDefinitions = true;

    /** Map of types generated from antTypeTable */
    private final Hashtable typeClassDefinitions = new Hashtable();

    /** flag to rebuild typeClassDefinitions */
    private boolean rebuildTypeClassDefinitions = true;

    /** Set of namespaces that have been checked for antlibs */
    private final HashSet checkedNamespaces = new HashSet();

    /**
     * Stack of antlib contexts used to resolve definitions while
     *   processing antlib
     */
    private Stack antLibStack = new Stack();

    /** current antlib uri */
    private String antLibCurrentUri = null;

    /**
     * this does not appear to be used anywhere in the Ant codebase
     * even via its accessors
     */
    private ComponentHelper next;

    /**
     * Project that owns a component helper
     */
    private Project project;

    /**
     * Error string when the file taskdefs/defaults.properties cannot be found
     */
    private static final String ERROR_NO_TASK_LIST_LOAD = "Can't load default task list";

    /**
     * Error string when the typedefs/defaults.properties cannot be found
     */
    private static final String ERROR_NO_TYPE_LIST_LOAD = "Can't load default type list";

    /**
     * reference under which we register ourselves with a project -{@value}
     */
    public static final String COMPONENT_HELPER_REFERENCE = "ant.ComponentHelper";

    /**
     * string used to control build.syspath policy {@value}
     */
    private static final String BUILD_SYSCLASSPATH_ONLY = "only";

    /**
     * special name of ant's property task -{@value}. There is some
     * contrived work here to enable this early.
     */
    private static final String ANT_PROPERTY_TASK = "property";

    // {tasks, types}
    private static Properties[] defaultDefinitions = new Properties[2];

     /**
     * Get the project.
     * @return the project owner of this helper.
     */
     public Project getProject() {
         return project;
     }

    /**
     * Find a project component for a specific project, creating
     * it if it does not exist.
     * @param project the project.
     * @return the project component for a specific project.
     */
    public static ComponentHelper getComponentHelper(Project project) {
        if (project == null) {
            return null;
        }
        // Singleton for now, it may change ( per/classloader )
        ComponentHelper ph = (ComponentHelper) project.getReference(COMPONENT_HELPER_REFERENCE);
        if (ph != null) {
            return ph;
        }
        ph = new ComponentHelper();
        ph.setProject(project);

        project.addReference(COMPONENT_HELPER_REFERENCE, ph);
        return ph;
    }

    /**
     * Creates a new ComponentHelper instance.
     */
    protected ComponentHelper() {
    }

    /**
     * Set the next chained component helper.
     *
     * @param next the next chained component helper.
     */
    public void setNext(ComponentHelper next) {
        this.next = next;
    }

    /**
     * Get the next chained component helper.
     *
     * @return the next chained component helper.
     */
    public ComponentHelper getNext() {
        return next;
    }

    /**
     * Sets the project for this component helper.
     *
     * @param project the project for this helper.
     */
    public void setProject(Project project) {
        this.project = project;
        antTypeTable = new AntTypeTable(project);
    }

    /**
     * @return A copy of the CheckedNamespace.
     */
    private synchronized Set getCheckedNamespace() {
        return (Set) checkedNamespaces.clone();
    }

    /**
     * @return A deep copy of the restrictredDefinition
     */
    private Map getRestrictedDefintion() {
        Map result = new HashMap();
        synchronized (restrictedDefinitions) {
            for(Iterator i = restrictedDefinitions.entrySet().iterator();
                         i.hasNext();) {
                Map.Entry entry = (Map.Entry) i.next();
                List entryVal = (List) entry.getValue();
                synchronized (entryVal) {
                    //copy the entryVal
                    entryVal = new ArrayList(entryVal);
                }
                Object entryKey = entry.getKey();                                    
                result.put(entryKey, entryKey);
            }
        }
        return result;
    }

    
    /**
     * Used with creating child projects. Each child
     * project inherits the component definitions
     * from its parent.
     * @param helper the component helper of the parent project.
     */
    public void initSubProject(ComponentHelper helper) {
        // add the types of the parent project
        AntTypeTable typeTable = (AntTypeTable) helper.antTypeTable.clone();
        synchronized (antTypeTable) { 
            for (Iterator i = typeTable.values().iterator(); i.hasNext();) {
                AntTypeDefinition def = (AntTypeDefinition) i.next();
                antTypeTable.put(def.getName(), def);
            }
        }
        // add the parsed namespaces of the parent project
        Set inheritedCheckedNamespace = helper.getCheckedNamespace();
        synchronized (this) {
            checkedNamespaces.addAll(inheritedCheckedNamespace);
        }
        Map inheritedRestrictedDef = helper.getRestrictedDefintion();
        synchronized (restrictedDefinitions) {
            restrictedDefinitions.putAll(inheritedRestrictedDef);
        }
    }

    /**
     * Factory method to create the components.
     *
     * This should be called by UnknownElement.
     *
     * @param ue The Unknown Element creating this component.
     * @param ns Namespace URI. Also available as ue.getNamespace().
     * @param componentType The component type,
     *                       Also available as ue.getComponentName().
     * @return the created component.
     * @throws BuildException if an error occurs.
     */
    public Object createComponent(UnknownElement ue, String ns, String componentType)
            throws BuildException {
        Object component = createComponent(componentType);
        if (component instanceof Task) {
            Task task = (Task) component;
            task.setLocation(ue.getLocation());
            task.setTaskType(componentType);
            task.setTaskName(ue.getTaskName());
            task.setOwningTarget(ue.getOwningTarget());
            task.init();
        }
        return component;
    }

    /**
     * Create an object for a component.
     *
     * @param componentName the name of the component, if
     *                      the component is in a namespace, the
     *                      name is prefixed with the namespace uri and ":".
     * @return the class if found or null if not.
     */
    public Object createComponent(String componentName) {
        AntTypeDefinition def = getDefinition(componentName);
        return def == null ? null : def.create(project);
    }

    /**
     * Return the class of the component name.
     *
     * @param componentName the name of the component, if
     *                      the component is in a namespace, the
     *                      name is prefixed with the namespace uri and ":".
     * @return the class if found or null if not.
     */
    public Class getComponentClass(String componentName) {
        AntTypeDefinition def = getDefinition(componentName);
        return def == null ? null : def.getExposedClass(project);
    }

    /**
     * Return the antTypeDefinition for a componentName.
     * @param componentName the name of the component.
     * @return the ant definition or null if not present.
     */
    public AntTypeDefinition getDefinition(String componentName) {
        checkNamespace(componentName);
        return antTypeTable.getDefinition(componentName);
    }

    /**
     * This method is initialization code implementing the original ant component
     * loading from /org/apache/tools/ant/taskdefs/default.properties
     * and /org/apache/tools/ant/types/default.properties.
     */
    public void initDefaultDefinitions() {
        initTasks();
        initTypes();
    }

    /**
     * Adds a new task definition to the project.
     * Attempting to override an existing definition with an
     * equivalent one (i.e. with the same classname) results in
     * a verbose log message. Attempting to override an existing definition
     * with a different one results in a warning log message.
     *
     * @param taskName The name of the task to add.
     *                 Must not be <code>null</code>.
     * @param taskClass The full name of the class implementing the task.
     *                  Must not be <code>null</code>.
     *
     * @exception BuildException if the class is unsuitable for being an Ant
     *                           task. An error level message is logged before
     *                           this exception is thrown.
     *
     * @see #checkTaskClass(Class)
     */
    public void addTaskDefinition(String taskName, Class taskClass) {
        checkTaskClass(taskClass);
        AntTypeDefinition def = new AntTypeDefinition();
        def.setName(taskName);
        def.setClassLoader(taskClass.getClassLoader());
        def.setClass(taskClass);
        def.setAdapterClass(TaskAdapter.class);
        def.setClassName(taskClass.getName());
        def.setAdaptToClass(Task.class);
        updateDataTypeDefinition(def);
    }

    /**
     * Checks whether or not a class is suitable for serving as Ant task.
     * Ant task implementation classes must be public, concrete, and have
     * a no-arg constructor.
     *
     * @param taskClass The class to be checked.
     *                  Must not be <code>null</code>.
     *
     * @exception BuildException if the class is unsuitable for being an Ant
     *                           task. An error level message is logged before
     *                           this exception is thrown.
     */
    public void checkTaskClass(final Class taskClass) throws BuildException {
        if (!Modifier.isPublic(taskClass.getModifiers())) {
            final String message = taskClass + " is not public";
            project.log(message, Project.MSG_ERR);
            throw new BuildException(message);
        }
        if (Modifier.isAbstract(taskClass.getModifiers())) {
            final String message = taskClass + " is abstract";
            project.log(message, Project.MSG_ERR);
            throw new BuildException(message);
        }
        try {
            taskClass.getConstructor((Class[]) null);
            // don't have to check for public, since
            // getConstructor finds public constructors only.
        } catch (NoSuchMethodException e) {
            final String message = "No public no-arg constructor in " + taskClass;
            project.log(message, Project.MSG_ERR);
            throw new BuildException(message);
        }
        if (!Task.class.isAssignableFrom(taskClass)) {
            TaskAdapter.checkTaskClass(taskClass, project);
        }
    }

    /**
     * Returns the current task definition hashtable. The returned hashtable is
     * "live" and so should not be modified.  Also, the returned table may be 
     * modifed asynchronously.
     *
     * @return a map of from task name to implementing class
     *         (String to Class).
     */
    public Hashtable getTaskDefinitions() {
        synchronized (taskClassDefinitions) {
            synchronized (antTypeTable) {
                if (rebuildTaskClassDefinitions) {
                    taskClassDefinitions.clear();
                    for (Iterator i = antTypeTable.keySet().iterator(); i.hasNext();) {
                        String name = (String) i.next();
                        Class clazz = antTypeTable.getExposedClass(name);
                        if (clazz == null) {
                            continue;
                        }
                        if (Task.class.isAssignableFrom(clazz)) {
                            taskClassDefinitions.put(name, antTypeTable.getTypeClass(name));
                        }
                    }
                    rebuildTaskClassDefinitions = false;
                }
            }
        }
        return taskClassDefinitions;
    }

    /**
     * Returns the current type definition hashtable. The returned hashtable is
     * "live" and so should not be modified.
     *
     * @return a map of from type name to implementing class
     *         (String to Class).
     */
    public Hashtable getDataTypeDefinitions() {
        synchronized (typeClassDefinitions) {
            synchronized (antTypeTable) {
                if (rebuildTypeClassDefinitions) {
                    typeClassDefinitions.clear();
                    for (Iterator i = antTypeTable.keySet().iterator(); i.hasNext();) {
                        String name = (String) i.next();
                        Class clazz = antTypeTable.getExposedClass(name);
                        if (clazz == null) {
                            continue;
                        }
                        if (!(Task.class.isAssignableFrom(clazz))) {
                            typeClassDefinitions.put(name, antTypeTable.getTypeClass(name));
                        }
                    }
                    rebuildTypeClassDefinitions = false;
                }
            }
        }
        return typeClassDefinitions;
    }

    /**
     * This returns a list of restricted definitions for a name.
     * The returned List is "live" and so should not be modified.  
     * Also, the returned list may be modifed asynchronously.  
     * Any access must be guarded with a lock on the list itself.
     * 
     * @param componentName the name to use.
     * @return the list of restricted definitions for a particular name.
     */
    public List getRestrictedDefinitions(String componentName) {
        synchronized (restrictedDefinitions) {
            return (List) restrictedDefinitions.get(componentName);
        }
    }

    /**
     * Adds a new datatype definition.
     * Attempting to override an existing definition with an
     * equivalent one (i.e. with the same classname) results in
     * a verbose log message. Attempting to override an existing definition
     * with a different one results in a warning log message, but the
     * definition is changed.
     *
     * @param typeName The name of the datatype.
     *                 Must not be <code>null</code>.
     * @param typeClass The full name of the class implementing the datatype.
     *                  Must not be <code>null</code>.
     */
    public void addDataTypeDefinition(String typeName, Class typeClass) {
        AntTypeDefinition def = new AntTypeDefinition();
        def.setName(typeName);
        def.setClass(typeClass);
        updateDataTypeDefinition(def);
        project.log(" +User datatype: " + typeName + "     " + typeClass.getName(),
                Project.MSG_DEBUG);
    }

    /**
     * Describe <code>addDataTypeDefinition</code> method here.
     *
     * @param def an <code>AntTypeDefinition</code> value.
     */
    public void addDataTypeDefinition(AntTypeDefinition def) {
        if (!def.isRestrict()) {
           updateDataTypeDefinition(def);
        } else {
            updateRestrictedDefinition(def);
        }
    }

    /**
     * Returns the current datatype definition hashtable. The returned
     * hashtable is "live" and so should not be modified.
     *
     * @return a map of from datatype name to implementing class
     *         (String to Class).
     */
    public Hashtable getAntTypeTable() {
        return antTypeTable;
    }

    /**
     * Creates a new instance of a task.
     *
     *  Called from Project.createTask(), which can be called by tasks.
     *
     * @param taskType The name of the task to create an instance of.
     *                 Must not be <code>null</code>.
     *
     * @return an instance of the specified task, or <code>null</code> if
     *         the task name is not recognised.
     *
     * @exception BuildException if the task name is recognised but task
     *                           creation fails.
     */
    public Task createTask(String taskType) throws BuildException {
        Task task = createNewTask(taskType);
        if (task == null && taskType.equals(ANT_PROPERTY_TASK)) {
            // quick fix for Ant.java use of property before
            // initializing the project
            addTaskDefinition(ANT_PROPERTY_TASK, org.apache.tools.ant.taskdefs.Property.class);
            task = createNewTask(taskType);
        }
        return task;
    }

    /**
     * Creates a new instance of a task.
     * @since ant1.6
     * @param taskType The name of the task to create an instance of.
     *                 Must not be <code>null</code>.
     *
     * @return an instance of the specified task, or <code>null</code> if
     *         the task name is not recognised.
     *
     * @exception BuildException if the task name is recognised but task
     *                           creation fails.
     */
    private Task createNewTask(String taskType) throws BuildException {
        Class c = getComponentClass(taskType);
        if (c == null || !(Task.class.isAssignableFrom(c))) {
            return null;
        }
        Object obj = createComponent(taskType);
        if (obj == null) {
            return null;
        }
        if (!(obj instanceof Task)) {
            throw new BuildException("Expected a Task from '" + taskType
                    + "' but got an instance of " + obj.getClass().getName() + " instead");
        }
        Task task = (Task) obj;
        task.setTaskType(taskType);

        // set default value, can be changed by the user
        task.setTaskName(taskType);

        project.log("   +Task: " + taskType, Project.MSG_DEBUG);
        return task;
    }

    /**
     * Creates a new instance of a data type.
     *
     * @param typeName The name of the data type to create an instance of.
     *                 Must not be <code>null</code>.
     *
     * @return an instance of the specified data type, or <code>null</code> if
     *         the data type name is not recognised.
     *
     * @exception BuildException if the data type name is recognised but
     *                           instance creation fails.
     */
    public Object createDataType(String typeName) throws BuildException {
        return createComponent(typeName);
    }

    /**
     * Returns a description of the type of the given element.
     * <p>
     * This is useful for logging purposes.
     *
     * @param element The element to describe.
     *                Must not be <code>null</code>.
     *
     * @return a description of the element type.
     *
     * @since Ant 1.6
     */
    public String getElementName(Object element) {
        return getElementName(element, false);
    }

    /**
     * Returns a description of the type of the given element.
     * <p>
     * This is useful for logging purposes.
     *
     * @param o     The element to describe.
     *              Must not be <code>null</code>.
     * @param brief whether to use a brief description.
     * @return a description of the element type.
     *
     * @since Ant 1.7
     */
    public String getElementName(Object o, boolean brief) {
        //  PR: I do not know what to do if the object class
        //      has multiple defines
        //      but this is for logging only...
        Class elementClass = o.getClass();
        String elementClassname = elementClass.getName();
        synchronized (antTypeTable) {
            for (Iterator i = antTypeTable.values().iterator(); i.hasNext();) {
                AntTypeDefinition def = (AntTypeDefinition) i.next();
                if (elementClassname.equals(def.getClassName())
                        && (elementClass == def.getExposedClass(project))) {
                    String name = def.getName();
                    return brief ? name : "The <" + name + "> type";
                }
            }
        }
        return getUnmappedElementName(o.getClass(), brief);
    }

    /**
     * Convenient way to get some element name even when you may not have a
     * Project context.
     * @param p       The optional Project instance.
     * @param o       The element to describe.
     *                Must not be <code>null</code>.
     * @param brief   whether to use a brief description.
     * @return a description of the element type.
     * @since Ant 1.7
     */
    public static String getElementName(Project p, Object o, boolean brief) {
        if (p == null) {
            p = Project.getProject(o);
        }
        return p == null ? getUnmappedElementName(o.getClass(), brief) : getComponentHelper(p)
                .getElementName(o, brief);
    }

    private static String getUnmappedElementName(Class c, boolean brief) {
        if (brief) {
            String name = c.getName();
            return name.substring(name.lastIndexOf('.') + 1);
        }
        return c.toString();
    }

    /**
     * Check if definition is a valid definition--it may be a
     * definition of an optional task that does not exist.
     * @param def the definition to test.
     * @return true if exposed type of definition is present.
     */
    private boolean validDefinition(AntTypeDefinition def) {
        return !(def.getTypeClass(project) == null || def.getExposedClass(project) == null);
    }

    /**
     * Check if two definitions are the same.
     * @param def  the new definition.
     * @param old the old definition.
     * @return true if the two definitions are the same.
     */
    private boolean sameDefinition(AntTypeDefinition def, AntTypeDefinition old) {
        boolean defValid = validDefinition(def);
        boolean sameValidity = (defValid == validDefinition(old));
        //must have same validity; then if they are valid they must also be the same:
        return sameValidity && (!defValid || def.sameDefinition(old, project));
    }

    /**
      * update the restricted definition table with a new or
      * modified definition.
      */
    private void updateRestrictedDefinition(AntTypeDefinition def) {
        String name = def.getName();
        List list = null;
        synchronized (restrictedDefinitions) {
            list = (List) restrictedDefinitions.get(name);
            if (list == null) {
                list = new ArrayList();
                restrictedDefinitions.put(name, list);
            }
        }
        // Check if the classname is already present and remove it
        // if it is
        synchronized (list) {
            for (Iterator i = list.iterator(); i.hasNext();) {
                AntTypeDefinition current = (AntTypeDefinition) i.next();
                if (current.getClassName().equals(def.getClassName())) {
                    i.remove();
                    break;
                }
            }
            list.add(def);
        }
    }

    /**
     * Update the component definition table with a new or
     * modified definition.
     * @param def the definition to update or insert.
     */
    private void updateDataTypeDefinition(AntTypeDefinition def) {
        String name = def.getName();
        synchronized (antTypeTable) {
            rebuildTaskClassDefinitions = true;
            rebuildTypeClassDefinitions = true;
            AntTypeDefinition old = antTypeTable.getDefinition(name);
            if (old != null) {
                if (sameDefinition(def, old)) {
                    return;
                }
                Class oldClass = antTypeTable.getExposedClass(name);
                boolean isTask = oldClass != null && Task.class.isAssignableFrom(oldClass);
                project.log("Trying to override old definition of "
                        + (isTask ? "task " : "datatype ") + name, (def.similarDefinition(old,
                        project)) ? Project.MSG_VERBOSE : Project.MSG_WARN);
            }
            project.log(" +Datatype " + name + " " + def.getClassName(), Project.MSG_DEBUG);
            antTypeTable.put(name, def);
        }
    }

    /**
     * Called at the start of processing an antlib.
     * @param uri the uri that is associated with this antlib.
     */
    public void enterAntLib(String uri) {
        antLibCurrentUri = uri;
        antLibStack.push(uri);
    }

    /**
     * @return the current antlib uri.
     */
    public String getCurrentAntlibUri() {
        return antLibCurrentUri;
    }

    /**
     * Called at the end of processing an antlib.
     */
    public void exitAntLib() {
        antLibStack.pop();
        antLibCurrentUri = (antLibStack.size() == 0) ? null : (String) antLibStack.peek();
    }

    /**
     * Load ant's tasks.
     */
    private void initTasks() {
        ClassLoader classLoader = getClassLoader(null);
        Properties props = getDefaultDefinitions(false);
        Enumeration e = props.propertyNames();
        while (e.hasMoreElements()) {
            String name = (String) e.nextElement();
            String className = props.getProperty(name);
            AntTypeDefinition def = new AntTypeDefinition();
            def.setName(name);
            def.setClassName(className);
            def.setClassLoader(classLoader);
            def.setAdaptToClass(Task.class);
            def.setAdapterClass(TaskAdapter.class);
            antTypeTable.put(name, def);
        }
    }

    private ClassLoader getClassLoader(ClassLoader classLoader) {
        String buildSysclasspath = project.getProperty(MagicNames.BUILD_SYSCLASSPATH);
        if (project.getCoreLoader() != null
            && !(BUILD_SYSCLASSPATH_ONLY.equals(buildSysclasspath))) {
            classLoader = project.getCoreLoader();
        }
        return classLoader;
    }

    /**
     * Load default task or type definitions - just the names,
     *  no class loading.
     * Caches results between calls to reduce overhead.
     * @param type true for typedefs, false for taskdefs
     * @return a mapping from definition names to class names
     * @throws BuildException if there was some problem loading
     *                        or parsing the definitions list
     */
    private static synchronized Properties getDefaultDefinitions(boolean type)
            throws BuildException {
        int idx = type ? 1 : 0;
        if (defaultDefinitions[idx] == null) {
            String resource = type ? MagicNames.TYPEDEFS_PROPERTIES_RESOURCE
                    : MagicNames.TASKDEF_PROPERTIES_RESOURCE;
            String errorString = type ? ERROR_NO_TYPE_LIST_LOAD : ERROR_NO_TASK_LIST_LOAD;
            InputStream in = null;
            try {
                in = ComponentHelper.class.getResourceAsStream(resource);
                if (in == null) {
                    throw new BuildException(errorString);
                }
                Properties p = new Properties();
                p.load(in);
                defaultDefinitions[idx] = p;
            } catch (IOException e) {
                throw new BuildException(errorString, e);
            } finally {
                FileUtils.close(in);
            }
        }
        return defaultDefinitions[idx];
    }

    /**
     * Load ant's datatypes.
     */
    private void initTypes() {
        ClassLoader classLoader = getClassLoader(null);
        Properties props = getDefaultDefinitions(true);
        Enumeration e = props.propertyNames();
        while (e.hasMoreElements()) {
            String name = (String) e.nextElement();
            String className = props.getProperty(name);
            AntTypeDefinition def = new AntTypeDefinition();
            def.setName(name);
            def.setClassName(className);
            def.setClassLoader(classLoader);
            antTypeTable.put(name, def);
        }
    }

    /**
     * Called for each component name, check if the
     * associated URI has been examined for antlibs.
     * @param componentName the name of the component, which should include a URI
     *                      prefix if it is in a namespace
     */
    private synchronized void checkNamespace(String componentName) {
        String uri = ProjectHelper.extractUriFromComponentName(componentName);
        if ("".equals(uri)) {
            uri = ProjectHelper.ANT_CORE_URI;
        }
        if (!uri.startsWith(ProjectHelper.ANTLIB_URI)) {
            return; // namespace that does not contain antlib
        }
        if (checkedNamespaces.contains(uri)) {
            return; // Already processed
        }
        checkedNamespaces.add(uri);
        Typedef definer = new Typedef();
        definer.setProject(project);
        definer.init();
        definer.setURI(uri);
        //there to stop error messages being "null"
        definer.setTaskName(uri);
        //if this is left out, bad things happen. like all build files break
        //on the first element encountered.
        definer.setResource(Definer.makeResourceFromURI(uri));
        // a fishing expedition :- ignore errors if antlib not present
        definer.setOnError(new Typedef.OnError(Typedef.OnError.POLICY_IGNORE));
        definer.execute();
    }

    /**
     * Handler called to do decent diagnosis on instantiation failure.
     * @param componentName component name.
     * @param type component type, used in error messages
     * @return a string containing as much diagnostics info as possible.
     */
    public String diagnoseCreationFailure(String componentName, String type) {
        StringWriter errorText = new StringWriter();
        PrintWriter out = new PrintWriter(errorText);
        out.println("Problem: failed to create " + type + " " + componentName);
        //class of problem
        boolean lowlevel = false;
        boolean jars = false;
        boolean definitions = false;
        boolean antTask;
        String home = System.getProperty(Launcher.USER_HOMEDIR);
        File libDir = new File(home, Launcher.USER_LIBDIR);
        String antHomeLib;
        boolean probablyIDE = false;
        String anthome = System.getProperty(MagicNames.ANT_HOME);
        if (anthome != null) {
            File antHomeLibDir = new File(anthome, "lib");
            antHomeLib = antHomeLibDir.getAbsolutePath();
        } else {
            //running under an IDE that doesn't set ANT_HOME
            probablyIDE = true;
            antHomeLib = "ANT_HOME" + File.separatorChar + "lib";
        }
        StringBuffer dirListingText = new StringBuffer();
        final String tab = "        -";
        dirListingText.append(tab);
        dirListingText.append(antHomeLib);
        dirListingText.append('\n');
        if (probablyIDE) {
            dirListingText.append(tab);
            dirListingText.append("the IDE Ant configuration dialogs");
        } else {
            dirListingText.append(tab);
            dirListingText.append(libDir);
            dirListingText.append('\n');
            dirListingText.append(tab);
            dirListingText.append("a directory added on the command line with the -lib argument");
        }
        String dirListing = dirListingText.toString();

        //look up the name
        AntTypeDefinition def = getDefinition(componentName);
        if (def == null) {
            //not a known type
            printUnknownDefinition(out, componentName, dirListing);
            definitions = true;
        } else {
            //we are defined, so it is an instantiation problem
            final String classname = def.getClassName();
            antTask = classname.startsWith("org.apache.tools.ant.");
            boolean optional = classname.startsWith("org.apache.tools.ant.taskdefs.optional");
            optional |= classname.startsWith("org.apache.tools.ant.types.optional");

            //start with instantiating the class.
            Class clazz = null;
            try {
                clazz = def.innerGetTypeClass();
            } catch (ClassNotFoundException e) {
                jars = true;
                if (!optional) {
                    definitions = true;
                }
                printClassNotFound(out, classname, optional, dirListing);
            } catch (NoClassDefFoundError ncdfe) {
                jars = true;
                printNotLoadDependentClass(out, optional, ncdfe, dirListing);
            }
            //here we successfully loaded the class or failed.
            if (clazz != null) {
                //success: proceed with more steps
                try {
                    def.innerCreateAndSet(clazz, project);
                    //hey, there is nothing wrong with us
                    out.println("The component could be instantiated.");
                } catch (NoSuchMethodException e) {
                    lowlevel = true;
                    out.println("Cause: The class " + classname
                            + " has no compatible constructor.");

                } catch (InstantiationException e) {
                    lowlevel = true;
                    out.println("Cause: The class " + classname
                            + " is abstract and cannot be instantiated.");
                } catch (IllegalAccessException e) {
                    lowlevel = true;
                    out.println("Cause: The constructor for " + classname
                            + " is private and cannot be invoked.");
                } catch (InvocationTargetException ex) {
                    lowlevel = true;
                    Throwable t = ex.getTargetException();
                    out.println("Cause: The constructor threw the exception");
                    out.println(t.toString());
                    t.printStackTrace(out);
                }  catch (NoClassDefFoundError ncdfe) {
                    jars = true;
                    out.println("Cause:  A class needed by class " + classname
                            + " cannot be found: ");
                    out.println("       " + ncdfe.getMessage());
                    out.println("Action: Determine what extra JAR files are"
                            + " needed, and place them in:");
                    out.println(dirListing);
                }
            }
            out.println();
            out.println("Do not panic, this is a common problem.");
            if (definitions) {
                out.println("It may just be a typographical error in the build file "
                        + "or the task/type declaration.");
            }
            if (jars) {
                out.println("The commonest cause is a missing JAR.");
            }
            if (lowlevel) {
                out.println("This is quite a low level problem, which may need "
                        + "consultation with the author of the task.");
                if (antTask) {
                    out.println("This may be the Ant team. Please file a "
                            + "defect or contact the developer team.");
                } else {
                    out.println("This does not appear to be a task bundled with Ant.");
                    out.println("Please take it up with the supplier of the third-party " + type
                            + ".");
                    out.println("If you have written it yourself, you probably have a bug to fix.");
                }
            } else {
                out.println();
                out.println("This is not a bug; it is a configuration problem");
            }
        }
        out.flush();
        out.close();
        return errorText.toString();
    }

    /**
     * Print unknown definition.forking
     */
    private void printUnknownDefinition(PrintWriter out, String componentName, String dirListing) {
        boolean isAntlib = componentName.indexOf(MagicNames.ANTLIB_PREFIX) == 0;
        String uri = ProjectHelper.extractUriFromComponentName(componentName);
        out.println("Cause: The name is undefined.");
        out.println("Action: Check the spelling.");
        out.println("Action: Check that any custom tasks/types have been declared.");
        out.println("Action: Check that any <presetdef>/<macrodef>"
                + " declarations have taken place.");
        if (uri.length() > 0) {
            List matches = antTypeTable.findMatches(uri);
            if (matches.size() > 0) {
                out.println();
                out.println("The definitions in the namespace " + uri + " are:");
                for (Iterator it = matches.iterator(); it.hasNext();) {
                    AntTypeDefinition def = (AntTypeDefinition) it.next();
                    String local = ProjectHelper.extractNameFromComponentName(def.getName());
                    out.println("    " + local);
                }
            } else {
                out.println("No types or tasks have been defined in this namespace yet");
                if (isAntlib) {
                    out.println();
                    out.println("This appears to be an antlib declaration. ");
                    out.println("Action: Check that the implementing library exists in one of:");
                    out.println(dirListing);
                }
            }
        }
    }

    /**
     * Print class not found.
     */
    private void printClassNotFound(PrintWriter out, String classname, boolean optional,
            String dirListing) {
        out.println("Cause: the class " + classname + " was not found.");
        if (optional) {
            out.println("        This looks like one of Ant's optional components.");
            out.println("Action: Check that the appropriate optional JAR exists in");
            out.println(dirListing);
        } else {
            out.println("Action: Check that the component has been correctly declared");
            out.println("        and that the implementing JAR is in one of:");
            out.println(dirListing);
        }
    }

    /**
     * Print could not load dependent class.
     */
    private void printNotLoadDependentClass(PrintWriter out, boolean optional,
            NoClassDefFoundError ncdfe, String dirListing) {
        out.println("Cause: Could not load a dependent class "
                    +  ncdfe.getMessage());
        if (optional) {
            out.println("       It is not enough to have Ant's optional JARs");
            out.println("       you need the JAR files that the" + " optional tasks depend upon.");
            out.println("       Ant's optional task dependencies are" + " listed in the manual.");
        } else {
            out.println("       This class may be in a separate JAR" + " that is not installed.");
        }
        out.println("Action: Determine what extra JAR files are"
                + " needed, and place them in one of:");
        out.println(dirListing);
    }

    /**
     * Map that contains the component definitions.
     */
    private static class AntTypeTable extends Hashtable {
        private static final long serialVersionUID = -3060442320477772028L;
        private Project project;

        AntTypeTable(Project project) {
            this.project = project;
        }

        AntTypeDefinition getDefinition(String key) {
            return (AntTypeDefinition) (super.get(key));
        }

        public Object get(Object key) {
            return getTypeClass((String) key);
        }

        Object create(String name) {
            AntTypeDefinition def = getDefinition(name);
            return (def == null) ? null : def.create(project);
        }

        Class getTypeClass(String name) {
            AntTypeDefinition def = getDefinition(name);
            return (def == null) ? null : def.getTypeClass(project);
        }

        Class getExposedClass(String name) {
            AntTypeDefinition def = getDefinition(name);
            return def == null ? null : def.getExposedClass(project);
        }

        public synchronized boolean contains(Object clazz) {
            boolean found = false;
            if (clazz instanceof Class) {
                for (Iterator i = values().iterator(); i.hasNext() && !found;) {
                    found = (((AntTypeDefinition) (i.next())).getExposedClass(project) == clazz);
                }
            }
            return found;
        }

        public boolean containsValue(Object value) {
            return contains(value);
        }

        /**
         * Create a list of all definitions that match a prefix, usually the URI
         * of a library
         * @param prefix prefix to match off
         * @return the (possibly empty) list of definitions
         */
        public synchronized List/*<AntTypeDefinition>*/ findMatches(String prefix) {
            ArrayList matches = new ArrayList();
            for (Iterator i = values().iterator(); i.hasNext();) {
                AntTypeDefinition def = (AntTypeDefinition) (i.next());
                if (def.getName().startsWith(prefix)) {
                    matches.add(def);
                }
            }
            return matches;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14346.java