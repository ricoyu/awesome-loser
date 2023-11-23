package com.loserico.common.lang.utils;

import com.loserico.common.lang.enums.SizeUnit;
import com.loserico.common.lang.exception.FileCopyException;
import com.loserico.common.lang.exception.IORuntimeException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.MoreObjects.firstNonNull;
import static com.loserico.common.lang.utils.Assert.notNull;
import static java.lang.Thread.currentThread;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.READ;
import static java.text.MessageFormat.format;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.join;

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
public class IOUtils {
	
	private static final Logger log = LoggerFactory.getLogger(IOUtils.class);
	/**
	 * The Unix directory separator character.
	 * 这个不要随便改, jar中的路径是以/分隔的
	 */
	public static final String DIR_SEPARATOR = "/";
	/**
	 * unix    /
	 * windows \
	 */
	//public static final String DIR_SEPARATOR = File.separator;
	// unix / , windows \ 这种方式可以用-Dfile.separator覆盖默认值
	//String separator = System.getProperty("file.separator");
	// unix / , windows \
	//String separator = FileSystems.getDefault().getSeparator();
	// unix / , windows \
	//String separator = File.separator;
	
	/**
	 * The Windows directory separator character.
	 */
	//public static final char DIR_SEPARATOR_WINDOWS = '\\';
	
	public static final String CLASSPATH_PREFIX = "classpath*:";
	
	/**
	 * The default buffer size ({@value}) to use in copy methods.
	 */
	public static final int DEFAULT_BUFFER_SIZE = 8192;
	
	public static final int MIN_BUFFER_SIZE = 1024;
	
	/**
	 * Represents the end-of-file (or stream).
	 */
	public static final int EOF = -1;
	
	/**
	 * 匹配文件名中不允许出现的字符
	 */
	public static final Pattern INVALID_FILENAME_PATTERN =
			Pattern.compile("[`~!@#$%^&*()+=|{}':;',\\\\\\[\\].<>\\/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]");
	
	/**
	 * 从InputStream读取字符串
	 *
	 * @param in
	 * @return String
	 */
	public static String readFileAsString(InputStream in) {
		StringBuilder result = new StringBuilder();
		boolean firstLine = true;
		try (Scanner scanner = new Scanner(in)) {
			while (scanner.hasNextLine()) {
				if (!firstLine) {
					result.append(System.lineSeparator());
				}
				String line = scanner.nextLine();
				result.append(line);
				firstLine = false;
			}
			scanner.close();
		} catch (Exception e) {
			log.warn(e.getMessage());
		}
		return result.toString();
	}
	
	
	/**
	 * 读取文件系统中的文件
	 *
	 * @param filePath
	 * @return String
	 */
	public static String readFileAsString(String filePath) {
		StringBuilder result = new StringBuilder();
		File file = new File(filePath);
		boolean firstLine = true;
		try (Scanner scanner = new Scanner(file)) {
			while (scanner.hasNextLine()) {
				if (!firstLine) {
					result.append(System.lineSeparator());
				}
				String line = scanner.nextLine();
				result.append(line);
				firstLine = false;
			}
			scanner.close();
		} catch (IOException e) {
			log.warn(e.getMessage());
		}
		return result.toString();
	}
	
	/**
	 * 读取文件系统中的文件
	 *
	 * @param file
	 * @return String
	 */
	public static String readFileAsString(File file) {
		if (file == null) {
			return null;
		}
		
		boolean firstLine = true;
		StringBuilder result = new StringBuilder();
		try (Scanner scanner = new Scanner(file)) {
			while (scanner.hasNextLine()) {
				if (!firstLine) {
					result.append(System.lineSeparator());
				}
				String line = scanner.nextLine();
				result.append(line);
				firstLine = false;
			}
			scanner.close();
		} catch (IOException e) {
			log.warn(e.getMessage());
		}
		return result.toString();
	}
	
	/**
	 * 读取classpath下文件内容,文件不存在则返回null PathMatchingResourcePatternResolver
	 *
	 * @param fileName
	 * @return String
	 */
	public static String readClassPathFileAsString(String fileName) {
		InputStream in = readClasspathFileAsInputStream(fileName);
		if (in == null) {
			log.debug("Cannot file {} under classpath", fileName);
			return null;
		}
		return readFileAsString(in);
	}
	
	/**
	 * 读取classpath下指定文件夹下的文件内容,文件不存在则返回null PathMatchingResourcePatternResolver
	 *
	 * @param dir
	 * @param fileName
	 * @return String
	 */
	public static String readClassPathFileAsString(String dir, String fileName) {
		InputStream in = readClasspathFileAsInputStream(dir, fileName);
		if (in == null) {
			log.debug("Cannot file {} under classpath", fileName);
			return null;
		}
		return readFileAsString(in);
	}
	
	public static String readFile(Path path, Charset charset) {
		Objects.requireNonNull(path, "path cannot be null!");
		byte[] bytes = new byte[0];
		try {
			bytes = Files.readAllBytes(path);
		} catch (IOException e) {
			log.error("", e);
			throw new RuntimeException(e);
		}
		
		return new String(bytes, charset);
	}
	
	/**
	 * 读取文件为File对象
	 *
	 * @param dir
	 * @param fileName
	 * @return File
	 */
	public static File readFile(String dir, String fileName) {
		notNull(dir, "dir 不能为null!");
		notNull(fileName, "fileName 不能为null!");
		return Paths.get(dir, fileName).toFile();
	}
	
