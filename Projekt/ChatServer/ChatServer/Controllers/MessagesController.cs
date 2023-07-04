using System.Text.Json;
using ChatServer.Model;
using ChatServer.Model.Controller;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace ChatServer.Controllers;

[Authorize]
[ApiController]
[Route("[controller]")]
public class MessagesController : UserControllerBase
{
    public MessagesController(IServiceProvider serviceProvider) : base(serviceProvider)
    {
    }

    [HttpPut("send")]
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

    [HttpGet("lastMessageTime")]
    public async Task<ActionResult> GetLastMessageTime()
    {
        var user = await GetUserAsync();
        if (user is null)
            return new UnauthorizedResult();

        var userGroups = await Db.UserGroups.Where(ug => ug.User == user)
            .Select(ug => ug.Group)
            .ToListAsync();

        var lastMessages = await Db.Messages.Where(m => userGroups.Contains(m.Group))
            .GroupBy(m => m.Group)
            .Select(g => new
            {
                GroupId = g.Key.Id,
                LastMessageTime = g.Max(m => m.Time)
            })
            .ToListAsync();

        return new OkObjectResult(lastMessages.Max(m => m.LastMessageTime));
    }

    [HttpGet("lastMessageTimeInGroup")]
    public async Task<ActionResult> GetLastMessageTimeInGroup(int groupId)
    {
        var user = await GetUserAsync();
        if (user is null)
            return new UnauthorizedResult();

        var group = await Db.Groups.FirstOrDefaultAsync(g => g.Id == groupId);
        if (group is null)
            return NotFound();

        var userGroup = await Db.UserGroups.FirstOrDefaultAsync(ug => ug.User == user && ug.Group == group);
        if (userGroup is null)
            return new UnauthorizedResult();

        var lastMessage = await Db.Messages.Where(m => m.Group == group)
            .MaxAsync(m => m.Time);

        return new OkObjectResult(lastMessage);
    }

    [HttpGet("lastMessage")]
    public async Task<ActionResult<MessageResponse?>> LastMessage(int groupId)
    {
        var user = await GetUserAsync();
        if (user is null)
            return new UnauthorizedResult();

        var group = await Db.Groups.FirstOrDefaultAsync(g => g.Id == groupId);
        var userGroup = await Db.UserGroups.FirstOrDefaultAsync(ug => ug.User == user && ug.Group == group);
        if (userGroup is null)
            return new UnauthorizedResult();

        if (group is null)
            return NotFound();

        var message = Db.Messages
            .Where(m => m.Group == group)
            .AsEnumerable()
            .MaxBy(m => m.Time);

        if (message is null)
            return new OkObjectResult(null);

        try
        {
            return new MessageResponse(message.Sender.Username, message.Sender.DisplayName, message.Content,
                message.Time);
        }
        catch
        {
            Console.WriteLine(JsonSerializer.Serialize(message.Sender));
            return new MessageResponse("", null, message.Content, message.Time);
        }
    }

    [HttpGet("allMessages")]
    public async Task<ActionResult<List<MessageResponse>>> AllMessages(int groupId, DateTime? olderThan)
    {
        var user = await GetUserAsync();
        if (user is null)
            return new UnauthorizedResult();

        var group = await Db.Groups.FirstOrDefaultAsync(g => g.Id == groupId);
        var userGroup = await Db.UserGroups.FirstOrDefaultAsync(ug => ug.User == user && ug.Group == group);
        if (userGroup is null)
            return new UnauthorizedResult();

        if (group is null)
            return NotFound();

        var messages = Db.Messages
            .Where(m => m.Group == group)
            .Select(m => new MessageResponse(m.Sender.Username, m.Sender.DisplayName, m.Content, m.Time));
        
        if (olderThan.HasValue)
            messages = messages.Where(m => m.Time < olderThan);
        
        return new OkObjectResult(await messages.ToListAsync());
    }
}