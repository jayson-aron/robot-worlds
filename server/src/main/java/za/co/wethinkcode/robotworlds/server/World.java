package za.co.wethinkcode.robotworlds.server;


import za.co.wethinkcode.robotworlds.protocols.ResponseProtocol;
import za.co.wethinkcode.robotworlds.server.servercommands.ServerCommands;

import java.util.*;

public class World {
    private final ObstacleGenerator obstacles;
    private final PitsGenerator pits;
    private final Mines mines;
    private final RobotHandler robotHandler;
    private final Random randomCoordinate;
    private final ConfigHandler configurations;
    private final Position TOP_LEFT;
    private final Position BOTTOM_RIGHT;

    public World(ConfigHandler configurations) {
        this.robotHandler = new RobotHandler();
        this.randomCoordinate = new Random();
        this.configurations = configurations;
        this.TOP_LEFT = configurations.getTopLeftPosition();
        this.BOTTOM_RIGHT = configurations.getBottomRightPosition();
        this.obstacles = new ObstacleGenerator(TOP_LEFT, BOTTOM_RIGHT);
        this.pits = new PitsGenerator(TOP_LEFT, BOTTOM_RIGHT);
        this.mines = new Mines();
    }

    public ResponseProtocol handleCommand(Command command, SimpleServer simpleServer) {
        return command.execute(this, simpleServer);
    }

    public ConfigHandler getConfigurations() {
        return this.configurations;
    }

    public boolean handleCommand(ServerCommands command) {
        return command.execute(this);
    }

    private Position getRandomPosition() {
        Position position;

        while (true) {
            int xCoordinate = this.TOP_LEFT.getX()
                    + randomCoordinate.nextInt(2 * this.BOTTOM_RIGHT.getX());
            int yCoordinate = this.BOTTOM_RIGHT.getY()
                    + randomCoordinate.nextInt(2 * this.TOP_LEFT.getY());
            position = new Position(xCoordinate, yCoordinate);

            if (pits.positionBlocked(position)
                    || obstacles.positionBlocked(position)
                    || robotHandler.positionBlocked(position)
                    || mines.positionBlocked(position))
                continue;
            break;
        }

        return position;
    }

    public void addRobot(String name, int shieldStrength, int shots, SimpleServer server) {
        if (shieldStrength > configurations.getMaxShieldStrength())
            shieldStrength = configurations.getMaxShieldStrength();
        this.robotHandler.addRobot(new Robot(name, shieldStrength, shots, getRandomPosition(), server));
    }

    public void setRobotDirection(String name, String argument) {
        Robot robot = getRobot(name);
        Direction direction;

        if (argument.equalsIgnoreCase("right")) {
            switch (robot.getCurrentDirection()) {
                case NORTH:
                    direction = Direction.EAST;
                    break;
                case EAST:
                    direction = Direction.SOUTH;
                    break;
                case SOUTH:
                    direction = Direction.WEST;
                    break;
                default:
                    direction = Direction.NORTH;
            }
        }
        else
            switch (robot.getCurrentDirection()) {
                case NORTH:
                    direction = Direction.WEST;
                    break;
                case WEST:
                    direction = Direction.SOUTH;
                    break;
                case SOUTH:
                    direction = Direction.EAST;
                    break;
                default:
                    direction = Direction.NORTH;
            }
        robot.setDirection(direction);
    }

    public UpdateResponse updateRobotPosition(String name, int nrSteps) {
        Robot robot = getRobot(name);

        int newX = robot.getCurrentPosition().getX();
        int newY = robot.getCurrentPosition().getY();

        if (Direction.NORTH.equals(robot.getCurrentDirection()))
            newY = newY + nrSteps;
        else if (Direction.EAST.equals(robot.getCurrentDirection()))
            newX = newX + nrSteps;
        else if (Direction.SOUTH.equals(robot.getCurrentDirection()))
            newY = newY - nrSteps;
        else
            newX = newX - nrSteps;

        Position newPosition = new Position(newX, newY);
        Position currentPosition = robot.getCurrentPosition();
        return move(robot, currentPosition, newPosition);
    }

