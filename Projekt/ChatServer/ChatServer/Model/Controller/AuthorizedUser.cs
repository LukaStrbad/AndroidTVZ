namespace ChatServer.Model.Controller;

public record AuthorizedUser(
    string Username,
    string Token
);