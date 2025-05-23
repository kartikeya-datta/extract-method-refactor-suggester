add(new Panel1<Void>("panel1"));

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
package org.apache.wicket.examples.template.pageinheritance;

/**
 * Concrete page. Note that it extends {@link TemplatePage} and the markup uses
 * &lt;wicket:extend&gt; tags to define the region that is to be expanded into the parent's
 * &lt;wicket:child&gt; element.
 * 
 * @author Eelco Hillenius
 */
public class Page1 extends TemplatePage
{
	/**
	 * Constructor
	 */
	public Page1()
	{
		setPageTitle("Template example, page 1");
		add(new Panel1("panel1"));
	}
}
 No newline at end of file