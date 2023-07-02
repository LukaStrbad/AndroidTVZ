using ChatServer.Model;
using ChatServer.Model.Controller;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace ChatServer.Controllers;

[ApiController]
[Route("[controller]")]
public class MessagesController : UserControllerBase
{
    public MessagesController(IServiceProvider serviceProvider) : base(serviceProvider)
    {
    }

    [HttpPut("send"), Authorize]
    public async Task<ActionResult> SendMessage([FromBody] FormMessage formMessage)
    {
        var user = await GetUserAsync();
        if (user is null)
            return new UnauthorizedResult();

        var group = await Db.Groups.FirstOrDefaultAsync(g => g.Id == formMessage.GroupId);
        if (group is null)
            return new BadRequestObjectResult("Group does not exist");

        var userGroup = await Db.UserGroups.FirstOrDefaultAsync(ug => ug.User == user && ug.Group == group);
        if (userGroup is null)
            return new UnauthorizedResult();

        var message = new Message
        {
            Sender = user,
            Group = group,
            Content = formMessage.Content
        };
        await Db.Messages.AddAsync(message);
        await Db.SaveChangesAsync();

        return Ok();
    }
}