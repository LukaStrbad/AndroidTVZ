using System.Net.Security;
using System.Security.Claims;
using ChatServer.Database;
using ChatServer.Model;
using ChatServer.Model.Controller;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace ChatServer.Controllers;

[ApiController]
[Route("[controller]")]
public class GroupController : UserControllerBase
{
    public GroupController(IServiceProvider serviceProvider) : base(serviceProvider)
    {
    }

    [HttpPost("create"), Authorize]
    public async Task<ActionResult> Create([FromBody] FormGroup formGroup)
    {
        var user = await GetUserAsync();
        if (user is null)
            return new UnauthorizedResult();

        try
        {
            var group = (await Db.Groups.AddAsync(new Group
            {
                Name = formGroup.Name,
                Owner = user
            })).Entity;
            await Db.SaveChangesAsync();

            var members = formGroup.Members ?? new List<string>();
            members.Add(user.Username);
            foreach (var member in members.Distinct())
            {
                var memberUser = await Db.Users.FirstOrDefaultAsync(u => u.Username == member);
                if (memberUser is null)
                {
                    continue;
                }

                Db.UserGroups.Add(new UserGroup
                {
                    User = memberUser,
                    Group = group
                });
            }

            await Db.SaveChangesAsync();

            return Ok();
        }
        catch
        {
            return BadRequest();
        }
    }

    [HttpGet("list"), Authorize]
    public async Task<ActionResult<List<Group>>> List()
    {
        var user = await GetUserAsync();
        if (user is null)
            return new UnauthorizedResult();

        return await Db.UserGroups
            .Where(ug => ug.User == user)
            .Select(ug => ug.Group)
            .ToListAsync();
    }

    [HttpGet("{id:int}/members"), Authorize]
    public async Task<ActionResult<List<User>>> List(int id)
    {
        var user = await GetUserAsync();
        if (user is null)
            return new UnauthorizedResult();

        var groupEntity = await Db.Groups.FirstOrDefaultAsync(g => g.Id == id);
        if (groupEntity is null)
            return NotFound();

        return await Db.UserGroups
            .Where(ug => ug.Group == groupEntity)
            .Select(ug => ug.User)
            .ToListAsync();
    }
    
    [HttpGet("{id:int}/messages"), Authorize]
    public async Task<ActionResult<List<MessageResponse>>> Messages(int id)
    {
        var user = await GetUserAsync();
        if (user is null)
            return new UnauthorizedResult();

        var groupEntity = await Db.Groups.FirstOrDefaultAsync(g => g.Id == id);
        if (groupEntity is null)
            return NotFound();

        return await Db.Messages
            .Where(m => m.Group == groupEntity)
            .Select(m => new MessageResponse(m.Sender.Username, m.Content, m.Time))
            .ToListAsync();
    }
}