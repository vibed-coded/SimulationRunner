# SimulationRunner

A 2D grid-based game built with JavaFX where an entity navigates to find a key and unlock a door.

## Requirements

- Java 25 (OpenJDK recommended)
- Maven 3.9+ (or use the included Maven wrapper)

## Building and Running

This project includes the Maven wrapper, so you don't need to install Maven separately.

### Using Maven Wrapper (Recommended)

**Unix/Linux/macOS:**
```bash
# Build the project
./mvnw clean package

# Run tests
./mvnw test

# Run the application
./mvnw javafx:run
```

**Windows:**
```cmd
# Build the project
mvnw.cmd clean package

# Run tests
mvnw.cmd test

# Run the application
mvnw.cmd javafx:run
```

### Using System Maven

If you have Maven installed:
```bash
# Build the project
mvn clean package

# Run tests
mvn test

# Run the application
mvn javafx:run
```

## Project Structure

```
SimulationRunner/
├── .mvn/wrapper/          # Maven wrapper configuration
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/simulationrunner/
│   │           ├── App.java           # Main JavaFX application
│   │           ├── SystemInfo.java    # System information utility
│   │           └── module-info.java   # Java module descriptor
│   └── test/
│       └── java/                      # Test sources (to be added)
├── pom.xml                # Maven project configuration
├── mvnw                   # Maven wrapper script (Unix/Linux/macOS)
└── mvnw.cmd               # Maven wrapper script (Windows)
```

## Technologies

- **Java 25** - Latest LTS version
- **JavaFX 25.0.1** - GUI framework
- **JUnit 5.11.4** - Testing framework
- **Maven 3.9.9** - Build tool (via wrapper)

## Development

The project uses Java 25 features and is configured with:
- Maven compiler plugin targeting Java 25
- JavaFX controls module
- JUnit 5 for unit testing
- Maven Surefire for test execution

## Installing Java 25

If you need to install Java 25, we recommend using SDKMAN:

```bash
# Install SDKMAN (if not already installed)
curl -s "https://get.sdkman.io" | bash

# Install Java 25
sdk install java 25-open

# Set as default
sdk default java 25-open
```

## License

This project is for educational purposes.
