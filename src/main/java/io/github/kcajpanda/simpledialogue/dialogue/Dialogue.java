package io.github.kcajpanda.simpledialogue.dialogue;

import java.util.List;
import java.util.Map;

/**
 * Immutable representation of one loaded dialogue YAML file.
 */
public record Dialogue(
    String id,
    NpcProfile npc,
    String start,
    Map<String, DialogueNode> nodes
) {
    public DialogueNode startNode() {
        return nodes.get(start);
    }

    public record NpcProfile(String fancyNpc, String name, String nameColor, String bracketColor) {
    }

    public record DialogueNode(
        String id,
        String speaker,
        List<String> lines,
        String left,
        String right,
        String leftText,
        String rightText,
        boolean end
    ) {
    }
}
