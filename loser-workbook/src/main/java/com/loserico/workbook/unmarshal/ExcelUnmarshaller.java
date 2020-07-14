package com.loserico.workbook.unmarshal;

import com.loserico.common.lang.concurrent.Concurrent;
import com.loserico.workbook.exception.BuilderUncompleteException;
import com.loserico.workbook.exception.WorkbookCreationException;
import com.loserico.workbook.unmarshal.assassinator.AssassinatorMaster;
import com.loserico.workbook.unmarshal.assassinator.POJOAssassinator;
import com.loserico.workbook.unmarshal.builder.POJOAssassinatorBuilder;
import com.loserico.workbook.unmarshal.iterator.RowIterator;
import com.loserico.workbook.utils.ExcelUtils;
import com.loserico.workbook.utils.ValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 将Excel解析为POJO列表的入口类 
 * <p>
 * Copyright: Copyright (c) 2019-10-15 10:50
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public final class ExcelUnmarshaller {

	private Workbook workbook;

	private File file;
	
	private MultipartFile multipartFile;

	private String sheetName;

	/**
	 * 从第0个Sheet开始
	 */
	private int fallbackSheetIndex = -1;

	/**
	 * 从第0行开始
	 */
	private int titleRowIndex = -1;

	private Class<?> pojoType;
	
	private List<POJOAssassinator> assassinators = null;
	
	private AssassinatorMaster assassinatorMaster = null;

	/** 是否要执行数据校验 */
	private boolean validate = false;

	private Validator validator;

	private RowIterator<Row> iterator = null;
	
	@SuppressWarnings("unchecked")
	private ExcelUnmarshaller(Builder builder) {
		this.workbook = builder.workbook;
		this.file = builder.file;
		this.multipartFile = builder.multipartFile;
		this.sheetName = builder.sheetName;
		this.fallbackSheetIndex = builder.fallbackSheetIndex;
		this.titleRowIndex = builder.titleRowIndex;
		this.pojoType = builder.pojoType;
		this.validate = builder.validate;
		if (this.validate) {
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			this.validator = factory.getValidator();
		}
		
		if (this.workbook == null) {
			try {
				if (this.file != null) {
					this.workbook = ExcelUtils.getWorkbook(this.file);
				} else if (this.multipartFile != null) {
					this.workbook = ExcelUtils.getWorkbook(this.multipartFile);
				}
			} catch (Exception e) {
				throw new WorkbookCreationException(e);
			}
		}
		
		this.assassinators = POJOAssassinatorBuilder.build(pojoType);
		this.assassinatorMaster = AssassinatorMaster.builder()
				.sheetName(sheetName)
				.fallbackSheetIndex(fallbackSheetIndex)
				.titleRowIndex(titleRowIndex)
				.build();
		
		this.iterator = assassinatorMaster.train(assassinators, workbook);
	}
	
	public void reset() {
		if (this.iterator != null) {
			this.iterator.reset();
		}
	}

	public <T> List<T> unmarshall() {

		int totalCount = iterator.getTotalCount();
		//用totalCount作为ArrayList的初始容量, 消除扩容带来的性能开销
		List<T> results = new ArrayList<>(totalCount);
		
		if (totalCount < 10000) {
			while (iterator.hasNext()) {
				Row row = iterator.next();
				T instance = null;
				try {
					instance = (T) pojoType.newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					log.error("Should have a default constructor", e);
					throw new RuntimeException("Should have a default constructor", e);
				}
				results.add(instance);

				for (POJOAssassinator pojoAssassinator : assassinators) {
					pojoAssassinator.assassinate(row, instance);
				}
				
				/*
				 * 每反序列化一个POJO执行一次数据校验, 防止在Excel很大时, 全部分序列化完成后再做数据
				 * 校验遇到失败的话整个过程会比较慢的情况. 
				 * 
				 * 这里的哲学是遇到错误尽快发现尽快上报
				 */
				if (validate) {
					Set<ConstraintViolation<T>> violations = validator.validate(instance);
					if (!violations.isEmpty()) {
						ValidationUtils.throwBindException(row.getRowNum(), violations);
						/*Set<ConstraintViolation<?>> vios = new HashSet<>();
						vios.addAll(violations);
						throw new BindException("Row[" + row.getRowNum() + "] validate failed!", vios);*/
					}
				}
			}
		} else {
			ExecutorService executorService = Concurrent.ncoreFixedThreadPool();
			while (iterator.hasNext()) {
				Row row = iterator.next();
				T instance;
				try {
					instance = (T) pojoType.newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					log.error("Should have a default constructor", e);
					throw new RuntimeException("Should have a default constructor", e);
				}
				results.add(instance);

				for (POJOAssassinator pojoAssassinator : assassinators) {
					executorService.execute(() -> pojoAssassinator.assassinate(row, instance));
				}
			}
			Concurrent.awaitTermination(executorService, 3, TimeUnit.MINUTES);
		}

		return results;
	}

	public static Builder builder(Workbook workbook) {
		Objects.requireNonNull(workbook, "workbook cannot be null");
		return new Builder(workbook);
	}
	
	public static Builder builder(MultipartFile multipartFile) {
		Objects.requireNonNull(multipartFile, "multipartFile cannot be null");
		return new Builder(multipartFile);
	}
	
	public static Builder builder(File file) {
		Objects.requireNonNull(file, "file cannot be null");
		return new Builder(file);
	}

	public static class Builder {

		private Workbook workbook;

		private File file;
		
		private MultipartFile multipartFile;

		private String sheetName;

		private int fallbackSheetIndex = -1;

		private int titleRowIndex = -1;

		private Class<?> pojoType;

		private boolean validate = false;
		
		public Builder(Workbook workbook) {
			this.workbook = workbook;
		}
		
		public Builder(MultipartFile multipartFile) {
			this.multipartFile = multipartFile;
		}
		
		public Builder(File file) {
			this.file = file;
		}

		public Builder sheetName(String sheetName) {
			this.sheetName = sheetName;
			return this;
		}

		public Builder fallbackSheetIndex(int fallbackSheetIndex) {
			this.fallbackSheetIndex = fallbackSheetIndex;
			return this;
		}

		public Builder titleRowIndex(int titleRowIndex) {
			this.titleRowIndex = titleRowIndex;
			return this;
		}

		public Builder pojoType(Class<?> pojoType) {
			this.pojoType = pojoType;
			return this;
		}

		/**
		 * 是否要对POJO执行JSR380 Bean Validation
		 * 如果执行数据校验, 是以反序列化一个POJO执行一次数据校验的形式出现
		 * 
		 * 参考: https://www.baeldung.com/javax-validation
		 * @param validate
		 * @return Builder
		 * @on
		 */
		public Builder validate(boolean validate) {
			this.validate = validate;
			return this;
		}

		public ExcelUnmarshaller build() {
			// 二者任选其一必须指定
			if (this.file == null && this.workbook == null && multipartFile == null) {
				throw new BuilderUncompleteException("Either multipartFile, file or workbook must exists!");
			}
			if (sheetName == null && fallbackSheetIndex == -1) {
				throw new BuilderUncompleteException("sheetName or fallbackSheetIndex must be set!");
			}
			if (pojoType == null) {
				throw new BuilderUncompleteException("pojoType must be set!");
			}

			return new ExcelUnmarshaller(this);
		}
	}
}
