/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.PAT.common;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author mihailupu
 */


public class ticker_message extends org.PAT.common.MySQL
{
    /* Constructors */
    public ticker_message() {
        super();
    }


    /* Methods */
    public java.lang.String getMessage() {
        String[] fields ={"message"};

        ArrayList<HashMap<String,String>>result= this.query("SELECT * from ticker_message;", fields);

        if (!result.isEmpty()){
            return result.get(0).get("message");
        }else{
            return "";
        }
    }

}
