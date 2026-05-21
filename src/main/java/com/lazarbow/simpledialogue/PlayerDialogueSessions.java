package com.lazarbow.simpledialogue;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Tracks each player's active dialogue and current node in memory.
 *
 * <p>Sessions intentionally reset on plugin reload/server restart so YAML remains the source
 * of truth while player progress is lightweight.</p>
 */
public final class PlayerDialogueSessions {
    private final Map<UUID, Session> sessions = new HashMap<>();

    public Optional<Session> get(UUID player) {
        return Optional.ofNullable(sessions.get(player));
    }

    public Session getOrStart(UUID player, String dialogueId, String startNode) {
        Session session = sessions.get(player);
        if (session == null || !session.dialogueId().equals(dialogueId)) {
            session = new Session(dialogueId, startNode);
            sessions.put(player, session);
        }

        return session;
    }

    public void setNode(UUID player, String dialogueId, String node) {
        sessions.put(player, new Session(dialogueId, node));
    }

    public void clear(UUID player) {
        sessions.remove(player);
    }

    public void clear() {
        sessions.clear();
    }

    public record Session(String dialogueId, String node) {
    }
}
