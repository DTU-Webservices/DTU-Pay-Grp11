./build.bat

# Update the set of services and
# build and execute the system tests
pushd end-to-end-tests
./deploy.bat
popd

# Cleanup the build images
docker image prune -f

pause