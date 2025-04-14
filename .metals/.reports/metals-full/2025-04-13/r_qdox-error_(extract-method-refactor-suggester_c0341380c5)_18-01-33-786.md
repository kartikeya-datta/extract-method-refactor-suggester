error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3809.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3809.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3809.java
text:
```scala
S@@treamUtils.readCharacterStream(res.getErrorStream()).toString());

//The contents of this file are subject to the Mozilla Public License Version 1.1
//(the "License"); you may not use this file except in compliance with the 
//License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
//
//Software distributed under the License is distributed on an "AS IS" basis,
//WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License 
//for the specific language governing rights and
//limitations under the License.
//
//The Original Code is "The Columba Project"
//
//The Initial Developers of the Original Code are Frederik Dietz and Timo Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003. 
//
//All Rights Reserved.
package org.columba.mail.pgp;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.columba.core.io.StreamUtils;
import org.columba.mail.config.SecurityItem;
import org.columba.mail.message.PGPMimePart;
import org.columba.ristretto.composer.MimePartRenderer;
import org.columba.ristretto.composer.MimeTreeRenderer;
import org.columba.ristretto.io.CharSequenceSource;
import org.columba.ristretto.io.SequenceInputStream;
import org.columba.ristretto.io.Source;
import org.columba.ristretto.message.InputStreamMimePart;
import org.columba.ristretto.message.LocalMimePart;
import org.columba.ristretto.message.MimeHeader;
import org.columba.ristretto.message.MimePart;
import org.columba.ristretto.message.StreamableMimePart;
import org.waffel.jscf.JSCFConnection;
import org.waffel.jscf.JSCFResultSet;
import org.waffel.jscf.JSCFStatement;


public class MultipartEncryptedRenderer extends MimePartRenderer {
    private StreamableMimePart controlPart;
    private MimeHeader encryptedHeader;

    public MultipartEncryptedRenderer() {
        MimeHeader controlHeader = new MimeHeader("application", "pgp-encrypted");
        Source controlBody = new CharSequenceSource("Version: 1\r\n");
        controlPart = new LocalMimePart(controlHeader, controlBody);

        encryptedHeader = new MimeHeader("application", "octet-stream");
    }

    /* (non-Javadoc)
 * @see org.columba.ristretto.composer.MimePartRenderer#getRegisterString()
 */
    public String getRegisterString() {
        return "multipart/encrypted";
    }

    /* (non-Javadoc)
 * @see org.columba.ristretto.composer.MimePartRenderer#render(org.columba.ristretto.message.StreamableMimePart)
 */
    public InputStream render(MimePart part) throws Exception {
        Vector streams = new Vector((2 * 2) + 3);

        MimeHeader header = part.getHeader();

        // Create boundary to separate the mime-parts
        String boundary = createUniqueBoundary().toString();
        header.putContentParameter("boundary", boundary);

        byte[] startBoundary = ("\r\n--" + boundary + "\r\n").getBytes();
        byte[] endBoundary = ("\r\n--" + boundary + "--\r\n").getBytes();

        // Add pgp-specific content-parameters
        header.putContentParameter("protocol", "application/pgp-encrypted");

        // Create the header of the multipart
        streams.add(header.getHeader().getInputStream());

        SecurityItem pgpItem = ((PGPMimePart) part).getPgpItem();

        // Add the ControlMimePart 		
        streams.add(new ByteArrayInputStream(startBoundary));
        streams.add(MimeTreeRenderer.getInstance().renderMimePart(controlPart));

        // Add the encrypted MimePart
        streams.add(new ByteArrayInputStream(startBoundary));

        StreamableMimePart encryptedPart;
        encryptedPart = null;

        /*
JSCFDriverManager.registerJSCFDriver(new GPGDriver());
JSCFConnection con = JSCFDriverManager.getConnection("jscf:gpg:"+pgpItem.get("path"));
*/
        JSCFController controller = JSCFController.getInstance();
        JSCFConnection con = controller.getConnection();

        //con.getProperties().put("USERID", pgpItem.get("id"));
        JSCFStatement stmt = con.createStatement();
        JSCFResultSet res = stmt.executeEncrypt(MimeTreeRenderer.getInstance()
                                                                .renderMimePart(part.getChild(
                        0)), pgpItem.get("recipients"));

        if (res.isError()) {
            JOptionPane.showMessageDialog(null,
                StreamUtils.readInString(res.getErrorStream()).toString());
        }

        encryptedPart = new InputStreamMimePart(encryptedHeader,
                res.getResultStream());
        streams.add(MimeTreeRenderer.getInstance().renderMimePart(encryptedPart));

        // Create the closing boundary
        streams.add(new ByteArrayInputStream(endBoundary));

        return new SequenceInputStream(streams);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3809.java