package org.hark7.fishingPlugin.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;

public class ComponentBuilder {
    private Component content;

    public ComponentBuilder(Component content) {
        this.content = content;
    }
    
    public Component build() {
        return content;
    }
    
    public ComponentBuilder replace(String src, String dst) {
        content = content
                .replaceText(TextReplacementConfig.builder()
                .matchLiteral(src)
                .replacement(dst)
                .build());
        return this;
    }
}
