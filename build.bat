cd messaging-utils
call ./build.bat
cd ..

# Build the services
cd dtu-merchant-service
call ./build.bat
cd..

cd dtu-tokenManagement-service
call ./build.bat
cd..

cd dtu-facade-entrypoint-service
call ./build.bat
cd..

call ./deploy.bat