# MongoDB Local Setup Guide

This guide explains how to run MongoDB locally for development.

## Option 1: Using Homebrew (Recommended for macOS)

### Installation

1. **Install MongoDB Community Edition:**
   ```bash
   brew tap mongodb/brew
   brew install mongodb-community
   ```

2. **Start MongoDB as a service:**
   ```bash
   brew services start mongodb-community
   ```

3. **Or start MongoDB manually:**
   ```bash
   mongod --config /opt/homebrew/etc/mongod.conf
   ```

### Verify Installation

Check if MongoDB is running:
```bash
# Check service status
brew services list | grep mongodb

# Or test connection
mongosh
# If connected, you'll see: "Current Mongosh Log ID: ..."
# Type 'exit' to leave
```

### Stop MongoDB

```bash
# Stop the service
brew services stop mongodb-community

# Or if running manually, press Ctrl+C
```

## Option 2: Using Docker (Cross-platform)

### Installation

1. **Pull MongoDB image:**
   ```bash
   docker pull mongo:latest
   ```

2. **Run MongoDB container:**
   ```bash
   docker run -d \
     --name mongodb \
     -p 27017:27017 \
     -v mongodb-data:/data/db \
     mongo:latest
   ```

3. **Verify it's running:**
   ```bash
   docker ps | grep mongodb
   ```

### Stop MongoDB Container

```bash
# Stop the container
docker stop mongodb

# Remove the container (optional)
docker rm mongodb

# Remove the volume (optional, deletes all data)
docker volume rm mongodb-data
```

### Start Existing Container

```bash
docker start mongodb
```

## Option 3: Manual Installation

1. **Download MongoDB:**
   - Visit: https://www.mongodb.com/try/download/community
   - Select: macOS, Community Server
   - Download and extract

2. **Create data directory:**
   ```bash
   mkdir -p /data/db
   sudo chown $(whoami) /data/db
   ```

3. **Start MongoDB:**
   ```bash
   /path/to/mongodb/bin/mongod
   ```

## Configuration for This Project

Your Spring Boot application is configured to connect to:
- **Host:** `localhost`
- **Port:** `27017` (default)
- **Database:** `springbootdb`

This is set in `src/main/resources/application.properties`:
```properties
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=springbootdb
```

## Verify MongoDB Connection

### Using MongoDB Shell (mongosh)

```bash
# Connect to MongoDB
mongosh

# Or connect to specific database
mongosh springbootdb

# List databases
show dbs

# Use your database
use springbootdb

# List collections
show collections

# Query users collection
db.users.find().pretty()

# Exit
exit
```

### Using MongoDB Compass (GUI Tool)

1. **Download MongoDB Compass:**
   - Visit: https://www.mongodb.com/products/compass
   - Download and install

2. **Connect:**
   - Connection string: `mongodb://localhost:27017`
   - Or use: `mongodb://localhost:27017/springbootdb`

## Troubleshooting

### MongoDB won't start

1. **Check if port 27017 is already in use:**
   ```bash
   lsof -i :27017
   ```

2. **Check MongoDB logs:**
   ```bash
   # Homebrew
   tail -f /opt/homebrew/var/log/mongodb/mongo.log
   
   # Docker
   docker logs mongodb
   ```

3. **Check permissions on data directory:**
   ```bash
   ls -la /data/db
   # or for Homebrew
   ls -la /opt/homebrew/var/mongodb
   ```

### Connection refused

- Ensure MongoDB is running: `brew services list` or `docker ps`
- Check firewall settings
- Verify port 27017 is not blocked

### Reset MongoDB (Delete all data)

**Homebrew:**
```bash
brew services stop mongodb-community
rm -rf /opt/homebrew/var/mongodb/*
brew services start mongodb-community
```

**Docker:**
```bash
docker stop mongodb
docker rm mongodb
docker volume rm mongodb-data
docker run -d --name mongodb -p 27017:27017 -v mongodb-data:/data/db mongo:latest
```

## Quick Start Commands

### Homebrew
```bash
# Start
brew services start mongodb-community

# Stop
brew services stop mongodb-community

# Restart
brew services restart mongodb-community

# Check status
brew services list | grep mongodb
```

### Docker
```bash
# Start
docker start mongodb

# Stop
docker stop mongodb

# View logs
docker logs mongodb

# Remove everything
docker stop mongodb && docker rm mongodb && docker volume rm mongodb-data
```

## Testing Your Spring Boot Connection

Once MongoDB is running, start your Spring Boot application:

```bash
mvn spring-boot:run
```

You should see in the logs:
```
Connected to MongoDB at localhost:27017
```

If you see connection errors, verify:
1. MongoDB is running
2. Port 27017 is accessible
3. No firewall blocking the connection

## Next Steps

After MongoDB is running:
1. Start your Spring Boot application: `mvn spring-boot:run`
2. Test the API endpoints (see README.md)
3. Use MongoDB Compass to view your data visually


