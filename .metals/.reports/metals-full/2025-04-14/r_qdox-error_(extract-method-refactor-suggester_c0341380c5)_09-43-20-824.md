error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15008.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15008.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15008.java
text:
```scala
F@@ile tmpFile = fu.createTempFileName("modified-", ".tmp", null, true);

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

package org.apache.tools.ant.types.selectors.modifiedselector;


// Java
import java.util.Comparator;
import java.util.Vector;
import java.util.Iterator;
import java.io.File;

// Ant
import org.apache.tools.ant.Project;
import org.apache.tools.ant.IntrospectionHelper;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.Parameter;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.selectors.ResourceSelector;
import org.apache.tools.ant.types.selectors.BaseExtendSelector;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.ResourceUtils;


/**
 * <p>Selector class that uses <i>Algorithm</i>, <i>Cache</i> and <i>Comparator</i>
 * for its work.
 * The <i>Algorithm</i> is used for computing a hashvalue for a file.
 * The <i>Comparator</i> decides whether to select or not.
 * The <i>Cache</i> stores the other value for comparison by the <i>Comparator</i>
 * in a persistent manner.</p>
 *
 * <p>The ModifiedSelector is implemented as a <b>CoreSelector</b> and uses default
 * values for all its attributes therefore the simpliest example is <pre>
 *   &lt;copy todir="dest"&gt;
 *       &lt;filelist dir="src"&gt;
 *           &lt;modified/&gt;
 *       &lt;/filelist&gt;
 *   &lt;/copy&gt;
 * </pre></p>
 *
 * <p>The same example rewritten as CoreSelector with setting the all values
 * (same as defaults are) would be <pre>
 *   &lt;copy todir="dest"&gt;
 *       &lt;filelist dir="src"&gt;
 *           &lt;modified update="true"
 *                     cache="propertyfile"
 *                     algorithm="digest"
 *                     comparator="equal"&gt;
 *               &lt;param name="cache.cachefile"     value="cache.properties"/&gt;
 *               &lt;param name="algorithm.algorithm" value="MD5"/&gt;
 *           &lt;/modified&gt;
 *       &lt;/filelist&gt;
 *   &lt;/copy&gt;
 * </pre></p>
 *
 * <p>And the same rewritten as CustomSelector would be<pre>
 *   &lt;copy todir="dest"&gt;
 *       &lt;filelist dir="src"&gt;
 *           &lt;custom class="org.apache.tools.ant.type.selectors.ModifiedSelector"&gt;
 *               &lt;param name="update"     value="true"/&gt;
 *               &lt;param name="cache"      value="propertyfile"/&gt;
 *               &lt;param name="algorithm"  value="digest"/&gt;
 *               &lt;param name="comparator" value="equal"/&gt;
 *               &lt;param name="cache.cachefile"     value="cache.properties"/&gt;
 *               &lt;param name="algorithm.algorithm" value="MD5"/&gt;
 *           &lt;/custom&gt;
 *       &lt;/filelist&gt;
 *   &lt;/copy&gt;
 * </pre></p>
 *
 * <p>If you want to provide your own interface implementation you can do
 * that via the *classname attributes. If the classes are not on Ant's core
 * classpath, you will have to provide the path via nested &lt;classpath&gt;
 * element, so that the selector can find the classes. <pre>
 *   &lt;modified cacheclassname="com.mycompany.MyCache"&gt;
 *       &lt;classpath&gt;
 *           &lt;pathelement location="lib/mycompony-antutil.jar"/&gt;
 *       &lt;/classpath&gt;
 *   &lt;/modified&gt;
 * </pre></p>
 *
 * <p>All these three examples copy the files from <i>src</i> to <i>dest</i>
 * using the ModifiedSelector. The ModifiedSelector uses the <i>PropertyfileCache
 * </i>, the <i>DigestAlgorithm</i> and the <i>EqualComparator</i> for its
 * work. The PropertyfileCache stores key-value-pairs in a simple java
 * properties file. The filename is <i>cache.properties</i>. The <i>update</i>
 * flag lets the selector update the values in the cache (and on first call
 * creates the cache). The <i>DigestAlgorithm</i> computes a hashvalue using the
 * java.security.MessageDigest class with its MD5-Algorithm and its standard
 * provider. The new computed hashvalue and the stored one are compared by
 * the <i>EqualComparator</i> which returns 'true' (more correct a value not
 * equals zero (1)) if the values are not the same using simple String
 * comparison.</p>
 *
 * <p>A useful scenario for this selector is inside a build environment
 * for homepage generation (e.g. with <a href="http://forrest.apache.org/">
 * Apache Forrest</a>). <pre>
 * &lt;target name="generate-and-upload-site"&gt;
 *     &lt;echo&gt; generate the site using forrest &lt;/echo&gt;
 *     &lt;antcall target="site"/&gt;
 *
 *     &lt;echo&gt; upload the changed files &lt;/echo&gt;
 *     &lt;ftp server="${ftp.server}" userid="${ftp.user}" password="${ftp.pwd}"&gt;
 *         &lt;fileset dir="htdocs/manual"&gt;
 *             &lt;modified/&gt;
 *         &lt;/fileset&gt;
 *     &lt;/ftp&gt;
 * &lt;/target&gt;
 * </pre> Here all <b>changed</b> files are uploaded to the server. The
 * ModifiedSelector saves therefore much upload time.</p>
 *
 *
 * <p>This selector uses reflection for setting the values of its three interfaces
 * (using org.apache.tools.ant.IntrospectionHelper) therefore no special
 * 'configuration interfaces' has to be implemented by new caches, algorithms or
 * comparators. All present <i>set</i>XX methods can be used. E.g. the DigestAlgorithm
 * can use a specified provider for computing its value. For selecting this
 * there is a <i>setProvider(String providername)</i> method. So you can use
 * a nested <i>&lt;param name="algorithm.provider" value="MyProvider"/&gt;</i>.
 *
 *
 * @since  Ant 1.6
 */
public class ModifiedSelector extends BaseExtendSelector
                              implements BuildListener, ResourceSelector {


    // -----  attributes  -----


    /** Cache name for later instantiation. */
    private CacheName cacheName = null;

    /** User specified classname for Cache. */
    private String cacheClass;

    /** Algorithm name for later instantiation. */
    private AlgorithmName algoName = null;

    /** User specified classname for Algorithm. */
    private String algorithmClass;

    /** Comparator name for later instantiation. */
    private ComparatorName compName = null;

    /** User specified classname for Comparator. */
    private String comparatorClass;

    /** Should the cache be updated? */
    private boolean update = true;

    /** Are directories selected? */
    private boolean selectDirectories = true;

    /**
     * Should Resources whithout an InputStream, and
     * therefore without checking, be selected?
     */
    private boolean selectResourcesWithoutInputStream = true;

    /** Delay the writing of the cache file */
    private boolean delayUpdate = true;


    // ----- internal member variables -----


    /** How should the cached value and the new one compared? */
    private Comparator comparator = null;

    /** Algorithm for computing new values and updating the cache. */
    private Algorithm algorithm = null;

    /** The Cache containing the old values. */
    private Cache cache = null;

    /** Count of modified properties */
    private int modified = 0;

    /** Flag whether this object is configured. Configuration is only done once. */
    private boolean isConfigured = false;

    /**
     * Parameter vector with parameters for later initialization.
     * @see #configure
     */
    private Vector configParameter = new Vector();

    /**
     * Parameter vector with special parameters for later initialization.
     * The names have the pattern '*.*', e.g. 'cache.cachefile'.
     * These parameters are used <b>after</b> the parameters with the pattern '*'.
     * @see #configure
     */
    private Vector specialParameter = new Vector();

    /** The classloader of this class. */
    private ClassLoader myClassLoader = null;

    /** provided classpath for the classloader */
    private Path classpath = null;


    // -----  constructors  -----


    /** Bean-Constructor. */
    public ModifiedSelector() {
    }


    // ----- configuration  -----


    /** Overrides BaseSelector.verifySettings(). */
    public void verifySettings() {
        configure();
        if (cache == null) {
            setError("Cache must be set.");
        } else if (algorithm == null) {
            setError("Algorithm must be set.");
        } else if (!cache.isValid()) {
            setError("Cache must be proper configured.");
        } else if (!algorithm.isValid()) {
            setError("Algorithm must be proper configured.");
        }
    }


    /**
     * Configures this Selector.
     * Does this work only once per Selector object.
     * <p>Because some problems while configuring from <custom>Selector
     * the configuration is done in the following order:<ol>
     * <li> collect the configuration data </li>
     * <li> wait for the first isSelected() call </li>
     * <li> set the default values </li>
     * <li> set values for name pattern '*': update, cache, algorithm, comparator </li>
     * <li> set values for name pattern '*.*: cache.cachefile, ... </li>
     * </ol></p>
     * <p>This configuration algorithm is needed because you don't know
     * the order of arriving config-data. E.g. if you first set the
     * <i>cache.cachefilename</i> and after that the <i>cache</i> itself,
     * the default value for cachefilename is used, because setting the
     * cache implies creating a new Cache instance - with its defaults.</p>
     */
    public void configure() {
        //
        // -----  The "Singleton"  -----
        //
        if (isConfigured) {
            return;
        }
        isConfigured = true;

        //
        // -----  Set default values  -----
        //
        Project p = getProject();
        String filename = "cache.properties";
        File cachefile = null;
        if (p != null) {
            // normal use inside Ant
            cachefile = new File(p.getBaseDir(), filename);

            // set self as a BuildListener to delay cachefile saves
            getProject().addBuildListener(this);
        } else {
            // no reference to project - e.g. during normal JUnit tests
            cachefile = new File(filename);
            setDelayUpdate(false);
        }
        Cache      defaultCache      = new PropertiesfileCache(cachefile);
        Algorithm  defaultAlgorithm  = new DigestAlgorithm();
        Comparator defaultComparator = new EqualComparator();
        update = true;
        selectDirectories = true;

        //
        // -----  Set the main attributes, pattern '*'  -----
        //
        for (Iterator itConfig = configParameter.iterator(); itConfig.hasNext();) {
            Parameter par = (Parameter) itConfig.next();
            if (par.getName().indexOf(".") > 0) {
                // this is a *.* parameter for later use
                specialParameter.add(par);
            } else {
                useParameter(par);
            }
        }
        configParameter = new Vector();

        // specify the algorithm classname
        if (algoName != null) {
            // use Algorithm defined via name
            if ("hashvalue".equals(algoName.getValue())) {
                algorithm = new HashvalueAlgorithm();
            } else if ("digest".equals(algoName.getValue())) {
                algorithm = new DigestAlgorithm();
            } else if ("checksum".equals(algoName.getValue())) {
                algorithm = new ChecksumAlgorithm();
            }
        } else {
            if (algorithmClass != null) {
                // use Algorithm specified by classname
                algorithm = (Algorithm) loadClass(
                    algorithmClass,
                    "is not an Algorithm.",
                    Algorithm.class);
            } else {
                // nothing specified - use default
                algorithm = defaultAlgorithm;
            }
        }

        // specify the cache classname
        if (cacheName != null) {
            // use Cache defined via name
            if ("propertyfile".equals(cacheName.getValue())) {
                cache = new PropertiesfileCache();
            }
        } else {
            if (cacheClass != null) {
                // use Cache specified by classname
                cache = (Cache) loadClass(cacheClass, "is not a Cache.", Cache.class);
            } else {
                // nothing specified - use default
                cache = defaultCache;
            }
        }

        // specify the comparator classname
        if (compName != null) {
            // use Algorithm defined via name
            if ("equal".equals(compName.getValue())) {
                comparator = new EqualComparator();
             } else if ("rule".equals(compName.getValue())) {
                // TODO there is a problem with the constructor for the RBC.
                // you have to provide the rules in the constructors - no setters
                // available.
                throw new BuildException("RuleBasedCollator not yet supported.");
                // Have to think about lazy initialization here...  JHM
                // comparator = new java.text.RuleBasedCollator();
            }
        } else {
            if (comparatorClass != null) {
                // use Algorithm specified by classname
                comparator = (Comparator) loadClass(
                    comparatorClass,
                    "is not a Comparator.",
                    Comparator.class);
            } else {
                // nothing specified - use default
                comparator = defaultComparator;
            }
        }

        //
        // -----  Set the special attributes, pattern '*.*'  -----
        //
        for (Iterator itSpecial = specialParameter.iterator(); itSpecial.hasNext();) {
            Parameter par = (Parameter) itSpecial.next();
            useParameter(par);
        }
        specialParameter = new Vector();
    }


    /**
     * Loads the specified class and initializes an object of that class.
     * Throws a BuildException using the given message if an error occurs during
     * loading/instantiation or if the object is not from the given type.
     * @param classname the classname
     * @param msg the message-part for the BuildException
     * @param type the type to check against
     * @return a castable object
     */
    protected Object loadClass(String classname, String msg, Class type) {
        try {
            // load the specified class
            ClassLoader cl = getClassLoader();
            Class clazz = null;
            if (cl != null) {
                clazz = cl.loadClass(classname);
            } else {
                clazz = Class.forName(classname);
            }

            Object rv = clazz.newInstance();

            if (!type.isInstance(rv)) {
                throw new BuildException("Specified class (" + classname + ") " + msg);
            }
            return rv;
        } catch (ClassNotFoundException e) {
            throw new BuildException("Specified class (" + classname + ") not found.");
        } catch (Exception e) {
            throw new BuildException(e);
        }
    }


    // -----  the selection work  -----


    /**
     * Implementation of ResourceSelector.isSelected().
     *
     * @param resource The resource to check
     * @return whether the resource is selected
     * @see ResourceSelector#isSelected(Resource)
     */
    public boolean isSelected(Resource resource) {
        if (resource.isFilesystemOnly()) {
            // We have a 'resourced' file, so reconvert it and use
            // the 'old' implementation.
            FileResource fileResource = (FileResource) resource;
            File file = fileResource.getFile();
            String filename = fileResource.getName();
            File basedir = fileResource.getBaseDir();
            return isSelected(basedir, filename, file);
        } else {
            try {
                // How to handle non-file-Resources? I copy temporarily the
                // resource to a file and use the file-implementation.
                FileUtils fu = FileUtils.getFileUtils();
                File tmpFile = fu.createTempFile("modified-", ".tmp", null);
                Resource tmpResource = new FileResource(tmpFile);
                ResourceUtils.copyResource(resource, tmpResource);
                boolean isSelected = isSelected(tmpFile.getParentFile(),
                                                tmpFile.getName(),
                                                resource.toLongString());
                tmpFile.delete();
                return isSelected;
            } catch (UnsupportedOperationException uoe) {
                log("The resource '"
                  + resource.getName()
                  + "' does not provide an InputStream, so it is not checked. "
                  + "Akkording to 'selres' attribute value it is "
                  + ((selectResourcesWithoutInputStream) ? "" : " not")
                  + "selected.", Project.MSG_INFO);
                return selectResourcesWithoutInputStream;
            } catch (Exception e) {
                throw new BuildException(e);
            }
        }
    }


    /**
     * Implementation of BaseExtendSelector.isSelected().
     *
     * @param basedir as described in BaseExtendSelector
     * @param filename as described in BaseExtendSelector
     * @param file as described in BaseExtendSelector
     * @return as described in BaseExtendSelector
     */
    public boolean isSelected(File basedir, String filename, File file) {
        return isSelected(basedir, filename, file.getAbsolutePath());
    }


    /**
     * The business logic of this selector for use as ResourceSelector of
     * FileSelector.
     *
     * @param basedir as described in BaseExtendSelector
     * @param filename as described in BaseExtendSelector
     * @param cacheKey the name for the key for storing the hashvalue
     * @return
     */
    private boolean isSelected(File basedir, String filename, String cacheKey) {
        validate();
        File f = new File(basedir, filename);

        // You can not compute a value for a directory
        if (f.isDirectory()) {
            return selectDirectories;
        }

        // Get the values and do the comparison
        String cachedValue = String.valueOf(cache.get(f.getAbsolutePath()));
        String newValue = algorithm.getValue(f);

        boolean rv = (comparator.compare(cachedValue, newValue) != 0);

        // Maybe update the cache
        if (update && rv) {
            cache.put(f.getAbsolutePath(), newValue);
            setModified(getModified() + 1);
            if (!getDelayUpdate()) {
                saveCache();
            }
        }

        return rv;
    }


   /**
    * save the cache file
    */
    protected void saveCache() {
        if (getModified() > 0) {
            cache.save();
            setModified(0);
        }
    }


    // -----  attribute and nested element support  -----


    /**
     * Setter for algorithmClass.
     * @param classname  new value
     */
    public void setAlgorithmClass(String classname) {
        algorithmClass = classname;
    }


    /**
     * Setter for comparatorClass.
     * @param classname  new value
     */
    public void setComparatorClass(String classname) {
        comparatorClass = classname;
    }


    /**
     * Setter for cacheClass.
     * @param classname  new value
     */
    public void setCacheClass(String classname) {
        cacheClass = classname;
    }


    /**
     * Support for <i>update</i> attribute.
     * @param update new value
     */
    public void setUpdate(boolean update) {
        this.update = update;
    }


    /**
     * Support for <i>seldirs</i> attribute.
     * @param seldirs new value
     */
    public void setSeldirs(boolean seldirs) {
        selectDirectories = seldirs;
    }


    /**
     * Support for <i>selres</i> attribute.
     * @param newValue the new value
     */
    public void setSelres(boolean newValue) {
        this.selectResourcesWithoutInputStream = newValue;
    }


    /**
     * Getter for the modified count
     * @return modified count
     */
    public int getModified() {
        return modified;
    }


    /**
     * Setter for the modified count
     * @param modified count
     */
    public void setModified(int modified) {
        this.modified = modified;
    }


    /**
     * Getter for the delay update
     * @return true if we should delay for performance
     */
    public boolean getDelayUpdate() {
        return delayUpdate;
    }


    /**
     * Setter for the delay update
     * @param delayUpdate true if we should delay for performance
     */
    public void setDelayUpdate(boolean delayUpdate) {
        this.delayUpdate = delayUpdate;
    }


    /**
     * Add the classpath.
     * @param path the classpath
     */
    public void addClasspath(Path path) {
        if (classpath != null) {
            throw new BuildException("<classpath> can be set only once.");
        }
        classpath = path;
    }


    /**
     * Returns and initializes the classloader for this class.
     * @return the classloader
     */
    public ClassLoader getClassLoader() {
        if (myClassLoader == null) {
            myClassLoader = (classpath == null)
                // the usual classloader
                ? getClass().getClassLoader()
                // additional use the provided classpath
                : getProject().createClassLoader(classpath);
        }
        return myClassLoader;
    }


    /**
     * Set the used ClassLoader.
     * If you invoke this selector by API (e.g. inside some testcases) the selector
     * will use a different classloader for loading the interface implementations than
     * the caller. Therefore you will get a ClassCastException if you get the
     * implementations from the selector and cast them.
     * @param loader the ClassLoader to use
     */
    public void setClassLoader(ClassLoader loader) {
        myClassLoader = loader;
    }


    /**
     * Support for nested &lt;param&gt; tags.
     * @param key the key of the parameter
     * @param value the value of the parameter
     */
    public void addParam(String key, Object value) {
        Parameter par = new Parameter();
        par.setName(key);
        par.setValue(String.valueOf(value));
        configParameter.add(par);
    }


    /**
     * Support for nested &lt;param&gt; tags.
     * @param parameter the parameter object
     */
    public void addParam(Parameter parameter) {
        configParameter.add(parameter);
    }


    /**
     * Defined in org.apache.tools.ant.types.Parameterizable.
     * Overwrite implementation in superclass because only special
     * parameters are valid.
     * @see #addParam(String,Object).
     * @param parameters the parameters to set.
     */
    public void setParameters(Parameter[] parameters) {
        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++) {
                configParameter.add(parameters[i]);
            }
        }
    }


    /**
     * Support for nested <param name="" value=""/> tags.
     * Parameter named <i>cache</i>, <i>algorithm</i>,
     * <i>comparator</i> or <i>update</i> are mapped to
     * the respective set-Method.
     * Parameter which names starts with <i>cache.</i> or
     * <i>algorithm.</i> or <i>comparator.</i> are tried
     * to set on the appropriate object via its set-methods.
     * Other parameters are invalid and an BuildException will
     * be thrown.
     *
     * @param parameter  Key and value as parameter object
     */
    public void useParameter(Parameter parameter) {
        String key = parameter.getName();
        String value = parameter.getValue();
        if ("cache".equals(key)) {
            CacheName cn = new CacheName();
            cn.setValue(value);
            setCache(cn);
        } else if ("algorithm".equals(key)) {
            AlgorithmName an = new AlgorithmName();
            an.setValue(value);
            setAlgorithm(an);
        } else if ("comparator".equals(key)) {
            ComparatorName cn = new ComparatorName();
            cn.setValue(value);
            setComparator(cn);
        } else if ("update".equals(key)) {
            boolean updateValue =
                ("true".equalsIgnoreCase(value))
                ? true
                : false;
            setUpdate(updateValue);
        } else if ("delayupdate".equals(key)) {
            boolean updateValue =
                ("true".equalsIgnoreCase(value))
                ? true
                : false;
            setDelayUpdate(updateValue);
        } else if ("seldirs".equals(key)) {
            boolean sdValue =
                ("true".equalsIgnoreCase(value))
                ? true
                : false;
            setSeldirs(sdValue);
        } else if (key.startsWith("cache.")) {
            String name = key.substring(6);
            tryToSetAParameter(cache, name, value);
        } else if (key.startsWith("algorithm.")) {
            String name = key.substring(10);
            tryToSetAParameter(algorithm, name, value);
        } else if (key.startsWith("comparator.")) {
            String name = key.substring(11);
            tryToSetAParameter(comparator, name, value);
        } else {
            setError("Invalid parameter " + key);
        }
    }


    /**
     * Try to set a value on an object using reflection.
     * Helper method for easier access to IntrospectionHelper.setAttribute().
     * @param obj the object on which the attribute should be set
     * @param name the attributename
     * @param value the new value
     */
    protected void tryToSetAParameter(Object obj, String name, String value) {
        Project prj = (getProject() != null) ? getProject() : new Project();
        IntrospectionHelper iHelper
            = IntrospectionHelper.getHelper(prj, obj.getClass());
        try {
            iHelper.setAttribute(prj, obj, name, value);
        } catch (org.apache.tools.ant.BuildException e) {
            // no-op
        }
    }


    // ----- 'beautiful' output -----


    /**
     * Override Object.toString().
     * @return information about this selector
     */
    public String toString() {
        StringBuffer buf = new StringBuffer("{modifiedselector");
        buf.append(" update=").append(update);
        buf.append(" seldirs=").append(selectDirectories);
        buf.append(" cache=").append(cache);
        buf.append(" algorithm=").append(algorithm);
        buf.append(" comparator=").append(comparator);
        buf.append("}");
        return buf.toString();
    }


    // ----- BuildListener interface methods -----


    /**
     * Signals that the last target has finished.
     * @param event recieved BuildEvent
    */
    public void buildFinished(BuildEvent event) {
        if (getDelayUpdate()) {
            saveCache();
        }
    }


    /**
     * Signals that a target has finished.
     * @param event recieved BuildEvent
    */
    public void targetFinished(BuildEvent event) {
        if (getDelayUpdate()) {
            saveCache();
        }
    }


    /**
     * Signals that a task has finished.
     * @param event recieved BuildEvent
    */
    public void taskFinished(BuildEvent event) {
        if (getDelayUpdate()) {
            saveCache();
        }
    }


    /**
     * Signals that a build has started.
     * @param event recieved BuildEvent
    */
    public void buildStarted(BuildEvent event) {
        // no-op
    }


    /**
     * Signals that a target is starting.
     * @param event received BuildEvent
    */
    public void targetStarted(BuildEvent event) {
        // no-op
    }



    /**
     * Signals that a task is starting.
     * @param event recieved BuildEvent
    */
    public void taskStarted(BuildEvent event) {
        // no-op
    }


    /**
     * Signals a message logging event.
     * @param event recieved BuildEvent
    */
    public void messageLogged(BuildEvent event) {
        // no-op
    }


    // The EnumeratedAttributes for the three interface implementations.
    // Name-Classname mapping is done in the configure() method.


    /**
     * Get the cache type to use.
     * @return the enumerated cache type
     */
    public Cache getCache() { return cache; }

    /**
     * Set the cache type to use.
     * @param name an enumerated cache type.
     */
    public void setCache(CacheName name) {
        cacheName = name;
    }

    /**
     * The enumerated type for cache.
     * The values are "propertyfile".
     */
    public static class CacheName extends EnumeratedAttribute {
        /** @see EnumeratedAttribute#getValues() */
        /** {@inheritDoc} */
        public String[] getValues() {
            return new String[] {"propertyfile" };
        }
    }

    /**
     * Get the algorithm type to use.
     * @return the enumerated algorithm type
     */
    public Algorithm getAlgorithm() { return algorithm; }

    /**
     * Set the algorithm type to use.
     * @param name an enumerated algorithm type.
     */
    public void setAlgorithm(AlgorithmName name) {
        algoName = name;
    }

    /**
     * The enumerated type for algorithm.
     * The values are "hashValue", "digest" and "checksum".
     */
    public static class AlgorithmName extends EnumeratedAttribute {
        /** @see EnumeratedAttribute#getValues() */
        /** {@inheritDoc} */
        public String[] getValues() {
            return new String[] {"hashvalue", "digest", "checksum" };
        }
    }

    /**
     * Get the comparator type to use.
     * @return the enumerated comparator type
     */
    public Comparator getComparator() { return comparator; }

    /**
     * Set the comparator type to use.
     * @param name an enumerated comparator type.
     */
    public void setComparator(ComparatorName name) {
        compName = name;
    }

    /**
     * The enumerated type for algorithm.
     * The values are "equal" and "rule".
     */
    public static class ComparatorName extends EnumeratedAttribute {
        /** @see EnumeratedAttribute#getValues() */
        /** {@inheritDoc} */
        public String[] getValues() {
            return new String[] {"equal", "rule" };
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15008.java