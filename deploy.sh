#!bin/bash
set -e

# Deployment script - @source Correlation Example from 02267 course.

docker image prune -f
docker-compose up -d rabbitMq
sleep 15
docker-compose up -d dtu-facade-entrypoint-service dtu-merchant-service dtu-customer-service dtu-moneytransfer-service dtu-tokenManagement-service dtu.reporting-service
