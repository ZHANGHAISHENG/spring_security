 
 token获取：
 -- 自定义filter
 curl 127.0.0.1:10030/form/token -X POST -d 'username=zhs'  -d 'password=123456' -H 'clientId:c1' -H 'clientSecret:123456'
 -- 内部controller
 curl 127.0.0.1:10030/oauth/token?client_id=c1\&client_secret=123456\&grant_type=password\&username=zhs\&password=123456

 token刷新：
 -- 自定义controller
 curl 127.0.0.1:10030/token/refresh -X POST  -H 'clientId:c1' -d 'refreshToken=d9478ae0-bb07-47de-b1d5-b55200da0410'
 -- 内部controller
 curl 127.0.0.1:8081/oauth/token?client_id=c1\&client_secret=123456\&grant_type=refresh_token\&refresh_token=5709e974-6d43-47e3-9a30-9d7e3c495ad0

 退出登录：
 curl 127.0.0.1:10030/loginOutTest -X POST -H 'token:586523f1-f3c1-4867-a2c1-b2daf71bb71e'
 curl 127.0.0.1:10030/logout -X GET -H 'token:d786d3dc-beeb-4d44-9a04-4ac6ad524570'
 
 访问需要登录的资源：
 -- 自定义授权filter
 curl 127.0.0.1:10030/test2 -X GET -H 'token:0dc17c0d-7737-4f8e-8973-59a431b7dd4a'
 -- 内部授权filter
 curl 127.0.0.1:10030/test2 -X GET -H 'Authorization:Bearer 0dc17c0d-7737-4f8e-8973-59a431b7dd4a'

 
用户名密码获取token fielter: 
/oauth/token（无法自定义username、password名称，代码写死）
ClientCredentialsTokenEndpointFilter: 校验clientId、clientSecret
  ProviderManager(client_id) -> TokenEndpoint -> ResourceOwnerPasswordTokenGranter -> ProviderManager(username) -> DaoAuthenticationProvider
/form/token （可自定义username、password名称）
UsernamePasswordAuthenticationFilter: 校验用户名密码
  ProviderManager -> DaoAuthenticationProvider (DelegatingPasswordEncoder（根据密码加密前缀来匹配对应的密码加密器）、UserDetailsService)

密码加密器： 
DelegatingPasswordEncoder -> BCryptPasswordEncoder
 加密后格式：{bcrypt}$2a$10$YK8.eVVAL/NxyZ
 还有其他的加密器：
 {pbkdf2}5d923b44a6d129f3ddf3e
 {sha256}97cde38028ad898e
 
自定义密码加密器：WebSecurityConfig -> @Bean(PasswordEncoder passwordEncoder())
 
自定义token 存储管理（进入OAuth Api） extends SavedRequestAwareAuthenticationSuccessHandler 调取OAuth2 RedisTokenStore JtwTokenStore(不会持久化用户信息)等类存储
  
自定义增加返回字段：implements TokenEnhancer

自定义JtwTokenStore减少jwt token存储信息（防止token太长），增加的自定义的字段还是会返回：extends DefaultAccessTokenConverter （设置到JwtAccessTokenConverter(TokenEnhancer子类)）
   TokenEnhancerChain（自定义TokenEnhancer -> JwtAccessTokenConverter）

jdbc加载client信息：@TODO   

token自动续签：
  方式一：app设置header Token
  重写RedisTokenStore  readAuthentication方法，发现快过期则刷新token

  方式二：(只适合cookies方式)
  通过exceptionTranslator.translate(authException)解析异常，判断异常类型（status）
  如果是401异常则向授权服务器发起token刷新的请求
  如果token刷新成功，则通过request.getRequestDispatcher(request.getRequestURI()).forward(request,response);再次请求资



权限默认filter: 
OAuth2AuthenticationProcessingFilter extends OncePerRequestFilter
   BearerTokenExtractor： 
     解析header Authorization:Bearer
	 
   异常处理(clientSecret错误、token错误、权限不足)：
     OAuth2AuthenticationEntryPoint（DefaultWebResponseExceptionTranslator：默认异常处理类）
   
   权限校验投票管理器：
     AffirmativeBased（有一个投票通过就通过） extends AbstractAccessDecisionManager decide() ->  WebExpressionVoter vote()
	 
自定义token权限校验：
   继承OncePerRequestFilter
 
 
删除redis所有key value:
 eval "return redis.call('del',unpack(redis.call('keys',ARGV[1])))" 0 '*'
 
 
