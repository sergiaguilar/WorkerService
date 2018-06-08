package com.everis.tesseractcontrolworker.interfaces;

public interface ITesseractControl {
    String Run (String imagePath, String exportPath, Integer maxVolume, Integer actualVolume, Integer precision) throws Exception;
    void inProcessTicket(String id);
    void ticketFinished(String id);
    void ticketWithError(String id, String error);
}
