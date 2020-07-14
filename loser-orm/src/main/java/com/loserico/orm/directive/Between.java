/*
 * Copyright 2003-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of the
 * License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */

package com.loserico.orm.directive;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.directive.DirectiveConstants;
import org.apache.velocity.runtime.parser.node.Node;

import java.io.IOException;
import java.io.Writer;

/**
 * 
 * <p>
 * Copyright: Copyright (c) 2018-06-07 16:25
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu	ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class Between extends Directive {

	public String getName() {
		return "between";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.velocity.runtime.directive.Directive#getType()
	 */
	public int getType() {
		return DirectiveConstants.LINE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.velocity.runtime.directive.Directive#render(org.apache.velocity.
	 * context.InternalContextAdapter, java.io.Writer,
	 * org.apache.velocity.runtime.parser.node.Node)
	 */
	public boolean render(InternalContextAdapter context, Writer writer,
			Node node) throws IOException, ResourceNotFoundException,
			ParseErrorException, MethodInvocationException {
		Object column = node.jjtGetChild(0).value(context);
		//两个边界值，如 2018-01-01 11:11:11
		Object leftValue = node.jjtGetChild(1).value(context);
		Object rightValue = node.jjtGetChild(2).value(context);
		int num = node.jjtGetNumChildren();
		String operator = "AND";
		if (num == 4) {
			operator = (String)node.jjtGetChild(3).value(context);
		}
		operator += " ";
		
		//取两个变量的名字，如拿到的是$applyDateBegin, 转成:applyDateBegin
		String left = ":" + node.jjtGetChild(1).literal().substring(1);
		String right = ":" + node.jjtGetChild(2).literal().substring(1);
		
		if (leftValue != null && rightValue != null) {
			writer.append(operator + column + " BETWEEN " + left + " AND " + right);
		} else if (leftValue != null && rightValue == null) {
			writer.append(operator + column + " >= " + left);
		} else if (leftValue == null && rightValue != null) {
			writer.append(operator + column + " <= " + right);
		}
		return true;
	}

}