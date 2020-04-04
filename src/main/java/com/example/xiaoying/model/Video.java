package com.example.xiaoying.model;

public class Video {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column video.id
     *
     * @mbg.generated Fri Apr 03 08:23:35 CST 2020
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column video.account_id
     *
     * @mbg.generated Fri Apr 03 08:23:35 CST 2020
     */
    private String accountId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column video.video_id
     *
     * @mbg.generated Fri Apr 03 08:23:35 CST 2020
     */
    private String videoId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column video.video_title
     *
     * @mbg.generated Fri Apr 03 08:23:35 CST 2020
     */
    private String videoTitle;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column video.video_tag
     *
     * @mbg.generated Fri Apr 03 08:23:35 CST 2020
     */
    private String videoTag;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column video.video_bio
     *
     * @mbg.generated Fri Apr 03 08:23:35 CST 2020
     */
    private String videoBio;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column video.video_cover
     *
     * @mbg.generated Fri Apr 03 08:23:35 CST 2020
     */
    private String videoCover;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column video.video_url
     *
     * @mbg.generated Fri Apr 03 08:23:35 CST 2020
     */
    private String videoUrl;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column video.status
     *
     * @mbg.generated Fri Apr 03 08:23:35 CST 2020
     */
    private String status;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column video.gmt_create
     *
     * @mbg.generated Fri Apr 03 08:23:35 CST 2020
     */
    private Long gmtCreate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column video.gmt_modified
     *
     * @mbg.generated Fri Apr 03 08:23:35 CST 2020
     */
    private Long gmtModified;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column video.view_count
     *
     * @mbg.generated Fri Apr 03 08:23:35 CST 2020
     */
    private Long viewCount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column video.like_count
     *
     * @mbg.generated Fri Apr 03 08:23:35 CST 2020
     */
    private Long likeCount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column video.comment_count
     *
     * @mbg.generated Fri Apr 03 08:23:35 CST 2020
     */
    private Long commentCount;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column video.id
     *
     * @return the value of video.id
     *
     * @mbg.generated Fri Apr 03 08:23:35 CST 2020
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column video.id
     *
     * @param id the value for video.id
     *
     * @mbg.generated Fri Apr 03 08:23:35 CST 2020
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column video.account_id
     *
     * @return the value of video.account_id
     *
     * @mbg.generated Fri Apr 03 08:23:35 CST 2020
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column video.account_id
     *
     * @param accountId the value for video.account_id
     *
     * @mbg.generated Fri Apr 03 08:23:35 CST 2020
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId == null ? null : accountId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column video.video_id
     *
     * @return the value of video.video_id
     *
     * @mbg.generated Fri Apr 03 08:23:35 CST 2020
     */
    public String getVideoId() {
        return videoId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column video.video_id
     *
     * @param videoId the value for video.video_id
     *
     * @mbg.generated Fri Apr 03 08:23:35 CST 2020
     */
    public void setVideoId(String videoId) {
        this.videoId = videoId == null ? null : videoId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column video.video_title
     *
     * @return the value of video.video_title
     *
     * @mbg.generated Fri Apr 03 08:23:35 CST 2020
     */
    public String getVideoTitle() {
        return videoTitle;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column video.video_title
     *
     * @param videoTitle the value for video.video_title
     *
     * @mbg.generated Fri Apr 03 08:23:35 CST 2020
     */
    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle == null ? null : videoTitle.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column video.video_tag
     *
     * @return the value of video.video_tag
     *
     * @mbg.generated Fri Apr 03 08:23:35 CST 2020
     */
    public String getVideoTag() {
        return videoTag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column video.video_tag
     *
     * @param videoTag the value for video.video_tag
     *
     * @mbg.generated Fri Apr 03 08:23:35 CST 2020
     */
    public void setVideoTag(String videoTag) {
        this.videoTag = videoTag == null ? null : videoTag.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column video.video_bio
     *
     * @return the value of video.video_bio
     *
     * @mbg.generated Fri Apr 03 08:23:35 CST 2020
     */
    public String getVideoBio() {
        return videoBio;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column video.video_bio
     *
     * @param videoBio the value for video.video_bio
     *
     * @mbg.generated Fri Apr 03 08:23:35 CST 2020
     */
    public void setVideoBio(String videoBio) {
        this.videoBio = videoBio == null ? null : videoBio.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column video.video_cover
     *
     * @return the value of video.video_cover
     *
     * @mbg.generated Fri Apr 03 08:23:35 CST 2020
     */
    public String getVideoCover() {
        return videoCover;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column video.video_cover
     *
     * @param videoCover the value for video.video_cover
     *
     * @mbg.generated Fri Apr 03 08:23:35 CST 2020
     */
    public void setVideoCover(String videoCover) {
        this.videoCover = videoCover == null ? null : videoCover.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column video.video_url
     *
     * @return the value of video.video_url
     *
     * @mbg.generated Fri Apr 03 08:23:35 CST 2020
     */
    public String getVideoUrl() {
        return videoUrl;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column video.video_url
     *
     * @param videoUrl the value for video.video_url
     *
     * @mbg.generated Fri Apr 03 08:23:35 CST 2020
     */
    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl == null ? null : videoUrl.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column video.status
     *
     * @return the value of video.status
     *
     * @mbg.generated Fri Apr 03 08:23:35 CST 2020
     */
    public String getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column video.status
     *
     * @param status the value for video.status
     *
     * @mbg.generated Fri Apr 03 08:23:35 CST 2020
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column video.gmt_create
     *
     * @return the value of video.gmt_create
     *
     * @mbg.generated Fri Apr 03 08:23:35 CST 2020
     */
    public Long getGmtCreate() {
        return gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column video.gmt_create
     *
     * @param gmtCreate the value for video.gmt_create
     *
     * @mbg.generated Fri Apr 03 08:23:35 CST 2020
     */
    public void setGmtCreate(Long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column video.gmt_modified
     *
     * @return the value of video.gmt_modified
     *
     * @mbg.generated Fri Apr 03 08:23:35 CST 2020
     */
    public Long getGmtModified() {
        return gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column video.gmt_modified
     *
     * @param gmtModified the value for video.gmt_modified
     *
     * @mbg.generated Fri Apr 03 08:23:35 CST 2020
     */
    public void setGmtModified(Long gmtModified) {
        this.gmtModified = gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column video.view_count
     *
     * @return the value of video.view_count
     *
     * @mbg.generated Fri Apr 03 08:23:35 CST 2020
     */
    public Long getViewCount() {
        return viewCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column video.view_count
     *
     * @param viewCount the value for video.view_count
     *
     * @mbg.generated Fri Apr 03 08:23:35 CST 2020
     */
    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column video.like_count
     *
     * @return the value of video.like_count
     *
     * @mbg.generated Fri Apr 03 08:23:35 CST 2020
     */
    public Long getLikeCount() {
        return likeCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column video.like_count
     *
     * @param likeCount the value for video.like_count
     *
     * @mbg.generated Fri Apr 03 08:23:35 CST 2020
     */
    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column video.comment_count
     *
     * @return the value of video.comment_count
     *
     * @mbg.generated Fri Apr 03 08:23:35 CST 2020
     */
    public Long getCommentCount() {
        return commentCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column video.comment_count
     *
     * @param commentCount the value for video.comment_count
     *
     * @mbg.generated Fri Apr 03 08:23:35 CST 2020
     */
    public void setCommentCount(Long commentCount) {
        this.commentCount = commentCount;
    }
}