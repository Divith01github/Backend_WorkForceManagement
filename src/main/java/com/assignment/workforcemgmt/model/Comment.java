package com.assignment.workforcemgmt.model;
//
//import lombok.Data;
//
//@Data
//public class TaskComment {
//    private Long id;
//    private Long taskId;
//    private String comment;
//    private String commenter;
//    private Long timestamp;
//}

import lombok.Data;

@Data
public class Comment {
    private Long id;
    private Long taskId;
    private Long commenterId;
    private String commentText;
    private Long timestamp;
}