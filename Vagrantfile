Vagrant.configure("2") do |config|
  # generic/ubuntu2004 supports both Hyper-V and VirtualBox
  config.vm.box = "generic/ubuntu2004"
  config.vm.hostname = "redis-server"

  # Forward Redis port from VM to host localhost:6379
  # This means your Spring Boot app can still use localhost:6379 unchanged
  config.vm.network "forwarded_port", guest: 6379, host: 6379

  # Hyper-V provider (built into Windows 10/11 Pro - no extra install needed)
  config.vm.provider "hyperv" do |hv|
    hv.vmname  = "redis-vagrant"
    hv.memory  = 512
    hv.cpus    = 1
    hv.enable_enhanced_session_mode = false
  end

  # Fallback: VirtualBox (install from https://www.virtualbox.org if preferred)
  config.vm.provider "virtualbox" do |vb|
    vb.name   = "redis-vagrant"
    vb.memory = "512"
    vb.cpus   = 1
  end

  # Provision: install and start Redis
  config.vm.provision "shell", inline: <<-SHELL
    apt-get update -y
    apt-get install -y redis-server

    # Allow connections from any interface (needed for port forwarding)
    sed -i 's/^bind 127.0.0.1 -::1/bind 0.0.0.0/' /etc/redis/redis.conf
    sed -i 's/^protected-mode yes/protected-mode no/' /etc/redis/redis.conf

    systemctl enable redis-server
    systemctl restart redis-server

    echo "Redis is running on port 6379"
  SHELL
end
