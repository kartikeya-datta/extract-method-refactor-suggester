error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12832.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12832.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12832.java
text:
```scala
h@@ttpContext.getFilters().add(new RedirectReadinessFilter(securityRealm, ErrorHandler.getRealmRedirect()));

/*
* JBoss, Home of Professional Open Source.
* Copyright 2011, Red Hat Middleware LLC, and individual contributors
* as indicated by the @author tags. See the copyright.txt file in the
* distribution for a full listing of individual contributors.
*
* This is free software; you can redistribute it and/or modify it
* under the terms of the GNU Lesser General Public License as
* published by the Free Software Foundation; either version 2.1 of
* the License, or (at your option) any later version.
*
* This software is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this software; if not, write to the Free
* Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
* 02110-1301 USA, or see the FSF site: http://www.fsf.org.
*/
package org.jboss.as.domain.http.server;

import org.jboss.as.domain.management.AuthenticationMechanism;
import org.jboss.as.domain.management.SecurityRealm;
import org.jboss.com.sun.net.httpserver.HttpContext;
import org.jboss.com.sun.net.httpserver.HttpServer;
import org.jboss.modules.ModuleLoadException;

/**
 * Different modes for showing the admin console
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
public enum ConsoleMode {

    /**
     * Show the console normally
     */
    CONSOLE {
        @Override
        ResourceHandler createConsoleHandler(String slot) throws ModuleLoadException {
            return new ConsoleHandler(slot);
        }
        @Override
        public boolean hasConsole() {
            return true;
        }
    },
    /**
     * If an attempt is made to go to the console show an error saying the host is a slave
     */
    SLAVE_HC {
        @Override
        ResourceHandler createConsoleHandler(String slot) throws ModuleLoadException {
            return DisabledConsoleHandler.createNoConsoleForSlave(slot);
        }
        @Override
        public boolean hasConsole() {
            return false;
        }
    },
    /**
     * If an attempt is made to go to the console show an error saying the server/host is in admin-only mode
     */
    ADMIN_ONLY{
        @Override
        ResourceHandler createConsoleHandler(String slot) throws ModuleLoadException {
            return DisabledConsoleHandler.createNoConsoleForAdminMode(slot);
        }
        @Override
        public boolean hasConsole() {
            return false;
        }
    },
    /**
     * If an attempt is made to go to the console a 404 is shown
     */
    NO_CONSOLE{
        @Override
        ResourceHandler createConsoleHandler(String slot) throws ModuleLoadException {
            return null;
        }
        @Override
        public boolean hasConsole() {
            return false;
        }
    };

    /**
     * Returns a console handler for the mode
     *
     * @return the console handler, may be {@code null}
     */
    ResourceHandler createConsoleHandler(String slot) throws ModuleLoadException {
        throw new IllegalStateException("Not overridden for " + this);
    }

    /**
     * Returns whether the console is displayed or not
     */
    public boolean hasConsole() {
        throw new IllegalStateException("Not overridden for " + this);
    }

    /**
     * An extension of the ResourceHandler to configure the handler to server up resources from the console module only.
     *
     * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
     */
    private static class ConsoleHandler extends ResourceHandler {

        private static final String NOCACHE_JS = ".nocache.js";
        private static final String INDEX_HTML = "index.html";
        private static final String APP_HTML = "App.html";

        private static final String CONSOLE_MODULE = "org.jboss.as.console";
        private static final String CONTEXT = "/console";
        private static final String DEFAULT_RESOURCE = "/" + INDEX_HTML;

        ConsoleHandler(String slot) throws ModuleLoadException {
            super(CONTEXT, DEFAULT_RESOURCE, getClassLoader(CONSOLE_MODULE, slot));
        }

        @Override
        protected boolean skipCache(String resource) {
            return resource.endsWith(NOCACHE_JS) || resource.endsWith(APP_HTML) || resource.endsWith(INDEX_HTML);
        }

        /*
        * This method is override so we can ensure the RealmReadinessFilter is in place.
        *
        * (Later may change the return type to return the context so a sub-class can just continue after the parent class start)
        */
        @Override
        public void start(HttpServer httpServer, SecurityRealm securityRealm) {
            HttpContext httpContext = httpServer.createContext(getContext(), this);
            if (securityRealm != null
                    && securityRealm.getSupportedAuthenticationMechanisms().contains(AuthenticationMechanism.CLIENT_CERT) == false) {
                httpContext.getFilters().add(new RealmReadinessFilter(securityRealm, ErrorHandler.getRealmRedirect()));
            }
        }
    }

    /**
     * An extension of the ResourceHandler to configure the handler to show an error page when the console has been turned off.
     */
    static class DisabledConsoleHandler extends ResourceHandler {

        private static final String ERROR_MODULE = "org.jboss.as.domain-http-error-context";
        private static final String CONTEXT = "/consoleerror";
        private static final String NO_CONSOLE_FOR_SLAVE = "/noConsoleForSlaveDcError.html";
        private static final String NO_CONSOLE_FOR_ADMIN_MODE = "/noConsoleForAdminModeError.html";

        private DisabledConsoleHandler(String slot, String resource) throws ModuleLoadException {
            super(CONTEXT, resource, getClassLoader(ERROR_MODULE, slot));
        }

        static DisabledConsoleHandler createNoConsoleForSlave(String slot) throws ModuleLoadException {
            return new DisabledConsoleHandler(slot, NO_CONSOLE_FOR_SLAVE);
        }

        static DisabledConsoleHandler createNoConsoleForAdminMode(String slot) throws ModuleLoadException {
            return new DisabledConsoleHandler(slot, NO_CONSOLE_FOR_ADMIN_MODE);
        }

        @Override
        protected boolean skipCache(String resource) {
            /*
             * This context is not expected to be used a lot, however if the pages can
             * be cached this can cause problems with new installations that may
             * have different content.
             */
            return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12832.java