# CryptoBank
The project uses Java 23. The server runs by default on port 8080.

Steps to run the database (for any problems or changes also check *resources->application.properties*):
1. Install MariaDB and make sure it is running
2. Create a new database named **cryptobank**:
    > CREATE DATABASE cryptobank;
3. Create a new user named **dbuser** with password **1234**.
    > CREATE USER 'dbuser'@'InsertHostnameHere' IDENTIFIED BY '1234';
4. Grant the user database permissions for **cryptobank**.
   > GRANT ALL PRIVILEGES ON cryptobank.* TO 'dbuser'@'InsertHostnameHere';