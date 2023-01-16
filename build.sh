#!bin/bash
set -e

pushd messaging-utils
./build.sh
popd

pushd dtu-facade-entrypoint-service
./build.sh
popd

pushd dtu-merchant-service
./build.sh
popd

pushd dtu-customer-service
./build.sh
popd

pushd dtu-moneyTransfer-service
./build.sh
popd

pushd dtu-tokenManagement-service
./build.sh
popd