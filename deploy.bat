docker image prune -f
docker-compose up -d rabbitMq
timeout/t 10
docker-compose up -d dtu-facade-entrypoint-service dtu-merchant-service dtu-tokenManagement-service

