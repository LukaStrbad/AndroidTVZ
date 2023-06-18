using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using WebServer.Database;
using WebServer.Migrations;
using WebServer.Model;

namespace WebServer.Controllers;

[ApiController]
[Route("[controller]")]
public class PictureController : ControllerBase
{
    private readonly AndroidContext _db;

    public PictureController(IServiceProvider serviceProvider)
    {
        _db = serviceProvider.GetRequiredService<AndroidContext>();
    }

    private Picture MapPictureImage(Picture picture)
    {
        var host = Request.HttpContext.Request.Host;
        var isHttps = Request.IsHttps;

        var http = isHttps ? "https" : "http";
        return picture with
        {
            Image = $"{http}://{host}/images/{picture.Image}"
        };
    }

    [HttpGet("allPictures")]
    public async Task<ActionResult<IEnumerable<Picture>>> GetAllPictures()
    {
        var pictures = await _db.Pictures.ToListAsync();
        return pictures.Select(MapPictureImage).ToList();
    }

    [HttpGet("latestPicture")]
    public async Task<ActionResult<Picture>> GetLatestPicture()
    {
        var pictures = await _db.Pictures.ToListAsync();
        var picture = pictures.MaxBy(picture => picture.Date);
        if (picture is null)
            return Empty;
        return MapPictureImage(picture);
    }

    [HttpPut("addPicture")]
    public async Task<ActionResult> AddPicture([FromBody] Picture picture)
    {
        _db.Add(picture);
        await _db.SaveChangesAsync();
        return Ok();
    }

    [HttpDelete("deletePicture/{id:int}")]
    public async Task<ActionResult> DeletePicture(int id)
    {
        var picture = _db.Pictures.FirstOrDefault(p => p.Id == id);
        if (picture is null)
            return NotFound();

        _db.Remove(picture);
        await _db.SaveChangesAsync();
        return Ok();
    }
}