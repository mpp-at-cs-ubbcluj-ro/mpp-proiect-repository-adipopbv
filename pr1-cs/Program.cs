using System;
using pr1_cs.Domain;
using pr1_cs.Repository;
using pr1_cs.Repository.Database;
using pr1_cs.UserInterfacing;

namespace pr1_cs
{
    internal static class Program
    {
        private static void Main(string[] args)
        {
	        new GtkClient().Run(args);
        }
    }
}