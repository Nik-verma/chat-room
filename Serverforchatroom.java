import com.vmm.JHTTPServer;
import java.io.*;
import java.sql.*;
import java.util.*;

public class Serverforchatroom extends JHTTPServer 
{
    public Serverforchatroom(int port) throws IOException {
        super(port);
    }

    /**
     * This is the common function which will receive the client's request and
     * will serve the response accordingly. for e.g 1. if request is for
     * /GetResource( image preview / any file download ) call
     * sendCompleteFile(uri) 2. if request is for /StreamMedia ( stream audio /
     * video ) call streamFile(uri,method,header) 3. in case of any other custom
     * request prepare your own response and return
     *
     * NOTE: In case of File upload (client to server) call any of the two
     * functions 1. saveFileOnServerWithOriginalName(files,parms,name,abspath)
     * 1. saveFileOnServerWithRandomName(files,parms,name,abspath)
     *
     *
     * @param uri will contain the extracted uri from the complete URL for e.g
     * (/GetResource/c1.jpg) (/GetResource/one.mp3) (/StreamMedia/ome.mp4)
     * @param method contains GET,POST,HEAD
     * @param header contains the extra header data (range, user-agent etc)
     * @param parms contains the query string parameters to extract text data
     * e.g String email = parms.getProperty("email");
     *
     * @param files contains the files in form of file upload(POST request)
     * filename = saveFileOnServerWithRandomName(files, parms, field_name,
     * abs_path);
     * @return
     */
    @Override
    public Response serve(String uri, String method, Properties header, 
                                                   Properties parms, Properties files) 
    {

        Response res = null;

        System.out.println("URI "+uri);
        
        if (uri.contains("/GetResource")) //request should be of the form /GetResource/src/content/one.jpg
        {
            uri = uri.substring(1);
            uri = uri.substring(uri.indexOf("/") + 1);
            System.out.println(uri +" *** " );
            res = sendCompleteFile(uri);
        } 
        else if (uri.contains("/StreamMedia")) //request should be of the form /GetResource/one.jpg
        {
            uri = uri.substring(1);
            uri = uri.substring(uri.indexOf("/") + 1);
            System.out.println(uri +" *** " );
            res = streamFile(uri, method, header);
        } 
        else if(uri.contains("/AdminLogin"))
        {
            String u =  parms.getProperty("username");
            String p =  parms.getProperty("password");
            
            System.out.println(u+" "+p);
            
            try
            {
              ResultSet rs = dbloader.executeQuery("select * from adminlogin where username='"+u+"' and password='"+p+"'");
            
                if(rs.next())
                {
                    res = new Response(HTTP_OK, "text/plain", "Login Successfull !!!");
                }
                else
                {
                    res = new Response(HTTP_OK, "text/plain", "Login Failed !!!");
                }
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
            
            
            else if(uri.contains("/addcategories"))
                    {
                           
                     String u =  parms.getProperty("username");
            String p =  parms.getProperty("password");
            
                    
                    
                    }
            
           
        
        }
        else if(uri.contains("/uploadfile"))
        {
            String a = parms.getProperty("a");
            String f1 =  files.getProperty("f1");
            
            System.out.println("--- File Upload Example ---");
            System.out.println(a);
            System.out.println(f1);
            
            saveFileOnServerWithRandomName(files, parms, "f1", "src/uploaded_pics");
       
            System.out.println("File Saved on Server");  
        }
        else 
        {
            res = new Response(HTTP_OK, "text/html", "<body style='background: #D46A6A; color: #fff'><center><h1>Hello</h1><br> <h3>Welcome To JHTTP Server (Version 1.0)</h3></body></center>");
      
        }
        

        return res;
    }
}
