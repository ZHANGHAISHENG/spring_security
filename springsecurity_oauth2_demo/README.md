

 curl 127.0.0.1:10030/form/token -X POST -d 'username=zhs'  -d 'password=123456' -H 'clientId:c1' -H 'clientSecret:123456'
  
 curl 127.0.0.1:10030/test2 -X GET -H 'token:eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJscyIsImF1dGhvciI6IuW8oOWogSIsInNjb3BlIjpbImFsbCJdLCJjb21wYW55IjoiYXdiZWNpLWNvcHkiLCJleHAiOjE1OTE0NDgxODQsImF1dGhvcml0aWVzIjpbImFkbWluIiwiUk9MRV9VU0VSIl0sImp0aSI6IjM3MTU1OGRmLWMzMzktNDI4MC05ZGVlLTk2ZDUwMjU2ZmJiNyIsImNsaWVudF9pZCI6ImMxIn0.pIsNSCZFX1CS2D6Q3Nj9kAEnL5gieJ9WyjAotUatfEU' 
 
 curl 127.0.0.1:10030/token/refresh -X POST  -H 'clientId:c1' -d 'refreshToken=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJ6aHMiLCJzY29wZSI6WyJhbGwiXSwiYXRpIjoiZDc0YWFkMDQtZDAwMC00YjcxLTg3NzUtY2JiNDFlODZlMjQ0IiwiZXhwIjoxNTkxNjAzNDY0LCJhdXRob3JpdGllcyI6WyJhZG1pbiIsIlJPTEVfVVNFUiJdLCJqdGkiOiI2Nzk5ZjE0NC1jNWFkLTQ2OWItOTk5YS1mMmVmYTk4NjYxYjYiLCJjbGllbnRfaWQiOiJjMSJ9.pIahYV1psi6x5sQzIMiN9SDwUGAJ0y-vIKyQjhyPyOY'
 
 
 curl 127.0.0.1:10030/oauth/token?client_id=c1\&client_secret=123456\&grant_type=password\&username=zhs\&password=123456
 
 -- ClientCredentialsTokenEndpointFilter
 curl 127.0.0.1:8081/oauth/token?client_id=c1\&client_secret=123456\&grant_type=password\&username=ls\&password=123456
 
 curl 127.0.0.1:8081/oauth/token?client_id=c1\&client_secret=123456\&grant_type=refresh_token\&refresh_token=5709e974-6d43-47e3-9a30-9d7e3c495ad0
 
 DelegatingPasswordEncoder
 https://blog.csdn.net/canon_in_d_major/article/details/79675033