# Spring Security com OAuth2 Resource Server

Este projeto utiliza Spring Security e oauth2-resource-server, que implementa uma solução de autenticação com tokens JWT.
Oferece geração de tokens de acesso(accessToken) e de atualização(refreshToken), Utilizando criptografia assimétrica com chaves RSA para assinar e validar os tokens. 


## Rodando localmente

Clone o projeto

```bash
  git clone https://github.com/nivaldoandrade/spring-security
```

Entre no diretório do projeto

```bash
  cd oauth2-resource-server
```

Inicie o docker-compose

```bash
  docker-compose up
```

Entre na documentação

```bash
  http://localhost:8080/swagger-ui/index.html
```

Entre na documentação

```bash
  http://localhost:8080/swagger-ui/index.html
  
  Usuários de teste:
      email: user@mail.com
      password: 123
      email: user1@mail.com
      password: 123
```

