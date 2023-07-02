using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace ChatServer.Model;

public class Message
{
    [Key] [JsonIgnore] public int Id { get; set; }
    public required User Sender { get; set; }
    public required Group Group { get; set; }
    public required string Content { get; set; }
    public DateTime Time { get; set; } = DateTime.Now;
}