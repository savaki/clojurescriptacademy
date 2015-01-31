NODE = ENV['NODE'] || 'nodemon'

require 'erb'
require 'rbconfig'

desc 'delete temporary files'
task :clean do
  run_command 'rm -rf target'
end

namespace :sass do
  task :prepare do
    unless Dir.exists? 'resources/styles/node_modules'
      run_command '(cd resources/styles; npm install material-ui-sass)'
    end
  end

  desc 'compiles the scss to css'
  task :compile => :prepare do
    run_command 'mkdir -p target/public/styles'
    run_command 'sass --style compact resources/styles/website.scss:target/public/styles/website.css'
  end

  desc 'automatically recompile on change to scss file(s)'
  task :watch => :prepare do
    run_command 'mkdir -p target/dev/styles'
    run_command 'sass --watch resources/styles/website.scss:target/dev/styles/website.css'
  end
end

namespace :lein do
  desc 'automatically compiles cljs -> js when files change'
  task :watch do
    run_command 'infra/lein cljsbuild auto dev server'
  end

  desc 'compiles cljs -> js'
  task :compile do
    run_command 'infra/lein cljsbuild once prod server'
  end
end

namespace :node do
  closure_bootstrap = 'https://raw.githubusercontent.com/google/closure-library/master/closure/goog/bootstrap/nodejs.js'
  basedir = 'target/server'

  desc 'prepares the server directory'
  task :prepare do
    # install the server
    run_command "cp infra/server.js #{basedir}/server.js"

    # install express, st
    run_command "(cd #{basedir}; npm install express st --save)" unless Dir.exists?("../.. /node_modules")

    # download the closure nodejs bootstrap
    filename = "#{basedir}/goog/bootstrap/nodejs.js"
    unless File.exists? filename
      run_command "mkdir -p #{File.dirname filename}"
      run_command "(cd #{File.dirname filename}; curl -s -o #{File.basename filename} #{closure_bootstrap})"
    end

    # download react
    unless File.exists? "#{basedir}/react.js"
      run_command "curl -L -s -o #{basedir}/react.js http://fb.me/react-0.12.2.js"
    end
  end

  desc 'do something'
  task :server => :prepare do
    run_command "(cd #{basedir} ; #{NODE} ./server.js)"
  end
end

namespace :ci do
  task :setup do
    begin
      run_command 'which sass >& /dev/null'
    rescue
      run_command 'sudo gem install sass'
    end
  end

  desc 'deploy the website to staging via ci server'
  task :staging => %w(setup deploy:staging)

  desc 'deploy the website to production via ci server'
  task :production => %w(setup deploy:production)
end

namespace :packer do
  def packer
    'tmp/packer/packer'
  end

  desc 'download packer'
  task :download do
    host_os = RbConfig::CONFIG['host_os']
    zip_file = case host_os
                 when /darwin|mac os/
                   'https://dl.bintray.com/mitchellh/packer/packer_0.7.5_darwin_amd64.zip'
                 when /linux/
                   'https://dl.bintray.com/mitchellh/packer/packer_0.7.5_linux_amd64.zip'
                 else
                   raise "unknown os: #{host_os.inspect}"
               end

    unless File.exists?(packer)
      run_command "mkdir -p #{File.dirname packer}"
      run_command "(cd #{File.dirname packer}; wget #{zip_file} ; unzip -U #{File.basename zip_file})"
    end
  end

  desc 'generates the packer.conf file'
  task :prepare => %w(download sass:compile lein:compile node:prepare) do

    source_ami = 'ami-9eaa1cf6'
    security_group = 'sg-c94897ad'
    subnet = 'subnet-ab9004dc'

    # "ssh_private_key_file": "/Users/matt/.ssh/id_packer",
    # "temporary_key_pair_name": "packer",
    File.open('packer.conf', 'w') do |io|
      io.puts <<EOF
{
  "builders": [
    {
      "ami_name": "clojurescriptacademy {{timestamp}}",
      "associate_public_ip_address": true,
      "instance_type": "t2.micro",
      "region": "us-east-1",
      "security_group_id": "#{security_group}",
      "source_ami": "#{source_ami}",
      "ssh_username": "ubuntu",
      "subnet_id": "#{subnet}",
      "type": "amazon-ebs"
    }
  ],

  "provisioners": [
    {
      "type": "shell",
      "inline": [
        "sudo apt-get update",
        "sudo apt-get install -y nodejs npm curl awscli jq"
      ]
    },
    {
      "type": "shell",
      "inline": [
        "sudo mkdir -p /app/target/public /app/bin",
        "sudo chown -R ubuntu:ubuntu /app",
        "(cd /app ; npm install express st)"
      ]
    },
    {
      "type": "file",
      "source": "target",
      "destination": "/app/target"
    },
    {
      "type": "file",
      "source": "resources/infra/rc.local",
      "destination": "/tmp/rc.local"
    },
    {
      "type": "shell",
      "inline": [
        "sudo mv /tmp/rc.local /etc/rc.local",
        "sudo chmod +x /etc/rc.local"
      ]
    }
  ]
}
EOF
    end
  end

  desc 'build the ami'
  task :build => :prepare do
    filename = 'tmp/build.log'
    run_command "mkdir -p #{File.dirname filename}; rm -f #{filename}"

    # create the ami
    run_command "#{packer} build -machine-readable packer.conf | tee -a #{filename}"

    # parse the output file and extract the ami
    ami = `grep 'artifact,0,id' #{filename} | awk -F: '{print $2}'`
    ami = ami.gsub(/\s*/, '')
    File.open('ami.txt', 'w') do |io|
      io.puts ami
    end
  end
end

namespace :ec2 do
  def ami_id
    File.read('ami.txt').gsub(/\s*/, '')
  end

  desc 'start a instance'
  task :start_instance do
    filename = 'instance.log'
    run_command "rm -f #{filename}"

    ENV['AWS_DEFAULT_REGION'] = 'us-east-1'

    security_group = 'sg-a97ea1cd'
    instance_type = 't2.micro'
    subnet = 'subnet-ab9004dc'
    profile = 'Name=clojurescriptacademy'
    key_name = 'us-east-1'

    run_command <<EOF
aws ec2 run-instances \
  --image-id #{ami_id} \
  --security-group-ids #{security_group} \
  --instance-type #{instance_type} \
  --subnet-id #{subnet} \
  --iam-instance-profile #{profile} \
  --associate-public-ip-address \
  --key-name #{key_name} | jq -r .Instances[0].InstanceId > #{filename}
EOF

    # tag the instance
    instance_id = File.read(filename).gsub(/\s*/, '')
    run_command "aws ec2 create-tags --resources #{instance_id} --tags Key=hostname,Value=staging 'Key=Name,Value=ClojureScript Academy - staging'"
  end

  desc 'stop all the other instances'
  task :terminate_other_instances do
    instances_to_delete = '/tmp/delete_instances.txt'
    run_command <<EOF
rm -f #{instances_to_delete}
aws ec2 describe-instances | jq -r '.Reservations[].Instances[] | select(.State.Name=="running" and .SecurityGroups[].GroupName=="clojurescript-academy" and .ImageId != "#{ami_id}") | .InstanceId' > #{instances_to_delete}
EOF

    File.open(instances_to_delete).each_line do |instance_id|
      run_command "aws ec2 terminate-instances --instance-ids #{instance_id}"
    end
  end
end

def run_command(command)
  puts command
  system(command) || raise("unable to execute command - #{command}")
end

