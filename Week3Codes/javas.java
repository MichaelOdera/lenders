public class OracleJdbcTest
{
    String driverClass = "oracle.jdbc.driver.OracleDriver";
 
    Connection con;
     
    public void init(FileInputStream fs) throws ClassNotFoundException, SQLException, FileNotFoundException, IOException
    {
        Properties props = new Properties();
        props.load(fs);
        String url = props.getProperty("db.url");
        String userName = props.getProperty("db.user");
        String password = props.getProperty("db.password");
        Class.forName(driverClass);
 
        con=DriverManager.getConnection(url, userName, password);
    }
     
    public void fetch() throws SQLException, IOException
    {
        PreparedStatement ps = con.prepareStatement("select SYSDATE from dual");
        ResultSet rs = ps.executeQuery();
         
        while (rs.next())
        {
            // do the thing you do
        }
        rs.close();
        ps.close();
    }
 
    public static void main(String[] args) 
    {
        OracleJdbcTest test = new OracleJdbcTest();
        test.init();
        test.fetch();
    }
}
Using NIO to copy Java file fast
//
//
//
public static void fileCopy( File in, File out )
            throws IOException
    {
        FileChannel inChannel = new FileInputStream( in ).getChannel();
        FileChannel outChannel = new FileOutputStream( out ).getChannel();
        try
        {
//          inChannel.transferTo(0, inChannel.size(), outChannel);      // original -- apparently has trouble copying large files on Windows
 
            // magic number for Windows, 64Mb - 32Kb)
            int maxCount = (64 * 1024 * 1024) - (32 * 1024);
            long size = inChannel.size();
            long position = 0;
            while ( position < size )
            {
               position += inChannel.transferTo( position, maxCount, outChannel );
            }
        }
        finally
        {
            if ( inChannel != null )
            {
               inChannel.close();
            }
            if ( outChannel != null )
            {
                outChannel.close();
            }
        }
    }