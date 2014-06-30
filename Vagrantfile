# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure("2") do |config|
  config.ssh.username = "vagrant"
  config.vm.box = "centos/lyrical"
  config.ssh.username = "vagrant"
  config.vm.box_url = "httpb://www.lyricalsoftware.com/downloads/centos65.box"
  
  config.vm.network :forwarded_port, guest: 9000, host: 9400

  config.vm.synced_folder ".", "/vagrant"

  config.vm.provider :virtualbox do |vb|
   vb.customize ["modifyvm", :id, "--memory", 4096]
   vb.customize ["modifyvm", :id, "--natdnshostresolver1", "on"]
   vb.customize ["modifyvm", :id, "--natdnsproxy1", "on"]
  end

  # build a docker image, this step could be a bit faster if we
  # committed these changes to an image and pushed up to docker hub.
  
$bootstrap = <<EOF
echo Installing Docker...
sudo rpm -iUvh http://dl.fedoraproject.org/pub/epel/6/x86_64/epel-release-6-8.noarch.rpm
#sudo yum update -y
sudo yum -y install docker-io
sudo service docker start
sudo chkconfig docker on
cd /vagrant

sudo docker stop --time=10 time-in-timezone
sudo docker rm --force time-in-timezone

sudo docker build -t evadnoob/time-in-timezone  .

sudo docker run -d -p 9000:9000 --name time-in-timezone evadnoob/time-in-timezone

echo docker setup done...
sudo docker inspect -f '{{ .NetworkSettings.IPAddress }}' time-in-timezone
EOF


  config.vm.provision :shell, :inline => $bootstrap, :privileged => false
end

