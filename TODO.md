# TODO: Add Player Entity to Grid

## Implementation Checklist

### 1. Create Player Entity Class
- [ ] Create `src/main/java/com/simulationrunner/entity/` directory
- [ ] Create `Player.java` class in the entity package
- [ ] Add fields: `gridX` and `gridY` (int) for grid coordinates
- [ ] Implement constructor `Player(GridConfig config)` that:
  - [ ] Uses `Random` to generate random grid position
  - [ ] Validates position is within grid bounds (0 to gridWidth-1, 0 to gridHeight-1)
- [ ] Add getter methods: `getGridX()` and `getGridY()`
- [ ] Implement `render(GraphicsContext gc, GridConfig config)` method that:
  - [ ] Calculates pixel position from grid coordinates
  - [ ] Draws a blue circle at 60% of cell size
  - [ ] Centers circle in the grid cell

### 2. Update Grid Class
- [ ] Add `private final Player player` field to `Grid.java`
- [ ] Initialize player in Grid constructor: `this.player = new Player(config)`
- [ ] Add `getPlayer()` getter method
- [ ] Update Grid constructor to pass config to Player

### 3. Update App Class
- [ ] After grid line rendering, add player rendering
- [ ] Call `grid.getPlayer().render(gc, config)` to draw the player
- [ ] Ensure player renders on top of grid lines

### 4. Write Unit Tests
- [ ] Create `src/test/java/com/simulationrunner/entity/` directory
- [ ] Create `PlayerTest.java` test class
- [ ] Test: Player spawns within grid bounds
- [ ] Test: `getGridX()` and `getGridY()` return valid coordinates
- [ ] Test: Constructor with different grid sizes
- [ ] Test: Verify random spawn distribution (optional)

### 5. Testing & Validation
- [ ] Run all tests: `./mvnw test`
- [ ] Build project: `./mvnw clean package`
- [ ] Run application: `./mvnw javafx:run`
- [ ] Visual verification:
  - [ ] Blue circle appears on the grid
  - [ ] Circle is centered in a grid cell
  - [ ] Circle size is approximately 60% of cell size
  - [ ] Each run spawns player at different random location

## Technical Details

**Player Rendering:**
- Color: `Color.BLUE`
- Circle diameter: `cellSize * 0.6`
- Position: Center of grid cell at coordinates (gridX, gridY)
- Pixel calculation:
  - centerX = (gridX * cellSize) + (cellSize / 2)
  - centerY = (gridY * cellSize) + (cellSize / 2)
  - radius = (cellSize * 0.6) / 2

**Future Enhancement (Not in this TODO):**
- WASD keyboard input for player movement
- Collision detection
- Multiple entity types (Key, Door, Enemy)
