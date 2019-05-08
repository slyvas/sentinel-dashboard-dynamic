#!/bin/sh
basepath=$(cd `dirname $0`; pwd)
echo $basepath
cd  $basepath
pid=`cat pid`
echo "kill -9 $pid"
kill -9 $pid
echo "" > pid

