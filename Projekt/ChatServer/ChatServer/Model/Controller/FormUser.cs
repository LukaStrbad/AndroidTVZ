namespace ChatServer.Model.Controller;

public record FormUser(
    string Username,
    string Password,
    string? DisplayName
) : LoginFormUser(Username, Password);