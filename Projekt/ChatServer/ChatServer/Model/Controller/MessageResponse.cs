namespace ChatServer.Model.Controller;

public record MessageResponse(
    string SenderUsername,
    string? SenderName,
    string Content,
    DateTime Time
);