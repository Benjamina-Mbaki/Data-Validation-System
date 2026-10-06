package ui;

import java.awt.Color;
import java.awt.Font;

final class Theme {
    static final Color NAVY = new Color(0x1F2A44);
    static final Color ORANGE = new Color(0xF39C12);
    static final Color BACKGROUND = new Color(0xF4F6FA);
    static final Color CARD = Color.WHITE;
    static final Color BORDER = new Color(0xCBD2E0);
    static final Color VALID = new Color(0x1E8E3E);
    static final Color INVALID = new Color(0xD93025);
    static final Color MUTED = new Color(0x6B7385);
    static final Font TITLE = new Font("SansSerif", Font.BOLD, 22);
    static final Font LABEL = new Font("SansSerif", Font.BOLD, 13);
    static final Font INPUT = new Font("SansSerif", Font.PLAIN, 14);
    static final Font HINT = new Font("SansSerif", Font.PLAIN, 12);
    
    private Theme() { }
}
