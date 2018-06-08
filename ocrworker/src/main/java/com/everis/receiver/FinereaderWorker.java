package com.everis.receiver;

import com.everis.ApplicationContextProvider;
import com.everis.autorization.model.Users;
import com.everis.billingworker.model.License;
import com.everis.businesslogicworker.interfaces.IProductControl;
import com.everis.businesslogicworker.logic.ProductControl;
import com.everis.facturationcontrolworker.interfaces.IFacturationControl;
import com.everis.finereadercontrolworker.interfaces.IFinereaderControl;
import com.everis.finereadercontrolworker.logic.FinereaderControl;
import com.everis.tokenuser.TokenUser;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import sun.reflect.Reflection;

import java.sql.Timestamp;

@Component
public class FinereaderWorker {

    private String idProduct = "ABBYYFR";

    private TokenUser tokenUser = new TokenUser();

    @Autowired
    private IFinereaderControl iFineReaderControl;

    @Autowired
    private IFacturationControl iFacturationControl;

    @Autowired
    private IProductControl iProductControl;

    public FinereaderWorker() {}

    public String ocrFineReaderExec(String imagePath, String exportPath, Integer precision, String token, String ticket)  {
        iFineReaderControl.inProcessTicket(ticket);
        Users users = tokenUser.getUser(token);

        License license = iFacturationControl.getLicense(idProduct);
        Integer maxVolume = license.getVolume();
        Integer actualVolume = license.getActualVolume();

        String classe = iProductControl.getClassName(idProduct);
        //IFinereaderControl = new  com.everis.finereadercontrolworker.logic.FinereaderControl();
        //ApplicationContextProvider.getBean("FinereaderControl", classe.getClass());

        String result = null;
        try {
            result = iFineReaderControl.Run(imagePath, exportPath, maxVolume,actualVolume, precision);
        } catch (Exception e) {
            return e.toString();
        }

        Timestamp stamp = new Timestamp(System.currentTimeMillis());

        if (!(result.startsWith("Error"))) {
            Integer numPags = Integer.valueOf(result);
            iFacturationControl.addPages(license, numPags);
            iFacturationControl.newFacturation(stamp, users, license, numPags);
            iFineReaderControl.ticketFinished(ticket);
            return "Document recognised correctly!!";
        }
        else iFineReaderControl.ticketWithError(ticket, result);
        System.out.println(result);
        return result;

    }
}