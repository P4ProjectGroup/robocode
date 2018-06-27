#!/bin/sh

setup_git() {
  git config --global user.email "martijn.debijl@gmail.com"
  git config --global user.name "Martijn de Bijl"
}

commit_website_files() {
  git checkout -b master
  git add /out
  git commit --message "Travis build: $TRAVIS_BUILD_NUMBER"
}

upload_files() {
  git remote add origin https://${GH_TOKEN}@github.com/P4ProjectGroup/robocode.git
  git push --quiet --set-upstream origin master
}

setup_git
commit_website_files
upload_files

