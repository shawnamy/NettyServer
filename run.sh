#!/bin/sh
PATH="$PATH:$JAVA_HOME/bin"
export PATH
CLASSPATH=.
for k in lib/*.jar
do
 CLASSPATH=$CLASSPATH:$k
done
export CLASSPATH
echo $CLASSPATH

java  -Xms512m -Xmx768m -classpath "$CLASSPATH" com.ford.cevdm.tcp.TcpServerTest