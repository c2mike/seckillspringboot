package springbootseckill.dto;

import lombok.Data;

import java.util.Date;
@Data
public class Expose {
    private boolean success;
    private String md5;
    private String errorMessage;
    private long now;
    private long startTime;
    private long endTime;
}
