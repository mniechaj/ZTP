package pl.jrj.mdb;

import javax.ejb.Remote;

/**
 *
 * @author Maciek Niechaj
 */
@Remote
public interface IMdbManager {
    public String currencyId();     // trzyliterowy kod waluty
}
