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
 * 传一个参数true表示分页查询中的count(1)子句，替换实际SELECT子句中的内容
 * @author Rico Yu	ricoyu520@gmail.com
 * @since 2017-02-23 15:15
 * @version 1.0
 *
 */
public class Count extends Directive {

	@Override
	public String getName() {
		return "count";
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
			writer.append(" count(1) ");
		} else {
			Node content = node.jjtGetChild(0);
			content.render(context, writer);
		}
		return true;
	}

}
