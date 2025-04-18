import org.apache.wicket.request.mapper.parameter.PageParameters;

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
package org.apache.wicket.markup.html.internal;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameters.PageParameters;

/**
 * Mock page for testing.
 * 
 */
public class EnclosurePage_4 extends WebPage
{
	private static final long serialVersionUID = 1L;

	/**
	 * Construct.
	 * 
	 * @param param
	 */
	public EnclosurePage_4(PageParameters param)
	{
		final WebMarkupContainer foo = new WebMarkupContainer("foo");
		final Label bar = new Label("bar", "bazqux");

		// that one doesn't matter
		boolean visible = param.getNamedParameter("visible").toBoolean();
		bar.setVisible(visible);

		foo.add(bar);
		add(foo);
	}
}