package com.loserico.workbook.utils;

import com.loserico.workbook.exception.FileCopyException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.MoreObjects.firstNonNull;
import static java.lang.Thread.currentThread;
import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.text.MessageFormat.format;

/**
 * IO 读写工具类
 * <p>
 * Copyright: Copyright (c) 2019/10/15 10:59
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class IOUtils {

	public static final Charset GBK = Charset.forName("GBK");

	/**
	 * The Unix directory separator character.
	 */
	public static final String DIR_SEPARATOR_UNIX = "/";
	
	/**
	 * The Windows directory separator character.
	 */
	public static final char DIR_SEPARATOR_WINDOWS = '\\';

	public static final String CLASSPATH_PREFIX = "classpath*:";

	public static ByteArrayInputStream toByteArrayInputStream(File file) throws IOException {
		return new ByteArrayInputStream(Files.readAllBytes(file.toPath()));
	}

	public static BufferedReader toBufferedReader(InputStream in, Charset charset) {
		return new BufferedReader(new InputStreamReader(in, charset));
	}

	/**
	 * 在临时目录创建指定后缀的文件
	 *
	 * @param suffix
	 * @return
	 * @throws IOException
	 */
	public static File tempFile(String suffix) throws IOException {
		if (suffix != null && suffix.indexOf(".") != 0) {
			suffix = "." + suffix;
		}
		return File.createTempFile(RandomStringUtils.randomAlphanumeric(16), suffix);
	}

	/*
	 * Copy one file to another place
	 */
	public static boolean copy(Path copyFrom, Path copyTo, CopyOption... options) {
		boolean parentCreateResult = createParentDir(copyTo);
		if (parentCreateResult) {
			try (InputStream is = new FileInputStream(copyFrom.toFile())) {
				Files.copy(is, copyTo, options);
				return true;
			} catch (IOException e) {
				log.error(format("copy from [{0}] to [{1}] failed!", copyFrom, copyTo), e);
			}
		}
		return false;
	}


	/**
	 * @of
	 * Create parent directory if this path has a parent path and if not exist yet
	 * 不管是否执行了父目录的创建，返回父目录存在与否的最终状态
	 * true 存在
	 * false 不存在
	 *
	 * @param path
	 * @return boolean
	 * @on
	 */
	public static boolean createParentDir(Path path) {
		Optional.of(path.getParent())
				.ifPresent(parent -> {
					if (!Files.exists(parent, NOFOLLOW_LINKS)) {
						try {
							Files.createDirectories(parent);
						} catch (IOException e) {
							log.error(format("create parent directory [{0}] failed", parent), e);
						}
					}
				});
		return Files.exists(path.getParent(), NOFOLLOW_LINKS);
	}

	public static Path fileCopy(String sourceFileName) {
		Objects.requireNonNull(sourceFileName);
		Path sourcePath = Paths.get(sourceFileName);
		return fileCopy(sourcePath);
	}


	public static Path fileCopy(Path sourcePath) {
		Objects.requireNonNull(sourcePath);
		String fileName = sourcePath.getFileName().toString();
		String suffix = FilenameUtils.getExtension(fileName);
		try {
			Path destPath = tempFile(suffix).toPath();
			Files.copy(sourcePath, destPath, REPLACE_EXISTING);
			return destPath;
		} catch (IOException e) {
			log.error("Failed to copy file {} ", fileName);
			throw new FileCopyException(e);
		}
	}


	/**
	 * 返回文件名后缀 <pre> foo.txt --&gt; "txt" a/b/c.jpg --&gt; "jpg" a/b.txt/c
	 * --&gt; "" a/b/c --&gt; "" </pre>
	 *
	 * @param filename
	 * @return
	 */
	public static String fileExtension(String filename) {
		return FilenameUtils.getExtension(filename);
	}


	/**
	 * 在临时目录创建指定文件名和后缀的文件 java.io.tmpdir
	 *
	 * @param fileName
	 * @param suffix
	 * @return
	 * @throws IOException
	 */
	public static File tempFile(String fileName, String suffix) throws IOException {
		Objects.requireNonNull(fileName, "fileName 不可以为null哦");
		if (suffix != null && suffix.indexOf(".") != 0) {
			suffix = "." + suffix;
		}
		String tempDir = System.getProperty("java.io.tmpdir");
		return Paths.get(tempDir, fileName + suffix).toFile();
	}


	/**
	 * 读取classpath下某个文件，返回File
	 *
	 * @param fileName
	 * @return File
	 */
	public static File readClasspathFileAsFile(String fileName) {
		ClassLoader classLoader = firstNonNull(currentThread().getContextClassLoader(), IOUtils.class.getClassLoader());
		URL url = classLoader.getResource(fileName);
		if (url == null && !fileName.startsWith(DIR_SEPARATOR_UNIX)) {
			log.warn("Cannot find file {} under classpath", fileName);
			url = classLoader.getResource("/" + fileName);
		}
		if (url != null) {
			return new File(url.getFile());
		}

		/*
		 * Java Application中不带目录的时候可以查到
		 */
		List<File> files = Resources.getResources(fileName);
		if (!files.isEmpty()) {
			return files.get(0);
		}

		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource resource = resolver.getResource(fileName);
		if (resource.exists()) {
			try {
				return resource.getFile();
			} catch (IOException e) {
				log.error("", e);
				return null;
			}
		}

		try {
			if (!fileName.startsWith(DIR_SEPARATOR_UNIX)) {
				fileName = CLASSPATH_PREFIX + DIR_SEPARATOR_UNIX + "**" + DIR_SEPARATOR_UNIX + fileName;
			}
			Resource[] resources = resolver.getResources(fileName);
			if (resources.length > 0) {
				return resources[0].getFile();
			}
			return null;
		} catch (IOException e) {
			log.error("", e);
			return null;
		}

	}

}
