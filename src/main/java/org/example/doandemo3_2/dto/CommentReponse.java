package org.example.doandemo3_2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CommentReponse {
    private String errorCode;
    private String message;
}
