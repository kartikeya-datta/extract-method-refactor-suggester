error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12399.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12399.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12399.java
text:
```scala
static v@@oid processType(final UnifiedServiceRefMetaData serviceRefUMDM) {

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
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

package org.jboss.as.webservices.webserviceref;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceRef;
import javax.xml.ws.WebServiceRefs;
import javax.xml.ws.soap.MTOM;

import org.jboss.logging.Logger;
import org.jboss.metadata.javaee.jboss.JBossPortComponentRef;
import org.jboss.metadata.javaee.jboss.JBossServiceReferenceMetaData;
import org.jboss.metadata.javaee.jboss.StubPropertyMetaData;
import org.jboss.metadata.javaee.spec.Addressing;
import org.jboss.metadata.javaee.spec.ParamValueMetaData;
import org.jboss.metadata.javaee.spec.PortComponentRef;
import org.jboss.metadata.javaee.spec.ServiceReferenceHandlerChainMetaData;
import org.jboss.metadata.javaee.spec.ServiceReferenceHandlerChainsMetaData;
import org.jboss.metadata.javaee.spec.ServiceReferenceHandlerMetaData;
import org.jboss.metadata.javaee.spec.ServiceReferenceMetaData;
import org.jboss.wsf.spi.metadata.j2ee.serviceref.UnifiedHandlerChainMetaData;
import org.jboss.wsf.spi.metadata.j2ee.serviceref.UnifiedHandlerChainsMetaData;
import org.jboss.wsf.spi.metadata.j2ee.serviceref.UnifiedHandlerMetaData;
import org.jboss.wsf.spi.metadata.j2ee.serviceref.UnifiedInitParamMetaData;
import org.jboss.wsf.spi.metadata.j2ee.serviceref.UnifiedPortComponentRefMetaData;
import org.jboss.wsf.spi.metadata.j2ee.serviceref.UnifiedServiceRefMetaData;
import org.jboss.wsf.spi.metadata.j2ee.serviceref.UnifiedStubPropertyMetaData;
import org.jboss.wsf.spi.serviceref.ServiceRefHandler.Type;

/**
 * Translates WS Refs from JBossAS MD to JBossWS UMDM format.
 *
 * @author <a href="mailto:ropalka@redhat.com">Richard Opalka</a>
 */
final class WSRefUtils {

    private static final Logger log = Logger.getLogger(WSRefUtils.class);

    private WSRefUtils() {
        // forbidden instantiation
    }

    static UnifiedServiceRefMetaData translate(final ServiceReferenceMetaData serviceRefMD, final UnifiedServiceRefMetaData serviceRefUMDM) {
        serviceRefUMDM.setServiceRefName(serviceRefMD.getName());
        serviceRefUMDM.setServiceRefType(serviceRefMD.getServiceRefType());
        serviceRefUMDM.setServiceInterface(serviceRefMD.getServiceInterface());
        serviceRefUMDM.setWsdlFile(serviceRefMD.getWsdlFile());
        serviceRefUMDM.setMappingFile(serviceRefMD.getJaxrpcMappingFile());
        serviceRefUMDM.setServiceQName(serviceRefMD.getServiceQname());

        // propagate port components
        final Collection<? extends PortComponentRef> portComponentsMD = serviceRefMD.getPortComponentRef();
        if (portComponentsMD != null) {
            for (final PortComponentRef portComponentMD : portComponentsMD) {
                final UnifiedPortComponentRefMetaData portComponentUMDM = getUnifiedPortComponentRefMetaData(serviceRefUMDM,
                        portComponentMD);
                if (portComponentUMDM.getServiceEndpointInterface() != null || portComponentUMDM.getPortQName() != null) {
                    serviceRefUMDM.addPortComponentRef(portComponentUMDM);
                } else {
                    log.warn("Ignoring <port-component-ref> without <service-endpoint-interface> and <port-qname>: " + portComponentUMDM);
                }
            }
        }

        // propagate handlers
        final Collection<ServiceReferenceHandlerMetaData> handlersMD = serviceRefMD.getHandlers();
        if (handlersMD != null) {
           for (final ServiceReferenceHandlerMetaData handlerMD : handlersMD) {
              final UnifiedHandlerMetaData handlerUMDM = getUnifiedHandlerMetaData(handlerMD);
              serviceRefUMDM.addHandler(handlerUMDM);
           }
        }

        // propagate handler chains
        ServiceReferenceHandlerChainsMetaData handlerChainsMD = serviceRefMD.getHandlerChains();
        if (handlerChainsMD != null) {
           final UnifiedHandlerChainsMetaData handlerChainsUMDM = getUnifiedHandlerChainsMetaData(handlerChainsMD);
           serviceRefUMDM.setHandlerChains(handlerChainsUMDM);
        }

        // propagate jboss specific MD
        if (serviceRefMD instanceof JBossServiceReferenceMetaData) {
           processUnifiedJBossServiceRefMetaData(serviceRefUMDM, serviceRefMD);
        }

        // detect JAXWS or JAXRPC type
        processType(serviceRefUMDM);

        return serviceRefUMDM;
    }

    private static void processUnifiedJBossServiceRefMetaData(final UnifiedServiceRefMetaData serviceRefUMDM, final ServiceReferenceMetaData serviceRefMD) {
        final JBossServiceReferenceMetaData jbossServiceRefMD = (JBossServiceReferenceMetaData) serviceRefMD;
        serviceRefUMDM.setServiceImplClass(jbossServiceRefMD.getServiceClass());
        serviceRefUMDM.setConfigName(jbossServiceRefMD.getConfigName());
        serviceRefUMDM.setConfigFile(jbossServiceRefMD.getConfigFile());
        serviceRefUMDM.setWsdlOverride(jbossServiceRefMD.getWsdlOverride());
        serviceRefUMDM.setHandlerChain(jbossServiceRefMD.getHandlerChain());
    }

    private static UnifiedPortComponentRefMetaData getUnifiedPortComponentRefMetaData(final UnifiedServiceRefMetaData serviceRefUMDM, final PortComponentRef portComponentMD) {
        final UnifiedPortComponentRefMetaData portComponentUMDM = new UnifiedPortComponentRefMetaData(serviceRefUMDM);

        // propagate service endpoint interface
        portComponentUMDM.setServiceEndpointInterface(portComponentMD.getServiceEndpointInterface());

        // propagate MTOM properties
        portComponentUMDM.setMtomEnabled(portComponentMD.isEnableMtom());
        portComponentUMDM.setMtomThreshold(portComponentMD.getMtomThreshold());

        // propagate addressing properties
        final Addressing addressingMD = portComponentMD.getAddressing();
        if (addressingMD != null) {
            portComponentUMDM.setAddressingAnnotationSpecified(true);
            portComponentUMDM.setAddressingEnabled(addressingMD.isEnabled());
            portComponentUMDM.setAddressingRequired(addressingMD.isRequired());
            portComponentUMDM.setAddressingResponses(addressingMD.getResponses());
        }

        // propagate respect binding properties
        if (portComponentMD.getRespectBinding() != null) {
            portComponentUMDM.setRespectBindingAnnotationSpecified(true);
            portComponentUMDM.setRespectBindingEnabled(true);
        }

        // propagate link
        portComponentUMDM.setPortComponentLink(portComponentMD.getPortComponentLink());

        // propagate jboss specific MD
        if (portComponentMD instanceof JBossPortComponentRef) {
            processUnifiedJBossPortComponentRefMetaData(portComponentUMDM, portComponentMD);
        }

        return portComponentUMDM;
    }

    private static void processUnifiedJBossPortComponentRefMetaData(final UnifiedPortComponentRefMetaData portComponentUMDM, final PortComponentRef portComponentMD) {
        final JBossPortComponentRef jbossPortComponentMD = (JBossPortComponentRef) portComponentMD;

        // propagate port QName
        portComponentUMDM.setPortQName(jbossPortComponentMD.getPortQname());

        // propagate configuration properties
        portComponentUMDM.setConfigName(jbossPortComponentMD.getConfigName());
        portComponentUMDM.setConfigFile(jbossPortComponentMD.getConfigFile());

        // propagate stub properties
        final List<StubPropertyMetaData> stubPropertiesMD = jbossPortComponentMD.getStubProperties();
        if (stubPropertiesMD != null) {
            for (final StubPropertyMetaData stubPropertyMD : stubPropertiesMD) {
                final UnifiedStubPropertyMetaData stubPropertyUMDM = new UnifiedStubPropertyMetaData();
                stubPropertyUMDM.setPropName(stubPropertyMD.getPropName());
                stubPropertyUMDM.setPropValue(stubPropertyMD.getPropValue());
                portComponentUMDM.addStubProperty(stubPropertyUMDM);
            }
        }
    }

    private static UnifiedHandlerMetaData getUnifiedHandlerMetaData(ServiceReferenceHandlerMetaData srhmd) {
        UnifiedHandlerMetaData handlerUMDM = new UnifiedHandlerMetaData();
        handlerUMDM.setHandlerName(srhmd.getHandlerName());
        handlerUMDM.setHandlerClass(srhmd.getHandlerClass());
        List<ParamValueMetaData> initParams = srhmd.getInitParam();
        if (initParams != null) {
            for (ParamValueMetaData initParam : initParams) {
                UnifiedInitParamMetaData param = new UnifiedInitParamMetaData();
                param.setParamName(initParam.getParamName());
                param.setParamValue(initParam.getParamValue());
                handlerUMDM.addInitParam(param);
            }
        }
        List<QName> soapHeaders = srhmd.getSoapHeader();
        if (soapHeaders != null) {
            for (QName soapHeader : soapHeaders) {
                handlerUMDM.addSoapHeader(soapHeader);
            }
        }
        List<String> soapRoles = srhmd.getSoapRole();
        if (soapRoles != null) {
            for (String soapRole : soapRoles) {
                handlerUMDM.addSoapRole(soapRole);
            }
        }
        List<String> portNames = srhmd.getPortName();
        if (portNames != null) {
            for (String portName : portNames) {
                handlerUMDM.addPortName(portName);
            }
        }
        return handlerUMDM;
    }

    private static UnifiedHandlerChainsMetaData getUnifiedHandlerChainsMetaData(final ServiceReferenceHandlerChainsMetaData handlerChainsMD) {
        final UnifiedHandlerChainsMetaData handlerChainsUMDM = new UnifiedHandlerChainsMetaData();

        for (final ServiceReferenceHandlerChainMetaData handlerChainMD : handlerChainsMD.getHandlers()) {
            final UnifiedHandlerChainMetaData handlerChainUMDM = new UnifiedHandlerChainMetaData();
            handlerChainUMDM.setServiceNamePattern(handlerChainMD.getServiceNamePattern());
            handlerChainUMDM.setPortNamePattern(handlerChainMD.getPortNamePattern());
            handlerChainUMDM.setProtocolBindings(handlerChainMD.getProtocolBindings());

            for (final ServiceReferenceHandlerMetaData handlerMD : handlerChainMD.getHandler()) {
                final UnifiedHandlerMetaData handlerUMDM = getUnifiedHandlerMetaData(handlerMD);
                handlerChainUMDM.addHandler(handlerUMDM);
            }

            handlerChainsUMDM.addHandlerChain(handlerChainUMDM);
        }

        return handlerChainsUMDM;
    }

    private static void processType(final UnifiedServiceRefMetaData serviceRefUMDM) {
        final boolean isJAXRPC = serviceRefUMDM.getMappingFile() != null || "javax.xml.rpc.Service".equals(serviceRefUMDM.getServiceInterface());
        serviceRefUMDM.setType(isJAXRPC ? Type.JAXRPC : Type.JAXWS);
    }

    static void processAnnotatedElement(final AnnotatedElement anElement, final UnifiedServiceRefMetaData serviceRefUMDM) {
       processAddressingAnnotation(anElement, serviceRefUMDM);
       processMTOMAnnotation(anElement, serviceRefUMDM);
       processRespectBindingAnnotation(anElement, serviceRefUMDM);
       processHandlerChainAnnotation(anElement, serviceRefUMDM);
       processServiceRefType(anElement, serviceRefUMDM);
    }

    private static void processAddressingAnnotation(final AnnotatedElement anElement, final UnifiedServiceRefMetaData serviceRefUMDM) {
         final javax.xml.ws.soap.Addressing addressingAnnotation = getAnnotation(anElement, javax.xml.ws.soap.Addressing.class);

         if (addressingAnnotation != null) {
            serviceRefUMDM.setAddressingAnnotationSpecified(true);
            serviceRefUMDM.setAddressingEnabled(addressingAnnotation.enabled());
            serviceRefUMDM.setAddressingRequired(addressingAnnotation.required());
            serviceRefUMDM.setAddressingResponses(addressingAnnotation.responses().toString());
         }
      }

      private static void processMTOMAnnotation(final AnnotatedElement anElement, final UnifiedServiceRefMetaData serviceRefUMDM) {
         final MTOM mtomAnnotation = getAnnotation(anElement, MTOM.class);

         if (mtomAnnotation != null) {
            serviceRefUMDM.setMtomAnnotationSpecified(true);
            serviceRefUMDM.setMtomEnabled(mtomAnnotation.enabled());
            serviceRefUMDM.setMtomThreshold(mtomAnnotation.threshold());
         }
      }

      private static void processRespectBindingAnnotation(final AnnotatedElement anElement, final UnifiedServiceRefMetaData serviceRefUMDM) {
         final javax.xml.ws.RespectBinding respectBindingAnnotation = getAnnotation(anElement, javax.xml.ws.RespectBinding.class);

         if (respectBindingAnnotation != null) {
            serviceRefUMDM.setRespectBindingAnnotationSpecified(true);
            serviceRefUMDM.setRespectBindingEnabled(respectBindingAnnotation.enabled());
         }
      }

      private static  void processServiceRefType(final AnnotatedElement anElement, final UnifiedServiceRefMetaData serviceRefUMDM) {
         if (anElement instanceof Field) {
            final Class<?> targetClass = ((Field) anElement).getType();
            serviceRefUMDM.setServiceRefType(targetClass.getName());

            if (Service.class.isAssignableFrom(targetClass))
               serviceRefUMDM.setServiceInterface(targetClass.getName());
         } else if (anElement instanceof Method) {
            final Class<?> targetClass = ((Method) anElement).getParameterTypes()[0];
            serviceRefUMDM.setServiceRefType(targetClass.getName());

            if (Service.class.isAssignableFrom(targetClass))
               serviceRefUMDM.setServiceInterface(targetClass.getName());
         } else {
            final WebServiceRef serviceRefAnnotation = getWebServiceRefAnnotation(anElement, serviceRefUMDM);
            Class<?> targetClass = null;
            if (serviceRefAnnotation != null && (serviceRefAnnotation.type() != Object.class))
            {
               targetClass = serviceRefAnnotation.type();
               serviceRefUMDM.setServiceRefType(targetClass.getName());

               if (Service.class.isAssignableFrom(targetClass))
                  serviceRefUMDM.setServiceInterface(targetClass.getName());
            }
         }
      }

      private static void processHandlerChainAnnotation(final AnnotatedElement anElement, final UnifiedServiceRefMetaData serviceRefUMDM) {
         final javax.jws.HandlerChain handlerChainAnnotation = getAnnotation(anElement, javax.jws.HandlerChain.class);

         if (handlerChainAnnotation != null) {
            // Set the handlerChain from @HandlerChain on the annotated element
            String handlerChain = null;
            if (handlerChainAnnotation.file().length() > 0)
               handlerChain = handlerChainAnnotation.file();

            // Resolve path to handler chain
            if (handlerChain != null) {
               try {
                  new URL(handlerChain);
               } catch (MalformedURLException ignored) {
                  final Class<?> declaringClass = getDeclaringClass(anElement);

                  handlerChain = declaringClass.getPackage().getName().replace('.', '/') + "/" + handlerChain;
               }

               serviceRefUMDM.setHandlerChain(handlerChain);
            }
         }
      }

      private static <T extends Annotation> T getAnnotation(final AnnotatedElement anElement, final Class<T> annotationClass) {
         return anElement != null ? (T) anElement.getAnnotation(annotationClass) : null;
      }

      private static Class<?> getDeclaringClass(final AnnotatedElement annotatedElement) {
         Class<?> declaringClass = null;
         if (annotatedElement instanceof Field) {
            declaringClass = ((Field) annotatedElement).getDeclaringClass();
         } else if (annotatedElement instanceof Method) {
            declaringClass = ((Method) annotatedElement).getDeclaringClass();
         } else if (annotatedElement instanceof Class) {
            declaringClass = (Class<?>) annotatedElement;
         }

         return declaringClass;
      }

      private static WebServiceRef getWebServiceRefAnnotation(final AnnotatedElement anElement, final UnifiedServiceRefMetaData serviceRefUMDM) {
          final WebServiceRef webServiceRefAnnotation = getAnnotation(anElement, WebServiceRef.class);
          final WebServiceRefs webServiceRefsAnnotation = getAnnotation(anElement, WebServiceRefs.class);

          if (webServiceRefAnnotation == null && webServiceRefsAnnotation == null) {
              return null;
          }

          // Build the list of @WebServiceRef relevant annotations
          final List<WebServiceRef> wsrefList = new ArrayList<WebServiceRef>();

          if (webServiceRefAnnotation != null) {
              wsrefList.add(webServiceRefAnnotation);
          }

          if (webServiceRefsAnnotation != null) {
              for (final WebServiceRef webServiceRefAnn : webServiceRefsAnnotation.value()) {
                  wsrefList.add(webServiceRefAnn);
              }
          }

          // Return effective @WebServiceRef annotation
          WebServiceRef returnValue = null;
          if (wsrefList.size() == 1) {
              returnValue = wsrefList.get(0);
          } else {
              for (WebServiceRef webServiceRefAnn : wsrefList) {
                  if (serviceRefUMDM.getServiceRefName().endsWith(webServiceRefAnn.name())) {
                      returnValue = webServiceRefAnn;
                      break;
                  }
              }
          }

          return returnValue;
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12399.java