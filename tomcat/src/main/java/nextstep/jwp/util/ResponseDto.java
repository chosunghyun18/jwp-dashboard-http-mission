package nextstep.jwp.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class ResponseDto {
    private final String header;
    private final String data;

    public ResponseDto(String header, byte[] imageData) {
        this.header = header;
        String imageStringData ="";
        try{
            final OutputStream outputStream = new ByteArrayOutputStream(imageData.length);
            outputStream.write(imageData);
            outputStream.close();
            imageStringData = outputStream.toString();
        } catch (IOException e) {
            imageStringData = "[ERROR]";
        }
        this.data = imageStringData;
    }
    public ResponseDto(String header, String imageData) {
        this.header = header;
        this.data = imageData;
    }

    public String getHeader() {
        return header;
    }

    public String getData() {
        return data;
    }
}
