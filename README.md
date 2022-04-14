# Robot-Worlds
Robot-Worlds is a peer-to-peer game with an open world where in players are robots. There are numerous robots to choose from such as recon, soldier, sniper e.t.c.
Players are free to move around the world and also shoot each other and set mines.

## Technology stack:
* `Java`
* `Maven`

## Requirements:
* `Java  - openjdk 11.0.x`
* `Maven - maven 3.6.x`

## Setup and Configuration:
In the Robot-Worlds root folder there are 2 configuration files , one for the client and one for the server
#### 1. Worlds_config:
The world configuration file handles:
* `The size of the world`
* `The range of visibility of the objects in the world`
* `The robot shield repair, weapon reload and mine set times, as well as the robots maximum shield strength`

#### 2. Client_config:
* `Configure IP Address & port for the game-server you will be connecting to `


## Running the game:
#### 1. Clean
* `mvn clean`

#### 2. Compile
* `mvn compile`

#### 3. Test
* `mvn test`

#### 4. Package
* `mvn package`

#### 5. Run The Server
* `java -jar server/target/server.jar`

#### 6. Run The Client(s)
* `java -jar client/target/client.jar`

## Testing & Functionality:
### Server(World):
* The world is a grid, the size should be configured using a configuration file.
* The range of visibility of objects in the world is configured in the server configuration file.
* The world is managed via a console interface on the server program, which will allow the program to accept issued commands.

### Client:
* The client program must connect to a world using the world’s IP address and port. A configuration file is used to specify the IP address and port of the server.
* A client program has the ability to launch a robot that can move around the world.
* The client can be programmed to have different makes (or models) of robots with different capability configurations.
* The robot has the ability to look around the world.
* The robot has attack and defense functionality.

