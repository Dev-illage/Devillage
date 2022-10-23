#!/bin/bash

REPOSITORY= /home/ec2-user/project/devillage/backend/Devillage

sudo cd $REPOSITORY

echo "> GIT STASH"
git stash

echo "> GIT PULL"
git pull

APP_NAME=devillage
JAR_NAME=$(ls $REPOSITORY/build/libs/ | grep '.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/build/libs/$JAR_NAME

CURRENT_PID=$(pgrep -f $APP_NAME)

if [ -z $CURRENT_PID ]
then
  echo "> 종료할것 없음."
else
  echo "> kill -9 $CURRENT_PID"
  sudo kill -15 $CURRENT_PID
  sleep 5
fi

echo "> GRADLE CLEAN"
sudo ./gradlew clean

echo "> GRADLE BUILD"
sudo ./gradlew build

echo "> $JAR_PATH 배포"

shdo nohup java -jar $JAR_PATH > /dev/null 2> /dev/null < /dev/null &

