using System;
using System.Data;
using Mono.Data.Sqlite;

namespace pr1_cs.DbUtils
{
	public class SqliteConnectionFactory : ConnectionFactory
	{
		public override IDbConnection CreateConnection()
		{
			//Mono Sqlite Connection
			String connectionString = "URI=file:/home/adipopbv/the-backpack/Work/University/Year 2/Semester 2/Medii de Proiectare si Programare/Laborator/proiect1-adipopbv/pr1-cs/basketball-games.db";
			return new SqliteConnection(connectionString);
		}
	}
}
