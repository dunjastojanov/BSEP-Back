package com.myhouse.MyHouse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document("log")
public class Log {
    @Id
    private String id;
    private Date timestamp;
    private String level;
    private String logger;
    private String message;
    private String thread;

    public Log(Date timestamp, String level, String logger, String message, String thread) {
        this.timestamp = timestamp;
        this.level = level;
        this.logger = logger;
        this.message = message;
        this.thread = thread;
    }
}
