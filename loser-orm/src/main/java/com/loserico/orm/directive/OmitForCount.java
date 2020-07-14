package com.loserico.orm.directive;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;

import java.io.IOException;
import java.io.Writer;

/**
 * 为分页查询中的count查询截掉limit xx xx子句
 * @author Rico Yu	ricoyu520@gmail.com
 * @since 2017-02-23 15:20
 * @version 1.0
 *
 */
public class OmitForCount extends Directive {

	@Override
	public String getName() {
		return "omitForCount";
	}

	@Override
	public int getType() {
		return BLOCK;
	}

	@Override
	public boolean render(InternalContextAdapter context, Writer writer, Node node)
			throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {
		Object isCountQuery = context.get("isCountQuery");
		if (isCountQuery != null && isCountQuery.equals(Boolean.TRUE)) {
			writer.append("");
		} else {
			Node content = node.jjtGetChild(0);
			content.render(context, writer);
		}
		return true;
	}

}
