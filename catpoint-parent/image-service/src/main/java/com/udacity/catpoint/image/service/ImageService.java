package com.udacity.catpoint.image.service;

import software.amazon.awssdk.services.rekognition.model.DetectLabelsResponse;

import java.awt.image.BufferedImage;

public interface ImageService {

    boolean imageContainsCat(BufferedImage image, float confidenceThreshhold);
//    void logLabelsForFun(DetectLabelsResponse response);
}
