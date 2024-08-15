package org.debilla.github_repo_details.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class GithubRepoDTO implements Serializable {
    @JsonProperty("fullName")
    private String fullName;
    @JsonProperty("description")
    private String description;
    @JsonProperty("cloneUrl")
    private String cloneUrl;
    @JsonProperty("stars")
    private int stars;
    @JsonProperty("createdAt")
    private String createdAt;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCloneUrl() {
        return cloneUrl;
    }

    public void setCloneUrl(String cloneUrl) {
        this.cloneUrl = cloneUrl;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
