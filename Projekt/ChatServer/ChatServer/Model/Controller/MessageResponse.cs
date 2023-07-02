namespace ChatServer.Model.Controller;

public record MessageResponse(
    string SenderUsername,
    string Content,
    DateTime Time
);