namespace ChatServer;

public record JwtOptions(
    string Issuer,
    string Audience,
    byte[] Key
);