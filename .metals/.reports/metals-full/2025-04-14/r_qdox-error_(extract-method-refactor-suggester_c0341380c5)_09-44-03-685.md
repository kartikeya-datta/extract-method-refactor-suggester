error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9099.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9099.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9099.java
text:
```scala
u@@til.log("Error occurred: " + e.getMessage(), VAJUtil.MSG_ERR);

/*
 * Copyright  2001-2004 The Apache Software Foundation
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

package org.apache.tools.ant.taskdefs.optional.ide;

import com.ibm.ivj.toolserver.servletclasses.servlet.ServletException;
import com.ibm.ivj.toolserver.servletclasses.servlet.http.HttpServlet;
import com.ibm.ivj.toolserver.servletclasses.servlet.http.HttpServletRequest;
import com.ibm.ivj.toolserver.servletclasses.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.StringUtils;

/**
 * Abstract base class to provide common services for the
 * VAJ tool API servlets
 *
 * @author Wolf Siberski, based on servlets written by Glenn McAllister
 */
public abstract class VAJToolsServlet extends HttpServlet {
    /**
     * Adaptation of VAJUtil for servlet context.
     */
    class VAJLocalServletUtil extends VAJLocalUtil {
        public void log(String msg, int level) {
            try {
                if (msg != null) {
                    msg = msg.replace('\r', ' ');
                    int i = 0;
                    while (i < msg.length()) {
                        int nlPos = msg.indexOf('\n', i);
                        if (nlPos == -1) {
                            nlPos = msg.length();
                        }
                        response.getWriter().println(Integer.toString(level)
                                                     + " " + msg.substring(i, nlPos));
                        i = nlPos + 1;
                    }
                }
            } catch (IOException e) {
                throw new BuildException("logging failed. msg was: "
                                         + e.getMessage());
            }
        }
    }

    // constants for servlet param names
    public static final String DIR_PARAM = "dir";
    public static final String INCLUDE_PARAM = "include";
    public static final String EXCLUDE_PARAM = "exclude";
    public static final String CLASSES_PARAM = "cls";
    public static final String SOURCES_PARAM = "src";
    public static final String RESOURCES_PARAM = "res";
    public static final String DEFAULT_EXCLUDES_PARAM = "dex";
    public static final String PROJECT_NAME_PARAM = "project";


    // current request
    HttpServletRequest  request;

    // response to current request
    HttpServletResponse response;

    // implementation of VAJUtil used by the servlet
    VAJUtil util;


    /**
     * Execute the request by calling the appropriate
     * VAJ tool API methods. This method must be implemented
     * by the concrete servlets
     */
    protected abstract void executeRequest();

    /**
     * Respond to a HTTP request. This method initializes
     * the servlet and handles errors.
     * The real work is done in the abstract method executeRequest()
     */
    public void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
        try {
            response = res;
            request = req;
            initRequest();
            executeRequest();
        } catch (BuildException e) {
            util.log("Error occured: " + e.getMessage(), VAJUtil.MSG_ERR);
        } catch (Exception e) {
            try {
                if (!(e instanceof BuildException)) {
                    String trace = StringUtils.getStackTrace(e);
                    util.log("Program error in " + this.getClass().getName()
                             + ":\n" + trace, VAJUtil.MSG_ERR);
                }
            } catch (Throwable t) {
                t.printStackTrace();
            } finally {
                if (!(e instanceof BuildException)) {
                    throw new ServletException(e.getMessage());
                }
            }
        }
    }

    /**
     * initialize the servlet.
     */
    protected void initRequest() throws IOException {
        response.setContentType("text/ascii");
        if (util == null) {
            util = new VAJLocalServletUtil();
        }
    }

    /**
     * Get the VAJUtil implementation
     */
    VAJUtil getUtil() {
        return util;
    }

    /**
     * Get the boolean value of a parameter.
     */
    protected boolean getBooleanParam(String param) {
        return getBooleanParam(param, false);
    }

    /**
     * Get the boolean value of a parameter, with a default value if
     * the parameter hasn't been passed to the servlet.
     */
    protected boolean getBooleanParam(String param, boolean defaultValue) {
        String value = getFirstParamValueString(param);
        if (value != null) {
            return toBoolean(value);
        } else {
            return defaultValue;
        }
    }

    /**
     * Returns the first encountered value for a parameter.
     */
    protected String getFirstParamValueString(String param) {
        String[] paramValuesArray = request.getParameterValues(param);
        if (paramValuesArray == null) {
            return null;
        }
        return paramValuesArray[0];
    }

    /**
     * Returns all values for a parameter.
     */
    protected String[] getParamValues(String param) {
        return request.getParameterValues(param);
    }

    /**
     * A utility method to translate the strings "yes", "true", and "ok"
     * to boolean true, and everything else to false.
     */
    protected boolean toBoolean(String string) {
        String lower = string.toLowerCase();
        return (lower.equals("yes") || lower.equals("true") || lower.equals("ok"));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9099.java