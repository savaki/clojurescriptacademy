require 'erb'

namespace :sass do
  task :prepare do
    unless Dir.exists? 'resources/styles/node_modules'
      run_command '(cd resources/styles; npm install material-ui-sass)'
    end
  end

  desc 'compiles the scss to css'
  task :compile => :prepare do
    run_command 'mkdir -p target/styles'
    run_command 'sass resources/styles/website.scss:target/styles/website.css '
  end

  desc 'automatically recompile on change to scss file(s)'
  task :watch => :prepare do
    run_command 'mkdir -p resources/styles'
    run_command 'sass --watch resources/styles/website.scss:resources/public/website.css'
  end
end

namespace :lein do
  desc 'compiles the cljs to js'
  task :compile do
    run_command './lein cljsbuild once prod'
  end

  desc 'automatically recompile on change to cljs file(s)'
  task :watch do
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
  task :pages => ['lein:compile', 'sass:compile'] do
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
    run_command 'aws s3 cp --acl public-read --cache-control max-age=90 target/public s3://clojurescriptacademy-production'

    run_command 'gzip target/public/index/app.js'
    run_command 'aws s3 cp --acl public-read --cache-control max-age=90 --content-encoding gzip target/public/index/app.js.gz s3://clojurescriptacademy-production/index/app.js'

    run_command 'gzip target/public/website.css'
    run_command 'aws s3 cp --acl public-read --cache-control max-age=90 --content-encoding gzip target/public/website.css.gz s3://clojurescriptacademy-production/website.css'
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

def run_command(command)
  puts command
  system(command) || raise("unable to execute command - #{command}")
end

