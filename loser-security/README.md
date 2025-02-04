# loser-security

# 一 提供的核心类

1. UsernamePasswordAuthenticationFilter

   这个filter在SpringSecurity自带的UsernamePasswordAuthenticationFilter基础上提供先尝试表单提交认证、接着是request body认证

2. XSSFilter

   注册到该bean到Spring容器中即可, 提供了防御跨站脚本攻击的能力

3. Jwts

   根据JWT Token解析Jwt对象出来

4. 