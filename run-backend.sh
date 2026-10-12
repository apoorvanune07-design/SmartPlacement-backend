#!/usr/bin/env bash
echo "============================================================"
echo "Starting Smart Placement & Internship Management System Backend"
echo "============================================================"

if command -v mvn &> /dev/null
then
    mvn spring-boot:run
else
    echo "Maven (mvn) not found in PATH. Please install Maven and Java 17+."
    exit 1
fi
