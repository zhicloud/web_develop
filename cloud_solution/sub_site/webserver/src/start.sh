#!/bin/bash
echo "show tomcat pid...."
ps -ef|grep tomcat |grep -v grep |awk '{print $2}'

echo "kill tomcat pid ..."

ps -ef|grep tomcat|grep -v grep |awk '{print $2}'|xargs kill -9

sleep 1


echo "start tomcat ..."

cd /opt/apache-tomcat-7.0.54/bin
./startup.sh
