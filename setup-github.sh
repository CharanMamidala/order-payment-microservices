#!/bin/bash

# GitHub Repository Setup Script
# This script helps you set up your GitHub repository for the Order Payment Microservices project

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}🚀 Setting up GitHub Repository for Order Payment Microservices${NC}"
echo ""

# Check if git is installed
if ! command -v git &> /dev/null; then
    echo -e "${RED}❌ Git is not installed. Please install Git first.${NC}"
    exit 1
fi

# Check if we're in a git repository
if [ ! -d ".git" ]; then
    echo -e "${YELLOW}📁 Initializing Git repository...${NC}"
    git init
    echo -e "${GREEN}✅ Git repository initialized${NC}"
else
    echo -e "${GREEN}✅ Git repository already exists${NC}"
fi

# Add all files
echo -e "${YELLOW}📝 Adding files to Git...${NC}"
git add .

# Initial commit
echo -e "${YELLOW}💾 Creating initial commit...${NC}"
git commit -m "feat: initial commit - Order Payment Microservices

- Add Spring Boot microservices (Order, Payment, Products)
- Add responsive frontend with modern UI/UX
- Add Docker configuration for containerization
- Add ECS deployment configuration
- Add comprehensive documentation
- Add GitHub Actions CI/CD pipeline
- Add issue and PR templates"

echo -e "${GREEN}✅ Initial commit created${NC}"

# Ask for GitHub repository URL
echo ""
echo -e "${BLUE}🔗 GitHub Repository Setup${NC}"
echo "Please provide your GitHub repository URL:"
echo "Example: https://github.com/yourusername/order-payment-microservices.git"
read -p "GitHub URL: " github_url

if [ -z "$github_url" ]; then
    echo -e "${YELLOW}⚠️  No URL provided. You can add the remote later with:${NC}"
    echo "git remote add origin <your-github-url>"
    echo "git branch -M main"
    echo "git push -u origin main"
else
    # Add remote origin
    echo -e "${YELLOW}🔗 Adding remote origin...${NC}"
    git remote add origin "$github_url"
    
    # Rename branch to main
    echo -e "${YELLOW}🌿 Renaming branch to main...${NC}"
    git branch -M main
    
    # Push to GitHub
    echo -e "${YELLOW}📤 Pushing to GitHub...${NC}"
    git push -u origin main
    
    echo -e "${GREEN}✅ Repository pushed to GitHub successfully!${NC}"
fi

echo ""
echo -e "${BLUE}📋 Next Steps:${NC}"
echo "1. ${YELLOW}Set up GitHub Secrets${NC}:"
echo "   - Go to your repository Settings > Secrets and variables > Actions"
echo "   - Add the following secrets:"
echo "     - AWS_ACCESS_KEY_ID"
echo "     - AWS_SECRET_ACCESS_KEY"
echo ""
echo "2. ${YELLOW}Update Configuration${NC}:"
echo "   - Edit deploy-to-ecs.sh and update AWS_REGION and AWS_ACCOUNT_ID"
echo "   - Update README.md with your actual GitHub username"
echo "   - Update .github/workflows/ci-cd.yml if needed"
echo ""
echo "3. ${YELLOW}Test the Setup${NC}:"
echo "   - Run: docker-compose up --build"
echo "   - Access: http://localhost:3000"
echo ""
echo "4. ${YELLOW}Enable GitHub Actions${NC}:"
echo "   - Go to Actions tab in your repository"
echo "   - Enable GitHub Actions if prompted"
echo ""
echo -e "${GREEN}🎉 Your GitHub repository is ready!${NC}"
echo ""
echo -e "${BLUE}📚 Useful Commands:${NC}"
echo "docker-compose up --build    # Start all services"
echo "docker-compose down          # Stop all services"
echo "git status                   # Check repository status"
echo "git log --oneline           # View commit history"
echo ""
echo -e "${BLUE}🔗 Repository URL:${NC} $github_url" 