    public boolean robotExists(String name) {
        return this.robotHandler.robotExists(name);
    }

    public boolean capacityReached() {
        return (this.robotHandler.getRobotCount() == 10);
    }

    public Robot getRobot(String name) {
        return this.robotHandler.getRobot(name);
    }

    public SimpleServer getRobotServer(String name) {
        return getRobot(name).getServer();
    }

    public ArrayList<Robot> getAllRobots() {
        return robotHandler.getAllRobots();
    }

    public int getVisibility() {
        return this.configurations.getVisibility();
    }

    public List<Obstacle> getAllObstacles() {
        return obstacles.getObstacles();
    }

    public List<Obstacle> getAllPits() {
        return pits.getPits();
    }

    public List<Position> getAllMines() {
        return mines.getMines();
    }

    public Map<String, Object> getRobotState(String name) {
        Map<String, Object> state = new HashMap<>();
        Robot robot = getRobot(name);

        state.put("position", robot.getPositionArray());
        state.put("direction", robot.getCurrentDirection().toString());
        state.put("shields", robot.getShieldStrength());
        state.put("shots", robot.getShots());
        state.put("status", robot.getStatus().toString());

        return state;
    }

    public OperationalMode getRobotStatus(String name) {
        return getRobot(name).getStatus();
    }

    public ArrayList<Integer> getRobotArrayPosition(String name) {
        return getRobot(name).getPositionArray();
    }

    public void setMine(String name) {
        Robot robot = getRobot(name);
        robot.setShieldStrength(0);
        mines.placeMine(robot.getCurrentPosition());
        robot.setStatus(OperationalMode.SETMINE);
    }

    public boolean robotCanPlaceMines(String name) {
        return (getRobot(name).getDefaultShots() == 0);
    }

    public void removeRobot(String name) {
        robotHandler.removeRobot(getRobot(name));
    }

    public String getTargetRobotName(String name) {
        return getRobot(name).getTargetName();
    }

    public int getRobotCount() {
        return this.robotHandler.getRobotCount();
    }

    public int getMineSetTime() {
        return configurations.getMineSetTime();
    }

    public int getRobotShieldStrength(String name) {
        return getRobot(name).getShieldStrength();
    }

    public void setRobotShieldStrength(String name, int shieldStrength) {
        getRobot(name).setShieldStrength(shieldStrength);
    }

    public void repairRobotShield(String name) {
        Robot robot = getRobot(name);
        robot.setStatus(OperationalMode.REPAIR);
        robot.resetShieldStrength();
    }

    public void reloadRobotWeapons(String name) {
        Robot robot = getRobot(name);
        robot.setStatus(OperationalMode.RELOAD);
        robot.resetShots();
    }

    public int getRobotShots(String name) {
        return getRobot(name).getShots();
    }

    public UpdateResponse shootABullet(String name) {
        Robot robot = getRobot(name);
        int x = robot.getCurrentPosition().getX();
        int y = robot.getCurrentPosition().getY();
        int shotSteps = robot.getShotSteps();
        robot.setStatus(OperationalMode.NORMAL);

        switch (robot.getCurrentDirection()) {
            case NORTH:
                return positiveYShot(robot, y + 1, y + shotSteps, x);
            case EAST:
                return positiveXShot(robot, x + 1, x + shotSteps, y);
            case WEST:
                return negativeXShot(robot, x - 1, x - shotSteps, y);
            default:
                return negativeYShot(robot, y - 1, y - shotSteps, x);
        }
    }

    public void robotShotABullet(String name) {
        getRobot(name).lostShot();
    }

    public int getShieldRepairTime() {
        return configurations.getShieldRepairTime();
    }

    public int getWeaponsReloadTime() {
        return configurations.getWeaponReloadTime();
    }

