package com.spamdetector.service;

import com.spamdetector.domain.TestFile;
import com.spamdetector.util.SpamDetector;
import com.sun.source.doctree.TextTree;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import jakarta.ws.rs.core.Response;

@Path("/spam")
public class SpamResource {

//    your SpamDetector Class responsible for all the SpamDetecting logic
    SpamDetector detector = new SpamDetector();
    // list of the tested files
    List<TestFile> testFiles;


    SpamResource() throws IOException {
//        TODO: load resources, train and test to improve performance on the endpoint calls
        System.out.print("Training and testing the model, please wait");

//      TODO: call  this.trainAndTest();
        this.testFiles = this.trainAndTest();


    }
    @GET
    @Produces("application/json")
    public Response getSpamResults() throws IOException {
//       TODO: return the test results list of TestFile, return in a Response object
        return Response.status(200)
                .header("Access-Control-Allow-Origin", "*")
                .header("Content-Type", "application/json")
                .entity(Arrays.toString(testFiles.toArray()))
                .build();
    }

    @GET
    @Path("/accuracy")
    @Produces("application/json")
    public Response getAccuracy() {
//      TODO: return the accuracy of the detector, return in a Response object
        double accuracy;
        int count = 0;
        //calculate correct guesses
        for(TestFile file : testFiles) {
            if(file.getSpamProbability() >= 0.5 && file.getActualClass() == "spam"){
                count+= 1;
            }
            else if (file.getSpamProbability() < 0.5 && file.getActualClass() == "ham"){
                count+= 1;
            }
        }
        accuracy = count/(testFiles.size());

        String ret = "{\"val\":"+ accuracy + "}";
        return Response.status(200)
                .header("Access-Control-Allow-Origin", "*")
                .header("Content-Type", "application/json")
                .entity(ret)
                .build();
    }

    @GET
    @Path("/precision")
    @Produces("application/json")
    public Response getPrecision() {
       //      TODO: return the precision of the detector, return in a Response object
        double precision;
        int count = 0;
        int truePositive = 0;
        //calculate num true positive and false positive
        for(TestFile file : testFiles) {
            if(file.getActualClass() == "spam"){
                if (file.getSpamProbability() >= 50){
                    truePositive +=1;
                    count +=1;
                }
                else{
                    count +=1;
                }
            }

        }
        precision = truePositive/count;
        String ret = "{\"val\":"+ precision + "}";
        return Response.status(200)
                .header("Access-Control-Allow-Origin", "*")
                .header("Content-Type", "application/json")
                .entity(ret)
                .build();
    }

    private List<TestFile> trainAndTest() throws IOException {
        if (this.detector==null){
            this.detector = new SpamDetector();
        }

//        TODO: load the main directory "data" here from the Resources folder
        URL url = this.getClass().getClassLoader().getResource("/data");
        File mainDirectory = null;
        try {
            mainDirectory = new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return this.detector.trainAndTest(mainDirectory);
    }
}