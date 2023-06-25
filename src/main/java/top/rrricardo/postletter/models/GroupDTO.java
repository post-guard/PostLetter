package top.rrricardo.postletter.models;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

public class GroupDTO implements Serializable {

    private String name;

    private String details;

    private int sessionId;

    private List<Integer> participants;

    public GroupDTO() {

    }

    public GroupDTO(
            @NotNull String name,
            @NotNull String details,
            int sessionId,
            @NotNull List<Integer> participants
    ) {
        this.name = name;
        this.details = details;
        this.sessionId = sessionId;
        this.participants = participants;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public @NotNull String getDetails() {
        return details;
    }

    public void setDetails(@NotNull String details) {
        this.details = details;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public @NotNull List<Integer> getParticipants() {
        return participants;
    }

    public void setParticipants(@NotNull List<Integer> participants) {
        this.participants = participants;
    }
}
