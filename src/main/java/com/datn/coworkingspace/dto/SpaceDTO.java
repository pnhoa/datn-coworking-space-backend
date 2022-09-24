package com.datn.coworkingspace.dto;

import com.datn.coworkingspace.entity.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties({"category"})
public class SpaceDTO extends AbstractDTO {

    @NotNull(message = "Please enter name")
    private String name;

    @NotNull(message = "Please enter price")
    private BigDecimal price;

    @NotNull(message = "Please enter unit(day/month/...)")
    private String unit;

    @NotNull(message = "Please upload large image")
    private String largeImage;

    private BigDecimal minPrice;

    private BigDecimal maxPrice;

    private Integer numberOfRoom;

    private BigDecimal acreage;

    private BigDecimal electricPrice;

    private BigDecimal waterPrice;

    private boolean status;

    private boolean approved;

    private boolean notApproved;

    private double xCoordinate;

    private double yCoordinate;

    @Min(0)
    @Max(99)
    private Integer discount;

    private BigDecimal ratingAverage;

    private SpaceDescriptionDTO spaceDescriptionDTO;

    private SpaceContactDTO spaceContactDTO;

    private SpaceAmenityDTO spaceAmenityDTO;

    private SpaceAddressDTO spaceAddressDTO;

    private List<SpaceOperationTimeDTO> SpaceOperationTimeDTOs;

    private List<String> imageUrls;

    private CustomerDTO userDTO;

    private Category category;

    private List<CommentDTO> commentDTOs;

    private List<ServiceSpaceDTO> serviceSpaceDTOs;

    @NotNull(message = "Please input user id")
    private Long userId;


    @NotNull(message = "Please select category")
    private Long categoryId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getDiscount() { return discount; }

    public void setDiscount(Integer discount) { this.discount = discount; }

    public BigDecimal getRatingAverage() { return ratingAverage; }

    public void setRatingAverage(BigDecimal ratingAverage) { this.ratingAverage = ratingAverage; }

    public String getLargeImage() {
        return largeImage;
    }

    public void setLargeImage(String largeImage) {
        this.largeImage = largeImage;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Integer getNumberOfRoom() {
        return numberOfRoom;
    }

    public void setNumberOfRoom(Integer numberOfRoom) {
        this.numberOfRoom = numberOfRoom;
    }

    public BigDecimal getAcreage() {
        return acreage;
    }

    public void setAcreage(BigDecimal acreage) {
        this.acreage = acreage;
    }

    public BigDecimal getElectricPrice() {
        return electricPrice;
    }

    public void setElectricPrice(BigDecimal electricPrice) {
        this.electricPrice = electricPrice;
    }

    public BigDecimal getWaterPrice() {
        return waterPrice;
    }

    public void setWaterPrice(BigDecimal waterPrice) {
        this.waterPrice = waterPrice;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public double getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(double xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public double getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(double yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public SpaceDescriptionDTO getSpaceDescriptionDTO() {
        return spaceDescriptionDTO;
    }

    public void setSpaceDescriptionDTO(SpaceDescriptionDTO spaceDescriptionDTO) {
        this.spaceDescriptionDTO = spaceDescriptionDTO;
    }

    public SpaceContactDTO getSpaceContactDTO() {
        return spaceContactDTO;
    }

    public void setSpaceContactDTO(SpaceContactDTO spaceContactDTO) {
        this.spaceContactDTO = spaceContactDTO;
    }

    public SpaceAmenityDTO getSpaceAmenityDTO() {
        return spaceAmenityDTO;
    }

    public void setSpaceAmenityDTO(SpaceAmenityDTO spaceAmenityDTO) {
        this.spaceAmenityDTO = spaceAmenityDTO;
    }

    public SpaceAddressDTO getSpaceAddressDTO() {
        return spaceAddressDTO;
    }

    public void setSpaceAddressDTO(SpaceAddressDTO spaceAddressDTO) {
        this.spaceAddressDTO = spaceAddressDTO;
    }

    public List<SpaceOperationTimeDTO> getSpaceOperationTimeDTOs() { return SpaceOperationTimeDTOs; }

    public void setSpaceOperationTimeDTOs(List<SpaceOperationTimeDTO> spaceOperationTimeDTOs) { SpaceOperationTimeDTOs = spaceOperationTimeDTOs; }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public CustomerDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(CustomerDTO userDTO) {
        this.userDTO = userDTO;
    }

    public List<CommentDTO> getCommentDTOs() {
        return commentDTOs;
    }

    public void setCommentDTOs(List<CommentDTO> commentDTOs) {
        this.commentDTOs = commentDTOs;
    }

    public List<ServiceSpaceDTO> getServiceSpaceDTOs() { return serviceSpaceDTOs; }

    public void setServiceSpaceDTOs(List<ServiceSpaceDTO> serviceSpaceDTOs) { this.serviceSpaceDTOs = serviceSpaceDTOs; }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isNotApproved() { return notApproved; }

    public void setNotApproved(boolean notApproved) { this.notApproved = notApproved; }

    public String getUnit() { return unit; }

    public void setUnit(String unit) { this.unit = unit; }

    public SpaceDTO() {
    }
}
