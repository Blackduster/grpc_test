package servermain;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import service_impl.PersonManagementServiceImpl;

import java.io.IOException;

public class PersonManagementServer
{
  private final Server server;

  public PersonManagementServer( int port )
  {
    this.server = ServerBuilder.forPort( port )
        .addService( new PersonManagementServiceImpl() )
        .build( );
  }

  public static void main( String[] args ) throws Exception
  {
    final PersonManagementServer server = new PersonManagementServer( 8888 );
    server.start( );
    System.out.println( "Server running ..." );
    server.blockUntilShutdown( );
  }

  public void start( ) throws IOException
  {
    server.start( );

    Runtime.getRuntime( ).addShutdownHook( new Thread( )
    {

      @Override
      public void run( )
      {
        System.err.println( "Shutting down gRPC server since JVM is shutting down" );
        PersonManagementServer.this.stop( );
        System.err.println( "Server shut down" );
      }
    } );
  }

  public void stop( )
  {
    if ( server != null )
    {
      server.shutdown( );
    }
  }

  private void blockUntilShutdown( ) throws InterruptedException
  {
    if ( server != null )
    {
      server.awaitTermination( );
    }
  }
}
