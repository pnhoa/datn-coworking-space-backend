package com.datn.coworkingspace.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@JsonIgnoreProperties({"space"})
public class Comment extends BaseEntity {

    @NotBlank
    private String content;

    private long rate;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "space_id")
    private Space space;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Transient
    @JsonProperty(value = "spaceId")
    private Long spaceIds;

    @Transient
    @JsonProperty(value = "userId")
    private Long userIds;

    public Comment() {
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

    public Space getSpace() { return space; }

    public void setSpace(Space space) { this.space = space; }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getSpaceIds() {
        return spaceIds;
    }

    public void setSpaceIds(Long spaceIds) {
        this.spaceIds = spaceIds;
    }

    public Long getUserIds() {
        return userIds;
    }

    public void setUserIds(Long userIds) {
        this.userIds = userIds;
    }
}
