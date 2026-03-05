#!/bin/bash

# Ensure Java and Maven are installed
if ! command -v java &> /dev/null; then
    echo "Java not found. Installing..."
    sudo apt-get update
    sudo apt-get install -y openjdk-17-jdk
fi

if ! command -v mvn &> /dev/null; then
    echo "Maven not found. Installing..."
    sudo apt-get install -y maven
fi

# Start Backend
echo "Starting Backend..."
cd backend
mvn spring-boot:run > ../backend.log 2>&1 &
BACKEND_PID=$!
echo "Backend started with PID $BACKEND_PID. Logs in backend.log"
cd ..

# Start Frontend
echo "Starting Frontend..."
cd frontend
if [ ! -d "node_modules" ]; then
    echo "Installing frontend dependencies..."
    npm install
fi
npm run dev > ../frontend.log 2>&1 &
FRONTEND_PID=$!
echo "Frontend started with PID $FRONTEND_PID. Logs in frontend.log"
cd ..

echo "Services are starting..."
echo "Backend: http://localhost:8080"
echo "Frontend: http://localhost:5173"

# Wait for user to exit
read -p "Press Enter to stop..."

kill $BACKEND_PID
kill $FRONTEND_PID
