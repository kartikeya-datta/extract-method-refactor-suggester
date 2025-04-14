error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11791.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11791.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11791.java
text:
```scala
private l@@ong elementHash() {

package org.jboss.as.messaging;

import org.hornetq.api.core.Pair;
import org.hornetq.api.core.SimpleString;
import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.core.config.Configuration;
import org.hornetq.core.config.impl.Validators;
import org.hornetq.core.settings.impl.AddressFullMessagePolicy;
import org.hornetq.core.settings.impl.AddressSettings;
import org.jboss.as.model.AbstractModelElement;
import org.jboss.logging.Logger;
import org.jboss.msc.service.ServiceActivator;
import org.jboss.msc.service.ServiceActivatorContext;
import org.jboss.staxmapper.XMLExtendedStreamReader;
import org.jboss.staxmapper.XMLExtendedStreamWriter;

import javax.xml.stream.XMLStreamException;
import java.util.Set;

/**
 * @author scott.stark@jboss.org
 * @version $Revision:$
 */
public class AddressSettingsElement extends AbstractModelElement<AddressSettingsElement> implements ServiceActivator {
   private static final long serialVersionUID = 1L;
   private static final Logger log = Logger.getLogger("org.jboss.as.messaging");

   public AddressSettingsElement(final XMLExtendedStreamReader reader, Configuration config) throws XMLStreamException {
      boolean trace = log.isTraceEnabled();
      if(trace)
         log.trace("Begin " + reader.getLocation() + reader.getLocalName());      // Handle elements
      int tag = reader.getEventType();
      String localName = null;
      do {
         tag = reader.nextTag();
         localName = reader.getLocalName();
         final Element element = Element.forName(reader.getLocalName());
         /*
            <address-settings>
               <!--default for catch all-->
               <address-setting match="#">
                  <dead-letter-address>jms.queue.DLQ</dead-letter-address>
                  <expiry-address>jms.queue.ExpiryQueue</expiry-address>
                  <redelivery-delay>0</redelivery-delay>
                  <max-size-bytes>10485760</max-size-bytes>
                  <message-counter-history-day-limit>10</message-counter-history-day-limit>
                  <address-full-policy>BLOCK</address-full-policy>
               </address-setting>
            </address-settings>
         */
         switch (element) {
         case ADDRESS_SETTING:
            String match = reader.getAttributeValue(0);
            Pair<String, AddressSettings> settings = parseAddressSettings(reader, match);
            config.getAddressesSettings().put(settings.a, settings.b);
            break;
         }
      } while (reader.hasNext() && localName.equals(Element.ADDRESS_SETTING.getLocalName()));
      if(trace)
         log.trace("End " + reader.getLocation() + reader.getLocalName());
   }

   @Override
   public long elementHash() {
      return 0;  //To change body of implemented methods use File | Settings | File Templates.
   }

   @Override
   protected Class<AddressSettingsElement> getElementClass() {
      return AddressSettingsElement.class;  //To change body of implemented methods use File | Settings | File Templates.
   }

   @Override
   public void writeContent(XMLExtendedStreamWriter streamWriter) throws XMLStreamException {
      //To change body of implemented methods use File | Settings | File Templates.
   }

   @Override
   public void activate(ServiceActivatorContext serviceActivatorContext) {
      //To change body of implemented methods use File | Settings | File Templates.
   }

   public Set<TransportConfiguration> getTransportConfiguration() {
      return null;  //To change body of created methods use File | Settings | File Templates.
   }

   public Pair<String, AddressSettings> parseAddressSettings(final XMLExtendedStreamReader reader, String match)
      throws XMLStreamException {
      AddressSettings addressSettings = new AddressSettings();

      Pair<String, AddressSettings> setting = new Pair<String, AddressSettings>(match, addressSettings);

      int tag = reader.getEventType();
      String localName = null;
      do {
         tag = reader.nextTag();
         localName = reader.getLocalName();
         final Element element = Element.forName(reader.getLocalName());

         switch (element) {
         case DEAD_LETTER_ADDRESS_NODE_NAME: {
            SimpleString queueName = new SimpleString(reader.getElementText().trim());
            addressSettings.setDeadLetterAddress(queueName);
         }
         break;
         case EXPIRY_ADDRESS_NODE_NAME: {
            SimpleString queueName = new SimpleString(reader.getElementText().trim());
            addressSettings.setExpiryAddress(queueName);
         }
         break;
         case REDELIVERY_DELAY_NODE_NAME: {
            addressSettings.setRedeliveryDelay(Long.valueOf(reader.getElementText().trim()));
         }
         break;
         case MAX_SIZE_BYTES_NODE_NAME: {
            addressSettings.setMaxSizeBytes(Long.valueOf(reader.getElementText().trim()));
         }
         break;
         case PAGE_SIZE_BYTES_NODE_NAME: {
            addressSettings.setPageSizeBytes(Long.valueOf(reader.getElementText().trim()));
         }
         break;
         case MESSAGE_COUNTER_HISTORY_DAY_LIMIT_NODE_NAME: {
            addressSettings.setMessageCounterHistoryDayLimit(Integer.valueOf(reader.getElementText().trim()));
         }
         break;
         case ADDRESS_FULL_MESSAGE_POLICY_NODE_NAME: {
            String value = reader.getElementText().trim();
            Validators.ADDRESS_FULL_MESSAGE_POLICY_TYPE.validate(Element.ADDRESS_FULL_MESSAGE_POLICY_NODE_NAME.getLocalName(),
               value);
            AddressFullMessagePolicy policy = null;
            if (value.equals(AddressFullMessagePolicy.BLOCK.toString())) {
               policy = AddressFullMessagePolicy.BLOCK;
            } else if (value.equals(AddressFullMessagePolicy.DROP.toString())) {
               policy = AddressFullMessagePolicy.DROP;
            } else if (value.equals(AddressFullMessagePolicy.PAGE.toString())) {
               policy = AddressFullMessagePolicy.PAGE;
            }
            addressSettings.setAddressFullMessagePolicy(policy);
         }
         break;
         case LVQ_NODE_NAME: {
            addressSettings.setLastValueQueue(Boolean.valueOf(reader.getElementText().trim()));
         }
         break;
         case MAX_DELIVERY_ATTEMPTS: {
            addressSettings.setMaxDeliveryAttempts(Integer.valueOf(reader.getElementText().trim()));
         }
         break;
         case REDISTRIBUTION_DELAY_NODE_NAME: {
            addressSettings.setRedistributionDelay(Long.valueOf(reader.getElementText().trim()));
         }
         break;
         case SEND_TO_DLA_ON_NO_ROUTE: {
            addressSettings.setSendToDLAOnNoRoute(Boolean.valueOf(reader.getElementText().trim()));
         }
         break;
         default:
            break;
         }

         reader.discardRemainder();
      }
      while (!reader.getLocalName().equals(Element.ADDRESS_SETTING.getLocalName()) && reader.getEventType() != XMLExtendedStreamReader.END_ELEMENT);

      return setting;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11791.java