FROM jenkins/jenkins:lts
USER root

# Install prerequisites
RUN apt-get update && \
    apt-get -y install apt-transport-https \
      ca-certificates \
      curl \
      gnupg2 \
      software-properties-common

# Add Docker's official GPG key and repository
RUN curl -fsSL https://download.docker.com/linux/$(. /etc/os-release; echo "$ID")/gpg | apt-key add - && \
    echo "deb [arch=amd64] https://download.docker.com/linux/$(. /etc/os-release; echo "$ID") $(lsb_release -cs) stable" > /etc/apt/sources.list.d/docker.list

# Update the package index again and install Docker CLI
RUN apt-get update && apt-get -y install docker-ce-cli

# Install Docker Compose
RUN curl -L "https://github.com/docker/compose/releases/download/v2.11.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose && \
    chmod +x /usr/local/bin/docker-compose

# Create docker group and add jenkins user to it
RUN groupadd -f docker && \
    usermod -aG docker jenkins

# Switch back to jenkins user
USER jenkins
