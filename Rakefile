require 'erb'

namespace :sass do
  desc 'compiles the scss to css'
  task :compile do
    run_command 'mkdir -p target/styles'
    run_command 'sass resources/styles/website.scss:target/styles/website.css '
  end

  desc 'automatically recompile on change to scss file(s)'
  task :watch do
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

def run_command(command)
  puts command
  system(command) || raise("unable to execute command - #{command}")
end

