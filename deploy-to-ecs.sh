#!/bin/bash

# Configuration
AWS_REGION="us-east-1"
AWS_ACCOUNT_ID="YOUR_ACCOUNT_ID"
ECR_REPOSITORY_PREFIX="order-payment-microservices"
ECS_CLUSTER_NAME="order-payment-cluster"
ECS_SERVICE_NAME="order-payment-service"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}Starting deployment to ECS...${NC}"

# Check if AWS CLI is installed
if ! command -v aws &> /dev/null; then
    echo -e "${RED}AWS CLI is not installed. Please install it first.${NC}"
    exit 1
fi

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo -e "${RED}Docker is not installed. Please install it first.${NC}"
    exit 1
fi

# Login to ECR
echo -e "${YELLOW}Logging in to Amazon ECR...${NC}"
aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com

# Create ECR repositories if they don't exist
echo -e "${YELLOW}Creating ECR repositories...${NC}"
for service in order-service payment-service products-service frontend; do
    aws ecr describe-repositories --repository-names $service --region $AWS_REGION 2>/dev/null || \
    aws ecr create-repository --repository-name $service --region $AWS_REGION
done

# Build and push Docker images
echo -e "${YELLOW}Building and pushing Docker images...${NC}"

# Order Service
echo -e "${YELLOW}Building order service...${NC}"
cd order-microservice
docker build -t $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/order-service:latest .
docker push $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/order-service:latest
cd ..

# Payment Service
echo -e "${YELLOW}Building payment service...${NC}"
cd payment-microservice2
docker build -t $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/payment-service:latest .
docker push $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/payment-service:latest
cd ..

# Products Service
echo -e "${YELLOW}Building products service...${NC}"
cd products-microservice
docker build -t $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/products-service:latest .
docker push $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/products-service:latest
cd ..

# Frontend
echo -e "${YELLOW}Building frontend...${NC}"
cd frontend
docker build -t $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/frontend:latest .
docker push $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/frontend:latest
cd ..

# Create ECS cluster if it doesn't exist
echo -e "${YELLOW}Creating ECS cluster...${NC}"
aws ecs create-cluster --cluster-name $ECS_CLUSTER_NAME --region $AWS_REGION 2>/dev/null || echo "Cluster already exists"

# Register task definition
echo -e "${YELLOW}Registering task definition...${NC}"
# Replace placeholders in task definition
sed "s/YOUR_ACCOUNT_ID/$AWS_ACCOUNT_ID/g; s/YOUR_REGION/$AWS_REGION/g" ecs-task-definition.json > ecs-task-definition-temp.json
aws ecs register-task-definition --cli-input-json file://ecs-task-definition-temp.json --region $AWS_REGION

# Create or update service
echo -e "${YELLOW}Creating/updating ECS service...${NC}"
if aws ecs describe-services --cluster $ECS_CLUSTER_NAME --services $ECS_SERVICE_NAME --region $AWS_REGION 2>/dev/null | grep -q "serviceName"; then
    # Update existing service
    aws ecs update-service --cluster $ECS_CLUSTER_NAME --service $ECS_SERVICE_NAME --task-definition order-payment-microservices --region $AWS_REGION
else
    # Create new service
    sed "s/YOUR_ACCOUNT_ID/$AWS_ACCOUNT_ID/g; s/YOUR_REGION/$AWS_REGION/g" ecs-service-definition.json > ecs-service-definition-temp.json
    aws ecs create-service --cli-input-json file://ecs-service-definition-temp.json --region $AWS_REGION
fi

# Clean up temporary files
rm -f ecs-task-definition-temp.json ecs-service-definition-temp.json

echo -e "${GREEN}Deployment completed successfully!${NC}"
echo -e "${YELLOW}You can monitor the deployment in the AWS ECS console.${NC}"
echo -e "${YELLOW}Service URL will be available once the load balancer is configured.${NC}" 