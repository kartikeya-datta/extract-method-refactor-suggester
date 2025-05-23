error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4185.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4185.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4185.java
text:
```scala
r@@eturn ""; //$NON-NLS-1$

/*******************************************************************************
 * Copyright (c) 2008 IONA Technologies PLC
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IONA Technologies PLC - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.core.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.wsdl.Definition;
import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.soap.SOAPAddress;
import javax.wsdl.extensions.soap12.SOAP12Address;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.wsdl.xml.WSDLWriter;

import org.eclipse.core.filesystem.URIUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCorePlugin;
import org.xml.sax.InputSource;

/**
 * WSDL Utility class.
 * <p>
 * <strong>Provisional API:</strong> This class/interface is part of an interim API that is still under 
 * development and expected to change significantly before reaching stability. It is being made available at 
 * this early stage to solicit feedback from pioneering adopters on the understanding that any code that uses 
 * this API will almost certainly be broken (repeatedly) as the API evolves.
 * </p>
 */
public final class WSDLUtils {
    private static final String WSDL_FILE_NAME_PATTERN = "[a-zA-Z0-9_\\-]+.wsdl";//$NON-NLS-1$
    private static final String WSDL_QUERY = "?wsdl"; //$NON-NLS-1$

	private static final IPath WSDL_FOLDER_PATH = new Path("wsdl/"); //$NON-NLS-1$
    private static final int TIMEOUT = 30000;

	public static final String WSDL_FILE_EXTENSION = ".wsdl"; //$NON-NLS-1$
	
    private WSDLUtils() {
    }
        
    public static Definition readWSDL(URL wsdlURL) throws IOException {
    	URLConnection urlConnection = wsdlURL.openConnection();
    	urlConnection.setConnectTimeout(TIMEOUT);
    	urlConnection.setReadTimeout(TIMEOUT);
    	InputStream inputStream = null;
        try {
            inputStream = urlConnection.getInputStream();
            InputSource inputSource = new InputSource(inputStream);
            WSDLFactory wsdlFactory = WSDLFactory.newInstance();
            WSDLReader wsdlReader = wsdlFactory.newWSDLReader();
            Definition definition = wsdlReader.readWSDL(wsdlURL.getPath(), inputSource);
            return definition;
        } catch (WSDLException wsdle) {
            JAXWSCorePlugin.log(wsdle);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return null;
    }
    
    public static void writeWSDL(URL wsdlURL, Definition definition) throws IOException, CoreException {
    	URI wsdlURI = null;
        OutputStream wsdlOutputStream = null;
        try {
        	wsdlURI = wsdlURL.toURI();
            File wsdlFile = new File(wsdlURI);
            wsdlOutputStream = new FileOutputStream(wsdlFile);
            WSDLFactory wsdlFactory = WSDLFactory.newInstance();
            WSDLWriter wsdlWriter = wsdlFactory.newWSDLWriter();
            wsdlWriter.writeWSDL(definition, wsdlOutputStream);
        } catch (WSDLException wsdle) {
        	JAXWSCorePlugin.log(wsdle);
        } catch (URISyntaxException urise) {
        	JAXWSCorePlugin.log(urise);
        } finally {
            if (wsdlOutputStream != null) {
                wsdlOutputStream.close();
                IFile file = ResourcesPlugin.getWorkspace().getRoot()
						.getFileForLocation(URIUtil.toPath(wsdlURI));
                if (file != null && file.exists()) {
                	file.refreshLocal(IResource.DEPTH_ONE, new NullProgressMonitor());
                }
            }
        }
    }
   
    public static boolean isValidWSDLFileName(String wsdlFileName) {
        return wsdlFileName != null && wsdlFileName.matches(WSDL_FILE_NAME_PATTERN);     
    }
        
    public static IProject getProject(String projectName) {
        return ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
    }
        
    public static IFolder getWebContentFolder(String projectName) {
    	return WSDLUtils.getWebContentFolder(WSDLUtils.getProject(projectName));
    }
    
    public static IFolder getWebContentFolder(IProject project) {
		return ResourcesPlugin.getWorkspace().getRoot().getFolder(
				WSDLUtils.getWebContentPath(project));
	}

    public static IFolder getWSDLFolder(String projectName) {
        return WSDLUtils.getWSDLFolder(WSDLUtils.getProject(projectName));
    }

    public static IFolder getWSDLFolder(IProject project) {
        IPath wsdlFolderPath = WSDLUtils.getWebContentPath(project).append(WSDLUtils.WSDL_FOLDER_PATH);
        IFolder wsdlFolder = ResourcesPlugin.getWorkspace().getRoot().getFolder(wsdlFolderPath);
        if (!wsdlFolder.exists()) {
            try {
                wsdlFolder.create(true, true, new NullProgressMonitor());
            } catch (CoreException ce) {
                JAXWSCorePlugin.log(ce.getStatus());
            }
        }
        return wsdlFolder;
    }
    
    public static IPath getWebContentPath(IProject project) {
        return J2EEUtils.getWebContentPath(project).addTrailingSeparator();
    }

    public static String getWSDLFileNameFromURL(URL wsdlURL) {
        IPath wsdlPath = new Path(wsdlURL.toExternalForm());
        return wsdlPath.lastSegment();
    }

    /**
     * will return one of: 
     * <li>SOAPAddress<li>SOAP12Address<li>null if it can not find a soap address
     */
    @SuppressWarnings("unchecked")
    public static ExtensibilityElement getEndpointAddress(Definition definition) {
        if (definition != null) {
            Map servicesMap = definition.getServices();
            Set<Map.Entry> servicesSet = servicesMap.entrySet();
            for (Map.Entry serviceEntry : servicesSet) {
                Service service = (Service) serviceEntry.getValue();
                Map portsMap = service.getPorts();
                Set<Map.Entry> portsSet = portsMap.entrySet();
                for (Map.Entry portEntry : portsSet) {
                    Port port = (Port) portEntry.getValue();
                    List extensibilityElements = port.getExtensibilityElements();
                    for (Object object : extensibilityElements) {
                        if (object instanceof SOAPAddress || object instanceof SOAP12Address) {
                            return (ExtensibilityElement) object;      
                        }
                    }
                 }
            }
        }
        return null;
    }

    public static String getWSDLLocation(Definition definition) throws MalformedURLException {
		ExtensibilityElement extensibilityElement = WSDLUtils.getEndpointAddress(definition);
		if (extensibilityElement != null) {
	        String locationURI = getLocationURI(extensibilityElement);
	        if (locationURI.length() > 0) {
	            URL endpointURL = new URL(locationURI);
	            if (endpointURL.getQuery() == null) {
	                locationURI += WSDL_QUERY;
	            }
                return locationURI;
	        }
		}
	    return null;
    }
    
    private static String getLocationURI(ExtensibilityElement extensibilityElement) {
	    if (extensibilityElement instanceof SOAPAddress) {
            return ((SOAPAddress) extensibilityElement).getLocationURI();
        }
        if (extensibilityElement instanceof SOAP12Address) {
            return ((SOAP12Address) extensibilityElement).getLocationURI();
        }
		return "";
    }
    
    public static String getPackageNameFromNamespace(String namespace) {
        String packageName = ""; //$NON-NLS-1$
        try {
            List<String> packageNameElements = new ArrayList<String>();

            URL namespaceURL = new URL(namespace);

            // Remove www if there
            String authority = namespaceURL.getAuthority();
            if (authority.indexOf("www") != -1) { //$NON-NLS-1$
                authority = authority.substring(authority.indexOf(".") + 1, authority.length()); //$NON-NLS-1$
            }

            List<String> authorityElements = Arrays.asList(authority.split("\\.")); //$NON-NLS-1$
            Collections.reverse(authorityElements);
            packageNameElements.addAll(authorityElements);

            String path = namespaceURL.getPath();
            List<String> pathElements = Arrays.asList(path.split("[/\\\\]")); //$NON-NLS-1$
            packageNameElements.addAll(pathElements);

            Iterator<String> packageIterator = packageNameElements.iterator();
            while (packageIterator.hasNext()) {
                String element = packageIterator.next();
                if (element.trim().length() > 0) {
                    packageName += element;
                    if (packageIterator.hasNext()) {
                        packageName += "."; //$NON-NLS-1$
                    }
                }
            }
        } catch (MalformedURLException murle) {
            JAXWSCorePlugin.log(murle);
        }
        return packageName.toLowerCase();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4185.java