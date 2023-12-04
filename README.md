# espresso api

este projeto foi criado com o [Spring Initializr](https://start.spring.io/).
nele está contido todas as rotas necessárias para o projeto final espresso.

# como rodar

1. clone o repositório
2. crie um arquivo `.env` seguindo o modelo do `example.env`
3. rode o comando `docker-compose up -d`
4. crie o arquivo  `src/main/resources/application.yml` seguindo o modelo do `application-example.yml`
5. rode o comando `gradle bootRun`
6. rode os scripts contidos em `utils.sql` através do `pgadmin`

pronto! a aplicação está rodando na porta 8080

# Rotas

você pode acessar as rotas através da coleção do postman contida em `espresso-postman.json`