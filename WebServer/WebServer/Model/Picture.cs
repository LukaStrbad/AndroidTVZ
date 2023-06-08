using System.Text.Json.Serialization;

namespace WebServer.Model;

public class Picture
{
    [JsonPropertyName("id")]
    public int Id { get; set; }
    
    [JsonPropertyName("title")]
    public string Title { get; set; }
    
    [JsonPropertyName("description")]
    public string Description { get; set; }
    
    [JsonPropertyName("url")]
    public string PictureUrl { get; set; }
    
    [JsonPropertyName("web_link")]
    public string WebLink { get; set; }
}