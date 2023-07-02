using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace ChatServer.Model;

public class Group
{
    [Key] public int Id { get; set; }
    public required string Name { get; set; }
    public required User Owner { get; set; }
}