    private UpdateResponse move(Robot robot, Position current, Position destination) {
        int x1 = current.getX();
        int y1 = current.getY();
        int x2 = destination.getX();
        int y2 = destination.getY();

        if (x1 == x2)
            return (y1 < y2) ? positiveYMovement(robot, y1 + 1, y2, x1) : negativeYMovement(robot, y1 - 1, y2, x1);
        else
            return (x1 < x2) ? positiveXMovement(robot, x1 + 1, x2, y1) : negativeXMovement(robot , x1 - 1, x2, y1);
    }

    public ArrayList<Map<String, Object>> lookAroundTheWorld(String robotName) {
        ArrayList<Map<String, Object>> objects = new ArrayList<>();
        Map<String, Object> objectState;
        Robot robot = getRobot(robotName);
        Position currentPosition = robot.getCurrentPosition();
        int x1 = currentPosition.getX();
        int y1 = currentPosition.getY();


        objectState = northView(y1 + 1, y1 + getVisibility(), x1);
        if(!objectState.isEmpty())
            objects.add(objectState);
        objectState = southView(y1 - 1, y1 - getVisibility(), x1);
        if(!objectState.isEmpty())
            objects.add(objectState);
        objectState = eastView(x1 + 1, x1 + getVisibility(), y1);
        if(!objectState.isEmpty())
            objects.add(objectState);
        objectState = westView(x1 - 1, x1 - getVisibility(), y1);
        if(!objectState.isEmpty())
            objects.add(objectState);

        return objects;
    }

    private UpdateResponse robotHitByMineResponse(Robot robot, Position position) {
        if ((robot.getShieldStrength() - 3) < 0) {
            robot.setShieldStrength(0);
            robot.setStatus(OperationalMode.DEAD);
            robot.setPosition(position);
            return UpdateResponse.MINE;
        } else {
            robot.setShieldStrength(robot.getShieldStrength() - 3);
            robot.setStatus(OperationalMode.NORMAL);
            robot.setPosition(position);
            return UpdateResponse.MINEBUTALIVE;
        }
    }

    private UpdateResponse robotObstructedResponse(Robot robot, Position position) {
        robot.setStatus(OperationalMode.NORMAL);
        robot.setPosition(position);
        return UpdateResponse.OBSTRUCTED;
    }

    private UpdateResponse robotFellInAPitResponse(Robot robot, Position position) {
        robot.setStatus(OperationalMode.DEAD);
        robot.setPosition(position);
        return UpdateResponse.FELL;
    }

    private UpdateResponse robotMovedUnobstructedResponse(Robot robot, Position position) {
        robot.setPosition(position);
        robot.setStatus(OperationalMode.NORMAL);
        return UpdateResponse.SUCCESS;
    }

    private UpdateResponse positiveYShot(Robot robot, int y1, int y2, int x1) {
        Position position;

        while(y1 <= y2) {
            position = new Position(x1, y1);
            if (obstacles.positionBlocked(position)) {
                return UpdateResponse.MISS;
            } else if (robotHandler.positionBlocked(position)) {
                robot.setTargetName(robotHandler.getRobotAt(position).getName());
                return UpdateResponse.HIT;
            } else if (!position.isIn(TOP_LEFT, BOTTOM_RIGHT)) {
                return UpdateResponse.MISS;
            }
            y1 += 1;
        }
        return UpdateResponse.MISS;
    }

    private UpdateResponse positiveXShot(Robot robot, int x1, int x2, int y1) {
        Position position;

        while(x1 <= x2) {
            position = new Position(x1, y1);
            if (obstacles.positionBlocked(position)) {
                return UpdateResponse.MISS;
            } else if (robotHandler.positionBlocked(position)) {
                robot.setTargetName(robotHandler.getRobotAt(position).getName());
                return UpdateResponse.HIT;
            } else if (!position.isIn(TOP_LEFT, BOTTOM_RIGHT)) {
                return UpdateResponse.MISS;
            }
            x1 += 1;
        }
        return UpdateResponse.MISS;
    }

