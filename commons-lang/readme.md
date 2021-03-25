公共模块, 大部分通用工具类都在这里, 对其他模块启动一个功能支撑的作用

**下面列举几个常用的类**

# 1.DateUtils

* 字符串转Date类型, 大多数场景日期格式这个方法都支持, 通过正则表达式来判断具体日期格式

  ```java
  public static Date parse(String source)
  ```

* 带时区的版本

  ```java
  public static Date parse(String source, TimeZone timezone)
  ```

* 指定日期格式

  ```java
  public static Date parse(String source, String format)
  ```

* Date转LocalDate

  ```java
  public static LocalDate toLocalDate(Date date)
  ```

* 字符串转LocalDateTime, 根据正则表达式判断具体日期格式, 大多数日期格式都支持

  ```java
  public static LocalDateTime toLocalDateTime(String source)
  ```

* Date转LocalDateTime

  ```java
  public static LocalDateTime toLocalDateTime(Date date)
  ```

* 将Date用指定时区转成LocalDateTime

  ```java
  public static LocalDateTime toLocalDateTime(Date date, ZoneId zoneId)
  ```

* 将Date用+8(东8区 Asia/Shanghai)时区转成LocalDateTime

  ```java
  public static LocalDateTime toLocalDateTimeCTT(Date date)
  ```

# 2.ReflectionUtils

反射工具类

* 通过反射设置字段值

  ```java
  public static void setField(String fieldName, Object target, Object value)
  public static void setField(Field field, Object target, Object value)
  ```

* 获取声明的字段

  ```java
  public static Field[] getDeclaredFields(Class<?> clazz)
  ```

* 拿所有的field, 包括父类的field

  ```java
  public static Field[] getFields(Class<?> clazz)
  ```

* 获取指定的字段

  ```java
  public static Object getField(Object targetObject, String name)
  ```

* 获取字段值

  ```java
  public static <T> T getFieldValue(String fieldName, Object target)
  public static Object getFieldValue(Field field, Object target)
  ```

* 获取方法

  ```java
  public static Method findMethod(Class<?> clazz, String name, Class<?>... paramTypes)
  ```

* 调用对象的方法

  ```java
  public static Object invokeMethod(Method method, Object target)
  public static Object invokeMethod(Method method, Object target, Object... args)
  public static <T> T invokeMethod(Object target, String name, Object... args)    
  ```

* 调用静态方法

  ```java
  public static Object invokeStatic(String className, String methodName)
  ```


# 3.IOUtils

* 从InputStream读取字符串

  ```java
  public static String readFileAsString(InputStream in)
  ```

* 读取文件系统中的文件

  ```java
  public static String readFileAsString(String filePath)
  ```

* 读取classpath下文件内容,文件不存在则返回null

  ```java
  public static String readClassPathFileAsString(String fileName)
  ```

* 将文件读到byte[]中

  ```java
  public static byte[] readFileAsBytes(String filePath)
  public static byte[] readFileAsBytes(Path path)
  public static byte[] readClassPathFileAsBytes(String fileName)
  ```

* 读取文件到InputStream

  ```java
  public static InputStream readFileAsStream(String filePath) throws IOException
  public static InputStream readClasspathFileAsInputStream(String fileName)
  ```

* 字符串写入到文件

  ```java
  public static boolean write(String filePath, String data)
  public static boolean write(String filePath, String data, Charset charset) 
  public static boolean write(Path path, String data)
  ```

* 二进制数据写入文件

  ```java
  public static boolean write(String filePath, byte[] data)
  public static boolean write(Path path, byte[] data)
  public static boolean write(Path path, byte[] data, OpenOption... options)
  ```

* 将content写入临时文件

  ```java
  public static Path writeTempFile(String fileName, String suffix, String content, Charset charset) throws IOException
  ```

* 给定一个Path, 创建这个Path的所有父目录, 返回父目录存在与否的最终状态

  ```java
  public static boolean createParentDir(Path path)
  ```

* 创建目录

  ```java
  public static boolean createDir(Path path)
  ```

* 删除文件

  ```java
  public static boolean deleteFile(Path path)
  ```

* 删除目录

  ```java
  public static boolean deleteDirectory(String path)
  public static boolean deleteDirectory(File path)
  public static boolean deleteDirectory(Path path)
  ```

* 移动文件

  ```java
  public static void move(Path source, Path targetFolder) throws IOException
  public static void move(Path source, Path targetFolder, String renameTo) throws IOException
  ```

  

