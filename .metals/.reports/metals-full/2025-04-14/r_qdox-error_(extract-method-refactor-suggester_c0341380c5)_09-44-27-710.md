error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8535.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8535.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8535.java
text:
```scala
b@@uildFile = fu.normalize(buildFile.getAbsolutePath());

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

package org.apache.tools.ant.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Stack;

import org.xml.sax.Locator;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import org.apache.tools.ant.util.JAXPUtils;
import org.apache.tools.ant.util.FileUtils;

import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.RuntimeConfigurable;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Location;
import org.apache.tools.ant.UnknownElement;

import org.xml.sax.XMLReader;

/**
 * Sax2 based project reader
 *
 */
public class ProjectHelper2 extends ProjectHelper {
    /* Stateless */

    // singletons - since all state is in the context
    private static AntHandler elementHandler = new ElementHandler();
    private static AntHandler targetHandler = new TargetHandler();
    private static AntHandler mainHandler = new MainHandler();
    private static AntHandler projectHandler = new ProjectHandler();

    /**
     * helper for path -> URI and URI -> path conversions.
     */
    private static FileUtils fu = FileUtils.newFileUtils();

    /**
     * Parse an unknown element from a url
     *
     * @param project the current project
     * @param source  the url containing the task
     * @return a configured task
     * @exception BuildException if an error occurs
     */
    public UnknownElement parseUnknownElement(Project project, URL source)
        throws BuildException {
        Target dummyTarget = new Target();
        dummyTarget.setProject(project);

        AntXMLContext context = new AntXMLContext(project);
        context.addTarget(dummyTarget);
        context.setImplicitTarget(dummyTarget);

        parse(context.getProject(), source,
              new RootHandler(context, elementHandler));
        Task[] tasks = dummyTarget.getTasks();
        if (tasks.length != 1) {
            throw new BuildException("No tasks defined");
        }
        return (UnknownElement) tasks[0];
    }
    /**
     * Parse a source xml input.
     *
     * @param project the current project
     * @param source  the xml source
     * @exception BuildException if an error occurs
     */
    public void parse(Project project, Object source)
            throws BuildException {
        getImportStack().addElement(source);
        //System.out.println("Adding " + source);
        AntXMLContext context = null;
        context = (AntXMLContext) project.getReference("ant.parsing.context");
//        System.out.println("Parsing " + getImportStack().size() + " " +
//                context+ " " + getImportStack() );
        if (context == null) {
            context = new AntXMLContext(project);
            project.addReference("ant.parsing.context", context);
            project.addReference("ant.targets", context.getTargets());
        }

        if (getImportStack().size() > 1) {
            // we are in an imported file.
            context.setIgnoreProjectTag(true);
            Target currentTarget = context.getCurrentTarget();
            try {
                Target newCurrent = new Target();
                newCurrent.setProject(project);
                newCurrent.setName("");
                context.setCurrentTarget(newCurrent);
                parse(project, source, new RootHandler(context, mainHandler));
                newCurrent.execute();
            } finally {
                context.setCurrentTarget(currentTarget);
            }
        } else {
            // top level file
            parse(project, source, new RootHandler(context, mainHandler));
            // Execute the top-level target
            context.getImplicitTarget().execute();
        }
    }

    /**
     * Parses the project file, configuring the project as it goes.
     *
     * @param project the current project
     * @param source  the xml source
     * @param handler the root handler to use (contains the current context)
     * @exception BuildException if the configuration is invalid or cannot
     *                           be read
     */
    public void parse(Project project, Object source, RootHandler handler)
            throws BuildException {

        AntXMLContext context = handler.context;

        File buildFile = null;
        URL  url = null;
        String buildFileName = null;

        if (source instanceof File) {
            buildFile = (File) source;
            buildFile = new File(buildFile.getAbsolutePath());
            context.setBuildFile(buildFile);
            buildFileName = buildFile.toString();
//         } else if (source instanceof InputStream ) {
        } else if (source instanceof URL) {
            if (handler.getCurrentAntHandler() != elementHandler) {
                throw new BuildException(
                    "Source " + source.getClass().getName()
                    + " not supported by this plugin for "
                    + " non task xml");
            }
            url = (URL) source;
            buildFileName = url.toString();
//         } else if (source instanceof InputSource ) {
        } else {
            throw new BuildException("Source " + source.getClass().getName()
                                     + " not supported by this plugin");
        }

        InputStream inputStream = null;
        InputSource inputSource = null;


        try {
            /**
             * SAX 2 style parser used to parse the given file.
             */
            XMLReader parser = JAXPUtils.getNamespaceXMLReader();

            String uri = null;
            if (buildFile != null) {
                uri = fu.toURI(buildFile.getAbsolutePath());
                inputStream = new FileInputStream(buildFile);
            } else {
                inputStream = url.openStream();
                uri = url.toString(); // ?? OK ??
            }

            inputSource = new InputSource(inputStream);
            if (uri != null) {
                inputSource.setSystemId(uri);
            }
            project.log("parsing buildfile " + buildFileName
                        + " with URI = " + uri, Project.MSG_VERBOSE);

            DefaultHandler hb = handler;

            parser.setContentHandler(hb);
            parser.setEntityResolver(hb);
            parser.setErrorHandler(hb);
            parser.setDTDHandler(hb);
            parser.parse(inputSource);
        } catch (SAXParseException exc) {
            Location location = new Location(exc.getSystemId(),
                exc.getLineNumber(), exc.getColumnNumber());

            Throwable t = exc.getException();
            if (t instanceof BuildException) {
                BuildException be = (BuildException) t;
                if (be.getLocation() == Location.UNKNOWN_LOCATION) {
                    be.setLocation(location);
                }
                throw be;
            }

            throw new BuildException(exc.getMessage(), t, location);
        } catch (SAXException exc) {
            Throwable t = exc.getException();
            if (t instanceof BuildException) {
                throw (BuildException) t;
            }
            throw new BuildException(exc.getMessage(), t);
        } catch (FileNotFoundException exc) {
            throw new BuildException(exc);
        } catch (UnsupportedEncodingException exc) {
              throw new BuildException("Encoding of project file "
                                       + buildFileName + " is invalid.",
                                       exc);
        } catch (IOException exc) {
            throw new BuildException("Error reading project file "
                                     + buildFileName + ": " + exc.getMessage(),
                                     exc);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ioe) {
                    // ignore this
                }
            }
        }
    }

    /**
     * The common superclass for all SAX event handlers used to parse
     * the configuration file.
     *
     * The context will hold all state information. At each time
     * there is one active handler for the current element. It can
     * use onStartChild() to set an alternate handler for the child.
     */
    public static class AntHandler  {
        /**
         * Handles the start of an element. This base implementation does
         * nothing.
         *
         * @param uri the namespace URI for the tag
         * @param tag The name of the element being started.
         *            Will not be <code>null</code>.
         * @param qname The qualified name of the element.
         * @param attrs Attributes of the element being started.
         *              Will not be <code>null</code>.
         * @param context The context that this element is in.
         *
         * @exception SAXParseException if this method is not overridden, or in
         *                              case of error in an overridden version
         */
        public void onStartElement(String uri, String tag, String qname,
                                   Attributes attrs,
                                   AntXMLContext context)
            throws SAXParseException {
        }

        /**
         * Handles the start of an element. This base implementation just
         * throws an exception - you must override this method if you expect
         * child elements.
         *
         * @param uri The namespace uri for this element.
         * @param tag The name of the element being started.
         *            Will not be <code>null</code>.
         * @param qname The qualified name for this element.
         * @param attrs Attributes of the element being started.
         *              Will not be <code>null</code>.
         * @param context The current context.
         * @return a handler (in the derived classes)
         *
         * @exception SAXParseException if this method is not overridden, or in
         *                              case of error in an overridden version
         */
        public AntHandler onStartChild(String uri, String tag, String qname,
                                       Attributes attrs,
                                       AntXMLContext context)
            throws SAXParseException {
            throw new SAXParseException("Unexpected element \"" + qname
                + " \"", context.getLocator());
        }

        /**
         * Handle the end of a element.
         *
         * @param uri the namespace uri of the element
         * @param tag the tag of the element
         * @param qname the qualified name of the element
         * @param context the current context
         * @exception SAXParseException if an error occurs
         */
        public void onEndChild(String uri, String tag, String qname,
                                     AntXMLContext context)
            throws SAXParseException {
        }

        /**
         * Called when this element and all elements nested into it have been
         * handled (i.e. at the </end_tag_of_the_element> ).
         * @param uri the namespace uri for this element
         * @param tag the element name
         * @param context the current context
         */
        public void onEndElement(String uri, String tag,
                                 AntXMLContext context) {
        }

        /**
         * Handles text within an element. This base implementation just
         * throws an exception, you must override it if you expect content.
         *
         * @param buf A character array of the text within the element.
         *            Will not be <code>null</code>.
         * @param start The start element in the array.
         * @param count The number of characters to read from the array.
         * @param context The current context.
         *
         * @exception SAXParseException if this method is not overridden, or in
         *                              case of error in an overridden version
         */
        public void characters(char[] buf, int start, int count, AntXMLContext context)
            throws SAXParseException {
            String s = new String(buf, start, count).trim();

            if (s.length() > 0) {
                throw new SAXParseException("Unexpected text \"" + s
                    + "\"", context.getLocator());
            }
        }

        /**
         * Will be called every time a namespace is reached.
         * It'll verify if the ns was processed, and if not load the task
         * definitions.
         * @param uri The namespace uri.
         */
        protected void checkNamespace(String uri) {

        }
    }

    /**
     * Handler for ant processing. Uses a stack of AntHandlers to
     * implement each element ( the original parser used a recursive behavior,
     * with the implicit execution stack )
     */
    public static class RootHandler extends DefaultHandler {
        private Stack antHandlers = new Stack();
        private AntHandler currentHandler = null;
        private AntXMLContext context;

        /**
         * Creates a new RootHandler instance.
         *
         * @param context The context for the handler.
         * @param rootHandler The handler for the root element.
         */
        public RootHandler(AntXMLContext context, AntHandler rootHandler) {
            currentHandler = rootHandler;
            antHandlers.push(currentHandler);
            this.context = context;
        }

        /**
         * Returns the current ant handler object.
         * @return the current ant handler.
         */
        public AntHandler getCurrentAntHandler() {
            return currentHandler;
        }

        /**
         * Resolves file: URIs relative to the build file.
         *
         * @param publicId The public identifier, or <code>null</code>
         *                 if none is available. Ignored in this
         *                 implementation.
         * @param systemId The system identifier provided in the XML
         *                 document. Will not be <code>null</code>.
         * @return an inputsource for this identifier
         */
        public InputSource resolveEntity(String publicId,
                                         String systemId) {

            context.getProject().log("resolving systemId: "
                + systemId, Project.MSG_VERBOSE);

            if (systemId.startsWith("file:")) {
                String path = fu.fromURI(systemId);

                File file = new File(path);
                if (!file.isAbsolute()) {
                    file = fu.resolveFile(context.getBuildFileParent(), path);
                }
                try {
                    InputSource inputSource =
                            new InputSource(new FileInputStream(file));
                    inputSource.setSystemId(fu.toURI(file.getAbsolutePath()));
                    return inputSource;
                } catch (FileNotFoundException fne) {
                    context.getProject().log(file.getAbsolutePath()
                        + " could not be found", Project.MSG_WARN);
                }

            }
            // use default if not file or file not found
            return null;
        }

        /**
         * Handles the start of a project element. A project handler is created
         * and initialised with the element name and attributes.
         *
         * @param uri The namespace uri for this element.
         * @param tag The name of the element being started.
         *            Will not be <code>null</code>.
         * @param qname The qualified name for this element.
         * @param attrs Attributes of the element being started.
         *              Will not be <code>null</code>.
         *
         * @exception org.xml.sax.SAXParseException if the tag given is not
         *                              <code>"project"</code>
         */
        public void startElement(String uri, String tag, String qname, Attributes attrs)
            throws SAXParseException {
            AntHandler next
                = currentHandler.onStartChild(uri, tag, qname, attrs, context);
            antHandlers.push(currentHandler);
            currentHandler = next;
            currentHandler.onStartElement(uri, tag, qname, attrs, context);
        }

        /**
         * Sets the locator in the project helper for future reference.
         *
         * @param locator The locator used by the parser.
         *                Will not be <code>null</code>.
         */
        public void setDocumentLocator(Locator locator) {
            context.setLocator(locator);
        }

        /**
         * Handles the end of an element. Any required clean-up is performed
         * by the onEndElement() method and then the original handler
         * is restored to the parser.
         *
         * @param uri  The namespace URI for this element.
         * @param name The name of the element which is ending.
         *             Will not be <code>null</code>.
         * @param qName The qualified name for this element.
         *
         * @exception SAXException in case of error (not thrown in
         *                         this implementation)
         *
         */
        public void endElement(String uri, String name, String qName) throws SAXException {
            currentHandler.onEndElement(uri, name, context);
            AntHandler prev = (AntHandler) antHandlers.pop();
            currentHandler = prev;
            if (currentHandler != null) {
                currentHandler.onEndChild(uri, name, qName, context);
            }
        }

        /**
         * Handle text within an element, calls currentHandler.characters.
         *
         * @param buf  A character array of the test.
         * @param start The start offset in the array.
         * @param count The number of characters to read.
         * @exception SAXParseException if an error occurs
         */
        public void characters(char[] buf, int start, int count)
            throws SAXParseException {
            currentHandler.characters(buf, start, count, context);
        }

        /**
         * Start a namespace prefix to uri mapping
         *
         * @param prefix the namespace prefix
         * @param uri the namespace uri
         */
        public void startPrefixMapping(String prefix, String uri) {
            context.startPrefixMapping(prefix, uri);
        }

        /**
         * End a namepace prefix to uri mapping
         *
         * @param prefix the prefix that is not mapped anymore
         */
        public void endPrefixMapping(String prefix) {
            context.endPrefixMapping(prefix);
        }
    }

    /**
     * The main handler - it handles the &lt;project&gt; tag.
     *
     * @see AntHandler
     */
    public static class MainHandler extends AntHandler {

        /**
         * Handle the project tag
         *
         * @param uri The namespace uri.
         * @param name The element tag.
         * @param qname The element qualified name.
         * @param attrs The attributes of the element.
         * @param context The current context.
         * @return The project handler that handles subelements of project
         * @exception SAXParseException if the qualified name is not "project".
         */
        public AntHandler onStartChild(String uri, String name, String qname,
                                       Attributes attrs,
                                       AntXMLContext context)
            throws SAXParseException {
            if (name.equals("project")
                && (uri.equals("") || uri.equals(ANT_CORE_URI))) {
                return ProjectHelper2.projectHandler;
            } else {
//                 if (context.importlevel > 0) {
//                     // we are in an imported file. Allow top-level <target>.
//                     if (qname.equals( "target" ) )
//                         return ProjectHelper2.targetHandler;
//                 }
                throw new SAXParseException("Unexpected element \"" + qname
                    + "\" " + name, context.getLocator());
            }
        }
    }

    /**
     * Handler for the top level "project" element.
     */
    public static class ProjectHandler extends AntHandler {

        /**
         * Initialisation routine called after handler creation
         * with the element name and attributes. The attributes which
         * this handler can deal with are: <code>"default"</code>,
         * <code>"name"</code>, <code>"id"</code> and <code>"basedir"</code>.
         *
         * @param uri The namespace URI for this element.
         * @param tag Name of the element which caused this handler
         *            to be created. Should not be <code>null</code>.
         *            Ignored in this implementation.
         * @param qname The qualified name for this element.
         * @param attrs Attributes of the element which caused this
         *              handler to be created. Must not be <code>null</code>.
         * @param context The current context.
         *
         * @exception SAXParseException if an unexpected attribute is
         *            encountered or if the <code>"default"</code> attribute
         *            is missing.
         */
        public void onStartElement(String uri, String tag, String qname,
                                   Attributes attrs,
                                   AntXMLContext context)
            throws SAXParseException {
            String id = null;
            String baseDir = null;
            boolean nameAttributeSet = false;

            Project project = context.getProject();

            /** XXX I really don't like this - the XML processor is still
             * too 'involved' in the processing. A better solution (IMO)
             * would be to create UE for Project and Target too, and
             * then process the tree and have Project/Target deal with
             * its attributes ( similar with Description ).
             *
             * If we eventually switch to ( or add support for ) DOM,
             * things will work smoothly - UE can be avoided almost completely
             * ( it could still be created on demand, for backward compatibility )
             */

            for (int i = 0; i < attrs.getLength(); i++) {
                String attrUri = attrs.getURI(i);
                if (attrUri != null
                    && !attrUri.equals("")
                    && !attrUri.equals(uri)) {
                    continue; // Ignore attributes from unknown uris
                }
                String key = attrs.getLocalName(i);
                String value = attrs.getValue(i);

                if (key.equals("default")) {
                    if (value != null && !value.equals("")) {
                        if (!context.isIgnoringProjectTag()) {
                            project.setDefault(value);
                        }
                    }
                } else if (key.equals("name")) {
                    if (value != null) {
                        context.setCurrentProjectName(value);
                        nameAttributeSet = true;
                        if (!context.isIgnoringProjectTag()) {
                            project.setName(value);
                            project.addReference(value, project);
                        }
                    }
                } else if (key.equals("id")) {
                    if (value != null) {
                        // What's the difference between id and name ?
                        if (!context.isIgnoringProjectTag()) {
                            project.addReference(value, project);
                        }
                    }
                } else if (key.equals("basedir")) {
                    if (!context.isIgnoringProjectTag()) {
                        baseDir = value;
                    }
                } else {
                    // XXX ignore attributes in a different NS ( maybe store them ? )
                    throw new SAXParseException("Unexpected attribute \""
                        + attrs.getQName(i) + "\"", context.getLocator());
                }
            }

            // XXX Move to Project ( so it is shared by all helpers )
            String antFileProp = "ant.file." + context.getCurrentProjectName();
            String dup = project.getProperty(antFileProp);
            if (dup != null && nameAttributeSet) {
                File dupFile = new File(dup);
                if (context.isIgnoringProjectTag()
                    && !dupFile.equals(context.getBuildFile())) {
                    project.log("Duplicated project name in import. Project "
                        + context.getCurrentProjectName() + " defined first in "
                        + dup + " and again in " + context.getBuildFile(),
                        Project.MSG_WARN);
                }
            }

            if (context.getBuildFile() != null) {
                project.setUserProperty("ant.file."
                    + context.getCurrentProjectName(),
                    context.getBuildFile().toString());
            }

            if (context.isIgnoringProjectTag()) {
                // no further processing
                return;
            }
            // set explicitly before starting ?
            if (project.getProperty("basedir") != null) {
                project.setBasedir(project.getProperty("basedir"));
            } else {
                // Default for baseDir is the location of the build file.
                if (baseDir == null) {
                    project.setBasedir(context.getBuildFileParent().getAbsolutePath());
                } else {
                    // check whether the user has specified an absolute path
                    if ((new File(baseDir)).isAbsolute()) {
                        project.setBasedir(baseDir);
                    } else {
                        project.setBaseDir(fu.resolveFile(
                                               context.getBuildFileParent(), baseDir));
                    }
                }
            }

            project.addTarget("", context.getImplicitTarget());
            context.setCurrentTarget(context.getImplicitTarget());
        }

        /**
         * Handles the start of a top-level element within the project. An
         * appropriate handler is created and initialised with the details
         * of the element.
         *
         * @param uri The namespace URI for this element.
         * @param name The name of the element being started.
         *            Will not be <code>null</code>.
         * @param qname The qualified name for this element.
         * @param attrs Attributes of the element being started.
         *              Will not be <code>null</code>.
         * @param context The context for this element.
         * @return a target or an element handler.
         *
         * @exception org.xml.sax.SAXParseException if the tag given is not
         *            <code>"taskdef"</code>, <code>"typedef"</code>,
         *            <code>"property"</code>, <code>"target"</code>
         *            or a data type definition
         */
        public AntHandler onStartChild(String uri, String name, String qname,
                                       Attributes attrs,
                                       AntXMLContext context)
            throws SAXParseException {
            if (name.equals("target")
                && (uri.equals("") || uri.equals(ANT_CORE_URI))) {
                return ProjectHelper2.targetHandler;
            } else {
                return ProjectHelper2.elementHandler;
            }
        }

    }

    /**
     * Handler for "target" elements.
     */
    public static class TargetHandler extends AntHandler {

        /**
         * Initialisation routine called after handler creation
         * with the element name and attributes. The attributes which
         * this handler can deal with are: <code>"name"</code>,
         * <code>"depends"</code>, <code>"if"</code>,
         * <code>"unless"</code>, <code>"id"</code> and
         * <code>"description"</code>.
         *
         * @param uri The namespace URI for this element.
         * @param tag Name of the element which caused this handler
         *            to be created. Should not be <code>null</code>.
         *            Ignored in this implementation.
         * @param qname The qualified name for this element.
         * @param attrs Attributes of the element which caused this
         *              handler to be created. Must not be <code>null</code>.
         * @param context The current context.
         *
         * @exception SAXParseException if an unexpected attribute is encountered
         *            or if the <code>"name"</code> attribute is missing.
         */
        public void onStartElement(String uri, String tag, String qname,
                                   Attributes attrs,
                                   AntXMLContext context)
            throws SAXParseException {
            String name = null;
            String depends = "";

            Project project = context.getProject();
            Target target = new Target();
            target.setProject(project);
            context.addTarget(target);

            for (int i = 0; i < attrs.getLength(); i++) {
                String attrUri = attrs.getURI(i);
                if (attrUri != null
                    && !attrUri.equals("")
                    && !attrUri.equals(uri)) {
                    continue; // Ignore attributes from unknown uris
                }
                String key = attrs.getLocalName(i);
                String value = attrs.getValue(i);

                if (key.equals("name")) {
                    name = value;
                    if ("".equals(name)) {
                        throw new BuildException("name attribute must "
                            + "not be empty");
                    }
                } else if (key.equals("depends")) {
                    depends = value;
                } else if (key.equals("if")) {
                    target.setIf(value);
                } else if (key.equals("unless")) {
                    target.setUnless(value);
                } else if (key.equals("id")) {
                    if (value != null && !value.equals("")) {
                        context.getProject().addReference(value, target);
                    }
                } else if (key.equals("description")) {
                    target.setDescription(value);
                } else {
                    throw new SAXParseException("Unexpected attribute \""
                        + key + "\"", context.getLocator());
                }
            }

            if (name == null) {
                throw new SAXParseException("target element appears without "
                    + "a name attribute", context.getLocator());
            }

            Hashtable currentTargets = project.getTargets();

            // If the name has already been defined ( import for example )
            if (currentTargets.containsKey(name)) {
                if (!context.isIgnoringProjectTag()) {
                    // not in an import'ed file
                    throw new BuildException(
                        "Duplicate target '" + name + "'",
                        new Location(context.getLocator().getSystemId(),
                                     context.getLocator().getLineNumber(),
                                     context.getLocator().getColumnNumber()));
                }
                // Alter the name.
                if (context.getCurrentProjectName() != null) {
                    String newName = context.getCurrentProjectName()
                        + "." + name;
                    project.log("Already defined in main or a previous import, "
                        + "define " + name + " as " + newName,
                                Project.MSG_VERBOSE);
                    name = newName;
                } else {
                    project.log("Already defined in main or a previous import, "
                        + "ignore " + name, Project.MSG_VERBOSE);
                    name = null;
                }
            }

            if (name != null) {
                target.setName(name);
                project.addOrReplaceTarget(name, target);
            }

            // take care of dependencies
            if (depends.length() > 0) {
                target.setDepends(depends);
            }
        }

        /**
         * Handles the start of an element within a target.
         *
         * @param uri The namespace URI for this element.
         * @param name The name of the element being started.
         *            Will not be <code>null</code>.
         * @param qname The qualified name for this element.
         * @param attrs Attributes of the element being started.
         *              Will not be <code>null</code>.
         * @param context The current context.
         * @return an element handler.
         *
         * @exception SAXParseException if an error occurs when initialising
         *                              the appropriate child handler
         */
        public AntHandler onStartChild(String uri, String name, String qname,
                                       Attributes attrs,
                                       AntXMLContext context)
            throws SAXParseException {
            return ProjectHelper2.elementHandler;
        }

        /**
         * Handle the end of the project, sets the current target of the
         * context to be the implicit target.
         *
         * @param uri The namespace URI of the element.
         * @param tag The name of the element.
         * @param context The current context.
         */
        public void onEndElement(String uri, String tag, AntXMLContext context) {
            context.setCurrentTarget(context.getImplicitTarget());
        }
    }

    /**
     * Handler for all project elements ( tasks, data types )
     */
    public static class ElementHandler extends AntHandler {

        /**
         * Constructor.
         */
        public ElementHandler() {
        }

        /**
         * Initialisation routine called after handler creation
         * with the element name and attributes. This configures
         * the element with its attributes and sets it up with
         * its parent container (if any). Nested elements are then
         * added later as the parser encounters them.
         *
         * @param uri The namespace URI for this element.
         * @param tag Name of the element which caused this handler
         *            to be created. Must not be <code>null</code>.
         * @param qname The qualified name for this element.
         * @param attrs Attributes of the element which caused this
         *              handler to be created. Must not be <code>null</code>.
         * @param context The current context.
         *
         * @exception SAXParseException in case of error (not thrown in
         *                              this implementation)
         */
        public void onStartElement(String uri, String tag, String qname,
                                   Attributes attrs,
                                   AntXMLContext context)
            throws SAXParseException {
            RuntimeConfigurable parentWrapper = context.currentWrapper();
            Object parent = null;

            if (parentWrapper != null) {
                parent = parentWrapper.getProxy();
            }

            /* UnknownElement is used for tasks and data types - with
               delayed eval */
            UnknownElement task = new UnknownElement(tag);
            task.setProject(context.getProject());
            task.setNamespace(uri);
            task.setQName(qname);
            task.setTaskType(
                ProjectHelper.genComponentName(task.getNamespace(), tag));
            task.setTaskName(qname);

            Location location = new Location(context.getLocator().getSystemId(),
                    context.getLocator().getLineNumber(),
                    context.getLocator().getColumnNumber());
            task.setLocation(location);
            task.setOwningTarget(context.getCurrentTarget());

            context.configureId(task, attrs);

            if (parent != null) {
                // Nested element
                ((UnknownElement) parent).addChild(task);
            }  else {
                // Task included in a target ( including the default one ).
                context.getCurrentTarget().addTask(task);
            }

            // container.addTask(task);
            // This is a nop in UE: task.init();

            RuntimeConfigurable wrapper
                = new RuntimeConfigurable(task, task.getTaskName());

            for (int i = 0; i < attrs.getLength(); i++) {
                String attrUri = attrs.getURI(i);
                if (attrUri != null
                    && !attrUri.equals("")
                    && !attrUri.equals(uri)) {
                    continue; // Ignore attributes from unknown uris
                }
                String name = attrs.getLocalName(i);
                String value = attrs.getValue(i);
                // PR: Hack for ant-type value
                //  an ant-type is a component name which can
                // be namespaced, need to extract the name
                // and convert from qualified name to uri/name
                if (name.equals("ant-type")) {
                    int index = value.indexOf(":");
                    if (index != -1) {
                        String prefix = value.substring(0, index);
                        String mappedUri = context.getPrefixMapping(prefix);
                        if (mappedUri == null) {
                            throw new BuildException(
                                "Unable to find XML NS prefix " + prefix);
                        }
                        value = ProjectHelper.genComponentName(
                            mappedUri, value.substring(index + 1));
                    }
                }
                wrapper.setAttribute(name, value);
            }

            if (parentWrapper != null) {
                parentWrapper.addChild(wrapper);
            }

            context.pushWrapper(wrapper);
        }

        /**
         * Adds text to the task, using the wrapper
         *
         * @param buf A character array of the text within the element.
         *            Will not be <code>null</code>.
         * @param start The start element in the array.
         * @param count The number of characters to read from the array.
         * @param context The current context.
         *
         * @exception SAXParseException if the element doesn't support text
         *
         * @see ProjectHelper#addText(Project,java.lang.Object,char[],int,int)
         */
        public void characters(char[] buf, int start, int count,
                               AntXMLContext context)
            throws SAXParseException {
            RuntimeConfigurable wrapper = context.currentWrapper();
            wrapper.addText(buf, start, count);
        }

        /**
         * Handles the start of an element within a target. Task containers
         * will always use another task handler, and all other tasks
         * will always use a nested element handler.
         *
         * @param uri The namespace URI for this element.
         * @param tag The name of the element being started.
         *            Will not be <code>null</code>.
         * @param qname The qualified name for this element.
         * @param attrs Attributes of the element being started.
         *              Will not be <code>null</code>.
         * @param context The current context.
         * @return The handler for elements.
         *
         * @exception SAXParseException if an error occurs when initialising
         *                              the appropriate child handler
         */
        public AntHandler onStartChild(String uri, String tag, String qname,
                                       Attributes attrs,
                                       AntXMLContext context)
            throws SAXParseException {
            return ProjectHelper2.elementHandler;
        }

        /**
         * Handles the end of the element. This pops the wrapper from
         * the context.
         *
         * @param uri The namespace URI for the element.
         * @param tag The name of the element.
         * @param context The current context.
         */
        public void onEndElement(String uri, String tag, AntXMLContext context) {
            context.popWrapper();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8535.java