using System.Text.Json.Serialization;

namespace WebServer.Model;

public record Picture
{
    [JsonPropertyName("id")] public int? Id { get; init; }
    [JsonPropertyName("date")] public required string Date { get; init; }

    [JsonPropertyName("title")] public required string Title { get; init; }

    [JsonPropertyName("description")] public required string Description { get; init; }

    [JsonPropertyName("image")] public required string Image { get; set; }

    [JsonPropertyName("web_link")]
    public string WebLink
    {
        get
        {
            var year = Date[2..4];
            var monthDay = Date[4..].Replace("-", "");
            return $"https://apod.nasa.gov/apod/ap{year}{monthDay}.html";
        }
    }
    
}