    private UpdateResponse negativeYShot(Robot robot, int y1, int y2, int x1) {
        Position position;

        while(y1 >= y2) {
            position = new Position(x1, y1);
            if (obstacles.positionBlocked(position)) {
                return UpdateResponse.MISS;
            } else if (robotHandler.positionBlocked(position)) {
                robot.setTargetName(robotHandler.getRobotAt(position).getName());
                return UpdateResponse.HIT;
            } else if (!position.isIn(TOP_LEFT, BOTTOM_RIGHT)) {
                return UpdateResponse.MISS;
            }
            y1 -= 1;
        }
        return UpdateResponse.MISS;
    }

    private UpdateResponse negativeXShot(Robot robot, int x1, int x2, int y1) {
        Position position;

        while(x1 >= x2) {
            position = new Position(x1, y1);
            if (obstacles.positionBlocked(position)) {
                return UpdateResponse.MISS;
            } else if (robotHandler.positionBlocked(position)) {
                robot.setTargetName(robotHandler.getRobotAt(position).getName());
                return UpdateResponse.HIT;
            } else if (!position.isIn(TOP_LEFT, BOTTOM_RIGHT)) {
                return UpdateResponse.MISS;
            }
            x1 -= 1;
        }
        return UpdateResponse.MISS;
    }

    private Map<String, Object> northView(int y1, int y2, int x1) {
        Map<String, Object> objectState = new HashMap<>();
        Position position;
        int distance = 0;

        while(y1 <= y2) {
            position = new Position(x1, y1);
            if (obstacles.positionBlocked(position)) {
                objectState.put("direction", Direction.NORTH);
                objectState.put("type", ObjectType.OBSTACLE);
                objectState.put("distance", distance);
                return objectState;
            }
            else if (pits.positionBlocked(position)) {
                objectState.put("direction", Direction.NORTH);
                objectState.put("type", ObjectType.PIT);
                objectState.put("distance", distance);
                return objectState;
            }
            else if(mines.positionBlocked(position)){
                objectState.put("direction", Direction.NORTH);
                objectState.put("type", ObjectType.MINE);
                objectState.put("distance", distance);
                return objectState;
            }
            else if(robotHandler.positionBlocked(position)) {
                objectState.put("direction", Direction.NORTH);
                objectState.put("type", ObjectType.ROBOT);
                objectState.put("distance", distance);
                return objectState;
            }
            else if(!position.isIn(TOP_LEFT, BOTTOM_RIGHT)) {
                objectState.put("direction", Direction.NORTH);
                objectState.put("type", ObjectType.EDGE);
                objectState.put("distance", distance);
                return objectState;
            }
            y1 += 1;
            distance +=1;
        }
        return objectState;
    }

    private Map<String, Object> eastView(int x1, int x2, int y1) {
        Map<String, Object> objectState = new HashMap<>();
        Position position;
        int distance = 0;

        while(x1 <= x2) {
            position = new Position(x1, y1);
            if (obstacles.positionBlocked(position)) {
                objectState.put("direction", Direction.EAST);
                objectState.put("type", ObjectType.OBSTACLE);
                objectState.put("distance", distance);
                return objectState;
            }
            else if (pits.positionBlocked(position)) {
                objectState.put("direction", Direction.EAST);
                objectState.put("type", ObjectType.PIT);
                objectState.put("distance", distance);
                return objectState;
            }
            else if(mines.positionBlocked(position)){
                objectState.put("direction", Direction.EAST);
                objectState.put("type", ObjectType.MINE);
                objectState.put("distance", distance);
                return objectState;
            }
            else if(robotHandler.positionBlocked(position)) {
                objectState.put("direction", Direction.EAST);
                objectState.put("type", ObjectType.ROBOT);
                objectState.put("distance", distance);
                return objectState;
            }
            else if(!position.isIn(TOP_LEFT, BOTTOM_RIGHT)) {
                objectState.put("direction", Direction.EAST);
                objectState.put("type", ObjectType.EDGE);
                objectState.put("distance", distance);
                return objectState;
            }
            x1 += 1;
            distance +=1;
        }
        return objectState;
    }

