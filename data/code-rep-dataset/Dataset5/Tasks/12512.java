import org.apache.wicket.request.component.PageParameters;

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
package org.apache.wicket.examples.niceurl.mounted;

import org.apache.wicket.examples.WicketExamplePage;
import org.apache.wicket.examples.niceurl.Home;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.ng.request.component.PageParameters;


/**
 * Simple bookmarkable page.
 * 
 * @author Eelco Hillenius
 */
public class Page3 extends WicketExamplePage
{
	/**
	 * Constructor
	 * 
	 * @param parameters
	 */
	public Page3(PageParameters parameters)
	{
		add(new BookmarkablePageLink("homeLink", Home.class));
	}
}