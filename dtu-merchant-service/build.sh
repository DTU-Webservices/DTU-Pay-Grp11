#!/bin/bash
set -e
mvn clean package
docker-compose build dtu-merchant-service