    private Map<String, Object> westView(int x1, int x2, int y1) {
        Map<String, Object> objectState = new HashMap<>();
        Position position;
        int distance = 0;

        while(x1 >= x2) {
            position = new Position(x1, y1);
            if (obstacles.positionBlocked(position)) {
                objectState.put("direction", Direction.WEST);
                objectState.put("type", ObjectType.OBSTACLE);
                objectState.put("distance", distance);
                return objectState;
            }
            else if (pits.positionBlocked(position)) {
                objectState.put("direction", Direction.WEST);
                objectState.put("type", ObjectType.PIT);
                objectState.put("distance", distance);
                return objectState;
            }
            else if(mines.positionBlocked(position)){
                objectState.put("direction", Direction.WEST);
                objectState.put("type", ObjectType.MINE);
                objectState.put("distance", distance);
                return objectState;
            }
            else if(robotHandler.positionBlocked(position)) {
                objectState.put("direction", Direction.WEST);
                objectState.put("type", ObjectType.ROBOT);
                objectState.put("distance", distance);
                return objectState;
            }
            else if(!position.isIn(TOP_LEFT, BOTTOM_RIGHT)) {
                objectState.put("direction", Direction.WEST);
                objectState.put("type", ObjectType.EDGE);
                objectState.put("distance", distance);
                return objectState;
            }
            x1 -= 1;
            distance +=1;
        }
        return objectState;
    }

    private Map<String, Object> southView(int y1, int y2, int x1) {
        Map<String, Object> objectState = new HashMap<>();
        Position position;
        int distance = 0;

        while(y1 >= y2) {
            position = new Position(x1, y1);
            if (obstacles.positionBlocked(position)) {
                objectState.put("direction", Direction.SOUTH);
                objectState.put("type", ObjectType.OBSTACLE);
                objectState.put("distance", distance);
                return objectState;
            }
            else if (pits.positionBlocked(position)) {
                objectState.put("direction", Direction.SOUTH);
                objectState.put("type", ObjectType.PIT);
                objectState.put("distance", distance);
                return objectState;
            }
            else if(mines.positionBlocked(position)){
                objectState.put("direction", Direction.SOUTH);
                objectState.put("type", ObjectType.MINE);
                objectState.put("distance", distance);
                return objectState;
            }
            else if(robotHandler.positionBlocked(position)) {
                objectState.put("direction", Direction.SOUTH);
                objectState.put("type", ObjectType.ROBOT);
                objectState.put("distance", distance);
                return objectState;
            }
            else if(!position.isIn(TOP_LEFT, BOTTOM_RIGHT)) {
                objectState.put("direction", Direction.SOUTH);
                objectState.put("type", ObjectType.EDGE);
                objectState.put("distance", distance);
                return objectState;
            }
            y1 -= 1;
            distance +=1;
        }
        return objectState;
    }

    private UpdateResponse positiveYMovement(Robot robot, int y1, int y2, int x1) {
        Position position;

        while(y1 <= y2) {
            position = new Position(x1, y1);
            if ((obstacles.positionBlocked(position) || (!position.isIn(TOP_LEFT, BOTTOM_RIGHT)))
                    && mines.positionBlocked(new Position(x1, y1 - 1)))
                return robotHitByMineResponse(robot, new Position(x1, y1 - 1));
            else if (obstacles.positionBlocked(position))
                return robotObstructedResponse(robot, new Position(x1, y1 - 1));
            else if (robotHandler.positionBlocked(position))
                return robotObstructedResponse(robot, new Position(x1, y1 - 1));
            else if (pits.positionBlocked(position))
                return robotFellInAPitResponse(robot, position);
            else if (mines.positionBlocked(position)) {
                return robotHitByMineResponse(robot, position);
            } else if (!position.isIn(TOP_LEFT, BOTTOM_RIGHT))
                return robotObstructedResponse(robot, new Position(x1, y1 - 1));
            y1 += 1;
        }
        return robotMovedUnobstructedResponse(robot, new Position(x1, y2));
    }

