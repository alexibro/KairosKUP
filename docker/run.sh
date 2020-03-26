#!/bin/sh
while ! nc -z db 3306; do
    echo “Waiting DB”
    sleep 2
done

exec java -jar app.jar