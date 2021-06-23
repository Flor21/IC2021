FROM jenkins/jenkins

USER root
RUN apt-get -y update && apt-get install -y maven

USER jenkins
RUN jenkins-plugin-cli --plugins "blueocean:1.24.7"