    private UpdateResponse negativeYMovement(Robot robot, int y1, int y2, int x1) {
        Position position;

        while(y1 >= y2) {
            position = new Position(x1, y1);
            if ((obstacles.positionBlocked(position) || (!position.isIn(TOP_LEFT, BOTTOM_RIGHT)))
                    && mines.positionBlocked(new Position(x1, y1 + 1)))
                return robotHitByMineResponse(robot, new Position(x1, y1 + 1));
            else if (obstacles.positionBlocked(position))
                return robotObstructedResponse(robot, new Position(x1, y1 + 1));
            else if (robotHandler.positionBlocked(position))
                return robotObstructedResponse(robot, new Position(x1, y1 + 1));
            else if (pits.positionBlocked(position))
                return robotFellInAPitResponse(robot, position);
            else if (mines.positionBlocked(position))
                return robotHitByMineResponse(robot, position);
            else if (!position.isIn(TOP_LEFT, BOTTOM_RIGHT))
                return robotObstructedResponse(robot, new Position(x1, y1 + 1));
            y1 -= 1;
        }
        return robotMovedUnobstructedResponse(robot, new Position(x1, y2));
    }

    private UpdateResponse positiveXMovement(Robot robot, int x1, int x2, int y1) {
        Position position;

        while(x1 <= x2) {
            position = new Position(x1, y1);
            if ((obstacles.positionBlocked(position) || (!position.isIn(TOP_LEFT, BOTTOM_RIGHT)))
                    && mines.positionBlocked(new Position(x1 - 1, y1)))
                return robotHitByMineResponse(robot, new Position(x1 - 1, y1));
            else if (obstacles.positionBlocked(position))
                return robotObstructedResponse(robot, new Position(x1 - 1, y1));
            else if (robotHandler.positionBlocked(position))
                return robotObstructedResponse(robot, new Position(x1 - 1, y1));
            else if (pits.positionBlocked(position))
                return robotFellInAPitResponse(robot, position);
            else if (mines.positionBlocked(position))
                return robotHitByMineResponse(robot, position);
            else if (!position.isIn(TOP_LEFT, BOTTOM_RIGHT))
                return robotObstructedResponse(robot, new Position(x1 - 1, y1));
            x1 += 1;
        }
        return robotMovedUnobstructedResponse(robot, new Position(x2, y1));
    }

    private UpdateResponse negativeXMovement(Robot robot, int x1, int x2, int y1) {
        Position position;

        while(x1 >= x2) {
            position = new Position(x1, y1);
            if ((obstacles.positionBlocked(position) || (!position.isIn(TOP_LEFT, BOTTOM_RIGHT)))
                    && mines.positionBlocked(new Position(x1 + 1, y1)))
                return robotHitByMineResponse(robot, new Position(x1 + 1, y1));
            else if (obstacles.positionBlocked(position))
                return robotObstructedResponse(robot, new Position(x1 + 1, y1));
            else if (robotHandler.positionBlocked(position))
                return robotObstructedResponse(robot, new Position(x1 + 1, y1));
            else if (pits.positionBlocked(position))
                return robotFellInAPitResponse(robot, position);
            else if (mines.positionBlocked(position))
                return robotHitByMineResponse(robot, position);
            else if (!position.isIn(TOP_LEFT, BOTTOM_RIGHT))
                return robotObstructedResponse(robot, new Position(x1 + 1, y1));
            x1 -= 1;
        }
        return robotMovedUnobstructedResponse(robot, new Position(x2, y1));
    }
}
