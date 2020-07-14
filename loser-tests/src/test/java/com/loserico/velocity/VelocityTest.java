package com.loserico.velocity;

import com.loserico.common.lang.utils.IOUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;
import org.junit.Test;

import java.io.StringWriter;
import java.util.Properties;

public class VelocityTest {

	@Test
	public void testCreateCustomDirective() {
		Properties properties = new Properties();
		properties.setProperty("userdirective", "com.loserico.velocity.TruncateDirective");
		properties.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.Log4JLogChute");
		properties.setProperty("runtime.log.logsystem.log4j.logger", "velocity");
		Velocity.init(properties); //初始化运行时引擎
		
		String template = IOUtils.readClassPathFileAsString("truncate-template.vm");
		VelocityContext context = new VelocityContext();
		//解析后数据的输出目标，java.io.Writer的子类  
		StringWriter out = new StringWriter();
		//进行解析  
		Velocity.evaluate(context, out, "truncate-line", template);
		System.out.println(out.toString());
	}
}
