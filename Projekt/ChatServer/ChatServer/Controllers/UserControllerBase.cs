using ChatServer.Database;
using ChatServer.Model;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace ChatServer.Controllers;

public class UserControllerBase: ControllerBase
{
    protected readonly ChatContext Db;

    public UserControllerBase(IServiceProvider serviceProvider)
    {
        Db = serviceProvider.GetRequiredService<ChatContext>();
    }
    
    protected async Task<User?> GetUserAsync()
    {
        var userIdentity = User.Identity;
        if (userIdentity is not { IsAuthenticated: true })
            return null;

        return await Db.Users.FirstOrDefaultAsync(u => u.Username == userIdentity.Name);
    }    
}