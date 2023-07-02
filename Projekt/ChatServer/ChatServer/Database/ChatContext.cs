using ChatServer.Model;
using Microsoft.EntityFrameworkCore;

namespace ChatServer.Database;

public class ChatContext : DbContext
{
    public DbSet<User> Users { get; set; } = default!;
    public DbSet<Message> Messages { get; set; } = default!;
    public DbSet<Group> Groups { get; set; } = default!;
    public DbSet<UserGroup> UserGroups { get; set; } = default!;

    private string DbPath { get; }
    
    public ChatContext()
    {
        const Environment.SpecialFolder folder = Environment.SpecialFolder.LocalApplicationData;
        var path = Environment.GetFolderPath(folder);
        DbPath = Path.Join(path, "chat.db");
    }
    
    protected override void OnConfiguring(DbContextOptionsBuilder options)
        => options.UseSqlite($"Data Source={DbPath}");
}