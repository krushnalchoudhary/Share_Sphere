import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class RecieveFile extends Thread {
    public void run() {
        try {

            // Create a server socket to listen for incoming connections on port 1234
            ServerSocket ss = new ServerSocket(2001);
            Socket s = null;

            try {
                // Wait for a client to connect
                s = ss.accept();
            } catch (Exception e) {
                // Handle any exceptions and close the server socket
                ss.close();
                return;
            }

            // Get the input stream to receive data from the client
            InputStream is = s.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            // Read the file name sent by the client
            String file_name = br.readLine();
            long sizeOfFile = Long.parseLong(br.readLine());

            // Define the destination path where the received file will be saved
            String destination_path = App.storeDirFile.getAbsolutePath() + file_name;
            File output_file = new File(destination_path);

            // Create a FileOutputStream to write the received data to the output file
            FileOutputStream fos = new FileOutputStream(output_file);

            byte[] data = new byte[1024];
            int len = 0;
            long total = 0;


            // Read data from the input stream and write it to the output file
            while ((len = is.read(data)) > 0) {

                try{
                    Thread.sleep(50);
                }catch(Exception e){

                }

                total += 1;

                double per = ((double)total / (double)sizeOfFile ) * 100.0d / 100.0d;

                // Update the progress bar in the main application
                App.progressBar.setProgress(per);

                fos.write(data, 0, len);
            }

            // Close the FileOutputStream, socket, and server socket
            fos.close();
            s.close();
            ss.close();

        } catch (IOException e) {
            // Handle any IO exceptions and display a popup with an error message
            App.popUp("An Error Occurred In Sharing");
        }
    }
}