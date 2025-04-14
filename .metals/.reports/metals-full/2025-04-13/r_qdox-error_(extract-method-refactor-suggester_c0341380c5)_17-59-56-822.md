error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/391.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/391.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/391.java
text:
```scala
U@@RI resolveRelativeURI(String baseURI, String systemId) throws URISyntaxException {

package org.apache.solr.util;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.lucene.analysis.util.ResourceLoader;

import org.xml.sax.InputSource;
import org.xml.sax.EntityResolver;
import org.xml.sax.ext.EntityResolver2;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXSource;
import javax.xml.stream.XMLResolver;
import javax.xml.stream.XMLStreamException;

/**
 * This is a helper class to support resolving of XIncludes or other hrefs
 * inside XML files on top of a {@link ResourceLoader}. Just plug this class
 * on top of a {@link ResourceLoader} and pass it as {@link EntityResolver} to SAX parsers
 * or via wrapper methods as {@link URIResolver} to XSL transformers or {@link XMLResolver} to STAX parsers.
 * The resolver handles special SystemIds with an URI scheme of {@code solrres:} that point
 * to resources. To produce such systemIds when you initially call the parser, use
 * {@link #createSystemIdFromResourceName} which produces a SystemId that can
 * be included along the InputStream coming from {@link ResourceLoader#openResource}.
 * <p>In general create the {@link InputSource} to be passed to the parser like:</p>
 * <pre class="prettyprint">
 *  InputSource is = new InputSource(loader.openSchema(name));
 *  is.setSystemId(SystemIdResolver.createSystemIdFromResourceName(name));
 *  final DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
 *  db.setEntityResolver(new SystemIdResolver(loader));
 *  Document doc = db.parse(is);
 * </pre>
 */
public final class SystemIdResolver implements EntityResolver, EntityResolver2 {
  private static final Logger log = LoggerFactory.getLogger(SystemIdResolver.class);

  public static final String RESOURCE_LOADER_URI_SCHEME = "solrres";
  public static final String RESOURCE_LOADER_AUTHORITY_ABSOLUTE = "@";

  private final ResourceLoader loader;

  public SystemIdResolver(ResourceLoader loader) {
    this.loader = loader;
  }
  
  public EntityResolver asEntityResolver() {
    return this;
  }
  
  public URIResolver asURIResolver() {
    return new URIResolver() {
      public Source resolve(String href, String base) throws TransformerException {
        try {
          final InputSource src = SystemIdResolver.this.resolveEntity(null, null, base, href);
          return (src == null) ? null : new SAXSource(src);
        } catch (IOException ioe) {
          throw new TransformerException("Cannot resolve entity", ioe);
        }
      }
    };
  }
  
  public XMLResolver asXMLResolver() {
    return new XMLResolver() {
      public Object resolveEntity(String publicId, String systemId, String baseURI, String namespace) throws XMLStreamException {
        try {
          final InputSource src = SystemIdResolver.this.resolveEntity(null, publicId, baseURI, systemId);
          return (src == null) ? null : src.getByteStream();
        } catch (IOException ioe) {
          throw new XMLStreamException("Cannot resolve entity", ioe);
        }
      }
    };
  }
  
  URI resolveRelativeURI(String baseURI, String systemId) throws IOException,URISyntaxException {
    URI uri;
    
    // special case for backwards compatibility: if relative systemId starts with "/" (we convert that to an absolute solrres:-URI)
    if (systemId.startsWith("/")) {
      uri = new URI(RESOURCE_LOADER_URI_SCHEME, RESOURCE_LOADER_AUTHORITY_ABSOLUTE, "/", null, null).resolve(systemId);
    } else {
      // simply parse as URI
      uri = new URI(systemId);
    }
    
    // do relative resolving
    if (baseURI != null ) {
      uri = new URI(baseURI).resolve(uri);
    }
    
    return uri;
  }
  
  // *** EntityResolver(2) methods:
  
  public InputSource getExternalSubset(String name, String baseURI) {
    return null;
  }
  
  public InputSource resolveEntity(String name, String publicId, String baseURI, String systemId) throws IOException {
    if (systemId == null)
      return null;
    try {
      final URI uri = resolveRelativeURI(baseURI, systemId);
      
      // check schema and resolve with ResourceLoader
      if (RESOURCE_LOADER_URI_SCHEME.equals(uri.getScheme())) {
        String path = uri.getPath(), authority = uri.getAuthority();
        if (!RESOURCE_LOADER_AUTHORITY_ABSOLUTE.equals(authority)) {
          path = path.substring(1);
        }
        try {
          final InputSource is = new InputSource(loader.openResource(path));
          is.setSystemId(uri.toASCIIString());
          is.setPublicId(publicId);
          return is;
        } catch (RuntimeException re) {
          // unfortunately XInclude fallback only works with IOException, but openResource() never throws that one
          throw (IOException) (new IOException(re.getMessage()).initCause(re));
        }
      } else {
        // resolve all other URIs using the standard resolver
        return null;
      }
    } catch (URISyntaxException use) {
      log.warn("An URI systax problem occurred during resolving SystemId, falling back to default resolver", use);
      return null;
    }
  }

  public InputSource resolveEntity(String publicId, String systemId) throws IOException {
    return resolveEntity(null, publicId, null, systemId);
  }
  
  public static String createSystemIdFromResourceName(String name) {
    name = name.replace(File.separatorChar, '/');
    final String authority;
    if (name.startsWith("/")) {
      // a hack to preserve absolute filenames and keep them absolute after resolving, we set the URI's authority to "@" on absolute filenames:
      authority = RESOURCE_LOADER_AUTHORITY_ABSOLUTE;
    } else {
      authority = null;
      name = "/" + name;
    }
    try {
      return new URI(RESOURCE_LOADER_URI_SCHEME, authority, name, null, null).toASCIIString();
    } catch (URISyntaxException use) {
      throw new IllegalArgumentException("Invalid syntax of Solr Resource URI", use);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/391.java