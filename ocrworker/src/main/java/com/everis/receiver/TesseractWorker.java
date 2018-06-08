package com.everis.receiver;

import com.everis.autorization.model.Users;
import com.everis.billingworker.model.License;
import com.everis.businesslogicworker.interfaces.IProductControl;
import com.everis.facturationcontrolworker.interfaces.IFacturationControl;
import com.everis.finereadercontrolworker.interfaces.IFinereaderControl;
import com.everis.tesseractcontrolworker.interfaces.ITesseractControl;
import com.everis.tokenuser.TokenUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class TesseractWorker {

    private String idProduct = "tesseract";

    private TokenUser tokenUser = new TokenUser();

    @Autowired
    private ITesseractControl iTesseractControl;

    @Autowired
    private IFacturationControl iFacturationControl;

    @Autowired
    private IProductControl iProductControl;

    public TesseractWorker() {}

    public String ocrTesseractExec(String imagePath, String exportPath, Integer precision, String token, String ticket)  {
        iTesseractControl.inProcessTicket(ticket);
        Users users = tokenUser.getUser(token);

        License license = iFacturationControl.getLicense(idProduct);
        Integer maxVolume = license.getVolume();
        Integer actualVolume = license.getActualVolume();

        String result = null;
        try {
            result = iTesseractControl.Run(imagePath, exportPath, maxVolume,actualVolume, precision);
        } catch (Exception e) {
            return e.toString();
        }

        Timestamp stamp = new Timestamp(System.currentTimeMillis());

        if (!(result.startsWith("Error"))) {
            Integer numPags = Integer.valueOf(result);
            iFacturationControl.addPages(license, numPags);
            iFacturationControl.newFacturation(stamp, users, license, numPags);
            iTesseractControl.ticketFinished(ticket);
            return "Document recognised correctly!!";
        }
        else iTesseractControl.ticketWithError(ticket, result);
        System.out.println(result);
        return result;


    }
}