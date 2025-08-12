# RawInput Mod - Java 17+ Compatibility Version

This is a modified version of the RawInput mod that adds compatibility with Java 17+ while maintaining backward compatibility with Java 8.

## Changes Made

### 1. Reflection Utilities
Added a new `Fields` utility class in `mod.seanld.rawinput.reflect` package that provides safe reflection access for Java 12+ versions. This class handles the stricter reflection restrictions introduced in newer Java versions.

### 2. Mouse Helper Modification
Modified `RawMouseHelper.java` to use the new reflection utilities when accessing protected fields:
- `mouseXYChange()` method now uses reflection to safely access `deltaX` and `deltaY` fields
- `grabMouseCursor()` method uses reflection to reset mouse delta values

### 3. Input Handler Updates
Updated `RawInputHandler.java`:
- Added reflection-based access for toggling mouse helper instances
- Maintains backward compatibility with Java 8 through try-catch fallbacks

### 4. Main Class Modification
Modified `RawInput.java` to use reflection when setting the initial mouse helper.

### 5. Java 17+ Compatibility
Added `addon.gradle` configuration file that:
- Overrides Java version restrictions
- Adds necessary JVM arguments for reflection access
- Configures Java toolchain for Java 17 compatibility

## Building for Java 17+

To build this mod for Java 17+, you need to:

1. Ensure you have Java 17+ installed
2. Run the build with: `./gradlew build`
3. The addon.gradle file will automatically apply Java 17+ compatibility settings

## Runtime Requirements

When running with Java 17+, you may need to add these JVM arguments:
```
--add-opens java.base/java.lang=ALL-UNNAMED
--add-opens java.base/java.lang.reflect=ALL-UNNAMED
--add-opens java.base/java.util=ALL-UNNAMED
--add-opens java.desktop/sun.awt=ALL-UNNAMED
--add-opens java.desktop/sun.java2d=ALL-UNNAMED
--add-exports java.base/sun.nio.ch=ALL-UNNAMED
--add-exports jdk.unsupported/sun.misc=ALL-UNNAMED
```

## Compatibility

This version maintains full compatibility with:
- Minecraft 1.7.10
- Java 8 (original target)
- Java 17+ (newly added support)
- GTNH (GregTech New Horizons) modpack

## Usage

The mod functions exactly the same as the original:
- `/rawinput` - Toggle raw input on/off
- `/rescan` - Rescan for input devices
- Key bindings for toggle and rescan functions

## Technical Details

The reflection utilities implement several strategies for accessing restricted fields:
1. Direct field access (Java 8 and early Java versions)
2. Method handle-based access
3. Unsafe-based access (when available)
4. Fallback mechanisms for different Java versions

This approach ensures the mod works across a wide range of Java versions while respecting the security improvements in newer versions.
