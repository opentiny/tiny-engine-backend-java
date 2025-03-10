#!/bin/bash

# set -ex
# 复制 sql script 到 docker 容器内部
docker cp init_database_mysql.sh  te_java_mysql:/tmp/
docker cp ./app/src/main/resources/sql/mysql  te_java_mysql:/tmp/sql
# 在 docker 容器内部执行 script
docker exec te_java_mysql sh -c "chmod +x /tmp/init_database_mysql.sh && /tmp/init_database_mysql.sh"
