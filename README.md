- .env
```.env
# MongoDB
MONGO_DB_USER=root
MONGO_DB_PASS=example
MONGO_DB_HOST=zimacube.tail21fc44.ts.net:27017
MONGO_DB_NAME=orchidbe
MONGO_DB_URL_SELFHOST="mongodb://${MONGO_DB_USER}:${MONGO_DB_PASS}@${MONGO_DB_HOST}/${MONGO_DB_NAME}?authSource=admin"
MONGODB_URL_CLOUD="mongodb+srv://${MONGO_DB_USER}:${MONGO_DB_PASS}@${MONGO_DB_HOST}/${MONGO_DB_NAME}?retryWrites=true&w=majority&appName=Crud"

# SQLServer
SQL_SERVER_USER=sa
SQL_SERVER_PASS=12345
SQL_SERVER_HOST=localhost
SQL_SERVER_PORT=1433
SQL_SERVER_DB=OrchidBE

# PostgreSQL
POSTGRES_HOST=localhost
POSTGRES_PORT=5432
POSTGRES_USER=admin
POSTGRES_PASSWORD=admin
POSTGRES_DB=orchid_db

JWT_EXPIRATION=86400
JWT_REFRESH_TOKEN_EXPIRATION=5184000
JWT_SECRET_KEY=LuuCaoHoangLuuCaoHoangLuuCaoHoangLuuCaoHoangLuuCaoHoang

# Application logging
# Your app's log level
APP_LOG_LEVEL=DEBUG
# Overall system log level
ROOT_LOG_LEVEL=INFO
# Console output threshold
CONSOLE_LOG_LEVEL=WARN

# Framework logging
# Spring framework
SPRING_LOG_LEVEL=WARN
# Spring Web (useful for request logging)
SPRING_WEB_LOG_LEVEL=INFO
# Security events
SECURITY_LOG_LEVEL=INFO
# MongoDB operations
MONGO_LOG_LEVEL=INFO
# MongoDB driver (very verbose if DEBUG)
MONGO_DRIVER_LOG_LEVEL=WARN

# Log file configuration

# Log directory
LOG_HOME=./logs
# App name for log files
APP_NAME=orchidbe
# Max size before rolling
MAX_LOG_FILE_SIZE=100MB
# Days to keep logs
MAX_LOG_HISTORY=60
```
