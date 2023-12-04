# espresso api

Este projeto foi criado com o [Spring Initializr](https://start.spring.io/).
Nele está contido todas as rotas necessárias para o projeto final espresso.

# Como rodar

1. Clone o repositório
2. Crie um arquivo `.env` seguindo o modelo do `example.env`
3. Rode o comando `docker-compose up -d`
4. Crie o arquivo  `src/main/resources/application.yml` seguindo o modelo do `application-example.yml`
5. Rode o comando `gradle bootRun`
6. Rode os scripts contidos em `utils.sql` através do `pgadmin`

Pronto! A aplicação está rodando na porta 8080

# Rotas

Você pode acessar as rotas através da coleção do postman contida em `espresso-postman.json`