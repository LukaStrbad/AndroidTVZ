using Microsoft.EntityFrameworkCore;
using WebServer.Model;

namespace WebServer.Database;

public class AndroidContext : DbContext
{
   public DbSet<Picture> Pictures { get; set; }
   
   public string DbPath { get; }

   public AndroidContext()
   {
      var folder = Environment.SpecialFolder.LocalApplicationData;
      var path = Environment.GetFolderPath(folder);
      DbPath = Path.Join(path, "android.db");
   }

   protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder) 
      => optionsBuilder.UseSqlite($"Data Source={DbPath}");
}