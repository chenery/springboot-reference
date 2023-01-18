## mvn clean install

# docker build --tag springboot-reference:latest .

# or mvn spring-boot:build-image (no need for Dockerfile)


# Retrieve an authentication token and authenticate your Docker client to your registry. Use the AWS CLI:
# aws ecr get-login-password --region ap-southeast-2 | docker login --username AWS --password-stdin 541928850166.dkr.ecr.ap-southeast-2.amazonaws.com

# docker tag springboot-reference:1.0.0 541928850166.dkr.ecr.ap-southeast-2.amazonaws.com/springboot-reference-ecr:1.0.0

# docker push 541928850166.dkr.ecr.ap-southeast-2.amazonaws.com/springboot-reference-ecr:1.0.0