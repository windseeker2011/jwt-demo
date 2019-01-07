# jwt-demo
how to use jwt into spring cloud zuul with spring security.


## 1.get token
`curl -i -H "Content-Type: application/json" -X POST -d '{"username":"admin","password":"admin"}' http://localhost:8080/auth/login`

`curl -i -H "Content-Type: application/json" -X POST -d '{"username":"user","password":"user"}' http://localhost:8080/auth/login`

## 2.use token
with token
`curl -i -H "Authorization: Bearer {u_get_token}" http://localhost:8080/service/user`
or without token
`curl -i http://localhost:8080/service/user`

## 3.result
|                                     | /service/admin | /service/user | /service/guest |
| ----------------------------------- | -------------- | ------------- | -------------- |
| `admin` token (role `USER` `ADMIN`) |            200 |           200 |            200 |
| `user` token (role `USER`)       	  |            403 |           200 |            200 |
| no token                            |            401 |           401 |            200 |