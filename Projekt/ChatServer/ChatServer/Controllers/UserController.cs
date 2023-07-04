using System.Globalization;
using System.IdentityModel.Tokens.Jwt;
using System.Net;
using System.Security.Claims;
using ChatServer.Database;
using ChatServer.Model;
using ChatServer.Model.Controller;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.IdentityModel.Tokens;
using JwtRegisteredClaimNames = Microsoft.IdentityModel.JsonWebTokens.JwtRegisteredClaimNames;

namespace ChatServer.Controllers;

[ApiController]
[Route("[controller]")]
public class UserController : UserControllerBase
{
    private readonly JwtOptions _jwtOptions;

    public UserController(IServiceProvider serviceProvider) : base(serviceProvider)
    {
        _jwtOptions = serviceProvider.GetRequiredService<JwtOptions>();
    }

    [HttpPost("create")]
    public async Task<ActionResult> Create([FromBody] FormUser formUser)
    {
        // Check if username is already taken
        if (await Db.Users.AnyAsync(u => u.Username == formUser.Username))
        {
            return new BadRequestObjectResult("Username is already taken");
        }

        var passwordHasher = new PasswordHasher<LoginFormUser>();
        var password = passwordHasher.HashPassword(formUser, formUser.Password);
        var user = new User
        {
            Username = formUser.Username,
            Password = password,
            DisplayName = formUser.DisplayName
        };
        var createdUser = await Db.Users.AddAsync(user);
        await Db.SaveChangesAsync();

        var stringToken = GetToken(createdUser.Entity);
        return new OkObjectResult(new AuthorizedUser(createdUser.Entity.Username, stringToken));
    }

    [HttpPost("login")]
    public async Task<ActionResult> Login([FromBody] LoginFormUser formUser)
    {
        var passwordHasher = new PasswordHasher<LoginFormUser>();
        var user = await Db.Users.FirstOrDefaultAsync(u => u.Username == formUser.Username);
        if (user is null)
        {
            return new BadRequestObjectResult("Username or password is incorrect");
        }

        var result = passwordHasher.VerifyHashedPassword(formUser, user.Password, formUser.Password);
        if (result == PasswordVerificationResult.Failed)
        {
            return new BadRequestObjectResult("Username or password is incorrect");
        }

        if (result == PasswordVerificationResult.SuccessRehashNeeded)
        {
            user.Password = passwordHasher.HashPassword(formUser, formUser.Password);
            await Db.SaveChangesAsync();
        }

        var stringToken = GetToken(user);
        return new OkObjectResult(new AuthorizedUser(user.Username, stringToken));
    }

    [HttpGet("profile")]
    [Authorize]
    public async Task<ActionResult<User>> Profile()
    {
        var user = await GetUserAsync();

        if (user is null)
            return new UnauthorizedResult();

        return new OkObjectResult(user);
    }

    private string GetToken(User user)
    {
        var claims = new[]
        {
            new Claim(JwtRegisteredClaimNames.Sub, "TokenForApi"),
            new Claim(JwtRegisteredClaimNames.Jti, Guid.NewGuid().ToString()),
            new Claim(JwtRegisteredClaimNames.Iat, DateTime.UtcNow.ToString(CultureInfo.InvariantCulture)),
            new Claim(ClaimTypes.NameIdentifier, user.Id.ToString()),
            new Claim(ClaimTypes.Name, user.Username)
        };

        var token = new JwtSecurityToken(_jwtOptions.Issuer,
            _jwtOptions.Audience,
            claims,
            expires: DateTime.UtcNow.AddDays(365),
            signingCredentials: new SigningCredentials(new SymmetricSecurityKey(_jwtOptions.Key),
                SecurityAlgorithms.HmacSha256)
        );

        var tokenHandler = new JwtSecurityTokenHandler();
        return tokenHandler.WriteToken(token);
    }

    [HttpGet("getUsers")]
    public async Task<ActionResult<List<User>>> GetUsers(string nameContains = "")
    {
        var containsLower = nameContains.ToLower();
        var users = (await Db.Users
                .ToListAsync())
            .Where(u =>
                u.Username.ToLower().Contains(containsLower) ||
                u.DisplayName?.ToLower().Contains(containsLower) == true);
        return new OkObjectResult(users);
    }
}