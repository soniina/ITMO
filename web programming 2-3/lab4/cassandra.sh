#!/bin/bash

docker ps -q --filter "name=cassandra" | xargs -r docker stop
docker ps -aq --filter "name=cassandra" | xargs -r docker rm
docker run --name cassandra --add-host=host.docker.internal:host-gateway -d -p 9042:9042 -v $(pwd)/init.cql:/init.cql cassandra:latest

echo "Waiting for Cassandra to initialize..."
sleep 60

docker exec -i cassandra cqlsh -f /init.cql
docker exec cassandra sed -i 's/^rpc_address:.*/rpc_address: 0.0.0.0/' /etc/cassandra/cassandra.yaml
