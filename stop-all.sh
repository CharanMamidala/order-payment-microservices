#!/bin/bash

# All-in-One Store Microservices Stop Script
# This script stops all running services

echo "🛑 Stopping All-in-One Store Microservices..."
echo "=============================================="

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to stop service by PID file
stop_service() {
    local service_name=$1
    local pid_file="logs/$service_name.pid"
    
    if [ -f "$pid_file" ]; then
        local pid=$(cat "$pid_file")
        if ps -p $pid > /dev/null 2>&1; then
            echo -e "${YELLOW}🛑 Stopping $service_name (PID: $pid)...${NC}"
            kill $pid
            sleep 2
            
            # Force kill if still running
            if ps -p $pid > /dev/null 2>&1; then
                echo -e "${YELLOW}⚠️ Force killing $service_name...${NC}"
                kill -9 $pid
            fi
            
            echo -e "${GREEN}✅ $service_name stopped${NC}"
        else
            echo -e "${YELLOW}⚠️ $service_name was not running${NC}"
        fi
        rm -f "$pid_file"
    else
        echo -e "${YELLOW}⚠️ No PID file found for $service_name${NC}"
    fi
}

# Stop all services
stop_service "payment-service"
stop_service "order-service"
stop_service "products-service"
stop_service "frontend"

# Alternative: Kill processes by port
echo -e "${BLUE}🔍 Checking for any remaining processes...${NC}"

# Kill processes on specific ports
for port in 3000 8080 8081 8082; do
    pid=$(lsof -ti:$port 2>/dev/null)
    if [ ! -z "$pid" ]; then
        echo -e "${YELLOW}🛑 Killing process on port $port (PID: $pid)...${NC}"
        kill -9 $pid 2>/dev/null
    fi
done

echo ""
echo -e "${GREEN}🎉 All services stopped successfully!${NC}"
echo "=============================================="
echo -e "${YELLOW}📋 Logs are still available in the logs/ directory${NC}"
echo -e "${YELLOW}🚀 To start services again, run: ./start-all.sh${NC}" 