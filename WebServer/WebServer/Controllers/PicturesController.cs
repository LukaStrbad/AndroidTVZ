using Microsoft.AspNetCore.Mvc;
using WebServer.Model;

namespace WebServer.Controllers;

[ApiController]
[Route("[controller]")]
public class PicturesController : ControllerBase
{
    [HttpGet("allPictures")]
    public ActionResult<IEnumerable<Picture>> GetAllPictures() => new List<Picture>();
}