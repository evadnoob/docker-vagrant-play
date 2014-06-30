# DOCKER-VERSION 0.3.4
FROM    centos:6.4

RUN     yum install -y unzip zip java-1.7.0-openjdk-devel.x86_64 

RUN     mkdir -p /opt
WORKDIR /opt
RUN     curl -OL http://dl.bintray.com/sbt/native-packages/sbt/0.13.5/sbt-0.13.5.zip
RUN     unzip -o sbt-0.13.5.zip


# make app source available on the /vagrant mount point
#ADD target/universal/stage /vagrant
ADD . /vagrant
#ADD ~/.ivy2 /ivy2

WORKDIR /vagrant

#only build the distribution if it is not already built
RUN if [[ ! -f target/universal/stage/bin/time-in-timezone ]]; then /opt/sbt/bin/sbt compile stage ; fi

EXPOSE  9000

CMD cd target/universal/stage && bin/time-in-timezone

