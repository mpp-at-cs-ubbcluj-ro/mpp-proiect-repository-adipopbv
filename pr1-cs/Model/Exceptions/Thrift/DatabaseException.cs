/**
 * Autogenerated by Thrift Compiler (0.14.1)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */

using System.Text;
using System.Threading;
using Thrift;
using Thrift.Protocol;
using Thrift.Protocol.Entities;
using Thrift.Protocol.Utilities;

#pragma warning disable IDE0079  // remove unnecessary pragmas
#pragma warning disable IDE1006  // parts of the code use IDL spelling


namespace Model.Exceptions.Thrift
{
  public partial class DatabaseException : TException, TBase
  {
    private string _message;

    public string Message
    {
      get
      {
        return _message;
      }
      set
      {
        __isset.message = true;
        this._message = value;
      }
    }


    public Isset __isset;
    public struct Isset
    {
      public bool message;
    }

    public DatabaseException()
    {
      this._message = "database error";
      this.__isset.message = true;
    }

    public DatabaseException DeepCopy()
    {
      var tmp4 = new DatabaseException();
      if((Message != null) && __isset.message)
      {
        tmp4.Message = this.Message;
      }
      tmp4.__isset.message = this.__isset.message;
      return tmp4;
    }

    public async global::System.Threading.Tasks.Task ReadAsync(TProtocol iprot, CancellationToken cancellationToken)
    {
      iprot.IncrementRecursionDepth();
      try
      {
        TField field;
        await iprot.ReadStructBeginAsync(cancellationToken);
        while (true)
        {
          field = await iprot.ReadFieldBeginAsync(cancellationToken);
          if (field.Type == TType.Stop)
          {
            break;
          }

          switch (field.ID)
          {
            case 1:
              if (field.Type == TType.String)
              {
                Message = await iprot.ReadStringAsync(cancellationToken);
              }
              else
              {
                await TProtocolUtil.SkipAsync(iprot, field.Type, cancellationToken);
              }
              break;
            default: 
              await TProtocolUtil.SkipAsync(iprot, field.Type, cancellationToken);
              break;
          }

          await iprot.ReadFieldEndAsync(cancellationToken);
        }

        await iprot.ReadStructEndAsync(cancellationToken);
      }
      finally
      {
        iprot.DecrementRecursionDepth();
      }
    }

    public async global::System.Threading.Tasks.Task WriteAsync(TProtocol oprot, CancellationToken cancellationToken)
    {
      oprot.IncrementRecursionDepth();
      try
      {
        var struc = new TStruct("DatabaseException");
        await oprot.WriteStructBeginAsync(struc, cancellationToken);
        var field = new TField();
        if((Message != null) && __isset.message)
        {
          field.Name = "message";
          field.Type = TType.String;
          field.ID = 1;
          await oprot.WriteFieldBeginAsync(field, cancellationToken);
          await oprot.WriteStringAsync(Message, cancellationToken);
          await oprot.WriteFieldEndAsync(cancellationToken);
        }
        await oprot.WriteFieldStopAsync(cancellationToken);
        await oprot.WriteStructEndAsync(cancellationToken);
      }
      finally
      {
        oprot.DecrementRecursionDepth();
      }
    }

    public override bool Equals(object that)
    {
      if (!(that is DatabaseException other)) return false;
      if (ReferenceEquals(this, other)) return true;
      return ((__isset.message == other.__isset.message) && ((!__isset.message) || (System.Object.Equals(Message, other.Message))));
    }

    public override int GetHashCode() {
      int hashcode = 157;
      unchecked {
        if((Message != null) && __isset.message)
        {
          hashcode = (hashcode * 397) + Message.GetHashCode();
        }
      }
      return hashcode;
    }

    public override string ToString()
    {
      var sb = new StringBuilder("DatabaseException(");
      int tmp5 = 0;
      if((Message != null) && __isset.message)
      {
        if(0 < tmp5++) { sb.Append(", "); }
        sb.Append("Message: ");
        Message.ToString(sb);
      }
      sb.Append(')');
      return sb.ToString();
    }
  }
}
