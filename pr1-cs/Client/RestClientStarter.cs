using System;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Threading.Tasks;
using Model;

namespace Client
{
    public class RestClientStarter
    {
        private readonly HttpClient _client = new();

        public void Run(string[] args)
        {
            RunAsync().Wait();
        }

        private async Task RunAsync()
        {
            _client.BaseAddress = new Uri("http://localhost:8080/basketball-games/games");
            _client.DefaultRequestHeaders.Accept.Clear();
            _client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));
            var game = await GetGameAsync("http://localhost:8080/basketball-games/games/" + 3);
            Console.WriteLine(game);
            Console.ReadLine();
        }

        private async Task<Game> GetGameAsync(string path)
        {
            Game game = null;
            HttpResponseMessage response = await _client.GetAsync(path);
            if (response.IsSuccessStatusCode) game = await response.Content.ReadAsAsync<Game>();
            return game;
        }
    }
}