	/**
	 * 读取文件为File对象
	 *
	 * @param dir
	 * @param fileName
	 * @param suffix
	 * @return File
	 */
	public static File readFile(String dir, String fileName, String suffix) {
		notNull(dir, "dir 不能为null!");
		notNull(fileName, "fileName 不能为null!");
		if (isNotBlank(suffix)) {
			if (suffix.indexOf(".") != 0) {
				suffix = "." + suffix;
			}
			return Paths.get(dir, fileName + suffix).toFile();
		}
		
		return Paths.get(dir, fileName).toFile();
	}
	
	/**
	 * 读取文件为File对象
	 *
	 * @param fullFilename
	 * @return File
	 */
	public static File readFile(String fullFilename) {
		notNull(fullFilename, "fullFilename 不能为null!");
		return Paths.get(fullFilename).toFile();
	}
	
	public static String readFile(Path path) {
		Objects.requireNonNull(path, "path cannot be null!");
		byte[] bytes = new byte[0];
		try {
			bytes = Files.readAllBytes(path);
		} catch (IOException e) {
			log.error("", e);
			throw new RuntimeException(e);
		}
		
		return new String(bytes, UTF_8);
	}
	
	/**
	 * 将文件读到byte[]中
	 *
	 * @param filePath
	 * @return
	 */
	public static byte[] readFileAsBytes(String filePath) {
		Path path = Paths.get(filePath);
		try {
			return Files.readAllBytes(path);
		} catch (IOException e) {
			log.warn(e.getMessage());
		}
		return new byte[0];
	}
	
	public static byte[] readFileAsBytes(Path path) {
		Objects.requireNonNull(path, "path cannot be null!");
		try {
			return Files.readAllBytes(path);
		} catch (IOException e) {
			log.warn(e.getMessage());
		}
		return new byte[0];
	}
	
	public static byte[] readFileAsBytes(File file) {
		requireNonNull(file, "file 不能为null");
		try {
			return Files.readAllBytes(file.toPath());
		} catch (IOException e) {
			log.warn(e.getMessage());
		}
		return new byte[0];
	}
	
