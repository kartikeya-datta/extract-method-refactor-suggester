error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10889.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10889.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10889.java
text:
```scala
X@@MLOutputFactory factory = XMLOutputFactory.newInstance();

/*
* JBoss, Home of Professional Open Source.
* Copyright 2012, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.as.config.assembly;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StringWriter;
import java.net.URL;

import javax.xml.stream.XMLOutputFactory;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSParser;
import org.w3c.dom.ls.LSSerializer;

/**
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
public class SubsystemParserTestCase {
    @Test
    public void testEmptySubsystem() throws Exception {
        testSubsystem("empty.xml", "org.jboss.as.empty");
    }

    @Test
    public void testEmptyWithAttributesSubsystem() throws Exception {
        testSubsystem("empty-with-attributes.xml", "org.jboss.as.empty");
    }

    @Test
    public void testSimpleSubsystem() throws Exception {
        testSubsystem("simple.xml", "org.jboss.as.simple");
    }

    @Test
    public void testSimpleComment() throws Exception {
        testSubsystem("simple-comment.xml", "org.jboss.as.simple");
    }

    @Ignore("CDATA not reported propery. Look into if we start using that in our config files")
    @Test
    public void testSimpleCData() throws Exception {
        testSubsystem("simple-cdata.xml", "org.jboss.as.simple");
    }

    @Test
    public void testLoggingSubsystem() throws Exception {
        testSubsystem("logging.xml", "org.jboss.as.logging");
    }

    @Test
    public void testTextAndCommentsSubsystem() throws Exception {
        testSubsystem("simple-with-text-and-comments.xml", "org.jboss.as.simple");
    }

    @Test
    public void testEjb3Subsystem() throws Exception {
        testSubsystem("ejb3.xml", "org.jboss.as.ejb3");
    }

    @Test
    public void testSupplementDefault() throws Exception {
        SubsystemParser parser = testSubsystem("simple-with-supplements.xml", "org.jboss.as.simple", null, false);
        String marshalled = marshall(parser);
        String expected =
                "<?xml version=\"1.0\" ?>" +
                "<subsystem xmlns=\"urn:jboss:domain:simple-with-text-and-comments:1.0\">" +
                "   <some-element value=\"true\"/>" +
                "</subsystem>";
        Assert.assertEquals(normalizeXML(expected), normalizeXML(marshalled));
    }

    @Test
    public void testSupplementFull() throws Exception {
        SubsystemParser parser = testSubsystem("simple-with-supplements.xml", "org.jboss.as.simple", "full", false);
        String marshalled = marshall(parser);
        String expected =
                "<?xml version=\"1.0\" ?>" +
                "<subsystem xmlns=\"urn:jboss:domain:simple-with-text-and-comments:1.0\">" +
                "   <some-element value=\"false\"/>" +
                "  <childA childA-attr=\"child-one\">Hello</childA>" +
                "  <childB ohildB-attr=\"child two\">" +
                "    <childB1/>" +
                "  </childB>" +
                "</subsystem>";

        Assert.assertEquals(normalizeXML(expected), normalizeXML(marshalled));
    }

    @Test
    public void testSupplementHa() throws Exception {
        SubsystemParser parser = testSubsystem("simple-with-supplements.xml", "org.jboss.as.simple", "ha", false);
        String marshalled = marshall(parser);
        String expected =
                "<?xml version=\"1.0\" ?>" +
                "<subsystem xmlns=\"urn:jboss:domain:simple-with-text-and-comments:1.0\">" +
                "   <some-element value=\"true\"/>" +
                "   <childC ohildC-attr=\"child two\">" +
                "      <childC1>Yo</childC1>" +
                "   </childC>" +
                "</subsystem>";

        Assert.assertEquals(normalizeXML(expected), normalizeXML(marshalled));
    }

    @Test
    public void testSupplementFullHa() throws Exception {
        SubsystemParser parser = testSubsystem("simple-with-supplements.xml", "org.jboss.as.simple", "full-ha", false);
        String marshalled = marshall(parser);
        String expected =
                "<?xml version=\"1.0\" ?>" +
                "<subsystem xmlns=\"urn:jboss:domain:simple-with-text-and-comments:1.0\">" +
                "   <some-element value=\"true\"/>" +
                "   <childA childA-attr=\"child-one\">Hello</childA>" +
                "   <childB ohildB-attr=\"child two\">" +
                "      <childB1/>" +
                "   </childB>" +
                "   <childC ohildC-attr=\"child two\">" +
                "      <childC1>Overridden by full-ha</childC1>" +
                "   </childC>" +
                "</subsystem>";

        Assert.assertEquals(normalizeXML(expected), normalizeXML(marshalled));
    }

    @Test
    public void testSimpleWithSeveralAttributeReplacements() throws Exception {
        SubsystemParser parser = testSubsystem("simple-with-several-attribute-replacements.xml", "org.jboss.as.simple", null, false);
        String marshalled = marshall(parser);
        String expected =
            "<?xml version='1.0' encoding='UTF-8'?>" +
            "<subsystem xmlns=\"urn:jboss:domain:simple-with-text-and-comments:1.0\">" +
            "   <some-element value=\"12345\"/>" +
            "   <another-element value=\"12345\"/>" +
            "   <yet-another-element value=\"12345\"/>" +
            "</subsystem>";
        Assert.assertEquals(normalizeXML(expected), normalizeXML(marshalled));
    }

    private SubsystemParser testSubsystem(String xml, String extensionModule) throws Exception {
        return testSubsystem(xml, extensionModule, null, true);
    }

    private SubsystemParser testSubsystem(String xml, String extensionModule, String supplement, boolean compareWithOriginal) throws Exception {
        URL url = this.getClass().getResource(xml);
        Assert.assertNotNull(url);
        SubsystemParser subsystemParser = new SubsystemParser(null, supplement, new File(url.toURI()));
        subsystemParser.parse();

        Assert.assertNotNull(subsystemParser.getExtensionModule());
        Assert.assertEquals(extensionModule, subsystemParser.getExtensionModule());

        if (compareWithOriginal) {
            String marshalled = marshall(subsystemParser);
            Assert.assertEquals(normalizeXML(trimOriginalXml(url)), normalizeXML(marshalled));
        }
        return subsystemParser;
    }

    private String marshall(SubsystemParser subsystemParser) throws Exception {
        StringWriter stringWriter = new StringWriter();
        XMLOutputFactory factory = XMLOutputFactory.newFactory();
        FormattingXMLStreamWriter writer = new FormattingXMLStreamWriter(factory.createXMLStreamWriter(stringWriter));
        try {
            Assert.assertNotNull(subsystemParser.getSubsystem());
            writer.writeStartDocument();
            subsystemParser.getSubsystem().marshall(writer);
            writer.writeEndDocument();
        } finally {
            writer.close();
        }

        System.out.println(stringWriter.getBuffer().toString());
        return stringWriter.getBuffer().toString();
    }

    private String trimOriginalXml(URL url) throws Exception {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(new File(url.toURI())));
        try {
            String s = reader.readLine();
            while (s != null) {
                if (!s.contains("config>") && ! s.contains("<extension-module")) {
                    sb.append(s);
                    sb.append("\n");
                }
                if (s.contains("</subsystem>")) {
                    break;
                }
                s = reader.readLine();
            }
            return sb.toString();
        } finally {
            reader.close();
        }
    }

    /**
     * Normalize and pretty-print XML so that it can be compared using string
     * compare. The following code does the following: - Removes comments -
     * Makes sure attributes are ordered consistently - Trims every element -
     * Pretty print the document
     *
     * @param xml
     *            The XML to be normalized
     * @return The equivalent XML, but now normalized
     */
    protected String normalizeXML(String xml) throws Exception {
        // Remove all white space adjoining tags ("trim all elements")
        xml = xml.replaceAll("\\s*<", "<");
        xml = xml.replaceAll(">\\s*", ">");

        DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
        DOMImplementationLS domLS = (DOMImplementationLS) registry.getDOMImplementation("LS");
        LSParser lsParser = domLS.createLSParser(DOMImplementationLS.MODE_SYNCHRONOUS, null);

        LSInput input = domLS.createLSInput();
        input.setStringData(xml);
        Document document = lsParser.parse(input);

        LSSerializer lsSerializer = domLS.createLSSerializer();
        lsSerializer.getDomConfig().setParameter("comments", Boolean.TRUE);
        lsSerializer.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE);
        return lsSerializer.writeToString(document);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10889.java