# Mail microservice

For this microservice, we need RabbitMQ running in docker:

```
docker run -d --hostname rabbitmq-host --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:management
```