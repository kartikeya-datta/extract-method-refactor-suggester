public boolean resolve(final MarkupContainer container, final MarkupStream markupStream,

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.markup.resolver;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.parser.filter.WicketTagIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This is a tag resolver which handles &lt;wicket:container&gt;
 * 
 * Sometimes adding components in certain ways may lead to output of invalid markup. For example,
 * lets pretend we output table rows two at a time using a repeater. The markup would look something
 * like this:
 * 
 * <code>
 * 	<table>
 * 		<span wicket:id="repeater">
 * 			<tr><td>...</td></tr>
 * 			<tr><td>...</td></tr>
 * 		</span>
 * 	</table>
 * </code>
 * 
 * Notice that we had to attach the repeater to a component tag - in this case a <code>span</code>,
 * but a span is not a legal tag to nest under <code>table</code>. So we can rewrite the example
 * as following:
 * 
 * <code>
 * 	<table>
 * 		<wicket:container wicket:id="repeater">
 * 			<tr><td>...</td></tr>
 * 			<tr><td>...</td></tr>
 * 		</wicket:container>
 * 	</table>
 * </code>
 * 
 * The above is valid markup because wicket namespaced tags are allowed anywhere
 * 
 * @author Igor Vaynberg (ivaynberg)
 */
public class WicketContainerResolver implements IComponentResolver
{
	private static final Logger log = LoggerFactory.getLogger(WicketContainerResolver.class);

	static
	{
		// register "wicket:container"
		WicketTagIdentifier.registerWellKnownTagName("container");
	}


	private static final long serialVersionUID = 1L;

	/**
	 * Try to resolve the tag, then create a component, add it to the container and render it.
	 * 
	 * @see org.apache.wicket.markup.resolver.IComponentResolver#resolve(MarkupContainer,
	 *      MarkupStream, ComponentTag)
	 * 
	 * @param container
	 *            The container parsing its markup
	 * @param markupStream
	 *            The current markupStream
	 * @param tag
	 *            The current component tag while parsing the markup
	 * @return true, if componentId was handle by the resolver. False, otherwise
	 */
	public boolean resolve(final MarkupContainer< ? > container, final MarkupStream markupStream,
		final ComponentTag tag)
	{
		// this is only here so we register wicket:container tag, this resolver
		// does not actually do anything special to the tag
		return false;
	}

}