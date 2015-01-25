require 'erb'
require 'rbconfig'

namespace :sass do
  task :prepare do
    unless Dir.exists? 'resources/styles/node_modules'
      run_command '(cd resources/styles; npm install material-ui-sass)'
    end
  end

  desc 'compiles the scss to css'
  task :compile => :prepare do
    run_command 'mkdir -p target/public/static/styles'
    run_command 'sass --style compact resources/styles/website.scss:target/public/static/styles/website.css'
  end

  desc 'automatically recompile on change to scss file(s)'
  task :watch => :prepare do
    run_command 'mkdir -p resources/styles/static/styles'
    run_command 'sass --watch resources/styles/website.scss:resources/public/static/styles/website.css'
  end
end

namespace :lein do
  task :prepare do
    run_command '(cd bin; wget http://fb.me/react-0.12.2.js)' unless File.exists?('bin/react-0.12.2.js')
  end

  desc 'compiles the cljs to js'
  task :compile => :prepare do
    run_command './lein cljsbuild once prod'
  end

  desc 'automatically recompile on change to cljs file(s)'
  task :watch => :prepare do
    run_command './lein cljsbuild auto dev'
  end
end

namespace :server do
  desc 'starts the local development web server'
  task :run do
    run_command '(cd resources/public ; python -m SimpleHTTPServer)'
  end
end

namespace :render do
  desc 'pre-render the reagent pages'
  task :pages => %w(lein:compile sass:compile) do
    run_command 'node bin/gen-site.js'
  end
end

namespace :deploy do
  desc 'deploy code to staging environment'
  task :staging => %w(render:pages) do
    run_command 'aws s3 cp --recursive --acl public-read target/public s3://clojurescriptacademy-staging'
  end

  desc 'deploy code to production environment'
  task :production => %w(render:pages) do
    run_command 'aws s3 cp --recursive --acl public-read --cache-control max-age=90 target/public s3://clojurescriptacademy-production'

    run_command 'gzip target/public/index/app.js'
    run_command 'aws s3 cp --acl public-read --cache-control max-age=90 --content-encoding gzip target/public/index/app.js.gz s3://clojurescriptacademy-production/index/app.js'

    run_command 'gzip target/public/website.css'
    run_command 'aws s3 cp --acl public-read --cache-control max-age=90 --content-encoding gzip target/public/website.css.gz s3://clojurescriptacademy-production/website.css'
  end
end

namespace :node do
  desc 'compile all the files required by the node server'
  task :prepare => %w(lein:compile sass:compile) do
    run_command 'npm install express st' unless Dir.exists?("#{ENV['HOME']}/node_modules")
  end

  desc 'start the node server'
  task :server => :prepare do
    run_command 'node bin/server.js'
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
  task :prepare => :download do

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
        "sudo apt-get install -y nodejs npm"
      ]
    },
    {
      "type": "shell",
      "inline": [
        "sudo mkdir -p /app/target/public /app/bin",
        "sudo chown -R ubuntu:ubuntu /app",
        "(cd /app ; npm install express)"
      ]
    },
    {
      "type": "file",
      "source": "bin/",
      "destination": "/app/bin"
    },
    {
      "type": "file",
      "source": "target/public/",
      "destination": "/app/target/public"
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
    filename = 'tmp/output.log'
    run_command "rm -f #{filename}"

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

def run_command(command)
  puts command
  system(command) || raise("unable to execute command - #{command}")
end

