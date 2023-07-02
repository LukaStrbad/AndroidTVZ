namespace ChatServer.Model.Controller;

public record FormGroup(
    string Name,
    List<string>? Members
);