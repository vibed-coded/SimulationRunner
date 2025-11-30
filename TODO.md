# TODO: Key Entity Implementation

## Overview
Implement entity architecture with abstract Entity class, then create Key entity with List-based storage.

## Requirements
- Keys spawn at least 5 grid tiles away from player (Manhattan distance)
- Keys cannot spawn on same tile as player
- Keys render as gold rectangles at 30% cell size
- Automatic pickup when player moves onto key tile
- Key disappears after pickup (collected state)
- Support multiple keys with configurable count
- Keys must be 5+ tiles from player only (can be near each other)

## Implementation Steps

### 1. Create abstract Entity.java base class
**File:** `src/main/java/com/simulationrunner/entity/Entity.java`

- Abstract class with protected `gridX`, `gridY` fields
- Constructor with validation (non-negative coordinates, within bounds)
- Public getters: `getGridX()`, `getGridY()`
- Abstract method: `render(GraphicsContext, GridConfig)`
- Protected static helper: `manhattanDistance(int x1, int y1, int x2, int y2)`
- Implement equals(), hashCode() based on position
- Implement toString()

### 2. Refactor Player.java to extend Entity
**File:** `src/main/java/com/simulationrunner/entity/Player.java`

- Change `public class Player` to `public class Player extends Entity`
- Remove duplicate gridX/gridY fields (inherited from Entity)
- Remove duplicate getters (inherited)
- Keep Player-specific: `move()` method, `CIRCLE_SIZE_RATIO` constant
- Update constructor to call `super(gridX, gridY)`
- Keep `createRandom(GridConfig)` factory method
- Implement abstract `render(GraphicsContext, GridConfig)` method
- Update equals/hashCode if needed (may inherit from Entity)

### 3. Create Key.java extending Entity
**File:** `src/main/java/com/simulationrunner/entity/Key.java`

- Extend Entity with `private boolean collected` field
- Constructor: calls `super(gridX, gridY)`, initializes `collected = false`
- Static factory: `createRandomKeys(GridConfig config, Player player, int count)` returns `List<Key>`
  - Each key must be 5+ tiles from player (Manhattan distance)
  - Keys can be near each other (no inter-key distance constraint)
  - Fallback strategy for small grids (place at farthest possible point)
  - Max attempts limit to prevent infinite loops (e.g., 1000 per key)
- Implement `render(GraphicsContext, GridConfig)`:
  - Skip rendering if `collected == true`
  - Draw gold rectangle centered in grid cell
  - Rectangle size: `cellSize * RECTANGLE_SIZE_RATIO` (30%)
  - Color: `Color.GOLD`
- Methods:
  - `collect()` - sets `collected = true`
  - `isCollected()` - returns `collected`
- Constant: `RECTANGLE_SIZE_RATIO = 0.3`
- Implement toString() including collected state

### 4. Modify Grid.java for List<Key>
**File:** `src/main/java/com/simulationrunner/Grid.java`

- Add `private final List<Key> keys;` field
- Add constructor parameter: `int keyCount`
- Constructor signature: `public Grid(GridConfig config, int keyCount)`
- Initialize player first: `this.player = Player.createRandom(config);`
- Initialize keys: `this.keys = Key.createRandomKeys(config, player, keyCount);`
- Add getter: `public List<Key> getKeys()` returning `Collections.unmodifiableList(keys)`
- Keep existing player field and getter

### 5. Modify App.java
**File:** `src/main/java/com/simulationrunner/App.java`

**Update Grid construction:**
- Change from `new Grid(config)` to `new Grid(config, 3)` (or configurable number)

**Add checkKeyPickup() method:**
```java
private void checkKeyPickup() {
    Player player = grid.getPlayer();

    for (Key key : grid.getKeys()) {
        if (!key.isCollected() &&
            player.getGridX() == key.getGridX() &&
            player.getGridY() == key.getGridY()) {
            key.collect();
        }
    }
}
```

**Update keyboard event handler:**
- Call `checkKeyPickup()` after player movement, before `render()`

**Update render() method:**
- After rendering player, add loop to render all keys:
```java
for (Key key : grid.getKeys()) {
    key.render(gc, config);
}
```

### 6. Create/Update Tests

**Create EntityTest.java (optional):**
- Test abstract class behavior if needed
- May not be necessary if testing through concrete classes

**Update PlayerTest.java:**
**File:** `src/test/java/com/simulationrunner/entity/PlayerTest.java`

- Verify Player extends Entity
- Verify constructor calls super correctly
- Verify inherited getters work
- All existing tests should still pass

**Create KeyTest.java:**
**File:** `src/test/java/com/simulationrunner/entity/KeyTest.java`

- Constructor validation tests (negative coordinates, null config)
- `createRandomKeys()` tests:
  - `@RepeatedTest(50)` for distance constraint verification (all keys 5+ tiles from player)
  - Test with various grid sizes (20x20, 10x10)
  - Small grid edge cases (5x5, 6x6) where 5 tiles is impossible
  - Multiple keys (count=1, count=3, count=10)
  - Keys within bounds of grid
- Collection state tests:
  - Initial state is `isCollected() == false`
  - After `collect()`, `isCollected() == true`
- Rendering tests (if possible with mocked GraphicsContext)
- Null safety tests
- equals(), hashCode(), toString() tests

**Update/Create GridTest.java:**
**File:** `src/test/java/com/simulationrunner/Grid.java`

- Test Grid constructor with keyCount parameter
- Verify correct number of keys created
- Verify getKeys() returns unmodifiable list
- Verify keys are not null

### 7. Run Tests and Manual Verification

**Run all tests:**
```bash
./mvnw clean test
```

**Run specific test classes:**
```bash
./mvnw test -Dtest=KeyTest
./mvnw test -Dtest=PlayerTest
./mvnw test -Dtest=GridTest
```

**Manual testing:**
```bash
./mvnw javafx:run
```

**Verify:**
- Multiple gold rectangles (keys) appear on grid
- Keys are at least 5 tiles from player's starting position
- Player can move with WASD
- When player moves onto a key, the key disappears
- All keys can be collected

## Architecture Decisions

1. **Abstract Entity class** with shared gridX, gridY, and render() contract
2. **List<Key>** storage in Grid with configurable count
3. **Grid constructor parameter** for number of keys to spawn
4. **5-tile minimum from player only** (keys can be adjacent to each other)
5. **Manhattan distance** for spawn constraint checking (reflects 4-directional grid movement)
6. **Collected state** in Key (not removed from list, just skip rendering when collected)
7. **Color.GOLD and 30% size** for key rectangle rendering
8. **Automatic pickup** detection in App after player movement

## Future Enhancements (Not in Current Scope)

- Configurable key colors based on game mechanics
- Key-to-key distance constraints
- Key collection counter UI
- Key collection events/callbacks
- Door entity that requires keys
- Level system with varying key counts
- Key spawn animations
- Collection sound effects