	/**
	 * 从指定的offset开始读取指定的字节数
	 *
	 * @param filePath
	 * @param offset      从第几个字节开始读取
	 * @param bytesToRead 读取多少个字节
	 * @return
	 */
	public static byte[] readFileAsBytes(String filePath, int offset, int bytesToRead) {
		requireNonNull(filePath, "file 不能为null");
		Path path = Paths.get(filePath);
		log.info("通过fILE获取文件大小: {} BYTES", path.toFile().length());
		try {
			InputStream inputStream = Files.newInputStream(path);
			log.info("通过InputStream获取文件大小: {} BYTES", inputStream.available());
			RandomAccessFile raf = new RandomAccessFile(path.toFile(), "r");
			raf.seek(offset);
			byte[] buffer = new byte[bytesToRead];
			int len = 0;
			while (-1 != (len = raf.read(buffer))) {
				
			}
			return buffer;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 将多个块合并成一个文件
	 *
	 * @param destFile
	 * @param blocks
	 */
	public static void merge(String destFile, String... blocks) {
		int len = 0;
		byte[] buffer = new byte[1024];
		InputStream is = null;
		OutputStream bos = null;
		for (int i = 0; i < blocks.length; i++) {
			try {
				bos = new BufferedOutputStream(new FileOutputStream(new File(destFile), true));
				is = new BufferedInputStream(new FileInputStream(new File(blocks[i])));
				while (-1 != (len = is.read(buffer))) {
					bos.write(buffer, 0, len);
				}
				bos.flush();
				bos.close();
				is.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	/**
	 * 将classpath的文件读到byte[]中, classpath中的文件只能通过InputStream操作, 不能通过File对象来操作
	 * 因为打成jar包后, 是读不到jar包中classpath下的某个文件的, 记住一定要通过流来操作
	 *
	 * @param fileName
	 * @return byte[]
	 */
	public static byte[] readClassPathFileAsBytes(String fileName) {
		InputStream in = readClasspathFileAsInputStream(fileName);
		return toByteArray(in);
	}
	
	
	public static File readInputStreamAsFile(InputStream in) throws IOException {
		final File tempFile = File.createTempFile(RandomStringUtils.randomAlphanumeric(16), "tmp");
		tempFile.deleteOnExit();
		try (FileOutputStream out = new FileOutputStream(tempFile)) {
			IOUtils.copy(in, out);
		}
		return tempFile;
	}
	
	/**
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static InputStream readFileAsStream(String filePath) throws IOException {
		return Files.newInputStream(Paths.get(filePath), READ);
	}
	
	/**
	 * 读取classpath下某个文件夹下的某个文件，返回InputStream
	 *
	 * @param dir
	 * @param fileName
	 * @return
	 */
	public static InputStream readClasspathFileAsInputStream(String dir, String fileName) {
		if (isBlank(dir)) {
			return readClasspathFileAsInputStream(fileName);
		}
		
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		try {
			if (!fileName.startsWith(DIR_SEPARATOR)) {
				fileName = CLASSPATH_PREFIX + DIR_SEPARATOR + dir + DIR_SEPARATOR + "**" + DIR_SEPARATOR + fileName;
			}
			Resource[] resources = resolver.getResources(fileName);
			if (resources.length > 0) {
				return resources[0].getInputStream();
			}
		} catch (IOException e) {
			log.warn(e.getMessage());
			return null;
		}
		
		return null;
	}
	
	/**
	 * 读取classpath下某个文件，返回InputStream
	 *
	 * @param fileName
	 * @return
	 */
	public static InputStream readClasspathFileAsInputStream(String fileName) {
		if (isBlank(fileName)) {
			return null;
		}
		
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		if (fileName.startsWith("classpath")) {
			Resource resource = resolver.getResource(fileName);
			if (resource.exists()) {
				try {
					return resource.getInputStream();
				} catch (IOException e) {
					log.warn(e.getMessage());
					return null;
				}
			}
		}
		
		/*
		 * 先读classpath根目录
		 */
		List<File> files = Resources.getResources(fileName);
		if (!files.isEmpty()) {
			try {
				return new FileInputStream(files.get(0));
			} catch (FileNotFoundException e) {
				log.warn(e.getMessage());
				return null;
			}
		}
		
		ClassLoader classLoader = firstNonNull(currentThread().getContextClassLoader(), IOUtils.class.getClassLoader());
		URL url = classLoader.getResource(fileName);
		if (url == null && !fileName.startsWith(DIR_SEPARATOR)) {
			log.debug("Cannot find file {} under classpath", fileName);
			url = classLoader.getResource("/" + fileName);
		}
		if (url != null) {
			try {
				return url.openStream();
			} catch (IOException e) {
				log.warn(e.getMessage());
				return null;
			}
		}
		
		try {
			if (!fileName.startsWith(DIR_SEPARATOR)) {
				fileName = CLASSPATH_PREFIX + DIR_SEPARATOR + "**" + DIR_SEPARATOR + fileName;
			}
			Resource[] resources = resolver.getResources(fileName);
			if (resources.length > 0) {
				return resources[0].getInputStream();
			}
		} catch (IOException e) {
			log.warn(e.getMessage());
			return null;
		}
		
		return null;
	}
	
	public static List<String> readLines(String filePath) {
		List<String> lines = new ArrayList<String>();
		File file = new File(filePath);
		try (Scanner scanner = new Scanner(file)) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				lines.add(line);
			}
			scanner.close();
		} catch (IOException e) {
			log.warn(e.getMessage());
		}
		return lines;
	}
	
	public static List<String> readLines(InputStream in) {
		List<String> lines = new ArrayList<String>();
		try (BufferedInputStream bufferedInputStream = new BufferedInputStream(in);
		     Scanner scanner = new Scanner(bufferedInputStream)) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				lines.add(line);
			}
			scanner.close();
		} catch (IOException e) {
			log.warn(e.getMessage());
		}
		return lines;
	}
	
	/**
	 * 持续从命令行读取数据并交给consumer, 收到exit或者quit退出
	 *
	 * @param consumer
	 */
	public static void readCommandLine(Consumer<String> consumer) {
		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));) {
			
			while (true) {
				String command = bufferedReader.readLine();
				if ("quit".equalsIgnoreCase(command) || "exit".equalsIgnoreCase(command)) {
					break;
				}
				consumer.accept(command);
			}
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		
	}
	
	/**
	 * 从InputStream读取字符串
	 *
	 * @param in
	 * @return String
	 */
	public static String readAsString(InputStream in) {
		return readAsString(in, true);
	}
	
	/**
	 * 从InputStream读取字符串
	 *
	 * @param in
	 * @param autoClose
	 * @return String
	 */
	public static String readAsString(InputStream in, boolean autoClose) {
		List<String> lines = new ArrayList<String>();
		try (BufferedInputStream bufferedInputStream = new BufferedInputStream(in);
		     Scanner scanner = new Scanner(bufferedInputStream)) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				lines.add(line);
			}
			scanner.close();
			if (autoClose) {
				in.close();
			}
		} catch (IOException e) {
			log.warn(e.getMessage());
		}
		return join(lines, System.lineSeparator());
	}
	
	
	/**
	 * 读取classpath下某个文件, 返回File <p/>
	 * 会先读classpath root, 找不到继续从classpath下的jar里面找, 可能会比较慢
	 *
	 * @param fileName
	 * @return File
	 */
	public static File readClasspathFileAsFile(String fileName) {
		/*
		 * 先读classpath根目录
		 */
		List<File> files = Resources.getResources(fileName);
		if (!files.isEmpty()) {
			return files.get(0);
		}
		
		ClassLoader classLoader = firstNonNull(currentThread().getContextClassLoader(), IOUtils.class.getClassLoader());
		URL url = classLoader.getResource(fileName);
		if (url == null && !fileName.startsWith(DIR_SEPARATOR)) {
			log.debug("Cannot find file {} under classpath", fileName);
			url = classLoader.getResource("/" + fileName);
		}
		if (url != null) {
			return new File(url.getFile());
		}
		
		
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource resource = resolver.getResource(fileName);
		if (resource.exists()) {
			try {
				return resource.getFile();
			} catch (IOException e) {
				log.warn(e.getMessage());
				return null;
			}
		}
		
		try {
			if (!fileName.startsWith(DIR_SEPARATOR)) {
				fileName = CLASSPATH_PREFIX + DIR_SEPARATOR + "**" + DIR_SEPARATOR + fileName;
			}
			Resource[] resources = resolver.getResources(fileName);
			if (resources.length > 0) {
				return resources[0].getFile();
			}
			return null;
		} catch (IOException e) {
			log.warn(e.getMessage());
			return null;
		}
		
	}
	
	
	/**
	 * Write string data to file
	 *
	 * @param filePath
	 * @param data
	 * @return
	 */
	public static boolean write(String filePath, String data) {
		Objects.requireNonNull(filePath, "filePath cannot be null!");
		Path path = Paths.get(filePath);
		return write(path, data);
	}
	
	public static boolean write(String filePath, String data, Charset charset) {
		Objects.requireNonNull(filePath, "filePath cannot be null!");
		Path path = Paths.get(filePath);
		return write(path, data, charset);
	}
	
	/**
	 * Write string data to file
	 *
	 * @param path
	 * @param data
	 * @return
	 */
	public static boolean write(Path path, String data) {
		Objects.requireNonNull(path, "path cannot be null!");
		return write(path, Optional.of(data).orElse("").getBytes(UTF_8), CREATE, APPEND);
	}
	
	/**
	 * 用指定的编码格式写文件
	 *
	 * @param path
	 * @param data
	 * @param charset
	 * @return
	 */
	public static boolean write(Path path, String data, Charset charset) {
		Objects.requireNonNull(path, "path cannot be null!");
		return write(path, Optional.of(data).orElse("").getBytes(charset), CREATE, APPEND);
	}
	
	public static boolean write(String filePath, byte[] data) {
		Objects.requireNonNull(filePath, "filePath cannot be null!");
		Path path = Paths.get(filePath);
		return write(path, data);
	}
	
	public static boolean write(Path path, byte[] data) {
		Objects.requireNonNull(path, "path cannot be null!");
		return write(path, data, CREATE, APPEND);
	}
	
	/**
	 * Write byte[] data to file
	 *
	 * @param path
	 * @param data
	 * @param options
	 * @return
	 */
	public static boolean write(Path path, byte[] data, OpenOption... options) {
		Objects.requireNonNull(path, "path cannot be null!");
		createParentDir(path);
		try {
			Files.write(path, data, options);
			return true;
		} catch (IOException e) {
			log.warn(format("Write data [{0}] to path [{1}] failed!", data, path), e);
		}
		return false;
	}
	
	/**
	 * 将content写入临时文件
	 *
	 * @param fileName
	 * @param suffix
	 * @param content
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static Path writeTempFile(String fileName, String suffix, String content,
	                                 Charset charset) throws IOException {
		Path path = tempFile(fileName, suffix).toPath();
		write(path, content, charset);
		return path;
	}
	
	/**
	 * @param path
	 * @return boolean
	 * @of Create parent directory if this path has a parent path and if not exist yet
	 * 不管是否执行了父目录的创建，返回父目录存在与否的最终状态
	 * true 存在
	 * false 不存在
	 */
	public static boolean createParentDir(Path path) {
		Optional.of(path.getParent())
				.ifPresent(parent -> {
					if (!Files.exists(parent, NOFOLLOW_LINKS)) {
						try {
							Files.createDirectories(parent);
						} catch (IOException e) {
							log.warn(format("create parent directory [{0}] failed", parent), e);
						}
					}
				});
		return Files.exists(path.getParent(), NOFOLLOW_LINKS);
	}
	
	public static boolean createDir(Path path) {
		Optional.of(path)
				.ifPresent(dir -> {
					if (!Files.exists(dir, NOFOLLOW_LINKS)) {
						try {
							Files.createDirectories(dir);
						} catch (IOException e) {
							log.warn(format("create directory [{0}] failed", dir), e);
						}
					}
				});
		return Files.exists(path, NOFOLLOW_LINKS);
	}
	
	/**
	 * Delete a file if exists
	 *
	 * @param path
	 * @return
	 */
	public static boolean deleteFile(Path path) {
		Objects.requireNonNull(path, "path cannot be null!");
		try {
			Files.deleteIfExists(path);
		} catch (IOException e) {
			log.warn(format("Delete file {0} failed", path), e);
		}
		return true;
	}
	
	/**
	 * Delete specified directory with its sub-dir and all of tis files
	 *
	 * @param path
	 * @return
	 */
	public static boolean deleteDirectory(String path) {
		Path directory = Paths.get(path);
		return deleteDirectory(directory);
	}
	
	public static boolean deleteDirectory(File path) {
		Path directory = path.toPath();
		return deleteDirectory(directory);
	}
	
	/**
	 * Delete specified directory with its sub-dir
	 *
	 * @param path
	 * @return boolean 删除成功与否
	 */
	public static boolean deleteDirectory(Path path) {
		if (!Files.isDirectory(path, NOFOLLOW_LINKS)) {
			log.error("{} is not a directory!", path);
			return false;
		}
		try {
			Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					Files.delete(file);
					return FileVisitResult.CONTINUE;
				}
				
				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
					Files.delete(dir);
					return FileVisitResult.CONTINUE;
				}
			});
			return true;
		} catch (IOException e) {
			log.warn("Delete path " + path + " failed", e);
			return false;
		}
	}
	
	/**
	 * 将source文件移动到targetFolder
	 *
	 * @param source
	 * @param targetFolder
	 * @return
	 */
	public static void move(Path source, Path targetFolder) throws IOException {
		move(source, targetFolder, null);
	}
	
	/**
	 * 将source文件移动到targetFolder,并重命名为renameTo
	 *
	 * @param source
	 * @param targetFolder
	 * @param renameTo
	 * @return
	 */
	public static void move(Path source, Path targetFolder, String renameTo) throws IOException {
		if (Files.notExists(source, NOFOLLOW_LINKS)) {
			return;
		}
		
		if (Files.notExists(targetFolder, NOFOLLOW_LINKS)) {
			try {
				Files.createDirectories(targetFolder);
			} catch (Throwable e) {
				String msg = format("Create directory[{0}] failed", targetFolder.toString());
				log.error(msg, e);
				throw new IOException(msg, e);
			}
		}
		try {
			Path targetFile = null;
			if (isNotBlank(renameTo)) {
				targetFile = targetFolder.resolve(renameTo);
			} else {
				targetFile = targetFolder.resolve(source.getFileName());
			}
			Files.move(source, targetFile, REPLACE_EXISTING);
		} catch (Throwable e) {
			String msg = format("Move file[{0}] to [{1}] failed.", source, targetFolder);
			log.error(msg, e);
			throw new IOException(msg, e);
		}
	}
	
	
	/**
	 * 获取根目录
	 *
	 * @param path
	 * @return
	 */
	public static Path getRootDirectory(Path path) {
		return Optional.ofNullable(path.getRoot())
				.map(root -> {
					StringBuilder pathStr = new StringBuilder();
					pathStr.append(root.toString());
					Optional.of(path.subpath(0, 1)).ifPresent(sub -> pathStr.append(sub));
					return Paths.get(pathStr.toString());
				}).orElseGet(() -> path.subpath(0, 1));
	}
	
	/**
	 * 将path代表的文件写入OutputStream
	 *
	 * @param path
	 * @param out
	 * @throws IOException
	 */
	public static void copy(Path path, final OutputStream out) throws IOException {
		InputStream inputStream = Files.newInputStream(path, READ);
		final byte[] buf = new byte[2048];
		int len;
		while ((len = inputStream.read(buf)) != -1) {
			out.write(buf, 0, len);
		}
		inputStream.close();
	}
	
