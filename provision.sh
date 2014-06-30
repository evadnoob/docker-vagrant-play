#!/bin/bash
yum install -y zip unzip java-1.7.0-openjdk-devel.x86_64

mkdir -p /opt && cd /opt

#sudo alternatives --config java
rm -rf sbt/
curl -OL http://dl.bintray.com/sbt/native-packages/sbt/0.13.5/sbt-0.13.5.zip
unzip -o sbt-0.13.5.zip







