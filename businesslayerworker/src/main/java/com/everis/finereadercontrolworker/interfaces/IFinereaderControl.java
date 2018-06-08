package com.everis.finereadercontrolworker.interfaces;

public interface IFinereaderControl {
    String Run (String imagePath, String exportPath, Integer maxVolume, Integer actualVolume, Integer precision) throws Exception;
    void inProcessTicket(String id);
    void ticketFinished(String id);
    void ticketWithError(String id, String error);
}
