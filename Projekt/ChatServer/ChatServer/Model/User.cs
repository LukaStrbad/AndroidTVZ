using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace ChatServer.Model;

public class User
{
    [Key] [JsonIgnore] public int Id { get; set; }
    
    public required string Username { get; set; }

    [JsonIgnore] public string Password { get; set; } = "";
    public string? DisplayName { get; set; }

    [JsonIgnore] public DateTime CreatedAt { get; set; } = DateTime.Now;
    public bool IsOnline { get; set; }
}