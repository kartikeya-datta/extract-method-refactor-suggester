error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16725.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16725.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16725.java
text:
```scala
i@@h = IntrospectionHelper.getHelper(getProject(), element);

/*
 * Copyright  2000-2005 The Apache Software Foundation
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

package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.IntrospectionHelper;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.TaskContainer;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.Reference;

/**
 * Creates a partial DTD for Ant from the currently known tasks.
 *
 *
 * @since Ant 1.1
 *
 * @ant.task category="xml"
 */
public class AntStructure extends Task {

    private final String lSep = System.getProperty("line.separator");

    private static final String BOOLEAN = "%boolean;";
    private static final String TASKS = "%tasks;";
    private static final String TYPES = "%types;";

    private Hashtable visited = new Hashtable();

    private File output;

    /**
     * The output file.
     * @param output the output file
     */
    public void setOutput(File output) {
        this.output = output;
    }

    /**
     * Build the antstructure DTD.
     *
     * @exception BuildException if the DTD cannot be written.
     */
    public void execute() throws BuildException {

        if (output == null) {
            throw new BuildException("output attribute is required", getLocation());
        }

        PrintWriter out = null;
        try {
            try {
                out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(output), "UTF8"));
            } catch (UnsupportedEncodingException ue) {
                /*
                 * Plain impossible with UTF8, see
                 * http://java.sun.com/products/jdk/1.2/docs/guide/internat/encoding.doc.html
                 *
                 * fallback to platform specific anyway.
                 */
                out = new PrintWriter(new FileWriter(output));
            }

            printHead(out, getProject().getTaskDefinitions().keys(),
                      getProject().getDataTypeDefinitions().keys());

            printTargetDecl(out);

            Enumeration dataTypes = getProject().getDataTypeDefinitions().keys();
            while (dataTypes.hasMoreElements()) {
                String typeName = (String) dataTypes.nextElement();
                printElementDecl(out, typeName,
                                 (Class) getProject().getDataTypeDefinitions().get(typeName));
            }

            Enumeration tasks = getProject().getTaskDefinitions().keys();
            while (tasks.hasMoreElements()) {
                String tName = (String) tasks.nextElement();
                printElementDecl(out, tName,
                                 (Class) getProject().getTaskDefinitions().get(tName));
            }

        } catch (IOException ioe) {
            throw new BuildException("Error writing "
                + output.getAbsolutePath(), ioe, getLocation());
        } finally {
            if (out != null) {
                out.close();
            }
            visited.clear();
        }
    }

    /**
     * Prints the header of the generated output.
     *
     * <p>Basically this prints the XML declaration, defines some
     * entities and the project element.</p>
     */
    private void printHead(PrintWriter out, Enumeration tasks,
                           Enumeration types) {
        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        out.println("<!ENTITY % boolean \"(true|false|on|off|yes|no)\">");
        out.print("<!ENTITY % tasks \"");
        boolean first = true;
        while (tasks.hasMoreElements()) {
            String tName = (String) tasks.nextElement();
            if (!first) {
                out.print(" | ");
            } else {
                first = false;
            }
            out.print(tName);
        }
        out.println("\">");
        out.print("<!ENTITY % types \"");
        first = true;
        while (types.hasMoreElements()) {
            String typeName = (String) types.nextElement();
            if (!first) {
                out.print(" | ");
            } else {
                first = false;
            }
            out.print(typeName);
        }
        out.println("\">");

        out.println("");

        out.print("<!ELEMENT project (target | ");
        out.print(TASKS);
        out.print(" | ");
        out.print(TYPES);
        out.println(")*>");
        out.println("<!ATTLIST project");
        out.println("          name    CDATA #IMPLIED");
        out.println("          default CDATA #IMPLIED");
        out.println("          basedir CDATA #IMPLIED>");
        out.println("");
    }

    /**
     * Prints the definition for the target element.
     */
    private void printTargetDecl(PrintWriter out) {
        out.print("<!ELEMENT target (");
        out.print(TASKS);
        out.print(" | ");
        out.print(TYPES);
        out.println(")*>");
        out.println("");

        out.println("<!ATTLIST target");
        out.println("          id          ID    #IMPLIED");
        out.println("          name        CDATA #REQUIRED");
        out.println("          if          CDATA #IMPLIED");
        out.println("          unless      CDATA #IMPLIED");
        out.println("          depends     CDATA #IMPLIED");
        out.println("          description CDATA #IMPLIED>");
        out.println("");
    }

    /**
     * Print the definition for a given element.
     */
    private void printElementDecl(PrintWriter out, String name, Class element)
        throws BuildException {

        if (visited.containsKey(name)) {
            return;
        }
        visited.put(name, "");

        IntrospectionHelper ih = null;
        try {
            ih = IntrospectionHelper.getHelper(element);
        } catch (Throwable t) {
            /*
             * XXX - failed to load the class properly.
             *
             * should we print a warning here?
             */
            return;
        }

        StringBuffer sb = new StringBuffer("<!ELEMENT ");
        sb.append(name).append(" ");

        if (org.apache.tools.ant.types.Reference.class.equals(element)) {
            sb.append("EMPTY>").append(lSep);
            sb.append("<!ATTLIST ").append(name);
            sb.append(lSep).append("          id ID #IMPLIED");
            sb.append(lSep).append("          refid IDREF #IMPLIED");
            sb.append(">").append(lSep);
            out.println(sb);
            return;
        }

        Vector v = new Vector();
        if (ih.supportsCharacters()) {
            v.addElement("#PCDATA");
        }

        if (TaskContainer.class.isAssignableFrom(element)) {
            v.addElement(TASKS);
        }

        Enumeration e = ih.getNestedElements();
        while (e.hasMoreElements()) {
            v.addElement(e.nextElement());
        }

        if (v.isEmpty()) {
            sb.append("EMPTY");
        } else {
            sb.append("(");
            final int count = v.size();
            for (int i = 0; i < count; i++) {
                if (i != 0) {
                    sb.append(" | ");
                }
                sb.append(v.elementAt(i));
            }
            sb.append(")");
            if (count > 1 || !v.elementAt(0).equals("#PCDATA")) {
                sb.append("*");
            }
        }
        sb.append(">");
        out.println(sb);

        sb = new StringBuffer("<!ATTLIST ");
        sb.append(name);
        sb.append(lSep).append("          id ID #IMPLIED");

        e = ih.getAttributes();
        while (e.hasMoreElements()) {
            String attrName = (String) e.nextElement();
            if ("id".equals(attrName)) {
              continue;
            }

            sb.append(lSep).append("          ").append(attrName).append(" ");
            Class type = ih.getAttributeType(attrName);
            if (type.equals(java.lang.Boolean.class)
 type.equals(java.lang.Boolean.TYPE)) {
                sb.append(BOOLEAN).append(" ");
            } else if (Reference.class.isAssignableFrom(type)) {
                sb.append("IDREF ");
            } else if (EnumeratedAttribute.class.isAssignableFrom(type)) {
                try {
                    EnumeratedAttribute ea =
                        (EnumeratedAttribute) type.newInstance();
                    String[] values = ea.getValues();
                    if (values == null
 values.length == 0
 !areNmtokens(values)) {
                        sb.append("CDATA ");
                    } else {
                        sb.append("(");
                        for (int i = 0; i < values.length; i++) {
                            if (i != 0) {
                                sb.append(" | ");
                            }
                            sb.append(values[i]);
                        }
                        sb.append(") ");
                    }
                } catch (InstantiationException ie) {
                    sb.append("CDATA ");
                } catch (IllegalAccessException ie) {
                    sb.append("CDATA ");
                }
            } else {
                sb.append("CDATA ");
            }
            sb.append("#IMPLIED");
        }
        sb.append(">").append(lSep);
        out.println(sb);

        final int count = v.size();
        for (int i = 0; i < count; i++) {
            String nestedName = (String) v.elementAt(i);
            if (!"#PCDATA".equals(nestedName)
                 && !TASKS.equals(nestedName)
                 && !TYPES.equals(nestedName)) {
                printElementDecl(out, nestedName, ih.getElementType(nestedName));
            }
        }
    }

    /**
     * Does this String match the XML-NMTOKEN production?
     * @param s the string to test
     * @return true if the string matches the XML-NMTOKEN
     */
    protected boolean isNmtoken(String s) {
        final int length = s.length();
        for (int i = 0; i < length; i++) {
            char c = s.charAt(i);
            // XXX - we are committing CombiningChar and Extender here
            if (!Character.isLetterOrDigit(c)
                && c != '.' && c != '-' && c != '_' && c != ':') {
                return false;
            }
        }
        return true;
    }

    /**
     * Do the Strings all match the XML-NMTOKEN production?
     *
     * <p>Otherwise they are not suitable as an enumerated attribute,
     * for example.</p>
     * @param s the array of string to test
     * @return true if all the strings in the array math XML-NMTOKEN
     */
    protected boolean areNmtokens(String[] s) {
        for (int i = 0; i < s.length; i++) {
            if (!isNmtoken(s[i])) {
                return false;
            }
        }
        return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16725.java