using System;

namespace Networking.Reflection
{
    [Serializable]
    public class Response
    {
        public ResponseType Type { get; set; }
        public object Data { get; set; }

        public Response(ResponseType type)
        {
            Type = type;
            Data = null;
        }

        public Response(ResponseType type, object data)
        {
            Type = type;
            Data = data;
        }

        public Response SetType(ResponseType type)
        {
            Type = type;
            return this;
        }

        public Response SetData(object data)
        {
            Data = data;
            return this;
        }
    }
}