#!/usr/bin/env bash
set -euo pipefail

cd ~/apps/it_network

docker compose -f docker-compose.prod.yml pull
docker compose -f docker-compose.prod.yml up -d

docker image prune -f
