error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3759.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3759.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3759.java
text:
```scala
L@@OG.info("Option Debug: " + MainInterface.DEBUG);

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

package org.columba.core.main;

import java.util.logging.Logger;

import org.columba.mail.gui.composer.ComposerController;
import org.columba.mail.gui.composer.ComposerModel;
import org.columba.mail.parser.MailUrlParser;

/**
 * This class handles given arguments (in style of commandline arguments. If for example the
 * argument --composer is given, on startup the composer window is viewed. All other arguments like
 * subject ect. also given to the composer, so any values to write a mail can given here as
 * arguments and then a composer window with all values are opened.
 *
 * @author waffel
 */
public class CmdLineArgumentHandler {

    private static final Logger LOG = Logger.getLogger("org.columba.core.main");

    /**
     * Constructs a new CommandLineArgumentHandler. This Handler parsed the given commandline
     * Options an if needed starts a composer window. If any commandlineargument unknown a message
     * is printed out to the error console and the system will exit.
     * @param args Commandline Arguments to be parsed.
     */
    public CmdLineArgumentHandler(ColumbaCmdLineParser cmdLineParser) {
        String mailURL = cmdLineParser.getMailurlOption();

        if (mailURL != null) {
            if (MailUrlParser.isMailUrl(mailURL)) {
                MailUrlParser mailToParser = new MailUrlParser(mailURL);
                cmdLineParser.setComposerOption(true);
                cmdLineParser.setRcptOption((String) mailToParser.get("mailto:"));
                cmdLineParser.setSubjectOption((String) mailToParser.get(
                        "subject="));
                cmdLineParser.setCcOption((String) mailToParser.get("cc="));
                cmdLineParser.setBccOption((String) mailToParser.get("bcc="));
                cmdLineParser.setBodyOption((String) mailToParser.get("body="));
            }
        }

        LOG.info("Option Debug: " + cmdLineParser.getComposerOption());
        LOG.info("Option subject: " + cmdLineParser.getSubjectOption());
        LOG.info("Option composer: " + cmdLineParser.getComposerOption());
        LOG.info("Option mailurl: " + cmdLineParser.getMailurlOption());

        if (cmdLineParser.getComposerOption()) {
            ComposerModel model = new ComposerModel();

            if (cmdLineParser.getRcptOption() != null) {
                model.setTo(cmdLineParser.getRcptOption());
            }

            if (cmdLineParser.getSubjectOption() != null) {
                model.setSubject(cmdLineParser.getSubjectOption());
            }

            if (cmdLineParser.getCcOption() != null) {
                model.setHeaderField("Cc", cmdLineParser.getCcOption());
            }

            if (cmdLineParser.getBccOption() != null) {
                model.setHeaderField("Bcc", cmdLineParser.getBccOption());
            }

            if (cmdLineParser.getBodyOption() != null) {
                String body = cmdLineParser.getBodyOption();

                /*
                 * *20030917, karlpeder* Set the model to html or text
                 * based on the body specified on the command line. This
                 * is done using a simple check: Does the body contains
                 * <html> and </html>
                 */
                boolean html = false;
                String lcase = body.toLowerCase();

                if ((lcase.indexOf("<html>") != -1) && (lcase.indexOf("</html>") != -1)) {
                    html = true;
                }

                model.setHtml(html);

                // set the body text
                model.setBodyText(body);
            }

            ComposerController c = (ComposerController)
                    MainInterface.frameModel.openView("Composer");
            c.setComposerModel(model);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3759.java