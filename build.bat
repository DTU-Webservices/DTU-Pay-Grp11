cd messaging-utils
call ./build.bat
cd ..

# Build the services
cd dtu-merchant-service
call ./build.bat
cd..

cd dtu-customer-service
call ./build.bat
cd..

cd dtu-moneyTransfer-service
call ./build.bat
cd..

cd dtu-tokenManagement-service
call ./build.bat
cd..

cd dtu-facade-entrypoint-service
call ./build.bat
cd..

cd dtu-reporting-service
call ./build.bat
cd..
