error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11167.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11167.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11167.java
text:
```scala
c@@onnection.setRequestProperty("Content-Type","multipart/form-data;boundary=---------------------------261773107125236");

/**
 *
 */
package org.jboss.as.demos.domain.http.deploy.runner;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

import org.jboss.as.demos.war.archive.SimpleServlet;
import org.jboss.dmr.ModelNode;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.container.ResourceContainer;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;

/**
 * Deploys a war to the domain via the HTTP API.
 *
 * @author Brian Stansberry (c) 2011 Red Hat Inc.
 */
public class ExampleRunner {

    /**
     * @param args
     */
    public static void main(String[] args) {

        BufferedOutputStream os = null;
        BufferedInputStream is = null;

        try {

            // I. We need to upload content to the domain controller.

            /* The following mimics what would happen as part of submitting an HTML form like the
             * following where the selected file is the "war-example.war" created above:
            <form method="post" action="http://localhost:9990/domain-api/add-content" enctype="multipart/form-data">
              file: <input type="file" name="file">
              <input type="submit">
            </form>
            */

            // Create the test WAR file and get a stream to its contents to be included in the POST.
            WebArchive archive = ShrinkWrap.create(WebArchive.class, "war-example.war");
            archive.addPackage(SimpleServlet.class.getPackage());
            addResources("archives/war-example.war", archive);
            is = new BufferedInputStream(archive.as(ZipExporter.class).exportZip());

            // Write the POST request and read the response from the HTTP server.
            URL uploadContent = new URL("http://localhost:9990/domain-api/add-content");
            HttpURLConnection connection =(HttpURLConnection) uploadContent.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","multipart/form-data;boundary=-----------------------------261773107125236");
            os = new BufferedOutputStream(connection.getOutputStream());

            StringBuilder builder = new StringBuilder();
            builder.append("-----------------------------261773107125236");
            builder.append("\r\n");
            builder.append("Content-Disposition: form-data; name=\"file\"; filename=\"war-example.war\"");
            builder.append("\r\n");
            builder.append("Content-Type: application/octet-stream");
            builder.append("\r\n");
            builder.append("\r\n");
            os.write(builder.toString().getBytes());

            final byte[] buffer = new byte[1024];
            int numRead = 0;

            while(numRead > -1) {
                numRead = is.read(buffer);
                if(numRead > 0) {
                    os.write(buffer,0,numRead);
                }
            }

            is.close();

            builder = new StringBuilder();
            builder.append("\r\n");
            builder.append("-----------------------------261773107125236");
            builder.append("--");
            builder.append("\r\n");

            os.write(builder.toString().getBytes());
            os.flush();

            // Read the response and get the hash of the new content from it
            ModelNode node = ModelNode.fromJSONStream(connection.getInputStream());
            System.out.println("Response to content upload request:");
            System.out.println(node);

            if (!"success".equals(node.require("outcome").asString())) {
                throw new IllegalStateException("Deployment request did not succeed");
            }

            byte[] hash = node.require("result").asBytes();

            // II. Associate the new content with its name

            connection =(HttpURLConnection) new URL("http://localhost:9990/domain-api/").openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            os = new BufferedOutputStream(connection.getOutputStream());

            ModelNode op = new ModelNode();
            op.get("operation").set("add");
            op.get("address").add("deployment", "war-example.war");
            op.get("hash").set(hash);

            String json = op.toJSONString(true);
            System.out.println(json);
            os.write(json.getBytes());
            os.flush();

            node = ModelNode.fromJSONStream(connection.getInputStream());
            System.out.println("Response to deployment add request:");
            System.out.println(node);
            if (!"success".equals(node.require("outcome").asString())) {
                throw new IllegalStateException("Deployment request did not succeed");
            }

            // III. Map the new deployment to a server group and deploy it

            connection =(HttpURLConnection) new URL("http://localhost:9990/domain-api/").openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            os = new BufferedOutputStream(connection.getOutputStream());

            op = new ModelNode();
            op.get("operation").set("add");
            ModelNode address = op.get("address");
            address.add("server-group", "main-server-group");
            address.add("deployment", "war-example.war");
            op.get("hash").set(hash);
            op.get("enabled").set(true);

            json = op.toJSONString(true);
            System.out.println(json);
            os.write(json.getBytes());
            os.flush();

            node = ModelNode.fromJSONStream(connection.getInputStream());
            System.out.println("Response to deployment add request:");
            System.out.println(node);
            if (!"success".equals(node.require("outcome").asString())) {
                throw new IllegalStateException("Deployment request did not succeed");
            }

            // IV. Access the deployment
            URL url = new URL("http://localhost:8080/war-example/simple?input=Hello");
            System.out.println("Reading response from " + url + ":");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            is = new BufferedInputStream(connection.getInputStream());
            int i = is.read();
            while (i != -1) {
               System.out.print((char) i);
               i = is.read();
            }
            System.out.println("");

            is.close();

            // V. Redeploy the content

            connection =(HttpURLConnection) new URL("http://localhost:9990/domain-api/").openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            os = new BufferedOutputStream(connection.getOutputStream());

            op = new ModelNode();
            op.get("operation").set("redeploy");
            address = op.get("address");
            address.add("server-group", "main-server-group");
            address.add("deployment", "war-example.war");

            json = op.toJSONString(true);
            System.out.println(json);
            os.write(json.getBytes());
            os.flush();

            node = ModelNode.fromJSONStream(connection.getInputStream());
            System.out.println("Response to deployment redeploy request:");
            System.out.println(node);
            if (!"success".equals(node.require("outcome").asString())) {
                throw new IllegalStateException("Deployment request did not succeed");
            }

            // VI. Undeploy and remove the deployment from the server group

            connection =(HttpURLConnection) new URL("http://localhost:9990/domain-api/").openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            os = new BufferedOutputStream(connection.getOutputStream());

            op = new ModelNode();
            op.get("operation").set("remove");
            address = op.get("address");
            address.add("server-group", "main-server-group");
            address.add("deployment", "war-example.war");

            json = op.toJSONString(true);
            System.out.println(json);
            os.write(json.getBytes());
            os.flush();

            node = ModelNode.fromJSONStream(connection.getInputStream());
            System.out.println("Response to deployment remove request:");
            System.out.println(node);
            if (!"success".equals(node.require("outcome").asString())) {
                throw new IllegalStateException("Deployment request did not succeed");
            }

            // VII. Remove the deployment mapping from the domain

            connection =(HttpURLConnection) new URL("http://localhost:9990/domain-api/").openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            os = new BufferedOutputStream(connection.getOutputStream());

            op = new ModelNode();
            op.get("operation").set("remove");
            op.get("address").add("deployment", "war-example.war");

            json = op.toJSONString(true);
            System.out.println(json);
            os.write(json.getBytes());
            os.flush();

            node = ModelNode.fromJSONStream(connection.getInputStream());
            System.out.println("Response to deployment remove request:");
            System.out.println(node);
            if (!"success".equals(node.require("outcome").asString())) {
                throw new IllegalStateException("Deployment request did not succeed");
            }

        } catch (final Exception e) {
            e.printStackTrace(System.out);
        }
        finally {
            closeQuietly(is);
            closeQuietly(os);
        }

    }

    private static void closeQuietly(final Closeable closeable) {
        if(closeable != null) {
            try {
                closeable.close();
            } catch (final IOException e) {}
        }
    }



    private static void addResources(String archiveName, ResourceContainer<?> archive) {
        File resourcesDir = getResources(archiveName);
        if (resourcesDir != null) {
            addFiles(archive, resourcesDir, ArchivePaths.create("/"));
        }

    }

    public static File getResources(String archiveName) {
        String name = archiveName;

        URL url = Thread.currentThread().getContextClassLoader().getResource(name);
        if (url == null) {
            System.err.println("No resources for " + archiveName);
            return null;
        }
        try {
            File file = new File(url.toURI());
            if (!file.exists()) {
                throw new IllegalArgumentException("Could not find " + file.getAbsolutePath());
            }
            if (!file.isDirectory()) {
                throw new IllegalArgumentException(file.getAbsolutePath() + " is not a directory");
            }
            return file;

        } catch (URISyntaxException e) {
            throw new RuntimeException("Could not get file for " + url);
        }
    }

    public static void addFiles(ResourceContainer<?> archive, File dir, ArchivePath dest) {
        for (String name : dir.list()) {
            File file = new File(dir, name);
            if (file.isDirectory()) {
                addFiles(archive, file, ArchivePaths.create(dest, name));
            } else {
                archive.addResource(file, ArchivePaths.create(dest, name));
            }
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11167.java