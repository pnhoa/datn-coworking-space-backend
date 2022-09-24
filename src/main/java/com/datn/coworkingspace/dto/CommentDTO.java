package com.datn.coworkingspace.dto;

import com.datn.coworkingspace.entity.Space;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.NotNull;

@JsonIgnoreProperties({"space", "userDTO"})
public class CommentDTO extends AbstractDTO {

    private String content;

    private long rate;

    private Space space;

    @NotNull(message = "Please input space id")
    private Long spaceId;

    private CustomerDTO userDTO;

    @NotNull(message = "Please input user id")
    private Long userId;

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getRate() {
        return rate;
    }

    public void setRate(long rate) {
        this.rate = rate;
    }

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
    }

    public Long getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Long spaceId) {
        this.spaceId = spaceId;
    }

    public CustomerDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(CustomerDTO userDTO) {
        this.userDTO = userDTO;
    }

    public Long getUserId() {
        return userId;
    }

}