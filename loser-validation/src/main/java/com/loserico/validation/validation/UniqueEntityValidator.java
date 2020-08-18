package com.loserico.validation.validation;

import com.loserico.common.lang.context.ApplicationContextHolder;
import com.loserico.common.lang.exception.ApplicationException;
import com.loserico.common.lang.utils.ReflectionUtils;
import com.loserico.validation.service.UniqueEntityService;
import com.loserico.validation.validation.annotation.UniqueEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * If value of referenceField equals as specified in referenceValue, mandatoryField is
 * mandatory, otherwise not.
 *
 * @author xuehyu
 * @since Aug 22, 2014
 */
public class UniqueEntityValidator implements ConstraintValidator<UniqueEntity, Object> {
	
	private static final Logger logger = LoggerFactory.getLogger(UniqueEntityValidator.class);
	
	private String table = null;
	private String[] fieldNames = null;
	private String primaryKeyField = null;
	private String[] properties = null;
	private boolean isSoftDelete;
	private Class<? extends UniqueEntityService> serviceClass;
	
	@Override
	public void initialize(UniqueEntity constraintAnnotation) {
		fieldNames = constraintAnnotation.fieldNames();
		primaryKeyField = constraintAnnotation.primaryKey();
		properties = constraintAnnotation.properties();
		isSoftDelete = constraintAnnotation.isSoftDelete();
		serviceClass = constraintAnnotation.serviceBean();
	}
	
	@Override
	public boolean isValid(Object bean, ConstraintValidatorContext context) {
		Serializable primaryKey;
		try {
			//先从容器中取对应的bean
			UniqueEntityService uniqueEntityService = ApplicationContextHolder.getBean(serviceClass);
			if (uniqueEntityService == null) {
				throw new ApplicationException("找不到类型为: " + serviceClass.getName() + " 的Bean");
			}
			
			//propertyValues是SQL where条件中的值
			primaryKey = ReflectionUtils.getFieldValue(primaryKeyField, bean);
			Object[] propertyValues = new Object[properties.length];
			for (int i = 0; i < properties.length; i++) {
				propertyValues[i] = ReflectionUtils.getFieldValue(properties[i], bean);
			}
			
			//如果只有一个字段并且字段值为null, 那么认为合法
			if (fieldNames.length == 1 && propertyValues[0] == null) {
				return true;
			}
			
			Map<String, Object> params = new HashMap<>();
			for (int i = 0; i < properties.length; i++) {
				params.put(properties[i], propertyValues[i]);
			}
			int count = uniqueEntityService.count(params, primaryKey);
			return count == 0;
		} catch (Exception e) {
			logger.error("", e);
			throw new ApplicationException("执行UniqueEntityValidator异常", e);
		}
	}
	
}
