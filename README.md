# ASSEMBLY - API

O objetivo dessa API é para votar em pauta de assembleia e obter resultado.

## Abordagem de implementação da abertura da votação (Explicação)

Ao iniciar uma pauta, foi informado uma data de incialização, uma data de finalização e uma flag para informar
se pauta está iniciada ou não. Um schedule é executado de minuto em minuto para verificar pautas abertas que
necessitam ser fechadas por conta do seu tempo de finalização. O schedule executa os seguintes passos:
1. Busca pautas por range de data ques estão abertas
2. Atualiza as pautas para fechadas
3. Envia notiticação para usuário (cpf) com resultado da pauta **caso exista um consumidor, regra de notificação seria aplicada**

**documentação swagger se encontra no arquivo open-api.yml**
## Instalação

Para poder rodar a aplicação, alguns passos são necessários para funcionar como esperado.


### Variaveis de Ambiente

| Variável | Descrição | Valor padrão
| ------ | ------ | ------ |
| SERVER_PORT | Porta do servidor | 8080
| APP_ENVIRONMENT | Ambiente onde serao executado a aplicacao | (staging, development)
| RM_HOST| Host do rabbit | localhost
| RM_PORT| Porta do rabbit | 5672
| RM_USER| User do rabbit | guest
| RM_PASSWORD| Senha do rabbit | guest
|RM_QUEUE_NOTIFICATION| nome da fila | notification-vote-send
|AMQP_ADDRS | Endereço do cluster | xxx (staging)
|MONGO_URI| endereço do mongodb| mongodb://localhost:27017/assembly
|




### Imagem Docker

1. Criar artefado da aplicação utilizando o Gradle (buildjar)
2. Excutar comando de criação de imagem
```sh
docker build -t assembly:1.0 .
```
> Para testar localmente execute o arquivo docker-compose.yml através do comando docker-compose up -d.