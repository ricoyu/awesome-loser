package com.loserico.validation.validation;

import com.loserico.common.lang.exception.ApplicationException;
import com.loserico.common.lang.utils.ReflectionUtils;
import com.loserico.validation.validation.annotation.MandatoryIf;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static java.util.Optional.ofNullable;

/**
 * If value of referenceField equals as specified in referenceValue, mandatoryField is
 * mandatory, otherwise not.
 *
 * @author xuehyu
 * @since Aug 22, 2014
 */
public class MandatoryIfValidator implements ConstraintValidator<MandatoryIf, Object> {
	
	private static final Logger log = LoggerFactory.getLogger(MandatoryIfValidator.class);
	private String mandatoryField = null;
	private String referenceField = null;
	private String referenceValue = null;
	private String message = null;
	
	@Override
	public void initialize(MandatoryIf constraintAnnotation) {
		mandatoryField = constraintAnnotation.mandatoryField();
		referenceField = constraintAnnotation.referenceField();
		referenceValue = constraintAnnotation.referenceValue();
		message = constraintAnnotation.message();
	}
	
	@Override
	public boolean isValid(Object bean, ConstraintValidatorContext context) {
		try {
			Object referencedFieldValue = ReflectionUtils.getFieldValue(referenceField, bean);
			// referenceField has empty value, so mandatoryField is considered to be valid
			if (null == referencedFieldValue) {
				return true;
			}
			
			//参考值等于给定值或者没有参考值
			if (null == referenceValue ||
					"".equals(referenceValue) ||
					referenceValue.equals(referencedFieldValue.toString())) {
				Object mandatoryFieldValue = ReflectionUtils.getFieldValue(mandatoryField, bean);
				boolean isEmpty = mandatoryFieldValue == null ||
						((mandatoryFieldValue instanceof String) && StringUtils.isBlank((String) mandatoryFieldValue));
				if (isEmpty) {
					context.disableDefaultConstraintViolation();
					context.buildConstraintViolationWithTemplate(ofNullable(message).orElse("Is required."))
							.addPropertyNode(mandatoryField)
							.addConstraintViolation();
					//not valid
					return false;
				}
				
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ApplicationException(e);
		}
		return true;
	}
	
}
