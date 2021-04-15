using System;

namespace Networking.Reflection
{
    [Serializable]
    public class Request
    {
        public RequestType Type { get; set; }
        public object Data { get; set; }

        public Request(RequestType type)
        {
            Type = type;
            Data = null;
        }

        public Request(RequestType type, object data)
        {
            Type = type;
            Data = data;
        }

        public Request SetType(RequestType type)
        {
            Type = type;
            return this;
        }

        public Request SetData(object data)
        {
            Data = data;
            return this;
        }
    }
}