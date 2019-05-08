#/bin/sh
basepath=$(cd `dirname $0`; pwd)
echo $basepath
cd  $basepath
nohup java -Dserver.port=10001 -Dcsp.sentinel.zookeeper=192.168.0.152:2184  -Dcsp.sentinel.dashboard.server=localhost:10001 -Dproject.name=sentinel-dashboard -jar ../sentinel-dashboard.jar 2>&1 &
echo $!>pid
