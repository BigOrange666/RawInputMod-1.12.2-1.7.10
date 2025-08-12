package mod.seanld.rawinput;

import net.minecraft.util.MouseHelper;
import org.lwjgl.input.Mouse;
import mod.seanld.rawinput.reflect.Fields;

public class RawMouseHelper extends MouseHelper {
    @Override
    public void mouseXYChange() {
        // 使用反射来安全地访问字段
        try {
            Fields.setField(this, "deltaX", RawInputHandler.dx);
            Fields.setField(this, "deltaY", -RawInputHandler.dy);
        } catch (Exception e) {
            // 回退到直接访问（在旧版本Java中）
            this.deltaX = RawInputHandler.dx;
            this.deltaY = -RawInputHandler.dy;
        }
        RawInputHandler.dx = 0;
        RawInputHandler.dy = 0;
    }

    @Override
    public void grabMouseCursor() {
        if (Boolean.parseBoolean(System.getProperty("fml.noGrab", "false"))) return;
        Mouse.setGrabbed(true);
        
        // 使用反射来安全地重置字段
        try {
            Fields.setField(this, "deltaX", 0);
            Fields.setField(this, "deltaY", 0);
        } catch (Exception e) {
            // 回退到直接访问（在旧版本Java中）
            this.deltaX = 0;
            this.deltaY = 0;
        }
        RawInputHandler.dx = 0;
        RawInputHandler.dy = 0;
    }
}
