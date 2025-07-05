#!/bin/bash

# All-in-One Store Microservices Startup Script
# This script starts all backend services and the frontend

echo "🏪 Starting All-in-One Store Microservices..."
echo "=============================================="

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to check if a port is in use
check_port() {
    if lsof -Pi :$1 -sTCP:LISTEN -t >/dev/null ; then
        echo -e "${RED}❌ Port $1 is already in use${NC}"
        return 1
    else
        echo -e "${GREEN}✅ Port $1 is available${NC}"
        return 0
    fi
}

# Function to wait for service to be ready
wait_for_service() {
    local port=$1
    local service_name=$2
    echo -e "${YELLOW}⏳ Waiting for $service_name to be ready on port $port...${NC}"
    
    for i in {1..30}; do
        if curl -s http://localhost:$port >/dev/null 2>&1; then
            echo -e "${GREEN}✅ $service_name is ready!${NC}"
            return 0
        fi
        sleep 2
    done
    
    echo -e "${RED}❌ $service_name failed to start within 60 seconds${NC}"
    return 1
}

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo -e "${RED}❌ Java is not installed. Please install Java 17+ first.${NC}"
    exit 1
fi

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo -e "${RED}❌ Maven is not installed. Please install Maven 3.6+ first.${NC}"
    exit 1
fi

# Check if Python is installed
if ! command -v python3 &> /dev/null; then
    echo -e "${RED}❌ Python 3 is not installed. Please install Python 3+ first.${NC}"
    exit 1
fi

echo -e "${BLUE}🔍 Checking port availability...${NC}"

# Check all required ports
check_port 8080 || exit 1
check_port 8081 || exit 1
check_port 8082 || exit 1
check_port 3000 || exit 1

echo -e "${BLUE}🚀 Starting backend services...${NC}"

# Start Payment Service (Port 8080)
echo -e "${YELLOW}💳 Starting Payment Service on port 8080...${NC}"
cd payment-microservice2
mvn spring-boot:run > ../logs/payment-service.log 2>&1 &
PAYMENT_PID=$!
cd ..

# Start Order Service (Port 8081)
echo -e "${YELLOW}📦 Starting Order Service on port 8081...${NC}"
cd order-microservice
mvn spring-boot:run > ../logs/order-service.log 2>&1 &
ORDER_PID=$!
cd ..

# Start Products Service (Port 8082)
echo -e "${YELLOW}🛍️ Starting Products Service on port 8082...${NC}"
cd products-microservice
mvn spring-boot:run > ../logs/products-service.log 2>&1 &
PRODUCTS_PID=$!
cd ..

# Create logs directory if it doesn't exist
mkdir -p logs

# Wait for services to be ready
echo -e "${BLUE}⏳ Waiting for services to start...${NC}"
sleep 10

# Check if services are running
wait_for_service 8080 "Payment Service" || exit 1
wait_for_service 8081 "Order Service" || exit 1
wait_for_service 8082 "Products Service" || exit 1

# Start Frontend (Port 3000)
echo -e "${YELLOW}🌐 Starting Frontend on port 3000...${NC}"
cd frontend
python3 -m http.server 3000 > ../logs/frontend.log 2>&1 &
FRONTEND_PID=$!
cd ..

# Wait for frontend
sleep 3

# Check if frontend is running
if curl -s http://localhost:3000 >/dev/null 2>&1; then
    echo -e "${GREEN}✅ Frontend is ready!${NC}"
else
    echo -e "${RED}❌ Frontend failed to start${NC}"
    exit 1
fi

# Save PIDs to file for cleanup
echo $PAYMENT_PID > logs/payment-service.pid
echo $ORDER_PID > logs/order-service.pid
echo $PRODUCTS_PID > logs/products-service.pid
echo $FRONTEND_PID > logs/frontend.pid

echo ""
echo -e "${GREEN}🎉 All services started successfully!${NC}"
echo "=============================================="
echo -e "${BLUE}📱 Frontend:${NC} http://localhost:3000"
echo -e "${BLUE}💳 Payment API:${NC} http://localhost:8080"
echo -e "${BLUE}📦 Order API:${NC} http://localhost:8081"
echo -e "${BLUE}🛍️ Products API:${NC} http://localhost:8082"
echo ""
echo -e "${YELLOW}🔑 Demo Credentials:${NC}"
echo "   Admin: admin/admin"
echo "   User:  user/user"
echo ""
echo -e "${YELLOW}📋 Logs are available in the logs/ directory${NC}"
echo -e "${YELLOW}🛑 To stop all services, run: ./stop-all.sh${NC}"
echo ""
echo -e "${GREEN}Happy shopping! 🛒${NC}"

# Keep script running
wait 