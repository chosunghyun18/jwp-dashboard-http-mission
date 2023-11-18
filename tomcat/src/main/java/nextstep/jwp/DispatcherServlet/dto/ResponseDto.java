package nextstep.jwp.DispatcherServlet.dto;

public class ResponseDto {
    private final byte[] header;
    private final byte[] data;

    public ResponseDto(String header, byte[] imageData) {
        this.header = header.getBytes();
        this.data = imageData;
    }
    public ResponseDto(String header, String imageData) {
        this.header = header.getBytes();
        this.data = imageData.getBytes();
    }
    public byte[] getHeader() {
        return header;
    }

    public byte[] getData() {
        return data;
    }
}
