


 curl 127.0.0.1:10030/form/token -X POST -d 'username=zhs'  -d 'password=123456' -H 'clientId:c1' -H 'clientSecret:123456'
  
 curl 127.0.0.1:10030/test2 -X GET -H 'token:d786d3dc-beeb-4d44-9a04-4ac6ad524570'
 
 curl 127.0.0.1:10030/token/refresh -X POST  -H 'clientId:c1' -d 'refreshToken=d9478ae0-bb07-47de-b1d5-b55200da0410'

 curl 127.0.0.1:10030/loginOutTest -X POST -H 'token:586523f1-f3c1-4867-a2c1-b2daf71bb71e'
 
 
 curl 127.0.0.1:10030/oauth/token?client_id=c1\&client_secret=123456\&grant_type=password\&username=zhs\&password=123456
 
 curl 127.0.0.1:10030/logout -X GET -H 'token:d786d3dc-beeb-4d44-9a04-4ac6ad524570'
 
 -- ClientCredentialsTokenEndpointFilter
 curl 127.0.0.1:8081/oauth/token?client_id=c1\&client_secret=123456\&grant_type=password\&username=ls\&password=123456
 
 curl 127.0.0.1:8081/oauth/token?client_id=c1\&client_secret=123456\&grant_type=refresh_token\&refresh_token=5709e974-6d43-47e3-9a30-9d7e3c495ad0
 
 DelegatingPasswordEncoder
 https://blog.csdn.net/canon_in_d_major/article/details/79675033