# Spring Security com OAuth2 Resource Server

Este projeto utiliza Spring Security e oauth2-resource-server, que implementa uma solução de autenticação com tokens JWT.
Oferece geração de tokens de acesso(accessToken) e de atualização(refreshToken). Utilizando criptografia assimétrica com chaves RSA para assinar e validar os tokens. 

## Rodando Localmente com Docker

### 1. Clone o Repositório

Clone o repositório para o seu ambiente local:
```bash
  git clone https://github.com/nivaldoandrade/spring-security
```

### 2. Iniciar o Docker

No terminal, navegue até o diretório onde está o arquivo Dockerfile:

Entre no diretório do projeto:

```bash
  cd oauth2-resource-server
```

Execute o comando para buildar o docker:

```bash
  docker build -t oauth2-resource-server .
```

Inicie o docker da API:

```bash
  docker run --name oauth2-resource-server -p 8080:8080 oauth2-resource-server -d
```

## Rodando Localmente sem Docker

### 1. Clone o Repositório

Clone o repositório para o seu ambiente local:
```bash
  git clone https://github.com/nivaldoandrade/spring-security
```

Entre no diretório do projeto:

```bash
  cd oauth2-resource-server
```

### 2. Compilar e Executar o Sistema

1. Compile o projeto:

```bash
./mvnw clean package -DskipTests
```

2. Execute o sistema: Navegue até o diretório onde o JAR foi gerado e execute:

```bash
java -jar target/oauth2-resource-server-0.0.1-SNAPSHOT.jar
```
Ou, se você estiver usando uma IDE, execute a classe principal LocadoraApplication a partir do diretório src/main/java.


## **Documentação da API**

Para visualizar a documentação interativa da API, você pode usar o Swagger. O Swagger fornece uma interface gráfica onde você pode explorar e testar as endpoints da API.

Após iniciar os serviços, a documentação do Swagger estará disponível em:
```bash
  http://localhost:8080/swagger-ui/index.html
  
  Usuários de teste:
      email: user@mail.com
      password: 123
      email: user1@mail.com
      password: 123
```
Navegue até este URL no seu navegador para acessar a interface do Swagger, onde você poderá visualizar e interagir com a documentação da API.