/*	public static void copy(final InputStream in, final OutputStream out) throws IOException {
		final byte[] buf = new byte[2048];
		int len;
		while ((len = in.read(buf)) != -1) {
			out.write(buf, 0, len);
		}
	}*/
	
	
	/**
	 * Copy one file to another place
	 */
	public static boolean copy(Path copyFrom, Path copyTo, CopyOption... options) {
		boolean parentCreateResult = createParentDir(copyTo);
		if (parentCreateResult) {
			try (InputStream is = new FileInputStream(copyFrom.toFile())) {
				Files.copy(is, copyTo, options);
				return true;
			} catch (IOException e) {
				log.warn(format("copy from [{0}] to [{1}] failed!", copyFrom, copyTo), e);
			}
		}
		return false;
	}
	
	/**
	 * 通过NIO方式拷贝数据，每读取一部分数据就立刻写入输出流
	 *
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	public static void copyPositive(InputStream in, OutputStream out) throws IOException {
		ReadableByteChannel inChannel = Channels.newChannel(in);
		WritableByteChannel outChannel = Channels.newChannel(out);
		
		ByteBuffer buffer = ByteBuffer.allocate(8192);
		int read;
		
		while ((read = inChannel.read(buffer)) > 0) {
			buffer.rewind();
			buffer.limit(read);
			
			while (read > 0) {
				read -= outChannel.write(buffer);
			}
			
			buffer.clear();
		}
	}
	
	public static void copyNegative(InputStream in, OutputStream out) throws IOException {
		ReadableByteChannel inChannel = Channels.newChannel(in);
		WritableByteChannel outChannel = Channels.newChannel(out);
		
		ByteBuffer buffer = ByteBuffer.allocate(8192);
		while (inChannel.read(buffer) != -1) {
			buffer.flip();
			outChannel.write(buffer);
			buffer.compact();
		}
		
		buffer.flip();
		while (buffer.hasRemaining()) {
			outChannel.write(buffer);
		}
		inChannel.close();
		outChannel.close();
	}
	
	/**
	 * 将数据从 InputStream 拷贝到 OutputStream，最后两个都关闭
	 *
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	public static void copyAndClose(final InputStream in, final OutputStream out) throws IOException {
		try {
			copy(in, out);
			in.close();
			out.close();
		} catch (IOException ex) {
			closeSilently(in);
			closeSilently(out);
			throw ex;
		}
	}
	
	/**
	 * 保留文件的后缀，将文件名前缀替换为随机字符串+日期
	 *
	 * @param fileName
	 * @return String 随机生成的文件名
	 */
	public static String randomFileName(String fileName) {
		if (isBlank(fileName)) {
			return "";
		}
		int dotIndex = fileName.lastIndexOf(".");
		String suffix = dotIndex == -1 ? "" : fileName.substring(dotIndex);
		String baseName = RandomStringUtils.randomAlphanumeric(16);
		String timeSuffix = LocalDateTime.now().format(ofPattern("yyyyMMddHHmmss"));
		return String.join("", baseName, timeSuffix, suffix);
	}
	
	public static void closeSilently(final Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (IOException e) {
			log.warn(e.getMessage());
		}
	}
	
	public static BufferedReader toBufferedReader(InputStream in) {
		return new BufferedReader(new InputStreamReader(in, UTF_8));
	}
	
	public static BufferedReader toBufferedReader(InputStream in, Charset charset) {
		return new BufferedReader(new InputStreamReader(in, charset));
	}
	
	public static ByteArrayInputStream toByteArrayInputStream(File file) throws IOException {
		return new ByteArrayInputStream(Files.readAllBytes(file.toPath()));
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
	 * 获取文件大小 如果path代表一个目录，获取目录中所有文件大小之和
	 *
	 * @param path
	 * @return
	 */
	public static long length(Path path) {
		return FileUtils.sizeOf(path.toFile());
	}
	
	
	/**
	 * Gets the contents of an <code>InputStream</code> as a <code>byte[]</code>.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 *
	 * @param input the <code>InputStream</code> to read from
	 * @return the requested byte array
	 * @throws NullPointerException if the input is null
	 * @throws IOException          if an I/O error occurs
	 */
	public static byte[] toByteArray(final InputStream input) {
		if (input == null) {
			return new byte[0];
		}
		byte[] initialBuffer = new byte[MIN_BUFFER_SIZE];
		int read = 0;
		try {
			read = input.read(initialBuffer);
		} catch (IOException e) {
			log.warn(e.getMessage());
			throw new IORuntimeException(e);
		}
		
		//如果input已经结束, 或者发送的数据小于MIN_BUFFER_SIZE, 那么一次就读完了, 不需要再次读取
		if (read == -1) {
			return new byte[0];
		}
		if (read < initialBuffer.length) {
			byte[] bytes = new byte[read];
			System.arraycopy(initialBuffer, 0, bytes, 0, read);
			return bytes;
		}
		
		//一次读不完, 那么多次读取, 累积放入ByteArrayOutputStream, 最后返回byte[]
		try (final ByteArrayOutputStream output = new ByteArrayOutputStream()) {
			output.write(initialBuffer, 0, initialBuffer.length);
			copy(input, output);
			return output.toByteArray();
		} catch (IOException e) {
			log.warn(e.getMessage());
			throw new IORuntimeException(e);
		}
	}
	
	/**
	 * 从channel中读取数据
	 *
	 * @param channel
	 * @return byte[]
	 * @throws IOException
	 */
	public static byte[] toByteArray(final ByteChannel channel) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(MIN_BUFFER_SIZE);
		int read = channel.read(buffer);
		
		//如果input已经结束, 不需要再次读取
		if (read == -1) {
			return new byte[0];
		}
		//发送的数据小于MIN_BUFFER_SIZE, 那么一次就读完了, 不需要再次读取
		if (buffer.hasRemaining()) {
			return buffer.array();
		}
		
		//一次读不完, 那么多次读取, 累积放入ByteArrayOutputStream, 最后返回byte[]
		try (final ByteArrayOutputStream output = new ByteArrayOutputStream(DEFAULT_BUFFER_SIZE * 4)) {
			//先把上面第一次读取的放入output
			output.write(buffer.array(), 0, buffer.capacity());
			
			//1024 bytes一次读不完, 那么一次分配大一点的buffer
			buffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
			//只要读到了数据就往output里面写
			while ((read = channel.read(buffer)) != -1) {
				output.write(buffer.array(), 0, read);
				//清空buffer, 为继续写入buffer做准备
				buffer.clear();
			}
			
			return output.toByteArray();
		}
	}
	
	/**
	 * Copies bytes from an <code>InputStream</code> to an
	 * <code>OutputStream</code>.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 * <p>
	 * Large streams (over 2GB) will return a bytes copied value of
	 * <code>-1</code> after the copy has completed since the correct
	 * number of bytes cannot be returned as an int. For large streams
	 * use the <code>copyLarge(InputStream, OutputStream)</code> method.
	 *
	 * @param input  the <code>InputStream</code> to read from
	 * @param output the <code>OutputStream</code> to write to
	 * @return the number of bytes copied, or -1 if &gt; Integer.MAX_VALUE
	 * @throws NullPointerException if the input or output is null
	 * @throws IOException          if an I/O error occurs
	 * @since 1.1
	 */
	public static int copy(final InputStream input, final OutputStream output) throws IOException {
		final long count = copyLarge(input, output);
		if (count > Integer.MAX_VALUE) {
			return -1;
		}
		return (int) count;
	}
	
	/**
	 * Copies bytes from an <code>InputStream</code> to an <code>OutputStream</code> using an internal buffer of the
	 * given size.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a <code>BufferedInputStream</code>.
	 * <p>
	 *
	 * @param input      the <code>InputStream</code> to read from
	 * @param output     the <code>OutputStream</code> to write to
	 * @param bufferSize the bufferSize used to copy from the input to the output
	 * @return the number of bytes copied
	 * @throws NullPointerException if the input or output is null
	 * @throws IOException          if an I/O error occurs
	 * @since 2.5
	 */
	public static long copy(final InputStream input, final OutputStream output, final int bufferSize)
			throws IOException {
		return copyLarge(input, output, new byte[bufferSize]);
	}
	
	/**
	 * Copies bytes from a large (over 2GB) <code>InputStream</code> to an
	 * <code>OutputStream</code>.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 * <p>
	 * The buffer size is given by {@link #DEFAULT_BUFFER_SIZE}.
	 *
	 * @param input  the <code>InputStream</code> to read from
	 * @param output the <code>OutputStream</code> to write to
	 * @return the number of bytes copied
	 * @throws NullPointerException if the input or output is null
	 * @throws IOException          if an I/O error occurs
	 * @since 1.3
	 */
	public static long copyLarge(final InputStream input, final OutputStream output)
			throws IOException {
		return copy(input, output, DEFAULT_BUFFER_SIZE);
	}
	
	
	/**
	 * Copies bytes from a large (over 2GB) <code>InputStream</code> to an
	 * <code>OutputStream</code>.
	 * <p>
	 * This method uses the provided buffer, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 * <p>
	 *
	 * @param input  the <code>InputStream</code> to read from
	 * @param output the <code>OutputStream</code> to write to
	 * @param buffer the buffer to use for the copy
	 * @return the number of bytes copied
	 * @throws NullPointerException if the input or output is null
	 * @throws IOException          if an I/O error occurs
	 * @since 2.2
	 */
	public static long copyLarge(final InputStream input, final OutputStream output, final byte[] buffer)
			throws IOException {
		long count = 0;
		int n;
		while (EOF != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}
	
	/**
	 * 列出指定目录下所有普通文件
	 *
	 * @param dir
	 * @return List<String>
	 */
	public static List<String> listFileNames(String dir) {
		List<String> fileList = new ArrayList<>();
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dir))) {
			for (Path path : stream) {
				if (!Files.isDirectory(path)) {
					fileList.add(path.getFileName().toString());
				}
			}
		} catch (IOException e) {
			log.warn(e.getMessage());
			throw new RuntimeException(e);
		}
		return fileList;
	}
	
	/**
	 * 返回指定目录下所有普通文件对象
	 *
	 * @param dir
	 * @return List<File>
	 */
	public static List<File> listFiles(String dir) {
		List<File> fileList = new ArrayList<>();
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dir))) {
			for (Path path : stream) {
				if (!Files.isDirectory(path)) {
					fileList.add(path.toFile());
				}
			}
		} catch (IOException e) {
			log.warn(e.getMessage());
			throw new RuntimeException(e);
		}
		return fileList;
	}
	
	/**
	 * 从指定目录开始, 递归列出所有文件以及子目录下的文件
	 *
	 * @param dir
	 * @param depth
	 * @return List<String>
	 */
	public static List<String> listFileNames(String dir, int depth) {
		try {
			try (Stream<Path> stream = Files.walk(Paths.get(dir), depth)) {
				return stream.filter(path -> !Files.isDirectory(path))
						.map(Path::getFileName)
						.map(Path::toString)
						.collect(Collectors.toList());
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 从指定目录开始, 递归列出所有文件以及子目录下的文件
	 *
	 * @param dir
	 * @param depth
	 * @return List<File>
	 */
	public static List<File> listFiles(String dir, int depth) {
		try {
			try (Stream<Path> stream = Files.walk(Paths.get(dir), depth)) {
				return stream.filter(path -> !Files.isDirectory(path))
						.map(Path::getFileName)
						.map(Path::toFile)
						.collect(Collectors.toList());
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 列出classpath下指定目录及其子目录中的所有文件
	 *
	 * @param classpathDir
	 * @return List<String>
	 */
	public static List<String> listClasspathFileNames(String classpathDir) {
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		if (!classpathDir.startsWith(DIR_SEPARATOR)) {
			classpathDir =
					CLASSPATH_PREFIX + DIR_SEPARATOR + classpathDir + DIR_SEPARATOR + "**" + DIR_SEPARATOR + "*.*";
		} else {
			classpathDir = CLASSPATH_PREFIX + classpathDir + DIR_SEPARATOR + "**" + DIR_SEPARATOR + "*.*";
		}
		try {
			Resource[] resources = resolver.getResources(classpathDir);
			List<String> fileNames = Arrays.asList(resources)
					.stream()
					.map(Resource::getFilename)
					.collect(toList());
			return fileNames;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 列出classpath下指定目录及其子目录中的所有文件
	 *
	 * @param classpathDir
	 * @param filePattern
	 * @return List<String>
	 */
	public static List<String> listClasspathFileNames(String classpathDir, String filePattern) {
		filePattern = (isBlank(filePattern) ? "*.*" : filePattern);
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		if (!classpathDir.startsWith(DIR_SEPARATOR)) {
			classpathDir =
					CLASSPATH_PREFIX + DIR_SEPARATOR + classpathDir + DIR_SEPARATOR + "**" + DIR_SEPARATOR + filePattern;
		} else {
			classpathDir = CLASSPATH_PREFIX + classpathDir + DIR_SEPARATOR + "**" + DIR_SEPARATOR + filePattern;
		}
		try {
			Resource[] resources = resolver.getResources(classpathDir);
			List<String> fileNames = Arrays.asList(resources)
					.stream()
					.map(Resource::getFilename)
					.collect(toList());
			return fileNames;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 返回文件的后缀, 包含前面的.号
	 *
	 * @param file
	 * @return String
	 */
	public static String suffix(File file) {
		if (file == null) {
			return null;
		}
		
		String fileName = file.getName();
		return suffix(fileName);
	}
	
	/**
	 * 返回文件的后缀, 包含前面的.号
	 *
	 * @param fileName
	 * @return String
	 */
	public static String suffix(String fileName) {
		if (isBlank(fileName)) {
			return null;
		}
		
		int index = fileName.lastIndexOf(".");
		if (index != -1) {
			return fileName.substring(index);
		}
		
		return null;
	}
	
	/**
	 * 检查文件名是否出现了不允许出现的特殊字符
	 *
	 * @param filename
	 * @return
	 */
	public static boolean isInvalidFilename(String filename) {
		if (isBlank(filename)) {
			return false;
		}
		
		return INVALID_FILENAME_PATTERN.matcher(filename).matches();
	}
	
	/**
	 * 检查文件大小始否超过阈值
	 *
	 * @param data 文件的byte数组
	 * @param size 限制文件大小不能拆过size
	 * @param unit 单位, gb, g, mb, m, kb, k,
	 * @return boolean
	 */
	public static boolean isExceedLimitSize(byte[] data, long size, String unit) {
		requireNonNull(data, "data cannot be null!");
		SizeUnit sizeUnit = SizeUnit.parse(unit);
		if (sizeUnit == null) {
			return false;
		}
		
		long limitBytes = sizeUnit.toBytes(size);
		return data.length > limitBytes;
	}
	
	/**
	 * 检查文件大小始否超过阈值
	 *
	 * @param file 文件
	 * @param size 限制文件大小不能拆过size
	 * @param unit 单位, gb, g, mb, m, kb, k,
	 * @return boolean
	 */
	public static boolean isExceedLimitSize(File file, long size, String unit) {
		requireNonNull(file, "data cannot be null!");
		if (!file.exists()) {
			return false;
		}
		
		byte[] data;
		try {
			data = Files.readAllBytes(file.toPath());
		} catch (IOException e) {
			log.error("", e);
			throw new IORuntimeException(e);
		}
		SizeUnit sizeUnit = SizeUnit.parse(unit);
		if (sizeUnit == null) {
			return false;
		}
		
		long limitBytes = sizeUnit.toBytes(size);
		return data.length > limitBytes;
	}
	
	/**
	 * 检查文件大小始否超过阈值
	 *
	 * @param fileSize 文件大小(bytes数)
	 * @param size     限制文件大小不能拆过size
	 * @param unit     单位, gb, g, mb, m, kb, k,
	 * @return boolean
	 */
	public static boolean isExceedLimitSize(long fileSize, long size, String unit) {
		SizeUnit sizeUnit = SizeUnit.parse(unit);
		if (sizeUnit == null) {
			return false;
		}
		
		long limitBytes = sizeUnit.toBytes(size);
		return fileSize > limitBytes;
	}
	
	/**
	 * 检查文件大小在给定的范围内
	 *
	 * @param data           文件的byte数组
	 * @param lowerBound     文件大小下限, 必须>=lowerBound
	 * @param lowerBoundUnit 单位, k kb m mb g gb
	 * @param upperBound     文件大小上限, 必须<=lowerBound
	 * @param upperBoundUnit 单位, k kb m mb g gb
	 * @return boolean
	 */
	public static boolean isBetweenLimitSize(byte[] data, long lowerBound, String lowerBoundUnit, long upperBound,
	                                         String upperBoundUnit) {
		requireNonNull(data, "data cannot be null!");
		return isBetweenLimitSize(data.length, lowerBound, lowerBoundUnit, upperBound, upperBoundUnit);
	}
	
	/**
	 * 检查文件大小始否超过阈值
	 *
	 * @param file           文件
	 * @param lowerBound     文件大小下限, 必须>=lowerBound
	 * @param lowerBoundUnit 单位, k kb m mb g gb
	 * @param upperBound     文件大小上限, 必须<=lowerBound
	 * @param upperBoundUnit 单位, k kb m mb g gb
	 * @return boolean
	 */
	public static boolean isBetweenLimitSize(File file, long lowerBound, String lowerBoundUnit, long upperBound,
	                                         String upperBoundUnit) {
		requireNonNull(file, "data cannot be null!");
		if (!file.exists()) {
			return false;
		}
		
		byte[] data;
		try {
			data = Files.readAllBytes(file.toPath());
		} catch (IOException e) {
			log.error("", e);
			throw new IORuntimeException(e);
		}
		
		return isBetweenLimitSize(data.length, lowerBound, lowerBoundUnit, upperBound, upperBoundUnit);
	}
	
	/**
	 * 检查文件大小始否超过阈值
	 *
	 * @param fileSize       文件大小(bytes数)
	 * @param lowerBound     文件大小下限, 必须>=lowerBound
	 * @param lowerBoundUnit 单位, k kb m mb g gb
	 * @param upperBound     文件大小上限, 必须<=lowerBound
	 * @param upperBoundUnit 单位, k kb m mb g gb
	 * @return boolean
	 */
	public static boolean isBetweenLimitSize(long fileSize, long lowerBound, String lowerBoundUnit, long upperBound,
	                                         String upperBoundUnit) {
		SizeUnit lowerBoundSizeUnit = SizeUnit.parse(lowerBoundUnit);
		
		long lowerBoundBytes = lowerBoundSizeUnit.toBytes(lowerBound);
		if (fileSize < lowerBoundBytes) {
			return false;
		}
		
		SizeUnit upperBoundSizeUnit = SizeUnit.parse(upperBoundUnit);
		long upperBoundBytes = lowerBoundSizeUnit.toBytes(upperBound);
		if (fileSize > upperBoundBytes) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * 拷贝文件到临时目录下, 如果sourceFileName在磁盘上存在, 先读磁盘上; 如果在磁盘上不存在, 尝试读Jar文件中的
	 *
	 * @param sourceFileName
	 * @return Path 拷贝后得到的文件
	 */
	public static Path fileCopy(String sourceFileName) {
		Objects.requireNonNull(sourceFileName);
		Path sourcePath = Paths.get(sourceFileName);
		if (sourcePath.toFile().exists()) {
			return fileCopy(sourcePath);
		}
		
		InputStream inputStream = readClasspathFileAsInputStream(sourceFileName);
		return fileCopy(inputStream, FilenameUtils.getExtension(sourceFileName));
	}
	
	/**
	 * 拷贝一个磁盘上的文件到临时目录下, classpath下的文件本方法不适用
	 *
	 * @param sourcePath
	 * @return Path
	 */
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
	 * 拷贝一个InputStream代表的文件到临时目录下
	 *
	 * @param in
	 * @param suffix
	 * @return Path
	 */
	public static Path fileCopy(InputStream in, String suffix) {
		Objects.requireNonNull(in);
		try {
			Path destPath = tempFile(suffix).toPath();
			Files.copy(in, destPath, REPLACE_EXISTING);
			return destPath;
		} catch (IOException e) {
			log.error("Failed to copy file ");
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
	 * 从命令行不断读取用户输入
	 *
	 * @param consumer
	 */
	public static void readUserInput(Consumer<String> consumer) {
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.print("请输入命令（输入'exit'以退出）: ");
			String input = sc.nextLine();
			if ("exit".equals(input)) {
				break;
			}
			consumer.accept(input);
		}
	}
	
	/**
	 * 从命令行读取一次用户输入
	 *
	 * @param inputPrompt 输入提示
	 * @return String 用户输入的内容
	 */
	public static String readUserInputOnce(String inputPrompt) {
		//Scanner用完不要关闭, sc.close()实际关闭的是System.in, 所以即使每次方法里面重新new Scanner也没用, 不能再重新读取了
		Scanner sc = new Scanner(System.in);
		System.out.print(inputPrompt);
		return sc.nextLine();
	}
	
	public static void slientUserInput(String prompt, Consumer<String> consumer) {
		new Thread(() -> {
			Scanner sc = new Scanner(System.in);
			while (true) {
				System.out.print(prompt);
				String input = sc.nextLine();
				if ("exit".equals(input)) {
					break;
				}
				consumer.accept(input);
			}
		}, "等待用户输入线程").start();
	}
}
