package org.hark7.fishingPlugin.util;

import net.kyori.adventure.text.Component;

public class ComponentBuilder {
    private String content;

    public ComponentBuilder(String content) {
        this.content = content;
    }
    
    public Component build() {
        return Component.text(content);
    }
    
    public ComponentBuilder replace(String src, String dst) {
        content = content.replace(src, dst);
        return this;
    }
}
