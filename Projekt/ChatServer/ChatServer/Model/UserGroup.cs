using System.ComponentModel.DataAnnotations;

namespace ChatServer.Model;

public class UserGroup
{
    [Key]
    public int Id { get; set; }
    public required User User { get; set; }
    public required Group Group { get; set; }
}