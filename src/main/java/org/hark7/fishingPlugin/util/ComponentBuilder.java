package org.hark7.fishingPlugin.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;

public class ComponentBuilder {
    private Component content;

    private ComponentBuilder(Component content) {
        this.content = content;
    }

    public static ComponentBuilder builder(Component content) {
        return new ComponentBuilder(content);
    }
    
    public Component build() {
        return content;
    }
    
    public <T> ComponentBuilder replace(String src, T dst) {
        content = content
                .replaceText(TextReplacementConfig.builder()
                .matchLiteral(src)
                .replacement(dst.toString())
                .build());
        return this;
    }
}
