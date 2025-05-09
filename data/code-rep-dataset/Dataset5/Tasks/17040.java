CharSequence transform(final Component component, final CharSequence output) throws Exception;

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
package org.apache.wicket.markup.transformer;

import org.apache.wicket.Component;

/**
 * A common interface to be implemented be OutputTransformerContainers and
 * TranformerBehaviors which post-process the output markup of a component.
 * 
 * @author Juergen Donnerstag
 */
public interface ITransformer
{
	/**
	 * Will be invoked after all child components have been processed to allow
	 * for transforming the markup generated.
	 * 
	 * @param component
	 *            The associated Wicket component
	 * @param output
	 *            The markup generated by the child components
	 * @return The output which will be appended to the orginal response
	 * @throws Exception
	 */
	CharSequence transform(final Component component, final String output) throws Exception;
}