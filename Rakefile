namespace :lein do
  desc 'compiles the cljs to js'
  task :compile do
    run_command './lein cljsbuild once dev'
  end

  desc 'automatically recompile on change to cljs files'
  task :watch do
    run_command './lein cljsbuild auto dev'
  end
end

namespace :sass do
  desc 'compiles the scss to css'
  task :compile do
    run_command 'sass src/sass/website.scss:public/website.css'
  end

  desc 'automatically recompile on change to scss file(s)'
  task :watch do
    run_command 'sass --watch src/sass/website.scss:public/website.css'
  end
end

namespace :deploy do
  desc 'deploy code to staging environment'
  task :staging => %w(lein:compile sass:compile)
end

namespace :ci do
  desc 'install required tools'
  task :setup do
    begin
      run_command 'which sass >& /dev/null'
    rescue
      run_command 'sudo gem install sass'
    end
  end

  desc 'deploy the website via ci server'
  task :staging => %w(setup deploy:staging)
end

def run_command(command)
  puts command
  system(command) || raise("unable to execute command - #{command}")
end