#!/bin/bash

# set -ex

# read variables from environment
db_host=${TE_DATABASE_HOST:-127.0.0.1}
db_port=${TE_DATABASE_PORT:-3306}
db_username=${TE_DATABASE_USER:-root}
db_password=${TE_DATABASE_PASSWORD:-} # default to empty password
db_name=${TE_DATABASE_NAME:-tiny_engine_data_java}

# prepare MySQL param
param="--default-character-set=utf8mb4 -h $db_host -P $db_port -u $db_username"
if [ -n "$db_password" ]; then
  param="$param -p$db_password"
fi


# reset database
echo "init database $db_name in local"
mysql $param -e "DROP DATABASE IF EXISTS $db_name"
mysql $param -e "CREATE DATABASE $db_name CHARACTER SET utf8mb4"



# find all sql files and sort
sql_files=$(ls /tmp/sql/*.sql | sort)
echo "🤖 Running the following SQL files:"

# execute sql files
for file in $sql_files; do
  echo "🔖 Running $file..."
  mysql $param $db_name < "$file"
done

echo "🎉 prepare database $db_name done"
mysql $param -e "USE $db_name; SHOW TABLES;"
